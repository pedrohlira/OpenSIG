Ext.define('SIG.model.PokerTorneio', {
	// classes obrigatorias
	requires : [ 'Ext.data.Model' ],
	// classe mae
	extend : 'Ext.data.Model',
	// seta um nome menor para instanciar
	alias : 'pokerTorneio',
	// o campo que representa o id
	idProperty : 'pokerTorneioId',
	// configuracoes
	config : {
		// campos do modelo
		fields : [ {
			name : 'pokerTorneioId',
			type : 'int'
		}, {
			name : 'pokerTorneioAdicional',
			type : 'float'
		}, {
			name : 'pokerTorneioCodigo',
			type : 'string'
		}, {
			name : 'pokerTorneioTaxa',
			type : 'float'
		}, {
			name : 'pokerTorneioComissao',
			type : 'float'
		}, {
			name : 'pokerTorneioData',
			type : 'date'
		}, {
			name : 'pokerTorneioEntrada',
			type : 'float'
		}, {
			name : 'pokerTorneioEntradaFicha',
			type : 'int'
		}, {
			name : 'pokerTorneioReentradaFicha',
			type : 'int'
		}, {
			name : 'pokerTorneioAdicionalFicha',
			type : 'int'
		}, {
			name : 'pokerTorneioAdicionalNivel',
			type : 'int'
		}, {
			name : 'pokerTorneioNome',
			type : 'string'
		}, {
			name : 'pokerTorneioPonto',
			type : 'int'
		}, {
			name : 'pokerTorneioPremio',
			type : 'float'
		}, {
			name : 'pokerTorneioArrecadado',
			type : 'float'
		}, {
			name : 'pokerTorneioReentrada',
			type : 'float'
		}, {
			name : 'pokerTorneioAtivo',
			type : 'boolean'
		}, {
			name : 'pokerTorneioFechado',
			type : 'boolean'
		}, {
			name : 'pokerTorneioTipo',
			type : 'auto'
		} ]
	}
});