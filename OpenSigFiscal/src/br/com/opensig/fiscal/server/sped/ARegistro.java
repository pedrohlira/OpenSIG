package br.com.opensig.fiscal.server.sped;

import java.io.File;
import java.io.Writer;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;

import org.beanio.BeanWriter;
import org.beanio.StreamFactory;

import br.com.opensig.comercial.shared.modelo.ComCompra;
import br.com.opensig.comercial.shared.modelo.ComEcfNota;
import br.com.opensig.comercial.shared.modelo.ComEcfZ;
import br.com.opensig.comercial.shared.modelo.ComFrete;
import br.com.opensig.comercial.shared.modelo.ComVenda;
import br.com.opensig.core.server.UtilServer;
import br.com.opensig.core.shared.modelo.Autenticacao;
import br.com.opensig.fiscal.client.servico.FiscalService;
import br.com.opensig.fiscal.shared.modelo.FisSpedBloco;
import br.com.opensig.fiscal.shared.modelo.FisSpedFiscal;
import br.com.opensig.produto.shared.modelo.ProdProduto;

public abstract class ARegistro<E extends Bean, T> implements IRegistro<E, T> {

	protected static FisSpedFiscal sped;
	protected static FiscalService service;
	protected static Autenticacao auth;
	protected static Date inicio;
	protected static Date fim;
	protected static double pis;
	protected static double cofins;
	protected static List<FisSpedBloco> blocos;
	protected static List<ComCompra> compras;
	protected static List<ComFrete> fretes;
	protected static List<ComVenda> vendas;
	protected static List<ComEcfZ> zs;
	protected static List<ComEcfNota> notas;
	protected static List<ProdProduto> estoque;
	protected File leitor;
	protected Writer escritor;
	protected String bean;
	protected int qtdLinhas;
	protected boolean fimBloco;
	protected E bloco;
	protected T dados;

	public ARegistro() {
		bean = "/" + getClass().getName().replace("Registro", "Bean").replace('.', '/') + ".xml";
	}

	public ARegistro(String bean) {
		this.bean = bean;
	}

	@Override
	public void executar() {
		try {
			StreamFactory factory = StreamFactory.newInstance();
			factory.load(getClass().getResourceAsStream(bean));
			BeanWriter out = factory.createWriter("EFD", escritor);
			bloco = getDados(dados);
			normalizar(bloco);
			out.write(bloco);
			out.flush();
			qtdLinhas = 1;
		} catch (Exception e) {
			qtdLinhas = 0;
			UtilServer.LOG.error("Erro na geracao do Registro -> " + bean, e);
		}
	}

	protected void normalizar(E bloco) {
		for (Method metodo : bloco.getClass().getMethods()) {
			try {
				if (UtilServer.isGetter(metodo)) {
					Object valorMetodo = metodo.invoke(bloco, new Object[] {});

					if (metodo.getReturnType() == String.class) {
						String nomeMetodo = metodo.getName().replaceFirst("get", "set");
						Method set = bloco.getClass().getMethod(nomeMetodo, new Class[] { String.class });
						String valor = valorMetodo == null ? "" : valorMetodo.toString();
						valor = UtilServer.normaliza(valor).replaceAll(auth.getConf().get("nfe.regexp"), "");
						set.invoke(bloco, new Object[] { valor.trim() });
					}
				}
			} catch (Exception ex) {
				UtilServer.LOG.debug("Erro ao padronizar. " + metodo.getName(), ex);
			}
		}
	}

	protected int getSubBlocos(String letra) {
		int tot = 0;
		for (FisSpedBloco bl : blocos) {
			if (bl.getFisSpedBlocoLetra().equals(letra) && bl.getFisSpedBlocoNivel() > 1) {
				tot++;
			}
		}
		return tot;
	}

	protected abstract E getDados(T dados) throws Exception;

	@Override
	public File getLeitor() {
		return this.leitor;
	}

	@Override
	public void setLeitor(File leitor) {
		this.leitor = leitor;
	}

	@Override
	public Writer getEscritor() {
		return escritor;
	}

	@Override
	public void setEscritor(Writer arquivo) {
		this.escritor = arquivo;
	}

	@Override
	public FisSpedFiscal getSped() {
		return sped;
	}

	@Override
	public void setSped(FisSpedFiscal sped) {
		ARegistro.sped = sped;
	}

	@Override
	public FiscalService getService() {
		return service;
	}

	@Override
	public void setService(FiscalService service) {
		ARegistro.service = service;
	}

	@Override
	public Autenticacao getAuth() {
		return auth;
	}

	@Override
	public void setAuth(Autenticacao auth) {
		ARegistro.auth = auth;
	}

	@Override
	public int getQtdLinhas() {
		return qtdLinhas;
	}

	@Override
	public void setQtdLInhas(int qtdLinhas) {
		this.qtdLinhas = qtdLinhas;
	}

	@Override
	public boolean getFimBloco() {
		return fimBloco;
	}

	@Override
	public void setFimBloco(boolean fimBloco) {
		this.fimBloco = fimBloco;
	}

	@Override
	public Date getInicio() {
		return inicio;
	}

	@Override
	public void setInicio(Date inicio) {
		ARegistro.inicio = inicio;
	}

	@Override
	public Date getFim() {
		return fim;
	}

	@Override
	public void setFim(Date fim) {
		ARegistro.fim = fim;
	}

	@Override
	public double getPis() {
		return pis;
	}

	@Override
	public void setPis(double pis) {
		ARegistro.pis = pis;
	}

	@Override
	public double getCofins() {
		return cofins;
	}

	@Override
	public void setCofins(double cofins) {
		ARegistro.cofins = cofins;
	}

	@Override
	public List<FisSpedBloco> getBlocos() {
		return blocos;
	}

	@Override
	public void setBlocos(List<FisSpedBloco> blocos) {
		ARegistro.blocos = blocos;
	}

	@Override
	public List<ComCompra> getCompras() {
		return compras;
	}

	@Override
	public void setCompras(java.util.List<ComCompra> compras) {
		ARegistro.compras = compras;
	}

	@Override
	public List<ComFrete> getFretes() {
		return fretes;
	}

	@Override
	public void setFretes(List<ComFrete> fretes) {
		ARegistro.fretes = fretes;
	}

	@Override
	public List<ComVenda> getVendas() {
		return vendas;
	}

	@Override
	public void setVendas(List<ComVenda> vendas) {
		ARegistro.vendas = vendas;
	}

	@Override
	public List<ComEcfZ> getZs() {
		return zs;
	}

	@Override
	public void setZs(List<ComEcfZ> zs) {
		ARegistro.zs = zs;
	}

	@Override
	public List<ComEcfNota> getNotas() {
		return notas;
	}

	@Override
	public void setNotas(List<ComEcfNota> notas) {
		ARegistro.notas = notas;
	}

	@Override
	public List<ProdProduto> getEstoque() {
		return estoque;
	}

	@Override
	public void setEstoque(List<ProdProduto> estoque) {
		ARegistro.estoque = estoque;
	}

	@Override
	public T getDados() {
		return this.dados;
	}

	@Override
	public void setDados(T dados) {
		this.dados = dados;
	}

	@Override
	public E getBloco() {
		return this.bloco;
	}

	@Override
	public void setBloco(E bloco) {
		this.bloco = bloco;
	};
}
