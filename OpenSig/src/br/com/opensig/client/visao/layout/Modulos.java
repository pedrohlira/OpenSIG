package br.com.opensig.client.visao.layout;

import br.com.opensig.client.visao.NavegacaoTarefa;
import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.padroes.Visitable;
import br.com.opensig.core.client.padroes.Visitor;
import br.com.opensig.core.client.padroes.Observable;
import br.com.opensig.core.client.padroes.Observer;
import br.com.opensig.core.client.visao.Ponte;
import br.com.opensig.core.shared.modelo.ILogin;
import br.com.opensig.core.shared.modelo.sistema.SisModulo;

import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.layout.FitLayout;

/**
 * Classe do componente visual que compoen a esquerda da area de trabalho.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 */
public class Modulos extends Panel implements Observer, Visitable {

	private static Modulos modulos;

	/**
	 * Construtor padrao
	 */
	private Modulos() {
		inicializar();
		Ponte.getInstancia().addObserver(this);
	}

	/**
	 * Metodo que devolve a referencia a unica instancia.
	 * 
	 * @return referencia a unica instancia do objeto.
	 */
	public static Modulos getInstancia() {
		if (modulos == null) {
			modulos = new Modulos();
		}
		return modulos;
	}

	// inicializa os componentes visuais
	private void inicializar() {
		setTitle(OpenSigCore.i18n.txtModulos());
		setIconCls("icon-modulo");
		setLayout(new FitLayout());
	}

	@Override
	public void update(Observable o, final Object arg) {
		if (arg instanceof ILogin) {
			accept(new NavegacaoTarefa());
		}
	}

	@Override
	public void accept(Visitor visitor) {
		Esquerda.getInstancia().getEl().mask(OpenSigCore.i18n.txtAguarde());
		SisModulo[] modulos = Ponte.getLogin().getModulos().toArray(new SisModulo[0]);
		visitor.visit(modulos);
		doLayout();
		Esquerda.getInstancia().getEl().unmask();
	}
}
