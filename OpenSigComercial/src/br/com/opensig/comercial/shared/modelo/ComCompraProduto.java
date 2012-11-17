package br.com.opensig.comercial.shared.modelo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.com.opensig.core.client.UtilClient;
import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.produto.shared.modelo.ProdEmbalagem;
import br.com.opensig.produto.shared.modelo.ProdProduto;

/**
 * Classe que representa um produto da compra no sistema.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 * @since 07/08/2009
 */
@Entity
@Table(name = "com_compra_produto")
public class ComCompraProduto extends Dados implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "com_compra_produto_id")
	private int comCompraProdutoId;

	@Column(name = "com_compra_produto_cfop")
	private int comCompraProdutoCfop;

	@Column(name = "com_compra_produto_icms")
	private Double comCompraProdutoIcms;

	@Column(name = "com_compra_produto_ipi")
	private Double comCompraProdutoIpi;

	@Column(name = "com_compra_produto_preco")
	private Double comCompraProdutoPreco;

	@Column(name = "com_compra_produto_quantidade")
	private Double comCompraProdutoQuantidade;

	@Column(name = "com_compra_produto_total")
	private Double comCompraProdutoTotal;

	@Column(name = "com_compra_produto_valor")
	private Double comCompraProdutoValor;

	@Column(name = "com_compra_produto_ordem")
	private int comCompraProdutoOrdem;

	@JoinColumn(name = "prod_produto_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private ProdProduto prodProduto;

	@JoinColumn(name = "prod_embalagem_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private ProdEmbalagem prodEmbalagem;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "com_compra_id")
	private ComCompra comCompra;

	public ComCompraProduto() {
		this(0);
	}

	public ComCompraProduto(int comCompraProdutoId) {
		super("pu_comercial", "ComCompraProduto", "comCompraProdutoId", "comCompraProdutoOrdem");
		this.comCompraProdutoId = comCompraProdutoId;
	}

	public int getComCompraProdutoId() {
		return this.comCompraProdutoId;
	}

	public void setComCompraProdutoId(int comCompraProdutoId) {
		this.comCompraProdutoId = comCompraProdutoId;
	}

	public double getComCompraProdutoIcms() {
		return this.comCompraProdutoIcms;
	}

	public void setComCompraProdutoIcms(Double comCompraProdutoIcms) {
		this.comCompraProdutoIcms = comCompraProdutoIcms;
	}

	public double getComCompraProdutoIpi() {
		return this.comCompraProdutoIpi;
	}

	public void setComCompraProdutoIpi(Double comCompraProdutoIpi) {
		this.comCompraProdutoIpi = comCompraProdutoIpi;
	}

	public int getComCompraProdutoCfop() {
		return comCompraProdutoCfop;
	}

	public void setComCompraProdutoCfop(int comCompraProdutoCfop) {
		this.comCompraProdutoCfop = comCompraProdutoCfop;
	}

	public double getComCompraProdutoPreco() {
		return this.comCompraProdutoPreco;
	}

	public void setComCompraProdutoPreco(Double comCompraProdutoPreco) {
		this.comCompraProdutoPreco = comCompraProdutoPreco;
	}

	public Double getComCompraProdutoQuantidade() {
		return this.comCompraProdutoQuantidade;
	}

	public void setComCompraProdutoQuantidade(Double comCompraProdutoQuantidade) {
		this.comCompraProdutoQuantidade = comCompraProdutoQuantidade;
	}

	public double getComCompraProdutoTotal() {
		return this.comCompraProdutoTotal;
	}

	public void setComCompraProdutoTotal(Double comCompraProdutoTotal) {
		this.comCompraProdutoTotal = comCompraProdutoTotal;
	}

	public double getComCompraProdutoValor() {
		return this.comCompraProdutoValor;
	}

	public void setComCompraProdutoValor(Double comCompraProdutoValor) {
		this.comCompraProdutoValor = comCompraProdutoValor;
	}

	public int getComCompraProdutoOrdem() {
		return comCompraProdutoOrdem;
	}

	public void setComCompraProdutoOrdem(int comCompraProdutoOrdem) {
		this.comCompraProdutoOrdem = comCompraProdutoOrdem;
	}

	public ProdProduto getProdProduto() {
		return this.prodProduto;
	}

	public void setProdProduto(ProdProduto prodProduto) {
		this.prodProduto = prodProduto;
	}

	public ProdEmbalagem getProdEmbalagem() {
		return prodEmbalagem;
	}

	public void setProdEmbalagem(ProdEmbalagem prodEmbalagem) {
		this.prodEmbalagem = prodEmbalagem;
	}

	public ComCompra getComCompra() {
		return this.comCompra;
	}

	public void setComCompra(ComCompra comCompra) {
		this.comCompra = comCompra;
	}

	public Number getId() {
		return comCompraProdutoId;
	}

	public void setId(Number id) {
		comCompraProdutoId = id.intValue();
	}

	public String[] toArray() {
		return new String[] { comCompraProdutoId + "", comCompra.getComCompraId() + "", comCompra.getEmpEmpresa().getEmpEmpresaId() + "", comCompra.getEmpEmpresa().getEmpEntidade().getEmpEntidadeNome1(),
				prodProduto.getEmpFornecedor().getEmpEntidade().getEmpEntidadeNome1(), prodProduto.getProdProdutoId() + "", prodProduto.getProdProdutoBarra(), prodProduto.getProdProdutoDescricao(),
				prodProduto.getProdProdutoReferencia(), prodProduto.getProdTributacao().getProdTributacaoDentro() + "", prodProduto.getProdTributacao().getProdTributacaoCst(),
				UtilClient.getDataGrid(comCompra.getComCompraRecebimento()), comCompraProdutoQuantidade + "", prodEmbalagem.getProdEmbalagemId() + "", prodEmbalagem.getProdEmbalagemNome(),
				comCompraProdutoValor.toString(), comCompraProdutoTotal.toString(), comCompraProdutoCfop + "", comCompraProdutoIcms + "", comCompraProdutoIpi + "", comCompraProdutoPreco.toString(),
				prodProduto.getProdProdutoIncentivo() + "", comCompraProdutoOrdem + "" };
	}

	public Dados getObjeto(String campo) {
		if (campo.startsWith("comCompra")) {
			return new ComCompra();
		} else if (campo.startsWith("prodProduto")) {
			return new ProdProduto();
		} else if (campo.startsWith("prodEmbalagem")) {
			return new ProdEmbalagem();
		} else {
			return null;
		}
	}

	public void anularDependencia() {
		comCompra = null;
		prodProduto = null;
		prodEmbalagem = null;
	}
}