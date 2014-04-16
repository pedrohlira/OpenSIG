Ext.define('SIG.view.JogadorInfo', {
	extend : 'Ext.form.FormPanel',
	alias : 'widget.jogadorInfo',

	config : {
		title : 'A&ccedil;&otilde;es',
		items : [ {
			xtype : 'fieldset',
			items : [ {
				xtype : 'textfield',
				name : 'pokerClienteNome',
				label : 'Nome',
				disabled : true
			}, {
				xtype : 'selectfield',
				name : 'pokerForma',
				label : 'Tipo',
				required : true,
				displayField : 'pokerFormaNome',
				valueField : 'pokerFormaId'
			}, {
				xtype : 'numberfield',
				name : 'pokerValor',
				label : 'Valor',
				required : true,
			} ]
		} ]
	}
});