package br.com.opensig.financeiro.server.boleto;

import org.jboleto.Banco;
import org.jboleto.JBoletoBean;

public interface IBoleto {
	
	public byte[] getBoleto(JBoletoBean bean, Banco banco);
}
