package br.com.opensig.core.client.visao.abstrato;

import java.util.Date;
import java.util.Map;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.controlador.comando.EModo;
import br.com.opensig.core.client.controlador.comando.IComando;
import br.com.opensig.core.client.controlador.filtro.IFiltro;
import br.com.opensig.core.client.servico.CoreProxy;
import br.com.opensig.core.client.visao.NavegacaoLista;
import br.com.opensig.core.client.visao.Paginador;
import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.core.shared.modelo.ExpListagem;
import br.com.opensig.core.shared.modelo.IFavorito;
import br.com.opensig.core.shared.modelo.sistema.SisExpImp;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.widgets.grid.CellMetadata;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.Renderer;
import com.gwtext.client.widgets.menu.Menu;
import com.gwtextux.client.widgets.grid.plugins.GridFilter;

/**
 * Interface que representa o modelo genérico das listagens do sistema.
 * 
 * @param <E>
 *            classe generica de dados.
 * @author Pedro H. Lira
 * @version 1.0
 */
public interface IListagem<E extends Dados> {

	/**
	 * Formato de data com hora medio. ex:31/01/2011 12:00:00
	 */
	public DateTimeFormat DTF = DateTimeFormat.getFormat(PredefinedFormat.DATE_TIME_MEDIUM);
	/**
	 * Formato de data medio. ex:31/01/2011
	 */
	public DateTimeFormat DF = DateTimeFormat.getFormat(PredefinedFormat.DATE_MEDIUM);
	/**
	 * Formato de hora medio. ex:12:00:00
	 */
	public DateTimeFormat TF = DateTimeFormat.getFormat(PredefinedFormat.TIME_MEDIUM);
	/**
	 * Formato de numero decimal. ex:10,55
	 */
	public NumberFormat NDF = NumberFormat.getDecimalFormat();
	/**
	 * Formato de numero dinheiro. ex:R$ 10,55
	 */
	public NumberFormat NCF = NumberFormat.getCurrencyFormat();
	/**
	 * Formato de numero personalizado. ex:0,1234
	 */
	public NumberFormat CUSTOM = NumberFormat.getFormat("0.0000");
	/**
	 * Renderiza a celula da listagem como Sim/Nao.
	 */
	public Renderer BOLEANO = new Renderer() {
		public String render(Object value, CellMetadata cellMetadata, Record record, int rowIndex, int colNum, Store store) {
			cellMetadata.setCssClass("colCentro");
			return value != null && Boolean.valueOf(value.toString()) ? OpenSigCore.i18n.txtSim() : OpenSigCore.i18n.txtNao();
		}
	};
	/**
	 * Renderiza a celula da listagem como numero.
	 */
	public Renderer NUMERO = new Renderer() {
		public String render(Object value, CellMetadata cellMetadata, Record record, int rowIndex, int colNum, Store store) {
			cellMetadata.setCssClass("colDireita");
			return value != null ? NDF.format(Float.parseFloat(value.toString())) : "";
		}
	};
	/**
	 * Renderiza a celula da listagem como dinheiro.
	 */
	public Renderer DINHEIRO = new Renderer() {
		public String render(Object value, CellMetadata cellMetadata, Record record, int rowIndex, int colNum, Store store) {
			cellMetadata.setCssClass("colDireita");
			return value != null ? NCF.format(Double.parseDouble(value.toString())) : "";
		}
	};
	/**
	 * Renderiza a celula da listagem como porcentagem.
	 */
	public Renderer PORCENTAGEM = new Renderer() {
		public String render(Object value, CellMetadata cellMetadata, Record record, int rowIndex, int colNum, Store store) {
			return NUMERO.render(value, cellMetadata, record, rowIndex, colNum, store) + "%";
		}
	};
	/**
	 * Renderiza a celula da listagem como valor personalizado.
	 */
	public Renderer VALOR = new Renderer() {
		public String render(Object value, CellMetadata cellMetadata, Record record, int rowIndex, int colNum, Store store) {
			cellMetadata.setCssClass("colDireita");
			return value != null ? CUSTOM.format(Double.parseDouble(value.toString())) : "";
		}
	};
	/**
	 * Renderiza a celula da listagem como data/hora.
	 */
	public Renderer DATAHORA = new Renderer() {
		public String render(Object value, CellMetadata cellMetadata, Record record, int rowIndex, int colNum, Store store) {
			cellMetadata.setCssClass("colCentro");
			return value != null ? DTF.format((Date) value) : "";
		}
	};
	/**
	 * Renderiza a celula da listagem como data.
	 */
	public Renderer DATA = new Renderer() {
		public String render(Object value, CellMetadata cellMetadata, Record record, int rowIndex, int colNum, Store store) {
			cellMetadata.setCssClass("colCentro");
			return value != null ? DF.format((Date) value) : "";
		}
	};
	/**
	 * Renderiza a celula da listagem como hora.
	 */
	public Renderer HORA = new Renderer() {
		public String render(Object value, CellMetadata cellMetadata, Record record, int rowIndex, int colNum, Store store) {
			cellMetadata.setCssClass("colCentro");
			return value != null ? TF.format((Date) value) : "";
		}
	};
	/**
	 * Renderiza a celula da listagem como contador, usado em sumarios.
	 */
	public Renderer CONTADOR = new Renderer() {
		public String render(Object value, CellMetadata cellMetadata, Record record, int rowIndex, int colNum, Store store) {
			if (value != null) {
				int intValue = ((Integer) value).intValue();
				if (intValue == 0 || intValue > 0) {
					return "(" + intValue + " " + OpenSigCore.i18n.txtItens() + ")";
				} else {
					return "(1 " + OpenSigCore.i18n.txtItem() + ")";
				}
			} else {
				return "";
			}
		}
	};

	/**
	 * Metodo que inicializa os objetos visuais.
	 */
	public void inicializar();

	/**
	 * Metodo que define od filtros da listagem
	 */
	public void setGridFiltro();

	/**
	 * Metodo que seta os dados com o resultado do favorito.
	 * 
	 * @param favorito
	 *            as definicoes salvas pelo usuario anteriormente.
	 */
	public void setFavorito(IFavorito favorito);

	/**
	 * Metodo que pega os dados para exportacao.
	 * 
	 * @return o objeto de exportacao de listagem.
	 */
	public ExpListagem<E> getExportacao();

	/**
	 * Metodo que recupera o tipo de exportacao.
	 * 
	 * @param expimp
	 *            o tipo de opcoes para exportar.
	 * @param modo
	 *            o modo de exportacao externa.
	 * @param modo2
	 *            o modo de exportacao interna.
	 * @param async
	 *            o metodo assincrono a ser executado.
	 */
	public void setExportacao(SisExpImp expimp, EModo modo, EModo modo2, AsyncCallback<String> async);

	/**
	 * Metodo que recupera o tipo de importacao.
	 * 
	 * @param expimp
	 *            o tipo de opcoes para importar.
	 */
	public void setImportacao(SisExpImp expimp);

	/**
	 * Metodo que retorna o objeto visual da listagem.
	 * 
	 * @return o objeto visual da listagem.
	 */
	public GridPanel getPanel();

	/**
	 * Metodo que gera os sub-menus de link para outras funcoes.
	 */
	public void irPara();

	/**
	 * Metodo que e executada antes de qualquer comando da listagem.
	 * 
	 * @param comando
	 *            ativado no momento pelo usuario.
	 * @return o comando que sera executado pelo usuario, apos validacoes.
	 */
	public IComando AntesDaAcao(IComando comando);

	/**
	 * Metodo que e executado apos qualquer comando da listagem.
	 * 
	 * @param comando
	 *            ativado no momento pelo usuario.
	 */
	public void DepoisDaAcao(IComando comando);

	/**
	 * Metodo que e executada antes de ativar a funcao.
	 * 
	 * @param comando
	 *            ativado no momento pelo usuario.
	 * @return o comando que sera executado pelo usuario, apos validacoes.
	 */
	public IComando AntesDaFuncao(IComando comando, Map contexto);

	/**
	 * Metodo que e executado apos ativar a funcao.
	 * 
	 * @param comando
	 *            ativado no momento pelo usuario.
	 */
	public void DepoisDaFuncao(IComando comando, Map contexto);

	/**
	 * Valida se o campo na listagem e do tipo informado.
	 * 
	 * @param valor
	 *            o valor do campo.
	 * @param tipo
	 *            o tipo esperado.
	 * @return verdadeiro se correspondente, falso caso contrario.
	 */
	public boolean validarCampo(String valor, String tipo);

	/**
	 * Valida se o campo na listagem e do tipo data.
	 * 
	 * @param valor
	 *            o valor do campo.
	 * @return verdadeiro se correspondente, falso caso contrario.
	 */
	public boolean validarCampoData(String valor);

	/**
	 * Valida se o campo na listagem e do tipo texto.
	 * 
	 * @param valor
	 *            o valor do campo.
	 * @return verdadeiro se correspondente, falso caso contrario.
	 */
	public boolean validarCampoTexto(String valor);

	/**
	 * Valida se o campo na listagem e do tipo numero generico.
	 * 
	 * @param valor
	 *            o valor do campo.
	 * @return verdadeiro se correspondente, falso caso contrario.
	 */
	public boolean validarCampoNumero(String valor);

	/**
	 * Valida se o campo na listagem e do tipo numero inteiro.
	 * 
	 * @param valor
	 *            o valor do campo.
	 * @return verdadeiro se correspondente, falso caso contrario.
	 */
	public boolean validarCampoInteiro(String valor);

	/**
	 * Valida se o campo na listagem e do tipo numero decimal.
	 * 
	 * @param valor
	 *            o valor do campo.
	 * @return verdadeiro se correspondente, falso caso contrario.
	 */
	public boolean validarCampoDecimal(String valor);

	/**
	 * Valida se o campo na listagem e do tipo binario.
	 * 
	 * @param valor
	 *            o valor do campo.
	 * @return verdadeiro se correspondente, falso caso contrario.
	 */
	public boolean validarCampoBinario(String valor);

	// Gets e Seteres

	public boolean isBarraTarefa();

	public void setBarraTarefa(boolean barraTarefa);

	public RecordDef getCampos();

	public void setCampos(RecordDef campos);

	public E getClasse();

	public void setClasse(E classe);

	public Map getContexto();

	public void setContexto(Map contexto);

	public Paginador getPaginador();

	public void setPaginador(Paginador paginador);

	public CoreProxy<E> getProxy();

	public void setProxy(CoreProxy<E> persistencia);

	public IFiltro getFiltroPadrao();

	public void setFiltroPadrao(IFiltro filtroPadrao);

	public void setFiltroExtra(IFiltro filtroExtra);

	public IFiltro getFiltroExtra();

	public Map<String, GridFilter> getFiltros();

	public void setFiltros(Map<String, GridFilter> filtros);

	public IFormulario<E> getForm();

	public void setForm(IFormulario<E> form);

	public SisFuncao getFuncao();

	public void setFuncao(SisFuncao funcao);

	public ColumnModel getModelos();

	public void setModelos(ColumnModel modelos);

	public boolean isAgrupar();

	public void setAgrupar(boolean agrupar);

	public boolean isPaginar();

	public void setPaginar(boolean paginar);

	public Menu getMenu();

	public void setMenu(Menu menu);

	public NavegacaoLista<E> getNavegacao();

	public void setNavegacao(NavegacaoLista<E> navegacao);

}
