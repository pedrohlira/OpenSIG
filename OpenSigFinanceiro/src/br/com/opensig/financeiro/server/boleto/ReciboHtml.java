package br.com.opensig.financeiro.server.boleto;

import java.text.DateFormat;

import br.com.opensig.core.server.UtilServer;
import br.com.opensig.core.server.exportar.Html;
import br.com.opensig.financeiro.shared.modelo.FinRecebimento;

public class ReciboHtml extends Html implements IRecibo {

	public byte[] getRecibo(FinRecebimento boleto) {
		// inicio do arquivo
		StringBuffer sb = new StringBuffer("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\"><html xmlns='http://www.w3.org/1999/xhtml'>");
		// estilo do arquivo
		formato = "portrait";
		sb.append(getEstilo("recibo"));
		// cabecalho da empresa
		sb.append(getCabecalhoEmpresa());
		// inicio do registro
		sb.append("<table>");
		// cabeçalho do registro
		sb.append("RECIBO");
		// corpo do registro
		sb.append(getCorpoRegistro(boleto));
		// fim do registro
		sb.append("</table>");
		// fim do arquivo
		sb.append("</body></html>");
		// normaliza
		return UtilServer.normaliza(sb.toString()).getBytes();
	}

	public String getCorpoRegistro(FinRecebimento boleto) {
		StringBuffer sb = new StringBuffer("<tbody>");
		sb.append("<tr><td><h2>Referente ao " + boleto.getFinForma().getFinFormaDescricao() + " - " + UtilServer.formataNumero(boleto.getFinRecebimentoId(), 9, 0, false)
				+ "</h2><br /><br /></td></tr>");
		sb.append("<tr><td><b>Documento :: </b>" + boleto.getFinRecebimentoDocumento() + "<br /><br />");
		sb.append("<tr><td><b>Parcela :: </b>" + boleto.getFinRecebimentoParcela() + "<br /><br />");
		sb.append("<tr><td><b>Entidade :: </b>" + boleto.getFinReceber().getEmpEntidade().getEmpEntidadeNome1() + "<br /><br />");
		sb.append("<b>Data Vencimento :: </b>" + UtilServer.formataData(boleto.getFinRecebimentoVencimento(), DateFormat.MEDIUM) + "<br /><br />");
		sb.append("<b>Data Quitação :: </b>" + UtilServer.formataData(boleto.getFinRecebimentoRealizado(), DateFormat.MEDIUM) + "<br /><br />");
		sb.append("<b>Data Conciliação :: </b>" + UtilServer.formataData(boleto.getFinRecebimentoConciliado(), DateFormat.MEDIUM) + "<br /><br />");
		sb.append("<b>Valor :: </b>" + UtilServer.formataNumero(boleto.getFinRecebimentoValor(), 1, 2, true) + "<br /><br />");
		if (boleto.getFinRecebimentoObservacao() != null && !boleto.getFinRecebimentoObservacao().equals("")) {
			sb.append("<b>Observação :: </b>" + boleto.getFinRecebimentoObservacao() + "<br /><br />");
		}
		sb.append("<hr style='border:none;border-bottom:1px dashed' /></tr></td></tbody>");
		return sb.toString();
	}

}
