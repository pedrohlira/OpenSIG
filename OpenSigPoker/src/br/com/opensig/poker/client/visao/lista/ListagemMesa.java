package br.com.opensig.poker.client.visao.lista;

import java.util.List;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.FiltroNumero;
import br.com.opensig.core.client.visao.abstrato.AListagemEditor;
import br.com.opensig.core.client.visao.abstrato.IListagem;
import br.com.opensig.poker.shared.modelo.PokerMesa;

import com.gwtext.client.data.BooleanFieldDef;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.IntegerFieldDef;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.widgets.form.Checkbox;
import com.gwtext.client.widgets.form.NumberField;
import com.gwtext.client.widgets.grid.BaseColumnConfig;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridEditor;

public class ListagemMesa extends AListagemEditor<PokerMesa> {

	private NumberField txtNumero;
	private NumberField txtLugares;
	private Checkbox chkAtivo;

	public ListagemMesa(boolean barraTarefa) {
		super(new PokerMesa(), barraTarefa);
		inicializar();
	}

	public void inicializar() {
		// campos
		FieldDef[] fd = new FieldDef[] { new IntegerFieldDef("pokerMesaId"), new IntegerFieldDef("pokerMesaNumero"), new IntegerFieldDef("pokerMesaLugares"),
				new IntegerFieldDef("pokerParticipantes"), new BooleanFieldDef("pokerMesaAtivo") };
		campos = new RecordDef(fd);

		// colunas
		ColumnConfig ccId = new ColumnConfig("", "pokerMesaId", 10, false);
		ccId.setHidden(true);
		ccId.setFixed(true);

		txtNumero = new NumberField();
		txtNumero.setAllowBlank(false);
		txtNumero.setAllowDecimals(false);
		txtNumero.setAllowNegative(false);
		txtNumero.setSelectOnFocus(true);
		ColumnConfig ccNumero = new ColumnConfig(OpenSigCore.i18n.txtNumero(), "pokerMesaNumero", 100, false);
		ccNumero.setEditor(new GridEditor(txtNumero));

		txtLugares = new NumberField();
		txtLugares.setAllowBlank(false);
		txtLugares.setAllowDecimals(false);
		txtLugares.setAllowNegative(false);
		txtLugares.setDecimalPrecision(0);
		txtLugares.setSelectOnFocus(true);
		ColumnConfig ccLugares = new ColumnConfig(OpenSigCore.i18n.txtLugares(), "pokerMesaLugares", 100, false);
		ccLugares.setEditor(new GridEditor(txtLugares));

		ColumnConfig ccParticipante = new ColumnConfig(OpenSigCore.i18n.txtParticipantes(), "pokerParticipantes", 100, false);
		
		chkAtivo = new Checkbox();
		ColumnConfig ccAtivo = new ColumnConfig(OpenSigCore.i18n.txtAtivo(), "pokerMesaAtivo", 100, false, IListagem.BOLEANO);
		ccAtivo.setEditor(new GridEditor(chkAtivo));

		BaseColumnConfig[] bcc = new BaseColumnConfig[] { ccId, ccNumero, ccLugares, ccParticipante, ccAtivo };
		modelos = new ColumnModel(bcc);

		filtroPadrao = new FiltroNumero("pokerMesaId", ECompara.IGUAL, 0);

		// configurações padrão e carrega dados
		setTitle(OpenSigCore.i18n.txtMesa(), "icon-mesa");
		super.inicializar();
	}

	public boolean validar(List<PokerMesa> lista) {
		Record[] recs = getStore().getRecords();
		boolean valida = recs.length > 0;

		for (Record rec : recs) {
			try {
				int id = rec.getAsInteger("pokerMesaId");
				int numero = rec.getAsInteger("pokerMesaNumero");
				int lugares = rec.getAsInteger("pokerMesaLugares");
				boolean ativo = rec.getAsBoolean("pokerMesaAtivo");

				if (numero < 0 || lugares < 1 || lugares > 9) {
					throw new Exception();
				}

				PokerMesa mesa = new PokerMesa(id);
				mesa.setPokerMesaNumero(numero);
				mesa.setPokerMesaLugares(lugares);
				mesa.setPokerMesaAtivo(ativo);
				lista.add(mesa);
			} catch (Exception ex) {
				valida = false;
				break;
			}
		}

		return valida;
	}

	public NumberField getTxtNumero() {
		return txtNumero;
	}

	public void setTxtNumero(NumberField txtNumero) {
		this.txtNumero = txtNumero;
	}

	public NumberField getTxtLugar() {
		return txtLugares;
	}

	public void setTxtLugar(NumberField txtLugar) {
		this.txtLugares = txtLugar;
	}

	public Checkbox getChkAtivo() {
		return chkAtivo;
	}

	public void setChkAtivo(Checkbox chkAtivo) {
		this.chkAtivo = chkAtivo;
	}

}
