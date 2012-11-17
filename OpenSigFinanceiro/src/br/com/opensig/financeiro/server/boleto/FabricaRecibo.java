package br.com.opensig.financeiro.server.boleto;


/**
 * Classe para recuparar o boleto de acordo com o tipo.
 * 
 * @author Pedro H. Lira
 * @since 13/04/2009
 * @version 1.0
 */
public class FabricaRecibo {

	private static final FabricaRecibo fb = new FabricaRecibo();

	private FabricaRecibo() {
	}

	/**
	 * Metodo que retorna a instancia da fábrica.
	 * 
	 * @return uma fábrica de exportação.
	 */
	public static FabricaRecibo getInstancia() {
		return fb;
	}

	/**
	 * Metodo que retorna uma classe de boleto informado o tipo.
	 * 
	 * @param tipo
	 *            da classe que representa a exportacao.
	 * @return a exportacao propriemente dita.
	 */
	public IRecibo getRecibo(String tipo) {
		IRecibo boleto;

		if (tipo.equalsIgnoreCase("pdf")) {
			boleto = new ReciboPdf();
		} else {
			boleto = new ReciboHtml();
		}

		return boleto;
	}
}
