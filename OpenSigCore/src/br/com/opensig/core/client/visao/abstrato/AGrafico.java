package br.com.opensig.core.client.visao.abstrato;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.UtilClient;
import br.com.opensig.core.client.controlador.comando.AComando;
import br.com.opensig.core.client.controlador.comando.EModo;
import br.com.opensig.core.client.controlador.comando.FabricaComando;
import br.com.opensig.core.client.controlador.comando.IComando;
import br.com.opensig.core.client.controlador.comando.grafico.ComandoGrafico;
import br.com.opensig.core.client.controlador.comando.grafico.ComandoGraficoImagem;
import br.com.opensig.core.client.visao.grafico.AGraficoDados;
import br.com.opensig.core.client.visao.grafico.GraficoBarra;
import br.com.opensig.core.client.visao.grafico.GraficoLinha;
import br.com.opensig.core.client.visao.grafico.GraficoPizza;
import br.com.opensig.core.client.visao.grafico.IGraficoDados;
import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.core.shared.modelo.EBusca;
import br.com.opensig.core.shared.modelo.EData;
import br.com.opensig.core.shared.modelo.EDirecao;
import br.com.opensig.core.shared.modelo.EGrafico;
import br.com.opensig.core.shared.modelo.IFavoritoGrafico;
import br.com.opensig.core.shared.modelo.sistema.SisAcao;

import com.google.gwt.user.client.Timer;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.data.DateFieldDef;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.FloatFieldDef;
import com.gwtext.client.data.IntegerFieldDef;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.SimpleStore;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.event.StoreListenerAdapter;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.ToolTip;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.event.PanelListenerAdapter;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.NumberField;
import com.gwtext.client.widgets.form.event.ComboBoxListenerAdapter;
import com.gwtext.client.widgets.menu.BaseItem;
import com.gwtext.client.widgets.menu.CheckItem;
import com.gwtext.client.widgets.menu.Menu;
import com.gwtext.client.widgets.menu.event.MenuListenerAdapter;
import com.rednels.ofcgwt.client.ChartWidget;
import com.rednels.ofcgwt.client.model.ChartData;

/**
 * Classe que representa o modelo genérico dos gráficos do sistema.
 * 
 * @param <E>
 *            classe genérica de dados.
 * @author Pedro H. Lira
 * @version 1.0
 */

public abstract class AGrafico<E extends Dados> extends Panel implements IGrafico<E> {

	/**
	 * Classe do tipo que o grafico manipula.
	 */
	protected E classe;
	/**
	 * Listagem vinculada ao grafico.
	 */
	protected IListagem<E> lista;
	/**
	 * Conjunto de dados do grafico.
	 */
	protected IGraficoDados<E> dados;
	/**
	 * Tipo do grafico enumerado.
	 */
	protected EGrafico egrafico;
	/**
	 * O agrupamento de data usado.
	 */
	protected EData edata;
	/**
	 * O agrupamento de valor usado.
	 */
	protected EBusca evalor;
	/**
	 * A ordem dos dados usado.
	 */
	protected EDirecao eordem;
	/**
	 * Mapa de contexto da funcao.
	 */
	protected Map contexto;
	/**
	 * Objeto visual do grafico.
	 */
	protected ChartWidget grafico;
	/**
	 * Barra de tarefas.
	 */
	protected Toolbar toolGrafico;
	/**
	 * Botao que seleciona o tipo.
	 */
	protected ToolbarButton btnTipo;
	/**
	 * Botao que seleciona o grupo data.
	 */
	protected ToolbarButton btnParteData;
	/**
	 * Boato que seleciona o grupo valor.
	 */
	protected ToolbarButton btnGrupoValor;
	/**
	 * Botao que preenche os dados com valores e mostra no grafico.
	 */
	protected ToolbarButton btnAcao;
	/**
	 * Botao que exporta o grafico em imagem.
	 */
	protected ToolbarButton btnImagem;
	/**
	 * Campo que define o limite de dados mostrado.
	 */
	protected NumberField txtLimite;
	/**
	 * Combo que define o campo a ser usado no eixo X.
	 */
	protected ComboBox cmbCampoX;
	/**
	 * Combo que define o campo a ser usado como sub conjunto do eixo X.
	 */
	protected ComboBox cmbCampoSubX;
	/**
	 * Combo que define o campo a ser usado no eixo Y.
	 */
	protected ComboBox cmbCampoY;
	/**
	 * Valor do campo padrao.
	 */
	protected String valorCampo;
	/**
	 * Item do tipo pizza.
	 */
	protected CheckItem itemPizza;
	/**
	 * Item do tipo barra.
	 */
	protected CheckItem itemBarra;
	/**
	 * Item do tipo linha.
	 */
	protected CheckItem itemLinha;
	/**
	 * Item da data por dia.
	 */
	protected CheckItem itemDia;
	/**
	 * Item da data por mes.
	 */
	protected CheckItem itemMes;
	/**
	 * Item da data por ano.
	 */
	protected CheckItem itemAno;
	/**
	 * Item da agrupamento por soma.
	 */
	protected CheckItem itemSoma;
	/**
	 * Item da agrupamento por media.
	 */
	protected CheckItem itemMedia;
	/**
	 * Item da agrupamento por contagem.
	 */
	protected CheckItem itemTotal;
	/**
	 * Item da ordenacao ascendente.
	 */
	protected CheckItem itemAsc;
	/**
	 * Item da ordenacao descendente.
	 */
	protected CheckItem itemDesc;

	/**
	 * Construtor que recebe a classe de dados e a listagem vinculada.
	 * 
	 * @param classe
	 *            a classe do mesmo tipo setado com os dados.
	 * @param lista
	 *            a listagem vinculada ao grafico, usado para pegar os dados filtrados.
	 */
	public AGrafico(E classe, final IListagem<E> lista) {
		this.classe = classe;
		this.lista = lista;
		this.dados = new AGraficoDados<E>() {
			public void setData(ChartData data) {
			}
		};
		dados.setLista(lista);

		this.egrafico = EGrafico.PIZZA;
		this.edata = EData.MES;
		this.evalor = EBusca.SOMA;
		this.eordem = EDirecao.ASC;

		contexto = new HashMap();
		contexto.put("dados", classe);
		contexto.put("lista", lista);
		contexto.put("grafico", this);

		addListener(new PanelListenerAdapter() {
			public void onActivate(Panel panel) {
				gerarGrafico();
			}
		});
	}

	@Override
	public void inicializar() {
		// config padrao
		setTitle(OpenSigCore.i18n.txtGrafico(), "icon-grafico");
		setAutoScroll(true);
		setPaddings(5);
		setMargins(1);

		// menu superior
		toolGrafico = new Toolbar();
		toolGrafico.addButton(getTipo());
		toolGrafico.addSeparator();
		toolGrafico.addField(getCampoX());
		toolGrafico.addField(getCampoSubX());
		toolGrafico.addButton(getParteData());
		toolGrafico.addSeparator();
		toolGrafico.addField(getCampoY());
		toolGrafico.addButton(getGrupoValor());
		toolGrafico.addText(OpenSigCore.i18n.txtLimite());
		toolGrafico.addField(getLimite());
		toolGrafico.addSeparator();
		toolGrafico.addButton(getAcao());
		toolGrafico.addButton(getImagem());
		addFavorito();
		setTopToolbar(toolGrafico);

		// grafico
		grafico = new ChartWidget();
		grafico.setWidth("100%");
		add(grafico);
	}

	protected ToolbarButton getTipo() {
		// menu pizza
		itemPizza = new CheckItem();
		itemPizza.setText(OpenSigCore.i18n.txtPizza());
		itemPizza.setTitle(OpenSigCore.i18n.msgPizza());
		itemPizza.setChecked(true);
		itemPizza.setGroup("tipoGrafico");

		// menu barra
		itemBarra = new CheckItem();
		itemBarra.setText(OpenSigCore.i18n.txtBarra());
		itemBarra.setTitle(OpenSigCore.i18n.msgBarra());
		itemBarra.setGroup("tipoGrafico");

		// menu linha
		itemLinha = new CheckItem();
		itemLinha.setText(OpenSigCore.i18n.txtLinha());
		itemLinha.setTitle(OpenSigCore.i18n.msgLinha());
		itemLinha.setGroup("tipoGrafico");

		// menus do tipo de grafico
		Menu mnuTipo = new Menu();
		mnuTipo.addItem(itemPizza);
		mnuTipo.addItem(itemBarra);
		mnuTipo.addItem(itemLinha);
		mnuTipo.addListener(new MenuListenerAdapter() {
			public void onItemClick(BaseItem item, EventObject e) {
				if (itemPizza.getId().equals(item.getId())) {
					egrafico = EGrafico.PIZZA;
				} else if (itemBarra.getId().equals(item.getId())) {
					egrafico = EGrafico.BARRA;
				} else {
					egrafico = EGrafico.LINHA;
				}

				if (egrafico == EGrafico.LINHA && !lista.validarCampoData(cmbCampoX.getValue())) {
					cmbCampoSubX.enable();
				} else {
					cmbCampoSubX.disable();
					cmbCampoSubX.setValue("");
				}

				if (lista.validarCampoData(cmbCampoX.getValue())) {
					btnParteData.enable();
					txtLimite.disable();
					txtLimite.setValue(0);
				} else {
					btnParteData.disable();
					txtLimite.enable();
				}

				gerarGrafico();
			}
		});

		btnTipo = new ToolbarButton();
		btnTipo.setText(OpenSigCore.i18n.txtTipo());
		btnTipo.setIconCls("icon-grafico");
		btnTipo.setMenu(mnuTipo);

		return btnTipo;
	}

	protected ToolbarButton getParteData() {
		// menu dia
		itemDia = new CheckItem();
		itemDia.setText(OpenSigCore.i18n.txtDia());
		itemDia.setGroup("parteData");

		// menu mes
		itemMes = new CheckItem();
		itemMes.setText(OpenSigCore.i18n.txtMes());
		itemMes.setGroup("parteData");
		itemMes.setChecked(true);

		// menu ano
		itemAno = new CheckItem();
		itemAno.setText(OpenSigCore.i18n.txtAno());
		itemAno.setGroup("parteData");

		// menus parte
		Menu mnuParteData = new Menu();
		mnuParteData.addItem(itemDia);
		mnuParteData.addItem(itemMes);
		mnuParteData.addItem(itemAno);
		mnuParteData.addListener(new MenuListenerAdapter() {
			public void onItemClick(BaseItem item, EventObject e) {
				if (itemDia.getId().equals(item.getId())) {
					edata = EData.DIA;
				} else if (itemMes.getId().equals(item.getId())) {
					edata = EData.MES;
				} else {
					edata = EData.ANO;
				}
			}
		});

		btnParteData = new ToolbarButton();
		btnParteData.setText(OpenSigCore.i18n.txtData());
		btnParteData.setIconCls("icon-data");
		btnParteData.setTooltip(OpenSigCore.i18n.msgData());
		btnParteData.setMenu(mnuParteData);
		btnParteData.disable();

		return btnParteData;
	}

	protected ToolbarButton getGrupoValor() {
		// menu soma
		itemSoma = new CheckItem();
		itemSoma.setText(OpenSigCore.i18n.txtSoma());
		itemSoma.setGroup("grupo");
		itemSoma.setChecked(true);

		// menu media
		itemMedia = new CheckItem();
		itemMedia.setText(OpenSigCore.i18n.txtMedia());
		itemMedia.setGroup("grupo");

		// menu total
		itemTotal = new CheckItem();
		itemTotal.setText(OpenSigCore.i18n.txtContagem());
		itemTotal.setGroup("grupo");

		// menu asc
		itemAsc = new CheckItem();
		itemAsc.setText(EDirecao.ASC.toString());
		itemAsc.setGroup("ordem");
		itemAsc.setChecked(true);

		// menu desc
		itemDesc = new CheckItem();
		itemDesc.setText(EDirecao.DESC.toString());
		itemDesc.setGroup("ordem");

		// menus grupo
		Menu mnuGrupo = new Menu();
		mnuGrupo.addItem(itemSoma);
		mnuGrupo.addItem(itemMedia);
		mnuGrupo.addItem(itemTotal);
		mnuGrupo.addSeparator();
		mnuGrupo.addItem(itemAsc);
		mnuGrupo.addItem(itemDesc);
		mnuGrupo.addListener(new MenuListenerAdapter() {
			public void onItemClick(BaseItem item, EventObject e) {
				if (itemSoma.getId().equals(item.getId())) {
					evalor = EBusca.SOMA;
				} else if (itemMedia.getId().equals(item.getId())) {
					evalor = EBusca.MEDIA;
				} else if (itemTotal.getId().equals(item.getId())) {
					evalor = EBusca.CONTAGEM;
				} else if (itemAsc.getId().equals(item.getId())) {
					eordem = EDirecao.ASC;
				} else if (itemDesc.getId().equals(item.getId())) {
					eordem = EDirecao.DESC;
				}
			}
		});

		btnGrupoValor = new ToolbarButton();
		btnGrupoValor.setText(OpenSigCore.i18n.txtAgrupar());
		btnGrupoValor.setIconCls("icon-agrupamento");
		btnGrupoValor.setTooltip(OpenSigCore.i18n.msgAgrupar());
		btnGrupoValor.setMenu(mnuGrupo);

		return btnGrupoValor;
	}

	protected ToolbarButton getAcao() {
		btnAcao = new ToolbarButton(OpenSigCore.i18n.txtCarregar());
		btnAcao.setIconCls("icon-carregar");
		btnAcao.setTooltip(OpenSigCore.i18n.msgCarregar());
		btnAcao.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				gerarGrafico();
			}
		});

		return btnAcao;
	}

	protected ToolbarButton getImagem() {
		btnImagem = new ToolbarButton(OpenSigCore.i18n.txtImagem());
		btnImagem.setIconCls("icon-imagem");
		btnImagem.setTooltip(OpenSigCore.i18n.msgImagem());
		btnImagem.disable();
		btnImagem.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				exportarImagem();
			}
		});

		return btnImagem;
	}

	protected void addFavorito() {
		// favorito
		SisAcao acao = null;
		for (SisAcao a : lista.getFuncao().getSisAcoes()) {
			if (a.getSisAcaoClasse().contains("Favorito")) {
				acao = a;
				break;
			}
		}

		if (acao != null) {
			Record rec = ANavegacao.getRegistro(ANavegacao.ACOES, acao.getSisAcaoClasse());
			if (rec != null) {
				final IComando comando = FabricaComando.getInstancia().getComando(acao.getSisAcaoClasse());

				// barra de ações
				ToolbarButton btnFavorito = new ToolbarButton();
				btnFavorito.setText(rec.getAsString("nome"));
				btnFavorito.setIconCls(rec.getAsString("imagem"));
				btnFavorito.setTooltip(rec.getAsString("descricao"));
				btnFavorito.addListener(new ButtonListenerAdapter() {
					public void onClick(Button button, EventObject e) {
						lista.getContexto().put("acao", EModo.LISTAGEM);
						comando.execute(contexto);
					}
				});
				toolGrafico.addButton(btnFavorito);
			}
		}
	}

	@Override
	public void gerarGrafico() {
		if (validarFiltros()) {
			final IComando cmdGrafico = AntesDaAcao(new ComandoGrafico<E>());

			if (cmdGrafico != null) {
				// comando para ativar o depois
				IComando depois = new AComando<E>() {
					public void execute(Map contexto) {
						DepoisDaAcao(cmdGrafico);
					}
				};
				UtilClient.comandoFinal(cmdGrafico, depois);

				// executando
				cmdGrafico.execute(contexto);
			}
		} else {
			MessageBox.alert(OpenSigCore.i18n.txtCarregar(), OpenSigCore.i18n.errCarregar());
		}
	}

	@Override
	public void exportarImagem() {
		String imagem = grafico.getImageData();
		if (imagem == null) {
			MessageBox.alert(OpenSigCore.i18n.txtExportar(), OpenSigCore.i18n.errImagem());
		} else {
			contexto.put("imagem", grafico.getImageData());
			final IComando cmdGraficoImagem = AntesDaAcao(new ComandoGraficoImagem<E>());

			if (cmdGraficoImagem != null) {
				// comando para ativar o depois
				IComando depois = new AComando<E>() {
					public void execute(Map contexto) {
						DepoisDaAcao(cmdGraficoImagem);
					}
				};
				UtilClient.comandoFinal(cmdGraficoImagem, depois);

				// executando
				cmdGraficoImagem.execute(contexto);
			}
		}
	}

	@Override
	public void mostrarGrafico(Collection<String[]> resultado) {
		// verifica se o campoX é booleano
		if (lista.validarCampoBinario(getCmbCampoX().getValue())) {
			alterarValores(resultado);
		}

		IGraficoDados<E> tipoGrafico = null;

		// gerando grafico de acordo com o tip
		switch (egrafico) {
		case PIZZA:
			tipoGrafico = new GraficoPizza<E>(cmbCampoX.getValue(), edata, evalor);
			break;
		case BARRA:
			tipoGrafico = new GraficoBarra<E>(cmbCampoX.getValue(), cmbCampoY.getRawValue(), edata, evalor);
			break;
		case LINHA:
			tipoGrafico = new GraficoLinha<E>(cmbCampoX.getValue(), cmbCampoSubX.getValue(), cmbCampoY.getRawValue(), edata, evalor);
			break;
		}

		// criando a massa de dados do grafico
		final ChartData data = new ChartData(getTitulo());
		data.setBackgroundColour("#ffffff");
		data.setDecimalSeparatorComma(true);
		data.setFixedNumDecimalsForced(true);

		// setando o modo de dinheiro
		if (evalor == EBusca.CONTAGEM) {
			data.setNumDecimals(0);
		} else {
			data.setNumDecimals(2);
		}

		// inserindo os dados e no grafico
		tipoGrafico.setLista(lista);
		tipoGrafico.setDados(resultado);
		tipoGrafico.setLimite(txtLimite.getValue().intValue());
		tipoGrafico.setData(data);
		btnImagem.enable();

		new Timer() {
			public void run() {
				grafico.setChartData(data);
			}
		}.schedule(100);
	}

	@Override
	public String getTitulo() {
		// nome da listagem
		Record rec = ANavegacao.getRegistro(ANavegacao.FUNCOES, lista.getFuncao().getSisFuncaoClasse());

		// formacao padrao
		String titulo = rec.getAsString("nome") + " - " + evalor.name() + "[" + cmbCampoY.getRawValue() + "] / " + cmbCampoX.getRawValue();

		// caso campoX data
		if (lista.validarCampoData(cmbCampoX.getValue())) {
			titulo += "[" + edata.name() + "]";
			// caso campoSubX data
		} else if (lista.validarCampoData(cmbCampoSubX.getValue())) {
			titulo += " -> " + cmbCampoSubX.getRawValue() + "[" + edata.name() + "]";
		}

		// ordem
		titulo += " - " + OpenSigCore.i18n.txtOrdem() + "[" + eordem.name() + "]";

		// se filtrado
		if (lista.getProxy().getFiltroTotal() != null) {
			titulo += " *" + OpenSigCore.i18n.txtFiltros();
		}

		return titulo;
	}

	@Override
	public boolean validarFiltros() {
		return !cmbCampoX.getValue().equals(cmbCampoY.getValue()) && !cmbCampoX.getValue().equals(cmbCampoSubX.getValue()) && txtLimite.isValid();
	}

	protected ComboBox getCampoX() {
		cmbCampoX = getCampos("x");
		cmbCampoX.setEmptyText(OpenSigCore.i18n.txtCampo() + " X");
		cmbCampoX.addListener(new ComboBoxListenerAdapter() {
			public void onSelect(ComboBox comboBox, Record record, int index) {
				if (egrafico == EGrafico.LINHA && !lista.validarCampoData(cmbCampoX.getValue())) {
					cmbCampoSubX.enable();
				} else {
					cmbCampoSubX.disable();
					cmbCampoSubX.setValue("");
				}

				if (lista.validarCampoData(comboBox.getValue()) || lista.validarCampoData(cmbCampoSubX.getValue())) {
					btnParteData.enable();
					txtLimite.disable();
					txtLimite.setValue(0);
				} else {
					btnParteData.disable();
					txtLimite.enable();
				}
			}
		});

		ToolTip tolX = new ToolTip(OpenSigCore.i18n.msgEixoX());
		tolX.applyTo(cmbCampoX);
		return cmbCampoX;
	}

	protected ComboBox getCampoSubX() {
		cmbCampoSubX = getCampos("subx");
		cmbCampoSubX.disable();
		cmbCampoSubX.setValue("");
		cmbCampoSubX.addListener(new ComboBoxListenerAdapter() {
			public void onSelect(ComboBox comboBox, Record record, int index) {
				if (comboBox.getValue() == null) {
					btnParteData.disable();
				} else {
					btnParteData.enable();
				}
			}
		});

		ToolTip tolSubX = new ToolTip(OpenSigCore.i18n.msgEixoXSub());
		tolSubX.applyTo(cmbCampoSubX);
		return cmbCampoSubX;
	}

	protected ComboBox getCampoY() {
		cmbCampoY = getCampos("y");
		cmbCampoY.setEmptyText(OpenSigCore.i18n.txtCampo() + " Y");

		ToolTip tolSubY = new ToolTip(OpenSigCore.i18n.msgEixoY());
		tolSubY.applyTo(cmbCampoY);
		return cmbCampoY;
	}

	@Override
	public ComboBox getCampos(final String campo) {
		Store store = new SimpleStore(new String[] { "nome", "valor" }, getValoresCombo(campo));

		final ComboBox cmbCampos = new ComboBox();
		cmbCampos.setAllowBlank(false);
		cmbCampos.setEditable(false);
		cmbCampos.setForceSelection(true);
		cmbCampos.setDisplayField("nome");
		cmbCampos.setValueField("valor");
		cmbCampos.setMode(ComboBox.LOCAL);
		cmbCampos.setTriggerAction(ComboBox.ALL);
		cmbCampos.setSelectOnFocus(true);
		cmbCampos.setWidth(100);
		cmbCampos.setListWidth(200);
		cmbCampos.setStore(store);

		store.addStoreListener(new StoreListenerAdapter() {
			public void onLoad(Store store, Record[] records) {
				for (Record rec : records) {
					if (campo.equals("x")) {
						valorCampo = rec.getAsString("valor");
						cmbCampos.setValue(rec.getAsString("valor"));
						break;
					} else if (campo.equals("y") && !rec.getAsString("valor").equals(valorCampo)) {
						cmbCampos.setValue(rec.getAsString("valor"));
						break;
					}
				}
			}
		});
		store.load();

		return cmbCampos;
	}

	protected NumberField getLimite() {
		txtLimite = new NumberField();
		txtLimite.setAllowBlank(false);
		txtLimite.setAllowDecimals(false);
		txtLimite.setAllowNegative(false);
		txtLimite.setValue(10);
		txtLimite.setMinValue(0);
		txtLimite.setMaxValue(60);
		txtLimite.setWidth(30);

		ToolTip tip = new ToolTip(OpenSigCore.i18n.msgLimite());
		tip.applyTo(txtLimite);
		return txtLimite;
	}

	@Override
	public String[][] getValoresCombo(String combo) {
		List<String[]> valores = new ArrayList<String[]>();
		boolean permite;

		if (combo.equals("subx")) {
			valores.add(new String[] { OpenSigCore.i18n.txtSelecionar(), "" });
		}

		int diff = lista.getModelos().getColumnCount() - lista.getCampos().getFields().length;
		
		for (int i = 1; i < lista.getModelos().getColumnCount(); i++) {
			try {
				if (!(lista.getModelos().isHidden(i) && lista.getModelos().isFixed(i))) {
					FieldDef campo = lista.getCampos().getFields()[i - diff];
					permite = false;

					if (combo.equals("x")) {
						permite = true;
					} else if (combo.equals("subx") && campo instanceof DateFieldDef) {
						permite = true;
					} else if (combo.equals("y") && (campo instanceof IntegerFieldDef || campo instanceof FloatFieldDef)) {
						permite = true;
					}

					if (campo.getName().startsWith("Cod") || lista.getModelos().getColumnHeader(i).startsWith("Cod")) {
						permite = false;
					}

					if (permite) {
						String[] valor = new String[2];
						valor[0] = lista.getModelos().getColumnHeader(i);
						valor[1] = UtilClient.getCampoPrefixado(lista.getModelos().getDataIndex(i));
						valores.add(valor);
					}
				}
			} catch (Exception ex) {
				// nada o campo nao tem as propriedades
			}
		}

		return valores.toArray(new String[][] {});
	}

	@Override
	public void alterarValores(Collection<String[]> dados) {
		for (String[] valor : dados) {
			if (valor[0].equals("0")) {
				valor[0] = OpenSigCore.i18n.txtNao();
			} else {
				valor[0] = OpenSigCore.i18n.txtSim();
			}

			if (getCmbCampoSubX().getValue() == null) {
				valor[1] = valor[0];
			}
		}
	}

	@Override
	public void setFavorito(IFavoritoGrafico favorito) {
		egrafico = EGrafico.valueOf(favorito.getSisFavoritoGraficoTipo());
		cmbCampoX.setValue(favorito.getSisFavoritoGraficoX());
		cmbCampoSubX.setValue(favorito.getSisFavoritoGraficoSubx());
		cmbCampoY.setValue(favorito.getSisFavoritoGraficoY());
		edata = EData.valueOf(favorito.getSisFavoritoGraficoData());
		evalor = EBusca.getBusca(favorito.getSisFavoritoGraficoBusca());
		eordem = EDirecao.valueOf(favorito.getSisFavoritoGraficoOrdem());
		txtLimite.setValue(favorito.getSisFavoritoGraficoLimite());

		if (egrafico == EGrafico.BARRA) {
			itemPizza.setChecked(false);
			itemBarra.setChecked(true);
		} else if (egrafico == EGrafico.LINHA) {
			itemPizza.setChecked(false);
			itemLinha.setChecked(true);
		}

		if (edata == EData.DIA) {
			itemMes.setChecked(false);
			itemDia.setChecked(true);
		} else if (edata == EData.ANO) {
			itemMes.setChecked(false);
			itemAno.setChecked(true);
		}

		if (evalor == EBusca.MEDIA) {
			itemSoma.setChecked(false);
			itemMedia.setChecked(true);
		} else if (evalor == EBusca.CONTAGEM) {
			itemSoma.setChecked(false);
			itemTotal.setChecked(true);
		}

		if (eordem == EDirecao.DESC) {
			itemAsc.setChecked(false);
			itemDesc.setChecked(true);
		}
	}

	// Gets e Seteres

	@Override
	public Panel getPanel() {
		return this;
	}

	@Override
	public ChartWidget getGrafico() {
		return grafico;
	}

	@Override
	public void setGrafico(ChartWidget grafico) {
		this.grafico = grafico;
	}

	@Override
	public Toolbar getToolGrafico() {
		return toolGrafico;
	}

	@Override
	public void setToolGrafico(Toolbar toolGrafico) {
		this.toolGrafico = toolGrafico;
	}

	@Override
	public ToolbarButton getBtnTipo() {
		return btnTipo;
	}

	@Override
	public void setBtnTipo(ToolbarButton btnTipo) {
		this.btnTipo = btnTipo;
	}

	@Override
	public ComboBox getCmbCampoX() {
		return cmbCampoX;
	}

	@Override
	public void setCmbCampoX(ComboBox cmbCampoX) {
		this.cmbCampoX = cmbCampoX;
	}

	@Override
	public ComboBox getCmbCampoSubX() {
		return cmbCampoSubX;
	}

	@Override
	public void setCmbCampoSubX(ComboBox cmbCampoSubX) {
		this.cmbCampoSubX = cmbCampoSubX;
	}

	@Override
	public ComboBox getCmbCampoY() {
		return cmbCampoY;
	}

	@Override
	public void setCmbCampoY(ComboBox cmbCampoY) {
		this.cmbCampoY = cmbCampoY;
	}

	@Override
	public ToolbarButton getBtnParteData() {
		return btnParteData;
	}

	@Override
	public void setBtnParteData(ToolbarButton btnParteData) {
		this.btnParteData = btnParteData;
	}

	@Override
	public ToolbarButton getBtnGrupoValor() {
		return btnGrupoValor;
	}

	@Override
	public void setBtnGrupoValor(ToolbarButton btnGrupoValor) {
		this.btnGrupoValor = btnGrupoValor;
	}

	@Override
	public ToolbarButton getBtnAcao() {
		return btnAcao;
	}

	@Override
	public void setBtnAcao(ToolbarButton btnAcao) {
		this.btnAcao = btnAcao;
	}

	@Override
	public ToolbarButton getBtnImagem() {
		return btnImagem;
	}

	@Override
	public void setBtnImagem(ToolbarButton btnImagem) {
		this.btnImagem = btnImagem;
	}

	@Override
	public IListagem<E> getLista() {
		return lista;
	}

	@Override
	public void setLista(IListagem<E> lista) {
		this.lista = lista;
	}

	@Override
	public EGrafico getEGrafico() {
		return egrafico;
	}

	@Override
	public void setEGrafico(EGrafico egrafico) {
		this.egrafico = egrafico;
	}

	@Override
	public EData getEData() {
		return edata;
	}

	@Override
	public void setEData(EData edata) {
		this.edata = edata;
	}

	@Override
	public EBusca getEValor() {
		return evalor;
	}

	@Override
	public void setEValor(EBusca evalor) {
		this.evalor = evalor;
	}

	@Override
	public EDirecao getEOrdem() {
		return eordem;
	}

	@Override
	public void setEOrdem(EDirecao eordem) {
		this.eordem = eordem;
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
	public IGraficoDados<E> getDados() {
		return dados;
	}

	@Override
	public void setDados(IGraficoDados<E> dados) {
		this.dados = dados;
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
	public String getValorCampo() {
		return valorCampo;
	}

	@Override
	public void setValorCampo(String valorCampo) {
		this.valorCampo = valorCampo;
	}

	@Override
	public NumberField getTxtLimite() {
		return txtLimite;
	}

	@Override
	public void setTxtLimite(NumberField txtLimite) {
		this.txtLimite = txtLimite;
	}

	@Override
	public IComando AntesDaAcao(IComando comando) {
		return comando;
	}

	@Override
	public void DepoisDaAcao(IComando comando) {
	}
}
