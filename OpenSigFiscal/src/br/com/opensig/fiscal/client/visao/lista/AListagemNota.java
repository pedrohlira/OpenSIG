package br.com.opensig.fiscal.client.visao.lista;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.UtilClient;
import br.com.opensig.core.client.controlador.comando.lista.ComandoPermiteEmpresa;
import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.FiltroNumero;
import br.com.opensig.core.client.controlador.filtro.FiltroObjeto;
import br.com.opensig.core.client.js.OpenSigCoreJS;
import br.com.opensig.core.client.servico.CoreProxy;
import br.com.opensig.core.client.servico.ImportacaoProxy;
import br.com.opensig.core.client.visao.JanelaUpload;
import br.com.opensig.core.client.visao.Ponte;
import br.com.opensig.core.client.visao.abstrato.AListagem;
import br.com.opensig.core.client.visao.abstrato.IFormulario;
import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.core.shared.modelo.IFavorito;
import br.com.opensig.core.shared.modelo.sistema.SisExpImp;
import br.com.opensig.empresa.shared.modelo.EmpEmpresa;
import br.com.opensig.fiscal.client.servico.FiscalProxy;
import br.com.opensig.fiscal.shared.modelo.ENotaStatus;
import br.com.opensig.fiscal.shared.modelo.FisNotaStatus;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.DateFieldDef;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.FloatFieldDef;
import com.gwtext.client.data.IntegerFieldDef;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.grid.BaseColumnConfig;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtextux.client.widgets.grid.plugins.ColumnWithCellActionsConfig;
import com.gwtextux.client.widgets.grid.plugins.GridCellAction;
import com.gwtextux.client.widgets.grid.plugins.GridCellActionListener;
import com.gwtextux.client.widgets.grid.plugins.GridCellActionsPlugin;
import com.gwtextux.client.widgets.grid.plugins.GridDateFilter;
import com.gwtextux.client.widgets.grid.plugins.GridFilter;
import com.gwtextux.client.widgets.grid.plugins.GridListFilter;
import com.gwtextux.client.widgets.grid.plugins.GridLongFilter;
import com.gwtextux.client.widgets.grid.plugins.GridSummaryPlugin;
import com.gwtextux.client.widgets.grid.plugins.SummaryColumnConfig;
import com.gwtextux.client.widgets.window.ToastWindow;

public abstract class AListagemNota<E extends Dados> extends AListagem<E> {

	protected Map<String, String> nomes;
	protected JanelaUpload<E> janela;

	public AListagemNota(IFormulario<E> formulario) {
		super(formulario);
		nomes = new HashMap();
		addPlugin(new GridSummaryPlugin());
	}

	public void inicializar() {
		// campos
		FieldDef[] fd = new FieldDef[] { new IntegerFieldDef(nomes.get("id")), new IntegerFieldDef("empEmpresa.empEmpresaId"), new StringFieldDef("empEmpresa.empEntidade.empEntidadeNome1"),
				new IntegerFieldDef("fisNotaStatus.fisNotaStatusId"), new StringFieldDef("fisNotaStatus.fisNotaStatusDescricao"), new DateFieldDef(nomes.get("cadastro")),
				new IntegerFieldDef(nomes.get("numero")), new DateFieldDef(nomes.get("data")), new FloatFieldDef(nomes.get("valor")), new StringFieldDef(nomes.get("chave")),
				new FloatFieldDef(nomes.get("icms")), new FloatFieldDef(nomes.get("ipi")), new FloatFieldDef(nomes.get("pis")), new FloatFieldDef(nomes.get("cofins")),
				new StringFieldDef(nomes.get("protocolo")), new StringFieldDef(nomes.get("xml")), new StringFieldDef("danfe"), new StringFieldDef(nomes.get("protocoloCancelado")),
				new StringFieldDef(nomes.get("xmlCancelado")), new StringFieldDef(nomes.get("recibo")), new StringFieldDef(nomes.get("erro")) };
		campos = new RecordDef(fd);

		// colunas
		ColumnConfig ccId = new ColumnConfig(OpenSigCore.i18n.txtCod(), nomes.get("id"), 50, true);
		ColumnConfig ccEmpresaId = new ColumnConfig(OpenSigCore.i18n.txtCod() + " - " + OpenSigCore.i18n.txtEmpresa(), "empEmpresa.empEmpresaId", 100, true);
		ccEmpresaId.setHidden(true);
		ColumnConfig ccEmpresa = new ColumnConfig(OpenSigCore.i18n.txtEmpresa(), "empEmpresa.empEntidade.empEntidadeNome1", 100, true);
		ccEmpresa.setHidden(true);
		ColumnConfig ccStatusId = new ColumnConfig(OpenSigCore.i18n.txtCod() + " - " + OpenSigCore.i18n.txtStatus(), "fisNotaStatus.fisNotaStatusId", 100, true);
		ccStatusId.setHidden(true);
		ColumnConfig ccStatus = new ColumnConfig(OpenSigCore.i18n.txtStatus(), "fisNotaStatus.fisNotaStatusDescricao", 100, true);
		ColumnConfig ccCadastro = new ColumnConfig(OpenSigCore.i18n.txtCadastro(), nomes.get("cadastro"), 75, true, DATA);
		ccCadastro.setHidden(true);
		ColumnConfig ccNumero = new ColumnConfig(OpenSigCore.i18n.txtNumero(), nomes.get("numero"), 50, true);
		ColumnConfig ccData = new ColumnConfig(OpenSigCore.i18n.txtData(), nomes.get("data"), 75, true, DATA);
		ColumnConfig ccChave = new ColumnConfig(OpenSigCore.i18n.txtChave(), nomes.get("chave"), 250, true);
		ColumnConfig ccProtocolo = new ColumnConfig(OpenSigCore.i18n.txtProtocolo(), nomes.get("protocolo"), 100, true);
		ColumnConfig ccProtocoloCancelado = new ColumnConfig(OpenSigCore.i18n.txtProtocolo() + " - " + OpenSigCore.i18n.txtCancelada(), nomes.get("protocoloCancelado"), 100, true);
		ccProtocoloCancelado.setHidden(true);
		ColumnConfig ccRecibo = new ColumnConfig(OpenSigCore.i18n.txtRecibo(), nomes.get("recibo"), 100, true);
		ccRecibo.setHidden(true);
		// sumarios
		SummaryColumnConfig sumValor = new SummaryColumnConfig(SummaryColumnConfig.SUM, new ColumnConfig(OpenSigCore.i18n.txtValor(), nomes.get("valor"), 75, true, DINHEIRO), DINHEIRO);
		SummaryColumnConfig sumIcms = new SummaryColumnConfig(SummaryColumnConfig.SUM, new ColumnConfig(OpenSigCore.i18n.txtIcms(), nomes.get("icms"), 75, true, DINHEIRO), DINHEIRO);
		SummaryColumnConfig sumIpi = new SummaryColumnConfig(SummaryColumnConfig.SUM, new ColumnConfig(OpenSigCore.i18n.txtIpi(), nomes.get("ipi"), 75, true, DINHEIRO), DINHEIRO);
		SummaryColumnConfig sumPis = new SummaryColumnConfig(SummaryColumnConfig.SUM, new ColumnConfig(OpenSigCore.i18n.txtPis(), nomes.get("pis"), 75, true, DINHEIRO), DINHEIRO);
		SummaryColumnConfig sumCofins = new SummaryColumnConfig(SummaryColumnConfig.SUM, new ColumnConfig(OpenSigCore.i18n.txtCofins(), nomes.get("cofins"), 75, true, DINHEIRO), DINHEIRO);
		// celulas com acao
		GridCellAction celXml = new GridCellAction("icon-xml", OpenSigCore.i18n.txtNfe(), new GridCellActionListener() {
			public boolean execute(GridPanel grid, final Record record, String action, Object value, String dataIndex, int rowIndex, int colIndex) {
				int status = record.getAsInteger("fisNotaStatus.fisNotaStatusId");
				int id = record.getAsInteger(nomes.get("id"));

				if (status == ENotaStatus.AUTORIZANDO.getId() || status == ENotaStatus.AUTORIZADO.getId() || status == ENotaStatus.INUTILIZANDO.getId() || status == ENotaStatus.INUTILIZADO.getId()
						|| status == ENotaStatus.CANCELADO.getId() || status == ENotaStatus.CANCELANDO.getId()) {
					baixarArquivo("xml", false, id);
				} else {
					new ToastWindow(OpenSigCore.i18n.txtNfe(), OpenSigCore.i18n.errRegistro()).show();
				}
				return true;
			}
		});
		ColumnWithCellActionsConfig ccXml = new ColumnWithCellActionsConfig(OpenSigCore.i18n.txtNfe(), nomes.get("xml"), 50);
		ccXml.setMenuDisabled(true);
		ccXml.setCellActions(new GridCellAction[] { celXml });

		GridCellAction celDanfe = new GridCellAction("icon-pdf", OpenSigCore.i18n.txtDanfe(), new GridCellActionListener() {
			public boolean execute(GridPanel grid, final Record record, String action, Object value, String dataIndex, int rowIndex, int colIndex) {
				int status = record.getAsInteger("fisNotaStatus.fisNotaStatusId");
				int id = record.getAsInteger(nomes.get("id"));

				if (status != ENotaStatus.INUTILIZANDO.getId() && status != ENotaStatus.INUTILIZADO.getId() && status != ENotaStatus.ERRO.getId()) {
					baixarArquivo("pdf", false, id);
				} else {
					new ToastWindow(OpenSigCore.i18n.txtDanfe(), OpenSigCore.i18n.errRegistro()).show();
				}
				return true;
			}
		});
		ColumnWithCellActionsConfig ccDanfe = new ColumnWithCellActionsConfig(OpenSigCore.i18n.txtDanfe(), "danfe", 50);
		ccDanfe.setMenuDisabled(true);
		ccDanfe.setCellActions(new GridCellAction[] { celDanfe });

		GridCellAction celXmlCancelado = new GridCellAction("icon-cancelar", OpenSigCore.i18n.txtCancelada(), new GridCellActionListener() {
			public boolean execute(GridPanel grid, final Record record, String action, Object value, String dataIndex, int rowIndex, int colIndex) {
				int status = record.getAsInteger("fisNotaStatus.fisNotaStatusId");
				int id = record.getAsInteger(nomes.get("id"));

				if (status == ENotaStatus.CANCELANDO.getId() || status == ENotaStatus.CANCELADO.getId()) {
					baixarArquivo("xml", true, id);
				} else {
					new ToastWindow(OpenSigCore.i18n.txtCancelada(), OpenSigCore.i18n.errRegistro()).show();
				}
				return true;
			}
		});
		ColumnWithCellActionsConfig ccXmlCancelado = new ColumnWithCellActionsConfig(OpenSigCore.i18n.txtCancelada(), nomes.get("xmlCancelado"), 60);
		ccXmlCancelado.setMenuDisabled(true);
		ccXmlCancelado.setCellActions(new GridCellAction[] { celXmlCancelado });

		GridCellAction cellErro = new GridCellAction("icon-analisar", OpenSigCore.i18n.txtErro(), new GridCellActionListener() {
			public boolean execute(GridPanel grid, final Record record, String action, Object value, String dataIndex, int rowIndex, int colIndex) {
				int status = record.getAsInteger("fisNotaStatus.fisNotaStatusId");

				if (status == ENotaStatus.ERRO.getId() || status == ENotaStatus.FS_DA.getId() || status == ENotaStatus.AUTORIZANDO.getId() || status == ENotaStatus.CANCELANDO.getId()
						|| status == ENotaStatus.INUTILIZANDO.getId()) {
					MessageBox.wait(OpenSigCore.i18n.txtAguarde(), OpenSigCore.i18n.txtErro());
					int id = record.getAsInteger(nomes.get("id"));
					CoreProxy<E> proxy = new CoreProxy<E>(classe);
					proxy.selecionar(id, new AsyncCallback<E>() {

						public void onFailure(Throwable caught) {
							MessageBox.hide();
							MessageBox.alert(OpenSigCore.i18n.txtExportar(), OpenSigCore.i18n.errExportar());
						}

						public void onSuccess(E result) {
							MessageBox.hide();
							classe = result;
							mostrarErro(result);
						}
					});
				} else {
					new ToastWindow(OpenSigCore.i18n.txtErro(), OpenSigCore.i18n.errRegistro()).show();
				}
				return true;
			}
		});
		ColumnWithCellActionsConfig ccErro = new ColumnWithCellActionsConfig(OpenSigCore.i18n.txtAnalisar(), nomes.get("erro"), 50);
		ccErro.setMenuDisabled(true);
		ccErro.setCellActions(new GridCellAction[] { cellErro });

		BaseColumnConfig[] bcc = new BaseColumnConfig[] { ccId, ccEmpresaId, ccEmpresa, ccStatusId, ccStatus, ccCadastro, ccNumero, ccData, sumValor, ccChave, sumIcms, sumIpi, sumPis, sumCofins,
				ccProtocolo, ccXml, ccDanfe, ccProtocoloCancelado, ccXmlCancelado, ccRecibo, ccErro };
		modelos = new ColumnModel(bcc);

		if (UtilClient.getAcaoPermitida(funcao, ComandoPermiteEmpresa.class) == null) {
			filtroPadrao = new FiltroObjeto("empEmpresa", ECompara.IGUAL, new EmpEmpresa(Ponte.getLogin().getEmpresaId()));
		}

		addPlugin(new GridCellActionsPlugin("left", null));
		super.inicializar();
	}

	private void baixarArquivo(final String extensao, final boolean cancelado, int id) {
		MessageBox.wait(OpenSigCore.i18n.txtAguarde(), OpenSigCore.i18n.txtArquivo());
		CoreProxy<E> proxy = new CoreProxy<E>(classe);
		proxy.selecionar(id, new AsyncCallback<E>() {

			public void onFailure(Throwable caught) {
				MessageBox.hide();
				MessageBox.alert(OpenSigCore.i18n.txtExportar(), OpenSigCore.i18n.errExportar());
			}

			public void onSuccess(E result) {
				classe = result;
				String arquivo;
				if (cancelado) {
					arquivo = OpenSigCoreJS.base64encode(getXmlCancelado(result));
				} else {
					arquivo = OpenSigCoreJS.base64encode(getXml(result));
				}

				FiscalProxy<E> fiscal = new FiscalProxy<E>();
				fiscal.exportar(arquivo, getChave(result), extensao, new AsyncCallback<String>() {
					public void onSuccess(String result) {
						MessageBox.hide();
						UtilClient.exportar("ExportacaoService?id=" + result);
					}

					public void onFailure(Throwable caught) {
						MessageBox.hide();
						MessageBox.alert(OpenSigCore.i18n.txtExportar(), OpenSigCore.i18n.errExportar());
					}
				});
			}
		});
	}

	public void setGridFiltro() {
		super.setGridFiltro();
		for (Entry<String, GridFilter> entry : filtros.entrySet()) {
			if (entry.getKey().equals(nomes.get("data"))) {
				((GridDateFilter) entry.getValue()).setValueOn(new Date());
			} else if (entry.getKey().equals("empEmpresa.empEmpresaId")) {
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

			} else if (entry.getKey().equals("fisNotaStatus.fisNotaStatusDescricao")) {
				// status
				FieldDef[] fdStatus = new FieldDef[] { new IntegerFieldDef("fisNotaStatusId"), new StringFieldDef("fisNotaStatusDescricao") };
				CoreProxy<FisNotaStatus> proxy = new CoreProxy<FisNotaStatus>(new FisNotaStatus());
				Store storeStatus = new Store(proxy, new ArrayReader(new RecordDef(fdStatus)), true);

				GridListFilter fStatus = new GridListFilter("fisNotaStatus.fisNotaStatusDescricao", storeStatus);
				fStatus.setLabelField("fisNotaStatusDescricao");
				fStatus.setLabelValue("fisNotaStatusDescricao");
				fStatus.setLoadingText(OpenSigCore.i18n.txtAguarde());
				entry.setValue(fStatus);
			}
		}
	}

	public void setFavorito(IFavorito favorito) {
		filtros.get(nomes.get("data")).setActive(false, true);
		filtros.get("empEmpresa.empEmpresaId").setActive(false, true);
		super.setFavorito(favorito);
	}

	@Override
	public void setImportacao(final SisExpImp modo) {
		janela = new JanelaUpload<E>();
		janela.setTipos(modo.getSisExpImpExtensoes() != null ? modo.getSisExpImpExtensoes().split(" ") : null);
		janela.setAssincrono(new AsyncCallback() {
			public void onSuccess(Object result) {
				analisar(modo);
			}

			public void onFailure(Throwable caught) {
				analisar(modo);
			}
		});
		janela.inicializar();
	}

	private void analisar(SisExpImp modo) {
		if (janela.getOks().size() > 0) {
			MessageBox.wait(OpenSigCore.i18n.txtAguarde(), OpenSigCore.i18n.txtNfe());
			ImportacaoProxy<E> proxy = new ImportacaoProxy<E>();
			proxy.importar(modo, janela.getOks(), new AsyncCallback<Map<String, List<E>>>() {

				public void onSuccess(Map<String, List<E>> result) {
					janela.getOks().clear();
					janela.getErros().clear();
					MessageBox.hide();
					
					if (result.size() == 0) {
						new ToastWindow(OpenSigCore.i18n.txtNfe(), OpenSigCore.i18n.msgImportarOK()).show();
					} else {
						for (E erro : result.get("erro")) {
							janela.getErros().add(getErro(erro));
						}
						janela.resultado();
					}
				}

				public void onFailure(Throwable caught) {
					MessageBox.hide();
					MessageBox.alert(OpenSigCore.i18n.txtNfe(), caught.toString());
					new ToastWindow(OpenSigCore.i18n.txtImportar(), OpenSigCore.i18n.errImportar()).show();
				}
			});
		}
	}

	protected abstract String getXml(E result);

	protected abstract String getXmlCancelado(E result);

	protected abstract String getChave(E result);

	protected abstract String getErro(E result);
	
	protected abstract void mostrarErro(E result);

	public Map<String, String> getNomes() {
		return nomes;
	}

	public void setNomes(Map<String, String> nomes) {
		this.nomes = nomes;
	}
}
