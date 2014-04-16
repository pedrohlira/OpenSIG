Ext.define('SIG.model.PokerCash', {
	// classes obrigatorias
	requires : [ 'Ext.data.Model' ],
	// classe mae
	extend : 'Ext.data.Model',
	// seta um nome menor para instanciar
	alias : 'pokerCash',
	// o campo que representa o id
	idProperty : 'pokerCashId',
	// configuracoes
	config : {
		// campos do modelo
		fields : [ {
			name : 'pokerCashId',
			type : 'int'
		}, {
			name : 'pokerCashCodigo',
			type : 'string'
		}, {
			name : 'pokerCashPago',
			type : 'float'
		}, {
			name : 'pokerCashRecebido',
			type : 'float'
		}, {
			name : 'pokerCashSaldo',
			type : 'float'
		}, {
			name : 'pokerCashFechado',
			type : 'boolean'
		}, {
			name : 'pokerCashData',
			type : 'date'
		} ]
	}
});