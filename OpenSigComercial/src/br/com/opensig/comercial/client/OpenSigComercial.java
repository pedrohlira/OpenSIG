package br.com.opensig.comercial.client;

import java.util.ArrayList;
import java.util.Collection;

import br.com.opensig.comercial.client.controlador.comando.ComandoCompra;
import br.com.opensig.comercial.client.controlador.comando.ComandoCompraProduto;
import br.com.opensig.comercial.client.controlador.comando.ComandoEcf;
import br.com.opensig.comercial.client.controlador.comando.ComandoEcfDocumento;
import br.com.opensig.comercial.client.controlador.comando.ComandoEcfNota;
import br.com.opensig.comercial.client.controlador.comando.ComandoEcfNotaProduto;
import br.com.opensig.comercial.client.controlador.comando.ComandoEcfVenda;
import br.com.opensig.comercial.client.controlador.comando.ComandoEcfVendaProduto;
import br.com.opensig.comercial.client.controlador.comando.ComandoEcfZ;
import br.com.opensig.comercial.client.controlador.comando.ComandoFrete;
import br.com.opensig.comercial.client.controlador.comando.ComandoNatureza;
import br.com.opensig.comercial.client.controlador.comando.ComandoValorProduto;
import br.com.opensig.comercial.client.controlador.comando.ComandoVenda;
import br.com.opensig.comercial.client.controlador.comando.ComandoVendaProduto;
import br.com.opensig.comercial.client.controlador.comando.acao.ComandoFecharCompra;
import br.com.opensig.comercial.client.controlador.comando.acao.ComandoFecharEcfVenda;
import br.com.opensig.comercial.client.controlador.comando.acao.ComandoFecharFrete;
import br.com.opensig.comercial.client.controlador.comando.acao.ComandoFecharVenda;
import br.com.opensig.comercial.client.controlador.comando.acao.ComandoGerarNfeEntrada;
import br.com.opensig.comercial.client.controlador.comando.acao.ComandoGerarNfeSaida;
import br.com.opensig.comercial.client.controlador.comando.acao.ComandoPagar;
import br.com.opensig.comercial.client.controlador.comando.acao.ComandoPagarFrete;
import br.com.opensig.comercial.client.controlador.comando.acao.ComandoReceber;
import br.com.opensig.core.client.controlador.comando.FabricaComando;
import br.com.opensig.core.client.controlador.comando.IComando;
import br.com.opensig.core.client.controlador.comando.lista.ComandoEditar;
import br.com.opensig.core.client.controlador.comando.lista.ComandoEditarFiltrados;
import br.com.opensig.core.client.controlador.comando.lista.ComandoExcluir;
import br.com.opensig.core.client.controlador.comando.lista.ComandoExcluirFiltrados;
import br.com.opensig.core.client.controlador.comando.lista.ComandoNovo;
import br.com.opensig.core.client.controlador.comando.lista.ComandoNovoDuplicar;
import br.com.opensig.core.client.visao.Ponte;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;

/**
 * Classe que inicializa o módulo OpenSigComercial.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 */
public class OpenSigComercial implements EntryPoint {

	/**
	 * Metodo que é disparado ao iniciar o projeto que contém este módulo. Usar este método para adicionar as classes de comando na fábrica de comandos.
	 */
	public void onModuleLoad() {
		FabricaComando fc = FabricaComando.getInstancia();
		// compra
		fc.addComando(ComandoCompra.class.getName(), (IComando) GWT.create(ComandoCompra.class));
		fc.addComando(ComandoCompraProduto.class.getName(), (IComando) GWT.create(ComandoCompraProduto.class));
		fc.addComando(ComandoFecharCompra.class.getName(), (IComando) GWT.create(ComandoFecharCompra.class));
		fc.addComando(ComandoPagar.class.getName(), (IComando) GWT.create(ComandoPagar.class));
		fc.addComando(ComandoGerarNfeEntrada.class.getName(), (IComando) GWT.create(ComandoGerarNfeEntrada.class));

		// venda
		fc.addComando(ComandoVenda.class.getName(), (IComando) GWT.create(ComandoVenda.class));
		fc.addComando(ComandoVendaProduto.class.getName(), (IComando) GWT.create(ComandoVendaProduto.class));
		fc.addComando(ComandoFecharVenda.class.getName(), (IComando) GWT.create(ComandoFecharVenda.class));
		fc.addComando(ComandoReceber.class.getName(), (IComando) GWT.create(ComandoReceber.class));
		fc.addComando(ComandoGerarNfeSaida.class.getName(), (IComando) GWT.create(ComandoGerarNfeSaida.class));

		// ecf
		fc.addComando(ComandoEcf.class.getName(), (IComando) GWT.create(ComandoEcf.class));
		fc.addComando(ComandoEcfZ.class.getName(), (IComando) GWT.create(ComandoEcfZ.class));
		fc.addComando(ComandoEcfVenda.class.getName(), (IComando) GWT.create(ComandoEcfVenda.class));
		fc.addComando(ComandoEcfVendaProduto.class.getName(), (IComando) GWT.create(ComandoEcfVendaProduto.class));
		fc.addComando(ComandoEcfNota.class.getName(), (IComando) GWT.create(ComandoEcfNota.class));
		fc.addComando(ComandoEcfNotaProduto.class.getName(), (IComando) GWT.create(ComandoEcfNotaProduto.class));
		fc.addComando(ComandoEcfDocumento.class.getName(), (IComando) GWT.create(ComandoEcfDocumento.class));
		fc.addComando(ComandoFecharEcfVenda.class.getName(), (IComando) GWT.create(ComandoFecharEcfVenda.class));

		// frete
		fc.addComando(ComandoFrete.class.getName(), (IComando) GWT.create(ComandoFrete.class));
		fc.addComando(ComandoFecharFrete.class.getName(), (IComando) GWT.create(ComandoFecharFrete.class));
		fc.addComando(ComandoPagarFrete.class.getName(), (IComando) GWT.create(ComandoPagarFrete.class));

		// natureza
		fc.addComando(ComandoValorProduto.class.getName(), (IComando) GWT.create(ComandoValorProduto.class));
		fc.addComando(ComandoNatureza.class.getName(), (IComando) GWT.create(ComandoNatureza.class));

		// acoes proibidas para venda das ecf
		Collection<Class> acoes = new ArrayList<Class>();
		acoes.add(ComandoNovo.class);
		acoes.add(ComandoNovoDuplicar.class);
		acoes.add(ComandoEditarFiltrados.class);
		acoes.add(ComandoExcluir.class);
		acoes.add(ComandoExcluirFiltrados.class);

		// acoes proibidas para os produtos do comercial
		Collection<Class> acoes2 = new ArrayList<Class>(acoes);
		acoes2.add(ComandoEditar.class);

		Ponte.setAcoesProibidas(ComandoEcfZ.class.getName(), acoes);
		Ponte.setAcoesProibidas(ComandoEcfVenda.class.getName(), acoes);
		Ponte.setAcoesProibidas(ComandoEcfDocumento.class.getName(), acoes2);
		Ponte.setAcoesProibidas(ComandoEcfNota.class.getName(), acoes2);
		Ponte.setAcoesProibidas(ComandoCompraProduto.class.getName(), acoes2);
		Ponte.setAcoesProibidas(ComandoVendaProduto.class.getName(), acoes2);
		Ponte.setAcoesProibidas(ComandoEcfVendaProduto.class.getName(), acoes2);
		Ponte.setAcoesProibidas(ComandoEcfNotaProduto.class.getName(), acoes2);
	}
}
