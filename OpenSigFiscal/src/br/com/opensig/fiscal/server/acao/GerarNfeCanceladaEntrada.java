package br.com.opensig.fiscal.server.acao;

import javax.xml.bind.JAXBElement;

import br.com.opensig.cancnfe.TCancNFe;
import br.com.opensig.cancnfe.TCancNFe.InfCanc;
import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.FiltroNumero;
import br.com.opensig.core.client.padroes.Chain;
import br.com.opensig.core.client.servico.CoreService;
import br.com.opensig.core.client.servico.OpenSigException;
import br.com.opensig.core.server.UtilServer;
import br.com.opensig.core.shared.modelo.Autenticacao;
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
		entrada = (FisNotaEntrada) servico.selecionar(entrada, fn, false);
		// cria o xml
		String xml = getXml(entrada, obs);
		// salva o registro
		SalvarEntrada salvar = new SalvarEntrada(next, xml, new FisNotaStatus(ENotaStatus.CANCELANDO), auth);
		salvar.execute();
		entrada = salvar.getNota();
	}
	
	public FisNotaEntrada getNota(){
		return entrada;
	}

	private String getXml(FisNotaEntrada entrada, String obs) throws OpenSigException {
		try {
			InfCanc infCanc = new InfCanc();
			infCanc.setId("ID" + entrada.getFisNotaEntradaChave());
			infCanc.setTpAmb(auth.getConf().get("nfe.tipoamb"));
			infCanc.setChNFe(entrada.getFisNotaEntradaChave());
			infCanc.setNProt(entrada.getFisNotaEntradaProtocolo());
			infCanc.setXJust(UtilServer.normaliza(obs));
			infCanc.setXServ("CANCELAR");
			TCancNFe cancNfe = new TCancNFe();
			cancNfe.setInfCanc(infCanc);
			cancNfe.setVersao(auth.getConf().get("nfe.versao"));
			// transforma em string o xml e salva
			JAXBElement<TCancNFe> element = new br.com.opensig.cancnfe.ObjectFactory().createCancNFe(cancNfe);
			return UtilServer.objToXml(element, "br.com.opensig.cancnfe");
		} catch (Exception e) {
			throw new OpenSigException(e.getMessage());
		}
	}
}
