package br.com.opensig.core.client.controlador.filtro;

import java.io.Serializable;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.controlador.parametro.ParametroException;

/**
 * Classe que define um filtro do tipo texto.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 */
public class FiltroTexto extends AFiltro<String> {

	/**
	 * @see AFiltro#AFiltro()
	 */
	public FiltroTexto() {
		super();
	}

	/**
	 * @see AFiltro#AFiltro(String, ECompara, Serializable)
	 */
	public FiltroTexto(String campo, ECompara compara, String valor) {
		super.campo = campo;
		super.compara = compara;
		super.valor = valor;
	}

	@Override
	public String getSql() throws ParametroException {
		if (compara != ECompara.MAIOR && compara != ECompara.MAIOR_IGUAL && compara != ECompara.MENOR && compara != ECompara.MENOR_IGUAL) {
			tratarPrefixo();
			return "UPPER(" + prefixo + campo + ") " + compara.toString() + " :" + getCampoId();
		} else {
			throw new ParametroException(OpenSigCore.i18n.errFiltro());
		}
	}

	@Override
	public String getValor() {
		String retorno = "";

		if (valor != null) {
			if (compara == ECompara.CONTEM) {
				retorno = ("%" + valor.toUpperCase() + "%");
			} else if (compara == ECompara.CONTEM_FIM) {
				retorno = ("%" + valor.toUpperCase());
			} else if (compara == ECompara.CONTEM_INICIO) {
				retorno = (valor.toUpperCase() + "%");
			} else {
				retorno = (valor.toUpperCase());
			}
		} else {
			retorno = null;
		}

		return retorno;
	}

	@Override
	public void setValorString(String valor) {
		super.valor = valor;
	}
}
