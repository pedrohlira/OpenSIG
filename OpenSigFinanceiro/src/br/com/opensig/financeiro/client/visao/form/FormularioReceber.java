package br.com.opensig.financeiro.client.visao.form;

import java.util.Map;

import br.com.opensig.core.client.controlador.comando.AComando;
import br.com.opensig.core.client.controlador.comando.IComando;
import br.com.opensig.core.client.controlador.comando.form.ComandoSalvar;
import br.com.opensig.core.client.controlador.comando.form.ComandoSalvarFinal;
import br.com.opensig.core.client.visao.Ponte;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.empresa.shared.modelo.EmpEmpresa;
import br.com.opensig.empresa.shared.modelo.EmpEntidade;
import br.com.opensig.financeiro.client.servico.FinanceiroProxy;
import br.com.opensig.financeiro.shared.modelo.FinConta;
import br.com.opensig.financeiro.shared.modelo.FinReceber;
import br.com.opensig.financeiro.shared.modelo.FinRecebimento;

public class FormularioReceber extends AFormularioFinanceiro<FinReceber, FinRecebimento> {

	public FormularioReceber(SisFuncao funcao) {
		super(new FinReceber(), new FinRecebimento(), funcao);
		inicializar();
	}

	public void inicializar() {
		nomes.put("classe", "finReceber");
		nomes.put("id", "finReceberId");
		nomes.put("valor", "finReceberValor");
		nomes.put("cadastro", "finReceberCadastro");
		nomes.put("categoria", "finReceberCategoria");
		nomes.put("nota", "finReceberNfe");
		nomes.put("observacao", "finReceberObservacao");
		super.inicializar();
	}

	@Override
	public IComando AntesDaAcao(IComando comando) {
		// salavando
		if (comando instanceof ComandoSalvar) {
			comando = new AComando(new ComandoSalvarFinal()) {
				public void execute(Map contexto) {
					super.execute(contexto);
					FinanceiroProxy proxy = new FinanceiroProxy();
					proxy.salvarReceber(classe, categorias, ASYNC);
				}
			};
		}
		
		return comando;
	}
	
	/*
	 * @see br.com.sig.core.client.visao.lista.IFormulario#setDados()
	 */
	public boolean setDados() {
		boolean retorno = super.setDados();

		classe.setFinRecebimentos(formas);
		classe.setFinReceberId(Integer.valueOf(hdnCod.getValueAsString()));
		if (hdnEmpresa.getValueAsString().equals("0")) {
			classe.setEmpEmpresa(new EmpEmpresa(Ponte.getLogin().getEmpresaId()));
		} else {
			classe.setEmpEmpresa(new EmpEmpresa(Integer.valueOf(hdnEmpresa.getValueAsString())));
		}
		if (!hdnEntidade.getValueAsString().equals("0")) {
			EmpEntidade entidade = new EmpEntidade(Integer.valueOf(hdnEntidade.getValueAsString()));
			classe.setEmpEntidade(entidade);
		}
		if (cmbConta.getValue() != null) {
			FinConta conta = new FinConta(Integer.valueOf(cmbConta.getValue()));
			classe.setFinConta(conta);
		}
		if (txtNfe.getValue() != null) {
			classe.setFinReceberNfe(txtNfe.getValue().intValue());
		} else {
			classe.setFinReceberNfe(0);
		}
		if (txtValor.getValue() != null) {
			classe.setFinReceberValor(txtValor.getValue().doubleValue());
		}
		classe.setFinReceberCadastro(dtCadastro.getValue());
		classe.setFinReceberCategoria(strCategorias);
		classe.setFinReceberObservacao(txtObservacao.getValueAsString());

		return retorno;
	}
}
