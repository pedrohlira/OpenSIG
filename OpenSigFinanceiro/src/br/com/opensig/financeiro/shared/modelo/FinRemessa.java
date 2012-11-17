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
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.opensig.core.client.UtilClient;
import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.core.shared.modelo.EDirecao;
import br.com.opensig.empresa.shared.modelo.EmpEmpresa;

/**
 * Classe que representa uma remessa no sistema.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 * @since 31/10/2010
 */
@Entity
@Table(name = "fin_remessa")
public class FinRemessa extends Dados implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "fin_remessa_id")
	private int finRemessaId;

	@Lob()
	@Column(name = "fin_remessa_arquivo")
	private String finRemessaArquivo;

	@Temporal(TemporalType.DATE)
	@Column(name = "fin_remessa_cadastro")
	private Date finRemessaCadastro;

	@Temporal(TemporalType.DATE)
	@Column(name = "fin_remessa_dataate")
	private Date finRemessaDataate;

	@Temporal(TemporalType.DATE)
	@Column(name = "fin_remessa_datade")
	private Date finRemessaDatade;

	@Column(name = "fin_remessa_quantidade")
	private int finRemessaQuantidade;

	@Column(name = "fin_remessa_valor")
	private Double finRemessaValor;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "emp_empresa_id")
	private EmpEmpresa empEmpresa;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fin_conta_id")
	private FinConta finConta;

	public FinRemessa() {
		this(0);
	}

	public FinRemessa(int finRemessaId) {
		super("pu_financeiro", "FinRemessa", "finRemessaId", "finRemessaCadastro", EDirecao.DESC);
		this.finRemessaId = finRemessaId;
		setLimpaBranco(false);
	}

	public int getFinRemessaId() {
		return this.finRemessaId;
	}

	public void setFinRemessaId(int finRemessaId) {
		this.finRemessaId = finRemessaId;
	}

	public String getFinRemessaArquivo() {
		return this.finRemessaArquivo;
	}

	public void setFinRemessaArquivo(String finRemessaArquivo) {
		this.finRemessaArquivo = finRemessaArquivo;
	}

	public Date getFinRemessaCadastro() {
		return this.finRemessaCadastro;
	}

	public void setFinRemessaCadastro(Date finRemessaCadastro) {
		this.finRemessaCadastro = finRemessaCadastro;
	}

	public Date getFinRemessaDataate() {
		return this.finRemessaDataate;
	}

	public void setFinRemessaDataate(Date finRemessaDataate) {
		this.finRemessaDataate = finRemessaDataate;
	}

	public Date getFinRemessaDatade() {
		return this.finRemessaDatade;
	}

	public void setFinRemessaDatade(Date finRemessaDatade) {
		this.finRemessaDatade = finRemessaDatade;
	}

	public int getFinRemessaQuantidade() {
		return this.finRemessaQuantidade;
	}

	public void setFinRemessaQuantidade(int finRemessaQuantidade) {
		this.finRemessaQuantidade = finRemessaQuantidade;
	}

	public Double getFinRemessaValor() {
		return this.finRemessaValor;
	}

	public void setFinRemessaValor(Double finRemessaValor) {
		this.finRemessaValor = finRemessaValor;
	}

	public EmpEmpresa getEmpEmpresa() {
		return empEmpresa;
	}

	public void setEmpEmpresa(EmpEmpresa empEmpresa) {
		this.empEmpresa = empEmpresa;
	}

	public FinConta getFinConta() {
		return finConta;
	}

	public void setFinConta(FinConta finConta) {
		this.finConta = finConta;
	}

	public Number getId() {
		return finRemessaId;
	}

	public void setId(Number id) {
		finRemessaId = id.intValue();
	}

	public void anularDependencia() {
		empEmpresa = null;
		finConta = null;
	}

	public String[] toArray() {
		return new String[] { finRemessaId + "", empEmpresa.getEmpEmpresaId() + "", empEmpresa.getEmpEntidade().getEmpEntidadeNome1(), finConta.getFinContaId() + "", finConta.getFinContaNome(),
				UtilClient.getDataGrid(finRemessaCadastro), UtilClient.getDataGrid(finRemessaDatade), UtilClient.getDataGrid(finRemessaDataate), finRemessaQuantidade + "", finRemessaValor.toString(),
				"arquivo" };
	}
}