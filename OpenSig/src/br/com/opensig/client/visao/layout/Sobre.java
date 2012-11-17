package br.com.opensig.client.visao.layout;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.visao.abstrato.ANavegacao;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.gwtext.client.core.Position;
import com.gwtext.client.data.Record;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.TabPanel;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.form.MultiFieldPanel;
import com.gwtext.client.widgets.form.TextArea;
import com.gwtext.client.widgets.layout.ColumnLayout;
import com.gwtext.client.widgets.layout.ColumnLayoutData;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtext.client.widgets.layout.FormLayout;
import com.gwtext.client.widgets.layout.VerticalLayout;

/**
 * Classe do componente visual que tem os dados de contato da empresa.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 */
public class Sobre {

	/**
	 * Construtor padrao.
	 */
	public Sobre() {
		inicializar();
	}

	// inicializa os componentes visuais
	private void inicializar() {
		Window wndSobre = new Window(OpenSigCore.i18n.txtSobre(), 500, 280, true, false);
		wndSobre.setButtonAlign(Position.CENTER);
		wndSobre.setIconCls("icon-sobre");
		wndSobre.setLayout(new FitLayout());

		TabPanel tab = new TabPanel();
		tab.add(getPrincipal());
		tab.add(getModulos());
		tab.add(getLicenca());
		tab.setActiveTab(0);

		wndSobre.add(tab);
		wndSobre.show();
	}

	// primeira aba
	private Panel getPrincipal() {
		Panel panPrincipal = new Panel();
		panPrincipal.setLayout(new FormLayout());
		panPrincipal.setHeader(false);
		panPrincipal.setIconCls("icon-principal");
		panPrincipal.setTitle(OpenSigCore.i18n.txtSistema());
		panPrincipal.add(getSobre());
		panPrincipal.add(getContato());
		panPrincipal.add(getVersao());

		return panPrincipal;
	}

	// aba de sobre
	private Panel getSobre() {
		Panel col1 = new Panel();
		col1.setPaddings(5);
		col1.setBorder(false);
		col1.setLayout(new VerticalLayout(5));

		Label lblSistema = new Label(OpenSigCore.i18n.txtSistema());
		col1.add(lblSistema);

		Label lblEmpresa = new Label(OpenSigCore.i18n.txtEmpresa());
		col1.add(lblEmpresa);

		Label lblAutor = new Label(OpenSigCore.i18n.txtAutor());
		col1.add(lblAutor);

		Panel col2 = new Panel();
		col2.setPaddings(5);
		col2.setBorder(false);
		col2.setLayout(new VerticalLayout(5));

		Label sistema = new Label("OpenSIG - Sistemas Integrados Gerenciáveis Open Source");
		sistema.setStyle("font-weight: bold");
		col2.add(sistema);

		Label empresa = new Label("PhD - Systems Solutions");
		empresa.setStyle("font-weight: bold");
		col2.add(empresa);

		Label autor = new Label("Pedro Henrique de Lira");
		autor.setStyle("font-weight: bold");
		col2.add(autor);

		Panel panSobre = new Panel();
		panSobre.setBorder(false);
		panSobre.setLayout(new ColumnLayout());
		panSobre.add(col1, new ColumnLayoutData(.2));
		panSobre.add(col2, new ColumnLayoutData(.8));

		return panSobre;
	}

	// aba de contatos
	private Panel getContato() {
		Panel col1 = new Panel();
		col1.setPaddings(5);
		col1.setBorder(false);
		col1.setLayout(new VerticalLayout(5));

		Label site = new Label(OpenSigCore.i18n.txtSite() + ": " + OpenSigCore.i18n.msgSite());
		col1.add(site);

		Label email = new Label(OpenSigCore.i18n.txtEmail() + ": " + OpenSigCore.i18n.msgEmail());
		col1.add(email);

		Panel col2 = new Panel();
		col2.setPaddings(5);
		col2.setBorder(false);
		col2.setLayout(new VerticalLayout(5));

		Label telefone = new Label(OpenSigCore.i18n.txtTelefone() + ": " + OpenSigCore.i18n.msgTelefone());
		col2.add(telefone);

		Label skype = new Label(OpenSigCore.i18n.txtSkype() + ": " + OpenSigCore.i18n.msgSkype());
		col2.add(skype);

		Panel panContato = new Panel();
		panContato.setBorder(false);
		panContato.setLayout(new ColumnLayout());
		panContato.add(col1, new ColumnLayoutData(.6));
		panContato.add(col2, new ColumnLayoutData(.4));
		panContato.setTitle(OpenSigCore.i18n.txtContato(), "icon-contato");

		return panContato;
	}

	// aba de versao
	private Panel getVersao() {
		Panel panVersao = new Panel();
		panVersao.setBorder(false);
		panVersao.setPaddings(5);
		panVersao.setLayout(new FitLayout());
		panVersao.setTitle(OpenSigCore.i18n.txtVersao());
		panVersao.setIconCls("icon-informacao");

		Label lblVersao = new Label(OpenSigCore.i18n.txtVersao() + ":");
		final Label lblVersao1 = new Label();
		lblVersao1.setStyle("font-weight: bold");

		Label lblData = new Label(OpenSigCore.i18n.txtData() + ":");
		final Label lblData1 = new Label();
		lblData1.setStyle("font-weight: bold");

		MultiFieldPanel linha1 = new MultiFieldPanel();
		linha1.setBorder(false);
		linha1.addToRow(lblVersao, 50);
		linha1.addToRow(lblVersao1, 250);
		linha1.addToRow(lblData, 40);
		linha1.addToRow(lblData1, 120);
		panVersao.add(linha1);

		RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, GWT.getHostPageBaseURL() + "manifesto/OpenSig.MF");
		try {
			requestBuilder.sendRequest(null, new RequestCallback() {
				public void onError(Request request, Throwable exception) {
				}

				public void onResponseReceived(Request request, Response response) {
					String[] linhas = response.getText().split("\n");

					for (String linha : linhas) {
						if (linha.startsWith("Implementation-Version")) {
							lblVersao1.setText(linha.split(":")[1]);
						} else if (linha.startsWith("Built-Date")) {
							lblData1.setText(linha.split(": ")[1]);
						}
					}
				}
			});
		} catch (RequestException ex) {
		}

		return panVersao;
	}

	// aba dos modulos
	private TabPanel getModulos() {
		TabPanel tab = new TabPanel();
		tab.setEnableTabScroll(true);
		tab.setTabPosition(Position.BOTTOM);
		tab.setIconCls("icon-modulo");
		tab.setTitle(OpenSigCore.i18n.txtModulos());

		for (Record rec : ANavegacao.MODULOS.getRecords()) {
			String[] classe = rec.getAsString("classe").split("\\.");
			String nome = "manifesto/" + classe[classe.length - 1] + ".MF";

			Panel panModulo = new Panel();
			panModulo.setLayout(new FitLayout());
			panModulo.setIconCls(rec.getAsString("imagem"));
			panModulo.setTitle("|");

			TextArea area = new TextArea();
			area.setHeight(155);
			area.setReadOnly(true);
			panModulo.add(area);

			tab.add(panModulo);
			carregaTexto(area, nome);
		}

		return tab;
	}

	// aba da licenca
	private Panel getLicenca() {
		Panel panLicenca = new Panel();
		panLicenca.setLayout(new FitLayout());
		panLicenca.setTitle(OpenSigCore.i18n.txtLicenca());
		panLicenca.setIconCls("icon-padrao");

		TextArea area = new TextArea();
		area.setHeight(200);
		area.setReadOnly(true);
		panLicenca.add(area);

		carregaTexto(area, "licenca.txt");

		return panLicenca;
	}

	// carrega o conteudo do texto
	private void carregaTexto(final TextArea area, final String arquivo) {
		RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, GWT.getHostPageBaseURL() + arquivo);

		try {
			requestBuilder.sendRequest(null, new RequestCallback() {
				public void onError(Request request, Throwable exception) {
					area.setValue(OpenSigCore.i18n.errRegistro());
				}

				public void onResponseReceived(Request request, Response response) {
					if (arquivo.contains("MF")) {
						String[] linhas = response.getText().split("\n");
						StringBuffer sb = new StringBuffer();
						for (String linha : linhas) {
							if (linha.startsWith("Implementation") || linha.startsWith("Built")) {
								sb.append(linha);
							}
						}
						area.setValue(sb.toString());
					} else {
						area.setValue(response.getText());
					}
				}
			});
		} catch (RequestException ex) {
			area.setValue(OpenSigCore.i18n.errRegistro());
		}
	}
}
