Ext
		.define(
				'SIG.view.CashLista',
				{
					extend : 'Ext.List',
					alias : 'widget.cashLista',

					config : {
						loadingText : 'Carregando...',
						itemTpl : '<div>Codigo : <b>{pokerCashCodigo}</b> - Data : <i>{pokerCashData:date("d/m/Y")}</i></div>',
						title : 'Cashs',
						emptyText : 'Nenhum Cash Aberto'
					}
				});