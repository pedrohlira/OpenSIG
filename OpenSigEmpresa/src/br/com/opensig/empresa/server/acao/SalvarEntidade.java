package br.com.opensig.empresa.server.acao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.FiltroObjeto;
import br.com.opensig.core.client.padroes.Chain;
import br.com.opensig.core.client.servico.OpenSigException;
import br.com.opensig.core.server.Conexao;
import br.com.opensig.core.server.CoreServiceImpl;
import br.com.opensig.core.server.UtilServer;
import br.com.opensig.core.shared.modelo.EComando;
import br.com.opensig.core.shared.modelo.Sql;
import br.com.opensig.empresa.client.servico.EmpresaException;
import br.com.opensig.empresa.shared.modelo.EmpContato;
import br.com.opensig.empresa.shared.modelo.EmpEndereco;
import br.com.opensig.empresa.shared.modelo.EmpEntidade;

public class SalvarEntidade extends Chain {

	private CoreServiceImpl servico;
	private EmpEntidade entidade;

	public SalvarEntidade(Chain next, CoreServiceImpl servico, EmpEntidade entidade) throws OpenSigException {
		super(next);
		this.servico = servico;
		this.entidade = entidade;
	}

	@Override
	public void execute() throws OpenSigException {
		EntityManagerFactory emf = null;
		EntityManager em = null;

		try {
			// recupera uma instância do gerenciador de entidades
			emf = Conexao.getInstancia(entidade.getPu());
			em = emf.createEntityManager();
			em.getTransaction().begin();

			List<EmpContato> contatos = entidade.getEmpContatos();
			List<EmpEndereco> enderecos = entidade.getEmpEnderecos();
			// deleta
			if (entidade.getEmpEntidadeId() > 0) {
				FiltroObjeto fo = new FiltroObjeto("empEntidade", ECompara.IGUAL, entidade);
				Sql sql = new Sql(new EmpContato(), EComando.EXCLUIR, fo);
				servico.executar(em, sql);
				Sql sql1 = new Sql(new EmpEndereco(), EComando.EXCLUIR, fo);
				servico.executar(em, sql1);
			}

			// salva
			entidade.setEmpContatos(null);
			entidade.setEmpEnderecos(null);
			servico.salvar(em, entidade);

			// insere
			for (EmpContato cont : contatos) {
				cont.setEmpEntidade(entidade);
			}
			servico.salvar(em, contatos);
			for (EmpEndereco ende : enderecos) {
				ende.setEmpEntidade(entidade);
			}
			servico.salvar(em, enderecos);

			if (next != null) {
				next.execute();
			}
			em.getTransaction().commit();
		} catch (Exception ex) {
			if (em != null && em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}

			UtilServer.LOG.error("Erro na salvar a entidade.", ex);
			throw new EmpresaException(ex.getMessage());
		} finally {
			em.close();
			emf.close();
		}
	}

	public EmpEntidade getEntidade() {
		return entidade;
	}

}
