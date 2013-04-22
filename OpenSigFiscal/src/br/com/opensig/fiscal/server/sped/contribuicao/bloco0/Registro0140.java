package br.com.opensig.fiscal.server.sped.contribuicao.bloco0;

import java.util.List;

import org.beanio.BeanWriter;
import org.beanio.StreamFactory;

import br.com.opensig.core.server.UtilServer;
import br.com.opensig.empresa.shared.modelo.EmpEmpresa;
import br.com.opensig.empresa.shared.modelo.EmpMunicipio;
import br.com.opensig.fiscal.server.sped.ARegistro;

public class Registro0140 extends ARegistro<Dados0140, EmpEmpresa> {

	@Override
	public void executar() {
		try {
			StreamFactory factory = StreamFactory.newInstance();
			factory.load(getClass().getResourceAsStream(bean));
			BeanWriter out = factory.createWriter("EFD", escritor);

			String cnpjBase = sped.getEmpEmpresa().getEmpEntidade().getEmpEntidadeDocumento1().substring(0, 10);
			List<EmpEmpresa> empresas = service.selecionar(new EmpEmpresa(), 0, 0, null, false).getLista();
			for (EmpEmpresa emp : empresas) {
				String cnpj = emp.getEmpEntidade().getEmpEntidadeDocumento1();
				if (cnpj.startsWith(cnpjBase)) {
					bloco = getDados(emp);
					out.write(bloco);
					out.flush();
				}
			}
		} catch (Exception e) {
			qtdLinhas = 0;
			UtilServer.LOG.error("Erro na geracao do Registro -> " + bean, e);
		}
	}

	@Override
	protected Dados0140 getDados(EmpEmpresa dados) throws Exception {
		Dados0140 d = new Dados0140();
		d.setCod_est(dados.getEmpEmpresaId() + "");
		d.setNome(dados.getEmpEntidade().getEmpEntidadeNome1());
		d.setNome(dados.getEmpEntidade().getEmpEntidadeNome1());
		d.setCnpj(dados.getEmpEntidade().getEmpEntidadeDocumento1().replaceAll("\\D", ""));
		d.setIe(dados.getEmpEntidade().getEmpEntidadeDocumento2().replaceAll("\\D", ""));
		d.setIm(dados.getEmpEntidade().getEmpEntidadeDocumento3().replaceAll("\\D", ""));

		EmpMunicipio mun = dados.getEmpEntidade().getEmpEnderecos().get(0).getEmpMunicipio();
		d.setUf(mun.getEmpEstado().getEmpEstadoSigla());
		d.setCod_mun(mun.getEmpMunicipioIbge());
		d.setSuframa(auth.getConf().get("sped.0000.suframa"));

		normalizar(d);
		qtdLinhas++;
		return d;
	}

}
