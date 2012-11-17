package br.com.opensig.permissao.client.visao.form;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.controlador.comando.IComando;
import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.EJuncao;
import br.com.opensig.core.client.controlador.filtro.FiltroBinario;
import br.com.opensig.core.client.controlador.filtro.FiltroData;
import br.com.opensig.core.client.controlador.filtro.FiltroNumero;
import br.com.opensig.core.client.controlador.filtro.FiltroObjeto;
import br.com.opensig.core.client.controlador.filtro.GrupoFiltro;
import br.com.opensig.core.client.controlador.filtro.IFiltro;
import br.com.opensig.core.client.servico.CoreProxy;
import br.com.opensig.core.client.visao.Arvore;
import br.com.opensig.core.client.visao.Paginador;
import br.com.opensig.core.client.visao.Ponte;
import br.com.opensig.core.client.visao.abstrato.AFormulario;
import br.com.opensig.core.client.visao.abstrato.IGrafico;
import br.com.opensig.core.shared.modelo.IFavorito;
import br.com.opensig.core.shared.modelo.Lista;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.empresa.shared.modelo.EmpEmpresa;
import br.com.opensig.permissao.shared.modelo.SisFavorito;
import br.com.opensig.permissao.shared.modelo.SisFavoritoCampo;
import br.com.opensig.permissao.shared.modelo.SisFavoritoGrafico;
import br.com.opensig.permissao.shared.modelo.SisGrupo;
import br.com.opensig.permissao.shared.modelo.SisUsuario;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.Ext;
import com.gwtext.client.data.BooleanFieldDef;
import com.gwtext.client.data.DateFieldDef;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.FloatFieldDef;
import com.gwtext.client.data.IntegerFieldDef;
import com.gwtext.client.data.Node;
import com.gwtext.client.data.SortState;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.form.TextArea;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.layout.AnchorLayoutData;
import com.gwtext.client.widgets.layout.ColumnLayout;
import com.gwtext.client.widgets.layout.ColumnLayoutData;
import com.gwtext.client.widgets.layout.FormLayout;
import com.gwtext.client.widgets.menu.Menu;
import com.gwtext.client.widgets.tree.TreeNode;
import com.gwtext.client.widgets.tree.event.TreeNodeListenerAdapter;

public class FormularioFavorito extends AFormulario<SisFavorito> {

	private TextField txtNome;
	private TextArea txtDescricao;
	private Arvore treeAcesso;
	private TreeNode nodeGrupo;
	private TreeNode nodeUsuario;
	private IGrafico<SisFavorito> grafico;

	public FormularioFavorito(SisFuncao funcao) {
		super(new SisFavorito(), funcao);
		inicializar();
	}

	public void inicializar() {
		super.inicializar();
		btnSalvar.setMenu(new Menu());
		enable();
		setHeader(false);

		Panel coluna1 = new Panel();
		coluna1.setBorder(false);
		coluna1.setLayout(new FormLayout());

		txtNome = new TextField(OpenSigCore.i18n.txtNome(), "sisFavoritoNome", 250);
		txtNome.setAllowBlank(false);
		txtNome.setMaxLength(50);
		coluna1.add(txtNome);

		txtDescricao = new TextArea(OpenSigCore.i18n.txtDescricao(), "sisFavoritoDescricao");
		txtDescricao.setAllowBlank(false);
		txtDescricao.setMaxLength(255);
		coluna1.add(txtDescricao, new AnchorLayoutData("90% 60%"));

		nodeGrupo = new TreeNode(OpenSigCore.i18n.txtGrupo());
		nodeGrupo.setExpandable(true);
		nodeGrupo.addListener(new TreeNodeListenerAdapter() {
			public void onExpand(Node node) {
				if (node.getChildNodes().length == 0) {
					carregaGrupos();
				}
			}
		});

		nodeUsuario = new TreeNode(OpenSigCore.i18n.txtUsuario());
		nodeUsuario.setExpandable(true);
		nodeUsuario.addListener(new TreeNodeListenerAdapter() {
			public void onExpand(Node node) {
				if (node.getChildNodes().length == 0) {
					carregaUsuarios();
				}
			}
		});

		treeAcesso = new Arvore();
		treeAcesso.getRoot().appendChild(nodeGrupo);
		treeAcesso.getRoot().appendChild(nodeUsuario);
		treeAcesso.setFiltrar(true);
		treeAcesso.setExpandir(true);
		treeAcesso.setHeight(200);
		treeAcesso.inicializar();

		Panel coluna2 = new Panel(OpenSigCore.i18n.txtAcesso());
		coluna2.setHeight(225);
		coluna2.setWidth(200);
		coluna2.setLayout(new FormLayout());
		coluna2.add(treeAcesso);

		Panel formColuna = new Panel();
		formColuna.setBorder(false);
		formColuna.setLayout(new ColumnLayout());
		formColuna.add(coluna1, new ColumnLayoutData(.6));
		formColuna.add(coluna2, new ColumnLayoutData(.4));
		add(formColuna);
	}

	@Override
	public void DepoisDaAcao(IComando comando) {
		Ponte.setDados((IFavorito) contexto.get("resultado"));
		getPanel().enable();
	}

	private void carregaGrupos() {
		treeAcesso.getEl().mask(OpenSigCore.i18n.txtAguarde());

		GrupoFiltro gf = new GrupoFiltro();
		FiltroObjeto fo = new FiltroObjeto("empEmpresa", ECompara.IGUAL, new EmpEmpresa(Ponte.getLogin().getEmpresaId()));
		gf.add(fo, EJuncao.E);

		FiltroBinario fb1 = new FiltroBinario("sisGrupoAtivo", ECompara.IGUAL, 1);
		gf.add(fb1, EJuncao.E);

		CoreProxy<SisGrupo> proxy = new CoreProxy<SisGrupo>(new SisGrupo());
		proxy.selecionar(gf, new AsyncCallback<Lista<SisGrupo>>() {

			public void onFailure(Throwable caught) {
				MessageBox.alert(OpenSigCore.i18n.txtGrupo(), OpenSigCore.i18n.errRegistro());
				treeAcesso.getEl().unmask();
			}

			public void onSuccess(Lista<SisGrupo> result) {
				for (String[] grp : result.getDados()) {
					SisGrupo grupo = SisGrupo.getGrupo(grp);
					TreeNode node = new TreeNode(grupo.getSisGrupoNome(), "icon-grupo");
					node.setChecked(false);
					node.setUserObject(grupo);
					nodeGrupo.appendChild(node);
				}
				treeAcesso.getEl().unmask();
			}
		});
	}

	private void carregaUsuarios() {
		treeAcesso.getEl().mask(OpenSigCore.i18n.txtAguarde());

		GrupoFiltro gf = new GrupoFiltro();
		FiltroNumero fn = new FiltroNumero("empEmpresaId", ECompara.IGUAL, Ponte.getLogin().getEmpresaId());
		fn.setCampoPrefixo("t1.");
		gf.add(fn, EJuncao.E);

		FiltroBinario fb1 = new FiltroBinario("sisUsuarioAtivo", ECompara.IGUAL, 1);
		gf.add(fb1, EJuncao.E);

		CoreProxy<SisUsuario> proxy = new CoreProxy<SisUsuario>(new SisUsuario());
		proxy.selecionar(gf, new AsyncCallback<Lista<SisUsuario>>() {

			public void onFailure(Throwable caught) {
				MessageBox.alert(OpenSigCore.i18n.txtUsuario(), OpenSigCore.i18n.errRegistro());
				treeAcesso.getEl().unmask();
			}

			public void onSuccess(Lista<SisUsuario> result) {
				for (String[] usu : result.getDados()) {
					SisUsuario usuario = SisUsuario.getUsuario(usu);
					TreeNode node = new TreeNode(usuario.getSisUsuarioLogin(), "icon-usuario");
					node.setUserObject(usuario);
					node.setChecked(false);
					nodeUsuario.appendChild(node);
				}
				treeAcesso.getEl().unmask();
			}
		});
	}

	public boolean setDados() {
		int pag = ((Paginador) lista.getPanel().getBottomToolbar()).getPageSize();
		SisUsuario usuario = new SisUsuario(Ponte.getLogin().getId());
		String busca = lista.getNavegacao().getTxtBusca().getValueAsString() == null ? "" : lista.getNavegacao().getTxtBusca().getValueAsString();
		SortState ordem = lista.getPanel().getStore().getSortState();

		List<SisFavoritoCampo> campos = new ArrayList<SisFavoritoCampo>();
		List<SisFavoritoGrafico> graficos = new ArrayList<SisFavoritoGrafico>();
		List<SisGrupo> grupos = new ArrayList<SisGrupo>();
		List<SisUsuario> usuarios = new ArrayList<SisUsuario>();

		for (FieldDef def : lista.getCampos().getFields()) {
			SisFavoritoCampo campo = new SisFavoritoCampo();
			campo.setSisFavoritoCampoNome(def.getName());
			campo.setSisFavoritoCampoTipo(def.getClass().getName());
			campo.setSisFavoritoCampoVisivel(!lista.getModelos().isHidden("col_" + def.getName()));
			campo.setSisFavorito(classe);
			campo.setSisFavoritoCampoFiltro1Compara("");
			campo.setSisFavoritoCampoFiltro2Compara("");
			campo.setSisFavoritoCampoFiltro1Valor("");
			campo.setSisFavoritoCampoFiltro2Valor("");
			setCampoFiltro(campo, lista.getProxy().getFiltroAtual().toArray());
			campos.add(campo);
		}

		if (grafico != null) {
			SisFavoritoGrafico graf = new SisFavoritoGrafico();
			graf.setSisFavorito(classe);
			graf.setSisFavoritoGraficoTipo(grafico.getEGrafico().toString());
			graf.setSisFavoritoGraficoX(grafico.getCmbCampoX().getValue());
			graf.setSisFavoritoGraficoSubx(grafico.getCmbCampoSubX().getValue());
			graf.setSisFavoritoGraficoY(grafico.getCmbCampoY().getValue());
			graf.setSisFavoritoGraficoData(grafico.getEData().toString());
			graf.setSisFavoritoGraficoBusca(grafico.getEValor().toString());
			graf.setSisFavoritoGraficoOrdem(grafico.getEOrdem().toString());
			graf.setSisFavoritoGraficoLimite(grafico.getTxtLimite().getValue().intValue());
			graficos.add(graf);
		}

		for (TreeNode node : treeAcesso.getChecked()) {
			if (node.getUserObject() instanceof SisGrupo) {
				grupos.add((SisGrupo) node.getUserObject());
			} else if (node.getUserObject() instanceof SisUsuario) {
				usuarios.add((SisUsuario) node.getUserObject());
			}
		}

		classe.setSisFavoritoId(0);
		classe.setSisUsuario(usuario);
		classe.setSisFavoritoNome(txtNome.getValueAsString());
		classe.setSisFavoritoDescricao(txtDescricao.getValueAsString());
		classe.setSisFuncao(funcao);
		classe.setSisFavoritoPaginacao(pag);
		classe.setSisFavoritoBusca(busca);
		classe.setSisFavoritoOrdem(ordem.getField());
		classe.setSisFavoritoOrdemDirecao(ordem.getDirection().getDirection());
		classe.setSisFavoritoGrafico(graficos);
		classe.setSisFavoritoCampos(campos);
		classe.setSisGrupos(grupos);
		classe.setSisUsuarios(usuarios);

		return true;
	}

	public void limparDados() {
		getForm().reset();
		treeAcesso.getTxtFiltro().setValue("");
		treeAcesso.collapseAll();

		for (Node node : nodeGrupo.getChildNodes()) {
			TreeNode tNode = (TreeNode) node;
			tNode.getUI().toggleCheck(false);
		}

		for (Node node : nodeUsuario.getChildNodes()) {
			TreeNode tNode = (TreeNode) node;
			tNode.getUI().toggleCheck(false);
		}

		Ext.getCmp("wndFavorito").hide();
	}

	public void gerarListas() {
	}

	public void mostrarDados() {
		txtNome.focus(true);
	}

	private void setCampoFiltro(SisFavoritoCampo campo, IFiltro[] filtros) {
		boolean filtro1 = true;

		try {
			for (IFiltro filtro : filtros) {
				if (filtro instanceof GrupoFiltro) {
					setCampoFiltro(campo, ((GrupoFiltro) filtro).toArray());
				} else if (campo.getSisFavoritoCampoNome().equals(filtro.getCampo())) {
					if (campo.getSisFavoritoCampoTipo().equals(IntegerFieldDef.class.getName()) || campo.getSisFavoritoCampoTipo().equals(FloatFieldDef.class.getName())) {
						if (filtro1) {
							campo.setSisFavoritoCampoFiltro1Compara(filtro.getCompara().name());
							campo.setSisFavoritoCampoFiltro1Valor(filtro.getValor().toString());
							filtro1 = false;
						} else {
							campo.setSisFavoritoCampoFiltro2Compara(filtro.getCompara().name());
							campo.setSisFavoritoCampoFiltro2Valor(filtro.getValor().toString());
							break;
						}
					} else if (campo.getSisFavoritoCampoTipo().equals(DateFieldDef.class.getName())) {
						FiltroData fd = (FiltroData) filtro;
						if (filtro1) {
							campo.setSisFavoritoCampoFiltro1Compara(filtro.getCompara().name());
							campo.setSisFavoritoCampoFiltro1Valor(DateTimeFormat.getFormat(PredefinedFormat.DATE_MEDIUM).format(fd.getValor()));
							filtro1 = false;
						} else {
							campo.setSisFavoritoCampoFiltro2Compara(filtro.getCompara().name());
							campo.setSisFavoritoCampoFiltro2Valor(DateTimeFormat.getFormat(PredefinedFormat.DATE_MEDIUM).format(fd.getValor()));
							break;
						}
					} else if (campo.getSisFavoritoCampoTipo().equals(StringFieldDef.class.getName()) || campo.getSisFavoritoCampoTipo().equals(BooleanFieldDef.class.getName())) {
						campo.setSisFavoritoCampoFiltro1Compara(filtro.getCompara().name());
						if (filtro.getValor() != null) {
							campo.setSisFavoritoCampoFiltro1Valor(filtro.getValor().toString().replace("%", ""));
						} else {
							StringBuffer sb = new StringBuffer();
							GrupoFiltro gf = (GrupoFiltro) filtro;

							for (Iterator<IFiltro> it2 = gf.iterator(); it2.hasNext();) {
								IFiltro fil = it2.next();
								sb.append(fil.getValor().toString());
								if (it2.hasNext()) {
									sb.append("¬");
								}
							}
							campo.setSisFavoritoCampoFiltro1Valor(sb.toString());
						}
						break;
					}
				}
			}
		} catch (Exception ex) {
		}
	}

	public TextField getTxtNome() {
		return txtNome;
	}

	public void setTxtNome(TextField txtNome) {
		this.txtNome = txtNome;
	}

	public TextArea getTxtDescricao() {
		return txtDescricao;
	}

	public void setTxtDescricao(TextArea txtDescricao) {
		this.txtDescricao = txtDescricao;
	}

	public Arvore getTreeAcesso() {
		return treeAcesso;
	}

	public void setTreeAcesso(Arvore treeAcesso) {
		this.treeAcesso = treeAcesso;
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

	public IGrafico<SisFavorito> getGrafico() {
		return grafico;
	}

	public void setGrafico(IGrafico<SisFavorito> grafico) {
		this.grafico = grafico;
	}
}