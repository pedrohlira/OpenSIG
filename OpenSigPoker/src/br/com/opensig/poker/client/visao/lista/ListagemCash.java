package br.com.opensig.poker.client.visao.lista;

import java.util.Date;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.UtilClient;
import br.com.opensig.core.client.controlador.comando.IComando;
import br.com.opensig.core.client.controlador.comando.lista.ComandoExcluir;
import br.com.opensig.core.client.controlador.comando.lista.ComandoNovo;
import br.com.opensig.core.client.visao.abstrato.AListagem;
import br.com.opensig.core.client.visao.abstrato.IFormulario;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.poker.client.controlador.comando.ComandoJogador;
import br.com.opensig.poker.client.controlador.comando.ComandoPagar;
import br.com.opensig.poker.client.controlador.comando.ComandoReceber;
import br.com.opensig.poker.client.servico.PokerProxy;
import br.com.opensig.poker.shared.modelo.PokerCash;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.data.BooleanFieldDef;
import com.gwtext.client.data.DateFieldDef;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.FloatFieldDef;
import com.gwtext.client.data.IntegerFieldDef;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.MessageBox.PromptCallback;
import com.gwtext.client.widgets.grid.BaseColumnConfig;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.menu.Menu;
import com.gwtext.client.widgets.menu.MenuItem;
import com.gwtextux.client.widgets.grid.plugins.GridSummaryPlugin;
import com.gwtextux.client.widgets.grid.plugins.SummaryColumnConfig;
import com.gwtextux.client.widgets.window.ToastWindow;

public class ListagemCash extends AListagem<PokerCash> {

	public ListagemCash(IFormulario formulario) {
		super(formulario);
		inicializar();
		addPlugin(new GridSummaryPlugin());
	}

	public void inicializar() {
		// campos
		FieldDef[] fd = new FieldDef[] { new IntegerFieldDef("pokerCashId"), new StringFieldDef("pokerCashMesa"), new DateFieldDef("pokerCashInicio"), new DateFieldDef("pokerCashFim"),
				new FloatFieldDef("pokerCashPago"), new FloatFieldDef("pokerCashRecebido"), new FloatFieldDef("pokerCashSaldo"), new BooleanFieldDef("pokerCashFechado") };
		campos = new RecordDef(fd);

		// colunas
		ColumnConfig ccId = new ColumnConfig(OpenSigCore.i18n.txtCod(), "pokerCashId", 50, true);
		ColumnConfig ccMesa = new ColumnConfig(OpenSigCore.i18n.txtMesa(), "pokerCashMesa", 100, true);
		ColumnConfig ccInicio = new ColumnConfig(OpenSigCore.i18n.txtInicio(), "pokerCashInicio", 120, true, DATAHORA);
		ColumnConfig ccFim = new ColumnConfig(OpenSigCore.i18n.txtFim(), "pokerCashFim", 120, true, DATAHORA);
		ColumnConfig ccFechado = new ColumnConfig(OpenSigCore.i18n.txtFechada(), "pokerCashFechado", 100, true, BOLEANO);

		// sumarios
		SummaryColumnConfig ccPago = new SummaryColumnConfig(SummaryColumnConfig.SUM, new ColumnConfig(OpenSigCore.i18n.txtPago(), "pokerCashPago", 100, true, DINHEIRO), DINHEIRO);
		SummaryColumnConfig ccRecebido = new SummaryColumnConfig(SummaryColumnConfig.SUM, new ColumnConfig(OpenSigCore.i18n.txtRecebido(), "pokerCashRecebido", 100, true, DINHEIRO), DINHEIRO);
		SummaryColumnConfig ccSaldo = new SummaryColumnConfig(SummaryColumnConfig.SUM, new ColumnConfig(OpenSigCore.i18n.txtSaldo(), "pokerCashSaldo", 100, true, DINHEIRO), DINHEIRO);

		BaseColumnConfig[] bcc = new BaseColumnConfig[] { ccId, ccMesa, ccInicio, ccFim, ccPago, ccRecebido, ccSaldo, ccFechado };
		modelos = new ColumnModel(bcc);
		super.inicializar();
	}

	public IComando AntesDaAcao(IComando comando) {
		Record rec = getSelectionModel().getSelected();

		// adiciona um novo
		if (comando instanceof ComandoNovo) {
			MessageBox.prompt(OpenSigCore.i18n.txtMesa(), "", new PromptCallback() {
				public void execute(String btnID, String text) {
					if (btnID.equalsIgnoreCase("ok") && text != null && !text.equals("")) {
						PokerCash cash = new PokerCash();
						cash.setPokerCashMesa(text);
						cash.setPokerCashInicio(new Date());

						PokerProxy proxy = new PokerProxy();
						proxy.salvar(cash, new AsyncCallback<PokerCash>() {
							public void onSuccess(PokerCash result) {
								getStore().reload();
								new ToastWindow(OpenSigCore.i18n.txtSalvar(), OpenSigCore.i18n.msgSalvarOK()).show();
							}

							public void onFailure(Throwable caught) {
								MessageBox.alert(OpenSigCore.i18n.txtSalvar(), OpenSigCore.i18n.errSalvar());
							}
						});
					}
				}
			});
			comando = null;
		}
		// valida se pode excluir
		else if (comando instanceof ComandoExcluir) {
			if (rec != null && rec.getAsBoolean("pokerCashFechado")) {
				MessageBox.alert(OpenSigCore.i18n.txtExcluir(), OpenSigCore.i18n.txtAcessoNegado());
				comando = null;
			}
		}
		return comando;
	}

	public void irPara() {
		Menu mnuContexto = new Menu();

		// jogadores
		SisFuncao jogadores = UtilClient.getFuncaoPermitida(ComandoJogador.class);
		MenuItem itemJogador = gerarFuncao(jogadores, "pokerCash.pokerCashId", "pokerCashId");
		if (itemJogador != null) {
			mnuContexto.addItem(itemJogador);
		}

		// pagar
		SisFuncao pagar = UtilClient.getFuncaoPermitida(ComandoPagar.class);
		MenuItem itemPagar = gerarFuncao(pagar, "pokerCash.pokerCashId", "pokerCashId");
		if (itemPagar != null) {
			mnuContexto.addItem(itemPagar);
		}

		// receber
		SisFuncao receber = UtilClient.getFuncaoPermitida(ComandoReceber.class);
		MenuItem itemReceber = gerarFuncao(receber, "pokerCash.pokerCashId", "pokerCashId");
		if (itemReceber != null) {
			mnuContexto.addItem(itemReceber);
		}

		if (mnuContexto.getItems().length > 0) {
			MenuItem mnuItem = getIrPara();
			mnuItem.setMenu(mnuContexto);

			getMenu().addSeparator();
			getMenu().addItem(mnuItem);
		}
	}
}
