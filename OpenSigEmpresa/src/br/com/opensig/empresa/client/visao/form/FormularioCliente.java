package br.com.opensig.empresa.client.visao.form;

import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.empresa.shared.modelo.EmpCliente;

import com.gwtext.client.widgets.form.Hidden;

public class FormularioCliente extends FormularioEntidade<EmpCliente> {

	public FormularioCliente(SisFuncao funcao) {
		super(new EmpCliente(), funcao, "empEntidade");
		inicializar();
	}

	public void inicializar() {
		hdnId = new Hidden("empClienteId", "0");
		add(hdnId);

		super.configurar();
		super.inicializar();
	}

	public boolean setDados() {
		classe.setEmpClienteId(Integer.valueOf(hdnId.getValueAsString()));
		classe.setEmpEntidade(entidade);
		return super.setDados();
	}
}
