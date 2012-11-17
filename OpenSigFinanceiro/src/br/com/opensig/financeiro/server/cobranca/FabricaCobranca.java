package br.com.opensig.financeiro.server.cobranca;

import org.jboleto.Banco;

import br.com.opensig.financeiro.shared.modelo.FinConta;

/**
 * Classe para recuparar a cobranca de acordo com o banco.
 * 
 * @author Pedro H. Lira
 * @since 13/04/2009
 * @version 1.0
 */
public class FabricaCobranca {

	private static final FabricaCobranca fb = new FabricaCobranca();

	private FabricaCobranca() {
	}

	/**
	 * Metodo que retorna a instancia da fábrica.
	 * 
	 * @return uma fábrica de exportação.
	 */
	public static FabricaCobranca getInstancia() {
		return fb;
	}

	/**
	 * Metodo que retorna uma classe de cobranca informado o banco.
	 * 
	 * @param conta
	 *            a conta da conbranca.
	 * @return o objeto de cobranca.
	 */
	public ICobranca getCobranca(FinConta conta) {
		ICobranca cobranca;
		int banco = Integer.valueOf(conta.getFinBanco().getFinBancoNumero());

		switch (banco) {
		case Banco.BANCO_DO_BRASIL:
			cobranca = new BancoBrasil(conta);
			break;
		case Banco.BRADESCO:
			cobranca = new Bradesco(conta);
			break;
		case Banco.ITAU:
			cobranca = new Itau(conta);
			break;
		case Banco.CAIXA_ECONOMICA:
			cobranca = new CaixaEconomica(conta);
			break;
		case Banco.HSBC:
			cobranca = new Hsbc(conta);
			break;
		case Banco.NOSSACAIXA:
			cobranca = new NossaCaixa(conta);
			break;
		case Banco.SANTANDER:
			cobranca = new Santander(conta);
			break;
		default:
			cobranca = null;
		}

		return cobranca;
	}
}
