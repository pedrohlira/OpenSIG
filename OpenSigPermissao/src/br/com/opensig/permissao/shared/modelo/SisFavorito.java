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

import br.com.opensig.core.shared.modelo.Colecao;
import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.core.shared.modelo.ELetra;
import br.com.opensig.core.shared.modelo.IFavorito;
import br.com.opensig.core.shared.modelo.IFavoritoCampo;
import br.com.opensig.core.shared.modelo.IFavoritoGrafico;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;

/**
 * Classe que representa um favorito no sistema.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 * @since 27/08/2009
 */
@Entity
@Table(name = "sis_favorito")
public class SisFavorito extends Dados implements Serializable, IFavorito {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "sis_favorito_id")
	private int sisFavoritoId;

	@Column(name = "sis_favorito_busca")
	private String sisFavoritoBusca;

	@Column(name = "sis_favorito_descricao")
	private String sisFavoritoDescricao;

	@Column(name = "sis_favorito_nome")
	private String sisFavoritoNome;

	@Column(name = "sis_favorito_ordem")
	private String sisFavoritoOrdem;

	@Column(name = "sis_favorito_ordem_descricao")
	private String sisFavoritoOrdemDirecao;

	@Column(name = "sis_favorito_paginacao")
	private int sisFavoritoPaginacao;

	@JoinTable(name = "sis_favorito_usuario", joinColumns = { @JoinColumn(name = "sis_favorito_id", referencedColumnName = "sis_favorito_id") }, inverseJoinColumns = { @JoinColumn(name = "sis_usuario_id", referencedColumnName = "sis_usuario_id") })
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
	private List<SisUsuario> sisUsuarios;

	@JoinTable(name = "sis_favorito_grupo", joinColumns = { @JoinColumn(name = "sis_favorito_id", referencedColumnName = "sis_favorito_id") }, inverseJoinColumns = { @JoinColumn(name = "sis_grupo_id", referencedColumnName = "sis_grupo_id") })
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
	private List<SisGrupo> sisGrupos;

	@OneToMany(mappedBy = "sisFavorito", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private List<SisFavoritoCampo> sisFavoritoCampos;

	@OneToMany(mappedBy = "sisFavorito", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private List<SisFavoritoGrafico> sisFavoritoGrafico;

	@JoinColumn(name = "sis_usuario_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private SisUsuario sisUsuario;

	@JoinColumn(name = "sis_funcao_id")
	@ManyToOne(fetch = FetchType.EAGER)
	private SisFuncao sisFuncao;

	public SisFavorito() {
		this(0);
	}

	public SisFavorito(int sisFavoritoId) {
		super("pu_permissao", "SisFavorito", "sisFavoritoId", "sisFavoritoNome");
		super.setTipoLetra(ELetra.NORMAL);
		this.sisFavoritoId = sisFavoritoId;
		Colecao col1 = new Colecao("SisGrupo", "t.sisGrupos", "LEFT JOIN", "t0");
		Colecao col2 = new Colecao("SisUsuario", "t0.sisUsuarios", "LEFT JOIN", "t1");
		setColecao(new Colecao[] { col1, col2 });
	}

	public int getSisFavoritoId() {
		return this.sisFavoritoId;
	}

	public void setSisFavoritoId(int sisFavoritoId) {
		this.sisFavoritoId = sisFavoritoId;
	}

	public String getSisFavoritoBusca() {
		return this.sisFavoritoBusca;
	}

	public void setSisFavoritoBusca(String sisFavoritoBusca) {
		this.sisFavoritoBusca = sisFavoritoBusca;
	}

	public String getSisFavoritoDescricao() {
		return this.sisFavoritoDescricao;
	}

	public void setSisFavoritoDescricao(String sisFavoritoDescricao) {
		this.sisFavoritoDescricao = sisFavoritoDescricao;
	}

	public String getSisFavoritoNome() {
		return this.sisFavoritoNome;
	}

	public void setSisFavoritoNome(String sisFavoritoNome) {
		this.sisFavoritoNome = sisFavoritoNome;
	}

	public String getSisFavoritoOrdem() {
		return this.sisFavoritoOrdem;
	}

	public void setSisFavoritoOrdem(String sisFavoritoOrdem) {
		this.sisFavoritoOrdem = sisFavoritoOrdem;
	}

	public String getSisFavoritoOrdemDirecao() {
		return this.sisFavoritoOrdemDirecao;
	}

	public void setSisFavoritoOrdemDirecao(String sisFavoritoOrdemDirecao) {
		this.sisFavoritoOrdemDirecao = sisFavoritoOrdemDirecao;
	}

	public int getSisFavoritoPaginacao() {
		return this.sisFavoritoPaginacao;
	}

	public void setSisFavoritoPaginacao(int sisFavoritoPaginacao) {
		this.sisFavoritoPaginacao = sisFavoritoPaginacao;
	}

	public List<SisUsuario> getSisUsuarios() {
		return sisUsuarios;
	}

	public void setSisUsuarios(List<SisUsuario> sisUsuarios) {
		this.sisUsuarios = sisUsuarios;
	}

	public List<SisGrupo> getSisGrupos() {
		return sisGrupos;
	}

	public void setSisGrupos(List<SisGrupo> sisGrupos) {
		this.sisGrupos = sisGrupos;
	}

	public SisUsuario getSisUsuario() {
		return sisUsuario;
	}

	public void setSisUsuario(SisUsuario sisUsuario) {
		this.sisUsuario = sisUsuario;
	}

	public SisFuncao getSisFuncao() {
		return sisFuncao;
	}

	public void setSisFuncao(SisFuncao sisFuncao) {
		this.sisFuncao = sisFuncao;
	}

	public Number getId() {
		return sisFavoritoId;
	}

	public void setId(Number id) {
		sisFavoritoId = id.intValue();
	}

	public String[] toArray() {
		return new String[] { sisFavoritoId + "", sisFavoritoNome, sisFavoritoDescricao, sisFuncao.getSisFuncaoClasse(), !sisFavoritoGrafico.isEmpty() + "" };
	}

	public Dados getObjeto(String campo) {
		if (campo.startsWith("sisUsuario")) {
			return new SisUsuario();
		} else {
			return null;
		}
	}

	public void anularDependencia() {
		sisGrupos = null;
		sisUsuarios = null;
		sisUsuario = null;
	}

	public void setSisFavoritoCampos(List<SisFavoritoCampo> sisFavoritoCampos) {
		this.sisFavoritoCampos = sisFavoritoCampos;
	}

	public List<IFavoritoCampo> getSisFavoritoCampos() {
		List<IFavoritoCampo> campos = new ArrayList<IFavoritoCampo>();
		for (IFavoritoCampo iFavoritoCampo : sisFavoritoCampos) {
			campos.add(iFavoritoCampo);
		}

		return campos;
	}

	public void setSisFavoritoGrafico(List<SisFavoritoGrafico> sisFavoritoGrafico) {
		this.sisFavoritoGrafico = sisFavoritoGrafico;
	}

	public List<IFavoritoGrafico> getSisFavoritoGrafico() {
		List<IFavoritoGrafico> grafico = new ArrayList<IFavoritoGrafico>();
		if (sisFavoritoGrafico != null) {
			for (IFavoritoGrafico iFavoritoGrafico : sisFavoritoGrafico) {
				grafico.add(iFavoritoGrafico);
			}
		}
		return grafico;
	}
}