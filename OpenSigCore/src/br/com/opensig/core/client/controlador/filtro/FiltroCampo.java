package br.com.opensig.core.client.controlador.filtro;

import java.io.Serializable;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.controlador.parametro.ParametroException;

/**
 * Classe que define um filtro do tipo campo.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 */
public class FiltroCampo extends AFiltro<String> {

	/**
	 * @see AFiltro#AFiltro()
	 */
	public FiltroCampo() {
		super();
	}

	/**
	 * @see AFiltro#AFiltro(String, ECompara, Serializable)
	 */
	public FiltroCampo(String campo, ECompara compara, String campo2) {
		super.campo = campo;
		super.compara = compara;
		super.valor = campo2;
	}

	@Override
	public String getSql() throws ParametroException {
		if (compara != ECompara.NULO && compara != ECompara.VAZIO) {
			return campo + " " + compara.toString() + " " + valor;
		} else {
			throw new ParametroException(OpenSigCore.i18n.errFiltro());
		}
	}

	@Override
	public void setValorString(String valor) {
		super.valor = valor;
	}
}
