package br.com.opensig.comercial.client.visao.lista;

import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

import br.com.opensig.comercial.client.servico.ComercialProxy;
import br.com.opensig.comercial.shared.modelo.ComFrete;
import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.UtilClient;
import br.com.opensig.core.client.controlador.comando.AComando;
import br.com.opensig.core.client.controlador.comando.IComando;
import br.com.opensig.core.client.controlador.comando.lista.ComandoEditar;
import br.com.opensig.core.client.controlador.comando.lista.ComandoExcluir;
import br.com.opensig.core.client.controlador.comando.lista.ComandoExcluirFinal;
import br.com.opensig.core.client.controlador.comando.lista.ComandoPermiteEmpresa;
import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.FiltroNumero;
import br.com.opensig.core.client.controlador.filtro.FiltroObjeto;
import br.com.opensig.core.client.servico.CoreProxy;
import br.com.opensig.core.client.visao.Ponte;
import br.com.opensig.core.client.visao.abstrato.AListagem;
import br.com.opensig.core.client.visao.abstrato.IFormulario;
import br.com.opensig.core.shared.modelo.IFavorito;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.empresa.client.controlador.comando.ComandoTransportadora;
import br.com.opensig.empresa.shared.modelo.EmpEmpresa;
import br.com.opensig.financeiro.client.controlador.comando.ComandoPagar;

import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.BooleanFieldDef;
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
import com.gwtext.client.widgets.menu.Menu;
import com.gwtext.client.widgets.menu.MenuItem;
import com.gwtextux.client.widgets.grid.plugins.GridDateFilter;
import com.gwtextux.client.widgets.grid.plugins.GridFilter;
import com.gwtextux.client.widgets.grid.plugins.GridListFilter;
import com.gwtextux.client.widgets.grid.plugins.GridLongFilter;
import com.gwtextux.client.widgets.grid.plugins.GridSummaryPlugin;
import com.gwtextux.client.widgets.grid.plugins.SummaryColumnConfig;

public class ListagemFrete extends AListagem<ComFrete> {

	protected IComando cmdExcluir;

	public ListagemFrete(IFormulario<ComFrete> formulario) {
		super(formulario);
		inicializar();
		addPlugin(new GridSummaryPlugin());
	}

	public void inicializar() {
		// campos
		FieldDef[] fd = new FieldDef[] { new IntegerFieldDef("comFreteId"), new IntegerFieldDef("empFornecedor.empFornecedorId"), new StringFieldDef("empFornecedor.empEntidade.empEntidadeNome1"),
				new IntegerFieldDef("empEmpresa.empEmpresaId"), new StringFieldDef("empEmpresa.empEntidade.empEntidadeNome1"), new IntegerFieldDef("empTransportadora.empTransportadoraId"),
				new IntegerFieldDef("empTransportadora.empEntidade.empEntidadeId"), new StringFieldDef("empTransportadora.empEntidade.empEntidadeNome1"), new IntegerFieldDef("comFreteCtrc"),
				new DateFieldDef("comFreteEmissao"), new DateFieldDef("comFreteRecebimento"), new IntegerFieldDef("comFreteSerie"), new IntegerFieldDef("comFreteCfop"),
				new IntegerFieldDef("comFreteVolume"), new StringFieldDef("comFreteEspecie"), new FloatFieldDef("comFretePeso"), new FloatFieldDef("comFreteCubagem"),
				new FloatFieldDef("comFreteValorProduto"), new IntegerFieldDef("comFreteNota"), new FloatFieldDef("comFreteValor"), new FloatFieldDef("comFreteBase"),
				new FloatFieldDef("comFreteAliquota"), new FloatFieldDef("comFreteIcms"), new BooleanFieldDef("comFreteFechada"), new IntegerFieldDef("contaId"),
				new IntegerFieldDef("finPagar.finPagarId"), new BooleanFieldDef("comFretePaga"), new StringFieldDef("comFreteObservacao") };
		campos = new RecordDef(fd);

		// colunas
		ColumnConfig ccId = new ColumnConfig(OpenSigCore.i18n.txtCod(), "comFreteId", 75, true);
		ColumnConfig ccFornecedorId = new ColumnConfig(OpenSigCore.i18n.txtCod() + " - " + OpenSigCore.i18n.txtFornecedor(), "empFornecedor.empFornecedorId", 100, true);
		ccFornecedorId.setHidden(true);
		ColumnConfig ccFonecedorNome = new ColumnConfig(OpenSigCore.i18n.txtFornecedor(), "empFornecedor.empEntidade.empEntidadeNome1", 200, true);
		ColumnConfig ccEmpresaId = new ColumnConfig(OpenSigCore.i18n.txtCod() + " - " + OpenSigCore.i18n.txtEmpresa(), "empEmpresa.empEmpresaId", 100, true);
		ccEmpresaId.setHidden(true);
		ColumnConfig ccEmpresa = new ColumnConfig(OpenSigCore.i18n.txtEmpresa(), "empEmpresa.empEntidade.empEntidadeNome1", 100, true);
		ccEmpresa.setHidden(true);
		ColumnConfig ccTransportadoraId = new ColumnConfig(OpenSigCore.i18n.txtCod() + " - " + OpenSigCore.i18n.txtTransportadora(), "empTransportadora.empTransportadoraId", 100, true);
		ccTransportadoraId.setHidden(true);
		ColumnConfig ccEntidadeId = new ColumnConfig(OpenSigCore.i18n.txtCod() + " - " + OpenSigCore.i18n.txtEntidade(), "empTransportadora.empEntidade.empEntidadeId", 100, true);
		ccEntidadeId.setHidden(true);
		ColumnConfig ccEntidadeNome = new ColumnConfig(OpenSigCore.i18n.txtTransportadora(), "empTransportadora.empEntidade.empEntidadeNome1", 200, true);
		ColumnConfig ccCtrc = new ColumnConfig(OpenSigCore.i18n.txtCtrc(), "comFreteCtrc", 50, true);
		ColumnConfig ccEmissao = new ColumnConfig(OpenSigCore.i18n.txtEmissao(), "comFreteEmissao", 75, true, DATA);
		ColumnConfig ccRecebimento = new ColumnConfig(OpenSigCore.i18n.txtRecebimento(), "comFreteRecebimento", 75, true, DATA);
		ColumnConfig ccSerie = new ColumnConfig(OpenSigCore.i18n.txtSerie(), "comFreteSerie", 75, true, NUMERO);
		ColumnConfig ccCfop = new ColumnConfig(OpenSigCore.i18n.txtCfop(), "comFreteCfop", 50, true);
		ColumnConfig ccVolume = new ColumnConfig(OpenSigCore.i18n.txtVolume(), "comFreteVolume", 75, true, NUMERO);
		ColumnConfig ccEspecie = new ColumnConfig(OpenSigCore.i18n.txtEspecie(), "comFreteEspecie", 75, true);
		ColumnConfig ccPeso = new ColumnConfig(OpenSigCore.i18n.txtPeso(), "comFretePeso", 50, true, NUMERO);
		ColumnConfig ccCubagem = new ColumnConfig(OpenSigCore.i18n.txtCubagem(), "comFreteCubagem", 75, true, NUMERO);
		ColumnConfig ccProduto = new ColumnConfig(OpenSigCore.i18n.txtValorProduto(), "comFreteValorProduto", 75, true, DINHEIRO);
		ColumnConfig ccNota = new ColumnConfig(OpenSigCore.i18n.txtNota(), "comFreteNota", 75, true, NUMERO);
		ColumnConfig ccBase = new ColumnConfig(OpenSigCore.i18n.txtIcmsBase(), "comFreteBase", 75, true, DINHEIRO);
		ColumnConfig ccAliquota = new ColumnConfig(OpenSigCore.i18n.txtAliquota(), "comFreteAliquota", 75, true, PORCENTAGEM);
		ColumnConfig ccFechada = new ColumnConfig(OpenSigCore.i18n.txtFechada(), "comFreteFechada", 75, true, BOLEANO);
		ColumnConfig ccContaId = new ColumnConfig(OpenSigCore.i18n.txtCod() + " - " + OpenSigCore.i18n.txtConta(), "contaId", 100, true);
		ccContaId.setHidden(true);
		ccContaId.setFixed(true);
		ColumnConfig ccPagarId = new ColumnConfig(OpenSigCore.i18n.txtCod() + " - " + OpenSigCore.i18n.txtPagar(), "finPagar.finPagarId", 100, true);
		ccPagarId.setHidden(true);
		ColumnConfig ccPaga = new ColumnConfig(OpenSigCore.i18n.txtPaga(), "comFretePaga", 75, true, BOLEANO);
		ColumnConfig ccObservacao = new ColumnConfig(OpenSigCore.i18n.txtObservacao(), "comFreteObservacao", 200, true);

		// sumarios
		SummaryColumnConfig sumValor = new SummaryColumnConfig(SummaryColumnConfig.SUM, new ColumnConfig(OpenSigCore.i18n.txtValor(), "comFreteValor", 75, true, DINHEIRO), DINHEIRO);
		SummaryColumnConfig sumIcms = new SummaryColumnConfig(SummaryColumnConfig.SUM, new ColumnConfig(OpenSigCore.i18n.txtIcms(), "comFreteIcms", 75, true, DINHEIRO), DINHEIRO);

		BaseColumnConfig[] bcc = new BaseColumnConfig[] { ccId, ccFornecedorId, ccFonecedorNome, ccEmpresaId, ccEmpresa, ccTransportadoraId, ccEntidadeId, ccEntidadeNome, ccCtrc, ccEmissao,
				ccRecebimento, ccSerie, ccCfop, ccVolume, ccEspecie, ccPeso, ccCubagem, ccProduto, ccNota, sumValor, ccBase, ccAliquota, sumIcms, ccFechada, ccContaId, ccPagarId, ccPaga, ccObservacao };
		modelos = new ColumnModel(bcc);

		// excluindo
		cmdExcluir = new AComando(new ComandoExcluirFinal()) {
			public void execute(Map contexto) {
				super.execute(contexto);
				int id = UtilClient.getSelecionado(getPanel());
				classe.setId(id);

				ComercialProxy proxy = new ComercialProxy();
				proxy.excluirFrete(classe, ASYNC);
			}
		};

		if (UtilClient.getAcaoPermitida(funcao, ComandoPermiteEmpresa.class) == null) {
			filtroPadrao = new FiltroObjeto("empEmpresa", ECompara.IGUAL, new EmpEmpresa(Ponte.getLogin().getEmpresaId()));
		}

		super.inicializar();
	}

	public IComando AntesDaAcao(IComando comando) {
		Record rec = getSelectionModel().getSelected();

		// valida se pode editar
		if (comando instanceof ComandoEditar) {
			if (rec != null && rec.getAsBoolean("comFreteFechada")) {
				MessageBox.alert(OpenSigCore.i18n.txtAcesso(), OpenSigCore.i18n.txtAcessoNegado());
				comando = null;
			}
		}// valida se pode excluir
		else if (comando instanceof ComandoExcluir) {
			comando = null;
			if (rec != null) {
				MessageBox.confirm(OpenSigCore.i18n.txtExcluir(), OpenSigCore.i18n.msgExcluir(), new MessageBox.ConfirmCallback() {
					public void execute(String btnID) {
						if (btnID.equalsIgnoreCase("yes")) {
							MessageBox.wait(OpenSigCore.i18n.txtAguarde(), OpenSigCore.i18n.txtExcluir());
							cmdExcluir.execute(contexto);
						}
					}
				});
			}
		}

		return comando;
	}

	public void setGridFiltro() {
		super.setGridFiltro();
		for (Entry<String, GridFilter> entry : filtros.entrySet()) {
			if (entry.getKey().equals("comFreteRecebimento")) {
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
			}
		}
	}

	public void setFavorito(IFavorito favorito) {
		filtros.get("comFreteRecebimento").setActive(false, true);
		filtros.get("empEmpresa.empEmpresaId").setActive(false, true);
		super.setFavorito(favorito);
	}
	
	@Override
	public void irPara() {
		Menu mnuContexto = new Menu();
		
		// transportadora
		SisFuncao transportadora = UtilClient.getFuncaoPermitida(ComandoTransportadora.class);
		MenuItem itemTransportadora = gerarFuncao(transportadora, "empTransportadoraId", "empTransportadora.empTransportadoraId");
		if (itemTransportadora != null) {
			mnuContexto.addItem(itemTransportadora);
		}
		
		// pagar
		SisFuncao pagar = UtilClient.getFuncaoPermitida(ComandoPagar.class);
		MenuItem itemPagar = gerarFuncao(pagar, "finPagarId", "finPagar.finPagarId");
		if (itemPagar != null) {
			mnuContexto.addItem(itemPagar);
		}
		
		if (mnuContexto.getItems().length > 0) {
			MenuItem mnuItem = getIrPara();
			mnuItem.setMenu(mnuContexto);

			getMenu().addSeparator();
			getMenu().addItem(mnuItem);
		}
	}
}
