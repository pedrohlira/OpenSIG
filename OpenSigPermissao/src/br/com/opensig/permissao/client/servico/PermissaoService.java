package br.com.opensig.permissao.client.servico;

import br.com.opensig.core.client.servico.CoreService;
import br.com.opensig.core.client.servico.MailException;
import br.com.opensig.core.shared.modelo.Autenticacao;

/**
 * Interface que define o login no sistema.
 * 
 * @author Pedro H. Lira
 * @since 14/04/2009
 * @version 1.0
 */
public interface PermissaoService extends CoreService {

	/**
	 * Metodo que autentica o usuário no sistema.
	 * 
	 * @param usuario
	 *            texto que o identifica.
	 * @param senha
	 *            chave de segurança.
	 * @param captcha
	 *            imagem de segurança.
	 * @param empresa
	 *            compania que pertence.
	 * @param permissao
	 *            caso verdadeiro, serve para permitir e nao logar.
	 * @return uma instância da classe SisUsuario quando autenticado, caso
	 *         contrario gera exceção.
	 * @throws PermissaoException
	 *             disparada quando não encontrou nenhum usuário com os dados de
	 *             autenticação informados.
	 */
	public Autenticacao entrar(String usuario, String senha, String captcha, int empresa, boolean permissao) throws PermissaoException;

	/**
	 * Metodo que remove o usuário da sessão atual.
	 */
	public void sair();

	/**
	 * Metodo que retorna se o usuario esta logado.
	 * 
	 * @return verdadeiro se a sessao esta ativa, falso caso contrario.
	 */
	public boolean isLogado();

	/**
	 * Metodo que bloqueia a sessão atual com o usuário logao.
	 * 
	 * @param bloqueio
	 *            verdadeiro para bloquear, falso para desbloquear.
	 */
	public void bloquear(boolean bloqueio) throws PermissaoException;

	/**
	 * Metodo que envia um email para recuperação de senha
	 * 
	 * @param email
	 *            o email do usuario cadastrado.
	 * @throws PermissaoException
	 *             dispara uma excecao caso o email nao seja de um usuario.
	 * @throws MailException
	 *             dispara uma excecao caso nao consiga enviar o email.
	 */
	public void recuperarSenha(String email) throws PermissaoException, MailException;
}
