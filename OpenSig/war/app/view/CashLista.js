Ext
		.define(
				'SIG.view.CashLista',
				{
					extend : 'Ext.List',
					alias : 'widget.cashLista',

					config : {
						loadingText : 'Carregando...',
						itemTpl : '<div>Codigo : <b>{pokerCashId}</b> - Mesa : <i>{pokerCashMesa}</i></div>',
						title : 'Cashs',
						emptyText : 'Nenhum Cash Aberto'
					}
				});