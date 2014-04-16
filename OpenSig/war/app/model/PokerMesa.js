Ext.define('SIG.model.PokerMesa', {
	// classes obrigatorias
	requires : [ 'Ext.data.Model' ],
	// classe mae
	extend : 'Ext.data.Model',
	// seta um nome menor para instanciar
	alias : 'pokerMesa',
	// o campo que representa o id
	idProperty : 'pokerMesaId',
	// configuracoes
	config : {
		// campos do modelo
		fields : [ {
			name : 'pokerMesaId',
			type : 'int'
		}, {
			name : 'pokerMesaNumero',
			type : 'int'
		}, {
			name : 'pokerMesaLugares',
			type : 'int'
		}, {
			name : 'pokerMesaAtivo',
			type : 'boolean'
		}, {
			name : 'pokerParticipantes',
			type : 'auto'
		} ]
	}
});