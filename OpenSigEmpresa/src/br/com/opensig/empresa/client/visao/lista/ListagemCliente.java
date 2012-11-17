package br.com.opensig.empresa.client.visao.lista;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.UtilClient;
import br.com.opensig.core.client.controlador.comando.FabricaComando;
import br.com.opensig.core.client.visao.abstrato.IFormulario;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.empresa.shared.modelo.EmpCliente;

import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.IntegerFieldDef;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.widgets.grid.BaseColumnConfig;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.menu.Menu;
import com.gwtext.client.widgets.menu.MenuItem;

public class ListagemCliente extends AListagemEntidade<EmpCliente> {

	public ListagemCliente(IFormulario<EmpCliente> formulario) {
		super(formulario);
		inicializar();
	}

	public void inicializar() {
		listaCampos.add(new IntegerFieldDef("empClienteId"));
		listaColunas.add(new ColumnConfig(OpenSigCore.i18n.txtCod(), "empClienteId", 50, true));
		super.configurar();

		campos = new RecordDef(listaCampos.toArray(new FieldDef[] {}));
		modelos = new ColumnModel(listaColunas.toArray(new BaseColumnConfig[] {}));
		super.inicializar();
	}
	
	@Override
	public void irPara() {
		Menu mnuContexto = new Menu();
		
		// venda
		String strVenda = FabricaComando.getInstancia().getComandoCompleto("ComandoVenda");
		SisFuncao venda = UtilClient.getFuncaoPermitida(strVenda);
		MenuItem itemVenda = gerarFuncao(venda, "empCliente.empClienteId", "empClienteId");
		if (itemVenda != null) {
			mnuContexto.addItem(itemVenda);
		}
		
		// receber
		String strReceber = FabricaComando.getInstancia().getComandoCompleto("ComandoReceber");
		SisFuncao receber = UtilClient.getFuncaoPermitida(strReceber);
		MenuItem itemReceber = gerarFuncao(receber, "empEntidade.empEntidadeId", "empEntidade.empEntidadeId");
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
