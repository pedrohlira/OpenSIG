package br.com.opensig.fiscal.client.visao.lista;

import java.util.Map.Entry;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.UtilClient;
import br.com.opensig.core.client.controlador.comando.lista.ComandoPermiteEmpresa;
import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.FiltroNumero;
import br.com.opensig.core.client.controlador.filtro.FiltroObjeto;
import br.com.opensig.core.client.servico.CoreProxy;
import br.com.opensig.core.client.visao.JanelaUpload;
import br.com.opensig.core.client.visao.Ponte;
import br.com.opensig.core.client.visao.abstrato.AListagem;
import br.com.opensig.core.client.visao.abstrato.IFormulario;
import br.com.opensig.core.shared.modelo.IFavorito;
import br.com.opensig.empresa.shared.modelo.EmpEmpresa;
import br.com.opensig.fiscal.shared.modelo.FisCertificado;

import com.google.gwt.core.client.JavaScriptObject;
import com.gwtext.client.core.UrlParam;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.DateFieldDef;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.IntegerFieldDef;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.util.JavaScriptObjectHelper;
import com.gwtext.client.widgets.grid.BaseColumnConfig;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtextux.client.widgets.grid.plugins.ColumnWithCellActionsConfig;
import com.gwtextux.client.widgets.grid.plugins.GridCellAction;
import com.gwtextux.client.widgets.grid.plugins.GridCellActionListener;
import com.gwtextux.client.widgets.grid.plugins.GridCellActionsPlugin;
import com.gwtextux.client.widgets.grid.plugins.GridFilter;
import com.gwtextux.client.widgets.grid.plugins.GridListFilter;
import com.gwtextux.client.widgets.grid.plugins.GridLongFilter;
import com.gwtextux.client.widgets.upload.UploadDialog;
import com.gwtextux.client.widgets.upload.UploadDialogListenerAdapter;
import com.gwtextux.client.widgets.window.ToastWindow;

public class ListagemCertificado extends AListagem<FisCertificado> {

	private String cnpj;
	private JanelaUpload janela;
	
	public ListagemCertificado(IFormulario formulario) {
		super(formulario);
		inicializar();
	}

	public void inicializar() {
		// campos
		FieldDef[] fd = new FieldDef[] { new IntegerFieldDef("fisCertificadoId"), new IntegerFieldDef("empEmpresa.empEmpresaId"), new StringFieldDef("empEmpresa.empEntidade.empEntidadeNome1"),
				new StringFieldDef("empEmpresa.empEntidade.empEntidadeDocumento1"), new DateFieldDef("fisCertificadoInicio"), new DateFieldDef("fisCertificadoFim"), new StringFieldDef("imagem") };
		campos = new RecordDef(fd);

		// colunas
		ColumnConfig ccId = new ColumnConfig(OpenSigCore.i18n.txtCod(), "fisCertificadoId", 50, true);
		ColumnConfig ccEmpresaId = new ColumnConfig(OpenSigCore.i18n.txtCod() + " - " + OpenSigCore.i18n.txtEmpresa(), "empEmpresa.empEmpresaId", 100, true);
		ccEmpresaId.setHidden(true);
		ColumnConfig ccEmpresa = new ColumnConfig(OpenSigCore.i18n.txtEmpresa(), "empEmpresa.empEntidade.empEntidadeNome1", 300, true);
		ColumnConfig ccCnpj = new ColumnConfig(OpenSigCore.i18n.txtCnpj(), "empEmpresa.empEntidade.empEntidadeDocumento1", 150, true);
		ColumnConfig ccInicio = new ColumnConfig(OpenSigCore.i18n.txtInicio(), "fisCertificadoInicio", 75, true, DATA);
		ColumnConfig ccFim = new ColumnConfig(OpenSigCore.i18n.txtFim(), "fisCertificadoFim", 75, true, DATA);

		GridCellAction cellImagem = new GridCellAction("icon-imagem", OpenSigCore.i18n.txtImagem(), new GridCellActionListener() {
			public boolean execute(GridPanel grid, final Record record, String action, Object value, String dataIndex, int rowIndex, int colIndex) {
				cnpj = record.getAsString("empEmpresa.empEntidade.empEntidadeDocumento1");
				if (cnpj != null) {
					cnpj = cnpj.replaceAll("\\D", "");
					abrirArquivo();
					return true;
				} else {
					return false;
				}
			}
		});

		ColumnWithCellActionsConfig ccImagem = new ColumnWithCellActionsConfig(OpenSigCore.i18n.txtImagem(), "imagem", 50);
		ccImagem.setMenuDisabled(true);
		ccImagem.setCellActions(new GridCellAction[] { cellImagem });

		BaseColumnConfig[] bcc = new BaseColumnConfig[] { ccId, ccEmpresaId, ccEmpresa, ccCnpj, ccInicio, ccFim, ccImagem };
		modelos = new ColumnModel(bcc);

		if (UtilClient.getAcaoPermitida(funcao, ComandoPermiteEmpresa.class) == null) {
			filtroPadrao = new FiltroObjeto("empEmpresa", ECompara.IGUAL, new EmpEmpresa(Ponte.getLogin().getEmpresaId()));
		}

		addPlugin(new GridCellActionsPlugin("left", null));
		super.inicializar();
	}

	public void setGridFiltro() {
		super.setGridFiltro();
		for (Entry<String, GridFilter> entry : filtros.entrySet()) {
			if (entry.getKey().equals("empEmpresa.empEmpresaId")) {
				((GridLongFilter) entry.getValue()).setValueEquals(Ponte.getLogin().getEmpresaId());
			} else if (entry.getKey().equals("empEmpresa.empEntidade.empEntidadeNome1")) {
				// empresa
				FiltroNumero fn = null;
				if (UtilClient.getAcaoPermitida(funcao, ComandoPermiteEmpresa.class) == null) {
					fn = new FiltroNumero("empEmpresaId", ECompara.IGUAL, Ponte.getLogin().getEmpresaId());
				}

				FieldDef[] fdEmpresa = new FieldDef[] { new IntegerFieldDef("empEmpresaId"), new IntegerFieldDef("empEntidade.empEntidadeId"), new StringFieldDef("empEntidade.empEntidadeNome1") };
				CoreProxy<EmpEmpresa> proxy = new CoreProxy<EmpEmpresa>(new EmpEmpresa(), fn);
				Store storeEmpresa = new Store(proxy, new ArrayReader(new RecordDef(fdEmpresa)), true);

				GridListFilter fEmpresa = new GridListFilter("empEmpresa.empEntidade.empEntidadeNome1", storeEmpresa);
				fEmpresa.setLabelField("empEntidade.empEntidadeNome1");
				fEmpresa.setLabelValue("empEntidade.empEntidadeNome1");
				fEmpresa.setLoadingText(OpenSigCore.i18n.txtAguarde());
				entry.setValue(fEmpresa);
			}
		}
	}

	public void setFavorito(IFavorito favorito) {
		filtros.get("empEmpresa.empEmpresaId").setActive(false, true);
		super.setFavorito(favorito);
	}
	
	private void abrirArquivo() {
		UrlParam[] params = new UrlParam[] { JanelaUpload.ACAO_SALVAR, JanelaUpload.LOCAL_FISICO, JanelaUpload.PATH_REAL(UtilClient.CONF.get("sistema.empresas") + cnpj + "/"), JanelaUpload.NOME_ARQUIVO("logo.png") };

		janela = new JanelaUpload("png");
		janela.setParams(params);
		janela.inicializar();
		
		janela.getUplArquivo().purgeListeners();
		janela.getUplArquivo().addListener(new UploadDialogListenerAdapter() {
			public void onUploadSuccess(UploadDialog source, String filename, JavaScriptObject data) {
				new ToastWindow(OpenSigCore.i18n.txtImagem(), OpenSigCore.i18n.msgSalvarOK()).show();
				source.close();
			}

			public void onUploadError(UploadDialog source, String filename, JavaScriptObject data) {
				new ToastWindow(OpenSigCore.i18n.txtErro(), JavaScriptObjectHelper.getAttribute(data, "dados")).show();
			}

			public boolean onBeforeAdd(UploadDialog source, String filename) {
				return source.getQueuedCount() == 0;
			}
			
			public void onFileAdd(UploadDialog source, String filename) {
				source.startUpload();
			}
		});
	}
}
