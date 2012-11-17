package br.com.opensig.client.visao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.opensig.client.visao.layout.Modulos;
import br.com.opensig.client.visao.layout.Usuario;
import br.com.opensig.core.client.controlador.comando.FabricaComando;
import br.com.opensig.core.client.controlador.comando.IComando;
import br.com.opensig.core.client.visao.Arvore;
import br.com.opensig.core.client.visao.Ponte;
import br.com.opensig.core.client.visao.abstrato.ANavegacao;
import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.core.shared.modelo.sistema.SisModulo;

import com.gwtext.client.core.EventObject;
import com.gwtext.client.data.Node;
import com.gwtext.client.data.Record;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.tree.TreeNode;
import com.gwtext.client.widgets.tree.event.TreeNodeListenerAdapter;

/**
 * Classe que representa a navegacao em tarefas dos modulos e funcoes do
 * sistema.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 */
public class NavegacaoTarefa extends ANavegacao {

	private Arvore treeTarefa;
	private Panel tarefas;
	private SisModulo[] modulos;
	private List<SisFuncao> agrupadas;

	/**
	 * Construtor padrao.
	 */
	public NavegacaoTarefa() {
		this.agrupadas = new ArrayList<SisFuncao>();
		this.tarefas = Ponte.getEsquerda().getItem(Modulos.getInstancia().getId());
		this.treeTarefa = new Arvore();
		treeTarefa.setWidth(180);
		treeTarefa.setFiltrar(true);
		treeTarefa.setExpandir(true);
		setStatus();
		treeTarefa.inicializar();
	}

	@Override
	public void visit(Dados[] dados) {
		if (dados instanceof SisModulo[]) {
			modulos = (SisModulo[]) dados;
			for (SisModulo modulo : modulos) {
				novoModulo(modulo);
			}

			tarefas.add(treeTarefa);
			tarefas.doLayout();
		}
	}

	// opcao da tarefa se status do sistema.
	private void setStatus() {
		Toolbar toolStatus = new Toolbar();
		toolStatus.addButton(new Usuario());
		treeTarefa.setBottomToolbar(toolStatus);
	}

	// cria um novo modulo em formato de tarefa
	private void novoModulo(final SisModulo sisModulo) {
		Record rec = getRegistro(MODULOS, sisModulo.getSisModuloClasse());

		if (rec != null) {
			TreeNode treeFuncao = new TreeNode(rec.getAsString("nome"));
			treeFuncao.setIconCls(rec.getAsString("imagem"));
			treeFuncao.setTooltip(rec.getAsString("descricao"));
			treeFuncao.setExpanded(true);

			SisFuncao anterior = null;
			for (SisFuncao funcao : sisModulo.getSisFuncoes()) {
				if (funcao.getSisFuncaoSubOrdem() > 0) {
					if (anterior != null && anterior.getSisFuncaoOrdem() != funcao.getSisFuncaoOrdem()) {
						gerarAgrupado(treeFuncao);
					}
					agrupadas.add(funcao);
				} else {
					gerarAgrupado(treeFuncao);
					TreeNode node = new TreeNode();
					if (gerarFuncao(node, funcao)) {
						treeFuncao.appendChild(node);
					}
				}
				anterior = funcao;
			}

			gerarAgrupado(treeFuncao);
			treeTarefa.getRoot().appendChild(treeFuncao);
		}
	}

	// cria uma nova funcao sub-menu em formato de sub-tarefa
	private boolean gerarFuncao(TreeNode node, SisFuncao sisFuncao) {
		final Record rec = getRegistro(FUNCOES, sisFuncao.getSisFuncaoClasse());

		if (rec != null) {
			final IComando comando = FabricaComando.getInstancia().getComando(sisFuncao.getSisFuncaoClasse());
			final Map contexto = new HashMap();
			contexto.put("dados", sisFuncao);

			node.setText(rec.getAsString("nome"));
			node.setIconCls(rec.getAsString("imagem"));
			node.addListener(new TreeNodeListenerAdapter() {
				public void onClick(Node node, EventObject e) {
					comando.execute(contexto);
				}
			});
			node.setTooltip(rec.getAsString("descricao"));
			return true;
		} else {
			return false;
		}
	}

	// agrupa as funcoes nas sub-tarefas
	private void gerarAgrupado(TreeNode treeFuncao) {
		if (!agrupadas.isEmpty()) {
			SisFuncao pai = agrupadas.remove(0);

			// gerando um no
			TreeNode subTree = new TreeNode();
			if (gerarFuncao(subTree, pai)) {
				treeFuncao.appendChild(subTree);
			}

			// gerando o subNo
			for (SisFuncao funcao : agrupadas) {
				TreeNode node = new TreeNode();
				if (gerarFuncao(node, funcao)) {
					subTree.appendChild(node);
				}
			}

			agrupadas.clear();
		}
	}

	// Gets e Seteres

	public Arvore getTreeTarefa() {
		return treeTarefa;
	}

	public void setTreeTarefa(Arvore treeTarefa) {
		this.treeTarefa = treeTarefa;
	}

	public Panel getTarefas() {
		return tarefas;
	}

	public void setTarefas(Panel tarefas) {
		this.tarefas = tarefas;
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
