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
 * Classe que representa um estoque da grade de produtos no sistema.
 * 
 * @author Pedro H. Lira
 */
@Entity
@Table(name = "prod_estoque_grade")
public class ProdEstoqueGrade extends Dados implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "prod_estoque_grade_id")
	private int prodEstoqueGradeId;

	@Column(name = "prod_estoque_grade_quantidade")
	private Double prodEstoqueGradeQuantidade;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "prod_grade_id")
	private ProdGrade prodGrade;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "emp_empresa_id")
	private EmpEmpresa empEmpresa;

	public ProdEstoqueGrade() {
		this(0);
	}

	public ProdEstoqueGrade(int prodEstoqueGradeId) {
		super("pu_produto", "ProdEstoqueGrade", "prodEstoqueGradeId", "prodEstoqueGradeQuantidade", EDirecao.DESC);
		this.prodEstoqueGradeId = prodEstoqueGradeId;
	}

	public int getProdEstoqueGradeId() {
		return prodEstoqueGradeId;
	}

	public void setProdEstoqueGradeId(int prodEstoqueGradeId) {
		this.prodEstoqueGradeId = prodEstoqueGradeId;
	}

	public Double getProdEstoqueGradeQuantidade() {
		return prodEstoqueGradeQuantidade;
	}

	public void setProdEstoqueGradeQuantidade(Double prodEstoqueGradeQuantidade) {
		this.prodEstoqueGradeQuantidade = prodEstoqueGradeQuantidade;
	}

	public ProdGrade getProdGrade() {
		return prodGrade;
	}

	public void setProdGrade(ProdGrade prodGrade) {
		this.prodGrade = prodGrade;
	}

	public EmpEmpresa getEmpEmpresa() {
		return empEmpresa;
	}

	public void setEmpEmpresa(EmpEmpresa empEmpresa) {
		this.empEmpresa = empEmpresa;
	}

	public Number getId() {
		return prodEstoqueGradeId;
	}

	public void setId(Number id) {
		prodEstoqueGradeId = id.intValue();
	}

	public String[] toArray() {
		return new String[] { prodEstoqueGradeId + "", empEmpresa.getEmpEmpresaId() + "", empEmpresa.getEmpEntidade().getEmpEntidadeNome1(),
				prodGrade.getProdProduto().getEmpFornecedor().getEmpEntidade().getEmpEntidadeNome1(), prodGrade.getProdProduto().getProdProdutoId() + "", prodGrade.getProdGradeBarra(),
				prodGrade.getProdProduto().getProdProdutoDescricao(), prodGrade.getProdProduto().getProdProdutoReferencia(), prodGrade.getProdGradeTamanho(), prodGrade.getProdGradeCor(),
				prodGrade.getProdGradeOpcao(), prodEstoqueGradeQuantidade.toString() };
	}

	public void anularDependencia() {
		prodGrade = null;
		empEmpresa = null;
	}
}