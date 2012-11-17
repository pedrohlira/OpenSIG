package br.com.opensig.core.client.visao.abstrato;

import java.util.Collection;
import java.util.Map;

import br.com.opensig.core.client.controlador.comando.IComando;
import br.com.opensig.core.client.visao.grafico.IGraficoDados;
import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.core.shared.modelo.EBusca;
import br.com.opensig.core.shared.modelo.EData;
import br.com.opensig.core.shared.modelo.EDirecao;
import br.com.opensig.core.shared.modelo.EGrafico;
import br.com.opensig.core.shared.modelo.IFavoritoGrafico;

import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.NumberField;
import com.rednels.ofcgwt.client.ChartWidget;

/**
 * Interface que representa o modelo genérico dos graficos do sistema.
 * 
 * @param <E>
 *            classe generica de dados.
 * @author Pedro H. Lira
 * @version 1.0
 */
public interface IGrafico<E extends Dados> {

	/**
	 * Metodo que inicializa os objetos visuais do grafico.
	 */
	public void inicializar();

	/**
	 * Metodo que gera o grafico visualmente na tela.
	 */
	public void gerarGrafico();

	/**
	 * Metodo que exporta para uma imagem o grafico atual.
	 */
	public void exportarImagem();

	/**
	 * Metodo que define os dados para preencher no grafico.
	 * 
	 * @param resultado
	 *            uma colecao de strings com os dados.
	 */
	public void mostrarGrafico(Collection<String[]> resultado);

	/**
	 * Metodo que recupera o panel visual do grafico.
	 * 
	 * @return a referencia ao objeto visual.
	 */
	public Panel getPanel();

	/**
	 * Metodo que seta os dados do grafico com o favorito.
	 * 
	 * @param favorito
	 *            o favorito a ser carregado.
	 */
	public void setFavorito(IFavoritoGrafico favorito);

	/**
	 * Metodo que e disparado antes de qualquer acao do grafico.
	 * 
	 * @param comando
	 *            disparado pelo usuario.
	 * @return o comando que sera executado apos validado.
	 */
	public IComando AntesDaAcao(IComando comando);

	/**
	 * Metodo que e disparado apos qualquer acao do grafico.
	 * 
	 * @param comando
	 *            disparado pelo usuario.
	 */
	public void DepoisDaAcao(IComando comando);

	// Gets e Seteres
	
	public String getTitulo();

	public boolean validarFiltros();

	public ComboBox getCampos(final String campo);

	public NumberField getTxtLimite();

	public String[][] getValoresCombo(String combo);

	public void alterarValores(Collection<String[]> dados);

	public ChartWidget getGrafico();

	public void setGrafico(ChartWidget grafico);

	public Toolbar getToolGrafico();

	public void setToolGrafico(Toolbar toolGrafico);

	public ToolbarButton getBtnTipo();

	public void setBtnTipo(ToolbarButton btnTipo);

	public ComboBox getCmbCampoX();

	public void setCmbCampoX(ComboBox cmbCampoX);

	public ComboBox getCmbCampoSubX();

	public void setCmbCampoSubX(ComboBox cmbCampoSubX);

	public ComboBox getCmbCampoY();

	public void setCmbCampoY(ComboBox cmbCampoY);

	public ToolbarButton getBtnParteData();

	public void setBtnParteData(ToolbarButton btnParteData);

	public ToolbarButton getBtnGrupoValor();

	public void setBtnGrupoValor(ToolbarButton btnGrupoValor);

	public ToolbarButton getBtnAcao();

	public void setBtnAcao(ToolbarButton btnAcao);

	public ToolbarButton getBtnImagem();

	public void setBtnImagem(ToolbarButton btnImagem);

	public IListagem<E> getLista();

	public void setLista(IListagem<E> lista);

	public EGrafico getEGrafico();

	public void setEGrafico(EGrafico egrafico);

	public EData getEData();

	public void setEData(EData edata);

	public EBusca getEValor();

	public void setEValor(EBusca evalor);

	public EDirecao getEOrdem();

	public void setEOrdem(EDirecao eordem);

	public E getClasse();

	public void setClasse(E classe);

	public IGraficoDados<E> getDados();

	public void setDados(IGraficoDados<E> dados);

	public Map getContexto();

	public void setContexto(Map contexto);

	public String getValorCampo();

	public void setValorCampo(String valorCampo);

	public void setTxtLimite(NumberField txtLimite);

}