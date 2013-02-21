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
import br.com.opensig.core.client.servico.OpenSigException;
import br.com.opensig.core.server.UtilServer;
import br.com.opensig.core.shared.modelo.Autenticacao;
import br.com.opensig.core.shared.modelo.sistema.SisExpImp;
import br.com.opensig.empresa.server.EmpresaServiceImpl;
import br.com.opensig.empresa.shared.modelo.EmpContato;
import br.com.opensig.empresa.shared.modelo.EmpContatoTipo;
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
import br.com.opensig.nfe.TNFe.InfNFe.Cobr.Dup;
import br.com.opensig.nfe.TNFe.InfNFe.Det;
import br.com.opensig.nfe.TNFe.InfNFe.Emit;
import br.com.opensig.nfe.TNFe.InfNFe.Ide;
import br.com.opensig.nfe.TNFe.InfNFe.Total.ICMSTot;
import br.com.opensig.produto.shared.modelo.ProdEmbalagem;
import br.com.opensig.produto.shared.modelo.ProdIpi;
import br.com.opensig.produto.shared.modelo.ProdOrigem;
import br.com.opensig.produto.shared.modelo.ProdProduto;
import br.com.opensig.produto.shared.modelo.ProdTributacao;

public class ImportarCompra extends ImportarNFe<ComCompra> {

	private ComCompra compra;
	private List<ComCompraProduto> comProdutos;
	private EmpFornecedor fornecedor;

	public ImportarCompra() {
	}

	@Override
	public Map<String, List<ComCompra>> setArquivo(Autenticacao auth, Map<String, byte[]> arquivos, SisExpImp modo) throws OpenSigException {
		this.servico = new ComercialServiceImpl(auth);
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
		validarEmpresa(nfe.getInfNFe().getDest().getCNPJ());
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

	private void validarCompra() throws OpenSigException {
		Ide ide = nfe.getInfNFe().getIde();
		ICMSTot tot = nfe.getInfNFe().getTotal().getICMSTot();

		// tenta achar a compra
		FiltroObjeto fo = new FiltroObjeto("empEmpresa", ECompara.IGUAL, empresa);
		String cnpj = UtilServer.formataTexto(nfe.getInfNFe().getEmit().getCNPJ(), "##.###.###/####-##");
		FiltroTexto ft = new FiltroTexto("empFornecedor.empEntidade.empEntidadeDocumento1", ECompara.IGUAL, cnpj);
		FiltroNumero fn = new FiltroNumero("comCompraNumero", ECompara.IGUAL, ide.getNNF());
		GrupoFiltro gf = new GrupoFiltro(EJuncao.E, new IFiltro[] { fo, ft, fn });
		compra = (ComCompra) servico.selecionar(new ComCompra(), gf, false);
		
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
			compra.setComCompraObservacao(nfe.getInfNFe().getInfAdic().getInfCpl() == null ? "" : nfe.getInfNFe().getInfAdic().getInfCpl());
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
		cnpj = UtilServer.formataTexto(cnpj, "##.###.###/####-##");
		FiltroTexto ft = new FiltroTexto("empEntidade.empEntidadeDocumento1", ECompara.IGUAL, cnpj);
		fornecedor = (EmpFornecedor) servico.selecionar(new EmpFornecedor(), ft, false);

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
			enti.setEmpEntidadeDocumento3("ISENTO");
			enti.setEmpEntidadeAtivo(true);
			enti.setEmpEntidadePessoa(auth.getConf().get("txtJuridica"));
			enti.setEmpEntidadeObservacao("");
			enti.setEmpEntidadeData(new Date());

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
		// pega os dados auxiliares
		origens = servico.selecionar(new ProdOrigem(), 0, 0, null, false).getLista();
		tributacao = servico.selecionar(new ProdTributacao(), 0, 0, null, false).getLista();
		ipis = servico.selecionar(new ProdIpi(), 0, 0, null, false).getLista();
		embalagem = servico.selecionar(new ProdEmbalagem(), 0, 0, null, false).getLista();

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
			ProdProduto prod = getProduto(fornecedor, det.getProd(), myicms, ipi);
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
			comProd.setComCompraProdutoOrdem(Integer.valueOf(det.getNItem()));
			comProd.setComCompra(compra);
			comProdutos.add(comProd);
		}
	}

	public ComCompra getCompra() {
		compra.setEmpEmpresa(empresa);
		compra.setEmpFornecedor(fornecedor);
		compra.setComCompraProdutos(comProdutos);
		compra.getFinPagar().setEmpEmpresa(empresa);
		compra.getFinPagar().setEmpEntidade(fornecedor.getEmpEntidade());
		return compra;
	}

}
