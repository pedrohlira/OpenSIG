package br.com.opensig.core.client.controlador.filtro;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import br.com.opensig.core.client.controlador.parametro.IParametro;
import br.com.opensig.core.client.controlador.parametro.ParametroException;

/**
 * Classe que representa um agrupamento de filtros, podendo ter filtros ou mesmo
 * grupos de filtros.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 */

public class GrupoFiltro implements IFiltro {

	private Map<IFiltro, EJuncao> filtros = new LinkedHashMap<IFiltro, EJuncao>();

	/**
	 * Construtor padrao.
	 */
	public GrupoFiltro() {
	}

	/**
	 * Contrutor com multiplos filtros
	 * 
	 * @param juncao
	 *            a Juncao padrao de todos os filtros passados.
	 * @param filtros
	 *            multiplos filtros.
	 */
	public GrupoFiltro(EJuncao juncao, IFiltro[] filtros) {
		if (filtros != null) {
			for (IFiltro fil : filtros) {
				add(fil, juncao);
			}
		}
	}

	/**
	 * Metodo que remove um filtro ou grupo.
	 * 
	 * @param filtro
	 *            do tipo IFiltro.
	 */
	public void remove(IFiltro filtro) {
		filtros.remove(filtro);
	}

	/**
	 * Metodo que limpa os filtros do grupo.
	 */
	public void clear() {
		filtros.clear();
	}

	/**
	 * Metodo que retorna a quantidade de filtros dentro do grupo.
	 * 
	 * @return quantidade de filtros.
	 */
	public int size() {
		return filtros.size();
	}

	/**
	 * Metodo que adiciona um filtro ou grupo sem junçao, filtro final.
	 * 
	 * @param filtro
	 *            do tipo IFiltro.
	 * @throws ParametroException
	 *             ocorre caso seja adicionado mais de um filtro com junçao
	 *             null.
	 */
	public void add(IFiltro filtro) throws IllegalArgumentException {
		add(filtro, null);
	}

	/**
	 * Metodo que adiciona um filtro ou grupo.
	 * 
	 * @param filtro
	 *            do tipo IFiltro.
	 * @param juncao
	 *            a forma de uniao com o próximo filtro, deve ser null caso nao
	 *            tenha outro filtro.
	 * @throws ParametroException
	 *             ocorre caso seja adicionado mais de um filtro com junçao
	 *             null.
	 */
	public void add(IFiltro filtro, EJuncao juncao) throws IllegalArgumentException {
		if (juncao == null && filtros.containsValue(null)) {
			throw new IllegalArgumentException("Falta a junçao!");
		} else {
			filtros.put(filtro, juncao);
		}
	}

	/**
	 * Metodo que fornece uma interaçao nos objetos de filtros.
	 * 
	 * @return um interador de IFiltro.
	 */
	public Iterator<IFiltro> iterator() {
		return filtros.keySet().iterator();
	}

	public IFiltro[] toArray() {
		return filtros.keySet().toArray(new IFiltro[] {});
	}

	@Override
	public String getSql() throws ParametroException {
		StringBuffer sql = new StringBuffer();
		if (!filtros.isEmpty()) {
			sql.append("(");

			for (Iterator<Entry<IFiltro, EJuncao>> it = filtros.entrySet().iterator(); it.hasNext();) {
				Entry<IFiltro, EJuncao> grupoFiltro = it.next();
				if (grupoFiltro.getKey() != null) {
					sql.append(grupoFiltro.getKey().getSql());
				}
				if (grupoFiltro.getValue() != null && it.hasNext()) {
					sql.append(" " + grupoFiltro.getValue().toString() + " ");
				}
			}

			sql.append(")");
		}

		return sql.toString();
	}

	@Override
	public Collection<IParametro> getParametro() {
		return getParametro(this);
	}

	@Override
	public Collection<IParametro> getParametro(IParametro param) {
		Collection<IParametro> parametros = new ArrayList<IParametro>();
		GrupoFiltro gf = (GrupoFiltro) param;

		for (Iterator<IFiltro> it = gf.iterator(); it.hasNext();) {
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
