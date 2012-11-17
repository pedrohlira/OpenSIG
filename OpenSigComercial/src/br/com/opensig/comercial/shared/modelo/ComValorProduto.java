package br.com.opensig.comercial.shared.modelo;

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

import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.empresa.shared.modelo.EmpEmpresa;
import br.com.opensig.empresa.shared.modelo.EmpFornecedor;
import br.com.opensig.produto.shared.modelo.ProdProduto;

/**
 * Classe que representa um valor do produto no sistema.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 * @since 07/08/2009
 */
@Entity
@Table(name = "com_valor_produto")
public class ComValorProduto extends Dados implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "com_valor_produto_id")
	private int comValorProdutoId;

	@Column(name = "com_valor_produto_despesa")
	private int comValorProdutoDespesa;

	@Column(name = "com_valor_produto_formula")
	private String comValorProdutoFormula;

	@Column(name = "com_valor_produto_markup")
	private int comValorProdutoMarkup;

	@JoinColumn(name = "emp_empresa_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private EmpEmpresa empEmpresa;

	@JoinColumn(name = "emp_fornecedor_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private EmpFornecedor empFornecedor;

	@JoinColumn(name = "prod_produto_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private ProdProduto prodProduto;

	@OneToMany(mappedBy = "comValorProduto", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private List<ComValorArredonda> comValorArredondas;

	public ComValorProduto() {
		this(0);
	}

	public ComValorProduto(int comValorProdutoId) {
		super("pu_comercial", "ComValorProduto", "comValorProdutoId", "comValorProdutoId");
		this.comValorProdutoId = comValorProdutoId;
	}

	public int getComValorProdutoId() {
		return this.comValorProdutoId;
	}

	public void setComValorProdutoId(int comValorProdutoId) {
		this.comValorProdutoId = comValorProdutoId;
	}

	public int getComValorProdutoDespesa() {
		return this.comValorProdutoDespesa;
	}

	public void setComValorProdutoDespesa(int comValorProdutoDespesa) {
		this.comValorProdutoDespesa = comValorProdutoDespesa;
	}

	public String getComValorProdutoFormula() {
		return this.comValorProdutoFormula;
	}

	public void setComValorProdutoFormula(String comValorProdutoFormula) {
		this.comValorProdutoFormula = comValorProdutoFormula;
	}

	public int getComValorProdutoMarkup() {
		return this.comValorProdutoMarkup;
	}

	public void setComValorProdutoMarkup(int comValorProdutoMarkup) {
		this.comValorProdutoMarkup = comValorProdutoMarkup;
	}

	public EmpEmpresa getEmpEmpresa() {
		return empEmpresa;
	}

	public void setEmpEmpresa(EmpEmpresa empEmpresa) {
		this.empEmpresa = empEmpresa;
	}

	public EmpFornecedor getEmpFornecedor() {
		return this.empFornecedor;
	}

	public void setEmpFornecedor(EmpFornecedor empFornecedor) {
		this.empFornecedor = empFornecedor;
	}

	public ProdProduto getProdProduto() {
		return this.prodProduto;
	}

	public void setProdProduto(ProdProduto prodProduto) {
		this.prodProduto = prodProduto;
	}

	public List<ComValorArredonda> getComValorArredondas() {
		return this.comValorArredondas;
	}

	public void setComValorArredondas(List<ComValorArredonda> comValorArredondas) {
		this.comValorArredondas = comValorArredondas;
	}

	public Number getId() {
		return comValorProdutoId;
	}

	public void setId(Number id) {
		comValorProdutoId = id.intValue();
	}

	public String[] toArray() {
		String fornecedorId = "0";
		String produtoId = "0";
		String fornecedorNome = "";
		String produtoNome = "";

		if (empFornecedor != null) {
			fornecedorId = empFornecedor.getEmpFornecedorId() + "";
			fornecedorNome = empFornecedor.getEmpEntidade().getEmpEntidadeNome1();
		}

		if (prodProduto != null) {
			produtoId = prodProduto.getProdProdutoId() + "";
			produtoNome = prodProduto.getProdProdutoDescricao();
		}

		return new String[] { comValorProdutoId + "", empEmpresa.getEmpEmpresaId() + "", empEmpresa.getEmpEntidade().getEmpEntidadeNome1(), comValorProdutoDespesa + "", comValorProdutoMarkup + "",
				comValorProdutoFormula, fornecedorId, fornecedorNome, produtoId, produtoNome };
	}

	public Dados getObjeto(String campo) {
		if (campo.startsWith("empFornecedor")) {
			return new EmpFornecedor();
		} else if (campo.startsWith("empEmpresa")) {
			return new EmpEmpresa();
		} else if (campo.startsWith("prodProduto")) {
			return new ProdProduto();
		} else {
			return null;
		}
	}

	public void anularDependencia() {
		comValorArredondas = null;
		empEmpresa = null;
		empFornecedor = null;
		prodProduto = null;
	}
}