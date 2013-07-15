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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import br.com.opensig.core.client.UtilClient;
import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.produto.shared.modelo.ProdEmbalagem;
import br.com.opensig.produto.shared.modelo.ProdProduto;

/**
 * Classe que representa um produto da troca no sistema.
 * 
 * @author Pedro H. Lira
 */
@Entity
@Table(name = "com_troca_produto")
@XmlRootElement(name = "EcfTrocaProduto")
public class ComTrocaProduto extends Dados implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "com_troca_produto_id")
	@XmlElement(name = "ecfTrocaProdutoId")
	private Integer comTrocaProdutoId;

	@Column(name = "com_troca_produto_barra")
	@XmlElement(name = "ecfTrocaProdutoBarra")
	private String comTrocaProdutoBarra;

	@Column(name = "com_troca_produto_quantidade")
	@XmlElement(name = "ecfTrocaProdutoQuantidade")
	private Double comTrocaProdutoQuantidade;

	@Column(name = "com_troca_produto_valor")
	@XmlElement(name = "ecfTrocaProdutoValor")
	private Double comTrocaProdutoValor;

	@Column(name = "com_troca_produto_total")
	@XmlElement(name = "ecfTrocaProdutoTotal")
	private Double comTrocaProdutoTotal;

	@Column(name = "com_troca_produto_ordem")
	@XmlElement(name = "ecfTrocaProdutoOrdem")
	private int comTrocaProdutoOrdem;

	@JoinColumn(name = "prod_produto_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private ProdProduto prodProduto;

	@JoinColumn(name = "prod_embalagem_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private ProdEmbalagem prodEmbalagem;

	@JoinColumn(name = "com_troca_id")
	@ManyToOne(fetch = FetchType.LAZY)
	@XmlTransient
	private ComTroca comTroca;
	
	public ComTrocaProduto() {
		this(0);
	}

	public ComTrocaProduto(int comTrocaProdutoId) {
		super("pu_comercial", "ComTrocaProduto", "comTrocaProdutoId", "comTrocaProdutoOrdem");
		this.comTrocaProdutoId = comTrocaProdutoId;
	}

	public Integer getComTrocaProdutoId() {
		return comTrocaProdutoId;
	}

	public void setComTrocaProdutoId(Integer comTrocaProdutoId) {
		this.comTrocaProdutoId = comTrocaProdutoId;
	}

	public String getComTrocaProdutoBarra() {
		return comTrocaProdutoBarra;
	}

	public void setComTrocaProdutoBarra(String comTrocaProdutoBarra) {
		this.comTrocaProdutoBarra = comTrocaProdutoBarra;
	}

	public Double getComTrocaProdutoQuantidade() {
		return comTrocaProdutoQuantidade;
	}

	public void setComTrocaProdutoQuantidade(Double comTrocaProdutoQuantidade) {
		this.comTrocaProdutoQuantidade = comTrocaProdutoQuantidade;
	}

	public Double getComTrocaProdutoValor() {
		return comTrocaProdutoValor;
	}

	public void setComTrocaProdutoValor(Double comTrocaProdutoValor) {
		this.comTrocaProdutoValor = comTrocaProdutoValor;
	}

	public Double getComTrocaProdutoTotal() {
		return comTrocaProdutoTotal;
	}

	public void setComTrocaProdutoTotal(Double comTrocaProdutoTotal) {
		this.comTrocaProdutoTotal = comTrocaProdutoTotal;
	}

	public int getComTrocaProdutoOrdem() {
		return comTrocaProdutoOrdem;
	}

	public void setComTrocaProdutoOrdem(int comTrocaProdutoOrdem) {
		this.comTrocaProdutoOrdem = comTrocaProdutoOrdem;
	}

	public ComTroca getComTroca() {
		return comTroca;
	}

	public void setComTroca(ComTroca comTroca) {
		this.comTroca = comTroca;
	}

	public ProdProduto getProdProduto() {
		return prodProduto;
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

	public Number getId() {
		return comTrocaProdutoId;
	}

	public void setId(Number id) {
		comTrocaProdutoId = id.intValue();
	}

	public String[] toArray() {
		return new String[] { comTrocaProdutoId + "", comTroca.getComTrocaId() + "", comTroca.getEmpEmpresa().getEmpEmpresaId() + "", comTroca.getEmpEmpresa().getEmpEntidade().getEmpEntidadeNome1(),
				prodProduto.getEmpFornecedor().getEmpEntidade().getEmpEntidadeNome1(), prodProduto.getProdProdutoId() + "", comTrocaProdutoBarra, prodProduto.getProdProdutoDescricao(),
				prodProduto.getProdProdutoReferencia(), UtilClient.getDataGrid(comTroca.getComTrocaData()), comTrocaProdutoQuantidade + "", prodEmbalagem.getProdEmbalagemId() + "",
				prodEmbalagem.getProdEmbalagemNome(), comTrocaProdutoValor.toString(), comTrocaProdutoTotal.toString(), comTrocaProdutoOrdem + "" };
	}

	public Dados getObjeto(String campo) {
		if (campo.startsWith("comTroca")) {
			return new ComTroca();
		} else if (campo.startsWith("prodProduto")) {
			return new ProdProduto();
		} else if (campo.startsWith("prodEmbalagem")) {
			return new ProdEmbalagem();
		} else {
			return null;
		}
	}

	public void anularDependencia() {
		comTroca = null;
		prodProduto = null;
		prodEmbalagem = null;
	}
}
