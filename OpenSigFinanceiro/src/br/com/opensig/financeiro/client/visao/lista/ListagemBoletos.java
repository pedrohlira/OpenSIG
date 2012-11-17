package br.com.opensig.financeiro.client.visao.lista;

import java.util.Date;
import java.util.List;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.FiltroNumero;
import br.com.opensig.core.client.visao.abstrato.AListagemEditor;
import br.com.opensig.core.client.visao.abstrato.IListagem;
import br.com.opensig.financeiro.shared.modelo.FinConta;
import br.com.opensig.financeiro.shared.modelo.FinReceber;
import br.com.opensig.financeiro.shared.modelo.FinRecebimento;

import com.google.gwt.core.client.GWT;
import com.gwtext.client.data.DateFieldDef;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.FloatFieldDef;
import com.gwtext.client.data.IntegerFieldDef;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.form.DateField;
import com.gwtext.client.widgets.form.NumberField;
import com.gwtext.client.widgets.grid.BaseColumnConfig;
import com.gwtext.client.widgets.grid.CellMetadata;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridEditor;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.Renderer;
import com.gwtext.client.widgets.grid.event.EditorGridListenerAdapter;
import com.gwtextux.client.widgets.grid.plugins.GridSummaryPlugin;
import com.gwtextux.client.widgets.grid.plugins.SummaryColumnConfig;

public class ListagemBoletos extends AListagemEditor<FinRecebimento> {

	private NumberField txtId;
	private NumberField txtValor;
	private DateField dtRealizado;
	private DateField dtConciliado;

	public ListagemBoletos() {
		super(new FinRecebimento(), false);
		inicializar();
		addPlugin(new GridSummaryPlugin());
	}

	public void inicializar() {
		// campos
		FieldDef[] fd = new FieldDef[] { new IntegerFieldDef("id"), new StringFieldDef("nome"), new StringFieldDef("documento"), new FloatFieldDef("valor"), new StringFieldDef("parcela"),
				new DateFieldDef("vencimento"), new StringFieldDef("status"), new DateFieldDef("realizado"), new DateFieldDef("conciliado"), new StringFieldDef("situacao"), new StringFieldDef("Observacao"),  new IntegerFieldDef("contaId") };
		campos = new RecordDef(fd);
		
		// editores
		txtId = new NumberField();
		txtId.setAllowBlank(false);
		txtId.setAllowDecimals(false);
		txtId.setAllowNegative(false);
		txtId.setSelectOnFocus(true);
		txtId.setMinValue(1);

		txtValor = new NumberField();
		txtValor.setAllowBlank(false);
		txtValor.setAllowNegative(false);
		txtValor.setSelectOnFocus(true);
		txtValor.setDecimalPrecision(2);
		txtValor.setMaxLength(13);

		dtRealizado = new DateField();
		dtRealizado.setAllowBlank(false);
		
		dtConciliado = new DateField();
		dtConciliado.setAllowBlank(false);

		// colunas
		ColumnConfig ccId = new ColumnConfig(OpenSigCore.i18n.txtCod(), "id", 75, true);
		ccId.setEditor(new GridEditor(txtId));

		ColumnConfig ccNome = new ColumnConfig(OpenSigCore.i18n.txtNome(), "nome", 200, true);

		ColumnConfig ccValor = new ColumnConfig(OpenSigCore.i18n.txtValor(), "valor", 75, true, IListagem.DINHEIRO);
		ccValor.setEditor(new GridEditor(txtValor));

		ColumnConfig ccDocumento = new ColumnConfig(OpenSigCore.i18n.txtDocumento(), "documento", 75, true);

		ColumnConfig ccParcela = new ColumnConfig(OpenSigCore.i18n.txtParcela(), "parcela", 50, true);

		ColumnConfig ccVencimento = new ColumnConfig(OpenSigCore.i18n.txtVencimento(), "vencimento", 75, true, IListagem.DATA);

		ColumnConfig ccStatus = new ColumnConfig(OpenSigCore.i18n.txtStatus(), "status", 100, true);

		ColumnConfig ccRealizado = new ColumnConfig(OpenSigCore.i18n.txtRealizado(), "realizado", 75, true, IListagem.DATA);
		ccRealizado.setEditor(new GridEditor(dtRealizado));
		
		ColumnConfig ccConciliado = new ColumnConfig(OpenSigCore.i18n.txtConciliado(), "conciliado", 75, true, IListagem.DATA);
		ccConciliado.setEditor(new GridEditor(dtConciliado));

		ColumnConfig ccSituacao = new ColumnConfig(OpenSigCore.i18n.txtSituacao(), "situacao", 50, false, new Renderer() {

			public String render(Object value, CellMetadata cellMetadata, Record record, int rowIndex, int colNum, Store store) {
				cellMetadata.setCssClass("colCentro");
				if (value != null && value.toString().equals("1")) {
					return "<img src='" + GWT.getModuleBaseURL() + "img/salvar.png' />";
				} else if (value != null && value.toString().equals("0")) {
					return "<img src='" + GWT.getModuleBaseURL() + "img/atencao.png' />";
				} else {
					return "<img src='" + GWT.getModuleBaseURL() + "img/cancelar.png' />";
				}
			}
		});

		ColumnConfig ccObservacao = new ColumnConfig(OpenSigCore.i18n.txtObservacao(), "Observacao", 300, true);

		// sumarios
		SummaryColumnConfig contCod = new SummaryColumnConfig(SummaryColumnConfig.COUNT, ccId, IListagem.CONTADOR);
		SummaryColumnConfig sumValor = new SummaryColumnConfig(SummaryColumnConfig.SUM, ccValor, IListagem.DINHEIRO);

		BaseColumnConfig[] bcc = new BaseColumnConfig[] { contCod, ccNome, sumValor, ccDocumento, ccParcela, ccVencimento, ccStatus, ccRealizado, ccConciliado, ccSituacao, ccObservacao };
		modelos = new ColumnModel(bcc);

		addEditorGridListener(new EditorGridListenerAdapter() {
			public boolean doBeforeEdit(GridPanel grid, Record record, String field, Object value, int rowIndex, int colIndex) {
				if (!record.getAsString("status").equalsIgnoreCase(OpenSigCore.i18n.txtAberto())) {
					MessageBox.alert(OpenSigCore.i18n.txtAcesso(), OpenSigCore.i18n.txtAcessoNegado());
					return false;
				} else {
					return true;
				}
			}

			public void onAfterEdit(GridPanel grid, Record record, String field, Object newValue, Object oldValue, int rowIndex, int colIndex) {
				if (field.equals("id")) {
					record.set("situacao", "1");
				}
			}
		});

		filtroPadrao = new FiltroNumero("finRecebimentoId", ECompara.IGUAL, 0);
		super.inicializar();
		
		// grid
		setTitle(OpenSigCore.i18n.txtBoleto(), "icon-boleto");
		setHeight(500);
		setWidth("100%");
	}

	public boolean validar(List<FinRecebimento> lista) {
		boolean valida = true;
		Record[] recs = getStore().getRecords();

		for (Record rec : recs) {
			try {
				int id = rec.getAsInteger("id");
				double valor = rec.getAsDouble("valor");
				Date realizado = rec.getAsDate("realizado");
				Date conciliado = rec.getAsDate("conciliado");
				String situacao = rec.getAsString("situacao");
				int contaId = rec.getAsInteger("contaId");

				if (situacao.equals("1")) {
					FinRecebimento fin = new FinRecebimento(id);
					fin.setFinRecebimentoValor(valor);
					fin.setFinRecebimentoRealizado(realizado);
					fin.setFinRecebimentoConciliado(conciliado);
					
					FinReceber receber = new FinReceber();
					FinConta conta = new FinConta(contaId);
					receber.setFinConta(conta);
					fin.setFinReceber(receber);
					
					lista.add(fin);
				}
			} catch (Exception ex) {
				valida = false;
				int row = getStore().indexOf(rec);
				getView().getRow(row).getStyle().setColor("red");
			}
		}

		return !lista.isEmpty() && valida;
	}

	public NumberField getTxtId() {
		return txtId;
	}

	public void setTxtId(NumberField txtId) {
		this.txtId = txtId;
	}

	public NumberField getTxtValor() {
		return txtValor;
	}

	public void setTxtValor(NumberField txtValor) {
		this.txtValor = txtValor;
	}

	public DateField getDtRealizado() {
		return dtRealizado;
	}

	public void setDtRealizado(DateField dtRealizado) {
		this.dtRealizado = dtRealizado;
	}
	
	public DateField getDtConciliado() {
		return dtConciliado;
	}
	
	public void setDtConciliado(DateField dtConciliado) {
		this.dtConciliado = dtConciliado;
	}
}
