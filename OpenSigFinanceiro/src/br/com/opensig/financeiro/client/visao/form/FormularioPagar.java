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
import br.com.opensig.financeiro.shared.modelo.FinPagamento;
import br.com.opensig.financeiro.shared.modelo.FinPagar;

public class FormularioPagar extends AFormularioFinanceiro<FinPagar, FinPagamento> {

	public FormularioPagar(SisFuncao funcao) {
		super(new FinPagar(), new FinPagamento(), funcao);
		inicializar();
	}

	public void inicializar() {
		nomes.put("classe", "finPagar");
		nomes.put("id", "finPagarId");
		nomes.put("valor", "finPagarValor");
		nomes.put("cadastro", "finPagarCadastro");
		nomes.put("categoria", "finPagarCategoria");
		nomes.put("nota", "finPagarNfe");
		nomes.put("observacao", "finPagarObservacao");
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
					proxy.salvarPagar(classe, categorias, ASYNC);
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

		classe.setFinPagamentos(formas);
		classe.setFinPagarId(Integer.valueOf(hdnCod.getValueAsString()));
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
			classe.setFinPagarNfe(txtNfe.getValue().intValue());
		} else {
			classe.setFinPagarNfe(0);
		}
		if (txtValor.getValue() != null) {
			classe.setFinPagarValor(txtValor.getValue().doubleValue());
		}
		classe.setFinPagarCadastro(dtCadastro.getValue());
		classe.setFinPagarCategoria(strCategorias);
		classe.setFinPagarObservacao(txtObservacao.getValueAsString());

		return retorno;
	}
}
