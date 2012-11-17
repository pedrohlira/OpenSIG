package br.com.opensig.empresa.client.js;

public class OpenSigEmpresaJS {

    public static final native boolean validarCPF(String id, String cpf)/*-{
    return $wnd.$('#' + id).validarCPF(cpf);
    }-*/;

    public static final native boolean validarCNPJ(String id, String cnpj)/*-{
    return $wnd.$('#' + id).validarCNPJ(cnpj);
    }-*/;
}
