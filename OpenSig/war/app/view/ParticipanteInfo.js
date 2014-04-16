Ext.define('SIG.view.ParticipanteInfo', {
	extend : 'Ext.form.FormPanel',
	alias : 'widget.participanteInfo',

	config : {
		title : 'Informa&ccedil;&otilde;es',
		items : [ {
			xtype : 'titlebar',
			docked : 'top',
			ui : 'light',
			items : [ {
				text : 'Re-Buy',
				align : 'left',
				action : 'rebuy'
			}, {
				text : 'Add-On',
				align : 'left',
				action : 'addon'
			}, {
				text : 'Mudar',
				align : 'right',
				ui : 'confirm',
				action : 'mudar'
			}, {
				text : 'Remover',
				align : 'right',
				ui : 'decline',
				action : 'remover'
			} ]
		}, {
			xtype : 'fieldset',
			items : [ {
				xtype : 'textfield',
				name : 'pokerClienteNome',
				label : 'Nome',
				disabled : true
			}, {
				xtype : 'numberfield',
				name : 'pokerParticipanteReentrada',
				label : 'Re-Buys',
				disabled : true
			}, {
				xtype : 'togglefield',
				name : 'pokerParticipanteAdicional',
				label : 'Add-On',
				disabled : true
			} ]
		} ]
	}
});