package br.com.opensig.financeiro.server.cobranca;

import java.text.DateFormat;
import java.util.Date;

import org.jboleto.Banco;
import org.jboleto.FabricaBanco;
import org.jboleto.JBoletoBean;

import br.com.opensig.core.server.UtilServer;
import br.com.opensig.core.shared.modelo.Autenticacao;
import br.com.opensig.empresa.shared.modelo.EmpEndereco;
import br.com.opensig.empresa.shared.modelo.EmpEntidade;
import br.com.opensig.financeiro.client.servico.FinanceiroException;
import br.com.opensig.financeiro.server.boleto.FabricaBoleto;
import br.com.opensig.financeiro.server.boleto.IBoleto;
import br.com.opensig.financeiro.shared.modelo.FinConta;
import br.com.opensig.financeiro.shared.modelo.FinRecebimento;

public abstract class ACobranca implements ICobranca {

	protected Autenticacao auth;
	protected FinConta conta;
	protected int banco;

	public ACobranca(FinConta conta, int banco) {
		this.conta = conta;
		this.banco = banco;
	}

	@Override
	public void setAuth(Autenticacao auth) {
		this.auth = auth;
	}
	
	public byte[] boleto(String tipo, FinRecebimento finBoleto) throws FinanceiroException {
		IBoleto bol = FabricaBoleto.getInstancia().getBoleto(tipo);
		JBoletoBean bean = getBean(auth.getEmpresa(), finBoleto);
		Banco banco = FabricaBanco.getBanco(bean, this.banco);
		return bol.getBoleto(bean, banco);
	}
	
	protected JBoletoBean getBean(String[] empresa, FinRecebimento boleto) {
		EmpEntidade cliente = boleto.getFinReceber().getEmpEntidade();
		EmpEndereco endereco = cliente.getEmpEnderecos().isEmpty() ? new EmpEndereco() : cliente.getEmpEnderecos().get(0);

		JBoletoBean jBoletoBean = new JBoletoBean();
		jBoletoBean.setImagemMarketing(UtilServer.getRealPath("img/boleto.gif"));
		jBoletoBean.setTituloBoletoHtml("boleto");
		jBoletoBean.setEspecieDocumento("DM");
		jBoletoBean.setAceite("N");
		jBoletoBean.setDataDocumento(UtilServer.formataData(new Date(), DateFormat.MEDIUM));
		jBoletoBean.setDataProcessamento(UtilServer.formataData(new Date(), DateFormat.MEDIUM));
		jBoletoBean.setCedente(empresa[2] + " - " + empresa[5]);

		jBoletoBean.setNomeSacado(cliente.getEmpEntidadeNome1());
		jBoletoBean.setCpfSacado(cliente.getEmpEntidadeDocumento1());
		jBoletoBean.setEnderecoSacado(endereco.getEmpEnderecoLogradouro());
		jBoletoBean.setBairroSacado(endereco.getEmpEnderecoBairro());
		jBoletoBean.setCidadeSacado(endereco.getEmpMunicipio().getEmpMunicipioDescricao());
		jBoletoBean.setUfSacado(endereco.getEmpMunicipio().getEmpEstado().getEmpEstadoSigla());
		jBoletoBean.setCepSacado(endereco.getEmpEnderecoCep());

		jBoletoBean.setLocalPagamento(auth.getConf().get("boleto.local1"));
		jBoletoBean.setLocalPagamento2(auth.getConf().get("boleto.local2"));
		jBoletoBean.setDataVencimento(UtilServer.formataData(boleto.getFinRecebimentoVencimento(), DateFormat.MEDIUM));

		jBoletoBean.setInstrucao1(auth.getConf().get("boleto.instrucao1"));
		jBoletoBean.setInstrucao2(auth.getConf().get("boleto.instrucao2"));
		jBoletoBean.setInstrucao3(auth.getConf().get("boleto.instrucao3"));
		jBoletoBean.setInstrucao4(auth.getConf().get("boleto.instrucao4"));
		jBoletoBean.setInstrucao5(auth.getConf().get("boleto.instrucao5"));

		// identificando a agencia
		String ag = conta.getFinContaAgencia();
		String agf = ag.replaceAll("\\D", "");
		if (ag.equals(agf)) {
			ag = "";
		} else {
			ag = agf.substring(agf.length() - 1);
			agf = agf.substring(0, agf.length() - 1);
		}

		// identificando a conta
		String cc = conta.getFinContaNumero();
		String ccf = cc.replaceAll("\\D", "");
		if (cc.equals(ccf)) {
			cc = "";
		} else {
			cc = ccf.substring(ccf.length() - 1);
			ccf = ccf.substring(0, ccf.length() - 1);
		}

		jBoletoBean.setCarteira(conta.getFinContaCarteira());
		jBoletoBean.setAgencia(agf);
		jBoletoBean.setDvAgencia(ag);
		jBoletoBean.setContaCorrente(ccf);
		jBoletoBean.setDvContaCorrente(cc);
		jBoletoBean.setNumConvenio(conta.getFinContaConvenio());

		jBoletoBean.setNossoNumero(boleto.getFinRecebimentoId() + "", 10);
		jBoletoBean.setNoDocumento(boleto.getFinRecebimentoDocumento() + boleto.getFinRecebimentoParcela());
		jBoletoBean.setValorBoleto(UtilServer.formataNumero(boleto.getFinRecebimentoValor(), 1, 2, true));

		return jBoletoBean;
	}
	
	protected String limitaTamanho(String campo, int tamanho) {
		if (campo == null) {
			campo = "";
		}
		
		if(campo.length() > tamanho){
			campo = campo.substring(0, tamanho);
		}
		
		return UtilServer.formataTexto(campo, " ", tamanho, true);
	}
}
