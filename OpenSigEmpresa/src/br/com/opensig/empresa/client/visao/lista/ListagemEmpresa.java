package br.com.opensig.empresa.client.visao.lista;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.UtilClient;
import br.com.opensig.core.client.controlador.comando.lista.ComandoPermiteEmpresa;
import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.FiltroNumero;
import br.com.opensig.core.client.visao.Ponte;
import br.com.opensig.core.client.visao.abstrato.IFormulario;
import br.com.opensig.empresa.shared.modelo.EmpEmpresa;

import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.IntegerFieldDef;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.widgets.grid.BaseColumnConfig;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;

public class ListagemEmpresa extends AListagemEntidade<EmpEmpresa> {

	public ListagemEmpresa(IFormulario<EmpEmpresa> formulario) {
		super(formulario);
		inicializar();
	}

	public void inicializar() {
		listaCampos.add(new IntegerFieldDef("empEmpresaId"));
		listaColunas.add(new ColumnConfig(OpenSigCore.i18n.txtCod(), "empEmpresaId", 50, true));
		super.configurar();

		campos = new RecordDef(listaCampos.toArray(new FieldDef[] {}));
		modelos = new ColumnModel(listaColunas.toArray(new BaseColumnConfig[] {}));
		
		if (UtilClient.getAcaoPermitida(funcao, ComandoPermiteEmpresa.class) == null) {
			filtroPadrao = new FiltroNumero("empEmpresaId", ECompara.IGUAL, Ponte.getLogin().getEmpresaId());
		}
		
		super.inicializar();
	}
}
