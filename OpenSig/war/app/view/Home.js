Ext.define('SIG.view.Home', {
	extend : 'Ext.form.FormPanel',
	alias : 'widget.home',

	config : {
		iconCls : 'home',
		title : 'Inicio',
		html : [ '<div class="home"><h2>Vers&atilde;o (1.0)</h2>', '</div>' ].join(""),
		items : [ {
			xtype : 'titlebar',
			docked : 'top',
			title : 'Poker Hold&rsquo;Em Club'
		}, {
			xtype : 'fieldset',
			title : 'Login',
			items : [ {
				xtype : 'textfield',
				label : 'Nome',
				name : 'sisUsuarioLogin',
				maxLength : 40,
				required : true
			}, {
				xtype : 'passwordfield',
				label : 'Senha',
				name : 'sisUsuarioSenha',
				maxLength : 40,
				required : true
			} ]
		}, {
			xtype : 'button',
			text : 'Logar',
			ui : 'confirm',
			action : 'logar',
			hidden : false
		} , {
			xtype : 'button',
			text : 'Sair',
			ui : 'decline',
			action : 'sair',
			hidden : true
		} ]
	}
});