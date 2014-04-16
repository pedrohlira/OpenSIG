package br.com.opensig.poker.server.rest;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBContext;

import org.eclipse.persistence.jaxb.JAXBContextFactory;
import org.eclipse.persistence.jaxb.rs.MOXyJsonProvider;

import br.com.opensig.core.shared.modelo.Lista;
import br.com.opensig.permissao.shared.modelo.SisUsuario;
import br.com.opensig.poker.shared.modelo.PokerCash;
import br.com.opensig.poker.shared.modelo.PokerCliente;
import br.com.opensig.poker.shared.modelo.PokerForma;
import br.com.opensig.poker.shared.modelo.PokerJogador;
import br.com.opensig.poker.shared.modelo.PokerMesa;
import br.com.opensig.poker.shared.modelo.PokerParticipante;
import br.com.opensig.poker.shared.modelo.PokerTorneio;
import br.com.opensig.poker.shared.modelo.PokerTorneioTipo;

/**
 * Classe que habilita o contexto do JAXB usando a implementacao do EclipseLink
 * moxy.
 * 
 * @author Pedro H. Lira
 */
@Provider
public class RestContexto extends MOXyJsonProvider implements ContextResolver<JAXBContext> {

	private JAXBContext context;
	private Class[] types = { SisUsuario.class, PokerCliente.class, PokerMesa.class, PokerParticipante.class, PokerTorneio.class, PokerTorneioTipo.class, PokerCash.class, PokerForma.class,
			PokerJogador.class, Lista.class, String.class };

	/**
	 * Construtor padrao.
	 * 
	 * @throws Exception
	 *             dispara caso nao consiga criar.
	 */
	public RestContexto() throws Exception {
		this.context = JAXBContextFactory.createContext(types, null);
	}

	/**
	 * Recupera o contexto atual da classe informada.
	 * 
	 * @param classe
	 *            o tipo de classe informada.
	 * @return o contexto atual.
	 */
	@Override
	public JAXBContext getContext(Class<?> classe) {
		for (Class type : types) {
			if (type == classe) {
				return context;
			}
		}
		return null;
	}
}