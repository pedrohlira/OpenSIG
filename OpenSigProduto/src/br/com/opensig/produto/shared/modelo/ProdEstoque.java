package br.com.opensig.produto.shared.modelo;

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

import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.core.shared.modelo.EDirecao;
import br.com.opensig.empresa.shared.modelo.EmpEmpresa;

/**
 * Classe que representa um estoque de produtos no sistema.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 * @since 27/05/2010
 */
@Entity
@Table(name = "prod_estoque")
public class ProdEstoque extends Dados implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "prod_estoque_id")
	private int prodEstoqueId;

	@Column(name = "prod_estoque_quantidade")
	private Double prodEstoqueQuantidade;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "prod_produto_id")
	private ProdProduto prodProduto;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "emp_empresa_id")
	private EmpEmpresa empEmpresa;

	public ProdEstoque() {
		this(0);
	}

	public ProdEstoque(int prodEstoqueId) {
		super("pu_produto", "ProdEstoque", "prodEstoqueId", "prodEstoqueQuantidade", EDirecao.DESC);
		this.prodEstoqueId = prodEstoqueId;
	}

	public int getProdEstoqueId() {
		return this.prodEstoqueId;
	}

	public void setProdEstoqueId(int prodEstoqueId) {
		this.prodEstoqueId = prodEstoqueId;
	}

	public Double getProdEstoqueQuantidade() {
		return this.prodEstoqueQuantidade;
	}

	public void setProdEstoqueQuantidade(Double prodEstoqueQuantidade) {
		this.prodEstoqueQuantidade = prodEstoqueQuantidade;
	}

	public ProdProduto getProdProduto() {
		return prodProduto;
	}

	public void setProdProduto(ProdProduto prodProduto) {
		this.prodProduto = prodProduto;
	}

	public EmpEmpresa getEmpEmpresa() {
		return empEmpresa;
	}

	public void setEmpEmpresa(EmpEmpresa empEmpresa) {
		this.empEmpresa = empEmpresa;
	}

	public Number getId() {
		return prodEstoqueId;
	}

	public void setId(Number id) {
		prodEstoqueId = id.intValue();
	}

	public String[] toArray() {
		return new String[] { prodEstoqueId + "", empEmpresa.getEmpEmpresaId() + "", prodProduto.getProdProdutoId() + "", prodEstoqueQuantidade.toString()};
	}

	public void anularDependencia() {
		prodProduto = null;
		empEmpresa = null;
	}
}