package br.com.opensig.comercial.server.rest;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBContext;

import org.eclipse.persistence.jaxb.JAXBContextFactory;
import org.eclipse.persistence.jaxb.rs.MOXyJsonProvider;

import br.com.opensig.comercial.shared.modelo.ComEcf;
import br.com.opensig.comercial.shared.rest.SisCliente;
import br.com.opensig.comercial.shared.rest.SisEstado;
import br.com.opensig.comercial.shared.rest.SisMunicipio;
import br.com.opensig.empresa.shared.modelo.EmpEstado;
import br.com.opensig.empresa.shared.modelo.EmpMunicipio;
import br.com.opensig.financeiro.shared.modelo.FinForma;
import br.com.opensig.permissao.shared.modelo.SisUsuario;
import br.com.opensig.produto.shared.modelo.ProdComposicao;
import br.com.opensig.produto.shared.modelo.ProdEmbalagem;
import br.com.opensig.produto.shared.modelo.ProdGrade;
import br.com.opensig.produto.shared.modelo.ProdGradeTipo;
import br.com.opensig.produto.shared.modelo.ProdPreco;
import br.com.opensig.produto.shared.modelo.ProdProduto;

/**
 * Classe que habilita o contexto do JAXB usando a implementacao do EclipseLink
 * moxy.
 * 
 * @author Pedro H. Lira
 */
@Provider
public class RestContexto extends MOXyJsonProvider implements ContextResolver<JAXBContext> {

	private JAXBContext context;
	private Class[] types = { EmpEstado.class, EmpMunicipio.class, SisUsuario.class, SisCliente.class, SisEstado.class, SisMunicipio.class, ProdEmbalagem.class, ProdProduto.class, ProdPreco.class,
			ProdComposicao.class, ProdGrade.class, ProdGradeTipo.class, ComEcf.class, FinForma.class, String.class };

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