package br.com.opensig.fiscal.server.sped.bloco0;

import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.empresa.shared.modelo.EmpContato;
import br.com.opensig.empresa.shared.modelo.EmpEndereco;
import br.com.opensig.fiscal.server.sped.ARegistro;

public class Registro0005 extends ARegistro<Dados0005, Dados> {

	@Override
	protected Dados0005 getDados(Dados dados) throws Exception {
		Dados0005 d = new Dados0005();
		d.setFantasia(sped.getEmpEmpresa().getEmpEntidade().getEmpEntidadeNome2());

		EmpEndereco end = sped.getEmpEmpresa().getEmpEntidade().getEmpEnderecos().get(0);
		d.setCep(Integer.valueOf(end.getEmpEnderecoCep().replaceAll("\\D", "")));
		d.setEnd(end.getEmpEnderecoLogradouro());
		d.setNum(end.getEmpEnderecoNumero() + "");
		d.setCompl(end.getEmpEnderecoComplemento());
		d.setBairro(end.getEmpEnderecoBairro());

		for (EmpContato cont : sped.getEmpEmpresa().getEmpEntidade().getEmpContatos()) {
			if (cont.getEmpContatoTipo().getEmpContatoTipoId() == Integer.valueOf(auth.getConf().get("nfe.tipoconttel"))) {
				d.setFone(cont.getEmpContatoDescricao().replaceAll("\\D", ""));
			} else if (cont.getEmpContatoTipo().getEmpContatoTipoId() == Integer.valueOf(auth.getConf().get("nfe.tipocontemail"))) {
				d.setEmail(cont.getEmpContatoDescricao());
			}
		}

		return d;
	}
}
