<%@page import="br.com.opensig.core.shared.modelo.Lista"%>
<%@page import="br.com.opensig.core.server.UtilServer"%>
<%@page import="br.com.opensig.poker.shared.modelo.*"%>
<%@page import="br.com.opensig.core.client.controlador.filtro.*"%>
<%@page import="br.com.opensig.core.server.CoreServiceImpl"%>
<%@page import="br.com.opensig.core.client.servico.CoreService"%>
<%
	// Parametros
	CoreService service = new CoreServiceImpl();
	Lista lista = service.selecionar(new PokerJackpot(), 0, 2, null, false);
	PokerJackpot pot1 = (PokerJackpot) lista.getLista().get(0);
	PokerJackpot pot2 = (PokerJackpot) lista.getLista().get(1);
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">

<link rel="shortcut icon" href="img/favicon.ico" />
<link rel="stylesheet" href="css/player.css" type="text/css">
<script type="text/javascript">
	var segundos = 0;
	function timer() {
		// incrementa
		if (segundos == 60) {
			document.location.href = '/jackpot.jsp';
		} else {
			segundos++;
		}
	}
</script>
<title>Jackpot</title>
</head>
<body onload="setInterval(timer, 1000);">
	<div id="player">
		<div id="esquerda" class="box" style="height: 620px">
			<div class="relogio" style="height: 100px; font-size: 40px; line-height: 100px;"><%=pot1.getPokerForma().getPokerFormaNome()%></div>
			<div class="linha destaque">Total R$ <%=UtilServer.formataNumero(pot1.getPokerJackpotTotal(), 1, 2, true)%></div>
			<div class="linha">Último Prêmio Pago</div>
			<div class="linha destaque">R$ <%= pot1.getPokerPagar() == null ? "0,00" : UtilServer.formataNumero(pot1.getPokerPagar().getPokerPagarValor(), 1, 2, true)%></div>
			<div class="linha">Ganhador do Prêmio</div>
			<div class="linha destaque"><%= pot1.getPokerPagar() == null ? "NENHUM" : pot1.getPokerPagar().getPokerPagarDescricao()%></div>
			<div class="linha">Data : <%= pot1.getPokerPagar() == null ? "NENHUMA" : UtilServer.formataData(pot1.getPokerPagar().getPokerPagarRealizado(), "dd/MM/yyy")%></div>
			<hr style="width: 95%">
			<div class="linha">Último Recebido</div>
			<div class="linha destaque">R$ <%= pot1.getPokerReceber() == null ? "0,00" : UtilServer.formataNumero(pot1.getPokerReceber().getPokerReceberValor(), 1, 2, true)%></div>
		</div>
		<div id="direita" class="box" style="height: 620px">
			<div class="relogio" style="height: 100px; font-size: 40px; line-height: 100px;"><%=pot2.getPokerForma().getPokerFormaNome()%></div>
			<div class="linha destaque">Total R$ <%=UtilServer.formataNumero(pot2.getPokerJackpotTotal(), 1, 2, true)%></div>
			<div class="linha">Último Prêmio Pago</div>
			<div class="linha destaque">R$ <%= pot2.getPokerPagar() == null ? "0,00" : UtilServer.formataNumero(pot2.getPokerPagar().getPokerPagarValor(), 1, 2, true)%></div>
			<div class="linha">Ganhador do Prêmio</div>
			<div class="linha destaque"><%= pot2.getPokerPagar() == null ? "NENHUM" :  pot2.getPokerPagar().getPokerPagarDescricao()%></div>
			<div class="linha">Data : <%= pot2.getPokerPagar() == null ? "NENHUMA" :  UtilServer.formataData(pot2.getPokerPagar().getPokerPagarRealizado(), "dd/MM/yyy")%></div>
			<hr style="width: 95%">
			<div class="linha">Último Recebido</div>
			<div class="linha destaque">R$ <%= pot2.getPokerReceber() == null ? "0,00" : UtilServer.formataNumero(pot2.getPokerReceber().getPokerReceberValor(), 1, 2, true)%></div>
		</div>
			<div style="float: left; width: 100%; margin-top: 5px; vertical-align: text-top;" class="box" ><br>&copy; PhD - http://phdss.com.br</div>
	</div>
</body>
</html>