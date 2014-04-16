Ext.define('SIG.controller.Jogador', {
	extend : 'Ext.app.Controller',

	statics : {
		id : 0,
		clientes : null
	},

	config : {
		refs : {
			viewCash : 'cash',
			listaJogador : 'jogadorLista',
			btnCliente : 'button[action=cliente]',
			btnCodigo : 'button[action=codigo]',
			pickerJogador : 'picker',
			txtFiltro : 'textfield[id=filtro]'
		},
		control : {
			listaJogador : {
				activate : 'carregar',
				deactivate : 'limpar',
				itemtap : 'info'
			},
			btnCliente : {
				tap : 'listarCliente'
			},
			btnCodigo : {
				tap : 'inserir'
			},
			pickerJogador : {
				change : 'adicionar'
			},
			txtFiltro : {
				keyup : 'filtrar'
			}
		}
	},

	carregar : function() {
		var viewCash = this.getViewCash();
		var listaJogador = this.getListaJogador();
		var pickerJogador = this.getPickerJogador();

		// adiciona o botao de novo
		viewCash.getNavigationBar().add({
			xtype : 'button',
			align : 'right',
			text : 'Clientes',
			action : 'cliente'
		});

		// jogadores ativos
		var px = Ext.create('SIG.store.SigProxy', {
			url : '/openpoker/jogador/' + SIG.controller.Cash.id
		});

		var store = Ext.create('SIG.store.SigStore', {
			model : 'SIG.model.PokerJogador',
			proxy : px
		});
		listaJogador.setStore(store);
		store.load({
			callback : function(records, operation, success) {
				listaJogador.setMasked(false);
			}
		});
	},

	limpar : function(obj, newActiveItem, oldActiveItem, eOpts) {
		var viewCash = this.getViewCash();
		var btnCliente = this.getBtnCliente();
		viewCash.getNavigationBar().remove(btnCliente);
	},

	info : function(list, index, target, record, e, eOpts) {
		var viewCash = this.getViewCash();
		var infoJogador = Ext.create('SIG.view.JogadorInfo');

		infoJogador.setRecord(record);
		infoJogador.getFields('pokerClienteNome').setValue(
				record.get('pokerCliente').pokerClienteNome);
		this.self.id = record.get('pokerJogadorId');
		viewCash.push(infoJogador);
	},

	listarCliente : function(btn, e, eOpts) {
		// clientes ativos
		var px = Ext.create('SIG.store.SigProxy', {
			url : '/openpoker/cliente/'
		});

		SIG.controller.Jogador.clientes = Ext.create('SIG.store.SigStore', {
			model : 'SIG.model.PokerCliente',
			proxy : px,
			remoteFilter: false
		});
		SIG.controller.Jogador.clientes.load({
			callback : function(records, operation, success) {
				this.listar();
			},
			scope : this
		});
	},
	
	listar : function(){
		var pickerJogador = this.getPickerJogador();
		var dados = new Array();
		
		SIG.controller.Jogador.clientes.each(function(rec) {
			dados.push({
				text : rec.data.pokerClienteNome,
				value : rec.data.pokerClienteId,
				cod : rec.data.pokerClienteCodigo
			});
		});
		pickerJogador.setSlots({
			name : 'cliente',
			Title : 'Clientes',
			data : dados,
			itemTpl : '<div><b>{cod}</b> - <i>{text}</i></div>',
		});
		pickerJogador.setHeight(Ext.getBody().getHeight() - 100);
		pickerJogador.show();
	},

	adicionar : function(obj, value, eOpts) {
		var listaJogador = this.getListaJogador();
		// salva o jogador
		var px = Ext.create('SIG.store.SigProxy', {
			url : '/openpoker/adicionar/' + SIG.controller.Cash.id + '/'
					+ value.cliente
		});

		var store = Ext.create('SIG.store.SigStore', {
			model : 'SIG.model.PokerJogador',
			proxy : px
		});
		store.load({
			callback : function(records, operation, success) {
				listaJogador.getStore().load();
			}
		});
	},

	inserir : function(btn, e, eOpts) {
		var pickerJogador = this.getPickerJogador();
		Ext.Msg.prompt('Cliente', 'Digite o codigo do cliente!', function(
				buttonId, value) {
			if (buttonId == Ext.MessageBox.OK.itemId) {
				var cliente = SIG.controller.Jogador.clientes.findRecord(
						'pokerClienteCodigo', value, 0, false, true, true);
				if (cliente != null) {
					this.adicionar(btn, {
						'cliente' : cliente.data.pokerClienteId
					}, eOpts);
					pickerJogador.hide();
				} else {
					Ext.Msg.alert('Cliente', 'Cliente nao encontrado!',
							Ext.emptyFn);
				}
			}
		}, this);
	},

	filtrar : function(txt, e, eOpts) {	
		SIG.controller.Jogador.clientes.clearFilter();
		if (txt.getValue() != '') {
			SIG.controller.Jogador.clientes.filter("pokerClienteNome", txt.getValue(), true);
		}
		this.listar();
	}
});