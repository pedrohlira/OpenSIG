Ext
		.define(
				'SIG.view.JogadorLista',
				{
					extend : 'Ext.List',
					alias : 'widget.jogadorLista',

					config : {
						loadingText : 'Carregando...',
						itemTpl : '<div><b>{pokerCliente.pokerClienteCodigo}</b> - <i>{pokerCliente.pokerClienteNome}</i></div>',
						title : 'Jogadores',
						emptyText : 'Nenhum Jogador Ativo',
						items : [ {
							xtype : 'picker',
							hidden : true,
							cancelButton : 'Cancelar',
							doneButton : 'Adicionar',
							toolbar : {
								items : [ {
									xtype : 'textfield',
									id : 'filtro',
									placeHolder : 'Digite para filtrar',
									width : '200px'
								}, {
									xtype : 'button',
									text : 'Por Codigo',
									align : 'right',
									ui : 'confirm',
									action : 'codigo'
								} ]
							}
						} ]
					}
				});