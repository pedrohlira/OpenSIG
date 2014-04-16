Ext.define('SIG.view.Cash', {
	extend : 'Ext.navigation.View',
	requires : [ 'SIG.view.CashLista' ],
	alias : 'widget.cash',

	config : {
		iconCls : 'team',
		title : 'Cash',
		useTitleForBackButtonText : true,

		items : [ {
			xtype : 'cashLista'
		} ]
	}
});