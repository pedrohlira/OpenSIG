package br.com.opensig.comercial.client.visao.lista;

import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

import br.com.opensig.comercial.client.servico.ComercialProxy;
import br.com.opensig.comercial.shared.modelo.ComConsumo;
import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.UtilClient;
import br.com.opensig.core.client.controlador.comando.AComando;
import br.com.opensig.core.client.controlador.comando.IComando;
import br.com.opensig.core.client.controlador.comando.lista.ComandoEditar;
import br.com.opensig.core.client.controlador.comando.lista.ComandoExcluir;
import br.com.opensig.core.client.controlador.comando.lista.ComandoExcluirFinal;
import br.com.opensig.core.client.controlador.comando.lista.ComandoPermiteEmpresa;
import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.FiltroNumero;
import br.com.opensig.core.client.controlador.filtro.FiltroObjeto;
import br.com.opensig.core.client.servico.CoreProxy;
import br.com.opensig.core.client.visao.Ponte;
import br.com.opensig.core.client.visao.abstrato.AListagem;
import br.com.opensig.core.client.visao.abstrato.IFormulario;
import br.com.opensig.core.shared.modelo.IFavorito;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.empresa.client.controlador.comando.ComandoFornecedor;
import br.com.opensig.empresa.shared.modelo.EmpEmpresa;
import br.com.opensig.financeiro.client.controlador.comando.ComandoPagar;

import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.BooleanFieldDef;
import com.gwtext.client.data.DateFieldDef;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.FloatFieldDef;
import com.gwtext.client.data.IntegerFieldDef;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.SimpleStore;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.grid.BaseColumnConfig;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.menu.Menu;
import com.gwtext.client.widgets.menu.MenuItem;
import com.gwtextux.client.widgets.grid.plugins.GridDateFilter;
import com.gwtextux.client.widgets.grid.plugins.GridFilter;
import com.gwtextux.client.widgets.grid.plugins.GridListFilter;
import com.gwtextux.client.widgets.grid.plugins.GridLongFilter;
import com.gwtextux.client.widgets.grid.plugins.GridSummaryPlugin;
import com.gwtextux.client.widgets.grid.plugins.SummaryColumnConfig;

public class ListagemConsumo extends AListagem<ComConsumo> {

	protected IComando cmdExcluir;

	public ListagemConsumo(IFormulario<ComConsumo> formulario) {
		super(formulario);
		inicializar();
		addPlugin(new GridSummaryPlugin());
	}

	public void inicializar() {
		// campos
		FieldDef[] fd = new FieldDef[] { new IntegerFieldDef("comConsumoId"), new IntegerFieldDef("empFornecedor.empFornecedorId"), new IntegerFieldDef("empFornecedor.empEntidade.empEntidadeId"),
				new StringFieldDef("empFornecedor.empEntidade.empEntidadeNome1"), new IntegerFieldDef("empEmpresa.empEmpresaId"), new StringFieldDef("empEmpresa.empEntidade.empEntidadeNome1"),
				new StringFieldDef("comConsumoTipo"), new IntegerFieldDef("comConsumoDocumento"), new DateFieldDef("comConsumoData"), new FloatFieldDef("comConsumoValor"),
				new IntegerFieldDef("comConsumoCfop"), new FloatFieldDef("comConsumoBase"), new FloatFieldDef("comConsumoAliquota"), new FloatFieldDef("comConsumoIcms"),
				new BooleanFieldDef("comConsumoFechada"), new IntegerFieldDef("finPagar.finPagarId"), new BooleanFieldDef("comConsumoPaga"), new StringFieldDef("comConsumoObservacao") };
		campos = new RecordDef(fd);

		// colunas
		ColumnConfig ccId = new ColumnConfig(OpenSigCore.i18n.txtCod(), "comConsumoId", 75, true);
		ColumnConfig ccFornecedorId = new ColumnConfig(OpenSigCore.i18n.txtCod() + " - " + OpenSigCore.i18n.txtFornecedor(), "empFornecedor.empFornecedorId", 100, true);
		ccFornecedorId.setHidden(true);
		ColumnConfig ccEntidadeId = new ColumnConfig(OpenSigCore.i18n.txtCod() + " - " + OpenSigCore.i18n.txtEntidade(), "empFornecedor.empEntidade.empEntidadeId", 100, true);
		ccEntidadeId.setHidden(true);
		ColumnConfig ccFonecedorNome = new ColumnConfig(OpenSigCore.i18n.txtFornecedor(), "empFornecedor.empEntidade.empEntidadeNome1", 200, true);
		ColumnConfig ccEmpresaId = new ColumnConfig(OpenSigCore.i18n.txtCod() + " - " + OpenSigCore.i18n.txtEmpresa(), "empEmpresa.empEmpresaId", 100, true);
		ccEmpresaId.setHidden(true);
		ColumnConfig ccEmpresa = new ColumnConfig(OpenSigCore.i18n.txtEmpresa(), "empEmpresa.empEntidade.empEntidadeNome1", 100, true);
		ccEmpresa.setHidden(true);
		ColumnConfig ccTipo = new ColumnConfig(OpenSigCore.i18n.txtTipo(), "comConsumoTipo", 100, true);
		ColumnConfig ccDoc = new ColumnConfig(OpenSigCore.i18n.txtDocumento(), "comConsumoDocumento", 75, true, NUMERO);
		ColumnConfig ccData = new ColumnConfig(OpenSigCore.i18n.txtData(), "comConsumoData", 75, true, DATA);
		ColumnConfig ccCfop = new ColumnConfig(OpenSigCore.i18n.txtCfop(), "comConsumoCfop", 50, true);
		ColumnConfig ccBase = new ColumnConfig(OpenSigCore.i18n.txtIcmsBase(), "comConsumoBase", 75, true, DINHEIRO);
		ColumnConfig ccAliquota = new ColumnConfig(OpenSigCore.i18n.txtAliquota(), "comConsumoAliquota", 75, true, PORCENTAGEM);
		ColumnConfig ccFechada = new ColumnConfig(OpenSigCore.i18n.txtFechada(), "comConsumoFechada", 75, true, BOLEANO);
		ColumnConfig ccPagarId = new ColumnConfig(OpenSigCore.i18n.txtCod() + " - " + OpenSigCore.i18n.txtPagar(), "finPagar.finPagarId", 100, true);
		ccPagarId.setHidden(true);
		ColumnConfig ccPaga = new ColumnConfig(OpenSigCore.i18n.txtPaga(), "comConsumoPaga", 75, true, BOLEANO);
		ColumnConfig ccObservacao = new ColumnConfig(OpenSigCore.i18n.txtObservacao(), "comConsumoObservacao", 200, true);

		// sumarios
		SummaryColumnConfig sumValor = new SummaryColumnConfig(SummaryColumnConfig.SUM, new ColumnConfig(OpenSigCore.i18n.txtValor(), "comConsumoValor", 75, true, DINHEIRO), DINHEIRO);
		SummaryColumnConfig sumIcms = new SummaryColumnConfig(SummaryColumnConfig.SUM, new ColumnConfig(OpenSigCore.i18n.txtIcms(), "comConsumoIcms", 75, true, DINHEIRO), DINHEIRO);

		BaseColumnConfig[] bcc = new BaseColumnConfig[] { ccId, ccFornecedorId, ccEntidadeId, ccFonecedorNome, ccEmpresaId, ccEmpresa, ccTipo, ccDoc, ccData, sumValor, ccCfop, ccBase, ccAliquota,
				sumIcms, ccFechada, ccPagarId, ccPaga, ccObservacao };
		modelos = new ColumnModel(bcc);

		// excluindo
		cmdExcluir = new AComando(new ComandoExcluirFinal()) {
			public void execute(Map contexto) {
				super.execute(contexto);
				int id = UtilClient.getSelecionado(getPanel());
				classe.setId(id);

				ComercialProxy proxy = new ComercialProxy();
				proxy.excluirConsumo(classe, ASYNC);
			}
		};

		if (UtilClient.getAcaoPermitida(funcao, ComandoPermiteEmpresa.class) == null) {
			filtroPadrao = new FiltroObjeto("empEmpresa", ECompara.IGUAL, new EmpEmpresa(Ponte.getLogin().getEmpresaId()));
		}

		super.inicializar();
	}

	public IComando AntesDaAcao(IComando comando) {
		Record rec = getSelectionModel().getSelected();

		// valida se pode editar
		if (comando instanceof ComandoEditar) {
			if (rec != null && rec.getAsBoolean("comConsumoFechada")) {
				MessageBox.alert(OpenSigCore.i18n.txtAcesso(), OpenSigCore.i18n.txtAcessoNegado());
				comando = null;
			}
		}// valida se pode excluir
		else if (comando instanceof ComandoExcluir) {
			comando = null;
			if (rec != null) {
				MessageBox.confirm(OpenSigCore.i18n.txtExcluir(), OpenSigCore.i18n.msgExcluir(), new MessageBox.ConfirmCallback() {
					public void execute(String btnID) {
						if (btnID.equalsIgnoreCase("yes")) {
							MessageBox.wait(OpenSigCore.i18n.txtAguarde(), OpenSigCore.i18n.txtExcluir());
							cmdExcluir.execute(contexto);
						}
					}
				});
			}
		}

		return comando;
	}

	public void setGridFiltro() {
		super.setGridFiltro();
		for (Entry<String, GridFilter> entry : filtros.entrySet()) {
			if (entry.getKey().equals("comConsumoData")) {
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
			} else if (entry.getKey().equals("comConsumoTipo")) {
				// tipos de consumos
				Store store = new SimpleStore(new String[] { "id", "valor" }, new String[][] { new String[] { "21 - REDE", "21 - REDE" }, new String[] { "22 - FONE", "22 - FONE" },
						new String[] { "06 - LUZ", "06 - LUZ" }, new String[] { "28 - GÁS", "28 - GÁS" }, new String[] { "29 - ÁGUA", "29 - ÁGUA" } });
				store.load();
				GridListFilter fTipo = new GridListFilter("comConsumoTipo", store);
				fTipo.setLabelField("id");
				fTipo.setLabelValue("valor");
				fTipo.setLoadingText(OpenSigCore.i18n.txtAguarde());
				entry.setValue(fTipo);
			}
		}
	}

	public void setFavorito(IFavorito favorito) {
		filtros.get("comConsumoData").setActive(false, true);
		filtros.get("empEmpresa.empEmpresaId").setActive(false, true);
		super.setFavorito(favorito);
	}

	@Override
	public void irPara() {
		Menu mnuContexto = new Menu();

		// fornecedor
		SisFuncao fornecedor = UtilClient.getFuncaoPermitida(ComandoFornecedor.class);
		MenuItem itemFornecedor = gerarFuncao(fornecedor, "empFornecedorId", "empFornecedor.empFornecedorId");
		if (itemFornecedor != null) {
			mnuContexto.addItem(itemFornecedor);
		}

		// pagar
		SisFuncao pagar = UtilClient.getFuncaoPermitida(ComandoPagar.class);
		MenuItem itemPagar = gerarFuncao(pagar, "finPagarId", "finPagar.finPagarId");
		if (itemPagar != null) {
			mnuContexto.addItem(itemPagar);
		}

		if (mnuContexto.getItems().length > 0) {
			MenuItem mnuItem = getIrPara();
			mnuItem.setMenu(mnuContexto);

			getMenu().addSeparator();
			getMenu().addItem(mnuItem);
		}
	}
}
