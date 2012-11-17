package br.com.opensig.permissao.shared.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import br.com.opensig.core.shared.modelo.Colecao;
import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.empresa.shared.modelo.EmpEmpresa;

/**
 * Classe que representa um usuario no sistema.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 * @since 06/10/2008
 */
@Entity
@Table(name = "sis_usuario")
@XmlRootElement
public class SisUsuario extends Dados implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "sis_usuario_id")
	private int sisUsuarioId;

	@Column(name = "sis_usuario_ativo")
	@XmlElement(type = Boolean.class)
	private int sisUsuarioAtivo;

	@Transient
	private boolean sisUsuarioCaixa;
	
	@Transient
	private boolean sisUsuarioGerente;
	
	@Column(name = "sis_usuario_login")
	private String sisUsuarioLogin;

	@Column(name = "sis_usuario_senha")
	private String sisUsuarioSenha;

	@Column(name = "sis_usuario_sistema")
	@XmlTransient
	private int sisUsuarioSistema;

	@Column(name = "sis_usuario_desconto")
	private int sisUsuarioDesconto;

	@Column(name = "sis_usuario_email")
	@XmlTransient
	private String sisUsuarioEmail;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "sis_usuario_empresa", joinColumns = { @JoinColumn(name = "sis_usuario_id", referencedColumnName = "sis_usuario_id") }, inverseJoinColumns = { @JoinColumn(name = "emp_empresa_id", referencedColumnName = "emp_empresa_id") })
	@XmlTransient
	private List<EmpEmpresa> empEmpresas;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "sis_grupo_usuario", joinColumns = { @JoinColumn(name = "sis_usuario_id", referencedColumnName = "sis_usuario_id") }, inverseJoinColumns = { @JoinColumn(name = "sis_grupo_id", referencedColumnName = "sis_grupo_id") })
	@XmlTransient
	private List<SisGrupo> sisGrupos;

	@OneToMany(mappedBy = "sisUsuario", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@XmlTransient
	private List<SisPermissao> sisPermissoes;

	public SisUsuario() {
		this(0);
	}

	public SisUsuario(int sisUsuarioId) {
		super("pu_permissao", "SisUsuario", "sisUsuarioId", "sisUsuarioLogin");
		this.sisUsuarioId = sisUsuarioId;
		Colecao col = new Colecao("EmpEmpresa", "t.empEmpresas", "JOIN", "t1");
		setColecao(new Colecao[] { col });
	}

	public int getSisUsuarioId() {
		return this.sisUsuarioId;
	}

	public void setSisUsuarioId(int sisUsuarioId) {
		this.sisUsuarioId = sisUsuarioId;
	}

	public String getSisUsuarioLogin() {
		return this.sisUsuarioLogin;
	}

	public void setSisUsuarioLogin(String sisUsuarioLogin) {
		this.sisUsuarioLogin = sisUsuarioLogin;
	}

	public String getSisUsuarioSenha() {
		return this.sisUsuarioSenha;
	}

	public void setSisUsuarioSenha(String sisUsuarioSenha) {
		this.sisUsuarioSenha = sisUsuarioSenha;
	}

	public boolean getSisUsuarioAtivo() {
		return sisUsuarioAtivo == 0 ? false : true;
	}

	public void setSisUsuarioAtivo(boolean sisUsuarioAtivo) {
		this.sisUsuarioAtivo = sisUsuarioAtivo == false ? 0 : 1;
	}

	public boolean getSisUsuarioCaixa(){
		return sisUsuarioCaixa;
	}
	
	public void setSisUsuarioCaixa(boolean sisUsuarioCaixa) {
		this.sisUsuarioCaixa = sisUsuarioCaixa;
	}
	
	public boolean getSisUsuarioGerente() {
		return sisUsuarioGerente;
	}

	public void setSisUsuarioGerente(boolean sisUsuarioGerente) {
		this.sisUsuarioGerente = sisUsuarioGerente;
	}

	public boolean getSisUsuarioSistema() {
		return sisUsuarioSistema == 0 ? false : true;
	}

	public void setSisUsuarioSistema(boolean sisUsuarioSistema) {
		this.sisUsuarioSistema = sisUsuarioSistema == false ? 0 : 1;
	}

	public String getSisUsuarioEmail() {
		return sisUsuarioEmail;
	}

	public void setSisUsuarioEmail(String sisUsuarioEmail) {
		this.sisUsuarioEmail = sisUsuarioEmail;
	}

	public List<EmpEmpresa> getEmpEmpresas() {
		return empEmpresas;
	}

	public void setEmpEmpresas(List<EmpEmpresa> empEmpresas) {
		this.empEmpresas = empEmpresas;
	}

	public List<SisGrupo> getSisGrupos() {
		return this.sisGrupos;
	}

	public void setSisGrupos(List<SisGrupo> sisGrupos) {
		this.sisGrupos = sisGrupos;
	}

	public List<SisPermissao> getSisPermissoes() {
		return this.sisPermissoes;
	}

	public void setSisPermissoes(List<SisPermissao> sisPermissoes) {
		this.sisPermissoes = sisPermissoes;
	}

	public boolean getAtivo() {
		return getSisUsuarioAtivo();
	}

	public void setSisUsuarioAtivo(int sisUsuarioAtivo) {
		this.sisUsuarioAtivo = sisUsuarioAtivo;
	}

	public boolean getSistema() {
		return getSisUsuarioSistema();
	}

	public void setSisUsuarioSistema(int sisUsuarioSistema) {
		this.sisUsuarioSistema = sisUsuarioSistema;
	}

	public int getSisUsuarioDesconto() {
		return sisUsuarioDesconto;
	}

	public void setSisUsuarioDesconto(int sisUsuarioDesconto) {
		this.sisUsuarioDesconto = sisUsuarioDesconto;
	}

	public Number getId() {
		return sisUsuarioId;
	}

	public void setId(Number id) {
		sisUsuarioId = id.intValue();
	}

	public String[] toArray() {
		StringBuffer grp = new StringBuffer();
		StringBuffer grpId = new StringBuffer("");
		for (SisGrupo sisGrupo : sisGrupos) {
			grpId.append(sisGrupo.getSisGrupoId() + "::");
			grp.append(sisGrupo.getSisGrupoNome() + "::");
		}

		StringBuffer emp = new StringBuffer("");
		StringBuffer empId = new StringBuffer("");
		for (EmpEmpresa empEmpresa : empEmpresas) {
			empId.append(empEmpresa.getEmpEmpresaId() + "::");
			emp.append(empEmpresa.getEmpEntidade().getEmpEntidadeNome1() + "::");
		}

		StringBuffer per = new StringBuffer("");
		if (sisPermissoes != null) {
			for (SisPermissao sisPermissao : sisPermissoes) {
				per.append(sisPermissao.getSisPermissaoId() + "," + sisPermissao.getSisModuloId() + "," + sisPermissao.getSisFuncaoId() + "," + sisPermissao.getSisAcaoId() + ","
						+ sisPermissao.getSisExecutar() + "::");
			}
		}

		return new String[] { sisUsuarioId + "", sisUsuarioLogin, sisUsuarioSenha, sisUsuarioDesconto + "", sisUsuarioEmail, emp.toString(), grp.toString(), getSisUsuarioAtivo() + "",
				getSisUsuarioSistema() + "", grpId.toString(), empId.toString(), per.toString() };
	}

	public String toString() {
		return sisUsuarioLogin;
	}

	public void anularDependencia() {
		sisGrupos = null;
		sisPermissoes = null;
		empEmpresas = null;
	}

	public static SisUsuario getUsuario(String[] usu) {
		SisUsuario usuario = new SisUsuario();
		usuario.setSisUsuarioId(Integer.valueOf(usu[0]));
		usuario.setSisUsuarioLogin(usu[1]);
		usuario.setSisUsuarioSenha(usu[2]);
		usuario.setSisUsuarioDesconto(Integer.valueOf(usu[3]));
		usuario.setSisUsuarioEmail(usu[4]);
		usuario.setSisUsuarioAtivo(Boolean.valueOf(usu[7]));
		usuario.setSisUsuarioSistema(Boolean.valueOf(usu[8]));
		// grupos
		List<SisGrupo> grupos = new ArrayList<SisGrupo>();
		for (String id : usu[9].split("::")) {
			if (!id.equals("")) {
				grupos.add(new SisGrupo(Integer.valueOf(id)));
			}
		}
		usuario.setSisGrupos(grupos);
		// empresas
		List<EmpEmpresa> empresas = new ArrayList<EmpEmpresa>();
		for (String id : usu[10].split("::")) {
			if (!id.equals("")) {
				empresas.add(new EmpEmpresa(Integer.valueOf(id)));
			}
		}
		usuario.setEmpEmpresas(empresas);
		usuario.setSisPermissoes(SisPermissao.getPermissoes(usu[11]));
		return usuario;
	}
}