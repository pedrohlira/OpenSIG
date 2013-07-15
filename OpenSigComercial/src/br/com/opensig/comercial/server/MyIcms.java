package br.com.opensig.comercial.server;

import br.com.opensig.nfe.TNFe.InfNFe.Det.Imposto.ICMS;

public class MyIcms {
	private String origem;
	private String cst;
	private Double aliquota = 0.00;
	private Double aliquotaSt = 0.00;
	private Double base = 0.00;
	private Double baseSt = 0.00;
	private Double valor = 0.00;
	private Double valorSt = 0.00;

	public MyIcms(ICMS icms) {
		try {
			// simples
			if (icms.getICMSSN101() != null) {
				origem = icms.getICMSSN101().getOrig();
				cst = icms.getICMSSN101().getCSOSN();
				base = 0.00;
				aliquota = 0.00;
				valor = 0.00;
			} else if (icms.getICMSSN102() != null) {
				origem = icms.getICMSSN102().getOrig();
				cst = icms.getICMSSN102().getCSOSN();
				base = 0.00;
				aliquota = 0.00;
				valor = 0.00;
			} else if (icms.getICMSSN201() != null) {
				origem = icms.getICMSSN201().getOrig();
				cst = icms.getICMSSN201().getCSOSN();
				baseSt = Double.valueOf(icms.getICMSSN201().getVBCST());
				aliquotaSt = Double.valueOf(icms.getICMSSN201().getPICMSST());
				valorSt = Double.valueOf(icms.getICMSSN201().getVICMSST());
			} else if (icms.getICMSSN202() != null) {
				origem = icms.getICMSSN202().getOrig();
				cst = icms.getICMSSN202().getCSOSN();
				baseSt = Double.valueOf(icms.getICMSSN202().getVBCST());
				aliquotaSt = Double.valueOf(icms.getICMSSN202().getPICMSST());
				valorSt = Double.valueOf(icms.getICMSSN202().getVICMSST());
			} else if (icms.getICMSSN500() != null) {
				origem = icms.getICMSSN500().getOrig();
				cst = icms.getICMSSN500().getCSOSN();
				baseSt = Double.valueOf(icms.getICMSSN500().getVBCSTRet());
				valorSt = Double.valueOf(icms.getICMSSN500().getVICMSSTRet());
				aliquotaSt = valorSt * 100 / baseSt;
			} else if (icms.getICMSSN900() != null) {
				origem = icms.getICMSSN900().getOrig();
				cst = icms.getICMSSN900().getCSOSN();
				if (icms.getICMSSN900().getModBC() != null) {
					base = Double.valueOf(icms.getICMSSN900().getVBC());
					aliquota = Double.valueOf(icms.getICMSSN900().getPICMS());
					valor = Double.valueOf(icms.getICMSSN900().getVICMS());
				} else {
					baseSt = Double.valueOf(icms.getICMSSN900().getVBCST());
					aliquotaSt = Double.valueOf(icms.getICMSSN900().getPICMSST());
					valorSt = Double.valueOf(icms.getICMSSN900().getVICMSST());
				}
			}
			// normal
			else if (icms.getICMS00() != null) {
				origem = icms.getICMS00().getOrig();
				cst = icms.getICMS00().getCST();
				base = Double.valueOf(icms.getICMS00().getVBC());
				aliquota = Double.valueOf(icms.getICMS00().getPICMS());
				valor = Double.valueOf(icms.getICMS00().getVICMS());
			} else if (icms.getICMS10() != null) {
				origem = icms.getICMS10().getOrig();
				cst = icms.getICMS10().getCST();
				baseSt = Double.valueOf(icms.getICMS10().getVBCST());
				aliquotaSt = Double.valueOf(icms.getICMS10().getPICMSST());
				valorSt = Double.valueOf(icms.getICMS10().getVICMSST());
			} else if (icms.getICMS20() != null) {
				origem = icms.getICMS20().getOrig();
				cst = icms.getICMS20().getCST();
				base = Double.valueOf(icms.getICMS20().getVBC());
				aliquota = Double.valueOf(icms.getICMS20().getPICMS());
				valor = Double.valueOf(icms.getICMS20().getVICMS());
			} else if (icms.getICMS30() != null) {
				origem = icms.getICMS30().getOrig();
				cst = icms.getICMS30().getCST();
				baseSt = Double.valueOf(icms.getICMS30().getVBCST());
				aliquotaSt = Double.valueOf(icms.getICMS30().getPICMSST());
				valorSt = Double.valueOf(icms.getICMS30().getVICMSST());
			} else if (icms.getICMS40() != null) {
				origem = icms.getICMS40().getOrig();
				cst = icms.getICMS40().getCST();
				base = 0.00;
				aliquota = 0.00;
				valor = 0.00;
			} else if (icms.getICMS51() != null) {
				origem = icms.getICMS51().getOrig();
				cst = icms.getICMS51().getCST();
				base = Double.valueOf(icms.getICMS51().getVBC());
				aliquota = Double.valueOf(icms.getICMS51().getPICMS());
				valor = Double.valueOf(icms.getICMS51().getVICMS());
			} else if (icms.getICMS60() != null) {
				origem = icms.getICMS60().getOrig();
				cst = icms.getICMS60().getCST();
				baseSt = Double.valueOf(icms.getICMS60().getVBCSTRet());
				valor = Double.valueOf(icms.getICMS60().getVICMSSTRet());
				aliquotaSt = valorSt * 100 / baseSt;
			} else if (icms.getICMS70() != null) {
				origem = icms.getICMS70().getOrig();
				cst = icms.getICMS70().getCST();
				if (icms.getICMS70().getModBC() != null) {
					base = Double.valueOf(icms.getICMS70().getVBC());
					aliquota = Double.valueOf(icms.getICMS70().getPICMS());
					valor = Double.valueOf(icms.getICMS70().getVICMS());
				} else {
					baseSt = Double.valueOf(icms.getICMS70().getVBCST());
					valorSt = Double.valueOf(icms.getICMS70().getVICMSST());
					aliquotaSt = Double.valueOf(icms.getICMS70().getPICMSST());
				}
			} else if (icms.getICMS90() != null) {
				origem = icms.getICMS90().getOrig();
				cst = icms.getICMS90().getCST();
				if (icms.getICMS90().getModBC() != null) {
					base = Double.valueOf(icms.getICMS90().getVBC());
					aliquota = Double.valueOf(icms.getICMS90().getPICMS());
					valor = Double.valueOf(icms.getICMS90().getVICMS());
				} else {
					baseSt = Double.valueOf(icms.getICMS90().getVBCST());
					valorSt = Double.valueOf(icms.getICMS90().getVICMSST());
					aliquotaSt = Double.valueOf(icms.getICMS90().getPICMSST());
				}
			}
		} catch (Exception ex) {
			// nao faz nada, deixa zerados.
		}
	}

	public String getOrigem() {
		return origem;
	}

	public void setOrigem(String origem) {
		this.origem = origem;
	}

	public String getCst() {
		return cst;
	}

	public void setCst(String cst) {
		this.cst = cst;
	}

	public Double getAliquota() {
		return aliquota;
	}

	public void setAliquota(Double aliquota) {
		this.aliquota = aliquota;
	}

	public Double getAliquotaSt() {
		return aliquotaSt;
	}

	public void setAliquotaSt(Double aliquotaSt) {
		this.aliquotaSt = aliquotaSt;
	}

	public Double getBase() {
		return base;
	}

	public void setBase(Double base) {
		this.base = base;
	}

	public Double getBaseSt() {
		return baseSt;
	}

	public void setBaseSt(Double baseSt) {
		this.baseSt = baseSt;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public Double getValorSt() {
		return valorSt;
	}

	public void setValorSt(Double valorSt) {
		this.valorSt = valorSt;
	}

}
