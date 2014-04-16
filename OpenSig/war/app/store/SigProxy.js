/**
 * Classe de proxy, que faz a comunicacao com o servidor.
 */
Ext.define('SIG.store.SigProxy', {
			// classe mae
			extend : 'Ext.data.proxy.Rest',
			// seta um nome menor para instanciar
			alias : 'sigproxy',

			config : {
				// seta o tipo
				type : 'rest',
				// seta o nome do parametro de inicio
				startParam : 'inicio',
				// seta o nome do parametro de limite
				limitParam : 'limite',
				// seta o nome do parametro de pagina
				pageParam : 'pagina',
				// seta o nome do parametro de filtro
				filterParam : 'filtro',
				// seta o nome do parametro de ordem
				sortParam : 'ordem',
				// seta o nome do parametro extras
				extraParams : 'extras',
				// setando o cabecalho para identificar o tipo de dado
				headers : {
					'Content-Type' : 'application/json',
					'Accept' : 'application/json'
				},
				// Setando o modo de leitura dos dados
				reader : {
					type : 'json',
					root : 'lista.lista',
					rootProperty : 'lista.lista',
					totalProperty : 'lista.total'
				},
				writer : {
					type : 'json',
					allowSingle : true
				}
			}
		});