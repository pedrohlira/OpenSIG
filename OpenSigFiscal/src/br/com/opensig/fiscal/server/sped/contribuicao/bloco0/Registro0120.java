package br.com.opensig.fiscal.server.sped.contribuicao.bloco0;

import java.util.List;

import org.beanio.BeanWriter;
import org.beanio.StreamFactory;

import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.EJuncao;
import br.com.opensig.core.client.controlador.filtro.FiltroNumero;
import br.com.opensig.core.client.controlador.filtro.GrupoFiltro;
import br.com.opensig.core.client.controlador.filtro.IFiltro;
import br.com.opensig.core.server.UtilServer;
import br.com.opensig.fiscal.server.sped.ARegistro;
import br.com.opensig.fiscal.shared.modelo.FisSped;

public class Registro0120 extends ARegistro<Dados0120, String> {

	@Override
	public void executar() {
		try {
			if (sped.getFisSpedMes() == 12) {
				StreamFactory factory = StreamFactory.newInstance();
				factory.load(getClass().getResourceAsStream(bean));
				BeanWriter out = factory.createWriter("EFD", escritor);

				// filtro dos meses que nao tiveram movimentos
				FiltroNumero fn1 = new FiltroNumero("fisSpedAno", ECompara.IGUAL, sped.getFisSpedAno());
				FiltroNumero fn2 = new FiltroNumero("fisSpedMes", ECompara.DIFERENTE, 12);
				GrupoFiltro gf = new GrupoFiltro(EJuncao.E, new IFiltro[] { fn1, fn2 });
				List<FisSped> speds = service.selecionar(new FisSped(), 0, 0, gf, false).getLista();

				for (int mes = 1; mes < 12; mes++) {
					boolean existe = false;
					for (FisSped sped : speds) {
						if (sped.getFisSpedMes() == mes) {
							existe = true;
							break;
						}
					}

					if (!existe) {
						bloco = getDados(UtilServer.formataTexto(mes + "" + sped.getFisSpedAno(), "0", 6, false));
						out.write(bloco);
						out.flush();
					}
				}
			}
		} catch (Exception e) {
			qtdLinhas = 0;
			UtilServer.LOG.error("Erro na geracao do Registro -> " + bean, e);
		}
	}

	@Override
	protected Dados0120 getDados(String dados) throws Exception {
		Dados0120 d = new Dados0120();
		d.setMes_dispensa(dados);

		normalizar(d);
		qtdLinhas++;
		return d;
	}
}
