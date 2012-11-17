package br.com.opensig.core.server.importar;

import java.util.List;
import java.util.Map;

import br.com.opensig.core.client.servico.OpenSigException;
import br.com.opensig.core.shared.modelo.Autenticacao;
import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.core.shared.modelo.sistema.SisExpImp;

public interface IImportacao<E extends Dados> {

	/**
	 * Metodo que importa o arquivo.
	 * 
	 * @param auth
	 *            login do usuario atual.
	 * @param arquivos
	 *            uma mapa com nome e lista de array de bytes que representa os dados a serem importados.
	 * @param modo
	 *            objeto que determina as configuracoes da importacao.
	 * @return um objeto do tipo especificado.
	 * @throws OpenSigException
	 *             caso tenha erro no reconhecimento dos dados.
	 */
	public Map<String, List<E>> setArquivo(Autenticacao auth, Map<String, byte[]> arquivos, SisExpImp modo) throws OpenSigException;
}
