Ext.define('SIG.model.PokerJogador', {
	// classes obrigatorias
	requires : [ 'Ext.data.Model' ],
	// classe mae
	extend : 'Ext.data.Model',
	// seta um nome menor para instanciar
	alias : 'pokerJogador',
	// o campo que representa o id
	idProperty : 'pokerJogadorId',
	// configuracoes
	config : {
		// campos do modelo
		fields : [ {
			name : 'pokerJogadorId',
			type : 'int'
		}, {
			name : 'pokerJogadorAtivo',
			type : 'boolean'
		}, {
			name : 'pokerCash',
			type : 'auto'
		}, {
			name : 'pokerCliente',
			type : 'auto'
		} ]
	}
});