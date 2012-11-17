package br.com.opensig.comercial.client.visao.lista;

import java.util.List;

import br.com.opensig.comercial.shared.modelo.ComEcfZTotais;
import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.FiltroNumero;
import br.com.opensig.core.client.visao.abstrato.AListagemEditor;
import br.com.opensig.core.client.visao.abstrato.IListagem;

import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.FloatFieldDef;
import com.gwtext.client.data.IntegerFieldDef;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.form.NumberField;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.grid.BaseColumnConfig;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridEditor;

public class ListagemEcfZTotais extends AListagemEditor<ComEcfZTotais> {

	private TextField txtCodigo;
	private NumberField txtValor;

	public ListagemEcfZTotais(boolean barraTarefa) {
		super(new ComEcfZTotais(), barraTarefa);
		inicializar();
	}

	public void inicializar() {
		// campos
		FieldDef[] fd = new FieldDef[] { new IntegerFieldDef("comEcfZId"), new IntegerFieldDef("comEcfZTotaisId"), new StringFieldDef("comEcfZTotaisCodigo"), new FloatFieldDef("comEcfZTotaisValor") };
		campos = new RecordDef(fd);

		// editores
		txtCodigo = new TextField();
		txtCodigo.setAllowBlank(false);
		txtCodigo.setMaxLength(7);
		txtCodigo.setSelectOnFocus(true);

		txtValor = new NumberField();
		txtValor.setAllowBlank(false);
		txtValor.setAllowDecimals(true);
		txtValor.setAllowNegative(false);
		txtValor.setDecimalPrecision(2);
		txtValor.setMaxLength(15);
		txtValor.setSelectOnFocus(true);

		// colunas
		ColumnConfig ccZid = new ColumnConfig("", "comEcfZId", 10, false);
		ccZid.setHidden(true);
		ccZid.setFixed(true);

		ColumnConfig ccTotalId = new ColumnConfig("", "comEcfZTotaisId", 10, false);
		ccTotalId.setHidden(true);
		ccTotalId.setFixed(true);

		ColumnConfig ccCodigo = new ColumnConfig(OpenSigCore.i18n.txtCodigo(), "comEcfZTotaisCodigo", 100, false);
		ccCodigo.setEditor(new GridEditor(txtCodigo));

		ColumnConfig ccValor = new ColumnConfig(OpenSigCore.i18n.txtValor(), "comEcfZTotaisValor", 100, false, IListagem.DINHEIRO);
		ccValor.setEditor(new GridEditor(txtValor));

		BaseColumnConfig[] bcc = new BaseColumnConfig[] { ccZid, ccTotalId, ccCodigo, ccValor };
		modelos = new ColumnModel(bcc);

		filtroPadrao = new FiltroNumero("comEcfZTotaisId", ECompara.IGUAL, 0);
		setTitle(OpenSigCore.i18n.txtTotal(), "icon-preco");
		setHeight(200);
		super.inicializar();
	}

	public boolean validar(List<ComEcfZTotais> lista) {
		boolean valida = true;
		Record[] recs = getStore().getRecords();

		for (Record rec : recs) {
			try {
				String codigo = rec.getAsString("comEcfZTotaisCodigo");
				double valor = rec.getAsDouble("comEcfZTotaisValor");

				if (codigo.equals("") || valor < 0.01) {
					throw new Exception();
				}

				ComEcfZTotais zTotal = new ComEcfZTotais();
				zTotal.setComEcfZTotaisCodigo(codigo);
				zTotal.setComEcfZTotaisValor(valor);
				lista.add(zTotal);
			} catch (Exception ex) {
				valida = false;
				break;
			}
		}

		return valida;
	}

	public TextField getTxtCodigo() {
		return txtCodigo;
	}

	public void setTxtCodigo(TextField txtCodigo) {
		this.txtCodigo = txtCodigo;
	}

	public NumberField getTxtValor() {
		return txtValor;
	}

	public void setTxtValor(NumberField txtValor) {
		this.txtValor = txtValor;
	}

}
