package br.com.opensig.permissao.client.visao.form;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.UtilClient;
import br.com.opensig.core.client.controlador.comando.ComandoExecutar;
import br.com.opensig.core.client.controlador.comando.IComando;
import br.com.opensig.core.client.controlador.comando.form.ComandoCancelar;
import br.com.opensig.core.client.controlador.comando.form.ComandoSalvar;
import br.com.opensig.core.client.controlador.comando.lista.ComandoPermiteUsuario;
import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.EJuncao;
import br.com.opensig.core.client.controlador.filtro.FiltroBinario;
import br.com.opensig.core.client.controlador.filtro.FiltroNumero;
import br.com.opensig.core.client.controlador.filtro.FiltroObjeto;
import br.com.opensig.core.client.controlador.filtro.GrupoFiltro;
import br.com.opensig.core.client.controlador.filtro.IFiltro;
import br.com.opensig.core.client.servico.CoreProxy;
import br.com.opensig.core.client.visao.Arvore;
import br.com.opensig.core.client.visao.Ponte;
import br.com.opensig.core.client.visao.abstrato.AFormulario;
import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.core.shared.modelo.EComando;
import br.com.opensig.core.shared.modelo.Lista;
import br.com.opensig.core.shared.modelo.Sql;
import br.com.opensig.core.shared.modelo.sistema.SisAcao;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.core.shared.modelo.sistema.SisModulo;
import br.com.opensig.empresa.shared.modelo.EmpEmpresa;
import br.com.opensig.permissao.client.visao.NavegacaoAcesso;
import br.com.opensig.permissao.shared.modelo.SisGrupo;
import br.com.opensig.permissao.shared.modelo.SisPermissao;
import br.com.opensig.permissao.shared.modelo.SisUsuario;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.data.Node;
import com.gwtext.client.data.NodeTraversalCallback;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.layout.ColumnLayout;
import com.gwtext.client.widgets.layout.ColumnLayoutData;
import com.gwtext.client.widgets.tree.TreeNode;
import com.gwtext.client.widgets.tree.event.TreeNodeListener;
import com.gwtext.client.widgets.tree.event.TreeNodeListenerAdapter;

public class FormularioAcesso extends AFormulario<SisPermissao> {

	private Dados tipo;
	private List<SisPermissao> nPermissoes;
	private List<SisPermissao> vPermissoes;
	private Arvore treeTipo;
	private TreeNode nodeGrupo;
	private TreeNode nodeUsuario;
	private NavegacaoAcesso navAcesso;
	private Panel panTipo;
	private Panel panAcesso;

	private TreeNodeListener treeListener = new TreeNodeListenerAdapter() {

		public void onClick(Node node, EventObject e) {
			tipo = (Dados) node.getUserObject();

			if (tipo != null) {
				TreeNode tNode = (TreeNode) node;
				panAcesso.setTitle(OpenSigCore.i18n.txtAcesso() + " - " + tNode.getText());

				if (tipo instanceof SisGrupo) {
					vPermissoes = ((SisGrupo) tipo).getSisPermissoes();
					panTipo.setTitle(OpenSigCore.i18n.txtTipo() + " - " + OpenSigCore.i18n.txtGrupo());
				} else if (tipo instanceof SisUsuario) {
					vPermissoes = ((SisUsuario) tipo).getSisPermissoes();
					panTipo.setTitle(OpenSigCore.i18n.txtTipo() + " - " + OpenSigCore.i18n.txtUsuario());
				}
			}
			checaModulos();
		}
	};

	public FormularioAcesso(SisFuncao funcao) {
		super(new SisPermissao(), funcao);
		inicializar();
	}

	public void inicializar() {
		super.inicializar();
		enable();

		nodeGrupo = new TreeNode(OpenSigCore.i18n.txtGrupo());
		nodeGrupo.setExpandable(true);
		nodeGrupo.addListener(treeListener);
		nodeGrupo.addListener(new TreeNodeListenerAdapter() {
			public void onExpand(Node node) {
				if (node.getChildNodes().length == 0) {
					carregaGrupos();
				}
			}
		});

		nodeUsuario = new TreeNode(OpenSigCore.i18n.txtUsuario());
		nodeUsuario.setExpandable(true);
		nodeUsuario.addListener(treeListener);
		nodeUsuario.addListener(new TreeNodeListenerAdapter() {
			public void onExpand(Node node) {
				if (node.getChildNodes().length == 0) {
					carregaUsuarios();
				}
			}
		});

		navAcesso = new NavegacaoAcesso();

		treeTipo = new Arvore();
		treeTipo.getRoot().appendChild(nodeGrupo);
		treeTipo.getRoot().appendChild(nodeUsuario);
		treeTipo.setFiltrar(true);
		treeTipo.setExpandir(true);
		treeTipo.setHeight(375);
		treeTipo.inicializar();

		panTipo = new Panel(OpenSigCore.i18n.txtTipo());
		panTipo.setHeight(400);
		panTipo.setWidth(300);
		panTipo.add(treeTipo);

		panAcesso = new Panel(OpenSigCore.i18n.txtAcesso());
		panAcesso.setHeight(400);
		panAcesso.setWidth(300);
		panAcesso.add(navAcesso.getTreeAcesso());

		Panel separador = new Panel();
		separador.setBorder(false);
		separador.setHeader(false);
		separador.setHeight(400);

		setLayout(new ColumnLayout());
		add(panTipo, new ColumnLayoutData(.4));
		add(separador, new ColumnLayoutData(.2));
		add(panAcesso, new ColumnLayoutData(.4));

		new Timer() {
			public void run() {
				carregaModulos();
			}
		}.schedule(100);
	}

	private void carregaGrupos() {
		treeTipo.getEl().mask(OpenSigCore.i18n.txtAguarde());

		for (Node node : nodeGrupo.getChildNodes()) {
			nodeGrupo.removeChild(node);
		}

		GrupoFiltro gf = new GrupoFiltro();
		FiltroObjeto fo = new FiltroObjeto("empEmpresa", ECompara.IGUAL, new EmpEmpresa(Ponte.getLogin().getEmpresaId()));
		gf.add(fo, EJuncao.E);

		FiltroBinario fb1 = new FiltroBinario("sisGrupoAtivo", ECompara.IGUAL, 1);
		gf.add(fb1, EJuncao.E);

		if (UtilClient.getAcaoPermitida(funcao, ComandoPermiteUsuario.class) == null) {
			FiltroBinario fb2 = new FiltroBinario("sisGrupoSistema", ECompara.IGUAL, 0);
			gf.add(fb2);
		}

		CoreProxy<SisGrupo> proxy = new CoreProxy<SisGrupo>(new SisGrupo());
		proxy.selecionar(gf, new AsyncCallback<Lista<SisGrupo>>() {

			public void onFailure(Throwable caught) {
				MessageBox.alert(OpenSigCore.i18n.txtGrupo(), OpenSigCore.i18n.errRegistro());
				treeTipo.getEl().unmask();
			}

			public void onSuccess(Lista<SisGrupo> result) {
				for (String[] grp : result.getDados()) {
					SisGrupo grupo = SisGrupo.getGrupo(grp);
					TreeNode node = new TreeNode(grupo.getSisGrupoNome(), "icon-grupo");
					node.setUserObject(grupo);
					node.addListener(treeListener);
					nodeGrupo.appendChild(node);
				}
				treeTipo.getEl().unmask();
			}
		});
	}

	private void carregaUsuarios() {
		treeTipo.getEl().mask(OpenSigCore.i18n.txtAguarde());

		for (Node node : nodeUsuario.getChildNodes()) {
			nodeUsuario.removeChild(node);
		}

		GrupoFiltro gf = new GrupoFiltro();
		FiltroNumero fn = new FiltroNumero("empEmpresaId", ECompara.IGUAL, Ponte.getLogin().getEmpresaId());
		fn.setCampoPrefixo("t1.");
		gf.add(fn, EJuncao.E);

		FiltroBinario fb1 = new FiltroBinario("sisUsuarioAtivo", ECompara.IGUAL, 1);
		gf.add(fb1, EJuncao.E);

		if (UtilClient.getAcaoPermitida(funcao, ComandoPermiteUsuario.class) == null) {
			FiltroBinario fb2 = new FiltroBinario("sisUsuarioSistema", ECompara.IGUAL, 0);
			gf.add(fb2);
		}

		CoreProxy<SisUsuario> proxy = new CoreProxy<SisUsuario>(new SisUsuario());
		proxy.selecionar(gf, new AsyncCallback<Lista<SisUsuario>>() {

			public void onFailure(Throwable caught) {
				MessageBox.alert(OpenSigCore.i18n.txtUsuario(), OpenSigCore.i18n.errRegistro());
				treeTipo.getEl().unmask();
			}

			public void onSuccess(Lista<SisUsuario> result) {
				for (String[] usu : result.getDados()) {
					SisUsuario usuario = SisUsuario.getUsuario(usu);
					TreeNode node = new TreeNode(usuario.getSisUsuarioLogin(), "icon-usuario");
					node.setUserObject(usuario);
					node.addListener(treeListener);
					nodeUsuario.appendChild(node);
				}
				treeTipo.getEl().unmask();
			}
		});
	}

	private void carregaModulos() {
		List<SisModulo> modulos = Ponte.getLogin().getModulos();
		for (SisModulo sisModulo : modulos) {
			for (SisFuncao sisFuncao : sisModulo.getSisFuncoes()) {
				filtraAcoes(sisFuncao);
			}
		}

		navAcesso.visit(modulos.toArray(new SisModulo[0]));
		navAcesso.getTreeAcesso().getEl().unmask();
	}

	private void filtraAcoes(SisFuncao sisFuncao) {
		Collection<Class> proibidas = Ponte.getAcoesProibidas(sisFuncao.getSisFuncaoClasse());
		if (proibidas != null) {
			for (Class acaoClasse : proibidas) {
				for (SisAcao acao : sisFuncao.getSisAcoes()) {
					if (acao.getSisAcaoClasse().equals(acaoClasse.getName())) {
						sisFuncao.getSisAcoes().remove(acao);
						break;
					}
				}
			}
		}
	}

	private void checaModulos() {
		navAcesso.getTreeAcesso().getEl().mask(OpenSigCore.i18n.txtAguarde());
		navAcesso.getTreeAcesso().getRootNode().cascade(new NodeTraversalCallback() {

			public boolean execute(Node node) {
				Dados dados = (Dados) node.getUserObject();
				TreeNode tNode = (TreeNode) node;
				tNode.getUI().toggleCheck(false);

				if (tipo != null) {
					if (dados instanceof SisModulo) {
						for (SisPermissao sisPermissao : vPermissoes) {
							if (sisPermissao.getSisModuloId() == dados.getId().intValue()) {
								tNode.getUI().toggleCheck(true);
								tNode.expand();
								break;
							}
						}
					} else if (dados instanceof SisFuncao) {
						Node funcao = node.getParentNode().getUserObject() instanceof SisFuncao ? node.getParentNode() : node;
						Node modulo = funcao.getParentNode();
						int idModulo = ((SisModulo) modulo.getUserObject()).getSisModuloId();

						for (SisPermissao sisPermissao : vPermissoes) {
							if (sisPermissao.getSisFuncaoId() == dados.getId().intValue() || (sisPermissao.getSisModuloId() == idModulo && sisPermissao.getSisFuncaoId() == -1)) {
								tNode.getUI().toggleCheck(true);
								tNode.expand();
								break;
							}
						}
					} else if (dados instanceof SisAcao) {
						Node acao = node.getParentNode().getUserObject() instanceof SisAcao ? node.getParentNode() : node;
						Node funcao = acao.getParentNode();
						Node modulo = funcao.getParentNode();
						int idFuncao = ((SisFuncao) funcao.getUserObject()).getSisFuncaoId();
						int idModulo = ((SisModulo) modulo.getUserObject()).getSisModuloId();
						tNode.setAttribute("executar", false);

						for (SisPermissao sisPermissao : vPermissoes) {
							if (sisPermissao.getSisModuloId() == idModulo && (sisPermissao.getSisFuncaoId() == idFuncao || sisPermissao.getSisFuncaoId() == -1)
									&& (sisPermissao.getSisAcaoId() == dados.getId().intValue() || sisPermissao.getSisAcaoId() == -1)) {
								tNode.getUI().toggleCheck(true);
								tNode.expand();
								tNode.setAttribute("executar", sisPermissao.getSisExecutar());
								break;
							}
						}
					}
				}

				navAcesso.getTreeAcesso().getEl().unmask();
				return true;
			}
		});
	}

	public boolean setDados() {
		nPermissoes = new ArrayList<SisPermissao>();
		int moduloId = 0;
		int funcaoId = 0;

		for (TreeNode node : navAcesso.getTreeAcesso().getChecked()) {
			Dados obj = (Dados) node.getUserObject();

			if (obj instanceof SisModulo) {
				moduloId = obj.getId().intValue();
			} else if (obj instanceof SisFuncao) {
				funcaoId = obj.getId().intValue();
			} else if (obj instanceof SisAcao) {
				SisPermissao permissao = new SisPermissao();
				String executar = node.getAttribute("executar");
				boolean exec = executar == null ? false : executar.equalsIgnoreCase("true");
				permissao.setSisModuloId(moduloId);
				permissao.setSisFuncaoId(funcaoId);
				permissao.setSisAcaoId(obj.getId().intValue());
				permissao.setSisExecutar(exec);
				if (tipo instanceof SisGrupo) {
					permissao.setSisGrupo((SisGrupo) tipo);
				} else {
					permissao.setSisUsuario((SisUsuario) tipo);
				}
				nPermissoes.add(permissao);
			}
		}

		return tipo != null;
	}

	@Override
	public IComando AntesDaAcao(IComando comando) {
		if (comando instanceof ComandoSalvar) {
			// salva se tem permissoes
			ComandoSalvar<SisPermissao> salvar = new ComandoSalvar<SisPermissao>();
			salvar.setRegistros(nPermissoes);

			// deleta as antigas funcoes que tem permissao e salva as novas
			String campo = tipo instanceof SisGrupo ? "sisGrupo" : "sisUsuario";
			FiltroObjeto fo = new FiltroObjeto(campo, ECompara.IGUAL, tipo);

			GrupoFiltro gFuncoes = new GrupoFiltro();
			for (SisModulo modulo : Ponte.getLogin().getModulos()) {
				for (SisFuncao funcao : modulo.getSisFuncoes()) {
					FiltroNumero fn = new FiltroNumero("sisFuncaoId", ECompara.IGUAL, funcao.getId());
					gFuncoes.add(fn, EJuncao.OU);
				}
			}

			GrupoFiltro gf = new GrupoFiltro(EJuncao.E, new IFiltro[] { fo, gFuncoes });
			Sql sql = new Sql(new SisPermissao(), EComando.EXCLUIR, gf);
			comando = new ComandoExecutar<SisPermissao>(nPermissoes.size() > 0 ? salvar : salvar.getProximo(), new Sql[] { sql });
		} else if (comando instanceof ComandoCancelar) {
			tipo = null;
			limparDados();
			comando = null;
		}

		return comando;
	}

	@Override
	public void DepoisDaAcao(IComando comando) {
		getPanel().enable();
	}

	public void limparDados() {
		if (tipo instanceof SisGrupo) {
			for (Node node : nodeGrupo.getChildNodes()) {
				if (tipo.equals(node.getUserObject())) {
					SisGrupo g = (SisGrupo) node.getUserObject();
					g.setSisPermissoes(nPermissoes);
					node.setUserObject(g);
					((TreeNode) node).unselect();
					break;
				}
			}
		} else if (tipo instanceof SisUsuario) {
			for (Node node : nodeUsuario.getChildNodes()) {
				if (tipo.equals(node.getUserObject())) {
					SisUsuario u = (SisUsuario) node.getUserObject();
					u.setSisPermissoes(nPermissoes);
					node.setUserObject(u);
					((TreeNode) node).unselect();
					break;
				}
			}
		}

		// limpando
		tipo = null;
		treeTipo.collapseAll();
		panTipo.setTitle(OpenSigCore.i18n.txtTipo());

		navAcesso.getTreeAcesso().collapseAll();
		panAcesso.setTitle(OpenSigCore.i18n.txtAcesso());
		checaModulos();
	}

	public void gerarListas() {
	}

	public void mostrarDados() {
	}

	public Dados getTipo() {
		return tipo;
	}

	public void setTipo(Dados tipo) {
		this.tipo = tipo;
	}

	public Panel getPanTipo() {
		return panTipo;
	}

	public void setPanTipo(Panel panTipo) {
		this.panTipo = panTipo;
	}

	public Panel getPanAcesso() {
		return panAcesso;
	}

	public void setPanAcesso(Panel panAcesso) {
		this.panAcesso = panAcesso;
	}

	public List<SisPermissao> getnPermissoes() {
		return nPermissoes;
	}

	public void setnPermissoes(List<SisPermissao> nPermissoes) {
		this.nPermissoes = nPermissoes;
	}

	public List<SisPermissao> getvPermissoes() {
		return vPermissoes;
	}

	public void setvPermissoes(List<SisPermissao> vPermissoes) {
		this.vPermissoes = vPermissoes;
	}

	public Arvore getTreeTipo() {
		return treeTipo;
	}

	public void setTreeTipo(Arvore treeTipo) {
		this.treeTipo = treeTipo;
	}

	public TreeNode getNodeGrupo() {
		return nodeGrupo;
	}

	public void setNodeGrupo(TreeNode nodeGrupo) {
		this.nodeGrupo = nodeGrupo;
	}

	public TreeNode getNodeUsuario() {
		return nodeUsuario;
	}

	public void setNodeUsuario(TreeNode nodeUsuario) {
		this.nodeUsuario = nodeUsuario;
	}

	public NavegacaoAcesso getNavAcesso() {
		return navAcesso;
	}

	public void setNavAcesso(NavegacaoAcesso navAcesso) {
		this.navAcesso = navAcesso;
	}

	public TreeNodeListener getTreeListener() {
		return treeListener;
	}

	public void setTreeListener(TreeNodeListener treeListener) {
		this.treeListener = treeListener;
	}
}
