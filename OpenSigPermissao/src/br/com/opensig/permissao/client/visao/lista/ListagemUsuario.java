package br.com.opensig.permissao.client.visao.lista;

import java.util.Map.Entry;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.UtilClient;
import br.com.opensig.core.client.controlador.comando.lista.ComandoPermiteEmpresa;
import br.com.opensig.core.client.controlador.comando.lista.ComandoPermiteUsuario;
import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.EJuncao;
import br.com.opensig.core.client.controlador.filtro.FiltroBinario;
import br.com.opensig.core.client.controlador.filtro.FiltroNumero;
import br.com.opensig.core.client.controlador.filtro.GrupoFiltro;
import br.com.opensig.core.client.visao.Ponte;
import br.com.opensig.core.client.visao.abstrato.AListagem;
import br.com.opensig.core.client.visao.abstrato.IFormulario;
import br.com.opensig.core.shared.modelo.IFavorito;
import br.com.opensig.permissao.shared.modelo.SisUsuario;

import com.gwtext.client.data.BooleanFieldDef;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.IntegerFieldDef;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.grid.BaseColumnConfig;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtextux.client.widgets.grid.plugins.GridBooleanFilter;
import com.gwtextux.client.widgets.grid.plugins.GridFilter;

public class ListagemUsuario extends AListagem<SisUsuario> {

	public ListagemUsuario(IFormulario formulario) {
		super(formulario);
		inicializar();
	}

	public void inicializar() {
		// campos
		FieldDef[] fd = new FieldDef[] { new IntegerFieldDef("sisUsuarioId"), new StringFieldDef("sisUsuarioLogin"), new StringFieldDef("sisUsuarioSenha"), new IntegerFieldDef("sisUsuarioDesconto"),
				new StringFieldDef("sisUsuarioEmail"), new StringFieldDef("empEmpresas"), new StringFieldDef("sisGrupos"), new BooleanFieldDef("sisUsuarioAtivo"),
				new BooleanFieldDef("sisUsuarioSistema"), new StringFieldDef("grupoId"), new StringFieldDef("empresaId"), new StringFieldDef("sisPermissao") };
		campos = new RecordDef(fd);

		// colunas
		ColumnConfig ccId = new ColumnConfig(OpenSigCore.i18n.txtCod(), "sisUsuarioId", 50, true);
		ColumnConfig ccLogin = new ColumnConfig(OpenSigCore.i18n.txtNome(), "sisUsuarioLogin", 200, true);
		ColumnConfig ccSenha = new ColumnConfig("", "sisUsuarioSenha", 10);
		ccSenha.setHidden(true);
		ccSenha.setFixed(true);
		ColumnConfig ccDesconto = new ColumnConfig(OpenSigCore.i18n.txtDesconto() + "_%", "sisUsuarioDesconto", 75, true);
		ColumnConfig ccEmail = new ColumnConfig(OpenSigCore.i18n.txtEmail(), "sisUsuarioEmail", 150, true);
		ColumnConfig ccEmpresa = new ColumnConfig(OpenSigCore.i18n.txtEmpresa(), "empEmpresas", 200, false);
		ColumnConfig ccGrupos = new ColumnConfig(OpenSigCore.i18n.txtGrupo(), "sisGrupos", 200, false);
		ColumnConfig ccAtivo = new ColumnConfig(OpenSigCore.i18n.txtAtivo(), "sisUsuarioAtivo", 50, true, BOLEANO);
		ColumnConfig ccSistema = new ColumnConfig(OpenSigCore.i18n.txtSistema(), "sisUsuarioSistema", 75, false, BOLEANO);

		BaseColumnConfig[] bcc = new BaseColumnConfig[] { ccId, ccLogin, ccSenha, ccDesconto, ccEmail, ccEmpresa, ccGrupos, ccAtivo, ccSistema };
		modelos = new ColumnModel(bcc);

		GrupoFiltro gf = new GrupoFiltro();
		if (UtilClient.getAcaoPermitida(funcao, ComandoPermiteEmpresa.class) == null) {
			FiltroNumero fn = new FiltroNumero("empEmpresaId", ECompara.IGUAL, Ponte.getLogin().getEmpresaId());
			fn.setCampoPrefixo("t1.");
			gf.add(fn, EJuncao.E);
		}
		
		if (UtilClient.getAcaoPermitida(funcao, ComandoPermiteUsuario.class) == null) {
			FiltroNumero fn = new FiltroNumero("sisUsuarioId", ECompara.IGUAL, Ponte.getLogin().getId());
			gf.add(fn, EJuncao.E);
		}
		
		if (UtilClient.getAcaoPermitida(funcao, ComandoPermiteUsuario.class) == null) {
			FiltroBinario fb = new FiltroBinario("sisUsuarioSistema", ECompara.IGUAL, 0);
			gf.add(fb);
		}

		filtroPadrao = gf.size() > 0 ? gf : null;
		super.inicializar();
	}

	public void setGridFiltro() {
		super.setGridFiltro();
		for (Entry<String, GridFilter> entry : filtros.entrySet()) {
			if (entry.getKey().equals("sisUsuarioAtivo")) {
				((GridBooleanFilter) entry.getValue()).setValue(true);
				break;
			}
		}
	}

	public void setFavorito(IFavorito favorito) {
		filtros.get("sisUsuarioAtivo").setActive(false, true);
		super.setFavorito(favorito);
	}
}
