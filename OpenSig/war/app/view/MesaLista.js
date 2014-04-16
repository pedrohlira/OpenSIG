Ext
		.define(
				'SIG.view.MesaLista',
				{
					extend : 'Ext.List',
					alias : 'widget.mesaLista',

					config : {
						loadingText : 'Carregando...',
						itemTpl : '<div><b>Mesa {pokerMesaNumero}</b> - Lugares : <i>{pokerParticipantes.length} / {pokerMesaLugares}</i></div>',
						title : 'Mesas',
						emptyText : 'Nenhuma Mesa Aberta'
					}
				});