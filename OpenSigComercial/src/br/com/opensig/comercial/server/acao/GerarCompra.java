package br.com.opensig.comercial.server.acao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.opensig.comercial.client.servico.ComercialException;
import br.com.opensig.comercial.shared.modelo.ComCompra;
import br.com.opensig.comercial.shared.modelo.ComCompraProduto;
import br.com.opensig.comercial.shared.modelo.ComNatureza;
import br.com.opensig.comercial.shared.modelo.ComTroca;
import br.com.opensig.comercial.shared.modelo.ComTrocaProduto;
import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.FiltroNumero;
import br.com.opensig.core.client.controlador.filtro.FiltroTexto;
import br.com.opensig.core.client.controlador.filtro.IFiltro;
import br.com.opensig.core.client.controlador.parametro.GrupoParametro;
import br.com.opensig.core.client.controlador.parametro.IParametro;
import br.com.opensig.core.client.controlador.parametro.ParametroBinario;
import br.com.opensig.core.client.controlador.parametro.ParametroObjeto;
import br.com.opensig.core.client.padroes.Chain;
import br.com.opensig.core.client.servico.OpenSigException;
import br.com.opensig.core.server.CoreServiceImpl;
import br.com.opensig.core.server.UtilServer;
import br.com.opensig.core.shared.modelo.Autenticacao;
import br.com.opensig.core.shared.modelo.EComando;
import br.com.opensig.core.shared.modelo.Lista;
import br.com.opensig.core.shared.modelo.Sql;
import br.com.opensig.empresa.shared.modelo.EmpEmpresa;
import br.com.opensig.empresa.shared.modelo.EmpFornecedor;
import br.com.opensig.produto.shared.modelo.ProdProduto;

public class GerarCompra extends Chain {

	private CoreServiceImpl servico;
	private IFiltro filtro;
	private ComCompra compra;
	private Autenticacao auth;

	public GerarCompra(Chain next, CoreServiceImpl servico, IFiltro filtro, Autenticacao auth) throws OpenSigException {
		super(next);
		this.servico = servico;
		this.filtro = filtro;
		this.compra = new ComCompra();
		this.auth = auth;
	}

	@Override
	public void execute() throws OpenSigException {
		try {
			// seleciona todas as trocas marcadas pelo filtro
			Lista<ComTroca> trocas = servico.selecionar(new ComTroca(), 0, 0, filtro, false);
			List<ComCompraProduto> produtos = new ArrayList<ComCompraProduto>();

			// objetos
			FiltroNumero fn = new FiltroNumero("empEmpresaId", ECompara.IGUAL, auth.getEmpresa()[0]);
			EmpEmpresa empresa = (EmpEmpresa) servico.selecionar(new EmpEmpresa(), fn, false);

			FiltroTexto ft = new FiltroTexto("empEntidade.empEntidadeDocumento1", ECompara.IGUAL, empresa.getEmpEntidade().getEmpEntidadeDocumento1());
			EmpFornecedor fornecedor = (EmpFornecedor) servico.selecionar(new EmpFornecedor(), ft, false);
			if (fornecedor == null) {
				throw new Exception("Cadastre uma fornecedor com os mesmos dados da empresa.");
			}

			FiltroNumero fn1 = new FiltroNumero("comNaturezaCfopTrib", ECompara.IGUAL, 1202);
			ComNatureza natureza = (ComNatureza) servico.selecionar(new ComNatureza(), fn1, false);
			if (natureza == null) {
				throw new Exception("Cadastre uma natureza de operação com cfop 1202.");
			}

			// variaveis
			StringBuilder sb = new StringBuilder("CNPJ/CPF - ");
			double valProd = 0.00;
			double baseIcms = 0.00;
			double valorIcms = 0.00;
			int ordem = 1;

			// percorre as trocas e seus produtos
			for (ComTroca troca : trocas.getLista()) {
				sb.append("[").append(troca.getComTrocaCliente().replaceAll("\\D", "")).append("]");
				for (ComTrocaProduto tp : troca.getComTrocaProdutos()) {
					ProdProduto pp = tp.getProdProduto();
					if (natureza.getComNaturezaIcms() && pp.getProdIcms().getProdIcmsCst().equals("00")) {
						baseIcms += tp.getComTrocaProdutoTotal();
						valorIcms += tp.getComTrocaProdutoTotal() * pp.getProdIcms().getProdIcmsDentro() / 100;
					}
					valProd += tp.getComTrocaProdutoTotal();

					// cria os produtos da compra
					ComCompraProduto cp = new ComCompraProduto();
					cp.setComCompra(null);
					cp.setProdProduto(pp);
					cp.setProdEmbalagem(tp.getProdEmbalagem());
					cp.setComCompraProdutoQuantidade(tp.getComTrocaProdutoQuantidade());
					cp.setComCompraProdutoValor(tp.getComTrocaProdutoValor());
					cp.setComCompraProdutoTotal(tp.getComTrocaProdutoTotal());
					cp.setComCompraProdutoCfop(1202);
					cp.setComCompraProdutoIcmsCst(auth.getConf().get("nfe.crt").equals("1") ? pp.getProdIcms().getProdIcmsCson() : pp.getProdIcms().getProdIcmsCst());
					cp.setComCompraProdutoIcms(natureza.getComNaturezaIcms() ? pp.getProdIcms().getProdIcmsDentro() : 0.00);
					cp.setComCompraProdutoIpiCst(pp.getProdIpi().getProdIpiCstEntrada());
					cp.setComCompraProdutoIpi(natureza.getComNaturezaIpi() ? pp.getProdIpi().getProdIpiAliquota() : 0.00);
					cp.setComCompraProdutoPisCst(pp.getProdPis().getProdPisCstEntrada());
					cp.setComCompraProdutoPis(natureza.getComNaturezaPis() ? pp.getProdPis().getProdPisAliquota() : 0.00);
					cp.setComCompraProdutoCofinsCst(pp.getProdCofins().getProdCofinsCstEntrada());
					cp.setComCompraProdutoCofins(natureza.getComNaturezaCofins() ? pp.getProdCofins().getProdCofinsAliquota() : 0.00);
					cp.setComCompraProdutoPreco(tp.getComTrocaProdutoValor());
					cp.setComCompraProdutoOrdem(ordem++);
					produtos.add(cp);
				}
			}

			// cria o objeto da compra
			compra.setEmpFornecedor(fornecedor);
			compra.setEmpEmpresa(empresa);
			compra.setEmpEstado(empresa.getEmpEntidade().getEmpEnderecos().get(0).getEmpMunicipio().getEmpEstado());
			compra.setComNatureza(natureza);
			compra.setFinPagar(null);
			compra.setFisNotaEntrada(null);
			compra.setComCompraProdutos(produtos);
			compra.setComCompraSerie(0);
			compra.setComCompraNumero(0);
			compra.setComCompraEmissao(new Date());
			compra.setComCompraRecebimento(new Date());
			compra.setComCompraIcmsBase(baseIcms);
			compra.setComCompraIcmsValor(valorIcms);
			compra.setComCompraIcmssubBase(0.00);
			compra.setComCompraIcmssubValor(0.00);
			compra.setComCompraValorProduto(valProd);
			compra.setComCompraValorFrete(0.00);
			compra.setComCompraValorSeguro(0.00);
			compra.setComCompraValorDesconto(0.00);
			compra.setComCompraValorIpi(0.00);
			compra.setComCompraValorOutros(0.00);
			compra.setComCompraValorNota(valProd);
			compra.setComCompraFechada(false);
			compra.setComCompraPaga(false);
			compra.setComCompraNfe(false);
			compra.setComCompraObservacao(sb.toString());

			// salva a compra
			SalvarCompra salvar = new SalvarCompra(next, servico, compra);
			salvar.execute();
			compra = salvar.getCompra();

			// atualiza as trocas para ativo = false e vincula a compra
			ParametroBinario pb = new ParametroBinario("comTrocaAtivo", 0);
			ParametroObjeto po = new ParametroObjeto("comCompra", compra);
			GrupoParametro gp = new GrupoParametro(new IParametro[] { pb, po });
			Sql sql = new Sql(new ComTroca(), EComando.ATUALIZAR, filtro, gp);
			servico.executar(new Sql[] { sql });
		} catch (Exception ex) {
			UtilServer.LOG.error("Erro na geracao da compra.", ex);
			throw new ComercialException(ex.getMessage());
		}

	}
}
