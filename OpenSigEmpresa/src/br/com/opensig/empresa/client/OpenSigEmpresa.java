package br.com.opensig.empresa.client;

import java.util.ArrayList;
import java.util.Collection;

import br.com.opensig.core.client.controlador.comando.FabricaComando;
import br.com.opensig.core.client.controlador.comando.IComando;
import br.com.opensig.core.client.controlador.comando.lista.ComandoEditarFiltrados;
import br.com.opensig.core.client.visao.Ponte;
import br.com.opensig.empresa.client.controlador.comando.ComandoCliente;
import br.com.opensig.empresa.client.controlador.comando.ComandoContatoTipo;
import br.com.opensig.empresa.client.controlador.comando.ComandoEmpresa;
import br.com.opensig.empresa.client.controlador.comando.ComandoEnderecoTipo;
import br.com.opensig.empresa.client.controlador.comando.ComandoEstado;
import br.com.opensig.empresa.client.controlador.comando.ComandoFornecedor;
import br.com.opensig.empresa.client.controlador.comando.ComandoFuncionario;
import br.com.opensig.empresa.client.controlador.comando.ComandoMunicipio;
import br.com.opensig.empresa.client.controlador.comando.ComandoPais;
import br.com.opensig.empresa.client.controlador.comando.ComandoTransportadora;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;

/**
 * Classe que inicializa o módulo SigEmpresa.
 * @author Pedro H. Lira
 * @since 08/06/2009
 * @version 1.0
 */
public class OpenSigEmpresa implements EntryPoint {

    /**
     * Metodo que é disparado ao iniciar o projeto que contém este módulo.
     * Usar este método para adicionar as classes de comando na fábrica de comandos.
     */
    public void onModuleLoad() {
        FabricaComando fc = FabricaComando.getInstancia();
        fc.addComando(ComandoEmpresa.class.getName(), (IComando) GWT.create(ComandoEmpresa.class));
        fc.addComando(ComandoFuncionario.class.getName(), (IComando) GWT.create(ComandoFuncionario.class));
        fc.addComando(ComandoCliente.class.getName(), (IComando) GWT.create(ComandoCliente.class));
        fc.addComando(ComandoFornecedor.class.getName(), (IComando) GWT.create(ComandoFornecedor.class));
        fc.addComando(ComandoTransportadora.class.getName(), (IComando) GWT.create(ComandoTransportadora.class));
        fc.addComando(ComandoContatoTipo.class.getName(), (IComando) GWT.create(ComandoContatoTipo.class));
        fc.addComando(ComandoEnderecoTipo.class.getName(), (IComando) GWT.create(ComandoEnderecoTipo.class));
        fc.addComando(ComandoPais.class.getName(), (IComando) GWT.create(ComandoPais.class));
        fc.addComando(ComandoEstado.class.getName(), (IComando) GWT.create(ComandoEstado.class));
        fc.addComando(ComandoMunicipio.class.getName(), (IComando) GWT.create(ComandoMunicipio.class));
        
		// acoes proibidas dos cadastros comuns
		Collection<Class> acoes = new ArrayList<Class>();
		acoes.add(ComandoEditarFiltrados.class);
		
		Ponte.setAcoesProibidas(ComandoEmpresa.class.getName(), acoes);
		Ponte.setAcoesProibidas(ComandoFuncionario.class.getName(), acoes);
		Ponte.setAcoesProibidas(ComandoCliente.class.getName(), acoes);
		Ponte.setAcoesProibidas(ComandoFornecedor.class.getName(), acoes);
		Ponte.setAcoesProibidas(ComandoTransportadora.class.getName(), acoes);
    }
}
