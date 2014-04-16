package br.com.opensig.poker.client.visao.lista;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.UtilClient;
import br.com.opensig.core.client.controlador.comando.IComando;
import br.com.opensig.core.client.controlador.comando.lista.ComandoEditar;
import br.com.opensig.core.client.controlador.comando.lista.ComandoExcluir;
import br.com.opensig.core.client.visao.abstrato.AListagem;
import br.com.opensig.core.client.visao.abstrato.IFormulario;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.poker.client.controlador.comando.ComandoParticipante;
import br.com.opensig.poker.shared.modelo.PokerTorneio;

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

public class ListagemTorneio extends AListagem<PokerTorneio> {

	public ListagemTorneio(IFormulario formulario) {
		super(formulario);
		inicializar();
		addPlugin(new GridSummaryPlugin());
	}

	public void inicializar() {
		// campos
		FieldDef[] fd = new FieldDef[] { new IntegerFieldDef("pokerTorneioId"), new IntegerFieldDef("pokerTorneioTipo.pokerTorneioTipoId"),
				new StringFieldDef("pokerTorneioTipo.pokerTorneioTipoNome"), new StringFieldDef("pokerTorneioCodigo"), new StringFieldDef("pokerTorneioNome"),
				new FloatFieldDef("pokerTorneioEntrada"), new IntegerFieldDef("pokerTorneioEntradaFicha"), new FloatFieldDef("pokerTorneioReentrada"),
				new IntegerFieldDef("pokerTorneioReentradaFicha"), new FloatFieldDef("pokerTorneioAdicional"), new IntegerFieldDef("pokerTorneioAdicionalFicha"),
				new FloatFieldDef("pokerTorneioDealer"), new IntegerFieldDef("pokerTorneioDealerFicha"), new IntegerFieldDef("pokerTorneioPonto"), new FloatFieldDef("pokerTorneioArrecadado"),
				new FloatFieldDef("pokerTorneioPremio"), new FloatFieldDef("pokerTorneioTaxa"), new FloatFieldDef("pokerTorneioComissao"), new DateFieldDef("pokerTorneioData"),
				new BooleanFieldDef("pokerTorneioFechado"), new BooleanFieldDef("pokerTorneioAtivo") };
		campos = new RecordDef(fd);

		// colunas
		ColumnConfig ccId = new ColumnConfig(OpenSigCore.i18n.txtCod(), "pokerTorneioId", 50, true);
		ColumnConfig ccTipoId = new ColumnConfig(OpenSigCore.i18n.txtCod() + "-" + OpenSigCore.i18n.txtTipo(), "pokerTorneioTipo.pokerTorneioTipoId", 50, true);
		ccTipoId.setHidden(true);
		ColumnConfig ccTipo = new ColumnConfig(OpenSigCore.i18n.txtTipo(), "pokerTorneioTipo.pokerTorneioTipoNome", 100, true);
		ColumnConfig ccCodigo = new ColumnConfig(OpenSigCore.i18n.txtCod(), "pokerTorneioCodigo", 100, true);
		ColumnConfig ccNome = new ColumnConfig(OpenSigCore.i18n.txtNome(), "pokerTorneioNome", 200, true);
		ColumnConfig ccEntradaFicha = new ColumnConfig("B" + OpenSigCore.i18n.txtFichas(), "pokerTorneioEntradaFicha", 50, true, NUMERO);
		ColumnConfig ccReentradaFicha = new ColumnConfig("R" + OpenSigCore.i18n.txtFichas(), "pokerTorneioReentradaFicha", 50, true, NUMERO);
		ColumnConfig ccAdicionalFicha = new ColumnConfig("A" + OpenSigCore.i18n.txtFichas(), "pokerTorneioAdicionalFicha", 50, true, NUMERO);
		ColumnConfig ccDealerFicha = new ColumnConfig("D" + OpenSigCore.i18n.txtFichas(), "pokerTorneioDealerFicha", 50, true, NUMERO);
		ColumnConfig ccPonto = new ColumnConfig(OpenSigCore.i18n.txtPontos(), "pokerTorneioPonto", 50, true, NUMERO);
		ColumnConfig ccTaxa = new ColumnConfig(OpenSigCore.i18n.txtTaxa() + "%", "pokerTorneioTaxa", 75, true, PORCENTAGEM);
		ColumnConfig ccData = new ColumnConfig(OpenSigCore.i18n.txtData(), "pokerTorneioData", 75, true, DATA);
		ColumnConfig ccFechado = new ColumnConfig(OpenSigCore.i18n.txtFechada(), "pokerTorneioFechado", 75, true, BOLEANO);
		ColumnConfig ccAtivo = new ColumnConfig(OpenSigCore.i18n.txtAtivo(), "pokerTorneioAtivo", 50, true, BOLEANO);

		// sumarios
		SummaryColumnConfig ccEntrada = new SummaryColumnConfig(SummaryColumnConfig.SUM, new ColumnConfig(OpenSigCore.i18n.txtBuyin(), "pokerTorneioEntrada", 75, true, DINHEIRO), DINHEIRO);
		SummaryColumnConfig ccReentrada = new SummaryColumnConfig(SummaryColumnConfig.SUM, new ColumnConfig(OpenSigCore.i18n.txtRebuy(), "pokerTorneioReentrada", 75, true, DINHEIRO), DINHEIRO);
		SummaryColumnConfig ccAdicional = new SummaryColumnConfig(SummaryColumnConfig.SUM, new ColumnConfig(OpenSigCore.i18n.txtAddon(), "pokerTorneioAdicional", 75, true, DINHEIRO), DINHEIRO);
		SummaryColumnConfig ccDealer = new SummaryColumnConfig(SummaryColumnConfig.SUM, new ColumnConfig(OpenSigCore.i18n.txtDealer(), "pokerTorneioDealer", 75, true, DINHEIRO), DINHEIRO);
		SummaryColumnConfig ccArrecadado = new SummaryColumnConfig(SummaryColumnConfig.SUM, new ColumnConfig(OpenSigCore.i18n.txtArrecadado(), "pokerTorneioArrecadado", 75, true, DINHEIRO), DINHEIRO);
		SummaryColumnConfig ccPremio = new SummaryColumnConfig(SummaryColumnConfig.SUM, new ColumnConfig(OpenSigCore.i18n.txtPremio(), "pokerTorneioPremio", 75, true, DINHEIRO), DINHEIRO);
		SummaryColumnConfig ccComissao = new SummaryColumnConfig(SummaryColumnConfig.SUM, new ColumnConfig(OpenSigCore.i18n.txtComissao(), "pokerTorneioComissao", 75, true, DINHEIRO), DINHEIRO);

		BaseColumnConfig[] bcc = new BaseColumnConfig[] { ccId, ccTipoId, ccTipo, ccCodigo, ccNome, ccEntrada, ccEntradaFicha, ccReentrada, ccReentradaFicha, ccAdicional, ccAdicionalFicha, ccDealer,
				ccDealerFicha, ccPonto, ccArrecadado, ccPremio, ccTaxa, ccComissao, ccData, ccFechado, ccAtivo };
		modelos = new ColumnModel(bcc);
		super.inicializar();
	}

	public IComando AntesDaAcao(IComando comando) {
		Record rec = getSelectionModel().getSelected();

		// valida se pode editar
		if (comando instanceof ComandoEditar || comando instanceof ComandoExcluir) {
			if (rec != null && rec.getAsBoolean("pokerTorneioFechado") && !rec.getAsBoolean("pokerTorneioAtivo")) {
				MessageBox.alert(OpenSigCore.i18n.txtAcesso(), OpenSigCore.i18n.txtAcessoNegado());
				comando = null;
			}
		}
		return comando;
	}

	public void irPara() {
		Menu mnuContexto = new Menu();

		// participantes
		SisFuncao participantes = UtilClient.getFuncaoPermitida(ComandoParticipante.class);
		MenuItem itemParticipante = gerarFuncao(participantes, "pokerTorneio.pokerTorneioId", "pokerTorneioId");
		if (itemParticipante != null) {
			mnuContexto.addItem(itemParticipante);
		}

		if (mnuContexto.getItems().length > 0) {
			MenuItem mnuItem = getIrPara();
			mnuItem.setMenu(mnuContexto);

			getMenu().addSeparator();
			getMenu().addItem(mnuItem);
		}
	}
}
