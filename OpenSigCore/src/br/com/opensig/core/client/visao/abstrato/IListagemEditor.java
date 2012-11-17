package br.com.opensig.core.client.visao.abstrato;

import java.util.List;
import java.util.Map;

import br.com.opensig.core.client.controlador.comando.IComando;
import br.com.opensig.core.client.controlador.filtro.IFiltro;
import br.com.opensig.core.client.servico.CoreProxy;
import br.com.opensig.core.shared.modelo.Dados;

import com.gwtext.client.data.RecordDef;
import com.gwtext.client.widgets.Component;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.EditorGridPanel;
import com.gwtext.client.widgets.menu.Item;
import com.gwtext.client.widgets.menu.Menu;

/**
 * Interface que representa o modelo genérico das sub-listagens do sistema.
 * 
 * @param <E>
 *            classe generica de dados.
 * @author Pedro H. Lira
 * @version 1.0
 */
public interface IListagemEditor<E extends Dados> {

	/**
	 * Metodo que inicializa os objetos visuais.
	 */
	public void inicializar();

	/**
	 * Metodo que seta o campo que ser escutado pela sub-listagem pertencente ao
	 * formulario.
	 * 
	 * @param campo
	 *            o objeto do campo.
	 */
	public void setEditorListener(Field campo);

	/**
	 * Metodo que retorna o objeto visual da sub-listagem.
	 * 
	 * @return o objeto visual da sub-listagem.
	 */
	public EditorGridPanel getPanel();

	/**
	 * Metodo que e executada antes de qualquer comando da sub-listagem.
	 * 
	 * @param comando
	 *            ativado no momento pelo usuario.
	 * @return o comando que sera executado pelo usuario, apos validacoes.
	 */
	public IComando AntesDoComando(IComando comando);

	/**
	 * Metodo que e executado apos qualquer comando da sub-listagem.
	 * 
	 * @param comando
	 *            ativado no momento pelo usuario.
	 */
	public void DepoisDoComando(IComando comando);

	public ToolbarButton getAdicionar();

	public ToolbarButton getRemover();

	public Item getMenuAdicionar();

	public Item getMenuRemover();

	public boolean validar(List<E> lista);

	public RecordDef getCampos();

	public void setCampos(RecordDef campos);

	public ColumnModel getModelos();

	public void setModelos(ColumnModel modelos);

	public IFiltro getFiltroPadrao();

	public void setFiltroPadrao(IFiltro filtroPadrao);

	public CoreProxy<E> getProxy();

	public void setProxy(CoreProxy<E> persistencia);

	public E getClasse();

	public ToolbarButton getBtnAdicionar();

	public void setBtnAdicionar(ToolbarButton btnAdicionar);

	public ToolbarButton getBtnRemover();

	public void setBtnRemover(ToolbarButton btnRemover);

	public boolean isBarraTarefa();

	public void setBarraTarefa(boolean barraTarefa);

	public Menu getMenu();

	public void setMenu(Menu menu);

	public Map getContexto();

	public void setContexto(Map contexto);

	public int[] getCelula();

	public void setCelula(int[] celula);

	public void setClasse(E classe);

	public Component getFocusPadrao();

	public void setFocusPadrao(Component focusPadrao);

}