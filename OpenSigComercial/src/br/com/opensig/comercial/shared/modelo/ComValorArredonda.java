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

import br.com.opensig.core.shared.modelo.Dados;

/**
 * Classe que representa um valor de arredondamento no sistema.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 * @since 07/08/2009
 */
@Entity
@Table(name = "com_valor_arredonda")
public class ComValorArredonda extends Dados implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "com_valor_arredonda_id")
	private int comValorArredondaId;

	@Column(name = "com_valor_arredonda_fixo")
	private Double comValorArredondaFixo;

	@Column(name = "com_valor_arredonda_max")
	private Double comValorArredondaMax;

	@Column(name = "com_valor_arredonda_min")
	private Double comValorArredondaMin;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "com_valor_produto_id")
	private ComValorProduto comValorProduto;

	public ComValorArredonda() {
		this(0);
	}

	public ComValorArredonda(int comValorArredondaId) {
		super("pu_comercial", "ComValorArredonda", "comValorArredondaId", "comValorArredondaMin");
		this.comValorArredondaId = comValorArredondaId;
	}

	public int getComValorArredondaId() {
		return this.comValorArredondaId;
	}

	public void setComValorArredondaId(int comValorArredondaId) {
		this.comValorArredondaId = comValorArredondaId;
	}

	public double getComValorArredondaFixo() {
		return this.comValorArredondaFixo;
	}

	public void setComValorArredondaFixo(double comValorArredondaFixo) {
		this.comValorArredondaFixo = comValorArredondaFixo;
	}

	public double getComValorArredondaMax() {
		return this.comValorArredondaMax;
	}

	public void setComValorArredondaMax(double comValorArredondaMax) {
		this.comValorArredondaMax = comValorArredondaMax;
	}

	public double getComValorArredondaMin() {
		return this.comValorArredondaMin;
	}

	public void setComValorArredondaMin(double comValorArredondaMin) {
		this.comValorArredondaMin = comValorArredondaMin;
	}

	public ComValorProduto getComValorProduto() {
		return this.comValorProduto;
	}

	public void setComValorProduto(ComValorProduto comValorProduto) {
		this.comValorProduto = comValorProduto;
	}

	public Number getId() {
		return comValorArredondaId;
	}

	public void setId(Number id) {
		comValorArredondaId = id.intValue();
	}

	public String[] toArray() {
		return new String[] { comValorProduto.getComValorProdutoId() + "", comValorArredondaId + "", comValorArredondaMin.toString(), comValorArredondaMax.toString(), comValorArredondaFixo.toString() };
	}

	public void anularDependencia() {
		comValorProduto = null;
	}
}