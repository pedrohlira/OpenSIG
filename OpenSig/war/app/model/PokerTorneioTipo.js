Ext.define('SIG.model.PokerTorneioTipo', {
	// classes obrigatorias
	requires : [ 'Ext.data.Model' ],
	// classe mae
	extend : 'Ext.data.Model',
	// seta um nome menor para instanciar
	alias : 'pokerTorneioTipo',
	// o campo que representa o id
	idProperty : 'pokerTorneioTipoId',
	// configuracoes
	config : {
		// campos do modelo
		fields : [ {
			name : 'pokerTorneioTipoId',
			type : 'int'
		}, {
			name : 'pokerTorneioTipoNome',
			type : 'string'
		} ]
	}
});