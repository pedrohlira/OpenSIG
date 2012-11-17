package br.com.opensig.fiscal.client.visao.form;

import java.util.Map;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.UtilClient;
import br.com.opensig.core.client.controlador.comando.AComando;
import br.com.opensig.core.client.controlador.comando.IComando;
import br.com.opensig.core.client.controlador.comando.form.ComandoSalvar;
import br.com.opensig.core.client.controlador.comando.form.ComandoSalvarFinal;
import br.com.opensig.core.client.visao.JanelaUpload;
import br.com.opensig.core.client.visao.Ponte;
import br.com.opensig.core.client.visao.abstrato.AFormulario;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.empresa.shared.modelo.EmpEmpresa;
import br.com.opensig.empresa.shared.modelo.EmpEntidade;
import br.com.opensig.fiscal.client.servico.FiscalProxy;
import br.com.opensig.fiscal.shared.modelo.FisCertificado;

import com.google.gwt.core.client.JavaScriptObject;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.UrlParam;
import com.gwtext.client.data.Record;
import com.gwtext.client.util.JavaScriptObjectHelper;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.DateField;
import com.gwtext.client.widgets.form.Hidden;
import com.gwtext.client.widgets.form.MultiFieldPanel;
import com.gwtext.client.widgets.form.TextField;
import com.gwtextux.client.widgets.upload.UploadDialog;
import com.gwtextux.client.widgets.upload.UploadDialogListenerAdapter;
import com.gwtextux.client.widgets.window.ToastWindow;

public class FormularioCertificado extends AFormulario<FisCertificado> {

	private Hidden hdnCod;
	private Hidden hdnEmpresa;
	private Hidden hdnCnpj;
	private TextField txtSenha;
	private TextField txtArquivo;
	private DateField dfInicio;
	private DateField dfFim;
	private ToolbarButton btnArquivo;
	private JanelaUpload janela;

	public FormularioCertificado(SisFuncao funcao) {
		super(new FisCertificado(), funcao);
		inicializar();
	}

	public void inicializar() {
		super.inicializar();

		hdnCod = new Hidden("fisCertificadoId", "0");
		add(hdnCod);
		hdnEmpresa = new Hidden("empEmpresa.empEmpresaId", "0");
		add(hdnEmpresa);
		hdnCnpj = new Hidden("empEmpresa.empEntidade.empEntidadeDocumento1", "0");
		add(hdnCnpj);

		txtSenha = new TextField(OpenSigCore.i18n.txtSenha(), "fisCertificadoSenha", 210);
		txtSenha.setAllowBlank(false);
		txtSenha.setMaxLength(20);
		txtSenha.setPassword(true);

		dfInicio = new DateField(OpenSigCore.i18n.txtInicio(), "fisCertificadoInicio", 80);
		dfInicio.setReadOnly(true);

		dfFim = new DateField(OpenSigCore.i18n.txtFim(), "fisCertificadoFim", 80);
		dfFim.setReadOnly(true);

		txtArquivo = new TextField(OpenSigCore.i18n.txtArquivo(), "fisCertificadoArquivo", 210);
		txtArquivo.setAllowBlank(false);
		txtArquivo.setReadOnly(true);

		btnArquivo = new ToolbarButton(OpenSigCore.i18n.txtArquivo());
		btnArquivo.setIconCls("icon-chave");
		btnArquivo.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				abrirArquivo();
			}
		});

		MultiFieldPanel linha = new MultiFieldPanel();
		linha.setBorder(false);
		linha.addToRow(txtSenha, 230);
		linha.addToRow(dfInicio, 120);
		linha.addToRow(dfFim, 120);
		linha.addToRow(txtArquivo, 230);
		add(linha);

		getTopToolbar().addSeparator();
		getTopToolbar().addButton(btnArquivo);
	}

	private void abrirArquivo() {
		String cnpj = hdnCnpj.getValueAsString().replaceAll("\\D", "");
		UrlParam[] params = new UrlParam[] { JanelaUpload.ACAO_SALVAR, JanelaUpload.LOCAL_FISICO, JanelaUpload.PATH_REAL(UtilClient.CONF.get("sistema.empresas") + cnpj + "/"),
				JanelaUpload.NOME_ARQUIVO("certificado.pfx") };

		janela = new JanelaUpload("pfx");
		janela.setParams(params);
		janela.inicializar();
		
		janela.getUplArquivo().purgeListeners();
		janela.getUplArquivo().addListener(new UploadDialogListenerAdapter() {
			public void onUploadSuccess(UploadDialog source, String filename, JavaScriptObject data) {
				txtArquivo.setValue(filename);
				source.close();
			}

			public void onUploadError(UploadDialog source, String filename, JavaScriptObject data) {
				txtArquivo.setValue(null);
				new ToastWindow(OpenSigCore.i18n.txtErro(), JavaScriptObjectHelper.getAttribute(data, "dados")).show();
			}

			public void onUploadFailed(UploadDialog source, String filename) {
				txtArquivo.setValue(null);
			}

			public boolean onBeforeAdd(UploadDialog source, String filename) {
				return source.getQueuedCount() == 0;
			}
			
			public void onFileAdd(UploadDialog source, String filename) {
				source.startUpload();
			}
		});
	}

	@Override
	public IComando AntesDaAcao(IComando comando) {
		// salvando
		if (comando instanceof ComandoSalvar) {
			comando = new AComando(new ComandoSalvarFinal()) {
				public void execute(Map contexto) {
					super.execute(contexto);
					FiscalProxy proxy = new FiscalProxy();
					proxy.salvarCertificado(classe, ASYNC);
				}
			};
		}

		return comando;
	}

	@Override
	public boolean setDados() {
		EmpEntidade ent = new EmpEntidade();
		ent.setEmpEntidadeDocumento1(Ponte.getLogin().getEmpresa()[5]);
		EmpEmpresa emp = new EmpEmpresa(Ponte.getLogin().getEmpresaId());
		emp.setEmpEntidade(ent);

		classe.setFisCertificadoId(Integer.valueOf(hdnCod.getValueAsString()));
		classe.setEmpEmpresa(emp);
		if (!txtSenha.getValueAsString().equals("")) {
			classe.setFisCertificadoSenha(txtSenha.getValueAsString());
		}

		return true;
	}

	@Override
	public void mostrarDados() {
		Record rec = lista.getPanel().getSelectionModel().getSelected();

		if (rec != null) {
			getForm().loadRecord(rec);
		} else {
			hdnEmpresa.setValue(Ponte.getLogin().getEmpresaId() + "");
			hdnCnpj.setValue(Ponte.getLogin().getEmpresa()[5]);
		}
	}

	@Override
	public void limparDados() {
		getForm().reset();
	}

	@Override
	public void gerarListas() {
	}
}
