package br.com.opensig.core.client.visao;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.UtilClient;
import br.com.opensig.core.client.controlador.filtro.FiltroTexto;
import br.com.opensig.core.client.controlador.filtro.GrupoFiltro;
import br.com.opensig.core.client.controlador.parametro.IParametro;
import br.com.opensig.core.client.controlador.parametro.ParametroException;
import br.com.opensig.core.client.servico.CoreException;
import br.com.opensig.core.client.servico.CoreProxy;
import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.core.shared.modelo.EDirecao;
import br.com.opensig.core.shared.modelo.Lista;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.UrlParam;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.MessageBox.AlertCallback;
import com.gwtextux.client.widgets.window.ToastWindow;

/**
 * Classe abstrata que padroniza a persistencia dos dados.
 * 
 * @param <E>
 *            a tipagem do objeto a ser persistido um POJO, usado para filtros
 *            de combobox.
 * @author Pedro H. Lira
 * @version 1.0
 */
public class ComboEntidade<E extends Dados> extends CoreProxy<E> {

	/**
	 * Construtor que recebe um objeto da mesma tipagem da classe.
	 * 
	 * @param classe
	 *            objeto tipado do mesmo tipo da classe.
	 */
	public ComboEntidade(E classe) {
		super(classe);
	}

	@Override
	public void load(int start, int limit, String sort, String dir, final JavaScriptObject o, UrlParam[] baseParams) {
		sort = UtilClient.getCampoPrefixado(baseParams[0].getValue().replaceAll("__", "."));
		String valor = baseParams.length > 1 ? baseParams[1].getValue() : "";
		GrupoFiltro filtros = (GrupoFiltro) getFiltroPadrao();

		for (IParametro param : filtros.getParametro()) {
			if (param instanceof FiltroTexto) {
				param.setValorString(valor);
			}
		}

		classe.setCampoOrdem(sort);
		classe.setOrdemDirecao(EDirecao.ASC);
		selecionar(classe, 0, 0, filtros, true, new AsyncCallback<Lista<E>>() {

			public void onFailure(Throwable caught) {
				if (caught instanceof ParametroException) {
					new ToastWindow(OpenSigCore.i18n.txtAtencao(), OpenSigCore.i18n.errFiltro());
				} else if (caught instanceof CoreException) {
					new ToastWindow(OpenSigCore.i18n.txtAtencao(), OpenSigCore.i18n.errListagem());
				} else {
					MessageBox.alert(OpenSigCore.i18n.txtAtencao(), OpenSigCore.i18n.errSessao(), new AlertCallback() {
						public void execute() {
							Window.Location.reload();
						}
					});
				}

				loadResponse(o, false, 0, (JavaScriptObject) null);
			}

			public void onSuccess(Lista<E> result) {
				loadResponse(o, true, result.getTotal(), result.getDados());
			}
		});
	}
}
