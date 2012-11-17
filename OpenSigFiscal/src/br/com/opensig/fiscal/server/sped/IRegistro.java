package br.com.opensig.fiscal.server.sped;

import java.io.File;
import java.io.Writer;
import java.util.Date;
import java.util.List;

import br.com.opensig.comercial.shared.modelo.ComCompra;
import br.com.opensig.comercial.shared.modelo.ComEcfNota;
import br.com.opensig.comercial.shared.modelo.ComEcfZ;
import br.com.opensig.comercial.shared.modelo.ComFrete;
import br.com.opensig.comercial.shared.modelo.ComVenda;
import br.com.opensig.core.shared.modelo.Autenticacao;
import br.com.opensig.fiscal.client.servico.FiscalService;
import br.com.opensig.fiscal.shared.modelo.FisSpedBloco;
import br.com.opensig.fiscal.shared.modelo.FisSpedFiscal;
import br.com.opensig.produto.shared.modelo.ProdProduto;

public interface IRegistro<E, T> {

	public File getLeitor();

	public void setLeitor(File leitor);

	public Writer getEscritor();

	public void setEscritor(Writer escritor);

	public FisSpedFiscal getSped();

	public void setSped(FisSpedFiscal sped);

	public FiscalService getService();

	public void setService(FiscalService service);

	public Autenticacao getAuth();

	public void setAuth(Autenticacao auth);

	public int getQtdLinhas();

	public void setQtdLInhas(int qtdLinhas);

	public Date getInicio();

	public void setInicio(Date inicio);

	public Date getFim();

	public void setFim(Date fim);

	public double getPis();

	public void setPis(double pis);

	public double getCofins();

	public void setCofins(double cofins);

	public boolean getFimBloco();

	public void setFimBloco(boolean fimBloco);

	public List<FisSpedBloco> getBlocos();

	public void setBlocos(List<FisSpedBloco> blocos);

	public List<ComCompra> getCompras();

	public void setCompras(List<ComCompra> compras);

	public List<ComFrete> getFretes();

	public void setFretes(List<ComFrete> fretes);

	public List<ComVenda> getVendas();

	public void setVendas(List<ComVenda> vendas);

	public List<ComEcfZ> getZs();

	public void setZs(List<ComEcfZ> zs);

	public List<ComEcfNota> getNotas();

	public void setNotas(List<ComEcfNota> notas);

	public List<ProdProduto> getEstoque();

	public void setEstoque(List<ProdProduto> estoque);

	public T getDados();

	public void setDados(T dados);

	public E getBloco();

	public void setBloco(E bloco);

	public void executar();

}
