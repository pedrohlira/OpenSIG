package br.com.opensig.empresa.client.visao.form;

import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.empresa.shared.modelo.EmpTransportadora;

import com.gwtext.client.widgets.form.Hidden;

public class FormularioTransportadora extends FormularioEntidade<EmpTransportadora> {

	public FormularioTransportadora(SisFuncao funcao) {
		super(new EmpTransportadora(), funcao, "empEntidade");
		inicializar();
	}

	public void inicializar() {
		hdnId = new Hidden("empTransportadoraId", "0");
		add(hdnId);

		super.configurar();
		super.inicializar();
	}

	public boolean setDados() {
		classe.setEmpTransportadoraId(Integer.valueOf(hdnId.getValueAsString()));
		classe.setEmpEntidade(entidade);
		return super.setDados();
	}
}
