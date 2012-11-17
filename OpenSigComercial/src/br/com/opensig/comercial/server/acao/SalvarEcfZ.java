package br.com.opensig.comercial.server.acao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import br.com.opensig.comercial.client.servico.ComercialException;
import br.com.opensig.comercial.shared.modelo.ComEcfZ;
import br.com.opensig.comercial.shared.modelo.ComEcfZTotais;
import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.FiltroObjeto;
import br.com.opensig.core.client.padroes.Chain;
import br.com.opensig.core.client.servico.OpenSigException;
import br.com.opensig.core.server.Conexao;
import br.com.opensig.core.server.CoreServiceImpl;
import br.com.opensig.core.server.UtilServer;
import br.com.opensig.core.shared.modelo.EComando;
import br.com.opensig.core.shared.modelo.Sql;

public class SalvarEcfZ extends Chain {

	private CoreServiceImpl servico;
	private ComEcfZ z;

	public SalvarEcfZ(Chain next, CoreServiceImpl servico, ComEcfZ z) throws OpenSigException {
		super(next);
		this.servico = servico;
		this.z = z;
	}

	@Override
	public void execute() throws OpenSigException {
		EntityManagerFactory emf = null;
		EntityManager em = null;

		try {
			// recupera uma instância do gerenciador de entidades
			emf = Conexao.getInstancia(z.getPu());
			em = emf.createEntityManager();
			em.getTransaction().begin();
			List<ComEcfZTotais> totais = z.getComEcfZTotais();

			// salva
			z.setComEcfZTotais(null);
			z.setComEcfVendas(null);
			servico.salvar(em, z);

			// deleta
			if (z.getComEcfZId() > 0) {
				FiltroObjeto fo = new FiltroObjeto("comEcfZ", ECompara.IGUAL, z);
				Sql sql = new Sql(new ComEcfZTotais(), EComando.EXCLUIR, fo);
				servico.executar(em, sql);
			}
			
			// insere
			for (ComEcfZTotais tot : totais) {
				tot.setComEcfZ(z);
			}
			servico.salvar(em, totais);

			if (next != null) {
				next.execute();
			}
			em.getTransaction().commit();
		} catch (Exception ex) {
			if (em != null && em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}

			UtilServer.LOG.error("Erro ao salvar o valor.", ex);
			throw new ComercialException(ex.getMessage());
		} finally {
			em.close();
			emf.close();
		}
	}

}
