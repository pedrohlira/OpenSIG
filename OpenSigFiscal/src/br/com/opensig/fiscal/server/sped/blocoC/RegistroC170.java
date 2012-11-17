package br.com.opensig.fiscal.server.sped.blocoC;

import br.com.opensig.core.server.UtilServer;
import br.com.opensig.fiscal.server.sped.ARegistro;
import br.com.opensig.nfe.TNFe.InfNFe.Det;
import br.com.opensig.nfe.TNFe.InfNFe.Det.Imposto.COFINS;
import br.com.opensig.nfe.TNFe.InfNFe.Det.Imposto.ICMS.ICMS00;
import br.com.opensig.nfe.TNFe.InfNFe.Det.Imposto.ICMS.ICMS10;
import br.com.opensig.nfe.TNFe.InfNFe.Det.Imposto.ICMS.ICMS20;
import br.com.opensig.nfe.TNFe.InfNFe.Det.Imposto.ICMS.ICMS30;
import br.com.opensig.nfe.TNFe.InfNFe.Det.Imposto.ICMS.ICMS40;
import br.com.opensig.nfe.TNFe.InfNFe.Det.Imposto.ICMS.ICMS51;
import br.com.opensig.nfe.TNFe.InfNFe.Det.Imposto.ICMS.ICMS60;
import br.com.opensig.nfe.TNFe.InfNFe.Det.Imposto.ICMS.ICMS70;
import br.com.opensig.nfe.TNFe.InfNFe.Det.Imposto.ICMS.ICMS90;
import br.com.opensig.nfe.TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN101;
import br.com.opensig.nfe.TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN102;
import br.com.opensig.nfe.TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN201;
import br.com.opensig.nfe.TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN202;
import br.com.opensig.nfe.TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN500;
import br.com.opensig.nfe.TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN900;
import br.com.opensig.nfe.TNFe.InfNFe.Det.Imposto.IPI;
import br.com.opensig.nfe.TNFe.InfNFe.Det.Imposto.PIS;
import br.com.opensig.nfe.TNFe.InfNFe.Det.Prod;
import br.com.opensig.produto.shared.modelo.ProdProduto;

public class RegistroC170 extends ARegistro<DadosC170, Det> {

	private ProdProduto produto;
	private String crt;
	private int natId;
	private boolean venda;

	public RegistroC170() {
		super("/br/com/opensig/fiscal/server/sped/blocoC/BeanC170.xml");
	}

	@Override
	protected DadosC170 getDados(Det dados) throws Exception {
		Prod prod = dados.getProd();

		DadosC170 d = new DadosC170();
		d.setNum_item(Integer.valueOf(dados.getNItem()));
		d.setCod_item(produto.getProdProdutoId() + "");
		d.setDescr_compl("");
		d.setQtd(Double.valueOf(prod.getQCom()));
		d.setUnid(produto.getProdEmbalagem().getProdEmbalagemNome());
		d.setVl_item(Double.valueOf(prod.getVProd()));
		d.setVl_desc(prod.getVDesc() == null ? 0.00 : Double.valueOf(prod.getVDesc()));
		d.setInd_mov("0");
		int cfop = Integer.valueOf(prod.getCFOP());
		if (!venda) {
			if (cfop == 5929 || cfop == 6929) {
				cfop -= 4827;
			} else if (cfop >= 5000) {
				cfop -= 4000;
			}
		}
		d.setCfop(cfop);
		d.setCod_nat(natId + "");

		try {
			if (crt.equals("1")) {
				ICMSSN101 icms101 = dados.getImposto().getICMS().getICMSSN101();
				if (icms101 != null) {
					d.setCst_icms(icms101.getCSOSN());
					d.setVl_bc_icms(d.getVl_item());
					d.setAliq_icms(Double.valueOf(icms101.getPCredSN()));
					d.setVl_icms(Double.valueOf(icms101.getVCredICMSSN()));
				} else {
					ICMSSN102 icms102 = dados.getImposto().getICMS().getICMSSN102();
					if (icms102 != null) {
						d.setCst_icms(icms102.getCSOSN());
					} else {
						ICMSSN201 icms201 = dados.getImposto().getICMS().getICMSSN201();
						if (icms201 != null) {
							d.setCst_icms(icms201.getCSOSN());
							d.setVl_bc_icms_st(Double.valueOf(icms201.getVBCST()));
							d.setAliq_st(Double.valueOf(icms201.getPICMSST()));
							d.setVl_icms_st(Double.valueOf(icms201.getVICMSST()));
						} else {
							ICMSSN202 icms202 = dados.getImposto().getICMS().getICMSSN202();
							if (icms202 != null) {
								d.setCst_icms(icms202.getCSOSN());
								d.setVl_bc_icms_st(Double.valueOf(icms202.getVBCST()));
								d.setAliq_st(Double.valueOf(icms202.getPICMSST()));
								d.setVl_icms_st(Double.valueOf(icms202.getVICMSST()));
							} else {
								ICMSSN500 icms500 = dados.getImposto().getICMS().getICMSSN500();
								if (icms500 != null) {
									d.setCst_icms(icms500.getCSOSN());
									d.setVl_bc_icms_st(Double.valueOf(icms500.getVBCSTRet()));
									d.setVl_icms_st(Double.valueOf(icms500.getVICMSSTRet()));
									d.setAliq_st(d.getVl_icms_st() * 100 / d.getVl_bc_icms_st());
								} else {
									ICMSSN900 icms900 = dados.getImposto().getICMS().getICMSSN900();
									d.setCst_icms(icms900.getCSOSN());
									if (icms900.getModBC() != null) {
										d.setVl_bc_icms(Double.valueOf(icms900.getVBC()));
										d.setAliq_icms(Double.valueOf(icms900.getPICMS()));
										d.setVl_icms(Double.valueOf(icms900.getVICMS()));
									} else {
										d.setVl_bc_icms(Double.valueOf(icms900.getVBCST()));
										d.setAliq_icms(Double.valueOf(icms900.getPICMSST()));
										d.setVl_icms(Double.valueOf(icms900.getVICMSST()));
									}
								}
							}
						}
					}
				}
			} else {
				ICMS00 icms00 = dados.getImposto().getICMS().getICMS00();
				if (icms00 != null) {
					d.setCst_icms("0" + icms00.getCST());
					d.setVl_bc_icms(Double.valueOf(icms00.getVBC()));
					d.setAliq_icms(Double.valueOf(icms00.getPICMS()));
					d.setVl_icms(Double.valueOf(icms00.getVICMS()));
				} else {
					ICMS10 icms10 = dados.getImposto().getICMS().getICMS10();
					if (icms10 != null) {
						d.setCst_icms("0" + icms10.getCST());
						d.setVl_bc_icms_st(Double.valueOf(icms10.getVBCST()));
						d.setAliq_st(Double.valueOf(icms10.getPICMSST()));
						d.setVl_icms_st(Double.valueOf(icms10.getVICMSST()));
					} else {
						ICMS20 icms20 = dados.getImposto().getICMS().getICMS20();
						if (icms20 != null) {
							d.setCst_icms("0" + icms20.getCST());
							d.setVl_bc_icms(Double.valueOf(icms20.getVBC()));
							d.setAliq_icms(Double.valueOf(icms20.getPICMS()));
							d.setVl_icms(Double.valueOf(icms20.getVICMS()));
						} else {
							ICMS30 icms30 = dados.getImposto().getICMS().getICMS30();
							if (icms30 != null) {
								d.setCst_icms("0" + icms30.getCST());
								d.setVl_bc_icms_st(Double.valueOf(icms30.getVBCST()));
								d.setAliq_st(Double.valueOf(icms30.getPICMSST()));
								d.setVl_icms_st(Double.valueOf(icms30.getVICMSST()));
							} else {
								ICMS40 icms40 = dados.getImposto().getICMS().getICMS40();
								if (icms40 != null) {
									d.setCst_icms("0" + icms40.getCST());
								} else {
									ICMS51 icms51 = dados.getImposto().getICMS().getICMS51();
									if (icms51 != null) {
										d.setCst_icms("0" + icms51.getCST());
										d.setVl_bc_icms(Double.valueOf(icms51.getVBC()));
										d.setAliq_icms(Double.valueOf(icms51.getPICMS()));
										d.setVl_icms(Double.valueOf(icms51.getVICMS()));
									} else {
										ICMS60 icms60 = dados.getImposto().getICMS().getICMS60();
										if (icms60 != null) {
											d.setCst_icms("0" + icms60.getCST());
											d.setVl_bc_icms_st(Double.valueOf(icms60.getVBCSTRet()));
											d.setVl_icms_st(Double.valueOf(icms60.getVICMSSTRet()));
										} else {
											ICMS70 icms70 = dados.getImposto().getICMS().getICMS70();
											if (icms70 != null) {
												d.setCst_icms("0" + icms70.getCST());
												if (icms70.getModBC() != null) {
													d.setVl_bc_icms(Double.valueOf(icms70.getVBC()));
													d.setAliq_icms(Double.valueOf(icms70.getPICMS()));
													d.setVl_icms(Double.valueOf(icms70.getVICMS()));
												} else {
													d.setVl_bc_icms_st(Double.valueOf(icms70.getVBCST()));
													d.setVl_icms_st(Double.valueOf(icms70.getVICMSST()));
													d.setAliq_st(Double.valueOf(icms70.getPICMSST()));
												}
											} else {
												ICMS90 icms90 = dados.getImposto().getICMS().getICMS90();
												d.setCst_icms("0" + icms90.getCST());
												if (icms90.getModBC() != null) {
													d.setVl_bc_icms(Double.valueOf(icms90.getVBC()));
													d.setAliq_icms(Double.valueOf(icms90.getPICMS()));
													d.setVl_icms(Double.valueOf(icms90.getVICMS()));
												} else {
													d.setVl_bc_icms_st(Double.valueOf(icms90.getVBCST()));
													d.setVl_icms_st(Double.valueOf(icms90.getVICMSST()));
													d.setAliq_st(Double.valueOf(icms90.getPICMSST()));
												}
											}
										}
									}
								}
							}
						}
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
				d.setCst_ipi(ipi.getIPITrib().getCST());
				d.setVl_bc_ipi(Double.valueOf(ipi.getIPITrib().getVBC()));
				d.setAliq_ipi(Double.valueOf(ipi.getIPITrib().getPIPI()));
				d.setVl_ipi(Double.valueOf(ipi.getIPITrib().getVIPI()));
			} catch (Exception e) {
				d.setCst_ipi(ipi.getIPINT().getCST());
			} finally {
				int cst_ipi = Integer.valueOf(d.getCst_ipi());
				if (!venda && cst_ipi >= 50) {
					cst_ipi -= 50;
				}
				d.setCst_ipi(UtilServer.formataNumero(cst_ipi, 2, 0, false));
			}
		} else {
			d.setCst_ipi("");
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

	public String getCrt() {
		return crt;
	}

	public void setCrt(String crt) {
		this.crt = crt;
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

}
