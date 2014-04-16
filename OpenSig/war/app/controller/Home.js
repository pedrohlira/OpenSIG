Ext.define('SIG.controller.Home', {
	extend : 'Ext.app.Controller',

	config : {
		refs : {
			main : 'main',
			frmHome : 'home',
			btnLogar : 'button[action=logar]',
			btnSair : 'button[action=sair]',
			txtSenha : 'passwordfield[name=sisUsuarioSenha]'
		},
		control : {
			btnLogar : {
				tap : 'entrar'
			},
			btnSair : {
				tap : 'sair'
			},
			txtSenha : {
				keyup : 'validar'
			}
		}
	},

	validar : function(txt, e, eOpts) {
		if (e.event.keyCode == 13) {
			this.entrar();
		}
	},

	entrar : function() {
		var me = this;
		var frmHome = this.getFrmHome();
		var usuario = Ext.create('SIG.model.SisUsuario', frmHome.getValues());
		var errors = usuario.validate();

		if (errors.isValid()) {
			var senha = this.getTxtSenha();
			senha.setValue(encrypt.sha1(senha.getValue()));
			frmHome.submit({
				url : '/openpoker/logar',
				method : 'POST',
				failure : function(form, response) {
					frmHome.setValues({
						sisUsuarioLogin : '',
						sisUsuarioSenha : ''
					});

					if (response.responseText == undefined) {
						me.logado(true, response.sisGrupos);
					} else {
						Ext.Msg.alert('Login', response.responseText,
								Ext.emptyFn);
					}
				}
			});
		} else {
			Ext.Msg.alert('Login', 'Todos os campos sao obrigatorios!',
					Ext.emptyFn);
		}
	},

	sair : function() {
		var me = this;
		var operation = Ext.create('Ext.data.Operation', {
			action : 'read'
		});
		var proxy = Ext.create('Ext.data.proxy.Ajax', {
			url : '/openpoker/deslogar'
		});
		proxy.read(operation, function() {
			me.logado(false);
		});
	},

	logado : function(hidden, grupos) {
		var btnLogar = this.getBtnLogar();
		var btnSair = this.getBtnSair();
		var frmHome = this.getFrmHome();
		var main = this.getMain();

		btnLogar.setHidden(hidden);
		btnSair.setHidden(!hidden);

		if (hidden) {
			Ext.Array.each(grupos, function(grupo) {
				if (grupo.sisGrupoNome == 'CROUPIER') {
					main.add({
						xtype : 'torneio'
					});
				}
				if (grupo.sisGrupoNome == 'CAIXA') {
					main.add({
						xtype : 'cash'
					});
				}
			});
			main.setActiveItem(1);
		} else {
			for (var i = main.items.length; i > 1; i--) {
				main.removeAt(i);
			}
		}
	}
});