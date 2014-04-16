/**
 * Classe de dados, que representa a massa de dados a ser manipulada.
 */
Ext.define('SIG.store.SigStore', {
			// classes obrigatorias
			requires : ['Ext.data.JsonStore'],
			// uso
			uses : ['SIG.store.SigProxy'],
			// classe mae
			extend : 'Ext.data.JsonStore',
			// seta um nome menor para instanciar
			alias : 'sigstore',

			config : {
				// ordenacao remota
				remoteSort : true,
				// filtro remoto
				remoteFilter : true,
				// tamanho da paginacao
				pageSize : 50
			}
		});