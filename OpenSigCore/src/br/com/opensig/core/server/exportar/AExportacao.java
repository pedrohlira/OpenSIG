package br.com.opensig.core.server.exportar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import br.com.opensig.core.server.UtilServer;
import br.com.opensig.core.shared.modelo.Autenticacao;
import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.core.shared.modelo.ExpListagem;
import br.com.opensig.core.shared.modelo.ExpRegistro;
import br.com.opensig.core.shared.modelo.Lista;
import br.com.opensig.core.shared.modelo.sistema.SisExpImp;

/**
 * Classe abstrata que generaliza a forma como o sistema trata as exportacoes.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 */
public abstract class AExportacao<E extends Dados> implements IExportacao<E> {

	/**
	 * Autenticacao do usuario.
	 */
	protected Autenticacao auth;
	/**
	 * Dados recuperados do banco.
	 */
	protected Lista<E> registro;
	/**
	 * Dados recuperados do banco.
	 */
	protected Lista<E> lista;
	/**
	 * Modo de exportacao
	 */
	protected SisExpImp modo;
	/**
	 * valors agrupados para usar como sumarios.
	 */
	protected double[] agrupados;
	/**
	 * tipo de exportacao em listagem.
	 */
	protected ExpListagem<E> expLista;
	/**
	 * tipo de exportacao em registro.
	 */
	protected ExpRegistro<E> expReg;
	/**
	 * formato de apresentacao
	 */
	protected String formato;
 
	@Override
	public void setAuth(Autenticacao auth) {
		this.auth = auth;
	}
	
	/**
	 * Metodo que identifica o tipo de valor e formata de acordo com a
	 * localizacao.
	 * 
	 * @param valor
	 *            o texto cuja informacao sera avaliada.
	 * @return o texto formato de acordo com o tipo.
	 */
	public String getValor(String valor) {
		String retorno = valor;

		if (valor != null) {
			try {
				Date data = new SimpleDateFormat("MM/dd/yyyy", Locale.US).parse(valor);
				retorno = UtilServer.formataData(data, DateFormat.MEDIUM);
				if (valor.indexOf(":") > 0) {
					data = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss", Locale.US).parse(valor);
					retorno += " " + UtilServer.formataHora(data, DateFormat.MEDIUM);
				}
			} catch (Exception e1) {
				try {
					if (valor.contains(".")) {
						retorno = UtilServer.formataNumero(valor, 1, 2, true);
					} else {
						retorno = Long.parseLong(valor) + "";
					}
				} catch (Exception e4) {
					if (valor.equalsIgnoreCase("true")) {
						retorno = auth.getConf().get("txtSim");
					} else if (valor.equalsIgnoreCase("false")) {
						retorno = auth.getConf().get("txtNao");
					}
				}
			}
		}

		return retorno == null || retorno.equals("") || retorno.equals("null") ? " " : retorno;
	}
}
