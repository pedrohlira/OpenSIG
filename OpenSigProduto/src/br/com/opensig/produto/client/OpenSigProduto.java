package br.com.opensig.produto.client;

import java.util.ArrayList;
import java.util.Collection;

import br.com.opensig.core.client.controlador.comando.FabricaComando;
import br.com.opensig.core.client.controlador.comando.IComando;
import br.com.opensig.core.client.controlador.comando.lista.ComandoEditar;
import br.com.opensig.core.client.controlador.comando.lista.ComandoEditarFiltrados;
import br.com.opensig.core.client.controlador.comando.lista.ComandoExcluir;
import br.com.opensig.core.client.controlador.comando.lista.ComandoExcluirFiltrados;
import br.com.opensig.core.client.controlador.comando.lista.ComandoFavorito;
import br.com.opensig.core.client.controlador.comando.lista.ComandoNovo;
import br.com.opensig.core.client.controlador.comando.lista.ComandoNovoDuplicar;
import br.com.opensig.core.client.controlador.comando.lista.ComandoPermiteEmpresa;
import br.com.opensig.core.client.controlador.comando.lista.ComandoPermiteUsuario;
import br.com.opensig.core.client.controlador.comando.lista.ComandoVisualizar;
import br.com.opensig.core.client.visao.Ponte;
import br.com.opensig.produto.client.controlador.comando.ComandoCofins;
import br.com.opensig.produto.client.controlador.comando.ComandoEmbalagem;
import br.com.opensig.produto.client.controlador.comando.ComandoGrade;
import br.com.opensig.produto.client.controlador.comando.ComandoGradeTipo;
import br.com.opensig.produto.client.controlador.comando.ComandoIcms;
import br.com.opensig.produto.client.controlador.comando.ComandoIpi;
import br.com.opensig.produto.client.controlador.comando.ComandoOrigem;
import br.com.opensig.produto.client.controlador.comando.ComandoPesquisa;
import br.com.opensig.produto.client.controlador.comando.ComandoPis;
import br.com.opensig.produto.client.controlador.comando.ComandoProduto;
import br.com.opensig.produto.client.controlador.comando.ComandoTipo;
import br.com.opensig.produto.client.controlador.comando.ComandoTributacao;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;

/**
 * Classe que inicializa o módulo SigProduto.
 * 
 * @author Pedro H. Lira
 */
public class OpenSigProduto implements EntryPoint {

	/**
	 * Metodo que é disparado ao iniciar o projeto que contém este módulo. Usar
	 * este método para adicionar as classes de comando na fábrica de comandos.
	 */

	public void onModuleLoad() {
		FabricaComando fc = FabricaComando.getInstancia();
		fc.addComando(ComandoProduto.class.getName(), (IComando) GWT.create(ComandoProduto.class));
		fc.addComando(ComandoTributacao.class.getName(), (IComando) GWT.create(ComandoTributacao.class));
		fc.addComando(ComandoIcms.class.getName(), (IComando) GWT.create(ComandoIcms.class));
		fc.addComando(ComandoIpi.class.getName(), (IComando) GWT.create(ComandoIpi.class));
		fc.addComando(ComandoPis.class.getName(), (IComando) GWT.create(ComandoPis.class));
		fc.addComando(ComandoCofins.class.getName(), (IComando) GWT.create(ComandoCofins.class));
		fc.addComando(ComandoGrade.class.getName(), (IComando) GWT.create(ComandoGrade.class));
		fc.addComando(ComandoGradeTipo.class.getName(), (IComando) GWT.create(ComandoGradeTipo.class));
		fc.addComando(ComandoPesquisa.class.getName(), (IComando) GWT.create(ComandoPesquisa.class));
		fc.addComando(ComandoEmbalagem.class.getName(), (IComando) GWT.create(ComandoEmbalagem.class));
		fc.addComando(ComandoTipo.class.getName(), (IComando) GWT.create(ComandoTipo.class));
		fc.addComando(ComandoOrigem.class.getName(), (IComando) GWT.create(ComandoOrigem.class));

		// ações que não devem ter na função de pesquisa
		Collection<Class> acoes = new ArrayList<Class>();
		for (Class classe : Ponte.getAcoesPadroes()) {
			if (!classe.getName().equals(ComandoVisualizar.class.getName())) {
				acoes.add(classe);
			}
		}
		acoes.add(ComandoFavorito.class);
		acoes.add(ComandoPermiteUsuario.class);
		acoes.add(ComandoPermiteEmpresa.class);

		// acoes proibidas para venda das ecf
		Collection<Class> acoes2 = new ArrayList<Class>();
		acoes2.add(ComandoNovo.class);
		acoes2.add(ComandoNovoDuplicar.class);
		acoes2.add(ComandoEditar.class);
		acoes2.add(ComandoEditarFiltrados.class);
		acoes2.add(ComandoExcluir.class);
		acoes2.add(ComandoExcluirFiltrados.class);

		Ponte.setAcoesProibidas(ComandoPesquisa.class.getName(), acoes);
		Ponte.setAcoesProibidas(ComandoGrade.class.getName(), acoes2);
	}
}
