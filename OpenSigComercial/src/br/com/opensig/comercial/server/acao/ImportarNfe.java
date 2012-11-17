package br.com.opensig.comercial.server.acao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;

import br.com.opensig.comercial.client.servico.ComercialException;
import br.com.opensig.comercial.server.ComercialServiceImpl;
import br.com.opensig.comercial.shared.modelo.ComCompra;
import br.com.opensig.comercial.shared.modelo.ComCompraProduto;
import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.EJuncao;
import br.com.opensig.core.client.controlador.filtro.FiltroNumero;
import br.com.opensig.core.client.controlador.filtro.FiltroObjeto;
import br.com.opensig.core.client.controlador.filtro.FiltroTexto;
import br.com.opensig.core.client.controlador.filtro.GrupoFiltro;
import br.com.opensig.core.client.controlador.filtro.IFiltro;
import br.com.opensig.core.client.servico.CoreService;
import br.com.opensig.core.client.servico.OpenSigException;
import br.com.opensig.core.server.UtilServer;
import br.com.opensig.core.server.importar.IImportacao;
import br.com.opensig.core.shared.modelo.Autenticacao;
import br.com.opensig.core.shared.modelo.Lista;
import br.com.opensig.core.shared.modelo.sistema.SisExpImp;
import br.com.opensig.empresa.server.EmpresaServiceImpl;
import br.com.opensig.empresa.shared.modelo.EmpContato;
import br.com.opensig.empresa.shared.modelo.EmpContatoTipo;
import br.com.opensig.empresa.shared.modelo.EmpEmpresa;
import br.com.opensig.empresa.shared.modelo.EmpEndereco;
import br.com.opensig.empresa.shared.modelo.EmpEnderecoTipo;
import br.com.opensig.empresa.shared.modelo.EmpEntidade;
import br.com.opensig.empresa.shared.modelo.EmpEstado;
import br.com.opensig.empresa.shared.modelo.EmpFornecedor;
import br.com.opensig.empresa.shared.modelo.EmpMunicipio;
import br.com.opensig.financeiro.shared.modelo.FinConta;
import br.com.opensig.financeiro.shared.modelo.FinForma;
import br.com.opensig.financeiro.shared.modelo.FinPagamento;
import br.com.opensig.financeiro.shared.modelo.FinPagar;
import br.com.opensig.fiscal.shared.modelo.FisNotaEntrada;
import br.com.opensig.nfe.TEnderEmi;
import br.com.opensig.nfe.TNFe;
import br.com.opensig.nfe.TNFe.InfNFe.Cobr.Dup;
import br.com.opensig.nfe.TNFe.InfNFe.Det;
import br.com.opensig.nfe.TNFe.InfNFe.Det.Imposto.ICMS;
import br.com.opensig.nfe.TNFe.InfNFe.Det.Prod;
import br.com.opensig.nfe.TNFe.InfNFe.Emit;
import br.com.opensig.nfe.TNFe.InfNFe.Ide;
import br.com.opensig.nfe.TNFe.InfNFe.Total.ICMSTot;
import br.com.opensig.produto.shared.modelo.ProdEmbalagem;
import br.com.opensig.produto.shared.modelo.ProdIpi;
import br.com.opensig.produto.shared.modelo.ProdOrigem;
import br.com.opensig.produto.shared.modelo.ProdProduto;
import br.com.opensig.produto.shared.modelo.ProdTipo;
import br.com.opensig.produto.shared.modelo.ProdTributacao;

public class ImportarNfe implements IImportacao<ComCompra> {

	private CoreService servico;
	private ComCompra compra;
	private List<ComCompraProduto> comProdutos;
	private List<ProdTributacao> tributacao;
	private List<ProdIpi> ipis;
	private List<ProdEmbalagem> embalagem;
	private EmpEmpresa empresa;
	private EmpFornecedor fornecedor;
	private TNFe nfe;
	private Autenticacao auth;

	public ImportarNfe() {
	}

	@Override
	public Map<String, List<ComCompra>> setArquivo(Autenticacao auth, Map<String, byte[]> arquivos, SisExpImp modo) throws OpenSigException {
		this.servico = new ComercialServiceImpl(auth);
		this.fornecedor = new EmpFornecedor();
		this.auth = auth;
		String xml = "";

		for (byte[] valor : arquivos.values()) {
			xml = new String(valor);
			break;
		}

		// valida se é NFe
		try {
			// pega a NFe
			int I = xml.indexOf("<infNFe");
			int F = xml.indexOf("</NFe>") + 6;
			String texto = "<NFe xmlns=\"http://www.portalfiscal.inf.br/nfe\">" + xml.substring(I, F);
			nfe = UtilServer.xmlToObj(texto, "br.com.opensig.nfe");
		} catch (Exception e) {
			throw new ComercialException("Não é uma NFe válida!");
		}

		// valida se tem protocolo
		try {
			Document doc = UtilServer.getXml(xml);
			UtilServer.getValorTag(doc.getDocumentElement(), "nProt", true);
		} catch (Exception e) {
			throw new ComercialException("Não tem o protocolo da Sefaz!");
		}

		// valida a empresa
		validarEmpresa();
		// valida a compra
		validarCompra();
		// valida o fornecedor
		validarFornecedor();
		// valida os produtos
		validarProduto();
		// coloca a nota
		FisNotaEntrada nota = new FisNotaEntrada();
		nota.setFisNotaEntradaXml(xml);
		compra.setFisNotaEntrada(nota);
		compra.setComCompraNfe(true);

		Map<String, List<ComCompra>> resp = new HashMap<String, List<ComCompra>>();
		List<ComCompra> lista = new ArrayList<ComCompra>();
		lista.add(getCompra());
		resp.put("ok", lista);

		return resp;
	}

	private void validarEmpresa() throws OpenSigException {
		if (nfe.getInfNFe().getDest().getCNPJ().equals(auth.getEmpresa()[5].replaceAll("\\W", ""))) {
			EmpEntidade ent = new EmpEntidade(Integer.valueOf(auth.getEmpresa()[1]));
			ent.setEmpEntidadeNome1(auth.getEmpresa()[2]);
			ent.setEmpEntidadeNome2(auth.getEmpresa()[3]);
			ent.setEmpEntidadePessoa(auth.getEmpresa()[4]);
			ent.setEmpEntidadeDocumento1(auth.getEmpresa()[5]);
			ent.setEmpEntidadeDocumento2(auth.getEmpresa()[6]);
			ent.setEmpEntidadeObservacao("");
			empresa = new EmpEmpresa(Integer.valueOf(auth.getEmpresa()[0]));
			empresa.setEmpEntidade(ent);
		} else {
			throw new ComercialException("O destinatário não é a empresa logada!");
		}
	}

	private void validarCompra() throws OpenSigException {
		Ide ide = nfe.getInfNFe().getIde();
		ICMSTot tot = nfe.getInfNFe().getTotal().getICMSTot();

		// tenta achar a compra
		FiltroObjeto fo = new FiltroObjeto("empEmpresa", ECompara.IGUAL, empresa);
		FiltroTexto ft = new FiltroTexto("empFornecedor.empEntidade.empEntidadeDocumento1", ECompara.IGUAL, nfe.getInfNFe().getEmit().getCNPJ());
		FiltroNumero fn = new FiltroNumero("comCompraNumero", ECompara.IGUAL, ide.getNNF());
		GrupoFiltro gf = new GrupoFiltro(EJuncao.E, new IFiltro[] { fo, ft, fn });

		compra = new ComCompra();
		compra = (ComCompra) servico.selecionar(compra, gf, false);
		if (compra != null) {
			throw new ComercialException("A compra já existe!");
		} else {
			// recupera os demais campos do xml
			Date dtData = null;
			try {
				dtData = new SimpleDateFormat("yyyy-MM-dd").parse(ide.getDEmi());
			} catch (ParseException e) {
				UtilServer.LOG.debug("Data invalida.");
				throw new ComercialException(auth.getConf().get("errInvalido") + " -> dEmi");
			}

			compra = new ComCompra();
			compra.setEmpEstado(getEstado(ide.getCUF()));
			compra.setComCompraEmissao(dtData);
			compra.setComCompraRecebimento(new Date());
			compra.setComCompraSerie(Integer.valueOf(ide.getSerie()));
			compra.setComCompraNumero(Integer.valueOf(ide.getNNF()));
			compra.setComCompraIcmsBase(Double.valueOf(tot.getVBC()));
			compra.setComCompraIcmssubBase(Double.valueOf(tot.getVBCST()));
			compra.setComCompraIcmsValor(Double.valueOf(tot.getVICMS()));
			compra.setComCompraIcmssubValor(Double.valueOf(tot.getVST()));
			compra.setComCompraValorFrete(Double.valueOf(tot.getVFrete()));
			compra.setComCompraValorSeguro(Double.valueOf(tot.getVSeg()));
			compra.setComCompraValorDesconto(Double.valueOf(tot.getVDesc()));
			compra.setComCompraValorIpi(Double.valueOf(tot.getVIPI()));
			compra.setComCompraValorOutros(Double.valueOf(tot.getVOutro()));
			compra.setComCompraValorProduto(Double.valueOf(tot.getVProd()));
			compra.setComCompraValorNota(Double.valueOf(tot.getVNF()));
			compra.setComCompraPaga(nfe.getInfNFe().getCobr() != null);
			compra.setFinPagar(getPagar());
			compra.setComCompraObservacao("");
		}
	}

	private EmpEstado getEstado(String ibge) throws OpenSigException {
		FiltroNumero fn = new FiltroNumero("empEstadoIbge", ECompara.IGUAL, ibge);
		EmpEstado est = (EmpEstado) servico.selecionar(new EmpEstado(), fn, true);
		return est;
	}

	private FinPagar getPagar() throws OpenSigException {
		// pagar
		FinPagar pagar = new FinPagar();
		List<FinPagamento> pagamentos = new ArrayList<FinPagamento>();
		pagar.setFinConta(new FinConta(Integer.valueOf(auth.getConf().get("conta.padrao"))));
		pagar.setFinPagamentos(pagamentos);
		pagar.setFinPagarObservacao("");
		// acha a forma de pagamento boleto
		FiltroTexto ft = new FiltroTexto("finFormaDescricao", ECompara.IGUAL, auth.getConf().get("txtBoleto"));
		FinForma forma = (FinForma) servico.selecionar(new FinForma(), ft, false);
		// caso nao encontre coloca a primeira
		if (forma == null) {
			forma = new FinForma(1);
			forma.setFinFormaDescricao(auth.getConf().get("txtDinheiro"));
		}

		if (nfe.getInfNFe().getCobr() != null) {
			List<Dup> duplicatas = nfe.getInfNFe().getCobr().getDup();
			int par = 1;
			for (Dup dup : duplicatas) {
				// data
				Date dtData = null;
				try {
					dtData = new SimpleDateFormat("yyyy-MM-dd").parse(dup.getDVenc());
				} catch (ParseException e) {
					dtData = new Date();
				}

				// parcela
				String parcela = par < 10 ? "0" + par : "" + par;
				parcela += duplicatas.size() < 10 ? "/0" + duplicatas.size() : "/" + duplicatas.size();

				// pagamentos
				FinPagamento pag = new FinPagamento();
				pag.setFinForma(forma);
				pag.setFinPagamentoDocumento(dup.getNDup());
				pag.setFinPagamentoValor(Double.valueOf(dup.getVDup()));
				pag.setFinPagamentoParcela(parcela);
				pag.setFinPagamentoCadastro(new Date());
				pag.setFinPagamentoVencimento(dtData);
				pag.setFinPagar(pagar);
				pag.setFinPagamentoStatus(auth.getConf().get("txtAberto"));
				pag.setFinPagamentoObservacao("");
				pagamentos.add(pag);
				par++;
			}
		}

		return pagar;
	}

	private void validarFornecedor() throws OpenSigException {

		// recupera o cnpj
		fornecedor = getFornecedor(nfe.getInfNFe().getEmit().getCNPJ());
		// caso seje um novo
		if (fornecedor.getEmpFornecedorId() == 0) {
			EmpresaServiceImpl<EmpFornecedor> servico = new EmpresaServiceImpl<EmpFornecedor>(auth);
			servico.salvar(fornecedor);
		}
		fornecedor.anularDependencia();
	}

	private EmpFornecedor getFornecedor(String cnpj) throws OpenSigException {
		try {
			cnpj = UtilServer.formataTexto(cnpj, "##.###.###/####-##");
			FiltroTexto ft = new FiltroTexto("empEntidade.empEntidadeDocumento1", ECompara.IGUAL, cnpj);
			fornecedor = (EmpFornecedor) servico.selecionar(fornecedor, ft, false);
		} catch (Exception e) {
			fornecedor = null;
		}

		if (fornecedor == null) {
			Emit emit = nfe.getInfNFe().getEmit();
			TEnderEmi ende = emit.getEnderEmit();

			// seta fornecedor
			EmpEntidade enti = new EmpEntidade();
			enti.setEmpEntidadeNome1(emit.getXNome());
			String fantasia = emit.getXFant() != null ? emit.getXFant() : emit.getXNome();
			if (fantasia.length() > 15) {
				fantasia = fantasia.substring(0, 15);
			}
			enti.setEmpEntidadeNome2(fantasia);
			enti.setEmpEntidadeDocumento1(cnpj);
			enti.setEmpEntidadeDocumento2(emit.getIE());
			enti.setEmpEntidadeAtivo(true);
			enti.setEmpEntidadePessoa(auth.getConf().get("txtJuridica"));
			enti.setEmpEntidadeObservacao("");

			// seta o endereco
			EmpEndereco endereco = new EmpEndereco();
			endereco.setEmpEnderecoTipo(new EmpEnderecoTipo(Integer.valueOf(auth.getConf().get("nfe.tipoendecom"))));
			endereco.setEmpEnderecoLogradouro(ende.getXLgr());
			endereco.setEmpEnderecoNumero(Integer.valueOf(ende.getNro().replaceAll("\\D", "")));
			endereco.setEmpEnderecoComplemento(ende.getXCpl() != null ? ende.getXCpl() : "");
			endereco.setEmpEnderecoBairro(ende.getXBairro());
			FiltroNumero fn = new FiltroNumero("empMunicipioIbge", ECompara.IGUAL, ende.getCMun());
			EmpMunicipio empM = (EmpMunicipio) servico.selecionar(new EmpMunicipio(), fn, false);
			endereco.setEmpMunicipio(empM);
			String cep = ende.getCEP() != null ? ende.getCEP().substring(0, 5) + "-" + ende.getCEP().substring(5) : "00000-000";
			endereco.setEmpEnderecoCep(cep);

			List<EmpEndereco> ends = new ArrayList<EmpEndereco>();
			ends.add(endereco);
			enti.setEmpEnderecos(ends);

			// seta o contato telefone
			String fone = ende.getFone() != null ? ende.getFone().substring(0, 2) + " " + ende.getFone().substring(2, 6) + "-" + ende.getFone().substring(6) : "0000-0000";
			EmpContato contato = new EmpContato();
			contato.setEmpContatoTipo(new EmpContatoTipo(Integer.valueOf(auth.getConf().get("nfe.tipoconttel"))));
			contato.setEmpContatoDescricao(fone);
			contato.setEmpContatoPessoa("");

			List<EmpContato> conts = new ArrayList<EmpContato>();
			conts.add(contato);
			enti.setEmpContatos(conts);

			fornecedor = new EmpFornecedor();
			fornecedor.setEmpEntidade(enti);
		}

		return fornecedor;
	}

	private void validarProduto() throws OpenSigException {
		// pega os tributados
		Lista<ProdTributacao> tributo = servico.selecionar(new ProdTributacao(), 0, 0, null, false);
		tributacao = tributo.getLista();

		// pega os ipis
		Lista<ProdIpi> tributoIpi = servico.selecionar(new ProdIpi(), 0, 0, null, false);
		ipis = tributoIpi.getLista();

		// pega as embalagens
		Lista<ProdEmbalagem> emb = servico.selecionar(new ProdEmbalagem(), 0, 0, null, false);
		embalagem = emb.getLista();

		// seta os tipos
		comProdutos = new ArrayList<ComCompraProduto>();
		for (Det det : nfe.getInfNFe().getDet()) {
			MyIcms myicms = getIcms(det.getImposto().getICMS());
			String ipi = "";
			String pIpi = "";
			try {
				ipi = det.getImposto().getIPI().getIPITrib().getCST();
				pIpi = det.getImposto().getIPI().getIPITrib().getPIPI();
				if (pIpi == null) {
					pIpi = "0.00";
				}
			} catch (Exception e) {
				ipi = "99";
				pIpi = "0.00";
			}

			// setando o produto da compra
			ComCompraProduto comProd = new ComCompraProduto();
			ProdProduto prod = getProduto(det.getProd(), myicms, ipi);
			comProd.setProdProduto(prod);
			comProd.setProdEmbalagem(prod.getProdEmbalagem());
			int cfop = Integer.valueOf(det.getProd().getCFOP());
			comProd.setComCompraProdutoCfop(cfop >= 5000 ? cfop - 4000 : cfop);
			comProd.setComCompraProdutoIcms(Double.valueOf(myicms.getAliquota()));
			comProd.setComCompraProdutoIpi(Double.valueOf(pIpi));
			comProd.setComCompraProdutoQuantidade(Double.valueOf(det.getProd().getQCom()));
			comProd.setComCompraProdutoValor(Double.valueOf(det.getProd().getVUnCom()));
			comProd.setComCompraProdutoTotal(Double.valueOf(det.getProd().getVProd()));
			comProd.setComCompraProdutoPreco(prod.getProdProdutoPreco());
			comProd.setComCompra(compra);
			comProdutos.add(comProd);
		}
	}

	private MyIcms getIcms(ICMS icms) {
		MyIcms myicms = new MyIcms();

		// normal
		if (icms.getICMS00() != null) {
			myicms.setAliquota(icms.getICMS00().getPICMS());
			myicms.setCst(icms.getICMS00().getCST());
			myicms.setOrigem(icms.getICMS00().getOrig());
		} else if (icms.getICMS10() != null) {
			myicms.setAliquota(icms.getICMS10().getPICMS());
			myicms.setCst(icms.getICMS10().getCST());
			myicms.setOrigem(icms.getICMS10().getOrig());
		} else if (icms.getICMS20() != null) {
			myicms.setAliquota(icms.getICMS20().getPICMS());
			myicms.setCst(icms.getICMS20().getCST());
			myicms.setOrigem(icms.getICMS20().getOrig());
		} else if (icms.getICMS30() != null) {
			myicms.setAliquota("0");
			myicms.setCst(icms.getICMS30().getCST());
			myicms.setOrigem(icms.getICMS30().getOrig());
		} else if (icms.getICMS40() != null) {
			myicms.setAliquota("0");
			myicms.setCst(icms.getICMS40().getCST());
			myicms.setOrigem(icms.getICMS40().getOrig());
		} else if (icms.getICMS51() != null) {
			myicms.setAliquota(icms.getICMS51().getPICMS());
			myicms.setCst(icms.getICMS51().getCST());
			myicms.setOrigem(icms.getICMS51().getOrig());
		} else if (icms.getICMS60() != null) {
			myicms.setAliquota("0");
			myicms.setCst(icms.getICMS60().getCST());
			myicms.setOrigem(icms.getICMS60().getOrig());
		} else if (icms.getICMS70() != null) {
			myicms.setAliquota(icms.getICMS70().getPICMS());
			myicms.setCst(icms.getICMS70().getCST());
			myicms.setOrigem(icms.getICMS70().getOrig());
		} else if (icms.getICMS90() != null) {
			myicms.setAliquota(icms.getICMS90().getPICMS());
			myicms.setCst(icms.getICMS90().getCST());
			myicms.setOrigem(icms.getICMS90().getOrig());
			// simples
		} else if (icms.getICMSSN101() != null) {
			myicms.setAliquota("0");
			myicms.setCst(icms.getICMSSN101().getCSOSN());
			myicms.setOrigem(icms.getICMSSN101().getOrig());
		} else if (icms.getICMSSN102() != null) {
			myicms.setAliquota("0");
			myicms.setCst(icms.getICMSSN102().getCSOSN());
			myicms.setOrigem(icms.getICMSSN102().getOrig());
		} else if (icms.getICMSSN201() != null) {
			myicms.setAliquota("0");
			myicms.setCst(icms.getICMSSN201().getCSOSN());
			myicms.setOrigem(icms.getICMSSN201().getOrig());
		} else if (icms.getICMSSN202() != null) {
			myicms.setAliquota("0");
			myicms.setCst(icms.getICMSSN202().getCSOSN());
			myicms.setOrigem(icms.getICMSSN202().getOrig());
		} else if (icms.getICMSSN500() != null) {
			myicms.setAliquota("0");
			myicms.setCst(icms.getICMSSN500().getCSOSN());
			myicms.setOrigem(icms.getICMSSN500().getOrig());
		} else if (icms.getICMSSN900() != null) {
			myicms.setAliquota("0");
			myicms.setCst(icms.getICMSSN900().getCSOSN());
			myicms.setOrigem(icms.getICMSSN900().getOrig());
		}

		return myicms;
	}

	private ProdProduto getProduto(Prod prod, MyIcms icms, String ipi) throws OpenSigException {
		String ean = prod.getCEAN();
		// barra
		GrupoFiltro grupoBarra = new GrupoFiltro();
		FiltroTexto ft1 = new FiltroTexto("prodProdutoBarra", ECompara.IGUAL, ean);
		grupoBarra.add(ft1, EJuncao.OU);
		// barra do preco
		FiltroTexto ft2 = new FiltroTexto("prodPrecoBarra", ECompara.IGUAL, ean);
		ft2.setCampoPrefixo("t2.");
		grupoBarra.add(ft2, EJuncao.OU);

		// busca
		ProdProduto produto = new ProdProduto();
		Lista<ProdProduto> lista = servico.selecionar(produto, 0, 1, grupoBarra, false);
		// verifica se achou
		if (lista.getTotal() == 1) {
			produto = lista.getLista().get(0);
			produto.anularDependencia();
			produto.setProdEmbalagem(getEmbalagem(prod.getUCom()));
			produto.setProdTipo(new ProdTipo(1));
			produto.setProdIpi(getIpi(ipi));
			produto.setProdOrigem(new ProdOrigem(Integer.valueOf(icms.getOrigem()) + 1));
			produto.setEmpFornecedor(fornecedor);
			produto.setEmpFabricante(fornecedor);
			produto.setProdTributacao(getTributacao(icms.getCst()));
			// caso nao acha cria um novo para confirmar
		} else {
			produto.setProdProdutoNcm(prod.getNCM());
			produto.setProdProdutoBarra(ean);
			produto.setProdProdutoReferencia(prod.getCProd());
			produto.setProdProdutoDescricao(prod.getXProd());
			produto.setProdEmbalagem(new ProdEmbalagem(1));
			produto.setProdTipo(new ProdTipo(1));
			produto.setProdProdutoVolume(1);
			produto.setProdOrigem(new ProdOrigem(Integer.valueOf(icms.getOrigem()) + 1));
			produto.setEmpFornecedor(fornecedor);
			produto.setEmpFabricante(fornecedor);
			produto.setProdTributacao(getTributacao(icms.getCst()));
			produto.setProdIpi(getIpi(ipi));
			produto.setProdProdutoAtivo(true);
			produto.setProdProdutoCategoria(auth.getConf().get("categoria.padrao") + "::");
			produto.setProdProdutoCadastrado(new Date());
			produto.setProdProdutoCusto(Double.valueOf(prod.getVUnCom()));
			produto.setProdProdutoPreco(0.00);
			produto.setProdProdutoObservacao("");
		}

		return produto;
	}

	private ProdEmbalagem getEmbalagem(String nome) {
		// se nao achar colocar a padrao UND
		ProdEmbalagem resp = new ProdEmbalagem(1);

		// percorre as embalagens
		for (ProdEmbalagem emb : embalagem) {
			if (emb.getProdEmbalagemNome().equalsIgnoreCase(nome)) {
				resp = emb;
				break;
			}
		}

		return resp;
	}

	private ProdTributacao getTributacao(String cst) {
		// se nao achar colocar a padrao 00
		ProdTributacao resp = null;

		// percorre as tributacoes
		for (ProdTributacao trib : tributacao) {
			if (cst.length() == 2 && trib.getProdTributacaoCst().equals(cst)) {
				resp = trib;
				break;
			} else if (cst.length() == 3 && trib.getProdTributacaoCson().equals(cst)) {
				resp = trib;
				break;
			} else if (trib.getProdTributacaoCst().equals("00")) {
				resp = trib;
			}
		}

		return resp;
	}

	private ProdIpi getIpi(String cst) {
		// se nao achar colocar a padrao 00
		ProdIpi resp = new ProdIpi(1);

		// percorre as tributacoes
		for (ProdIpi ipi : ipis) {
			if (ipi.getProdIpiCstSaida().equals(cst)) {
				resp = ipi;
				break;
			}
		}

		return resp;
	}

	public ComCompra getCompra() {
		compra.setEmpEmpresa(empresa);
		compra.setEmpFornecedor(fornecedor);
		compra.setComCompraProdutos(comProdutos);
		compra.getFinPagar().setEmpEmpresa(empresa);
		compra.getFinPagar().setEmpEntidade(fornecedor.getEmpEntidade());
		return compra;
	}

	private class MyIcms {

		private String aliquota;
		private String origem;
		private String cst;

		public MyIcms() {
		}

		public String getAliquota() {
			return aliquota;
		}

		public void setAliquota(String aliquota) {
			this.aliquota = aliquota;
		}

		public String getOrigem() {
			return origem;
		}

		public void setOrigem(String origem) {
			this.origem = origem;
		}

		public String getCst() {
			return cst;
		}

		public void setCst(String cst) {
			this.cst = cst;
		}
	}

}
