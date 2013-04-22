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
import br.com.opensig.comercial.shared.modelo.ComVenda;
import br.com.opensig.comercial.shared.modelo.ComVendaProduto;
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
import br.com.opensig.empresa.shared.modelo.EmpCliente;
import br.com.opensig.empresa.shared.modelo.EmpContato;
import br.com.opensig.empresa.shared.modelo.EmpContatoTipo;
import br.com.opensig.empresa.shared.modelo.EmpEndereco;
import br.com.opensig.empresa.shared.modelo.EmpEnderecoTipo;
import br.com.opensig.empresa.shared.modelo.EmpEntidade;
import br.com.opensig.empresa.shared.modelo.EmpFornecedor;
import br.com.opensig.empresa.shared.modelo.EmpMunicipio;
import br.com.opensig.financeiro.shared.modelo.FinForma;
import br.com.opensig.financeiro.shared.modelo.FinReceber;
import br.com.opensig.financeiro.shared.modelo.FinRecebimento;
import br.com.opensig.fiscal.shared.modelo.FisNotaSaida;
import br.com.opensig.nfe.TEndereco;
import br.com.opensig.nfe.TNFe.InfNFe.Cobr.Dup;
import br.com.opensig.nfe.TNFe.InfNFe.Dest;
import br.com.opensig.nfe.TNFe.InfNFe.Det;
import br.com.opensig.nfe.TNFe.InfNFe.Ide;
import br.com.opensig.nfe.TNFe.InfNFe.Total.ICMSTot;
import br.com.opensig.permissao.shared.modelo.SisUsuario;
import br.com.opensig.produto.shared.modelo.ProdEmbalagem;
import br.com.opensig.produto.shared.modelo.ProdIpi;
import br.com.opensig.produto.shared.modelo.ProdOrigem;
import br.com.opensig.produto.shared.modelo.ProdProduto;
import br.com.opensig.produto.shared.modelo.ProdTributacao;

public class ImportarVenda extends ImportarNFe<ComVenda> {

	private ComVenda venda;
	private List<ComVendaProduto> venProdutos;
	private EmpCliente cliente;

	public ImportarVenda() {
	}

	@Override
	public Map<String, List<ComVenda>> setArquivo(Autenticacao auth, Map<String, byte[]> arquivos, SisExpImp modo) throws OpenSigException {
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
		validarEmpresa(nfe.getInfNFe().getEmit().getCNPJ());
		// valida a venda
		validarVenda();
		// valida o cliente
		validarCliente();
		// valida os produtos
		validarProduto();
		// coloca a nota
		FisNotaSaida nota = new FisNotaSaida();
		nota.setFisNotaSaidaXml(xml);
		nota.setFisNotaSaidaNumero(Integer.valueOf(nfe.getInfNFe().getIde().getNNF()));
		venda.setFisNotaSaida(nota);
		venda.setComVendaNfe(true);

		Map<String, List<ComVenda>> resp = new HashMap<String, List<ComVenda>>();
		List<ComVenda> lista = new ArrayList<ComVenda>();
		lista.add(getVenda());
		resp.put("ok", lista);

		return resp;
	}

	private void validarVenda() throws OpenSigException {
		Ide ide = nfe.getInfNFe().getIde();
		ICMSTot tot = nfe.getInfNFe().getTotal().getICMSTot();

		// tenta achar a venda
		FiltroObjeto fo = new FiltroObjeto("empEmpresa", ECompara.IGUAL, empresa);
		String cnpj = UtilServer.formataTexto(nfe.getInfNFe().getDest().getCNPJ(), "##.###.###/####-##");
		FiltroTexto ft = new FiltroTexto("empCliente.empEntidade.empEntidadeDocumento1", ECompara.IGUAL, cnpj);
		FiltroNumero fn = new FiltroNumero("fisNotaSaida.fisNotaSaidaNumero", ECompara.IGUAL, ide.getNNF());
		GrupoFiltro gf = new GrupoFiltro(EJuncao.E, new IFiltro[] { fo, ft, fn });
		venda = (ComVenda) servico.selecionar(new ComVenda(), gf, false);
		
		if (venda != null) {
			throw new ComercialException("A venda já existe!");
		} else {
			// recupera os demais campos do xml
			Date dtData = null;
			try {
				dtData = new SimpleDateFormat("yyyy-MM-dd").parse(ide.getDEmi());
			} catch (ParseException e) {
				UtilServer.LOG.debug("Data invalida.");
				throw new ComercialException(auth.getConf().get("errInvalido") + " -> dEmi");
			}

			venda = new ComVenda();
			venda.setSisUsuario(new SisUsuario(Integer.valueOf(auth.getUsuario()[0])));
			venda.setComVendaData(dtData);
			venda.setComVendaValorBruto(Double.valueOf(tot.getVProd()));
			venda.setComVendaValorLiquido(Double.valueOf(tot.getVNF()));
			venda.setComVendaRecebida(nfe.getInfNFe().getCobr() != null);
			venda.setFinReceber(getReceber());
			venda.setComVendaObservacao("");
		}
	}

	private FinReceber getReceber() throws OpenSigException {
		// receber
		FinReceber receber = new FinReceber();
		List<FinRecebimento> recebimentos = new ArrayList<FinRecebimento>();
		receber.setFinRecebimentos(recebimentos);
		receber.setFinReceberObservacao("");
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

				// recebimentos
				FinRecebimento rec = new FinRecebimento();
				rec.setFinForma(forma);
				rec.setFinConta(null);
				rec.setFinRecebimentoDocumento(dup.getNDup());
				rec.setFinRecebimentoValor(Double.valueOf(dup.getVDup()));
				rec.setFinRecebimentoParcela(parcela);
				rec.setFinRecebimentoCadastro(new Date());
				rec.setFinRecebimentoVencimento(dtData);
				rec.setFinReceber(receber);
				rec.setFinRecebimentoStatus(auth.getConf().get("txtAberto"));
				rec.setFinRecebimentoObservacao("");
				recebimentos.add(rec);
				par++;
			}
		}

		return receber;
	}

	private void validarCliente() throws OpenSigException {
		// recupera o cnpj
		cliente = getCliente(nfe.getInfNFe().getDest().getCNPJ());
		// caso seje um novo
		if (cliente.getEmpClienteId() == 0) {
			EmpresaServiceImpl<EmpCliente> servico = new EmpresaServiceImpl<EmpCliente>(auth);
			servico.salvar(cliente);
		}
		cliente.anularDependencia();
	}

	private EmpCliente getCliente(String cnpj) throws OpenSigException {
		cnpj = UtilServer.formataTexto(cnpj, "##.###.###/####-##");
		FiltroTexto ft = new FiltroTexto("empEntidade.empEntidadeDocumento1", ECompara.IGUAL, cnpj);
		cliente = (EmpCliente) servico.selecionar(new EmpCliente(), ft, false);

		if (cliente == null) {
			Dest dest = nfe.getInfNFe().getDest();
			TEndereco ende = dest.getEnderDest();

			// seta cliente
			EmpEntidade enti = new EmpEntidade();
			enti.setEmpEntidadeNome1(dest.getXNome());
			String fantasia = dest.getXNome();
			if (fantasia.length() > 15) {
				fantasia = fantasia.substring(0, 15);
			}
			enti.setEmpEntidadeNome2(fantasia);
			enti.setEmpEntidadeDocumento1(cnpj);
			enti.setEmpEntidadeDocumento2(dest.getIE());
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

			cliente = new EmpCliente();
			cliente.setEmpEntidade(enti);
		}

		return cliente;
	}

	private void validarProduto() throws OpenSigException {
		// pega os dados auxiliares
		origens = servico.selecionar(new ProdOrigem(), 0, 0, null, false).getLista();
		tributacao = servico.selecionar(new ProdTributacao(), 0, 0, null, false).getLista();
		ipis = servico.selecionar(new ProdIpi(), 0, 0, null, false).getLista();
		embalagem = servico.selecionar(new ProdEmbalagem(), 0, 0, null, false).getLista();

		// o fornecedor padrao, para caso nao ache do produto
		FiltroNumero fn = new FiltroNumero("empFornecedorId", ECompara.IGUAL, 1);
		EmpFornecedor forne = (EmpFornecedor) servico.selecionar(new EmpFornecedor(), fn, false);
		forne.getEmpEntidade().anularDependencia();

		// seta os tipos
		venProdutos = new ArrayList<ComVendaProduto>();
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

			// setando o produto da venda
			ComVendaProduto venProd = new ComVendaProduto();
			ProdProduto prod = getProduto(forne, det.getProd(), myicms, ipi);
			venProd.setProdProduto(prod);
			venProd.setProdEmbalagem(prod.getProdEmbalagem());
			venProd.setComVenda(venda);
			venProd.setComVendaProdutoIcms(Double.valueOf(myicms.getAliquota()));
			venProd.setComVendaProdutoIpi(Double.valueOf(pIpi));
			venProd.setComVendaProdutoQuantidade(Double.valueOf(det.getProd().getQCom()));
			venProd.setComVendaProdutoBruto(Double.valueOf(det.getProd().getVUnCom()));
			venProd.setComVendaProdutoDesconto(det.getProd().getVDesc() == null ? 0.00 : Double.valueOf(det.getProd().getVDesc()));
			venProd.setComVendaProdutoLiquido(venProd.getComVendaProdutoBruto() - venProd.getComVendaProdutoDesconto());
			venProd.setComVendaProdutoTotalBruto(venProd.getComVendaProdutoQuantidade() * venProd.getComVendaProdutoBruto());
			venProd.setComVendaProdutoTotalLiquido(venProd.getComVendaProdutoQuantidade() * venProd.getComVendaProdutoLiquido());
			venProd.setComVendaProdutoBarra(prod.getProdProdutoBarra());
			venProd.setComVendaProdutoOrdem(Integer.valueOf(det.getNItem()));
			venProdutos.add(venProd);
		}
	}

	public ComVenda getVenda() {
		venda.setEmpEmpresa(empresa);
		venda.setEmpCliente(cliente);
		venda.setComVendaProdutos(venProdutos);
		venda.getFinReceber().setEmpEmpresa(empresa);
		venda.getFinReceber().setEmpEntidade(cliente.getEmpEntidade());
		return venda;
	}

}
