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
import br.com.opensig.cce.TEvento;
import br.com.opensig.cce.TEvento.InfEvento;
import br.com.opensig.cce.TEvento.InfEvento.DetEvento;
import br.com.opensig.fiscal.server.FiscalServiceImpl;
import br.com.opensig.fiscal.shared.modelo.FisNotaSaida;

public class GerarCartaSaida extends Chain {

	private CoreService servico;
	private FisNotaSaida saida;
	private String obs;
	private Autenticacao auth;

	public GerarCartaSaida(Chain next, CoreService servico, FisNotaSaida saida, String obs, Autenticacao auth) throws OpenSigException {
		super(next);
		this.servico = servico;
		this.saida = saida;
		this.obs = obs;
		this.auth = auth;
	}

	@Override
	public void execute() throws OpenSigException {
		// seleciona a nota
		FiltroNumero fn = new FiltroNumero("fisNotaSaidaId", ECompara.IGUAL, saida.getId());
		saida = (FisNotaSaida) servico.selecionar(new FisNotaSaida(), fn, false);
		// valida se a data da nota ainda pode ser cancelada
		int dias = Integer.valueOf(auth.getConf().get("nfe.tempo_cce"));
		Calendar cal = Calendar.getInstance();
		cal.setTime(saida.getFisNotaSaidaData());
		cal.add(Calendar.DATE, dias);

		Date hoje = new Date();
		if (hoje.compareTo(cal.getTime()) > 0) {
			throw new OpenSigException("Data limite para carta de correcao desta NFe era " + UtilServer.formataData(cal.getTime(), "dd/MM/yyyy"));
		}
		// cria o xml
		String xml = getXml(saida, obs);
		saida.setFisNotaSaidaXmlCarta(xml);
		// envia o evento
		EnviarCartaSaida cartaSaida = new EnviarCartaSaida(next, new FiscalServiceImpl<FisNotaSaida>(auth), saida, auth);
		cartaSaida.execute();
	}

	public FisNotaSaida getNota() {
		return saida;
	}

	private String getXml(FisNotaSaida saida, String obs) throws OpenSigException {
		try {
			// desmembra a chave
			Date agora = new Date();
			String chave = saida.getFisNotaSaidaChave();
			String uf = chave.substring(0, 2);
			String cnpj = chave.substring(6, 20);
			String data = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz").format(agora);
			String tipo = "110110";
			String versao = auth.getConf().get("nfe.evento");
			String seq = (saida.getFisNotaSaidaEvento() + 1) + "";

			// informacoes
			InfEvento infEvento = new InfEvento();
			infEvento.setId("ID" + tipo + chave + UtilServer.formataNumero(seq, 2, 0, false));
			infEvento.setCOrgao(uf);
			infEvento.setTpAmb(auth.getConf().get("nfe.tipoamb"));
			infEvento.setCNPJ(cnpj);
			infEvento.setChNFe(chave);
			infEvento.setDhEvento(data.replace("GMT", ""));
			infEvento.setTpEvento(tipo);
			infEvento.setNSeqEvento(seq);
			infEvento.setVerEvento(versao);

			// descricao
			DetEvento detEvento = new DetEvento();
			detEvento.setVersao(versao);
			detEvento.setDescEvento("Carta de Correcao");
			detEvento.setXCorrecao(UtilServer.normaliza(obs));
			detEvento
					.setXCondUso("A Carta de Correcao e disciplinada pelo paragrafo 1o-A do art. 7o do Convenio S/N, de 15 de dezembro de 1970 e pode ser utilizada para regularizacao de erro ocorrido na emissao de documento fiscal, desde que o erro nao esteja relacionado com: I - as variaveis que determinam o valor do imposto tais como: base de calculo, aliquota, diferenca de preco, quantidade, valor da operacao ou da prestacao; II - a correcao de dados cadastrais que implique mudanca do remetente ou do destinatario; III - a data de emissao ou de saida.");
			infEvento.setDetEvento(detEvento);

			// evento
			TEvento evento = new TEvento();
			evento.setInfEvento(infEvento);
			evento.setVersao(versao);

			// transforma em string o xml e salva
			JAXBElement<TEvento> element = new br.com.opensig.cce.ObjectFactory().createEvento(evento);
			return UtilServer.objToXml(element, "br.com.opensig.cce");
		} catch (Exception e) {
			throw new OpenSigException(e.getMessage());
		}
	}
}
