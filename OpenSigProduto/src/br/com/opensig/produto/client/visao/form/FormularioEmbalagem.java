package br.com.opensig.produto.client.visao.form;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.visao.abstrato.AFormulario;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.produto.shared.modelo.ProdEmbalagem;

import com.gwtext.client.data.Record;
import com.gwtext.client.widgets.form.Hidden;
import com.gwtext.client.widgets.form.MultiFieldPanel;
import com.gwtext.client.widgets.form.NumberField;
import com.gwtext.client.widgets.form.TextField;

public class FormularioEmbalagem extends AFormulario<ProdEmbalagem> {

	private Hidden hdnCod;
	private TextField txtNome;
	private NumberField txtUnidade;
	private TextField txtDescricao;

	public FormularioEmbalagem(SisFuncao funcao) {
		super(new ProdEmbalagem(), funcao);
		inicializar();
	}

	public void inicializar() {
		super.inicializar();

		hdnCod = new Hidden("prodEmbalagemId", "0");
		add(hdnCod);

		txtNome = new TextField(OpenSigCore.i18n.txtNome(), "prodEmbalagemNome", 100);
		txtNome.setAllowBlank(false);
		txtNome.setMaxLength(6);

		txtUnidade = new NumberField(OpenSigCore.i18n.txtUnidade(), "prodEmbalagemUnidade", 80);
		txtUnidade.setAllowBlank(false);
		txtUnidade.setAllowDecimals(false);
		txtUnidade.setAllowNegative(false);
		txtUnidade.setMinValue(1);

		txtDescricao = new TextField(OpenSigCore.i18n.txtDescricao(), "prodEmbalagemDescricao", 400);
		txtDescricao.setAllowBlank(false);
		txtDescricao.setMaxLength(100);
		
		MultiFieldPanel linha1 = new MultiFieldPanel();
		linha1.setBorder(false);
		linha1.addToRow(txtNome, 120);
		linha1.addToRow(txtUnidade, 100);
		linha1.addToRow(txtDescricao, 420);
		add(linha1);
	}

	public boolean setDados() {
		classe.setProdEmbalagemId(Integer.valueOf(hdnCod.getValueAsString()));
		classe.setProdEmbalagemNome(txtNome.getValueAsString());
		if (txtUnidade.getValue() != null) {
			classe.setProdEmbalagemUnidade(txtUnidade.getValue().intValue());
		}
		classe.setProdEmbalagemDescricao(txtDescricao.getValueAsString());
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

		if (duplicar) {
			hdnCod.setValue("0");
			duplicar = false;
		}
	}

	public void gerarListas() {
	}

	public Hidden getHdnCod() {
		return hdnCod;
	}

	public void setHdnCod(Hidden hdnCod) {
		this.hdnCod = hdnCod;
	}

	public TextField getTxtNome() {
		return txtNome;
	}

	public void setTxtNome(TextField txtNome) {
		this.txtNome = txtNome;
	}

	public NumberField getTxtUnidade() {
		return txtUnidade;
	}

	public void setTxtUnidade(NumberField txtUnidade) {
		this.txtUnidade = txtUnidade;
	}

	public TextField getTxtDescricao() {
		return txtDescricao;
	}

	public void setTxtDescricao(TextField txtDescricao) {
		this.txtDescricao = txtDescricao;
	}

}
