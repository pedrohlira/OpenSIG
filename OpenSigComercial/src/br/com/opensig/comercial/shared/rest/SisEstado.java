package br.com.opensig.comercial.shared.rest;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.empresa.shared.modelo.EmpEstado;

/**
 * Classe modelo que representa o estado no sistema OpenPDV.
 * 
 * @author Pedro H. Lira
 */
@XmlRootElement
public class SisEstado extends Dados implements Serializable {

	private Integer sisEstadoId;
	private int sisEstadoIbge;
	private String sisEstadoDescricao;
	private String sisEstadoSigla;

	/**
	 * Construtor padrao
	 */
	public SisEstado() {
		this(0);
	}

	/**
	 * Construtor padrao passando o id
	 * 
	 * @param sisEstadoId
	 *            o id do estado.
	 */
	public SisEstado(Integer sisEstadoId) {
		super("", "", "");
		this.sisEstadoId = sisEstadoId;
	}
	
	/**
	 * Construtor padrao passando um estado do OpenSIG
	 * 
	 * @param mun
	 *            o objeto esatdo
	 */
	public SisEstado(EmpEstado est) {
		super("", "", "");
		sisEstadoId = est.getEmpEstadoId();
		sisEstadoIbge = est.getEmpEstadoIbge();
		sisEstadoDescricao = est.getEmpEstadoDescricao();
		sisEstadoSigla = est.getEmpEstadoSigla();
	}

	@Override
	public Number getId() {
		return this.sisEstadoId;
	}

	@Override
	public void setId(Number id) {
		this.sisEstadoId = id.intValue();
	}

	@Override
	public String[] toArray() {
		return null;
	}

	public Integer getSisEstadoId() {
		return sisEstadoId;
	}

	public void setSisEstadoId(Integer sisEstadoId) {
		this.sisEstadoId = sisEstadoId;
	}

	public int getSisEstadoIbge() {
		return sisEstadoIbge;
	}

	public void setSisEstadoIbge(int sisEstadoIbge) {
		this.sisEstadoIbge = sisEstadoIbge;
	}

	public String getSisEstadoDescricao() {
		return sisEstadoDescricao;
	}

	public void setSisEstadoDescricao(String sisEstadoDescricao) {
		this.sisEstadoDescricao = sisEstadoDescricao;
	}

	public String getSisEstadoSigla() {
		return sisEstadoSigla;
	}

	public void setSisEstadoSigla(String sisEstadoSigla) {
		this.sisEstadoSigla = sisEstadoSigla;
	}
}
