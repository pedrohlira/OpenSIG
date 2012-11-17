package br.com.opensig.core.client.visao;

import java.util.ArrayList;
import java.util.List;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.shared.modelo.Dados;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.NameValuePair;
import com.gwtext.client.core.Position;
import com.gwtext.client.core.UrlParam;
import com.gwtext.client.util.JavaScriptObjectHelper;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.MessageBox.PromptCallback;
import com.gwtext.client.widgets.MessageBoxConfig;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.TextArea;
import com.gwtext.client.widgets.layout.ColumnLayout;
import com.gwtext.client.widgets.layout.ColumnLayoutData;
import com.gwtextux.client.widgets.upload.UploadDialog;
import com.gwtextux.client.widgets.upload.UploadDialogListenerAdapter;

/**
 * Classe abstrata que define as opcoes padroes de upload de arquivos.
 * 
 * @param <E>
 *            O tipo de dados de classe.
 * @author Pedro H. Lira
 * @version 1.0
 */
public class JanelaUpload<E extends Dados> {

	/**
	 * Define o paramento com a acao de ler o arquivo carregado.
	 */
	public static final UrlParam ACAO_LER = new UrlParam("acao", "ler");
	/**
	 * Define o paramento com a acao de salvar o arquivo carregado.
	 */
	public static final UrlParam ACAO_SALVAR = new UrlParam("acao", "salvar");
	/**
	 * Define o paramento com o local em sessao para salvar o arquivo carregado.
	 */
	public static final UrlParam LOCAL_SESSAO = new UrlParam("local", "sessao");
	/**
	 * Define o paramento com o local fisico para salvar o arquivo carregado.
	 */
	public static final UrlParam LOCAL_FISICO = new UrlParam("local", "fisico");

	/**
	 * Define o paramento com o path relativo usado no arquivo ao salvar.
	 * 
	 * @param path
	 *            a String do path a ser usadao.
	 * @return o parametro correspondente.
	 */
	public static final UrlParam PATH_RELATIVO(String path) {
		return new UrlParam("pathRelativo", path);
	}

	/**
	 * Define o paramento com o path real usado no arquivo ao salvar.
	 * 
	 * @param path
	 *            a String do path a ser usadao.
	 * @return o parametro correspondente.
	 */
	public static final UrlParam PATH_REAL(String path) {
		return new UrlParam("pathReal", path);
	}

	/**
	 * Define o paramento com o nome usado no arquivo ao salvar.
	 * 
	 * @param nome
	 *            a String do nome a ser usadao.
	 * @return o parametro correspondente.
	 */
	public static final UrlParam NOME_ARQUIVO(String nome) {
		return new UrlParam("nomeArquivo", nome);
	}

	/**
	 * Define o paramento com o separador usado no agrupamento de varios textos.
	 * 
	 * @param separador
	 *            a String do separador a ser usadao.
	 * @return o parametro correspondente.
	 */
	public static final UrlParam NOME_SEPARADOR(String separador) {
		return new UrlParam("separador", separador);
	}

	/**
	 * A janela de upload do arquivo.
	 */
	protected UploadDialog uplArquivo;
	/**
	 * Descricao dos encontrados nos arquivos.
	 */
	protected List<String> oks;
	/**
	 * Descricao dos encontrados nos arquivos.
	 */
	protected List<String> erros;
	/**
	 * Os tipos dos arquivos aceitos.
	 */
	protected String[] tipos;
	/**
	 * Os parametros para salvar
	 */
	protected UrlParam[] params;
	/**
	 * A funcao de retorno da resposta.
	 */
	protected AsyncCallback assincrono;

	/**
	 * Construtor padrao.
	 */
	public JanelaUpload() {
		this(null);
	}

	/**
	 * Construtor padrao que define somente um tipo de arquivo e seta como padrao a acao de salvar em sessao.
	 * 
	 * @param tipo
	 *            o tipo unico de arquivo aceito, ou null para aceitar todos.
	 */
	public JanelaUpload(String tipo) {
		this(tipo == null ? null : new String[] { tipo }, new UrlParam[] { ACAO_SALVAR, LOCAL_SESSAO }, null);
	}

	/**
	 * Construtor padrao que permite setar as opcoes de tipo, parametros e funcao de retorno.
	 * 
	 * @param tipos
	 *            um array de tipos que define o arquivo de importacao, se passar null aceitara todas extensoes.
	 * @param params
	 *            um array de opcoes que definem as acoes a fazer com os arquivos, se passar null sera usado por padrao a acao ler.
	 * @param assincrono
	 *            a funcao de retorno a ser disparada apos execucao, se passar null mostrara a mensagem de quantidade de arquivos OK e ERROS.
	 */
	public JanelaUpload(String[] tipos, UrlParam[] params, AsyncCallback assincrono) {
		this.tipos = tipos;
		this.params = params;
		this.assincrono = assincrono;
		this.oks = new ArrayList<String>();
		this.erros = new ArrayList<String>();
	}

	public void inicializar() {
		uplArquivo = new UploadDialog();
		uplArquivo.setModal(true);
		uplArquivo.setClosable(false);
		uplArquivo.setUrl(GWT.getHostPageBaseURL() + "UploadService");
		uplArquivo.setAllowCloseOnUpload(false);
		if (tipos != null && tipos.length > 0) {
			uplArquivo.setPermittedExtensions(tipos);
		}
		uplArquivo.setBaseParams(params);

		uplArquivo.addListener(new UploadDialogListenerAdapter() {
			@Override
			public void onUploadStart(UploadDialog source) {
				oks.clear();
				erros.clear();
			}

			@Override
			public void onUploadSuccess(UploadDialog source, String filename, JavaScriptObject data) {
				oks.add(JavaScriptObjectHelper.getAttribute(data, "dados"));
			}

			@Override
			public void onUploadError(UploadDialog source, String filename, JavaScriptObject data) {
				erros.add(JavaScriptObjectHelper.getAttribute(data, "dados"));
			}

			@Override
			public void onUploadFailed(UploadDialog source, String filename) {
				erros.add(OpenSigCore.i18n.errInvalido() + " => " + filename);
			}

			@Override
			public void onUploadComplete(UploadDialog source) {
				if (assincrono != null) {
					if (erros.size() > 0) {
						assincrono.onFailure(null);
					} else {
						assincrono.onSuccess(null);
					}
				} else {
					resultado();
				}
			}
		});
		uplArquivo.show();
	}

	/**
	 * Mostra os textos devoldidos pelo servidro sejam os OKs ou ERROs.
	 */
	public void resultado() {
		NameValuePair[] botoes = new NameValuePair[] { new NameValuePair("yes", OpenSigCore.i18n.txtRealizado() + " - " + oks.size() + " / " + erros.size()),
				new NameValuePair("cancel", OpenSigCore.i18n.txtCancelar()) };

		MessageBoxConfig config = new MessageBoxConfig();
		config.setTitle(OpenSigCore.i18n.txtImportar());
		config.setMsg(OpenSigCore.i18n.msgImportarOK());
		config.setClosable(false);
		config.setButtons(botoes);
		config.setCallback(new PromptCallback() {
			public void execute(String btnID, String text) {
				if (!btnID.equals("cancel")) {
					String textoOK = "";
					for (String ok : oks) {
						textoOK += ok + "\n----------\n";
					}
					TextArea taOK = new TextArea(OpenSigCore.i18n.txtOk());
					taOK.setReadOnly(true);
					taOK.setSize(280, 330);
					taOK.setValue(textoOK);
					
					FormPanel panOK = new FormPanel();
					panOK.setLabelAlign(Position.TOP);
					panOK.setPaddings(5);
					panOK.setMargins(1);
					panOK.add(taOK);
					
					String textoERR = "";
					for (String erro : erros) {
						textoERR += erro + "\n----------\n";
					}
					TextArea taERR = new TextArea(OpenSigCore.i18n.txtErro());
					taERR.setReadOnly(true);
					taERR.setSize(280, 330);
					taERR.setValue(textoERR);
					
					FormPanel panERR = new FormPanel();
					panERR.setLabelAlign(Position.TOP);
					panERR.setPaddings(5);
					panERR.setMargins(1);
					panERR.add(taERR);

					Window wnd = new Window(OpenSigCore.i18n.txtRealizado(), 600, 400, false, false);
					wnd.setLayout(new ColumnLayout());
					wnd.add(panOK, new ColumnLayoutData(.5));
					wnd.add(panERR, new ColumnLayoutData(.5));
					wnd.doLayout();
					wnd.show();
				}
			}
		});
		MessageBox.show(config);
	}

	// Gets e Seteres

	public UploadDialog getUplArquivo() {
		return uplArquivo;
	}

	public void setUplArquivo(UploadDialog uplArquivo) {
		this.uplArquivo = uplArquivo;
	}

	public List<String> getOks() {
		return oks;
	}

	public void setOks(List<String> oks) {
		this.oks = oks;
	}

	public List<String> getErros() {
		return erros;
	}

	public void setErros(List<String> erros) {
		this.erros = erros;
	}

	public String[] getTipos() {
		return tipos;
	}

	public void setTipos(String[] tipos) {
		this.tipos = tipos;
	}

	public AsyncCallback getAssincrono() {
		return assincrono;
	}

	public void setAssincrono(AsyncCallback assincrono) {
		this.assincrono = assincrono;
	}

	public UrlParam[] getParams() {
		return params;
	}

	public void setParams(UrlParam[] params) {
		this.params = params;
	}

}
