package br.com.opensig.produto.server;

import java.util.List;

import br.com.opensig.core.server.CoreServiceImpl;
import br.com.opensig.core.server.UtilServer;
import br.com.opensig.core.shared.modelo.Autenticacao;
import br.com.opensig.produto.client.servico.ProdutoException;
import br.com.opensig.produto.client.servico.ProdutoService;
import br.com.opensig.produto.server.acao.SalvarProduto;
import br.com.opensig.produto.shared.modelo.ProdCategoria;
import br.com.opensig.produto.shared.modelo.ProdProduto;

public class ProdutoServiceImpl extends CoreServiceImpl implements ProdutoService {

	public ProdutoServiceImpl(){
	}
	
	public ProdutoServiceImpl(Autenticacao auth){
		super(auth);
	}
	
	public ProdProduto salvarProduto(ProdProduto produto, List<ProdCategoria> categorias) throws ProdutoException {
		try {
			new SalvarProduto(null, this, produto, categorias).execute();
			produto.anularDependencia();
			return produto;
		} catch (Exception ex) {
			UtilServer.LOG.error("Erro ao salvar o produto", ex);
			throw new ProdutoException(ex.getMessage());
		}		
	}

}
