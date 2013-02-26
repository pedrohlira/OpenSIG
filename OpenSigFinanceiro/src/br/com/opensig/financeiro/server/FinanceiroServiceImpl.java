package br.com.opensig.financeiro.server;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.FiltroNumero;
import br.com.opensig.core.server.CoreServiceImpl;
import br.com.opensig.core.server.SessionManager;
import br.com.opensig.core.server.UtilServer;
import br.com.opensig.core.shared.modelo.Autenticacao;
import br.com.opensig.financeiro.client.servico.FinanceiroException;
import br.com.opensig.financeiro.client.servico.FinanceiroService;
import br.com.opensig.financeiro.server.acao.SalvarPagar;
import br.com.opensig.financeiro.server.acao.SalvarReceber;
import br.com.opensig.financeiro.server.boleto.FabricaRecibo;
import br.com.opensig.financeiro.server.boleto.IRecibo;
import br.com.opensig.financeiro.server.cobranca.Boleto;
import br.com.opensig.financeiro.shared.modelo.FinCategoria;
import br.com.opensig.financeiro.shared.modelo.FinPagar;
import br.com.opensig.financeiro.shared.modelo.FinReceber;
import br.com.opensig.financeiro.shared.modelo.FinRecebimento;

public class FinanceiroServiceImpl extends CoreServiceImpl implements FinanceiroService {

	public FinanceiroServiceImpl(){
	}
	
	public FinanceiroServiceImpl(Autenticacao auth){
		super(auth);
	}
	
	public String gerar(int boletoId, String tipo, boolean recibo) throws FinanceiroException {
		String retorno = "";
		HttpSession sessao = getThreadLocalRequest().getSession();
		Autenticacao auth = SessionManager.LOGIN.get(sessao);

		try {
			FiltroNumero fn = new FiltroNumero("finRecebimentoId", ECompara.IGUAL, boletoId);
			FinRecebimento finBoleto = (FinRecebimento) selecionar(new FinRecebimento(), fn, false);

			byte[] obj = null;
			String nome = "";

			if (recibo) {
				nome = "recibo";
				IRecibo rec = FabricaRecibo.getInstancia().getRecibo(tipo);
				rec.setAuth(auth);
				obj = rec.getRecibo(finBoleto);
			} else {
				nome = "boleto";
				Boleto bol  = new Boleto(finBoleto.getFinConta(), auth);
				obj = bol.gerar(tipo, finBoleto);
			}

			retorno = sessao.getId() + new Date().getTime();
			sessao.setAttribute(retorno, obj);
			sessao.setAttribute(retorno + "arquivo", nome + "." + tipo);
		} catch (Exception e) {
			UtilServer.LOG.error("Erro ao gerar boleto ou recibo", e);
			throw new FinanceiroException(e.getMessage());
		}

		return retorno;
	}

	public FinReceber salvarReceber(FinReceber receber, List<FinCategoria> categorias) throws FinanceiroException {
		try {
			new SalvarReceber(null, this, receber, categorias).execute();
			receber.anularDependencia();
			return receber;
		} catch (Exception e) {
			UtilServer.LOG.error("Erro ao salvar receber", e);
			throw new FinanceiroException(e.getMessage());
		}
	}

	public FinPagar salvarPagar(FinPagar pagar, List<FinCategoria> categorias) throws FinanceiroException {
		try {
			new SalvarPagar(null, this, pagar, categorias).execute();
			pagar.anularDependencia();
			return pagar;
		} catch (Exception e) {
			UtilServer.LOG.error("Erro ao salvar pagar", e);
			throw new FinanceiroException(e.getMessage());
		}
	}

}
