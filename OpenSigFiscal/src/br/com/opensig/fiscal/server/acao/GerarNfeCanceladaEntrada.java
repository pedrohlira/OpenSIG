package br.com.opensig.fiscal.server.acao;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.xml.bind.JAXBElement;

import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.FiltroNumero;
import br.com.opensig.core.client.padroes.Chain;
import br.com.opensig.core.client.servico.CoreService;
import br.com.opensig.core.client.servico.OpenSigException;
import br.com.opensig.core.server.UtilServer;
import br.com.opensig.core.shared.modelo.Autenticacao;
import br.com.opensig.eventocancnfe.TEvento;
import br.com.opensig.eventocancnfe.TEvento.InfEvento;
import br.com.opensig.eventocancnfe.TEvento.InfEvento.DetEvento;
import br.com.opensig.fiscal.shared.modelo.ENotaStatus;
import br.com.opensig.fiscal.shared.modelo.FisNotaEntrada;
import br.com.opensig.fiscal.shared.modelo.FisNotaStatus;

public class GerarNfeCanceladaEntrada extends Chain {

	private CoreService servico;
	private FisNotaEntrada entrada;
	private String obs;
	private Autenticacao auth;

	public GerarNfeCanceladaEntrada(Chain next, CoreService servico, FisNotaEntrada entrada, String obs, Autenticacao auth) throws OpenSigException {
		super(next);
		this.servico = servico;
		this.entrada = entrada;
		this.obs = obs;
		this.auth = auth;
	}

	@Override
	public void execute() throws OpenSigException {
		// seleciona a nota
		FiltroNumero fn = new FiltroNumero("fisNotaEntradaId", ECompara.IGUAL, entrada.getId());
		entrada = (FisNotaEntrada) servico.selecionar(new FisNotaEntrada(), fn, false);
		// valida se a data da nota ainda pode ser cancelada
		int dias = Integer.valueOf(auth.getConf().get("nfe.tempo_cancela"));
		Calendar cal = Calendar.getInstance();
		cal.setTime(entrada.getFisNotaEntradaData());
		cal.add(Calendar.DATE, dias);

		Date hoje = new Date();
		if (hoje.compareTo(cal.getTime()) > 0) {
			throw new OpenSigException("Data limite para cancelamento desta NFe era " + UtilServer.formataData(cal.getTime(), "dd/MM/yyyy"));
		}
		// cria o xml
		String xml = getXml(entrada, obs);
		// salva o registro
		SalvarEntrada salvar = new SalvarEntrada(next, xml, new FisNotaStatus(ENotaStatus.CANCELANDO), auth);
		salvar.execute();
		entrada = salvar.getNota();
	}

	public FisNotaEntrada getNota() {
		return entrada;
	}

	private String getXml(FisNotaEntrada entrada, String obs) throws OpenSigException {
		try {
			// desmembra a chave
			Date agora = new Date();
			String chave = entrada.getFisNotaEntradaChave();
			String uf = chave.substring(0, 2);
			String cnpj = chave.substring(6, 20);
			String data = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(agora) + "-03:00";
			String tipo = "110111";
			String versao = auth.getConf().get("nfe.evento");
			String seq = "1";

			// informacoes
			InfEvento infEvento = new InfEvento();
			infEvento.setId("ID" + tipo + chave + "01");
			infEvento.setCOrgao(uf);
			infEvento.setTpAmb(auth.getConf().get("nfe.tipoamb"));
			infEvento.setCNPJ(cnpj);
			infEvento.setChNFe(chave);
			infEvento.setDhEvento(data);
			infEvento.setTpEvento(tipo);
			infEvento.setNSeqEvento(seq);
			infEvento.setVerEvento(versao);

			// descricao
			DetEvento detEvento = new DetEvento();
			detEvento.setVersao(versao);
			detEvento.setDescEvento("Cancelamento");
			detEvento.setNProt(entrada.getFisNotaEntradaProtocolo());
			detEvento.setXJust(UtilServer.normaliza(obs));
			infEvento.setDetEvento(detEvento);

			// evento
			TEvento evento = new TEvento();
			evento.setInfEvento(infEvento);
			evento.setVersao(versao);

			// transforma em string o xml e salva
			JAXBElement<TEvento> element = new br.com.opensig.eventocancnfe.ObjectFactory().createEvento(evento);
			return UtilServer.objToXml(element, "br.com.opensig.eventocancnfe");
		} catch (Exception e) {
			throw new OpenSigException(e.getMessage());
		}
	}
}
