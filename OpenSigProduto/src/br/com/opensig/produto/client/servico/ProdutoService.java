package br.com.opensig.produto.client.servico;

import java.util.List;

import br.com.opensig.core.client.servico.CoreService;
import br.com.opensig.produto.shared.modelo.ProdCategoria;
import br.com.opensig.produto.shared.modelo.ProdProduto;

public interface ProdutoService extends CoreService {
	
	public ProdProduto salvarProduto(ProdProduto produto, List<ProdCategoria> categorias) throws ProdutoException;
	
}
