package br.com.opensig.produto.client.visao.lista;

import java.util.List;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.UtilClient;
import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.FiltroNumero;
import br.com.opensig.core.client.servico.CoreProxy;
import br.com.opensig.core.client.visao.abstrato.AListagemEditor;
import br.com.opensig.core.client.visao.abstrato.IListagem;
import br.com.opensig.produto.shared.modelo.ProdEmbalagem;
import br.com.opensig.produto.shared.modelo.ProdPreco;

import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.FloatFieldDef;
import com.gwtext.client.data.IntegerFieldDef;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.NumberField;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.grid.BaseColumnConfig;
import com.gwtext.client.widgets.grid.CellMetadata;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridEditor;
import com.gwtext.client.widgets.grid.Renderer;

public class ListagemPreco extends AListagemEditor<ProdPreco> {

	private Store storeEmbalagem;
	private NumberField txtValor;
	private TextField txtBarra;
	private RecordDef camposTabela;

	public ListagemPreco(boolean barraTarefa) {
		super(new ProdPreco(), barraTarefa);
		inicializar();
	}

	public void inicializar() {
		// campos
		FieldDef[] fd = new FieldDef[] { new IntegerFieldDef("prodPrecoId"), new IntegerFieldDef("prodEmbalagem.prodEmbalagemId"), new StringFieldDef("prodEmbalagem.prodEmbalagemNome"),
				new FloatFieldDef("prodPrecoValor"), new StringFieldDef("prodPrecoBarra") };
		campos = new RecordDef(fd);

		// editores
		FieldDef[] fdEmbalagem = new FieldDef[] { new IntegerFieldDef("prodEmbalagemId"), new StringFieldDef("prodEmbalagemNome"), new IntegerFieldDef("prodEmbalagemUnidade"),
				new StringFieldDef("prodEmbalagemDescricao") };
		CoreProxy<ProdEmbalagem> proxy = new CoreProxy<ProdEmbalagem>(new ProdEmbalagem());
		storeEmbalagem = new Store(proxy, new ArrayReader(new RecordDef(fdEmbalagem)), true);
		storeEmbalagem.load();

		// colunas
		ColumnConfig ccId = new ColumnConfig("", "prodPrecoId", 10, false);
		ccId.setHidden(true);
		ccId.setFixed(true);

		ColumnConfig ccEmbalagemId = new ColumnConfig(OpenSigCore.i18n.txtEmbalagem(), "prodEmbalagem.prodEmbalagemId", 100, false, new Renderer() {
			public String render(Object value, CellMetadata cellMetadata, Record record, int rowIndex, int colNum, Store store) {
				if (value != null) {
					Record reg = UtilClient.getRegistro(storeEmbalagem, "prodEmbalagemId", value.toString());
					return reg.getAsString("prodEmbalagemNome");
				} else {
					return "";
				}
			}
		});
		ccEmbalagemId.setEditor(new GridEditor(getEmbalagem()));

		ColumnConfig ccEmbalagem = new ColumnConfig("", "prodEmbalagem.prodEmbalagemNome", 10, false);
		ccEmbalagem.setHidden(true);
		ccEmbalagem.setFixed(true);

		txtValor = new NumberField();
		txtValor.setAllowBlank(false);
		txtValor.setAllowDecimals(true);
		txtValor.setAllowNegative(false);
		txtValor.setDecimalPrecision(2);
		txtValor.setMaxLength(13);
		txtValor.setSelectOnFocus(true);
		ColumnConfig ccPreco = new ColumnConfig(OpenSigCore.i18n.txtPreco(), "prodPrecoValor", 100, false, IListagem.DINHEIRO);
		ccPreco.setEditor(new GridEditor(txtValor));

		txtBarra = new TextField();
		txtBarra.setMinLength(8);
		txtBarra.setMaxLength(14);
		txtBarra.setRegex("^(\\d{8}|\\d{12}|\\d{13}|\\d{14})$");
		txtBarra.setSelectOnFocus(true);
		ColumnConfig ccBarra = new ColumnConfig(OpenSigCore.i18n.txtBarra(), "prodPrecoBarra", 100, false);
		ccBarra.setEditor(new GridEditor(txtBarra));

		BaseColumnConfig[] bcc = new BaseColumnConfig[] { ccId, ccEmbalagemId, ccEmbalagem, ccPreco, ccBarra };
		modelos = new ColumnModel(bcc);

		// barra de menu
		filtroPadrao = new FiltroNumero("prodPrecoId", ECompara.IGUAL, 0);

		// configurações padrão e carrega dados
		setTitle(OpenSigCore.i18n.txtPreco(), "icon-preco");
		super.inicializar();
	}

	public boolean validar(List<ProdPreco> lista) {
		boolean valida = true;
		Record[] recs = getStore().getRecords();

		for (Record rec : recs) {
			try {
				int embalagemId = rec.getAsInteger("prodEmbalagem.prodEmbalagemId");
				double valor = rec.getAsDouble("prodPrecoValor");
				String barra = rec.getAsString("prodPrecoBarra");

				if (embalagemId < 1 || valor < 0.01 || (barra != null && barra.length() != 8 && barra.length() != 12 && barra.length() != 13 && barra.length() != 14)) {
					throw new Exception();
				}

				ProdPreco preco = new ProdPreco();
				preco.setProdEmbalagem(new ProdEmbalagem(embalagemId));
				preco.setProdPrecoValor(valor);
				preco.setProdPrecoBarra(barra);
				lista.add(preco);
			} catch (Exception ex) {
				valida = false;
				break;
			}
		}

		return valida;
	}

	private ComboBox getEmbalagem() {
		ComboBox cmbEmbalagem = new ComboBox(OpenSigCore.i18n.txtEmbalagem(), "prodEmbalagem.prodEmbalagemId", 60);
		cmbEmbalagem.setAllowBlank(false);
		cmbEmbalagem.setStore(storeEmbalagem);
		cmbEmbalagem.setTriggerAction(ComboBox.ALL);
		cmbEmbalagem.setMode(ComboBox.LOCAL);
		cmbEmbalagem.setDisplayField("prodEmbalagemNome");
		cmbEmbalagem.setValueField("prodEmbalagemId");
		cmbEmbalagem.setTpl("<div class=\"x-combo-list-item\"><b>{prodEmbalagemNome}</b> - <i>" + OpenSigCore.i18n.txtUnidade() + " [{prodEmbalagemUnidade}]</i></div>");
		cmbEmbalagem.setForceSelection(true);
		cmbEmbalagem.setListWidth(150);
		cmbEmbalagem.setEditable(false);
		return cmbEmbalagem;
	}

	public Store getStoreEmbalagem() {
		return storeEmbalagem;
	}

	public void setStoreEmbalagem(Store storeEmbalagem) {
		this.storeEmbalagem = storeEmbalagem;
	}

	public NumberField getTxtValor() {
		return txtValor;
	}

	public void setTxtValor(NumberField txtValor) {
		this.txtValor = txtValor;
	}

	public TextField getTxtBarra() {
		return txtBarra;
	}

	public void setTxtBarra(TextField txtBarra) {
		this.txtBarra = txtBarra;
	}

	public RecordDef getCamposTabela() {
		return camposTabela;
	}

	public void setCamposTabela(RecordDef camposTabela) {
		this.camposTabela = camposTabela;
	}

}
