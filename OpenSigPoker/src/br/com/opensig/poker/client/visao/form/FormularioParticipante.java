package br.com.opensig.poker.client.visao.form;

import java.util.Map;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.controlador.comando.AComando;
import br.com.opensig.core.client.controlador.comando.IComando;
import br.com.opensig.core.client.controlador.comando.form.ComandoSalvar;
import br.com.opensig.core.client.controlador.comando.form.ComandoSalvarFinal;
import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.EJuncao;
import br.com.opensig.core.client.controlador.filtro.FiltroBinario;
import br.com.opensig.core.client.controlador.filtro.FiltroNumero;
import br.com.opensig.core.client.controlador.filtro.FiltroObjeto;
import br.com.opensig.core.client.controlador.filtro.GrupoFiltro;
import br.com.opensig.core.client.controlador.filtro.IFiltro;
import br.com.opensig.core.client.servico.CoreProxy;
import br.com.opensig.core.client.visao.abstrato.AFormulario;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.poker.client.servico.PokerProxy;
import br.com.opensig.poker.shared.modelo.PokerCliente;
import br.com.opensig.poker.shared.modelo.PokerMesa;
import br.com.opensig.poker.shared.modelo.PokerParticipante;
import br.com.opensig.poker.shared.modelo.PokerTorneio;

import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.IntegerFieldDef;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.data.event.StoreListenerAdapter;
import com.gwtext.client.widgets.form.Checkbox;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.Hidden;
import com.gwtext.client.widgets.form.MultiFieldPanel;
import com.gwtext.client.widgets.form.NumberField;
import com.gwtext.client.widgets.form.event.ComboBoxListenerAdapter;

public class FormularioParticipante extends AFormulario<PokerParticipante> {

	private Hidden hdnCod;
	private Hidden hdnCliente;
	private Hidden hdnTorneio;
	private Hidden hdnMesa;
	private ComboBox cmbCliente;
	private ComboBox cmbTorneio;
	private ComboBox cmbMesa;
	private NumberField txtBonus;
	private NumberField txtReentrada;
	private NumberField txtPosicao;
	private NumberField txtPonto;
	private NumberField txtPremio;
	private Checkbox chkAdicional;
	private Checkbox chkDealer;
	private Checkbox chkAtivo;

	public FormularioParticipante(SisFuncao funcao) {
		super(new PokerParticipante(), funcao);
		inicializar();
	}

	public void inicializar() {
		hdnCod = new Hidden("pokerParticipanteId", "0");
		add(hdnCod);

		hdnCliente = new Hidden("pokerCliente.pokerClienteId", "0");
		add(hdnCliente);

		hdnTorneio = new Hidden("pokerTorneio.pokerTorneioId", "0");
		add(hdnTorneio);

		hdnMesa = new Hidden("pokerMesa.pokerMesaId", "0");
		add(hdnMesa);

		chkAdicional = new Checkbox(OpenSigCore.i18n.txtAddon(), "pokerParticipanteAdicional");
		
		chkDealer = new Checkbox(OpenSigCore.i18n.txtDealer(), "pokerParticipanteDealer");
		
		chkAtivo = new Checkbox(OpenSigCore.i18n.txtAtivo(), "pokerParticipanteAtivo");
		chkAtivo.setValue(true);

		MultiFieldPanel linha1 = new MultiFieldPanel();
		linha1.setBorder(false);
		linha1.addToRow(getTorneio(), 150);
		linha1.addToRow(getCliente(), 200);
		linha1.addToRow(getMesa(), 100);
		linha1.addToRow(chkAdicional, 70);
		linha1.addToRow(chkDealer, 100);
		linha1.addToRow(chkAtivo, 60);
		add(linha1);

		txtBonus = new NumberField(OpenSigCore.i18n.txtBonus(), "pokerParticipanteBonus", 80);
		txtBonus.setAllowBlank(false);
		txtBonus.setAllowNegative(false);
		txtBonus.setAllowDecimals(false);
		txtBonus.setValue(0);

		txtReentrada = new NumberField(OpenSigCore.i18n.txtRebuy(), "pokerParticipanteReentrada", 80);
		txtReentrada.setAllowBlank(false);
		txtReentrada.setAllowNegative(false);
		txtReentrada.setAllowDecimals(false);
		txtReentrada.setValue(0);

		txtPosicao = new NumberField(OpenSigCore.i18n.txtPosicao(), "pokerParticipantePosicao", 80);
		txtPosicao.setAllowBlank(false);
		txtPosicao.setAllowNegative(false);
		txtPosicao.setAllowDecimals(false);
		txtPosicao.setValue(0);

		txtPonto = new NumberField(OpenSigCore.i18n.txtPontos(), "pokerParticipantePonto", 80);
		txtPonto.setAllowBlank(false);
		txtPonto.setAllowNegative(false);
		txtPonto.setAllowDecimals(false);
		txtPonto.setValue(0);

		txtPremio = new NumberField(OpenSigCore.i18n.txtPremio(), "pokerParticipantePremio", 80);
		txtPremio.setAllowBlank(false);
		txtPremio.setAllowNegative(false);
		txtPremio.setValue(0);

		MultiFieldPanel linha2 = new MultiFieldPanel();
		linha2.addToRow(txtBonus, 100);
		linha2.addToRow(txtReentrada, 100);
		linha2.addToRow(txtPosicao, 100);
		linha2.addToRow(txtPonto, 100);
		linha2.addToRow(txtPremio, 100);
		linha2.setBorder(false);
		add(linha2);

		super.inicializar();
	}

	public IComando AntesDaAcao(IComando comando) {
		// salvando
		if (comando instanceof ComandoSalvar) {
			comando = new AComando(new ComandoSalvarFinal()) {
				public void execute(Map contexto) {
					super.execute(contexto);
					PokerProxy proxy = new PokerProxy();
					proxy.salvarParticipante(classe, ASYNC);
				}
			};
		}

		return comando;
	}

	public boolean setDados() {
		classe.setPokerParticipanteId(Integer.valueOf(hdnCod.getValueAsString()));
		if (!hdnCliente.getValueAsString().equals("0")) {
			PokerCliente cliente = new PokerCliente(Integer.valueOf(hdnCliente.getValueAsString()));
			classe.setPokerCliente(cliente);
		}
		if (!hdnTorneio.getValueAsString().equals("0")) {
			PokerTorneio torneio = new PokerTorneio(Integer.valueOf(hdnTorneio.getValueAsString()));
			classe.setPokerTorneio(torneio);
		}
		if (!hdnMesa.getValueAsString().equals("0")) {
			PokerMesa mesa = new PokerMesa(Integer.valueOf(hdnMesa.getValueAsString()));
			classe.setPokerMesa(mesa);
		}
		if (txtBonus.getValue() != null) {
			classe.setPokerParticipanteBonus(txtBonus.getValue().intValue());
		}
		if (txtReentrada.getValue() != null) {
			classe.setPokerParticipanteReentrada(txtReentrada.getValue().intValue());
		}
		classe.setPokerParticipanteAdicional(chkAdicional.getValue() ? 1 : 0);
		classe.setPokerParticipanteDealer(chkDealer.getValue() ? 1 : 0);
		if (txtPosicao.getValue() != null) {
			classe.setPokerParticipantePosicao(txtPosicao.getValue().intValue());
		}
		if (txtPonto.getValue() != null) {
			classe.setPokerParticipantePonto(txtPonto.getValue().intValue());
		}
		if (txtPremio.getValue() != null) {
			classe.setPokerParticipantePremio(txtPremio.getValue().doubleValue());
		}
		classe.setPokerParticipanteAtivo(chkAtivo.getValue());

		return true;
	}

	public void limparDados() {
		getForm().reset();
	}

	public void mostrarDados() {
		Record rec = lista.getPanel().getSelectionModel().getSelected();
		if (rec != null) {
			getForm().loadRecord(rec);
		}
		cmbTorneio.focus(true);

		if (duplicar) {
			hdnCod.setValue("0");
			hdnTorneio.setValue("0");
			cmbTorneio.setRawValue("");
			duplicar = false;
		}
	}

	public void gerarListas() {
	}

	private ComboBox getTorneio() {
		FieldDef[] campos = new FieldDef[] { new IntegerFieldDef("pokerTorneioId"), new IntegerFieldDef("pokerTorneioTipoId"), new StringFieldDef("pokerTorneioTipoNome"),
				new StringFieldDef("pokerTorneioCodigo") };
		FiltroBinario fb = new FiltroBinario("pokerTorneioFechado", ECompara.IGUAL, 0);
		CoreProxy<PokerTorneio> proxy = new CoreProxy<PokerTorneio>(new PokerTorneio(), fb);
		Store store = new Store(proxy, new ArrayReader(new RecordDef(campos)), false);

		cmbTorneio = new ComboBox(OpenSigCore.i18n.txtTorneio(), "pokerTorneio.pokerTorneioCodigo", 130);
		cmbTorneio.setAllowBlank(false);
		cmbTorneio.setStore(store);
		cmbTorneio.setTriggerAction(ComboBox.ALL);
		cmbTorneio.setMode(ComboBox.REMOTE);
		cmbTorneio.setDisplayField("pokerTorneioCodigo");
		cmbTorneio.setValueField("pokerTorneioId");
		cmbTorneio.setTpl("<div class=\"x-combo-list-item\"><b>{pokerTorneioTipoNome}</b> - <i>{pokerTorneioCodigo}</i></div>");
		cmbTorneio.setForceSelection(true);
		cmbTorneio.setEditable(false);
		cmbTorneio.setListWidth(200);
		cmbTorneio.addListener(new ComboBoxListenerAdapter() {
			public void onSelect(ComboBox comboBox, Record record, int index) {
				hdnTorneio.setValue(comboBox.getValue());
				// filtra as mesas
				PokerTorneio torneio = new PokerTorneio(Integer.valueOf(hdnTorneio.getValueAsString()));
				FiltroObjeto fo = new FiltroObjeto("pokerTorneio", ECompara.IGUAL, torneio);
				FiltroBinario fb = new FiltroBinario("pokerMesaAtivo", ECompara.IGUAL, 1);
				GrupoFiltro gf = new GrupoFiltro(EJuncao.E, new IFiltro[] { fo, fb });
				CoreProxy<PokerMesa> proxy = new CoreProxy<PokerMesa>(new PokerMesa(), gf);
				cmbMesa.getStore().setDataProxy(proxy);
				cmbMesa.getStore().reload();
				// filtra os clientes
				FiltroObjeto fo1 = new FiltroObjeto("pokerTorneio", ECompara.NULO, null);
				fo1.setCampoPrefixo("t1.");
				FiltroNumero fn1 = new FiltroNumero("pokerTorneio.pokerTorneioId", ECompara.DIFERENTE, torneio.getId());
				fn1.setCampoPrefixo("t1.");
				GrupoFiltro gf1 = new GrupoFiltro(EJuncao.OU, new IFiltro[] { fo1, fn1 });
				FiltroBinario fb1 = new FiltroBinario("pokerClienteAtivo", ECompara.IGUAL, 1);
				GrupoFiltro gf2 = new GrupoFiltro(EJuncao.E, new IFiltro[] { gf1, fb1 });
				CoreProxy<PokerCliente> proxy1 = new CoreProxy<PokerCliente>(new PokerCliente(), gf2);
				cmbCliente.getStore().setDataProxy(proxy1);
				cmbCliente.getStore().reload();
			}

			public void onBlur(Field field) {
				if (cmbTorneio.getRawValue().equals("")) {
					hdnTorneio.setValue("0");
				}
			}
		});

		return cmbTorneio;
	}

	private ComboBox getCliente() {
		FieldDef[] campos = new FieldDef[] { new IntegerFieldDef("pokerClienteId"), new IntegerFieldDef("pokerClienteCodigo"), new StringFieldDef("pokerClienteNome") };
		Store store = new Store(new ArrayReader(new RecordDef(campos)));

		cmbCliente = new ComboBox(OpenSigCore.i18n.txtCliente(), "pokerCliente.pokerClienteNome", 180);
		cmbCliente.setAllowBlank(false);
		cmbCliente.setStore(store);
		cmbCliente.setTriggerAction(ComboBox.ALL);
		cmbCliente.setMode(ComboBox.REMOTE);
		cmbCliente.setDisplayField("pokerClienteNome");
		cmbCliente.setValueField("pokerClienteId");
		cmbCliente.setForceSelection(true);
		cmbCliente.setEditable(true);
		cmbCliente.setMinChars(1);
		cmbCliente.setTypeAhead(true);
		cmbCliente.setListWidth(200);
		cmbCliente.addListener(new ComboBoxListenerAdapter() {
			public void onSelect(ComboBox comboBox, Record record, int index) {
				hdnCliente.setValue(comboBox.getValue());
			}

			public void onBlur(Field field) {
				if (cmbCliente.getRawValue().equals("")) {
					hdnCliente.setValue("0");
				}
			}
		});

		return cmbCliente;
	}

	private ComboBox getMesa() {
		FieldDef[] campos = new FieldDef[] { new IntegerFieldDef("pokerMesaId"), new IntegerFieldDef("pokerMesaNumero"), new IntegerFieldDef("pokerMesaLugares"),
				new StringFieldDef("pokerMesaOcupados") };
		Store store = new Store(new ArrayReader(new RecordDef(campos)));
		store.addStoreListener(new StoreListenerAdapter() {
			public void onLoad(Store store, Record[] records) {
				for (Record rec : records) {
					if (rec.getAsInteger("pokerMesaLugares") == rec.getAsInteger("pokerMesaOcupados")) {
						store.remove(rec);
					}
				}
			}
		});

		cmbMesa = new ComboBox(OpenSigCore.i18n.txtMesa(), "pokerMesa.pokerMesaNumero", 80);
		cmbMesa.setAllowBlank(false);
		cmbMesa.setStore(store);
		cmbMesa.setTriggerAction(ComboBox.ALL);
		cmbMesa.setMode(ComboBox.REMOTE);
		cmbMesa.setDisplayField("pokerMesaNumero");
		cmbMesa.setValueField("pokerMesaId");
		cmbMesa.setTpl("<div class=\"x-combo-list-item\"><b>Mesa {pokerMesaNumero}</b> - <i>{pokerMesaOcupados}/{pokerMesaLugares}</i></div>");
		cmbMesa.setForceSelection(true);
		cmbMesa.setEditable(false);
		cmbMesa.setListWidth(120);
		cmbMesa.addListener(new ComboBoxListenerAdapter() {
			public void onSelect(ComboBox comboBox, Record record, int index) {
				hdnMesa.setValue(comboBox.getValue());
			}

			public void onBlur(Field field) {
				if (cmbMesa.getRawValue().equals("")) {
					hdnMesa.setValue("0");
				}
			}
		});

		return cmbMesa;
	}

	public Hidden getHdnCod() {
		return hdnCod;
	}

	public void setHdnCod(Hidden hdnCod) {
		this.hdnCod = hdnCod;
	}

	public Hidden getHdnCliente() {
		return hdnCliente;
	}

	public void setHdnCliente(Hidden hdnCliente) {
		this.hdnCliente = hdnCliente;
	}

	public Hidden getHdnTorneio() {
		return hdnTorneio;
	}

	public void setHdnTorneio(Hidden hdnTorneio) {
		this.hdnTorneio = hdnTorneio;
	}

	public Hidden getHdnMesa() {
		return hdnMesa;
	}

	public void setHdnMesa(Hidden hdnMesa) {
		this.hdnMesa = hdnMesa;
	}

	public ComboBox getCmbCliente() {
		return cmbCliente;
	}

	public void setCmbCliente(ComboBox cmbCliente) {
		this.cmbCliente = cmbCliente;
	}

	public ComboBox getCmbTorneio() {
		return cmbTorneio;
	}

	public void setCmbTorneio(ComboBox cmbTorneio) {
		this.cmbTorneio = cmbTorneio;
	}

	public ComboBox getCmbMesa() {
		return cmbMesa;
	}

	public void setCmbMesa(ComboBox cmbMesa) {
		this.cmbMesa = cmbMesa;
	}

	public NumberField getTxtReentrada() {
		return txtReentrada;
	}

	public void setTxtReentrada(NumberField txtReentrada) {
		this.txtReentrada = txtReentrada;
	}

	public NumberField getTxtPosicao() {
		return txtPosicao;
	}

	public void setTxtPosicao(NumberField txtPosicao) {
		this.txtPosicao = txtPosicao;
	}

	public NumberField getTxtPonto() {
		return txtPonto;
	}

	public void setTxtPonto(NumberField txtPonto) {
		this.txtPonto = txtPonto;
	}

	public NumberField getTxtPremio() {
		return txtPremio;
	}

	public void setTxtPremio(NumberField txtPremio) {
		this.txtPremio = txtPremio;
	}

	public Checkbox getChkAdicional() {
		return chkAdicional;
	}

	public void setChkAdicional(Checkbox chkAdicional) {
		this.chkAdicional = chkAdicional;
	}

	public NumberField getTxtBonus() {
		return txtBonus;
	}

	public void setTxtBonus(NumberField txtBonus) {
		this.txtBonus = txtBonus;
	}

	public Checkbox getChkDealer() {
		return chkDealer;
	}

	public void setChkDealer(Checkbox chkDealer) {
		this.chkDealer = chkDealer;
	}

	public Checkbox getChkAtivo() {
		return chkAtivo;
	}

	public void setChkAtivo(Checkbox chkAtivo) {
		this.chkAtivo = chkAtivo;
	}

}
