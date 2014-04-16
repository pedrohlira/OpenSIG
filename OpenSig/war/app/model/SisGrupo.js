Ext.define('SIG.model.SisGrupo', {
	// classes obrigatorias
	requires : [ 'Ext.data.Model' ],
	// classe mae
	extend : 'Ext.data.Model',
	// seta um nome menor para instanciar
	alias : 'sisGrupo',
	// o campo que representa o id
	idProperty : 'sisGrupoId',
	// configuracoes
	config : {
		// campos do modelo
		fields : [ {
			name : 'sisGrupoId',
			type : 'int'
		}, {
			name : 'sisGrupoNome',
			type : 'string'
		}, {
			name : 'sisGrupoDescricao',
			type : 'string'
		}, {
			name : 'sisGrupoAtivo',
			type : 'boolean'
		} ]
	}
});