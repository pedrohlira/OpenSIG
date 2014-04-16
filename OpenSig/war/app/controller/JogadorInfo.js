Ext.define('SIG.controller.JogadorInfo', {
	extend : 'Ext.app.Controller',

	statics : {
		stForma : null
	},

	config : {
		refs : {
			viewCash : 'cash',
			infoJogador : 'jogadorInfo',
			listaJogador : 'jogadorLista',
			btnReceber : 'button[action=receber]',
			btnPagar : 'button[action=pagar]',
			btnExcluir : 'button[action=excluir]',
		},
		control : {
			infoJogador : {
				activate : 'carregar',
				deactivate : 'limpar',
			},
			btnReceber : {
				tap : 'receber'
			},
			btnPagar : {
				tap : 'pagar'
			},
			btnExcluir : {
				tap : 'excluir'
			}
		}
	},

	carregar : function() {
		var viewCash = this.getViewCash();
		var infoJogador = this.getInfoJogador();
		infoJogador.getFields('pokerForma').setRecord(null);
		infoJogador.getFields('pokerValor').setValue("");

		// adiciona os botoes
		viewCash.getNavigationBar().add({
			xtype : 'button',
			align : 'left',
			ui : 'action',
			text : 'Receber',
			action : 'receber'
		});
		viewCash.getNavigationBar().add({
			xtype : 'button',
			align : 'right',
			ui : 'action',
			text : 'Pagar',
			action : 'pagar'
		});
		viewCash.getNavigationBar().add({
			xtype : 'button',
			align : 'right',
			ui : 'decline',
			text : 'Excluir',
			action : 'excluir'
		});

		if (this.self.stForma == null) {
			var pxForma = Ext.create('SIG.store.SigProxy', {
				url : '/openpoker/forma'
			});
			this.self.stForma = Ext.create('SIG.store.SigStore', {
				model : 'SIG.model.PokerForma',
				proxy : pxForma,
				autoLoad : true
			});
		}
		infoJogador.getFields('pokerForma').setStore(this.self.stForma);
	},

	limpar : function(obj, newActiveItem, oldActiveItem, eOpts) {
		var viewCash = this.getViewCash();
		var btnReceber = this.getBtnReceber();
		var btnPagar = this.getBtnPagar();
		var btnExcluir = this.getBtnExcluir();

		viewCash.getNavigationBar().remove(btnReceber);
		viewCash.getNavigationBar().remove(btnPagar);
		viewCash.getNavigationBar().remove(btnExcluir);
	},

	receber : function(btn, e, eOpts) {
		var infoJogador = this.getInfoJogador();
		var forma = infoJogador.getFields('pokerForma').getValue();
		var valor = infoJogador.getFields('pokerValor').getValue();

		if (forma != null && valor > 0) {
			Ext.Msg.confirm('Receber', 'Efetuar recebimento ?', function(
					buttonId, value) {
				if (buttonId == Ext.MessageBox.YES.itemId) {
					var px = Ext.create('SIG.store.SigProxy', {
						url : '/openpoker/receber/' + SIG.controller.Jogador.id
								+ '/' + forma + '/' + valor
					});
					var store = Ext.create('SIG.store.SigStore', {
						model : 'SIG.model.PokerJogador',
						proxy : px
					});

					store.load({
						callback : function(records, operation, success) {
							Ext.Msg.alert('Receber', "Executado com sucesso!",
									Ext.emptyFn);
							infoJogador.getFields('pokerValor').setValue("");
						}
					});
				}
			});
		} else {
			Ext.Msg.alert('Receber', "Informe o tipo e valor!", Ext.emptyFn);
		}
	},

	pagar : function(btn, e, eOpts) {
		var infoJogador = this.getInfoJogador();
		var forma = infoJogador.getFields('pokerForma').getValue();
		var valor = infoJogador.getFields('pokerValor').getValue();

		if (forma != null && valor > 0) {
			Ext.Msg.confirm('Pagar', 'Efetuar pagamento ?', function(buttonId,
					value) {
				if (buttonId == Ext.MessageBox.YES.itemId) {
					var px = Ext.create('SIG.store.SigProxy', {
						url : '/openpoker/pagar/' + SIG.controller.Jogador.id
								+ '/' + forma + '/' + valor
					});
					var store = Ext.create('SIG.store.SigStore', {
						model : 'SIG.model.PokerJogador',
						proxy : px
					});

					store.load({
						callback : function(records, operation, success) {
							Ext.Msg.alert('Pagar', "Executado com sucesso!",
									Ext.emptyFn);
							infoJogador.getFields('pokerValor').setValue("");
						}
					});
				}
			});
		} else {
			Ext.Msg.alert('Pagar', "Informe o tipo e valor!", Ext.emptyFn);
		}
	},

	excluir : function(btn, e, eOpts) {
		var viewCash = this.getViewCash();
		var infoJogador = this.getInfoJogador();
		var listaJogador = this.getListaJogador();

		Ext.Msg.show({
			title : 'Excluir',
			message : 'Deseja quitar as pendencias ?',
			buttons : Ext.MessageBox.YESNOCANCEL,
			fn : function(buttonId, value) {
				if (buttonId != Ext.MessageBox.CANCEL.itemId) {
					var quitar = buttonId == Ext.MessageBox.YES.itemId;
					var px = Ext.create('SIG.store.SigProxy', {
						url : '/openpoker/excluir/' + SIG.controller.Jogador.id
								+ '/' + quitar
					});
					var store = Ext.create('SIG.store.SigStore', {
						model : 'SIG.model.PokerJogador',
						proxy : px
					});

					store.load({
						callback : function(records, operation, success) {
							viewCash.pop(1);
							listaJogador.getStore().load();
						}
					});
				}
			}
		});
	}

});