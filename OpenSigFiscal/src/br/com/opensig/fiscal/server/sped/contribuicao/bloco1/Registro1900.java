package br.com.opensig.fiscal.server.sped.contribuicao.bloco1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.beanio.BeanWriter;
import org.beanio.StreamFactory;

import br.com.opensig.comercial.shared.modelo.ComEcfNota;
import br.com.opensig.comercial.shared.modelo.ComEcfZ;
import br.com.opensig.comercial.shared.modelo.ComEcfZTotais;
import br.com.opensig.comercial.shared.modelo.ComVenda;
import br.com.opensig.core.server.UtilServer;
import br.com.opensig.fiscal.server.sped.ARegistro;

public class Registro1900 extends ARegistro<Dados1900, Dados1900> {

	@Override
	public void executar() {
		if (vendas.size() > 0 || zs.size() > 0 || notas.size() > 0) {
			try {
				StreamFactory factory = StreamFactory.newInstance();
				factory.load(getClass().getResourceAsStream(bean));
				BeanWriter out = factory.createWriter("EFD", escritor);

				// das vendas
				Map<Integer, List<Double>> vendasNFe = new HashMap<Integer, List<Double>>();
				for (ComVenda venda : vendas) {
					// identifica o cfop da venda de acordo com a atividade e
					// destinatario
					int cfop;
					int estOri = sped.getEmpEmpresa().getEmpEntidade().getEmpEnderecos().get(0).getEmpMunicipio().getEmpEstado().getEmpEstadoIbge();
					int estDest = venda.getEmpCliente().getEmpEntidade().getEmpEnderecos().get(0).getEmpMunicipio().getEmpEstado().getEmpEstadoIbge();

					if (auth.getConf().get("sped.contribuicao.0000.ind_ativ").equals("0")) {
						if (estOri == estDest) {
							cfop = 5101;
						} else {
							cfop = 6101;
						}
					} else {
						if (estOri == estDest) {
							cfop = 5102;
						} else {
							cfop = 6102;
						}
					}

					// verifica se existe na lista, para adicionar
					if (!vendasNFe.containsKey(cfop)) {
						vendasNFe.put(cfop, new ArrayList<Double>());
					}
					vendasNFe.get(cfop).add(venda.getComVendaValorLiquido());
				}
				for (Entry<Integer, List<Double>> entry : vendasNFe.entrySet()) {
					double valor = 0.00;
					int qtd = 0;
					for (Double val : entry.getValue()) {
						valor += val;
						qtd++;
					}

					Dados1900 _1900 = new Dados1900();
					_1900.setCod_mod("55");
					_1900.setVl_tot_rec(valor);
					_1900.setQuant_doc(qtd);
					_1900.setCfop(entry.getKey());

					bloco = getDados(_1900);
					out.write(bloco);
					out.flush();
				}

				// das z
				Dados1900 _1900 = new Dados1900();
				double valor = 0.00;
				int qtd = 0;
				for (ComEcfZ z : zs) {
					valor += z.getComEcfZBruto();
					qtd++;
					// pegando as cancelads
					for (ComEcfZTotais zt : z.getComEcfZTotais()) {
						if (zt.getComEcfZTotaisCodigo().startsWith("D")) {
							valor -= zt.getComEcfZTotaisValor();
						}
					}
				}
				if (valor > 0.00) {
					_1900.setCod_mod("2D");
					_1900.setVl_tot_rec(valor);
					_1900.setQuant_doc(qtd);
					_1900.setCfop(5102);

					bloco = getDados(_1900);
					out.write(bloco);
					out.flush();
				}

				// das notas consumidor
				valor = 0.00;
				qtd = 0;
				for (ComEcfNota nota : notas) {
					valor += nota.getComEcfNotaLiquido();
					qtd++;
				}
				if (valor > 0.00) {
					_1900.setCod_mod("02");
					_1900.setVl_tot_rec(valor);
					_1900.setQuant_doc(qtd);
					_1900.setCfop(5102);

					bloco = getDados(_1900);
					out.write(bloco);
					out.flush();
				}

			} catch (Exception e) {
				qtdLinhas = 0;
				UtilServer.LOG.error("Erro na geracao do Registro -> " + bean, e);
			}
		}
	}

	@Override
	protected Dados1900 getDados(Dados1900 dados) throws Exception {
		Dados1900 d = new Dados1900();
		d.setCnpj(sped.getEmpEmpresa().getEmpEntidade().getEmpEntidadeDocumento1().replaceAll("\\D", ""));
		d.setCod_mod(dados.getCod_mod());
		d.setCod_sit("00");
		d.setVl_tot_rec(dados.getVl_tot_rec());
		d.setQuant_doc(dados.getQuant_doc());
		d.setCst_pis("01");
		d.setCst_cofins("01");
		d.setCfop(dados.getCfop());

		normalizar(d);
		qtdLinhas++;
		return d;
	}
}
