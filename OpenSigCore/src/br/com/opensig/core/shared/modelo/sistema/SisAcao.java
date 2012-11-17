package br.com.opensig.core.shared.modelo.sistema;

import java.io.Serializable;
import javax.persistence.*;

import br.com.opensig.core.shared.modelo.Dados;

/**
 * Classe que representa uma acao no sistema.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 */
@Entity
@Table(name = "sis_acao")
public class SisAcao extends Dados implements Serializable, Comparable<SisAcao> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "sis_acao_id")
	private int sisAcaoId;

	@Column(name = "sis_acao_ativo")
	private int sisAcaoAtivo;

	@Column(name = "sis_acao_classe")
	private String sisAcaoClasse;

	@Column(name = "sis_acao_ordem")
	private int sisAcaoOrdem;

	@Column(name = "sis_acao_subordem")
	private int sisAcaoSubOrdem;

	@Column(name = "sis_acao_visivel")
	private int sisAcaoVisivel;

	@Transient
	private boolean executar;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "sis_funcao_id")
	private SisFuncao sisFuncao;

	public SisAcao() {
		this(0);
	}

	public SisAcao(int sisAcaoId) {
		super("pu_core", "SisAcao", "sisAcaoId", "sisAcaoOrdem");
		this.sisAcaoId = sisAcaoId;
	}

	public int getSisAcaoId() {
		return this.sisAcaoId;
	}

	public void setSisAcaoId(int sisAcaoId) {
		this.sisAcaoId = sisAcaoId;
	}

	public boolean getSisAcaoAtivo() {
		return sisAcaoAtivo == 0 ? false : true;
	}

	public void setSisAcaoAtivo(boolean sisAcaoAtivo) {
		this.sisAcaoAtivo = sisAcaoAtivo == false ? 0 : 1;
	}

	public String getSisAcaoClasse() {
		return this.sisAcaoClasse;
	}

	public void setSisAcaoClasse(String sisAcaoClasse) {
		this.sisAcaoClasse = sisAcaoClasse;
	}

	public int getSisAcaoOrdem() {
		return this.sisAcaoOrdem;
	}

	public void setSisAcaoOrdem(int sisAcaoOrdem) {
		this.sisAcaoOrdem = sisAcaoOrdem;
	}

	public SisFuncao getSisFuncao() {
		return this.sisFuncao;
	}

	public void setSisFuncao(SisFuncao sisFuncao) {
		this.sisFuncao = sisFuncao;
	}

	public boolean getSisAcaoVisivel() {
		return sisAcaoVisivel == 0 ? false : true;
	}

	public void setSisAcaoVisivel(boolean sisAcaoVisivel) {
		this.sisAcaoVisivel = sisAcaoVisivel == false ? 0 : 1;
	}

	public int getSisAcaoSubOrdem() {
		return sisAcaoSubOrdem;
	}

	public void setSisAcaoSubOrdem(int sisAcaoSubOrdem) {
		this.sisAcaoSubOrdem = sisAcaoSubOrdem;
	}

	/**
	 * Metodo que ordena as acoes pelo campo de ordem.
	 * 
	 * @param o
	 *            Objeto do tipo SisAcao para comparacao.
	 * @return 1 se for maior, 0 se for igual e -1 se for menor.
	 */
	public int compareTo(SisAcao o) {
		if (this.sisAcaoOrdem < o.getSisAcaoOrdem()) {
			return -1;
		} else if (this.sisAcaoOrdem > o.getSisAcaoOrdem()) {
			return 1;
		} else {
			if (this.sisAcaoSubOrdem < o.sisAcaoSubOrdem) {
				return -1;
			} else if (this.sisAcaoSubOrdem > o.sisAcaoSubOrdem) {
				return 1;
			} else {
				return 0;
			}
		}
	}

	@Override
	public Number getId() {
		return sisAcaoId;
	}

	@Override
	public void setId(Number id) {
		sisAcaoId = id.intValue();
	}

	@Override
	public String[] toArray() {
		return new String[] { sisAcaoId + "", sisAcaoOrdem + "", sisAcaoSubOrdem + "", sisAcaoClasse, sisAcaoAtivo + "", sisAcaoVisivel + "" };
	}

	public boolean getAtivo() {
		return getSisAcaoAtivo();
	}

	public boolean getSistema() {
		return true;
	}

	public boolean isExecutar() {
		return executar;
	}

	public void setExecutar(boolean executar) {
		this.executar = executar;
	}

}