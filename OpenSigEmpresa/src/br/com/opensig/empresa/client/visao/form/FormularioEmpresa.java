package br.com.opensig.empresa.client.visao.form;

import java.util.ArrayList;
import java.util.List;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.UtilClient;
import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.FiltroObjeto;
import br.com.opensig.core.shared.modelo.EDirecao;
import br.com.opensig.core.shared.modelo.ExpListagem;
import br.com.opensig.core.shared.modelo.ExpMeta;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.empresa.client.visao.lista.ListagemPlano;
import br.com.opensig.empresa.shared.modelo.EmpEmpresa;
import br.com.opensig.empresa.shared.modelo.EmpPlano;

import com.gwtext.client.data.Record;
import com.gwtext.client.data.SortState;
import com.gwtext.client.widgets.form.Hidden;
import com.gwtextux.client.widgets.window.ToastWindow;

public class FormularioEmpresa extends FormularioEntidade<EmpEmpresa> {

	private ListagemPlano gridPlano;

	public FormularioEmpresa(SisFuncao funcao) {
		super(new EmpEmpresa(), funcao, "empEntidade");
		inicializar();
	}

	public void inicializar() {
		hdnId = new Hidden("empEmpresaId", "0");
		add(hdnId);

		super.configurar();
		// adicionando nova aba planos
		gridPlano = new ListagemPlano(true);
		tabDados.add(gridPlano);
		super.inicializar();
	}

	@Override
	public void gerarListas() {
		super.gerarListas();

		// plano
		List<ExpMeta> metadados = new ArrayList<ExpMeta>();
		for (int i = 0; i < gridPlano.getModelos().getColumnCount(); i++) {
			if (gridPlano.getModelos().isHidden(i)) {
				metadados.add(null);
			} else {
				metadados.add(new ExpMeta(gridPlano.getModelos().getColumnHeader(i), gridPlano.getModelos().getColumnWidth(i), null));
			}
		}

		SortState ordem = gridPlano.getStore().getSortState();
		EmpPlano plano = new EmpPlano();
		plano.setCampoOrdem(ordem.getField());
		plano.setOrdemDirecao(EDirecao.valueOf(ordem.getDirection().getDirection()));
		// filtro
		int id = UtilClient.getSelecionado(lista.getPanel());
		FiltroObjeto filtro = new FiltroObjeto("empEmpresa", ECompara.IGUAL, new EmpEmpresa(id));
		
		ExpListagem<EmpPlano> planos = new ExpListagem<EmpPlano>();
		planos.setClasse(plano);
		planos.setMetadados(metadados);
		planos.setNome(gridPlano.getTitle());
		planos.setFiltro(filtro);
		expLista.add(planos);
	}

	@Override
	public void limparDados() {
		super.limparDados();
		FiltroObjeto fo = new FiltroObjeto("empEmpresa", ECompara.IGUAL, new EmpEmpresa(0));
		gridPlano.getProxy().setFiltroPadrao(fo);
		gridPlano.getStore().removeAll();
	}

	@Override
	public boolean setDados() {
		classe.setEmpEmpresaId(Integer.valueOf(hdnId.getValueAsString()));
		classe.setEmpEntidade(entidade);

		boolean retorno = super.setDados();
		List<EmpPlano> planos = new ArrayList<EmpPlano>();
		if (!gridPlano.validar(planos)) {
			retorno = false;
			tabDados.setActiveItem(2);
			new ToastWindow(OpenSigCore.i18n.txtListagem(), OpenSigCore.i18n.errLista()).show();
		}
		classe.setEmPlano(planos);
		return retorno;
	}

	@Override
	public void mostrarDados() {
		super.mostrarDados();
		Record rec = lista.getPanel().getSelectionModel().getSelected();

		if (rec != null) {
			FiltroObjeto fo = new FiltroObjeto("empEmpresa", ECompara.IGUAL, new EmpEmpresa(rec.getAsInteger("empEmpresaId")));
			gridPlano.getProxy().setFiltroPadrao(fo);
			gridPlano.getStore().reload();
		}
	}

	public ListagemPlano getGridPlano() {
		return gridPlano;
	}

	public void setGridPlano(ListagemPlano gridPlano) {
		this.gridPlano = gridPlano;
	}

}
