package br.com.opensig.poker.client.visao.lista;

import java.util.List;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.FiltroNumero;
import br.com.opensig.core.client.visao.abstrato.AListagemEditor;
import br.com.opensig.core.client.visao.abstrato.IListagem;
import br.com.opensig.poker.shared.modelo.PokerPremiacao;

import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.FloatFieldDef;
import com.gwtext.client.data.IntegerFieldDef;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.widgets.form.NumberField;
import com.gwtext.client.widgets.grid.BaseColumnConfig;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridEditor;

public class ListagemPremiacao extends AListagemEditor<PokerPremiacao> {

	private NumberField txtPosicao;
	private NumberField txtValor;
	private NumberField txtPonto;

	public ListagemPremiacao(boolean barraTarefa) {
		super(new PokerPremiacao(), barraTarefa);
		inicializar();
	}

	public void inicializar() {
		// campos
		FieldDef[] fd = new FieldDef[] { new IntegerFieldDef("pokerPremiacaoId"), new IntegerFieldDef("pokerPremiacaoPosicao"), new FloatFieldDef("pokerPremiacaoValor"),
				new IntegerFieldDef("pokerPremiacaoPonto") };
		campos = new RecordDef(fd);

		// colunas
		ColumnConfig ccId = new ColumnConfig("", "pokerPremiacaoId", 10, false);
		ccId.setHidden(true);
		ccId.setFixed(true);

		txtPosicao = new NumberField();
		txtPosicao.setAllowBlank(false);
		txtPosicao.setAllowDecimals(false);
		txtPosicao.setAllowNegative(false);
		txtPosicao.setDecimalPrecision(0);
		txtPosicao.setSelectOnFocus(true);
		ColumnConfig ccPosicao = new ColumnConfig(OpenSigCore.i18n.txtPosicao(), "pokerPremiacaoPosicao", 100, false);
		ccPosicao.setEditor(new GridEditor(txtPosicao));

		txtValor = new NumberField();
		txtValor.setAllowBlank(false);
		txtValor.setAllowDecimals(false);
		txtValor.setAllowNegative(false);
		txtValor.setDecimalPrecision(0);
		txtValor.setSelectOnFocus(true);
		ColumnConfig ccValor = new ColumnConfig(OpenSigCore.i18n.txtValor(), "pokerPremiacaoValor", 100, false, IListagem.DINHEIRO);
		ccValor.setEditor(new GridEditor(txtValor));

		txtPonto = new NumberField();
		txtPonto.setAllowBlank(false);
		txtPonto.setAllowNegative(false);
		txtPonto.setSelectOnFocus(true);
		ColumnConfig ccPonto = new ColumnConfig(OpenSigCore.i18n.txtPontos(), "pokerPremiacaoPonto", 100, false, IListagem.NUMERO);
		ccPonto.setEditor(new GridEditor(txtPonto));

		BaseColumnConfig[] bcc = new BaseColumnConfig[] { ccId, ccPosicao, ccValor, ccPonto };
		modelos = new ColumnModel(bcc);

		// barra de menu
		filtroPadrao = new FiltroNumero("pokerPremiacaoId", ECompara.IGUAL, 0);

		// configurações padrão e carrega dados
		setTitle(OpenSigCore.i18n.txtPremio(), "icon-premio");
		super.inicializar();
	}

	public boolean validar(List<PokerPremiacao> lista) {
		Record[] recs = getStore().getRecords();
		boolean valida = recs.length > 0;

		for (Record rec : recs) {
			try {
				int id = rec.getAsInteger("pokerPremiacaoId");
				int posicao = rec.getAsInteger("pokerPremiacaoPosicao");
				double valor = rec.getAsDouble("pokerPremiacaoValor");
				int ponto = rec.getAsInteger("pokerPremiacaoPonto");

				if (posicao < 1) {
					throw new Exception();
				}

				PokerPremiacao premio = new PokerPremiacao(id);
				premio.setPokerPremiacaoPosicao(posicao);
				premio.setPokerPremiacaoValor(valor);
				premio.setPokerPremiacaoPonto(ponto);

				lista.add(premio);
			} catch (Exception ex) {
				valida = false;
				break;
			}
		}

		return valida;
	}

	public NumberField getTxtPosicao() {
		return txtPosicao;
	}

	public void setTxtPosicao(NumberField txtPosicao) {
		this.txtPosicao = txtPosicao;
	}

	public NumberField getTxtValor() {
		return txtValor;
	}

	public void setTxtValor(NumberField txtValor) {
		this.txtValor = txtValor;
	}

	public NumberField getTxtPonto() {
		return txtPonto;
	}

	public void setTxtPonto(NumberField txtPonto) {
		this.txtPonto = txtPonto;
	}

}
