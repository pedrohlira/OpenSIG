package br.com.opensig.fiscal.server.sped.blocoC;

import org.beanio.BeanWriter;
import org.beanio.StreamFactory;

import br.com.opensig.comercial.shared.modelo.ComConsumo;
import br.com.opensig.core.server.UtilServer;
import br.com.opensig.fiscal.server.sped.ARegistro;

public class RegistroC500 extends ARegistro<DadosC500, ComConsumo> {

	@Override
	public void executar() {
		try {
			StreamFactory factory = StreamFactory.newInstance();
			factory.load(getClass().getResourceAsStream(bean));
			BeanWriter out = factory.createWriter("EFD", escritor);

			RegistroC590 r590 = new RegistroC590();
			for (ComConsumo consumo : consumos) {
				// somente os consumos de luz, gas e agua
				if (consumo.getComConsumoTipo().startsWith("06") || consumo.getComConsumoTipo().startsWith("28") || consumo.getComConsumoTipo().startsWith("29")) {
					bloco = getDados(consumo);
					out.write(bloco);
					out.flush();
					
					// analitico dos consumos
					r590.setDados(consumo);
					r590.executar();
					qtdLinhas += r590.getQtdLinhas();
				}
			}
		} catch (Exception e) {
			qtdLinhas = 0;
			UtilServer.LOG.error("Erro na geracao do Registro -> " + bean, e);
		}
	}

	@Override
	protected DadosC500 getDados(ComConsumo dados) throws Exception {
		DadosC500 d = new DadosC500();
		d.setInd_oper("0");
		d.setInd_emit("1");
		d.setCod_part(dados.getEmpFornecedor().getEmpEntidade().getEmpEntidadeId() + "");
		d.setCod_mod(dados.getComConsumoTipo().substring(0, 2));
		d.setCod_sit("00");
		d.setSer("");
		d.setSub(0);
		// definicao do luz e gas
		if (d.getCod_mod().equals("06") || d.getCod_mod().equals("28")) {
			d.setCod_cons("01");
		} else {
			if (dados.getComConsumoValor() <= 50.00) {
				d.setCod_cons("20");
			} else if (dados.getComConsumoValor() <= 100.00) {
				d.setCod_cons("21");
			} else if (dados.getComConsumoValor() <= 200.00) {
				d.setCod_cons("22");
			} else if (dados.getComConsumoValor() <= 300.00) {
				d.setCod_cons("23");
			} else if (dados.getComConsumoValor() <= 400.00) {
				d.setCod_cons("24");
			} else if (dados.getComConsumoValor() <= 500.00) {
				d.setCod_cons("25");
			} else if (dados.getComConsumoValor() <= 1000.00) {
				d.setCod_cons("26");
			} else {
				d.setCod_cons("27");
			}
		}
		d.setNum_doc(dados.getComConsumoDocumento());
		d.setDt_doc(dados.getComConsumoData());
		d.setDt_e_s(dados.getComConsumoData());
		d.setVl_doc(dados.getComConsumoValor());
		d.setVl_desc(0.00);
		d.setVl_forn(dados.getComConsumoValor());
		d.setVl_serv_nt(0.00);
		d.setVl_terc(0.00);
		d.setVl_da(0.00);
		d.setVl_bc_icms(dados.getComConsumoBase());
		d.setVl_icms(dados.getComConsumoIcms());
		d.setVl_bc_icms_st(0.00);
		d.setVl_icms_st(0.00);
		d.setCod_inf("");
		d.setVl_pis(dados.getComConsumoValor() * pis / 100);
		d.setVl_cofins(dados.getComConsumoValor() * cofins / 100);
		d.setTp_ligacao("");
		d.setCod_grupo_tensao("");

		normalizar(d);
		qtdLinhas++;
		return d;
	}
}
