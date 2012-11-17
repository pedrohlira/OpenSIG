package br.com.opensig.fiscal.client.visao.lista;

import java.util.Map;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.UtilClient;
import br.com.opensig.core.client.controlador.comando.AComando;
import br.com.opensig.core.client.controlador.comando.FabricaComando;
import br.com.opensig.core.client.controlador.comando.IComando;
import br.com.opensig.core.client.controlador.comando.lista.ComandoExcluir;
import br.com.opensig.core.client.visao.abstrato.IFormulario;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.fiscal.client.servico.FiscalProxy;
import br.com.opensig.fiscal.client.visao.form.FormularioErro;
import br.com.opensig.fiscal.shared.modelo.ENotaStatus;
import br.com.opensig.fiscal.shared.modelo.FisNotaSaida;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.data.Record;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.MessageBox.PromptCallback;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.menu.Menu;
import com.gwtext.client.widgets.menu.MenuItem;
import com.gwtextux.client.widgets.window.ToastWindow;

public class ListagemSaida extends AListagemNota<FisNotaSaida> {

	private IComando cmdCancelar;
	private String motivo;

	public ListagemSaida(IFormulario<FisNotaSaida> formulario) {
		super(formulario);
		nomes.put("id", "fisNotaSaidaId");
		nomes.put("numero", "fisNotaSaidaNumero");
		nomes.put("cadastro", "fisNotaSaidaCadastro");
		nomes.put("data", "fisNotaSaidaData");
		nomes.put("valor", "fisNotaSaidaValor");
		nomes.put("chave", "fisNotaSaidaChave");
		nomes.put("icms", "fisNotaSaidaIcms");
		nomes.put("ipi", "fisNotaSaidaIpi");
		nomes.put("pis", "fisNotaSaidaPis");
		nomes.put("cofins", "fisNotaSaidaCofins");
		nomes.put("protocolo", "fisNotaSaidaProtocolo");
		nomes.put("xml", "fisNotaSaidaXml");
		nomes.put("protocoloCancelado", "fisNotaSaidaProtocoloCancelado");
		nomes.put("xmlCancelado", "fisNotaSaidaXmlCancelado");
		nomes.put("recibo", "fisNotaSaidaRecibo");
		nomes.put("erro", "fisNotaSaidaErro");
		inicializar();

		// cancelando
		cmdCancelar = new AComando() {
			public void execute(Map contexto) {
				super.execute(contexto);
				int id = UtilClient.getSelecionado(getPanel());
				classe.setId(id);

				FiscalProxy<FisNotaSaida> proxy = new FiscalProxy<FisNotaSaida>();
				proxy.cancelarSaida(classe, motivo, new AsyncCallback<Map<String, String>>() {
					public void onFailure(Throwable caught) {
						getPanel().getEl().unmask();
						MessageBox.alert(OpenSigCore.i18n.txtCancelar(), OpenSigCore.i18n.errExcluir());
					};

					public void onSuccess(Map<String, String> result) {
						getPanel().getEl().unmask();
						Record rec = getSelectionModel().getSelected();

						ENotaStatus status = ENotaStatus.valueOf(result.get("status"));
						rec.set("fisNotaStatus.fisNotaStatusId", status.getId());
						rec.set("fisNotaStatus.fisNotaStatusDescricao", status.toString());
						rec.set("fisNotaSaidaErro", result.get("msg").toString());

						String msg = status == ENotaStatus.ERRO ? OpenSigCore.i18n.errExcluir() : OpenSigCore.i18n.msgExcluirOK();
						new ToastWindow(OpenSigCore.i18n.txtCancelar(), msg).show();
					};
				});
			}
		};
	}

	protected String getXml(FisNotaSaida result) {
		return result.getFisNotaSaidaXml();
	}

	protected String getXmlCancelado(FisNotaSaida result) {
		return result.getFisNotaSaidaXmlCancelado();
	};

	protected String getChave(FisNotaSaida result) {
		return result.getFisNotaSaidaChave();
	}

	protected String getErro(FisNotaSaida result) {
		return result.getFisNotaSaidaErro();
	}
	
	protected void mostrarErro(FisNotaSaida result) {
		Window wnd = new Window(OpenSigCore.i18n.txtErro(), 800, 600, true, false);
		FormularioErro<FisNotaSaida> frmErro = new FormularioErro<FisNotaSaida>(result, getFuncao(), wnd);
		frmErro.setLista(this);
		frmErro.mostrarDados();
		wnd.add(frmErro);
		wnd.show();
	}

	@Override
	public IComando AntesDaAcao(IComando comando) {
		Record rec = getSelectionModel().getSelected();

		// valida se pode excluir ou cancelar
		if (comando instanceof ComandoExcluir) {
			comando = null;
			if (rec != null && rec.getAsInteger("fisNotaStatus.fisNotaStatusId") == ENotaStatus.AUTORIZADO.getId()) {
				MessageBox.prompt(OpenSigCore.i18n.txtCancelar(), OpenSigCore.i18n.msgConfirma(), new PromptCallback() {
					public void execute(String btnID, String text) {
						if (btnID.equalsIgnoreCase("ok")) {
							if (text == null) {
								new ToastWindow(OpenSigCore.i18n.txtCancelar(), OpenSigCore.i18n.errInvalido()).show();
							} else {
								getPanel().getEl().mask(OpenSigCore.i18n.txtAguarde());
								motivo = text;
								cmdCancelar.execute(contexto);
							}
						}
					}
				}, true);
			} else {
				MessageBox.alert(OpenSigCore.i18n.txtAcesso(), OpenSigCore.i18n.txtAcessoNegado());
			}
		}

		return comando;
	}

	@Override
	public void irPara() {
		Menu mnuContexto = new Menu();

		// venda
		String strVenda = FabricaComando.getInstancia().getComandoCompleto("ComandoVenda");
		SisFuncao venda = UtilClient.getFuncaoPermitida(strVenda);
		MenuItem itemVenda = gerarFuncao(venda, "fisNotaSaida.fisNotaSaidaId", "fisNotaSaidaId");
		if (itemVenda != null) {
			mnuContexto.addItem(itemVenda);
		}

		if (mnuContexto.getItems().length > 0) {
			MenuItem mnuItem = getIrPara();
			mnuItem.setMenu(mnuContexto);

			getMenu().addSeparator();
			getMenu().addItem(mnuItem);
		}
	}
}
