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
 * Classe que representa um produto da venda D1 do ECF.
 * 
 * @author Pedro H. Lira
 */
@Entity
@Table(name = "com_ecf_nota_produto")
@XmlRootElement(name = "EcfNotaProduto")
public class ComEcfNotaProduto extends Dados implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "com_ecf_nota_produto_id")
	@XmlElement(name = "ecfNotaProdutoId")
	private int comEcfNotaProdutoId;

	@Column(name = "com_ecf_nota_produto_quantidade")
	@XmlElement(name = "ecfNotaProdutoQuantidade")
	private Double comEcfNotaProdutoQuantidade;

	@Column(name = "com_ecf_nota_produto_bruto")
	@XmlElement(name = "ecfNotaProdutoBruto")
	private Double comEcfNotaProdutoBruto;

	@Column(name = "com_ecf_nota_produto_desconto")
	@XmlElement(name = "ecfNotaProdutoDesconto")
	private Double comEcfNotaProdutoDesconto;

	@Column(name = "com_ecf_nota_produto_liquido")
	@XmlElement(name = "ecfNotaProdutoLiquido")
	private Double comEcfNotaProdutoLiquido;

	@Column(name = "com_ecf_nota_produto_icms")
	@XmlElement(name = "ecfNotaProdutoIcms")
	private Double comEcfNotaProdutoIcms;

	@Column(name = "com_ecf_nota_produto_ipi")
	@XmlElement(name = "ecfNotaProdutoIpi")
	private Double comEcfNotaProdutoIpi;

	@Column(name = "com_ecf_nota_produto_ordem")
	@XmlElement(name = "ecfNotaProdutoOrdem")
	private int comEcfNotaProdutoOrdem;

	@JoinColumn(name = "prod_produto_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private ProdProduto prodProduto;

	@JoinColumn(name = "prod_embalagem_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private ProdEmbalagem prodEmbalagem;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "com_ecf_nota_id")
	@XmlTransient
	private ComEcfNota comEcfNota;

	public ComEcfNotaProduto() {
		this(0);
	}

	public ComEcfNotaProduto(int comEcfNotaProdutoId) {
		super("pu_comercial", "ComEcfNotaProduto", "comEcfNotaProdutoId", "comEcfNotaProdutoOrdem");
		this.comEcfNotaProdutoId = comEcfNotaProdutoId;
	}

	public int getComEcfNotaProdutoId() {
		return comEcfNotaProdutoId;
	}

	public void setComEcfNotaProdutoId(int comEcfNotaProdutoId) {
		this.comEcfNotaProdutoId = comEcfNotaProdutoId;
	}

	public Double getComEcfNotaProdutoQuantidade() {
		return comEcfNotaProdutoQuantidade;
	}

	public void setComEcfNotaProdutoQuantidade(Double comEcfNotaProdutoQuantidade) {
		this.comEcfNotaProdutoQuantidade = comEcfNotaProdutoQuantidade;
	}

	public Double getComEcfNotaProdutoBruto() {
		return comEcfNotaProdutoBruto;
	}

	public void setComEcfNotaProdutoBruto(Double comEcfNotaProdutoBruto) {
		this.comEcfNotaProdutoBruto = comEcfNotaProdutoBruto;
	}

	public Double getComEcfNotaProdutoDesconto() {
		return comEcfNotaProdutoDesconto;
	}

	public void setComEcfNotaProdutoDesconto(Double comEcfNotaProdutoDesconto) {
		this.comEcfNotaProdutoDesconto = comEcfNotaProdutoDesconto;
	}

	public Double getComEcfNotaProdutoLiquido() {
		return comEcfNotaProdutoLiquido;
	}

	public void setComEcfNotaProdutoLiquido(Double comEcfNotaProdutoLiquido) {
		this.comEcfNotaProdutoLiquido = comEcfNotaProdutoLiquido;
	}

	public Double getComEcfNotaProdutoIcms() {
		return comEcfNotaProdutoIcms;
	}

	public void setComEcfNotaProdutoIcms(Double comEcfNotaProdutoIcms) {
		this.comEcfNotaProdutoIcms = comEcfNotaProdutoIcms;
	}

	public Double getComEcfNotaProdutoIpi() {
		return comEcfNotaProdutoIpi;
	}

	public void setComEcfNotaProdutoIpi(Double comEcfNotaProdutoIpi) {
		this.comEcfNotaProdutoIpi = comEcfNotaProdutoIpi;
	}

	public int getComEcfNotaProdutoOrdem() {
		return comEcfNotaProdutoOrdem;
	}

	public void setComEcfNotaProdutoOrdem(int comEcfNotaProdutoOrdem) {
		this.comEcfNotaProdutoOrdem = comEcfNotaProdutoOrdem;
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

	public ComEcfNota getComEcfNota() {
		return comEcfNota;
	}

	public void setComEcfNota(ComEcfNota comEcfNota) {
		this.comEcfNota = comEcfNota;
	}

	public Number getId() {
		return comEcfNotaProdutoId;
	}

	public void setId(Number id) {
		comEcfNotaProdutoId = id.intValue();
	}

	public String[] toArray() {
		return new String[] { comEcfNotaProdutoId + "", comEcfNota.getComEcfNotaId() + "", comEcfNota.getEmpEmpresa().getEmpEmpresaId() + "",
				comEcfNota.getEmpEmpresa().getEmpEntidade().getEmpEntidadeNome1(), comEcfNota.getEmpCliente().getEmpEntidade().getEmpEntidadeNome1(),
				prodProduto.getEmpFornecedor().getEmpEntidade().getEmpEntidadeNome1(), prodProduto.getProdProdutoId() + "", prodProduto.getProdProdutoBarra(), prodProduto.getProdProdutoDescricao(),
				prodProduto.getProdProdutoReferencia(), UtilClient.getDataGrid(comEcfNota.getComEcfNotaData()), comEcfNotaProdutoQuantidade + "", prodEmbalagem.getProdEmbalagemId() + "",
				prodEmbalagem.getProdEmbalagemNome(), comEcfNotaProdutoBruto.toString(), comEcfNotaProdutoDesconto.toString(), comEcfNotaProdutoLiquido.toString(), comEcfNotaProdutoIcms + "",
				comEcfNotaProdutoIpi + "", comEcfNotaProdutoOrdem + "" };
	}

	public Dados getObjeto(String campo) {
		if (campo.startsWith("comEcfNota")) {
			return new ComEcfNota();
		} else if (campo.startsWith("prodProduto")) {
			return new ProdProduto();
		} else if (campo.startsWith("prodEmbalagem")) {
			return new ProdEmbalagem();
		} else {
			return null;
		}
	}

	public void anularDependencia() {
		comEcfNota = null;
		prodProduto = null;
		prodEmbalagem = null;
	}
}
