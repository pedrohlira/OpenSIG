package br.com.opensig.core.client.visao.abstrato;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.UtilClient;
import br.com.opensig.core.client.controlador.comando.AComando;
import br.com.opensig.core.client.controlador.comando.IComando;
import br.com.opensig.core.client.controlador.comando.form.ComandoCancelar;
import br.com.opensig.core.client.controlador.comando.form.ComandoSalvar;
import br.com.opensig.core.client.controlador.comando.lista.ComandoNovo;
import br.com.opensig.core.client.controlador.comando.lista.ComandoNovoDuplicar;
import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.FiltroNumero;
import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.core.shared.modelo.EBusca;
import br.com.opensig.core.shared.modelo.ExpListagem;
import br.com.opensig.core.shared.modelo.ExpMeta;
import br.com.opensig.core.shared.modelo.ExpRegistro;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;

import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Function;
import com.gwtext.client.core.Position;
import com.gwtext.client.data.Record;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Component;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarMenuButton;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.event.FieldListenerAdapter;
import com.gwtext.client.widgets.form.event.FormPanelListenerAdapter;
import com.gwtext.client.widgets.menu.BaseItem;
import com.gwtext.client.widgets.menu.Item;
import com.gwtext.client.widgets.menu.Menu;
import com.gwtext.client.widgets.menu.event.BaseItemListenerAdapter;
import com.gwtextux.client.widgets.grid.plugins.SummaryColumnConfig;

/**
 * Classe abstrata que representa o modelo genérico dos formulários do sistema.
 * 
 * @param <E>
 *            classe generica de dados.
 * @author Pedro H. Lira
 * @version 1.0
 */
public abstract class AFormulario<E extends Dados> extends FormPanel implements IFormulario<E> {

	/**
	 * Funcao que foi clicada.
	 */
	protected SisFuncao funcao;
	/**
	 * Classe do tipo que o formulario manipula.
	 */
	protected E classe;
	/**
	 * Mapa de contexto da funcao.
	 */
	protected Map contexto;
	/**
	 * Listagem que o formulario tem vinculo.
	 */
	protected IListagem<E> lista;
	/**
	 * Item do menu salvar e novo.
	 */
	protected Item itSalvarNovo;
	/**
	 * Item do meni salvar e duplicar.
	 */
	protected Item itSalvarDuplicar;
	/**
	 * Botao do menu salvar.
	 */
	protected ToolbarMenuButton btnSalvar;
	/**
	 * Botao do menu cancelar.
	 */
	protected ToolbarMenuButton btnCancelar;
	/**
	 * Sub-listagens do formulario para exportacao.
	 */
	protected List<ExpListagem> expLista;
	/**
	 * Se a opcao duplicar esta ativa naquele momento.
	 */
	protected boolean duplicar;
	/**
	 * Qual o campo padrao para receber o foco.
	 */
	protected Component focusPadrao;

	/**
	 * Construtor que recebe o objeto do tipo e sua funcao ativadora.
	 * 
	 * @param classe
	 *            o objeto do tipo definido que o formulario ira tratar.
	 * @param funcao
	 *            que utilizada pelo cliente que abriu este formulario.
	 */
	public AFormulario(E classe, SisFuncao funcao) {
		this.classe = classe;
		this.funcao = funcao;

		contexto = new HashMap();
		contexto.put("dados", classe);
		contexto.put("form", this);
	}

	@Override
	public void inicializar() {
		// salvar + novo
		Record rec = ANavegacao.getRegistro(ANavegacao.ACOES, ComandoNovo.class.getName());
		itSalvarNovo = new Item();
		itSalvarNovo.setText(OpenSigCore.i18n.txtSalvar() + " + " + rec.getAsString("nome"));
		itSalvarNovo.setIconCls("icon-salvar");
		itSalvarNovo.setTitle(OpenSigCore.i18n.msgSalvar());
		itSalvarNovo.addListener(new BaseItemListenerAdapter() {
			public void onClick(BaseItem item, EventObject e) {
				salvar(new ComandoNovo());
			}
		});

		// salvar + duplicar
		rec = ANavegacao.getRegistro(ANavegacao.ACOES, ComandoNovoDuplicar.class.getName());
		itSalvarDuplicar = new Item();
		itSalvarDuplicar.setText(OpenSigCore.i18n.txtSalvar() + " + " + rec.getAsString("nome"));
		itSalvarDuplicar.setIconCls("icon-salvar");
		itSalvarDuplicar.setTitle(OpenSigCore.i18n.msgSalvar());
		itSalvarDuplicar.addListener(new BaseItemListenerAdapter() {
			public void onClick(BaseItem item, EventObject e) {
				salvar(new ComandoNovoDuplicar());
			}
		});

		// definindo o menu
		Menu mnuSalvar = new Menu();
		if (funcao != null && UtilClient.getAcaoPermitida(funcao, ComandoNovo.class) != null) {
			mnuSalvar.addItem(itSalvarNovo);
			if (UtilClient.getAcaoPermitida(funcao, ComandoNovoDuplicar.class) != null) {
				mnuSalvar.addItem(itSalvarDuplicar);
			}
		}

		btnSalvar = new ToolbarMenuButton(OpenSigCore.i18n.txtSalvar(), mnuSalvar);
		btnSalvar.setTooltip(OpenSigCore.i18n.msgSalvar());
		btnSalvar.setIconCls("icon-salvar");
		btnSalvar.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				salvar(null);
			}
		});
		btnSalvar.addListener("onkeypress", new Function() {
			public void execute() {
				salvar(null);
			}
		});
		focusPadrao = btnSalvar;

		final ComandoCancelar cmdCancelar = new ComandoCancelar();
		btnCancelar = new ToolbarMenuButton(OpenSigCore.i18n.txtCancelar());
		btnCancelar.setTooltip(OpenSigCore.i18n.msgCancelar());
		btnCancelar.setIconCls("icon-cancelar");
		btnCancelar.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				if (AntesDaAcao(cmdCancelar) != null) {
					cmdCancelar.execute(contexto);
					DepoisDaAcao(cmdCancelar);
				}
			}
		});

		Toolbar tlbAcao = new Toolbar();
		tlbAcao.addButton(btnSalvar);
		tlbAcao.addButton(btnCancelar);

		setTitle(OpenSigCore.i18n.txtFormulario(), "icon-formulario");
		setAutoScroll(true);
		setLabelAlign(Position.TOP);
		setButtonAlign(Position.CENTER);
		setTopToolbar(tlbAcao);
		setPaddings(5);
		setMargins(1);
		disable();

		addListener(new FormPanelListenerAdapter() {
			public void onRender(Component component) {
				super.onRender(component);
				for (Field campo : getFields()) {
					campo.addListener(new FieldListenerAdapter() {
						public void onSpecialKey(Field field, EventObject e) {
							if (e.getKey() == EventObject.ENTER) {
								setFieldFocus(field);
							}
						}
					});
				}
			}
		});
	}

	@Override
	public void setFieldFocus(Field campo) {
		int i = 0;
		for (; i < getFields().length; i++) {
			if (campo.getName().equals(getFields()[i].getName())) {
				if (i + 1 == getFields().length && focusPadrao != null) {
					focusPadrao.focus();
				} else {
					getFields()[i + 1].focus();
				}
				break;
			}
		}
	}

	@Override
	public ExpRegistro<E> getExportacao() {
		List<ExpMeta> metadados = new ArrayList<ExpMeta>();
		for (int i = 0; i < lista.getModelos().getColumnCount(); i++) {
			if (lista.getModelos().isHidden(i)) {
				metadados.add(null);
			} else if (!lista.getModelos().getColumnHeader(i).startsWith("<div")) {
				ExpMeta meta = new ExpMeta(lista.getModelos().getColumnHeader(i), lista.getModelos().getColumnWidth(i), null);
				if (lista.getModelos().getColumnConfigs()[i] instanceof SummaryColumnConfig) {
					SummaryColumnConfig col = (SummaryColumnConfig) lista.getModelos().getColumnConfigs()[i];
					String tp = col.getSummaryType().equals("average") ? "AVG" : col.getSummaryType().toUpperCase();
					meta.setGrupo(EBusca.getBusca(tp));
				}
				metadados.add(meta);
			}
		}

		// filtro
		int id = UtilClient.getSelecionado(lista.getPanel());
		FiltroNumero filtro = new FiltroNumero(classe.getCampoId(), ECompara.IGUAL, id);

		Record recFuncao = UtilClient.getRegistro(ANavegacao.FUNCOES, "classe", funcao.getSisFuncaoClasse());
		ExpRegistro<E> expRegistro = new ExpRegistro<E>();
		expRegistro.setClasse(classe);
		expRegistro.setNome(recFuncao.getAsString("nome"));
		expRegistro.setMetadados(metadados);
		expRegistro.setFiltro(filtro);

		gerarListas();
		expRegistro.setExpLista(expLista);
		return expRegistro;
	}

	@Override
	public void salvar(IComando comando) {
		if (getForm().isValid() && setDados()) {
			final IComando cmdSalvar = AntesDaAcao(new ComandoSalvar<E>());
			if (cmdSalvar != null) {
				MessageBox.wait(OpenSigCore.i18n.txtAguarde(), OpenSigCore.i18n.txtSalvar());

				// comando para acao de depois de executar
				IComando cmdDepois = new AComando<E>() {
					public void execute(Map contexto) {
						DepoisDaAcao(cmdSalvar);
					}
				};

				// verifica se tem um comando apos salvar
				if (comando != null) {
					comando.setProximo(cmdDepois);
				} else {
					comando = new ComandoCancelar(cmdDepois);
				}
				UtilClient.comandoFinal(cmdSalvar, comando);

				// executa
				cmdSalvar.execute(contexto);
			}
		}
	}

	@Override
	public ToolbarMenuButton getBtnCancelar() {
		return btnCancelar;
	}

	@Override
	public void setBtnCancelar(ToolbarMenuButton btnCancelar) {
		this.btnCancelar = btnCancelar;
	}

	@Override
	public ToolbarMenuButton getBtnSalvar() {
		return btnSalvar;
	}

	@Override
	public void setBtnSalvar(ToolbarMenuButton btnSalvar) {
		this.btnSalvar = btnSalvar;
	}

	@Override
	public E getClasse() {
		return classe;
	}

	@Override
	public void setClasse(E classe) {
		this.classe = classe;
	}

	@Override
	public Map getContexto() {
		return contexto;
	}

	@Override
	public void setContexto(Map contexto) {
		this.contexto = contexto;
	}

	@Override
	public SisFuncao getFuncao() {
		return funcao;
	}

	@Override
	public void setFuncao(SisFuncao funcao) {
		this.funcao = funcao;
	}

	@Override
	public IListagem<E> getLista() {
		return lista;
	}

	@Override
	public void setLista(IListagem<E> lista) {
		this.lista = lista;
		contexto.put("lista", lista);
	}

	@Override
	public List<ExpListagem> getExpLista() {
		return expLista;
	}

	@Override
	public void setExpLista(List<ExpListagem> expLista) {
		this.expLista = expLista;
	}

	@Override
	public Toolbar getTlbAcao() {
		return getTopToolbar();
	}

	@Override
	public void setTlbAcao(Toolbar tlbAcao) {
		setTopToolbar(tlbAcao);
	}

	@Override
	public Item getItSalvarNovo() {
		return itSalvarNovo;
	}

	@Override
	public void setItSalvarNovo(Item itSalvarNovo) {
		this.itSalvarNovo = itSalvarNovo;
	}

	@Override
	public Item getItSalvarDuplicar() {
		return itSalvarDuplicar;
	}

	@Override
	public void setItSalvarDuplicar(Item itSalvarDuplicar) {
		this.itSalvarDuplicar = itSalvarDuplicar;
	}

	@Override
	public boolean isDuplicar() {
		return duplicar;
	}

	@Override
	public void setDuplicar(boolean duplicar) {
		this.duplicar = duplicar;
	}

	@Override
	public Component getFocusPadrao() {
		return focusPadrao;
	}

	@Override
	public void setFocusPadrao(Component focusPadrao) {
		this.focusPadrao = focusPadrao;
	}

	@Override
	public FormPanel getPanel() {
		return this;
	}

	@Override
	public IComando AntesDaAcao(IComando comando) {
		return comando;
	}

	@Override
	public void DepoisDaAcao(IComando comando) {
	}
}
