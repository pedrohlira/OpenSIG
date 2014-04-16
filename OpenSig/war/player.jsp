<%@page import="br.com.opensig.core.shared.modelo.EComando"%>
<%@page import="br.com.opensig.core.shared.modelo.Sql"%>
<%@page import="br.com.opensig.core.client.controlador.parametro.ParametroBinario"%>
<%@page import="br.com.opensig.core.shared.modelo.Lista"%>
<%@page import="br.com.opensig.core.server.UtilServer"%>
<%@page import="br.com.opensig.poker.shared.modelo.*"%>
<%@page import="br.com.opensig.core.client.controlador.filtro.*"%>
<%@page import="br.com.opensig.core.server.CoreServiceImpl"%>
<%@page import="br.com.opensig.core.client.servico.CoreService"%>
<%
	// Parametros
	CoreService service = new CoreServiceImpl();
	String tID = request.getParameter("tID") == null ? "0" : request.getParameter("tID");
	String nID = request.getParameter("nID") == null ? "0" : request.getParameter("nID");
	String acao = request.getParameter("acao") == null ? "Avancar" : request.getParameter("acao");
	String minuto = request.getParameter("minuto") == null ? "0" : request.getParameter("minuto");
	
	// Selecionando o torneio
	FiltroNumero fn = new FiltroNumero("pokerTorneioId",ECompara.IGUAL, tID);
	FiltroBinario fb = new FiltroBinario("pokerTorneioAtivo", ECompara.IGUAL, 1);
	GrupoFiltro gf = new GrupoFiltro(EJuncao.E, new IFiltro[]{fn, fb});
	PokerTorneio torneio = (PokerTorneio) service.selecionar(new PokerTorneio(), gf, false);

	// Setando as variaveis
	PokerNivel nivel = new PokerNivel();
	String nome = "";
	double arrecadado = 0.00;
	int ativos = 0;
	int entradas = 0;
	int rebuys = 0;
	int addon = 0;
	int dealer = 0;
	int bonus = 0;
	int media = 0;
	int total = 0;
	int numero = 0;
	int tempo = 0;
	int espera = 0;
	int small = 0;
	int big = 0;
	int ante = 0;

	// caso o torneio esteja aberto
	if (torneio != null) {
		nome = torneio.getPokerTorneioNome();
		for (int i = 0; i < torneio.getPokerParticipantes().size(); i++) {
			PokerParticipante parti = (PokerParticipante) torneio.getPokerParticipantes().get(i);
			entradas++;
			rebuys += parti.getPokerParticipanteReentrada();
			addon += parti.getPokerParticipanteAdicional();
			dealer += parti.getPokerParticipanteDealer();
			bonus += parti.getPokerParticipanteBonus();
			if (parti.getPokerParticipanteAtivo()) {
				ativos++;
			}
		}
		total = bonus
				+ (entradas * torneio.getPokerTorneioEntradaFicha())
				+ (rebuys * torneio.getPokerTorneioReentradaFicha())
				+ (addon * torneio.getPokerTorneioAdicionalFicha())
				+ (dealer * torneio.getPokerTorneioDealerFicha());
		if(ativos > 0) {
			media = total / ativos;
		}
		arrecadado = (entradas * torneio.getPokerTorneioEntrada())
				+ (rebuys * torneio.getPokerTorneioReentrada())
				+ (addon * torneio.getPokerTorneioAdicional());

		int nivelN = Integer.valueOf(nID).intValue();
		if (acao.equalsIgnoreCase("Avancar")) {
			// Inativa o nivel atual
			FiltroNumero fn1 = new FiltroNumero("pokerNivelNumero", ECompara.IGUAL, nID);
			ParametroBinario pb = new ParametroBinario("pokerNivelAtivo", 0);
			Sql sql = new Sql(nivel, EComando.ATUALIZAR, fn1, pb);
			service.executar(new Sql[]{ sql });
		} else if (acao.equalsIgnoreCase("Voltar")) {
			// Ativa o nivel anterior
			nivelN--;
			FiltroNumero fn1 = new FiltroNumero("pokerNivelNumero", ECompara.IGUAL, nivelN + "");
			ParametroBinario pb = new ParametroBinario("pokerNivelAtivo", 1);
			Sql sql = new Sql(nivel, EComando.ATUALIZAR, fn1, pb);
			service.executar(new Sql[]{ sql });
		}
		
		// recupera o menor nivel ativo
		FiltroObjeto fo1 = new FiltroObjeto("pokerTorneio",	ECompara.IGUAL, torneio);
		FiltroBinario fb1 = new FiltroBinario("pokerNivelAtivo", ECompara.IGUAL, 1);
		GrupoFiltro gf1 = new GrupoFiltro(EJuncao.E, new IFiltro[]{fo1, fb1});
		Lista niveis = service.selecionar(nivel, 0, 1, gf1, false);

		// caso encontre um nivel ativo
		if (niveis.getLista().size() == 1) {
			nivel = (PokerNivel) niveis.getLista().get(0);
			numero = nivel.getPokerNivelNumero();
			tempo = acao.equalsIgnoreCase("Atualizar") ? Integer.valueOf(minuto).intValue() : nivel.getPokerNivelTempo();
			espera = nivel.getPokerNivelEspera();
			small = nivel.getPokerNivelPequeno();
			big = nivel.getPokerNivelGrande();
			ante = nivel.getPokerNivelAnte();
			
			// Reativa o nivel atual caso o nivel selecionado seja menor
			if(nivelN > numero){
				FiltroNumero fn1 = new FiltroNumero("pokerNivelNumero", ECompara.IGUAL, nID);
				ParametroBinario pb = new ParametroBinario("pokerNivelAtivo", 1);
				Sql sql = new Sql(nivel, EComando.ATUALIZAR, fn1, pb);
				service.executar(new Sql[]{ sql });
			}
		} else {
			nivel = null;
		}
	}
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">

<link rel="shortcut icon" href="img/favicon.ico" />
<link rel="stylesheet" href="css/player.css" type="text/css">
<script type="text/javascript">
	<%if (torneio == null){%>
		alert('Torneio inexistente!');
		window.close();
	<%}else if (nivel == null) {%>
		alert('Torneio finalizado!');
		window.close();
	<%}else{%>
	// variaveis
	var play = true;
	var intervalo = false;
	var minutos = <%=tempo%>;
	var segundos = 0;
	var espera = <%=espera%>;
	var titulo = '<%=nome%>';
	var som = new Audio("img/beep.mp3");

	function timer() {
		if(play) {
			// decrementa
			if (segundos == 0) {
				minutos--;
				segundos = 59;
			} else {
				segundos--;
			}
	
			// formata o texto
			_minuto = minutos > 9 ? "" + minutos : "0" + minutos;
			_segundo = segundos > 9 ? "" + segundos : "0" + segundos;
			tempo = _minuto + ":" + _segundo;
	
			// insere na pagina
			document.getElementById('relogio').innerHTML = tempo;
			document.getElementById('relogio').style.color = intervalo ? 'darkred' : 'black';
	
			// verifica se o tempo acabou
			if (minutos == 0 && segundos == 0) {
				// verifica se tem espera
				if (espera > 0) {
					minutos = espera;
					segundos = 0;
					intervalo = true;
					espera = 0;
					document.getElementById('topo').innerHTML = 'INTERVALO';
					document.getElementById('topo').style.color = 'darkred';
				} else {
					// proximo nivel
					play = false;
					document.location.href = '/player.jsp?tID=<%=torneio.getId()%>&nID=<%=numero%>&acao=Avancar&minuto=0';
				}
			} else if (intervalo == false && segundos == 0){
				// atualiza os dados
				play = false;
				document.location.href = '/player.jsp?tID=<%=torneio.getId()%>&nID=<%=numero%>&acao=Atualizar&minuto=' + minutos;
			}
			
			// toca o beep
			if((minutos == 1 && segundos == 0) || (minutos == 0 && segundos <= 10)){
				som.play();
			}
		}
	}
	
	// funcao que pause ou continua o relogio
	function play_pause(acao){
		play = acao;
		if(intervalo) {
			document.getElementById('topo').innerHTML = play ? 'INTERVALO' : 'PAUSADO';
		}else {
			document.getElementById('topo').innerHTML = play ? titulo : 'PAUSADO';
			document.getElementById('topo').style.color = play ? 'black' : 'darkred';
		}
	}
	
	// funcao que muda o nivel para cima ou para baixo
	function mudar_nivel(acao){
		play = false;
		document.location.href = '/player.jsp?tID=<%=torneio.getId()%>&nID=<%=numero%>&minuto=0&acao=' + acao;
	}
	
	<%}%>
</script>
<title>Player Poker</title>
</head>
<body onload="setInterval(timer, 1000);">
	<div id="player">
		<div id="topo" class="box"><%=nome%></div>
		<div id="separador"></div>
		<div id="esquerda" class="box">
			<div class="linha">Arrecadado R$ <%=UtilServer.formataNumero(arrecadado, 1, 2, true)%></div>
			<div class="linha destaque">Buy-Ins <%=UtilServer.formataNumero(entradas, 2, 0, false)%></div>
			<div class="linha">Jogadores <%=UtilServer.formataNumero(ativos, 2, 0, false)%></div>
			<div class="linha destaque">Re-Buys <%=UtilServer.formataNumero(rebuys, 2, 0, false)%></div>
			<div class="linha">M&eacute;dia Fichas $ <%=UtilServer.formataNumero(media, 1, 0, true)%></div>
			<div class="linha destaque">Add-Ons <%=UtilServer.formataNumero(addon, 2, 0, false)%></div>
			<div class="linha">Total Fichas $ <%=UtilServer.formataNumero(total, 1, 0, true)%></div>
		</div>
		<div id="direita" class="box">
			<div id="relogio" class="relogio"><%=UtilServer.formataNumero(tempo, 2, 0, false)%>:00</div>
			<div class="linha destaque">
				<img title="Clique aqui para voltar um nivel!" src="img/voltar.png" onclick="mudar_nivel('Voltar');" style="cursor: pointer;">
				<img title="Clique aqui para pausar o relogio!" src="img/pausar.png" onclick="play_pause(false);" style="cursor: pointer;">
				<img title="Clique aqui para continuar o relogio!" src="img/iniciar.png" onclick="play_pause(true);" style="cursor: pointer;">
				<img title="Clique aqui para avancar um nivel!" src="img/avancar.png" onclick="mudar_nivel('Avancar');" style="cursor: pointer;">
			</div>
			<div class="linha">Blinds $ <%=UtilServer.formataNumero(small, 1, 0, true)%> / $ <%=UtilServer.formataNumero(big, 1, 0, true)%></div>
			<div class="linha destaque">Ante $ <%=UtilServer.formataNumero(ante, 1, 0, true)%></div>
			<div class="linha">N&iacute;vel <%=UtilServer.formataNumero(numero, 2, 0, false)%></div>
		</div>
			<div style="float: left; width: 100%; margin-top: 5px; vertical-align: text-top;" class="box" ><br>&copy; PhD - http://phdss.com.br</div>
	</div>
</body>
</html>