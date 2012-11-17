package br.com.opensig.financeiro.client.visao.lista;

import java.util.Map;
import java.util.Map.Entry;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.UtilClient;
import br.com.opensig.core.client.controlador.comando.IComando;
import br.com.opensig.core.client.controlador.comando.lista.ComandoEditar;
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
import br.com.opensig.financeiro.client.visao.form.AFormularioFinanciado;
import br.com.opensig.financeiro.shared.modelo.FinForma;

import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.DateFieldDef;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.FloatFieldDef;
import com.gwtext.client.data.IntegerFieldDef;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.SimpleStore;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.grid.BaseColumnConfig;
import com.gwtext.client.widgets.grid.CheckboxColumnConfig;
import com.gwtext.client.widgets.grid.CheckboxSelectionModel;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtextux.client.widgets.grid.plugins.GridFilter;
import com.gwtextux.client.widgets.grid.plugins.GridListFilter;
import com.gwtextux.client.widgets.grid.plugins.GridLongFilter;
import com.gwtextux.client.widgets.grid.plugins.GridSummaryPlugin;
import com.gwtextux.client.widgets.grid.plugins.SummaryColumnConfig;

public class AListagemFinanciado<E extends Dados> extends AListagem<E> {

	protected Map<String, String> nomes;

	public AListagemFinanciado(AFormularioFinanciado<E> formulario) {
		super(formulario);
		nomes = formulario.getNomes();
		addPlugin(new GridSummaryPlugin());
	}

	public void inicializar() {
		// campos
		FieldDef[] fd = new FieldDef[] { new IntegerFieldDef(nomes.get("id")), new IntegerFieldDef(nomes.get("financeiroId")), new IntegerFieldDef(nomes.get("financeiroEmpresa") + ".empEmpresaId"),
				new StringFieldDef(nomes.get("financeiroEmpresa") + ".empEntidade.empEntidadeNome1"), new StringFieldDef(nomes.get("financeiroNome")),
				new IntegerFieldDef(nomes.get("financeiroConta")), new IntegerFieldDef("finForma.finFormaId"), new StringFieldDef("finForma.finFormaDescricao"),
				new StringFieldDef(nomes.get("documento")), new FloatFieldDef(nomes.get("valor")), new StringFieldDef(nomes.get("parcela")), new DateFieldDef(nomes.get("cadastro")),
				new DateFieldDef(nomes.get("vencimento")), new StringFieldDef(nomes.get("status")), new DateFieldDef(nomes.get("realizado")), new DateFieldDef(nomes.get("conciliado")),
				new IntegerFieldDef(nomes.get("financeiroNfe")), new StringFieldDef(nomes.get("observacao")) };
		campos = new RecordDef(fd);

		// selected
		CheckboxSelectionModel model = new CheckboxSelectionModel();
		CheckboxColumnConfig check = new CheckboxColumnConfig(model);

		// colunas
		ColumnConfig ccId = new ColumnConfig(OpenSigCore.i18n.txtCod(), nomes.get("id"), 75, true);
		ColumnConfig ccFinanceiroId = new ColumnConfig(OpenSigCore.i18n.txtCod() + " - " + OpenSigCore.i18n.txtFinanceiro(), nomes.get("financeiroId"), 100, true);
		ccFinanceiroId.setHidden(true);
		ColumnConfig ccEmpresaId = new ColumnConfig(OpenSigCore.i18n.txtCod() + " - " + OpenSigCore.i18n.txtEmpresa(), nomes.get("financeiroEmpresa") + ".empEmpresaId", 100, true);
		ccEmpresaId.setHidden(true);
		ColumnConfig ccEmpresa = new ColumnConfig(OpenSigCore.i18n.txtEmpresa(), nomes.get("financeiroEmpresa") + ".empEntidade.empEntidadeNome1", 100, true);
		ccEmpresa.setHidden(true);
		ColumnConfig ccNome = new ColumnConfig(OpenSigCore.i18n.txtEntidade(), nomes.get("financeiroNome"), 200, true);
		ColumnConfig ccContaId = new ColumnConfig(OpenSigCore.i18n.txtCod() + " - " + OpenSigCore.i18n.txtConta(), nomes.get("financeiroConta"), 100, true);
		ccContaId.setHidden(true);
		ColumnConfig ccFormaId = new ColumnConfig(OpenSigCore.i18n.txtCod() + " - " + OpenSigCore.i18n.txtTipo(), "finForma.finFormaId", 100, true);
		ccFormaId.setHidden(true);
		ColumnConfig ccDescricao = new ColumnConfig(OpenSigCore.i18n.txtTipo(), "finForma.finFormaDescricao", 100, true);
		ColumnConfig ccDocumento = new ColumnConfig(OpenSigCore.i18n.txtDocumento(), nomes.get("documento"), 100, true);
		ColumnConfig ccParcela = new ColumnConfig(OpenSigCore.i18n.txtParcela(), nomes.get("parcela"), 50, true);
		ColumnConfig ccCadastro = new ColumnConfig(OpenSigCore.i18n.txtCadastro(), nomes.get("cadastro"), 75, true, DATA);
		ColumnConfig ccVencimento = new ColumnConfig(OpenSigCore.i18n.txtVencimento(), nomes.get("vencimento"), 75, true, DATA);
		ColumnConfig ccStatus = new ColumnConfig(OpenSigCore.i18n.txtStatus(), nomes.get("status"), 100, true);
		ColumnConfig ccRealizado = new ColumnConfig(OpenSigCore.i18n.txtRealizado(), nomes.get("realizado"), 75, true, DATA);
		ColumnConfig ccConciliado = new ColumnConfig(OpenSigCore.i18n.txtConciliado(), nomes.get("conciliado"), 75, true, DATA);
		ColumnConfig ccNfe = new ColumnConfig(OpenSigCore.i18n.txtNota(), nomes.get("financeiroNfe"), 75, true);
		ColumnConfig ccObservacao = new ColumnConfig(OpenSigCore.i18n.txtObservacao(), nomes.get("observacao"), 200, true);

		// sumarios
		SummaryColumnConfig sumValor = new SummaryColumnConfig(SummaryColumnConfig.SUM, new ColumnConfig(OpenSigCore.i18n.txtValor(), nomes.get("valor"), 75, true, DINHEIRO), DINHEIRO);

		BaseColumnConfig[] bcc = new BaseColumnConfig[] { check, ccId, ccFinanceiroId, ccEmpresaId, ccEmpresa, ccNome, ccContaId, ccFormaId, ccDescricao, ccDocumento, sumValor, ccParcela, ccCadastro,
				ccVencimento, ccStatus, ccRealizado, ccConciliado, ccNfe, ccObservacao };
		modelos = new ColumnModel(bcc);

		if (UtilClient.getAcaoPermitida(funcao, ComandoPermiteEmpresa.class) == null) {
			filtroPadrao = new FiltroObjeto(nomes.get("financeiroEmpresa"), ECompara.IGUAL, new EmpEmpresa(Ponte.getLogin().getEmpresaId()));
		}

		super.inicializar();
		setSelectionModel(model);
	}

	@Override
	public IComando AntesDaAcao(IComando comando) {
		Record[] recs = getSelectionModel().getSelections();

		// valida se pode editar
		if (comando instanceof ComandoEditar) {
			if (recs != null && recs.length > 1) {
				comando = null;
				MessageBox.alert(OpenSigCore.i18n.txtAtencao(), OpenSigCore.i18n.errSelecionar());
			} else if (recs != null && !recs[0].getAsString(nomes.get("status")).equalsIgnoreCase(OpenSigCore.i18n.txtAberto())) {
				comando = null;
				MessageBox.alert(OpenSigCore.i18n.txtAcesso(), OpenSigCore.i18n.txtAcessoNegado());
			}
		}

		return comando;
	}

	public void setGridFiltro() {
		super.setGridFiltro();
		for (Entry<String, GridFilter> entry : filtros.entrySet()) {
			if (entry.getKey().equals(nomes.get("status"))) {
				// status
				Store storeStatus = new SimpleStore("status", new String[] { OpenSigCore.i18n.txtAberto().toUpperCase(), OpenSigCore.i18n.txtRealizado().toUpperCase(), OpenSigCore.i18n.txtConciliado().toUpperCase() });
				GridListFilter fTipo = new GridListFilter(nomes.get("status"), storeStatus);
				fTipo.setLabelField("status");
				fTipo.setLabelValue("status");
				fTipo.setValue(new String[] { OpenSigCore.i18n.txtAberto().toUpperCase() });
				fTipo.setActive(true, true);
				entry.setValue(fTipo);
			} else if (entry.getKey().equals(nomes.get("financeiroEmpresa") + ".empEmpresaId")) {
				((GridLongFilter) entry.getValue()).setValueEquals(Ponte.getLogin().getEmpresaId());
			} else if (entry.getKey().equals(nomes.get("financeiroEmpresa") + ".empEntidade.empEntidadeNome1")) {
				// empresa
				FiltroNumero fn = null;
				if (UtilClient.getAcaoPermitida(funcao, ComandoPermiteEmpresa.class) == null) {
					fn = new FiltroNumero("empEmpresaId", ECompara.IGUAL, Ponte.getLogin().getEmpresaId());
				}

				FieldDef[] fdEmpresa = new FieldDef[] { new IntegerFieldDef("empEmpresaId"), new IntegerFieldDef("empEntidade.empEntidadeId"), new StringFieldDef("empEntidade.empEntidadeNome1") };
				CoreProxy<EmpEmpresa> proxy = new CoreProxy<EmpEmpresa>(new EmpEmpresa(), fn);
				Store storeEmpresa = new Store(proxy, new ArrayReader(new RecordDef(fdEmpresa)), true);

				GridListFilter fEmpresa = new GridListFilter(nomes.get("financeiroEmpresa") + ".empEntidade.empEntidadeNome1", storeEmpresa);
				fEmpresa.setLabelField("empEntidade.empEntidadeNome1");
				fEmpresa.setLabelValue("empEntidade.empEntidadeNome1");
				fEmpresa.setLoadingText(OpenSigCore.i18n.txtAguarde());
				entry.setValue(fEmpresa);

			} else if (entry.getKey().equals("finForma.finFormaDescricao")) {
				// tipo
				FieldDef[] fdTipo = new FieldDef[] { new IntegerFieldDef("finFormaId"), new StringFieldDef("finFormaDescricao") };
				CoreProxy<FinForma> proxy = new CoreProxy<FinForma>(new FinForma());
				Store storeTipo = new Store(proxy, new ArrayReader(new RecordDef(fdTipo)), true);

				GridListFilter fTipo = new GridListFilter("finForma.finFormaDescricao", storeTipo);
				fTipo.setLabelField("finFormaDescricao");
				fTipo.setLabelValue("finFormaDescricao");
				fTipo.setLoadingText(OpenSigCore.i18n.txtAguarde());
				entry.setValue(fTipo);
			}
		}
	}

	public void setFavorito(IFavorito favorito) {
		filtros.get(nomes.get("status")).setActive(false, true);
		filtros.get(nomes.get("financeiroEmpresa") + ".empEmpresaId").setActive(false, true);
		super.setFavorito(favorito);
	}

	public Map<String, String> getNomes() {
		return nomes;
	}

	public void setNomes(Map<String, String> nomes) {
		this.nomes = nomes;
	}
}
