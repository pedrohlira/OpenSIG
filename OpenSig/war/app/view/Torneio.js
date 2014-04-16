Ext.define('SIG.view.Torneio', {
	extend : 'Ext.navigation.View',
	requires : [ 'SIG.view.TorneioLista' ],
	alias : 'widget.torneio',

	config : {
		iconCls : 'star',
		title : 'Torneios',
		useTitleForBackButtonText : true,

		items : [ {
			xtype : 'torneioLista'
		} ]
	}
});