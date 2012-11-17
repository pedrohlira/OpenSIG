package br.com.opensig.empresa.client.visao.lista;

import java.util.Date;
import java.util.List;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.FiltroNumero;
import br.com.opensig.core.client.visao.abstrato.AListagemEditor;
import br.com.opensig.core.client.visao.abstrato.IListagem;
import br.com.opensig.empresa.shared.modelo.EmpPlano;

import com.gwtext.client.data.BooleanFieldDef;
import com.gwtext.client.data.DateFieldDef;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.IntegerFieldDef;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.widgets.form.Checkbox;
import com.gwtext.client.widgets.form.DateField;
import com.gwtext.client.widgets.form.NumberField;
import com.gwtext.client.widgets.grid.BaseColumnConfig;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridEditor;

public class ListagemPlano extends AListagemEditor<EmpPlano> {

	private DateField txtInicio;
	private DateField txtFim;
	private NumberField txtLimite;
	private Checkbox chkExcedente;

	public ListagemPlano(boolean barraTarefa) {
		super(new EmpPlano(), barraTarefa);
		inicializar();
	}

	public void inicializar() {
		// campos
		FieldDef[] fd = new FieldDef[] { new IntegerFieldDef("empPlanoId"), new IntegerFieldDef("empEmpresaId"), new DateFieldDef("empPlanoInicio"), new DateFieldDef("empPlanoFim"),
				new IntegerFieldDef("empPlanoLimite"), new BooleanFieldDef("empPlanoExcedente") };
		campos = new RecordDef(fd);

		// colunas
		ColumnConfig ccId = new ColumnConfig(OpenSigCore.i18n.txtCod(), "empPlanoId", 10, false);
		ccId.setHidden(true);
		ccId.setFixed(true);

		ColumnConfig ccEmpresa = new ColumnConfig(OpenSigCore.i18n.txtCod(), "empEmpresaId", 10, false);
		ccEmpresa.setHidden(true);
		ccEmpresa.setFixed(true);

		txtInicio = new DateField();
		txtInicio.setAllowBlank(false);
		txtInicio.setSelectOnFocus(true);
		ColumnConfig ccInicio = new ColumnConfig(OpenSigCore.i18n.txtInicio(), "empPlanoInicio", 75, false, IListagem.DATA);
		ccInicio.setEditor(new GridEditor(txtInicio));

		txtFim = new DateField();
		txtFim.setSelectOnFocus(true);
		ColumnConfig ccFim = new ColumnConfig(OpenSigCore.i18n.txtFim(), "empPlanoFim", 75, false, IListagem.DATA);
		ccFim.setEditor(new GridEditor(txtFim));

		txtLimite = new NumberField();
		txtLimite.setAllowBlank(false);
		txtLimite.setAllowDecimals(false);
		txtLimite.setAllowNegative(false);
		ColumnConfig ccLimite = new ColumnConfig(OpenSigCore.i18n.txtLimite(), "empPlanoLimite", 75, false, IListagem.NUMERO);
		ccLimite.setEditor(new GridEditor(txtLimite));
		
		chkExcedente = new Checkbox();
		ColumnConfig ccExcedente = new ColumnConfig(OpenSigCore.i18n.txtExcedente(), "empPlanoExcedente", 75, false, IListagem.BOLEANO);
		ccExcedente.setEditor(new GridEditor(chkExcedente));

		BaseColumnConfig[] bcc = new BaseColumnConfig[] { ccId, ccEmpresa, ccInicio, ccFim, ccLimite, ccExcedente };
		modelos = new ColumnModel(bcc);

		filtroPadrao = new FiltroNumero("empEmpresaId", ECompara.IGUAL, 0);
		setTitle(OpenSigCore.i18n.txtPlano(), "icon-padrao");
		super.inicializar();
	}

	public boolean validar(List<EmpPlano> lista) {
		boolean valida = true;
		Record[] recs = getStore().getRecords();

		for (Record rec : recs) {
			try {
				Date inicio = rec.getAsDate("empPlanoInicio");
				Date fim = rec.getAsDate("empPlanoFim");
				int limite = rec.getAsInteger("empPlanoLimite");
				boolean excedente = rec.getAsBoolean("empPlanoExcedente");

				if (inicio == null || limite < 0) {
					throw new Exception();
				}

				EmpPlano plano = new EmpPlano();
				plano.setEmpPlanoInicio(inicio);
				plano.setEmpPlanoFim(fim);
				plano.setEmpPlanoLimite(limite);
				plano.setEmpPlanoExcedente(excedente);
				lista.add(plano);
			} catch (Exception ex) {
				valida = false;
				break;
			}
		}

		return valida && recs.length == 1;
	}

	public DateField getTxtInicio() {
		return txtInicio;
	}

	public void setTxtInicio(DateField txtInicio) {
		this.txtInicio = txtInicio;
	}

	public DateField getTxtFim() {
		return txtFim;
	}

	public void setTxtFim(DateField txtFim) {
		this.txtFim = txtFim;
	}

	public NumberField getTxtLimite() {
		return txtLimite;
	}

	public void setTxtLimite(NumberField txtLimite) {
		this.txtLimite = txtLimite;
	}

	public Checkbox getChkExcedente() {
		return chkExcedente;
	}

	public void setChkExcedente(Checkbox chkExcedente) {
		this.chkExcedente = chkExcedente;
	}

}
