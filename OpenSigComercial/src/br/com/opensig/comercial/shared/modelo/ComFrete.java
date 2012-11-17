package br.com.opensig.comercial.shared.modelo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.opensig.core.client.UtilClient;
import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.core.shared.modelo.EDirecao;
import br.com.opensig.empresa.shared.modelo.EmpEmpresa;
import br.com.opensig.empresa.shared.modelo.EmpFornecedor;
import br.com.opensig.empresa.shared.modelo.EmpTransportadora;
import br.com.opensig.financeiro.shared.modelo.FinPagar;

/**
 * Classe que representa um frete no sistema.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 * @since 06/10/2009
 */
@Entity
@Table(name = "com_frete")
public class ComFrete extends Dados implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "com_frete_id")
	private int comFreteId;

	@Column(name = "com_frete_aliquota")
	private Double comFreteAliquota;

	@Column(name = "com_frete_base")
	private Double comFreteBase;

	@Column(name = "com_frete_cfop")
	private int comFreteCfop;

	@Column(name = "com_frete_ctrc")
	private int comFreteCtrc;

	@Column(name = "com_frete_nota")
	private int comFreteNota;

	@Column(name = "com_frete_cubagem")
	private Double comFreteCubagem;

	@Column(name = "com_frete_emissao")
	@Temporal(TemporalType.DATE)
	private Date comFreteEmissao;

	@Column(name = "com_frete_especie")
	private String comFreteEspecie;

	@Column(name = "com_frete_icms")
	private Double comFreteIcms;

	@Column(name = "com_frete_observacao")
	private String comFreteObservacao;

	@Column(name = "com_frete_peso")
	private Double comFretePeso;

	@Column(name = "com_frete_recebimento")
	@Temporal(TemporalType.DATE)
	private Date comFreteRecebimento;

	@Column(name = "com_frete_serie")
	private int comFreteSerie;

	@Column(name = "com_frete_valor")
	private Double comFreteValor;

	@Column(name = "com_frete_valor_produto")
	private Double comFreteValorProduto;

	@Column(name = "com_frete_volume")
	private int comFreteVolume;

	@Column(name = "com_frete_fechada")
	private int comFreteFechada;

	@Column(name = "com_frete_paga")
	private int comFretePaga;

	@JoinColumn(name = "emp_transportadora_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private EmpTransportadora empTransportadora;

	@JoinColumn(name = "emp_empresa_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private EmpEmpresa empEmpresa;

	@JoinColumn(name = "emp_fornecedor_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private EmpFornecedor empFornecedor;

	@JoinColumn(name = "fin_pagar_id")
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private FinPagar finPagar;

	public ComFrete() {
		this(0);
	}

	public ComFrete(int comFreteId) {
		super("pu_comercial", "ComFrete", "comFreteId", "comFreteRecebimento", EDirecao.DESC);
		this.comFreteId = comFreteId;
	}

	public int getComFreteId() {
		return comFreteId;
	}

	public void setComFreteId(int comFreteId) {
		this.comFreteId = comFreteId;
	}

	public double getComFreteAliquota() {
		return comFreteAliquota;
	}

	public void setComFreteAliquota(double comFreteAliquota) {
		this.comFreteAliquota = comFreteAliquota;
	}

	public Double getComFreteBase() {
		return comFreteBase;
	}

	public void setComFreteBase(Double comFreteBase) {
		this.comFreteBase = comFreteBase;
	}

	public int getComFreteCfop() {
		return comFreteCfop;
	}

	public void setComFreteCfop(int comFreteCfop) {
		this.comFreteCfop = comFreteCfop;
	}

	public int getComFreteCtrc() {
		return comFreteCtrc;
	}

	public void setComFreteCtrc(int comFreteCtrc) {
		this.comFreteCtrc = comFreteCtrc;
	}

	public Double getComFreteCubagem() {
		return comFreteCubagem;
	}

	public void setComFreteCubagem(Double comFreteCubagem) {
		this.comFreteCubagem = comFreteCubagem;
	}

	public Date getComFreteEmissao() {
		return comFreteEmissao;
	}

	public void setComFreteEmissao(Date comFreteEmissao) {
		this.comFreteEmissao = comFreteEmissao;
	}

	public String getComFreteEspecie() {
		return comFreteEspecie;
	}

	public void setComFreteEspecie(String comFreteEspecie) {
		this.comFreteEspecie = comFreteEspecie;
	}

	public Double getComFreteIcms() {
		return comFreteIcms;
	}

	public void setComFreteIcms(Double comFreteIcms) {
		this.comFreteIcms = comFreteIcms;
	}

	public String getComFreteObservacao() {
		return comFreteObservacao;
	}

	public void setComFreteObservacao(String comFreteObservacao) {
		this.comFreteObservacao = comFreteObservacao;
	}

	public Double getComFretePeso() {
		return comFretePeso;
	}

	public void setComFretePeso(Double comFretePeso) {
		this.comFretePeso = comFretePeso;
	}

	public Date getComFreteRecebimento() {
		return comFreteRecebimento;
	}

	public void setComFreteRecebimento(Date comFreteRecebimento) {
		this.comFreteRecebimento = comFreteRecebimento;
	}

	public int getComFreteSerie() {
		return comFreteSerie;
	}

	public void setComFreteSerie(int comFreteSerie) {
		this.comFreteSerie = comFreteSerie;
	}

	public Double getComFreteValor() {
		return comFreteValor;
	}

	public void setComFreteValor(Double comFreteValor) {
		this.comFreteValor = comFreteValor;
	}

	public Double getComFreteValorProduto() {
		return comFreteValorProduto;
	}

	public void setComFreteValorProduto(Double comFreteValorProduto) {
		this.comFreteValorProduto = comFreteValorProduto;
	}

	public int getComFreteVolume() {
		return comFreteVolume;
	}

	public void setComFreteVolume(int comFreteVolume) {
		this.comFreteVolume = comFreteVolume;
	}

	public EmpTransportadora getEmpTransportadora() {
		return empTransportadora;
	}

	public void setEmpTransportadora(EmpTransportadora empTransportadora) {
		this.empTransportadora = empTransportadora;
	}

	public EmpEmpresa getEmpEmpresa() {
		return empEmpresa;
	}

	public void setEmpEmpresa(EmpEmpresa empEmpresa) {
		this.empEmpresa = empEmpresa;
	}

	public int getComFreteNota() {
		return comFreteNota;
	}

	public void setComFreteNota(int comFreteNota) {
		this.comFreteNota = comFreteNota;
	}

	public EmpFornecedor getEmpFornecedor() {
		return empFornecedor;
	}

	public void setEmpFornecedor(EmpFornecedor empFornecedor) {
		this.empFornecedor = empFornecedor;
	}

	public boolean getComFreteFechada() {
		return comFreteFechada == 0 ? false : true;
	}

	public void setComFreteFechada(boolean comFreteFechada) {
		this.comFreteFechada = comFreteFechada == false ? 0 : 1;
	}

	public boolean getComFretePaga() {
		return comFretePaga == 0 ? false : true;
	}

	public void setComFretePaga(boolean comFretePaga) {
		this.comFretePaga = comFretePaga == false ? 0 : 1;
	}

	public FinPagar getFinPagar() {
		return finPagar;
	}

	public void setFinPagar(FinPagar finPagar) {
		this.finPagar = finPagar;
	}

	public Number getId() {
		return comFreteId;
	}

	public void setId(Number id) {
		comFreteId = id.intValue();
	}

	public String[] toArray() {
		int pagarId = finPagar == null ? 0 : finPagar.getFinPagarId();

		int contaId = 0;
		if (finPagar != null) {
			contaId = finPagar.getFinConta().getFinContaId();
		}

		return new String[] { comFreteId + "", empFornecedor.getId().toString(), empFornecedor.getEmpEntidade().getEmpEntidadeNome1(), empEmpresa.getId().toString(),
				empEmpresa.getEmpEntidade().getEmpEntidadeNome1(), empTransportadora.getId().toString(), empTransportadora.getEmpEntidade().getEmpEntidadeId() + "",
				empTransportadora.getEmpEntidade().getEmpEntidadeNome1(), comFreteCtrc + "", UtilClient.getDataGrid(comFreteEmissao), UtilClient.getDataGrid(comFreteRecebimento), comFreteSerie + "",
				comFreteCfop + "", comFreteVolume + "", comFreteEspecie, comFretePeso.toString(), comFreteCubagem + "", comFreteValorProduto.toString(), comFreteNota + "", comFreteValor.toString(),
				comFreteBase.toString(), comFreteAliquota + "", comFreteIcms.toString(), getComFreteFechada() + "", contaId + "", pagarId + "", getComFretePaga() + "", comFreteObservacao };
	}

	public Dados getObjeto(String campo) {
		if (campo.startsWith("empFornecedor")) {
			return new EmpFornecedor();
		} else if (campo.startsWith("empEmpresa")) {
			return new EmpEmpresa();
		} else if (campo.startsWith("empTransportadora")) {
			return new EmpTransportadora();
		} else if (campo.startsWith("finPagar")) {
			return new FinPagar();
		} else {
			return null;
		}
	}

	public void anularDependencia() {
		empFornecedor = null;
		empEmpresa = null;
		empTransportadora = null;
		finPagar = null;
	}
}