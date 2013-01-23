package br.com.opensig.comercial.server.acao;

import br.com.opensig.comercial.shared.modelo.ComEcfVenda;
import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.FiltroNumero;
import br.com.opensig.core.client.padroes.Chain;
import br.com.opensig.core.client.servico.OpenSigException;
import br.com.opensig.core.server.CoreServiceImpl;
import br.com.opensig.core.shared.modelo.Autenticacao;

public class CancelarEcfVenda extends Chain {

	private CoreServiceImpl servico;
	private ComEcfVenda venda;

	public CancelarEcfVenda(Chain next, CoreServiceImpl servico, ComEcfVenda venda, Autenticacao auth) throws OpenSigException {
		super(null);
		this.servico = servico;
		this.venda = venda;
		this.next = next;
	}

	@Override
	public void execute() throws OpenSigException {
		// seta a venda
		FiltroNumero fn = new FiltroNumero("comEcfVendaId", ECompara.IGUAL, venda.getId());
		venda = (ComEcfVenda) servico.selecionar(venda, fn, false);
		if (venda.getComEcfVendaProdutos() == null || venda.getComEcfVendaProdutos().isEmpty()) {
			venda.setComEcfVendaCancelada(true);
			servico.salvar(venda);
		} else {
			throw new OpenSigException("Nao pode cancelar uma venda de ecf contendo produtos.");
		}

		if (next != null) {
			next.execute();
		}
	}

}
