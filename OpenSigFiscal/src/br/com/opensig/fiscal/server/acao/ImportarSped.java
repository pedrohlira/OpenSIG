package br.com.opensig.fiscal.server.acao;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.EJuncao;
import br.com.opensig.core.client.controlador.filtro.FiltroNumero;
import br.com.opensig.core.client.controlador.filtro.FiltroObjeto;
import br.com.opensig.core.client.controlador.filtro.FiltroTexto;
import br.com.opensig.core.client.controlador.filtro.GrupoFiltro;
import br.com.opensig.core.client.controlador.filtro.IFiltro;
import br.com.opensig.core.client.controlador.parametro.GrupoParametro;
import br.com.opensig.core.client.controlador.parametro.ParametroBinario;
import br.com.opensig.core.client.controlador.parametro.ParametroData;
import br.com.opensig.core.client.controlador.parametro.ParametroTexto;
import br.com.opensig.core.client.servico.OpenSigException;
import br.com.opensig.core.server.UtilServer;
import br.com.opensig.core.server.importar.IImportacao;
import br.com.opensig.core.shared.modelo.Autenticacao;
import br.com.opensig.core.shared.modelo.EComando;
import br.com.opensig.core.shared.modelo.Sql;
import br.com.opensig.core.shared.modelo.sistema.SisExpImp;
import br.com.opensig.empresa.shared.modelo.EmpEmpresa;
import br.com.opensig.fiscal.server.FiscalServiceImpl;
import br.com.opensig.fiscal.shared.modelo.FisSpedFiscal;

public class ImportarSped implements IImportacao<FisSpedFiscal> {

	@Override
	public Map<String, List<FisSpedFiscal>> setArquivo(Autenticacao auth, Map<String, byte[]> arquivos, SisExpImp modo) throws OpenSigException {
		String nome = "";
		byte[] obj = null;

		try {

			// pega o nome e arquivo
			for (Entry<String, byte[]> entry : arquivos.entrySet()) {
				if (entry.getValue() != null) {
					obj = entry.getValue();
				} else {
					nome = entry.getKey().toUpperCase();
				}
			}

			// filtros
			String tipo = nome.substring(0, nome.length() - 6);
			String ano = nome.substring(nome.length() - 6, nome.length() - 2);
			String mes = nome.substring(nome.length() - 2);
			FiltroObjeto fo = new FiltroObjeto("empEmpresa", ECompara.IGUAL, new EmpEmpresa(Integer.valueOf(auth.getEmpresa()[0])));
			FiltroTexto ft = new FiltroTexto("fisSpedFiscalTipo", ECompara.IGUAL, tipo);
			FiltroNumero fn1 = new FiltroNumero("fisSpedFiscalAno", ECompara.IGUAL, ano);
			FiltroNumero fn2 = new FiltroNumero("fisSpedFiscalMes", ECompara.IGUAL, mes);
			GrupoFiltro gf = new GrupoFiltro(EJuncao.E, new IFiltro[] { fo, ft, fn1, fn2 });
			// parametros
			GrupoParametro gp = new GrupoParametro();
			ParametroData pd = new ParametroData("fisSpedFiscalData", new Date());
			gp.add(pd);
			ParametroBinario pb = new ParametroBinario("fisSpedFiscalAtivo", 1);
			gp.add(pb);
			if (modo.getSisExpImpExtensoes().equalsIgnoreCase("REC")) {
				ParametroTexto pt = new ParametroTexto("fisSpedFiscalProtocolo", "RECEBIDO");
				gp.add(pt);
			}

			// atualiza o registro
			Sql sql = new Sql(new FisSpedFiscal(), EComando.ATUALIZAR, gf, gp);
			FiscalServiceImpl<FisSpedFiscal> servico = new FiscalServiceImpl<FisSpedFiscal>(auth);
			Integer[] resp = servico.executar(new Sql[] { sql });

			// caso exista o registro
			if (resp[0] == 1) {
				// procura pelo arquivo
				String cnpj = auth.getEmpresa()[5].replaceAll("\\D", "");
				String arquivo = UtilServer.PATH_EMPRESA + cnpj + "/sped/" + nome + "." + modo.getSisExpImpExtensoes();
				File arq = new File(arquivo);
				if (arq.exists()) {
					arq.delete();
				}

				// salva o arquivo
				FileOutputStream fos = new FileOutputStream(arq);
				fos.write(obj);
				fos.close();
			} else {

			}
		} catch (Exception e) {
			throw new OpenSigException(e.getMessage(), e);
		}

		return null;
	}
}
