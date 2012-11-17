package br.com.opensig.core.shared.modelo;

import java.io.Serializable;
import java.util.List;

import br.com.opensig.core.shared.modelo.sistema.SisAcao;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.core.shared.modelo.sistema.SisModulo;

/**
 * Interface que define o acesso dos usuários ao sistema e o controle de
 * permissao que cada módulo pode ter acesso.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 */
public interface ILogin extends Serializable {

	/**
	 * Metodo que retorna o id do usuário logado.
	 * 
	 * @return o id do usuário.
	 */
	public int getId();

	/**
	 * Metodo que retorna o nome do usuário logado.
	 * 
	 * @return o nome do usuário.
	 */
	public String getUsuario();

	/**
	 * Metodo que retorna a senha do usuário logado em SHA-1
	 * 
	 * @return a senha criptografada
	 */
	public String getSenha();

	/**
	 * Metodo que retorna o desconto que o usuário possui, sendo o valor máximo.
	 * 
	 * @return um inteiro que representa o valor máximo de desconto.
	 */
	public int getDesconto();

	/**
	 * Metodo que retorna os dados da empresa logada atualmente.
	 * 
	 * @return um array de String com dado da empresa.
	 */
	public String[] getEmpresa();

	/**
	 * Metodo que retorna o ID da empresa logada atualmente.
	 * 
	 * @return um inteiro com dado da empresa.
	 */
	public int getEmpresaId();

	/**
	 * Metodo que retorna o NOME da empresa logada atualmente.
	 * 
	 * @return uma String com dado da empresa.
	 */
	public String getEmpresaNome();

	/**
	 * Metodo que atualiza a senha no login ativo do sistema.
	 * 
	 * @param novaSenha
	 *            a nova senha criptografa com SHA1.
	 */
	public void atualizarSenha(String novaSenha);

	/**
	 * Metodo que retorna se o sistema está bloqueado no login.
	 * 
	 * @return verdadeiro se estiver bloqueado e falso caso contrario.
	 */
	public boolean isBloqueado();

	/**
	 * Metodo que retorna os módulos permitidos do usuário.
	 * 
	 * @return uma coleçao de módulos.
	 */
	public List<SisModulo> getModulos();

	/**
	 * Metodo que retorna as funções permitidas do módulo informado.
	 * 
	 * @param moduloId
	 *            identificador do módulo.
	 * @return uma coleçao de funções.
	 */
	public List<SisFuncao> getFuncoes(int moduloId);

	/**
	 * Metodo que retorna as funções permitidas do módulo informado.
	 * 
	 * @param moduloClasse
	 *            classe do módulo.
	 * @return uma coleçao de funções.
	 */
	public List<SisFuncao> getFuncoes(String moduloClasse);

	/**
	 * Metodo que retorna as ações permitidas da funçao informada.
	 * 
	 * @param moduloId
	 *            identificador do módulo.
	 * @param funcaoId
	 *            identificador da funçao.
	 * @return uma coleçao de ações.
	 */
	public List<SisAcao> getAcoes(int moduloId, int funcaoId);

	/**
	 * Metodo que retorna as ações permitidas da funçao informada.
	 * 
	 * @param moduloClasse
	 *            classe do módulo.
	 * @param funcaoClasse
	 *            classe da funçao.
	 * @return uma coleçao de ações.
	 */
	public List<SisAcao> getAcoes(String moduloClasse, String funcaoClasse);

	/**
	 * Metodo que retorna se o login atual é de sistema
	 * 
	 * @return verdadeiro se o for de sistema.
	 */
	public boolean isSistema();
}
