package br.com.opensig.poker.client.visao.lista;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.UtilClient;
import br.com.opensig.core.client.visao.abstrato.AListagem;
import br.com.opensig.core.client.visao.abstrato.IFormulario;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.poker.client.controlador.comando.ComandoPagar;
import br.com.opensig.poker.client.controlador.comando.ComandoReceber;
import br.com.opensig.poker.shared.modelo.PokerJackpot;

import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.FloatFieldDef;
import com.gwtext.client.data.IntegerFieldDef;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.grid.BaseColumnConfig;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.menu.Menu;
import com.gwtext.client.widgets.menu.MenuItem;
import com.gwtextux.client.widgets.grid.plugins.GridSummaryPlugin;
import com.gwtextux.client.widgets.grid.plugins.SummaryColumnConfig;

public class ListagemJackpot extends AListagem<PokerJackpot> {

	public ListagemJackpot(IFormulario formulario) {
		super(formulario);
		inicializar();
		addPlugin(new GridSummaryPlugin());
	}

	public void inicializar() {
		// campos
		FieldDef[] fd = new FieldDef[] { new IntegerFieldDef("pokerJackpotId"), new IntegerFieldDef("pokerForma.pokerFormaId"), new StringFieldDef("pokerForma.pokerFormaNome"),
				new IntegerFieldDef("pokerReceber.pokerReceberId"), new FloatFieldDef("pokerReceberValor"), new IntegerFieldDef("pokerPagar.pokerPagarId"), new FloatFieldDef("pokerPagarValor"),
				new FloatFieldDef("pokerJackpotTotal") };
		campos = new RecordDef(fd);

		// colunas
		ColumnConfig ccId = new ColumnConfig(OpenSigCore.i18n.txtCod(), "pokerJackpotId", 50, true);
		ColumnConfig ccTipoId = new ColumnConfig(OpenSigCore.i18n.txtCod() + "-" + OpenSigCore.i18n.txtTipo(), "pokerForma.pokerFormaId", 50, true);
		ccTipoId.setHidden(true);
		ColumnConfig ccTipo = new ColumnConfig(OpenSigCore.i18n.txtTipo(), "pokerForma.pokerFormaNome", 100, true);
		ColumnConfig ccReceberId = new ColumnConfig(OpenSigCore.i18n.txtCod() + "-" + OpenSigCore.i18n.txtReceber(), "pokerReceber.pokerReceberId", 50, true);
		ccReceberId.setHidden(true);
		ColumnConfig ccReceberValor = new ColumnConfig(OpenSigCore.i18n.txtUltimo() + "-" + OpenSigCore.i18n.txtRecebido(), "pokerReceberValor", 100, true, DINHEIRO);
		ColumnConfig ccPagarId = new ColumnConfig(OpenSigCore.i18n.txtCod() + "-" + OpenSigCore.i18n.txtPagar(), "pokerPagar.pokerPagarId", 50, true);
		ccPagarId.setHidden(true);
		ColumnConfig ccPagarValor = new ColumnConfig(OpenSigCore.i18n.txtUltimo() + "-" + OpenSigCore.i18n.txtPago(), "pokerPagarValor", 100, true, DINHEIRO);

		// sumarios
		SummaryColumnConfig ccTotal = new SummaryColumnConfig(SummaryColumnConfig.SUM, new ColumnConfig(OpenSigCore.i18n.txtTotal(), "pokerJackpotTotal", 100, true, DINHEIRO), DINHEIRO);

		BaseColumnConfig[] bcc = new BaseColumnConfig[] { ccId, ccTipoId, ccTipo, ccReceberId, ccReceberValor, ccPagarId, ccPagarValor, ccTotal };
		modelos = new ColumnModel(bcc);
		super.inicializar();
	}

	public void irPara() {
		Menu mnuContexto = new Menu();

		// pagar
		SisFuncao pagar = UtilClient.getFuncaoPermitida(ComandoPagar.class);
		MenuItem itemPagar = gerarFuncao(pagar, "pokerPagarId", "pokerPagar.pokerPagarId");
		if (itemPagar != null) {
			mnuContexto.addItem(itemPagar);
		}

		// receber
		SisFuncao receber = UtilClient.getFuncaoPermitida(ComandoReceber.class);
		MenuItem itemReceber = gerarFuncao(receber, "pokerReceberId", "pokerReceber.pokerReceberId");
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
