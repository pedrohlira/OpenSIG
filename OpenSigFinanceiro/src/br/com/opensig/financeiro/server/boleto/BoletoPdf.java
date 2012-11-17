package br.com.opensig.financeiro.server.boleto;

import org.jboleto.Banco;
import org.jboleto.JBoletoBean;

import br.com.opensig.core.server.UtilServer;

public class BoletoPdf extends BoletoHtml {

	public byte[] getBoleto(JBoletoBean bean, Banco banco) {
		byte[] obj = super.getBoleto(bean, banco);
		return UtilServer.getPDF(obj, "portrait");
	}
}
