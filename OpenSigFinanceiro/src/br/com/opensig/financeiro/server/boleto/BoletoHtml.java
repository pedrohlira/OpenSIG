package br.com.opensig.financeiro.server.boleto;

import org.jboleto.Banco;
import org.jboleto.JBoletoBean;
import org.jboleto.control.HtmlGenerator;

public class BoletoHtml implements IBoleto {
	
	public byte[] getBoleto(JBoletoBean bean, Banco banco) {
		HtmlGenerator boleto = new HtmlGenerator(bean, banco);
		boleto.addBoleto();
		return boleto.closeBoleto();
	}
}
