package br.com.opensig.empresa.client.visao.form;

import br.com.opensig.core.client.visao.Ponte;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.empresa.shared.modelo.EmpEmpresa;
import br.com.opensig.empresa.shared.modelo.EmpFuncionario;

import com.gwtext.client.widgets.form.Hidden;

public class FormularioFuncionario extends FormularioEntidade<EmpFuncionario> {

	public FormularioFuncionario(SisFuncao funcao) {
		super(new EmpFuncionario(), funcao, "empEntidade");
		inicializar();
	}

	public void inicializar() {
		hdnId = new Hidden("empFuncionarioId", "0");
		add(hdnId);

		super.configurar();
		super.inicializar();
	}

	public boolean setDados() {
		classe.setEmpFuncionarioId(Integer.valueOf(hdnId.getValueAsString()));
		classe.setEmpEntidade(entidade);
		classe.setEmpEmpresa(new EmpEmpresa(Ponte.getLogin().getEmpresaId()));
		return super.setDados();
	}
}
