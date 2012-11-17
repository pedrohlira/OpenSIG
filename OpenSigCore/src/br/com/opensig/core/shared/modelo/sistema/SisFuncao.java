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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import br.com.opensig.core.shared.modelo.Dados;

/**
 * Classe que representa uma funcao no sistema.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 */
@Entity
@Table(name = "sis_funcao")
public class SisFuncao extends Dados implements Serializable, Comparable<SisFuncao> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "sis_funcao_id")
	private int sisFuncaoId;

	@Column(name = "sis_funcao_ativo")
	private int sisFuncaoAtivo;

	@Column(name = "sis_funcao_classe")
	private String sisFuncaoClasse;

	@Column(name = "sis_funcao_ordem")
	private int sisFuncaoOrdem;

	@Column(name = "sis_funcao_subordem")
	private int sisFuncaoSubOrdem;

	@OneToMany(mappedBy = "sisFuncao", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<SisAcao> sisAcoes;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "sis_modulo_id")
	private SisModulo sisModulo;

	public SisFuncao() {
		this(0);
	}

	public SisFuncao(int sisFuncaoId) {
		super("pu_core", "SisFuncao", "sisFuncaoId", "sisFuncaoOrdem");
		this.sisFuncaoId = sisFuncaoId;
	}

	public int getSisFuncaoId() {
		return this.sisFuncaoId;
	}

	public void setSisFuncaoId(int sisFuncaoId) {
		this.sisFuncaoId = sisFuncaoId;
	}

	public boolean getSisFuncaoAtivo() {
		return sisFuncaoAtivo == 0 ? false : true;
	}

	public void setSisFuncaoAtivo(boolean sisFuncaoAtivo) {
		this.sisFuncaoAtivo = sisFuncaoAtivo == false ? 0 : 1;
	}

	public String getSisFuncaoClasse() {
		return this.sisFuncaoClasse;
	}

	public void setSisFuncaoClasse(String sisFuncaoClasse) {
		this.sisFuncaoClasse = sisFuncaoClasse;
	}

	public int getSisFuncaoOrdem() {
		return this.sisFuncaoOrdem;
	}

	public void setSisFuncaoOrdem(int sisFuncaoOrdem) {
		this.sisFuncaoOrdem = sisFuncaoOrdem;
	}

	public List<SisAcao> getSisAcoes() {
		return this.sisAcoes;
	}

	public void setSisAcoes(List<SisAcao> sisAcoes) {
		this.sisAcoes = sisAcoes;
	}

	public SisModulo getSisModulo() {
		return this.sisModulo;
	}

	public void setSisModulo(SisModulo sisModulo) {
		this.sisModulo = sisModulo;
	}

	public int getSisFuncaoSubOrdem() {
		return sisFuncaoSubOrdem;
	}

	public void setSisFuncaoSubOrdem(int sisFuncaoSubOrdem) {
		this.sisFuncaoSubOrdem = sisFuncaoSubOrdem;
	}

	/**
	 * Metodo que ordena as funcoes pelo campo de ordem.
	 * 
	 * @param o
	 *            Objeto do tipo SisFuncao para comparacao.
	 * @return 1 se for maior, 0 se for igual e -1 se for menor.
	 */
	public int compareTo(SisFuncao o) {
		if (this.sisFuncaoOrdem < o.getSisFuncaoOrdem()) {
			return -1;
		} else if (this.sisFuncaoOrdem > o.getSisFuncaoOrdem()) {
			return 1;
		} else {
			if (this.sisFuncaoSubOrdem < o.sisFuncaoSubOrdem) {
				return -1;
			} else if (this.sisFuncaoSubOrdem > o.sisFuncaoSubOrdem) {
				return 1;
			} else {
				return 0;
			}
		}
	}

	@Override
	public Number getId() {
		return sisFuncaoId;
	}

	@Override
	public void setId(Number id) {
		sisFuncaoId = id.intValue();
	}

	@Override
	public String[] toArray() {
		return new String[] { sisFuncaoId + "", sisFuncaoClasse, sisFuncaoOrdem + "", sisFuncaoSubOrdem + "", sisFuncaoAtivo + "" };
	}

	public boolean getAtivo() {
		return getSisFuncaoAtivo();
	}

	public boolean getSistema() {
		return true;
	}
}