package br.com.opensig.fiscal.server.acao;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import br.com.opensig.core.client.servico.CoreService;
import br.com.opensig.core.server.UtilServer;
import br.com.opensig.core.server.exportar.AExportacao;
import br.com.opensig.core.shared.modelo.ExpListagem;
import br.com.opensig.core.shared.modelo.ExpRegistro;
import br.com.opensig.core.shared.modelo.sistema.SisExpImp;
import br.com.opensig.fiscal.shared.modelo.FisSped;

public class ExportarSped extends AExportacao<FisSped> {

	@Override
	public byte[] getArquivo(CoreService<FisSped> service, SisExpImp modo, ExpListagem<FisSped> exp, String[][] enderecos, String[][] contatos) {
		return getArquivo(exp.getClasse(), exp.getNome(), modo);
	}

	@Override
	public byte[] getArquivo(CoreService<FisSped> service, SisExpImp modo, ExpRegistro<FisSped> exp, String[][] enderecos, String[][] contatos) {
		return getArquivo(exp.getClasse(), exp.getNome(), modo);
	}

	private byte[] getArquivo(FisSped sped, String nome, SisExpImp modo) {
		byte[] obj = null;

		try {
			String cnpj = auth.getEmpresa()[5].replaceAll("\\D", "");
			String arquivo = UtilServer.PATH_EMPRESA + cnpj + "/sped/" + nome + "." + modo.getSisExpImpExtensoes();
			File arq = new File(arquivo);

			if (arq.exists()) {
				InputStream is = new FileInputStream(arq);
				obj = new byte[is.available()];
				is.read(obj);
				is.close();
			}
		} catch (Exception e) {
			obj = null;
		}

		return obj;
	}
}
