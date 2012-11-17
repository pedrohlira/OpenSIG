package br.com.opensig.core.client.controlador.comando;

import java.util.Map;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.visao.abstrato.IFormulario;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.layout.FitLayout;

/**
 * Classe de comando formulario, usada para abrir um formulario isolado da
 * listagem.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 */
public class ComandoFormulario extends ComandoFuncao {

	private Window wnd;

	/**
	 * Construtor padrao.
	 * 
	 * @param formulario
	 *            O objeto que representa o formulario.
	 */
	public ComandoFormulario(IFormulario formulario) {
		this(formulario, null);
	}

	/**
	 * Construtor padrao.
	 * 
	 * @param formulario
	 *            O objeto que representa o formulario.
	 * @param async
	 *            funcao de retorno a ser disparada apos as acoes de Salvar ou
	 *            Cancelar do formulario.
	 */
	public ComandoFormulario(final IFormulario formulario, final AsyncCallback async) {
		FORM = formulario;
		FORM.getPanel().setHeader(false);
		FORM.getPanel().enable();

		// muda o botao o cancelar
		ToolbarButton btnCancelar = new ToolbarButton(OpenSigCore.i18n.txtCancelar());
		btnCancelar.setTooltip(OpenSigCore.i18n.msgCancelar());
		btnCancelar.setIconCls("icon-cancelar");
		btnCancelar.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				wnd.close();
				async.onFailure(null);
			}
		});

		// muda o botao salvar
		final IComando cmdFechar = new AComando() {
			public void execute(Map contexto) {
				wnd.close();
				async.onSuccess(contexto.get("resultado"));
			}
		};

		ToolbarButton btnSalvar = new ToolbarButton(OpenSigCore.i18n.txtSalvar());
		btnSalvar.setTooltip(OpenSigCore.i18n.msgSalvar());
		btnSalvar.setIconCls("icon-salvar");
		btnSalvar.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				FORM.salvar(cmdFechar);
			}
		});

		// muda a barra de tarefas
		Toolbar tool = new Toolbar();
		tool.addButton(btnSalvar);
		tool.addButton(btnCancelar);
		FORM.setTlbAcao(tool);
	}

	@Override
	public void execute(Map contexto) {
		wnd = new Window();
		wnd.setLayout(new FitLayout());
		wnd.setModal(true);
		wnd.setClosable(false);
		wnd.setWidth(700);
		wnd.setHeight(550);
		wnd.setTitle(FORM.getPanel().getTitle(), FORM.getPanel().getIconCls());
		wnd.add(FORM.getPanel());
		wnd.show();

		if (comando != null) {
			comando.execute(contexto);
		}
	}
}
