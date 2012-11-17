package br.com.opensig.permissao.shared.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
 * Classe que representa uma permissao no sistema.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 * @since 15/10/2008
 */
@Entity
@Table(name = "sis_permissao")
public class SisPermissao extends Dados implements Serializable, Comparable<SisPermissao> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "sis_permissao_id")
	private int sisPermissaoId;

	@Column(name = "sis_acao_id")
	private int sisAcaoId;

	@Column(name = "sis_executar")
	private int sisExecutar;

	@Column(name = "sis_funcao_id")
	private int sisFuncaoId;

	@Column(name = "sis_modulo_id")
	private int sisModuloId;

	@JoinColumn(name = "sis_usuario_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private SisUsuario sisUsuario;
	
	@JoinColumn(name = "sis_grupo_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private SisGrupo sisGrupo;
	
	public SisPermissao() {
		this(0);
	}

	public SisPermissao(int sisPermissaoId) {
		super("pu_permissao", "SisPermissao", "sisPermissaoId", "sisPermissaoId");
		this.sisPermissaoId = sisPermissaoId;
	}

	public int getSisPermissaoId() {
		return this.sisPermissaoId;
	}

	public void setSisPermissaoId(int sisPermissaoId) {
		this.sisPermissaoId = sisPermissaoId;
	}

	public int getSisAcaoId() {
		return this.sisAcaoId;
	}

	public void setSisAcaoId(int sisAcaoId) {
		this.sisAcaoId = sisAcaoId;
	}

	public boolean getSisExecutar() {
		return sisExecutar == 0 ? false : true;
	}

	public void setSisExecutar(boolean sisExecutar) {
		this.sisExecutar = sisExecutar == false ? 0 : 1;
	}

	public int getSisFuncaoId() {
		return this.sisFuncaoId;
	}

	public void setSisFuncaoId(int sisFuncaoId) {
		this.sisFuncaoId = sisFuncaoId;
	}

	public int getSisModuloId() {
		return this.sisModuloId;
	}

	public void setSisModuloId(int sisModuloId) {
		this.sisModuloId = sisModuloId;
	}

	public SisUsuario getSisUsuario() {
		return sisUsuario;
	}

	public void setSisUsuario(SisUsuario sisUsuario) {
		this.sisUsuario = sisUsuario;
	}

	public SisGrupo getSisGrupo() {
		return sisGrupo;
	}

	public void setSisGrupo(SisGrupo sisGrupo) {
		this.sisGrupo = sisGrupo;
	}

	/**
	 * Metodo que ordena as permissoes na regra Modulo, Funcao e Acao.
	 * 
	 * @param o
	 *            Objeto do tipo SisPermissao para comparacao.
	 * @return 1 se for maior, 0 se for igual e -1 se for menor.
	 */
	public int compareTo(SisPermissao o) {
		if (this.getSisModuloId() < o.getSisModuloId()) {
			return -1;
		} else if (this.getSisModuloId() > o.getSisModuloId()) {
			return 1;
		} else {
			if (this.getSisFuncaoId() < o.getSisFuncaoId()) {
				return -1;
			} else if (this.getSisFuncaoId() > o.getSisFuncaoId()) {
				return 1;
			} else {
				if (this.getSisAcaoId() < o.getSisAcaoId()) {
					return -1;
				} else if (this.getSisAcaoId() > o.getSisAcaoId()) {
					return 1;
				} else {
					return 0;
				}
			}
		}
	}

	public Number getId() {
		return sisPermissaoId;
	}

	public void setId(Number id) {
		sisPermissaoId = id.intValue();
	}

	public String[] toArray() {
		return new String[] { sisPermissaoId + "", sisModuloId + "", sisFuncaoId + "", sisAcaoId + "", sisExecutar + "" };
	}
	
	public static List<SisPermissao> getPermissoes(String campo) {
		List<SisPermissao> permissoes = new ArrayList<SisPermissao>();
		if (campo != null && !campo.equals("")) {
			for (String per : campo.split("::")) {
				String[] p = per.split(",");
				SisPermissao permissao = new SisPermissao();
				permissao.setSisPermissaoId(Integer.valueOf(p[0]));
				permissao.setSisModuloId(Integer.valueOf(p[1]));
				permissao.setSisFuncaoId(Integer.valueOf(p[2]));
				permissao.setSisAcaoId(Integer.valueOf(p[3]));
				permissao.setSisExecutar(Boolean.valueOf(p[4]));
				permissoes.add(permissao);
			}
		}
		return permissoes;
	}
	
	@Override
	public void anularDependencia() {
		sisUsuario = null;
		sisGrupo = null;
	}
}