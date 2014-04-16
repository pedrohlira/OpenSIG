Ext.define('SIG.controller.Torneio', {
	extend : 'Ext.app.Controller',

	statics : {
		id : 0
	},

	config : {
		refs : {
			viewTorneio : 'torneio',
			listaTorneio : 'torneioLista'
		},
		control : {
			listaTorneio : {
				activate : 'carregar',
				itemtap : 'mesas'
			}
		}
	},

	carregar : function() {
		var listaTorneio = this.getListaTorneio();
		var px = Ext.create('SIG.store.SigProxy', {
			url : '/openpoker/torneio'
		});

		var store = Ext.create('SIG.store.SigStore', {
			model : 'SIG.model.PokerTorneio',
			proxy : px
		});
		listaTorneio.setStore(store);
		store.load({
			callback : function(records, operation, success) {
				listaTorneio.setMasked(false);
			}
		});
	},

	mesas : function(list, index, target, record, e, eOpts) {
		var viewTorneio = this.getViewTorneio();
		var listaMesa = Ext.create('SIG.view.MesaLista');
		this.self.id = record.get('pokerTorneioId');
		viewTorneio.push(listaMesa);
	}
});