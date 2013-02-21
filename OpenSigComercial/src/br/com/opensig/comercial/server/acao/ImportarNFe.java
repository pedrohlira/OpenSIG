package br.com.opensig.comercial.server.acao;

import java.util.Date;
import java.util.List;

import br.com.opensig.comercial.client.servico.ComercialException;
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
import br.com.opensig.nfe.TNFe.InfNFe.Det.Imposto.ICMS;
import br.com.opensig.produto.shared.modelo.ProdEmbalagem;
import br.com.opensig.produto.shared.modelo.ProdIpi;
import br.com.opensig.produto.shared.modelo.ProdOrigem;
import br.com.opensig.produto.shared.modelo.ProdProduto;
import br.com.opensig.produto.shared.modelo.ProdTipo;
import br.com.opensig.produto.shared.modelo.ProdTributacao;

public abstract class ImportarNFe<E extends Dados> implements IImportacao<E> {

	protected CoreService servico;
	protected EmpEmpresa empresa;
	protected Autenticacao auth;
	protected List<ProdOrigem> origens;
	protected List<ProdTributacao> tributacao;
	protected List<ProdIpi> ipis;
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

	protected MyIcms getIcms(ICMS icms) {
		MyIcms myicms = new MyIcms();

		// normal
		if (icms.getICMS00() != null) {
			myicms.setAliquota(icms.getICMS00().getPICMS());
			myicms.setCst(icms.getICMS00().getCST());
			myicms.setOrigem(icms.getICMS00().getOrig());
		} else if (icms.getICMS10() != null) {
			myicms.setAliquota(icms.getICMS10().getPICMS());
			myicms.setCst(icms.getICMS10().getCST());
			myicms.setOrigem(icms.getICMS10().getOrig());
		} else if (icms.getICMS20() != null) {
			myicms.setAliquota(icms.getICMS20().getPICMS());
			myicms.setCst(icms.getICMS20().getCST());
			myicms.setOrigem(icms.getICMS20().getOrig());
		} else if (icms.getICMS30() != null) {
			myicms.setAliquota("0");
			myicms.setCst(icms.getICMS30().getCST());
			myicms.setOrigem(icms.getICMS30().getOrig());
		} else if (icms.getICMS40() != null) {
			myicms.setAliquota("0");
			myicms.setCst(icms.getICMS40().getCST());
			myicms.setOrigem(icms.getICMS40().getOrig());
		} else if (icms.getICMS51() != null) {
			myicms.setAliquota(icms.getICMS51().getPICMS());
			myicms.setCst(icms.getICMS51().getCST());
			myicms.setOrigem(icms.getICMS51().getOrig());
		} else if (icms.getICMS60() != null) {
			myicms.setAliquota("0");
			myicms.setCst(icms.getICMS60().getCST());
			myicms.setOrigem(icms.getICMS60().getOrig());
		} else if (icms.getICMS70() != null) {
			myicms.setAliquota(icms.getICMS70().getPICMS());
			myicms.setCst(icms.getICMS70().getCST());
			myicms.setOrigem(icms.getICMS70().getOrig());
		} else if (icms.getICMS90() != null) {
			myicms.setAliquota(icms.getICMS90().getPICMS());
			myicms.setCst(icms.getICMS90().getCST());
			myicms.setOrigem(icms.getICMS90().getOrig());
			// simples
		} else if (icms.getICMSSN101() != null) {
			myicms.setAliquota("0");
			myicms.setCst(icms.getICMSSN101().getCSOSN());
			myicms.setOrigem(icms.getICMSSN101().getOrig());
		} else if (icms.getICMSSN102() != null) {
			myicms.setAliquota("0");
			myicms.setCst(icms.getICMSSN102().getCSOSN());
			myicms.setOrigem(icms.getICMSSN102().getOrig());
		} else if (icms.getICMSSN201() != null) {
			myicms.setAliquota("0");
			myicms.setCst(icms.getICMSSN201().getCSOSN());
			myicms.setOrigem(icms.getICMSSN201().getOrig());
		} else if (icms.getICMSSN202() != null) {
			myicms.setAliquota("0");
			myicms.setCst(icms.getICMSSN202().getCSOSN());
			myicms.setOrigem(icms.getICMSSN202().getOrig());
		} else if (icms.getICMSSN500() != null) {
			myicms.setAliquota("0");
			myicms.setCst(icms.getICMSSN500().getCSOSN());
			myicms.setOrigem(icms.getICMSSN500().getOrig());
		} else if (icms.getICMSSN900() != null) {
			myicms.setAliquota("0");
			myicms.setCst(icms.getICMSSN900().getCSOSN());
			myicms.setOrigem(icms.getICMSSN900().getOrig());
		}

		return myicms;
	}

	protected ProdProduto getProduto(EmpFornecedor fornecedor, Prod prod, MyIcms icms, String ipi) throws OpenSigException {
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
			produto.setProdEmbalagem(getEmbalagem(prod.getUCom()));
			produto.setProdTipo(new ProdTipo(1));
			produto.setProdIpi(getIpi(ipi));
			produto.setProdOrigem(getOrigem(icms.getOrigem()));
			produto.setEmpFornecedor(fornecedor);
			produto.setEmpFabricante(fornecedor);
			produto.setProdTributacao(getTributacao(icms.getCst()));
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
			produto.setProdEmbalagem(getEmbalagem("UND"));
			produto.setProdTipo(new ProdTipo(1));
			produto.setProdProdutoVolume(1);
			produto.setProdOrigem(getOrigem(icms.getOrigem()));
			produto.setEmpFornecedor(fornecedor);
			produto.setEmpFabricante(fornecedor);
			produto.setProdTributacao(getTributacao(icms.getCst()));
			produto.setProdIpi(getIpi(ipi));
			produto.setProdProdutoAtivo(true);
			produto.setProdProdutoCategoria(auth.getConf().get("categoria.padrao") + "::");
			produto.setProdProdutoCadastrado(new Date());
			produto.setProdProdutoCusto(Double.valueOf(prod.getVUnCom()));
			produto.setProdProdutoPreco(0.00);
			produto.setProdProdutoObservacao("");
		}

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
			} else if (emb.getProdEmbalagemId() == 1) {
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

	protected ProdTributacao getTributacao(String cst) {
		// se nao achar colocar a padrao 00
		ProdTributacao resp = null;

		// percorre as tributacoes
		for (ProdTributacao trib : tributacao) {
			if (cst.length() == 2 && trib.getProdTributacaoCst().equals(cst)) {
				resp = trib;
				break;
			} else if (cst.length() == 3 && trib.getProdTributacaoCson().equals(cst)) {
				resp = trib;
				break;
			} else if (trib.getProdTributacaoCst().equals("00")) {
				resp = trib;
			}
		}

		return resp;
	}

	protected ProdIpi getIpi(String cst) {
		// se nao achar colocar a padrao 00
		ProdIpi resp = null;

		// percorre as tributacoes
		for (ProdIpi ipi : ipis) {
			if (ipi.getProdIpiCstSaida().equals(cst)) {
				resp = ipi;
				break;
			} else if (ipi.getProdIpiId() == 1) {
				resp = ipi;
			}
		}

		return resp;
	}

	protected class MyIcms {

		private String aliquota;
		private String origem;
		private String cst;

		public MyIcms() {
		}

		public String getAliquota() {
			return aliquota;
		}

		public void setAliquota(String aliquota) {
			this.aliquota = aliquota;
		}

		public String getOrigem() {
			return origem;
		}

		public void setOrigem(String origem) {
			this.origem = origem;
		}

		public String getCst() {
			return cst;
		}

		public void setCst(String cst) {
			this.cst = cst;
		}
	}

}
