package br.com.opensig.core.client.controlador.parametro;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import br.com.opensig.core.client.controlador.filtro.ECompara;

/**
 * Classe que representa um agrupamento de parametros, podendo ter parametros ou
 * mesmo grupos de parametros.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 */
public class GrupoParametro implements IParametro {

	private static final long serialVersionUID = 1L;
	private List<IParametro> parametros = new LinkedList<IParametro>();

	/**
	 * Construtor padrão.
	 */
	public GrupoParametro() {
	}

	/**
	 * Construtor passando os parametros.
	 * 
	 * @param parametros
	 *            um array de parametros.
	 */
	public GrupoParametro(IParametro[] parametros) {
		if (parametros != null) {
			for (IParametro par : parametros) {
				add(par);
			}
		}
	}

	/**
	 * Metodo que remove um parametro ou grupo.
	 * 
	 * @param parametro
	 *            do tipo IParametro.
	 */
	public void remove(IParametro parametro) {
		parametros.remove(parametro);
	}

	/**
	 * Metodo que limpa os parametros do grupo.
	 */
	public void clear() {
		parametros.clear();
	}

	/**
	 * Metodo que retorna a quantidade de parametros dentro do grupo.
	 * 
	 * @return quantidade de filtros.
	 */
	public int size() {
		return parametros.size();
	}

	/**
	 * Metodo que adiciona um parametro ou grupo filtro final.
	 * 
	 * @param parametro
	 *            do tipo IParametro.
	 */
	public void add(IParametro parametro) {
		parametros.add(parametro);
	}

	/**
	 * Metodo que fornece uma interação nos objetos de parametros.
	 * 
	 * @return um interador de IParametro.
	 */
	public Iterator<IParametro> iterator() {
		return parametros.iterator();
	}

	/**
	 * Metodo que fornece uma array de IParametro.
	 * 
	 * @return um array de IParametro.
	 */
	public IParametro[] toArray() {
		return parametros.toArray(new IParametro[] {});
	}

	@Override
	public String getSql() throws ParametroException {
		StringBuffer sql = new StringBuffer();
		for (IParametro par : parametros) {
			sql.append(par.getSql() + ", ");
		}
		return sql.substring(0, sql.length() - 2);
	}

	@Override
	public Collection<IParametro> getParametro() {
		return getParametro(this);
	}

	@Override
	public Collection<IParametro> getParametro(IParametro filtro) {
		Collection<IParametro> parametros = new ArrayList<IParametro>();
		GrupoParametro gf = (GrupoParametro) filtro;

		for (Iterator<IParametro> it = gf.iterator(); it.hasNext();) {
			IParametro par = it.next();
			parametros.addAll(par.getParametro(par));
		}

		return parametros;
	}

	// Gets e Seteres
	
	public String getCampo() {
		return null;
	}

	public void setCampo(String campo) {
	}

	public Serializable getValor() {
		return null;
	}

	public void setValor(Serializable valor) {
	}

	public ECompara getCompara() {
		return null;
	}

	public void setCompara(ECompara compara) {
	}

	public String getCampoId() {
		return null;
	}

	public void setValorString(String valor) {
	}

	public String getCampoPrefixo() {
		return null;
	}

	public void setCampoPrefixo(String prefixo) {
	}
}
