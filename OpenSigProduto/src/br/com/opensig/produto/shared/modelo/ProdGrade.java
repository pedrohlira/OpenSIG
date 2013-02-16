package br.com.opensig.produto.shared.modelo;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.eclipse.persistence.oxm.annotations.XmlInverseReference;

import br.com.opensig.core.shared.modelo.Dados;

/**
 * Classe que representa uma grade no sistema.
 * 
 * @author Pedro H. Lira
 */
@Entity
@Table(name = "prod_grade")
@XmlRootElement
public class ProdGrade extends Dados implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "prod_grade_id")
	private int prodGradeId;

	@Column(name = "prod_grade_barra")
	private String prodGradeBarra;

	@Column(name = "prod_grade_tamanho")
	private String prodGradeTamanho;

	@Column(name = "prod_grade_cor")
	private String prodGradeCor;

	@Column(name = "prod_grade_opcao")
	private String prodGradeOpcao;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "prod_produto_id")
	@XmlInverseReference(mappedBy = "prodGrades")
	private ProdProduto prodProduto;

	@OneToMany(mappedBy = "prodGrade", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@XmlTransient
	private List<ProdEstoqueGrade> prodEstoqueGrades;

	@Transient
	private Double prodGradeEstoque;

	public ProdGrade() {
		this(0);
	}

	public ProdGrade(int prodGradeId) {
		super("pu_produto", "ProdGrade", "prodGradeId", "prodGradeId");
		this.prodGradeId = prodGradeId;
	}

	@Override
	public Number getId() {
		return prodGradeId;
	}

	@Override
	public void setId(Number id) {
		this.prodGradeId = id.intValue();
	}

	public int getProdGradeId() {
		return prodGradeId;
	}

	public void setProdGradeId(int prodGradeId) {
		this.prodGradeId = prodGradeId;
	}

	public String getProdGradeBarra() {
		return prodGradeBarra;
	}

	public void setProdGradeBarra(String prodGradeBarra) {
		this.prodGradeBarra = prodGradeBarra;
	}

	public String getProdGradeTamanho() {
		return prodGradeTamanho;
	}

	public void setProdGradeTamanho(String prodGradeTamanho) {
		this.prodGradeTamanho = prodGradeTamanho;
	}

	public String getProdGradeCor() {
		return prodGradeCor;
	}

	public void setProdGradeCor(String prodGradeCor) {
		this.prodGradeCor = prodGradeCor;
	}

	public String getProdGradeOpcao() {
		return prodGradeOpcao;
	}

	public void setProdGradeOpcao(String prodGradeOpcao) {
		this.prodGradeOpcao = prodGradeOpcao;
	}

	public ProdProduto getProdProduto() {
		return prodProduto;
	}

	public void setProdProduto(ProdProduto prodProduto) {
		this.prodProduto = prodProduto;
	}

	public List<ProdEstoqueGrade> getProdEstoqueGrades() {
		return prodEstoqueGrades;
	}

	public void setProdEstoqueGrades(List<ProdEstoqueGrade> prodEstoqueGrades) {
		this.prodEstoqueGrades = prodEstoqueGrades;
	}

	public Double getProdGradeEstoque() {
		return prodGradeEstoque;
	}

	public void setProdGradeEstoque(Double prodGradeEstoque) {
		this.prodGradeEstoque = prodGradeEstoque;
	}

	@Override
	public String[] toArray() {
		for (ProdEstoqueGrade est : prodEstoqueGrades) {
			if (est.getEmpEmpresa().getEmpEmpresaId() == getEmpresa()) {
				prodGradeEstoque = est.getProdEstoqueGradeQuantidade();
				break;
			}
		}

		return new String[] { prodGradeId + "", prodGradeBarra, prodGradeTamanho, prodGradeCor, prodGradeOpcao, prodGradeEstoque + "" };
	}

	public void anularDependencia() {
		prodProduto = null;
		prodEstoqueGrades = null;
	}
}