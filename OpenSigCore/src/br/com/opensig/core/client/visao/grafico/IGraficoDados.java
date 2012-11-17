package br.com.opensig.core.client.visao.grafico;

import java.util.Collection;
import java.util.List;

import br.com.opensig.core.client.visao.abstrato.IListagem;
import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.core.shared.modelo.EBusca;
import br.com.opensig.core.shared.modelo.EData;

import com.rednels.ofcgwt.client.model.ChartData;

/**
 * Interface que representa o modelo de dados em forma de objeto do grafico.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 */
public interface IGraficoDados<E extends Dados> {

	public String[] getCores(int total);

	public Number getValor(String[] valor);

	public List<String> getRotulos();

	public Collection<String[]> getDados();

	public void setDados(Collection<String[]> dados);

	public IListagem<E> getLista();

	public void setLista(IListagem<E> lista);

	public EData getParteData();

	public void setParteData(EData parteData);

	public EBusca getGrupoValor();

	public void setGrupoValor(EBusca grupoValor);

	public int getLimite();

	public void setLimite(int limite);

	public void setData(ChartData data);

}