package br.com.opensig.comercial.client.visao.lista;

import java.util.List;

import br.com.opensig.comercial.shared.modelo.ComValorArredonda;
import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.FiltroNumero;
import br.com.opensig.core.client.visao.abstrato.AListagemEditor;
import br.com.opensig.core.client.visao.abstrato.IListagem;

import com.gwtext.client.core.Ext;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.FloatFieldDef;
import com.gwtext.client.data.IntegerFieldDef;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.widgets.form.NumberField;
import com.gwtext.client.widgets.grid.BaseColumnConfig;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridEditor;

public class ListagemValorArredonda extends AListagemEditor<ComValorArredonda> {

	private NumberField txtMin;
	private NumberField txtMax;
	private NumberField txtFixo;

	public ListagemValorArredonda(boolean barraTarefa) {
		super(new ComValorArredonda(), barraTarefa);
		inicializar();
	}

	public void inicializar() {
		// campos
		FieldDef[] fd = new FieldDef[] { new IntegerFieldDef("comValorProdutoId"), new IntegerFieldDef("comValorArredondaId"), new FloatFieldDef("comValorArredondaMin"),
				new FloatFieldDef("comValorArredondaMax"), new FloatFieldDef("comValorArredondaFixo") };
		campos = new RecordDef(fd);

		// editores
		txtMin = new NumberField();
		txtMin.setAllowBlank(false);
		txtMin.setAllowDecimals(true);
		txtMin.setAllowNegative(false);
		txtMin.setDecimalPrecision(2);
		txtMin.setMaxLength(4);
		txtMin.setSelectOnFocus(true);

		txtMax = new NumberField();
		txtMax.setAllowBlank(false);
		txtMax.setAllowDecimals(true);
		txtMax.setAllowNegative(false);
		txtMax.setDecimalPrecision(2);
		txtMax.setMaxLength(4);
		txtMax.setSelectOnFocus(true);

		txtFixo = new NumberField();
		txtFixo.setAllowBlank(false);
		txtFixo.setAllowDecimals(true);
		txtFixo.setAllowNegative(false);
		txtFixo.setDecimalPrecision(2);
		txtFixo.setMaxLength(4);
		txtFixo.setSelectOnFocus(true);

		// colunas
		ColumnConfig ccProdId = new ColumnConfig("", "comValorProdutoId", 10, false);
		ccProdId.setHidden(true);
		ccProdId.setFixed(true);

		ColumnConfig ccId = new ColumnConfig("", "comValorArredondaId", 10, false);
		ccId.setHidden(true);
		ccId.setFixed(true);

		ColumnConfig ccMin = new ColumnConfig(OpenSigCore.i18n.txtMinimo(), "comValorArredondaMin", 100, false, IListagem.NUMERO);
		ccMin.setEditor(new GridEditor(txtMin));

		ColumnConfig ccMax = new ColumnConfig(OpenSigCore.i18n.txtMaximo(), "comValorArredondaMax", 100, false, IListagem.NUMERO);
		ccMax.setEditor(new GridEditor(txtMax));

		ColumnConfig ccFixo = new ColumnConfig(OpenSigCore.i18n.txtFixo(), "comValorArredondaFixo", 100, false, IListagem.NUMERO);
		ccFixo.setEditor(new GridEditor(txtFixo));

		BaseColumnConfig[] bcc = new BaseColumnConfig[] { ccProdId, ccId, ccMin, ccMax, ccFixo };
		modelos = new ColumnModel(bcc);

		filtroPadrao = new FiltroNumero("comValorProdutoId", ECompara.IGUAL, 0);
		setTitle(OpenSigCore.i18n.txtArredondamento(), "icon-valor");
		setHeight(Ext.getBody().getHeight() - 370);
		super.inicializar();
	}

	public boolean validar(List<ComValorArredonda> lista) {
		boolean valida = true;
		Record[] recs = getStore().getRecords();

		for (Record rec : recs) {
			try {
				double min = rec.getAsDouble("comValorArredondaMin");
				double max = rec.getAsDouble("comValorArredondaMax");
				double fixo = rec.getAsDouble("comValorArredondaFixo");

				if (!(min > 0.00 && max > 0.00 && fixo > 0.00)) {
					throw new Exception();
				}

				ComValorArredonda comArredonda = new ComValorArredonda();
				comArredonda.setComValorArredondaMin(min);
				comArredonda.setComValorArredondaMax(max);
				comArredonda.setComValorArredondaFixo(fixo);
				lista.add(comArredonda);
			} catch (Exception ex) {
				valida = false;
				break;
			}
		}

		return valida;
	}

	public NumberField getTxtMin() {
		return txtMin;
	}

	public void setTxtMin(NumberField txtMin) {
		this.txtMin = txtMin;
	}

	public NumberField getTxtMax() {
		return txtMax;
	}

	public void setTxtMax(NumberField txtMax) {
		this.txtMax = txtMax;
	}

	public NumberField getTxtFixo() {
		return txtFixo;
	}

	public void setTxtFixo(NumberField txtFixo) {
		this.txtFixo = txtFixo;
	}

}
