package br.com.opensig.fiscal.client.controlador.comando;

import java.util.Map;

import br.com.opensig.core.client.controlador.comando.ComandoFuncao;
import br.com.opensig.core.client.visao.FormularioVazio;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.fiscal.client.visao.lista.ListagemSpedFiscal;
import br.com.opensig.fiscal.shared.modelo.FisSpedFiscal;

public class ComandoSpedFiscal extends ComandoFuncao {

	public void execute(Map contexto) {
		DADOS = (SisFuncao) contexto.get("dados");
		FORM = new FormularioVazio<FisSpedFiscal>(new FisSpedFiscal(), DADOS);
		LISTA = new ListagemSpedFiscal(FORM);
		super.execute(contexto);
	}
}
