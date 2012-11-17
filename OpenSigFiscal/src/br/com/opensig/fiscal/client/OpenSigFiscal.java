package br.com.opensig.fiscal.client;

import java.util.ArrayList;
import java.util.Collection;

import br.com.opensig.core.client.controlador.comando.FabricaComando;
import br.com.opensig.core.client.controlador.comando.IComando;
import br.com.opensig.core.client.controlador.comando.lista.ComandoEditar;
import br.com.opensig.core.client.controlador.comando.lista.ComandoEditarFiltrados;
import br.com.opensig.core.client.controlador.comando.lista.ComandoExcluirFiltrados;
import br.com.opensig.core.client.controlador.comando.lista.ComandoFavorito;
import br.com.opensig.core.client.controlador.comando.lista.ComandoImportar;
import br.com.opensig.core.client.controlador.comando.lista.ComandoNovo;
import br.com.opensig.core.client.controlador.comando.lista.ComandoNovoDuplicar;
import br.com.opensig.core.client.controlador.comando.lista.ComandoPermiteEmpresa;
import br.com.opensig.core.client.controlador.comando.lista.ComandoPermiteUsuario;
import br.com.opensig.core.client.controlador.comando.lista.ComandoVisualizar;
import br.com.opensig.core.client.visao.Ponte;
import br.com.opensig.fiscal.client.controlador.comando.ComandoCadastro;
import br.com.opensig.fiscal.client.controlador.comando.ComandoCertificado;
import br.com.opensig.fiscal.client.controlador.comando.ComandoEntrada;
import br.com.opensig.fiscal.client.controlador.comando.ComandoIncentivo;
import br.com.opensig.fiscal.client.controlador.comando.ComandoSaida;
import br.com.opensig.fiscal.client.controlador.comando.ComandoSefaz;
import br.com.opensig.fiscal.client.controlador.comando.ComandoSituacao;
import br.com.opensig.fiscal.client.controlador.comando.ComandoSpedFiscal;
import br.com.opensig.fiscal.client.controlador.comando.ComandoStatus;
import br.com.opensig.fiscal.client.controlador.comando.acao.ComandoBackupEntrada;
import br.com.opensig.fiscal.client.controlador.comando.acao.ComandoBackupSaida;
import br.com.opensig.fiscal.client.controlador.comando.acao.ComandoInutilizarEntrada;
import br.com.opensig.fiscal.client.controlador.comando.acao.ComandoInutilizarSaida;
import br.com.opensig.fiscal.client.controlador.comando.acao.ComandoValidar;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;

/**
 * Classe que inicializa o modulo SigFiscal.
 * 
 * @author Pedro H. Lira
 * @since 20/07/2010
 * @version 1.0
 */
public class OpenSigFiscal implements EntryPoint {

	/**
	 * Metodo que é disparado ao iniciar o projeto que contém este módulo. Usar este método para adicionar as classes de comando na fábrica de comandos.
	 */
	public void onModuleLoad() {
		FabricaComando fc = FabricaComando.getInstancia();
		// funcoes
		fc.addComando(ComandoEntrada.class.getName(), (IComando) GWT.create(ComandoEntrada.class));
		fc.addComando(ComandoSaida.class.getName(), (IComando) GWT.create(ComandoSaida.class));
		fc.addComando(ComandoCertificado.class.getName(), (IComando) GWT.create(ComandoCertificado.class));
		fc.addComando(ComandoSefaz.class.getName(), (IComando) GWT.create(ComandoSefaz.class));
		fc.addComando(ComandoStatus.class.getName(), (IComando) GWT.create(ComandoStatus.class));
		fc.addComando(ComandoSituacao.class.getName(), (IComando) GWT.create(ComandoSituacao.class));
		fc.addComando(ComandoCadastro.class.getName(), (IComando) GWT.create(ComandoCadastro.class));
		fc.addComando(ComandoIncentivo.class.getName(), (IComando) GWT.create(ComandoIncentivo.class));
		fc.addComando(ComandoSpedFiscal.class.getName(), (IComando) GWT.create(ComandoSpedFiscal.class));

		// acoes
		fc.addComando(ComandoBackupSaida.class.getName(), (IComando) GWT.create(ComandoBackupSaida.class));
		fc.addComando(ComandoBackupEntrada.class.getName(), (IComando) GWT.create(ComandoBackupEntrada.class));
		fc.addComando(ComandoInutilizarSaida.class.getName(), (IComando) GWT.create(ComandoInutilizarSaida.class));
		fc.addComando(ComandoInutilizarEntrada.class.getName(), (IComando) GWT.create(ComandoInutilizarEntrada.class));
		fc.addComando(ComandoValidar.class.getName(), (IComando) GWT.create(ComandoValidar.class));

		// acoes proibidas do certificado
		Collection<Class> acoes = new ArrayList<Class>();
		acoes.add(ComandoEditar.class);
		acoes.add(ComandoEditarFiltrados.class);
		acoes.add(ComandoNovoDuplicar.class);
		acoes.add(ComandoImportar.class);

		// acoes proibidas para notas
		Collection<Class> acoes2 = new ArrayList<Class>();
		acoes2.add(ComandoNovo.class);
		acoes2.add(ComandoEditar.class);
		acoes2.add(ComandoEditarFiltrados.class);
		acoes2.add(ComandoNovoDuplicar.class);

		// acoes proibidas para sped fiscais
		Collection<Class> acoes3 = new ArrayList<Class>();
		acoes3.add(ComandoEditarFiltrados.class);
		acoes3.add(ComandoNovoDuplicar.class);
		acoes3.add(ComandoExcluirFiltrados.class);

		// acoes proibidas para sefaz e internos
		Collection<Class> acoes4 = new ArrayList<Class>();
		for (Class classe : Ponte.getAcoesPadroes()) {
			if (!classe.getName().equals(ComandoVisualizar.class.getName())) {
				acoes4.add(classe);
			}
		}
		acoes4.add(ComandoFavorito.class);
		acoes4.add(ComandoPermiteUsuario.class);
		acoes4.add(ComandoPermiteEmpresa.class);

		Ponte.setAcoesProibidas(ComandoEntrada.class.getName(), acoes2);
		Ponte.setAcoesProibidas(ComandoSaida.class.getName(), acoes2);
		Ponte.setAcoesProibidas(ComandoSpedFiscal.class.getName(), acoes3);
		Ponte.setAcoesProibidas(ComandoCertificado.class.getName(), acoes);
		Ponte.setAcoesProibidas(ComandoSefaz.class.getName(), acoes4);
		Ponte.setAcoesProibidas(ComandoStatus.class.getName(), acoes4);
		Ponte.setAcoesProibidas(ComandoSituacao.class.getName(), acoes4);
		Ponte.setAcoesProibidas(ComandoCadastro.class.getName(), acoes4);
	}
}
