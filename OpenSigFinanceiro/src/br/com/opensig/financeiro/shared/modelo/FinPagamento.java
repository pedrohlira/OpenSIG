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

import br.com.opensig.core.client.UtilClient;
import br.com.opensig.core.shared.modelo.Dados;

/**
 * Classe que representa um pagamento no sistema.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 * @since 18/11/2009
 */
@Entity
@Table(name = "fin_pagamento")
public class FinPagamento extends Dados implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "fin_pagamento_id")
	private int finPagamentoId;

	@Column(name = "fin_pagamento_documento")
	private String finPagamentoDocumento;

	@Column(name = "fin_pagamento_observacao")
	private String finPagamentoObservacao;

	@Column(name = "fin_pagamento_parcela")
	private String finPagamentoParcela;

	@Column(name = "fin_pagamento_status")
	private String finPagamentoStatus;

	@Temporal(TemporalType.DATE)
	@Column(name = "fin_pagamento_cadastro")
	private Date finPagamentoCadastro;

	@Temporal(TemporalType.DATE)
	@Column(name = "fin_pagamento_realizado")
	private Date finPagamentoRealizado;

	@Column(name = "fin_pagamento_valor")
	private Double finPagamentoValor;

	@Temporal(TemporalType.DATE)
	@Column(name = "fin_pagamento_vencimento")
	private Date finPagamentoVencimento;

	@Temporal(TemporalType.DATE)
	@Column(name = "fin_pagamento_conciliado")
	private Date finPagamentoConciliado;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fin_pagar_id")
	private FinPagar finPagar;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fin_forma_id")
	private FinForma finForma;

	public FinPagamento() {
		this(0);
	}

	public FinPagamento(int finPagamentoId) {
		super("pu_financeiro", "FinPagamento", "finPagamentoId", "finPagamentoVencimento");
		this.finPagamentoId = finPagamentoId;
	}

	public int getFinPagamentoId() {
		return this.finPagamentoId;
	}

	public void setFinPagamentoId(int finPagamentoId) {
		this.finPagamentoId = finPagamentoId;
	}

	public String getFinPagamentoDocumento() {
		return this.finPagamentoDocumento;
	}

	public void setFinPagamentoDocumento(String finPagamentoDocumento) {
		this.finPagamentoDocumento = finPagamentoDocumento;
	}

	public String getFinPagamentoObservacao() {
		return this.finPagamentoObservacao;
	}

	public void setFinPagamentoObservacao(String finPagamentoObservacao) {
		this.finPagamentoObservacao = finPagamentoObservacao;
	}

	public String getFinPagamentoParcela() {
		return this.finPagamentoParcela;
	}

	public void setFinPagamentoParcela(String finPagamentoParcela) {
		this.finPagamentoParcela = finPagamentoParcela;
	}

	public String getFinPagamentoStatus() {
		return this.finPagamentoStatus;
	}

	public void setFinPagamentoStatus(String finPagamentoStatus) {
		this.finPagamentoStatus = finPagamentoStatus;
	}

	public Date getFinPagamentoRealizado() {
		return this.finPagamentoRealizado;
	}

	public void setFinPagamentoRealizado(Date finPagamentoRealizado) {
		this.finPagamentoRealizado = finPagamentoRealizado;
	}

	public Double getFinPagamentoValor() {
		return this.finPagamentoValor;
	}

	public Date getFinPagamentoCadastro() {
		return finPagamentoCadastro;
	}

	public void setFinPagamentoCadastro(Date finPagamentoCadastro) {
		this.finPagamentoCadastro = finPagamentoCadastro;
	}

	public void setFinPagamentoValor(Double finPagamentoValor) {
		this.finPagamentoValor = finPagamentoValor;
	}

	public Date getFinPagamentoVencimento() {
		return this.finPagamentoVencimento;
	}

	public void setFinPagamentoVencimento(Date finPagamentoVencimento) {
		this.finPagamentoVencimento = finPagamentoVencimento;
	}

	public Date getFinPagamentoConciliado() {
		return finPagamentoConciliado;
	}

	public void setFinPagamentoConciliado(Date finPagamentoConciliado) {
		this.finPagamentoConciliado = finPagamentoConciliado;
	}

	public FinPagar getFinPagar() {
		return finPagar;
	}

	public void setFinPagar(FinPagar finPagar) {
		this.finPagar = finPagar;
	}

	public FinForma getFinForma() {
		return finForma;
	}

	public void setFinForma(FinForma finForma) {
		this.finForma = finForma;
	}

	public Number getId() {
		return finPagamentoId;
	}

	public void setId(Number id) {
		finPagamentoId = id.intValue();
	}

	public Dados getObjeto(String campo) {
		if (campo.startsWith("finPagar")) {
			return new FinPagar();
		} else if (campo.startsWith("finForma")) {
			return new FinForma();
		} else {
			return null;
		}
	}

	public void anularDependencia() {
		finPagar = null;
		finForma = null;
	}

	public String[] toArray() {
		return new String[] { finPagamentoId + "", finPagar.getFinPagarId() + "", finPagar.getEmpEmpresa().getEmpEmpresaId() + "", finPagar.getEmpEmpresa().getEmpEntidade().getEmpEntidadeNome1(),
				finPagar.getEmpEntidade().getEmpEntidadeNome1(), finPagar.getFinConta().getFinContaId() + "", finForma.getFinFormaId() + "", finForma.getFinFormaDescricao(), finPagamentoDocumento,
				finPagamentoValor.toString(), finPagamentoParcela, UtilClient.getDataGrid(finPagamentoCadastro), UtilClient.getDataGrid(finPagamentoVencimento), finPagamentoStatus,
				UtilClient.getDataGrid(finPagamentoRealizado), UtilClient.getDataGrid(finPagamentoConciliado), finPagar.getFinPagarNfe() + "", finPagamentoObservacao };
	}
}