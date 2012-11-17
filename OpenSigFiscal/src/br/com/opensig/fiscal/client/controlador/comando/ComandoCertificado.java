package br.com.opensig.fiscal.client.controlador.comando;

import java.util.Map;

import br.com.opensig.core.client.controlador.comando.ComandoFuncao;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.fiscal.client.visao.form.FormularioCertificado;
import br.com.opensig.fiscal.client.visao.lista.ListagemCertificado;

public class ComandoCertificado extends ComandoFuncao {

	public void execute(Map contexto) {
		DADOS = (SisFuncao) contexto.get("dados");
		FORM = new FormularioCertificado(DADOS);
		LISTA = new ListagemCertificado(FORM);
		super.execute(contexto);
	}
}
