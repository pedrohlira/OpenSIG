Ext.define('SIG.controller.Participante', {
	extend : 'Ext.app.Controller',

	statics : {
		id : 0
	},

	config : {
		refs : {
			viewTorneio : 'torneio',
			listaParticipante : 'participanteLista'
		},
		control : {
			listaParticipante : {
				activate : 'carregar',
				itemtap : 'info'
			}
		}
	},

	carregar : function() {
		var listaParticipante = this.getListaParticipante();
		var px = Ext.create('SIG.store.SigProxy', {
			url : '/openpoker/participante/' + SIG.controller.Mesa.id
		});

		var store = Ext.create('SIG.store.SigStore', {
			model : 'SIG.model.PokerParticipante',
			proxy : px
		});
		listaParticipante.setStore(store);
		store.load({
			callback : function(records, operation, success) {
				listaParticipante.setMasked(false);
			}
		});
	},

	info : function(list, index, target, record, e, eOpts) {
		var viewTorneio = this.getViewTorneio();
		var infoParticipante = Ext.create('SIG.view.ParticipanteInfo');

		infoParticipante.setRecord(record);
		infoParticipante.getFields('pokerClienteNome').setValue(
				record.get('pokerCliente').pokerClienteNome);
		this.self.id = record.get('pokerParticipanteId');
		viewTorneio.push(infoParticipante);
	}
});