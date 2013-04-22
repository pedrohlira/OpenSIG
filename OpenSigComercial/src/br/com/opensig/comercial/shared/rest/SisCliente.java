package br.com.opensig.comercial.shared.rest;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.empresa.shared.modelo.EmpCliente;
import br.com.opensig.empresa.shared.modelo.EmpContato;
import br.com.opensig.empresa.shared.modelo.EmpEndereco;
import br.com.opensig.empresa.shared.modelo.EmpEntidade;

/**
 * Classe modelo que representa o cliente no sistema OpenPDV.
 * 
 * @author Pedro H. Lira
 */
@XmlRootElement
public class SisCliente extends Dados implements Serializable {

	private Integer sisClienteId;
	private String sisClienteDoc;
	private String sisClienteDoc1;
	private String sisClienteNome;
	private String sisClienteEndereco;
	private int sisClienteNumero;
	private String sisClienteComplemento;
	private String sisClienteBairro;
	private String sisClienteCep;
	private String sisClienteTelefone;
	private String sisClienteEmail;
	private Date sisClienteData;
	private SisMunicipio sisMunicipio;

	/**
	 * Construtor padrao
	 */
	public SisCliente() {
		this(0);
	}

	/**
	 * Construtor padrao passando o id
	 * 
	 * @param sisClienteId
	 *            o id do cliente
	 */
	public SisCliente(Integer sisClienteId) {
		super("", "", "");
		this.sisClienteId = sisClienteId;
	}

	/**
	 * Construtor padrao passando um cliente do OpenSIG
	 * 
	 * @param cli
	 *            o objeto cliente
	 */
	public SisCliente(EmpCliente cli) {
		super("", "", "");
		EmpEntidade ent = cli.getEmpEntidade();
		sisClienteId = cli.getEmpClienteId();
		sisClienteDoc = ent.getEmpEntidadeDocumento1();
		sisClienteDoc1 = ent.getEmpEntidadeDocumento2();
		sisClienteNome = ent.getEmpEntidadeNome1();
		sisClienteData = ent.getEmpEntidadeData();

		if (ent.getEmpEnderecos().size() > 0) {
			EmpEndereco ende = ent.getEmpEnderecos().get(0);
			sisClienteEndereco = ende.getEmpEnderecoLogradouro();
			sisClienteNumero = ende.getEmpEnderecoNumero();
			sisClienteComplemento = ende.getEmpEnderecoComplemento();
			sisClienteBairro = ende.getEmpEnderecoBairro();
			sisClienteCep = ende.getEmpEnderecoCep();
			sisMunicipio = new SisMunicipio(ende.getEmpMunicipio());
		}

		if (ent.getEmpContatos().size() > 0) {
			for (EmpContato cont : ent.getEmpContatos()) {
				if (cont.getEmpContatoDescricao().contains("@")) {
					sisClienteEmail = cont.getEmpContatoDescricao();
				} else {
					sisClienteTelefone = cont.getEmpContatoDescricao();
				}
			}
		}
	}

	@Override
	public Number getId() {
		return this.sisClienteId;
	}

	@Override
	public void setId(Number id) {
		this.sisClienteId = id.intValue();
	}

	public Integer getSisClienteId() {
		return sisClienteId;
	}

	public void setSisClienteId(Integer sisClienteId) {
		this.sisClienteId = sisClienteId;
	}

	public String getSisClienteDoc() {
		return sisClienteDoc;
	}

	public void setSisClienteDoc(String sisClienteDoc) {
		this.sisClienteDoc = sisClienteDoc;
	}

	public String getSisClienteNome() {
		return sisClienteNome;
	}

	public void setSisClienteNome(String sisClienteNome) {
		this.sisClienteNome = sisClienteNome;
	}

	public String getSisClienteEndereco() {
		return sisClienteEndereco;
	}

	public void setSisClienteEndereco(String sisClienteEndereco) {
		this.sisClienteEndereco = sisClienteEndereco;
	}

	public String getSisClienteDoc1() {
		return sisClienteDoc1;
	}

	public void setSisClienteDoc1(String sisClienteDoc1) {
		this.sisClienteDoc1 = sisClienteDoc1;
	}

	public int getSisClienteNumero() {
		return sisClienteNumero;
	}

	public void setSisClienteNumero(int sisClienteNumero) {
		this.sisClienteNumero = sisClienteNumero;
	}

	public String getSisClienteComplemento() {
		return sisClienteComplemento;
	}

	public void setSisClienteComplemento(String sisClienteComplemento) {
		this.sisClienteComplemento = sisClienteComplemento;
	}

	public String getSisClienteBairro() {
		return sisClienteBairro;
	}

	public void setSisClienteBairro(String sisClienteBairro) {
		this.sisClienteBairro = sisClienteBairro;
	}

	public String getSisClienteCep() {
		return sisClienteCep;
	}

	public void setSisClienteCep(String sisClienteCep) {
		this.sisClienteCep = sisClienteCep;
	}

	public String getSisClienteTelefone() {
		return sisClienteTelefone;
	}

	public void setSisClienteTelefone(String sisClienteTelefone) {
		this.sisClienteTelefone = sisClienteTelefone;
	}

	public String getSisClienteEmail() {
		return sisClienteEmail;
	}

	public void setSisClienteEmail(String sisClienteEmail) {
		this.sisClienteEmail = sisClienteEmail;
	}

	public Date getSisClienteData() {
		return sisClienteData;
	}

	public void setSisClienteData(Date sisClienteData) {
		this.sisClienteData = sisClienteData;
	}

	public SisMunicipio getSisMunicipio() {
		return sisMunicipio;
	}

	public void setSisMunicipio(SisMunicipio sisMunicipio) {
		this.sisMunicipio = sisMunicipio;
	}

	@Override
	public String[] toArray() {
		return null;
	}

}
