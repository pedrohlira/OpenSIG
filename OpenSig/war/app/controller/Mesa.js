Ext.define('SIG.controller.Mesa', {
	extend : 'Ext.app.Controller',

	statics : {
		id : 0
	},
	
	config : {
		refs : {
			viewTorneio : 'torneio',
			listaMesa : 'mesaLista'
		},
		control : {
			listaMesa : {
				activate : 'carregar',
				itemtap : 'participantes'
			}
		}
	},

	carregar : function() {
		var listaMesa = this.getListaMesa();
		var px = Ext.create('SIG.store.SigProxy', {
			url : '/openpoker/mesa/' + SIG.controller.Torneio.id
		});

		var store = Ext.create('SIG.store.SigStore', {
			model : 'SIG.model.PokerMesa',
			proxy : px
		});
		listaMesa.setStore(store);
		store.load({
			callback : function(records, operation, success) {
				listaMesa.setMasked(false);
			}
		});
	},
	
	participantes : function(list, index, target, record, e, eOpts) {
		var viewTorneio = this.getViewTorneio();
		var listaParticipante = Ext.create('SIG.view.ParticipanteLista');
		this.self.id = record.get('pokerMesaId');
		viewTorneio.push(listaParticipante);
	}
});