package br.com.opensig.empresa.client.visao.form;

import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.empresa.shared.modelo.EmpFornecedor;

import com.gwtext.client.widgets.form.Hidden;

public class FormularioFornecedor extends FormularioEntidade<EmpFornecedor> {

	public FormularioFornecedor(SisFuncao funcao) {
		super(new EmpFornecedor(), funcao, "empEntidade");
		inicializar();
	}

	public void inicializar() {
		hdnId = new Hidden("empFornecedorId", "0");
		add(hdnId);

		super.configurar();
		super.inicializar();
	}

	public boolean setDados() {
		classe.setEmpFornecedorId(Integer.valueOf(hdnId.getValueAsString()));
		classe.setEmpEntidade(entidade);
		return super.setDados();
	}
}
