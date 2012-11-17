package br.com.opensig.core.client.visao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import br.com.opensig.core.client.controlador.comando.lista.ComandoEditar;
import br.com.opensig.core.client.controlador.comando.lista.ComandoEditarFiltrados;
import br.com.opensig.core.client.controlador.comando.lista.ComandoExcluir;
import br.com.opensig.core.client.controlador.comando.lista.ComandoExcluirFiltrados;
import br.com.opensig.core.client.controlador.comando.lista.ComandoExportar;
import br.com.opensig.core.client.controlador.comando.lista.ComandoFavorito;
import br.com.opensig.core.client.controlador.comando.lista.ComandoImportar;
import br.com.opensig.core.client.controlador.comando.lista.ComandoImprimir;
import br.com.opensig.core.client.controlador.comando.lista.ComandoNovo;
import br.com.opensig.core.client.controlador.comando.lista.ComandoNovoDuplicar;
import br.com.opensig.core.client.controlador.comando.lista.ComandoVisualizar;
import br.com.opensig.core.client.padroes.Observable;
import br.com.opensig.core.shared.modelo.IFavorito;
import br.com.opensig.core.shared.modelo.ILogin;

import com.google.gwt.core.client.GWT;
import com.gwtext.client.state.CookieProvider;
import com.gwtext.client.state.CookieProviderConfig;
import com.gwtext.client.state.Manager;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.TabPanel;
import com.gwtext.client.widgets.Toolbar;

/**
 * Classe que realiza a "Ponte" entre o sistema e os módulos, dando acesso a
 * objetos e contextos compartilhados.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 */

public class Ponte extends Observable {

	private static ILogin login;
	private static IFavorito favorito;
	private static Toolbar barraMenu;
	private static TabPanel esquerda;
	private static Panel info;
	private static TabPanel centro;
	private static Map<String, Collection<Class>> acoesProibidas;
	private static Collection<Class> acoesPadroes = new ArrayList<Class>();
	private static Ponte ponte = new Ponte();

	private Ponte() {
		CookieProviderConfig cpf = new CookieProviderConfig();
		cpf.setDomain(GWT.getHostPageBaseURL().replace("http://", ""));
		cpf.setPath("/");
		CookieProvider cookie = new CookieProvider(cpf);
		Manager.setProvider(cookie);

		// ações
		acoesProibidas = new HashMap<String, Collection<Class>>();
		acoesPadroes.add(ComandoVisualizar.class);
		acoesPadroes.add(ComandoNovo.class);
		acoesPadroes.add(ComandoNovoDuplicar.class);
		acoesPadroes.add(ComandoEditar.class);
		acoesPadroes.add(ComandoEditarFiltrados.class);
		acoesPadroes.add(ComandoExcluir.class);
		acoesPadroes.add(ComandoExcluirFiltrados.class);
		acoesPadroes.add(ComandoImprimir.class);
		acoesPadroes.add(ComandoExportar.class);
		acoesPadroes.add(ComandoImportar.class);
		acoesPadroes.add(ComandoFavorito.class);
	}

	/**
	 * Metodo que retorna a instancia da classe Ponte.
	 * 
	 * @return um objeto ponte.
	 */
	public static Ponte getInstancia() {
		return ponte;
	}

	/**
	 * Metodo que fornece aos módulos o login ativo do sistema.
	 * 
	 * @return o login ativo do sistema.
	 */
	public static ILogin getLogin() {
		return login;
	}

	/**
	 * Metodo usado pelo sistema para definir o login ativo.
	 * 
	 * @param login
	 *            o objeto ativo de ILogin.
	 */
	public static void setLogin(ILogin login) {
		Ponte.login = login;
		ponte.setChanged();
		ponte.notifyObservers(login);
	}

	/**
	 * Metodo que fornece aos módulos um objeto do tipo dados.
	 * 
	 * @return o dados alterado do sistema.
	 */
	public static IFavorito getDados() {
		return favorito;
	}

	/**
	 * Metodo usado pelo sistema para notificar um dado alterado.
	 * 
	 * @param favorito
	 *            o objeto alterado no sistema.
	 */
	public static void setDados(IFavorito favorito) {
		Ponte.favorito = favorito;
		ponte.setChanged();
		ponte.notifyObservers(favorito);
	}

	/**
	 * Metodo que fornece aos módulos a barra de menu ativa do sistema.
	 * 
	 * @return a barra de menu ativa do sistema.
	 */
	public static Toolbar getBarraMenu() {
		return barraMenu;
	}

	/**
	 * Metodo usado pelo sistema para definir a barra de menu ativa.
	 * 
	 * @param barraMenus
	 *            o objeto ativo de Toolbar.
	 */
	public static void setBarraMenu(Toolbar barraMenus) {
		Ponte.barraMenu = barraMenus;
		ponte.setChanged();
		ponte.notifyObservers(barraMenus);
	}

	/**
	 * Metodo que fornece aos módulos o painel do centro ativo do sistema.
	 * 
	 * @return o painel do centro ativo do sistema.
	 */
	public static TabPanel getCentro() {
		return centro;
	}

	/**
	 * Metodo usado pelo sistema para definir o painel do centro ativa.
	 * 
	 * @param centro
	 *            o objeto ativo de TabPanel.
	 */
	public static void setCentro(TabPanel centro) {
		Ponte.centro = centro;
		ponte.setChanged();
		ponte.notifyObservers(centro);
	}

	/**
	 * Metodo que fornece aos módulos o painel de informaçao ativo sistema.
	 * 
	 * @return o painel de informaçao ativo do sistema.
	 */
	public static Panel getInfo() {
		return info;
	}

	/**
	 * Metodo usado pelo sistema para definir o painel de informaçao ativo.
	 * 
	 * @param info
	 *            o objeto ativo de Panel.
	 */
	public static void setInfo(Panel info) {
		Ponte.info = info;
		ponte.setChanged();
		ponte.notifyObservers(info);
	}

	/**
	 * Metodo que fornece aos módulos o painel da esqueda ativo do sistema.
	 * 
	 * @return o painel da esqueda ativo do sistema.
	 */
	public static TabPanel getEsquerda() {
		return esquerda;
	}

	/**
	 * Metodo usado pelo sistema para definir o painel da esquerda ativo.
	 * 
	 * @param esquerda
	 *            o objeto ativo de Panel.
	 */
	public static void setEsquerda(TabPanel esquerda) {
		Ponte.esquerda = esquerda;
		ponte.setChanged();
		ponte.notifyObservers(esquerda);
	}

	/**
	 * Metodo que recupera as ações que a funçao nao deve ter, todas as ações
	 * padrões.
	 * 
	 * @return uma coleçao de nomes de classes das ações padrões.
	 */
	public static Collection<Class> getAcoesPadroes() {
		return acoesPadroes;
	}

	/**
	 * Metodo que define as ações que a funçao nao devem ter, em especial ações
	 * as padrões.
	 * 
	 * @param classeFuncao
	 *            o nome da classe da funçao.
	 * @param classesAcoes
	 *            uma coleçao de nomes de classes das ações.
	 */
	public static void setAcoesProibidas(String classeFuncao, Collection<Class> classesAcoes) {
		acoesProibidas.put(classeFuncao, classesAcoes);
	}

	/**
	 * Metodo que recupera as ações que a funçao nao deve ter, em especial as
	 * ações padrões.
	 * 
	 * @param classeFuncao
	 *            o nome da classe da funçao.
	 * @return uma coleçao de nomes de classes das ações.
	 */
	public static Collection<Class> getAcoesProibidas(String classeFuncao) {
		return acoesProibidas.get(classeFuncao);
	}
}
