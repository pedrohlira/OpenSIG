package br.com.opensig.core.client.js;

/**
 * Classe que permite acesso a funcoes nativas em javascript.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 */
public class OpenSigCoreJS {

	/**
	 * Metodo que colocar uma mascara em um campo text
	 * 
	 * @param id
	 *            o nome unico do componente.
	 * @param mascara
	 *            o formato da mascara.
	 * @param titular
	 *            se tem especo reservado.
	 */
	public final static native void mascarar(String id, String mascara, String titular) /*-{
																						if(titular != null){
																						$wnd.$('#' + id).mask(mascara, {placeholder:titular});
																						}else{
																						$wnd.$('#' + id).mask(mascara);
																						}}-*/;

	/**
	 * Metodo que criptograda um texto usando o algoritmo SHA1.
	 * 
	 * @param valor
	 *            o texto a ser criptografado.
	 * @return o texto ja criptografado.
	 */
	public static final native String sha1(String valor)/*-{
														return $wnd.encrypt.sha1(valor);
														}-*/;

	/**
	 * Metodo que valida a seguranca da senha.
	 * 
	 * @param senha
	 *            o texto usado como secreto.
	 * @return um inteiro informando de 0 a 100 a sua força.
	 */
	public static final native int policy(String senha)/*-{
														return $wnd.encrypt.policy(senha);
														}-*/;

	/**
	 * Metodo que transforma o texto ascii em base 64.
	 * 
	 * @param texto
	 *            o texto a ser transformado.
	 * @return o texto ja na base 64
	 */
	public static final native String base64encode(String texto)/*-{
																return $wnd.base64.encode(texto);
																}-*/;

	/**
	 * Metodo que transforma o texto base 64 em ascii.
	 * 
	 * @param texto
	 *            o texto a ser transformado.
	 * @return o texto ja em ascii.
	 */
	public static final native String base64decode(String texto)/*-{
																return $wnd.base64.decode(texto);
																}-*/;
}
