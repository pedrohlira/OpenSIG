package br.com.opensig.client.visao.layout;

import com.gwtext.client.core.RegionPosition;
import com.gwtext.client.widgets.TabPanel;
import com.gwtext.client.widgets.layout.BorderLayoutData;
import com.gwtext.client.widgets.layout.LayoutData;

/**
 * Classe do componente visual que compoen a esquerda da area de trabalho.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 */
public class Esquerda extends TabPanel {

	private static Esquerda esquerda;

	/**
	 * Construtor padrao
	 */
	private Esquerda() {
		inicializar();
	}

	/**
	 * Metodo que devolve a referencia a unica instancia.
	 * 
	 * @return referencia a unica instancia do objeto.
	 */
	public static Esquerda getInstancia() {
		if (esquerda == null) {
			esquerda = new Esquerda();
		}
		return esquerda;
	}

	// inicializa os componentes visuais
	private void inicializar() {
		setCollapsible(true);
		setCollapsed(false);
		setWidth(200);
		setStateEvents(new String[] { "collapse", "expand", "resize" });
	}

	/**
	 * Metodo que recupera o layout do componente.
	 * 
	 * @return o tipo de layout usado.
	 */
	public static LayoutData getData() {
		BorderLayoutData data = new BorderLayoutData(RegionPosition.WEST);
		data.setSplit(true);
		data.setMinSize(100);
		data.setMaxSize(300);
		return data;
	}
}
