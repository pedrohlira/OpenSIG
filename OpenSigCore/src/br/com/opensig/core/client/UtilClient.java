package br.com.opensig.core.client;

import java.util.Date;
import java.util.Map;

import br.com.opensig.core.client.controlador.comando.IComando;
import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.EJuncao;
import br.com.opensig.core.client.controlador.filtro.FiltroBinario;
import br.com.opensig.core.client.controlador.filtro.FiltroTexto;
import br.com.opensig.core.client.controlador.filtro.GrupoFiltro;
import br.com.opensig.core.client.controlador.filtro.IFiltro;
import br.com.opensig.core.client.visao.ComboEntidade;
import br.com.opensig.core.client.visao.Ponte;
import br.com.opensig.core.shared.modelo.sistema.SisAcao;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.core.shared.modelo.sistema.SisModulo;

import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.xml.client.Element;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.IntegerFieldDef;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.grid.GridPanel;

/**
 * Classe que implementa métodos uteis ao sistema como todo.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 */
public class UtilClient {

	/**
	 * Configuracoes setadas para a empresa.
	 */
	public static Map<String, String> CONF;

	/**
	 * Metodo que recupera um registro de uma massa de dados mediante filtro.
	 * 
	 * @param store
	 *            massa de dados a ser pesquisada.
	 * @param campoFiltro
	 *            nome do campo que será usado no filtro.
	 * @param valorFiltro
	 *            valor do filtro para identificar o registro.
	 * @return o registro encontrado após o filtro usado.
	 */
	public static Record getRegistro(Store store, String campoFiltro, String valorFiltro) {
		Record ret = null;
		try {
			for (Record rec : store.getRecords()) {
				String valor = rec.getAsString(campoFiltro).trim();
				valor = valor.replaceAll("\\s", "");
				if (valor.equals(valorFiltro)) {
					ret = rec;
					break;
				}
			}
		} catch (Exception ex) {
			ret = null;
		}
		return ret;
	}

	/**
	 * Metodo que padroniza o formato de datetime reconhecido pelo Grid.
	 * 
	 * @param data
	 *            o valor da data a ser formatada.
	 * @return a data formatada no padrão.
	 */
	public static String getDataHoraGrid(Date data) {
		if (data != null) {
			return getDataGrid(data) + " " + getHoraGrid(data);
		} else {
			return "";
		}
	}

	/**
	 * Metodo que padroniza o formato da date reconhecido pelo Grid.
	 * 
	 * @param data
	 *            o valor da data a ser formatada.
	 * @return a data formatada no padrão.
	 */
	public static String getDataGrid(Date data) {
		if (data != null) {
			String dia = data.getDate() > 9 ? data.getDate() + "" : "0" + data.getDate();
			String mes = data.getMonth() >= 9 ? (data.getMonth() + 1) + "" : "0" + (data.getMonth() + 1);
			String ano = (data.getYear() + 1900) + "";
			return (mes + "/" + dia + "/" + ano);
		} else {
			return "";
		}
	}

	/**
	 * Metodo que padroniza o formato da hora reconhecido pelo Grid.
	 * 
	 * @param hora
	 *            o valor da hora a ser formatada.
	 * @return a hora formatada no padrão.
	 */
	public static String getHoraGrid(Date hora) {
		if (hora != null) {
			String seg = hora.getSeconds() > 9 ? hora.getSeconds() + "" : "0" + hora.getSeconds();
			String min = hora.getMinutes() > 9 ? hora.getMinutes() + "" : "0" + hora.getMinutes();
			String hor = hora.getHours() > 9 ? hora.getHours() + "" : "0" + hora.getHours();
			return hor + ":" + min + ":" + seg;
		} else {
			return "";
		}
	}

	/**
	 * Metodo que verifica se a ação solicitada está entre as permitidas do função.
	 * 
	 * @param funcao
	 *            objeto de função a ser verificado a permissão.
	 * @param acao
	 *            classe de comando que executa a ação
	 * @return a classe de ação a ser executada
	 */
	public static SisAcao getAcaoPermitida(SisFuncao funcao, Class<? extends IComando> acao) {
		SisAcao retorno = null;

		if (funcao.getSisAcoes() != null) {
			for (SisAcao sAcao : funcao.getSisAcoes()) {
				if (sAcao.getSisAcaoClasse().equals(acao.getName())) {
					retorno = sAcao;
					break;
				}
			}
		}

		return retorno;
	}

	/**
	 * Metodoque verifica se a função solicitada está entre as permitidas do usuário.
	 * 
	 * @param funcao
	 *            classe de comando que executa a funcao.
	 * @return a classe de função a ser executada.
	 */
	public static SisFuncao getFuncaoPermitida(Class<? extends IComando> funcao) {
		return getFuncaoPermitida(funcao.getName());
	}

	/**
	 * Metodoque verifica se a função solicitada está entre as permitidas do usuário.
	 * 
	 * @param funcao
	 *            o nome da classe de comando que executa a funcao.
	 * @return a classe de função a ser executada.
	 */
	public static SisFuncao getFuncaoPermitida(String funcao) {
		SisFuncao retorno = null;

		for (SisModulo modulo : Ponte.getLogin().getModulos()) {
			for (SisFuncao sFuncao : modulo.getSisFuncoes()) {
				if (sFuncao.getSisFuncaoClasse().equals(funcao)) {
					retorno = sFuncao;
					break;
				}
			}
		}

		return retorno;
	}

	/**
	 * @see #formataNumero(double, int, int, boolean)
	 */
	public static String formataNumero(String valor, int inteiros, int decimal, boolean grupo) {
		return formataNumero(Double.valueOf(valor), inteiros, decimal, grupo);
	}

	/**
	 * Metodo que faz a formatacao de numeros com inteiros e fracoes
	 * 
	 * @param valor
	 *            o valor a ser formatado
	 * @param inteiros
	 *            o minimo de inteiros, que serao completados com ZEROS se preciso
	 * @param decimal
	 *            o minimo de decimais, que serao completados com ZEROS se preciso
	 * @param grupo
	 *            se sera colocado separador de grupo de milhar
	 * @return uma String com o numero formatado
	 */
	public static String formataNumero(double valor, int inteiros, int decimal, boolean grupo) {
		String padrao = "";

		if (grupo) {
			padrao = "#,##0";
		} else {
			for (int i = 0; i < inteiros; i++) {
				padrao += "0";
			}
		}

		if (decimal > 0) {
			padrao += ".";
			for (int i = 0; i < decimal; i++) {
				padrao += "0";
			}
		}

		NumberFormat nf = NumberFormat.getFormat(padrao);
		return nf.format(valor);
	}

	/**
	 * Metodo que verifica se o campo tem prefixo, senao coloca o padrao
	 * 
	 * @param campo
	 *            usado pela listagem.
	 * @return o campo prefixado.
	 */
	public static String getCampoPrefixado(String campo) {
		MatchResult mat = RegExp.compile("^t\\d+\\.").exec(campo);
		if (mat == null) {
			campo = "t." + campo.replace("t.", "");
		}
		return campo;
	}

	/**
	 * Metodo que cria um combobox padrao para entidades.
	 * 
	 * @param persiste
	 *            o objeto de persistencia.
	 * @return um combobox padrozinado para entidades.
	 */
	public static ComboBox getComboEntidade(ComboEntidade persiste) {
		FiltroTexto ft1 = new FiltroTexto("empEntidade.empEntidadeNome1", ECompara.CONTEM, null);
		FiltroTexto ft2 = new FiltroTexto("empEntidade.empEntidadeNome2", ECompara.CONTEM, null);
		FiltroTexto ft3 = new FiltroTexto("empEntidade.empEntidadeDocumento1", ECompara.CONTEM, null);
		GrupoFiltro gf = new GrupoFiltro(EJuncao.OU, new IFiltro[] { ft1, ft2, ft3 });

		FiltroBinario fb = new FiltroBinario("empEntidade.empEntidadeAtivo", ECompara.IGUAL, 1);
		GrupoFiltro filtros = new GrupoFiltro(EJuncao.E, new IFiltro[] { fb, gf });

		persiste.setFiltroPadrao(filtros);
		FieldDef[] campos = new FieldDef[] { new IntegerFieldDef(persiste.getClasse().getCampoId()), new IntegerFieldDef("empEntidadeId"), new StringFieldDef("empEntidade__empEntidadeNome1"),
				new StringFieldDef("empEntidade__empEntidadeNome2"), new StringFieldDef("empEntidade__empEntidadePessoa"), new StringFieldDef("empEntidade__empEntidadeDocumento1") };
		Store dados = new Store(persiste, new ArrayReader(new RecordDef(campos)), true);

		ComboBox combo = new ComboBox();
		combo.setWidth(300);
		combo.setMinChars(1);
		combo.setSelectOnFocus(true);
		combo.setListWidth(500);
		combo.setAllowBlank(false);
		combo.setStore(dados);
		combo.setTriggerAction(ComboBox.QUERY);
		combo.setMode(ComboBox.REMOTE);
		combo.setDisplayField("empEntidade__empEntidadeNome1");
		combo.setTpl("<div class=\"x-combo-list-item\"><b>{empEntidade__empEntidadeNome1}</b> - <i>{empEntidade__empEntidadeDocumento1}</i></div>");
		combo.setValueField(persiste.getClasse().getCampoId());
		combo.setForceSelection(true);

		return combo;
	}

	/**
	 * Metodo que retorna o ID do registro selecionado em um grid
	 * 
	 * @param grid
	 *            o grid a ser utilizado.
	 * @return 0 se nao tem ninguem selecionado ou um numero maior que 0 quando selecionado
	 */
	public static int getSelecionado(GridPanel grid) {
		if (grid.getSelectionModel().hasSelection() && !grid.getSelectionModel().getSelected().isEmpty(grid.getSelectionModel().getSelected().getFields()[0])) {
			return grid.getSelectionModel().getSelected().getAsInteger(grid.getSelectionModel().getSelected().getFields()[0]);
		} else {
			MessageBox.alert(OpenSigCore.i18n.txtSelecionar(), OpenSigCore.i18n.errSelecionar());
			return 0;
		}
	}

	/**
	 * Metodo que percorre os comando ate achar o ultimo para adicionar ao final da fila
	 * 
	 * @param comando
	 *            o IComando que será executado.
	 * @param ultimo
	 *            o IComando que será executado no final da lista de execucao.
	 */
	public static void comandoFinal(IComando comando, IComando ultimo) {
		IComando fim = comando.getProximo() != null ? comando.getProximo() : comando;
		while (fim.getProximo() != null) {
			fim = fim.getProximo();
		}
		fim.setProximo(ultimo);
	}

	/**
	 * Metodo que retorna o valor de uma tag dentro do xml.
	 * 
	 * @param ele
	 *            elemento xml em forma de objeto.
	 * @param tag
	 *            nome da tag que deseja recuperar o valor.
	 * @param excecao
	 *            se passado true dispara a exception se ocorrer erro, se false retorna null
	 * @return valor da tag encontrada ou NULL se nao achada.
	 * @exception NullPointerException
	 *                exceção disparada em caso de erro.
	 */
	public static String getValorTag(Element ele, String tag, boolean excecao) throws NullPointerException {
		try {
			return ele.getElementsByTagName(tag).item(0).getFirstChild().getNodeValue();
		} catch (Exception e) {
			if (excecao) {
				throw new NullPointerException(CONF.get("errInvalido") + " - > " + tag);
			}
			return null;
		}
	}

	/**
	 * Metodo que retorna o resultado de uma expressao.
	 * 
	 * @param expressao
	 *            uma expressao matemática.
	 * @return o resultado da expressao.
	 */
	public final static native String eval(String expressao)/*-{
															return eval(expressao).toString();
															}-*/;

	/**
	 * Metodo que solicita a exportacao
	 * 
	 * @param url
	 *            o endereco de exporracao formatada.
	 */
	public static native void exportar(String url) /*-{
													var frame = $doc.getElementById('__gwt_printingFrame');
													frame.src = url;
													}-*/;
}
