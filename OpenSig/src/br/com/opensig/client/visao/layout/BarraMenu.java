package br.com.opensig.client.visao.layout;

import br.com.opensig.client.visao.NavegacaoMenu;
import br.com.opensig.core.client.padroes.Visitable;
import br.com.opensig.core.client.padroes.Visitor;
import br.com.opensig.core.client.padroes.Observable;
import br.com.opensig.core.client.padroes.Observer;
import br.com.opensig.core.client.visao.Ponte;
import br.com.opensig.core.shared.modelo.ILogin;
import br.com.opensig.core.shared.modelo.sistema.SisModulo;

import com.gwtext.client.widgets.Toolbar;

/**
 * Classe do componente visual de barra de tarefa com os menus de
 * modulos/funcoes
 * 
 * @author Pedro H. Lira
 * @version 1.0
 */
public class BarraMenu extends Toolbar implements Observer, Visitable {

	private static Toolbar menus;

	/**
	 * Construtor padrao
	 */
	private BarraMenu() {
		inicializar();
	}

	/**
	 * Metodo que devolve a referencia a unica instancia.
	 * 
	 * @return referencia a unica instancia do objeto.
	 */
	public static Toolbar getInstancia() {
		if (menus == null) {
			menus = new BarraMenu();
		}
		return menus;
	}

	// inicializa os componentes visuais
	private void inicializar() {
		setHeight(25);
		Ponte.getInstancia().addObserver(this);
	}

	@Override
	public void update(Observable o, Object arg) {
		if (arg instanceof ILogin) {
			accept(new NavegacaoMenu());
		}
	}

	@Override
	public void accept(Visitor visitor) {
		SisModulo[] modulos = Ponte.getLogin().getModulos().toArray(new SisModulo[0]);
		visitor.visit(modulos);
		menus.addFill();
		menus.addButton(new Usuario());
	}

}
