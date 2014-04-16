Ext.define('SIG.model.PokerForma', {
	// classes obrigatorias
	requires : [ 'Ext.data.Model' ],
	// classe mae
	extend : 'Ext.data.Model',
	// seta um nome menor para instanciar
	alias : 'pokerForma',
	// o campo que representa o id
	idProperty : 'pokerFormaId',
	// configuracoes
	config : {
		// campos do modelo
		fields : [ {
			name : 'pokerFormaId',
			type : 'int'
		}, {
			name : 'pokerFormaNome',
			type : 'string'
		}, {
			name : 'pokerFormaRealizado',
			type : 'boolean'
		}, {
			name : 'pokerFormaJackpot',
			type : 'boolean'
		} ]
	}
});