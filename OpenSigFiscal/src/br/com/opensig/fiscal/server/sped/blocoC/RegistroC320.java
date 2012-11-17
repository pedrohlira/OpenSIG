package br.com.opensig.fiscal.server.sped.blocoC;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.beanio.BeanWriter;
import org.beanio.StreamFactory;

import br.com.opensig.comercial.shared.modelo.ComEcfNotaProduto;
import br.com.opensig.core.server.UtilServer;
import br.com.opensig.fiscal.server.sped.ARegistro;
import br.com.opensig.produto.shared.modelo.ProdProduto;

public class RegistroC320 extends ARegistro<DadosC320, List<ComEcfNotaProduto>> {

	private Map<Integer, List<ComEcfNotaProduto>> produtos = new HashMap<Integer, List<ComEcfNotaProduto>>();

	@Override
	public void executar() {
		try {
			StreamFactory factory = StreamFactory.newInstance();
			factory.load(getClass().getResourceAsStream(bean));
			BeanWriter out = factory.createWriter("EFD", escritor);
			bloco = getDados(dados);
			out.write(bloco);
			out.flush();
			
			// itens diarios
			if (!produtos.isEmpty()) {
				RegistroC321 r321 = new RegistroC321();
				r321.setEscritor(escritor);
				for (Entry<Integer, List<ComEcfNotaProduto>> entry : produtos.entrySet()) {
					r321.setDados(entry.getValue());
					r321.executar();
					qtdLinhas++;
				}
			}
		} catch (Exception e) {
			qtdLinhas = 0;
			UtilServer.LOG.error("Erro na geracao do Registro -> " + bean, e);
		}
	}

	@Override
	protected DadosC320 getDados(List<ComEcfNotaProduto> dados) throws Exception {
		DadosC320 d = new DadosC320();
		for (ComEcfNotaProduto np : dados) {
			ProdProduto prod = np.getProdProduto();
			d.setCst_icms(auth.getConf().get("nfe.crt").equals("1") ? prod.getProdTributacao().getProdTributacaoCson() : prod.getProdTributacao().getProdTributacaoCst());
			d.setCfop(prod.getProdTributacao().getProdTributacaoCfop());
			d.setAliq_icms(np.getComEcfNotaProdutoIcms());
			d.setVl_opr(d.getVl_opr() + np.getComEcfNotaProdutoLiquido());
			d.setCod_obs("");

			// agrupando os produtos pelo id
			List<ComEcfNotaProduto> lista = produtos.get(prod.getId());
			if (lista == null) {
				lista = new ArrayList<ComEcfNotaProduto>();
				lista.add(np);
				produtos.put(prod.getProdProdutoId(), lista);
			} else {
				lista.add(np);
			}
		}
		if (d.getAliq_icms() > 0) {
			d.setVl_bc_icms(d.getVl_opr());
			d.setVl_icms(d.getVl_bc_icms() * d.getAliq_icms() / 100);
		}
		
		normalizar(d);
		qtdLinhas++;
		return d;
	}
}
