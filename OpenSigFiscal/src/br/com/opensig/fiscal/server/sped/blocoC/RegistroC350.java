package br.com.opensig.fiscal.server.sped.blocoC;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.beanio.BeanWriter;
import org.beanio.StreamFactory;

import br.com.opensig.comercial.shared.modelo.ComEcfNota;
import br.com.opensig.comercial.shared.modelo.ComEcfNotaProduto;
import br.com.opensig.core.server.UtilServer;
import br.com.opensig.fiscal.server.sped.ARegistro;
import br.com.opensig.produto.shared.modelo.ProdTributacao;

public class RegistroC350 extends ARegistro<DadosC350, ComEcfNota> {

	private Map<String, List<ComEcfNotaProduto>> analitico = new HashMap<String, List<ComEcfNotaProduto>>();
	
	@Override
	public void executar() {
		try {
			StreamFactory factory = StreamFactory.newInstance();
			factory.load(getClass().getResourceAsStream(bean));
			BeanWriter out = factory.createWriter("EFD", escritor);
			for (ComEcfNota nota : notas) {
				bloco = getDados(nota);
				out.write(bloco);
				out.flush();
                
				// itens da venda
				RegistroC370 c370 = new RegistroC370();
				c370.setEscritor(escritor);
				for (ComEcfNotaProduto np : nota.getComEcfNotaProdutos()) {
					c370.setDados(np);
					c370.executar();
					qtdLinhas++;
					
					// agrupa os itens pelo cst+cfop+aliq
					ProdTributacao pt = np.getProdProduto().getProdTributacao();
					String cstCson = auth.getConf().get("nfe.crt").equals("1") ? pt.getProdTributacaoCson() : pt.getProdTributacaoCst();
					String chave = cstCson + pt.getProdTributacaoCfop() + pt.getProdTributacaoDentro();
                    List<ComEcfNotaProduto> lista = analitico.get(chave);
                    if (lista == null) {
                        lista = new ArrayList<ComEcfNotaProduto>();
                        lista.add(np);
                        analitico.put(chave, lista);
                    } else {
                        lista.add(np);
                    }
				}
				
				// analitico
				RegistroC390 c390 = new RegistroC390();
				c390.setEscritor(escritor);
				for (Entry<String, List<ComEcfNotaProduto>> entry : analitico.entrySet()) {
					c390.setDados(entry.getValue());
					c390.executar();
					qtdLinhas++;
				}
			}
		} catch (Exception e) {
			qtdLinhas = 0;
			UtilServer.LOG.error("Erro na geracao do Registro -> " + bean, e);
		}
	}

	@Override
	protected DadosC350 getDados(ComEcfNota dados) throws Exception {
		DadosC350 d = new DadosC350();
		d.setSer(dados.getComEcfNotaSerie());
		d.setSub(dados.getComEcfNotaSubserie());
		d.setNum_doc(dados.getComEcfNotaNumero());
		d.setDt_doc(dados.getComEcfNotaData());
		d.setCnpj_cpf(dados.getEmpCliente() != null ? dados.getEmpCliente().getEmpEntidade().getEmpEntidadeDocumento1().replaceAll("[^0-9]", "") : "");
		d.setVl_merc(dados.getComEcfNotaBruto());
		d.setVl_doc(dados.getComEcfNotaLiquido());
		d.setVl_desc(dados.getComEcfNotaDesconto());
		d.setVl_pis(dados.getComEcfNotaPis());
		d.setVl_cofins(dados.getComEcfNotaCofins());

		normalizar(d);
		qtdLinhas++;
		return d;
	}

}
