Ext
		.define(
				'SIG.view.TorneioLista',
				{
					extend : 'Ext.List',
					alias : 'widget.torneioLista',

					config : {
						loadingText : 'Carregando...',
						itemTpl : '<div>Tipo : {pokerTorneioTipo.pokerTorneioTipoNome} - Id : <b>{pokerTorneioCodigo}</b> - Nome : <i>{pokerTorneioNome}</i></div>',
						title : 'Torneios',
						emptyText : 'Nenhum Torneio Aberto'
					}
				});