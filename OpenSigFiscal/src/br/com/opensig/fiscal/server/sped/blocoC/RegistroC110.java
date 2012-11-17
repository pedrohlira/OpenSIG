package br.com.opensig.fiscal.server.sped.blocoC;

import br.com.opensig.fiscal.server.sped.ARegistro;

public class RegistroC110 extends ARegistro<DadosC110, String> {

	@Override
	protected DadosC110 getDados(String dados) throws Exception {
        DadosC110 d = new DadosC110();
        d.setCod_inf("1");
        d.setTxt_compl(dados);
        return d;
	}

}
