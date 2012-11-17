package br.com.opensig.fiscal.server.sped.bloco0;

import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.empresa.shared.modelo.EmpMunicipio;
import br.com.opensig.fiscal.server.sped.ARegistro;

public class Registro0000 extends ARegistro<Dados0000, Dados> {

	@Override
	protected Dados0000 getDados(Dados dados) throws Exception {
		Dados0000 d = new Dados0000();
		d.setCod_ver(Integer.valueOf(auth.getConf().get("sped.0000.cod_ver")));
		d.setCod_fin(sped.getFinalidade());
		d.setDt_ini(inicio);
		d.setDt_fin(fim);

		d.setNome(sped.getEmpEmpresa().getEmpEntidade().getEmpEntidadeNome1());
		if (sped.getEmpEmpresa().getEmpEntidade().getEmpEntidadeDocumento1().length() == 18) {
			d.setCnpj(sped.getEmpEmpresa().getEmpEntidade().getEmpEntidadeDocumento1().replaceAll("\\D", ""));
		} else {
			d.setCpf(sped.getEmpEmpresa().getEmpEntidade().getEmpEntidadeDocumento1().replaceAll("\\D", ""));
		}
		
		EmpMunicipio mun = sped.getEmpEmpresa().getEmpEntidade().getEmpEnderecos().get(0).getEmpMunicipio();
		d.setUf(mun.getEmpEstado().getEmpEstadoSigla());
		d.setIe(sped.getEmpEmpresa().getEmpEntidade().getEmpEntidadeDocumento2());
		d.setCod_mun(mun.getEmpMunicipioIbge());
		//TODO im adicionar ao sistema isso
		d.setSuframa(auth.getConf().get("sped.0000.suframa"));
		d.setInd_perfil(auth.getConf().get("sped.0000.ind_perfil"));
		d.setInd_ativ(Integer.valueOf(auth.getConf().get("sped.0000.ind_ativ")));
		return d;
	}

}
