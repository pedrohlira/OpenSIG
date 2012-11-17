package br.com.opensig.financeiro.client;

import java.util.ArrayList;
import java.util.Collection;

import br.com.opensig.core.client.controlador.comando.FabricaComando;
import br.com.opensig.core.client.controlador.comando.IComando;
import br.com.opensig.core.client.controlador.comando.lista.ComandoEditar;
import br.com.opensig.core.client.controlador.comando.lista.ComandoEditarFiltrados;
import br.com.opensig.core.client.controlador.comando.lista.ComandoExcluir;
import br.com.opensig.core.client.controlador.comando.lista.ComandoExcluirFiltrados;
import br.com.opensig.core.client.controlador.comando.lista.ComandoNovo;
import br.com.opensig.core.client.controlador.comando.lista.ComandoNovoDuplicar;
import br.com.opensig.core.client.visao.Ponte;
import br.com.opensig.financeiro.client.controlador.comando.ComandoBanco;
import br.com.opensig.financeiro.client.controlador.comando.ComandoConta;
import br.com.opensig.financeiro.client.controlador.comando.ComandoForma;
import br.com.opensig.financeiro.client.controlador.comando.ComandoPagamento;
import br.com.opensig.financeiro.client.controlador.comando.ComandoPagar;
import br.com.opensig.financeiro.client.controlador.comando.ComandoReceber;
import br.com.opensig.financeiro.client.controlador.comando.ComandoRecebimento;
import br.com.opensig.financeiro.client.controlador.comando.ComandoRemessa;
import br.com.opensig.financeiro.client.controlador.comando.ComandoRetorno;
import br.com.opensig.financeiro.client.controlador.comando.boleto.ComandoGerar;
import br.com.opensig.financeiro.client.controlador.comando.boleto.ComandoGerarHtml;
import br.com.opensig.financeiro.client.controlador.comando.boleto.ComandoGerarImprimir;
import br.com.opensig.financeiro.client.controlador.comando.boleto.ComandoGerarPdf;
import br.com.opensig.financeiro.client.controlador.comando.boleto.ComandoRecibo;
import br.com.opensig.financeiro.client.controlador.comando.boleto.ComandoReciboHtml;
import br.com.opensig.financeiro.client.controlador.comando.boleto.ComandoReciboImprimir;
import br.com.opensig.financeiro.client.controlador.comando.boleto.ComandoReciboPdf;
import br.com.opensig.financeiro.client.controlador.comando.financeiro.ComandoConciliarPagamento;
import br.com.opensig.financeiro.client.controlador.comando.financeiro.ComandoConciliarRecebimento;
import br.com.opensig.financeiro.client.controlador.comando.financeiro.ComandoEstornarPagamento;
import br.com.opensig.financeiro.client.controlador.comando.financeiro.ComandoEstornarRecebimento;
import br.com.opensig.financeiro.client.controlador.comando.financeiro.ComandoQuitarPagamento;
import br.com.opensig.financeiro.client.controlador.comando.financeiro.ComandoQuitarRecebimento;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;

/**
 * Classe que inicializa o modulo SigFinanceiro.
 * @author Pedro H. Lira
 * @since 05/11/2009
 * @version 1.0
 */
public class OpenSigFinanceiro implements EntryPoint {

    /**
     * Metodo que é disparado ao iniciar o projeto que contém este módulo.
     * Usar este método para adicionar as classes de comando na fábrica de comandos.
     */
    
	public void onModuleLoad() {
    	// comandos do financeiro
        FabricaComando fc = FabricaComando.getInstancia();
        fc.addComando(ComandoReceber.class.getName(), (IComando) GWT.create(ComandoReceber.class));
        fc.addComando(ComandoPagar.class.getName(), (IComando) GWT.create(ComandoPagar.class));
        fc.addComando(ComandoPagamento.class.getName(), (IComando) GWT.create(ComandoPagamento.class));
        fc.addComando(ComandoRecebimento.class.getName(), (IComando) GWT.create(ComandoRecebimento.class));
        fc.addComando(ComandoRemessa.class.getName(), (IComando) GWT.create(ComandoRemessa.class));
        fc.addComando(ComandoRetorno.class.getName(), (IComando) GWT.create(ComandoRetorno.class));
        fc.addComando(ComandoBanco.class.getName(), (IComando) GWT.create(ComandoBanco.class));
        fc.addComando(ComandoConta.class.getName(), (IComando) GWT.create(ComandoConta.class));
        fc.addComando(ComandoForma.class.getName(), (IComando) GWT.create(ComandoForma.class));
        // gerar boleto
        fc.addComando(ComandoGerar.class.getName(), (IComando) GWT.create(ComandoGerarImprimir.class));
        fc.addComando(ComandoGerarImprimir.class.getName(), (IComando) GWT.create(ComandoGerarImprimir.class));
        fc.addComando(ComandoGerarPdf.class.getName(), (IComando) GWT.create(ComandoGerarPdf.class));
        fc.addComando(ComandoGerarHtml.class.getName(), (IComando) GWT.create(ComandoGerarHtml.class));
        // gerar recibo
        fc.addComando(ComandoRecibo.class.getName(), (IComando) GWT.create(ComandoReciboImprimir.class));
        fc.addComando(ComandoReciboImprimir.class.getName(), (IComando) GWT.create(ComandoReciboImprimir.class));
        fc.addComando(ComandoReciboPdf.class.getName(), (IComando) GWT.create(ComandoReciboPdf.class));
        fc.addComando(ComandoReciboHtml.class.getName(), (IComando) GWT.create(ComandoReciboHtml.class));
        // financeiro
        fc.addComando(ComandoQuitarPagamento.class.getName(), (IComando) GWT.create(ComandoQuitarPagamento.class));
        fc.addComando(ComandoQuitarRecebimento.class.getName(), (IComando) GWT.create(ComandoQuitarRecebimento.class));
        fc.addComando(ComandoEstornarPagamento.class.getName(), (IComando) GWT.create(ComandoEstornarPagamento.class));
        fc.addComando(ComandoEstornarRecebimento.class.getName(), (IComando) GWT.create(ComandoEstornarRecebimento.class));
        fc.addComando(ComandoConciliarPagamento.class.getName(), (IComando) GWT.create(ComandoConciliarPagamento.class));
        fc.addComando(ComandoConciliarRecebimento.class.getName(), (IComando) GWT.create(ComandoConciliarRecebimento.class));
        
		// acoes proibidas do financeiro
		Collection<Class> acoes = new ArrayList<Class>();
		acoes.add(ComandoNovo.class);
		acoes.add(ComandoNovoDuplicar.class);
		acoes.add(ComandoEditarFiltrados.class);
		acoes.add(ComandoExcluir.class);
		acoes.add(ComandoExcluirFiltrados.class);
		
		// acoes proibidas do remessa e retorno
		Collection<Class> acoes2 = new ArrayList<Class>();
		acoes2.add(ComandoNovoDuplicar.class);
		acoes2.add(ComandoEditar.class);
		acoes2.add(ComandoEditarFiltrados.class);

		Ponte.setAcoesProibidas(ComandoPagamento.class.getName(), acoes);
		Ponte.setAcoesProibidas(ComandoRecebimento.class.getName(), acoes);
		Ponte.setAcoesProibidas(ComandoRemessa.class.getName(), acoes2);
		Ponte.setAcoesProibidas(ComandoRetorno.class.getName(), acoes2);
     }
}
