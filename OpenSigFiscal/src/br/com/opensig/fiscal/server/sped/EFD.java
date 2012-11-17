package br.com.opensig.fiscal.server.sped;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.opensig.comercial.shared.modelo.ComCompra;
import br.com.opensig.comercial.shared.modelo.ComEcfNota;
import br.com.opensig.comercial.shared.modelo.ComEcfZ;
import br.com.opensig.comercial.shared.modelo.ComFrete;
import br.com.opensig.comercial.shared.modelo.ComNatureza;
import br.com.opensig.comercial.shared.modelo.ComVenda;
import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.EJuncao;
import br.com.opensig.core.client.controlador.filtro.FiltroBinario;
import br.com.opensig.core.client.controlador.filtro.FiltroData;
import br.com.opensig.core.client.controlador.filtro.FiltroNumero;
import br.com.opensig.core.client.controlador.filtro.FiltroObjeto;
import br.com.opensig.core.client.controlador.filtro.FiltroTexto;
import br.com.opensig.core.client.controlador.filtro.GrupoFiltro;
import br.com.opensig.core.client.controlador.filtro.IFiltro;
import br.com.opensig.core.client.controlador.parametro.ParametroBinario;
import br.com.opensig.core.server.UtilServer;
import br.com.opensig.core.shared.modelo.Autenticacao;
import br.com.opensig.core.shared.modelo.EComando;
import br.com.opensig.core.shared.modelo.EDirecao;
import br.com.opensig.core.shared.modelo.Lista;
import br.com.opensig.core.shared.modelo.Sql;
import br.com.opensig.empresa.shared.modelo.EmpEmpresa;
import br.com.opensig.fiscal.client.servico.FiscalService;
import br.com.opensig.fiscal.server.FiscalServiceImpl;
import br.com.opensig.fiscal.shared.modelo.FisSpedBloco;
import br.com.opensig.fiscal.shared.modelo.FisSpedFiscal;
import br.com.opensig.produto.shared.modelo.ProdProduto;

public class EFD implements Runnable {

	private File arquivo;
	private FisSpedFiscal sped;
	private FiscalService service;
	private Autenticacao auth;
	private Date inicio;
	private Date fim;
	private double pis;
	private double cofins;
	private List<FisSpedBloco> blocos;
	private List<ComCompra> compras;
	private List<ComFrete> fretes;
	private List<ComVenda> vendas;
	private List<ComEcfNota> notas;
	private List<ComEcfZ> zs;
	private List<ProdProduto> estoque;

	public EFD(File arquivo, FisSpedFiscal sped, Autenticacao auth) {
		this.arquivo = arquivo;
		this.sped = sped;
		this.service = new FiscalServiceImpl(auth);
		this.auth = auth;
	}

	@Override
	public void run() {
		try {
			// criando o arquivo novo
			arquivo.createNewFile();
			// setando a empresa no sped
			FiltroNumero fn = new FiltroNumero("empEmpresaId", ECompara.IGUAL, sped.getEmpEmpresa().getEmpEmpresaId());
			EmpEmpresa emp = (EmpEmpresa) service.selecionar(new EmpEmpresa(), fn, false);
			sped.setEmpEmpresa(emp);
			// datas
			inicio = new SimpleDateFormat("ddMMyyyy").parse("01" + (sped.getFisSpedFiscalMes() > 9 ? sped.getFisSpedFiscalMes() : "0" + sped.getFisSpedFiscalMes()) + sped.getFisSpedFiscalAno());
			Calendar cal = Calendar.getInstance();
			cal.setTime(inicio);
			cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
			fim = cal.getTime();
			// prepara os dados
			blocos = getBlocos();
			compras = getCompras();
			fretes = getFretes();
			vendas = getVendas();
			notas = getNotas();
			zs = getZs();
			estoque = getEstoque();
			// pis/cofins
			FiltroNumero fn1 = new FiltroNumero("comNaturezaCfopTrib", ECompara.IGUAL, 5101);
			FiltroNumero fn2 = new FiltroNumero("comNaturezaCfopTrib", ECompara.IGUAL, 5102);
			GrupoFiltro gf = new GrupoFiltro(EJuncao.OU, new IFiltro[] { fn1, fn2 });
			ComNatureza nat = (ComNatureza) service.selecionar(new ComNatureza(), gf, true);
			pis = nat.getComNaturezaPis();
			cofins = nat.getComNaturezaCofins();
			// lendo dados do arquivo
			escreverRegistros();
			InputStream is = new FileInputStream(arquivo);
			byte[] obj = new byte[is.available()];
			is.read(obj);
			is.close();
			// gerando o zip
			Map<String, byte[]> zip = new HashMap<String, byte[]>();
			zip.put(arquivo.getName(), obj);
			obj = UtilServer.getZIP(zip);
			// salvando o zip em arquivo fisico
			OutputStream os = new FileOutputStream(arquivo.getPath().replace("TXT", "ZIP"));
			os.write(obj);
			os.flush();
			os.close();
			// atualizando o status do registro
			FiltroNumero fn3 = new FiltroNumero("fisSpedFiscalId", ECompara.IGUAL, sped.getFisSpedFiscalId());
			ParametroBinario pb = new ParametroBinario("fisSpedFiscalAtivo", 1);
			Sql sql = new Sql(new FisSpedFiscal(), EComando.ATUALIZAR, fn3, pb);
			service.executar(new Sql[] { sql });
		} catch (Exception e) {
			UtilServer.LOG.error("Nao gerou o efd.", e);
		} finally {
			// deletando o arquivo txt
			arquivo.delete();
		}
	}

	// metodo que recupera os blocos
	private List<FisSpedBloco> getBlocos() throws Exception {
		// caso nao seja mes Fevereiro, nao pegar o H005 e H010
		GrupoFiltro gf = new GrupoFiltro();
		FiltroBinario fb = new FiltroBinario(sped.getFisSpedFiscalTipo().contains("ICMS") ? "fisSpedBlocoIcmsIpi" : "fisSpedBlocoPisCofins", ECompara.IGUAL, 1);
		gf.add(fb, EJuncao.E);
		FiltroTexto ft = new FiltroTexto("fisSpedBlocoClasse", ECompara.DIFERENTE, "NULL");
		gf.add(ft);
		// seleciona os blocos
		return service.selecionar(new FisSpedBloco(), 0, 0, gf, false).getLista();
	}

	// metodo que recupera as compras
	private List<ComCompra> getCompras() throws Exception {
		// monta o filtro
		GrupoFiltro gf = new GrupoFiltro();
		FiltroBinario fb = new FiltroBinario("comCompraNfe", ECompara.IGUAL, 1);
		gf.add(fb, EJuncao.E);
		FiltroObjeto fo = new FiltroObjeto("empEmpresa", ECompara.IGUAL, sped.getEmpEmpresa());
		gf.add(fo, EJuncao.E);
		FiltroData fdInicio = new FiltroData("fisNotaEntrada.fisNotaEntradaCadastro", ECompara.MAIOR_IGUAL, inicio);
		gf.add(fdInicio, EJuncao.E);
		FiltroData fdFim = new FiltroData("fisNotaEntrada.fisNotaEntradaCadastro", ECompara.MENOR_IGUAL, fim);
		gf.add(fdFim);
		// seleciona as compras
		return service.selecionar(new ComCompra(), 0, 0, gf, false).getLista();
	}

	// metodo que recupera os fretes
	private List<ComFrete> getFretes() throws Exception {
		// monta o filtro
		GrupoFiltro gf = new GrupoFiltro();
		FiltroObjeto fo = new FiltroObjeto("empEmpresa", ECompara.IGUAL, sped.getEmpEmpresa());
		gf.add(fo, EJuncao.E);
		FiltroData fdInicio = new FiltroData("comFreteRecebimento", ECompara.MAIOR_IGUAL, inicio);
		gf.add(fdInicio, EJuncao.E);
		FiltroData fdFim = new FiltroData("comFreteRecebimento", ECompara.MENOR_IGUAL, fim);
		gf.add(fdFim);
		// seleciona os fretes
		return service.selecionar(new ComFrete(), 0, 0, gf, false).getLista();
	}

	// metodo que recupera as vendas
	private List<ComVenda> getVendas() throws Exception {
		// monta o filtro
		GrupoFiltro gf = new GrupoFiltro();
		FiltroBinario fb = new FiltroBinario("comVendaNfe", ECompara.IGUAL, 1);
		gf.add(fb, EJuncao.E);
		FiltroObjeto fo = new FiltroObjeto("empEmpresa", ECompara.IGUAL, sped.getEmpEmpresa());
		gf.add(fo, EJuncao.E);
		FiltroData fdInicio = new FiltroData("fisNotaSaida.fisNotaSaidaData", ECompara.MAIOR_IGUAL, inicio);
		gf.add(fdInicio, EJuncao.E);
		FiltroData fdFim = new FiltroData("fisNotaSaida.fisNotaSaidaData", ECompara.MENOR_IGUAL, fim);
		gf.add(fdFim);
		// seleciona todos as vendas
		return service.selecionar(new ComVenda(), 0, 0, gf, false).getLista();
	}

	// metodo que recupera as notas do consumidor
	private List<ComEcfNota> getNotas() throws Exception {
		// monta o filtro
		GrupoFiltro gf = new GrupoFiltro();
		FiltroObjeto fo = new FiltroObjeto("empEmpresa", ECompara.IGUAL, sped.getEmpEmpresa());
		gf.add(fo, EJuncao.E);
		FiltroData fdInicio = new FiltroData("comEcfNotaData", ECompara.MAIOR_IGUAL, inicio);
		gf.add(fdInicio, EJuncao.E);
		FiltroData fdFim = new FiltroData("comEcfNotaData", ECompara.MENOR_IGUAL, fim);
		gf.add(fdFim);
		// seleciona os fretes
		return service.selecionar(new ComEcfNota(), 0, 0, gf, false).getLista();
	}

	// metodo que recupera as leituras Zs
	private List<ComEcfZ> getZs() throws Exception {
		// monta o filtro
		GrupoFiltro gf = new GrupoFiltro();
		FiltroData fd1 = new FiltroData("comEcfZMovimento", ECompara.MAIOR_IGUAL, inicio);
		gf.add(fd1, EJuncao.E);
		FiltroData fd2 = new FiltroData("comEcfZMovimento", ECompara.MENOR_IGUAL, fim);
		gf.add(fd2, EJuncao.E);
		FiltroBinario fb = new FiltroBinario("comEcf.comEcfAtivo", ECompara.IGUAL, 1);
		gf.add(fb);

		// modificando a ordem
		ComEcfZ z = new ComEcfZ();
		z.setCampoOrdem("t.comEcf.comEcfId,t.comEcfZMovimento");
		z.setOrdemDirecao(EDirecao.ASC);

		// seleciona todos as vendas da ecf
		return service.selecionar(z, 0, 0, gf, false).getLista();
	}

	// metodo que recupera os produtos do estoque
	private List<ProdProduto> getEstoque() throws Exception {
		if (sped.getFisSpedFiscalMes() == 2) {
			// seleciona todos os produtos com estoque maior que ZERO
			FiltroObjeto fo = new FiltroObjeto("t1.empEmpresa", ECompara.IGUAL, sped.getEmpEmpresa());
			FiltroNumero fn = new FiltroNumero("t1.prodEstoqueQuantidade", ECompara.MAIOR, 0);
			GrupoFiltro gf = new GrupoFiltro(EJuncao.E, new IFiltro[] { fo, fn });
			Lista<ProdProduto> lista = service.selecionar(new ProdProduto(), 0, 0, gf, false);
			return lista.getLista();
		} else {
			return null;
		}
	}

	// Metodo que recupera os registros selecionados e chama a execucao de cada um
	private void escreverRegistros() throws Exception {
		// contagem das linhas do bloco e do arquivo
		int qtdBloco = 0;
		int qtdArquivo = 0;
		FileWriter escritor = new FileWriter(arquivo, true);
		// para cada registro instancia sua classe e executa o comando
		for (FisSpedBloco bloco : blocos) {
			if (bloco.getFisSpedBlocoNivel() < 3) {
				try {
					Class<IRegistro> classe = (Class<IRegistro>) Class.forName(bloco.getFisSpedBlocoClasse());
					IRegistro reg = classe.newInstance();
					if (bloco.getFisSpedBlocoClasse().endsWith("9999")) {
						reg.setQtdLInhas(qtdArquivo);
					} else if (bloco.getFisSpedBlocoClasse().endsWith("990")) {
						reg.setQtdLInhas(qtdBloco);
					} else {
						reg.setQtdLInhas(0);
					}
					reg.setLeitor(arquivo);
					reg.setEscritor(escritor);
					reg.setSped(sped);
					reg.setService(service);
					reg.setAuth(auth);
					reg.setInicio(inicio);
					reg.setFim(fim);
					reg.setBlocos(blocos);
					reg.setCompras(compras);
					reg.setFretes(fretes);
					reg.setVendas(vendas);
					reg.setNotas(notas);
					reg.setZs(zs);
					reg.setEstoque(estoque);
					reg.setPis(pis);
					reg.setCofins(cofins);
					reg.executar();
					// marcando as qtds
					qtdArquivo += reg.getQtdLinhas();
					qtdBloco = reg.getFimBloco() ? 0 : qtdBloco + reg.getQtdLinhas();
				} catch (Exception e) {
					UtilServer.LOG.error("Erro na execucao do registro " + bloco.getFisSpedBlocoClasse(), e);
				}
			}
		}
		escritor.flush();
		escritor.close();
	}
}
