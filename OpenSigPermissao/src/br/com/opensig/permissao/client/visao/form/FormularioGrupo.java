package br.com.opensig.permissao.client.visao.form;

import java.util.ArrayList;
import java.util.List;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.UtilClient;
import br.com.opensig.core.client.controlador.comando.lista.ComandoPermiteEmpresa;
import br.com.opensig.core.client.controlador.comando.lista.ComandoPermiteUsuario;
import br.com.opensig.core.client.visao.ComboEntidade;
import br.com.opensig.core.client.visao.Ponte;
import br.com.opensig.core.client.visao.abstrato.AFormulario;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.empresa.shared.modelo.EmpEmpresa;
import br.com.opensig.permissao.shared.modelo.SisGrupo;
import br.com.opensig.permissao.shared.modelo.SisPermissao;
import br.com.opensig.permissao.shared.modelo.SisUsuario;

import com.gwtext.client.data.Record;
import com.gwtext.client.widgets.form.Checkbox;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.Hidden;
import com.gwtext.client.widgets.form.MultiFieldPanel;
import com.gwtext.client.widgets.form.NumberField;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.ComboBoxListenerAdapter;

public class FormularioGrupo extends AFormulario<SisGrupo> {

	private Hidden hdnCod;
	private Hidden hdnEmpresa;
	private ComboBox cmbEmpresa;
	private TextField txtNome;
	private TextField txtDescricao;
	private NumberField txtDesconto;
	private Checkbox chkAtivo;
	private Checkbox chkSistema;

	public FormularioGrupo(SisFuncao funcao) {
		super(new SisGrupo(), funcao);
		inicializar();
	}

	public void inicializar() {
		super.inicializar();

		hdnCod = new Hidden("sisGrupoId", "0");
		add(hdnCod);
		hdnEmpresa = new Hidden("empEmpresa.empEmpresaId", "0");
		add(hdnEmpresa);

		txtNome = new TextField(OpenSigCore.i18n.txtNome(), "sisGrupoNome", 200);
		txtNome.setAllowBlank(false);
		txtNome.setMaxLength(50);

		MultiFieldPanel linha1 = new MultiFieldPanel();
		linha1.setBorder(false);
		linha1.addToRow(getEmpresa(), 330);
		linha1.addToRow(txtNome, 220);
		add(linha1);
		
		txtDescricao = new TextField(OpenSigCore.i18n.txtDescricao(), "sisGrupoDescricao", 300);
		txtDescricao.setAllowBlank(false);
		txtDescricao.setMaxLength(255);

		txtDesconto = new NumberField(OpenSigCore.i18n.txtDesconto() + "_%", "sisGrupoDesconto", 50);
		txtDesconto.setAllowBlank(false);
		txtDesconto.setAllowDecimals(false);
		txtDesconto.setAllowNegative(false);
		txtDesconto.setMinValue(0);
		txtDesconto.setMaxValue(100);

		chkAtivo = new Checkbox(OpenSigCore.i18n.txtAtivo(), "sisGrupoAtivo");
		chkAtivo.setValue(true);

		chkSistema = new Checkbox(OpenSigCore.i18n.txtSistema(), "sisGrupoSistema");
		chkSistema.setValue(false);
		
		MultiFieldPanel linha2 = new MultiFieldPanel();
		linha2.setBorder(false);
		linha2.addToRow(txtDescricao, 320);
		linha2.addToRow(txtDesconto, 70);
		linha2.addToRow(chkAtivo, 70);
		linha2.addToRow(chkSistema, 70);
		add(linha2);
	}

	public boolean setDados() {
		classe.setSisGrupoId(Integer.valueOf(hdnCod.getValueAsString()));
		classe.setSisGrupoNome(txtNome.getValueAsString());
		classe.setSisGrupoDescricao(txtDescricao.getValueAsString());
		if (txtDesconto.getValue() != null) {
			classe.setSisGrupoDesconto(txtDesconto.getValue().intValue());
		}
		classe.setSisGrupoAtivo(chkAtivo.getValue());
		classe.setSisGrupoSistema(chkSistema.getValue());
		
		if (!hdnEmpresa.getValueAsString().equals("0")) {
			EmpEmpresa empresa = new EmpEmpresa(Integer.valueOf(hdnEmpresa.getValueAsString()));
			classe.setEmpEmpresa(empresa);
		}
		
		return true;
	}

	public void limparDados() {
		getForm().reset();
	}

	public void mostrarDados() {
		Record rec = lista.getPanel().getSelectionModel().getSelected();
		if (rec != null) {
			getForm().loadRecord(rec);

			// usuarios
			String users = rec.getAsString("sisUsuario");
			if (users != null && !users.equals("")) {
				List<SisUsuario> usuarios = new ArrayList<SisUsuario>();
				for (String usuario : users.split("::")) {
					usuarios.add(new SisUsuario(Integer.valueOf(usuario)));
				}
				classe.setSisUsuarios(usuarios);
			}
			// permissoes
			String per = rec.getAsString("sisPermissao");
			classe.setSisPermissoes(SisPermissao.getPermissoes(per));
		}
		txtNome.focus(true);

		if (duplicar) {
			hdnCod.setValue("0");
			duplicar = false;
		}
		
		if (UtilClient.getAcaoPermitida(funcao, ComandoPermiteUsuario.class) == null) {
			chkSistema.disable();
		}
		
		if (UtilClient.getAcaoPermitida(funcao, ComandoPermiteEmpresa.class) == null) {
			hdnEmpresa.setValue(Ponte.getLogin().getEmpresaId() + "");
			cmbEmpresa.setRawValue(Ponte.getLogin().getEmpresaNome());
			cmbEmpresa.disable();
		}
	}

	private ComboBox getEmpresa() {
		cmbEmpresa = UtilClient.getComboEntidade(new ComboEntidade(new EmpEmpresa()));
		cmbEmpresa.setName("empEmpresa.empEntidade.empEntidadeNome1");
		cmbEmpresa.setLabel(OpenSigCore.i18n.txtEmpresa());
		cmbEmpresa.addListener(new ComboBoxListenerAdapter() {
			public void onSelect(ComboBox comboBox, Record record, int index) {
				hdnEmpresa.setValue(comboBox.getValue());
			}

			public void onBlur(Field field) {
				if (cmbEmpresa.getRawValue().equals("")) {
					hdnEmpresa.setValue("0");
				}
			}
		});

		return cmbEmpresa;
	}
	
	public void gerarListas() {
	}

	public Hidden getHdnCod() {
		return hdnCod;
	}

	public void setHdnCod(Hidden hdnCod) {
		this.hdnCod = hdnCod;
	}

	public TextField getTxtNome() {
		return txtNome;
	}

	public Hidden getHdnEmpresa() {
		return hdnEmpresa;
	}

	public void setHdnEmpresa(Hidden hdnEmpresa) {
		this.hdnEmpresa = hdnEmpresa;
	}

	public void setTxtNome(TextField txtNome) {
		this.txtNome = txtNome;
	}

	public TextField getTxtDescricao() {
		return txtDescricao;
	}

	public void setTxtDescricao(TextField txtDescricao) {
		this.txtDescricao = txtDescricao;
	}

	public NumberField getTxtDesconto() {
		return txtDesconto;
	}

	public void setTxtDesconto(NumberField txtDesconto) {
		this.txtDesconto = txtDesconto;
	}

	public Checkbox getChkAtivo() {
		return chkAtivo;
	}

	public void setChkAtivo(Checkbox chkAtivo) {
		this.chkAtivo = chkAtivo;
	}

	public Checkbox getChkSistema() {
		return chkSistema;
	}

	public void setChkSistema(Checkbox chkSistema) {
		this.chkSistema = chkSistema;
	}

	public ComboBox getCmbEmpresa() {
		return cmbEmpresa;
	}

	public void setCmbEmpresa(ComboBox cmbEmpresa) {
		this.cmbEmpresa = cmbEmpresa;
	}

}
