package br.com.opensig.financeiro.server.cobranca;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.jboleto.Banco;

import br.com.opensig.core.client.UtilClient;
import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.EJuncao;
import br.com.opensig.core.client.controlador.filtro.FiltroData;
import br.com.opensig.core.client.controlador.filtro.FiltroNumero;
import br.com.opensig.core.client.controlador.filtro.FiltroTexto;
import br.com.opensig.core.client.controlador.filtro.GrupoFiltro;
import br.com.opensig.core.client.controlador.filtro.IFiltro;
import br.com.opensig.core.server.CoreServiceImpl;
import br.com.opensig.core.server.UtilServer;
import br.com.opensig.core.shared.modelo.Lista;
import br.com.opensig.empresa.shared.modelo.EmpEndereco;
import br.com.opensig.empresa.shared.modelo.EmpEntidade;
import br.com.opensig.financeiro.client.servico.FinanceiroException;
import br.com.opensig.financeiro.shared.modelo.FinConta;
import br.com.opensig.financeiro.shared.modelo.FinRecebimento;
import br.com.opensig.financeiro.shared.modelo.FinRemessa;
import br.com.opensig.financeiro.shared.modelo.FinRetorno;

public class BancoBrasil extends ACobranca {

	private Date hoje;

	public BancoBrasil(FinConta conta) {
		super(conta, Banco.BANCO_DO_BRASIL);
		hoje = new Date();
	}

	public Boolean remessa(FinRemessa rem) throws FinanceiroException {
		try {
			StringBuffer sb = new StringBuffer();

			// filtros
			FiltroTexto ft = new FiltroTexto("finForma.finFormaDescricao", ECompara.IGUAL, auth.getConf().get("txtBoleto").toUpperCase());
			FiltroTexto ft1 = new FiltroTexto("finRecebimentoStatus", ECompara.IGUAL, auth.getConf().get("txtAberto").toUpperCase());
			FiltroData dt1 = new FiltroData("finRecebimentoCadastro", ECompara.MAIOR_IGUAL, rem.getFinRemessaDatade());
			FiltroData dt2 = new FiltroData("finRecebimentoCadastro", ECompara.MENOR_IGUAL, rem.getFinRemessaDataate());
			GrupoFiltro gf = new GrupoFiltro(EJuncao.E, new IFiltro[] { ft, ft1, dt1, dt2 });

			// Recuperando os boletos
			CoreServiceImpl<FinRecebimento> persistencia = new CoreServiceImpl<FinRecebimento>();
			Lista<FinRecebimento> lista = persistencia.selecionar(new FinRecebimento(), 0, 0, gf, false);
			double total = 0.00;
			int regs = 1;

			// Registro Header do arquivo
			sb.append(getHeaderArquivo() + "\n");
			// Registro Header de lote
			sb.append(getHeaderLote() + "\n");
			// Registro Detalhe
			for (FinRecebimento fin : lista.getLista()) {
				total += fin.getFinRecebimentoValor();
				// Segmento P
				sb.append(getSegmentoP(fin, regs) + "\n");
				// Segmento Q
				sb.append(getSegmentoQ(fin, regs) + "\n");
				regs++;
			}
			regs--;
			// Registro Trailer de lote
			sb.append(getTrailerLote(regs) + "\n");
			// Registro Trailer de arquivo
			sb.append(getTrailerArquivo(regs));

			if (regs > 0) {
				rem.setFinRemessaCadastro(hoje);
				rem.setFinRemessaQuantidade(regs);
				rem.setFinRemessaValor(total);
				rem.setFinRemessaArquivo(UtilServer.normaliza(sb.toString()));

				// salvando
				CoreServiceImpl<FinRemessa> persistencia2 = new CoreServiceImpl<FinRemessa>();
				persistencia2.salvar(rem);
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			UtilServer.LOG.error("Erro ao gerar a remessa", e);
			throw new FinanceiroException(e.getMessage());
		}
	}

	public String[][] retorno(FinRetorno ret) throws FinanceiroException {
		// seta as variaveis
		Collection<String[]> validos = new ArrayList<String[]>();
		String[] linhas = ret.getFinRetornoArquivo().split("\n");

		try {
			// percorre as linhas, pulando os header e trailer
			for (int i = 2; i < linhas.length - 2; i += 2) {
				validos.add(getRecebimento(linhas[i], linhas[i + 1]));
			}
		} catch (Exception e) {
			throw new FinanceiroException(e.getMessage());
		}

		// caso nenhum seja valido
		if (validos.isEmpty()) {
			UtilServer.LOG.debug("Erro ao gerar o retorno");
			throw new FinanceiroException("Erro ao gerar o retorno");
		}

		return validos.toArray(new String[][] {});
	}

	private String getHeaderArquivo() {
		StringBuffer sb = new StringBuffer();
		try {
			// CODIGO DO BANCO NA COMPENSACAO
			sb.append(conta.getFinBanco().getFinBancoNumero());
			// LOTE DE SERVICO
			sb.append("0000");
			// REGISTRO HEADER DE ARQUIVO
			sb.append("0");
			// USO EXCLUSIVO FEBRABAN/CNAB
			sb.append(UtilServer.formataTexto("", " ", 9, true));
			// TIPO DE INSCRICAO DA EMPRESA
			sb.append("2");
			// N DE INSCRICAO DA EMPRESA
			String cnpj = conta.getEmpEmpresa().getEmpEntidade().getEmpEntidadeDocumento1().replaceAll("\\D", "");
			sb.append(UtilServer.formataTexto(cnpj, "0", 14, false));
			// CODIGO DO CONVENIO NO BANCO
			String convenio = UtilServer.formataTexto(conta.getFinContaConvenio(), "0", 9, false);
			String produto = "0014";
			String carteira = conta.getFinContaCarteira();
			String variacao = "019";
			sb.append(UtilServer.formataTexto(convenio + produto + carteira + variacao, " ", 20, true));
			// AGENCIA MANTENEDORA DA CONTA E DV
			String agencia = conta.getFinContaAgencia().replaceAll("\\D", "");
			sb.append(UtilServer.formataTexto(agencia, "0", 6, false));
			// NUMERO DA CONTA CORRENTE
			String cc = conta.getFinContaNumero().replaceAll("\\D", "");
			sb.append(UtilServer.formataTexto(cc, "0", 13, false));
			// DIGITO VERIFICADOR DA AG/CONTA
			sb.append(" ");
			// NOME DA EMPRESA
			sb.append(limitaTamanho(conta.getEmpEmpresa().getEmpEntidade().getEmpEntidadeNome1(), 30));
			// NOME DO BANCO
			sb.append(limitaTamanho(conta.getFinBanco().getFinBancoDescricao(), 30));
			// USO EXCLUSIVO FEBRABAN/CNAB
			sb.append(UtilServer.formataTexto("", " ", 10, true));
			// CODIGO REMESSA / RETORNO
			sb.append("1");
			// DATA DE GERACAO DO ARQUIVO
			sb.append(UtilServer.formataData(hoje, "ddMMyyyy"));
			// HORA DE GERACAO DO ARQUIVO
			sb.append(UtilServer.formataData(hoje, "HHmmss"));
			// N SEQUENCIAL DO ARQUIVO
			sb.append("000001");
			// N DA VERSAO DO LAYOUT DO ARQUIVO
			sb.append("030");
			// DENSIDADE DE GRAVACAO DO ARQUIVO
			sb.append("00000");
			// PARA USO RESERVADO DO BANCO
			sb.append(UtilServer.formataTexto("", " ", 20, true));
			// PARA USO RESERVADO DA EMPRESA
			sb.append(UtilServer.formataTexto("", " ", 20, true));
			// USO EXCLUSIVO FEBRABAN/CNAB
			sb.append(UtilServer.formataTexto("", " ", 11, true));
			// IDENTIFICACAO COBRANCA S/PAPEL
			sb.append("   ");
			// USO EXCLUSIVO DAS VANS
			sb.append("000");
			// TIPO DE SERVICO
			sb.append("  ");
			// CODIGOS DAS OCORRENCIAS
			sb.append(UtilServer.formataTexto("", " ", 10, true));
		} catch (Exception e) {
			UtilServer.LOG.error("Erro ao gerar o headerArquivo", e);
		}
		return sb.toString();
	}

	private String getHeaderLote() {
		StringBuffer sb = new StringBuffer();
		try {
			// CODIGO DO BANCO NA COMPENSACAO
			sb.append(conta.getFinBanco().getFinBancoNumero());
			// LOTE DE SERVICO
			sb.append("0001");
			// REGISTRO HEADER DO LOTE
			sb.append("1");
			// TIPO DE OPERACAO
			sb.append("R");
			// TIPO DE SERVICO
			sb.append("01");
			// FORMA DE LANCAMENTO
			sb.append("00");
			// N DA VERSAO DO LAYOUT DO LOTE
			sb.append("020");
			// USO EXCLUSIVO FEBRABAN/CNAB
			sb.append(" ");
			// TIPO DE INSCRICAO DA EMPRESA
			sb.append("2");
			// N DE INSCRICAO DA EMPRESA
			String cnpj = conta.getEmpEmpresa().getEmpEntidade().getEmpEntidadeDocumento1().replaceAll("\\D", "");
			sb.append(UtilServer.formataTexto(cnpj, "0", 15, false));
			// CODIGO DO CONVENIO NO BANCO
			String convenio = UtilServer.formataTexto(conta.getFinContaConvenio(), "0", 9, false);
			String produto = "0014"; // cedente
			String carteira = conta.getFinContaCarteira();
			String variacao = "019";
			sb.append(UtilServer.formataTexto(convenio + produto + carteira + variacao, " ", 20, true));
			// AGENCIA MANTENEDORA DA CONTA E DV
			String agencia = conta.getFinContaAgencia().replaceAll("\\D", "");
			sb.append(UtilServer.formataTexto(agencia, "0", 6, false));
			// NUMERO DA CONTA CORRENTE
			String cc = conta.getFinContaNumero().replaceAll("\\D", "");
			sb.append(UtilServer.formataTexto(cc, "0", 13, false));
			// DIGITO VERIFICADOR DA AG/CONTA
			sb.append(" ");
			// NOME DA EMPRESA
			sb.append(limitaTamanho(conta.getEmpEmpresa().getEmpEntidade().getEmpEntidadeNome1(), 30));
			// MENSAGEM 1
			sb.append(limitaTamanho(auth.getConf().get("boleto.instrucao1"), 40));
			// MENSAGEM 2
			sb.append(limitaTamanho(auth.getConf().get("boleto.instrucao2"), 40));
			// NUMERO REMESSA/RETORNO
			sb.append(UtilServer.formataTexto("", "0", 8, true));
			// DATA DE GRAVACAO REMESSA/RETORNO
			sb.append(UtilServer.formataData(hoje, "ddMMyyyy"));
			// DATA DO CREDITO
			sb.append(UtilServer.formataData(hoje, "ddMMyyyy"));
			// USO EXCLUSIVO FEBRABAN/CNAB
			sb.append(UtilServer.formataTexto("", " ", 33, true));
		} catch (Exception e) {
			UtilServer.LOG.error("Erro ao gerar o headerLote", e);
		}
		return sb.toString();
	}

	private String getSegmentoP(FinRecebimento bol, int reg) {
		StringBuffer sb = new StringBuffer();
		try {
			// CODIGO DO BANCO NA COMPENSACAO
			sb.append(conta.getFinBanco().getFinBancoNumero());
			// LOTE DE SERVICO
			sb.append("0001");
			// REGISTRO DETALHE
			sb.append("3");
			// N SEQUENCIAL DO REG. NO LOTE
			sb.append(UtilServer.formataTexto(reg + "", "0", 5, false));
			// COD. SEGMENTO DO REG. DETALHE
			sb.append("P");
			// USO EXCLUSIVO FEBRABAN/CNAB
			sb.append(" ");
			// CODIGO DE MOVIMENTO
			sb.append("01");
			// AGENCIA MANTENEDORA DA CONTA E DV
			String agencia = conta.getFinContaAgencia().replaceAll("\\D", "");
			sb.append(UtilServer.formataTexto(agencia, "0", 6, false));
			// NUMERO DA CONTA CORRENTE
			String cc = conta.getFinContaNumero().replaceAll("\\D", "");
			sb.append(UtilServer.formataTexto(cc, "0", 13, false));
			// DIGITO VERIFICADOR DA AG/CONTA
			sb.append(" ");
			// IDENTIFICACAO DO TITULO NO BANCO
			String convenio = UtilServer.formataTexto(conta.getFinContaConvenio(), "0", 7, false);
			String sequencial = UtilServer.formataTexto(bol.getFinRecebimentoId() + "", "0", 10, false);
			sb.append(convenio + sequencial + "   ");
			// CODIGO DA CARTEIRA
			sb.append("7");
			// FORMA DE CADASTRAMENTO DO TITULO
			sb.append("1");
			// TIPO DE DOCUMENTO
			sb.append("1");
			// IDENTIFICAO DA EMISSAO DO BLOQUETO
			sb.append("2");
			// IDENTIFICACAO DA DISTRIBUICAO
			sb.append("2");
			// NUMERO DO DOCUMENTO DE COBRANCA
			sb.append(limitaTamanho(bol.getFinRecebimentoDocumento() + bol.getFinRecebimentoParcela(), 15));
			// DATA DE VENCIMENTO DO TITULO
			sb.append(UtilServer.formataData(bol.getFinRecebimentoVencimento(), "ddMMyyyy"));
			// VALOR NOMINAL DO TITULO
			String valor = UtilServer.formataNumero(bol.getFinRecebimentoValor(), 13, 2, false).replaceAll("\\D", "");
			sb.append(valor);
			// AGENCIA ENCARREGADA DA COBRANCA
			sb.append("00000");
			// DIGITO VERIFICADOR DA AGENCIA
			sb.append(" ");
			// ESPECIE DO TITULO
			sb.append("02");
			// IDENTIFICACAO DE TITULO ACEITO/NAO ACEITE
			sb.append("N");
			// DATA DA EMISSAO DO TITULO
			sb.append(UtilServer.formataData(hoje, "ddMMyyyy"));
			// CODIGO DO JUROS DE MORA
			sb.append("1");
			// DATA DO JUROS DE MORA
			sb.append(UtilServer.formataData(bol.getFinRecebimentoVencimento(), "ddMMyyyy"));
			// JUROS DE MORA POR DIA/TAXA
			sb.append(UtilServer.formataTexto("500", "0", 15, false));
			// CODIGO DO DESCONTO 1
			sb.append("0");
			// DATA DO DESCONTO 1
			sb.append(UtilServer.formataTexto("", "0", 8, false));
			// VALOR/PERCENTUAL A SER CONCEDIDO
			sb.append(UtilServer.formataTexto("", "0", 15, false));
			// VALOR DO IOF A SER RECOLHIDO
			sb.append(UtilServer.formataTexto("", "0", 15, false));
			// VALOR DO ABATIMENTO
			sb.append(UtilServer.formataTexto("", "0", 15, false));
			// IDENTIFICACAO DO TIT. NA EMPRESA
			sb.append(UtilServer.formataTexto(bol.getFinRecebimentoId() + "", "0", 25, false));
			// CODIGO PARA PROTESTO
			sb.append("2");
			// NUMERO DE DIAS PARA PROTESTO
			sb.append("03");
			// CODIGO PARA BAIXA/DEVOLUCAO
			sb.append("0");
			// NUMERO DE DIAS PARA BAIXA/DEVOLUCAO
			sb.append("000");
			// CODIGO DA MOEDA
			sb.append("09");
			// N. DO CONTR. DA OPERACAO D CRED
			sb.append(UtilServer.formataTexto("", "0", 10, false));
			// USO EXCLUSIVO FEBRABAN/CNAB
			sb.append(" ");
		} catch (Exception e) {
			UtilServer.LOG.error("Erro ao gerar o segmentoP", e);
		}
		return sb.toString();
	}

	private String getSegmentoQ(FinRecebimento bol, int reg) {
		StringBuffer sb = new StringBuffer();
		EmpEntidade cedente = bol.getFinReceber().getEmpEntidade();
		EmpEndereco ende = cedente.getEmpEnderecos().get(0);

		try {
			// CODIGO DO BANCO NA COMPENSACAO
			sb.append(conta.getFinBanco().getFinBancoNumero());
			// LOTE DE SERVICO
			sb.append("0001");
			// REGISTRO DETALHE
			sb.append("3");
			// N SEQUENCIAL DO REG. NO LOTE
			sb.append(UtilServer.formataTexto(reg + "", "0", 5, false));
			// COD. SEGMENTO DO REG. DETALHE
			sb.append("Q");
			// USO EXCLUSIVO FEBRABAN/CNAB
			sb.append(" ");
			// CODIGO DE MOVIMENTO
			sb.append("01");
			// TIPO DE INSCRICAO
			sb.append("2");
			// NUMERO DE INSCRICAO
			String cnpj = cedente.getEmpEntidadeDocumento1().replaceAll("\\D", "");
			sb.append(UtilServer.formataTexto(cnpj, "0", 15, false));
			// NOME
			sb.append(limitaTamanho(cedente.getEmpEntidadeNome1(), 40));
			// ENDERECO
			sb.append(limitaTamanho(ende.getEmpEnderecoLogradouro(), 40));
			// BAIRRO
			sb.append(limitaTamanho(ende.getEmpEnderecoBairro(), 15));
			// CEP
			sb.append(ende.getEmpEnderecoCep().substring(0, 5));
			// SUFIXO DO CEP
			sb.append(ende.getEmpEnderecoCep().substring(6, 9));
			// CIDADE
			sb.append(limitaTamanho(ende.getEmpMunicipio().getEmpMunicipioDescricao(), 15));
			// UNIDADE DA FEDERACAO
			sb.append(ende.getEmpMunicipio().getEmpEstado().getEmpEstadoSigla());
			// TIPO DE INSCRICAO
			sb.append("0");
			// NUMERO DE INSCRICAO
			sb.append(UtilServer.formataTexto("", "0", 15, false));
			// NOME DO SACADOR/AVALISTA
			sb.append(UtilServer.formataTexto("", " ", 40, true));
			// COD. BCO CORRESP. NA COMPENSACA
			sb.append("000");
			// NOSSO NUM. NO BCO CORRESPONDENT
			sb.append(UtilServer.formataTexto("", " ", 20, true));
			// USO EXCLUSIVO FEBRABAN/CNAB
			sb.append(UtilServer.formataTexto("", " ", 8, true));
		} catch (Exception e) {
			UtilServer.LOG.error("Erro ao gerar o segmentoQ", e);
		}
		return sb.toString();
	}

	private String getTrailerLote(int reg) {
		StringBuffer sb = new StringBuffer();
		try {
			// CODIGO DO BANCO NA COMPENSACAO
			sb.append(conta.getFinBanco().getFinBancoNumero());
			// LOTE DE SERVICO
			sb.append("0001");
			// REGISTRO TRAILER DO LOTE
			sb.append("5");
			// USO EXCLUSIVO FEBRABAN/CNAB
			sb.append(UtilServer.formataTexto("", " ", 9, true));
			// QUANTIDADE DE REGISTROS DO LOTE
			sb.append(UtilServer.formataTexto((reg * 2 + 2) + "", "0", 6, false));
			// QUANTIDADE DE TIT. EM COBRANCA
			sb.append(UtilServer.formataTexto("", "0", 6, false));
			// VALOR TOT. DOS TIT. EM CARTEIRA
			sb.append(UtilServer.formataTexto("", "0", 17, false));
			// QUANTIDADE DE TIT. EM COBRANCA
			sb.append(UtilServer.formataTexto("", "0", 6, false));
			// VALOR TOT DOS TIT. EM CARTEIRAS
			sb.append(UtilServer.formataTexto("", "0", 17, false));
			// QUANTIDADE DE TIT. EM COBRANCA
			sb.append(UtilServer.formataTexto("", "0", 6, false));
			// VALOR TOT DOS TIT. EM CARTEIRAS
			sb.append(UtilServer.formataTexto("", "0", 17, false));
			// QUANTIDADE DE TIT. EM COBRANCA
			sb.append(UtilServer.formataTexto("", "0", 6, false));
			// VALOR TOT DOS TIT. EM CARTEIRAS
			sb.append(UtilServer.formataTexto("", "0", 17, false));
			// NUMERO DO AVISO DE LANCAMENTO
			sb.append(UtilServer.formataTexto("", " ", 8, true));
			// USO EXCLUSIVO FEBRABAN/CNAB
			sb.append(UtilServer.formataTexto("", " ", 117, true));
		} catch (Exception e) {
			UtilServer.LOG.error("Erro ao gerar o trailerLote", e);
		}
		return sb.toString();
	}

	private String getTrailerArquivo(int reg) {
		StringBuffer sb = new StringBuffer();
		try {
			// CODIGO DO BANCO NA COMPENSACAO
			sb.append(conta.getFinBanco().getFinBancoNumero());
			// LOTE DE SERVICO
			sb.append("9999");
			// REGISTRO TRAILER DE ARQUIVO
			sb.append("9");
			// USO EXCLUSIVO FEBRABAN/CNAB
			sb.append(UtilServer.formataTexto("", " ", 9, true));
			// QUANTID. DE LOTES DO ARQUIVO
			sb.append(UtilServer.formataTexto("1", "0", 6, false));
			// QUANTID. DE REGISTROS DO ARQUIVO
			sb.append(UtilServer.formataTexto((reg * 2 + 4) + "", "0", 6, false));
			// QTDADE DE CONTAS P/CONC.- LOTES
			sb.append(UtilServer.formataTexto("1", "0", 6, false));
			// USO EXCLUSIVO FEBRABAN/CNAB
			sb.append(UtilServer.formataTexto("", " ", 205, true));
		} catch (Exception e) {
			UtilServer.LOG.error("Erro ao gerar o trailerArquivo", e);
		}
		return sb.toString();
	}

	private String[] getRecebimento(String seguimentoT, String seguimentoU) throws Exception {
		int id;
		double vl;
		Date dtRealizado;
		Date dtConciliado;
		String status = "1";

		// NUMERO DO DOCUMENTO DE COBRANCA
		try {
			String finRecebimentoId = seguimentoT.substring(58, 73);
			id = Integer.valueOf(finRecebimentoId);
		} catch (Exception e) {
			id = 0;
			status = "0";
		}

		// VALOR PAGO PELO SACADO
		try {
			String valor = seguimentoU.substring(78, 90) + "." + seguimentoU.substring(90, 92);
			vl = Double.valueOf(valor);
		} catch (Exception e) {
			vl = 0.00;
			status = "0";
		}

		// DATA DA OCORRENCIA
		try {
			String data = seguimentoU.substring(137, 145);
			dtRealizado = new SimpleDateFormat("ddMMyyyy").parse(data);
		} catch (Exception e) {
			dtRealizado = null;
			status = "0";
		}

		// DATA DO CREDITO
		try {
			String data = seguimentoU.substring(146, 153);
			dtConciliado = new SimpleDateFormat("ddMMyyyy").parse(data);
		} catch (Exception e) {
			dtConciliado = null;
			status = "0";
		}

		// busca o recebimento no sistema
		try {
			FiltroNumero fn = new FiltroNumero("finRecebimentoId", ECompara.IGUAL, id);
			CoreServiceImpl<FinRecebimento> persiste = new CoreServiceImpl<FinRecebimento>();
			FinRecebimento fin = persiste.selecionar(new FinRecebimento(), fn, false);

			return new String[] { fin.getFinRecebimentoId() + "", fin.getFinReceber().getEmpEntidade().getEmpEntidadeNome1(), fin.getFinRecebimentoDocumento(), vl + "",
					fin.getFinRecebimentoParcela(), UtilClient.getDataGrid(fin.getFinRecebimentoVencimento()), fin.getFinRecebimentoStatus(), UtilClient.getDataGrid(dtRealizado),
					UtilClient.getDataGrid(dtConciliado), fin.getFinRecebimentoStatus().equalsIgnoreCase(auth.getConf().get("txtAberto")) ? "0" : status, fin.getFinRecebimentoObservacao(),
					fin.getFinReceber().getFinConta().getFinContaId() + "" };
		} catch (Exception e) {
			UtilServer.LOG.error("Erro ao produrar o recebimento", e);
			return new String[] { id + "", "", "", vl + "", "", "", "0", UtilClient.getDataGrid(dtRealizado), "-1", seguimentoT, "0" };
		}

	}
}
