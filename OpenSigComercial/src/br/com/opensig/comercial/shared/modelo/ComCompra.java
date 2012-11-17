package br.com.opensig.comercial.shared.modelo;

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

import br.com.opensig.core.client.UtilClient;
import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.core.shared.modelo.EDirecao;
import br.com.opensig.empresa.shared.modelo.EmpEmpresa;
import br.com.opensig.empresa.shared.modelo.EmpEstado;
import br.com.opensig.empresa.shared.modelo.EmpFornecedor;
import br.com.opensig.financeiro.shared.modelo.FinPagar;
import br.com.opensig.fiscal.shared.modelo.FisNotaEntrada;

/**
 * Classe que representa uma compra no sistema.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 * @since 07/08/2009
 */
@Entity
@Table(name = "com_compra")
public class ComCompra extends Dados implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "com_compra_id")
	private int comCompraId;

	@Temporal(TemporalType.DATE)
	@Column(name = "com_compra_emissao")
	private Date comCompraEmissao;

	@Column(name = "com_compra_icms_base")
	private Double comCompraIcmsBase;

	@Column(name = "com_compra_icms_valor")
	private Double comCompraIcmsValor;

	@Column(name = "com_compra_icmssub_base")
	private Double comCompraIcmssubBase;

	@Column(name = "com_compra_icmssub_valor")
	private Double comCompraIcmssubValor;

	@Column(name = "com_compra_numero")
	private int comCompraNumero;

	@Column(name = "com_compra_observacao")
	private String comCompraObservacao;

	@Temporal(TemporalType.DATE)
	@Column(name = "com_compra_recebimento")
	private Date comCompraRecebimento;

	@Column(name = "com_compra_serie")
	private int comCompraSerie;

	@Column(name = "com_compra_valor_desconto")
	private Double comCompraValorDesconto;

	@Column(name = "com_compra_valor_frete")
	private Double comCompraValorFrete;

	@Column(name = "com_compra_valor_ipi")
	private Double comCompraValorIpi;

	@Column(name = "com_compra_valor_nota")
	private Double comCompraValorNota;

	@Column(name = "com_compra_valor_outros")
	private Double comCompraValorOutros;

	@Column(name = "com_compra_valor_produto")
	private Double comCompraValorProduto;

	@Column(name = "com_compra_fechada")
	private int comCompraFechada;

	@Column(name = "com_compra_paga")
	private int comCompraPaga;

	@Column(name = "com_compra_nfe")
	private int comCompraNfe;

	@Column(name = "com_compra_valor_seguro")
	private Double comCompraValorSeguro;

	@JoinColumn(name = "emp_fornecedor_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private EmpFornecedor empFornecedor;

	@JoinColumn(name = "emp_empresa_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private EmpEmpresa empEmpresa;

	@JoinColumn(name = "com_natureza_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private ComNatureza comNatureza;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "emp_estado_id")
	private EmpEstado empEstado;

	@JoinColumn(name = "fin_pagar_id")
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private FinPagar finPagar;

	@JoinColumn(name = "fis_nota_entrada_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private FisNotaEntrada fisNotaEntrada;

	@OneToMany(mappedBy = "comCompra", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private List<ComCompraProduto> comCompraProdutos;

	public ComCompra() {
		this(0);
	}

	public ComCompra(int comCompraId) {
		super("pu_comercial", "ComCompra", "comCompraId", "comCompraRecebimento", EDirecao.DESC);
		this.comCompraId = comCompraId;
	}

	public int getComCompraId() {
		return this.comCompraId;
	}

	public void setComCompraId(int comCompraId) {
		this.comCompraId = comCompraId;
	}

	public Date getComCompraEmissao() {
		return this.comCompraEmissao;
	}

	public void setComCompraEmissao(Date comCompraEmissao) {
		this.comCompraEmissao = comCompraEmissao;
	}

	public Double getComCompraIcmsBase() {
		return this.comCompraIcmsBase;
	}

	public void setComCompraIcmsBase(Double comCompraIcmsBase) {
		this.comCompraIcmsBase = comCompraIcmsBase;
	}

	public Double getComCompraIcmsValor() {
		return this.comCompraIcmsValor;
	}

	public void setComCompraIcmsValor(Double comCompraIcmsValor) {
		this.comCompraIcmsValor = comCompraIcmsValor;
	}

	public Double getComCompraIcmssubBase() {
		return this.comCompraIcmssubBase;
	}

	public void setComCompraIcmssubBase(Double comCompraIcmssubBase) {
		this.comCompraIcmssubBase = comCompraIcmssubBase;
	}

	public Double getComCompraIcmssubValor() {
		return this.comCompraIcmssubValor;
	}

	public void setComCompraIcmssubValor(Double comCompraIcmssubValor) {
		this.comCompraIcmssubValor = comCompraIcmssubValor;
	}

	public ComNatureza getComNatureza() {
		return comNatureza;
	}

	public void setComNatureza(ComNatureza comNatureza) {
		this.comNatureza = comNatureza;
	}

	public int getComCompraNumero() {
		return this.comCompraNumero;
	}

	public void setComCompraNumero(int comCompraNumero) {
		this.comCompraNumero = comCompraNumero;
	}

	public String getComCompraObservacao() {
		return this.comCompraObservacao;
	}

	public void setComCompraObservacao(String comCompraObservacao) {
		this.comCompraObservacao = comCompraObservacao;
	}

	public Date getComCompraRecebimento() {
		return this.comCompraRecebimento;
	}

	public void setComCompraRecebimento(Date comCompraRecebimento) {
		this.comCompraRecebimento = comCompraRecebimento;
	}

	public int getComCompraSerie() {
		return this.comCompraSerie;
	}

	public void setComCompraSerie(int comCompraSerie) {
		this.comCompraSerie = comCompraSerie;
	}

	public Double getComCompraValorDesconto() {
		return this.comCompraValorDesconto;
	}

	public void setComCompraValorDesconto(Double comCompraValorDesconto) {
		this.comCompraValorDesconto = comCompraValorDesconto;
	}

	public Double getComCompraValorFrete() {
		return this.comCompraValorFrete;
	}

	public void setComCompraValorFrete(Double comCompraValorFrete) {
		this.comCompraValorFrete = comCompraValorFrete;
	}

	public Double getComCompraValorIpi() {
		return this.comCompraValorIpi;
	}

	public void setComCompraValorIpi(Double comCompraValorIpi) {
		this.comCompraValorIpi = comCompraValorIpi;
	}

	public Double getComCompraValorNota() {
		return this.comCompraValorNota;
	}

	public void setComCompraValorNota(Double comCompraValorNota) {
		this.comCompraValorNota = comCompraValorNota;
	}

	public Double getComCompraValorOutros() {
		return this.comCompraValorOutros;
	}

	public void setComCompraValorOutros(Double comCompraValorOutros) {
		this.comCompraValorOutros = comCompraValorOutros;
	}

	public Double getComCompraValorProduto() {
		return this.comCompraValorProduto;
	}

	public void setComCompraValorProduto(Double comCompraValorProduto) {
		this.comCompraValorProduto = comCompraValorProduto;
	}

	public Double getComCompraValorSeguro() {
		return this.comCompraValorSeguro;
	}

	public void setComCompraValorSeguro(Double comCompraValorSeguro) {
		this.comCompraValorSeguro = comCompraValorSeguro;
	}

	public boolean getComCompraFechada() {
		return comCompraFechada == 0 ? false : true;
	}

	public void setComCompraFechada(boolean comCompraFechada) {
		this.comCompraFechada = comCompraFechada == false ? 0 : 1;
	}

	public boolean getComCompraPaga() {
		return comCompraPaga == 0 ? false : true;
	}

	public void setComCompraPaga(boolean comCompraPaga) {
		this.comCompraPaga = comCompraPaga == false ? 0 : 1;
	}

	public boolean getComCompraNfe() {
		return comCompraNfe == 0 ? false : true;
	}

	public void setComCompraNfe(boolean comCompraNfe) {
		this.comCompraNfe = comCompraNfe == false ? 0 : 1;
	}

	public EmpFornecedor getEmpFornecedor() {
		return this.empFornecedor;
	}

	public void setEmpFornecedor(EmpFornecedor empFornecedor) {
		this.empFornecedor = empFornecedor;
	}

	public List<ComCompraProduto> getComCompraProdutos() {
		return this.comCompraProdutos;
	}

	public void setComCompraProdutos(List<ComCompraProduto> comCompraProdutos) {
		this.comCompraProdutos = comCompraProdutos;
	}

	public EmpEmpresa getEmpEmpresa() {
		return empEmpresa;
	}

	public void setEmpEmpresa(EmpEmpresa empEmpresa) {
		this.empEmpresa = empEmpresa;
	}

	public EmpEstado getEmpEstado() {
		return empEstado;
	}

	public void setEmpEstado(EmpEstado empEstado) {
		this.empEstado = empEstado;
	}

	public FinPagar getFinPagar() {
		return finPagar;
	}

	public void setFinPagar(FinPagar finPagar) {
		this.finPagar = finPagar;
	}

	public FisNotaEntrada getFisNotaEntrada() {
		return fisNotaEntrada;
	}

	public void setFisNotaEntrada(FisNotaEntrada fisNotaEntrada) {
		this.fisNotaEntrada = fisNotaEntrada;
	}

	public Number getId() {
		return comCompraId;
	}

	public void setId(Number id) {
		comCompraId = id.intValue();
	}

	public String[] toArray() {
		int pagarId = finPagar == null ? 0 : finPagar.getFinPagarId();
		int nfeId = fisNotaEntrada == null ? 0 : fisNotaEntrada.getFisNotaEntradaId();
		int contaId = finPagar == null ? 0 : finPagar.getFinConta().getFinContaId();

		return new String[] { comCompraId + "", comCompraSerie + "", comCompraNumero + "", comNatureza.getComNaturezaId() + "", comNatureza.getComNaturezaNome(), UtilClient.getDataGrid(comCompraEmissao), UtilClient.getDataGrid(comCompraRecebimento),
				empFornecedor.getEmpFornecedorId() + "", empFornecedor.getEmpEntidade().getEmpEntidadeId() + "", empFornecedor.getEmpEntidade().getEmpEntidadeNome1(),
				empEmpresa.getEmpEmpresaId() + "", empEmpresa.getEmpEntidade().getEmpEntidadeNome1(), empEstado.getEmpEstadoId() + "", empEstado.getEmpEstadoDescricao(), comCompraIcmsBase.toString(),
				comCompraIcmsValor.toString(), comCompraIcmssubBase.toString(), comCompraIcmssubValor.toString(), comCompraValorProduto.toString(), comCompraValorFrete.toString(),
				comCompraValorSeguro.toString(), comCompraValorDesconto.toString(), comCompraValorIpi.toString(), comCompraValorOutros.toString(), comCompraValorNota.toString(),
				getComCompraFechada() + "", contaId + "", pagarId + "", getComCompraPaga() + "", nfeId + "", getComCompraNfe() + "", comCompraObservacao };
	}

	public Dados getObjeto(String campo) {
		if (campo.startsWith("empFornecedor")) {
			return new EmpFornecedor();
		} else if (campo.startsWith("empEmpresa")) {
			return new EmpEmpresa();
		} else if (campo.startsWith("empEstado")) {
			return new EmpEstado();
		} else if (campo.startsWith("comNatureza")) {
			return new ComNatureza();
		} else if (campo.startsWith("finPagar")) {
			return new FinPagar();
		} else if (campo.startsWith("fisNotaEntrada")) {
			return new FisNotaEntrada();
		} else {
			return null;
		}
	}

	public void anularDependencia() {
		empFornecedor = null;
		empEmpresa = null;
		empEstado = null;
		comNatureza = null;
		comCompraProdutos = null;
		finPagar = null;
		fisNotaEntrada = null;
	}
}