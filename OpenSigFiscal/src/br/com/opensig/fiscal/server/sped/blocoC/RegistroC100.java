package br.com.opensig.fiscal.server.sped.blocoC;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.beanio.BeanWriter;
import org.beanio.StreamFactory;

import br.com.opensig.comercial.shared.modelo.ComCompra;
import br.com.opensig.comercial.shared.modelo.ComVenda;
import br.com.opensig.core.server.UtilServer;
import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.fiscal.server.sped.ARegistro;
import br.com.opensig.nfe.TNFe;
import br.com.opensig.nfe.TNFe.InfNFe.Det;
import br.com.opensig.nfe.TNFe.InfNFe.Ide;
import br.com.opensig.nfe.TNFe.InfNFe.Total.ICMSTot;
import br.com.opensig.nfe.TNFe.InfNFe.Transp;

public class RegistroC100 extends ARegistro<DadosC100, Dados> {

	private Map<String, List<DadosC170>> analitico = new HashMap<String, List<DadosC170>>();

	@Override
	public void executar() {
		try {
			StreamFactory factory = StreamFactory.newInstance();
			factory.load(getClass().getResourceAsStream(bean));
			BeanWriter out = factory.createWriter("EFD", escritor);
			TNFe nfe = null;

			// processa as entradas / compras
			for (ComCompra compra : compras) {
				// pega a NFe
				String xml = compra.getFisNotaEntrada().getFisNotaEntradaXml();
				int I = xml.indexOf("<infNFe");
				int F = xml.indexOf("</NFe>") + 6;
				String texto = "<NFe xmlns=\"http://www.portalfiscal.inf.br/nfe\">" + xml.substring(I, F);
				nfe = UtilServer.xmlToObj(texto, "br.com.opensig.nfe");
				DadosC100 obj = getDados(nfe, "00", compra.getComCompraRecebimento());
				// seta os dados padrao
				obj.setInd_oper("0");
				obj.setInd_emit(compra.getEmpFornecedor().getEmpEntidade().getEmpEntidadeDocumento1() == compra.getEmpEmpresa().getEmpEntidade().getEmpEntidadeDocumento1() ? "0" : "1");
				obj.setCod_part(compra.getEmpFornecedor().getEmpEntidade().getEmpEntidadeId() + "");
				out.write(obj);
				out.flush();

				// informacoes da nota
				if (!compra.getComCompraObservacao().equals("")) {
					RegistroC110 r110 = new RegistroC110();
					r110.setDados(compra.getComCompraObservacao());
					r110.setEscritor(escritor);
					r110.executar();
					qtdLinhas += r110.getQtdLinhas();
				}

				// produtos
				RegistroC170 r170 = new RegistroC170();
				r170.setEscritor(escritor);
				r170.setCrt(nfe.getInfNFe().getEmit().getCRT());
				r170.setNatId(compra.getComNatureza().getComNaturezaId());
				r170.setVenda(false);
				int item = 0;
				for (Det det : nfe.getInfNFe().getDet()) {
					r170.setProduto(compra.getComCompraProdutos().get(item++).getProdProduto());
					r170.setDados(det);
					r170.executar();
					setAnalitico(r170.getBloco());
					qtdLinhas += r170.getQtdLinhas();
				}

				// analitico das compras
				getAnalitico();
			}

			// processa as saidas / vendas
			for (ComVenda venda : vendas) {
				DadosC100 obj = null;
				String cod_sit = venda.getComVendaCancelada() ? "02" : "00";

				try {
					// pega a NFe
					String xml = venda.getFisNotaSaida().getFisNotaSaidaXml();
					int I = xml.indexOf("<infNFe");
					int F = xml.indexOf("</NFe>") + 6;
					String texto = "<NFe xmlns=\"http://www.portalfiscal.inf.br/nfe\">" + xml.substring(I, F);

					nfe = UtilServer.xmlToObj(texto, "br.com.opensig.nfe");
					obj = getDados(nfe, cod_sit, venda.getComVendaData());
				} catch (Exception e) {
					// TODO inutilizada
					continue;
				}

				obj.setInd_oper("1");
				obj.setInd_emit("0");
				if (cod_sit.equals("00")) {
					obj.setCod_part(venda.getEmpCliente().getEmpEntidade().getEmpEntidadeId() + "");
				}
				out.write(obj);
				out.flush();

				// so para vendas nao canceladas
				if (!venda.getComVendaCancelada()) {
					// informacoes da nota
					if (!venda.getComVendaObservacao().equals("")) {
						RegistroC110 r110 = new RegistroC110();
						r110.setDados(venda.getComVendaObservacao());
						r110.setEscritor(escritor);
						r110.executar();
						qtdLinhas += r110.getQtdLinhas();
					}

					// produtos
					RegistroC170 r170 = new RegistroC170();
					r170.setCrt(nfe.getInfNFe().getEmit().getCRT());
					r170.setNatId(venda.getComNatureza().getComNaturezaId());
					r170.setVenda(true);
					int item = 0;
					// para NFe de saida nao precisa informar os produtos
					for (Det det : nfe.getInfNFe().getDet()) {
						r170.setProduto(venda.getComVendaProdutos().get(item++).getProdProduto());
						setAnalitico(r170.getDados(det));
					}

					// analitico da venda
					getAnalitico();
				}
			}
		} catch (Exception e) {
			qtdLinhas = 0;
			UtilServer.LOG.error("Erro na geracao do Registro -> " + bean, e);
		}
	}

	@Override
	protected DadosC100 getDados(Dados dados) throws Exception {
		return null;
	}

	private DadosC100 getDados(TNFe nfe, String cod_sit, Date data) throws Exception {
		Ide ide = nfe.getInfNFe().getIde();
		ICMSTot icms = nfe.getInfNFe().getTotal().getICMSTot();
		Transp transp = nfe.getInfNFe().getTransp();

		DadosC100 d = new DadosC100();
		d.setCod_sit(cod_sit);
		d.setSer(ide.getSerie());
		d.setCod_mod("55");
		d.setChv_nfe(nfe.getInfNFe().getId().replace("NFe", ""));
		d.setNum_doc(Integer.valueOf(ide.getNNF()));

		if (cod_sit.equals("00")) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			d.setDt_doc(sdf.parse(ide.getDEmi()));
			// data de saida/entrada
			if (ide.getDSaiEnt() != null) {
				Date e_s = sdf.parse(ide.getDSaiEnt());
				data = e_s.compareTo(data) == 1 ? e_s : data;
			} else {
				data = d.getDt_doc().compareTo(data) == 1 ? d.getDt_doc() : data;
			}
			d.setDt_e_s(data);
			d.setVl_doc(Double.valueOf(icms.getVNF()));
			d.setInd_pgto(ide.getIndPag());
			d.setVl_desc(Double.valueOf(icms.getVDesc()));
			d.setVl_merc(Double.valueOf(icms.getVProd()));
			d.setInd_frt(transp.getModFrete());
			d.setVl_frt(Double.valueOf(icms.getVFrete()));
			d.setVl_seg(Double.valueOf(icms.getVSeg()));
			d.setVl_out_da(Double.valueOf(icms.getVOutro()));
			d.setVl_bc_icms(Double.valueOf(icms.getVBC()));
			d.setVl_icms(Double.valueOf(icms.getVICMS()));
			d.setVl_bc_icms_st(Double.valueOf(icms.getVBCST()));
			d.setVl_icms_st(Double.valueOf(icms.getVST()));
			d.setVl_ipi(Double.valueOf(icms.getVIPI()));
			d.setVl_pis(Double.valueOf(icms.getVPIS()));
			d.setVl_cofins(Double.valueOf(icms.getVCOFINS()));
		}

		normalizar(d);
		qtdLinhas++;
		return d;
	}

	private void setAnalitico(DadosC170 d) {
		String chave = d.getCst_icms() + d.getCfop() + d.getAliq_icms();
		List<DadosC170> lista = analitico.get(chave);
		if (lista == null) {
			lista = new ArrayList<DadosC170>();
			lista.add(d);
			analitico.put(chave, lista);
		} else {
			lista.add(d);
		}
	}

	private void getAnalitico() {
		if (!analitico.isEmpty()) {
			RegistroC190 r190 = new RegistroC190();
			r190.setEscritor(escritor);
			r190.setAuth(auth);
			for (Entry<String, List<DadosC170>> entry : analitico.entrySet()) {
				r190.setDados(entry.getValue());
				r190.executar();
				qtdLinhas += r190.getQtdLinhas();
			}
		}
		analitico.clear();
	}
}
