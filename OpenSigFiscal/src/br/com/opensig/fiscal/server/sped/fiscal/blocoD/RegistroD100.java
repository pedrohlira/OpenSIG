package br.com.opensig.fiscal.server.sped.fiscal.blocoD;

import org.beanio.BeanWriter;
import org.beanio.StreamFactory;

import br.com.opensig.comercial.shared.modelo.ComFrete;
import br.com.opensig.core.server.UtilServer;
import br.com.opensig.fiscal.server.sped.ARegistro;

public class RegistroD100 extends ARegistro<DadosD100, ComFrete> {

	@Override
	public void executar() {
		try {
			StreamFactory factory = StreamFactory.newInstance();
			factory.load(getClass().getResourceAsStream(bean));
			BeanWriter out = factory.createWriter("EFD", escritor);

			RegistroD190 r190 = new RegistroD190();
			for (ComFrete frete : fretes) {
				bloco = getDados(frete);
				out.write(bloco);
				out.flush();

				// informacoes da nota
				r190.setDados(frete);
				r190.executar();
				qtdLinhas += r190.getQtdLinhas();
			}
		} catch (Exception e) {
			qtdLinhas = 0;
			UtilServer.LOG.error("Erro na geracao do Registro -> " + bean, e);
		}
	}

	@Override
	protected DadosD100 getDados(ComFrete dados) throws Exception {
		DadosD100 d = new DadosD100();
		d.setInd_oper("0");
		d.setInd_emit("1");
		d.setCod_part(dados.getEmpTransportadora().getEmpEntidade().getEmpEntidadeId() + "");
		d.setCod_mod("08");
		d.setCod_sit("00");
		d.setSer(dados.getComFreteSerie() + "");
		d.setSub("");
		d.setNum_doc(dados.getComFreteCtrc());
		d.setChv_cte("");
		d.setDt_doc(dados.getComFreteEmissao());
		d.setDt_a_p(dados.getComFreteRecebimento());
		d.setTp_cte("");
		d.setChv_cte_ref("");
		d.setVl_doc(dados.getComFreteValor());
		d.setVl_desc(0.00);
		d.setInd_frt(dados.getComFretePaga() ? "2" : "1");
		d.setVl_serv(dados.getComFreteValor());
		d.setVl_bc_icms(dados.getComFreteBase());
		d.setVl_icms(dados.getComFreteIcms());
		d.setVl_nt(0.00);
		d.setCod_inf("");
		d.setCod_cta("");

		normalizar(d);
		qtdLinhas++;
		return d;
	}
}
