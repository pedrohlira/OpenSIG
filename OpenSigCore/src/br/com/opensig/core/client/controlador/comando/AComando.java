package br.com.opensig.core.client.controlador.comando;

import java.util.Map;

import br.com.opensig.core.client.visao.abstrato.IFormulario;
import br.com.opensig.core.client.visao.abstrato.IGrafico;
import br.com.opensig.core.client.visao.abstrato.IListagem;
import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.core.shared.modelo.IFavorito;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.StatusCodeException;
import com.gwtext.client.widgets.TabPanel;

/**
 * Classe abstrata que implementa acoes padroes de comando.
 * 
 * @param <E>
 *            o tipo de dados do comando.
 * @author Pedro H. Lira
 * @version 1.0
 */
public abstract class AComando<E extends Dados> implements IComando {

	/**
	 * Um mapa que guarda o par chave/valor a ser transmitido entre os comandos.
	 */
	protected Map contexto;
	/**
	 * O objeto generico informado para manipulacao do comando.
	 */
	protected E DADOS;
	/**
	 * Interface de acesso ao favorito.
	 */
	protected IFavorito FAVORITO;
	/**
	 * Objeto referenciado da Tab onde se encontra a funcao deste comando.
	 */
	protected TabPanel TAB;
	/**
	 * Interface de acesso a listagem do comando.
	 */
	protected IListagem LISTA;
	/**
	 * Interface de acesso ao formulario do comando.
	 */
	protected IFormulario FORM;
	/**
	 * Interface de acesso ao grafico do comando.
	 */
	protected IGrafico GRAFICO;
	/**
	 * Interface de acesso ao proximo comando a ser executado.
	 */
	protected IComando comando;
	/**
	 * Chamada de retorno so metodos assincronos utilizados.
	 */
	protected AsyncCallback<E> ASYNC = new AsyncCallback<E>() {

		public void onFailure(Throwable caught) {
			if (comando != null) {
				if (caught instanceof StatusCodeException) {
					contexto.put("erro", null);
				} else {
					contexto.put("erro", caught);
				}
				comando.execute(contexto);
			}
		}

		public void onSuccess(E result) {
			if (comando != null) {
				contexto.put("resultado", result);
				comando.execute(contexto);
			}
		}
	};

	/**
	 * Construtor padrao.
	 */
	public AComando() {
		this(null);
	}

	/**
	 * Construtor padrao.
	 * 
	 * @param proximo
	 *            outro comando que sera executa logo que o atual termine.
	 */
	public AComando(IComando proximo) {
		this.comando = proximo;
	}

	@Override
	public void execute(Map contexto) {
		try {
			this.contexto = contexto;
			DADOS = (E) contexto.get("dados");
			FAVORITO = (IFavorito) contexto.get("favorito");
			FORM = (IFormulario) contexto.get("form");
			GRAFICO = (IGrafico) contexto.get("grafico");
			LISTA = (IListagem) contexto.get("lista");
		} catch (Exception ex) {
			// nada
		}
	}

	@Override
	public IComando getProximo() {
		return comando;
	}

	@Override
	public void setProximo(IComando comando) {
		this.comando = comando;
	}
}
