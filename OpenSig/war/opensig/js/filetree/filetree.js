Ext.BLANK_IMAGE_URL = '../ext/resources/images/default/s.gif';
Ext.onReady(function() {
    Ext.QuickTips.init();
    Ext.form.Field.prototype.msgTarget = 'side';
	Ext.state.Manager.setProvider(new Ext.state.CookieProvider);

	var themeCombo = new Ext.ux.ThemeCombo({renderTo:'comboct', width:140});
	var langCombo = new Ext.ux.LangSelectCombo({renderTo:'langcomboct', width:140, editable:false});
	langCombo.on('select', function() {document.cookie = 'locale=' + this.getValue()});

	// window with uploadpanel
    var win = new Ext.Window({
         width:180
		,minWidth:165
        ,id:'winid'
        ,height:220
		,minHeight:200
        ,layout:'fit'
        ,border:false
        ,closable:false
        ,title:'UploadPanel'
		,iconCls:'icon-upload'
		,items:[{
			  xtype:'uploadpanel'
			 ,buttonsAt:'tbar'
			 ,id:'uppanel'
			 ,url:'filetree.php'
			 ,path:'root'
			 ,maxFileSize:1048576
//			 ,enableProgress:false
//			 ,singleUpload:true
		}]
    });
    win.show();

	var treepanel = new Ext.ux.FileTreePanel({
		 width:284
		,height:400
		,id:'ftp'
		,title:'FileTreePanel'
		,renderTo:'treepanel'
		,rootPath:'root'
		,topMenu:true
		,autoScroll:true
		,enableProgress:false
//		,baseParams:{additional:'haha'}
//		,singleUpload:true
	});

//	var links = new Ext.Panel({
//		 renderTo:'linksct'
//		,autoLoad:'links.html'
//		,border:false
//	});
});

