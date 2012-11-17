package br.com.opensig.empresa.client.servico;

import br.com.opensig.core.client.servico.CoreService;
import br.com.opensig.core.shared.modelo.Dados;

public interface EmpresaService<E extends Dados> extends CoreService<E> {

	public E salvar(E dados) throws EmpresaException;

}
