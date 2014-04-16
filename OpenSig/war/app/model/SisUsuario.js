Ext.define('SIG.model.SisUsuario', {
	// classes obrigatorias
	requires : [ 'Ext.data.Model' ],
	// classe mae
	extend : 'Ext.data.Model',
	// seta um nome menor para instanciar
	alias : 'sisUsuario',
	// o campo que representa o id
	idProperty : 'sisUsuarioId',
	// configuracoes
	config : {
		// campos do modelo
		fields : [ {
			name : 'sisUsuarioId',
			type : 'int'
		}, {
			name : 'sisUsuarioLogin',
			type : 'string'
		}, {
			name : 'sisUsuarioSenha',
			type : 'string'
		}, {
			name : 'sisUsuarioAtivo',
			type : 'boolean'
		},{
			name : 'sisGrupos',
			type : 'auto'
		} ],
		// validadores
		validations : [ {
			type : 'presence',
			field : 'sisUsuarioLogin'
		}, {
			type : 'length',
			field : 'sisUsuarioLogin',
			max : 40
		}, {
			type : 'presence',
			field : 'sisUsuarioSenha'
		}, {
			type : 'length',
			field : 'sisUsuarioSenha',
			max : 40
		}, ]
	}
});