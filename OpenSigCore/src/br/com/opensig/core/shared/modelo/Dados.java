package br.com.opensig.core.shared.modelo;

import java.io.Serializable;

import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;

import br.com.opensig.core.client.UtilClient;

/**
 * Classe de abstrae os dados das classes POJOs que representam os dados das tabelas.
 * 
 * @author Pedro H. Lira
 */
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class Dados implements Serializable {

	@Transient
	@XmlTransient
	private String pu;
	@Transient
	@XmlTransient
	private String tabela;
	@Transient
	@XmlTransient
	private Colecao[] colecao;
	@Transient
	@XmlTransient
	private String campoId;
	@Transient
	@XmlTransient
	private String campoOrdem;
	@Transient
	@XmlTransient
	private EDirecao ordemDirecao;
	@Transient
	@XmlTransient
	private ELetra tipoLetra;
	@Transient
	@XmlTransient
	private boolean limpaBranco;
	@Transient
	@XmlTransient
	private int empresa;

	/**
	 * Construtor que define as valores padrões de cada classe POJO.
	 * 
	 * @param pu
	 *            a unidade de persistência que tem a definiçao do POJO.
	 * @param tabela
	 *            o nome da classe que representa a tabela.
	 * @param campoId
	 *            o nome do campo que é o Id da tabela.
	 */
	public Dados(String pu, String tabela, String campoId) {
		this(pu, tabela, campoId, campoId);
	}

	/**
	 * Construtor que define as valores padrões de cada classe POJO.
	 * 
	 * @param pu
	 *            a unidade de persistência que tem a definiçao do POJO.
	 * @param tabela
	 *            o nome da classe que representa a tabela.
	 * @param campoId
	 *            o nome do campo que é o Id da tabela.
	 * @param campoOrdem
	 *            o nome do campo que será usado como ordenaçao padrao.
	 */
	public Dados(String pu, String tabela, String campoId, String campoOrdem) {
		this(pu, tabela, campoId, campoOrdem, EDirecao.ASC);
	}

	/**
	 * Construtor que define as valores padrões de cada classe POJO.
	 * 
	 * @param pu
	 *            a unidade de persistência que tem a definiçao do POJO.
	 * @param tabela
	 *            o nome da classe que representa a tabela.
	 * @param campoId
	 *            o nome do campo que é o Id da tabela.
	 * @param campoOrdem
	 *            o nome do campo que será usado como ordenaçao padrao.
	 * @param ordemDirecao
	 *            a direçao de ordenaçao padrao.
	 */
	public Dados(String pu, String tabela, String campoId, String campoOrdem, EDirecao ordemDirecao) {
		this.pu = pu;
		this.tabela = tabela;
		this.campoId = campoId;
		this.campoOrdem = UtilClient.getCampoPrefixado(campoOrdem);
		this.ordemDirecao = ordemDirecao;
		this.tipoLetra = ELetra.GRANDE;
		this.limpaBranco = true;
	}

	/**
	 * Metodo que retorna a direçao da ordenaçao do campo Ordem.
	 * 
	 * @return a direçao da ordenaçao.
	 */
	public EDirecao getOrdemDirecao() {
		return ordemDirecao;
	}

	/**
	 * Metodo que retorna o nome do campo Ordem.
	 * 
	 * @return o campo Ordem.
	 */
	public String getCampoOrdem() {
		return campoOrdem;
	}

	/**
	 * Metodo que retorna o nome do campo Id.
	 * 
	 * @return o campo Id.
	 */
	public String getCampoId() {
		return campoId;
	}

	/**
	 * Metodo que retorna o nome da classe que representa a tabela.
	 * 
	 * @return a tabela.
	 */
	public String getTabela() {
		return tabela;
	}

	/**
	 * Metodo que retorna a coleçao da tabela secundaria.
	 * 
	 * @return a coleçao da tabela secundaria.
	 */
	public Colecao[] getColecao() {
		return colecao;
	}

	/**
	 * Metodo que retorna a unidade de persistência que contem a tabela.
	 * 
	 * @return a unidade de persistência.
	 */
	public String getPu() {
		return pu;
	}

	/**
	 * Metodo que define a direçao da ordenaçao do campo Ordem.
	 * 
	 * @param ordemDirecao
	 *            a direçao da ordenaçao.
	 */
	public void setOrdemDirecao(EDirecao ordemDirecao) {
		this.ordemDirecao = ordemDirecao;
	}

	/**
	 * Metodo que define o nome do campo Id.
	 * 
	 * @param campoId
	 *            o campo Id.
	 */
	public void setCampoId(String campoId) {
		this.campoId = campoId;
	}

	/**
	 * Metodo que define o nome do campo Ordem.
	 * 
	 * @param campoOrdem
	 *            o campo Ordem.
	 */
	public void setCampoOrdem(String campoOrdem) {
		this.campoOrdem = campoOrdem;
	}

	/**
	 * Metodo que define a unidade de persistência que contem a tabela.
	 * 
	 * @param pu
	 *            a unidade de persistência.
	 */
	public void setPu(String pu) {
		this.pu = pu;
	}

	/**
	 * Metodo que define o nome da classe que representa a tabela.
	 * 
	 * @param tabela
	 *            a tabela.
	 */
	public void setTabela(String tabela) {
		this.tabela = tabela;
	}

	/**
	 * Metodo que define a tabela secundaria.
	 * 
	 * @param colecao
	 *            a tabela secundaria.
	 */
	public void setColecao(Colecao[] colecao) {
		this.colecao = colecao;
	}

	/**
	 * Metodo que retorna o padrao de tamanho das letras.
	 * 
	 * @return o tipo da letra.
	 */
	public ELetra getTipoLetra() {
		return tipoLetra;
	}

	/**
	 * Metodo que define o padrao de tamanho das letras.
	 * 
	 * @param tipoLetra
	 *            o tipo.
	 */
	public void setTipoLetra(ELetra tipoLetra) {
		this.tipoLetra = tipoLetra;
	}

	/**
	 * Metodo que retorna se o sistema limpa os espaços em branco dos campos Texto na hora de salvar.
	 * 
	 * @return true [padrao] se ele esta limpando, false se nao é limpado.
	 */
	public boolean isLimpaBranco() {
		return limpaBranco;
	}

	/**
	 * Metodo que define se o sistema deve limpar os campos de Texto na hora de salvar.
	 * 
	 * @param limpaBranco
	 *            true [padrao] se ele deve limpar, false se nao deve limpar.
	 */
	public void setLimpaBranco(boolean limpaBranco) {
		this.limpaBranco = limpaBranco;
	}

	/**
	 * Metodo que retorna a empresa ativa nos dados.
	 * 
	 * @return o id da empresa.
	 */
	public int getEmpresa() {
		return empresa;
	}

	/**
	 * Metodo que seta a empresa ativa nos dados.
	 * 
	 * @param empresa
	 *            o id da empresa.
	 */
	public void setEmpresa(int empresa) {
		this.empresa = empresa;
	}

	/**
	 * Metodo padrao para retornar o valor do Id de qualquer classe POJO.
	 * 
	 * @return o getId do registro.
	 */
	public abstract Number getId();

	/**
	 * Metodo padrao para define o valor do id de qualquer classe POJO.
	 * 
	 * @param id
	 *            o valor a ser definido.
	 */
	public abstract void setId(Number id);

	/**
	 * Metodo que transforma a entidade em um Array de String.
	 * 
	 * @return um Array de String
	 */
	public abstract String[] toArray();

	/**
	 * Metodo que remove as dependencias de acesso indereto.
	 */
	public void anularDependencia() {
	}

	/**
	 * Metodo que retorna um objeto do campo interno vinculado.
	 * 
	 * @param campo
	 *            o nome do campo a ser usado para identificar e criar o objeto.
	 * @return uma instancia do objeto criado.
	 */
	public Dados getObjeto(String campo) {
		return null;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Dados) {
			Dados aux = (Dados) obj;
			return this.getId().longValue() == aux.getId().longValue();
		} else {
			return false;
		}
	}
}
