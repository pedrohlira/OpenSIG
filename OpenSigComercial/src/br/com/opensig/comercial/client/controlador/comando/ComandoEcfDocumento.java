package br.com.opensig.comercial.client.controlador.comando;

import java.util.Map;

import br.com.opensig.comercial.client.visao.lista.ListagemEcfDocumento;
import br.com.opensig.comercial.shared.modelo.ComEcfDocumento;
import br.com.opensig.core.client.controlador.comando.ComandoFuncao;
import br.com.opensig.core.client.visao.FormularioVazio;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;

public class ComandoEcfDocumento extends ComandoFuncao {

	public void execute(Map contexto) {
		DADOS = (SisFuncao) contexto.get("dados");
		FORM = new FormularioVazio<ComEcfDocumento>(new ComEcfDocumento(), DADOS);
		LISTA = new ListagemEcfDocumento(FORM);
		super.execute(contexto);
	}
}
