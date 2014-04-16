package br.com.opensig.poker.client.visao.lista;

import java.util.List;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.FiltroNumero;
import br.com.opensig.core.client.visao.abstrato.AListagemEditor;
import br.com.opensig.core.client.visao.abstrato.IListagem;
import br.com.opensig.poker.shared.modelo.PokerNivel;

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

public class ListagemNivel extends AListagemEditor<PokerNivel> {

	private NumberField txtNumero;
	private NumberField txtPequeno;
	private NumberField txtGrande;
	private NumberField txtAnte;
	private NumberField txtTempo;
	private NumberField txtEspera;
	private Checkbox chkAtivo;

	public ListagemNivel(boolean barraTarefa) {
		super(new PokerNivel(), barraTarefa);
		inicializar();
	}

	public void inicializar() {
		// campos
		FieldDef[] fd = new FieldDef[] { new IntegerFieldDef("pokerNivelId"), new IntegerFieldDef("pokerNivelNumero"), new IntegerFieldDef("pokerNivelPequeno"),
				new IntegerFieldDef("pokerNivelGrande"), new IntegerFieldDef("pokerNivelAnte"), new IntegerFieldDef("pokerNivelTempo"), new IntegerFieldDef("pokerNivelEspera"),
				new BooleanFieldDef("pokerNivelAtivo") };
		campos = new RecordDef(fd);

		// colunas
		ColumnConfig ccId = new ColumnConfig("", "pokerNivelId", 10, false);
		ccId.setHidden(true);
		ccId.setFixed(true);

		txtNumero = new NumberField();
		txtNumero.setAllowBlank(false);
		txtNumero.setAllowDecimals(false);
		txtNumero.setAllowNegative(false);
		txtNumero.setSelectOnFocus(true);
		ColumnConfig ccNumero = new ColumnConfig(OpenSigCore.i18n.txtNumero(), "pokerNivelNumero", 75, false);
		ccNumero.setEditor(new GridEditor(txtNumero));

		txtPequeno = new NumberField();
		txtPequeno.setAllowBlank(false);
		txtPequeno.setAllowDecimals(false);
		txtPequeno.setAllowNegative(false);
		txtPequeno.setSelectOnFocus(true);
		ColumnConfig ccPequeno = new ColumnConfig(OpenSigCore.i18n.txtSmall(), "pokerNivelPequeno", 75, false);
		ccPequeno.setEditor(new GridEditor(txtPequeno));

		txtGrande = new NumberField();
		txtGrande.setAllowBlank(false);
		txtGrande.setAllowDecimals(false);
		txtGrande.setAllowNegative(false);
		txtGrande.setSelectOnFocus(true);
		ColumnConfig ccGrande = new ColumnConfig(OpenSigCore.i18n.txtBig(), "pokerNivelGrande", 75, false);
		ccGrande.setEditor(new GridEditor(txtGrande));

		txtAnte = new NumberField();
		txtAnte.setAllowBlank(false);
		txtAnte.setAllowDecimals(false);
		txtAnte.setAllowNegative(false);
		txtAnte.setSelectOnFocus(true);
		ColumnConfig ccAnte = new ColumnConfig(OpenSigCore.i18n.txtAnte(), "pokerNivelAnte", 75, false);
		ccAnte.setEditor(new GridEditor(txtAnte));

		txtTempo = new NumberField();
		txtTempo.setAllowBlank(false);
		txtTempo.setAllowDecimals(false);
		txtTempo.setAllowNegative(false);
		txtTempo.setSelectOnFocus(true);
		ColumnConfig ccTempo = new ColumnConfig(OpenSigCore.i18n.txtTempo(), "pokerNivelTempo", 75, false);
		ccTempo.setEditor(new GridEditor(txtTempo));

		txtEspera = new NumberField();
		txtEspera.setAllowBlank(false);
		txtEspera.setAllowDecimals(false);
		txtEspera.setAllowNegative(false);
		txtEspera.setSelectOnFocus(true);
		ColumnConfig ccEspera = new ColumnConfig(OpenSigCore.i18n.txtEspera(), "pokerNivelEspera", 75, false);
		ccEspera.setEditor(new GridEditor(txtEspera));

		chkAtivo = new Checkbox();
		ColumnConfig ccAtivo = new ColumnConfig(OpenSigCore.i18n.txtAtivo(), "pokerNivelAtivo", 75, false, IListagem.BOLEANO);
		ccAtivo.setEditor(new GridEditor(chkAtivo));

		BaseColumnConfig[] bcc = new BaseColumnConfig[] { ccId, ccNumero, ccPequeno, ccGrande, ccAnte, ccTempo, ccEspera, ccAtivo };
		modelos = new ColumnModel(bcc);

		// barra de menu
		filtroPadrao = new FiltroNumero("pokerNivelId", ECompara.IGUAL, 0);

		// configurações padrão e carrega dados
		setTitle(OpenSigCore.i18n.txtNivel(), "icon-nivel");
		super.inicializar();
	}

	public boolean validar(List<PokerNivel> lista) {
		Record[] recs = getStore().getRecords();
		boolean valida = recs.length > 0;

		for (Record rec : recs) {
			try {
				int id = rec.getAsInteger("pokerNivelId");
				int numero = rec.getAsInteger("pokerNivelNumero");
				int pequeno = rec.getAsInteger("pokerNivelPequeno");
				int grande = rec.getAsInteger("pokerNivelGrande");
				int ante = rec.getAsInteger("pokerNivelAnte");
				int tempo = rec.getAsInteger("pokerNivelTempo");
				int espera = rec.getAsInteger("pokerNivelEspera");
				boolean ativo = rec.getAsBoolean("pokerNivelAtivo");

				if (numero < 1 || pequeno < 1 || grande < 1 || tempo < 1) {
					throw new Exception();
				}

				PokerNivel nivel = new PokerNivel(id);
				nivel.setPokerNivelNumero(numero);
				nivel.setPokerNivelPequeno(pequeno);
				nivel.setPokerNivelGrande(grande);
				nivel.setPokerNivelAnte(ante);
				nivel.setPokerNivelTempo(tempo);
				nivel.setPokerNivelEspera(espera);
				nivel.setPokerNivelAtivo(ativo);

				lista.add(nivel);
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

	public NumberField getTxtPequeno() {
		return txtPequeno;
	}

	public void setTxtPequeno(NumberField txtPequeno) {
		this.txtPequeno = txtPequeno;
	}

	public NumberField getTxtGrande() {
		return txtGrande;
	}

	public void setTxtGrande(NumberField txtGrande) {
		this.txtGrande = txtGrande;
	}

	public NumberField getTxtAnte() {
		return txtAnte;
	}

	public void setTxtAnte(NumberField txtAnte) {
		this.txtAnte = txtAnte;
	}

	public NumberField getTxtTempo() {
		return txtTempo;
	}

	public void setTxtTempo(NumberField txtTempo) {
		this.txtTempo = txtTempo;
	}

	public NumberField getTxtEspera() {
		return txtEspera;
	}

	public void setTxtEspera(NumberField txtEspera) {
		this.txtEspera = txtEspera;
	}

	public Checkbox getChkAtivo() {
		return chkAtivo;
	}

	public void setChkAtivo(Checkbox chkAtivo) {
		this.chkAtivo = chkAtivo;
	}

}
