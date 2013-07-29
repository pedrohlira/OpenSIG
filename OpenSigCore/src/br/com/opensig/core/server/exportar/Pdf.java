package br.com.opensig.core.server.exportar;

import br.com.opensig.core.client.servico.CoreService;
import br.com.opensig.core.server.UtilServer;
import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.core.shared.modelo.ExpListagem;
import br.com.opensig.core.shared.modelo.ExpRegistro;
import br.com.opensig.core.shared.modelo.sistema.SisExpImp;

/**
 * Classe que define a exportacao de arquivo no formato de PDF.
 * 
 * @author Pedro H. Lira
 */
public class Pdf<E extends Dados> extends Html<E> {

	@Override
	public String getArquivo(CoreService<E> service, SisExpImp modo, ExpListagem<E> exp, String[][] enderecos, String[][] contatos) {
		String pathHtml = super.getArquivo(service, modo, exp, enderecos, contatos);
		return UtilServer.getPDF(pathHtml, formato);
	}

	@Override
	public String getArquivo(CoreService<E> service, SisExpImp modo, ExpRegistro<E> exp, String[][] enderecos, String[][] contatos) {
		String pathHtml = super.getArquivo(service, modo, exp, enderecos, contatos);
		return UtilServer.getPDF(pathHtml, formato);
	}
}
