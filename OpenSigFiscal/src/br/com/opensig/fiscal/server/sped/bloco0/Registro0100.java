package br.com.opensig.fiscal.server.sped.bloco0;

import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.FiltroNumero;
import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.empresa.shared.modelo.EmpContato;
import br.com.opensig.empresa.shared.modelo.EmpEndereco;
import br.com.opensig.empresa.shared.modelo.EmpFuncionario;
import br.com.opensig.fiscal.server.sped.ARegistro;

public class Registro0100 extends ARegistro<Dados0100, Dados> {

	@Override
	protected Dados0100 getDados(Dados dados) throws Exception {
		FiltroNumero fn = new FiltroNumero("empFuncionarioId", ECompara.IGUAL, auth.getConf().get("sped.0100.id_funcionario"));
		EmpFuncionario fun = (EmpFuncionario) service.selecionar(new EmpFuncionario(), fn, false);

		Dados0100 d = new Dados0100();
		d.setNome(fun.getEmpEntidade().getEmpEntidadeNome1());
		d.setCpf(fun.getEmpEntidade().getEmpEntidadeDocumento1().replaceAll("\\D", ""));
		d.setCrc(fun.getEmpEntidade().getEmpEntidadeDocumento2());
		
		EmpEndereco end = fun.getEmpEntidade().getEmpEnderecos().get(0);
		d.setCep(Integer.valueOf(end.getEmpEnderecoCep().replaceAll("\\D", "")));
		d.setEnd(end.getEmpEnderecoLogradouro());
		d.setNum(end.getEmpEnderecoNumero() + "");
		d.setCompl(end.getEmpEnderecoComplemento());
		d.setBairro(end.getEmpEnderecoBairro());
		d.setCod_mun(end.getEmpMunicipio().getEmpMunicipioIbge());

		for (EmpContato cont : fun.getEmpEntidade().getEmpContatos()) {
			if (cont.getEmpContatoTipo().getEmpContatoTipoId() == Integer.valueOf(auth.getConf().get("nfe.tipoconttel"))) {
				d.setFone(cont.getEmpContatoDescricao().replaceAll("\\D", ""));
			} else if (cont.getEmpContatoTipo().getEmpContatoTipoId() == Integer.valueOf(auth.getConf().get("nfe.tipocontemail"))) {
				d.setEmail(cont.getEmpContatoDescricao());
			}
		}
		
		return d;
	}
}
