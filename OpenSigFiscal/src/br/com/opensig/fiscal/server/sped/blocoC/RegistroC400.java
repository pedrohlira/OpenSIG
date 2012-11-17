package br.com.opensig.fiscal.server.sped.blocoC;

import java.util.ArrayList;
import java.util.List;

import org.beanio.BeanWriter;
import org.beanio.StreamFactory;

import br.com.opensig.comercial.shared.modelo.ComEcf;
import br.com.opensig.comercial.shared.modelo.ComEcfZ;
import br.com.opensig.core.server.UtilServer;
import br.com.opensig.fiscal.server.sped.ARegistro;

public class RegistroC400 extends ARegistro<DadosC400, ComEcf> {

	@Override
	public void executar() {
		List<Integer> ecfId = new ArrayList<Integer>();

		try {
			StreamFactory factory = StreamFactory.newInstance();
			factory.load(getClass().getResourceAsStream(bean));
			BeanWriter out = factory.createWriter("EFD", escritor);
			
			// dados da Z
			RegistroC405 r405 = new RegistroC405();
			r405.setEscritor(escritor);
			for (ComEcfZ z : zs) {
				if (!ecfId.contains(z.getComEcf().getComEcfId())) {
					ecfId.add(z.getComEcf().getComEcfId());
					bloco = getDados(z.getComEcf());
					out.write(bloco);
					out.flush();
				}
				if (z.getComEcfZBruto() > 0.00) {
					r405.setDados(z);
					r405.executar();
					qtdLinhas += r405.getQtdLinhas();
				}
			}
		} catch (Exception e) {
			qtdLinhas = 0;
			UtilServer.LOG.error("Erro na geracao do Registro -> " + bean, e);
		}
	}

	@Override
	protected DadosC400 getDados(ComEcf dados) throws Exception {
		DadosC400 d = new DadosC400();
		d.setCod_mod(dados.getComEcfCodigo());
		d.setEcf_mod(dados.getComEcfModelo());
		d.setEcf_fab(dados.getComEcfSerie());
		d.setEcf_cx(dados.getComEcfCaixa());

		normalizar(d);
		qtdLinhas++;
		return d;
	}
}
