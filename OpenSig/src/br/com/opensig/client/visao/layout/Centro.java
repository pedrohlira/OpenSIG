package br.com.opensig.client.visao.layout;

import br.com.opensig.client.visao.portal.Principal;
import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.padroes.Observable;
import br.com.opensig.core.client.padroes.Observer;
import br.com.opensig.core.client.visao.Ponte;
import br.com.opensig.core.shared.modelo.ILogin;

import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.RegionPosition;
import com.gwtext.client.widgets.Component;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.TabPanel;
import com.gwtext.client.widgets.event.TabPanelListenerAdapter;
import com.gwtext.client.widgets.layout.BorderLayoutData;
import com.gwtext.client.widgets.layout.LayoutData;
import com.gwtext.client.widgets.menu.BaseItem;
import com.gwtext.client.widgets.menu.Item;
import com.gwtext.client.widgets.menu.Menu;
import com.gwtext.client.widgets.menu.event.BaseItemListenerAdapter;

/**
 * Classe do componente visual que compoen o centro da area de trabalho.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 */
public class Centro extends TabPanel implements Observer {

	private static TabPanel centro;
	private Menu menu = new Menu();

	/**
	 * Construtor padrao
	 */
	private Centro() {
		inicializar();
	}

	/**
	 * Metodo que devolve a referencia a unica instancia.
	 * 
	 * @return referencia a unica instancia do objeto.
	 */
	public static TabPanel getInstancia() {
		if (centro == null) {
			centro = new Centro();
		}
		return centro;
	}

	// inicializa os componentes visuais
	private void inicializar() {
		add(Principal.getInstancia());
		setEnableTabScroll(true);
		setActiveTab(0);

		Item mnuFechar = new Item(OpenSigCore.i18n.txtFechar());
		mnuFechar.setIconCls("icon-fecharAba");
		mnuFechar.addListener(new BaseItemListenerAdapter() {
			public void onClick(BaseItem item, EventObject e) {
				centro.remove(centro.getActiveTab());
			}
		});
		menu.addItem(mnuFechar);
		menu.addSeparator();

		Item mnuFecharTodas = new Item(OpenSigCore.i18n.txtFecharTodas());
		mnuFecharTodas.setIconCls("icon-fecharAba");
		mnuFecharTodas.addListener(new BaseItemListenerAdapter() {
			public void onClick(BaseItem item, EventObject e) {
				fecharAba("");
			}
		});
		menu.addItem(mnuFecharTodas);

		Item mnuOutras = new Item(OpenSigCore.i18n.txtFecharOutras());
		mnuOutras.setIconCls("icon-fecharAba");
		mnuOutras.addListener(new BaseItemListenerAdapter() {
			public void onClick(BaseItem item, EventObject e) {
				fecharAba(centro.getActiveTab().getId());
			}
		});
		menu.addItem(mnuOutras);

		addListener(new TabPanelListenerAdapter() {
			public void onContextMenu(TabPanel source, Panel tab, EventObject e) {
				showMenu(tab, e);
				source.activate(tab.getId());
			}
		});

		Ponte.getInstancia().addObserver(this);
	}

	/**
	 * Metodo que recupera o layout do componente.
	 * 
	 * @return o tipo de layout usado.
	 */
	public static LayoutData getData() {
		BorderLayoutData data = new BorderLayoutData(RegionPosition.CENTER);
		return data;
	}

	@Override
	public void update(Observable o, Object arg) {
		if (arg instanceof ILogin) {
		}
	}

	// abre o menu suspenso no local do mouse.
	private void showMenu(Panel tab, EventObject e) {
		BaseItem itemFechar = menu.getItems()[0];
		itemFechar.setDisabled(!tab.isClosable());

		BaseItem itemFecharTodas = menu.getItems()[1];
		itemFecharTodas.setDisabled(!tab.isClosable());

		menu.showAt(e.getXY());
	}

	// acao de fechar a aba
	private void fecharAba(String tabId) {
		Component[] items = centro.getItems();
		for (int i = 0; i < items.length; i++) {
			Panel panel = (Panel) items[i];
			if (panel.isClosable() && !panel.getId().equals(tabId)) {
				centro.remove(panel);
			}
		}
	}
}
