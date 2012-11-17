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
 * Classe que representa um produto da venda no sistema.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 * @since 07/08/2009
 */
@Entity
@Table(name = "com_venda_produto")
public class ComVendaProduto extends Dados implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "com_venda_produto_id")
	private int comVendaProdutoId;

	@Column(name = "com_venda_produto_quantidade")
	private Double comVendaProdutoQuantidade;

	@Column(name = "com_venda_produto_bruto")
	private Double comVendaProdutoBruto;

	@Column(name = "com_venda_produto_desconto")
	private Double comVendaProdutoDesconto;

	@Column(name = "com_venda_produto_liquido")
	private Double comVendaProdutoLiquido;

	@Column(name = "com_venda_produto_total_bruto")
	private Double comVendaProdutoTotalBruto;

	@Column(name = "com_venda_produto_total_liquido")
	private Double comVendaProdutoTotalLiquido;

	@Column(name = "com_venda_produto_icms")
	private Double comVendaProdutoIcms;

	@Column(name = "com_venda_produto_ipi")
	private Double comVendaProdutoIpi;

	@Column(name = "com_venda_produto_ordem")
	private int comVendaProdutoOrdem;

	@JoinColumn(name = "prod_produto_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private ProdProduto prodProduto;

	@JoinColumn(name = "prod_embalagem_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private ProdEmbalagem prodEmbalagem;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "com_venda_id")
	private ComVenda comVenda;

	public ComVendaProduto() {
		this(0);
	}

	public ComVendaProduto(int comVendaProdutoId) {
		super("pu_comercial", "ComVendaProduto", "comVendaProdutoId", "comVendaProdutoOrdem");
		this.comVendaProdutoId = comVendaProdutoId;
	}

	public int getComVendaProdutoId() {
		return comVendaProdutoId;
	}

	public void setComVendaProdutoId(int comVendaProdutoId) {
		this.comVendaProdutoId = comVendaProdutoId;
	}

	public Double getComVendaProdutoQuantidade() {
		return comVendaProdutoQuantidade;
	}

	public void setComVendaProdutoQuantidade(Double comVendaProdutoQuantidade) {
		this.comVendaProdutoQuantidade = comVendaProdutoQuantidade;
	}

	public Double getComVendaProdutoBruto() {
		return comVendaProdutoBruto;
	}

	public void setComVendaProdutoBruto(Double comVendaProdutoBruto) {
		this.comVendaProdutoBruto = comVendaProdutoBruto;
	}

	public Double getComVendaProdutoDesconto() {
		return comVendaProdutoDesconto;
	}

	public void setComVendaProdutoDesconto(Double comVendaProdutoDesconto) {
		this.comVendaProdutoDesconto = comVendaProdutoDesconto;
	}

	public Double getComVendaProdutoLiquido() {
		return comVendaProdutoLiquido;
	}

	public void setComVendaProdutoLiquido(Double comVendaProdutoLiquido) {
		this.comVendaProdutoLiquido = comVendaProdutoLiquido;
	}

	public Double getComVendaProdutoTotalBruto() {
		return comVendaProdutoTotalBruto;
	}

	public void setComVendaProdutoTotalBruto(Double comVendaProdutoTotalBruto) {
		this.comVendaProdutoTotalBruto = comVendaProdutoTotalBruto;
	}

	public Double getComVendaProdutoTotalLiquido() {
		return comVendaProdutoTotalLiquido;
	}

	public void setComVendaProdutoTotalLiquido(Double comVendaProdutoTotalLiquido) {
		this.comVendaProdutoTotalLiquido = comVendaProdutoTotalLiquido;
	}

	public double getComVendaProdutoIcms() {
		return comVendaProdutoIcms;
	}

	public void setComVendaProdutoIcms(Double comVendaProdutoIcms) {
		this.comVendaProdutoIcms = comVendaProdutoIcms;
	}

	public double getComVendaProdutoIpi() {
		return comVendaProdutoIpi;
	}

	public void setComVendaProdutoIpi(Double comVendaProdutoIpi) {
		this.comVendaProdutoIpi = comVendaProdutoIpi;
	}

	public ProdProduto getProdProduto() {
		return prodProduto;
	}

	public int getComVendaProdutoOrdem() {
		return comVendaProdutoOrdem;
	}

	public void setComVendaProdutoOrdem(int comVendaProdutoOrdem) {
		this.comVendaProdutoOrdem = comVendaProdutoOrdem;
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

	public ComVenda getComVenda() {
		return comVenda;
	}

	public void setComVenda(ComVenda comVenda) {
		this.comVenda = comVenda;
	}

	public Number getId() {
		return comVendaProdutoId;
	}

	public void setId(Number id) {
		comVendaProdutoId = id.intValue();
	}

	public String[] toArray() {
		return new String[] { comVendaProdutoId + "", comVenda.getComVendaId() + "", comVenda.getEmpEmpresa().getEmpEmpresaId() + "", comVenda.getEmpEmpresa().getEmpEntidade().getEmpEntidadeNome1(),
				comVenda.getEmpCliente().getEmpEntidade().getEmpEntidadeNome1(), prodProduto.getEmpFornecedor().getEmpEntidade().getEmpEntidadeNome1(), prodProduto.getProdProdutoId() + "",
				prodProduto.getProdProdutoBarra(), prodProduto.getProdProdutoDescricao(), prodProduto.getProdProdutoReferencia(), UtilClient.getDataGrid(comVenda.getComVendaData()),
				comVendaProdutoQuantidade + "", prodEmbalagem.getProdEmbalagemId() + "", prodEmbalagem.getProdEmbalagemNome(), comVendaProdutoBruto.toString(), comVendaProdutoDesconto.toString(),
				comVendaProdutoLiquido.toString(), comVendaProdutoTotalBruto.toString(), comVendaProdutoTotalLiquido.toString(), "0", prodProduto.getProdOrigem().getProdOrigemId() + "",
				comVendaProdutoIcms + "", comVendaProdutoIpi + "", comVendaProdutoOrdem + "" };
	}

	public Dados getObjeto(String campo) {
		if (campo.startsWith("comVenda")) {
			return new ComVenda();
		} else if (campo.startsWith("prodProduto")) {
			return new ProdProduto();
		} else if (campo.startsWith("prodEmbalagem")) {
			return new ProdEmbalagem();
		} else {
			return null;
		}
	}

	public void anularDependencia() {
		comVenda = null;
		prodProduto = null;
		prodEmbalagem = null;
	}
}
