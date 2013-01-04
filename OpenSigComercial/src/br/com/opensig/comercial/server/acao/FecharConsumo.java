package br.com.opensig.comercial.server.acao;

import br.com.opensig.comercial.shared.modelo.ComConsumo;
import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.FiltroNumero;
import br.com.opensig.core.client.controlador.parametro.ParametroBinario;
import br.com.opensig.core.client.padroes.Chain;
import br.com.opensig.core.client.servico.OpenSigException;
import br.com.opensig.core.server.CoreServiceImpl;
import br.com.opensig.core.shared.modelo.EComando;
import br.com.opensig.core.shared.modelo.Sql;

public class FecharConsumo extends Chain {

	private CoreServiceImpl servico;
	private ComConsumo consumo;

	public FecharConsumo(Chain next, CoreServiceImpl servico, ComConsumo consumo) throws OpenSigException {
		super(next);
		this.servico = servico;
		this.consumo = consumo;
	}

	@Override
	public void execute() throws OpenSigException {
		// atualiza o status para fechada
		FiltroNumero fn = new FiltroNumero("comConsumoId", ECompara.IGUAL, consumo.getId());
		ParametroBinario pb = new ParametroBinario("comConsumoFechada", 1);
		Sql sql = new Sql(consumo, EComando.ATUALIZAR, fn, pb);
		servico.executar(new Sql[] { sql });

		if (next != null) {
			next.execute();
		}
	}
}
