package br.com.opensig.poker.client.visao.lista;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.UtilClient;
import br.com.opensig.core.client.visao.abstrato.AListagem;
import br.com.opensig.core.client.visao.abstrato.IFormulario;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.poker.client.controlador.comando.ComandoCash;
import br.com.opensig.poker.client.controlador.comando.ComandoCliente;
import br.com.opensig.poker.shared.modelo.PokerJogador;

import com.gwtext.client.data.BooleanFieldDef;
import com.gwtext.client.data.DateFieldDef;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.IntegerFieldDef;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.grid.BaseColumnConfig;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.menu.Menu;
import com.gwtext.client.widgets.menu.MenuItem;

public class ListagemJogador extends AListagem<PokerJogador> {

	public ListagemJogador(IFormulario formulario) {
		super(formulario);
		inicializar();
	}

	public void inicializar() {
		// campos
		FieldDef[] fd = new FieldDef[] { new IntegerFieldDef("pokerJogadorId"), new IntegerFieldDef("pokerCliente.pokerClienteId"), new StringFieldDef("pokerCliente.pokerClienteCodigo"),
				new StringFieldDef("pokerCliente.pokerClienteNome"), new IntegerFieldDef("pokerCash.pokerCashId"), new StringFieldDef("pokerCash.pokerCashMesa"),
				new BooleanFieldDef("pokerCash.pokerCashFechado"), new DateFieldDef("pokerJogadorEntrada"), new DateFieldDef("pokerJogadorSaida"), new BooleanFieldDef("pokerJogadorAtivo") };
		campos = new RecordDef(fd);

		// colunas
		ColumnConfig ccId = new ColumnConfig(OpenSigCore.i18n.txtCod(), "pokerJogadorId", 50, true);
		ColumnConfig ccClienteId = new ColumnConfig(OpenSigCore.i18n.txtCod() + "-" + OpenSigCore.i18n.txtCliente(), "pokerCliente.pokerClienteId", 100, true);
		ccClienteId.setHidden(true);
		ColumnConfig ccClienteNumero = new ColumnConfig(OpenSigCore.i18n.txtNumero() + "-" + OpenSigCore.i18n.txtCliente(), "pokerCliente.pokerClienteCodigo", 150, true);
		ColumnConfig ccCliente = new ColumnConfig(OpenSigCore.i18n.txtCliente(), "pokerCliente.pokerClienteNome", 150, true);
		ColumnConfig ccCashId = new ColumnConfig(OpenSigCore.i18n.txtCod() + "-" + OpenSigCore.i18n.txtCash(), "pokerCash.pokerCashId", 100, true);
		ColumnConfig ccCashMesa = new ColumnConfig(OpenSigCore.i18n.txtMesa(), "pokerCash.pokerCashMesa", 100, true);
		ColumnConfig ccCashFechado = new ColumnConfig("", "pokerCash.pokerCashFechado", 50, false, BOLEANO);
		ccCashFechado.setHidden(true);
		ccCashFechado.setFixed(true);
		ColumnConfig ccEntrada = new ColumnConfig(OpenSigCore.i18n.txtEntrada(), "pokerJogadorEntrada", 120, true, DATAHORA);
		ColumnConfig ccSaida = new ColumnConfig(OpenSigCore.i18n.txtSaida(), "pokerJogadorSaida", 120, true, DATAHORA);
		ColumnConfig ccAtivo = new ColumnConfig(OpenSigCore.i18n.txtAtivo(), "pokerJogadorAtivo", 50, true, BOLEANO);

		BaseColumnConfig[] bcc = new BaseColumnConfig[] { ccId, ccClienteId, ccClienteNumero, ccCliente, ccCashId, ccCashMesa, ccCashFechado, ccEntrada, ccSaida, ccAtivo };
		modelos = new ColumnModel(bcc);
		super.inicializar();
	}

	public void irPara() {
		Menu mnuContexto = new Menu();

		// cash
		SisFuncao cash = UtilClient.getFuncaoPermitida(ComandoCash.class);
		MenuItem itemCash = gerarFuncao(cash, "pokerCashId", "pokerCash.pokerCashId");
		if (itemCash != null) {
			mnuContexto.addItem(itemCash);
		}

		// cliente
		SisFuncao cliente = UtilClient.getFuncaoPermitida(ComandoCliente.class);
		MenuItem itemCliente = gerarFuncao(cliente, "pokerClienteId", "pokerCliente.pokerClienteId");
		if (itemCliente != null) {
			mnuContexto.addItem(itemCliente);
		}

		if (mnuContexto.getItems().length > 0) {
			MenuItem mnuItem = getIrPara();
			mnuItem.setMenu(mnuContexto);

			getMenu().addSeparator();
			getMenu().addItem(mnuItem);
		}
	}
}
