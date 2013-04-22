package br.com.opensig.fiscal.server.sped.contribuicao.blocoF;

import org.beanio.BeanWriter;
import org.beanio.StreamFactory;

import br.com.opensig.comercial.shared.modelo.ComEcfNota;
import br.com.opensig.comercial.shared.modelo.ComEcfZ;
import br.com.opensig.comercial.shared.modelo.ComEcfZTotais;
import br.com.opensig.comercial.shared.modelo.ComVenda;
import br.com.opensig.core.server.UtilServer;
import br.com.opensig.empresa.shared.modelo.EmpEmpresa;
import br.com.opensig.fiscal.server.sped.ARegistro;

public class RegistroF525 extends ARegistro<DadosF525, DadosF525> {

	private EmpEmpresa emp;

	@Override
	public void executar() {
		try {
			StreamFactory factory = StreamFactory.newInstance();
			factory.load(getClass().getResourceAsStream(bean));
			BeanWriter out = factory.createWriter("EFD", escritor);

			// das vendas
			double valor = 0.00;
			DadosF525 f525 = new DadosF525();
			for (ComVenda venda : vendas) {
				if (venda.getEmpEmpresa().getEmpEmpresaId() == emp.getEmpEmpresaId()) {
					valor += venda.getComVendaValorLiquido();
				}
			}
			if (valor > 0.00) {
				f525.setVl_rec(valor);
				f525.setVl_rec_det(valor);
				f525.setInfo_compl("Valores referente as NFe.");

				bloco = getDados(f525);
				out.write(bloco);
				out.flush();
			}

			// das z
			valor = 0.00;
			for (ComEcfZ z : zs) {
				if (z.getComEcf().getEmpEmpresa().getEmpEmpresaId() == emp.getEmpEmpresaId()) {
					valor += z.getComEcfZBruto();
					// pegando as cancelads
					for (ComEcfZTotais zt : z.getComEcfZTotais()) {
						if (zt.getComEcfZTotaisCodigo().startsWith("D")) {
							valor -= zt.getComEcfZTotaisValor();
						}
					}
				}
			}
			if (valor > 0.00) {
				f525.setVl_rec(valor);
				f525.setVl_rec_det(valor);
				f525.setInfo_compl("Valores referente as ECF.");

				bloco = getDados(f525);
				out.write(bloco);
				out.flush();
			}

			// das notas consumidor
			valor = 0.00;
			for (ComEcfNota nota : notas) {
				if (nota.getEmpEmpresa().getEmpEmpresaId() == emp.getEmpEmpresaId()) {
					valor += nota.getComEcfNotaLiquido();
				}
			}
			if (valor > 0.00) {
				f525.setVl_rec(valor);
				f525.setVl_rec_det(valor);
				f525.setInfo_compl("Valores referente as NFC.");

				bloco = getDados(f525);
				out.write(bloco);
				out.flush();
			}

		} catch (Exception e) {
			qtdLinhas = 0;
			UtilServer.LOG.error("Erro na geracao do Registro -> " + bean, e);
		}
	}

	@Override
	protected DadosF525 getDados(DadosF525 dados) throws Exception {
		DadosF525 d = new DadosF525();
		d.setVl_rec(dados.getVl_rec());
		d.setInd_rec("99");
		d.setVl_rec_det(dados.getVl_rec_det());
		d.setCst_pis("01");
		d.setCst_cofins("01");
		d.setInfo_compl(dados.getInfo_compl());

		normalizar(d);
		qtdLinhas++;
		return d;
	}

	public EmpEmpresa getEmp() {
		return emp;
	}

	public void setEmp(EmpEmpresa emp) {
		this.emp = emp;
	}
}
