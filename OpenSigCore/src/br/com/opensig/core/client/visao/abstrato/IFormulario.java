package br.com.opensig.core.client.visao.abstrato;

import java.util.List;
import java.util.Map;

import br.com.opensig.core.client.controlador.comando.IComando;
import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.core.shared.modelo.ExpListagem;
import br.com.opensig.core.shared.modelo.ExpRegistro;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;

import com.gwtext.client.widgets.Component;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarMenuButton;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.menu.Item;

/**
 * Interface que representa o modelo genérico dos formulários do sistema.
 * 
 * @param <E>
 *            classe generica de dados.
 * @author Pedro H. Lira
 * @version 1.0
 */
public interface IFormulario<E extends Dados> {

	/**
	 * Metodo que inicializa os objetos visuais.
	 */
	public void inicializar();

	/**
	 * Metodo que salva os dados, podendo receber um outro comando para acionar
	 * apos salvar.
	 * 
	 * @param comando
	 *            a ser executado apos salvar, pode ser null.
	 */
	public void salvar(IComando comando);

	/**
	 * Metodo que seta os dados informados no formulario no objeto POJO a ser
	 * salvo.
	 * 
	 * @return verdadeiro se nao tiver faltando dados, falso caso contrario.
	 */
	public boolean setDados();

	/**
	 * Metodo que carrega os dados da base para o formulario e sub-listagens.
	 */
	public void mostrarDados();

	/**
	 * Metodo que limpa os dados do fomulario e suas sub-listagens.
	 */
	public void limparDados();

	/**
	 * Metodo que gera as listas de exportacao das sub-listagens do formulario.
	 */
	public void gerarListas();

	/**
	 * Metodo que retorna o objeto visual do formulario.
	 * 
	 * @return o Form visual.
	 */
	public FormPanel getPanel();

	/**
	 * Metodo que e executada antes de qualquer comando do formulario.
	 * 
	 * @param comando
	 *            ativado no momento pelo usuario.
	 * @return o comando que sera executado pelo usuario, apos validacoes.
	 */
	public IComando AntesDaAcao(IComando comando);

	/**
	 * Metodo que e executado apos qualquer comando do formulario.
	 * 
	 * @param comando
	 *            ativado no momento pelo usuario.
	 */
	public void DepoisDaAcao(IComando comando);

	// Gets e Seteres

	public ToolbarMenuButton getBtnCancelar();

	public void setBtnCancelar(ToolbarMenuButton btnCancelar);

	public ToolbarMenuButton getBtnSalvar();

	public void setBtnSalvar(ToolbarMenuButton btnSalvar);

	public E getClasse();

	public void setClasse(E classe);

	public Map getContexto();

	public void setContexto(Map contexto);

	public SisFuncao getFuncao();

	public void setFuncao(SisFuncao funcao);

	public IListagem<E> getLista();

	public void setLista(IListagem<E> lista);

	public List<ExpListagem> getExpLista();

	public void setExpLista(List<ExpListagem> expLista);

	public void setFieldFocus(Field campo);

	public ExpRegistro<E> getExportacao();

	public Toolbar getTlbAcao();

	public void setTlbAcao(Toolbar tlbAcao);

	public Item getItSalvarNovo();

	public void setItSalvarNovo(Item itSalvarNovo);

	public Item getItSalvarDuplicar();

	public void setItSalvarDuplicar(Item itSalvarDuplicar);

	public boolean isDuplicar();

	public void setDuplicar(boolean duplicar);

	public Component getFocusPadrao();

	public void setFocusPadrao(Component focusPadrao);

}