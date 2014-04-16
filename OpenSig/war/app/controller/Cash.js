Ext.define('SIG.controller.Cash', {
	extend : 'Ext.app.Controller',

	statics : {
		id : 0
	},

	config : {
		refs : {
			viewCash : 'cash',
			listaCash : 'cashLista'
		},
		control : {
			listaCash : {
				activate : 'carregar',
				itemtap : 'jogadores'
			}
		}
	},

	carregar : function() {
		var listaCash = this.getListaCash();
		var px = Ext.create('SIG.store.SigProxy', {
			url : '/openpoker/cash'
		});

		var store = Ext.create('SIG.store.SigStore', {
			model : 'SIG.model.PokerCash',
			proxy : px
		});
		listaCash.setStore(store);
		store.load({
			callback : function(records, operation, success) {
				listaCash.setMasked(false);
			}
		});
	},

	jogadores : function(list, index, target, record, e, eOpts) {
		var viewCash = this.getViewCash();
		var listaJogadores = Ext.create('SIG.view.JogadorLista');
		this.self.id = record.get('pokerCashId');
		viewCash.push(listaJogadores);
	}
});