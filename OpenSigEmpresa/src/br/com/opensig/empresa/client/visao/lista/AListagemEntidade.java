package br.com.opensig.empresa.client.visao.lista;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map.Entry;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.visao.abstrato.IFormulario;
import br.com.opensig.core.client.visao.abstrato.AListagem;
import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.core.shared.modelo.IFavorito;

import com.gwtext.client.data.BooleanFieldDef;
import com.gwtext.client.data.DateFieldDef;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.IntegerFieldDef;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.grid.BaseColumnConfig;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtextux.client.widgets.grid.plugins.GridBooleanFilter;
import com.gwtextux.client.widgets.grid.plugins.GridFilter;

public abstract class AListagemEntidade<E extends Dados> extends AListagem<E> {

	protected Collection<FieldDef> listaCampos = new ArrayList<FieldDef>();
	protected Collection<BaseColumnConfig> listaColunas = new ArrayList<BaseColumnConfig>();
	protected Collection<GridFilter> listaFiltros = new ArrayList<GridFilter>();
	protected ColumnConfig ccEntidade;

	public AListagemEntidade(IFormulario<E> formulario) {
		super(formulario);
	}

	protected void configurar() {
		// campos
		listaCampos.add(new IntegerFieldDef("empEntidade.empEntidadeId"));
		listaCampos.add(new StringFieldDef("empEntidade.empEntidadeNome1"));
		listaCampos.add(new StringFieldDef("empEntidade.empEntidadeNome2"));
		listaCampos.add(new StringFieldDef("empEntidade.empEntidadePessoa"));
		listaCampos.add(new StringFieldDef("empEntidade.empEntidadeDocumento1"));
		listaCampos.add(new StringFieldDef("empEntidade.empEntidadeDocumento2"));
		listaCampos.add(new StringFieldDef("empEntidade.empEntidadeDocumento3"));
		listaCampos.add(new BooleanFieldDef("empEntidade.empEntidadeAtivo"));
		listaCampos.add(new StringFieldDef("empEntidade.empEntidadeObservacao"));
		listaCampos.add(new DateFieldDef("empEntidade.empEntidadeData"));

		// colunas
		ccEntidade = new ColumnConfig(OpenSigCore.i18n.txtCod() + " - " + OpenSigCore.i18n.txtEntidade(), "empEntidade.empEntidadeId", 100, true);
		ccEntidade.setHidden(true);
		listaColunas.add(ccEntidade);
		ColumnConfig ccNome1 = new ColumnConfig(OpenSigCore.i18n.txtEntidadeNome1(), "empEntidade.empEntidadeNome1", 200, true);
		listaColunas.add(ccNome1);
		ColumnConfig ccNome2 = new ColumnConfig(OpenSigCore.i18n.txtEntidadeNome2(), "empEntidade.empEntidadeNome2", 200, true);
		listaColunas.add(ccNome2);
		ColumnConfig ccPessoa = new ColumnConfig(OpenSigCore.i18n.txtPessoa(), "empEntidade.empEntidadePessoa", 75, true);
		listaColunas.add(ccPessoa);
		ColumnConfig ccDoc1 = new ColumnConfig(OpenSigCore.i18n.txtEntidadeDoc1(), "empEntidade.empEntidadeDocumento1", 100, true);
		listaColunas.add(ccDoc1);
		ColumnConfig ccDoc2 = new ColumnConfig(OpenSigCore.i18n.txtEntidadeDoc2(), "empEntidade.empEntidadeDocumento2", 100, true);
		listaColunas.add(ccDoc2);
		ColumnConfig ccDoc3 = new ColumnConfig(OpenSigCore.i18n.txtEntidadeDoc3(), "empEntidade.empEntidadeDocumento3", 100, true);
		listaColunas.add(ccDoc3);
		ColumnConfig ccAtivo = new ColumnConfig(OpenSigCore.i18n.txtAtivo(), "empEntidade.empEntidadeAtivo", 50, true, BOLEANO);
		listaColunas.add(ccAtivo);
		ColumnConfig ccObservacao = new ColumnConfig(OpenSigCore.i18n.txtObservacao(), "empEntidade.empEntidadeObservacao", 200, true);
		listaColunas.add(ccObservacao);
		ColumnConfig ccData = new ColumnConfig(OpenSigCore.i18n.txtData(), "empEntidade.empEntidadeData", 100, true, DATAHORA);
		listaColunas.add(ccData);
		
	}

	public void setGridFiltro() {
		super.setGridFiltro();
		for (Entry<String, GridFilter> entry : filtros.entrySet()) {
			if (entry.getKey().equals("empEntidade.empEntidadeAtivo")) {
				((GridBooleanFilter) entry.getValue()).setValue(true);
				break;
			}
		}
	}

	public void setFavorito(IFavorito favorito) {
		filtros.get("empEntidade.empEntidadeAtivo").setActive(false, true);
		super.setFavorito(favorito);
	}

	public Collection<FieldDef> getListaCampos() {
		return listaCampos;
	}

	public void setListaCampos(Collection<FieldDef> listaCampos) {
		this.listaCampos = listaCampos;
	}

	public Collection<BaseColumnConfig> getListaColunas() {
		return listaColunas;
	}

	public void setListaColunas(Collection<BaseColumnConfig> listaColunas) {
		this.listaColunas = listaColunas;
	}

	public Collection<GridFilter> getListaFiltros() {
		return listaFiltros;
	}

	public void setListaFiltros(Collection<GridFilter> listaFiltros) {
		this.listaFiltros = listaFiltros;
	}

	public ColumnConfig getCcEntidade() {
		return ccEntidade;
	}

	public void setCcEntidade(ColumnConfig ccEntidade) {
		this.ccEntidade = ccEntidade;
	}
}
