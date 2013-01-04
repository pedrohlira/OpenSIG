package br.com.opensig.fiscal.server.sped.blocoD;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.beanio.BeanWriter;
import org.beanio.StreamFactory;

import br.com.opensig.comercial.shared.modelo.ComConsumo;
import br.com.opensig.core.server.UtilServer;
import br.com.opensig.fiscal.server.sped.ARegistro;

public class RegistroD500 extends ARegistro<DadosD500, ComConsumo> {

	private Map<String, List<ComConsumo>> analitico = new HashMap<String, List<ComConsumo>>();

	@Override
	public void executar() {
		try {
			StreamFactory factory = StreamFactory.newInstance();
			factory.load(getClass().getResourceAsStream(bean));
			BeanWriter out = factory.createWriter("EFD", escritor);

			RegistroD590 r590 = new RegistroD590();
			r590.setEscritor(escritor);
			for (ComConsumo consumo : consumos) {
				// somente os consumos de comunicacao e telecomunicacao
				if (consumo.getComConsumoTipo().startsWith("21") || consumo.getComConsumoTipo().startsWith("22")) {
					bloco = getDados(consumo);
					out.write(bloco);
					out.flush();
					setAnalitico(consumo);
				}
			}

			// analitico dos consumos
			getAnalitico();
		} catch (Exception e) {
			qtdLinhas = 0;
			UtilServer.LOG.error("Erro na geracao do Registro -> " + bean, e);
		}
	}

	@Override
	protected DadosD500 getDados(ComConsumo dados) throws Exception {
		DadosD500 d = new DadosD500();
		d.setInd_oper("0");
		d.setInd_emit("1");
		d.setCod_part(dados.getEmpFornecedor().getEmpEntidade().getEmpEntidadeId() + "");
		d.setCod_mod(dados.getComConsumoTipo().substring(0, 2));
		d.setCod_sit("00");
		d.setSer("");
		d.setSub("");
		d.setNum_doc(dados.getComConsumoDocumento());
		d.setDt_doc(dados.getComConsumoData());
		d.setDt_a_p(dados.getComConsumoData());
		d.setVl_doc(dados.getComConsumoValor());
		d.setVl_desc(0.00);
		d.setVl_serv(dados.getComConsumoValor());
		d.setVl_serv_nt(0.00);
		d.setVl_terc(0.00);
		d.setVl_da(0.00);
		d.setVl_bc_icms(dados.getComConsumoBase());
		d.setVl_icms(dados.getComConsumoIcms());
		d.setCod_inf("");
		d.setVl_pis(dados.getComConsumoValor() * pis / 100);
		d.setVl_cofins(dados.getComConsumoValor() * cofins / 100);
		d.setCod_cta("");
		d.setTp_assinante(1);
		
		normalizar(d);
		qtdLinhas++;
		return d;
	}

	private void setAnalitico(ComConsumo d) {
		String chave = "000" + d.getComConsumoCfop() + d.getComConsumoAliquota();
		List<ComConsumo> lista = analitico.get(chave);
		if (lista == null) {
			lista = new ArrayList<ComConsumo>();
			lista.add(d);
			analitico.put(chave, lista);
		} else {
			lista.add(d);
		}
	}

	private void getAnalitico() {
		if (!analitico.isEmpty()) {
			RegistroD590 r590 = new RegistroD590();
			r590.setEscritor(escritor);
			r590.setAuth(auth);
			for (Entry<String, List<ComConsumo>> entry : analitico.entrySet()) {
				r590.setDados(entry.getValue());
				r590.executar();
				qtdLinhas += r590.getQtdLinhas();
			}
		}
		analitico.clear();
	}
}
