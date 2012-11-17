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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.empresa.shared.modelo.EmpEmpresa;

/**
 * Classe que representa um grupo no sistema.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 * @since 15/10/2008
 */
@Entity
@Table(name = "sis_grupo")
public class SisGrupo extends Dados implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "sis_grupo_id")
	private int sisGrupoId;

	@Column(name = "sis_grupo_ativo")
	private int sisGrupoAtivo;

	@Column(name = "sis_grupo_descricao")
	private String sisGrupoDescricao;

	@Column(name = "sis_grupo_nome")
	private String sisGrupoNome;

	@Column(name = "sis_grupo_sistema")
	private int sisGrupoSistema;

	@Column(name = "sis_grupo_desconto")
	private int sisGrupoDesconto;

	@JoinColumn(name = "emp_empresa_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private EmpEmpresa empEmpresa;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "sis_grupo_usuario", joinColumns = { @JoinColumn(name = "sis_grupo_id", referencedColumnName = "sis_grupo_id") }, inverseJoinColumns = { @JoinColumn(name = "sis_usuario_id", referencedColumnName = "sis_usuario_id") })
	private List<SisUsuario> sisUsuarios;

	@OneToMany(mappedBy = "sisGrupo", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private List<SisPermissao> sisPermissoes;

	public SisGrupo() {
		this(0);
	}

	public SisGrupo(int sisGrupoId) {
		super("pu_permissao", "SisGrupo", "sisGrupoId", "sisGrupoNome");
		this.sisGrupoId = sisGrupoId;
	}

	public int getSisGrupoId() {
		return this.sisGrupoId;
	}

	public void setSisGrupoId(int sisGrupoId) {
		this.sisGrupoId = sisGrupoId;
	}

	public String getSisGrupoDescricao() {
		return this.sisGrupoDescricao;
	}

	public void setSisGrupoDescricao(String sisGrupoDescricao) {
		this.sisGrupoDescricao = sisGrupoDescricao;
	}

	public String getSisGrupoNome() {
		return this.sisGrupoNome;
	}

	public void setSisGrupoNome(String sisGrupoNome) {
		this.sisGrupoNome = sisGrupoNome;
	}

	public boolean getSisGrupoAtivo() {
		return sisGrupoAtivo == 0 ? false : true;
	}

	public void setSisGrupoAtivo(boolean sisGrupoAtivo) {
		this.sisGrupoAtivo = sisGrupoAtivo == false ? 0 : 1;
	}

	public boolean getSisGrupoSistema() {
		return sisGrupoSistema == 0 ? false : true;
	}

	public void setSisGrupoSistema(boolean sisGrupoSistema) {
		this.sisGrupoSistema = sisGrupoSistema == false ? 0 : 1;
	}

	public List<SisPermissao> getSisPermissoes() {
		return this.sisPermissoes;
	}

	public void setSisPermissoes(List<SisPermissao> sisPermissoes) {
		this.sisPermissoes = sisPermissoes;
	}

	public int getSisGrupoDesconto() {
		return sisGrupoDesconto;
	}

	public void setSisGrupoDesconto(int sisGrupoDesconto) {
		this.sisGrupoDesconto = sisGrupoDesconto;
	}

	public void setSisGrupoAtivo(int sisGrupoAtivo) {
		this.sisGrupoAtivo = sisGrupoAtivo;
	}

	public void setSisGrupoSistema(int sisGrupoSistema) {
		this.sisGrupoSistema = sisGrupoSistema;
	}

	public List<SisUsuario> getSisUsuarios() {
		return sisUsuarios;
	}

	public void setSisUsuarios(List<SisUsuario> sisUsuarios) {
		this.sisUsuarios = sisUsuarios;
	}

	public EmpEmpresa getEmpEmpresa() {
		return empEmpresa;
	}

	public void setEmpEmpresa(EmpEmpresa empEmpresa) {
		this.empEmpresa = empEmpresa;
	}

	public boolean getAtivo() {
		return getSisGrupoAtivo();
	}

	public boolean getSistema() {
		return getSisGrupoSistema();
	}

	public Number getId() {
		return sisGrupoId;
	}

	public void setId(Number id) {
		sisGrupoId = id.intValue();
	}

	public String[] toArray() {
		StringBuffer per = new StringBuffer();
		for (SisPermissao sisPermissao : sisPermissoes) {
			per.append(sisPermissao.getSisPermissaoId() + "," + sisPermissao.getSisModuloId() + "," + sisPermissao.getSisFuncaoId() + "," + sisPermissao.getSisAcaoId() + ","
					+ sisPermissao.getSisExecutar() + "::");
		}

		StringBuffer user = new StringBuffer();
		for (SisUsuario sisUsuario : sisUsuarios) {
			user.append(sisUsuario.getSisUsuarioId() + "::");
		}

		return new String[] { sisGrupoId + "", empEmpresa.getEmpEmpresaId() + "", empEmpresa.getEmpEntidade().getEmpEntidadeNome1(), sisGrupoNome, sisGrupoDescricao, sisGrupoDesconto + "",
				getSisGrupoAtivo() + "", getSisGrupoSistema() + "", per.toString(), user.toString() };
	}

	public String toString() {
		return sisGrupoId + "-" + sisGrupoNome;
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
		sisPermissoes = null;
		sisUsuarios = null;
	}

	public static SisGrupo getGrupo(String[] grp) {
		SisGrupo grupo = new SisGrupo();
		grupo.setSisGrupoId(Integer.valueOf(grp[0]));
		grupo.setEmpEmpresa(new EmpEmpresa(Integer.valueOf(grp[1])));
		grupo.setSisGrupoNome(grp[3]);
		grupo.setSisGrupoDescricao(grp[4]);
		grupo.setSisGrupoDesconto(Integer.valueOf(grp[5]));
		grupo.setSisGrupoAtivo(Boolean.valueOf(grp[6]));
		grupo.setSisGrupoSistema(Boolean.valueOf(grp[7]));
		grupo.setSisPermissoes(SisPermissao.getPermissoes(grp[8]));
		// usuarios
		if (grp[9] != null && !grp[9].equals("")) {
			List<SisUsuario> usuarios = new ArrayList<SisUsuario>();
			for (String usuario : grp[9].split("::")) {
				usuarios.add(new SisUsuario(Integer.valueOf(usuario)));
			}
			grupo.setSisUsuarios(usuarios);
		}

		return grupo;
	}
}