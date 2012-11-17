package br.com.opensig.permissao.client.controlador.comando;

import br.com.opensig.core.client.controlador.comando.ComandoFuncao;
import br.com.opensig.core.client.visao.Ponte;
import br.com.opensig.core.client.visao.abstrato.ANavegacao;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.permissao.client.visao.form.FormularioAcesso;

import com.gwtext.client.data.Record;
import com.gwtext.client.widgets.Panel;
import java.util.Map;

public class ComandoAcesso extends ComandoFuncao {

	public void execute(Map contexto) {
		DADOS = (SisFuncao) contexto.get("dados");
		FORM = new FormularioAcesso(DADOS);
		Record rec = ANavegacao.getRegistro(ANavegacao.FUNCOES, DADOS.getSisFuncaoClasse());

		Panel panel = Ponte.getCentro().getItem(DADOS.getSisFuncaoClasse() + "_tab");
		if (panel == null) {
			panel = FORM.getPanel();
			panel.setId(DADOS.getSisFuncaoClasse() + "_tab");
			panel.setTitle(rec.getAsString("nome"), rec.getAsString("imagem"));
			panel.setClosable(true);
			Ponte.getCentro().add(panel);
		}
		Ponte.getCentro().scrollToTab(panel, true);
		Ponte.getCentro().activate(DADOS.getSisFuncaoClasse() + "_tab");
	}
}
