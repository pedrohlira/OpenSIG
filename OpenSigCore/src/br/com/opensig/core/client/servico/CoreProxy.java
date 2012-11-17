package br.com.opensig.core.client.servico;

import java.util.Collection;
import java.util.Date;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.UtilClient;
import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.EJuncao;
import br.com.opensig.core.client.controlador.filtro.FiltroBinario;
import br.com.opensig.core.client.controlador.filtro.FiltroData;
import br.com.opensig.core.client.controlador.filtro.FiltroNumero;
import br.com.opensig.core.client.controlador.filtro.FiltroTexto;
import br.com.opensig.core.client.controlador.filtro.GrupoFiltro;
import br.com.opensig.core.client.controlador.filtro.IFiltro;
import br.com.opensig.core.client.controlador.parametro.ParametroException;
import br.com.opensig.core.shared.modelo.Autenticacao;
import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.core.shared.modelo.EBusca;
import br.com.opensig.core.shared.modelo.EDirecao;
import br.com.opensig.core.shared.modelo.Lista;
import br.com.opensig.core.shared.modelo.Sql;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.gwtext.client.core.UrlParam;
import com.gwtext.client.widgets.MessageBox;
import com.gwtextux.client.data.GWTProxy;
import com.gwtextux.client.widgets.window.ToastWindow;

/**
 * Classe que padroniza a persistencia dos dados.
 * 
 * @param <E>
 *            a tipagem do objeto a ser persistido um POJO.
 * @author Pedro H. Lira
 * @version 1.0
 */
public class CoreProxy<E extends Dados> extends GWTProxy implements CoreServiceAsync<E> {

	private static final CoreServiceAsync async = (CoreServiceAsync) GWT.create(CoreService.class);
	private static final ServiceDefTarget sdf = (ServiceDefTarget) async;

	/**
	 * O objeto setado genericamente.
	 */
	protected E classe;
	/**
	 * O filtro padrao usado na listagem.
	 */
	protected IFiltro filtroPadrao;
	/**
	 * O filtro extra usado na listagem, a busca livre.
	 */
	protected IFiltro filtroExtra;
	/**
	 * O filtro de favorito, no caso da listagem ter origem de um filtro.
	 */
	protected IFiltro filtroFavorito;
	/**
	 * O filtro atual usado na listagem, retrata as acoes do usuario.
	 */
	protected GrupoFiltro filtroAtual;
	/**
	 * O filtro total, juntando todos os filtros ativos aplicados na listagem.
	 */
	protected GrupoFiltro filtroTotal;

	/**
	 * Construtor padrao.
	 */
	public CoreProxy() {
		this(null, null);
	}

	/**
	 * Construtor que recebe um objeto da mesma tipagem da classe.
	 * 
	 * @param classe
	 *            objeto tipado do mesmo tipo da classe.
	 */
	public CoreProxy(E classe) {
		this(classe, null);
	}

	/**
	 * Construtor que recebe um objeto da mesma tipagem da classe e o fitro
	 * padrao.
	 * 
	 * @param classe
	 *            objeto tipado do mesmo tipo da classe.
	 * @param filtroPadrao
	 *            filtro padrao a ser usado na listagem.
	 */
	public CoreProxy(E classe, IFiltro filtroPadrao) {
		sdf.setServiceEntryPoint(GWT.getHostPageBaseURL() + "CoreService");
		this.classe = classe;
		this.filtroPadrao = filtroPadrao;
	}

	/**
	 * Metodo que simplifica o acesso ao contar utilizando dados padrões.
	 * 
	 * @see CoreServiceAsync#buscar(Dados, String, EBusca, IFiltro,
	 *      AsyncCallback)
	 */
	public void buscar(EBusca busca, IFiltro filtro, String campo, AsyncCallback<Number> asyncCallback) {
		buscar(classe, campo, busca, filtro, asyncCallback);
	}

	@Override
	public void buscar(Dados classe, String campo, EBusca busca, IFiltro filtro, AsyncCallback<Number> asyncCallback) {
		async.buscar(classe, campo, busca, filtro, asyncCallback);
	}

	/**
	 * Metodo que simplifica o acesso ao contar utilizando dados padrões.
	 * 
	 * @see #buscar(Dados, String, String, String, String, EBusca, EDirecao, IFiltro, AsyncCallback)
	 */
	public void buscar(String campoX, String campoSubX, String grupoX, String campoY, EBusca busca, EDirecao direcao, IFiltro filtro, AsyncCallback<Collection<String[]>> asyncCallback) {
		buscar(classe, campoX, campoSubX, grupoX, campoY, busca, direcao, filtro, asyncCallback);
	}

	@Override
	public void buscar(Dados classe, String campoX, String campoSubX, String grupoX, String campoY, EBusca busca, EDirecao direcao, IFiltro filtro, AsyncCallback<Collection<String[]>> asyncCallback) {
		async.buscar(classe, campoX, campoSubX, grupoX, campoY, busca, direcao, filtro, asyncCallback);
	}

	/**
	 * Metodo que simplifica o acesso ao selecionar utilizando dados padrões.
	 * 
	 * @see #selecionar(Dados, int, int, IFiltro, boolean, AsyncCallback)
	 */
	public void selecionar(Number id, AsyncCallback<E> asyncCallback) {
		FiltroNumero fn = new FiltroNumero(classe.getCampoId(), ECompara.IGUAL, id);
		selecionar(fn, true, asyncCallback);
	}

	/**
	 * Metodo que simplifica o acesso ao selecionar utilizando dados padrões.
	 * 
	 * @see CoreServiceAsync#selecionar(Dados, int, int, IFiltro, boolean,
	 *      AsyncCallback)
	 */
	public void selecionar(IFiltro filtro, boolean dependencia, AsyncCallback<E> asyncCallback) {
		selecionar(classe, filtro, dependencia, asyncCallback);
	}

	@Override
	public void selecionar(Dados classe, IFiltro filtro, boolean dependencia, AsyncCallback<E> asyncCallback) {
		async.selecionar(classe, filtro, dependencia, asyncCallback);
	}

	/**
	 * Metodo que simplifica o acesso ao selecionar utilizando dados padrões.
	 * 
	 * @see CoreServiceAsync#selecionar(Dados, int, int, IFiltro, boolean,
	 *      AsyncCallback)
	 */
	public void selecionar(IFiltro filtro, AsyncCallback<Lista<E>> asyncCallback) {
		selecionar(0, 0, filtro, true, asyncCallback);
	}

	/**
	 * Metodo que simplifica o acesso ao selecionar utilizando dados padrões.
	 * 
	 * @see #selecionar(Dados, int, int, IFiltro, boolean, AsyncCallback)
	 */
	public void selecionar(int inicio, int total, IFiltro filtro, boolean dependencia, AsyncCallback<Lista<E>> asyncCallback) {
		selecionar(classe, inicio, total, filtro, dependencia, asyncCallback);
	}

	@Override
	public void selecionar(Dados classe, int inicio, int total, IFiltro filtro, boolean dependencia, AsyncCallback<Lista<E>> asyncCallback) {
		async.selecionar(classe, inicio, total, filtro, dependencia, asyncCallback);
	}

	@Override
	public void load(int start, int limit, String sort, String dir, JavaScriptObject o, UrlParam[] baseParams) {
		filtroAtual = new GrupoFiltro();
		int i = 1;

		if (baseParams != null && baseParams.length > 1) {
			if (baseParams[1].getName().equals("query")) {
				String valor = baseParams[1].getValue();
				IFiltro fil = new FiltroTexto(baseParams[0].getValue().replaceAll("__", "."), ECompara.CONTEM, valor);
				filtroAtual.add(fil, EJuncao.E);
			} else {
				while (i < baseParams.length) {
					try {
						if (baseParams[i].getName().endsWith("[type]")) {
							IFiltro fil = null;

							if (baseParams[i].getValue().equals("string")) {
								String valor = baseParams[i + 1].getValue();
								fil = new FiltroTexto(baseParams[i - 1].getValue(), ECompara.CONTEM, valor);
							} else if (baseParams[i].getValue().equals("numeric")) {
								double valor = Double.valueOf(baseParams[i + 2].getValue());
								String campo = baseParams[i - 1].getValue();
								ECompara comp = ECompara.toCompara(baseParams[i + 1].getValue());
								fil = new FiltroNumero(campo, comp, valor);
							} else if (baseParams[i].getValue().equals("long")) {
								String sValor = baseParams[i + 2].getValue();
								fil = new FiltroNumero(baseParams[i - 1].getValue(), ECompara.toCompara(baseParams[i + 1].getValue()), Long.valueOf(sValor));
							} else if (baseParams[i].getValue().equals("date")) {
								Date valor = DateTimeFormat.getFormat("MM/dd/yyyy HH:mm:ss").parse(baseParams[i + 2].getValue() + " 00:00:00");
								Date valor1 = DateTimeFormat.getFormat("MM/dd/yyyy HH:mm:ss").parse(baseParams[i + 2].getValue() + " 23:59:59");
								ECompara compara = ECompara.toCompara(baseParams[i + 1].getValue());

								if (compara == ECompara.IGUAL) {
									FiltroData fd1 = new FiltroData(baseParams[i - 1].getValue(), ECompara.MAIOR_IGUAL, valor);
									FiltroData fd2 = new FiltroData(baseParams[i - 1].getValue(), ECompara.MENOR_IGUAL, valor1);
									GrupoFiltro between = new GrupoFiltro(EJuncao.E, new IFiltro[] { fd1, fd2 });
									fil = between;
								} else if (compara == ECompara.MAIOR) {
									fil = new FiltroData(baseParams[i - 1].getValue(), compara, valor1);
								} else {
									fil = new FiltroData(baseParams[i - 1].getValue(), compara, valor);
								}
							} else if (baseParams[i].getValue().equals("boolean")) {
								int valor = Boolean.valueOf(baseParams[i + 1].getValue()) ? 1 : 0;
								fil = new FiltroBinario(baseParams[i - 1].getValue(), ECompara.IGUAL, valor);
							} else if (baseParams[i].getValue().equals("list")) {
								String[] valores = baseParams[i + 1].getValue().split(",");
								GrupoFiltro lista = new GrupoFiltro();

								for (String valor : valores) {
									FiltroTexto ft = new FiltroTexto(baseParams[i - 1].getValue(), ECompara.CONTEM, valor);
									lista.add(ft, EJuncao.OU);
								}
								fil = lista;
							}

							filtroAtual.add(fil, EJuncao.E);
						}
					} catch (Exception ex) {
						continue;
					} finally {
						i++;
					}
				}
			}

		}

		// seta os filtros
		filtroTotal = new GrupoFiltro();

		if (filtroPadrao != null) {
			filtroTotal.add(filtroPadrao, EJuncao.E);
		}

		if (filtroExtra != null) {
			filtroTotal.add(filtroExtra, EJuncao.E);
		}

		if (filtroFavorito != null) {
			filtroTotal.add(filtroFavorito, EJuncao.E);
		}

		if (filtroAtual.size() > 0) {
			filtroTotal.add(filtroAtual);
		}

		if (filtroTotal.size() == 0) {
			filtroTotal = null;
		}

		// arruma os parametros e chama o carregamento
		sort = sort == null ? classe.getCampoOrdem() : UtilClient.getCampoPrefixado(sort);
		dir = dir == null ? "ASC" : dir;
		start = start < 0 ? 0 : start;
		limit = limit < 0 ? 0 : limit;
		carregar(start, limit, sort, dir, o);
	}

	// faz o carregamento e avisa para recarregar
	private synchronized void carregar(final int start, final int limit, final String sort, final String dir, final JavaScriptObject o) {
		classe.setCampoOrdem(sort);
		classe.setOrdemDirecao(EDirecao.valueOf(dir));
		classe.setEmpresa(Integer.valueOf(UtilClient.CONF.get("login.empresa")));
		
		selecionar(classe, start, limit, filtroTotal, true, new AsyncCallback<Lista<E>>() {
			public void onFailure(Throwable caught) {
				if (caught instanceof ParametroException) {
					new ToastWindow(OpenSigCore.i18n.txtAtencao(), OpenSigCore.i18n.errFiltro()).show();
				} else {
					new ToastWindow(OpenSigCore.i18n.txtAtencao(), OpenSigCore.i18n.errListagem()).show();
				}

				MessageBox.hide();
				loadResponse(o, false, 0, (JavaScriptObject) null);
			}

			public void onSuccess(Lista<E> result) {
				MessageBox.hide();
				loadResponse(o, true, result.getTotal(), result.getDados());
			}
		});
	}

	@Override
	public void salvar(Collection<E> unidades, AsyncCallback<Collection<E>> asyncCallback) {
		async.salvar(unidades, asyncCallback);
	}

	/**
	 * Metodo que simplifica o acesso ao salvar utilizando dados padrões.
	 * 
	 * @see CoreServiceAsync#salvar(Dados, AsyncCallback)
	 */
	public void salvar(AsyncCallback<E> asyncCallback) {
		salvar(classe, asyncCallback);
	}

	/**
	 * @see CoreServiceAsync#salvar(Dados, AsyncCallback)
	 */
	public void salvar(E unidade, AsyncCallback<E> asyncCallback) {
		async.salvar(unidade, asyncCallback);
	}

	@Override
	public void deletar(Collection<E> unidades, AsyncCallback<Collection<E>> asyncCallback) {
		async.deletar(unidades, asyncCallback);
	}

	/**
	 * Metodo que simplifica o acesso ao deletar utilizando dados padrões.
	 * 
	 * @see CoreServiceAsync#deletar(Dados, AsyncCallback)
	 */
	public void deletar(AsyncCallback<E> asyncCallback) {
		deletar(classe, asyncCallback);
	}

	@Override
	public void deletar(E unidade, AsyncCallback<E> asyncCallback) {
		async.deletar(unidade, asyncCallback);
	}

	@Override
	public void executar(Sql[] sqls, AsyncCallback<Integer[]> asyncCallback) {
		async.executar(sqls, asyncCallback);
	}
	
	@Override
	public void executar(String sql, AsyncCallback<Integer> asyncCallback) {
		async.executar(sql, asyncCallback);
	}

	@Override
	public void getAuth(AsyncCallback<Autenticacao> asyncCallback) {
		async.getAuth(asyncCallback);
	}

	// Gets e Seteres

	public IFiltro getFiltroPadrao() {
		return filtroPadrao;
	}

	public void setFiltroPadrao(IFiltro filtroPadrao) {
		this.filtroPadrao = filtroPadrao;
	}

	public IFiltro getFiltroExtra() {
		return filtroExtra;
	}

	public void setFiltroExtra(IFiltro filtroExtra) {
		this.filtroExtra = filtroExtra;
	}

	public IFiltro getFiltroFavorito() {
		return filtroFavorito;
	}

	public void setFiltroFavorito(IFiltro filtroFavorito) {
		this.filtroFavorito = filtroFavorito;
	}

	public GrupoFiltro getFiltroAtual() {
		return filtroAtual;
	}

	public GrupoFiltro getFiltroTotal() {
		return filtroTotal;
	}

	public E getClasse() {
		return classe;
	}
}
