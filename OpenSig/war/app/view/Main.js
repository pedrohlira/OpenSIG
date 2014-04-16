Ext.define('SIG.view.Main', {
	extend : 'Ext.TabPanel',
	requires : [ 'SIG.view.Home' ],
	alias : 'widget.main',

	config : {
		fullscreen : true,
		tabBar : {
			docked : 'bottom',
			layout : {
				pack : 'center'
			}
		},
		items : [ {
			xtype : 'home'
		} ]
	}
});