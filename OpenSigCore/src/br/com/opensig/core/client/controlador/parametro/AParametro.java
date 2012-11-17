package br.com.opensig.core.client.controlador.parametro;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;

import br.com.opensig.core.client.controlador.filtro.ECompara;

/**
 * Classe que abstrai as implementações do parametro definindos todos os métodos com funcionalidades padronizadas.
 * 
 * @param <E>
 *            usando genérico para tipar o modelo de parametro usado.
 * @author Pedro H. Lira
 * @version 1.0
 */
public abstract class AParametro<E extends Serializable> implements IParametro<E> {

	private static final long serialVersionUID = 1L;
	/**
	 * Campo contendo o tipo de comparação a ser usada no filtro.
	 */
	protected ECompara compara = ECompara.IGUAL;
	/**
	 * Campo contendo o prefixo do campos de acordo com a tabela referenciada.
	 */
	protected String prefixo = "t.";
	/**
	 * Campo contendo o nome do campo do filtro.
	 */
	protected String campo;
	/**
	 * Campo contendo o valor a ser usado pelo filtro.F
	 */
	protected E valor;
	/*
	 * geração de um identificador unico para o campo do filtro.
	 */
	private int campoId = (int) (Math.random() * Integer.MAX_VALUE);

	/**
	 * Construtor padrão.
	 */
	public AParametro() {
	}

	/**
	 * Construtor que define o campo, a comparação e o valor.
	 * 
	 * @param campo
	 *            o nome do campo.
	 * @param valor
	 *            o valor do filtro.
	 * @throws ParametroException
	 */
	public AParametro(String campo, String valor) {
		this.campo = campo;
		setValorString(valor);
	}

	/**
	 * Construtor que define o campo, a comparação e o valor.
	 * 
	 * @param campo
	 *            o nome do campo.
	 * @param valor
	 *            o valor do filtro.
	 */
	public AParametro(String campo, E valor) {
		this.campo = campo;
		this.valor = valor;
	}

	// Gets e Seteres

	public String getCampoId() {
		return campo.replace('.', '_') + "_" + campoId;
	}

	public String getCampoPrefixo() {
		return prefixo;
	}

	public String getCampo() {
		return campo;
	}

	public void setCampoPrefixo(String prefixo) {
		this.prefixo = prefixo;
	}

	public void setCampo(String campo) {
		this.campo = campo;
	}

	public E getValor() {
		return valor;
	}

	public void setValor(E valor) {
		this.valor = valor;
	}

	public String getSql() throws ParametroException {
		tratarPrefixo();
		return prefixo + campo + " " + compara.toString() + " :" + getCampoId();
	}

	public Collection<IParametro<E>> getParametro() {
		return getParametro(this);
	}

	public Collection<IParametro<E>> getParametro(IParametro<E> filtro) {
		Collection<IParametro<E>> parametros = new ArrayList<IParametro<E>>();
		parametros.add(filtro);
		return parametros;
	}

	public abstract void setValorString(String valor);

	protected void tratarPrefixo() {
		// valida se o filtro é já tem prefixo
		MatchResult mat = RegExp.compile("^t\\d+\\.").exec(campo);
		if (mat != null) {
			prefixo = "";
		}
	}
}
