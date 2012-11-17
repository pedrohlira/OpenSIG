package br.com.opensig.empresa.server;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.FiltroObjeto;
import br.com.opensig.core.client.servico.OpenSigException;
import br.com.opensig.core.server.Conexao;
import br.com.opensig.core.server.CoreServiceImpl;
import br.com.opensig.core.server.UtilServer;
import br.com.opensig.core.shared.modelo.Autenticacao;
import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.core.shared.modelo.EComando;
import br.com.opensig.core.shared.modelo.Sql;
import br.com.opensig.empresa.client.servico.EmpresaException;
import br.com.opensig.empresa.client.servico.EmpresaService;
import br.com.opensig.empresa.server.acao.SalvarEntidade;
import br.com.opensig.empresa.shared.modelo.EmpCliente;
import br.com.opensig.empresa.shared.modelo.EmpEmpresa;
import br.com.opensig.empresa.shared.modelo.EmpFornecedor;
import br.com.opensig.empresa.shared.modelo.EmpFuncionario;
import br.com.opensig.empresa.shared.modelo.EmpPlano;
import br.com.opensig.empresa.shared.modelo.EmpTransportadora;

public class EmpresaServiceImpl<E extends Dados> extends CoreServiceImpl<E> implements EmpresaService<E> {

	public EmpresaServiceImpl(){
	}
	
	public EmpresaServiceImpl(Autenticacao auth){
		super(auth);
	}
	
	public E salvar(E dados) throws EmpresaException {
		if (dados instanceof EmpEmpresa) {
			return (E) salvarEmpresa((EmpEmpresa) dados);
		} else {
			EntityManagerFactory emf = null;
			EntityManager em = null;

			try {
				// recupera uma instância do gerenciador de entidades
				emf = Conexao.getInstancia(dados.getPu());
				em = emf.createEntityManager();
				em.getTransaction().begin();

				// seta a entidade
				setEntidade(em, dados);

				salvar(em, dados);
				em.getTransaction().commit();
				dados.anularDependencia();
				return dados;
			} catch (Exception ex) {
				if (em != null && em.getTransaction().isActive()) {
					em.getTransaction().rollback();
				}

				UtilServer.LOG.error("Erro ao salvar a " + dados.getTabela(), ex);
				throw new EmpresaException(ex.getMessage());
			} finally {
				em.close();
				emf.close();
			}
		}
	};

	public EmpEmpresa salvarEmpresa(EmpEmpresa empresa) throws EmpresaException {
		EntityManagerFactory emf = null;
		EntityManager em = null;
		EmpresaServiceImpl servico = new EmpresaServiceImpl(null);
		
		try {
			// recupera uma instância do gerenciador de entidades
			emf = Conexao.getInstancia(empresa.getPu());
			em = emf.createEntityManager();
			em.getTransaction().begin();

			SalvarEntidade se = new SalvarEntidade(null, this, empresa.getEmpEntidade());
			se.execute();
			empresa.setEmpEntidade(se.getEntidade());

			List<EmpPlano> planos = empresa.getEmPlanos();
			empresa.setEmPlano(null);
			servico.salvar(em, empresa);

			// deleta o plano
			FiltroObjeto fo = new FiltroObjeto("empEmpresa", ECompara.IGUAL, empresa);
			Sql sql = new Sql(new EmpPlano(), EComando.EXCLUIR, fo);
			executar(em, sql);
			// insere o plano
			EmpPlano plano = planos.get(0);
			plano.setEmpEmpresa(empresa);
			servico.salvar(em, plano);

			em.getTransaction().commit();
			empresa.anularDependencia();
			return empresa;
		} catch (Exception ex) {
			if (em != null && em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}

			UtilServer.LOG.error("Erro ao salvar a empresa.", ex);
			throw new EmpresaException(ex.getMessage());
		} finally {
			em.close();
			emf.close();
		}
	}

	private void setEntidade(EntityManager em, E dados) throws OpenSigException {
		if (dados instanceof EmpCliente) {
			SalvarEntidade se = new SalvarEntidade(null, this, ((EmpCliente) dados).getEmpEntidade());
			se.execute();
			((EmpCliente) dados).setEmpEntidade(se.getEntidade());
		} else if (dados instanceof EmpFornecedor) {
			SalvarEntidade se = new SalvarEntidade(null, this, ((EmpFornecedor) dados).getEmpEntidade());
			se.execute();
			((EmpFornecedor) dados).setEmpEntidade(se.getEntidade());
		} else if (dados instanceof EmpFuncionario) {
			SalvarEntidade se = new SalvarEntidade(null, this, ((EmpFuncionario) dados).getEmpEntidade());
			se.execute();
			((EmpFuncionario) dados).setEmpEntidade(se.getEntidade());
		} else if (dados instanceof EmpTransportadora) {
			SalvarEntidade se = new SalvarEntidade(null, this, ((EmpTransportadora) dados).getEmpEntidade());
			se.execute();
			((EmpTransportadora) dados).setEmpEntidade(se.getEntidade());
		}
	}
}
