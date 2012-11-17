package br.com.opensig.client.visao.layout;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.padroes.Observable;
import br.com.opensig.core.client.padroes.Observer;
import br.com.opensig.core.client.visao.Ponte;
import br.com.opensig.core.shared.modelo.ILogin;
import br.com.opensig.permissao.client.controlador.comando.ComandoAlterarSenha;
import br.com.opensig.permissao.client.controlador.comando.ComandoBloquear;
import br.com.opensig.permissao.client.controlador.comando.ComandoSair;

import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.ToolbarMenuButton;
import com.gwtext.client.widgets.menu.BaseItem;
import com.gwtext.client.widgets.menu.Menu;
import com.gwtext.client.widgets.menu.MenuItem;
import com.gwtext.client.widgets.menu.event.BaseItemListenerAdapter;

/**
 * Classe do componente visual que compoen a esquerda da area de trabalho.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 */
public class Usuario extends ToolbarMenuButton implements Observer {

	private MenuItem itemEmpresa;

	/**
	 * Construtor padrao
	 */
	public Usuario() {
		inicializar();
	}

	// inicializa os componentes visuais
	private void inicializar() {
		itemEmpresa = new MenuItem();
		itemEmpresa.setIconCls("icon-empresas");
		getEmpresaAtiva(Ponte.getLogin());

		MenuItem itemSenha = new MenuItem();
		itemSenha.setText(OpenSigCore.i18n.txtSenha());
		itemSenha.setTitle(OpenSigCore.i18n.msgSenha());
		itemSenha.setIconCls("icon-entrar");
		itemSenha.addListener(new BaseItemListenerAdapter() {
			public void onClick(BaseItem item, EventObject e) {
				new ComandoAlterarSenha().execute(null);
			}
		});

		MenuItem itemBloquear = new MenuItem();
		itemBloquear.setText(OpenSigCore.i18n.txtBloquear());
		itemBloquear.setTitle(OpenSigCore.i18n.msgBloquear());
		itemBloquear.setIconCls("icon-acesso");
		itemBloquear.addListener(new BaseItemListenerAdapter() {
			public void onClick(BaseItem item, EventObject e) {
				new ComandoBloquear().execute(null);
			}
		});

		MenuItem itemSobre = new MenuItem();
		itemSobre.setText(OpenSigCore.i18n.txtSobre());
		itemSobre.setTitle(OpenSigCore.i18n.msgSobre());
		itemSobre.setIconCls("icon-sobre");
		itemSobre.addListener(new BaseItemListenerAdapter() {
			public void onClick(BaseItem item, EventObject e) {
				new Sobre();
			}
		});

		MenuItem itemSair = new MenuItem();
		itemSair.setText(OpenSigCore.i18n.txtSair());
		itemSair.setTitle(OpenSigCore.i18n.txtSairDescricao());
		itemSair.setIconCls("icon-sair");
		itemSair.addListener(new BaseItemListenerAdapter() {
			public void onClick(BaseItem item, EventObject e) {
				new ComandoSair().execute(null);
			}
		});

		Menu menu = new Menu();
		menu.addItem(itemEmpresa);
		menu.addSeparator();
		menu.addItem(itemSenha);
		menu.addItem(itemBloquear);
		menu.addItem(itemSobre);
		menu.addSeparator();
		menu.addItem(itemSair);

		setText(OpenSigCore.i18n.txtUsuario());
		setIconCls("icon-usuario");
		setMenu(menu);
		getLoginAtivo(Ponte.getLogin());
	}

	@Override
	public void update(Observable o, Object arg) {
		if (arg instanceof ILogin) {
			getLoginAtivo((ILogin) arg);
			getEmpresaAtiva((ILogin) arg);
		}
	}

	// pega o login ativo
	private void getLoginAtivo(ILogin login) {
		if (login == null) {
			setText(" ");
		} else {
			setText("<b>" + login.getUsuario() + "</b> ");
		}
	}

	// pega a empresa ativa
	private void getEmpresaAtiva(ILogin login) {
		if (login == null) {
			itemEmpresa.setText(OpenSigCore.i18n.txtEmpresa() + ": ");
		} else {
			itemEmpresa.setText(OpenSigCore.i18n.txtEmpresa() + ": <b>" + login.getEmpresaNome() + "</b> ");
		}
	}
}
