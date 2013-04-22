package br.com.opensig.fiscal.server.sped.contribuicao.blocoF;

import java.util.List;

import org.beanio.BeanWriter;
import org.beanio.StreamFactory;

import br.com.opensig.core.server.UtilServer;
import br.com.opensig.empresa.shared.modelo.EmpEmpresa;
import br.com.opensig.fiscal.server.sped.ARegistro;

public class RegistroF010 extends ARegistro<DadosF010, EmpEmpresa> {

	@Override
	public void executar() {
		if (vendas.size() > 0 || zs.size() > 0 || notas.size() > 0) {
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

						// se for por caixa
						if (auth.getConf().get("sped.contribuicao.0110.ind_reg_cum").equals("1")) {
							RegistroF500 r500 = new RegistroF500();
							r500.setDados(emp);
							r500.executar();
							qtdLinhas += r500.getQtdLinhas();

							RegistroF525 r525 = new RegistroF525();
							r525.setEmp(emp);
							r525.executar();
							qtdLinhas += r525.getQtdLinhas();
						} else { // se for por competencia
							RegistroF550 r550 = new RegistroF550();
							r550.setDados(emp);
							r550.executar();
							qtdLinhas += r550.getQtdLinhas();
						}
					}
				}
			} catch (Exception e) {
				qtdLinhas = 0;
				UtilServer.LOG.error("Erro na geracao do Registro -> " + bean, e);
			}
		}
	}

	@Override
	protected DadosF010 getDados(EmpEmpresa dados) throws Exception {
		DadosF010 d = new DadosF010();
		d.setCnpj(dados.getEmpEntidade().getEmpEntidadeDocumento1().replaceAll("\\D", ""));
		
		normalizar(d);
		qtdLinhas++;
		return d;
	}

}
