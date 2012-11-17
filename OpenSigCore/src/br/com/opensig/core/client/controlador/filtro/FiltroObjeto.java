package br.com.opensig.core.client.controlador.filtro;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.controlador.parametro.ParametroException;
import br.com.opensig.core.shared.modelo.Dados;

/**
 * Classe que define um filtro do tipo objeto.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 */
public class FiltroObjeto extends AFiltro<Dados> {

	/**
	 * @see AFiltro#AFiltro()
	 */
	public FiltroObjeto() {
		super();
	}

	/**
	 * @see AFiltro#AFiltro(String, ECompara, java.io.Serializable)
	 */
	public FiltroObjeto(String campo, ECompara compara, Dados valor) {
		super(campo, compara, valor);
	}

	@Override
	public String getSql() throws ParametroException {
		if (compara == ECompara.IGUAL || compara == ECompara.DIFERENTE) {
			return super.getSql();
		} else if (compara == ECompara.NULO || compara == ECompara.VAZIO) {
			tratarPrefixo();
			return prefixo + campo + " " + compara.toString();
		} else {
			throw new ParametroException(OpenSigCore.i18n.errFiltro());
		}
	}

	@Override
	public void setValorString(String valor) {
		throw new NullPointerException(OpenSigCore.i18n.errFiltro());
	}
}
