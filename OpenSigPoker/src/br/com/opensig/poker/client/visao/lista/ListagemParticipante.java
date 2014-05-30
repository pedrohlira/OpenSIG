package br.com.opensig.poker.client.visao.lista;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.UtilClient;
import br.com.opensig.core.client.controlador.comando.IComando;
import br.com.opensig.core.client.controlador.comando.lista.ComandoEditar;
import br.com.opensig.core.client.controlador.comando.lista.ComandoExcluir;
import br.com.opensig.core.client.visao.abstrato.AListagem;
import br.com.opensig.core.client.visao.abstrato.IFormulario;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.poker.client.controlador.comando.ComandoCliente;
import br.com.opensig.poker.client.controlador.comando.ComandoTorneio;
import br.com.opensig.poker.shared.modelo.PokerParticipante;

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
import com.gwtext.client.widgets.grid.CheckboxColumnConfig;
import com.gwtext.client.widgets.grid.CheckboxSelectionModel;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.menu.Menu;
import com.gwtext.client.widgets.menu.MenuItem;
import com.gwtextux.client.widgets.grid.plugins.GridSummaryPlugin;
import com.gwtextux.client.widgets.grid.plugins.SummaryColumnConfig;

public class ListagemParticipante extends AListagem<PokerParticipante> {

	public ListagemParticipante(IFormulario formulario) {
		super(formulario);
		inicializar();
		addPlugin(new GridSummaryPlugin());
	}

	public void inicializar() {
		// campos
		FieldDef[] fd = new FieldDef[] { new IntegerFieldDef("pokerParticipanteId"), new IntegerFieldDef("pokerTorneio.pokerTorneioId"),
				new StringFieldDef("pokerTorneio.pokerTorneioTipo.pokerTorneioTipoNome"), new StringFieldDef("pokerTorneio.pokerTorneioCodigo"), new DateFieldDef("pokerTorneio.pokerTorneioData"),
				new BooleanFieldDef("pokerTorneio.pokerTorneioFechado"), new IntegerFieldDef("pokerCliente.pokerClienteId"), new StringFieldDef("pokerCliente.pokerClienteCodigo"),
				new StringFieldDef("pokerCliente.pokerClienteNome"), new IntegerFieldDef("pokerMesa.pokerMesaId"), new StringFieldDef("pokerMesa.pokerMesaNumero"),
				new IntegerFieldDef("pokerParticipanteBonus"), new IntegerFieldDef("pokerParticipanteReentrada"), new IntegerFieldDef("pokerParticipanteAdicional"),
				new IntegerFieldDef("pokerParticipanteDealer"), new IntegerFieldDef("pokerParticipantePosicao"), new IntegerFieldDef("pokerParticipantePonto"),
				new FloatFieldDef("pokerParticipantePremio"), new BooleanFieldDef("pokerParticipanteAtivo") };
		campos = new RecordDef(fd);

		// selected
		CheckboxSelectionModel model = new CheckboxSelectionModel();
		CheckboxColumnConfig check = new CheckboxColumnConfig(model);

		// colunas
		ColumnConfig ccId = new ColumnConfig(OpenSigCore.i18n.txtCod(), "pokerParticipanteId", 50, true);
		ColumnConfig ccTorneioId = new ColumnConfig(OpenSigCore.i18n.txtCod() + "-" + OpenSigCore.i18n.txtTorneio(), "pokerTorneio.pokerTorneioId", 50, true);
		ccTorneioId.setHidden(true);
		ColumnConfig ccTipo = new ColumnConfig(OpenSigCore.i18n.txtTipo(), "pokerTorneio.pokerTorneioTipo.pokerTorneioTipoNome", 100, true);
		ColumnConfig ccTorneio = new ColumnConfig(OpenSigCore.i18n.txtTorneio(), "pokerTorneio.pokerTorneioCodigo", 100, true);
		ColumnConfig ccTorneioData = new ColumnConfig(OpenSigCore.i18n.txtData(), "pokerTorneio.pokerTorneioData", 75, true, DATA);
		ColumnConfig ccFechado = new ColumnConfig(OpenSigCore.i18n.txtFechada(), "pokerTorneio.pokerTorneioFechado", 50, true, BOLEANO);
		ccFechado.setHidden(true);
		ccFechado.setFixed(true);
		ColumnConfig ccClienteId = new ColumnConfig(OpenSigCore.i18n.txtCod() + "-" + OpenSigCore.i18n.txtCliente(), "pokerCliente.pokerClienteId", 50, true);
		ccClienteId.setHidden(true);
		ColumnConfig ccNumero = new ColumnConfig(OpenSigCore.i18n.txtNumero() + "-" + OpenSigCore.i18n.txtCliente(), "pokerCliente.pokerClienteCodigo", 100, true);
		ColumnConfig ccCliente = new ColumnConfig(OpenSigCore.i18n.txtCliente(), "pokerCliente.pokerClienteNome", 150, true);
		ColumnConfig ccMesaId = new ColumnConfig(OpenSigCore.i18n.txtCod() + "-" + OpenSigCore.i18n.txtMesa(), "pokerMesa.pokerMesaId", 50, true);
		ccMesaId.setHidden(true);
		ColumnConfig ccMesa = new ColumnConfig(OpenSigCore.i18n.txtMesa(), "pokerMesa.pokerMesaNumero", 50, true);
		ColumnConfig ccBonus = new ColumnConfig(OpenSigCore.i18n.txtBonus(), "pokerParticipanteBonus", 50, true);
		ColumnConfig ccPosicao = new ColumnConfig(OpenSigCore.i18n.txtPosicao(), "pokerParticipantePosicao", 50, true);
		ColumnConfig ccAtivo = new ColumnConfig(OpenSigCore.i18n.txtAtivo(), "pokerParticipanteAtivo", 50, true, BOLEANO);

		// sumarios
		SummaryColumnConfig ccAdicional = new SummaryColumnConfig(SummaryColumnConfig.SUM, new ColumnConfig(OpenSigCore.i18n.txtAddon(), "pokerParticipanteAdicional", 50, true, BOLEANO), NUMERO);
		SummaryColumnConfig ccDealer = new SummaryColumnConfig(SummaryColumnConfig.SUM, new ColumnConfig(OpenSigCore.i18n.txtDealer(), "pokerParticipanteDealer", 75, true, BOLEANO), NUMERO);
		SummaryColumnConfig ccReentrada = new SummaryColumnConfig(SummaryColumnConfig.SUM, new ColumnConfig(OpenSigCore.i18n.txtRebuy(), "pokerParticipanteReentrada", 50, true), NUMERO);
		SummaryColumnConfig ccPonto = new SummaryColumnConfig(SummaryColumnConfig.SUM, new ColumnConfig(OpenSigCore.i18n.txtPontos(), "pokerParticipantePonto", 50, true, NUMERO), NUMERO);
		SummaryColumnConfig ccPremio = new SummaryColumnConfig(SummaryColumnConfig.SUM, new ColumnConfig(OpenSigCore.i18n.txtPremio(), "pokerParticipantePremio", 75, true, DINHEIRO), DINHEIRO);

		BaseColumnConfig[] bcc = new BaseColumnConfig[] { check, ccId, ccTorneioId, ccTipo, ccTorneio, ccTorneioData, ccFechado, ccClienteId, ccNumero, ccCliente, ccMesaId, ccMesa, ccBonus,
				ccReentrada, ccAdicional, ccDealer, ccPosicao, ccPonto, ccPremio, ccAtivo };
		modelos = new ColumnModel(bcc);
		super.inicializar();
		setSelectionModel(model);
	}

	public IComando AntesDaAcao(IComando comando) {
		Record rec = getSelectionModel().getSelected();

		// valida se pode editar
		if (comando instanceof ComandoEditar || comando instanceof ComandoExcluir) {
			if (rec != null && rec.getAsBoolean("pokerTorneio.pokerTorneioFechado")) {
				MessageBox.alert(OpenSigCore.i18n.txtAcesso(), OpenSigCore.i18n.txtAcessoNegado());
				comando = null;
			}
		}
		return comando;
	}

	public void irPara() {
		Menu mnuContexto = new Menu();

		// participantes
		SisFuncao torneio = UtilClient.getFuncaoPermitida(ComandoTorneio.class);
		MenuItem itemTorneio = gerarFuncao(torneio, "pokerTorneioId", "pokerTorneio.pokerTorneioId");
		if (itemTorneio != null) {
			mnuContexto.addItem(itemTorneio);
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
