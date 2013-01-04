package br.com.opensig.fiscal.server.sped.blocoC;

import br.com.opensig.fiscal.server.sped.Bean;

public class DadosC410 extends Bean {

    private Double vl_pis;
    private Double vl_cofins;

    public DadosC410() {
    	reg = "C410";
    }

    public Double getVl_cofins() {
        return vl_cofins;
    }

    public void setVl_cofins(Double vl_cofins) {
        this.vl_cofins = vl_cofins;
    }

    public Double getVl_pis() {
        return vl_pis;
    }

    public void setVl_pis(Double vl_pis) {
        this.vl_pis = vl_pis;
    }
}
