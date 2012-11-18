<%@page import="java.util.Date"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
	String url = request.getContextPath() + "/PermissaoService?data=" + new Date().getTime();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<link rel="shortcut icon" href="img/favicon.ico" />
<link rel="stylesheet" type="text/css" href="css/Basico.css" />

<script type="text/javascript">
	function validar(opcao){
		var strChave = document.nfe.chave.value;
		var strCaptcha = document.nfe.captcha.value;
		
		if(strChave == "" || strChave.length < 44)
		{
			alert( "Preencha a chave com 44 digitos!" );
			document.nfe.chave.focus();
		}
		else if(strCaptcha == "")
		{
			alert( "Preencha o código da imagem!" );
			document.nfe.captcha.focus();
		}
		else {
			var url = "<%=request.getContextPath() + "/FiscalService"%>?chave="+ strChave + "&captcha=" + strCaptcha + "&opcao=" + opcao;
			document.getElementById("nfeFrame").src = url;
		}
	}
</script>

<title>OpenSIG - Sistemas Integrados Gerenci&aacute;veis Open Source</title>
</head>
<body onload="javascript:document.nfe.chave.focus();">
	<div id="stylized" class="myform">
		<form id="nfe" name="nfe" method="post">
			<a href="/" title="P&aacutegina Inicial"><img
				src="img/logo.png" width="32px" height="32px" /> </a>
			<h1>Baixar Nota Fiscal Eletrônica</h1>
			<p>Este formulário &eacute; usado para baixar os arquivos da NFe.</p>

			<label>Chave <span class="small">Digite a chave</span> </label> <input
				type="text" name="chave" id="chave" maxlength="44" /> <label>Segurança
				<span class="small">Veja a imagem</span> </label> <img src="<%=url%>" /> <label>Código
				<span class="small">Digite o código</span> </label> <input type="text"
				name="captcha" id="captcha" maxlength="5" class="small" />

			<div class="spacer"></div>
			<button type="button" style="margin-left: 50px;"
				onclick="validar(2);">NFe</button>
			<button type="button" style="margin-left: 20px;"
				onclick="validar(4);">Cancelada</button>
			<button type="button" style="margin-left: 20px;"
				onclick="validar(0);">Danfe</button>

			<div class="spacer"></div><br/>
			<p>Caso deseje mais informa&ccedil;&otilde;s entre em contato conosco. <a href="mailto:opensig@phdss.com.br">opensig@phdss.com.br</a></p>
			&copy; <a href="http://phdss.com.br"><i>PhD - Systems Solutions</i></a>
		</form>
	</div>

	<iframe id="nfeFrame"
		style="position: absolute; width: 0; height: 0; border: 0" src=""></iframe>

</body>
</html>
