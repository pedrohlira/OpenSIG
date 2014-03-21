package br.com.opensig.empresa.server.acao;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.opensig.core.client.servico.CoreService;
import br.com.opensig.core.server.UtilServer;
import br.com.opensig.core.server.exportar.AExportacao;
import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.core.shared.modelo.ExpListagem;
import br.com.opensig.core.shared.modelo.ExpRegistro;
import br.com.opensig.core.shared.modelo.sistema.SisExpImp;
import br.com.opensig.empresa.shared.modelo.EmpEndereco;
import br.com.opensig.empresa.shared.modelo.EmpEntidade;
import br.com.opensig.empresa.shared.modelo.Etiqueta;

public class ExportarEtiqueta<E extends Dados> extends AExportacao<E> {

	@Override
	public String getArquivo(CoreService<E> service, SisExpImp modo, ExpListagem<E> exp, String[][] enderecos, String[][] contatos) {
		this.expLista = exp;

		// seleciona os dados
		try {
			lista = service.selecionar(exp.getClasse(), modo.getInicio(), modo.getLimite(), exp.getFiltro(), false);
			return getDados(getEtiquetas(modo));
		} catch (Exception e) {
			UtilServer.LOG.error("Erro na exportacao -> ", e);
			return null;
		}
	}

	@Override
	public String getArquivo(CoreService<E> service, SisExpImp modo, ExpRegistro<E> exp, String[][] enderecos, String[][] contatos) {
		this.expReg = exp;

		// seleciona os dados
		try {
			this.lista = service.selecionar(exp.getClasse(), 0, 1, exp.getFiltro(), false);
			return getDados(getEtiquetas(modo));
		} catch (Exception e) {
			UtilServer.LOG.error("Erro na exportacao -> ", e);
			return null;
		}
	}

	private String getDados(List<Etiqueta> etiquetas) throws Exception {
		String path = UtilServer.PATH_EMPRESA + "tmp/" + new Date().getTime() + ".csv";

		try {
			bw = new BufferedWriter(new FileWriter(path));
			for (Etiqueta eti : etiquetas) {
				bw.write("\"" + eti.getRazao() + "\",");
				bw.write("\"" + eti.getEndereco() + "\",");
				bw.write("\"" + eti.getEndereco() + "\",");
				bw.write("\"" + eti.getBairro() + "\",");
				bw.write("\"" + eti.getCidade() + "\",");
				bw.write("\"" + eti.getUf() + "\",");
				bw.write("\"" + eti.getCep() + "\"\n");
				bw.flush();
			}
			bw.close();
			return path;
		} catch (IOException ex) {
			return null;
		}
	}

	private List<Etiqueta> getEtiquetas(SisExpImp modo) throws Exception {
		List<Etiqueta> etiquetas = new ArrayList<Etiqueta>();

		// etiqueta padrao de cabecalho
		Etiqueta cabecalho = new Etiqueta();
		cabecalho.setRazao("RAZAO");
		cabecalho.setEndereco("ENDERECO");
		cabecalho.setBairro("BAIRRO");
		cabecalho.setCidade("CIDADE");
		cabecalho.setUf("UF");
		cabecalho.setCep("CEP");
		etiquetas.add(cabecalho);

		for (E dados : lista.getLista()) {
			EmpEntidade ent = (EmpEntidade) dados.getClass().getMethod("getEmpEntidade", null).invoke(dados, null);
			EmpEndereco ende = ent.getEmpEnderecos().get(0);

			// etiqueta padrao da empresa
			Etiqueta etiPadrao = new Etiqueta();
			etiPadrao.setRazao(ent.getEmpEntidadeNome1());
			String logr = ende.getEmpEnderecoLogradouro();
			if (ende.getEmpEnderecoNumero() != null) {
				logr += ", " + ende.getEmpEnderecoNumero();
			}
			if (ende.getEmpEnderecoComplemento() != null) {
				logr += " " + ende.getEmpEnderecoComplemento();
			}
			etiPadrao.setEndereco(logr);
			etiPadrao.setBairro(ende.getEmpEnderecoBairro());
			etiPadrao.setCidade(ende.getEmpMunicipio().getEmpMunicipioDescricao());
			etiPadrao.setUf(ende.getEmpMunicipio().getEmpEstado().getEmpEstadoSigla());
			etiPadrao.setCep(ende.getEmpEnderecoCep());
			etiquetas.add(etiPadrao);
		}

		return etiquetas;
	}
}
