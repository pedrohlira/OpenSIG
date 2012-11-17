package br.com.opensig.core.shared.modelo.sistema;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import br.com.opensig.core.shared.modelo.Dados;

/**
 * Classe que representa um modulo no sistema.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 */
@Entity
@Table(name = "sis_modulo")
public class SisModulo extends Dados implements Serializable, Comparable<SisModulo> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "sis_modulo_id")
	private int sisModuloId;

	@Column(name = "sis_modulo_ativo")
	private int sisModuloAtivo;

	@Column(name = "sis_modulo_classe")
	private String sisModuloClasse;

	@Column(name = "sis_modulo_ordem")
	private int sisModuloOrdem;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "sisModulo", fetch = FetchType.EAGER)
	private List<SisFuncao> sisFuncoes;

	public SisModulo() {
		this(0);
	}

	public SisModulo(int sisModuloId) {
		super("pu_core", "SisModulo", "sisModuloId", "sisModuloOrdem");
		this.sisModuloId = sisModuloId;
	}

	public int getSisModuloId() {
		return this.sisModuloId;
	}

	public void setSisModuloId(int sisModuloId) {
		this.sisModuloId = sisModuloId;
	}

	public boolean getSisModuloAtivo() {
		return sisModuloAtivo == 0 ? false : true;
	}

	public void setSisModuloAtivo(boolean sisModuloAtivo) {
		this.sisModuloAtivo = sisModuloAtivo == false ? 0 : 1;
	}

	public String getSisModuloClasse() {
		return this.sisModuloClasse;
	}

	public void setSisModuloClasse(String sisModuloClasse) {
		this.sisModuloClasse = sisModuloClasse;
	}

	public int getSisModuloOrdem() {
		return this.sisModuloOrdem;
	}

	public void setSisModuloOrdem(int sisModuloOrdem) {
		this.sisModuloOrdem = sisModuloOrdem;
	}

	public List<SisFuncao> getSisFuncoes() {
		return this.sisFuncoes;
	}

	public void setSisFuncoes(List<SisFuncao> sisFuncoes) {
		this.sisFuncoes = sisFuncoes;
	}

	/**
	 * Metodo que ordena os modulos pelo campo de ordem.
	 * 
	 * @param o
	 *            Objeto do tipo SisModulo para comparacao.
	 * @return 1 se for maior, 0 se for igual e -1 se for menor.
	 */
	public int compareTo(SisModulo o) {
		if (this.sisModuloOrdem < o.getSisModuloOrdem()) {
			return -1;
		} else if (this.sisModuloOrdem > o.getSisModuloOrdem()) {
			return 1;
		} else {
			return 0;
		}
	}

	@Override
	public Number getId() {
		return sisModuloId;
	}

	@Override
	public void setId(Number id) {
		sisModuloId = id.intValue();
	}

	@Override
	public String[] toArray() {
		return new String[] { sisModuloId + "", sisModuloClasse, sisModuloOrdem + "", sisModuloAtivo + "" };
	}

	public boolean getAtivo() {
		return getSisModuloAtivo();
	}

	public boolean getSistema() {
		return true;
	}
}