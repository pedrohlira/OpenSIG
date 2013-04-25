package br.com.opensig.comercial.server.acao;

import java.util.Date;
import java.util.List;

import br.com.opensig.comercial.client.servico.ComercialException;
import br.com.opensig.comercial.server.MyIcms;
import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.EJuncao;
import br.com.opensig.core.client.controlador.filtro.FiltroTexto;
import br.com.opensig.core.client.controlador.filtro.GrupoFiltro;
import br.com.opensig.core.client.servico.CoreService;
import br.com.opensig.core.client.servico.OpenSigException;
import br.com.opensig.core.server.importar.IImportacao;
import br.com.opensig.core.shared.modelo.Autenticacao;
import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.core.shared.modelo.Lista;
import br.com.opensig.empresa.shared.modelo.EmpEmpresa;
import br.com.opensig.empresa.shared.modelo.EmpEntidade;
import br.com.opensig.empresa.shared.modelo.EmpFornecedor;
import br.com.opensig.nfe.TNFe;
import br.com.opensig.nfe.TNFe.InfNFe.Det.Prod;
import br.com.opensig.produto.shared.modelo.ProdCofins;
import br.com.opensig.produto.shared.modelo.ProdEmbalagem;
import br.com.opensig.produto.shared.modelo.ProdIcms;
import br.com.opensig.produto.shared.modelo.ProdIpi;
import br.com.opensig.produto.shared.modelo.ProdOrigem;
import br.com.opensig.produto.shared.modelo.ProdPis;
import br.com.opensig.produto.shared.modelo.ProdProduto;
import br.com.opensig.produto.shared.modelo.ProdTipo;

public abstract class ImportarNFe<E extends Dados> implements IImportacao<E> {

	protected CoreService servico;
	protected EmpEmpresa empresa;
	protected Autenticacao auth;
	protected List<ProdOrigem> origens;
	protected List<ProdIcms> icmss;
	protected List<ProdIpi> ipis;
	protected List<ProdPis> piss;
	protected List<ProdCofins> cofinss;
	protected List<ProdEmbalagem> embalagem;
	protected TNFe nfe;

	protected void validarEmpresa(String cnpj) throws OpenSigException {
		if (cnpj.equals(auth.getEmpresa()[5].replaceAll("\\D", ""))) {
			EmpEntidade ent = new EmpEntidade(Integer.valueOf(auth.getEmpresa()[1]));
			ent.setEmpEntidadeNome1(auth.getEmpresa()[2]);
			ent.setEmpEntidadeNome2(auth.getEmpresa()[3]);
			ent.setEmpEntidadePessoa(auth.getEmpresa()[4]);
			ent.setEmpEntidadeDocumento1(auth.getEmpresa()[5]);
			ent.setEmpEntidadeDocumento2(auth.getEmpresa()[6]);
			ent.setEmpEntidadeObservacao("");
			ent.setEmpEntidadeData(new Date());
			empresa = new EmpEmpresa(Integer.valueOf(auth.getEmpresa()[0]));
			empresa.setEmpEntidade(ent);
		} else {
			throw new ComercialException("O emitente/destinatario não é a empresa logada!");
		}
	}


	protected ProdProduto getProduto(EmpFornecedor fornecedor, Prod prod, MyIcms icms) throws OpenSigException {
		String ean = prod.getCEAN();
		String ref = prod.getCProd();
		GrupoFiltro filtro = new GrupoFiltro();

		if (ean != null && !ean.equals("")) {
			// barra
			FiltroTexto ft1 = new FiltroTexto("prodProdutoBarra", ECompara.IGUAL, ean);
			filtro.add(ft1, EJuncao.OU);
			// barra do preco
			FiltroTexto ft2 = new FiltroTexto("prodPrecoBarra", ECompara.IGUAL, ean);
			ft2.setCampoPrefixo("t2.");
			filtro.add(ft2, EJuncao.OU);
			// barra do grade
			FiltroTexto ft3 = new FiltroTexto("prodGradeBarra", ECompara.IGUAL, ean);
			ft3.setCampoPrefixo("t3.");
			filtro.add(ft3);
		} else {
			// referencia
			FiltroTexto ft1 = new FiltroTexto("prodProdutoReferencia", ECompara.IGUAL, ref);
			filtro.add(ft1);
		}

		// busca
		ProdProduto produto = new ProdProduto();
		Lista<ProdProduto> lista = servico.selecionar(produto, 0, 1, filtro, false);
		// verifica se achou
		if (lista.getTotal() == 1) {
			produto = lista.getLista().get(0);
			fornecedor = produto.getEmpFornecedor();
			fornecedor.anularDependencia();
			produto.anularDependencia();

			// caso nao acha cria um novo para confirmar
		} else {
			produto.setProdProdutoNcm(prod.getNCM());
			produto.setProdProdutoBarra(ean);
			if ((ref.length() == 8 || ref.length() >= 12) && ref.equals(ref.replaceAll("\\D", ""))) {
				produto.setProdProdutoReferencia("");
			} else {
				produto.setProdProdutoReferencia(ref);
			}
			produto.setProdProdutoDescricao(prod.getXProd());
			produto.setProdProdutoVolume(1);
			produto.setProdProdutoAtivo(true);
			produto.setProdProdutoCategoria(auth.getConf().get("categoria.padrao") + "::");
			produto.setProdProdutoCadastrado(new Date());
			produto.setProdProdutoCusto(Double.valueOf(prod.getVUnCom()));
			produto.setProdProdutoPreco(0.00);
			produto.setProdProdutoObservacao("");
		}
		produto.setProdEmbalagem(getEmbalagem(prod.getUCom()));
		produto.setProdTipo(new ProdTipo(1));
		produto.setProdOrigem(getOrigem(icms.getOrigem()));
		produto.setProdIcms(getIcms(icms.getCst()));
		produto.setProdIpi(getIpi());
		produto.setProdPis(getPis());
		produto.setProdCofins(getCofins());
		produto.setEmpFornecedor(fornecedor);
		produto.setEmpFabricante(fornecedor);
		
		return produto;
	}

	protected ProdEmbalagem getEmbalagem(String nome) {
		// se nao achar colocar a padrao UND
		ProdEmbalagem resp = null;

		// percorre as embalagens
		for (ProdEmbalagem emb : embalagem) {
			if (emb.getProdEmbalagemNome().equalsIgnoreCase(nome)) {
				resp = emb;
				break;
			} else if (emb.getProdEmbalagemNome().equalsIgnoreCase("UND")) {
				resp = emb;
			}
		}

		return resp;
	}

	protected ProdOrigem getOrigem(String origem) {
		// se nao achar colocar a padrao 0
		ProdOrigem resp = null;

		// percorre as origens
		for (ProdOrigem ori : origens) {
			if (ori.getProdOrigemValor() == Integer.valueOf(origem)) {
				resp = ori;
				break;
			} else if (ori.getProdOrigemValor() == 0) {
				resp = ori;
			}
		}

		return resp;
	}

	protected ProdIcms getIcms(String cst) {
		// se nao achar colocar a padrao 00
		ProdIcms resp = null;

		// percorre os icms
		for (ProdIcms icms : icmss) {
			if (cst.length() == 2 && icms.getProdIcmsCst().equals(cst)) {
				resp = icms;
				break;
			} else if (cst.length() == 3 && icms.getProdIcmsCson().equals(cst)) {
				resp = icms;
				break;
			} else if (icms.getProdIcmsCst().equals("00")) {
				resp = icms;
			}
		}

		return resp;
	}

	protected ProdIpi getIpi() {
		ProdIpi resp = null;
		String cst = auth.getConf().get("sped.fiscal.0000.ind_ativ").equals("0") ? "50" : "99";

		for (ProdIpi ipi : ipis) {
			if (ipi.getProdIpiCstSaida().equals(cst)) {
				resp = ipi;
				break;
			} else if (ipi.getProdIpiCstSaida().equals("50")) {
				resp = ipi;
			}
		}

		return resp;
	}

	protected ProdPis getPis() {
		ProdPis resp = null;
		String cst = auth.getConf().get("nfe.crt").equals("3") ? "01" : "49";

		for (ProdPis pis : piss) {
			if (pis.getProdPisCstSaida().equals(cst)) {
				resp = pis;
				break;
			} else if (pis.getProdPisCstSaida().equals("01")) {
				resp = pis;
			}
		}

		return resp;
	}

	protected ProdCofins getCofins() {
		ProdCofins resp = null;
		String cst = auth.getConf().get("nfe.crt").equals("3") ? "01" : "49";

		for (ProdCofins cofins : cofinss) {
			if (cofins.getProdCofinsCstSaida().equals(cst)) {
				resp = cofins;
				break;
			} else if (cofins.getProdCofinsCstSaida().equals("01")) {
				resp = cofins;
			}
		}

		return resp;
	}

}
