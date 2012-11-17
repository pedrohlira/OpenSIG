package br.com.opensig.financeiro.client.visao.lista;

import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.UtilClient;
import br.com.opensig.core.client.controlador.comando.IComando;
import br.com.opensig.core.client.controlador.comando.lista.ComandoExcluir;
import br.com.opensig.core.client.controlador.comando.lista.ComandoPermiteEmpresa;
import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.FiltroNumero;
import br.com.opensig.core.client.controlador.filtro.FiltroObjeto;
import br.com.opensig.core.client.servico.CoreProxy;
import br.com.opensig.core.client.visao.Ponte;
import br.com.opensig.core.client.visao.abstrato.AListagem;
import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.core.shared.modelo.IFavorito;
import br.com.opensig.empresa.shared.modelo.EmpEmpresa;
import br.com.opensig.financeiro.client.visao.form.AFormularioFinanceiro;
import br.com.opensig.financeiro.shared.modelo.FinCategoria;
import br.com.opensig.financeiro.shared.modelo.FinConta;

import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.DateFieldDef;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.FloatFieldDef;
import com.gwtext.client.data.IntegerFieldDef;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.grid.BaseColumnConfig;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtextux.client.widgets.grid.plugins.GridDateFilter;
import com.gwtextux.client.widgets.grid.plugins.GridFilter;
import com.gwtextux.client.widgets.grid.plugins.GridListFilter;
import com.gwtextux.client.widgets.grid.plugins.GridLongFilter;
import com.gwtextux.client.widgets.grid.plugins.GridSummaryPlugin;
import com.gwtextux.client.widgets.grid.plugins.SummaryColumnConfig;

public abstract class AListagemFinanceiro<E extends Dados, T extends Dados> extends AListagem<E> {

	protected Map<String, String> nomes;
	protected double financeiro;
	protected IComando cmdExcluir;

	public AListagemFinanceiro(AFormularioFinanceiro<E, T> formulario) {
		super(formulario);
		nomes = formulario.getNomes();
		addPlugin(new GridSummaryPlugin());
	}

	public void inicializar() {
		// campos
		FieldDef[] fd = new FieldDef[] { new IntegerFieldDef(nomes.get("id")), new IntegerFieldDef("empEmpresa.empEmpresaId"), new StringFieldDef("empEmpresa.empEntidade.empEntidadeNome1"),
				new IntegerFieldDef("empEntidade.empEntidadeId"), new StringFieldDef("empEntidade.empEntidadeNome1"), new IntegerFieldDef("finConta.finContaId"),
				new StringFieldDef("finConta.finContaNome"), new FloatFieldDef(nomes.get("valor")), new DateFieldDef(nomes.get("cadastro")), new StringFieldDef(nomes.get("categoria")),
				new IntegerFieldDef(nomes.get("nota")), new StringFieldDef(nomes.get("observacao")) };
		campos = new RecordDef(fd);

		// colunas
		ColumnConfig ccId = new ColumnConfig(OpenSigCore.i18n.txtCod(), nomes.get("id"), 75, true);
		ColumnConfig ccEmpresaId = new ColumnConfig(OpenSigCore.i18n.txtCod() + " - " + OpenSigCore.i18n.txtEmpresa(), "empEmpresa.empEmpresaId", 100, true);
		ccEmpresaId.setHidden(true);
		ColumnConfig ccEmpresa = new ColumnConfig(OpenSigCore.i18n.txtEmpresa(), "empEmpresa.empEntidade.empEntidadeNome1", 100, true);
		ccEmpresa.setHidden(true);
		ColumnConfig ccEntidadeId = new ColumnConfig(OpenSigCore.i18n.txtCod() + " - " + OpenSigCore.i18n.txtEntidade(), "empEntidade.empEntidadeId", 100, true);
		ccEntidadeId.setHidden(true);
		ColumnConfig ccNome = new ColumnConfig(OpenSigCore.i18n.txtEntidade(), "empEntidade.empEntidadeNome1", 200, true);
		ColumnConfig ccContaId = new ColumnConfig(OpenSigCore.i18n.txtCod() + " - " + OpenSigCore.i18n.txtConta(), "finConta.finContaId", 100, true);
		ccContaId.setHidden(true);
		ColumnConfig ccConta = new ColumnConfig(OpenSigCore.i18n.txtConta(), "finConta.finContaNome", 100, true);
		ccConta.setHidden(true);
		ColumnConfig ccCadastro = new ColumnConfig(OpenSigCore.i18n.txtCadastro(), nomes.get("cadastro"), 75, true, DATA);
		ColumnConfig ccCategoria = new ColumnConfig(OpenSigCore.i18n.txtCategoria(), nomes.get("categoria"), 100, true);
		ColumnConfig ccNota = new ColumnConfig(OpenSigCore.i18n.txtNota(), nomes.get("nota"), 75, true);
		ColumnConfig ccObservacao = new ColumnConfig(OpenSigCore.i18n.txtObservacao(), nomes.get("observacao"), 200, true);

		// sumarios
		SummaryColumnConfig sumValor = new SummaryColumnConfig(SummaryColumnConfig.SUM, new ColumnConfig(OpenSigCore.i18n.txtValor(), nomes.get("valor"), 75, true, DINHEIRO), DINHEIRO);

		BaseColumnConfig[] bcc = new BaseColumnConfig[] { ccId, ccEmpresaId, ccEmpresa, ccEntidadeId, ccNome, ccContaId, ccConta, sumValor, ccCadastro, ccCategoria, ccNota, ccObservacao };
		modelos = new ColumnModel(bcc);

		if (UtilClient.getAcaoPermitida(funcao, ComandoPermiteEmpresa.class) == null) {
			filtroPadrao = new FiltroObjeto("empEmpresa", ECompara.IGUAL, new EmpEmpresa(Ponte.getLogin().getEmpresaId()));
		}

		super.inicializar();
	}

	public IComando AntesDaAcao(IComando comando) {
		final Record rec = getSelectionModel().getSelected();

		if (comando instanceof ComandoExcluir) {
			comando = null;
			MessageBox.confirm(OpenSigCore.i18n.txtExcluir(), OpenSigCore.i18n.msgExcluir(), new MessageBox.ConfirmCallback() {
				public void execute(String btnID) {
					if (btnID.equalsIgnoreCase("yes") && rec != null) {
						MessageBox.wait(OpenSigCore.i18n.txtAguarde(), OpenSigCore.i18n.txtExcluir());
						cmdExcluir.execute(contexto);
					}
				}
			});
		}

		return comando;
	}
	
	public void setGridFiltro() {
		super.setGridFiltro();
		for (Entry<String, GridFilter> entry : filtros.entrySet()) {
			if (entry.getKey().equals(nomes.get("cadastro"))) {
				((GridDateFilter) entry.getValue()).setValueOn(new Date());
			} else if (entry.getKey().equals("empEmpresa.empEmpresaId")) {
				((GridLongFilter) entry.getValue()).setValueEquals(Ponte.getLogin().getEmpresaId());
			} else if (entry.getKey().equals("empEmpresa.empEntidade.empEntidadeNome1")) {
				// empresa
				FiltroNumero fn = null;
				if (UtilClient.getAcaoPermitida(funcao, ComandoPermiteEmpresa.class) == null) {
					fn = new FiltroNumero("empEmpresaId", ECompara.IGUAL, Ponte.getLogin().getEmpresaId());
				}

				FieldDef[] fdEmpresa = new FieldDef[] { new IntegerFieldDef("empEmpresaId"), new IntegerFieldDef("empEntidade.empEntidadeId"), new StringFieldDef("empEntidade.empEntidadeNome1") };
				CoreProxy<EmpEmpresa> proxy = new CoreProxy<EmpEmpresa>(new EmpEmpresa(), fn);
				Store storeEmpresa = new Store(proxy, new ArrayReader(new RecordDef(fdEmpresa)), true);

				GridListFilter fEmpresa = new GridListFilter("empEmpresa.empEntidade.empEntidadeNome1", storeEmpresa);
				fEmpresa.setLabelField("empEntidade.empEntidadeNome1");
				fEmpresa.setLabelValue("empEntidade.empEntidadeNome1");
				fEmpresa.setLoadingText(OpenSigCore.i18n.txtAguarde());
				entry.setValue(fEmpresa);

			} else if (entry.getKey().equals("finConta.finContaNome")) {
				// conta
				FieldDef[] fdConta = new FieldDef[] { new IntegerFieldDef("finContaId"), new IntegerFieldDef("EmpresaId"), new StringFieldDef("EmpresaNome"), new IntegerFieldDef("BancoId"),
						new StringFieldDef("BancoNome"), new StringFieldDef("finContaNome") };
				CoreProxy<FinConta> proxy = new CoreProxy<FinConta>(new FinConta(), filtroPadrao);
				Store storeConta = new Store(proxy, new ArrayReader(new RecordDef(fdConta)), true);

				GridListFilter fConta = new GridListFilter("finConta.finContaNome", storeConta);
				fConta.setLabelField("finContaNome");
				fConta.setLabelValue("finContaNome");
				fConta.setLoadingText(OpenSigCore.i18n.txtAguarde());
				entry.setValue(fConta);
			} else if (entry.getKey().equals(nomes.get("categoria"))) {
				// categoria
				FieldDef[] fdCategoria = new FieldDef[] { new IntegerFieldDef("finCategoriaId"), new StringFieldDef("finCategoriaDescricao") };
				CoreProxy<FinCategoria> proxy = new CoreProxy<FinCategoria>(new FinCategoria());
				Store storeCategoria = new Store(proxy, new ArrayReader(new RecordDef(fdCategoria)), true);

				GridListFilter fCategoria = new GridListFilter(nomes.get("categoria"), storeCategoria);
				fCategoria.setLabelField("finCategoriaDescricao");
				fCategoria.setLabelValue("finCategoriaDescricao");
				fCategoria.setLoadingText(OpenSigCore.i18n.txtAguarde());
				entry.setValue(fCategoria);
			}
		}
	}

	public void setFavorito(IFavorito favorito) {
		filtros.get(nomes.get("cadastro")).setActive(false, true);
		filtros.get("empEmpresa.empEmpresaId").setActive(false, true);
		super.setFavorito(favorito);
	}

	public Map<String, String> getNomes() {
		return nomes;
	}

	public void setNomes(Map<String, String> nomes) {
		this.nomes = nomes;
	}

	public double getFinanceiro() {
		return financeiro;
	}

	public void setFinanceiro(double financeiro) {
		this.financeiro = financeiro;
	}
}
