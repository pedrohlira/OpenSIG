package br.com.opensig.fiscal.server.sped.blocoC;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.beanio.BeanWriter;
import org.beanio.StreamFactory;

import br.com.opensig.comercial.shared.modelo.ComEcfVenda;
import br.com.opensig.comercial.shared.modelo.ComEcfVendaProduto;
import br.com.opensig.comercial.shared.modelo.ComEcfZTotais;
import br.com.opensig.core.server.UtilServer;
import br.com.opensig.fiscal.server.sped.ARegistro;

public class RegistroC420 extends ARegistro<DadosC420, ComEcfZTotais> {

	private Map<Integer, ComEcfVendaProduto> produtos = new HashMap<Integer, ComEcfVendaProduto>();

	@Override
	public void executar() {
		try {
			StreamFactory factory = StreamFactory.newInstance();
			factory.load(getClass().getResourceAsStream(bean));
			BeanWriter out = factory.createWriter("EFD", escritor);

			for (ComEcfVenda venda : dados.getComEcfZ().getComEcfVendas()) {
				if (venda.getComEcfVendaFechada() && !venda.getComEcfVendaCancelada()) {
					for (ComEcfVendaProduto pv : venda.getComEcfVendaProdutos()) {
						if (pv.getProdProduto().getProdTributacao().getProdTributacaoEcf().equals(dados.getComEcfZTotaisCodigo())) {
							ComEcfVendaProduto vp = produtos.get(pv.getProdProduto().getProdProdutoId());
							if (vp == null) {
								produtos.put(pv.getProdProduto().getProdProdutoId(), pv);
							} else {
								vp.setComEcfVendaProdutoQuantidade(vp.getComEcfVendaProdutoQuantidade() + pv.getComEcfVendaProdutoQuantidade());
								vp.setComEcfVendaProdutoTotal(vp.getComEcfVendaProdutoTotal() + pv.getComEcfVendaProdutoTotal());
							}
						}
					}
				}
			}

			bloco = getDados(dados);
			out.write(bloco);
			out.flush();

			// somente perfil B
			if (auth.getConf().get("sped.0000.ind_perfil").equals("B")) {
				RegistroC425 r425 = new RegistroC425();
				r425.setEscritor(escritor);
				r425.setAuth(auth);
				for (Entry<Integer, ComEcfVendaProduto> vp : produtos.entrySet()) {
					if (vp.getValue().getProdProduto().getProdTributacao().getProdTributacaoEcf().equals(dados.getComEcfZTotaisCodigo())) {
						r425.setDados(vp.getValue());
						r425.executar();
						qtdLinhas += r425.getQtdLinhas();
					}
				}
			}
		} catch (Exception e) {
			qtdLinhas = 0;
			UtilServer.LOG.error("Erro na geracao do Registro -> " + bean, e);
		}
	}

	@Override
	protected DadosC420 getDados(ComEcfZTotais dados) throws Exception {
		DadosC420 d = new DadosC420();
		d.setCod_tot_par(dados.getComEcfZTotaisCodigo());
		d.setVlr_acum_tot(dados.getComEcfZTotaisValor());
		if (dados.getComEcfZTotaisCodigo().length() == 7) {
			d.setNr_tot(dados.getComEcfZTotaisCodigo().substring(0, 2));
			d.setDescr_nr_tot("Tributado");
		}

		normalizar(d);
		qtdLinhas++;
		return d;
	}
}
