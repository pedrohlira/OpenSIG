package br.com.opensig.financeiro.client.controlador.comando;

import java.util.Map;

import br.com.opensig.core.client.controlador.comando.ComandoFuncao;
import br.com.opensig.core.client.visao.FormularioVazio;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.financeiro.client.visao.lista.ListagemCartaoAuditoria;
import br.com.opensig.financeiro.shared.modelo.FinCartaoAuditoria;

public class ComandoCartaoAuditoria extends ComandoFuncao {

	public void execute(Map contexto) {
		DADOS = (SisFuncao) contexto.get("dados");
		FORM = new FormularioVazio<FinCartaoAuditoria>(new FinCartaoAuditoria(), DADOS);
		LISTA = new ListagemCartaoAuditoria(FORM);
		super.execute(contexto);
	}
}
