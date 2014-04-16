Ext
		.define(
				'SIG.view.ParticipanteLista',
				{
					extend : 'Ext.List',
					alias : 'widget.participanteLista',

					config : {
						loadingText : 'Carregando...',
						itemTpl : '<div><b>{pokerCliente.pokerClienteCodigo}</b> - <i>{pokerCliente.pokerClienteNome}</i></div>',
						title : 'Participantes',
						emptyText : 'Nenhum Participante Ativo'
					}
				});