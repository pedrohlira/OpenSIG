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
import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.persistence.oxm.annotations.XmlInverseReference;

import br.com.opensig.core.shared.modelo.Dados;

/**
 * Classe que representa uma composicao de produtos no sistema.
 * 
 * @author Pedro H. Lira
 */
@Entity
@Table(name = "prod_composicao")
@XmlRootElement
public class ProdComposicao extends Dados implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "prod_composicao_id")
	private int prodComposicaoId;

	@Column(name = "prod_composicao_quantidade")
	private double prodComposicaoQuantidade;

	@Column(name = "prod_composicao_valor")
	private double prodComposicaoValor;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "prod_produto_principal")
	@XmlInverseReference(mappedBy = "prodComposicoes")
	private ProdProduto prodProdutoPrincipal;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "prod_produto_id")
	private ProdProduto prodProduto;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "prod_embalagem_id")
	private ProdEmbalagem prodEmbalagem;

	public ProdComposicao() {
		this(0);
	}

	public ProdComposicao(int prodComposicaoId) {
		super("pu_produto", "ProdComposicao", "prodComposicaoId", "prodComposicaoValor");
		this.prodComposicaoId = prodComposicaoId;
	}

	public int getProdComposicaoId() {
		return prodComposicaoId;
	}

	public void setProdComposicaoId(int prodComposicaoId) {
		this.prodComposicaoId = prodComposicaoId;
	}

	public double getProdComposicaoQuantidade() {
		return prodComposicaoQuantidade;
	}

	public void setProdComposicaoQuantidade(double prodComposicaoQuantidade) {
		this.prodComposicaoQuantidade = prodComposicaoQuantidade;
	}

	public double getProdComposicaoValor() {
		return prodComposicaoValor;
	}

	public void setProdComposicaoValor(double prodComposicaoValor) {
		this.prodComposicaoValor = prodComposicaoValor;
	}

	public ProdProduto getProdProdutoPrincipal() {
		return prodProdutoPrincipal;
	}

	public void setProdProdutoPrincipal(ProdProduto prodProdutoPrincipal) {
		this.prodProdutoPrincipal = prodProdutoPrincipal;
	}

	public ProdProduto getProdProduto() {
		return this.prodProduto;
	}

	public void setProdProduto(ProdProduto prodProduto) {
		this.prodProduto = prodProduto;
	}

	public ProdEmbalagem getProdEmbalagem() {
		return this.prodEmbalagem;
	}

	public void setProdEmbalagem(ProdEmbalagem prodEmbalagem) {
		this.prodEmbalagem = prodEmbalagem;
	}

	public Number getId() {
		return prodComposicaoId;
	}

	public void setId(Number id) {
		prodComposicaoId = id.intValue();
	}

	public String[] toArray() {
		return new String[] { prodComposicaoId + "", prodProduto.getProdProdutoId() + "", prodProduto.getProdProdutoDescricao(), prodEmbalagem.getProdEmbalagemId() + "",
				prodEmbalagem.getProdEmbalagemNome(), prodComposicaoQuantidade + "", prodComposicaoValor + "" };
	}

	public void anularDependencia() {
		prodProdutoPrincipal = null;
		prodProduto = null;
		prodEmbalagem = null;
	}
}