package br.com.opensig.poker.client.visao.lista;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.UtilClient;
import br.com.opensig.core.client.controlador.comando.IComando;
import br.com.opensig.core.client.controlador.comando.lista.ComandoEditar;
import br.com.opensig.core.client.controlador.comando.lista.ComandoExcluir;
import br.com.opensig.core.client.visao.abstrato.AListagem;
import br.com.opensig.core.client.visao.abstrato.IFormulario;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.poker.client.controlador.comando.ComandoCash;
import br.com.opensig.poker.shared.modelo.PokerReceber;

import com.gwtext.client.data.BooleanFieldDef;
import com.gwtext.client.data.DateFieldDef;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.FloatFieldDef;
import com.gwtext.client.data.IntegerFieldDef;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.grid.BaseColumnConfig;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.menu.Menu;
import com.gwtext.client.widgets.menu.MenuItem;
import com.gwtextux.client.widgets.grid.plugins.GridSummaryPlugin;
import com.gwtextux.client.widgets.grid.plugins.SummaryColumnConfig;

public class ListagemReceber extends AListagem<PokerReceber> {

	public ListagemReceber(IFormulario formulario) {
		super(formulario);
		inicializar();
		addPlugin(new GridSummaryPlugin());
	}

	public void inicializar() {
		// campos
		FieldDef[] fd = new FieldDef[] { new IntegerFieldDef("pokerReceberId"), new IntegerFieldDef("pokerForma.pokerFormaId"), new StringFieldDef("pokerForma.pokerFormaNome"),
				new IntegerFieldDef("pokerCash.pokerCashId"), new StringFieldDef("pokerCash.pokerCashMesa"), new StringFieldDef("pokerReceberDescricao"), new FloatFieldDef("pokerReceberValor"),
				new DateFieldDef("pokerReceberCadastrado"), new DateFieldDef("pokerReceberRealizado"), new BooleanFieldDef("pokerReceberAtivo") };
		campos = new RecordDef(fd);

		// colunas
		ColumnConfig ccId = new ColumnConfig(OpenSigCore.i18n.txtCod(), "pokerReceberId", 50, true);
		ColumnConfig ccTipoId = new ColumnConfig(OpenSigCore.i18n.txtCod() + "-" + OpenSigCore.i18n.txtTipo(), "pokerForma.pokerFormaId", 50, true);
		ccTipoId.setHidden(true);
		ColumnConfig ccTipo = new ColumnConfig(OpenSigCore.i18n.txtTipo(), "pokerForma.pokerFormaNome", 100, true);
		ColumnConfig ccCashId = new ColumnConfig(OpenSigCore.i18n.txtCod() + "-" + OpenSigCore.i18n.txtCash(), "pokerCash.pokerCashId", 100, true);
		ColumnConfig ccCashMesa = new ColumnConfig(OpenSigCore.i18n.txtMesa(), "pokerCash.pokerCashMesa", 100, true);
		ColumnConfig ccDescricao = new ColumnConfig(OpenSigCore.i18n.txtDescricao(), "pokerReceberDescricao", 200, true);
		ColumnConfig ccCadastro = new ColumnConfig(OpenSigCore.i18n.txtCadastrado(), "pokerReceberCadastrado", 120, true, DATAHORA);
		ColumnConfig ccRealizado = new ColumnConfig(OpenSigCore.i18n.txtRealizado(), "pokerReceberRealizado", 120, true, DATAHORA);
		ColumnConfig ccAtivo = new ColumnConfig(OpenSigCore.i18n.txtRecebido(), "pokerReceberAtivo", 75, true, BOLEANO);

		// sumarios
		SummaryColumnConfig ccValor = new SummaryColumnConfig(SummaryColumnConfig.SUM, new ColumnConfig(OpenSigCore.i18n.txtValor(), "pokerReceberValor", 75, true, DINHEIRO), DINHEIRO);

		BaseColumnConfig[] bcc = new BaseColumnConfig[] { ccId, ccTipoId, ccTipo, ccCashId, ccCashMesa, ccDescricao, ccValor, ccCadastro, ccRealizado, ccAtivo };
		modelos = new ColumnModel(bcc);
		super.inicializar();
	}

	public IComando AntesDaAcao(IComando comando) {
		Record rec = getSelectionModel().getSelected();

		// valida se pode editar
		if (comando instanceof ComandoEditar || comando instanceof ComandoExcluir) {
			if (rec != null && rec.getAsBoolean("pokerReceberAtivo")) {
				MessageBox.alert(OpenSigCore.i18n.txtAcesso(), OpenSigCore.i18n.txtAcessoNegado());
				comando = null;
			}
		}
		return comando;
	}

	public void irPara() {
		Menu mnuContexto = new Menu();

		// cash
		SisFuncao cash = UtilClient.getFuncaoPermitida(ComandoCash.class);
		MenuItem itemCash = gerarFuncao(cash, "pokerCashId", "pokerCash.pokerCashId");
		if (itemCash != null) {
			mnuContexto.addItem(itemCash);
		}

		if (mnuContexto.getItems().length > 0) {
			MenuItem mnuItem = getIrPara();
			mnuItem.setMenu(mnuContexto);

			getMenu().addSeparator();
			getMenu().addItem(mnuItem);
		}
	}
}
