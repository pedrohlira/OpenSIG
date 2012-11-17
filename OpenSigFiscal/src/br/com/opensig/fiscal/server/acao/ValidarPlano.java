package br.com.opensig.fiscal.server.acao;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.EJuncao;
import br.com.opensig.core.client.controlador.filtro.FiltroData;
import br.com.opensig.core.client.controlador.filtro.FiltroObjeto;
import br.com.opensig.core.client.controlador.filtro.GrupoFiltro;
import br.com.opensig.core.client.controlador.parametro.ParametroException;
import br.com.opensig.core.client.padroes.Chain;
import br.com.opensig.core.client.servico.CoreException;
import br.com.opensig.core.client.servico.CoreService;
import br.com.opensig.core.client.servico.OpenSigException;
import br.com.opensig.core.server.MailServiceImpl;
import br.com.opensig.core.server.UtilServer;
import br.com.opensig.core.shared.modelo.Autenticacao;
import br.com.opensig.core.shared.modelo.EBusca;
import br.com.opensig.empresa.client.servico.EmpresaService;
import br.com.opensig.empresa.server.EmpresaServiceImpl;
import br.com.opensig.empresa.shared.modelo.EmpContato;
import br.com.opensig.empresa.shared.modelo.EmpEmpresa;
import br.com.opensig.empresa.shared.modelo.EmpPlano;
import br.com.opensig.fiscal.client.servico.FiscalException;
import br.com.opensig.fiscal.shared.modelo.ENotaStatus;
import br.com.opensig.fiscal.shared.modelo.FisNotaEntrada;
import br.com.opensig.fiscal.shared.modelo.FisNotaSaida;
import br.com.opensig.fiscal.shared.modelo.FisNotaStatus;

public class ValidarPlano extends Chain {

	private Autenticacao auth;
	private CoreService servico;
	private FisNotaStatus status;

	public ValidarPlano(Chain next, CoreService servico, FisNotaStatus status, Autenticacao auth) throws OpenSigException {
		super(next);
		this.servico = servico;
		this.auth = auth;
		this.status = status;
	}

	@Override
	public void execute() throws OpenSigException {
		// recupera o plano da empresa
		EmpEmpresa empresa = new EmpEmpresa(Integer.valueOf(auth.getEmpresa()[0]));
		FiltroObjeto fo = new FiltroObjeto("empEmpresa", ECompara.IGUAL, empresa);
		EmpresaService<EmpPlano> service = new EmpresaServiceImpl<EmpPlano>(auth);
		EmpPlano plano = new EmpPlano();
		plano = service.selecionar(plano, fo, false);

		// valida a data
		Date hoje = new Date();
		if (plano.getEmpPlanoFim() != null && hoje.after(plano.getEmpPlanoFim())) {
			throw new FiscalException("Renove seu plano, pois ele venceu em " + UtilServer.formataData(plano.getEmpPlanoFim(), DateFormat.MEDIUM));
		}

		// valida o limite e excedente
		if (plano.getEmpPlanoLimite() > 0 && !plano.getEmpPlanoExcedente()) {
			// formando o filtro
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());

			// data inicio
			int primeiro = cal.getMinimum(Calendar.DATE);
			cal.set(Calendar.DATE, primeiro);
			Date inicio = cal.getTime();

			// data fim
			int ultimo = cal.getMaximum(Calendar.DATE);
			cal.set(Calendar.DATE, ultimo);
			Date fim = cal.getTime();

			// totais
			if (plano.getEmpPlanoLimite() < Integer.valueOf(auth.getConf().get("nfe.plano")) || status.getFisNotaStatusId() == ENotaStatus.AUTORIZADO.getId()) {
				int total = getTotalSaida(plano.getEmpPlanoLimite(), inicio, fim);
				total += getTotalEntrada(plano.getEmpPlanoLimite(), inicio, fim);
				int usado = total / plano.getEmpPlanoLimite() * 100;
				int aviso = auth.getConf().get("nfe.aviso") == null ? 90 : Integer.valueOf(auth.getConf().get("nfe.aviso"));
				
				if (usado >= 100) {
					throw new FiscalException("Aumente o limite do seu plano, pois ja usou a quantidade maxima deste mes.");
				} else if (usado == aviso) {
					try {
						// enviando, caso nao ache manda para o sistema
						String para = null;
						for (EmpContato cont : plano.getEmpEmpresa().getEmpEntidade().getEmpContatos()) {
							if (cont.getEmpContatoTipo().getEmpContatoTipoId() == Integer.valueOf(auth.getConf().get("nfe.tipocontemail"))) {
								para = cont.getEmpContatoDescricao();
								break;
							}
						}
						String msg = UtilServer.getTextoArquivo(UtilServer.getRealPath("/WEB-INF/modelos/limite.html"));
						msg.replace("#uso#", usado + "");
						msg.replace("#limite#", plano.getEmpPlanoLimite() + "");
						MailServiceImpl.enviar(null, para, null, null, "Limite das NFe", msg, null);
					} catch (Exception e) {
						UtilServer.LOG.error("Nao enviou o email avisando o cliente dos limites.", e);
					}
				}
			}
		}

		if (next != null) {
			next.execute();
		}
	}

	private int getTotalSaida(int limite, Date inicio, Date fim) throws ParametroException, CoreException {
		GrupoFiltro gf = new GrupoFiltro();
		if (limite >= Integer.valueOf(auth.getConf().get("nfe.plano"))) {
			FiltroObjeto fo1 = new FiltroObjeto("fisNotaStatus", ECompara.IGUAL, new FisNotaStatus(ENotaStatus.AUTORIZADO));
			gf.add(fo1, EJuncao.E);
		}

		FiltroData fd1 = new FiltroData("fisNotaSaidaCadastro", ECompara.MAIOR_IGUAL, inicio);
		FiltroData fd2 = new FiltroData("fisNotaSaidaCadastro", ECompara.MENOR_IGUAL, fim);
		gf.add(fd1, EJuncao.E);
		gf.add(fd2);

		return servico.buscar(new FisNotaSaida(), "fisNotaSaidaId", EBusca.CONTAGEM, gf).intValue();
	}

	private int getTotalEntrada(int limite, Date inicio, Date fim) throws ParametroException, CoreException {
		GrupoFiltro gf = new GrupoFiltro();
		if (limite >= Integer.valueOf(auth.getConf().get("nfe.plano"))) {
			FiltroObjeto fo1 = new FiltroObjeto("fisNotaStatus", ECompara.IGUAL, new FisNotaStatus(ENotaStatus.AUTORIZADO));
			gf.add(fo1, EJuncao.E);
		}

		FiltroData fd1 = new FiltroData("fisNotaEntradaCadastro", ECompara.MAIOR_IGUAL, inicio);
		FiltroData fd2 = new FiltroData("fisNotaEntradaCadastro", ECompara.MENOR_IGUAL, fim);
		gf.add(fd1, EJuncao.E);
		gf.add(fd2);

		return servico.buscar(new FisNotaEntrada(), "fisNotaEntradaId", EBusca.CONTAGEM, gf).intValue();
	}
}
