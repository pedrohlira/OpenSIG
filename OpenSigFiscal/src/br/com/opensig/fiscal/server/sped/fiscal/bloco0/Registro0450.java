package br.com.opensig.fiscal.server.sped.fiscal.bloco0;

import org.beanio.BeanWriter;
import org.beanio.StreamFactory;

import br.com.opensig.core.server.UtilServer;
import br.com.opensig.fiscal.server.sped.ARegistro;

public class Registro0450 extends ARegistro<Dados0450, String> {

	@Override
	public void executar() {
		try {
			StreamFactory factory = StreamFactory.newInstance();
			factory.load(getClass().getResourceAsStream(bean));
			BeanWriter out = factory.createWriter("EFD", escritor);
			out.write(getDados("Observacoes da NFe"));
			out.flush();
		} catch (Exception e) {
			UtilServer.LOG.error("Erro na geracao do Registro -> " + bean, e);
		}
	}

	@Override
	protected Dados0450 getDados(String texto) {
		Dados0450 d = new Dados0450();
		d.setCod_inf("1");
		d.setTxt(texto);

		normalizar(d);
		qtdLinhas++;
		return d;
	}
}
