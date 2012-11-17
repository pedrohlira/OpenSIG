package br.com.opensig.financeiro.client.visao.lista;

import java.util.Map;

import br.com.opensig.core.client.UtilClient;
import br.com.opensig.core.client.controlador.comando.AComando;
import br.com.opensig.core.client.controlador.comando.FabricaComando;
import br.com.opensig.core.client.controlador.comando.lista.ComandoExcluirFinal;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.financeiro.client.controlador.comando.ComandoRecebimento;
import br.com.opensig.financeiro.client.servico.FinanceiroProxy;
import br.com.opensig.financeiro.client.visao.form.AFormularioFinanceiro;
import br.com.opensig.financeiro.shared.modelo.FinReceber;
import br.com.opensig.financeiro.shared.modelo.FinRecebimento;

import com.gwtext.client.widgets.menu.Menu;
import com.gwtext.client.widgets.menu.MenuItem;

public class ListagemReceber extends AListagemFinanceiro<FinReceber, FinRecebimento> {

	public ListagemReceber(AFormularioFinanceiro<FinReceber, FinRecebimento> formulario) {
		super(formulario);
		inicializar();
		
		// deletando
		cmdExcluir = new AComando(new ComandoExcluirFinal()) {
			public void execute(Map contexto) {
				super.execute(contexto);
				int id = UtilClient.getSelecionado(getPanel());
				classe.setId(id);

				FinanceiroProxy proxy = new FinanceiroProxy();
				proxy.excluirReceber(classe, ASYNC);
			}
		};
	}
	
	@Override
	public void irPara() {
		Menu mnuContexto = new Menu();

		// venda
		String strVenda = FabricaComando.getInstancia().getComandoCompleto("ComandoVenda");
		SisFuncao venda = UtilClient.getFuncaoPermitida(strVenda);
		MenuItem itemVenda = gerarFuncao(venda, "finReceber.finReceberId", "finReceberId");
		if (itemVenda != null) {
			mnuContexto.addItem(itemVenda);
		}

		// ecf
		String strEcf = FabricaComando.getInstancia().getComandoCompleto("ComandoEcfVenda");
		SisFuncao ecf = UtilClient.getFuncaoPermitida(strEcf);
		MenuItem itemEcf = gerarFuncao(ecf, "finReceber.finReceberId", "finReceberId");
		if (itemEcf != null) {
			mnuContexto.addItem(itemEcf);
		}
		
		// recebimentos
		SisFuncao recebimentos = UtilClient.getFuncaoPermitida(ComandoRecebimento.class);
		MenuItem itemRecebimentos = gerarFuncao(recebimentos, "finReceber.finReceberId", "finReceberId");
		if (itemRecebimentos != null) {
			mnuContexto.addItem(itemRecebimentos);
		}

		if (mnuContexto.getItems().length > 0) {
			MenuItem mnuItem = getIrPara();
			mnuItem.setMenu(mnuContexto);

			getMenu().addSeparator();
			getMenu().addItem(mnuItem);
		}
	}
}
