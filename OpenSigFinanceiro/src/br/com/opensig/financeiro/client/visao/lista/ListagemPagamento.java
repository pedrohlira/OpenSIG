package br.com.opensig.financeiro.client.visao.lista;

import br.com.opensig.core.client.UtilClient;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.financeiro.client.controlador.comando.ComandoPagar;
import br.com.opensig.financeiro.client.visao.form.AFormularioFinanciado;
import br.com.opensig.financeiro.shared.modelo.FinPagamento;

import com.gwtext.client.widgets.menu.Menu;
import com.gwtext.client.widgets.menu.MenuItem;

public class ListagemPagamento extends AListagemFinanciado<FinPagamento> {

	public ListagemPagamento(AFormularioFinanciado<FinPagamento> formulario) {
		super(formulario);
		inicializar();
	}
	
	@Override
	public void irPara() {
		Menu mnuContexto = new Menu();

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
