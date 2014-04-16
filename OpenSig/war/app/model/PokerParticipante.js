Ext.define('SIG.model.PokerParticipante', {
	// classes obrigatorias
	requires : [ 'Ext.data.Model' ],
	// classe mae
	extend : 'Ext.data.Model',
	// seta um nome menor para instanciar
	alias : 'pokerParticipante',
	// o campo que representa o id
	idProperty : 'pokerParticipanteId',
	// configuracoes
	config : {
		// campos do modelo
		fields : [ {
			name : 'pokerParticipanteId',
			type : 'int'
		}, {
			name : 'pokerParticipanteAdicional',
			type : 'boolean'
		}, {
			name : 'pokerParticipantePonto',
			type : 'int'
		}, {
			name : 'pokerParticipanteAtivo',
			type : 'boolean'
		}, {
			name : 'pokerParticipantePosicao',
			type : 'int'
		}, {
			name : 'pokerParticipanteReentrada',
			type : 'int'
		}, {
			name : 'pokerParticipantePremio',
			type : 'float'
		}, {
			name : 'pokerCliente',
			type : 'auto'
		} ]
	}
});