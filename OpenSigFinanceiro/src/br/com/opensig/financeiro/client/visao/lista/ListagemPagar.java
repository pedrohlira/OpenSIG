package br.com.opensig.financeiro.client.visao.lista;

import java.util.Map;

import br.com.opensig.core.client.UtilClient;
import br.com.opensig.core.client.controlador.comando.AComando;
import br.com.opensig.core.client.controlador.comando.FabricaComando;
import br.com.opensig.core.client.controlador.comando.lista.ComandoExcluirFinal;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.financeiro.client.controlador.comando.ComandoPagamento;
import br.com.opensig.financeiro.client.servico.FinanceiroProxy;
import br.com.opensig.financeiro.client.visao.form.AFormularioFinanceiro;
import br.com.opensig.financeiro.shared.modelo.FinPagamento;
import br.com.opensig.financeiro.shared.modelo.FinPagar;

import com.gwtext.client.widgets.menu.Menu;
import com.gwtext.client.widgets.menu.MenuItem;

public class ListagemPagar extends AListagemFinanceiro<FinPagar, FinPagamento> {

	public ListagemPagar(AFormularioFinanceiro<FinPagar, FinPagamento> formulario) {
		super(formulario);
		inicializar();

		// deletando
		cmdExcluir = new AComando(new ComandoExcluirFinal()) {
			public void execute(Map contexto) {
				super.execute(contexto);
				int id = UtilClient.getSelecionado(getPanel());
				classe.setId(id);

				FinanceiroProxy proxy = new FinanceiroProxy();
				proxy.excluirPagar(classe, ASYNC);
			}
		};
	}
	
	@Override
	public void irPara() {
		Menu mnuContexto = new Menu();

		// compra
		String strCompra = FabricaComando.getInstancia().getComandoCompleto("ComandoCompra");
		SisFuncao compra = UtilClient.getFuncaoPermitida(strCompra);
		MenuItem itemCompra = gerarFuncao(compra, "finPagar.finPagarId", "finPagarId");
		if (itemCompra != null) {
			mnuContexto.addItem(itemCompra);
		}

		// frete
		String strFrete = FabricaComando.getInstancia().getComandoCompleto("ComandoFrete");
		SisFuncao frete = UtilClient.getFuncaoPermitida(strFrete);
		MenuItem itemFrete = gerarFuncao(frete, "finPagar.finPagarId", "finPagarId");
		if (itemFrete != null) {
			mnuContexto.addItem(itemFrete);
		}
		
		// pagamentos
		SisFuncao pagamentos = UtilClient.getFuncaoPermitida(ComandoPagamento.class);
		MenuItem itemPagamentos = gerarFuncao(pagamentos, "finPagar.finPagarId", "finPagarId");
		if (itemPagamentos != null) {
			mnuContexto.addItem(itemPagamentos);
		}

		if (mnuContexto.getItems().length > 0) {
			MenuItem mnuItem = getIrPara();
			mnuItem.setMenu(mnuContexto);

			getMenu().addSeparator();
			getMenu().addItem(mnuItem);
		}
	}
}
