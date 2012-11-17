package br.com.opensig.core.client.visao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.UtilClient;
import br.com.opensig.core.client.controlador.comando.AComando;
import br.com.opensig.core.client.controlador.comando.EModo;
import br.com.opensig.core.client.controlador.comando.FabricaComando;
import br.com.opensig.core.client.controlador.comando.IComando;
import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.EJuncao;
import br.com.opensig.core.client.controlador.filtro.FiltroNumero;
import br.com.opensig.core.client.controlador.filtro.FiltroTexto;
import br.com.opensig.core.client.controlador.filtro.GrupoFiltro;
import br.com.opensig.core.client.controlador.filtro.IFiltro;
import br.com.opensig.core.client.visao.abstrato.ANavegacao;
import br.com.opensig.core.client.visao.abstrato.IListagem;
import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.core.shared.modelo.sistema.SisAcao;

import com.google.gwt.i18n.client.NumberFormat;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.FloatFieldDef;
import com.gwtext.client.data.IntegerFieldDef;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.ToolbarMenuButton;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.event.KeyListener;
import com.gwtext.client.widgets.form.TriggerField;
import com.gwtext.client.widgets.menu.BaseItem;
import com.gwtext.client.widgets.menu.Item;
import com.gwtext.client.widgets.menu.Menu;
import com.gwtext.client.widgets.menu.MenuItem;
import com.gwtext.client.widgets.menu.event.BaseItemListenerAdapter;

/**
 * Classe que representa a navegacao de acoes da listagem.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 */
public class NavegacaoLista<E extends Dados> extends ANavegacao {

	private IListagem lista;
	private TriggerField txtBusca;
	private Toolbar tool;
	private List<SisAcao> agrupadas;

	/**
	 * Construtor que recebe a lista atual para construcao das acoes.
	 * 
	 * @param lista
	 *            a lista que sera adicionado a toolbar com os mesnus.
	 */
	public NavegacaoLista(final IListagem lista) {
		this.lista = lista;
		this.tool = new Toolbar();
		this.agrupadas = new ArrayList<SisAcao>();

		txtBusca = new TriggerField() {
			protected void onTriggerClick(EventObject event) {
				lista.setFiltroExtra(buscar(txtBusca.getValueAsString()));
				lista.getPanel().getStore().reload();
			}
		};
		txtBusca.setSelectOnFocus(true);
		txtBusca.setEmptyText(OpenSigCore.i18n.txtPesquisar());
		txtBusca.setTitle(OpenSigCore.i18n.msgBuscaLivre());
		txtBusca.setWidth(100);
		txtBusca.setMaxLength(100);
		txtBusca.setTriggerClass("x-form-search-trigger");
		txtBusca.addKeyListener(EventObject.ENTER, new KeyListener() {
			public void onKey(int key, EventObject e) {
				lista.setFiltroExtra(buscar(txtBusca.getValueAsString()));
				lista.getPanel().getStore().reload();
			}
		});
	}

	@Override
	public void visit(Dados[] dados) {
		if (dados instanceof SisAcao[]) {
			SisAcao[] acoes = (SisAcao[]) dados;
			SisAcao anterior = null;

			for (SisAcao acao : acoes) {
				if (acao.getSisAcaoVisivel()) {
					if (acao.getSisAcaoSubOrdem() > 0) {
						if (anterior != null && anterior.getSisAcaoOrdem() != acao.getSisAcaoOrdem()) {
							gerarAgrupado();
						}
						agrupadas.add(acao);
					} else {
						gerarAgrupado();

						// gerando um botao na barra de tarefa
						ToolbarButton botao = new ToolbarButton();
						if (gerarBotao(acao, botao)) {
							tool.addButton(botao);
						} else {
							tool.addSeparator();
						}

						// gerando um item do menu de contexto
						Item menu = new Item();
						if (gerarItem(acao, menu, EModo.REGISTRO)) {
							lista.getMenu().addItem(menu);
						} else {
							lista.getMenu().addSeparator();
						}
					}
					anterior = acao;
				}
			}

			gerarAgrupado();
			tool.addFill();
			tool.addField(txtBusca);
			tool.setHeight(25);
			lista.getPanel().setTopToolbar(tool);
		}
	}

	private void gerarAgrupado() {
		if (!agrupadas.isEmpty()) {
			SisAcao pai = agrupadas.remove(0);
			final IComando comando = FabricaComando.getInstancia().getComando(pai.getSisAcaoClasse());

			// gerando um botao na barra de tarefa
			ToolbarMenuButton botao = new ToolbarMenuButton();
			gerarBotao(pai, botao);

			// gerando um menu no contexto
			MenuItem mnuItem = new MenuItem();
			mnuItem.setText(botao.getText());
			mnuItem.setIconCls(botao.getIconCls());
			mnuItem.setTitle(botao.getTooltip());
			mnuItem.addListener(new BaseItemListenerAdapter() {
				public void onClick(BaseItem item, EventObject e) {
					lista.getContexto().put("acao", EModo.REGISTRO);
					executarComando(comando);
				}
			});

			Menu mnuBotao = new Menu();
			Menu mnuContexto = new Menu();

			for (SisAcao acao : agrupadas) {
				// gerando um item do menu do botao
				Item btItem = new Item();
				if (gerarItem(acao, btItem, EModo.LISTAGEM)) {
					mnuBotao.addItem(btItem);
				} else {
					mnuBotao.addSeparator();
				}

				Item mnItem = new Item();
				if (gerarItem(acao, mnItem, EModo.REGISTRO)) {
					mnuContexto.addItem(mnItem);
				} else {
					mnuContexto.addSeparator();
				}
			}

			botao.setMenu(mnuBotao);
			tool.addButton(botao);

			mnuItem.setMenu(mnuContexto);
			lista.getMenu().addItem(mnuItem);

			agrupadas.clear();
		}
	}

	private boolean gerarBotao(SisAcao sisAcao, Button btnAcao) {
		Record rec = ANavegacao.getRegistro(ACOES, sisAcao.getSisAcaoClasse());
		if (rec != null) {
			final IComando comando = FabricaComando.getInstancia().getComando(sisAcao.getSisAcaoClasse());

			// barra de ações
			btnAcao.setText(rec.getAsString("nome"));
			btnAcao.setIconCls(rec.getAsString("imagem"));
			btnAcao.setTooltip(rec.getAsString("descricao"));
			btnAcao.addListener(new ButtonListenerAdapter() {
				public void onClick(Button button, EventObject e) {
					lista.getContexto().put("acao", EModo.LISTAGEM);
					executarComando(comando);
				}
			});
			return true;
		} else {
			return false;
		}
	}

	private boolean gerarItem(SisAcao sisAcao, Item mnuAcao, final EModo modo) {
		Record rec = ANavegacao.getRegistro(ACOES, sisAcao.getSisAcaoClasse());

		if (rec != null) {
			final IComando comando = FabricaComando.getInstancia().getComando(sisAcao.getSisAcaoClasse());

			// menu contexto
			mnuAcao.setText(rec.getAsString("nome"));
			mnuAcao.setIconCls(rec.getAsString("imagem"));
			mnuAcao.setTitle(rec.getAsString("descricao"));
			mnuAcao.addListener(new BaseItemListenerAdapter() {
				public void onClick(BaseItem item, EventObject e) {
					lista.getContexto().put("acao", modo);
					executarComando(comando);
				}
			});
			return true;
		} else {
			return false;
		}
	}

	private void executarComando(IComando comando) {
		final IComando cmd = lista.AntesDaAcao(comando);
		// chama o metodo antes da execucao
		if (cmd != null) {
			IComando cmdDepois = new AComando<E>() {
				public void execute(Map contexto) {
					lista.DepoisDaAcao(cmd);
				}
			};

			UtilClient.comandoFinal(cmd, cmdDepois);
			cmd.execute(lista.getContexto());
		}
	}

	/**
	 * Metodo que retorna o filtro da busca livre da listagema atual.
	 * 
	 * @param busca
	 *            o texto usado na busca livre.
	 * @return o filtro de acordo com os campos da listagem.
	 */
	public IFiltro buscar(String busca) {
		IFiltro filtro;
		if (txtBusca.getValueAsString() == null) {
			filtro = null;
		} else {
			try {
				Number numero = NumberFormat.getDecimalFormat().parse(busca);
				filtro = getFiltroNumero(numero, busca);
			} catch (Exception ex2) {
				filtro = getFiltroTexto(busca);
			}
		}
		return filtro;
	}

	private IFiltro getFiltroTexto(String texto) {
		GrupoFiltro gf = new GrupoFiltro();

		for (FieldDef campo : lista.getCampos().getFields()) {
			if (campo instanceof StringFieldDef) {
				FiltroTexto ft = new FiltroTexto(UtilClient.getCampoPrefixado(campo.getName()), ECompara.CONTEM, texto);
				ft.setCampoPrefixo("");
				gf.add(ft, EJuncao.OU);
			}
		}

		return gf;
	}

	private IFiltro getFiltroNumero(Number numero, String texto) {
		GrupoFiltro gf = new GrupoFiltro();

		for (FieldDef campo : lista.getCampos().getFields()) {
			if (campo instanceof FloatFieldDef || campo instanceof IntegerFieldDef) {
				FiltroNumero fn = new FiltroNumero(UtilClient.getCampoPrefixado(campo.getName()), ECompara.IGUAL, numero);
				fn.setCampoPrefixo("");
				gf.add(fn, EJuncao.OU);
			} else if (campo instanceof StringFieldDef) {
				FiltroTexto ft = new FiltroTexto(UtilClient.getCampoPrefixado(campo.getName()), ECompara.CONTEM, texto);
				ft.setCampoPrefixo("");
				gf.add(ft, EJuncao.OU);
			}
		}

		return gf;
	}

	// Gets e Seteres

	public Toolbar getTool() {
		return tool;
	}

	public void setTool(Toolbar tool) {
		this.tool = tool;
	}

	public TriggerField getTxtBusca() {
		return txtBusca;
	}

	public void setTxtBusca(TriggerField txtBusca) {
		this.txtBusca = txtBusca;
	}
}
