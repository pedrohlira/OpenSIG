package br.com.opensig.core.client.visao;

import java.util.List;
import java.util.Map;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.controlador.comando.IComando;
import br.com.opensig.core.client.visao.abstrato.IFormulario;

import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.layout.FitLayout;

/**
 * Classe que gera um assistente de janelas, para criar passos de execucao.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 */
public class Assistente extends Window {

	private List<IFormulario> frmTelas;
	private IComando cmdFinalizar;
	private Map contexto;
	private int frmAtual;

	/**
	 * Construtor que recebe um mapa de contexto a ser usado.
	 * 
	 * @param contexto
	 *            um mapa de contexto com as opcoes setadas da funcao.
	 */
	public Assistente(Map contexto) {
		super("", 100, 100, true, true);
		setLayout(new FitLayout());
		setAutoHeight(true);
		setAutoWidth(true);
		setClosable(true);
		this.contexto = contexto;
	}

	/**
	 * Metodo que inicia o assistente, com um array de formularios e um comando para ativar apos finalizar.
	 * 
	 * @param frmTelas
	 *            um array de formularios que serao mostrados na ordem, como um passo-a-passo.
	 * @param cmdFinalizar
	 *            o comando disparado apos terminar todos os passos.
	 */
	public void iniciar(List<IFormulario> frmTelas, IComando cmdFinalizar) {
		this.frmTelas = frmTelas;
		this.cmdFinalizar = cmdFinalizar;
		IFormulario formulario = frmTelas.get(0);
		formulario.getPanel().setHeader(false);

		if (frmTelas.size() == 1) {
			formulario.getPanel().setButtons(new Button[] { getBotao("cancelar"), getBotao("finalizar") });
		} else {
			formulario.getPanel().setButtons(new Button[] { getBotao("cancelar"), getBotao("avancar") });

			formulario = frmTelas.get(frmTelas.size() - 1);
			formulario.getPanel().setButtons(new Button[] { getBotao("voltar"), getBotao("finalizar") });
			formulario.getPanel().setHeader(false);

			for (int i = 1; i < frmTelas.size() - 1; i++) {
				formulario = frmTelas.get(i);
				formulario.getPanel().setButtons(new Button[] { getBotao("voltar"), getBotao("avancar") });
				formulario.getPanel().setHeader(false);
			}
		}

		setTela();
		show();
	}

	/**
	 * Metodo que adiciona um formulario ao final da fila de passos.
	 * 
	 * @param formulario
	 *            um objeto do tipo formulario.
	 */
	public void addFormulario(IFormulario formulario) {
		formulario.getPanel().setButtons(new Button[] { getBotao("voltar"), getBotao("avancar") });
		frmTelas.add(frmTelas.size() - 1, formulario);
	}

	private void cancelar() {
		close();
	}

	private void voltar() {
		frmAtual--;
		setTela();
	}

	private void avancar() {
		IFormulario form = frmTelas.get(frmAtual);
		if (form.getPanel().getForm().isValid() && form.setDados()) {
			frmAtual++;
			setTela();
		}
	}

	private void finalizar() {
		IFormulario form = frmTelas.get(frmAtual);
		if (form.getPanel().getForm().isValid() && form.setDados()) {
			cmdFinalizar.execute(contexto);
		}
	}

	private void setTela() {
		IFormulario formulario = frmTelas.get(frmAtual);
		formulario.setContexto(contexto);
		formulario.getPanel().enable();
		formulario.getPanel().show();
		formulario.mostrarDados();

		setTitle(OpenSigCore.i18n.txtPasso() + " " + (frmAtual + 1) + " / " + frmTelas.size() + " - " + formulario.getPanel().getTitle());
		setIconCls(formulario.getPanel().getIconCls());
		removeAll();
		add(formulario.getPanel());
		doLayout();
		try {
			center();
		} catch (Exception e) {
			// ja esta no centro
		}
	}

	private Button getBotao(final String tipo) {
		Button botao = null;
		botao = new Button();
		botao.setIconCls("icon-" + tipo);

		if (tipo.equals("cancelar")) {
			botao.setText(OpenSigCore.i18n.txtCancelar());
			botao.addListener(new ButtonListenerAdapter() {
				public void onClick(Button button, EventObject e) {
					cancelar();
				}
			});
		} else if (tipo.equals("voltar")) {
			botao.setText(OpenSigCore.i18n.txtVoltar());
			botao.addListener(new ButtonListenerAdapter() {
				public void onClick(Button button, EventObject e) {
					voltar();
				}
			});
		} else if (tipo.equals("avancar")) {
			botao.setText(OpenSigCore.i18n.txtAvancar());
			botao.addListener(new ButtonListenerAdapter() {
				public void onClick(Button button, EventObject e) {
					avancar();
				}
			});
		} else if (tipo.equals("finalizar")) {
			botao.setText(OpenSigCore.i18n.txtFinalizar());
			botao.addListener(new ButtonListenerAdapter() {
				public void onClick(Button button, EventObject e) {
					finalizar();
				}
			});
		}

		return botao;
	}

	// Gets e Seteres

	public List<IFormulario> getFrmTelas() {
		return frmTelas;
	}

	public void setFrmTelas(List<IFormulario> frmTelas) {
		this.frmTelas = frmTelas;
	}

	public IComando getCmdFinalizar() {
		return cmdFinalizar;
	}

	public void setCmdFinalizar(IComando cmdFinalizar) {
		this.cmdFinalizar = cmdFinalizar;
	}

	public int getFrmAtual() {
		return frmAtual;
	}

	public void setFrmAtual(int frmAtual) {
		this.frmAtual = frmAtual;
	}

	public Map getContexto() {
		return contexto;
	}

	public void setContexto(Map contexto) {
		this.contexto = contexto;
	}

}
