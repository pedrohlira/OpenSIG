package br.com.opensig.empresa.shared.modelo;

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

import br.com.opensig.core.shared.modelo.Dados;

/**
 * Classe que representa um endereço no sistema.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 * @since 09/06/2009
 */
@Entity
@Table(name = "emp_endereco")
public class EmpEndereco extends Dados implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "emp_endereco_id")
	private int empEnderecoId;

	@Column(name = "emp_endereco_bairro")
	private String empEnderecoBairro;

	@Column(name = "emp_endereco_cep")
	private String empEnderecoCep;

	@Column(name = "emp_endereco_complemento")
	private String empEnderecoComplemento;

	@Column(name = "emp_endereco_logradouro")
	private String empEnderecoLogradouro;

	@Column(name = "emp_endereco_numero")
	private int empEnderecoNumero;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "emp_entidade_id")
	private EmpEntidade empEntidade;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "emp_endereco_tipo_id")
	private EmpEnderecoTipo empEnderecoTipo;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "emp_municipio_id")
	private EmpMunicipio empMunicipio;

	public EmpEndereco() {
		this(0);
	}

	public EmpEndereco(int empEnderecoId) {
		super("pu_empresa", "EmpEndereco", "empEnderecoId", "empEnderecoLogradouro");
		this.empEnderecoId = empEnderecoId;
	}

	public int getEmpEnderecoId() {
		return this.empEnderecoId;
	}

	public void setEmpEnderecoId(int empEnderecoId) {
		this.empEnderecoId = empEnderecoId;
	}

	public String getEmpEnderecoBairro() {
		return this.empEnderecoBairro;
	}

	public void setEmpEnderecoBairro(String empEnderecoBairro) {
		this.empEnderecoBairro = empEnderecoBairro;
	}

	public String getEmpEnderecoCep() {
		return this.empEnderecoCep;
	}

	public void setEmpEnderecoCep(String empEnderecoCep) {
		this.empEnderecoCep = empEnderecoCep;
	}

	public String getEmpEnderecoComplemento() {
		return this.empEnderecoComplemento;
	}

	public void setEmpEnderecoComplemento(String empEnderecoComplemento) {
		this.empEnderecoComplemento = empEnderecoComplemento;
	}

	public String getEmpEnderecoLogradouro() {
		return this.empEnderecoLogradouro;
	}

	public void setEmpEnderecoLogradouro(String empEnderecoLogradouro) {
		this.empEnderecoLogradouro = empEnderecoLogradouro;
	}

	public int getEmpEnderecoNumero() {
		return this.empEnderecoNumero;
	}

	public void setEmpEnderecoNumero(int empEnderecoNumero) {
		this.empEnderecoNumero = empEnderecoNumero;
	}

	public EmpEntidade getEmpEntidade() {
		return this.empEntidade;
	}

	public void setEmpEntidade(EmpEntidade empEntidade) {
		this.empEntidade = empEntidade;
	}

	public EmpEnderecoTipo getEmpEnderecoTipo() {
		return this.empEnderecoTipo;
	}

	public void setEmpEnderecoTipo(EmpEnderecoTipo empEnderecoTipo) {
		this.empEnderecoTipo = empEnderecoTipo;
	}

	public EmpMunicipio getEmpMunicipio() {
		return empMunicipio;
	}

	public void setEmpMunicipio(EmpMunicipio empMunicipio) {
		this.empMunicipio = empMunicipio;
	}

	public Number getId() {
		return empEnderecoId;
	}

	public void setId(Number id) {
		empEnderecoId = id.intValue();
	}

	public String[] toArray() {
		return new String[] { empEnderecoId + "", empEnderecoTipo.getEmpEnderecoTipoId() + "", empEnderecoTipo.getEmpEnderecoTipoDescricao(),
				empMunicipio.getEmpEstado().getEmpPais().getEmpPaisDescricao(), empMunicipio.getEmpEstado().getEmpEstadoDescricao(), empMunicipio.getEmpMunicipioId() + "",
				empMunicipio.getEmpMunicipioDescricao(), empEnderecoLogradouro, empEnderecoNumero + "", empEnderecoComplemento, empEnderecoBairro, empEnderecoCep };
	}

	public Dados getObjeto(String campo) {
		if (campo.startsWith("empEnderecoTipo")) {
			return new EmpEnderecoTipo();
		} else if (campo.startsWith("empMunicipio")) {
			return new EmpMunicipio();
		} else {
			return null;
		}
	}

	public void anularDependencia() {
		empEnderecoTipo = null;
		empEntidade = null;
		empMunicipio = null;
	}
}