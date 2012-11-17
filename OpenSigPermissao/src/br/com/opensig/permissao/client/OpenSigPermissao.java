package br.com.opensig.permissao.client;

import java.util.ArrayList;
import java.util.Collection;

import br.com.opensig.core.client.controlador.comando.FabricaComando;
import br.com.opensig.core.client.controlador.comando.IComando;
import br.com.opensig.core.client.controlador.comando.lista.ComandoEditar;
import br.com.opensig.core.client.controlador.comando.lista.ComandoFavorito;
import br.com.opensig.core.client.visao.Ponte;
import br.com.opensig.permissao.client.controlador.comando.ComandoAcesso;
import br.com.opensig.permissao.client.controlador.comando.ComandoConfiguracao;
import br.com.opensig.permissao.client.controlador.comando.ComandoFavoritoImpl;
import br.com.opensig.permissao.client.controlador.comando.ComandoGrupo;
import br.com.opensig.permissao.client.controlador.comando.ComandoPermiteWS;
import br.com.opensig.permissao.client.controlador.comando.ComandoUsuario;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;

/**
 * Classe que inicializa o módulo sigpermissao.
 * 
 * @author Pedro H. Lira
 * @since 13/04/2009
 * @version 1.0
 */
public class OpenSigPermissao implements EntryPoint {

	/**
	 * Metodo que é disparado ao iniciar o projeto que contém este módulo. Usar
	 * este método para adicionar as classes de comando na fábrica de comandos.
	 */
	public void onModuleLoad() {
		FabricaComando fc = FabricaComando.getInstancia();
		fc.addComando(ComandoUsuario.class.getName(), (IComando) GWT.create(ComandoUsuario.class));
		fc.addComando(ComandoGrupo.class.getName(), (IComando) GWT.create(ComandoGrupo.class));
		fc.addComando(ComandoAcesso.class.getName(), (IComando) GWT.create(ComandoAcesso.class));
		fc.addComando(ComandoConfiguracao.class.getName(), (IComando) GWT.create(ComandoConfiguracao.class));
		fc.addComando(ComandoFavoritoImpl.class.getName(), (IComando) GWT.create(ComandoFavorito.class));
		fc.addComando(ComandoPermiteWS.class.getName(), (IComando) GWT.create(ComandoPermiteWS.class));
		
		// ações que não devem ter na função de acesso
		Collection<Class> acoes = new ArrayList<Class>();
		for (Class classe : Ponte.getAcoesPadroes()) {
			if (!classe.getName().equals(ComandoEditar.class.getName())) {
				acoes.add(classe);
			}
		}
		acoes.add(ComandoFavoritoImpl.class);
		Ponte.setAcoesProibidas(ComandoAcesso.class.getName(), acoes);
	}
}
