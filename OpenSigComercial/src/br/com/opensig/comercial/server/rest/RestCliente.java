package br.com.opensig.comercial.server.rest;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

import org.apache.log4j.Logger;

import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.EJuncao;
import br.com.opensig.core.client.controlador.filtro.FiltroBinario;
import br.com.opensig.core.client.controlador.filtro.FiltroData;
import br.com.opensig.core.client.controlador.filtro.FiltroNumero;
import br.com.opensig.core.client.controlador.filtro.FiltroObjeto;
import br.com.opensig.core.client.controlador.filtro.FiltroTexto;
import br.com.opensig.core.client.controlador.filtro.GrupoFiltro;
import br.com.opensig.core.client.controlador.filtro.IFiltro;
import br.com.opensig.core.client.controlador.parametro.ParametroException;
import br.com.opensig.core.client.servico.CoreException;
import br.com.opensig.core.server.UtilServer;
import br.com.opensig.core.shared.modelo.EBusca;
import br.com.opensig.financeiro.shared.modelo.FinForma;
import br.com.opensig.fiscal.shared.modelo.FisNotaSaida;
import br.com.opensig.permissao.shared.modelo.SisConfiguracao;
import br.com.opensig.permissao.shared.modelo.SisGrupo;
import br.com.opensig.permissao.shared.modelo.SisUsuario;
import br.com.opensig.produto.shared.modelo.ProdEmbalagem;
import br.com.opensig.produto.shared.modelo.ProdEstoque;
import br.com.opensig.produto.shared.modelo.ProdProduto;

/**
 * Classe que representa a comunicacao do Servidor para o Cliente via Rest
 * 
 * @author Pedro H. Lira
 */
@Provider
@Path("/host")
public class RestCliente extends ARest {

	/**
	 * Construtor padrao.
	 */
	public RestCliente() {
		super();
		log = Logger.getLogger(RestCliente.class);
	}

	@GET
	@Produces(MediaType.TEXT_HTML)
	@Override
	public String ajuda() throws RestException {
		return super.ajuda();
	}

	/**
	 * Metodo que retorna o proximo numero de NFe a ser usado.
	 * 
	 * @return uma string com o nuemro da NFe.
	 * @throws RestException
	 *             em caso de nao conseguir acessar a informacao.
	 */
	@Path("/nfe")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getNfe() throws RestException {
		autorizar();
		try {
			FiltroObjeto fo = new FiltroObjeto("empEmpresa", ECompara.IGUAL, ecf.getEmpEmpresa());
			Number nfe = service.buscar(new FisNotaSaida(), "fisNotaSaidaNumero", EBusca.MAXIMO, fo);
			Integer resp;
			if (nfe != null && nfe.intValue() > 0) {
				resp = nfe.intValue() + 1;
			} else {
				FiltroTexto ft = new FiltroTexto("sisConfiguracaoChave", ECompara.IGUAL, "NFE.NUMERO");
				SisConfiguracao config = (SisConfiguracao) service.selecionar(new SisConfiguracao(), ft, false);
				resp = Integer.valueOf(config.getSisConfiguracaoValor()) + 1;
			}
			return resp.toString();
		} catch (Exception ex) {
			log.error(ex);
			throw new RestException(ex);
		}
	}

	/**
	 * Metodo que retorna a lista de usuario permitidos ao acesso ao sistema,
	 * devem estar vinculado ao grupo com nome CAIXA, para ser gerente precisar
	 * ter valor de desconto maior que zero.
	 * 
	 * @return uma lista de objetos usuario em formato JSON.
	 * @throws RestException
	 *             em caso de nao conseguir acessar a informacao.
	 */
	@Path("/usuario")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<SisUsuario> getUsuario() throws RestException {
		autorizar();
		try {
			FiltroTexto ft = getFiltroCnpj("empEntidade.empEntidadeDocumento1");
			ft.setCampoPrefixo("t1.");
			List<SisUsuario> usuarios = service.selecionar(new SisUsuario(), 0, 0, ft, false).getLista();
			List<SisUsuario> escolhidos = new ArrayList<SisUsuario>();

			for (SisUsuario usuario : usuarios) {
				for (SisGrupo grupo : usuario.getSisGrupos()) {
					if (grupo.getSisGrupoNome().equalsIgnoreCase("caixa")) {
						usuario.setSisUsuarioCaixa(true);
						if (usuario.getSisUsuarioDesconto() > 0) {
							usuario.setSisUsuarioGerente(true);
						}
						break;
					}
				}
				escolhidos.add(usuario);
			}
			return escolhidos;
		} catch (Exception ex) {
			log.error(ex);
			throw new RestException(ex);
		}
	}

	/**
	 * Metodo que retorna a lista de tipos de pagamento cadastrados no sistema.
	 * 
	 * @return uma lista de objetos tipos de pagamento em formato JSON.
	 * @throws RestException
	 *             em caso de nao conseguir acessar a informacao.
	 */
	@Path("/tipo_pagamento")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<FinForma> getPagamentoTipo() throws RestException {
		autorizar();
		try {
			FiltroTexto ft = new FiltroTexto("finFormaCodigo", ECompara.DIFERENTE, "00");
			FiltroBinario fb = new FiltroBinario("finFormaReceber", ECompara.IGUAL, 1);
			GrupoFiltro gf = new GrupoFiltro(EJuncao.E, new IFiltro[] { ft, fb });
			return service.selecionar(new FinForma(), 0, 0, gf, false).getLista();
		} catch (Exception ex) {
			log.error(ex);
			throw new RestException(ex);
		}
	}

	/**
	 * Metodo que retorna a lista de embalagens cadastradas no sistema.
	 * 
	 * @return uma lista de objetos embalagem em formato JSON.
	 * @throws RestException
	 *             em caso de nao conseguir acessar a informacao.
	 */
	@Path("/embalagem")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<ProdEmbalagem> getEmbalagem() throws RestException {
		autorizar();
		try {
			return service.selecionar(new ProdEmbalagem(), 0, 0, null, false).getLista();
		} catch (Exception ex) {
			log.error(ex);
			throw new RestException(ex);
		}
	}

	/**
	 * Metodo que retorna a lista de novos produtos cadastrados no sistema.
	 * 
	 * @param id
	 *            o ultimo id cadastro no banco do pdv.
	 * @param pagina
	 *            numero da pagina de retorno dos dados comecando pelo ZERO.
	 * @param limite
	 *            limite de registros a serem retornados.
	 * @return uma lista de produtos novos cadastrados no sistema.
	 * @throws RestException
	 *             em caso de nao conseguir acessar a informacao.
	 */
	@Path("/produtoNovo")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<ProdProduto> getProdutoNovo(@QueryParam("id") int id, @QueryParam("pagina") int pagina, @QueryParam("limite") int limite) throws RestException {
		autorizar();
		try {
			FiltroNumero fn = new FiltroNumero("prodProdutoId", ECompara.MAIOR, id);
			ProdProduto prod = new ProdProduto();
			prod.setCampoOrdem("prodProdutoId");
			List<ProdProduto> produtos = service.selecionar(prod, pagina * limite, limite, fn, false).getLista();
			setValoresProduto(produtos);
			return produtos;
		} catch (Exception ex) {
			log.error(ex);
			throw new RestException(ex);
		}
	}

	/**
	 * Metodo que retorna a lista de novos produtos atualizados no sistema.
	 * 
	 * @param data
	 *            data usada como corte para considerar produto atualizado.
	 * @param pagina
	 *            numero da pagina de retorno dos dados comecando pelo ZERO.
	 * @param limite
	 *            limite de registros a serem retornados.
	 * @return uma lista de produtos novos cadastrados no sistema.
	 * @throws RestException
	 *             em caso de nao conseguir acessar a informacao.
	 */
	@Path("/produtoAtualizado")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<ProdProduto> getProdutoAtualizado(@QueryParam("data") String data, @QueryParam("pagina") int pagina, @QueryParam("limite") int limite) throws RestException {
		autorizar();
		try {
			Date alterado = UtilServer.formataData(data, "dd/MM/yyyy HH:mm:ss");
			IFiltro filtro = null;
			if (alterado != null) {
				FiltroData fd1 = new FiltroData("prodProdutoAlterado", ECompara.MAIOR, alterado);
				FiltroData fd2 = new FiltroData("prodProdutoCadastrado", ECompara.MENOR, alterado);
				filtro = new GrupoFiltro(EJuncao.E, new IFiltro[] { fd1, fd2 });
			}
			ProdProduto prod = new ProdProduto();
			prod.setCampoOrdem("prodProdutoAlterado");

			List<ProdProduto> produtos = service.selecionar(prod, pagina * limite, limite, filtro, false).getLista();
			setValoresProduto(produtos);
			return produtos;
		} catch (Exception ex) {
			log.error(ex);
			throw new RestException(ex);
		}
	}

	/**
	 * Metodo que gera o filtro de empresa usando o cnpj da autorizacao.
	 * 
	 * @param campo
	 *            o nome do campo a ser usadao.
	 * @return Um objeto do tipo filtro de texto.
	 * @throws ParseException
	 *             dispara uma exececao caso nao consiga executar.
	 */
	private FiltroTexto getFiltroCnpj(String campo) throws ParseException {
		cnpj = cnpj.replaceAll("[^0-9]", "");
		cnpj = UtilServer.formataTexto(cnpj, "##.###.###/####-##");
		return new FiltroTexto(campo, ECompara.IGUAL, cnpj);
	}

	/**
	 * Metodo que seta os valores de acordo com o esperado pelo ECF.
	 * 
	 * @param produtos
	 *            uma lista de produtos.
	 * @throws CoreException
	 * @throws ParametroException
	 */
	private void setValoresProduto(List<ProdProduto> produtos) throws CoreException {
		FiltroObjeto fo = new FiltroObjeto("empEmpresa", ECompara.IGUAL, ecf.getEmpEmpresa());
		FiltroTexto ft = new FiltroTexto("sisConfiguracaoChave", ECompara.IGUAL, "NFE.CRT");
		GrupoFiltro gf = new GrupoFiltro(EJuncao.E, new IFiltro[] { fo, ft });
		SisConfiguracao config = (SisConfiguracao) service.selecionar(new SisConfiguracao(), gf, false);

		for (ProdProduto produto : produtos) {
			// seta o arrendondamento e fabricacao
			produto.setProdProdutoIat('A');
			produto.setProdProdutoIppt('T');
			// verifica se a empresa e simples
			if (config.getSisConfiguracaoValor().equals("1")) {
				produto.setProdProdutoCstCson(produto.getProdTributacao().getProdTributacaoCson());
			} else {
				produto.setProdProdutoCstCson(produto.getProdTributacao().getProdTributacaoCst());
			}
			// verifica a tributacao do produto na ecf
			if (produto.getProdTributacao().getProdTributacaoEcf().length() > 2) {
				produto.setProdProdutoTributacao(produto.getProdTributacao().getProdTributacaoEcf().charAt(2));
			} else {
				produto.setProdProdutoTributacao(produto.getProdTributacao().getProdTributacaoEcf().charAt(0));
			}
			// icms ou issqn
			if (produto.getProdProdutoTributacao() == 'S') {
				produto.setProdProdutoIssqn(produto.getProdTributacao().getProdTributacaoDentro());
				produto.setProdProdutoIcms(0.00);
			} else {
				produto.setProdProdutoIssqn(0.00);
				produto.setProdProdutoIcms(produto.getProdTributacao().getProdTributacaoDentro());
			}
			// estoque do produto nesta empresa
			for (ProdEstoque est : produto.getProdEstoques()) {
				if (cnpj.equals(est.getEmpEmpresa().getEmpEntidade().getEmpEntidadeDocumento1().replaceAll("[^0-9]", ""))) {
					produto.setProdProdutoEstoque(est.getProdEstoqueQuantidade());
					break;
				}
			}
		}
	}
}
