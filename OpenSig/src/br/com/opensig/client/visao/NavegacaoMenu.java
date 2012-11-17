package br.com.opensig.client.visao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.opensig.core.client.controlador.comando.FabricaComando;
import br.com.opensig.core.client.controlador.comando.IComando;
import br.com.opensig.core.client.visao.Ponte;
import br.com.opensig.core.client.visao.abstrato.ANavegacao;
import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.core.shared.modelo.sistema.SisModulo;

import com.gwtext.client.core.EventObject;
import com.gwtext.client.data.Record;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarMenuButton;
import com.gwtext.client.widgets.menu.BaseItem;
import com.gwtext.client.widgets.menu.Menu;
import com.gwtext.client.widgets.menu.MenuItem;
import com.gwtext.client.widgets.menu.event.BaseItemListenerAdapter;

/**
 * Classe que representa a navegacao em menus dos modulos e funcoes do sistema.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 */
public class NavegacaoMenu extends ANavegacao {

	private Toolbar tool;
	private SisModulo[] modulos;
	private List<SisFuncao> agrupadas;

	/**
	 * Construtor padrao.
	 */
	public NavegacaoMenu() {
		this.tool = Ponte.getBarraMenu();
		this.agrupadas = new ArrayList<SisFuncao>();
	}

	@Override
	public void visit(Dados[] dados) {
		if (dados instanceof SisModulo[]) {
			modulos = (SisModulo[]) dados;
			for (SisModulo modulo : modulos) {
				novoModulo(modulo);
			}
		}
	}

	// cria um novo modulo em formato de menu
	private void novoModulo(final SisModulo sisModulo) {
		Record rec = getRegistro(MODULOS, sisModulo.getSisModuloClasse());

		if (rec != null) {
			Menu menuFuncao = new Menu();
			SisFuncao anterior = null;

			for (SisFuncao funcao : sisModulo.getSisFuncoes()) {
				if (funcao.getSisFuncaoSubOrdem() > 0) {
					if (anterior != null && anterior.getSisFuncaoOrdem() != funcao.getSisFuncaoOrdem()) {
						gerarAgrupado(menuFuncao);
					}
					agrupadas.add(funcao);
				} else {
					gerarAgrupado(menuFuncao);
					MenuItem item = new MenuItem();
					if (gerarFuncao(item, funcao)) {
						menuFuncao.addItem(item);
					} else {
						menuFuncao.addSeparator();
					}
				}
				anterior = funcao;
			}

			gerarAgrupado(menuFuncao);
			ToolbarMenuButton menuModulo = new ToolbarMenuButton(rec.getAsString("nome"), menuFuncao);
			menuModulo.setIconCls(rec.getAsString("imagem"));
			menuModulo.setTooltip(rec.getAsString("descricao"));
			tool.addButton(menuModulo);
		}
	}

	// cria uma nova funcao em formato de sub-menu
	private boolean gerarFuncao(MenuItem item, SisFuncao sisFuncao) {
		Record rec = getRegistro(FUNCOES, sisFuncao.getSisFuncaoClasse());

		if (rec != null) {
			final IComando comando = FabricaComando.getInstancia().getComando(sisFuncao.getSisFuncaoClasse());
			final Map contexto = new HashMap();
			contexto.put("dados", sisFuncao);

			item.setText(rec.getAsString("nome"));
			item.setIconCls(rec.getAsString("imagem"));
			item.setTitle(rec.getAsString("descricao"));
			item.addListener(new BaseItemListenerAdapter() {
				public void onClick(BaseItem item, EventObject e) {
					comando.execute(contexto);
				}
			});
			return true;
		} else {
			return false;
		}
	}

	// agrupa as funcoes nos sub-menus
	private void gerarAgrupado(Menu menuFuncao) {
		if (!agrupadas.isEmpty()) {
			SisFuncao pai = agrupadas.remove(0);

			// gerando o subMenu
			Menu submenu = new Menu();
			for (SisFuncao funcao : agrupadas) {
				MenuItem item = new MenuItem();
				if (gerarFuncao(item, funcao)) {
					submenu.addItem(item);
				} else {
					submenu.addSeparator();
				}
			}

			// gerando um menu
			MenuItem mnuItem = new MenuItem("", submenu);
			if (gerarFuncao(mnuItem, pai)) {
				menuFuncao.addItem(mnuItem);
			} else {
				menuFuncao.addSeparator();
			}

			agrupadas.clear();
		}
	}

	// Gets e Seteres

	public Toolbar getTool() {
		return tool;
	}

	public void setTool(Toolbar tool) {
		this.tool = tool;
	}

	public SisModulo[] getModulos() {
		return modulos;
	}

	public void setModulos(SisModulo[] modulos) {
		this.modulos = modulos;
	}

	public List<SisFuncao> getAgrupadas() {
		return agrupadas;
	}

	public void setAgrupadas(List<SisFuncao> agrupadas) {
		this.agrupadas = agrupadas;
	}

}
