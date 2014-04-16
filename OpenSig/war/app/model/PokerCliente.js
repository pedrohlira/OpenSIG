Ext.define('SIG.model.PokerCliente', {
	// classes obrigatorias
	requires : [ 'Ext.data.Model' ],
	// classe mae
	extend : 'Ext.data.Model',
	// seta um nome menor para instanciar
	alias : 'pokerCliente',
	// o campo que representa o id
	idProperty : 'pokerClienteId',
	// configuracoes
	config : {
		// campos do modelo
		fields : [ {
			name : 'pokerClienteId',
			type : 'int'
		}, {
			name : 'pokerClienteAssociado',
			type : 'boolean'
		}, {
			name : 'pokerClienteCodigo',
			type : 'int'
		}, {
			name : 'pokerClienteAtivo',
			type : 'boolean'
		}, {
			name : 'pokerClienteAuxiliar',
			type : 'int'
		}, {
			name : 'pokerClienteContato',
			type : 'string'
		}, {
			name : 'pokerClienteDocumento',
			type : 'string'
		}, {
			name : 'pokerClienteEmail',
			type : 'string'
		}, {
			name : 'pokerClienteNome',
			type : 'string'
		}, {
			name : 'pokerClienteData',
			type : 'date'
		} ]
	}
});