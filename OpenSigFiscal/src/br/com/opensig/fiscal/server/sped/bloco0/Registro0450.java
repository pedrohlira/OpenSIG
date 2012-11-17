package br.com.opensig.fiscal.server.sped.bloco0;

import java.util.ArrayList;
import java.util.List;

import org.beanio.BeanWriter;
import org.beanio.StreamFactory;

import br.com.opensig.comercial.shared.modelo.ComVenda;
import br.com.opensig.comercial.shared.modelo.ComVendaProduto;
import br.com.opensig.core.server.UtilServer;
import br.com.opensig.fiscal.server.sped.ARegistro;
import br.com.opensig.produto.shared.modelo.ProdTributacao;

public class Registro0450 extends ARegistro<Dados0450, ProdTributacao> {

	private List<Integer> decreto;

	@Override
	public void executar() {
		decreto = new ArrayList<Integer>();

		try {
			StreamFactory factory = StreamFactory.newInstance();
			factory.load(getClass().getResourceAsStream(bean));
			BeanWriter out = factory.createWriter("EFD", escritor);
			// vendas
			for (ComVenda venda : vendas) {
				for (ComVendaProduto vProd : venda.getComVendaProdutos()) {
					if (!decreto.contains(vProd.getProdProduto().getProdTributacao().getProdTributacaoId()) && !vProd.getProdProduto().getProdTributacao().getProdTributacaoDecreto().equals("")) {
						out.write(getDados(vProd.getProdProduto().getProdTributacao()));
						out.flush();
						decreto.add(vProd.getProdProduto().getProdTributacao().getProdTributacaoId());
					}
				}
			}
		} catch (Exception e) {
			UtilServer.LOG.error("Erro na geracao do Registro -> " + bean, e);
		}
	}

	@Override
	protected Dados0450 getDados(ProdTributacao trib) {
		Dados0450 d = new Dados0450();
		d.setCod_inf(trib.getProdTributacaoId() + "");
		d.setTxt(trib.getProdTributacaoDecreto());
		
		normalizar(d);
		qtdLinhas++;
		return d;
	}
}
