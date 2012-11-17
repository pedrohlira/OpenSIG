package br.com.opensig.financeiro.client.visao.form;

import java.util.Date;
import java.util.Map;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.controlador.comando.AComando;
import br.com.opensig.core.client.controlador.comando.IComando;
import br.com.opensig.core.client.controlador.comando.form.ComandoSalvar;
import br.com.opensig.core.client.controlador.comando.form.ComandoSalvarFinal;
import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.FiltroObjeto;
import br.com.opensig.core.client.servico.CoreProxy;
import br.com.opensig.core.client.visao.Ponte;
import br.com.opensig.core.client.visao.abstrato.AFormulario;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.empresa.shared.modelo.EmpEmpresa;
import br.com.opensig.financeiro.client.servico.FinanceiroProxy;
import br.com.opensig.financeiro.shared.modelo.FinConta;
import br.com.opensig.financeiro.shared.modelo.FinRemessa;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.IntegerFieldDef;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.data.event.StoreListenerAdapter;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.MessageBox.ConfirmCallback;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.DateField;
import com.gwtext.client.widgets.form.Hidden;
import com.gwtext.client.widgets.form.MultiFieldPanel;
import com.gwtextux.client.widgets.window.ToastWindow;

public class FormularioRemessa extends AFormulario<FinRemessa> {

	private Hidden hdnCod;
	private DateField dtInicio;
	private DateField dtTermino;
	private ComboBox cmbConta;

	public FormularioRemessa(SisFuncao funcao) {
		super(new FinRemessa(), funcao);
		inicializar();
	}

	public void inicializar() {
		super.inicializar();

		hdnCod = new Hidden("finRemessaId", "0");
		add(hdnCod);

		dtInicio = new DateField(OpenSigCore.i18n.txtData() + " " + OpenSigCore.i18n.txtInicio(), "finRemessaDatade", 80);
		dtInicio.setAllowBlank(false);

		dtTermino = new DateField(OpenSigCore.i18n.txtData() + " " + OpenSigCore.i18n.txtFim(), "finRemessaDataate", 80);
		dtTermino.setAllowBlank(false);

		MultiFieldPanel linha1 = new MultiFieldPanel();
		linha1.setBorder(false);
		linha1.addToRow(getConta(), 240);
		linha1.addToRow(dtInicio, 120);
		linha1.addToRow(dtTermino, 120);
		add(linha1);
	}

	@Override
	public IComando AntesDaAcao(IComando comando) {
		// salavando
		if (comando instanceof ComandoSalvar) {
			comando = new AComando(new ComandoSalvarFinal()) {
				public void execute(final Map contexto) {
					super.execute(contexto);
					FinanceiroProxy proxy = new FinanceiroProxy();
					proxy.remessa(classe, new AsyncCallback<Boolean>() {
						public void onFailure(Throwable caught) {
							ASYNC.onFailure(caught);
							new ToastWindow(OpenSigCore.i18n.txtAtencao(), OpenSigCore.i18n.errCliente()).show();
						}

						public void onSuccess(Boolean result) {
							if (result) {
								comando.execute(contexto);
							} else {
								getEl().unmask();
								MessageBox.alert(OpenSigCore.i18n.txtSalvar(), OpenSigCore.i18n.msgRegistro());
							}
						}
					});
				}
			};
		}

		return comando;
	}

	public boolean setDados() {
		boolean retorno = true;

		classe.setFinRemessaDatade(dtInicio.getValue());
		classe.setFinRemessaDataate(dtTermino.getValue());
		classe.setEmpEmpresa(new EmpEmpresa(Ponte.getLogin().getEmpresaId()));
		classe.setFinConta(new FinConta(Integer.valueOf(cmbConta.getValue())));

		if (classe.getFinRemessaDatade().compareTo(classe.getFinRemessaDataate()) > 0) {
			retorno = false;
			MessageBox.alert(OpenSigCore.i18n.txtSalvar(), OpenSigCore.i18n.txtInicio() + " > " + OpenSigCore.i18n.txtFim());
		}

		return retorno;
	}

	private ComboBox getConta() {
		FieldDef[] fdConta = new FieldDef[] { new IntegerFieldDef("finContaId"), new IntegerFieldDef("empEmpresa.empEmpresaId"), new StringFieldDef("empEmpresa.empEntidade.empEntidadeNome1"),
				new IntegerFieldDef("finBanco.finBancoId"), new StringFieldDef("finBanco.finBancoDescricao"), new StringFieldDef("finContaNome") };
		FiltroObjeto fo = new FiltroObjeto("empEmpresa", ECompara.IGUAL, new EmpEmpresa(Ponte.getLogin().getEmpresaId()));
		CoreProxy<FinConta> proxy = new CoreProxy<FinConta>(new FinConta(), fo);
		final Store storeConta = new Store(proxy, new ArrayReader(new RecordDef(fdConta)), true);
		storeConta.addStoreListener(new StoreListenerAdapter() {
			public void onLoad(Store store, Record[] records) {
				mostrar();
			}

			public void onLoadException(Throwable error) {
				MessageBox.confirm(OpenSigCore.i18n.txtEmbalagem(), OpenSigCore.i18n.msgRecarregar(), new ConfirmCallback() {
					public void execute(String btnID) {
						if (btnID.equalsIgnoreCase("yes")) {
							storeConta.load();
						}
					}
				});
			}
		});

		cmbConta = new ComboBox(OpenSigCore.i18n.txtConta(), "finConta.finContaId", 200);
		cmbConta.setAllowBlank(false);
		cmbConta.setStore(storeConta);
		cmbConta.setListWidth(200);
		cmbConta.setTriggerAction(ComboBox.ALL);
		cmbConta.setMode(ComboBox.LOCAL);
		cmbConta.setDisplayField("finContaNome");
		cmbConta.setValueField("finContaId");
		cmbConta.setForceSelection(true);
		cmbConta.setEditable(false);

		return cmbConta;
	}

	public void limparDados() {
		getForm().reset();
		dtInicio.setRawValue(DateTimeFormat.getFormat(PredefinedFormat.DATE_MEDIUM).format(new Date()));
		dtTermino.setRawValue(dtInicio.getRawValue());
	}

	public void gerarListas() {
	}

	public void mostrarDados() {
		if (cmbConta.getStore().getRecords().length == 0) {
			cmbConta.getStore().load();
		} else {
			mostrar();
		}
	}

	private void mostrar() {
		MessageBox.hide();
		Record rec = lista.getPanel().getSelectionModel().getSelected();
		if (rec != null) {
			getForm().loadRecord(rec);
		} else {
			if (cmbConta.getStore().getRecords().length == 1) {
				cmbConta.setValue(cmbConta.getStore().getRecordAt(0).getAsString("finContaId"));
			}
		}
		cmbConta.focus(true);
		
		if (duplicar) {
			hdnCod.setValue("0");
			duplicar = false;
		}
	}

	public Hidden getHdnCod() {
		return hdnCod;
	}

	public void setHdnCod(Hidden hdnCod) {
		this.hdnCod = hdnCod;
	}

	public DateField getDtInicio() {
		return dtInicio;
	}

	public void setDtInicio(DateField dtInicio) {
		this.dtInicio = dtInicio;
	}

	public DateField getDtTermino() {
		return dtTermino;
	}

	public void setDtTermino(DateField dtTermino) {
		this.dtTermino = dtTermino;
	}

	public ComboBox getCmbConta() {
		return cmbConta;
	}

	public void setCmbConta(ComboBox cmbConta) {
		this.cmbConta = cmbConta;
	}

}
