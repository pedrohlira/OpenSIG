<%@page import="br.com.opensig.core.server.UtilServer"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%
	String idioma = getServletContext().getInitParameter("login.idioma");
	String tema = getServletContext().getInitParameter("login.tema");
	String captcha = getServletContext().getInitParameter("login.captcha");
	String esqueceu = getServletContext().getInitParameter("login.esqueceu");
	String modulos = getServletContext().getInitParameter("login.modulos");
%>

<html>
<head>
<meta http-equiv="content-type" content="text/html;charset=utf-8" />
<meta name="gwt:property" content="locale=<%=idioma%>" />

<meta http-equiv="Pragma" content="No-Cache" />
<meta http-equiv="cache-control" content="no-cache, no store" />
<meta name="Expires" content="Fri, 01 Jan 2010 08:00:00 GMT" />

<link rel="shortcut icon" href="img/favicon.ico" />
<link rel="stylesheet" type="text/css" href="css/OpenSig.css" />
<link rel="stylesheet" type="text/css" href="" id="estilo" />

<title>OpenSIG - Sistemas Integrados Gerenci&aacute;veis Open Source</title>

</head>
<body>

	<div id="idioma" style="display: none"><%=idioma%></div>
	<div id="tema" style="display: none"><%=tema%></div>
	<div id="captcha" style="display: none"><%=captcha%></div>
	<div id="esqueceu" style="display: none"><%=esqueceu%></div>
	<div id="modulos" style="display: none"><%=modulos%></div>

	<iframe id="__gwt_historyFrame"
		style="position: absolute; width: 0; height: 0; border: 0"></iframe>
	<iframe id="__gwt_printingFrame"
		style="position: absolute; width: 0; height: 0; border: 0" src=""></iframe>

	<!--add loading indicator while the app is being loaded-->
	<div id="loading">
		<div class="loading-indicator">
			<img src="img/loading.gif" width="32" height="32"
				style="margin-right: 8px; float: left; vertical-align: top;" />
			OpenSIG<br /> <font style="font-size: 8px; color: gray">BETA</font><br />
		</div>
	</div>

	<!--include the application JS-->
	<script type="text/javascript" language='javascript'
		src='opensig/opensig.nocache.js'></script>

	<!-- i18N -->
	<script type="text/javascript" src="js/ext/lang/<%=idioma%>.js"></script>

</body>
</html>
