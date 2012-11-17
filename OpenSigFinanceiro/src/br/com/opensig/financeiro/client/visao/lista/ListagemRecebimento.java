package br.com.opensig.financeiro.client.visao.lista;

import br.com.opensig.core.client.UtilClient;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.financeiro.client.controlador.comando.ComandoReceber;
import br.com.opensig.financeiro.client.visao.form.AFormularioFinanciado;
import br.com.opensig.financeiro.shared.modelo.FinRecebimento;

import com.gwtext.client.widgets.menu.Menu;
import com.gwtext.client.widgets.menu.MenuItem;

public class ListagemRecebimento extends AListagemFinanciado<FinRecebimento> {

	public ListagemRecebimento(AFormularioFinanciado<FinRecebimento> formulario) {
		super(formulario);
		inicializar();
	}

	@Override
	public void irPara() {
		Menu mnuContexto = new Menu();

		// receber
		SisFuncao receber = UtilClient.getFuncaoPermitida(ComandoReceber.class);
		MenuItem itemReceber = gerarFuncao(receber, "finReceberId", "finReceber.finReceberId");
		if (itemReceber != null) {
			mnuContexto.addItem(itemReceber);
		}

		if (mnuContexto.getItems().length > 0) {
			MenuItem mnuItem = getIrPara();
			mnuItem.setMenu(mnuContexto);

			getMenu().addSeparator();
			getMenu().addItem(mnuItem);
		}
	}
}
