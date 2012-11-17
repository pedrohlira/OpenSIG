package br.com.opensig.core.client.visao;

import br.com.opensig.core.client.visao.abstrato.AFormulario;
import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;

import com.gwtext.client.widgets.form.Hidden;

/**
 * Classe usada para listagem que nao tem formularios.
 * 
 * @param <E>
 *            O tipo de dado usado pela listagem.
 * @author Pedro H. Lira
 * @version 1.0
 */
public class FormularioVazio<E extends Dados> extends AFormulario<E> {

	/**
	 * Construtor que recebe um objeto do tipo informado e a funcao que ativou.
	 * 
	 * @param classe
	 * @param funcao
	 */
	public FormularioVazio(E classe, SisFuncao funcao) {
		super(classe, funcao);
		inicializar();
		add(new Hidden("id", "0"));
	}

	@Override
	public boolean setDados() {
		return true;
	}

	@Override
	public void limparDados() {
		disable();
	}

	@Override
	public void gerarListas() {
	}

	@Override
	public void mostrarDados() {
	}
}
