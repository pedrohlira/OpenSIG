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
import br.com.opensig.financeiro.shared.modelo.FinPagar;

/**
 * Classe que representa um consumo no sistema.
 * 
 * @author Pedro H. Lira
 */
@Entity
@Table(name = "com_consumo")
public class ComConsumo extends Dados implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "com_consumo_id")
	private int comConsumoId;

	@Column(name = "com_consumo_aliquota")
	private Double comConsumoAliquota;

	@Column(name = "com_consumo_base")
	private Double comConsumoBase;

	@Column(name = "com_consumo_cfop")
	private int comConsumoCfop;

	@Column(name = "com_consumo_data")
	@Temporal(TemporalType.DATE)
	private Date comConsumoData;

	@Column(name = "com_consumo_documento")
	private int comConsumoDocumento;

	@Column(name = "com_consumo_icms")
	private Double comConsumoIcms;

	@Column(name = "com_consumo_observacao")
	private String comConsumoObservacao;

	@Column(name = "com_consumo_valor")
	private Double comConsumoValor;

	@Column(name = "com_consumo_fechada")
	private int comConsumoFechada;

	@Column(name = "com_consumo_paga")
	private int comConsumoPaga;

	@Column(name = "com_consumo_tipo")
	private String comConsumoTipo;

	@JoinColumn(name = "emp_empresa_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private EmpEmpresa empEmpresa;

	@JoinColumn(name = "emp_fornecedor_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private EmpFornecedor empFornecedor;

	@JoinColumn(name = "fin_pagar_id")
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private FinPagar finPagar;

	public ComConsumo() {
		this(0);
	}

	public ComConsumo(int comConsumoId) {
		super("pu_comercial", "ComConsumo", "comConsumoId", "comConsumoData", EDirecao.DESC);
		this.comConsumoId = comConsumoId;
	}

	public int getComConsumoId() {
		return comConsumoId;
	}

	public void setComConsumoId(int comConsumoId) {
		this.comConsumoId = comConsumoId;
	}

	public double getComConsumoAliquota() {
		return comConsumoAliquota;
	}

	public void setComConsumoAliquota(double comConsumoAliquota) {
		this.comConsumoAliquota = comConsumoAliquota;
	}

	public Double getComConsumoBase() {
		return comConsumoBase;
	}

	public void setComConsumoBase(Double comConsumoBase) {
		this.comConsumoBase = comConsumoBase;
	}

	public int getComConsumoCfop() {
		return comConsumoCfop;
	}

	public void setComConsumoCfop(int comConsumoCfop) {
		this.comConsumoCfop = comConsumoCfop;
	}

	public Double getComConsumoIcms() {
		return comConsumoIcms;
	}

	public void setComConsumoIcms(Double comConsumoIcms) {
		this.comConsumoIcms = comConsumoIcms;
	}

	public String getComConsumoObservacao() {
		return comConsumoObservacao;
	}

	public void setComConsumoObservacao(String comConsumoObservacao) {
		this.comConsumoObservacao = comConsumoObservacao;
	}

	public Double getComConsumoValor() {
		return comConsumoValor;
	}

	public void setComConsumoValor(Double comConsumoValor) {
		this.comConsumoValor = comConsumoValor;
	}

	public Date getComConsumoData() {
		return comConsumoData;
	}

	public void setComConsumoData(Date comConsumoData) {
		this.comConsumoData = comConsumoData;
	}

	public int getComConsumoDocumento() {
		return comConsumoDocumento;
	}

	public void setComConsumoDocumento(int comConsumoDocumento) {
		this.comConsumoDocumento = comConsumoDocumento;
	}

	public String getComConsumoTipo() {
		return comConsumoTipo;
	}

	public void setComConsumoTipo(String comConsumoTipo) {
		this.comConsumoTipo = comConsumoTipo;
	}

	public EmpEmpresa getEmpEmpresa() {
		return empEmpresa;
	}

	public void setEmpEmpresa(EmpEmpresa empEmpresa) {
		this.empEmpresa = empEmpresa;
	}

	public EmpFornecedor getEmpFornecedor() {
		return empFornecedor;
	}

	public void setEmpFornecedor(EmpFornecedor empFornecedor) {
		this.empFornecedor = empFornecedor;
	}

	public boolean getComConsumoFechada() {
		return comConsumoFechada == 0 ? false : true;
	}

	public void setComConsumoFechada(boolean comConsumoFechada) {
		this.comConsumoFechada = comConsumoFechada == false ? 0 : 1;
	}

	public boolean getComConsumoPaga() {
		return comConsumoPaga == 0 ? false : true;
	}

	public void setComConsumoPaga(boolean comConsumoPaga) {
		this.comConsumoPaga = comConsumoPaga == false ? 0 : 1;
	}

	public FinPagar getFinPagar() {
		return finPagar;
	}

	public void setFinPagar(FinPagar finPagar) {
		this.finPagar = finPagar;
	}

	public Number getId() {
		return comConsumoId;
	}

	public void setId(Number id) {
		comConsumoId = id.intValue();
	}

	public String[] toArray() {
		int pagarId = finPagar == null ? 0 : finPagar.getFinPagarId();

		return new String[] { comConsumoId + "", empFornecedor.getId().toString(), empFornecedor.getEmpEntidade().getEmpEntidadeId() + "", empFornecedor.getEmpEntidade().getEmpEntidadeNome1(),
				empEmpresa.getId().toString(), empEmpresa.getEmpEntidade().getEmpEntidadeNome1(), comConsumoTipo, comConsumoDocumento + "", UtilClient.getDataGrid(comConsumoData),
				comConsumoValor.toString(), comConsumoCfop + "", comConsumoBase.toString(), comConsumoAliquota + "", comConsumoIcms.toString(), getComConsumoFechada() + "", pagarId + "",
				getComConsumoPaga() + "", comConsumoObservacao };
	}

	public Dados getObjeto(String campo) {
		if (campo.startsWith("empFornecedor")) {
			return new EmpFornecedor();
		} else if (campo.startsWith("empEmpresa")) {
			return new EmpEmpresa();
		} else if (campo.startsWith("finPagar")) {
			return new FinPagar();
		} else {
			return null;
		}
	}

	public void anularDependencia() {
		empFornecedor = null;
		empEmpresa = null;
		finPagar = null;
	}
}