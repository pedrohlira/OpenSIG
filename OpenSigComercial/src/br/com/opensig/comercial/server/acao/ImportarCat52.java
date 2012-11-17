package br.com.opensig.comercial.server.acao;

import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import org.beanio.BeanReader;
import org.beanio.StreamFactory;

import br.com.opensig.comercial.server.ComercialServiceImpl;
import br.com.opensig.comercial.shared.modelo.Cat52;
import br.com.opensig.comercial.shared.modelo.ComEcf;
import br.com.opensig.comercial.shared.modelo.ComEcfDocumento;
import br.com.opensig.comercial.shared.modelo.ComEcfVenda;
import br.com.opensig.comercial.shared.modelo.ComEcfVendaProduto;
import br.com.opensig.comercial.shared.modelo.ComEcfZ;
import br.com.opensig.comercial.shared.modelo.ComEcfZTotais;
import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.EJuncao;
import br.com.opensig.core.client.controlador.filtro.FiltroData;
import br.com.opensig.core.client.controlador.filtro.FiltroNumero;
import br.com.opensig.core.client.controlador.filtro.FiltroObjeto;
import br.com.opensig.core.client.controlador.filtro.FiltroTexto;
import br.com.opensig.core.client.controlador.filtro.GrupoFiltro;
import br.com.opensig.core.client.controlador.filtro.IFiltro;
import br.com.opensig.core.client.controlador.parametro.ParametroException;
import br.com.opensig.core.client.servico.CoreException;
import br.com.opensig.core.client.servico.OpenSigException;
import br.com.opensig.core.server.Conexao;
import br.com.opensig.core.server.UtilServer;
import br.com.opensig.core.server.importar.IImportacao;
import br.com.opensig.core.shared.modelo.Autenticacao;
import br.com.opensig.core.shared.modelo.Lista;
import br.com.opensig.core.shared.modelo.sistema.SisExpImp;
import br.com.opensig.empresa.shared.modelo.EmpEmpresa;
import br.com.opensig.permissao.shared.modelo.SisUsuario;
import br.com.opensig.produto.shared.modelo.ProdEmbalagem;

public class ImportarCat52 implements IImportacao<Cat52> {

	private List<ProdEmbalagem> embalagem;
	private ComercialServiceImpl service;
	private Autenticacao auth;
	private SisUsuario usuario;
	private List<Cat52> oks;
	private List<Cat52> err;
	private int primeiraVenda;

	@Override
	public Map<String, List<Cat52>> setArquivo(Autenticacao auth, Map<String, byte[]> arquivos, SisExpImp modo) throws OpenSigException {
		this.service = new ComercialServiceImpl(auth);
		this.auth = auth;
		this.oks = new ArrayList<Cat52>();
		this.err = new ArrayList<Cat52>();

		try {
			// lendo as definicoes
			StreamFactory factory = StreamFactory.newInstance();
			ByteArrayInputStream bais = new ByteArrayInputStream(modo.getSisExpImpModelo().getBytes());
			factory.load(bais);

			// pega as embalagens
			Lista<ProdEmbalagem> emb = service.selecionar(new ProdEmbalagem(), 0, 0, null, false);
			embalagem = emb.getLista();

			// pega o usuario atual
			usuario = new SisUsuario(Integer.valueOf(auth.getUsuario()[0]));

			// setando os objetos
			Object rec;
			ComEcf ecf;
			ComEcfZ ecfZ;
			BeanReader in;
			Map<Integer, ComEcfVenda> mapVenda;
			List<ComEcfDocumento> docs;

			for (Entry<String, byte[]> arquivo : arquivos.entrySet()) {
				Cat52 cat52 = new Cat52();
				cat52.setArquivo(arquivo.getKey());
				// setando os objetos
				rec = null;
				ecf = null;
				ecfZ = null;
				in = null;
				primeiraVenda = 0;
				mapVenda = new HashMap<Integer, ComEcfVenda>();
				docs = new ArrayList<ComEcfDocumento>();

				try {
					// lendo os dados do arquivo
					String texto = new String(arquivo.getValue());
					// limpando
					arquivo.setValue(null);
					StringReader sr = new StringReader(texto);
					in = factory.createReader("cat52", sr);
				} catch (Exception e) {
					cat52.setErro(e.getMessage());
					err.add(cat52);
					continue;
				}

				do {
					try {
						rec = in.read();
						// a impressora
						if (rec instanceof ComEcf) {
							ecf = getEcf((ComEcf) rec);
							// leitura z
						} else if (rec instanceof ComEcfZ) {
							ecfZ = getEcfZ((ComEcfZ) rec, ecf, cat52);
							if (ecfZ == null) {
								break;
							}
							// totais da leitura z
						} else if (rec instanceof ComEcfZTotais) {
							ComEcfZTotais total = (ComEcfZTotais) rec;
							if (total.getComEcfZTotaisValor() > 0.00) {
								total.setComEcfZTotaisValor(total.getComEcfZTotaisValor() / 100);
								ecfZ.getComEcfZTotais().add(total);
							}
							// venda
						} else if (rec instanceof ComEcfVenda) {
							ComEcfVenda venda = getVenda((ComEcfVenda) rec, ecf, ecfZ);
							mapVenda.put(venda.getComEcfVendaCoo(), venda);
							// produtos
						} else if (rec instanceof ComEcfVendaProduto) {
							ComEcfVendaProduto vp = (ComEcfVendaProduto) rec;
							ComEcfVenda venda = mapVenda.get(vp.getComEcfVendaProdutoCoo());
							setProduto(vp, venda);
							// documento
						} else if (rec instanceof ComEcfDocumento) {
							ComEcfDocumento doc = (ComEcfDocumento) rec;
							doc.setComEcfDocumentoUsuario(0);
							doc.setComEcf(ecf);
							docs.add(doc);
						}
					} catch (Exception e) {
						// faz nada, pois sao linhas nao reconhecidas
					}
				} while (rec != null);

				// limpando
				in.close();
				in = null;
				System.gc();

				// valida ecfZ
				if (ecfZ != null && ecfZ.getComEcfZTotais() != null && !ecfZ.getComEcfZTotais().isEmpty()) {
					// salva z
					service.salvarEcfZ(ecfZ);
					// salva as vendas
					salvaVendas(mapVenda, cat52);
					// salva os documentos
					service.salvar(docs);
					// atualiza os produtos das vendas
					atualizarProdutos();
					// recupera as vendas salvas
					FiltroNumero fnVenda = new FiltroNumero("comEcfVendaId", ECompara.MAIOR_IGUAL, primeiraVenda);
					Lista<ComEcfVenda> vendasEcf = service.selecionar(new ComEcfVenda(), 0, 0, fnVenda, false);
					// fechando as vendas
					fecharVendas(vendasEcf, cat52);
					// limpando
					mapVenda = null;
					vendasEcf = null;
					System.gc();
				} else {
					cat52.setErro("Dados ja existentes no sistema ou arquivo sem registros!");
					err.add(cat52);
				}
			}
		} catch (Exception e) {
			throw new OpenSigException(e.getMessage());
		}

		Map<String, List<Cat52>> resp = new HashMap<String, List<Cat52>>();
		resp.put("ok", oks);
		resp.put("erro", err);
		return resp;
	}

	// verifica se pode fechar as vendas
	private void fecharVendas(Lista<ComEcfVenda> vendasEcf, Cat52 cat52) {
		// zera os contadores
		int vendas = 0;
		int vendasNfechadas = 0;
		int prodNachados = 0;
		String estoque = auth.getConf().get("estoque.ativo");
		if (estoque.equalsIgnoreCase("sim")) {
			auth.getConf().put("estoque.ativo", "nao");
		}

		try {
			FecharEcfVenda fecharVenda;
			ArrayList<String[]> invalidos = new ArrayList<String[]>();

			// percorre as vendas
			for (ComEcfVenda venda : vendasEcf.getLista()) {
				vendas++;
				if (!venda.getComEcfVendaCancelada()) {
					// verifica se pode fechar a venda
					boolean fechar = true;
					for (ComEcfVendaProduto venProd : venda.getComEcfVendaProdutos()) {
						if (venProd.getProdProduto() == null) {
							prodNachados++;
							fechar = false;
						}
					}

					// valida se fecha a venda
					if (fechar) {
						fecharVenda = new FecharEcfVenda(null, service, venda, invalidos, auth);
						fecharVenda.execute();
					} else {
						vendasNfechadas++;
					}
				}
			}

			cat52.setVendas(vendas);
			cat52.setVendaNfechadas(vendasNfechadas);
			cat52.setProdNachados(prodNachados);
			oks.add(cat52);
		} catch (Exception e) {
			cat52.setErro(e.getMessage());
			err.add(cat52);
		} finally {
			auth.getConf().put("estoque.ativo", estoque);
		}
	}

	// encontra a ecf no sistema
	private ComEcf getEcf(ComEcf ecf) throws ParametroException, CoreException {
		// encontra a ecf no sistema
		EmpEmpresa empresa = new EmpEmpresa(Integer.valueOf(auth.getEmpresa()[0]));
		FiltroObjeto fo = new FiltroObjeto("empEmpresa", ECompara.IGUAL, empresa);
		FiltroTexto ft = new FiltroTexto("comEcfSerie", ECompara.IGUAL, ecf.getComEcfSerie());
		GrupoFiltro gf = new GrupoFiltro(EJuncao.E, new IFiltro[] { fo, ft });
		ComEcf ecf2 = (ComEcf) service.selecionar(ecf, gf, false);

		if (ecf2 == null) {
			ecf.setComEcfCodigo("2D");
			ecf.setComEcfIdentificacao("000000");
			ecf.setComEcfAtivo(true);
			ecf.setEmpEmpresa(empresa);
			service.salvar(ecf);
		} else {
			ecf = ecf2;
		}

		return ecf;
	}

	// seta os dados da Z
	private ComEcfZ getEcfZ(ComEcfZ ecfZ, ComEcf ecf, Cat52 cat52) throws ParametroException, CoreException {
		ecfZ.setComEcf(ecf);
		ecfZ.setComEcfZUsuario(0);
		ecfZ.setComEcfZBruto(ecfZ.getComEcfZBruto() / 100);
		ecfZ.setComEcfZIssqn(ecfZ.getIssqn().equalsIgnoreCase("S"));
		ecfZ.setComEcfZTotais(new ArrayList<ComEcfZTotais>());

		// valida Z
		FiltroObjeto fo = new FiltroObjeto("comEcf", ECompara.IGUAL, ecf);
		FiltroData fd = new FiltroData("comEcfZMovimento", ECompara.IGUAL, ecfZ.getComEcfZMovimento());
		GrupoFiltro gf = new GrupoFiltro(EJuncao.E, new IFiltro[] { fo, fd });
		if (service.selecionar(ecfZ, gf, false) != null) {
			ecfZ = null;
		} else {
			// seta o coo inicial
			Lista<ComEcfZ> zs = service.selecionar(ecfZ, 0, 1, fo, false);
			if (!zs.getLista().isEmpty()) {
				ecfZ.setComEcfZCooIni(zs.getLista().get(0).getComEcfZCooFin() + 1);
				ecfZ.setComEcfZGt(zs.getLista().get(0).getComEcfZGt() + ecfZ.getComEcfZBruto());
			} else {
				ecfZ.setComEcfZCooIni(1);
				ecfZ.setComEcfZGt(ecfZ.getComEcfZBruto());
				// coloca um aviso para alterar
				cat52.setErro("ATENCAO: alterar o COO_INI e o Total (GT) do registro da reducao Z, usando as informacoes impressas.");
				err.add(cat52);
			}
		}

		return ecfZ;
	}

	// coloca os dados da venda
	private ComEcfVenda getVenda(ComEcfVenda venda, ComEcf ecf, ComEcfZ z) {
		venda.setComEcf(ecf);
		venda.setComEcfZ(z);
		venda.setSisUsuario(usuario);
		venda.setEmpCliente(null);
		venda.setFinReceber(null);
		venda.setComEcfVendaBruto(venda.getComEcfVendaBruto() / 100);
		// desconto
		double desc;
		if (venda.getDescIndicador().equalsIgnoreCase("V")) {
			desc = venda.getComEcfVendaBruto() > 0 ? venda.getComEcfVendaDesconto() / venda.getComEcfVendaBruto() : 0.00;
		} else {
			desc = venda.getComEcfVendaDesconto() / 100;
		}
		venda.setComEcfVendaDesconto(desc > 0 ? desc : 0.00);
		// acrescimo
		double acres;
		if (venda.getAcresIndicador().equalsIgnoreCase("V")) {
			acres = venda.getComEcfVendaBruto() > 0 ? venda.getComEcfVendaAcrescimo() / venda.getComEcfVendaBruto() : 0.00;
		} else {
			acres = venda.getComEcfVendaAcrescimo() / 100;
		}
		venda.setComEcfVendaAcrescimo(acres > 0 ? acres : 0.00);
		venda.setComEcfVendaLiquido(venda.getComEcfVendaLiquido() / 100);
		venda.setComEcfVendaProdutos(new ArrayList<ComEcfVendaProduto>());
		venda.setComEcfVendaCancelada(venda.getCancelada().equalsIgnoreCase("S"));
		venda.setComEcfVendaFechada(venda.getComEcfVendaCancelada());
		return venda;
	}

	// coloco os dados do produto
	private void setProduto(ComEcfVendaProduto vp, ComEcfVenda venda) {
		vp.setProdEmbalagem(getEmbalagem(vp.getComEcfVendaProdutoUnd()));
		vp.setComEcfVendaProdutoBruto(vp.getComEcfVendaProdutoBruto() / 100);
		vp.setComEcfVendaProdutoDesconto(venda.getComEcfVendaDesconto());
		double desc = vp.getComEcfVendaProdutoBruto() * venda.getComEcfVendaDesconto() / 100;
		vp.setComEcfVendaProdutoAcrescimo(venda.getComEcfVendaAcrescimo());
		double acres = vp.getComEcfVendaProdutoBruto() * venda.getComEcfVendaAcrescimo() / 100;
		vp.setComEcfVendaProdutoQuantidade(vp.getComEcfVendaProdutoQuantidade() / 1000);
		vp.setComEcfVendaProdutoLiquido(vp.getComEcfVendaProdutoBruto() - desc + acres);
		vp.setComEcfVendaProdutoTotal(vp.getComEcfVendaProdutoLiquido() * vp.getComEcfVendaProdutoQuantidade());
		if (venda.getComEcfVendaCancelada()) {
			vp.setComEcfVendaProdutoCancelado(true);
		} else {
			vp.setComEcfVendaProdutoCancelado(vp.getCancelado().equalsIgnoreCase("S"));
		}
		venda.getComEcfVendaProdutos().add(vp);
	}

	// salva as vendas no sistema
	private void salvaVendas(Map<Integer, ComEcfVenda> mapVenda, Cat52 cat52) {
		// percorre as vendas
		for (Entry<Integer, ComEcfVenda> venda : mapVenda.entrySet()) {
			try {
				ComEcfVenda ecfVenda = service.salvarEcfVenda(venda.getValue());
				if (primeiraVenda == 0) {
					primeiraVenda = ecfVenda.getComEcfVendaId();
				}
			} catch (Exception e) {
				cat52.setErro(e.getMessage());
				err.add(cat52);
			}
		}
	}

	// recupera a embalagem
	private ProdEmbalagem getEmbalagem(String nome) {
		// se nao achar colocar a padrao UND
		ProdEmbalagem resp = new ProdEmbalagem(1);
		// percorre as embalagens
		for (ProdEmbalagem emb : embalagem) {
			if (emb.getProdEmbalagemNome().equalsIgnoreCase(nome)) {
				resp = emb;
				break;
			}
		}
		return resp;
	}

	// atualiza os produtos pelo codigo
	private void atualizarProdutos() {
		EntityManagerFactory emf = null;
		EntityManager em = null;
		try {
			// abre a conexao
			emf = Conexao.getInstancia(new ComEcfVendaProduto().getPu());
			em = emf.createEntityManager();
			// inicia a transacao
			em.getTransaction().begin();
			// atualiza pelo codigo de barras
			StringBuffer sql = new StringBuffer();
			sql.append("UPDATE com_ecf_venda_produto, prod_produto");
			sql.append(" SET com_ecf_venda_produto.prod_produto_id = prod_produto.prod_produto_id");
			sql.append(" WHERE com_ecf_venda_produto.prod_produto_id is null");
			sql.append(" AND com_ecf_venda_produto.com_ecf_venda_produto_codigo = prod_produto.prod_produto_barra");
			Query rs = em.createNativeQuery(sql.toString());
			// efetiva a transacao
			int total = rs.executeUpdate();
			em.getTransaction().commit();

			// inicia a transacao
			em.getTransaction().begin();
			// atualiza pela descricao e valor
			sql = new StringBuffer();
			sql.append("UPDATE com_ecf_venda_produto, prod_produto");
			sql.append(" SET com_ecf_venda_produto.prod_produto_id = prod_produto.prod_produto_id");
			sql.append(" WHERE com_ecf_venda_produto.prod_produto_id is null");
			sql.append(" AND com_ecf_venda_produto.com_ecf_venda_produto_descricao = prod_produto.prod_produto_descricao");
			sql.append(" AND com_ecf_venda_produto.com_ecf_venda_produto_bruto = prod_produto.prod_produto_preco");
			rs = em.createNativeQuery(sql.toString());
			// efetiva a transacao
			total += rs.executeUpdate();
			em.getTransaction().commit();

			// inicia a transacao
			em.getTransaction().begin();
			// atualiza pela descricao
			sql = new StringBuffer();
			sql.append("UPDATE com_ecf_venda_produto, prod_produto");
			sql.append(" SET com_ecf_venda_produto.prod_produto_id = prod_produto.prod_produto_id");
			sql.append(" WHERE com_ecf_venda_produto.prod_produto_id is null");
			sql.append(" AND com_ecf_venda_produto.com_ecf_venda_produto_descricao = prod_produto.prod_produto_descricao");
			rs = em.createNativeQuery(sql.toString());
			// efetiva a transacao
			total += rs.executeUpdate();
			em.getTransaction().commit();

			UtilServer.LOG.debug("Total de produtos atualizados = " + total);
		} catch (Exception ex) {
			// volta ao estado anterior
			if (em != null && em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
		} finally {
			em.close();
			emf.close();
		}
	}

}
