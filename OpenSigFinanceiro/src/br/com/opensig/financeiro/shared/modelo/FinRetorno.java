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
 * Classe que representa um retorno no sistema.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 * @since 31/10/2010
 */
@Entity
@Table(name = "fin_retorno")
public class FinRetorno extends Dados implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "fin_retorno_id")
	private int finRetornoId;

	@Lob()
	@Column(name = "fin_retorno_boletos")
	private String finRetornoBoletos;

	@Lob()
	@Column(name = "fin_retorno_arquivo")
	private String finRetornoArquivo;

	@Temporal(TemporalType.DATE)
	@Column(name = "fin_retorno_cadastro")
	private Date finRetornoCadastro;

	@Column(name = "fin_retorno_quantidade")
	private int finRetornoQuantidade;

	@Column(name = "fin_retorno_valor")
	private Double finRetornoValor;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "emp_empresa_id")
	private EmpEmpresa empEmpresa;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fin_conta_id")
	private FinConta finConta;

	public FinRetorno() {
		this(0);
	}

	public FinRetorno(int finRetornoId) {
		super("pu_financeiro", "FinRetorno", "finRetornoId", "finRetornoCadastro", EDirecao.DESC);
		this.finRetornoId = finRetornoId;
	}

	public int getFinRetornoId() {
		return this.finRetornoId;
	}

	public void setFinRetornoId(int finRetornoId) {
		this.finRetornoId = finRetornoId;
	}

	public String getFinRetornoBoletos() {
		return finRetornoBoletos;
	}

	public void setFinRetornoBoletos(String finRetornoBoletos) {
		this.finRetornoBoletos = finRetornoBoletos;
	}

	public String getFinRetornoArquivo() {
		return this.finRetornoArquivo;
	}

	public void setFinRetornoArquivo(String finRetornoArquivo) {
		this.finRetornoArquivo = finRetornoArquivo;
	}

	public Date getFinRetornoCadastro() {
		return this.finRetornoCadastro;
	}

	public void setFinRetornoCadastro(Date finRetornoCadastro) {
		this.finRetornoCadastro = finRetornoCadastro;
	}

	public int getFinRetornoQuantidade() {
		return this.finRetornoQuantidade;
	}

	public void setFinRetornoQuantidade(int finRetornoQuantidade) {
		this.finRetornoQuantidade = finRetornoQuantidade;
	}

	public Double getFinRetornoValor() {
		return this.finRetornoValor;
	}

	public void setFinRetornoValor(Double finRetornoValor) {
		this.finRetornoValor = finRetornoValor;
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
		return finRetornoId;
	}

	public void setId(Number id) {
		finRetornoId = id.intValue();
	}

	public void anularDependencia() {
		empEmpresa = null;
		finConta = null;
	}

	public String[] toArray() {
		return new String[] { finRetornoId + "", empEmpresa.getEmpEmpresaId() + "", empEmpresa.getEmpEntidade().getEmpEntidadeNome1(), finConta.getFinContaId() + "", finConta.getFinContaNome(),
				UtilClient.getDataGrid(finRetornoCadastro), finRetornoQuantidade + "", finRetornoValor.toString(), finRetornoBoletos, "arquivo" };
	}

}