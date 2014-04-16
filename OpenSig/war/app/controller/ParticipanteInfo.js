Ext.define('SIG.controller.ParticipanteInfo', {
	extend : 'Ext.app.Controller',

	config : {
		refs : {
			viewTorneio : 'torneio',
			infoParticipante : 'participanteInfo',
			listaParticipante : 'participanteLista',
			listaMesa : 'mesaLista',
			btnRebuy : 'button[action=rebuy]',
			btnAddon : 'button[action=addon]',
			btnRemover : 'button[action=remover]',
			btnMudar : 'button[action=mudar]'
		},
		control : {
			infoParticipante : {
				activate : 'carregar'
			},
			btnRebuy : {
				tap : 'rebuy'
			},
			btnAddon : {
				tap : 'addon'
			},
			btnRemover : {
				tap : 'remover'
			},
			btnMudar : {
				tap : 'mudar'
			}
		}
	},

	carregar : function() {
		var infoParticipante = this.getInfoParticipante();
		var rec = infoParticipante.getRecord();
		var btnAddon = this.getBtnAddon();
		var btnRebuy = this.getBtnRebuy();
		btnAddon.setDisabled(rec.get('pokerParticipanteAdicional'));
		btnRebuy.setDisabled(rec.get('pokerParticipanteAdicional'));
	},

	rebuy : function(btn, e, eOpts) {
		var infoParticipante = this.getInfoParticipante();

		Ext.Msg.confirm('Participante', 'Deseja fazer Re-Buy ?', function(buttonId, value) {
			if (buttonId == Ext.MessageBox.YES.itemId) {
				var px = Ext.create('SIG.store.SigProxy', {
					url : '/openpoker/rebuy/' + SIG.controller.Participante.id
				});
				var store = Ext.create('SIG.store.SigStore', {
					model : 'SIG.model.PokerParticipante',
					proxy : px
				});

				store.load({
					callback : function(records, operation, success) {
						var reentrada = infoParticipante.getFields('pokerParticipanteReentrada');
						reentrada.setValue(reentrada.getValue() + 1);
					}
				});
			}
		});
	},

	addon : function(btn, e, eOpts) {
		var btnAddon = this.getBtnAddon();
		var btnRebuy = this.getBtnRebuy();
		var infoParticipante = this.getInfoParticipante();

		Ext.Msg.confirm('Participante', 'Deseja fazer Add-On ?', function(buttonId, value) {
			if (buttonId == Ext.MessageBox.YES.itemId) {
				var px = Ext.create('SIG.store.SigProxy', {
					url : '/openpoker/addon/' + SIG.controller.Participante.id
				});
				var store = Ext.create('SIG.store.SigStore', {
					model : 'SIG.model.PokerParticipante',
					proxy : px
				});

				store.load({
					callback : function(records, operation, success) {
						var adicional = infoParticipante.getFields('pokerParticipanteAdicional');
						adicional.setValue(1);
						btnAddon.setDisabled(true);
						btnRebuy.setDisabled(true);
					}
				});
			}
		});
	},

	remover : function(btn, e, eOpts) {
		var viewTorneio = this.getViewTorneio();
		var listaParticipante = this.getListaParticipante();

		Ext.Msg.confirm('Participante', 'Deseja remover este participante ?',
				function(buttonId, value) {
					if (buttonId == Ext.MessageBox.YES.itemId) {
						var px = Ext.create('SIG.store.SigProxy', {
							url : '/openpoker/remover/' + SIG.controller.Participante.id
						});
						var store = Ext.create('SIG.store.SigStore', {
							model : 'SIG.model.PokerParticipante',
							proxy : px
						});

						store.load({
							callback : function(records, operation, success) {
								viewTorneio.pop(1);
								listaParticipante.getStore().load();
							}
						});
					}
				});
	},

	mudar : function(btn, e, eOpts) {
		var viewTorneio = this.getViewTorneio();
		var listaMesa = this.getListaMesa();
		var listaParticipante = this.getListaParticipante();

		Ext.Msg.prompt('Mesa', 'Escolha a mesa de destino!', function(buttonId, value) {
			if (buttonId == Ext.MessageBox.OK.itemId) {
				var px = Ext.create('SIG.store.SigProxy', {
					url : '/openpoker/mudar/' + SIG.controller.Participante.id + '/' + value
				});
				var store = Ext.create('SIG.store.SigStore', {
					model : 'SIG.model.PokerParticipante',
					proxy : px
				});

				store.load({
					callback : function(records, operation, success) {
						viewTorneio.pop(1);
						listaParticipante.getStore().load();
					}
				});
			}
		}, null, false, null, {
			placeHolder : 'Numero da mesa...'
		});

	}
});