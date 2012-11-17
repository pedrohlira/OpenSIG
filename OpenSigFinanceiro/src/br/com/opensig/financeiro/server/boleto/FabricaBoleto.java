package br.com.opensig.financeiro.server.boleto;


/**
 * Classe para recuparar o boleto de acordo com o tipo.
 * 
 * @author Pedro H. Lira
 * @since 13/04/2009
 * @version 1.0
 */
public class FabricaBoleto {

	private static final FabricaBoleto fb = new FabricaBoleto();

	private FabricaBoleto() {
	}

	/**
	 * Metodo que retorna a instancia da fábrica.
	 * 
	 * @return uma fábrica de exportação.
	 */
	public static FabricaBoleto getInstancia() {
		return fb;
	}

	/**
	 * Metodo que retorna uma classe de boleto informado o tipo.
	 * 
	 * @param tipo
	 *            da classe que representa a exportacao.
	 * @return a exportacao propriemente dita.
	 */
	public IBoleto getBoleto(String tipo) {
		IBoleto boleto;

		if (tipo.equalsIgnoreCase("pdf")) {
			boleto = new BoletoPdf();
		} else {
			boleto = new BoletoHtml();
		}

		return boleto;
	}
}
