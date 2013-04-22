package br.com.opensig.fiscal.server.sped.fiscal.blocoC;

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
		// executa para perfil igual a A
		if (auth.getConf().get("sped.fiscal.0000.ind_perfil").equals("A")) {
			try {
				StreamFactory factory = StreamFactory.newInstance();
				factory.load(getClass().getResourceAsStream(bean));
				BeanWriter out = factory.createWriter("EFD", escritor);

				RegistroC370 c370 = new RegistroC370();
				for (ComEcfNota nota : notas) {
					if (nota.getComEcfNotaCancelada() == false) {
						bloco = getDados(nota);
						out.write(bloco);
						out.flush();

						// itens da venda
						for (ComEcfNotaProduto np : nota.getComEcfNotaProdutos()) {
							c370.setDados(np);
							c370.executar();
							qtdLinhas += c370.getQtdLinhas();
							setAnalitico(np);
						}

						// analitico
						getAnalitico();
					}
				}
			} catch (Exception e) {
				qtdLinhas = 0;
				UtilServer.LOG.error("Erro na geracao do Registro -> " + bean, e);
			}
		}
	}

	@Override
	protected DadosC350 getDados(ComEcfNota dados) throws Exception {
		DadosC350 d = new DadosC350();
		d.setSer(dados.getComEcfNotaSerie());
		d.setSub(dados.getComEcfNotaSubserie());
		d.setNum_doc(dados.getComEcfNotaNumero());
		d.setDt_doc(dados.getComEcfNotaData());
		d.setCnpj_cpf(dados.getEmpCliente() != null ? dados.getEmpCliente().getEmpEntidade().getEmpEntidadeDocumento1().replaceAll("\\D", "") : "");
		d.setVl_merc(dados.getComEcfNotaBruto());
		d.setVl_doc(dados.getComEcfNotaLiquido());
		d.setVl_desc(dados.getComEcfNotaDesconto());
		d.setVl_pis(dados.getComEcfNotaPis());
		d.setVl_cofins(dados.getComEcfNotaCofins());

		normalizar(d);
		qtdLinhas++;
		return d;
	}

	private void setAnalitico(ComEcfNotaProduto d) {
		ProdTributacao pt = d.getProdProduto().getProdTributacao();
		String cstCson = auth.getConf().get("nfe.crt").equals("1") ? pt.getProdTributacaoCson() : pt.getProdTributacaoCst();
		String chave = cstCson + pt.getProdTributacaoCfop() + pt.getProdTributacaoDentro();
		List<ComEcfNotaProduto> lista = analitico.get(chave);
		if (lista == null) {
			lista = new ArrayList<ComEcfNotaProduto>();
			lista.add(d);
			analitico.put(chave, lista);
		} else {
			lista.add(d);
		}
	}

	private void getAnalitico() {
		if (!analitico.isEmpty()) {
			RegistroC390 r390 = new RegistroC390();
			for (Entry<String, List<ComEcfNotaProduto>> entry : analitico.entrySet()) {
				r390.setDados(entry.getValue());
				r390.executar();
				qtdLinhas += r390.getQtdLinhas();
			}
		}
		analitico.clear();
	}

}
