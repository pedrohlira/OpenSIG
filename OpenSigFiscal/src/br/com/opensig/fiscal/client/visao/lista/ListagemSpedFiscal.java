package br.com.opensig.fiscal.client.visao.lista;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.UtilClient;
import br.com.opensig.core.client.controlador.comando.AComando;
import br.com.opensig.core.client.controlador.comando.EModo;
import br.com.opensig.core.client.controlador.comando.IComando;
import br.com.opensig.core.client.controlador.comando.form.ComandoSalvarFinal;
import br.com.opensig.core.client.controlador.comando.lista.ComandoEditar;
import br.com.opensig.core.client.controlador.comando.lista.ComandoExcluir;
import br.com.opensig.core.client.controlador.comando.lista.ComandoExcluirFinal;
import br.com.opensig.core.client.controlador.comando.lista.ComandoNovo;
import br.com.opensig.core.client.controlador.comando.lista.ComandoPermiteEmpresa;
import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.FiltroNumero;
import br.com.opensig.core.client.controlador.filtro.FiltroObjeto;
import br.com.opensig.core.client.servico.CoreProxy;
import br.com.opensig.core.client.servico.ExportacaoProxy;
import br.com.opensig.core.client.servico.ImportacaoProxy;
import br.com.opensig.core.client.visao.JanelaUpload;
import br.com.opensig.core.client.visao.Ponte;
import br.com.opensig.core.client.visao.abstrato.AListagem;
import br.com.opensig.core.client.visao.abstrato.IFormulario;
import br.com.opensig.core.shared.modelo.ExpRegistro;
import br.com.opensig.core.shared.modelo.IFavorito;
import br.com.opensig.core.shared.modelo.sistema.SisExpImp;
import br.com.opensig.empresa.shared.modelo.EmpEmpresa;
import br.com.opensig.fiscal.client.servico.FiscalProxy;
import br.com.opensig.fiscal.client.visao.form.FormularioSpedFiscalOperacao;
import br.com.opensig.fiscal.shared.modelo.FisSpedFiscal;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Position;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.BooleanFieldDef;
import com.gwtext.client.data.DateFieldDef;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.IntegerFieldDef;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Component;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.event.WindowListenerAdapter;
import com.gwtext.client.widgets.grid.BaseColumnConfig;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtextux.client.widgets.grid.plugins.GridCellActionsPlugin;
import com.gwtextux.client.widgets.grid.plugins.GridFilter;
import com.gwtextux.client.widgets.grid.plugins.GridListFilter;
import com.gwtextux.client.widgets.grid.plugins.GridLongFilter;
import com.gwtextux.client.widgets.upload.UploadDialog;
import com.gwtextux.client.widgets.upload.UploadDialogListenerAdapter;

public class ListagemSpedFiscal extends AListagem<FisSpedFiscal> {

	private IComando cmdSalvar;
	private IComando cmdExcluir;

	public ListagemSpedFiscal(IFormulario<FisSpedFiscal> form) {
		super(form);
		inicializar();
	}

	@Override
	public void inicializar() {
		FieldDef[] fd = new FieldDef[] { new IntegerFieldDef("fisSpedFiscalId"), new IntegerFieldDef("empEmpresa.empEmpresaId"), new StringFieldDef("empEmpresa.empEntidade.empEntidadeNome1"),
				new IntegerFieldDef("fisSpedFiscalAno"), new IntegerFieldDef("fisSpedFiscalMes"), new StringFieldDef("fisSpedFiscalTipo"), new DateFieldDef("fisSpedFiscalData"),
				new BooleanFieldDef("fisSpedFiscalAtivo"), new StringFieldDef("fisSpedFiscalProtocolo") };
		campos = new RecordDef(fd);

		// colunas
		ColumnConfig ccId = new ColumnConfig(OpenSigCore.i18n.txtCod(), "fisSpedFiscalId", 50, true);
		ColumnConfig ccEmpresaId = new ColumnConfig(OpenSigCore.i18n.txtCod() + " - " + OpenSigCore.i18n.txtEmpresa(), "empEmpresa.empEmpresaId", 100, true);
		ccEmpresaId.setHidden(true);
		ColumnConfig ccEmpresa = new ColumnConfig(OpenSigCore.i18n.txtEmpresa(), "empEmpresa.empEntidade.empEntidadeNome1", 100, true);
		ccEmpresa.setHidden(true);
		ColumnConfig ccAno = new ColumnConfig(OpenSigCore.i18n.txtAno(), "fisSpedFiscalAno", 75, true);
		ColumnConfig ccMes = new ColumnConfig(OpenSigCore.i18n.txtMes(), "fisSpedFiscalMes", 75, true);
		ColumnConfig ccTipo = new ColumnConfig(OpenSigCore.i18n.txtTipo(), "fisSpedFiscalTipo", 75, true);
		ColumnConfig ccData = new ColumnConfig(OpenSigCore.i18n.txtData(), "fisSpedFiscalData", 75, true, DATA);
		ColumnConfig ccAtivo = new ColumnConfig(OpenSigCore.i18n.txtAtivo(), "fisSpedFiscalAtivo", 75, true, BOLEANO);
		ColumnConfig ccProtocolo = new ColumnConfig(OpenSigCore.i18n.txtProtocolo(), "fisSpedFiscalProtocolo", 100, true);

		BaseColumnConfig[] bcc = new BaseColumnConfig[] { ccId, ccEmpresaId, ccEmpresa, ccAno, ccMes, ccTipo, ccData, ccAtivo, ccProtocolo };
		modelos = new ColumnModel(bcc);

		// salvar
		cmdSalvar = new AComando(new ComandoSalvarFinal()) {
			public void execute(Map contexto) {
				super.execute(contexto);
				classe = (FisSpedFiscal) contexto.get("classe");
				classe.setEmpEmpresa(new EmpEmpresa(Ponte.getLogin().getEmpresaId()));
				classe.setFisSpedFiscalData(new Date());
				classe.setFisSpedFiscalProtocolo("");

				FiscalProxy<FisSpedFiscal> proxy = new FiscalProxy<FisSpedFiscal>(classe);
				proxy.salvar(ASYNC);
				getPaginador().getBtnAuto().toggle(true);
			}
		};

		// deletar
		cmdExcluir = new AComando<FisSpedFiscal>(new ComandoExcluirFinal()) {
			public void execute(Map contexto) {
				super.execute(contexto);
				MessageBox.confirm(OpenSigCore.i18n.txtExcluir(), OpenSigCore.i18n.msgExcluir(), new MessageBox.ConfirmCallback() {
					public void execute(String btnID) {
						if (btnID.equalsIgnoreCase("yes")) {
							MessageBox.wait(OpenSigCore.i18n.txtAguarde(), OpenSigCore.i18n.txtExcluir());
							FiscalProxy<FisSpedFiscal> proxy = new FiscalProxy<FisSpedFiscal>(classe);
							proxy.deletar(ASYNC);
						}
					}
				});
			}
		};

		if (UtilClient.getAcaoPermitida(funcao, ComandoPermiteEmpresa.class) == null) {
			filtroPadrao = new FiltroObjeto("empEmpresa", ECompara.IGUAL, new EmpEmpresa(Ponte.getLogin().getEmpresaId()));
		}

		addPlugin(new GridCellActionsPlugin("left", null));
		super.inicializar();
	}

	@Override
	public IComando AntesDaAcao(IComando comando) {
		final Record rec = getSelectionModel().getSelected();

		if (comando instanceof ComandoNovo) {
			comando = null;
			classe.setFisSpedFiscalId(0);
			classe.setFisSpedFiscalTipo("");
			classe.setFisSpedFiscalAno(0);
			classe.setFisSpedFiscalMes(0);
			abrirSped();
		} else if (comando instanceof ComandoEditar) {
			comando = null;
			if (rec != null) {
				classe.setFisSpedFiscalId(rec.getAsInteger("fisSpedFiscalId"));
				classe.setFisSpedFiscalTipo(rec.getAsString("fisSpedFiscalTipo"));
				classe.setFisSpedFiscalAno(rec.getAsInteger("fisSpedFiscalAno"));
				classe.setFisSpedFiscalMes(rec.getAsInteger("fisSpedFiscalMes"));
				abrirSped();
			} else {
				MessageBox.alert(OpenSigCore.i18n.txtSelecionar(), OpenSigCore.i18n.errSelecionar());
			}
		} else if (comando instanceof ComandoExcluir) {
			comando = null;
			if (rec != null) {
				if (rec.getAsString("fisSpedFiscalProtocolo") == null) {
					classe.setFisSpedFiscalId(rec.getAsInteger("fisSpedFiscalId"));
					classe.setFisSpedFiscalTipo(rec.getAsString("fisSpedFiscalTipo"));
					classe.setFisSpedFiscalAno(rec.getAsInteger("fisSpedFiscalAno"));
					classe.setFisSpedFiscalMes(rec.getAsInteger("fisSpedFiscalMes"));
					cmdExcluir.execute(contexto);
				} else {
					MessageBox.alert(OpenSigCore.i18n.txtAcesso(), OpenSigCore.i18n.txtAcessoNegado());
				}
			} else {
				MessageBox.alert(OpenSigCore.i18n.txtSelecionar(), OpenSigCore.i18n.errSelecionar());
			}
		}

		return comando;
	}

	@Override
	public void setExportacao(final SisExpImp expimp, EModo modo, EModo modo2, final AsyncCallback<String> async) {
		if (expimp.getSisExpImpClasse().equals("br.com.opensig.fiscal.server.acao.ExportarSped")) {
			Record rec = getSelectionModel().getSelected();
			if (rec != null) {
				String nome = rec.getAsString("fisSpedFiscalTipo");
				nome += rec.getAsString("fisSpedFiscalAno");
				nome += rec.getAsInteger("fisSpedFiscalMes") > 9 ? rec.getAsString("fisSpedFiscalMes") : "0" + rec.getAsString("fisSpedFiscalMes");

				ExpRegistro<FisSpedFiscal> exp = new ExpRegistro<FisSpedFiscal>();
				exp.setClasse(classe);
				exp.setNome(nome);

				getPanel().getEl().mask(OpenSigCore.i18n.txtAguarde());
				ExportacaoProxy proxy = new ExportacaoProxy();
				proxy.exportar(expimp, exp, async);
			} else {
				MessageBox.alert(OpenSigCore.i18n.txtSelecionar(), OpenSigCore.i18n.errSelecionar());
			}
		} else {
			super.setExportacao(expimp, modo, modo2, async);
		}
	}

	@Override
	public void setImportacao(final SisExpImp modo) {
		final JanelaUpload<FisSpedFiscal> janela = new JanelaUpload<FisSpedFiscal>();
		janela.setTipos(modo.getSisExpImpExtensoes().split(" "));
		janela.setAssincrono(new AsyncCallback() {
			public void onSuccess(Object result) {
				analisar(modo, janela.getOks());
				janela.getUplArquivo().close();
			}

			public void onFailure(Throwable caught) {
				janela.resultado();
			}
		});
		janela.inicializar();
		janela.getUplArquivo().addListener(new UploadDialogListenerAdapter() {
			public boolean onBeforeAdd(UploadDialog source, String filename) {
				return source.getQueuedCount() == 0;
			}

			public void onFileAdd(UploadDialog source, String filename) {
				source.startUpload();
			}
		});
	}

	private void analisar(final SisExpImp modo, final List<String> arquivos) {
		final Window wnd = new Window("", 400, 130, true, false);
		final FormularioSpedFiscalOperacao frm = new FormularioSpedFiscalOperacao(classe, funcao);
		frm.setHeader(false);

		Button btn = new Button(OpenSigCore.i18n.txtOk());
		btn.setIconCls("icon-salvar");
		btn.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				if (frm.getForm().isValid()) {
					String nome = frm.getCmbSped().getValueAsString();
					nome += frm.getCmbAno().getValueAsString();
					nome += frm.getCmbMes().getValueAsString();
					arquivos.add(nome);

					getEl().mask(OpenSigCore.i18n.txtAguarde());
					ImportacaoProxy<FisSpedFiscal> proxy = new ImportacaoProxy<FisSpedFiscal>();
					proxy.importar(modo, arquivos, new AsyncCallback<Map<String, List<FisSpedFiscal>>>() {

						public void onSuccess(Map<String, List<FisSpedFiscal>> result) {
							getEl().unmask();
							getStore().reload();
							MessageBox.alert(OpenSigCore.i18n.txtImportar(), OpenSigCore.i18n.msgImportarOK());
						}

						public void onFailure(Throwable caught) {
							getEl().unmask();
							getStore().reload();
							MessageBox.alert(OpenSigCore.i18n.txtImportar(), OpenSigCore.i18n.errImportar());
						}
					});
					wnd.close();
				}
			}
		});

		wnd.setTitle(OpenSigCore.i18n.txtImportar(), "icon-sped");
		wnd.setClosable(false);
		wnd.add(frm);
		wnd.addButton(btn);
		wnd.setButtonAlign(Position.CENTER);
		wnd.show();
	}

	public void setGridFiltro() {
		super.setGridFiltro();
		for (Entry<String, GridFilter> entry : filtros.entrySet()) {
			if (entry.getKey().equals("empEmpresa.empEmpresaId")) {
				((GridLongFilter) entry.getValue()).setValueEquals(Ponte.getLogin().getEmpresaId());
			} else if (entry.getKey().equals("fisSpedFiscalAno")) {
				((GridLongFilter) entry.getValue()).setValueEquals(new Date().getYear() + 1900);
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
		filtros.get("fisSpedFiscalAno").setActive(false, true);
		super.setFavorito(favorito);
	}

	private void abrirSped() {
		final Window wnd = new Window("", 400, 130, true, false);
		final FormularioSpedFiscalOperacao frm = new FormularioSpedFiscalOperacao(classe, funcao);
		frm.setHeader(false);

		Button btn = new Button(OpenSigCore.i18n.txtOk());
		btn.setIconCls("icon-salvar");
		btn.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				if (frm.getForm().isValid() && frm.setDados()) {
					cmdSalvar.execute(frm.getContexto());
					wnd.close();
				}
			}
		});

		wnd.setTitle(OpenSigCore.i18n.txtSped(), "icon-sped");
		wnd.setLayout(new FitLayout());
		wnd.add(frm);
		wnd.addButton(btn);
		wnd.setButtonAlign(Position.CENTER);
		wnd.addListener(new WindowListenerAdapter() {
			@Override
			public void onShow(Component component) {
				frm.mostrarDados();
			}
		});
		wnd.show();
	}
}
