package br.com.opensig.financeiro.client.visao.lista;

import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.UtilClient;
import br.com.opensig.core.client.controlador.comando.AComando;
import br.com.opensig.core.client.controlador.comando.IComando;
import br.com.opensig.core.client.controlador.comando.lista.ComandoExcluir;
import br.com.opensig.core.client.controlador.comando.lista.ComandoExcluirFinal;
import br.com.opensig.core.client.controlador.comando.lista.ComandoPermiteEmpresa;
import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.FiltroNumero;
import br.com.opensig.core.client.controlador.filtro.FiltroObjeto;
import br.com.opensig.core.client.js.OpenSigCoreJS;
import br.com.opensig.core.client.servico.CoreProxy;
import br.com.opensig.core.client.servico.ExportacaoProxy;
import br.com.opensig.core.client.visao.Ponte;
import br.com.opensig.core.client.visao.abstrato.AListagem;
import br.com.opensig.core.client.visao.abstrato.IFormulario;
import br.com.opensig.core.shared.modelo.IFavorito;
import br.com.opensig.empresa.shared.modelo.EmpEmpresa;
import br.com.opensig.financeiro.client.servico.FinanceiroProxy;
import br.com.opensig.financeiro.shared.modelo.FinConta;
import br.com.opensig.financeiro.shared.modelo.FinRetorno;

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

public class ListagemRetorno extends AListagem<FinRetorno> {

	protected IComando cmdExcluir;

	public ListagemRetorno(IFormulario formulario) {
		super(formulario);
		inicializar();
		addPlugin(new GridSummaryPlugin());
	}

	public void inicializar() {
		// campos
		FieldDef[] fd = new FieldDef[] { new IntegerFieldDef("finRetornoId"), new IntegerFieldDef("empEmpresa.empEmpresaId"), new StringFieldDef("empEmpresa.empEntidade.empEntidadeNome1"),
				new IntegerFieldDef("finConta.finContaId"), new StringFieldDef("finConta.finContaNome"), new DateFieldDef("finRetornoCadastro"), new IntegerFieldDef("finRetornoQuantidade"),
				new FloatFieldDef("finRetornoValor"), new StringFieldDef("finRetornoBoletos"), new StringFieldDef("finRetornoArquivo") };
		campos = new RecordDef(fd);

		// colunas
		ColumnConfig ccId = new ColumnConfig(OpenSigCore.i18n.txtCod(), "finRetornoId", 50, true);
		ColumnConfig ccEmpresaId = new ColumnConfig(OpenSigCore.i18n.txtCod() + " - " + OpenSigCore.i18n.txtEmpresa(), "empEmpresa.empEmpresaId", 100, true);
		ccEmpresaId.setHidden(true);
		ColumnConfig ccEmpresa = new ColumnConfig(OpenSigCore.i18n.txtEmpresa(), "empEmpresa.empEntidade.empEntidadeNome1", 100, true);
		ccEmpresa.setHidden(true);
		ColumnConfig ccContaId = new ColumnConfig(OpenSigCore.i18n.txtCod() + " - " + OpenSigCore.i18n.txtConta(), "finConta.finContaId", 100, true);
		ccContaId.setHidden(true);
		ColumnConfig ccConta = new ColumnConfig(OpenSigCore.i18n.txtConta(), "finConta.finContaNome", 100, true);
		ColumnConfig ccData = new ColumnConfig(OpenSigCore.i18n.txtData(), "finRetornoCadastro", 75, true, DATA);
		ColumnWithCellActionsConfig ccBoletos = new ColumnWithCellActionsConfig(OpenSigCore.i18n.txtBoleto(), "finRetornoBoletos", 200, true);
		ColumnWithCellActionsConfig ccArquivo = new ColumnWithCellActionsConfig(OpenSigCore.i18n.txtArquivo(), "finRetornoArquivo", 50, false);
		ccArquivo.setCellActions(new GridCellAction[] { new GridCellAction("icon-txt", OpenSigCore.i18n.txtBaixar(), new GridCellActionListener() {
			public boolean execute(GridPanel grid, Record record, String action, Object value, String dataIndex, int rowIndex, int colIndex) {
				int id = record.getAsInteger("finRetornoId");
				final CoreProxy<FinRetorno> core = new CoreProxy<FinRetorno>(classe);
				core.selecionar(id, new AsyncCallback<FinRetorno>() {

					public void onFailure(Throwable caught) {
						MessageBox.alert(OpenSigCore.i18n.txtExportar(), OpenSigCore.i18n.errExportar());
					}

					public void onSuccess(FinRetorno result) {
						String arquivo = OpenSigCoreJS.base64encode(result.getFinRetornoArquivo());
						ExportacaoProxy proxy = new ExportacaoProxy();
						proxy.exportar(arquivo, OpenSigCore.i18n.txtArquivo() + "_" + result.getFinRetornoId() + ".ret", new AsyncCallback<String>() {
							public void onSuccess(String result) {
								UtilClient.exportar("ExportacaoService?id=" + result);
							}

							public void onFailure(Throwable caught) {
								MessageBox.alert(OpenSigCore.i18n.txtExportar(), OpenSigCore.i18n.errExportar());
							}
						});
					}

				});

				return true;
			}
		}) });

		// sumarios
		SummaryColumnConfig sumQtd = new SummaryColumnConfig(SummaryColumnConfig.SUM, new ColumnConfig(OpenSigCore.i18n.txtQtd(), "finRetornoQuantidade", 75, true, NUMERO), NUMERO);
		SummaryColumnConfig sumValor = new SummaryColumnConfig(SummaryColumnConfig.SUM, new ColumnConfig(OpenSigCore.i18n.txtValor(), "finRetornoValor", 75, true, DINHEIRO), DINHEIRO);

		BaseColumnConfig[] bcc = new BaseColumnConfig[] { ccId, ccEmpresaId, ccEmpresa, ccContaId, ccConta, ccData, sumQtd, sumValor, ccBoletos, ccArquivo };
		modelos = new ColumnModel(bcc);

		// deletando
		cmdExcluir = new AComando(new ComandoExcluirFinal()) {
			public void execute(Map contexto) {
				super.execute(contexto);
				int id = UtilClient.getSelecionado(getPanel());
				classe.setId(id);

				FinanceiroProxy proxy = new FinanceiroProxy();
				proxy.excluirRetorno(classe, ASYNC);
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
		Record rec = getSelectionModel().getSelected();

		if (comando instanceof ComandoExcluir) {
			comando = null;
			if (rec != null && !rec.getAsBoolean("comCompraFechada")) {
				MessageBox.confirm(OpenSigCore.i18n.txtExcluir(), OpenSigCore.i18n.msgExcluir(), new MessageBox.ConfirmCallback() {
					public void execute(String btnID) {
						if (btnID.equalsIgnoreCase("yes")) {
							MessageBox.wait(OpenSigCore.i18n.txtAguarde(), OpenSigCore.i18n.txtExcluir());
							cmdExcluir.execute(contexto);
						}
					}
				});
			} else if (rec != null) {
				MessageBox.alert(OpenSigCore.i18n.txtAcesso(), OpenSigCore.i18n.txtAcessoNegado());
			}
		}

		return comando;
	}

	public void setGridFiltro() {
		super.setGridFiltro();
		for (Entry<String, GridFilter> entry : filtros.entrySet()) {
			if (entry.getKey().equals("finRetornoCadastro")) {
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
			} else if (entry.getKey().equals("finConta.finContaNome")) {
				// conta
				FieldDef[] fdConta = new FieldDef[] { new IntegerFieldDef("finContaId"), new IntegerFieldDef("EmpresaId"), new StringFieldDef("EmpresaNome"), new IntegerFieldDef("BancoId"),
						new StringFieldDef("BancoNome"), new StringFieldDef("finContaNome") };
				CoreProxy<FinConta> proxy = new CoreProxy<FinConta>(new FinConta(), filtroPadrao);
				Store storeConta = new Store(proxy, new ArrayReader(new RecordDef(fdConta)), true);

				GridListFilter fConta = new GridListFilter("finConta.finContaNome", storeConta);
				fConta.setLabelField("finContaNome");
				fConta.setLabelValue("finContaNome");
				fConta.setLoadingText(OpenSigCore.i18n.txtAguarde());
				entry.setValue(fConta);
			}
		}
	}

	public void setFavorito(IFavorito favorito) {
		filtros.get("finRetornoCadastro").setActive(false, true);
		filtros.get("empEmpresa.empEmpresaId").setActive(false, true);
		super.setFavorito(favorito);
	}
}
