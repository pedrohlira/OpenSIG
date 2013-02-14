package br.com.opensig.comercial.shared.rest;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.empresa.shared.modelo.EmpMunicipio;

/**
 * Classe modelo que representa o municipio no sistema OpenPDV.
 * 
 * @author Pedro H. Lira
 */
@XmlRootElement
public class SisMunicipio extends Dados implements Serializable {

	private Integer sisMunicipioId;
	private int sisMunicipioIbge;
	private String sisMunicipioDescricao;
	private SisEstado sisEstado;

	/**
	 * Construtor padrao
	 */
	public SisMunicipio() {
		this(0);
	}

	/**
	 * Construtor padrao passando o id
	 * 
	 * @param sisMunicipioId
	 *            o id do cliente
	 */
	public SisMunicipio(Integer sisMunicipioId) {
		super("", "", "");
		this.sisMunicipioId = sisMunicipioId;
	}
	
	/**
	 * Construtor padrao passando um municipio do OpenSIG
	 * 
	 * @param mun
	 *            o objeto municipio
	 */
	public SisMunicipio(EmpMunicipio mun) {
		super("", "", "");
		sisMunicipioId = mun.getEmpMunicipioId();
		sisMunicipioIbge = mun.getEmpMunicipioIbge();
		sisMunicipioDescricao = mun.getEmpMunicipioDescricao();
		sisEstado = new SisEstado(mun.getEmpEstado());
	}

	@Override
	public Number getId() {
		return this.sisMunicipioId;
	}

	@Override
	public void setId(Number id) {
		this.sisMunicipioId = id.intValue();
	}

	@Override
	public String[] toArray() {
		return null;
	}

	// GETs e SETs
	public Integer getSisMunicipioId() {
		return sisMunicipioId;
	}

	public void setSisMunicipioId(Integer sisMunicipioId) {
		this.sisMunicipioId = sisMunicipioId;
	}

	public int getSisMunicipioIbge() {
		return sisMunicipioIbge;
	}

	public void setSisMunicipioIbge(int sisMunicipioIbge) {
		this.sisMunicipioIbge = sisMunicipioIbge;
	}

	public String getSisMunicipioDescricao() {
		return sisMunicipioDescricao;
	}

	public void setSisMunicipioDescricao(String sisMunicipioDescricao) {
		this.sisMunicipioDescricao = sisMunicipioDescricao;
	}

	public SisEstado getSisEstado() {
		return sisEstado;
	}

	public void setSisEstado(SisEstado sisEstado) {
		this.sisEstado = sisEstado;
	}

}
