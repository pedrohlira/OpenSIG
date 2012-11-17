package br.com.opensig.permissao.client.visao;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.visao.Arvore;
import br.com.opensig.core.client.visao.abstrato.ANavegacao;
import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.core.shared.modelo.sistema.SisAcao;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.core.shared.modelo.sistema.SisModulo;

import com.gwtext.client.core.EventObject;
import com.gwtext.client.data.Node;
import com.gwtext.client.data.Record;
import com.gwtext.client.util.Format;
import com.gwtext.client.widgets.menu.BaseItem;
import com.gwtext.client.widgets.menu.CheckItem;
import com.gwtext.client.widgets.menu.Item;
import com.gwtext.client.widgets.menu.Menu;
import com.gwtext.client.widgets.menu.event.BaseItemListenerAdapter;
import com.gwtext.client.widgets.menu.event.CheckItemListenerAdapter;
import com.gwtext.client.widgets.tree.TreeNode;
import com.gwtext.client.widgets.tree.event.TreePanelListenerAdapter;

public class NavegacaoAcesso extends ANavegacao {

	private Menu menu;
	private Menu mnuExec;
	private Arvore treeAcesso;
	private TreeNode nodeSelecionado;
	private SisModulo[] modulos;

	public NavegacaoAcesso() {
		menu = new Menu();
		Item mnuMarcar = new Item(OpenSigCore.i18n.txtMarcar());
		mnuMarcar.setIconCls("icon-marcar");
		mnuMarcar.addListener(new BaseItemListenerAdapter() {

			public void onClick(BaseItem item, EventObject e) {
				TreeNode node = nodeSelecionado;
				do {
					node.getUI().toggleCheck(true);
					node = (TreeNode) node.getParentNode();
				} while (node != null && node.getDepth() > 0);
				checarNode(nodeSelecionado, true);
			}
		});
		menu.addItem(mnuMarcar);

		Item mnuDesmarcar = new Item(OpenSigCore.i18n.txtDesmarcar());
		mnuDesmarcar.setIconCls("icon-desmarcar");
		mnuDesmarcar.addListener(new BaseItemListenerAdapter() {

			public void onClick(BaseItem item, EventObject e) {
				checarNode(nodeSelecionado, false);
			}
		});
		menu.addItem(mnuDesmarcar);

		mnuExec = new Menu();
		final CheckItem chkExecutar = new CheckItem(OpenSigCore.i18n.txtExecutar());
		chkExecutar.addListener(new CheckItemListenerAdapter() {
			public void onCheckChange(CheckItem item, boolean checked) {
				nodeSelecionado.setAttribute("executar", checked);
			}
		});
		mnuExec.addItem(chkExecutar);

		treeAcesso = new Arvore();
		treeAcesso.setFiltrar(true);
		treeAcesso.setExpandir(true);
		treeAcesso.setHeight(375);
		treeAcesso.inicializar();
		treeAcesso.addListener(new TreePanelListenerAdapter() {

			public void onCheckChange(TreeNode node, boolean checked) {
				node.setAttribute("executar", checked);
				if (checked) {
					do {
						node = (TreeNode) node.getParentNode();
						node.getUI().toggleCheck(true);
					} while (node.getDepth() > 0);
				} else {
					checarNode(node, checked);
				}
			}

			public void onContextMenu(TreeNode node, EventObject e) {
				node.select();
				nodeSelecionado = node;
				if (node.getUserObject() instanceof SisAcao) {
					String executar = nodeSelecionado.getAttribute("executar");
					boolean exec = executar == null ? false : executar.equalsIgnoreCase("true");
					chkExecutar.setChecked(exec);
					mnuExec.showAt(e.getXY());
				} else {
					menu.showAt(e.getXY());
				}
			}
		});

	}

	public void visit(Dados[] dados) {
		if (dados instanceof SisModulo[]) {
			modulos = (SisModulo[]) dados;
			treeAcesso.limpar();

			for (SisModulo modulo : modulos) {
				novoModulo(modulo);
			}
		}
	}

	private void checarNode(TreeNode node, boolean check) {
		String texto = Format.stripTags(node.getText());
		if (!treeAcesso.getBtnFiltro().isPressed() || texto.toLowerCase().indexOf(treeAcesso.getTxtFiltro().getValueAsString().toLowerCase()) != -1) {
			node.getUI().toggleCheck(check);
			node.expand();

			if(check){
				((TreeNode) node.getParentNode()).getUI().toggleCheck(check);
			}
			
			if (node.getUserObject() instanceof SisAcao) {
				node.setAttribute("executar", check);
			}
		}
		
		for (Node filho : node.getChildNodes()) {
			checarNode((TreeNode) filho, check);
		}
	}

	private void novoModulo(SisModulo sisModulo) {
		Record rec = super.getRegistro(MODULOS, sisModulo.getSisModuloClasse());

		if (rec != null) {
			TreeNode nodeModulo = new TreeNode(rec.getAsString("nome"));
			nodeModulo.setChecked(false);
			nodeModulo.setUserObject(sisModulo);
			nodeModulo.setIconCls(rec.getAsString("imagem"));
			nodeModulo.setTooltip(rec.getAsString("descricao"));
			for (SisFuncao sis_funcao : sisModulo.getSisFuncoes()) {
				novaFuncao(nodeModulo, sis_funcao);
			}
			treeAcesso.getRoot().appendChild(nodeModulo);
		}
	}

	private void novaFuncao(TreeNode nodeModulo, SisFuncao sisFuncao) {
		Record rec = super.getRegistro(FUNCOES, sisFuncao.getSisFuncaoClasse());

		if (rec != null) {
			TreeNode nodeFuncao = new TreeNode(rec.getAsString("nome"));
			nodeFuncao.setChecked(false);
			nodeFuncao.setUserObject(sisFuncao);
			nodeFuncao.setIconCls(rec.getAsString("imagem"));
			nodeFuncao.setTooltip(rec.getAsString("descricao"));

			int ant = -1;
			TreeNode nodeAcao = null;
			for (SisAcao sis_acao : sisFuncao.getSisAcoes()) {
				if (ant != sis_acao.getSisAcaoOrdem()) {
					nodeAcao = novaAcao(nodeFuncao, sis_acao, null);
					ant = sis_acao.getSisAcaoOrdem();
				} else {
					novaAcao(nodeFuncao, sis_acao, nodeAcao);
				}
			}
			nodeModulo.appendChild(nodeFuncao);
		}
	}

	private TreeNode novaAcao(TreeNode nodeFuncao, SisAcao sisAcao, TreeNode anterior) {
		Record rec = super.getRegistro(ACOES, sisAcao.getSisAcaoClasse());

		if (rec != null) {
			TreeNode nodeAcao = new TreeNode(rec.getAsString("nome"));
			nodeAcao.setChecked(false);
			nodeAcao.setUserObject(sisAcao);
			nodeAcao.setIconCls(rec.getAsString("imagem"));
			nodeAcao.setTooltip(rec.getAsString("descricao"));

			if (anterior == null) {
				nodeFuncao.appendChild(nodeAcao);
			} else {
				anterior.appendChild(nodeAcao);
			}

			return nodeAcao;
		} else {
			return null;
		}
	}

	public Menu getMenu() {
		return menu;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}

	public Menu getMnuExec() {
		return mnuExec;
	}

	public void setMnuExec(Menu mnuExec) {
		this.mnuExec = mnuExec;
	}

	public Arvore getTreeAcesso() {
		return treeAcesso;
	}

	public void setTreeAcesso(Arvore treeAcesso) {
		this.treeAcesso = treeAcesso;
	}

	public TreeNode getNodeSelecionado() {
		return nodeSelecionado;
	}

	public void setNodeSelecionado(TreeNode nodeSelecionado) {
		this.nodeSelecionado = nodeSelecionado;
	}

	public SisModulo[] getModulos() {
		return modulos;
	}

	public void setModulos(SisModulo[] modulos) {
		this.modulos = modulos;
	}

}
