package br.com.opensig.comercial.shared.modelo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.empresa.shared.modelo.EmpEmpresa;

/**
 * Classe que representa uma natureza de venda no sistema.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 * @since 06/10/2009
 */
@Entity
@Table(name = "com_natureza")
public class ComNatureza extends Dados implements Serializable {

	@Id
	@Column(name = "com_natureza_id")
	private int comNaturezaId;

	@Column(name = "com_natureza_cfop_trib")
	private int comNaturezaCfopTrib;

	@Column(name = "com_natureza_cfop_sub")
	private int comNaturezaCfopSub;

	@Column(name = "com_natureza_descricao")
	private String comNaturezaDescricao;

	@Column(name = "com_natureza_nome")
	private String comNaturezaNome;

	@Column(name = "com_natureza_icms")
	private int comNaturezaIcms;

	@Column(name = "com_natureza_ipi")
	private int comNaturezaIpi;

	@Column(name = "com_natureza_pis")
	private Double comNaturezaPis;

	@Column(name = "com_natureza_cofins")
	private Double comNaturezaCofins;

	@JoinColumn(name = "emp_empresa_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private EmpEmpresa empEmpresa;

	public ComNatureza() {
		this(0);
	}

	public ComNatureza(int comNaturezaId) {
		super("pu_comercial", "ComNatureza", "comNaturezaId", "comNaturezaId");
		this.comNaturezaId = comNaturezaId;
	}

	public int getComNaturezaId() {
		return this.comNaturezaId;
	}

	public void setComNaturezaId(int comNaturezaId) {
		this.comNaturezaId = comNaturezaId;
	}

	public int getComNaturezaCfopTrib() {
		return this.comNaturezaCfopTrib;
	}

	public void setComNaturezaCfopTrib(int comNaturezaCfopTrib) {
		this.comNaturezaCfopTrib = comNaturezaCfopTrib;
	}

	public int getComNaturezaCfopSub() {
		return this.comNaturezaCfopSub;
	}

	public void setComNaturezaCfopSub(int comNaturezaCfopSub) {
		this.comNaturezaCfopSub = comNaturezaCfopSub;
	}

	public String getComNaturezaDescricao() {
		return this.comNaturezaDescricao;
	}

	public void setComNaturezaDescricao(String comNaturezaDescricao) {
		this.comNaturezaDescricao = comNaturezaDescricao;
	}

	public String getComNaturezaNome() {
		return this.comNaturezaNome;
	}

	public void setComNaturezaNome(String comNaturezaNome) {
		this.comNaturezaNome = comNaturezaNome;
	}

	public boolean getComNaturezaIcms() {
		return comNaturezaIcms == 0 ? false : true;
	}

	public void setComNaturezaIcms(boolean comNaturezaIcms) {
		this.comNaturezaIcms = comNaturezaIcms == false ? 0 : 1;
	}

	public boolean getComNaturezaIpi() {
		return comNaturezaIpi == 0 ? false : true;
	}

	public void setComNaturezaIpi(boolean comNaturezaIpi) {
		this.comNaturezaIpi = comNaturezaIpi == false ? 0 : 1;
	}

	public Double getComNaturezaPis() {
		return comNaturezaPis;
	}

	public void setComNaturezaPis(Double comNaturezaPis) {
		this.comNaturezaPis = comNaturezaPis;
	}

	public Double getComNaturezaCofins() {
		return comNaturezaCofins;
	}

	public void setComNaturezaCofins(Double comNaturezaCofins) {
		this.comNaturezaCofins = comNaturezaCofins;
	}

	public EmpEmpresa getEmpEmpresa() {
		return empEmpresa;
	}

	public void setEmpEmpresa(EmpEmpresa empEmpresa) {
		this.empEmpresa = empEmpresa;
	}

	public Number getId() {
		return comNaturezaId;
	}

	public void setId(Number id) {
		comNaturezaId = id.intValue();
	}

	public String[] toArray() {
		return new String[] { comNaturezaId + "", empEmpresa.getEmpEmpresaId() + "", empEmpresa.getEmpEntidade().getEmpEntidadeNome1(), comNaturezaNome, comNaturezaDescricao,
				comNaturezaCfopTrib + "", comNaturezaCfopSub + "", getComNaturezaIcms() + "", getComNaturezaIpi() + "", comNaturezaPis.toString(), comNaturezaCofins.toString() };
	}

	public Dados getObjeto(String campo) {
		if (campo.startsWith("empEmpresa")) {
			return new EmpEmpresa();
		} else {
			return null;
		}
	}

	public void anularDependencia() {
		empEmpresa = null;
	}
}