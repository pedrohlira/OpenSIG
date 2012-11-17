package br.com.opensig.core.client.controlador.filtro;

import java.io.Serializable;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.controlador.parametro.ParametroException;

/**
 * Classe que define um filtro do tipo boleano.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 */
public class FiltroBinario extends AFiltro<Integer> {

	/**
	 * @see AFiltro#AFiltro()
	 */
	public FiltroBinario() {
		super();
	}

	/**
	 * @see AFiltro#AFiltro(String, ECompara, String)
	 */
	public FiltroBinario(String campo, ECompara compara, String valor) {
		super(campo, compara, valor);
	}

	/**
	 * @see AFiltro#AFiltro(String, ECompara, Serializable)
	 */
	public FiltroBinario(String campo, ECompara compara, int valor) {
		super(campo, compara, valor);
	}

	@Override
	public String getSql() throws ParametroException {
		if (compara == ECompara.IGUAL || compara == ECompara.DIFERENTE) {
			return super.getSql();
		} else {
			throw new ParametroException(OpenSigCore.i18n.errFiltro());
		}
	}

	@Override
	public void setValorString(String valor) {
		if (valor == null || valor.equals("0")) {
			super.setValor(0);
		} else {
			super.setValor(1);
		}
	}
}
