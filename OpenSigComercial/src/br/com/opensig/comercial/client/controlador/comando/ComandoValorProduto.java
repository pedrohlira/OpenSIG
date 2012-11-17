package br.com.opensig.comercial.client.controlador.comando;

import java.util.Map;

import br.com.opensig.comercial.client.visao.form.FormularioValorProduto;
import br.com.opensig.comercial.client.visao.lista.ListagemValorProduto;
import br.com.opensig.core.client.controlador.comando.ComandoFuncao;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;


public class ComandoValorProduto extends ComandoFuncao {

	public void execute(Map contexto) {
		DADOS = (SisFuncao) contexto.get("dados");
		FORM = new FormularioValorProduto(DADOS);
		LISTA = new ListagemValorProduto(FORM);
		super.execute(contexto);
	}
}
