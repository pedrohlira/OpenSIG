package br.com.opensig.produto.shared.modelo;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import br.com.opensig.core.client.UtilClient;
import br.com.opensig.core.shared.modelo.Colecao;
import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.core.shared.modelo.EDirecao;
import br.com.opensig.empresa.shared.modelo.EmpFornecedor;

/**
 * Classe que representa um produto no sistema.
 * 
 * @author Pedro H. Lira
 */
@Entity
@Table(name = "prod_produto")
@XmlRootElement
public class ProdProduto extends Dados implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "prod_produto_id")
	private int prodProdutoId;

	@Column(name = "prod_produto_ncm")
	private String prodProdutoNcm;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "prod_produto_alterado")
	private Date prodProdutoAlterado;

	@Column(name = "prod_produto_ativo")
	@XmlElement(type = Boolean.class)
	private int prodProdutoAtivo;

	@Column(name = "prod_produto_incentivo")
	@XmlTransient
	private int prodProdutoIncentivo;

	@Column(name = "prod_produto_barra")
	private String prodProdutoBarra;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "prod_produto_cadastrado")
	private Date prodProdutoCadastrado;

	@Column(name = "prod_produto_categoria")
	@XmlTransient
	private String prodProdutoCategoria;

	@Column(name = "prod_produto_custo")
	@XmlTransient
	private Double prodProdutoCusto;

	@Column(name = "prod_produto_descricao")
	private String prodProdutoDescricao;

	@Column(name = "prod_produto_preco")
	private Double prodProdutoPreco;

	@Column(name = "prod_produto_volume")
	@XmlTransient
	private int prodProdutoVolume;

	@Column(name = "prod_produto_referencia")
	private String prodProdutoReferencia;
	
	@Column(name = "prod_produto_observacao")
	@XmlTransient
	private String prodProdutoObservacao;

	@OneToMany(mappedBy = "prodProduto", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private List<ProdPreco> prodPrecos;
	
	@OneToMany(mappedBy = "prodProdutoPrincipal", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private List<ProdComposicao> prodComposicoes;

	@OneToMany(mappedBy = "prodProduto", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@XmlTransient
	private List<ProdEstoque> prodEstoques;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "prod_tributacao_id")
	@XmlTransient
	private ProdTributacao prodTributacao;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "prod_ipi_id")
	@XmlTransient
	private ProdIpi prodIpi;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "prod_tipo_id")
	@XmlElement(name = "prodProdutoTipo", type = String.class)
	private ProdTipo prodTipo;

	@JoinColumn(name = "emp_fornecedor_id")
	@ManyToOne(fetch = FetchType.LAZY)
	@XmlTransient
	private EmpFornecedor empFornecedor;

	@JoinColumn(name = "emp_fabricante_id")
	@ManyToOne(fetch = FetchType.LAZY)
	@XmlTransient
	private EmpFornecedor empFabricante;

	@JoinColumn(name = "prod_origem_id")
	@ManyToOne(fetch = FetchType.LAZY)
	@XmlElement(name = "prodProdutoOrigem", type = String.class)
	private ProdOrigem prodOrigem;

	@JoinColumn(name = "prod_embalagem_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private ProdEmbalagem prodEmbalagem;

	@Column(name = "prod_produto_sinc")
	@XmlTransient
	private int prodProdutoSinc;
	
	@Transient
	private Double prodProdutoEstoque;
	
	@Transient
	private String prodProdutoCstCson;
	
	@Transient
	private char prodProdutoTributacao;
	
	@Transient
	private Double prodProdutoIcms;
	
	@Transient
	private Double prodProdutoIssqn;
	
	@Transient
	private char prodProdutoIat;

	@Transient
	private char prodProdutoIppt;
	
	public ProdProduto() {
		this(0);
	}

	public ProdProduto(int prodProdutoId) {
		super("pu_produto", "ProdProduto", "prodProdutoId", "prodProdutoDescricao", EDirecao.ASC);
		this.prodProdutoId = prodProdutoId;
		Colecao col = new Colecao("ProdEstoque", "t.prodEstoques", "JOIN", "t1");
		Colecao col1 = new Colecao("ProdPreco", "t.prodPrecos", "LEFT JOIN", "t2");
		Colecao col2 = new Colecao("ProdComposicao", "t.prodComposicoes", "LEFT JOIN", "t3");
		setColecao(new Colecao[] { col, col1, col2 });
	}

	public int getProdProdutoId() {
		return this.prodProdutoId;
	}

	public void setProdProdutoId(int prodProdutoId) {
		this.prodProdutoId = prodProdutoId;
	}

	public String getProdProdutoNcm() {
		return prodProdutoNcm;
	}

	public void setProdProdutoNcm(String prodProdutoNcm) {
		this.prodProdutoNcm = prodProdutoNcm;
	}

	public EmpFornecedor getEmpFornecedor() {
		return this.empFornecedor;
	}

	public void setEmpFornecedor(EmpFornecedor empFornecedor) {
		this.empFornecedor = empFornecedor;
	}

	public Date getProdProdutoAlterado() {
		return this.prodProdutoAlterado;
	}

	public void setProdProdutoAlterado(Date prodProdutoAlterado) {
		this.prodProdutoAlterado = prodProdutoAlterado;
	}

	public boolean getProdProdutoAtivo() {
		return prodProdutoAtivo == 0 ? false : true;
	}

	public void setProdProdutoAtivo(boolean prodProdutoAtivo) {
		this.prodProdutoAtivo = prodProdutoAtivo == false ? 0 : 1;
	}

	public boolean getProdProdutoIncentivo() {
		return prodProdutoIncentivo == 0 ? false : true;
	}

	public void setProdProdutoIncentivo(boolean prodProdutoIncentivo) {
		this.prodProdutoIncentivo = prodProdutoIncentivo == false ? 0 : 1;
	}

	public String getProdProdutoBarra() {
		return this.prodProdutoBarra;
	}

	public void setProdProdutoBarra(String prodProdutoBarra) {
		this.prodProdutoBarra = prodProdutoBarra;
	}

	public Date getProdProdutoCadastrado() {
		return this.prodProdutoCadastrado;
	}

	public void setProdProdutoCadastrado(Date prodProdutoCadastrado) {
		this.prodProdutoCadastrado = prodProdutoCadastrado;
	}

	public String getProdProdutoCategoria() {
		return this.prodProdutoCategoria;
	}

	public void setProdProdutoCategoria(String prodProdutoCategoria) {
		this.prodProdutoCategoria = prodProdutoCategoria;
	}

	public Double getProdProdutoCusto() {
		return this.prodProdutoCusto;
	}

	public void setProdProdutoCusto(Double prodProdutoCusto) {
		this.prodProdutoCusto = prodProdutoCusto;
	}

	public String getProdProdutoDescricao() {
		return this.prodProdutoDescricao;
	}

	public void setProdProdutoDescricao(String prodProdutoDescricao) {
		this.prodProdutoDescricao = prodProdutoDescricao;
	}

	public Double getProdProdutoPreco() {
		return this.prodProdutoPreco;
	}

	public void setProdProdutoPreco(Double prodProdutoPreco) {
		this.prodProdutoPreco = prodProdutoPreco;
	}

	public String getProdProdutoReferencia() {
		return this.prodProdutoReferencia;
	}

	public void setProdProdutoReferencia(String prodProdutoReferencia) {
		this.prodProdutoReferencia = prodProdutoReferencia;
	}

	public int getProdProdutoVolume() {
		return prodProdutoVolume;
	}

	public void setProdProdutoVolume(int prodProdutoVolume) {
		this.prodProdutoVolume = prodProdutoVolume;
	}

	public String getProdProdutoObservacao() {
		return prodProdutoObservacao;
	}

	public void setProdProdutoObservacao(String prodProdutoObservacao) {
		this.prodProdutoObservacao = prodProdutoObservacao;
	}

	public List<ProdPreco> getProdPrecos() {
		return this.prodPrecos;
	}

	public void setProdPrecos(List<ProdPreco> prodPrecos) {
		this.prodPrecos = prodPrecos;
	}

	public List<ProdComposicao> getProdComposicoes() {
		return prodComposicoes;
	}

	public void setProdComposicoes(List<ProdComposicao> prodComposicoes) {
		this.prodComposicoes = prodComposicoes;
	}

	public List<ProdEstoque> getProdEstoques() {
		return prodEstoques;
	}

	public void setProdEstoques(List<ProdEstoque> prodEstoques) {
		this.prodEstoques = prodEstoques;
	}

	public ProdTributacao getProdTributacao() {
		return this.prodTributacao;
	}

	public void setProdTributacao(ProdTributacao prodTributacao) {
		this.prodTributacao = prodTributacao;
	}

	public ProdIpi getProdIpi() {
		return prodIpi;
	}

	public void setProdIpi(ProdIpi prodIpi) {
		this.prodIpi = prodIpi;
	}

	public ProdTipo getProdTipo() {
		return prodTipo;
	}

	public void setProdTipo(ProdTipo prodTipo) {
		this.prodTipo = prodTipo;
	}

	public EmpFornecedor getEmpFabricante() {
		return empFabricante;
	}

	public void setEmpFabricante(EmpFornecedor empFabricante) {
		this.empFabricante = empFabricante;
	}

	public ProdOrigem getProdOrigem() {
		return prodOrigem;
	}

	public void setProdOrigem(ProdOrigem prodOrigem) {
		this.prodOrigem = prodOrigem;
	}

	public ProdEmbalagem getProdEmbalagem() {
		return prodEmbalagem;
	}

	public void setProdEmbalagem(ProdEmbalagem prodEmbalagem) {
		this.prodEmbalagem = prodEmbalagem;
	}

	public int getProdProdutoSinc() {
		return prodProdutoSinc;
	}

	public void setProdProdutoSinc(int prodProdutoSinc) {
		this.prodProdutoSinc = prodProdutoSinc;
	}

	public Double getProdProdutoEstoque() {
		return prodProdutoEstoque;
	}

	public void setProdProdutoEstoque(Double prodProdutoEstoque) {
		this.prodProdutoEstoque = prodProdutoEstoque;
	}

	public String getProdProdutoCstCson() {
		return prodProdutoCstCson;
	}

	public void setProdProdutoCstCson(String prodProdutoCstCson) {
		this.prodProdutoCstCson = prodProdutoCstCson;
	}

	public char getProdProdutoTributacao() {
		return prodProdutoTributacao;
	}

	public void setProdProdutoTributacao(char prodProdutoTributacao) {
		this.prodProdutoTributacao = prodProdutoTributacao;
	}

	public Double getProdProdutoIcms() {
		return prodProdutoIcms;
	}

	public void setProdProdutoIcms(Double prodProdutoIcms) {
		this.prodProdutoIcms = prodProdutoIcms;
	}

	public Double getProdProdutoIssqn() {
		return prodProdutoIssqn;
	}

	public void setProdProdutoIssqn(Double prodProdutoIssqn) {
		this.prodProdutoIssqn = prodProdutoIssqn;
	}

	public char getProdProdutoIat() {
		return prodProdutoIat;
	}

	public void setProdProdutoIat(char prodProdutoIat) {
		this.prodProdutoIat = prodProdutoIat;
	}

	public char getProdProdutoIppt() {
		return prodProdutoIppt;
	}

	public void setProdProdutoIppt(char prodProdutoIppt) {
		this.prodProdutoIppt = prodProdutoIppt;
	}

	public Number getId() {
		return prodProdutoId;
	}

	public void setId(Number id) {
		prodProdutoId = id.intValue();
	}

	public String[] toArray() {
		double estoque = 0;
		for (ProdEstoque est : prodEstoques) {
			if (est.getEmpEmpresa().getEmpEmpresaId() == getEmpresa()) {
				estoque = est.getProdEstoqueQuantidade();
				break;
			}
		}

		return new String[] { prodProdutoId + "", prodProdutoNcm, prodProdutoBarra, prodProdutoDescricao, prodProdutoReferencia, prodProdutoCusto + "", prodProdutoPreco + "",
				prodEmbalagem.getProdEmbalagemId() + "", prodEmbalagem.getProdEmbalagemNome(), prodProdutoVolume + "", estoque + "", prodProdutoCategoria, empFornecedor.getEmpFornecedorId() + "",
				empFornecedor.getEmpEntidade().getEmpEntidadeNome1(), empFabricante.getEmpFornecedorId() + "", empFabricante.getEmpEntidade().getEmpEntidadeNome1(),
				prodTributacao.getProdTributacaoId() + "", prodTributacao.getProdTributacaoNome(), prodTributacao.getProdTributacaoCst(), prodTributacao.getProdTributacaoCfop() + "",
				prodTributacao.getProdTributacaoDentro() + "", prodTributacao.getProdTributacaoFora() + "", prodTributacao.getProdTributacaoDecreto(), prodIpi.getProdIpiId() + "",
				prodIpi.getProdIpiNome(), prodIpi.getProdIpiAliquota() + "", prodTipo.getProdTipoId() + "", prodTipo.getProdTipoValor(), prodTipo.getProdTipoDescricao(),
				prodOrigem.getProdOrigemId() + "", prodOrigem.getProdOrigemDescricao(), UtilClient.getDataHoraGrid(prodProdutoCadastrado), UtilClient.getDataHoraGrid(prodProdutoAlterado),
				getProdProdutoAtivo() + "", getProdProdutoIncentivo() + "", prodProdutoSinc + "", prodProdutoObservacao };
	}

	public Dados getObjeto(String campo) {
		if (campo.startsWith("prodTributacao")) {
			return new ProdTributacao();
		} else if (campo.startsWith("prodIpi")) {
			return new ProdIpi();
		} else if (campo.startsWith("prodTipo")) {
			return new ProdTipo();
		} else if (campo.startsWith("empFornecedor")) {
			return new EmpFornecedor();
		} else if (campo.startsWith("empFabricante")) {
			return new EmpFornecedor();
		} else if (campo.startsWith("prodOrigem")) {
			return new ProdOrigem();
		} else if (campo.startsWith("prodEmbalagem")) {
			return new ProdEmbalagem();
		} else {
			return null;
		}
	}

	public void anularDependencia() {
		prodPrecos = null;
		prodComposicoes = null;
		prodEstoques = null;
		prodTributacao = null;
		prodIpi = null;
		prodTipo = null;
		empFornecedor = null;
		empFabricante = null;
		prodOrigem = null;
		prodEmbalagem = null;
	}
}