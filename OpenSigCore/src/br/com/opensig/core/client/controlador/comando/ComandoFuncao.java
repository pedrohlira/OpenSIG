package br.com.opensig.core.client.controlador.comando;

import java.util.Map;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.visao.Ponte;
import br.com.opensig.core.client.visao.abstrato.ANavegacao;
import br.com.opensig.core.shared.modelo.IFavoritoGrafico;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;

import com.gwtext.client.core.Position;
import com.gwtext.client.data.Record;
import com.gwtext.client.widgets.Component;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.TabPanel;
import com.gwtext.client.widgets.event.PanelListenerAdapter;
import com.gwtext.client.widgets.form.event.FormPanelListenerAdapter;

/**
 * Classe do comando função padrão, comando padronizado para abrir uma função
 * existente.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 */
public class ComandoFuncao extends AComando<SisFuncao> {

	/**
	 * @see AComando#AComando()
	 */
	public ComandoFuncao() {
		this(null);
	}

	/**
	 * @see AComando#AComando(IComando)
	 */
	public ComandoFuncao(IComando proximo) {
		super(proximo);
	}

	@Override
	public void execute(Map contexto) {
		contexto.put("form", FORM);
		contexto.put("lista", LISTA);
		contexto.put("grafico", GRAFICO);
		super.execute(contexto);

		if (LISTA.AntesDaFuncao(this, contexto) != null) {

			String sufixo = FAVORITO == null ? "_tab" : "_fav" + FAVORITO.getSisFavoritoId();
			LISTA.getPanel().setEnableColumnHide(FAVORITO == null);
			LISTA.getPanel().setHeader(contexto.get("portal") == null);
			if (GRAFICO != null) {
				GRAFICO.getPanel().setHeader(contexto.get("portal") == null);
			}

			if (contexto.get("portal") == null) {
				TAB = (TabPanel) Ponte.getCentro().getItem(DADOS.getSisFuncaoClasse() + sufixo);

				if (TAB == null) {
					TAB = new TabPanel();
					TAB.setClosable(true);
					TAB.setTabPosition(Position.BOTTOM);
					TAB.setId(DADOS.getSisFuncaoClasse() + sufixo);

					// se tiver listagem
					if (LISTA != null) {
						TAB.add(LISTA.getPanel());

						if (FAVORITO == null) {
							Record rec = ANavegacao.getRegistro(ANavegacao.FUNCOES, DADOS.getSisFuncaoClasse());
							TAB.setTitle(rec.getAsString("nome"), rec.getAsString("imagem"));
						} else {
							TAB.setTitle(FAVORITO.getSisFavoritoNome(), FAVORITO.getSisFavoritoGrafico().isEmpty() ? "icon-padrao" : "icon-grafico");
						}

						LISTA.getPanel().addListener(new PanelListenerAdapter() {
							public void onRender(Component component) {
								super.onRender(component);
								// setando a lista com os dados
								if (FAVORITO == null) {
									LISTA.getPanel().getStore().load(0, LISTA.getPaginador().getPageSize());
								} else {
									LISTA.setFavorito(FAVORITO);
									// se for favorito de grafico
									if (!FAVORITO.getSisFavoritoGrafico().isEmpty()) {
										IFavoritoGrafico graf = FAVORITO.getSisFavoritoGrafico().get(0);
										GRAFICO.setFavorito(graf);
										TAB.setActiveTab(2);
									}
								}
								MessageBox.wait(OpenSigCore.i18n.txtAguarde(), TAB.getTitle());
							}
						});
					}
					// se tiver formulario
					if (FORM != null) {
						FORM.setLista(LISTA);
						TAB.add(FORM.getPanel());
						TAB.setActiveTab(1);

						FORM.getPanel().addListener(new FormPanelListenerAdapter() {
							public void onRender(Component component) {
								super.onRender(component);
								TAB.setActiveTab(0);
							}
						});
					}
					// se tiver grafico
					if (GRAFICO != null) {
						TAB.add(GRAFICO.getPanel());
					}

					Ponte.getCentro().add(TAB);
				}

				Ponte.getCentro().scrollToTab(TAB, true);
				Ponte.getCentro().activate(TAB.getId());
			}

			LISTA.DepoisDaFuncao(this, contexto);
		}
	}
}
