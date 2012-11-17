package br.com.opensig.fiscal.server.sped.bloco9;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.beanio.BeanWriter;
import org.beanio.StreamFactory;

import br.com.opensig.core.server.UtilServer;
import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.fiscal.server.sped.ARegistro;

public class Registro9900 extends ARegistro<Dados9900, Dados> {

	private Map<String, Integer> regs = new LinkedHashMap<String, Integer>();

	@Override
	public void executar() {
		try {
			// lendo o arquivo
			BufferedReader br = new BufferedReader(new FileReader(leitor));
			String linha, chave;
			while ((linha = br.readLine()) != null) {
				chave = linha.substring(1, 5);
				Integer qtd = regs.get(chave);
				qtd = qtd == null ? 1 : qtd + 1;
				regs.put(chave, qtd);
			}
			// inputando os ultimos
			regs.put("9900", regs.size() + 3);
			regs.put("9990", 1);
			regs.put("9999", 1);

			StreamFactory factory = StreamFactory.newInstance();
			factory.load(getClass().getResourceAsStream(bean));
			BeanWriter out = factory.createWriter("EFD", escritor);
			for (Entry<String, Integer> entry : regs.entrySet()) {
				bloco = getDados(entry);
				out.write(bloco);
				out.flush();
			}
		} catch (Exception e) {
			qtdLinhas = 0;
			UtilServer.LOG.error("Erro na geracao do Registro -> " + bean, e);
		}
	}

	@Override
	protected Dados9900 getDados(Dados dados) throws Exception {
		return null;
	}

	protected Dados9900 getDados(Entry<String, Integer> dados) throws Exception {
		Dados9900 d = new Dados9900();
		d.setReg_bcl(dados.getKey());
		d.setQtd_reg_bcl(dados.getValue());

		normalizar(d);
		qtdLinhas++;
		return d;
	}

}
