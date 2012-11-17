package br.com.opensig.fiscal.server.acao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.w3c.dom.Document;

import br.com.opensig.core.client.servico.OpenSigException;
import br.com.opensig.core.server.UtilServer;
import br.com.opensig.core.server.importar.IImportacao;
import br.com.opensig.core.shared.modelo.Autenticacao;
import br.com.opensig.core.shared.modelo.sistema.SisExpImp;
import br.com.opensig.fiscal.server.NFe;
import br.com.opensig.fiscal.shared.modelo.ENotaStatus;
import br.com.opensig.fiscal.shared.modelo.FisNotaEntrada;
import br.com.opensig.fiscal.shared.modelo.FisNotaStatus;

public class RecuperarEntrada implements IImportacao<FisNotaEntrada> {

	@Override
	public Map<String, List<FisNotaEntrada>> setArquivo(Autenticacao auth, Map<String, byte[]> arquivos, SisExpImp modo) throws OpenSigException {
		List<FisNotaEntrada> lista = new ArrayList<FisNotaEntrada>();

		for (Entry<String, byte[]> arquivo : arquivos.entrySet()) {
			try {
				// gera um objeto DOM do xml
				String xml = new String(arquivo.getValue());
				Document doc = UtilServer.getXml(xml);
				String cnpj = UtilServer.normaliza(auth.getEmpresa()[5]);
				ENotaStatus status = NFe.validarEntrada(doc, cnpj);
				
				// valida o xml com o xsd
				if (status == ENotaStatus.AUTORIZANDO || status == ENotaStatus.CANCELANDO || status == ENotaStatus.INUTILIZANDO) {
					// caso o xml nao esteja assinado
					if (doc.getElementsByTagName("Signature").item(0) == null) {
						// assina
						xml = NFe.assinarXML(doc, status, auth);
					}

					String xsd = UtilServer.getRealPath(auth.getConf().get("nfe.xsd_" + status.toString().toLowerCase()));
					NFe.validarXML(xml, xsd);
				}
				
				FisNotaStatus nfStatus = new FisNotaStatus(status);
				new SalvarEntrada(null, xml, nfStatus, auth).execute();
			} catch (Exception ex) {
				FisNotaEntrada entrada = new FisNotaEntrada();
				entrada.setFisNotaEntradaErro(ex.getMessage());
				lista.add(entrada);
			}
		}
		
		Map<String, List<FisNotaEntrada>> retorno = new HashMap<String, List<FisNotaEntrada>>();
		retorno.put("erro", lista);
		return retorno;
	}
}
