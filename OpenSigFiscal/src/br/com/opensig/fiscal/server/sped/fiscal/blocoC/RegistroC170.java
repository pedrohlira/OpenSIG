package br.com.opensig.fiscal.server.sped.fiscal.blocoC;

import br.com.opensig.core.server.UtilServer;
import br.com.opensig.fiscal.server.sped.ARegistro;
import br.com.opensig.nfe.TNFe.InfNFe.Det;
import br.com.opensig.nfe.TNFe.InfNFe.Det.Imposto.COFINS;
import br.com.opensig.nfe.TNFe.InfNFe.Det.Imposto.ICMS;
import br.com.opensig.nfe.TNFe.InfNFe.Det.Imposto.IPI;
import br.com.opensig.nfe.TNFe.InfNFe.Det.Imposto.PIS;
import br.com.opensig.nfe.TNFe.InfNFe.Det.Prod;
import br.com.opensig.produto.shared.modelo.ProdProduto;

public class RegistroC170 extends ARegistro<DadosC170, Det> {

	private ProdProduto produto;
	private int natId;
	private int item;
	private boolean venda;

	@Override
	protected DadosC170 getDados(Det dados) throws Exception {
		Prod prod = dados.getProd();

		DadosC170 d = new DadosC170();
		d.setNum_item(item);
		d.setCod_item(produto.getProdProdutoId() + "");
		d.setDescr_compl("");
		d.setQtd(Double.valueOf(prod.getQCom()));
		d.setUnid(produto.getProdEmbalagem().getProdEmbalagemNome());
		d.setVl_item(Double.valueOf(prod.getVProd()));
		d.setVl_desc(prod.getVDesc() == null ? 0.00 : Double.valueOf(prod.getVDesc()));
		d.setVl_frete(prod.getVFrete() == null ? 0.00 : Double.valueOf(prod.getVFrete()));
		d.setVl_seguro(prod.getVSeg() == null ? 0.00 : Double.valueOf(prod.getVSeg()));
		d.setVl_outros(prod.getVOutro() == null ? 0.00 : Double.valueOf(prod.getVOutro()));
		d.setInd_mov("0");
		int cfop = Integer.valueOf(prod.getCFOP());
		// adaptacao do cfop das compras
		if (!venda) {
			// se o cfop faz referencia a um cupom fiscal
			if (cfop == 5929 || cfop == 6929) {
				cfop -= 4827;
			} else if (cfop >= 5000) {
				cfop -= 4000;
				// regra para entrada
				if (cfop > 2102) {
					cfop = auth.getConf().get("sped.fiscal.0000.ind_ativ").equals("0") ? 2101 : 2102;
				} else if (cfop < 2101) {
					cfop = auth.getConf().get("sped.fiscal.0000.ind_ativ").equals("0") ? 1101 : 1102;
				}
			}
		}
		d.setCfop(cfop);
		d.setCod_nat(natId + "");

		try {
			ICMS icms = dados.getImposto().getICMS();
			if (auth.getConf().get("nfe.crt").equals("1")) {
				// verifica qual icms CSON foi usado
				if (icms.getICMSSN101() != null) {
					d.setCst_icms(icms.getICMSSN101().getCSOSN());
					d.setVl_bc_icms(0.00);
					d.setAliq_icms(0.00);
					d.setVl_icms(0.00);
				} else if (icms.getICMSSN102() != null) {
					d.setCst_icms(icms.getICMSSN102().getCSOSN());
					d.setVl_bc_icms(0.00);
					d.setAliq_icms(0.00);
					d.setVl_icms(0.00);
				} else if (icms.getICMSSN201() != null) {
					d.setCst_icms(icms.getICMSSN201().getCSOSN());
					d.setVl_bc_icms_st(Double.valueOf(icms.getICMSSN201().getVBCST()));
					d.setAliq_st(Double.valueOf(icms.getICMSSN201().getPICMSST()));
					d.setVl_icms_st(Double.valueOf(icms.getICMSSN201().getVICMSST()));
				} else if (icms.getICMSSN202() != null) {
					d.setCst_icms(icms.getICMSSN202().getCSOSN());
					d.setVl_bc_icms_st(Double.valueOf(icms.getICMSSN202().getVBCST()));
					d.setAliq_st(Double.valueOf(icms.getICMSSN202().getPICMSST()));
					d.setVl_icms_st(Double.valueOf(icms.getICMSSN202().getVICMSST()));
				} else if (icms.getICMSSN500() != null) {
					d.setCst_icms(icms.getICMSSN500().getCSOSN());
					d.setVl_bc_icms_st(Double.valueOf(icms.getICMSSN500().getVBCSTRet()));
					d.setVl_icms_st(Double.valueOf(icms.getICMSSN500().getVICMSSTRet()));
					d.setAliq_st(d.getVl_icms_st() * 100 / d.getVl_bc_icms_st());
				} else if (icms.getICMSSN900() != null) {
					d.setCst_icms(icms.getICMSSN900().getCSOSN());
					if (icms.getICMSSN900().getModBC() != null) {
						d.setVl_bc_icms(Double.valueOf(icms.getICMSSN900().getVBC()));
						d.setAliq_icms(Double.valueOf(icms.getICMSSN900().getPICMS()));
						d.setVl_icms(Double.valueOf(icms.getICMSSN900().getVICMS()));
					} else {
						d.setVl_bc_icms(Double.valueOf(icms.getICMSSN900().getVBCST()));
						d.setAliq_icms(Double.valueOf(icms.getICMSSN900().getPICMSST()));
						d.setVl_icms(Double.valueOf(icms.getICMSSN900().getVICMSST()));
					}
				}
			} else {
				// verifica qual icms CST foi usado
				if (icms.getICMS00() != null) {
					d.setCst_icms("0" + icms.getICMS00().getCST());
					d.setVl_bc_icms(Double.valueOf(icms.getICMS00().getVBC()));
					d.setAliq_icms(Double.valueOf(icms.getICMS00().getPICMS()));
					d.setVl_icms(Double.valueOf(icms.getICMS00().getVICMS()));
				} else if (icms.getICMS10() != null) {
					d.setCst_icms("0" + icms.getICMS10().getCST());
					d.setVl_bc_icms_st(Double.valueOf(icms.getICMS10().getVBCST()));
					d.setAliq_st(Double.valueOf(icms.getICMS10().getPICMSST()));
					d.setVl_icms_st(Double.valueOf(icms.getICMS10().getVICMSST()));
				} else if (icms.getICMS20() != null) {
					d.setCst_icms("0" + icms.getICMS20().getCST());
					d.setVl_bc_icms(Double.valueOf(icms.getICMS20().getVBC()));
					d.setAliq_icms(Double.valueOf(icms.getICMS20().getPICMS()));
					d.setVl_icms(Double.valueOf(icms.getICMS20().getVICMS()));
				} else if (icms.getICMS30() != null) {
					d.setCst_icms("0" + icms.getICMS30().getCST());
					d.setVl_bc_icms_st(Double.valueOf(icms.getICMS30().getVBCST()));
					d.setAliq_st(Double.valueOf(icms.getICMS30().getPICMSST()));
					d.setVl_icms_st(Double.valueOf(icms.getICMS30().getVICMSST()));
				} else if (icms.getICMS40() != null) {
					d.setCst_icms("0" + icms.getICMS40().getCST());
					d.setVl_bc_icms(0.00);
					d.setAliq_icms(0.00);
					d.setVl_icms(0.00);
				} else if (icms.getICMS51() != null) {
					d.setCst_icms("0" + icms.getICMS51().getCST());
					d.setVl_bc_icms(Double.valueOf(icms.getICMS51().getVBC()));
					d.setAliq_icms(Double.valueOf(icms.getICMS51().getPICMS()));
					d.setVl_icms(Double.valueOf(icms.getICMS51().getVICMS()));
				} else if (icms.getICMS60() != null) {
					d.setCst_icms("0" + icms.getICMS60().getCST());
					d.setVl_bc_icms_st(Double.valueOf(icms.getICMS60().getVBCSTRet()));
					d.setAliq_st(d.getVl_icms_st() * 100 / d.getVl_bc_icms_st());
					d.setVl_icms_st(Double.valueOf(icms.getICMS60().getVICMSSTRet()));
				} else if (icms.getICMS70() != null) {
					d.setCst_icms("0" + icms.getICMS70().getCST());
					if (icms.getICMS70().getModBC() != null) {
						d.setVl_bc_icms(Double.valueOf(icms.getICMS70().getVBC()));
						d.setAliq_icms(Double.valueOf(icms.getICMS70().getPICMS()));
						d.setVl_icms(Double.valueOf(icms.getICMS70().getVICMS()));
					} else {
						d.setVl_bc_icms_st(Double.valueOf(icms.getICMS70().getVBCST()));
						d.setVl_icms_st(Double.valueOf(icms.getICMS70().getVICMSST()));
						d.setAliq_st(Double.valueOf(icms.getICMS70().getPICMSST()));
					}
				} else if (icms.getICMS90() != null) {
					d.setCst_icms("0" + icms.getICMS90().getCST());
					if (icms.getICMS90().getModBC() != null) {
						d.setVl_bc_icms(Double.valueOf(icms.getICMS90().getVBC()));
						d.setAliq_icms(Double.valueOf(icms.getICMS90().getPICMS()));
						d.setVl_icms(Double.valueOf(icms.getICMS90().getVICMS()));
					} else {
						d.setVl_bc_icms_st(Double.valueOf(icms.getICMS90().getVBCST()));
						d.setVl_icms_st(Double.valueOf(icms.getICMS90().getVICMSST()));
						d.setAliq_st(Double.valueOf(icms.getICMS90().getPICMSST()));
					}
				}
			}
		} catch (Exception e) {
			d.setCst_icms("000");
			d.setVl_bc_icms(0.00);
			d.setAliq_icms(0.00);
			d.setVl_icms(0.00);
		}

		// ipi
		d.setInd_apur("0");
		d.setCod_enq("");
		IPI ipi = dados.getImposto().getIPI();
		if (ipi != null) {
			try {
				// recupera o ipi tributado
				d.setCst_ipi(ipi.getIPITrib().getCST());
				d.setVl_bc_ipi(Double.valueOf(ipi.getIPITrib().getVBC()));
				d.setAliq_ipi(Double.valueOf(ipi.getIPITrib().getPIPI()));
				d.setVl_ipi(Double.valueOf(ipi.getIPITrib().getVIPI()));
			} catch (Exception e) {
				try {
					// recupera o ipi nao tributado
					d.setCst_ipi(ipi.getIPINT().getCST());
					d.setVl_ipi(0.00);
				} catch (Exception ex) {
					// caso nao tenha informado
					d.setCst_ipi(venda ? "99" : "49");
					d.setVl_ipi(0.00);
				}
			} finally {
				int cst_ipi = Integer.valueOf(d.getCst_ipi());
				// adaptacao do ipi das compras
				if (!venda && cst_ipi >= 50) {
					cst_ipi -= 50;
				}
				d.setCst_ipi(UtilServer.formataNumero(cst_ipi, 2, 0, false));
			}
		} else {
			d.setCst_ipi("");
			d.setVl_ipi(0.00);
		}

		// pis
		PIS pis = dados.getImposto().getPIS();
		if (pis != null) {
			try {
				if (pis.getPISAliq() != null) {
					d.setCst_pis(pis.getPISAliq().getCST());
					d.setVl_bc_pis(Double.valueOf(pis.getPISAliq().getVBC()));
					d.setAliq_pis(Double.valueOf(pis.getPISAliq().getPPIS()));
					d.setAliq2_pis(null);
					d.setVl_pis(Double.valueOf(pis.getPISAliq().getVPIS()));
				} else if (pis.getPISNT() != null) {
					d.setCst_pis(pis.getPISNT().getCST());
				} else if (pis.getPISOutr() != null) {
					d.setCst_pis(pis.getPISOutr().getCST());
					d.setVl_bc_pis(Double.valueOf(pis.getPISOutr().getVBC()));
					d.setAliq_pis(Double.valueOf(pis.getPISOutr().getPPIS()));
					d.setAliq2_pis(null);
					d.setQuant_bc_pis(pis.getPISOutr().getQBCProd() == null ? 0.00 : Double.valueOf(pis.getPISOutr().getQBCProd()));
					d.setVl_pis(Double.valueOf(pis.getPISOutr().getVPIS()));
				} else if (pis.getPISQtde() != null) {
					d.setCst_pis(pis.getPISQtde().getCST());
					d.setAliq_pis(null);
					d.setAliq2_pis(pis.getPISQtde().getVAliqProd() == null ? 0.00 : Double.valueOf(pis.getPISQtde().getVAliqProd()));
					d.setQuant_bc_pis(pis.getPISQtde().getQBCProd() == null ? 0.00 : Double.valueOf(pis.getPISQtde().getQBCProd()));
					d.setVl_pis(Double.valueOf(pis.getPISQtde().getVPIS()));
				}
			} catch (Exception e) {
				d.setCst_pis("");
			}
		} else {
			d.setCst_pis("");
		}

		// cofins
		COFINS cofins = dados.getImposto().getCOFINS();
		if (cofins != null) {
			try {
				if (cofins.getCOFINSAliq() != null) {
					d.setCst_cofins(cofins.getCOFINSAliq().getCST());
					d.setVl_bc_cofins(Double.valueOf(cofins.getCOFINSAliq().getVBC()));
					d.setAliq_cofins(Double.valueOf(cofins.getCOFINSAliq().getPCOFINS()));
					d.setAliq2_cofins(null);
					d.setVl_cofins(Double.valueOf(cofins.getCOFINSAliq().getVCOFINS()));
				} else if (cofins.getCOFINSNT() != null) {
					d.setCst_cofins(cofins.getCOFINSNT().getCST());
				} else if (cofins.getCOFINSOutr() != null) {
					d.setCst_cofins(cofins.getCOFINSOutr().getCST());
					d.setVl_bc_cofins(Double.valueOf(cofins.getCOFINSOutr().getVBC()));
					d.setAliq_cofins(Double.valueOf(cofins.getCOFINSOutr().getPCOFINS()));
					d.setAliq2_cofins(null);
					d.setQuant_bc_cofins(cofins.getCOFINSOutr().getQBCProd() == null ? 0.00 : Double.valueOf(cofins.getCOFINSOutr().getQBCProd()));
					d.setVl_cofins(Double.valueOf(cofins.getCOFINSOutr().getVCOFINS()));
				} else if (cofins.getCOFINSQtde() != null) {
					d.setCst_cofins(cofins.getCOFINSQtde().getCST());
					d.setAliq_cofins(null);
					d.setAliq2_cofins(cofins.getCOFINSQtde().getVAliqProd() == null ? 0.00 : Double.valueOf(cofins.getCOFINSQtde().getVAliqProd()));
					d.setQuant_bc_cofins(cofins.getCOFINSQtde().getQBCProd() == null ? 0.00 : Double.valueOf(cofins.getCOFINSQtde().getQBCProd()));
					d.setVl_cofins(Double.valueOf(cofins.getCOFINSQtde().getVCOFINS()));
				}
			} catch (Exception e) {
				d.setCst_cofins("");
			}
		} else {
			d.setCst_cofins("");
		}

		return d;
	}

	public ProdProduto getProduto() {
		return produto;
	}

	public void setProduto(ProdProduto produto) {
		this.produto = produto;
	}

	public boolean getVenda() {
		return this.venda;
	}

	public void setVenda(boolean venda) {
		this.venda = venda;
	}

	public int getNatId() {
		return natId;
	}

	public void setNatId(int natId) {
		this.natId = natId;
	}

	public int getItem() {
		return item;
	}

	public void setItem(int item) {
		this.item = item;
	}
}
