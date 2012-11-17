package br.com.opensig.comercial.server.acao;

import br.com.opensig.comercial.shared.modelo.ComFrete;
import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.FiltroNumero;
import br.com.opensig.core.client.controlador.parametro.ParametroBinario;
import br.com.opensig.core.client.padroes.Chain;
import br.com.opensig.core.client.servico.OpenSigException;
import br.com.opensig.core.server.CoreServiceImpl;
import br.com.opensig.core.shared.modelo.EComando;
import br.com.opensig.core.shared.modelo.Sql;

public class FecharFrete extends Chain {

	private CoreServiceImpl servico;
	private ComFrete frete;

	public FecharFrete(Chain next, CoreServiceImpl servico, ComFrete frete) throws OpenSigException {
		super(next);
		this.servico = servico;
		this.frete = frete;
	}

	@Override
	public void execute() throws OpenSigException {
		// atualiza o status para fechada
		FiltroNumero fn = new FiltroNumero("comFreteId", ECompara.IGUAL, frete.getId());
		ParametroBinario pb = new ParametroBinario("comFreteFechada", 1);
		Sql sql = new Sql(frete, EComando.ATUALIZAR, fn, pb);
		servico.executar(new Sql[] { sql });

		if (next != null) {
			next.execute();
		}
	}
}
