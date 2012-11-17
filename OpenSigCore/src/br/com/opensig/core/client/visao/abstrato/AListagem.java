package br.com.opensig.core.client.visao.abstrato;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.UtilClient;
import br.com.opensig.core.client.controlador.comando.EModo;
import br.com.opensig.core.client.controlador.comando.FabricaComando;
import br.com.opensig.core.client.controlador.comando.IComando;
import br.com.opensig.core.client.controlador.comando.lista.ComandoVisualizar;
import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.EJuncao;
import br.com.opensig.core.client.controlador.filtro.FiltroBinario;
import br.com.opensig.core.client.controlador.filtro.FiltroData;
import br.com.opensig.core.client.controlador.filtro.FiltroNumero;
import br.com.opensig.core.client.controlador.filtro.FiltroTexto;
import br.com.opensig.core.client.controlador.filtro.GrupoFiltro;
import br.com.opensig.core.client.controlador.filtro.IFiltro;
import br.com.opensig.core.client.servico.CoreProxy;
import br.com.opensig.core.client.servico.ExportacaoProxy;
import br.com.opensig.core.client.visao.FormularioVazio;
import br.com.opensig.core.client.visao.NavegacaoLista;
import br.com.opensig.core.client.visao.Paginador;
import br.com.opensig.core.client.visao.Ponte;
import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.core.shared.modelo.EBusca;
import br.com.opensig.core.shared.modelo.EDirecao;
import br.com.opensig.core.shared.modelo.ExpListagem;
import br.com.opensig.core.shared.modelo.ExpMeta;
import br.com.opensig.core.shared.modelo.ExpRegistro;
import br.com.opensig.core.shared.modelo.IFavorito;
import br.com.opensig.core.shared.modelo.IFavoritoCampo;
import br.com.opensig.core.shared.modelo.sistema.SisAcao;
import br.com.opensig.core.shared.modelo.sistema.SisExpImp;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;

import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.SortDir;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.BooleanFieldDef;
import com.gwtext.client.data.DateFieldDef;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.FloatFieldDef;
import com.gwtext.client.data.GroupingStore;
import com.gwtext.client.data.IntegerFieldDef;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.SortState;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.data.event.StoreListenerAdapter;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.TabPanel;
import com.gwtext.client.widgets.grid.BaseColumnConfig;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.GroupingView;
import com.gwtext.client.widgets.grid.RowSelectionModel;
import com.gwtext.client.widgets.grid.event.GridRowListenerAdapter;
import com.gwtext.client.widgets.menu.BaseItem;
import com.gwtext.client.widgets.menu.Menu;
import com.gwtext.client.widgets.menu.MenuItem;
import com.gwtext.client.widgets.menu.event.BaseItemListenerAdapter;
import com.gwtextux.client.widgets.grid.plugins.GridBooleanFilter;
import com.gwtextux.client.widgets.grid.plugins.GridDateFilter;
import com.gwtextux.client.widgets.grid.plugins.GridFilter;
import com.gwtextux.client.widgets.grid.plugins.GridFilterPlugin;
import com.gwtextux.client.widgets.grid.plugins.GridLongFilter;
import com.gwtextux.client.widgets.grid.plugins.GridNumericFilter;
import com.gwtextux.client.widgets.grid.plugins.GridStringFilter;
import com.gwtextux.client.widgets.grid.plugins.SummaryColumnConfig;
import com.gwtextux.client.widgets.window.ToastWindow;

/**
 * Classe que representa o modelo genérico das listas "grids" do sistema.
 * 
 * @param <E>
 *            classe generica de dados.
 * @author Pedro H. Lira
 * @version 1.0
 */
public abstract class AListagem<E extends Dados> extends GridPanel implements IListagem<E> {

	/**
	 * Classe do tipo que a listagem manipula.
	 */
	protected E classe;
	/**
	 * Funcao que foi clicada.
	 */
	protected SisFuncao funcao;
	/**
	 * Formulario que a listagem tem vinculo.
	 */
	protected IFormulario<E> form;
	/**
	 * Mapa de contexto da funcao.
	 */
	protected Map contexto;
	/**
	 * Menu que abre com o botao direito.
	 */
	protected Menu menu;
	/**
	 * Proxy de dados, para preencher a listagem.
	 */
	protected CoreProxy<E> proxy;
	/**
	 * Definicao de registros para os campos.
	 */
	protected RecordDef campos;
	/**
	 * Modelo de coluna dos campos.
	 */
	protected ColumnModel modelos;
	/**
	 * Mapa de filtros aplicados.
	 */
	protected Map<String, GridFilter> filtros;
	/**
	 * Filtro padrao na listagem sem poder remover.
	 */
	protected IFiltro filtroPadrao;
	/**
	 * Menus de navegacao.
	 */
	protected NavegacaoLista<E> navegacao;
	/**
	 * Objeto de paginacao
	 */
	protected Paginador paginador;
	/**
	 * Informa se a lista tem barra de tarefas.
	 */
	protected boolean barraTarefa = true;
	/**
	 * Informa se a lista tem barra de paginacao.
	 */
	protected boolean paginar = true;
	/**
	 * Informa se a lista permite agrupar por campos.
	 */
	protected boolean agrupar = true;

	/**
	 * Construtor padrao que recebe o formulario vinculado da lista.
	 * 
	 * @param form
	 *            o formulario que a listagem vincula os dados, caso nao tenha, informar o FomularioVazio.
	 */
	public AListagem(IFormulario<E> form) {
		this.form = form;
		this.classe = form.getClasse();
		this.funcao = form.getFuncao();
		this.menu = new Menu();

		contexto = new HashMap();
		contexto.put("dados", classe);
		contexto.put("form", form);
		contexto.put("lista", this);
	}

	@Override
	public void inicializar() {
		// renomeando colunas
		for (BaseColumnConfig col : modelos.getColumnConfigs()) {
			ColumnConfig coluna = null;
			if (col instanceof ColumnConfig) {
				coluna = (ColumnConfig) col;
			} else if (col instanceof SummaryColumnConfig) {
				SummaryColumnConfig sumario = (SummaryColumnConfig) col;
				coluna = (ColumnConfig) sumario.getColumn();
			}

			if (coluna != null) {
				coluna.setId("col_" + coluna.getDataIndex());
			}
		}

		// setando a fonte de dados
		proxy = new CoreProxy<E>(classe, filtroPadrao);
		Store dados = agrupar ? new GroupingStore(proxy, new ArrayReader(campos), true) : new Store(proxy, new ArrayReader(campos), true);
		dados.setSortInfo(new SortState(classe.getCampoOrdem(), SortDir.getValue(classe.getOrdemDirecao().toString())));
		dados.setAutoLoad(false);
		dados.addStoreListener(new StoreListenerAdapter() {
			// tratando caso nao tenha nenhum registro
			public void onLoad(Store store, Record[] records) {
				try {
					if (records.length == 0) {
						Record rec = campos.createRecord(new Object[campos.getFields().length]);
						store.add(rec);
					}
					getEl().unmask();
				} catch (Exception e) {
				}
			}
		});

		// adicionando a barra de menus
		if (barraTarefa) {
			navegacao = new NavegacaoLista<E>(this);
			if (funcao.getSisAcoes() != null) {
				navegacao.visit(funcao.getSisAcoes().toArray(new SisAcao[0]));
				irPara();
			}

			final ComandoVisualizar cmdVisualizar = new ComandoVisualizar();
			addGridRowListener(new GridRowListenerAdapter() {

				public void onRowDblClick(GridPanel grid, int rowIndex, EventObject e) {
					Record rec = getSelectionModel().getSelected();
					if (form != null && !(form instanceof FormularioVazio) && rec != null && rec.getAsInteger(rec.getFields()[0]) > 0) {
						cmdVisualizar.execute(contexto);
					} else {
						new ToastWindow(OpenSigCore.i18n.txtFormulario(), OpenSigCore.i18n.msgRegistro()).show();
					}
				}

				public void onRowContextMenu(GridPanel grid, int rowIndex, EventObject e) {
					grid.getSelectionModel().selectRow(rowIndex);
					menu.showAt(e.getXY());
				}
			});
		}

		// adicionando a barra de status com paginação
		if (paginar) {
			paginador = new Paginador(dados, true);
			setBottomToolbar(paginador);
		}

		// adicionando os filtros
		setGridFiltro();
		if (filtros != null) {
			GridFilterPlugin plugin = new GridFilterPlugin(filtros.values().toArray(new GridFilter[] {}));
			plugin.setAutoReload(true);
			plugin.setMenuFilterText(OpenSigCore.i18n.txtFiltros());
			plugin.setUpdateBuffer(1000);
			addPlugin(plugin);
		}

		// setando configuração
		setTitle(OpenSigCore.i18n.txtListagem(), "icon-listagem");
		setAutoScroll(true);
		setMargins(1);
		setLoadMask(true);
		setStripeRows(true);
		setStore(dados);
		setColumnModel(modelos);
		setSelectionModel(new RowSelectionModel(true));

		// caso a listagem tenha agrupamento visual
		if (agrupar) {
			GroupingView grupoVisao = new GroupingView();
			grupoVisao.setEmptyGroupText(OpenSigCore.i18n.msgRegistro());
			grupoVisao.setGroupTextTpl("{text} ({[values.rs.length]} {[values.rs.length > 1 ? \"" + OpenSigCore.i18n.txtItens() + "\" : \"" + OpenSigCore.i18n.txtItem() + "\"]})");
			grupoVisao.setEnableNoGroups(true);
			setView(grupoVisao);
		}
	}

	@Override
	public void irPara() {
	}

	@Override
	public void setGridFiltro() {
		filtros = new HashMap<String, GridFilter>();
		// renomeando colunas
		for (BaseColumnConfig col : modelos.getColumnConfigs()) {
			ColumnConfig coluna = null;
			if (col instanceof ColumnConfig) {
				coluna = (ColumnConfig) col;
			} else if (col instanceof SummaryColumnConfig) {
				SummaryColumnConfig sumario = (SummaryColumnConfig) col;
				coluna = (ColumnConfig) sumario.getColumn();
			}

			if (coluna != null) {
				String index = coluna.getDataIndex();
				GridFilter filtro;

				// varifica se é boolean
				if (validarCampoBinario(index)) {
					GridBooleanFilter binario = new GridBooleanFilter(index);
					binario.setYesText(OpenSigCore.i18n.txtSim());
					binario.setNoText(OpenSigCore.i18n.txtNao());
					filtro = binario;
					// valida se é data
				} else if (validarCampoData(index)) {
					GridDateFilter data = new GridDateFilter(index);
					data.setAfterText(OpenSigCore.i18n.txtDepois());
					data.setBeforeText(OpenSigCore.i18n.txtAntes());
					data.setOnText(OpenSigCore.i18n.txtIgual());
					filtro = data;
					// valida se é inteiro
				} else if (validarCampoInteiro(index)) {
					filtro = new GridLongFilter(index);
					// valida se é decimal
				} else if (validarCampoDecimal(index)) {
					filtro = new GridNumericFilter(index);
					// se nada funciona é texto
				} else {
					filtro = new GridStringFilter(index);
				}
				filtros.put(index, filtro);
			}
		}
	}

	@Override
	public void setFavorito(IFavorito favorito) {
		GrupoFiltro gf = new GrupoFiltro();
		for (IFavoritoCampo campo : favorito.getSisFavoritoCampos()) {
			if (!campo.getSisFavoritoCampoVisivel()) {
				hideColumn("col_" + campo.getSisFavoritoCampoNome());
			}

			// numeros
			if (campo.getSisFavoritoCampoTipo().equals(IntegerFieldDef.class.getName()) || campo.getSisFavoritoCampoTipo().equals(FloatFieldDef.class.getName())) {
				// filtro 1
				if (!campo.getSisFavoritoCampoFiltro1Compara().equals("")) {
					FiltroNumero fn = new FiltroNumero();
					setFiltro(fn, campo.getSisFavoritoCampoNome(), campo.getSisFavoritoCampoFiltro1Compara(), campo.getSisFavoritoCampoFiltro1Valor());
					gf.add(fn, EJuncao.E);
				}
				// filtro 2
				if (!campo.getSisFavoritoCampoFiltro2Compara().equals("")) {
					FiltroNumero fn = new FiltroNumero();
					setFiltro(fn, campo.getSisFavoritoCampoNome(), campo.getSisFavoritoCampoFiltro2Compara(), campo.getSisFavoritoCampoFiltro2Valor());
					gf.add(fn, EJuncao.E);
				}
			} // string
			else if (campo.getSisFavoritoCampoTipo().equals(StringFieldDef.class.getName())) {
				// filtro 1
				if (!campo.getSisFavoritoCampoFiltro1Compara().equals("")) {
					String[] valores = campo.getSisFavoritoCampoFiltro1Valor().split("¬");

					// list
					if (valores.length > 1) {
						GrupoFiltro gfs = new GrupoFiltro();
						for (String val : valores) {
							FiltroTexto ft = new FiltroTexto(campo.getSisFavoritoCampoNome(), ECompara.CONTEM, val);
							gfs.add(ft, EJuncao.OU);
						}
						gf.add(gfs);
					} // string ou binario
					else {
						FiltroTexto ft = new FiltroTexto();
						setFiltro(ft, campo.getSisFavoritoCampoNome(), campo.getSisFavoritoCampoFiltro1Compara(), campo.getSisFavoritoCampoFiltro1Valor());
						gf.add(ft, EJuncao.E);
					}
				}
			} // binario
			else if (campo.getSisFavoritoCampoTipo().equals(BooleanFieldDef.class.getName())) {
				// filtro 1
				if (!campo.getSisFavoritoCampoFiltro1Compara().equals("")) {
					FiltroBinario fb = new FiltroBinario();
					setFiltro(fb, campo.getSisFavoritoCampoNome(), campo.getSisFavoritoCampoFiltro1Compara(), campo.getSisFavoritoCampoFiltro1Valor());
					gf.add(fb, EJuncao.E);
				}
			} // data
			else if (campo.getSisFavoritoCampoTipo().equals(DateFieldDef.class.getName())) {
				// filtro 1
				if (!campo.getSisFavoritoCampoFiltro1Compara().equals("")) {
					FiltroData fd = new FiltroData();
					setFiltro(fd, campo.getSisFavoritoCampoNome(), campo.getSisFavoritoCampoFiltro1Compara(), campo.getSisFavoritoCampoFiltro1Valor());
					gf.add(fd, EJuncao.E);
				}
				// filtro 2
				if (!campo.getSisFavoritoCampoFiltro2Compara().equals("")) {
					FiltroData fd = new FiltroData();
					setFiltro(fd, campo.getSisFavoritoCampoNome(), campo.getSisFavoritoCampoFiltro2Compara(), campo.getSisFavoritoCampoFiltro2Valor());
					gf.add(fd, EJuncao.E);
				}
			}
		}

		if (!favorito.getSisFavoritoBusca().equals("")) {
			gf.add(navegacao.buscar(favorito.getSisFavoritoBusca()));
		}

		if (gf.size() > 0) {
			proxy.setFiltroFavorito(gf);
		}

		paginador.getTxtPag().setValue(favorito.getSisFavoritoPaginacao());
		paginador.setPageSize(favorito.getSisFavoritoPaginacao());
		getStore().setDefaultSort(favorito.getSisFavoritoOrdem(), SortDir.getValue(favorito.getSisFavoritoOrdemDirecao()));
		getStore().load(0, favorito.getSisFavoritoPaginacao());
	}

	@Override
	public ExpListagem<E> getExportacao() {
		List<ExpMeta> metadados = new ArrayList<ExpMeta>();

		for (int i = 0; i < modelos.getColumnCount(); i++) {
			if (modelos.isHidden(i)) {
				metadados.add(null);
			} else if (!modelos.getColumnHeader(i).startsWith("<div")) {
				ExpMeta meta = new ExpMeta(modelos.getColumnHeader(i), modelos.getColumnWidth(i), null);
				if (modelos.getColumnConfigs()[i] instanceof SummaryColumnConfig) {
					SummaryColumnConfig col = (SummaryColumnConfig) modelos.getColumnConfigs()[i];
					String tp = col.getSummaryType().equals("average") ? "AVG" : col.getSummaryType().toUpperCase();
					meta.setGrupo(EBusca.getBusca(tp));
				}
				metadados.add(meta);
			}
		}

		SortState ordem = getStore().getSortState();
		Record rec = UtilClient.getRegistro(ANavegacao.FUNCOES, "classe", funcao.getSisFuncaoClasse());
		classe.setCampoOrdem(ordem.getField());
		classe.setOrdemDirecao(EDirecao.valueOf(ordem.getDirection().getDirection()));

		ExpListagem<E> expLista = new ExpListagem<E>();
		expLista.setClasse(classe);
		expLista.setMetadados(metadados);
		expLista.setNome(rec.getAsString("nome"));
		expLista.setFiltro(proxy.getFiltroTotal());

		return expLista;
	}

	@Override
	public void setExportacao(SisExpImp expimp, EModo modo, EModo modo2, AsyncCallback<String> async) {
		getPanel().getEl().mask(OpenSigCore.i18n.txtAguarde());
		ExportacaoProxy proxy = new ExportacaoProxy();

		// se diferentes trata como registro com filtro da listagem
		if (modo != modo2) {
			getPanel().getSelectionModel().selectFirstRow();
			ExpRegistro exp = form.getExportacao();
			exp.setFiltro(form.getLista().getProxy().getFiltroTotal());
			proxy.exportar(expimp, exp, async);
		} else if (modo == EModo.LISTAGEM) {
			proxy.exportar(expimp, form.getLista().getExportacao(), async);
		} else {
			proxy.exportar(expimp, form.getExportacao(), async);
		}
	}

	@Override
	public void setImportacao(SisExpImp modo) {
	}

	private void setFiltro(IFiltro filtro, String campo, String compara, String valor) {
		MatchResult mat = RegExp.compile("^t\\d+\\.").exec(campo);
		if (mat != null) {
			filtro.setCampoPrefixo("");
		}
		filtro.setCampo(campo);
		filtro.setCompara(ECompara.valueOf(compara));
		filtro.setValorString(valor);
	}

	@Override
	public boolean validarCampo(String valor, String tipo) {
		if (valor != null && !valor.equals("")) {
			valor = valor.replace("^t\\d*\\.", "");
			for (FieldDef def : campos.getFields()) {
				if (def.getName().equals(valor) && def.getClass().getName().equals(tipo)) {
					return true;
				}
			}
		}
		return false;
	}

	protected MenuItem getIrPara() {
		MenuItem mnu = new MenuItem();
		mnu.setText(OpenSigCore.i18n.txtIrPara());
		mnu.setTitle(OpenSigCore.i18n.msgIrPara());
		mnu.setIconCls("icon-avancar");
		return mnu;
	}

	protected MenuItem gerarFuncao(final SisFuncao sisFuncao, final String campoFiltro, final String campoId) {
		final Record rec = sisFuncao == null ? null : ANavegacao.getRegistro(ANavegacao.FUNCOES, sisFuncao.getSisFuncaoClasse());

		if (rec != null) {
			MenuItem item = new MenuItem();
			item.setText(rec.getAsString("nome"));
			item.setIconCls(rec.getAsString("imagem"));
			item.setTitle(rec.getAsString("descricao"));
			item.addListener(new BaseItemListenerAdapter() {
				public void onClick(BaseItem item, EventObject e) {
					TabPanel TAB = (TabPanel) Ponte.getCentro().getItem(sisFuncao.getSisFuncaoClasse() + "_tab");
					if (TAB != null) {
						Ponte.getCentro().remove(TAB);
					}

					IComando comando = FabricaComando.getInstancia().getComando(sisFuncao.getSisFuncaoClasse());
					int id = getSelectionModel().getSelected().getAsInteger(campoId);

					if (id > 0) {
						Map contexto = new HashMap();
						contexto.put("dados", sisFuncao);
						contexto.put("id", id);
						contexto.put("campo", campoFiltro);
						comando.execute(contexto);
					} else {
						MessageBox.alert(rec.getAsString("nome"), OpenSigCore.i18n.msgRegistro());
					}
				}
			});
			return item;
		} else {
			return null;
		}
	}

	@Override
	public boolean validarCampoData(String valor) {
		return validarCampo(valor, DateFieldDef.class.getName());
	}

	@Override
	public boolean validarCampoTexto(String valor) {
		return validarCampo(valor, StringFieldDef.class.getName());
	}

	@Override
	public boolean validarCampoNumero(String valor) {
		return validarCampoInteiro(valor) || validarCampoDecimal(valor);
	}

	@Override
	public boolean validarCampoInteiro(String valor) {
		return validarCampo(valor, IntegerFieldDef.class.getName());
	}

	@Override
	public boolean validarCampoDecimal(String valor) {
		return validarCampo(valor, FloatFieldDef.class.getName());
	}

	@Override
	public boolean validarCampoBinario(String valor) {
		return validarCampo(valor, BooleanFieldDef.class.getName());
	}

	@Override
	public boolean isBarraTarefa() {
		return barraTarefa;
	}

	@Override
	public void setBarraTarefa(boolean barraTarefa) {
		this.barraTarefa = barraTarefa;
	}

	@Override
	public RecordDef getCampos() {
		return campos;
	}

	@Override
	public void setCampos(RecordDef campos) {
		this.campos = campos;
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
	public Paginador getPaginador() {
		return paginador;
	}

	@Override
	public void setPaginador(Paginador paginador) {
		this.paginador = paginador;
	}

	@Override
	public CoreProxy<E> getProxy() {
		return proxy;
	}

	@Override
	public void setProxy(CoreProxy<E> proxy) {
		this.proxy = proxy;
	}

	@Override
	public IFiltro getFiltroPadrao() {
		return filtroPadrao;
	}

	@Override
	public void setFiltroPadrao(IFiltro filtroPadrao) {
		this.filtroPadrao = filtroPadrao;
	}

	@Override
	public void setFiltroExtra(IFiltro filtroExtra) {
		proxy.setFiltroExtra(filtroExtra);
	}

	@Override
	public IFiltro getFiltroExtra() {
		return proxy.getFiltroExtra();
	}

	@Override
	public Map<String, GridFilter> getFiltros() {
		return filtros;
	}

	@Override
	public void setFiltros(Map<String, GridFilter> filtros) {
		this.filtros = filtros;
	}

	@Override
	public IFormulario<E> getForm() {
		return form;
	}

	@Override
	public void setForm(IFormulario<E> form) {
		this.form = form;
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
	public ColumnModel getModelos() {
		return modelos;
	}

	@Override
	public void setModelos(ColumnModel modelos) {
		this.modelos = modelos;
	}

	@Override
	public boolean isAgrupar() {
		return agrupar;
	}

	@Override
	public void setAgrupar(boolean agrupar) {
		this.agrupar = agrupar;
	}

	@Override
	public boolean isPaginar() {
		return paginar;
	}

	@Override
	public void setPaginar(boolean paginar) {
		this.paginar = paginar;
	}

	@Override
	public Menu getMenu() {
		return menu;
	}

	@Override
	public void setMenu(Menu menu) {
		this.menu = menu;
	}

	@Override
	public NavegacaoLista<E> getNavegacao() {
		return navegacao;
	}

	@Override
	public void setNavegacao(NavegacaoLista<E> navegacao) {
		this.navegacao = navegacao;
	}

	@Override
	public GridPanel getPanel() {
		return this;
	}

	@Override
	public IComando AntesDaAcao(IComando comando) {
		return comando;
	}

	@Override
	public void DepoisDaAcao(IComando comando) {
	}

	@Override
	public IComando AntesDaFuncao(IComando comando, Map contexto) {
		return comando;
	}

	@Override
	public void DepoisDaFuncao(IComando comando, Map contexto) {
		if (contexto.get("id") != null) {
			String id = contexto.get("id").toString();
			String campo = contexto.get("campo").toString();

			for (Entry<String, GridFilter> entry : filtros.entrySet()) {
				if (entry.getKey().equals(campo)) {
					GridLongFilter filtro = (GridLongFilter) entry.getValue();
					filtro.setValueEquals(Integer.valueOf(id));
					filtro.setActive(true, false);
				} else {
					entry.getValue().setActive(false, true);
				}
			}
		}
	}
}
