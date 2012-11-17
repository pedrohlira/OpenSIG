package br.com.opensig.fiscal.server.acao;

import javax.xml.bind.JAXBElement;

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
import br.com.opensig.inutnfe.TInutNFe;
import br.com.opensig.inutnfe.TInutNFe.InfInut;

public class GerarNfeInutilizadaEntrada extends Chain {

	private CoreService servico;
	private FisNotaEntrada entrada;
	private String motivo;
	private int ini;
	private int fim;
	private Autenticacao auth;
	
	public GerarNfeInutilizadaEntrada(Chain next, CoreService servico, FisNotaEntrada entrada, String motivo, int ini, int fim, Autenticacao auth) throws OpenSigException {
		super(next);
		this.servico = servico;
		this.entrada = entrada;
		this.motivo = motivo;
		this.ini = ini;
		this.fim = fim;
		this.auth = auth;
	}

	@Override
	public void execute() throws OpenSigException {
		// seleciona a nota
		FiltroNumero fn = new FiltroNumero("fisNotaEntradaId", ECompara.IGUAL, entrada.getId());
		entrada = (FisNotaEntrada) servico.selecionar(entrada, fn, false);
		// cria o xml
		String xml = getXml(entrada, motivo, ini, fim);
		// salva o registro
		SalvarEntrada salvar = new SalvarEntrada(next, xml, new FisNotaStatus(ENotaStatus.INUTILIZANDO), auth);
		salvar.execute();
		entrada = salvar.getNota();
	}
	
	public FisNotaEntrada getNota(){
		return entrada;
	}

	private String getXml(FisNotaEntrada entrada, String motivo, int ini, int fim) throws OpenSigException {
		try {
			// desmembra a chave
			String chave = entrada.getFisNotaEntradaChave();
			String uf = chave.substring(0, 2);
			String ano = chave.substring(2, 4);
			String cnpj = chave.substring(6, 20);
			String modo = chave.substring(20, 22);
			String serie = chave.substring(22, 25);
			String nfIni = UtilServer.formataNumero(ini, 9, 0, false);
			String nfFim = UtilServer.formataNumero(fim, 9, 0, false);
			String id = "ID" + uf + ano + cnpj + modo + serie + nfIni + nfFim;

			// gerar o objeto
			InfInut infInut = new InfInut();
			infInut.setTpAmb(auth.getConf().get("nfe.tipoamb"));
			infInut.setId(id);
			infInut.setCUF(uf);
			infInut.setAno(ano);
			infInut.setCNPJ(cnpj);
			infInut.setMod(modo);
			infInut.setSerie(Integer.valueOf(serie) + "");
			infInut.setNNFIni(ini + "");
			infInut.setNNFFin(fim + "");
			infInut.setXJust(UtilServer.normaliza(motivo));
			infInut.setXServ("INUTILIZAR");
			TInutNFe inutNfe = new TInutNFe();
			inutNfe.setInfInut(infInut);
			inutNfe.setVersao(auth.getConf().get("nfe.versao"));
			// transforma em string o xml e salva
			JAXBElement<TInutNFe> element = new br.com.opensig.inutnfe.ObjectFactory().createInutNFe(inutNfe);
			return UtilServer.objToXml(element, "br.com.opensig.inutnfe");
		} catch (Exception e) {
			throw new OpenSigException(e.getMessage());
		}
	}
}
