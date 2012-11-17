package br.com.opensig.core.client.visao.form;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.EJuncao;
import br.com.opensig.core.client.controlador.filtro.FiltroBinario;
import br.com.opensig.core.client.controlador.filtro.FiltroTexto;
import br.com.opensig.core.client.controlador.filtro.GrupoFiltro;
import br.com.opensig.core.client.controlador.filtro.IFiltro;
import br.com.opensig.core.client.servico.CoreProxy;
import br.com.opensig.core.client.visao.abstrato.AFormulario;
import br.com.opensig.core.shared.modelo.sistema.SisExpImp;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;

import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.IntegerFieldDef;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.SimpleStore;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.Hidden;
import com.gwtext.client.widgets.form.MultiFieldPanel;
import com.gwtext.client.widgets.form.TextArea;
import com.gwtext.client.widgets.form.TextField;

public class FormularioExpImp extends AFormulario<SisExpImp> {

	private Hidden hdnCod;
	private TextField txtNome;
	private TextField txtEntensao;
	private TextField txtDescricao;
	private TextField txtImagem;
	private TextField txtClasse;
	private ComboBox cmbFuncao;
	private ComboBox cmbTipo;
	private TextArea txtModelo;

	public FormularioExpImp(SisFuncao funcao) {
		super(new SisExpImp(), funcao);
		inicializar();
	}

	public void inicializar() {
		super.inicializar();

		hdnCod = new Hidden("sisExpImpId", "0");
		add(hdnCod);

		txtNome = new TextField(OpenSigCore.i18n.txtNome(), "sisExpImpNome", 100);
		txtNome.setAllowBlank(false);
		txtNome.setMaxLength(10);

		txtEntensao = new TextField(OpenSigCore.i18n.txtExtensao(), "sisExpImpExtensoes", 250);
		txtEntensao.setMaxLength(100);

		txtDescricao = new TextField(OpenSigCore.i18n.txtDescricao(), "sisExpImpDescricao", 250);
		txtDescricao.setAllowBlank(false);
		txtDescricao.setMaxLength(100);

		txtImagem = new TextField(OpenSigCore.i18n.txtImagem(), "sisExpImpImagem", 100);
		txtImagem.setAllowBlank(false);
		txtImagem.setMaxLength(20);

		MultiFieldPanel linha1 = new MultiFieldPanel();
		linha1.setBorder(false);
		linha1.addToRow(txtNome, 120);
		linha1.addToRow(txtEntensao, 270);
		linha1.addToRow(txtDescricao, 270);
		linha1.addToRow(txtImagem, 120);
		add(linha1);

		txtClasse = new TextField(OpenSigCore.i18n.txtClasse(), "sisExpImpClasse", 280);
		txtClasse.setAllowBlank(false);
		txtClasse.setMaxLength(255);

		MultiFieldPanel linha2 = new MultiFieldPanel();
		linha2.setBorder(false);

		linha2.addToRow(txtClasse, 300);
		linha2.addToRow(getExpFuncao(), 300);
		linha2.addToRow(getTipo(), 140);
		add(linha2);

		txtModelo = new TextArea(OpenSigCore.i18n.txtModelo(), "sisExpImpModelo");
		txtModelo.setWidth("95%");
		txtModelo.setHeight(400);
		add(txtModelo);
	}

	public boolean setDados() {
		classe.setSisExpImpId(Integer.valueOf(hdnCod.getValueAsString()));
		classe.setSisExpImpNome(txtNome.getValueAsString());
		classe.setSisExpImpExtensoes(txtEntensao.getValueAsString());
		classe.setSisExpImpDescricao(txtDescricao.getValueAsString());
		classe.setSisExpImpImagem(txtImagem.getValueAsString());
		classe.setSisExpImpClasse(txtClasse.getValueAsString());
		classe.setSisExpImpFuncao(cmbFuncao.getValue());
		classe.setSisExpImpTipo(cmbTipo.getValue());
		classe.setSisExpImpModelo(txtModelo.getValueAsString());

		return true;
	}

	public void limparDados() {
		getForm().reset();
	}

	public void mostrarDados() {
		Record rec = lista.getPanel().getSelectionModel().getSelected();
		if (rec != null) {
			getForm().loadRecord(rec);
		}
		txtNome.focus(true);

		if (duplicar) {
			hdnCod.setValue("0");
			duplicar = false;
		}
	}

	public void gerarListas() {
	}

	private ComboBox getTipo() {
		Store store = new SimpleStore(new String[] { "id", "valor" }, new String[][] { new String[] { "E", OpenSigCore.i18n.txtExportar() }, new String[] { "I", OpenSigCore.i18n.txtImportar() } });
		store.load();

		cmbTipo = new ComboBox(OpenSigCore.i18n.txtTipo(), "sisExpImpTipo", 120);
		cmbTipo.setForceSelection(true);
		cmbTipo.setListWidth(120);
		cmbTipo.setEditable(false);
		cmbTipo.setStore(store);
		cmbTipo.setDisplayField("valor");
		cmbTipo.setValueField("id");
		cmbTipo.setMode(ComboBox.LOCAL);
		cmbTipo.setTriggerAction(ComboBox.ALL);
		cmbTipo.setAllowBlank(false);

		return cmbTipo;
	}

	private ComboBox getExpFuncao() {
		FieldDef[] fdFuncao = new FieldDef[] { new IntegerFieldDef("sisFuncaoId"), new StringFieldDef("sisFuncaoClasse") };
		FiltroBinario fb = new FiltroBinario("sisFuncaoAtivo", ECompara.IGUAL, 1);
		FiltroTexto ft = new FiltroTexto("sisFuncaoClasse", ECompara.DIFERENTE, "Separador");
		GrupoFiltro gf = new GrupoFiltro(EJuncao.E, new IFiltro[] { fb, ft });
		CoreProxy<SisFuncao> proxy = new CoreProxy<SisFuncao>(new SisFuncao(), gf);
		Store store = new Store(proxy, new ArrayReader(new RecordDef(fdFuncao)), false);

		cmbFuncao = new ComboBox(OpenSigCore.i18n.txtFuncao(), "sisExpImpFuncao", 270);
		cmbFuncao.setMinChars(1);
		cmbFuncao.setListWidth(400);
		cmbFuncao.setAllowBlank(false);
		cmbFuncao.setStore(store);
		cmbFuncao.setTriggerAction(ComboBox.QUERY);
		cmbFuncao.setMode(ComboBox.REMOTE);
		cmbFuncao.setDisplayField("sisFuncaoClasse");
		cmbFuncao.setValueField("sisFuncaoClasse");
		cmbFuncao.setLoadingText(OpenSigCore.i18n.txtAguarde());
		cmbFuncao.setPageSize(10);
		cmbFuncao.setSelectOnFocus(true);
		cmbFuncao.setForceSelection(true);

		return cmbFuncao;
	}
}
