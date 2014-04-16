/**
 * Classe da aplicacao que define as configuracoes basicas.
 */
Ext.Loader.setConfig({
	enabled : true,
	paths : {
		'SIG' : 'app'
	}
});

Ext.application({
	name : 'SIG',
	icon : 'img/home.png',
	glossOnIcon : false,

	models : [ 'SisUsuario', 'SisGrupo', 'PokerTorneio', 'PokerTorneioTipo',
			'PokerMesa', 'PokerParticipante', 'PokerCliente', 'PokerCash',
			'PokerForma', 'PokerJogador' ],
	stores : [ 'SigProxy', 'SigStore' ],
	controllers : [ 'Home', 'Torneio', 'Mesa', 'Participante',
			'ParticipanteInfo', 'Cash', 'Jogador', 'JogadorInfo' ],
	views : [ 'Main', 'Home', 'Torneio', 'TorneioLista', 'Cash', 'CashLista',
			'MesaLista', 'ParticipanteLista', 'ParticipanteInfo',
			'JogadorLista', 'JogadorInfo' ],

	launch : function() {
		Ext.Viewport.add({
			xtype : 'main'
		});
	}
});