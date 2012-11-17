package br.com.opensig.financeiro.client.js;

public class OpenSigFinanceiroJS {

    public static final native boolean validarCMC7(String id, String cheque)/*-{
    return $wnd.$('#' + id).validarCMC7(cheque);
    }-*/;

}
