package br.com.opensig.produto.client.visao.lista;

import java.util.ArrayList;
import java.util.List;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.FiltroNumero;
import br.com.opensig.core.client.controlador.filtro.FiltroTexto;
import br.com.opensig.core.client.servico.CoreProxy;
import br.com.opensig.core.client.visao.Ponte;
import br.com.opensig.core.client.visao.abstrato.AListagemEditor;
import br.com.opensig.core.client.visao.abstrato.IListagem;
import br.com.opensig.empresa.shared.modelo.EmpEmpresa;
import br.com.opensig.produto.shared.modelo.ProdEstoqueGrade;
import br.com.opensig.produto.shared.modelo.ProdGrade;
import br.com.opensig.produto.shared.modelo.ProdGradeTipo;

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
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridEditor;

public class ListagemGrades extends AListagemEditor<ProdGrade> {

	private TextField txtBarra;
	private NumberField txtEstoque;

	public ListagemGrades(boolean barraTarefa) {
		super(new ProdGrade(), barraTarefa);
		inicializar();
	}

	public void inicializar() {
		// campos
		FieldDef[] fd = new FieldDef[] { new IntegerFieldDef("prodGradeId"), new StringFieldDef("prodGradeBarra"), new StringFieldDef("prodGradeTamanho"), new StringFieldDef("prodGradeCor"),
				new StringFieldDef("prodGradeOpcao"), new FloatFieldDef("prodGradeEstoque") };
		campos = new RecordDef(fd);

		// editores
		FieldDef[] fdSub = new FieldDef[] { new IntegerFieldDef("prodGradeTipoId"), new StringFieldDef("prodGradeTipoNome") };
		CoreProxy<ProdGradeTipo> proxy = new CoreProxy<ProdGradeTipo>(new ProdGradeTipo());

		// os subs de tamanho
		FiltroTexto ft1 = new FiltroTexto("prodGradeTipoOpcao", ECompara.IGUAL, "T");
		proxy.setFiltroPadrao(ft1);
		final Store stTamanho = new Store(proxy, new ArrayReader(new RecordDef(fdSub)), true);
		stTamanho.load();

		// os subs de cor
		FiltroTexto ft2 = new FiltroTexto("prodGradeTipoOpcao", ECompara.IGUAL, "C");
		proxy.setFiltroPadrao(ft2);
		final Store stCor = new Store(proxy, new ArrayReader(new RecordDef(fdSub)), true);
		stCor.load();

		// os subs de tipo
		FiltroTexto ft3 = new FiltroTexto("prodGradeTipoOpcao", ECompara.IGUAL, "O");
		proxy.setFiltroPadrao(ft3);
		final Store stTipo = new Store(proxy, new ArrayReader(new RecordDef(fdSub)), true);
		stTipo.load();

		// colunas
		ColumnConfig ccId = new ColumnConfig("", "prodGradeId", 10, false);
		ccId.setHidden(true);
		ccId.setFixed(true);

		txtBarra = new TextField();
		txtBarra.setAllowBlank(false);
		txtBarra.setMaxLength(14);
		txtBarra.setSelectOnFocus(true);
		ColumnConfig ccBarra = new ColumnConfig(OpenSigCore.i18n.txtBarra(), "prodGradeBarra", 100, false);
		ccBarra.setEditor(new GridEditor(txtBarra));

		ColumnConfig ccTamanho = new ColumnConfig(OpenSigCore.i18n.txtTamanho(), "prodGradeTamanho", 100, false);
		ccTamanho.setEditor(new GridEditor(getTipo(stTamanho)));

		ColumnConfig ccCor = new ColumnConfig(OpenSigCore.i18n.txtCor(), "prodGradeCor", 100, false);
		ccCor.setEditor(new GridEditor(getTipo(stCor)));

		ColumnConfig ccTipo = new ColumnConfig(OpenSigCore.i18n.txtOpcao(), "prodGradeOpcao", 100, false);
		ccTipo.setEditor(new GridEditor(getTipo(stTipo)));

		txtEstoque = new NumberField();
		txtEstoque.setAllowBlank(false);
		txtEstoque.setAllowNegative(false);
		txtEstoque.setMaxLength(10);
		txtEstoque.setSelectOnFocus(true);
		ColumnConfig ccEstoque = new ColumnConfig(OpenSigCore.i18n.txtEstoque(), "prodGradeEstoque", 100, false, IListagem.NUMERO);
		ccEstoque.setEditor(new GridEditor(txtEstoque));

		BaseColumnConfig[] bcc = new BaseColumnConfig[] { ccId, ccBarra, ccTamanho, ccCor, ccTipo, ccEstoque };
		modelos = new ColumnModel(bcc);

		// barra de menu
		filtroPadrao = new FiltroNumero("prodGradeId", ECompara.IGUAL, 0);

		// configurações padrão e carrega dados
		setTitle(OpenSigCore.i18n.txtGrade(), "icon-grade");
		super.inicializar();
	}

	public boolean validar(List<ProdGrade> lista) {
		boolean valida = true;
		Record[] recs = getStore().getRecords();

		for (Record rec : recs) {
			try {
				String barra = rec.getAsString("prodGradeBarra");
				String tamanho = rec.getAsString("prodGradeTamanho");
				String cor = rec.getAsString("prodGradeCor");
				String opcao = rec.getAsString("prodGradeOpcao");
				double estoque = rec.getAsDouble("prodGradeEstoque");

				if (barra.equals("") || tamanho == null || cor == null || opcao == null || estoque == Double.NaN) {
					throw new Exception();
				}

				ProdEstoqueGrade estGrade = new ProdEstoqueGrade();
				estGrade.setProdEstoqueGradeQuantidade(estoque);
				estGrade.setEmpEmpresa(new EmpEmpresa(Ponte.getLogin().getEmpresaId()));
				List<ProdEstoqueGrade> estoques = new ArrayList<ProdEstoqueGrade>();
				estoques.add(estGrade);

				ProdGrade grade = new ProdGrade();
				grade.setProdGradeBarra(barra);
				grade.setProdGradeTamanho(tamanho);
				grade.setProdGradeCor(cor);
				grade.setProdGradeOpcao(opcao);
				grade.setProdGradeEstoque(estoque);
				grade.setProdEstoqueGrades(estoques);
				lista.add(grade);
			} catch (Exception ex) {
				valida = false;
				break;
			}
		}

		return valida;
	}

	private ComboBox getTipo(Store store) {
		ComboBox cmbTipo = new ComboBox();
		cmbTipo.setAllowBlank(false);
		cmbTipo.setStore(store);
		cmbTipo.setTriggerAction(ComboBox.ALL);
		cmbTipo.setMode(ComboBox.LOCAL);
		cmbTipo.setDisplayField("prodGradeTipoNome");
		cmbTipo.setValueField("prodGradeTipoNome");
		cmbTipo.setForceSelection(true);
		cmbTipo.setListWidth(150);
		cmbTipo.setEditable(false);
		return cmbTipo;
	}

	public TextField getTxtBarra() {
		return txtBarra;
	}

	public void setTxtBarra(TextField txtBarra) {
		this.txtBarra = txtBarra;
	}

	public NumberField getTxtEstoque() {
		return txtEstoque;
	}

	public void setTxtEstoque(NumberField txtEstoque) {
		this.txtEstoque = txtEstoque;
	}

}
