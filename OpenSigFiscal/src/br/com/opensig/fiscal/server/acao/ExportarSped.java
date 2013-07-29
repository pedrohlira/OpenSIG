package br.com.opensig.fiscal.server.acao;

import br.com.opensig.core.client.servico.CoreService;
import br.com.opensig.core.server.UtilServer;
import br.com.opensig.core.server.exportar.AExportacao;
import br.com.opensig.core.shared.modelo.ExpListagem;
import br.com.opensig.core.shared.modelo.ExpRegistro;
import br.com.opensig.core.shared.modelo.sistema.SisExpImp;
import br.com.opensig.fiscal.shared.modelo.FisSped;

public class ExportarSped extends AExportacao<FisSped> {

	@Override
	public String getArquivo(CoreService<FisSped> service, SisExpImp modo, ExpListagem<FisSped> exp, String[][] enderecos, String[][] contatos) {
		return getArquivo(exp.getClasse(), exp.getNome(), modo);
	}

	@Override
	public String getArquivo(CoreService<FisSped> service, SisExpImp modo, ExpRegistro<FisSped> exp, String[][] enderecos, String[][] contatos) {
		return getArquivo(exp.getClasse(), exp.getNome(), modo);
	}

	private String getArquivo(FisSped sped, String nome, SisExpImp modo) {
		String cnpj = auth.getEmpresa()[5].replaceAll("\\D", "");
		return UtilServer.PATH_EMPRESA + cnpj + "/sped/" + nome + "." + modo.getSisExpImpExtensoes();
	}
}
