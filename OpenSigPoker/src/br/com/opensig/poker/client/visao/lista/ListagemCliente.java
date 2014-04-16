package br.com.opensig.poker.client.visao.lista;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.UtilClient;
import br.com.opensig.core.client.visao.abstrato.AListagem;
import br.com.opensig.core.client.visao.abstrato.IFormulario;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.poker.client.controlador.comando.ComandoJogador;
import br.com.opensig.poker.client.controlador.comando.ComandoParticipante;
import br.com.opensig.poker.shared.modelo.PokerCliente;

import com.gwtext.client.data.BooleanFieldDef;
import com.gwtext.client.data.DateFieldDef;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.IntegerFieldDef;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.grid.BaseColumnConfig;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.menu.Menu;
import com.gwtext.client.widgets.menu.MenuItem;

public class ListagemCliente extends AListagem<PokerCliente> {

	public ListagemCliente(IFormulario formulario) {
		super(formulario);
		inicializar();
	}

	public void inicializar() {
		// campos
		FieldDef[] fd = new FieldDef[] { new IntegerFieldDef("pokerClienteId"), new IntegerFieldDef("pokerClienteCodigo"), new StringFieldDef("pokerClienteNome"),
				new IntegerFieldDef("pokerClienteAuxiliar"), new StringFieldDef("pokerClienteDocumento"), new StringFieldDef("pokerClienteContato"), new StringFieldDef("pokerClienteEmail"),
				new DateFieldDef("pokerClienteData"), new BooleanFieldDef("pokerClienteAssociado"), new BooleanFieldDef("pokerClienteAtivo") };
		campos = new RecordDef(fd);

		// colunas
		ColumnConfig ccId = new ColumnConfig(OpenSigCore.i18n.txtCod(), "pokerClienteId", 50, true);
		ColumnConfig ccCodigo = new ColumnConfig(OpenSigCore.i18n.txtCod(), "pokerClienteCodigo", 75, true);
		ColumnConfig ccNome = new ColumnConfig(OpenSigCore.i18n.txtNome(), "pokerClienteNome", 150, true);
		ColumnConfig ccAux = new ColumnConfig(OpenSigCore.i18n.txtAuxiliar(), "pokerClienteAuxiliar", 75, true);
		ColumnConfig ccDocumento = new ColumnConfig(OpenSigCore.i18n.txtDocumento(), "pokerClienteDocumento", 100, true);
		ColumnConfig ccContato = new ColumnConfig(OpenSigCore.i18n.txtContato(), "pokerClienteContato", 100, true);
		ColumnConfig ccEmail = new ColumnConfig(OpenSigCore.i18n.txtEmail(), "pokerClienteEmail", 150, true);
		ColumnConfig ccAssociado = new ColumnConfig(OpenSigCore.i18n.txtAssociado(), "pokerClienteAssociado", 75, true, BOLEANO);
		ColumnConfig ccData = new ColumnConfig(OpenSigCore.i18n.txtData(), "pokerClienteData", 75, true, DATA);
		ColumnConfig ccAtivo = new ColumnConfig(OpenSigCore.i18n.txtAtivo(), "pokerClienteAtivo", 50, true, BOLEANO);

		BaseColumnConfig[] bcc = new BaseColumnConfig[] { ccId, ccCodigo, ccNome, ccAux, ccDocumento, ccContato, ccEmail, ccData, ccAssociado, ccAtivo };
		modelos = new ColumnModel(bcc);
		super.inicializar();
	}

	@Override
	public void irPara() {
		Menu mnuContexto = new Menu();

		// participante
		SisFuncao participante = UtilClient.getFuncaoPermitida(ComandoParticipante.class);
		MenuItem itemParticipante = gerarFuncao(participante, "pokerCliente.pokerClienteId", "pokerClienteId");
		if (itemParticipante != null) {
			mnuContexto.addItem(itemParticipante);
		}
		
		// jogador
		SisFuncao jogador = UtilClient.getFuncaoPermitida(ComandoJogador.class);
		MenuItem itemJogador = gerarFuncao(jogador, "pokerCliente.pokerClienteId", "pokerClienteId");
		if (itemJogador != null) {
			mnuContexto.addItem(itemJogador);
		}

		if (mnuContexto.getItems().length > 0) {
			MenuItem mnuItem = getIrPara();
			mnuItem.setMenu(mnuContexto);

			getMenu().addSeparator();
			getMenu().addItem(mnuItem);
		}
	}
}
