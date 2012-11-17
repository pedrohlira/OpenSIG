package br.com.opensig.client;

import br.com.opensig.client.visao.AreaTrabalho;
import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.UtilClient;
import br.com.opensig.core.client.visao.Ponte;
import br.com.opensig.core.client.visao.abstrato.ANavegacao;
import br.com.opensig.permissao.client.controlador.comando.ComandoBloquear;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.ui.RootPanel;
import com.gwtext.client.core.EventCallback;
import com.gwtext.client.core.EventManager;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Ext;
import com.gwtext.client.core.ListenerConfig;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.data.XmlReader;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.QuickTips;
import com.gwtext.client.widgets.Viewport;
import com.gwtext.client.widgets.layout.FitLayout;

/**
 * Classe que inicializa o Sistema.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 */
public class OpenSig implements EntryPoint {

	// tempo em minutos
	private int parado = 0;
	private String xmlIdioma = "";

	/**
	 * Metodo que é disparado ao iniciar o projeto.
	 */
	public void onModuleLoad() {
		// verifica se o usuario ficou sem mexer pelo tempo determinado
		Scheduler.get().scheduleFixedPeriod(new RepeatingCommand() {
			public boolean execute() {
				parado++;
				if (Ponte.getLogin() != null && parado == Integer.valueOf(UtilClient.CONF.get("sessao.bloqueio"))) {
					new ComandoBloquear().execute(null);
				}
				return true;
			}
		}, 60000);

		// inicia os objetos basicos e carrega o idioma
		QuickTips.init();
		carregarIdioma();

		EventManager.addListener(Ext.getDoc().getDOM(), "mousemove", new EventCallback() {
			public void execute(EventObject e) {
				parado = 0;
			}
		}, new ListenerConfig());
		EventManager.addListener(Ext.getDoc().getDOM(), "keypress", new EventCallback() {
			public void execute(EventObject e) {
				parado = 0;
			}
		}, new ListenerConfig());
		EventManager.addListener(Ext.getDoc().getDOM(), "contextmenu", new EventCallback() {
			public void execute(EventObject e) {
				e.stopEvent();
			}
		}, new ListenerConfig());
	}

	// carrega os xml contendo os dados do idioma selecionado
	private void carregarIdioma() {
		final String idioma = RootPanel.get("idioma").getElement().getInnerText();
		RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, GWT.getHostPageBaseURL() + "lang/" + idioma + ".xml");

		try {
			requestBuilder.sendRequest(null, new RequestCallback() {
				public void onError(Request request, Throwable exception) {
					MessageBox.alert(OpenSigCore.i18n.txtAtencao(), OpenSigCore.i18n.errInvalido() + " -> idioma = " + idioma);
				}

				public void onResponseReceived(Request request, Response response) {
					xmlIdioma = response.getText();

					// daods
					RecordDef dado = new RecordDef(new FieldDef[] { new StringFieldDef("classe"), new StringFieldDef("nome"), new StringFieldDef("descricao"), new StringFieldDef("imagem") });
					ANavegacao.MODULOS = getStore("modulo", dado);
					ANavegacao.FUNCOES = getStore("funcao", dado);
					ANavegacao.ACOES = getStore("acao", dado);

					abrirTela();
				}
			});
		} catch (RequestException ex) {
			// se der erro ele carrega o padrao que e PT_BR
		}
	}

	// cria a visao principal e adicionar a area de trabalho
	private void abrirTela() {
		Ext.get("loading").fadeOut();
		Panel panel = new Panel();
		panel.setBorder(false);
		panel.setLayout(new FitLayout());
		panel.add(new AreaTrabalho());
		panel.doLayout();

		Viewport view = new Viewport(panel);
		view.doLayout();
	}

	// carrega os xmls em formato de dados Store
	private Store getStore(String tag, RecordDef def) {
		XmlReader reader = new XmlReader(tag, def);
		Store store = new Store(reader);
		store.loadXmlData(xmlIdioma, false);
		return store;
	}

	// Gets e Seteres

	public int getParado() {
		return parado;
	}

	public void setParado(int parado) {
		this.parado = parado;
	}

	public String getXmlIdioma() {
		return xmlIdioma;
	}

	public void setXmlIdioma(String xmlIdioma) {
		this.xmlIdioma = xmlIdioma;
	}

}
