package br.com.opensig.comercial.server.acao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.JAXBElement;

import br.com.opensig.comercial.client.servico.ComercialException;
import br.com.opensig.comercial.shared.modelo.ComCompra;
import br.com.opensig.comercial.shared.modelo.ComCompraProduto;
import br.com.opensig.comercial.shared.modelo.ComFrete;
import br.com.opensig.comercial.shared.modelo.ComNatureza;
import br.com.opensig.comercial.shared.modelo.ComTroca;
import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.FiltroNumero;
import br.com.opensig.core.client.controlador.filtro.FiltroObjeto;
import br.com.opensig.core.client.controlador.filtro.FiltroTexto;
import br.com.opensig.core.client.controlador.parametro.GrupoParametro;
import br.com.opensig.core.client.controlador.parametro.IParametro;
import br.com.opensig.core.client.controlador.parametro.ParametroBinario;
import br.com.opensig.core.client.controlador.parametro.ParametroNumero;
import br.com.opensig.core.client.controlador.parametro.ParametroObjeto;
import br.com.opensig.core.client.padroes.Chain;
import br.com.opensig.core.client.servico.CoreException;
import br.com.opensig.core.client.servico.OpenSigException;
import br.com.opensig.core.server.CoreServiceImpl;
import br.com.opensig.core.server.UtilServer;
import br.com.opensig.core.shared.modelo.Autenticacao;
import br.com.opensig.core.shared.modelo.EBusca;
import br.com.opensig.core.shared.modelo.EComando;
import br.com.opensig.core.shared.modelo.Lista;
import br.com.opensig.core.shared.modelo.Sql;
import br.com.opensig.empresa.shared.modelo.EmpContato;
import br.com.opensig.empresa.shared.modelo.EmpEmpresa;
import br.com.opensig.empresa.shared.modelo.EmpEndereco;
import br.com.opensig.empresa.shared.modelo.EmpEntidade;
import br.com.opensig.empresa.shared.modelo.EmpMunicipio;
import br.com.opensig.empresa.shared.modelo.EmpTransportadora;
import br.com.opensig.financeiro.shared.modelo.FinPagamento;
import br.com.opensig.financeiro.shared.modelo.FinPagar;
import br.com.opensig.fiscal.server.acao.SalvarEntrada;
import br.com.opensig.fiscal.shared.modelo.ENotaStatus;
import br.com.opensig.fiscal.shared.modelo.FisNotaEntrada;
import br.com.opensig.fiscal.shared.modelo.FisNotaStatus;
import br.com.opensig.nfe.TEnderEmi;
import br.com.opensig.nfe.TEndereco;
import br.com.opensig.nfe.TNFe;
import br.com.opensig.nfe.TNFe.InfNFe;
import br.com.opensig.nfe.TNFe.InfNFe.Cobr;
import br.com.opensig.nfe.TNFe.InfNFe.Cobr.Dup;
import br.com.opensig.nfe.TNFe.InfNFe.Dest;
import br.com.opensig.nfe.TNFe.InfNFe.Det;
import br.com.opensig.nfe.TNFe.InfNFe.Det.Imposto;
import br.com.opensig.nfe.TNFe.InfNFe.Det.Imposto.COFINS;
import br.com.opensig.nfe.TNFe.InfNFe.Det.Imposto.COFINS.COFINSAliq;
import br.com.opensig.nfe.TNFe.InfNFe.Det.Imposto.COFINS.COFINSOutr;
import br.com.opensig.nfe.TNFe.InfNFe.Det.Imposto.ICMS;
import br.com.opensig.nfe.TNFe.InfNFe.Det.Imposto.ICMS.ICMS00;
import br.com.opensig.nfe.TNFe.InfNFe.Det.Imposto.ICMS.ICMS30;
import br.com.opensig.nfe.TNFe.InfNFe.Det.Imposto.ICMS.ICMS40;
import br.com.opensig.nfe.TNFe.InfNFe.Det.Imposto.ICMS.ICMS60;
import br.com.opensig.nfe.TNFe.InfNFe.Det.Imposto.ICMS.ICMS90;
import br.com.opensig.nfe.TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN101;
import br.com.opensig.nfe.TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN102;
import br.com.opensig.nfe.TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN201;
import br.com.opensig.nfe.TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN202;
import br.com.opensig.nfe.TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN500;
import br.com.opensig.nfe.TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN900;
import br.com.opensig.nfe.TNFe.InfNFe.Det.Imposto.IPI;
import br.com.opensig.nfe.TNFe.InfNFe.Det.Imposto.IPI.IPITrib;
import br.com.opensig.nfe.TNFe.InfNFe.Det.Imposto.PIS;
import br.com.opensig.nfe.TNFe.InfNFe.Det.Imposto.PIS.PISAliq;
import br.com.opensig.nfe.TNFe.InfNFe.Det.Imposto.PIS.PISOutr;
import br.com.opensig.nfe.TNFe.InfNFe.Det.Prod;
import br.com.opensig.nfe.TNFe.InfNFe.Emit;
import br.com.opensig.nfe.TNFe.InfNFe.Ide;
import br.com.opensig.nfe.TNFe.InfNFe.Ide.NFref;
import br.com.opensig.nfe.TNFe.InfNFe.Ide.NFref.RefECF;
import br.com.opensig.nfe.TNFe.InfNFe.InfAdic;
import br.com.opensig.nfe.TNFe.InfNFe.Total;
import br.com.opensig.nfe.TNFe.InfNFe.Total.ICMSTot;
import br.com.opensig.nfe.TNFe.InfNFe.Transp;
import br.com.opensig.nfe.TNFe.InfNFe.Transp.Transporta;
import br.com.opensig.nfe.TNFe.InfNFe.Transp.Vol;
import br.com.opensig.nfe.TUf;
import br.com.opensig.nfe.TUfEmi;
import br.com.opensig.produto.shared.modelo.ProdProduto;

public class GerarNfeEntrada extends Chain {
	private CoreServiceImpl servico;
	private ComCompra compra;
	private ComFrete frete;
	private List<ComTroca> trocas;
	private Date data;
	private FisNotaEntrada nota;
	private Autenticacao auth;
	private String nfe;

	private EmpEmpresa empEmissao;
	private EmpEndereco endeEmissao;
	private String telEmissao;
	private String emailEmissao;

	private String cNF;
	private String cDV;
	private String nNF;
	private String chave;

	private boolean dentro;
	private ComNatureza comNatureza;
	private Map<String, String> infos;

	private double valorProd;
	private double baseICMS;
	private double baseST;
	private double valorICMS;
	private double valorST;
	private double valorIpi;
	private double valorPis;
	private double valorCofins;

	public GerarNfeEntrada(Chain next, CoreServiceImpl servico, ComCompra compra, ComFrete frete, String nfe, Autenticacao auth) throws OpenSigException {
		super(next);
		this.servico = servico;
		this.compra = compra;
		this.frete = frete;
		this.data = new Date();
		this.infos = new HashMap<String, String>();
		this.auth = auth;
		this.nfe = nfe;
	}

	@Override
	public synchronized void execute() throws OpenSigException {
		try {
			// emissao
			getEmissao(compra.getEmpEmpresa().getEmpEmpresaId());
			// numero
			getNumero(compra.getEmpEmpresa().getEmpEmpresaId());
			// natureza
			getNatureza(compra.getComNatureza().getComNaturezaId());
			// chave
			getChaveAcesso();

			// informacoes da NFe
			InfNFe infNFe = new InfNFe();
			infNFe.setId("NFe" + chave);
			infNFe.setVersao(auth.getConf().get("nfe.versao"));
			// no do ide
			infNFe.setIde(getIde());
			// no do emissor
			infNFe.setEmit(getEmissor());
			// no do destinatario
			infNFe.setDest(getDestinatario());
			// produtos
			getProdutos(infNFe.getDet());
			// totais
			infNFe.setTotal(getTotais());
			// transporte
			infNFe.setTransp(getTransporte());
			// cobranca
			if (compra.getFinPagar() != null) {
				infNFe.setCobr(getCobranca(compra.getFinPagar()));
			}
			// informacoes
			infNFe.setInfAdic(getInformacoes());
			// finaliza
			TNFe nfe = new TNFe();
			nfe.setInfNFe(infNFe);

			// transforma em XML
			JAXBElement<TNFe> element = new br.com.opensig.nfe.ObjectFactory().createNFe(nfe);
			String xml = UtilServer.objToXml(element, "br.com.opensig.nfe");

			// Monta a NF de entrada, atualiza a compra internamente
			SalvarEntrada ss = new SalvarEntrada(null, xml, new FisNotaStatus(ENotaStatus.AUTORIZANDO), auth);
			ss.execute();
			nota = ss.getNota();

			// atualiza a compra
			FiltroNumero fn = new FiltroNumero("comCompraId", ECompara.IGUAL, compra.getComCompraId());
			ParametroBinario pb = new ParametroBinario("comCompraNfe", 1);
			ParametroObjeto po = new ParametroObjeto("fisNotaEntrada", nota);
			ParametroNumero pn1 = new ParametroNumero("comCompraSerie", 0);
			ParametroNumero pn2 = new ParametroNumero("comCompraNumero", nota.getFisNotaEntradaNumero());
			GrupoParametro gp = new GrupoParametro(new IParametro[] { pb, po, pn1, pn2 });

			Collection<Sql> sqls = new ArrayList<Sql>();
			sqls.add(new Sql(compra, EComando.ATUALIZAR, fn, gp));

			// atualiza o pagar
			if (compra.getFinPagar() != null) {
				FiltroNumero fn1 = new FiltroNumero("finPagarId", ECompara.IGUAL, compra.getFinPagar().getFinPagarId());
				ParametroNumero pn = new ParametroNumero("finPagarNfe", nota.getFisNotaEntradaNumero());
				sqls.add(new Sql(new FinPagar(), EComando.ATUALIZAR, fn1, pn));
			}
			servico.executar(sqls.toArray(new Sql[] {}));
		} catch (Exception ex) {
			UtilServer.LOG.error("Erro na montagem do xml.", ex);
			throw new ComercialException(ex.getMessage());
		}
	}

	public FisNotaEntrada getNota() {
		nota.anularDependencia();
		return nota;
	}

	private void getEmissao(int id) throws ComercialException {
		try {
			FiltroNumero fn = new FiltroNumero("empEmpresaId", ECompara.IGUAL, id);
			empEmissao = new EmpEmpresa();
			empEmissao = (EmpEmpresa) servico.selecionar(empEmissao, fn, false);

			// pega o endereco comercial
			for (EmpEndereco ende : empEmissao.getEmpEntidade().getEmpEnderecos()) {
				if (ende.getEmpEnderecoTipo().getEmpEnderecoTipoId() == Integer.valueOf(auth.getConf().get("nfe.tipoendecom"))) {
					endeEmissao = ende;
					break;
				}
			}

			// pega o telefone e o email
			for (EmpContato cont : empEmissao.getEmpEntidade().getEmpContatos()) {
				if (cont.getEmpContatoTipo().getEmpContatoTipoId() == Integer.valueOf(auth.getConf().get("nfe.tipoconttel")) && telEmissao == null) {
					telEmissao = cont.getEmpContatoDescricao();
				}
				if (cont.getEmpContatoTipo().getEmpContatoTipoId() == Integer.valueOf(auth.getConf().get("nfe.tipocontemail")) && emailEmissao == null) {
					emailEmissao = cont.getEmpContatoDescricao();
				}
			}
		} catch (Exception ex) {
			UtilServer.LOG.error("Erro no endereço do emissor.", ex);
			throw new ComercialException("Erro no endereço do emissor!");
		}
	}

	private void getNumero(int id) throws ComercialException {
		try {
			FiltroTexto ft = new FiltroTexto("fisNotaEntradaChave", ECompara.CONTEM, empEmissao.getEmpEntidade().getEmpEntidadeDocumento1().replaceAll("\\D", ""));
			Number nfeNumero = servico.buscar(new FisNotaEntrada(), "t.fisNotaEntradaNumero", EBusca.MAXIMO, ft);
			if (nfeNumero == null) {
				nfeNumero = 0;
			}
			nNF = UtilServer.formataNumero(nfeNumero.intValue() + 1, 9, 0, false);
		} catch (Exception ex) {
			UtilServer.LOG.error("Erro ao gerar o numero da NFe.", ex);
			throw new ComercialException("Erro ao gerar o numero da NFe!");
		}
	}

	private void getNatureza(int id) {
		try {
			FiltroNumero fn = new FiltroNumero("comNaturezaId", ECompara.IGUAL, id);
			comNatureza = new ComNatureza();
			comNatureza = (ComNatureza) servico.selecionar(comNatureza, fn, false);
		} catch (Exception ex) {
			comNatureza = new ComNatureza(0);
		}
	}

	public void getChaveAcesso() {
		StringBuffer sb = new StringBuffer();
		// uf
		sb.append(endeEmissao.getEmpMunicipio().getEmpEstado().getEmpEstadoIbge());
		// data
		sb.append(UtilServer.formataData(data, "yyMM"));
		// cnpj
		sb.append(empEmissao.getEmpEntidade().getEmpEntidadeDocumento1().replaceAll("\\D", ""));
		// modo
		sb.append(auth.getConf().get("nfe.modo"));
		// serie unicao = ZERO
		sb.append(UtilServer.formataNumero(0, 3, 0, false));
		// numero nf
		sb.append(nNF);
		// tipo emissao
		sb.append(auth.getConf().get("nfe.tipoemi"));
		// codigo nfe
		cNF = (data.getTime() + "").substring(0, 8);
		sb.append(cNF);
		// dv nfe 1 digito, por 9 casas
		cDV = UtilServer.modulo11(sb.toString(), 1, 9);
		sb.append(cDV);
		// coloca no config
		chave = sb.toString();
	}

	public Ide getIde() {
		// seleciona as trocas desta compra se tiver
		try {
			FiltroObjeto fo = new FiltroObjeto("comCompra", ECompara.IGUAL, compra);
			trocas = servico.selecionar(new ComTroca(), 0, 0, fo, false).getLista();
		} catch (CoreException e) {
			trocas = null;
		}

		Ide ide = new Ide();
		// uf
		ide.setCUF(endeEmissao.getEmpMunicipio().getEmpEstado().getEmpEstadoIbge() + "");
		// numero fiscal
		ide.setCNF(cNF);
		// natureza
		ide.setNatOp(comNatureza.getComNaturezaDescricao().trim());
		// forma pagamento
		ide.setIndPag((compra.getFinPagar() != null ? "1" : "2"));
		// modo
		ide.setMod(auth.getConf().get("nfe.modo"));
		// serie unica = ZERO
		ide.setSerie("0");
		// numero nf
		ide.setNNF(Integer.valueOf(nNF) + "");
		// data emissao
		ide.setDEmi(UtilServer.formataData(data, "yyyy-MM-dd"));
		// data saida
		ide.setDSaiEnt(UtilServer.formataData(data, "yyyy-MM-dd"));
		// operacao
		ide.setTpNF("0");
		// municipio
		ide.setCMunFG(endeEmissao.getEmpMunicipio().getEmpMunicipioIbge() + "");
		// impressao
		ide.setTpImp(auth.getConf().get("nfe.tipoimp"));
		// emissao
		ide.setTpEmis(auth.getConf().get("nfe.tipoemi"));
		// verificador
		ide.setCDV(cDV);
		// ambiente
		ide.setTpAmb(auth.getConf().get("nfe.tipoamb"));
		// finalidade
		ide.setFinNFe(nfe.equals("") ? "1" : "2");
		// processo emissao
		ide.setProcEmi(auth.getConf().get("nfe.procemi"));
		// versao processo
		ide.setVerProc(auth.getConf().get("nfe.procver"));

		// caso seja nfe complementar
		if (!nfe.equals("")) {
			NFref ref = new NFref();
			ref.setRefNFe(nfe);
			ide.getNFref().add(ref);
		}
		// caso seja troca com ecf refenciadas
		if (trocas != null) {
			for (ComTroca troca : trocas) {
				RefECF ecf = new RefECF();
				ecf.setMod("2D");
				ecf.setNECF(troca.getComTrocaEcf() + "");
				ecf.setNCOO(troca.getComTrocaCoo() + "");

				NFref ref = new NFref();
				ref.setRefECF(ecf);
				ide.getNFref().add(ref);
			}
		}

		return ide;
	}

	public Emit getEmissor() {
		Emit emit = new Emit();
		// crt
		emit.setCRT(auth.getConf().get("nfe.crt"));
		// empresa
		emit.setCNPJ(empEmissao.getEmpEntidade().getEmpEntidadeDocumento1().replaceAll("\\D", "").trim());
		String razao = empEmissao.getEmpEntidade().getEmpEntidadeNome1().trim();
		razao = razao.length() > 60 ? razao.substring(0, 60) : razao;
		emit.setXNome(razao);
		emit.setXFant(empEmissao.getEmpEntidade().getEmpEntidadeNome2().trim());
		emit.setIE(empEmissao.getEmpEntidade().getEmpEntidadeDocumento2().replaceAll("\\D", "").trim());
		// endereco
		EmpMunicipio mun = endeEmissao.getEmpMunicipio();
		TEnderEmi enderEmit = new TEnderEmi();
		enderEmit.setXLgr(endeEmissao.getEmpEnderecoLogradouro().trim());
		enderEmit.setNro(endeEmissao.getEmpEnderecoNumero());
		if (endeEmissao.getEmpEnderecoComplemento() != null && !endeEmissao.getEmpEnderecoComplemento().trim().equals("")) {
			enderEmit.setXCpl(endeEmissao.getEmpEnderecoComplemento().trim());
		}
		if (endeEmissao.getEmpEnderecoBairro() != null && !endeEmissao.getEmpEnderecoBairro().trim().equals("")) {
			enderEmit.setXBairro(endeEmissao.getEmpEnderecoBairro());
		}
		enderEmit.setCMun(mun.getEmpMunicipioIbge() + "");
		enderEmit.setXMun(mun.getEmpMunicipioDescricao().trim());
		enderEmit.setUF(TUfEmi.valueOf(mun.getEmpEstado().getEmpEstadoSigla().trim()));
		enderEmit.setCEP(endeEmissao.getEmpEnderecoCep().replaceAll("\\D", "").trim());
		enderEmit.setCPais(mun.getEmpEstado().getEmpPais().getEmpPaisIbge() + "");
		enderEmit.setXPais(mun.getEmpEstado().getEmpPais().getEmpPaisDescricao().trim());
		if (telEmissao != null && !telEmissao.replaceAll("\\D", "").trim().equals("")) {
			enderEmit.setFone(telEmissao.replaceAll("\\D", "").trim());
		}
		emit.setEnderEmit(enderEmit);

		return emit;
	}

	public Dest getDestinatario() {
		dentro = true;
		Dest dest = new Dest();
		String razao = empEmissao.getEmpEntidade().getEmpEntidadeNome1().trim();
		razao = razao.length() > 60 ? razao.substring(0, 60) : razao;

		// empresa
		if (auth.getConf().get("nfe.tipoamb").equals("2")) {
			dest.setCNPJ("99999999000191");
			dest.setXNome("NF-E EMITIDA EM AMBIENTE DE HOMOLOGACAO - SEM VALOR FISCAL");
			dest.setIE("");
		} else if (empEmissao.getEmpEntidade().getEmpEntidadePessoa().equalsIgnoreCase("jurídica")) {
			dest.setCNPJ(empEmissao.getEmpEntidade().getEmpEntidadeDocumento1().replaceAll("\\D", "").trim());
			dest.setXNome(razao);
			dest.setIE(empEmissao.getEmpEntidade().getEmpEntidadeDocumento2().replaceAll("\\D", "").trim());
		} else {
			dest.setCPF(empEmissao.getEmpEntidade().getEmpEntidadeDocumento1().replaceAll("\\D", "").trim());
			dest.setXNome(razao);
			dest.setIE("");
		}
		// endereco
		EmpMunicipio mun = endeEmissao.getEmpMunicipio();
		TEndereco enderDest = new TEndereco();
		enderDest.setXLgr(endeEmissao.getEmpEnderecoLogradouro().trim());
		enderDest.setNro(endeEmissao.getEmpEnderecoNumero());
		if (endeEmissao.getEmpEnderecoComplemento() != null && !endeEmissao.getEmpEnderecoComplemento().trim().equals("")) {
			enderDest.setXCpl(endeEmissao.getEmpEnderecoComplemento().trim());
		}
		if (endeEmissao.getEmpEnderecoBairro() != null && !endeEmissao.getEmpEnderecoBairro().trim().equals("")) {
			enderDest.setXBairro(endeEmissao.getEmpEnderecoBairro());
		}
		enderDest.setCMun(mun.getEmpMunicipioIbge() + "");
		enderDest.setXMun(mun.getEmpMunicipioDescricao().trim());
		enderDest.setUF(TUf.valueOf(mun.getEmpEstado().getEmpEstadoSigla().trim()));
		enderDest.setCEP(endeEmissao.getEmpEnderecoCep().replaceAll("\\D", "").trim());
		enderDest.setCPais(mun.getEmpEstado().getEmpPais().getEmpPaisIbge() + "");
		enderDest.setXPais(mun.getEmpEstado().getEmpPais().getEmpPaisDescricao().trim());
		if (telEmissao != null && !telEmissao.replaceAll("\\D", "").trim().equals("")) {
			enderDest.setFone(telEmissao.replaceAll("\\D", "").trim());
		}
		dest.setEnderDest(enderDest);

		return dest;
	}

	public void getProdutos(List<Det> dets) throws ComercialException {
		FiltroObjeto fo = new FiltroObjeto("comCompra", ECompara.IGUAL, compra);
		Lista<ComCompraProduto> produtos = null;
		try {
			produtos = (Lista<ComCompraProduto>) servico.selecionar(new ComCompraProduto(), 0, 0, fo, false);
		} catch (Exception ex) {
			UtilServer.LOG.error("Erro nos produtos da compra.", ex);
			throw new ComercialException("Erro nos produtos da compra!");
		}

		int i = 1;
		for (ComCompraProduto cp : produtos.getLista()) {
			ProdProduto pp = cp.getProdProduto();

			// setando o item
			Det det = new Det();
			det.setNItem((i++) + "");
			// cod produto
			Prod prod = new Prod();
			if (pp.getProdProdutoBarra() == null) {
				prod.setCProd(UtilServer.formataNumero(pp.getProdProdutoId(), 6, 0, false));
			} else {
				prod.setCProd(pp.getProdProdutoBarra());
			}
			// barra
			prod.setCEAN(pp.getProdProdutoBarra() == null ? "" : pp.getProdProdutoBarra());
			// descricao
			prod.setXProd(pp.getProdProdutoDescricao().trim());
			// ncm
			prod.setNCM(pp.getProdProdutoNcm());
			// cfop
			prod.setCFOP(getCfop(cp));
			// unidade
			prod.setUCom(pp.getProdEmbalagem().getProdEmbalagemNome());
			// quantidde
			prod.setQCom(UtilServer.formataNumero(cp.getComCompraProdutoQuantidade(), 1, 4, false).replace(",", "."));
			// valor unitario
			prod.setVUnCom(UtilServer.formataNumero(cp.getComCompraProdutoValor(), 1, 4, false).replace(",", "."));
			// valor produto
			String strProd = getValorNfe(cp.getComCompraProdutoTotal());
			valorProd += Double.valueOf(strProd);
			prod.setVProd(strProd);
			// barra do tributo
			prod.setCEANTrib(pp.getProdProdutoBarra() == null ? "" : pp.getProdProdutoBarra());
			// unidade do tributo
			prod.setUTrib(pp.getProdEmbalagem().getProdEmbalagemNome());
			// quantidde do tributo
			prod.setQTrib(UtilServer.formataNumero(cp.getComCompraProdutoQuantidade(), 1, 4, false).replace(",", "."));
			// valor unitario
			prod.setVUnTrib(UtilServer.formataNumero(cp.getComCompraProdutoValor(), 1, 4, false).replace(",", "."));
			// total da NF
			prod.setIndTot("1");
			// setando o produto
			det.setProd(prod);
			// setando os impostos
			det.setImposto(getImposto(cp));
			// adiciona a lista
			dets.add(det);

			// verifica se tem algum decreto de icms
			if (!pp.getProdIcms().getProdIcmsDecreto().equals("")) {
				infos.put("ICMS" + pp.getProdIcms().getProdIcmsId(), pp.getProdIcms().getProdIcmsDecreto());
			}
			// verifica se tem algum decreto de ipi
			if (!pp.getProdIpi().getProdIpiDecreto().equals("")) {
				infos.put("IPI" + pp.getProdIpi().getProdIpiId(), pp.getProdIpi().getProdIpiDecreto());
			}
			// verifica se tem algum decreto de pis
			if (!pp.getProdPis().getProdPisDecreto().equals("")) {
				infos.put("PIS" + pp.getProdPis().getProdPisId(), pp.getProdPis().getProdPisDecreto());
			}
			// verifica se tem algum decreto de cofins
			if (!pp.getProdCofins().getProdCofinsDecreto().equals("")) {
				infos.put("COFINS" + pp.getProdCofins().getProdCofinsId(), pp.getProdCofins().getProdCofinsDecreto());
			}
		}
	}

	public String getCfop(ComCompraProduto cp) {
		int cfop = dentro ? 0 : 1000;
		if (cp.getComCompraProdutoCfop() == 0) {
			String cst = cp.getComCompraProdutoIcmsCst().equals("") ? cp.getProdProduto().getProdIcms().getProdIcmsCst() : cp.getComCompraProdutoIcmsCst();
			// verifica se é substituicao
			if (cst.equals("10") || cst.equals("60")) {
				cfop += comNatureza.getComNaturezaCfopSub();
			} else {
				cfop += comNatureza.getComNaturezaCfopTrib();
			}
		} else {
			cfop = cp.getComCompraProdutoCfop();
		}

		return cfop + "";
	}

	public Imposto getImposto(ComCompraProduto cp) {
		Imposto imposto = new Imposto();
		String cst = cp.getComCompraProdutoIcmsCst();

		// icms
		if ((auth.getConf().get("nfe.crt").equals("1") && cst.equals("")) || cst.length() == 3) {
			imposto.setICMS(getSimples(cp));
		} else {
			imposto.setICMS(getNormal(cp));
		}
		// ipi
		imposto.setIPI(getIPI(cp));
		// pis
		imposto.setPIS(getPIS(cp));
		// confins
		imposto.setCOFINS(getCOFINS(cp));

		return imposto;
	}

	public ICMS getSimples(ComCompraProduto cp) {
		ICMS icms = new ICMS();
		String cson = cp.getComCompraProdutoIcmsCst().equals("") ? cp.getProdProduto().getProdIcms().getProdIcmsCson() : cp.getComCompraProdutoIcmsCst();
		String origem = String.valueOf(cp.getProdProduto().getProdOrigem().getProdOrigemValor());

		if (cson.equals("101")) {
			ICMSSN101 icmssn101 = new ICMSSN101();
			icmssn101.setOrig(origem);
			icmssn101.setCSOSN(cson);
			double porcento = cp.getComCompraProdutoIcms() == 0.00 ? Double.valueOf(auth.getConf().get("nfe.cson")) : cp.getComCompraProdutoIcms();
			icmssn101.setPCredSN(getValorNfe(porcento));
			double valor = cp.getComCompraProdutoTotal() * porcento / 100;
			icmssn101.setVCredICMSSN(getValorNfe(valor));
			icms.setICMSSN101(icmssn101);
		} else if (cson.equals("102")) {
			ICMSSN102 icmssn102 = new ICMSSN102();
			icmssn102.setOrig(origem);
			icmssn102.setCSOSN(cson);
			icms.setICMSSN102(icmssn102);
		} else if (cson.equals("201")) {
			ICMSSN201 icmssn201 = new ICMSSN201();
			icmssn201.setOrig(origem);
			icmssn201.setCSOSN(cson);
			icmssn201.setModBCST(auth.getConf().get("nfe.modocalcst"));
			icmssn201.setVBCST("0.00");
			icmssn201.setPICMSST("0.00");
			icmssn201.setVICMSST("0.00");
			icmssn201.setPCredSN("0.00");
			icmssn201.setVCredICMSSN("0.00");
			icms.setICMSSN201(icmssn201);
		} else if (cson.equals("202")) {
			ICMSSN202 icmssn202 = new ICMSSN202();
			icmssn202.setOrig(origem);
			icmssn202.setCSOSN(cson);
			icmssn202.setModBCST(auth.getConf().get("nfe.modocalcst"));
			icmssn202.setVBCST("0.00");
			icmssn202.setPICMSST("0.00");
			icmssn202.setVICMSST("0.00");
			icms.setICMSSN202(icmssn202);
		} else if (cson.equals("500")) {
			ICMSSN500 icmssn500 = new ICMSSN500();
			icmssn500.setOrig(origem);
			icmssn500.setCSOSN(cson);
			icmssn500.setVBCSTRet("0.00");
			icmssn500.setVICMSSTRet("0.00");
			icms.setICMSSN500(icmssn500);
		} else {
			ICMSSN900 icmssn900 = new ICMSSN900();
			icmssn900.setOrig(origem);
			icmssn900.setCSOSN(cson);
			icms.setICMSSN900(icmssn900);
		}

		return icms;
	}

	public ICMS getNormal(ComCompraProduto cp) {
		ICMS icms = new ICMS();
		String cst = cp.getComCompraProdutoIcmsCst().equals("") ? cp.getProdProduto().getProdIcms().getProdIcmsCst() : cp.getComCompraProdutoIcmsCst();
		String origem = String.valueOf(cp.getProdProduto().getProdOrigem().getProdOrigemValor());

		// se é 10 e muda pra 60
		if (cst.equals("10")) {
			cst = "60";
		}// se for 41 muda pra 40
		else if (cst.equals("41")) {
			cst = "40";
		}

		// modo base calculo normal
		if (cst.equals("00")) {
			ICMS00 icms00 = new ICMS00();
			icms00.setOrig(origem);
			icms00.setCST(cst);
			icms00.setModBC(auth.getConf().get("nfe.modocalc"));
			// porcentagem icms
			double porcento = 0.00;
			if (comNatureza.getComNaturezaIcms()) {
				if (cp.getComCompraProdutoIcms() > 0) {
					porcento = cp.getComCompraProdutoIcms();
				} else {
					porcento = dentro ? cp.getProdProduto().getProdIcms().getProdIcmsDentro() : cp.getProdProduto().getProdIcms().getProdIcmsFora();
				}
			}
			// valor da base de calculo
			String strBase = porcento == 0.00 ? "0.00" : getValorNfe(cp.getComCompraProdutoTotal());
			double base = Double.valueOf(strBase);
			icms00.setVBC(strBase);
			icms00.setPICMS(getValorNfe(porcento));
			// valor icms
			String strValor = getValorNfe(base * porcento / 100);
			double valor = Double.valueOf(strValor);
			icms00.setVICMS(strValor);
			icms.setICMS00(icms00);
			// executa a soma dos impostos
			baseICMS += base;
			valorICMS += valor;
		} else if (cst.equals("30")) {
			ICMS30 icms30 = new ICMS30();
			icms30.setOrig(origem);
			icms30.setCST(cst);
			icms30.setModBCST(auth.getConf().get("nfe.modocalcst"));
			icms30.setVBCST("0.00");
			icms30.setPICMSST("0.00");
			icms30.setVICMSST("0.00");
			icms.setICMS30(icms30);
		} else if (cst.equals("40")) {
			ICMS40 icms40 = new ICMS40();
			icms40.setOrig(origem);
			icms40.setCST(cst);
			icms.setICMS40(icms40);
		} else if (cst.equals("60")) {
			ICMS60 icms60 = new ICMS60();
			icms60.setOrig(origem);
			icms60.setCST(cst);
			icms60.setVBCSTRet("0.00");
			icms60.setVICMSSTRet("0.00");
			icms.setICMS60(icms60);
		} else {
			ICMS90 icms90 = new ICMS90();
			icms90.setOrig(origem);
			icms90.setCST(cst);
			icms.setICMS90(icms90);
		}

		return icms;
	}

	public IPI getIPI(ComCompraProduto cp) {
		IPI ipi = new IPI();
		String cst = cp.getComCompraProdutoIpiCst().equals("") ? cp.getProdProduto().getProdIpi().getProdIpiCstSaida() : cp.getComCompraProdutoIpiCst();

		// identifica pela natureza se cobra IPI
		double porcento = 0.00;
		if (comNatureza.getComNaturezaIpi()) {
			if (cp.getComCompraProdutoIpi() > 0) {
				porcento = cp.getComCompraProdutoIpi();
			} else {
				porcento = cp.getProdProduto().getProdIpi().getProdIpiAliquota();
			}
		}

		// faz o calculo do valor e seta o tipo do cst
		double valor = cp.getComCompraProdutoTotal() * porcento / 100;
		String strValor = getValorNfe(valor);
		valorIpi += Double.valueOf(strValor);

		IPITrib trib = new IPITrib();
		if (porcento == 0.00) {
			trib.setCST("49");
			trib.setVBC("0.00");
			trib.setPIPI("0.00");
			trib.setVIPI("0.00");
		} else {
			trib.setCST(cst);
			trib.setVBC(getValorNfe(cp.getComCompraProdutoTotal()));
			trib.setPIPI(getValorNfe(porcento));
			trib.setVIPI(strValor);
		}

		ipi.setIPITrib(trib);
		ipi.setCEnq(cp.getProdProduto().getProdIpi().getProdIpiEnq());
		return ipi;
	}

	public PIS getPIS(ComCompraProduto cp) {
		PIS pis = new PIS();
		String cst = cp.getComCompraProdutoPisCst().equals("") ? cp.getProdProduto().getProdPis().getProdPisCstSaida() : cp.getComCompraProdutoPisCst();

		// identifica pela natureza se cobra PIS
		double porcento = 0.00;
		if (comNatureza.getComNaturezaPis()) {
			if (cp.getComCompraProdutoPis() > 0) {
				porcento = cp.getComCompraProdutoPis();
			} else {
				porcento = cp.getProdProduto().getProdPis().getProdPisAliquota();
			}
		}

		// faz o calculo do valor e define
		double valor = cp.getComCompraProdutoTotal() * porcento / 100;
		String strValor = getValorNfe(valor);
		valorPis += Double.valueOf(strValor);

		// isento ou simples nacional
		if (porcento == 0.00) {
			PISOutr outr = new PISOutr();
			outr.setCST("98");
			outr.setVBC("0.00");
			outr.setPPIS("0.00");
			outr.setVPIS("0.00");
			pis.setPISOutr(outr);
		} else if (cst.equals("98")) {
			PISOutr outr = new PISOutr();
			outr.setCST(cst);
			outr.setVBC(getValorNfe(cp.getComCompraProdutoTotal()));
			outr.setPPIS(getValorNfe(porcento));
			outr.setVPIS(strValor);
			pis.setPISOutr(outr);
		} else {
			PISAliq aliq = new PISAliq();
			aliq.setCST(cst);
			aliq.setVBC(getValorNfe(cp.getComCompraProdutoTotal()));
			aliq.setPPIS(getValorNfe(porcento));
			aliq.setVPIS(strValor);
			pis.setPISAliq(aliq);
		}

		return pis;
	}

	public COFINS getCOFINS(ComCompraProduto cp) {
		COFINS cofins = new COFINS();
		String cst = cp.getComCompraProdutoCofinsCst().equals("") ? cp.getProdProduto().getProdCofins().getProdCofinsCstSaida() : cp.getComCompraProdutoCofinsCst();

		// identifica pela natureza se cobra PIS
		double porcento = 0.00;
		if (comNatureza.getComNaturezaCofins()) {
			if (cp.getComCompraProdutoCofins() > 0) {
				porcento = cp.getComCompraProdutoCofins();
			} else {
				porcento = cp.getProdProduto().getProdCofins().getProdCofinsAliquota();
			}
		}

		// faz o calculo do valor e define
		double valor = cp.getComCompraProdutoTotal() * porcento / 100;
		String strValor = getValorNfe(valor);
		valorCofins += Double.valueOf(strValor);

		// isento ou simples nacional
		if (porcento == 0.00) {
			COFINSOutr outr = new COFINSOutr();
			outr.setCST("98");
			outr.setVBC("0.00");
			outr.setPCOFINS("0.00");
			outr.setVCOFINS("0.00");
			cofins.setCOFINSOutr(outr);
		} else if (cst.equals("98")) {
			COFINSOutr outr = new COFINSOutr();
			outr.setCST(cst);
			outr.setVBC(getValorNfe(cp.getComCompraProdutoTotal()));
			outr.setPCOFINS(getValorNfe(porcento));
			outr.setVCOFINS(strValor);
			cofins.setCOFINSOutr(outr);
		} else {
			COFINSAliq aliq = new COFINSAliq();
			aliq.setCST(cst);
			aliq.setVBC(getValorNfe(cp.getComCompraProdutoTotal()));
			aliq.setPCOFINS(getValorNfe(porcento));
			aliq.setVCOFINS(strValor);
			cofins.setCOFINSAliq(aliq);
		}

		return cofins;
	}

	public Total getTotais() {
		Total total = new Total();
		ICMSTot icmstot = new ICMSTot();
		icmstot.setVBC(getValorNfe(baseICMS));
		icmstot.setVICMS(getValorNfe(valorICMS));
		icmstot.setVBCST(getValorNfe(baseST));
		icmstot.setVST(getValorNfe(valorST));
		icmstot.setVProd(getValorNfe(valorProd));
		icmstot.setVFrete("0.00");
		icmstot.setVSeg("0.00");
		icmstot.setVDesc("0.00");
		icmstot.setVII("0.00");
		icmstot.setVIPI(getValorNfe(valorIpi));
		icmstot.setVPIS(getValorNfe(valorPis));
		icmstot.setVCOFINS(getValorNfe(valorCofins));
		icmstot.setVOutro("0.00");
		icmstot.setVNF(getValorNfe(valorProd + valorIpi));
		total.setICMSTot(icmstot);
		return total;
	}

	public Transp getTransporte() throws ComercialException {
		Transp transp = new Transp();
		transp.setModFrete(frete.getComFreteId() + "");

		// ver se tem frete
		if (frete.getComFreteId() != 9) {
			// pegando a transportadora
			EmpEntidade ent;
			try {
				FiltroNumero fn = new FiltroNumero("empTransportadoraId", ECompara.IGUAL, frete.getEmpTransportadora().getEmpTransportadoraId());
				EmpTransportadora trans = new EmpTransportadora();
				trans = (EmpTransportadora) servico.selecionar(trans, fn, false);
				ent = trans.getEmpEntidade();
			} catch (Exception ex) {
				UtilServer.LOG.error("Erro no transporte da NFe.", ex);
				throw new ComercialException("Erro no transporte da NFe!");
			}

			// dados da transportadora
			Transporta transporta = new Transporta();
			transporta.setCNPJ(ent.getEmpEntidadeDocumento1().replaceAll("\\D", ""));
			transporta.setXNome(ent.getEmpEntidadeNome1().trim());
			transporta.setIE(ent.getEmpEntidadeDocumento2().replaceAll("\\D", "").trim());

			// endereco
			if (ent.getEmpEnderecos() != null && !ent.getEmpEnderecos().isEmpty()) {
				EmpEndereco ende = ent.getEmpEnderecos().get(0);
				String endereco = ende.getEmpEnderecoLogradouro().trim() + " " + ende.getEmpEnderecoNumero();
				if (ende.getEmpEnderecoBairro() != null && !ende.getEmpEnderecoBairro().trim().equals("")) {
					endereco += ende.getEmpEnderecoBairro().trim();
				}
				transporta.setXEnder(endereco);
				transporta.setXMun(ende.getEmpMunicipio().getEmpMunicipioDescricao().trim());
				transporta.setUF(TUf.valueOf(ende.getEmpMunicipio().getEmpEstado().getEmpEstadoSigla()));
			}
			transp.setTransporta(transporta);

			// dados de volume
			Vol vol = new Vol();
			vol.setQVol(frete.getComFreteVolume() + "");
			vol.setEsp(frete.getComFreteEspecie().toUpperCase().trim());
			if (frete.getComFreteCubagem() != null) {
				vol.setPesoL(UtilServer.formataNumero(frete.getComFreteCubagem(), 1, 3, false).replace(",", "."));
			}
			if (frete.getComFretePeso() != null) {
				vol.setPesoB(UtilServer.formataNumero(frete.getComFretePeso(), 1, 3, false).replace(",", "."));
			}
			transp.getVol().add(vol);
		}

		return transp;
	}

	public Cobr getCobranca(FinPagar pagar) throws ComercialException {
		// em precossso
		FiltroObjeto fo = new FiltroObjeto("finPagar", ECompara.IGUAL, pagar);
		Lista<FinPagamento> pagamentos = null;

		try {
			pagamentos = (Lista<FinPagamento>) servico.selecionar(new FinPagamento(), 0, 0, fo, false);
			Cobr cobr = new Cobr();
			for (FinPagamento fr : pagamentos.getLista()) {
				Dup dup = new Dup();
				if (fr.getFinForma().getFinFormaId() != 4) {
					dup.setNDup(fr.getFinForma().getFinFormaDescricao() + " " + fr.getFinPagamentoParcela());
				} else {
					dup.setNDup(fr.getFinPagamentoDocumento() + " " + fr.getFinPagamentoParcela());
				}
				dup.setDVenc(UtilServer.formataData(fr.getFinPagamentoVencimento(), "yyyy-MM-dd"));
				dup.setVDup(getValorNfe(fr.getFinPagamentoValor()));
				cobr.getDup().add(dup);
			}
			return cobr;
		} catch (Exception ex) {
			UtilServer.LOG.error("Erro no pagar da NFe.", ex);
			throw new ComercialException("Erro no pagar da NFe!");
		}
	}

	public InfAdic getInformacoes() {
		StringBuffer sb = new StringBuffer();
		// adiciona o decreto da natureza
		if (!comNatureza.getComNaturezaDecreto().equals("")) {
			sb.append(comNatureza.getComNaturezaDecreto() + "#");
		}
		// adiciona os decretos para os tributos
		for (Entry<String, String> info : infos.entrySet()) {
			sb.append(info.getValue() + "#");
		}
		// uma mensagem padrao se precisar
		if (auth.getConf().get("nfe.info") != null) {
			sb.append("#" + auth.getConf().get("nfe.info"));
		}
		// adiciona o pedido da venda
		sb.append("Compra " + UtilServer.formataNumero(compra.getComCompraId(), 6, 0, false) + " ");
		// case tenha alguma observacao
		if (compra.getComCompraObservacao() != null) {
			sb.append("#" + compra.getComCompraObservacao());
		}
		
		InfAdic inf = new InfAdic();
		inf.setInfCpl(sb.toString());
		return inf;
	}

	private String getValorNfe(double valor) {
		return UtilServer.formataNumero(valor, 1, 2, false).replace(",", ".");
	}
}
