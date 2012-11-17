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
 * Classe que representa um preço no sistema.
 * 
 * @author Pedro H. Lira
 */
@Entity
@Table(name = "prod_preco")
@XmlRootElement
public class ProdPreco extends Dados implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "prod_preco_id")
	private int prodPrecoId;

	@Column(name = "prod_preco_valor")
	private double prodPrecoValor;

	@Column(name = "prod_preco_barra")
	private String prodPrecoBarra;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "prod_produto_id")
	@XmlInverseReference(mappedBy = "prodPrecos")
	private ProdProduto prodProduto;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "prod_embalagem_id")
	private ProdEmbalagem prodEmbalagem;

	public ProdPreco() {
		this(0);
	}

	public ProdPreco(int prodPrecoId) {
		super("pu_produto", "ProdPreco", "prodPrecoId", "prodPrecoValor");
		this.prodPrecoId = prodPrecoId;
	}

	public int getProdPrecoId() {
		return this.prodPrecoId;
	}

	public void setProdPrecoId(int prodPrecoId) {
		this.prodPrecoId = prodPrecoId;
	}

	public double getProdPrecoValor() {
		return this.prodPrecoValor;
	}

	public void setProdPrecoValor(double prodPrecoValor) {
		this.prodPrecoValor = prodPrecoValor;
	}

	public String getProdPrecoBarra() {
		return prodPrecoBarra;
	}

	public void setProdPrecoBarra(String prodPrecoBarra) {
		this.prodPrecoBarra = prodPrecoBarra;
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
		return prodPrecoId;
	}

	public void setId(Number id) {
		prodPrecoId = id.intValue();
	}

	public String[] toArray() {
		return new String[] { prodPrecoId + "", prodEmbalagem.getProdEmbalagemId() + "", prodEmbalagem.getProdEmbalagemNome(), prodPrecoValor + "", prodPrecoBarra };
	}

	public void anularDependencia() {
		prodProduto = null;
		prodEmbalagem = null;
	}
}