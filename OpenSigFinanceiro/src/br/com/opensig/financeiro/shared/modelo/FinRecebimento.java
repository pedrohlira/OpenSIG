package br.com.opensig.financeiro.shared.modelo;

import java.io.Serializable;
import java.util.Date;

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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import br.com.opensig.core.client.UtilClient;
import br.com.opensig.core.shared.modelo.Dados;

/**
 * Classe que representa um Recebimento no sistema.
 * 
 * @author Pedro H. Lira
 */
@Entity
@Table(name = "fin_recebimento")
@XmlRootElement(name = "EcfPagamentoParcela")
public class FinRecebimento extends Dados implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "fin_recebimento_id")
	@XmlElement(name = "ecfPagamentoParcelaId")
	private int finRecebimentoId;

	@Column(name = "fin_recebimento_documento")
	@XmlElement(name = "ecfPagamentoParcelaNsu")
	private String finRecebimentoDocumento;

	@Column(name = "fin_recebimento_observacao")
	@XmlTransient
	private String finRecebimentoObservacao;

	@Column(name = "fin_recebimento_parcela")
	@XmlTransient
	private String finRecebimentoParcela;

	@Column(name = "fin_recebimento_status")
	@XmlTransient
	private String finRecebimentoStatus;

	@Temporal(TemporalType.DATE)
	@Column(name = "fin_recebimento_cadastro")
	@XmlTransient
	private Date finRecebimentoCadastro;

	@Temporal(TemporalType.DATE)
	@Column(name = "fin_recebimento_realizado")
	@XmlTransient
	private Date finRecebimentoRealizado;

	@Column(name = "fin_recebimento_valor")
	@XmlElement(name = "ecfPagamentoParcelaValor")
	private Double finRecebimentoValor;

	@Temporal(TemporalType.DATE)
	@Column(name = "fin_recebimento_vencimento")
	@XmlElement(name = "ecfPagamentoParcelaData")
	private Date finRecebimentoVencimento;

	@Temporal(TemporalType.DATE)
	@Column(name = "fin_recebimento_conciliado")
	@XmlTransient
	private Date finRecebimentoConciliado;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fin_receber_id")
	@XmlTransient
	private FinReceber finReceber;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fin_forma_id")
	@XmlTransient
	private FinForma finForma;

	public FinRecebimento() {
		this(0);
	}

	public FinRecebimento(int finRecebimentoId) {
		super("pu_financeiro", "FinRecebimento", "finRecebimentoId", "finRecebimentoVencimento");
		this.finRecebimentoId = finRecebimentoId;
	}

	public int getFinRecebimentoId() {
		return this.finRecebimentoId;
	}

	public void setFinRecebimentoId(int finRecebimentoId) {
		this.finRecebimentoId = finRecebimentoId;
	}

	public String getFinRecebimentoDocumento() {
		return this.finRecebimentoDocumento;
	}

	public void setFinRecebimentoDocumento(String finRecebimentoDocumento) {
		this.finRecebimentoDocumento = finRecebimentoDocumento;
	}

	public String getFinRecebimentoObservacao() {
		return this.finRecebimentoObservacao;
	}

	public void setFinRecebimentoObservacao(String finRecebimentoObservacao) {
		this.finRecebimentoObservacao = finRecebimentoObservacao;
	}

	public String getFinRecebimentoParcela() {
		return this.finRecebimentoParcela;
	}

	public void setFinRecebimentoParcela(String finRecebimentoParcela) {
		this.finRecebimentoParcela = finRecebimentoParcela;
	}

	public String getFinRecebimentoStatus() {
		return this.finRecebimentoStatus;
	}

	public void setFinRecebimentoStatus(String finRecebimentoStatus) {
		this.finRecebimentoStatus = finRecebimentoStatus;
	}

	public Date getFinRecebimentoRealizado() {
		return this.finRecebimentoRealizado;
	}

	public void setFinRecebimentoRealizado(Date finRecebimentoRealizado) {
		this.finRecebimentoRealizado = finRecebimentoRealizado;
	}

	public Date getFinRecebimentoCadastro() {
		return finRecebimentoCadastro;
	}

	public void setFinRecebimentoCadastro(Date finRecebimentoCadastro) {
		this.finRecebimentoCadastro = finRecebimentoCadastro;
	}

	public Double getFinRecebimentoValor() {
		return this.finRecebimentoValor;
	}

	public void setFinRecebimentoValor(Double finRecebimentoValor) {
		this.finRecebimentoValor = finRecebimentoValor;
	}

	public Date getFinRecebimentoVencimento() {
		return this.finRecebimentoVencimento;
	}

	public void setFinRecebimentoVencimento(Date finRecebimentoVencimento) {
		this.finRecebimentoVencimento = finRecebimentoVencimento;
	}

	public Date getFinRecebimentoConciliado() {
		return finRecebimentoConciliado;
	}

	public void setFinRecebimentoConciliado(Date finRecebimentoConciliado) {
		this.finRecebimentoConciliado = finRecebimentoConciliado;
	}

	public FinReceber getFinReceber() {
		return finReceber;
	}

	public void setFinReceber(FinReceber finReceber) {
		this.finReceber = finReceber;
	}

	public FinForma getFinForma() {
		return finForma;
	}

	public void setFinForma(FinForma finForma) {
		this.finForma = finForma;
	}

	public Number getId() {
		return finRecebimentoId;
	}

	public void setId(Number id) {
		finRecebimentoId = id.intValue();
	}

	public void anularDependencia() {
		finReceber = null;
		finForma = null;
	}

	public Dados getObjeto(String campo) {
		if (campo.startsWith("finReceber")) {
			return new FinReceber();
		} else if (campo.startsWith("finForma")) {
			return new FinForma();
		} else {
			return null;
		}
	}

	public String[] toArray() {
		return new String[] { finRecebimentoId + "", finReceber.getFinReceberId() + "", finReceber.getEmpEmpresa().getEmpEmpresaId() + "",
				finReceber.getEmpEmpresa().getEmpEntidade().getEmpEntidadeNome1(), finReceber.getEmpEntidade().getEmpEntidadeNome1(), finReceber.getFinConta().getFinContaId() + "",
				finForma.getFinFormaId() + "", finForma.getFinFormaDescricao(), finRecebimentoDocumento, finRecebimentoValor.toString(), finRecebimentoParcela,
				UtilClient.getDataGrid(finRecebimentoCadastro), UtilClient.getDataGrid(finRecebimentoVencimento), finRecebimentoStatus, UtilClient.getDataGrid(finRecebimentoRealizado),
				UtilClient.getDataGrid(finRecebimentoConciliado), finReceber.getFinReceberNfe() + "", finRecebimentoObservacao };
	}

}