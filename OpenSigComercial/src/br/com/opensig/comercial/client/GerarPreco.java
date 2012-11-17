package br.com.opensig.comercial.client;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import br.com.opensig.comercial.shared.modelo.ComCompra;
import br.com.opensig.comercial.shared.modelo.ComCompraProduto;
import br.com.opensig.comercial.shared.modelo.ComValorArredonda;
import br.com.opensig.comercial.shared.modelo.ComValorProduto;
import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.UtilClient;
import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.FiltroObjeto;
import br.com.opensig.core.client.servico.CoreProxy;
import br.com.opensig.fiscal.shared.modelo.FisIncentivoEstado;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.FloatFieldDef;
import com.gwtext.client.data.IntegerFieldDef;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.data.event.StoreListenerAdapter;
import com.gwtextux.client.widgets.window.ToastWindow;

public class GerarPreco {

	private ComCompra compra;
	private AsyncCallback asyncCallback;

	// geracao de preco
	private Store storeValor;
	private Store storeArredonda;
	private Store storeIncentivo;

	public GerarPreco(ComCompra compra) {
		this.compra = compra;
		// montando os stores
		FieldDef[] fdValor = new FieldDef[] { new IntegerFieldDef("comValorProdutoId"), new IntegerFieldDef("empEmpresa.empEmpresaId"), new StringFieldDef("empEmpresa.empEntidade.empEntidadeNome1"),
				new IntegerFieldDef("comValorProdutoDespesa"), new IntegerFieldDef("comValorProdutoMarkup"), new StringFieldDef("comValorProdutoFormula"), new IntegerFieldDef("empFornecedorId"),
				new StringFieldDef("empEntidadeNome1"), new IntegerFieldDef("prodProdutoId"), new StringFieldDef("prodProdutoDescricao") };

		FiltroObjeto fo = new FiltroObjeto("empEmpresa", ECompara.IGUAL, compra.getEmpEmpresa());
		CoreProxy<ComValorProduto> proxy = new CoreProxy<ComValorProduto>(new ComValorProduto(), fo);
		storeValor = new Store(proxy, new ArrayReader(new RecordDef(fdValor)), true);
		storeValor.addStoreListener(new StoreListenerAdapter() {
			public void onLoad(Store store, Record[] records) {
				storeArredonda.load();
			}
		});

		FieldDef[] fdArredonda = new FieldDef[] { new IntegerFieldDef("comValorArredondaId"), new IntegerFieldDef("comValorProdutoId"), new FloatFieldDef("comValorArredondaMin"),
				new FloatFieldDef("comValorArredondaMax"), new FloatFieldDef("comValorArredondaFixo") };
		CoreProxy<ComValorArredonda> proxy1 = new CoreProxy<ComValorArredonda>(new ComValorArredonda());
		storeArredonda = new Store(proxy1, new ArrayReader(new RecordDef(fdArredonda)), true);
		storeArredonda.addStoreListener(new StoreListenerAdapter() {
			public void onLoad(Store store, Record[] records) {
				storeIncentivo.load();
			}
		});

		FieldDef[] fdIncentivo = new FieldDef[] { new IntegerFieldDef("fisIncentivoEstadoId"), new IntegerFieldDef("empEmpresa.empEmpresaId"),
				new StringFieldDef("empEmpresa.empEntidade.empEntidadeNome1"), new IntegerFieldDef("empEstadoId"), new StringFieldDef("empEstadoDescricao"),
				new FloatFieldDef("fisIncentivoEstadoIcms1"), new FloatFieldDef("fisIncentivoEstadoIcms2") };
		CoreProxy<FisIncentivoEstado> proxy2 = new CoreProxy<FisIncentivoEstado>(new FisIncentivoEstado());
		storeIncentivo = new Store(proxy2, new ArrayReader(new RecordDef(fdIncentivo)), false);
		storeIncentivo.addStoreListener(new StoreListenerAdapter() {
			public void onLoad(Store store, Record[] records) {
				gerar();
			}
		});
	}

	public void gerar(AsyncCallback asyncCallback) {
		this.asyncCallback = asyncCallback;
		storeValor.load();
	}

	// gera o preço de cada de acordo com uma formula dinamica
	private void gerar() {
		// valor dos produtos
		double total = compra.getComCompraValorProduto();

		// faz os calculos percentuais dos outros tributos na NF
		int frete = (int) (compra.getComCompraValorFrete() / total * 100);
		int seguro = (int) (compra.getComCompraValorSeguro() / total * 100);
		int outros = (int) (compra.getComCompraValorOutros() / total * 100);
		int desconto = (int) (compra.getComCompraValorDesconto() / total * 100);

		// coloca tudo em um Map para utilizar na formula
		Map<String, String> vars = new HashMap<String, String>();
		vars.put("FRETE", frete < 10 ? "0" + frete : frete + "");
		vars.put("SEGURO", seguro < 10 ? "0" + seguro : seguro + "");
		vars.put("OUTROS", outros < 10 ? "0" + outros : outros + "");
		vars.put("DESCONTO", desconto < 10 ? "0" + desconto : desconto + "");

		// variaveis
		Record recProd, recInc;
		String valor, despesa, markup, cst;
		double dentro, icms, ipi, icmsMaior, icmsMenor;

		// faz o loop em todos os registros
		for (ComCompraProduto comProd : compra.getComCompraProdutos()) {
			if (comProd.getComCompraProdutoPreco() == 0.00) {
				try {
					recProd = getValorProduto(comProd.getComCompraProdutoId(), compra.getEmpFornecedor().getEmpFornecedorId());
					recInc = UtilClient.getRegistro(storeIncentivo, "empEstadoId", compra.getEmpEstado().getEmpEstadoId() + "");

					// recupera os valores individuais de cada produto
					valor = comProd.getComCompraProdutoValor() + "";
					dentro = comProd.getProdProduto().getProdTributacao().getProdTributacaoDentro();
					cst = comProd.getProdProduto().getProdTributacao().getProdTributacaoCst();
					icms = comProd.getComCompraProdutoIcms();
					ipi = comProd.getComCompraProdutoIpi();
					despesa = recProd.getAsString("comValorProdutoDespesa");
					markup = recProd.getAsString("comValorProdutoMarkup");
					icmsMaior = recInc != null ? recInc.getAsDouble("fisIncentivoEstadoIcms1") : 0.00;
					icmsMenor = recInc != null ? recInc.getAsDouble("fisIncentivoEstadoIcms2") : 0.00;

					vars.put("BRUTO", valor);
					vars.put("IPI", ipi >= 10 ? String.valueOf(ipi).replace(".", "") : "0" + String.valueOf(ipi).replace(".", ""));
					vars.put("DESPESA", despesa.length() == 1 ? "0" + despesa.replace(".", "") : despesa.replace(".", ""));
					vars.put("MARKUP", markup.length() == 1 ? "0" + markup.replace(".", "") : markup.replace(".", ""));

					// verifica a tributacao pelo cst
					if (cst.equals("00")) { // tributado integral
						// caso a empresa nao tenha incentivo
						if (icmsMaior == 0.00 || icmsMenor == 0.00) {
							vars.put("ICMS", (dentro - icms) >= 10 ? "" + (dentro - icms) : "0" + (dentro - icms));
						} else {
							String icmsInc = "";
							// tendo ver se o produto tem a menor taxa ou nao
							if (comProd.getProdProduto().getProdProdutoIncentivo()) {
								icmsInc = icmsMenor >= 10 ? "" + icmsMenor : "0" + icmsMenor;
							} else {
								icmsInc = icmsMaior >= 10 ? "" + icmsMaior : "0" + icmsMaior;
							}
							vars.put("ICMS", icmsInc);
						}
					} else if (cst.equals("10") || cst.equals("30") || cst.equals("40") || cst.equals("41") || cst.equals("60")) { // isento-substituicao
						vars.put("ICMS", icms >= 10 ? String.valueOf(icms) : "0" + String.valueOf(icms));
					}
					vars.put("ICMS", vars.get("ICMS").replace(".", ""));
					// utiliza a formula
					double preco = executarFormula(recProd.getAsString("comValorProdutoFormula"), vars);
					// arredonda
					preco = arredondar(preco, storeArredonda);
					comProd.setComCompraProdutoPreco(preco);
				} catch (Exception ex) {
					comProd.setComCompraProdutoPreco(0.00);
					new ToastWindow(OpenSigCore.i18n.txtIcms(), OpenSigCore.i18n.errIcms()).show();
				}
			}
		}

		asyncCallback.onSuccess(compra);
	}

	private Record getValorProduto(int produtoId, int fornecedorId) {
		// verifica se tem preco por produto
		Record recProd = UtilClient.getRegistro(storeValor, "prodProdutoId", produtoId + "");
		// case nao ache, procura pelo fornecedor
		if (recProd == null) {
			recProd = UtilClient.getRegistro(storeValor, "empFornecedorId", fornecedorId + "");
		}
		// caso nao ache pega o primeiro que e o geral
		if (recProd == null) {
			recProd = storeValor.getAt(0);
		}

		storeArredonda.filter("comValorProdutoId", recProd.getAsString("comValorProdutoId"));
		return recProd;
	}

	public static double executarFormula(String formula, Map<String, String> vars) throws Exception {
		for (Entry<String, String> entry : vars.entrySet()) {
			formula = formula.toUpperCase().replaceAll(entry.getKey().toUpperCase(), entry.getValue());
		}
		return Double.parseDouble(UtilClient.eval(formula));
	}

	public static double arredondar(Double valor, Store store) {
		int pos = valor.toString().indexOf(".");
		int inteiro = Integer.valueOf(valor.toString().substring(0, pos));
		double decimal = valor - inteiro;

		for (Record rec : store.getRecords()) {
			double min = rec.getAsDouble("comValorArredondaMin");
			double max = rec.getAsDouble("comValorArredondaMax");
			double fixo = rec.getAsDouble("comValorArredondaFixo");

			if (min < decimal && decimal < max) {
				decimal = fixo;
				break;
			}
		}

		return inteiro + decimal;
	}
}
