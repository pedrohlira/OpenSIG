<%@page import="br.com.opensig.core.server.UtilServer"%>
<%@page import="nl.captcha.Captcha"%>
<%@page import="br.com.opensig.core.client.controlador.filtro.IFiltro"%>
<%@page import="br.com.opensig.core.client.controlador.filtro.EJuncao"%>
<%@page
	import="br.com.opensig.core.client.controlador.filtro.GrupoFiltro"%>
<%@page import="br.com.opensig.core.client.controlador.filtro.ECompara"%>
<%@page
	import="br.com.opensig.core.client.controlador.filtro.FiltroTexto"%>
<%@page import="br.com.opensig.permissao.shared.modelo.SisUsuario"%>
<%@page import="br.com.opensig.core.server.CoreServiceImpl"%>
<%@page import="br.com.opensig.core.client.servico.CoreService"%>
<%@page import="java.util.Date"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
	String url = request.getContextPath() + "/PermissaoService?data="
			+ new Date().getTime();
	String email = request.getParameter("email");
	String id = request.getParameter("id");
	String nova = request.getParameter("nova");
	String captcha = request.getParameter("captcha");
	String msg = "";
	String redir = "document.location = '/';";

	if (email == null || id == null || email.isEmpty() || id.isEmpty()) {
		msg = "alert('Email ou Id inválidos');";
	} else {
		SisUsuario usuario = new SisUsuario();
		FiltroTexto ft1 = new FiltroTexto("sisUsuarioEmail",
				ECompara.IGUAL, email);
		FiltroTexto ft2 = new FiltroTexto("sisUsuarioSenha",
				ECompara.IGUAL, id);
		GrupoFiltro gf = new GrupoFiltro(EJuncao.E, new IFiltro[]{ft1,
				ft2});
		CoreService core = new CoreServiceImpl();
		usuario = (SisUsuario) core.selecionar(usuario, gf, false);

		if (usuario == null) {
			msg = "alert('Usuário não encontrado!');" + redir;
		} else if (nova != null && captcha != null && !nova.isEmpty()
				&& !captcha.isEmpty()) {
			HttpSession sessao = request.getSession();
			Captcha cap = (Captcha) sessao.getAttribute(Captcha.NAME);
			if (!cap.isCorrect(captcha)) {
				msg = "alert('Código da imagem inválido!');";
			} else {
				usuario.setSisUsuarioSenha(nova);
				core.salvar(usuario);
				msg = "alert('Senha alterada com sucesso!');" + redir;
			}
		}
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<link rel="shortcut icon" href="img/favicon.ico" />
<link rel="stylesheet" type="text/css" href="css/Basico.css" />

<script src="js/ext/adapter/jquery/jquery_mini.js" language="javascript"></script>
<script src="opensig/js/OpenSigCore_mini.js" language="javascript"></script>

<script type="text/javascript">
	<%=msg%>

	function validar(){
		var strNova = document.senha.nova.value;
		var strConfirma = document.senha.confirma.value;
		var strCaptcha = document.senha.captcha.value;
		
		if(strNova == "" || strNova.length < 6)
		{
			alert( "Preencha a nova senha com 6 caracteres no mínimo!" );
			document.senha.nova.focus();
			return false;
		}
		else if(strConfirma != strNova)
		{
			alert( "As duas senhas não são iguais!" );
			document.senha.confirma.focus();
			return false;
		}
		else if(strCaptcha == "")
		{
			alert( "Preencha o código da imagem!" );
			document.senha.captcha.focus();
			return false;
		}
		else {
			document.senha.nova.value = encrypt.sha1(strNova);
			return true;
		}
	}
	
</script>

<title>OpenSIG - Sistemas Integrados Gerenci&aacute;veis Open
	Source</title>
</head>
<body onload="javascript:document.senha.nova.focus();">
	<div id="stylized" class="myform" style="height: 350px">

		<form id="senha" name="senha" method="post"
			action="novasenha.jsp?email=<%=email%>&id=<%=id%>"
			onsubmit="return validar();">
			<a href="/" title="P&aacutegina Inicial"><img src="img/logo.png" width="32px" height="32px" /> </a>
			<h1>Alterar Senha</h1>
			<p>Este formulário &eacute; usado para alterar a senha no
				sistema.</p>
			<label>Nova Senha <span class="small">Digite senha</span> </label> <input
				type="password" name="nova" id="nova" maxlength="40" /> <label>Confirmar
				Senha <span class="small">Confirme a senha</span> </label> <input
				type="password" name="confirma" id="confirma" maxlength="40" /> <label>Segurança
				<span class="small">Veja a imagem</span> </label> <img src="<%=url%>" /> <label>Código
				<span class="small">Digite o código</span> </label> <input type="text"
				name="captcha" id="captcha" maxlength="5" class="small" />

			<div class="spacer"></div>
			<button type="submit" style="margin-left: 190px;">Salvar</button>

			<div class="spacer"></div>
			<br />
			<p>
				Caso deseje mais informa&ccedil;&otilde;s entre em contato conosco.
				<a href="mailto:opensig@phdss.com.br">opensig@phdss.com.br</a>
			</p>
			&copy; <a href="http://phdss.com.br"><i>PhD - Systems
					Solutions</i>
			</a>
		</form>
	</div>
</body>
</html>
