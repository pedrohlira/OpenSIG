// vim: ts=4:sw=4:nu:fdc=4:nospell
/**
* Ext.ux.FileTreeMenu
*
* @author Ing. Jozef Sakáloš
* @version $Id: Ext.ux.FileTreeMenu.js 112 2008-03-28 21:11:17Z jozo $
* @date 13. March 2008
*
* @license Ext.ux.FileField is licensed under the terms of
* the Open Source LGPL 3.0 license. Commercial use is permitted to the extent
* that the code/component(s) do NOT become part of another Open Source or Commercially
* licensed development library or toolkit without explicit permission.
*
* License details: http://www.gnu.org/licenses/lgpl.html
*/

/*global Ext */

/**
* @class Ext.ux.FileTreeMenu
* @extends Ext.menu.Menu
* @constructor
* Creates new FileTreeMenu object
* @param {Object} config A configuration object
*/
Ext.ux.FileTreeMenu = function(config) {
config = config || {};

var uploadPanelConfig = {
contextmenu:this
,buttonsAt:config.buttonsAt || 'tbar'
,singleUpload:config.singleUpload || false
,maxFileSize:config.maxFileSize
,enableProgress:config.enableProgress
};
if(config.baseParams) {
config.baseParams.cmd = config.baseParams.cmd || 'upload';
config.baseParams.dir = config.baseParams.dir || '.';
uploadPanelConfig.baseParams = config.baseParams;
}

// {{{
Ext.apply(config, {
items:[{
text:'&#160'
,cls:'ux-ftm-nodename'
,disabledClass:''
,disabled:true
,cmd:'nodename'
},{
text:this.openText + ' (Enter)'
,iconCls:this.openIconCls
,cmd:'open'
,menu:{
items:[{
text:this.openSelfText
,iconCls:this.openSelfIconCls
,cmd:'open-self'
},{
text:this.openPopupText
,iconCls:this.openPopupIconCls
,cmd:'open-popup'
},{
text:this.openBlankText
,iconCls:this.openBlankIconCls
,cmd:'open-blank'
},{
text:this.openDwnldText
,iconCls:this.openDwnldIconCls
,cmd:'open-dwnld'
}]
}
}
,new Ext.menu.Separator({cmd:'sep-open'})
,{
text:this.reloadText + ' (Ctrl+E)'
,iconCls:this.reloadIconCls
,cmd:'reload'
},{
text:this.expandText + ' (Ctrl+&nbsp;&rarr;)'
,iconCls:this.expandIconCls
,cmd:'expand'
},{
text:this.collapseText + ' (Ctrl+&nbsp;&larr;)'
,iconCls:this.collapseIconCls
,cmd:'collapse'
}
,new Ext.menu.Separator({cmd:'sep-collapse'})
,{
text:this.renameText + ' (F2)'
,iconCls:this.renameIconCls
,cmd:'rename'
},{
text:this.deleteText + ' (' + this.deleteKeyName + ')'
,iconCls:this.deleteIconCls
,cmd:'delete'
},{
text:this.newdirText + '... (Ctrl+N)'
,iconCls:this.newdirIconCls
,cmd:'newdir'
}
,new Ext.menu.Separator({cmd:'sep-upload'})
,{
text:this.uploadFileText + ' (Ctrl+U)'
,iconCls:this.uploadIconCls
,hideOnClick:false
,cmd:'upload'
}
,new Ext.menu.Adapter(new Ext.ux.UploadPanel(uploadPanelConfig), {
hideOnClick:false
,cmd:'upload-panel'
})
]
}); // eo apply
// }}}

// call parent
Ext.ux.FileTreeMenu.superclass.constructor.call(this, config);

// relay event from submenu
this.relayEvents(this.getItemByCmd('open').menu, ['click', 'itemclick']);

}; // eo constructor

Ext.extend(Ext.ux.FileTreeMenu, Ext.menu.Menu, {
// configuration options overridable from outside
/**
* @cfg {String} collapseIconCls icon class for collapse all item
*/
collapseIconCls:'icon-collapse-all'

/**
* @cfg {String} collapseText text for collapse all item
*/
,collapseText: 'Collapse all'

/**
* @cfg {String} deleteIconCls icon class for delete item
*/
,deleteIconCls:'icon-cross'

/**
* @cfg {String} deleteKeyName text for delete item shortcut
*/
,deleteKeyName:'Delete Key'

/**
* @cfg {String} deleteText text for delete item
*/
,deleteText:'Delete'

/**
* @cfg {String} expandIconCls icon class for expand all item
*/
,expandIconCls:'icon-expand-all'

/**
* @cfg {String} expandText text for expand all item
*/
,expandText: 'Expand all'

/**
* @cfg {String} newdirIconCls icon class for new directory item
*/
,newdirIconCls:'icon-folder-add'

/**
* @cfg {String} newdirText text for new directory item
*/
,newdirText:'New folder'

/**
* @cfg {String} openBlankIconCls icon class for open in new window item
*/
,openBlankIconCls:'icon-open-blank'

/**
* @cfg {String} openBlankText text for open in new window item
*/
,openBlankText:'Open in new window'

/**
* @cfg {String} openDwnldIconCls icon class for download item
*/
,openDwnldIconCls:'icon-open-download'

/**
* @cfg {String} openDwnldText text for download item
*/
,openDwnldText:'Download'

/**
* @cfg {String} openIconCls icon class for open submenu
*/
,openIconCls:'icon-open'

/**
* @cfg {String} openPopupIconCls icon class for open in popup item
*/
,openPopupIconCls:'icon-open-popup'


/**
* @cfg {String} text for open in poput item
*/
,openPopupText:'Open in popup'

/**
* @cfg {String} openSelfIconCls icon class for open in this window item
*/
,openSelfIconCls:'icon-open-self'

/**
* @cfg {String} openSelfText text for open in this window item
*/
,openSelfText:'Open in this window'

/**
* @cfg {String} openText text for open submenu
*/
,openText:'Open'

/**
* @cfg {String} reloadIconCls icon class for reload item
*/
,reloadIconCls:'icon-refresh'

/**
* @cfg {String} reloadText text for reload item
*/
,reloadText:'R<span style="text-decoration:underline">e</span>load'

/**
* @cfg {String} icon class for rename item
*/
,renameIconCls:'icon-pencil'

/**
* @cfg {String} renameText text for rename item
*/
,renameText: 'Rename'

/**
* @cfg {String} uploadFileText text for upload file item
*/
,uploadFileText:'<span style="text-decoration:underline">U</span>pload file'

/**
* @cfg {String} uploadIconCls icon class for upload file item
*/
,uploadIconCls:'icon-upload'

/**
* @cfg {String} uploadText text for word 'Upload'
*/
,uploadText:'Upload'

/**
* @cfg {Number} width Width of the menu.
* Cannot be empty as we have upload panel inside.
*/
,width:190

// {{{
/**
* Returns menu item identified by cmd. Unique cmd is used to identify menu items.
* I cannot use ids as they are applied to underlying DOM elements that would prevent
* to have more than one menu on the page.
* @param {String} cmd
* Valid cmds are:
* - nodename
* - open
* - open-self
* - open-popup
* - open-blank
* - open-dwnld
* - sep-open (for separator after open submenu)
* - reload
* - expand
* - collapse
* - sep-collapse (for separator after collapse item)
* - rename
* - delete
* - newdir
* - sep-upload (for separator before upload panel)
* - upload (for upload file item that does nothing)
* - upload-panel (for upload panel)
* @return {Ext.menu.Item} menu item
*/
,getItemByCmd:function(cmd) {
var open;
var item = this.items.find(function(i) {
return cmd === i.cmd;
});
if(!item) {
open = this.items.find(function(i) {
return 'open' === i.cmd;
});
if(!open) {
return null;
}
item = open.menu.items.find(function(i) {
return cmd === i.cmd;
});
}
return item;
} // eo function getItemByCmd
// }}}
// {{{
/**
* Sets/Unsets item identified by cmd to disabled/enabled state
* @param {String} cmd Item indentifier, see getItemByCmd for explanation
* @param {Boolean} disabled true to disable the item
*/
,setItemDisabled:function(cmd, disabled) {
var item = this.getItemByCmd(cmd);
if(item) {
item.setDisabled(disabled);
}
} // eo function setItemDisabled
// }}}
// {{{
/**
* destroys uploadPanel if we have one
* @private
*/
,beforeDestroy:function() {
var uploadPanel = this.getItemByCmd('upload-panel');
if(uploadPanel && uploadPanel.component) {
uploadPanel.component.purgeListeners();
uploadPanel.component.destroy();
uploadPanel.component = null;
}
} // eo function beforeDestroy
// }}}

}); // eo extend

// register xtype
Ext.reg('filetreemenu', Ext.ux.FileTreeMenu);

// eof

// vim: ts=4:sw=4:nu:fdc=4:nospell
/**
* Ext.ux.FileTreePanel
*
* @author Ing. Jozef Sakáloš
* @version $Id: Ext.ux.FileTreePanel.js 112 2008-03-28 21:11:17Z jozo $
* @date 13. March 2008
*
* @license Ext.ux.FileTreePanel is licensed under the terms of
* the Open Source LGPL 3.0 license. Commercial use is permitted to the extent
* that the code/component(s) do NOT become part of another Open Source or Commercially
* licensed development library or toolkit without explicit permission.
*
* License details: http://www.gnu.org/licenses/lgpl.html
*/

/*global Ext, window, document, setTimeout */

/**
* @class Ext.ux.FileTreePanel
* @extends Ext.tree.TreePanel
*/

Ext.ux.FileTreePanel = Ext.extend(Ext.tree.TreePanel, {
// config variables overridable from outside
// {{{
/**
* @cfg {Object} baseParams This object is not used directly by FileTreePanel but it is
* propagated to lower level objects instead. Included here for convenience.
*/

/**
* @cfg {String} confirmText Text to display as title of confirmation message box
*/
confirmText:'Confirm'

/**
* @cfg {Boolean} containerScroll true to register
* this container with ScrollManager (defaults to true)
*/
,containerScroll:true

/**
* @cfg {String} deleteText Delete text (for message box title or other displayed texts)
*/
,deleteText:'Delete'

/**
* @cfg {String} deleteUrl URL to use when deleting; this.url is used if not set (defaults to undefined)
*/

/**
* @cfg {String} downloadUrl URL to use when downloading; this.url is used if not set (defaults to undefined)
*/

/**
* @cfg {Boolean} enableDD true to enable drag & drop of files and folders (defaults to true)
*/
,enableDD:true


/**
* @cfg {Boolean) enableDelete true to enable to delete files and directories.
* If false context menu item is not shown (defaults to true)
*/
,enableDelete:true

/**
* @cfg {Boolean) enableNewDir true to enable to create new directory.
* If false context menu item is not shown (defaults to true)
*/
,enableNewDir:true

/**
* @cfg {Boolean) enableOpen true to enable open submenu
* If false context menu item is not shown (defaults to true)
*/
,enableOpen:true

/**
* @cfg {Boolean} enableProgress true to enable querying server for progress information
* Passed to underlying uploader. Included here for convenience.
*/
,enableProgress:true

/**
* @cfg {Boolean) enableRename true to enable to rename files and directories.
* If false context menu item is not shown (defaults to true)
*/
,enableRename:true

/**
* @cfg {Boolean} enableSort true to enable sorting of tree. See also folderSort (defaults to true)
*/
,enableSort:true

/**
* @cfg {Boolean) enableUpload true to enable to upload files.
* If false context menu item is not shown (defaults to true)
*/
,enableUpload:true

/**
* @cfg {String} errorText Text to display for an error
*/
,errorText:'Error'

/**
* @cfg {String} existsText Text to display in message box if file exists
*/
,existsText:'File <b>{0}</b> already exists'

/**
* @cfg {Boolean} true to expand root node on FileTreePanel render (defaults to true)
*/
,expandOnRender:true

/**
* @cfg {String} fileCls class prefix to add to nodes. "-extension" is appended to
* this prefix to form filetype class, for example: file-odt, file-pdf. These classes
* are used to display correct filetype icons in the tree. css file and icons must
* exist of course.
*/
,fileCls:'file'

/**
* @cfg {String} fileText
*/
,fileText:'File'

/**
* @cfg {Boolean} focusPopup true to focus new browser popup window for 'popup' openMode
* (defaults to true)
*/
,focusPopup:true

/**
* @cfg {Boolean} folderSort true to place directories at the top of the tree (defaults to true)
*/
,folderSort:true

/**
* @cfg {String} hrefPrefix Text to prepend before file href for file open command.
* (defaults to '')
*/
,hrefPrefix:''

/**
* @cfg {String} hrefSuffix Text to append to file href for file open command.
* (defaults to '')
*/
,hrefSuffix:''

/**
* @cfg {String} layout Layout to use for this panel (defaults to 'fit')
*/
,layout:'fit'

/**
* @cfg {String} loadingText Text to use for load mask msg
*/
,loadingText:'Loading'

/**
* @cfg {Boolean} loadMask True to mask tree panel while loading
*/
,loadMask:false

/**
* @cfg {Number} maxFileSize Maximum upload file size in bytes
* This config property is propagated down to uploader for convenience
*/
,maxFileSize:524288

/**
* @cfg {Number} maxMsgLen Maximum message length for message box (defaults to 2000).
* If message is longer Ext.util.Format.ellipsis is used to truncate it and append ...
*/
,maxMsgLen:2000

/**
* @cfg {String} method Method to use when posting to server. Other valid value is 'get'
* (defaults to 'post')
*/
,method:'post'

/**
* @cfg {String} newdirText Default name for new directories (defaults to 'New Folder')
*/
,newdirText:'New Folder'

/**
* @cfg {String} newdirUrl URL to use when creating new directory;
* this.url is used if not set (defaults to undefined)
*/

/**
* @cfg {String} openMode Default file open mode. This mode is used when user dblclicks
* a file. Other valid values are '_self', '_blank' and 'download' (defaults to 'popup')
*/
,openMode:'popup'

/**
* @cfg {String} overwriteText Text to use in overwrite confirmation message box
*/
,overwriteText:'Do you want to overwrite it?'

/**
* @cfg {String} popupFeatures Features for new browser window opened by popup open mode
*/
,popupFeatures:'width=800,height=600,dependent=1,scrollbars=1,resizable=1,toolbar=1'

/**
* @cfg {Boolean} readOnly true to disable write operations. treeEditor and context menu
* are not created if true (defaults to false)
*/
,readOnly:false

/**
* @cfg {String} reallyWantText Text to display for that question
*/
,reallyWantText:'Do you really want to'

/**
* @cfg {String} renameUrl URL to use when renaming; this.url is used if not set (defaults to undefined)
*/

/**
* @cfg {String} rootPath Relative path pointing to the directory that is root of this tree (defaults to 'root')
*/
,rootPath:'root'

/**
* @cfg {String} rootText Text to display for root node (defaults to 'Tree Root')
*/
,rootText:'Tree Root'

/**
* @cfg {Boolean} selectOnEdit true to select the edited text on edit start (defaults to true)
*/
,selectOnEdit:true

/**
* @cfg {Boolean} singleUpload true to upload files in one form, false to upload one by one
* This config property is propagated down to uploader for convenience
*/
,singleUpload:false

/**
* @cfg {Boolean} topMenu true to create top toolbar with menu in addition to contextmenu
*/
,topMenu:false

/**
* @cfg {String} url URL to use when communicating with server
*/
,url:'filetree.php'
// }}}

// overrides
// {{{
/**
* called by Ext when instantiating
* @private
* @param {Object} config Configuration object
*/
,initComponent:function() {


// {{{
Ext.apply(this, {

// create root node
root:new Ext.tree.AsyncTreeNode({
text:this.rootText
,path:this.rootPath
,allowDrag:false
})

// create treeEditor
,treeEditor:!this.readOnly ? new Ext.tree.TreeEditor(this, {
allowBlank:false
,cancelOnEsc:true
,completeOnEnter:true
,ignoreNoChange:true
,selectOnFocus:this.selectOnEdit
}) : undefined

// drop config
,dropConfig:this.dropConfig ? this.dropConfig : {
ddGroup:this.ddGroup || 'TreeDD'
,appendOnly:this.enableSort
,expandDelay:3600000 // do not expand on drag over node
}

// create treeSorter
,treeSorter:this.enableSort ? new Ext.tree.TreeSorter(this, {folderSort:this.folderSort}) : undefined

// {{{
,keys:[{
// Enter = open
key:Ext.EventObject.ENTER, scope:this
,fn:function(key, e) {
var sm = this.getSelectionModel();
var node = sm.getSelectedNode();
if(node && 0 !== node.getDepth() && node.isLeaf()) {
this.openNode(node);
}
}},{
// F2 = edit
key:113, scope:this
,fn:function(key, e) {
var sm = this.getSelectionModel();
var node = sm.getSelectedNode();
if(node && 0 !== node.getDepth() && this.enableRename && this.readOnly !== true) {
this.treeEditor.triggerEdit(node);
}
}},{
// Delete Key = Delete
key:46, stopEvent:true, scope:this
,fn:function(key, e) {
var sm = this.getSelectionModel();
var node = sm.getSelectedNode();
if(node && 0 !== node.getDepth() && this.enableDelete && this.readOnly !== true) {
this.deleteNode(node);
}
}},{
// Ctrl + E = reload
key:69, ctrl:true, stopEvent:true, scope:this
,fn:function(key, e) {
var sm = this.getSelectionModel();
var node = sm.getSelectedNode();
if(node) {
node = node.isLeaf() ? node.parentNode : node;
sm.select(node);
node.reload();
}
}},{
// Ctrl + -> = expand deep
key:39, ctrl:true, stopEvent:true, scope:this
,fn:function(key, e) {
var sm = this.getSelectionModel();
var node = sm.getSelectedNode();
if(node && !node.isLeaf()) {
sm.select(node);
node.expand.defer(1, node, [true]);
}
}},{
// Ctrl + <- = collapse deep
key:37, ctrl:true, scope:this, stopEvent:true
,fn:function(key, e) {
var sm = this.getSelectionModel();
var node = sm.getSelectedNode();
if(node && !node.isLeaf()) {
sm.select(node);
node.collapse.defer(1, node, [true]);
}
}},{
// Ctrl + N = New Directory
key:78, ctrl:true, scope:this, stopEvent:true
,fn:function(key, e) {
var sm, node;
sm = this.getSelectionModel();
node = sm.getSelectedNode();
if(node && this.enableNewDir && this.readOnly !== true) {
node = node.isLeaf() ? node.parentNode : node;
this.createNewDir(node);
}
}}]
// }}}

}); // eo apply
// }}}
// {{{
// create loader
if(!this.loader) {
this.loader = new Ext.tree.TreeLoader({
url:this.url
,baseParams:{cmd:'get'}
,listeners:{
beforeload:{scope:this, fn:function(loader, node) {
loader.baseParams.path = this.getPath(node);
loader.baseParams.cmd = 'get';
}}
}
});
}
// }}}
// {{{
// install top menu if configured
if(true === this.topMenu) {
this.tbar = [{
text:this.fileText
,disabled:true
,scope:this
,menu:this.getContextMenu()
}];
}
// }}}

// call parent
Ext.ux.FileTreePanel.superclass.initComponent.apply(this, arguments);

// {{{
// install treeEditor event handlers
if(this.treeEditor) {
// do not enter edit mode on selected node click
this.treeEditor.beforeNodeClick = function(node,e){return true;};

// treeEditor event handlers
this.treeEditor.on({
complete:{scope:this, fn:this.onEditComplete}
,beforecomplete:{scope:this, fn:this.onBeforeEditComplete}
});
}
// }}}
// {{{
// install event handlers
this.on({
contextmenu:{scope:this, fn:this.onContextMenu, stopEvent:true}
,dblclick:{scope:this, fn:this.onDblClick}
,beforenodedrop:{scope:this, fn:this.onBeforeNodeDrop}
,nodedrop:{scope:this, fn:this.onNodeDrop}
,nodedragover:{scope:this, fn:this.onNodeDragOver}
});

// }}}
// {{{
// add events
this.addEvents(
/**
* @event beforeopen
* Fires before file open. Return false to cancel the event
* @param {Ext.ux.FileTreePanel} this
* @param {String} fileName name of the file being opened
* @param {String} url url of the file being opened
* @param {String} mode open mode
*/
'beforeopen'
/**
* @event open
* Fires after file open has been initiated
* @param {Ext.ux.FileTreePanel} this
* @param {String} fileName name of the file being opened
* @param {String} url url of the file being opened
* @param {String} mode open mode
*/
,'open'
/**
* @event beforerename
* Fires after the user completes file name editing
* but before the file is renamed. Return false to cancel the event
* @param {Ext.ux.FileTreePanel} this
* @param {Ext.tree.AsyncTreeNode} node being renamed
* @param {String} newPath including file name
* @param {String} oldPath including file name
*/
,'beforerename'
/**
* @event rename
* Fires after the file has been successfully renamed
* @param {Ext.ux.FileTreePanel} this
* @param {Ext.tree.AsyncTreeNode} node that has been renamed
* @param {String} newPath including file name
* @param {String} oldPath including file name
*/
,'rename'
/**
* @event renamefailure
* Fires after a failure when renaming file
* @param {Ext.ux.FileTreePanel} this
* @param {Ext.tree.AsyncTreeNode} node rename of which failed
* @param {String} newPath including file name
* @param {String} oldPath including file name
*/
,'renamefailure'
/**
* @event beforedelete
* Fires before a file or directory is deleted. Return false to cancel the event.
* @param {Ext.ux.FileTreePanel} this
* @param {Ext.tree.AsyncTreeNode} node being deleted
*/
,'beforedelete'
/**
* @event delete
* Fires after a file or directory has been deleted
* @param {Ext.ux.FileTreePanel} this
* @param {String} path including file name that has been deleted
*/
,'delete'
/**
* @event deletefailure
* Fires if node delete failed
* @param {Ext.ux.FileTreePanel} this
* @param {Ext.tree.AsyncTreeNode} node delete of which failed
*/
,'deletefailure'
/**
* @event beforenewdir
* Fires before new directory is created. Return false to cancel the event
* @param {Ext.ux.FileTreePanel} this
* @param {Ext.tree.AsyncTreeNode} node under which the new directory is being created
*/
,'beforenewdir'
/**
* @event newdir
* Fires after the new directory has been successfully created
* @param {Ext.ux.FileTreePanel} this
* @param {Ext.tree.AsyncTreeNode} new node/directory that has been created
*/
,'newdir'
/**
* @event newdirfailure
* Fires if creation of new directory failed
* @param {Ext.ux.FileTreePanel} this
* @param {String} path creation of which failed
*/
,'newdirfailure'
); // eo addEvents
// }}}


} // eo function initComponent
// }}}
// {{{
/**
* onRender override - just expands root node if configured
* @private
*/
,onRender:function() {
// call parent
Ext.ux.FileTreePanel.superclass.onRender.apply(this, arguments);

if(true === this.topMenu) {
this.topMenu = Ext.getCmp(this.getTopToolbar().items.itemAt(0).id);
this.getSelectionModel().on({
scope:this
,selectionchange:function(sm, node) {
var disable = node ? false : true;
disable = disable || this.readOnly;
this.topMenu.setDisabled(disable);
}
});
Ext.apply(this.topMenu, {
showMenu:function() {
this.showContextMenu(false);
}.createDelegate(this)
// ,menu:this.getContextMenu()
});
}

// expand root node if so configured
if(this.expandOnRender) {
this.root.expand();
}

// prevent default browser context menu to appear
this.el.on({
contextmenu:{fn:function(){return false;},stopEvent:true}
});

// setup loading mask if configured
if(true === this.loadMask) {
this.loader.on({
scope:this.el
,beforeload:this.el.mask.createDelegate(this.el, [this.loadingText + '...'])
,load:this.el.unmask
,loadexception:this.el.unmask
});
}

} // eo function onRender
// }}}

// new methods
// {{{
/**
* runs after an Ajax requested command has completed/failed
* @private
* @param {Object} options Options used for the request
* @param {Boolean} success true if ajax call was successful (cmd may have failed)
* @param {Object} response ajax call response object
*/
,cmdCallback:function(options, success, response) {
var i, o, node;
var showMsg = true;

// process Ajax success
if(true === success) {

// try to decode JSON response
try {
o = Ext.decode(response.responseText);
}
catch(ex) {
this.showError(response.responseText);
}

// process command success
if(true === o.success) {
switch(options.params.cmd) {
case 'delete':
if(true !== this.eventsSuspended) {
this.fireEvent('delete', this, this.getPath(options.node));
}
options.node.parentNode.removeChild(options.node);
break;

case 'newdir':
if(true !== this.eventsSuspended) {
this.fireEvent('newdir', this, options.node);
}
break;

case 'rename':
this.updateCls(options.node, options.params.oldname);
if(true !== this.eventsSuspended) {
this.fireEvent('rename', this, options.node, options.params.newname, options.params.oldname);
}
break;
}
} // eo process command success
// process command failure
else {
switch(options.params.cmd) {

case 'rename':
// handle drag & drop rename error
if(options.oldParent) {
options.oldParent.appendChild(options.node);
}
// handle simple rename error
else {
options.node.setText(options.oldName);
}
// signal failure to onNodeDrop
if(options.e) {
options.e.failure = true;
}
if(true !== this.eventsSuspended) {
this.fireEvent('renamefailure', this, options.node, options.params.newname, options.params.oldname);
}
break;

case 'newdir':
if(false !== this.eventsSuspended) {
this.fireEvent('newdirfailure', this, options.params.dir);
}
options.node.parentNode.removeChild(options.node);
break;

case 'delete':
if(true !== this.eventsSuspended) {
this.fireEvent('deletefailure', this, options.node);
}
options.node.parentNode.reload.defer(1, options.node.parentNode);
break;

default:
this.root.reload();
break;
}

// show default message box with server error
this.showError(o.error || response.responseText);
} // eo process command failure
} // eo process Ajax success

// process Ajax failure
else {
this.showError(response.responseText);
}
} // eo function cmdCallback
// }}}
// {{{
/**
* displays overwrite confirm msg box and runs passed callback if response is yes
* @private
* @param {String} filename File to overwrite
* @param {Function} callback Function to call on yes response
* @param {Object} scope Scope for callback (defaults to this)
*/
,confirmOverwrite:function(filename, callback, scope) {
Ext.Msg.show({
title:this.confirmText
,msg:String.format(this.existsText, filename) + '. ' + this.overwriteText
,icon:Ext.Msg.QUESTION
,buttons:Ext.Msg.YESNO
,fn:callback.createDelegate(scope || this)
});
}
// }}}
// {{{
/**
* creates new directory (node)
* @private
* @param {Ext.tree.AsyncTreeNode} node
*/
,createNewDir:function(node) {

// fire beforenewdir event
if(true !== this.eventsSuspended && false === this.fireEvent('beforenewdir', this, node)) {
return;
}

var treeEditor = this.treeEditor;
var newNode;

// get node to append the new directory to
var appendNode = node.isLeaf() ? node.parentNode : node;

// create new folder after the appendNode is expanded
appendNode.expand(false, false, function(n) {
// create new node
newNode = n.appendChild(new Ext.tree.AsyncTreeNode({text:this.newdirText, iconCls:'folder'}));

// setup one-shot event handler for editing completed
treeEditor.on({
complete:{
scope:this
,single:true
,fn:this.onNewDir
}}
);


// creating new directory flag
treeEditor.creatingNewDir = true;

// start editing after short delay
(function(){treeEditor.triggerEdit(newNode);}.defer(10));
// expand callback needs to run in this context
}.createDelegate(this));

} // eo function creatingNewDir
// }}}
// {{{
/**
* deletes the passed node
* @private
* @param {Ext.tree.AsyncTreeNode} node
*/
,deleteNode:function(node) {
// fire beforedelete event
if(true !== this.eventsSuspended && false === this.fireEvent('beforedelete', this, node)) {
return;
}

Ext.Msg.show({
title:this.deleteText
,msg:this.reallyWantText + ' ' + this.deleteText.toLowerCase() + ' <b>' + node.text + '</b>?'
,icon:Ext.Msg.WARNING
,buttons:Ext.Msg.YESNO
,scope:this
,fn:function(response) {
// do nothing if answer is not yes
if('yes' !== response) {
this.getEl().dom.focus();
return;
}
// setup request options
var options = {
url:this.deleteUrl || this.url
,method:this.method
,scope:this
,callback:this.cmdCallback
,node:node
,params:{
cmd:'delete'
,file:this.getPath(node)
}
};
Ext.Ajax.request(options);
}
});
} // eo function deleteNode
// }}}
// {{{
/**
* requests file download from server
* @private
* @param {String} path Full path including file name but relative to server root path
*/
,downloadFile:function(path) {

// create hidden target iframe
var id = Ext.id();
var frame = document.createElement('iframe');
frame.id = id;
frame.name = id;
frame.className = 'x-hidden';
if(Ext.isIE) {
frame.src = Ext.SSL_SECURE_URL;
}

document.body.appendChild(frame);

if(Ext.isIE) {
document.frames[id].name = id;
}

var form = Ext.DomHelper.append(document.body, {
tag:'form'
,method:'post'
,action:this.downloadUrl || this.url
,target:id
});

document.body.appendChild(form);

var hidden;

// append cmd to form
hidden = document.createElement('input');
hidden.type = 'hidden';
hidden.name = 'cmd';
hidden.value = 'download';
form.appendChild(hidden);

// append path to form
hidden = document.createElement('input');
hidden.type = 'hidden';
hidden.name = 'path';
hidden.value = path;
form.appendChild(hidden);

var callback = function() {
Ext.EventManager.removeListener(frame, 'load', callback, this);
setTimeout(function() {document.body.removeChild(form);}, 100);
setTimeout(function() {document.body.removeChild(frame);}, 110);
};

Ext.EventManager.on(frame, 'load', callback, this);

form.submit();
}
// }}}
// {{{
/**
* returns (and lazy create) the context menu
* @private
*/
,getContextMenu:function() {
// lazy create context menu
if(!this.contextmenu) {
var config = {
singleUpload:this.singleUpload
,maxFileSize:this.maxFileSize
,enableProgress:this.enableProgress
};
if(this.baseParams) {
config.baseParams = this.baseParams;
}
this.contextmenu = new Ext.ux.FileTreeMenu(config);
this.contextmenu.on({click:{scope:this, fn:this.onContextClick}});

this.uploadPanel = this.contextmenu.getItemByCmd('upload-panel').component;
this.uploadPanel.on({
beforeupload:{scope:this, fn:this.onBeforeUpload}
,allfinished:{scope:this, fn:this.onAllFinished}
});
this.uploadPanel.setUrl(this.uploadUrl || this.url);

// relay view events
this.relayEvents(this.uploadPanel, [
'beforefileadd'
,'fileadd'
,'beforefileremove'
,'fileremove'
,'beforequeueclear'
,'queueclear'
,'beforeupload'
,'beforeallstart'
,'allfinished'
,'progress'
]);
}
return this.contextmenu;
} // eo function getContextMenu
// }}}
// {{{
/**
* returns file class based on name extension
* @private
* @param {String} name File name to get class of
*/
,getFileCls:function(name) {
var atmp = name.split('.');
if(1 === atmp.length) {
return this.fileCls;
}
else {
return this.fileCls + '-' + atmp.pop().toLowerCase();
}
}
// }}}
// {{{
/**
* returns path of node (file/directory)
* @private
*/
,getPath:function(node) {
var path, p, a;

// get path for non-root node
if(node !== this.root) {
p = node.parentNode;
a = [node.text];
while(p && p !== this.root) {
a.unshift(p.text);
p = p.parentNode;
}
a.unshift(this.root.attributes.path || '');
path = a.join(this.pathSeparator);
}

// path for root node is it's path attribute
else {
path = node.attributes.path || '';
}

// a little bit of security: strip leading / or .
// full path security checking has to be implemented on server
path = path.replace(/^[\/\.]*/, '');
return path;
} // eo function getPath
// }}}
// {{{
/**
* returns true if node has child with the specified name (text)
* @private
* @param {Ext.data.Node} node
* @param {String} childName
*/
,hasChild:function(node, childName) {
return (node.isLeaf() ? node.parentNode : node).findChild('text', childName) !== null;
}
// }}}
// {{{
/**
* Hides context menu
* @return {Ext.ux.FileTreeMenu} this
*/
,hideContextMenu:function() {
if(this.contextmenu && this.contextmenu.isVisible()) {
this.contextmenu.hide();
}
return this;
} // eo function hideContextMenu
// }}}
// {{{
/**
* called before editing is completed - allows edit cancellation
* @private
* @param {TreeEditor} editor
* @param {String} newName
* @param {String} oldName
*/
,onBeforeEditComplete:function(editor, newName, oldName) {
if(editor.cancellingEdit) {
editor.cancellingEdit = false;
return;
}
var oldPath = this.getPath(editor.editNode);
var newPath = oldPath.replace(/\/[^\\]+$/, '/' + newName);


if(false === this.fireEvent('beforerename', this, editor.editNode, newPath, oldPath)) {
editor.cancellingEdit = true;
editor.cancelEdit();
return false;
}
}
// }}}
// {{{
/**
* runs before node is dropped
* @private
* @param {Object} e dropEvent object
*/
,onBeforeNodeDrop:function(e) {

// source node, node being dragged
var s = e.dropNode;

// destination node (dropping on this node)
var d = e.target.leaf ? e.target.parentNode : e.target;

// node has been dropped within the same parent
if(s.parentNode === d) {
return false;
}

// check if same name exists in the destination
// this works only if destination node is loaded
if(this.hasChild(d, s.text) && undefined === e.confirmed) {
this.confirmOverwrite(s.text, function(response) {
e.confirmed = 'yes' === response;
this.onBeforeNodeDrop(e);
});
return false;
}
if(false === e.confirmed) {
return false;
}

e.confirmed = undefined;
e.oldParent = s.parentNode;

var oldName = this.getPath(s);
var newName = this.getPath(d) + '/' + s.text;

// fire beforerename event
if(true !== this.eventsSuspended && false === this.fireEvent('beforerename', this, s, newName, oldName)) {
return false;
}

var options = {
url:this.renameUrl || this.url
,method:this.method
,scope:this
,callback:this.cmdCallback
,node:s
,oldParent:s.parentNode
,e:e
,params:{
cmd:'rename'
,oldname:oldName
,newname:newName
}
};
Ext.Ajax.request(options);
return true;
}
// }}}
// {{{
/**
* sets uploadPanel's destination path
* @private
*/
,onBeforeUpload:function(uploadPanel) {

var menu = this.getContextMenu();
var path = this.getPath(menu.node);
if(menu.node.isLeaf()) {
path = path.replace(/\/[^\/]+$/, '', path);
}
uploadPanel.setPath(path);

} // eo function onBeforeUpload
// }}}
// {{{
/**
* reloads tree node on upload finish
* @private
*/
,onAllFinished:function(uploader) {
var menu = this.getContextMenu();
(menu.node.isLeaf() ? menu.node.parentNode : menu.node).reload();
} // eo function onAllFinished
// }}}
// {{{
/**
* @private
* context menu click handler
* @param {Ext.menu.Menu} context menu
* @param {Ext.menu.Item} item clicked
* @param {Ext.EventObject} raw event
*/
,onContextClick:function(menu, item, e) {
if(item.disabled) {
return;
}
var node = menu.node;
if(!node) {
node = menu.parentMenu.node;
}
switch(item.cmd) {
case 'reload':
node.reload();
break;

case 'expand':
node.expand(true);
break;

case 'collapse':
node.collapse(true);
break;

case 'open':
this.openNode(node);
break;

case 'open-self':
this.openNode(node, '_self');
break;

case 'open-popup':
this.openNode(node, 'popup');
break;

case 'open-blank':
this.openNode(node, '_blank');
break;

case 'open-dwnld':
this.openNode(node, 'download');
break;

case 'rename':
this.treeEditor.triggerEdit(node);
break;

case 'delete':
this.deleteNode(node);
break;

case 'newdir':
this.createNewDir(node);
break;

default:
break;
}
} // eo function onContextClick
// }}}
// {{{
/**
* contextmenu event handler
* @private
*/
,onContextMenu:function(node, e) {
if(this.readOnly) {
return false;
}
this.showContextMenu(node);

return false;
} // eo function onContextMenu
// }}}
// {{{
/**
* dblclick handlers
* @private
*/
,onDblClick:function(node, e) {
this.openNode(node);
} // eo function onDblClick
// }}}
// {{{
/**
* Destroys the FileTreePanel and sub-components
* @private
*/
,onDestroy:function() {

// destroy contextmenu
if(this.contextmenu) {
this.contextmenu.purgeListeners();
this.contextmenu.destroy();
this.contextmenu = null;
}

// destroy treeEditor
if(this.treeEditor) {
this.treeEditor.purgeListeners();
this.treeEditor.destroy();
this.treeEditor = null;
}


// remover reference to treeSorter
if(this.treeSorter) {
this.treeSorter = null;
}

// call parent
Ext.ux.FileTreePanel.superclass.onDestroy.call(this);

} // eo function onDestroy
// }}}
// {{{
/**
* runs when editing of a node (rename) is completed
* @private
* @param {Ext.Editor} editor
* @param {String} newName
* @param {String} oldName
*/
,onEditComplete:function(editor, newName, oldName) {

var node = editor.editNode;

if(newName === oldName || editor.creatingNewDir) {
editor.creatingNewDir = false;
return;
}
var path = this.getPath(node.parentNode);
var options = {
url:this.renameUrl || this.url
,method:this.method
,scope:this
,callback:this.cmdCallback
,node:node
,oldName:oldName
,params:{
cmd:'rename'
,oldname:path + '/' + oldName
,newname:path + '/' + newName
}
};
Ext.Ajax.request(options);
}
// }}}
// {{{
/**
* create new directory handler
* @private
* runs after editing of new directory name is completed
* @param {Ext.Editor} editor
*/
,onNewDir:function(editor) {
var path = this.getPath(editor.editNode);
var options = {
url:this.newdirUrl || this.url
,method:this.method
,scope:this
,node:editor.editNode
,callback:this.cmdCallback
,params:{
cmd:'newdir'
,dir:path
}
};
Ext.Ajax.request(options);
}
// }}}
// {{{
/**
* called while dragging over, decides if drop is allowed
* @private
* @param {Object} dd event
*/
,onNodeDragOver:function(e) {
e.cancel = e.target.disabled || e.dropNode.parentNode === e.target.parentNode && e.target.isLeaf();
} // eo function onNodeDragOver
// }}}
// {{{
/**
* called when node is dropped
* @private
* @param {Object} dd event
*/
,onNodeDrop:function(e) {

// failure can be signalled by cmdCallback
// put drop node to the original parent in that case
if(true === e.failure) {
e.oldParent.appendChild(e.dropNode);
return;
}

// if we already have node with the same text, remove the duplicate
var sameNode = e.dropNode.parentNode.findChild('text', e.dropNode.text);
if(sameNode && sameNode !== e.dropNode) {
sameNode.parentNode.removeChild(sameNode);
}
}
// }}}
// {{{
/**
* Opens node
* @param {Ext.tree.AsyncTreeNode} node
* @param {String} mode Can be "_self", "_blank", or "popup". Defaults to (this.openMode)
*/
,openNode:function(node, mode) {

if(!this.enableOpen) {
return;
}

mode = mode || this.openMode;

var url;
var path;
var newpath;
if(node.isLeaf()) {
path = this.getPath(node);

newpath = this.opennodepath(this, node, path, mode);
if(newpath !== undefined && newpath != null && newpath.length > 0){
path = newpath;
}

url = this.hrefPrefix + path + this.hrefSuffix;

// fire beforeopen event
if(true !== this.eventsSuspended && false === this.fireEvent('beforeopen', this, node.text, url, mode)) {
return;
}

switch(mode) {
case 'popup':
if(!this.popup || this.popup.closed) {
this.popup = window.open(url, this.hrefTarget, this.popupFeatures);
}
this.popup.location = url;
if(this.focusPopup) {
this.popup.focus();
}
break;

case '_self':
window.location = url;
break;

case '_blank':
window.open(url);
break;

case 'download':
this.downloadFile(path);
break;
}

// fire open event
if(true !== this.eventsSuspended) {
this.fireEvent('open', this, node.text, url, mode);
}
}

}
// }}}
// {{{
/**
* Sets/Unsets delete of files/directories disabled/enabled
* @param {Boolean} disabled
* @return {Ext.ux.FileTreePanel} this
*/
,setDeleteDisabled:function(disabled) {
disabled = !(!disabled);
if(!this.enableDelete === disabled) {
return this;
}
this.hideContextMenu();
this.enableDelete = !disabled;
} // eo function setDeleteDisabled
// }}}
// {{{
/**
* Sets/Unsets creation of new directory disabled/enabled
* @param {Boolean} disabled
* @return {Ext.ux.FileTreePanel} this
*/
,setNewdirDisabled:function(disabled) {
disabled = !(!disabled);
if(!this.enableNewDir === disabled) {
return this;
}
this.hideContextMenu();
this.enableNewDir = !disabled;

} // eo function setNewdirDisabled
// }}}
// {{{
/**
* Sets/Unsets open files disabled/enabled
* @param {Boolean} disabled
* @return {Ext.ux.FileTreePanel} this
*/
,setOpenDisabled:function(disabled) {
disabled = !(!disabled);
if(!this.enableOpen === disabled) {
return this;
}
this.hideContextMenu();
this.enableOpen = !disabled;


return this;
} // eo function setOpenDisabled
// }}}
// {{{
/**
* Sets/Unsets this tree to/from readOnly state
* @param {Boolean} readOnly
* @return {Ext.ux.FileTreePanel} this
*/
,setReadOnly:function(readOnly) {
readOnly = !(!readOnly);
if(this.readOnly === readOnly) {
return this;
}
this.hideContextMenu();
if(this.dragZone) {
this.dragZone.locked = readOnly;
}
this.readOnly = readOnly;

return this;

} // eo function setReadOnly
// }}}
// {{{
/**
* Sets/Unsets rename of files/directories disabled/enabled
* @param {Boolean} disabled
* @return {Ext.ux.FileTreePanel} this
*/
,setRenameDisabled:function(disabled) {
disabled = !(!disabled);
if(!this.enableRename === disabled) {
return this;
}
this.hideContextMenu();
if(this.dragZone) {
this.dragZone.locked = disabled;
}
this.enableRename = !disabled;

return this;
} // eo function setRenameDisabled
// }}}
// {{{
/**
* Sets/Unsets uploading of files disabled/enabled
* @param {Boolean} disabled
* @return {Ext.ux.FileTreePanel} this
*/
,setUploadDisabled:function(disabled) {
disabled = !(!disabled);
if(!this.enableUpload === disabled) {
return this;
}
this.hideContextMenu();
this.enableUpload = !disabled;

return this;
} // of function setUploadDisabled
// }}}
// {{{
/**
* adjusts context menu depending on many things and shows it
* @private
* @param {Ext.tree.AsyncTreeNode} node Node on which was right-clicked
*/
,showContextMenu:function(node) {

// setup node alignment
var topAlign = false;
var alignEl = this.topMenu ? this.topMenu.getEl() : this.body;

if(!node) {
node = this.getSelectionModel().getSelectedNode();
topAlign = true;
}
else {
alignEl = node.getUI().getEl();
}
if(!node) {
return;
}

var menu = this.getContextMenu();
menu.node = node;

// set node name
menu.getItemByCmd('nodename').setText(Ext.util.Format.ellipsis(node.text, 22));

// enable/disable items depending on node clicked
menu.setItemDisabled('open', !node.isLeaf());
menu.setItemDisabled('reload', node.isLeaf());
menu.setItemDisabled('expand', node.isLeaf());
menu.setItemDisabled('collapse', node.isLeaf());
menu.setItemDisabled('delete', node === this.root || node.disabled);
menu.setItemDisabled('rename', this.readOnly || node === this.root || node.disabled);
menu.setItemDisabled('newdir', this.readOnly || (node.isLeaf() ? node.parentNode.disabled : node.disabled));
menu.setItemDisabled('upload', node.isLeaf() ? node.parentNode.disabled : node.disabled);
menu.setItemDisabled('upload-panel', node.isLeaf() ? node.parentNode.disabled : node.disabled);

// show/hide logic
menu.getItemByCmd('open').setVisible(this.enableOpen);
menu.getItemByCmd('delete').setVisible(this.enableDelete);
menu.getItemByCmd('newdir').setVisible(this.enableNewDir);
menu.getItemByCmd('rename').setVisible(this.enableRename);
menu.getItemByCmd('upload').setVisible(this.enableUpload);
menu.getItemByCmd('upload-panel').setVisible(this.enableUpload);
menu.getItemByCmd('sep-upload').setVisible(this.enableUpload);
menu.getItemByCmd('sep-collapse').setVisible(this.enableNewDir || this.enableDelete || this.enableRename);

// select node
node.select();

// show menu
if(topAlign) {
menu.showAt(menu.getEl().getAlignToXY(alignEl, 'tl-bl?'));
}
else {
menu.showAt(menu.getEl().getAlignToXY(alignEl, 'tl-tl?', [0, 18]));
}
} // eo function
// }}}
// {{{
/**
* universal show error function
* @private
* @param {String} msg message
* @param {String} title title
*/
,showError:function(msg, title) {
Ext.Msg.show({
title:title || this.errorText
,msg:Ext.util.Format.ellipsis(msg, this.maxMsgLen)
,fixCursor:true
,icon:Ext.Msg.ERROR
,buttons:Ext.Msg.OK
,minWidth:1200 > String(msg).length ? 360 : 600
});
} // eo function showError
// }}}
// {{{
/**
* updates class of leaf after rename
* @private
* @param {Ext.tree.AsyncTreeNode} node Node to update class of
* @param {String} oldName Name the node had before
*/
,updateCls:function(node, oldName) {
if(node.isLeaf()) {
Ext.fly(node.getUI().iconNode).removeClass(this.getFileCls(oldName));
Ext.fly(node.getUI().iconNode).addClass(this.getFileCls(node.text));
}
}
// }}}

}); // eo extend

// register xtype
Ext.reg('filetreepanel', Ext.ux.FileTreePanel);

// eof

// vim: ts=4:sw=4:nu:fdc=4:nospell
/**
* Ext.ux.FileUploader
*
* @author Ing. Jozef Sakáloš
* @version $Id: Ext.ux.FileUploader.js 83 2008-03-21 12:54:35Z jozo $
* @date 15. March 2008
*
* @license Ext.ux.FileUploader is licensed under the terms of
* the Open Source LGPL 3.0 license. Commercial use is permitted to the extent
* that the code/component(s) do NOT become part of another Open Source or Commercially
* licensed development library or toolkit without explicit permission.
*
* License details: http://www.gnu.org/licenses/lgpl.html
*/

/*global Ext */

/**
* @class Ext.ux.FileUploader
* @extends Ext.util.Observable
* @constructor
*/
Ext.ux.FileUploader = function(config) {
Ext.apply(this, config);

// call parent
Ext.ux.FileUploader.superclass.constructor.apply(this, arguments);

// add events
// {{{
this.addEvents(
/**
* @event beforeallstart
* Fires before an upload (of all files) is started. Return false to cancel the event.
* @param {Ext.ux.FileUploader} this
*/
'beforeallstart'
/**
* @event allfinished
* Fires after upload (of all files) is finished
* @param {Ext.ux.FileUploader} this
*/
,'allfinished'
/**
* @event beforefilestart
* Fires before the file upload is started. Return false to cancel the event.
* Fires only when singleUpload = false
* @param {Ext.ux.FileUploader} this
* @param {Ext.data.Record} record upload of which is being started
*/
,'beforefilestart'
/**
* @event filefinished
* Fires when file finished uploading.
* Fires only when singleUpload = false
* @param {Ext.ux.FileUploader} this
* @param {Ext.data.Record} record upload of which has finished
*/
,'filefinished'
/**
* @event progress
* Fires when progress has been updated
* @param {Ext.ux.FileUploader} this
* @param {Object} data Progress data object
* @param {Ext.data.Record} record Only if singleUpload = false
*/
,'progress'
);
// }}}


}; // eo constructor

Ext.extend(Ext.ux.FileUploader, Ext.util.Observable, {

// configuration options
// {{{
/**
* @cfg {Object} baseParams baseParams are sent to server in each request.
*/
baseParams:{cmd:'upload',dir:'.'}

/**
* @cfg {Boolean} concurrent true to start all requests upon upload start, false to start
* the next request only if previous one has been completed (or failed). Applicable only if
* singleUpload = false
*/
,concurrent:true

/**
* @cfg {Boolean} enableProgress true to enable querying server for progress information
*/
,enableProgress:true

/**
* @cfg {String} jsonErrorText Text to use for json error
*/
,jsonErrorText:'Cannot decode JSON object'

/**
* @cfg {Number} Maximum client file size in bytes
*/
,maxFileSize:524288

/**
* @cfg {String} progressIdName Name to give hidden field for upload progress identificator
*/
,progressIdName:'UPLOAD_IDENTIFIER'

/**
* @cfg {Number} progressInterval How often (in ms) is progress requested from server
*/
,progressInterval:2000

/**
* @cfg {String} progressUrl URL to request upload progress from
*/
,progressUrl:'progress.php'

/**
* @cfg {Object} progressMap Mapping of received progress fields to store progress fields
*/
,progressMap:{
bytes_total:'bytesTotal'
,bytes_uploaded:'bytesUploaded'
,est_sec:'estSec'
,files_uploaded:'filesUploaded'
,speed_average:'speedAverage'
,speed_last:'speedLast'
,time_last:'timeLast'
,time_start:'timeStart'
}
/**
* @cfg {Boolean} singleUpload true to upload files in one form, false to upload one by one
*/
,singleUpload:false

/**
* @cfg {Ext.data.Store} store Mandatory. Store that holds files to upload
*/

/**
* @cfg {String} unknownErrorText Text to use for unknow error
*/
,unknownErrorText:'Unknown error'

/**
* @cfg {String} url Mandatory. URL to upload to
*/

// }}}

// private
// {{{
/**
* uploads in progress count
* @private
*/
,upCount:0
// }}}

// methods
// {{{
/**
* creates form to use for upload.
* @private
* @return {Ext.Element} form
*/
,createForm:function(record) {
var progressId = parseInt(Math.random() * 1e10, 10);
var form = Ext.getBody().createChild({
tag:'form'
,action:this.url
,method:'post'
,cls:'x-hidden'
,id:Ext.id()
,cn:[{
tag:'input'
,type:'hidden'
,name:'APC_UPLOAD_PROGRESS'
,value:progressId
},{
tag:'input'
,type:'hidden'
,name:this.progressIdName
,value:progressId
},{
tag:'input'
,type:'hidden'
,name:'MAX_FILE_SIZE'
,value:this.maxFileSize
}]
});
if(record) {
record.set('form', form);
record.set('progressId', progressId);
}
else {
this.progressId = progressId;
}
return form;

} // eo function createForm
// }}}
// {{{
,deleteForm:function(form, record) {
form.remove();
if(record) {
record.set('form', null);
}
} // eo function deleteForm
// }}}
// {{{
/**
* Fires event(s) on upload finish/error
* @private
*/
,fireFinishEvents:function(options) {
if(true !== this.eventsSuspended && !this.singleUpload) {
this.fireEvent('filefinished', this, options && options.record);
}
if(true !== this.eventsSuspended && 0 === this.upCount) {
this.stopProgress();
this.fireEvent('allfinished', this);
}
} // eo function fireFinishEvents
// }}}
// {{{
/**
* Geg the iframe identified by record
* @private
* @param {Ext.data.Record} record
* @return {Ext.Element} iframe or null if not found
*/
,getIframe:function(record) {
var iframe = null;
var form = record.get('form');
if(form && form.dom && form.dom.target) {
iframe = Ext.get(form.dom.target);
}
return iframe;
} // eo function getIframe
// }}}
// {{{
/**
* returns options for Ajax upload request
* @private
* @param {Ext.data.Record} record
* @param {Object} params params to add
*/
,getOptions:function(record, params) {
var o = {
url:this.url
,method:'post'
,isUpload:true
,scope:this
,callback:this.uploadCallback
,record:record
,params:this.getParams(record, params)
};
return o;
} // eo function getOptions
// }}}
// {{{
/**
* get params to use for request
* @private
* @return {Object} params
*/
,getParams:function(record, params) {
var p = {path:this.path};
Ext.apply(p, this.baseParams || {}, params || {});
return p;
}
// }}}
// {{{
/**
* processes success response
* @private
* @param {Object} options options the request was called with
* @param {Object} response request response object
* @param {Object} o decoded response.responseText
*/
,processSuccess:function(options, response, o) {
var record = false;


// all files uploadded ok
if(this.singleUpload) {
this.store.each(function(r) {
r.set('state', 'done');
r.set('error', '');
r.commit();
});
}
else {
record = options.record;
record.set('state', 'done');
record.set('error', '');
record.commit();
}

this.deleteForm(options.form, record);

} // eo processSuccess
// }}}
// {{{
/**
* processes failure response
* @private
* @param {Object} options options the request was called with
* @param {Object} response request response object
* @param {String/Object} error Error text or JSON decoded object. Optional.
*/
,processFailure:function(options, response, error) {
var record = options.record;
var records;

// singleUpload - all files uploaded in one form
if(this.singleUpload) {
// some files may have been successful
records = this.store.queryBy(function(r){return 'done' !== r.get('state');});
records.each(function(record) {
var e = error.errors ? error.errors[record.id] : this.unknownErrorText;
if(e) {
record.set('state', 'failed');
record.set('error', e);
Ext.getBody().appendChild(record.get('input'));
}
else {
record.set('state', 'done');
record.set('error', '');
}
record.commit();
}, this);

this.deleteForm(options.form);
}
// multipleUpload - each file uploaded in it's own form
else {
if(error && 'object' === Ext.type(error)) {
record.set('error', error.errors && error.errors[record.id] ? error.errors[record.id] : this.unknownErrorText);
}
else if(error) {
record.set('error', error);
}
else if(response && response.responseText) {
record.set('error', response.responseText);
}
else {
record.set('error', this.unknownErrorText);
}
record.set('state', 'failed');
record.commit();
}
} // eof processFailure
// }}}
// {{{
/**
* Delayed task callback
*/
,requestProgress:function() {
var records, p;
var o = {
url:this.progressUrl
,method:'post'
,params:{}
,scope:this
,callback:function(options, success, response) {
var o;
if(true !== success) {
return;
}
try {
o = Ext.decode(response.responseText);
}
catch(e) {
return;
}
if('object' !== Ext.type(o) || true !== o.success) {
return;
}

if(this.singleUpload) {
this.progress = {};
for(p in o) {
if(this.progressMap[p]) {
this.progress[this.progressMap[p]] = parseInt(o[p], 10);
}
}
if(true !== this.eventsSuspended) {
this.fireEvent('progress', this, this.progress);
}

}
else {
for(p in o) {
if(this.progressMap[p] && options.record) {
options.record.set(this.progressMap[p], parseInt(o[p], 10));
}
}
if(options.record) {
options.record.commit();
if(true !== this.eventsSuspended) {
this.fireEvent('progress', this, options.record.data, options.record);
}
}
}
this.progressTask.delay(this.progressInterval);
}
};
if(this.singleUpload) {
o.params[this.progressIdName] = this.progressId;
o.params.APC_UPLOAD_PROGRESS = this.progressId;
Ext.Ajax.request(o);
}
else {
records = this.store.query('state', 'uploading');
records.each(function(r) {
o.params[this.progressIdName] = r.get('progressId');
o.params.APC_UPLOAD_PROGRESS = o.params[this.progressIdName];
o.record = r;
(function() {
Ext.Ajax.request(o);
}).defer(250);
}, this);
}
} // eo function requestProgress
// }}}
// {{{
/**
* path setter
* @private
*/
,setPath:function(path) {
this.path = path;
} // eo setPath
// }}}
// {{{
/**
* url setter
* @private
*/
,setUrl:function(url) {
this.url = url;
} // eo setUrl
// }}}
// {{{
/**
* Starts progress fetching from server
* @private
*/
,startProgress:function() {
if(!this.progressTask) {
this.progressTask = new Ext.util.DelayedTask(this.requestProgress, this);
}
this.progressTask.delay.defer(this.progressInterval / 2, this.progressTask, [this.progressInterval]);
} // eo function startProgress
// }}}
// {{{
/**
* Stops progress fetching from server
* @private
*/
,stopProgress:function() {
if(this.progressTask) {
this.progressTask.cancel();
}
} // eo function stopProgress
// }}}
// {{{
/**
* Stops all currently running uploads
*/
,stopAll:function() {
var records = this.store.query('state', 'uploading');
records.each(this.stopUpload, this);
} // eo function stopAll
// }}}
// {{{
/**
* Stops currently running upload
* @param {Ext.data.Record} record Optional, if not set singleUpload = true is assumed
* and the global stop is initiated
*/
,stopUpload:function(record) {
// single abord
var iframe = false;
if(record) {
iframe = this.getIframe(record);
this.stopIframe(iframe);
this.upCount--;
this.upCount = 0 > this.upCount ? 0 : this.upCount;
record.set('state', 'stopped');
this.fireFinishEvents({record:record});
}
// all abort
else if(this.form) {
iframe = Ext.fly(this.form.dom.target);
this.stopIframe(iframe);
this.upCount = 0;
this.fireFinishEvents();
}


} // eo function abortUpload
// }}}
// {{{
/**
* Stops uploading in hidden iframe
* @private
* @param {Ext.Element} iframe
*/
,stopIframe:function(iframe) {
if(iframe) {
try {
iframe.dom.contentWindow.stop();
iframe.remove.defer(250, iframe);
}
catch(e){}
}
} // eo function stopIframe
// }}}
// {{{
/**
* Main public interface function. Preforms the upload
*/
,upload:function() {

var records = this.store.queryBy(function(r){return 'done' !== r.get('state');});
if(!records.getCount()) {
return;
}

// fire beforeallstart event
if(true !== this.eventsSuspended && false === this.fireEvent('beforeallstart', this)) {
return;
}
if(this.singleUpload) {
this.uploadSingle();
}
else {
records.each(this.uploadFile, this);
}

if(true === this.enableProgress) {
this.startProgress();
}

} // eo function upload
// }}}
// {{{
/**
* called for both success and failure. Does nearly nothing
* @private
* but dispatches processing to processSuccess and processFailure functions
*/
,uploadCallback:function(options, success, response) {

var o;
this.upCount--;
this.form = false;

// process ajax success
if(true === success) {
try {
o = Ext.decode(response.responseText);
}
catch(e) {
this.processFailure(options, response, this.jsonErrorText);
this.fireFinishEvents(options);
return;
}
// process command success
if(true === o.success) {
this.processSuccess(options, response, o);
}
// process command failure
else {
this.processFailure(options, response, o);
}
}
// process ajax failure
else {
this.processFailure(options, response);
}

this.fireFinishEvents(options);

} // eo function uploadCallback
// }}}
// {{{
/**
* Uploads one file
* @param {Ext.data.Record} record
* @param {Object} params Optional. Additional params to use in request.
*/
,uploadFile:function(record, params) {
// fire beforestart event
if(true !== this.eventsSuspended && false === this.fireEvent('beforefilestart', this, record)) {
return;
}

// create form for upload
var form = this.createForm(record);

// append input to the form
var inp = record.get('input');
inp.set({name:inp.id});
form.appendChild(inp);

// get params for request
var o = this.getOptions(record, params);
o.form = form;

// set state
record.set('state', 'uploading');
record.set('pctComplete', 0);

// increment active uploads count
this.upCount++;

// request upload
Ext.Ajax.request(o);

// todo:delete after devel
this.getIframe.defer(100, this, [record]);

} // eo function uploadFile
// }}}
// {{{
/**
* Uploads all files in single request
*/
,uploadSingle:function() {

// get records to upload
var records = this.store.queryBy(function(r){return 'done' !== r.get('state');});
if(!records.getCount()) {
return;
}

// create form and append inputs to it
var form = this.createForm();
records.each(function(record) {
var inp = record.get('input');
inp.set({name:inp.id});
form.appendChild(inp);
record.set('state', 'uploading');
}, this);

// create options for request
var o = this.getOptions();
o.form = form;

// save form for stop
this.form = form;

// increment active uploads counter
this.upCount++;

// request upload
Ext.Ajax.request(o);

} // eo function uploadSingle
// }}}

}); // eo extend

// register xtype
Ext.reg('fileuploader', Ext.ux.FileUploader);

// eof

Ext.namespace('Ext.ux.form');

/**
* @class Ext.ux.form.BrowseButton
* @extends Ext.Button
* Ext.Button that provides a customizable file browse button.
* Clicking this button, pops up a file dialog box for a user to select the file to upload.
* This is accomplished by having a transparent <input type="file"> box above the Ext.Button.
* When a user thinks he or she is clicking the Ext.Button, they're actually clicking the hidden input "Browse..." box.
* Note: this class can be instantiated explicitly or with xtypes anywhere a regular Ext.Button can be except in 2 scenarios:
* - Panel.addButton method both as an instantiated object or as an xtype config object.
* - Panel.buttons config object as an xtype config object.
* These scenarios fail because Ext explicitly creates an Ext.Button in these cases.
* Browser compatibility:
* Internet Explorer 6:
* - no issues
* Internet Explorer 7:
* - no issues
* Firefox 2 - Windows:
* - pointer cursor doesn't display when hovering over the button.
* Safari 3 - Windows:
* - no issues.
* @author loeppky - based on the work done by MaximGB in Ext.ux.UploadDialog (http://extjs.com/forum/showthread.php?t=21558)
* The follow the curosr float div idea also came from MaximGB.
* @see http://extjs.com/forum/showthread.php?t=29032
* @constructor
* Create a new BrowseButton.
* @param {Object} config Configuration options
*/
Ext.ux.form.BrowseButton = Ext.extend(Ext.Button, {
/*
* Config options:
*/
/**
* @cfg {String} inputFileName
* Name to use for the hidden input file DOM element. Deaults to "file".
*/
inputFileName: 'file',
/**
* @cfg {Boolean} debug
* Toggle for turning on debug mode.
* Debug mode doesn't make clipEl transparent so that one can see how effectively it covers the Ext.Button.
* In addition, clipEl is given a green background and floatEl a red background to see how well they are positioned.
*/
debug: false,


/*
* Private constants:
*/
/**
* @property FLOAT_EL_WIDTH
* @type Number
* The width (in pixels) of floatEl.
* It should be less than the width of the IE "Browse" button's width (65 pixels), since IE doesn't let you resize it.
* We define this width so we can quickly center floatEl at the mouse cursor without having to make any function calls.
* @private
*/
FLOAT_EL_WIDTH: 60,

/**
* @property FLOAT_EL_HEIGHT
* @type Number
* The heigh (in pixels) of floatEl.
* It should be less than the height of the "Browse" button's height.
* We define this height so we can quickly center floatEl at the mouse cursor without having to make any function calls.
* @private
*/
FLOAT_EL_HEIGHT: 18,


/*
* Private properties:
*/
/**
* @property buttonCt
* @type Ext.Element
* Element that contains the actual Button DOM element.
* We store a reference to it, so we can easily grab its size for sizing the clipEl.
* @private
*/
buttonCt: null,
/**
* @property clipEl
* @type Ext.Element
* Element that contains the floatEl.
* This element is positioned to fill the area of Ext.Button and has overflow turned off.
* This keeps floadEl tight to the Ext.Button, and prevents it from masking surrounding elements.
* @private
*/
clipEl: null,
/**
* @property floatEl
* @type Ext.Element
* Element that contains the inputFileEl.
* This element is size to be less than or equal to the size of the input file "Browse" button.
* It is then positioned wherever the user moves the cursor, so that their click always clicks the input file "Browse" button.
* Overflow is turned off to preven inputFileEl from masking surrounding elements.
* @private
*/
floatEl: null,
/**
* @property inputFileEl
* @type Ext.Element
* Element for the hiden file input.
* @private
*/
inputFileEl: null,
/**
* @property originalHandler
* @type Function
* The handler originally defined for the Ext.Button during construction using the "handler" config option.
* We need to null out the "handler" property so that it is only called when a file is selected.
* @private
*/
originalHandler: null,
/**
* @property originalScope
* @type Object
* The scope originally defined for the Ext.Button during construction using the "scope" config option.
* While the "scope" property doesn't need to be nulled, to be consistent with originalHandler, we do.
* @private
*/
originalScope: null,


/*
* Protected Ext.Button overrides
*/
/**
* @see Ext.Button.initComponent
*/
initComponent: function(){
Ext.ux.form.BrowseButton.superclass.initComponent.call(this);
// Store references to the original handler and scope before nulling them.
// This is done so that this class can control when the handler is called.
// There are some cases where the hidden file input browse button doesn't completely cover the Ext.Button.
// The handler shouldn't be called in these cases. It should only be called if a new file is selected on the file system.
this.originalHandler = this.handler || null;
this.originalScope = this.scope || window;
this.handler = null;
this.scope = null;
},

/**
* @see Ext.Button.onRender
*/
onRender: function(ct, position){
Ext.ux.form.BrowseButton.superclass.onRender.call(this, ct, position); // render the Ext.Button
this.buttonCt = this.el.child('.x-btn-center em');
this.buttonCt.position('relative'); // this is important!
var styleCfg = {
position: 'absolute',
overflow: 'hidden',
top: '0px', // default
left: '0px' // default
};
// browser specifics for better overlay tightness
if (Ext.isIE) {
Ext.apply(styleCfg, {
left: '-3px',
top: '-3px'
});
} else if (Ext.isGecko) {
Ext.apply(styleCfg, {
left: '-3px',
top: '-3px'
});
} else if (Ext.isSafari) {
Ext.apply(styleCfg, {
left: '-4px',
top: '-2px'
});
}
this.clipEl = this.buttonCt.createChild({
tag: 'div',
style: styleCfg
});
this.setClipSize();
this.clipEl.on({
'mousemove': this.onButtonMouseMove,
'mouseover': this.onButtonMouseMove,
scope: this
});

this.floatEl = this.clipEl.createChild({
tag: 'div',
style: {
position: 'absolute',
width: this.FLOAT_EL_WIDTH + 'px',
height: this.FLOAT_EL_HEIGHT + 'px',
overflow: 'hidden'
}
});


if (this.debug) {
this.clipEl.applyStyles({
'background-color': 'green'
});
this.floatEl.applyStyles({
'background-color': 'red'
});
} else {
this.clipEl.setOpacity(0.0);
}

this.createInputFile();
},


/*
* Private helper methods:
*/
/**
* Sets the size of clipEl so that is covering as much of the button as possible.
* @private
*/
setClipSize: function(){
if (this.clipEl) {
var width = this.buttonCt.getWidth();
var height = this.buttonCt.getHeight();
if (Ext.isIE) {
width = width + 5;
height = height + 5;
} else if (Ext.isGecko) {
width = width + 6;
height = height + 6;
} else if (Ext.isSafari) {
width = width + 6;

height = height + 6;
}
this.clipEl.setSize(width, height);
}
},

/**
* Creates the input file element and adds it to inputFileCt.
* The created input file elementis sized, positioned, and styled appropriately.
* Event handlers for the element are set up, and a tooltip is applied if defined in the original config.
* @private
*/
createInputFile: function(){

this.inputFileEl = this.floatEl.createChild({
tag: 'input',
type: 'file',
size: 1, // must be > 0. It's value doesn't really matter due to our masking div (inputFileCt).
name: this.inputFileName || Ext.id(this.el),
// Use the same pointer as an Ext.Button would use. This doesn't work in Firefox.
// This positioning right-aligns the input file to ensure that the "Browse" button is visible.
style: {
position: 'absolute',
cursor: 'pointer',
right: '0px',
top: '0px'
}
});
this.inputFileEl = this.inputFileEl.child('input') || this.inputFileEl;

// setup events
this.inputFileEl.on({
'click': this.onInputFileClick,
'change': this.onInputFileChange,
scope: this
});

// add a tooltip
if (this.tooltip) {
if (typeof this.tooltip == 'object') {
Ext.QuickTips.register(Ext.apply({
target: this.inputFileEl
}, this.tooltip));
} else {
this.inputFileEl.dom[this.tooltipType] = this.tooltip;
}
}
},

/**
* Handler when the cursor moves over the clipEl.
* The floatEl gets centered to the cursor location.
* @param {Event} e mouse event.
* @private
*/
onButtonMouseMove: function(e){
var xy = e.getXY();
xy[0] -= this.FLOAT_EL_WIDTH / 2;
xy[1] -= this.FLOAT_EL_HEIGHT / 2;
this.floatEl.setXY(xy);
},

/**
* Handler when inputFileEl's "Browse..." button is clicked.
* @param {Event} e click event.
* @private
*/
onInputFileClick: function(e){
e.stopPropagation();
},

/**
* Handler when inputFileEl changes value (i.e. a new file is selected).
* @private
*/
onInputFileChange: function(){
if (this.originalHandler) {
this.originalHandler.call(this.originalScope, this);
}
},


/*
* Public methods:
*/
/**
* Detaches the input file associated with this BrowseButton so that it can be used for other purposed (e.g. uplaoding).
* The returned input file has all listeners and tooltips applied to it by this class removed.
* @param {Boolean} whether to create a new input file element for this BrowseButton after detaching.
* True will prevent creation. Defaults to false.
* @return {Ext.Element} the detached input file element.
*/
detachInputFile: function(noCreate){
var result = this.inputFileEl;

if (typeof this.tooltip == 'object') {
Ext.QuickTips.unregister(this.inputFileEl);
} else {
this.inputFileEl.dom[this.tooltipType] = null;
}
this.inputFileEl.removeAllListeners();
this.inputFileEl = null;

if (!noCreate) {
this.createInputFile();
}
return result;
},

/**
* @return {Ext.Element} the input file element attached to this BrowseButton.
*/
getInputFile: function(){
return this.inputFileEl;
},

/**
* @see Ext.Button.disable
*/
disable: function(){
Ext.ux.form.BrowseButton.superclass.disable.call(this);
this.inputFileEl.dom.disabled = true;
},

/**
* @see Ext.Button.enable
*/
enable: function(){
Ext.ux.form.BrowseButton.superclass.enable.call(this);
this.inputFileEl.dom.disabled = false;
}
});

Ext.reg('browsebutton', Ext.ux.form.BrowseButton);

// vim: ts=4:sw=4:nu:fdc=2:nospell
/**
* Ext.ux.IconCombo Extension Class for Ext 2.x Library
*
* @author Ing. Jozef Sakalos
* @version $Id: Ext.ux.IconCombo.js 101 2008-03-27 00:46:38Z jozo $
*
* @license Ext.ux.IconCombo is licensed under the terms of
* the Open Source LGPL 3.0 license. Commercial use is permitted to the extent
* that the code/component(s) do NOT become part of another Open Source or Commercially
* licensed development library or toolkit without explicit permission.
*
* License details: http://www.gnu.org/licenses/lgpl.html
*/

/*global Ext */

/**
* @class Ext.ux.IconCombo
* @extends Ext.form.ComboBox
*/
Ext.ux.IconCombo = Ext.extend(Ext.form.ComboBox, {
initComponent:function() {

Ext.apply(this, {
tpl: '<tpl for=".">'
+ '<div class="x-combo-list-item ux-icon-combo-item '
+ '{' + this.iconClsField + '}">'
+ '{' + this.displayField + '}'
+ '</div></tpl>'
});

// call parent initComponent
Ext.ux.IconCombo.superclass.initComponent.apply(this, arguments);

} // eo function initComponent

,onRender:function(ct, position) {
// call parent onRender
Ext.ux.IconCombo.superclass.onRender.apply(this, arguments);

// adjust styles
this.wrap.applyStyles({position:'relative'});
this.el.addClass('ux-icon-combo-input');

// add div for icon
this.icon = Ext.DomHelper.append(this.el.up('div.x-form-field-wrap'), {
tag: 'div', style:'position:absolute'
});
} // eo function onRender

,afterRender:function() {
Ext.ux.IconCombo.superclass.afterRender.apply(this, arguments);
if(undefined !== this.value) {
this.setValue(this.value);
}
} // eo function afterRender
,setIconCls:function() {
var rec = this.store.query(this.valueField, this.getValue()).itemAt(0);
if(rec && this.icon) {
this.icon.className = 'ux-icon-combo-icon ' + rec.get(this.iconClsField);
}
} // eo function setIconCls

,setValue: function(value) {
Ext.ux.IconCombo.superclass.setValue.call(this, value);
this.setIconCls();
} // eo function setValue


,clearValue:function() {
Ext.ux.IconCombo.superclass.clearValue.call(this);
if(this.icon) {
this.icon.className = '';
}
} // eo function clearValue

});

// register xtype
Ext.reg('iconcombo', Ext.ux.IconCombo);

// eof

// vim: ts=4:sw=4:nu:fdc=4:nospell
/**
* Ext.ux.LangSelectCombo - Combo pre-configured for language selection
*
* @author Ing. Jozef Sakáloš <jsakalos@aariadne.com>
* @copyright (c) 2008, by Ing. Jozef Sakáloš
* @date 21. March 2008
* @version $Id: Ext.ux.LangSelectCombo.js 112 2008-03-28 21:11:17Z jozo $
*/

/*global Ext */

/**
* @class Ext.ux.LangSelectCombo
* @extends Ext.ux.IconCombo
*/

Ext.ux.LangSelectCombo = Ext.extend(Ext.ux.IconCombo, {
selectLangText:'Select Language'
,lazyRender:true
,lazyInit:true
,langVariable:'locale'
,typeAhead:true
,initComponent:function() {
var langCode = Ext.state.Manager.getProvider() ? Ext.state.Manager.get(this.langVariable) : 'en_US'
langCode = langCode ? langCode : 'en_US'
Ext.apply(this, {
store:new Ext.data.SimpleStore({
id:0
,fields:[
{name:'langCode', type:'string'}
,{name:'langName', type:'string'}
,{name:'langCls', type:'string'}
]
,data:[
['cs_CZ', 'Český', 'ux-flag-cz']
,['de_DE', 'Deutsch', 'ux-flag-de']
,['en_US', 'English', 'ux-flag-us']
,['ru_RU', 'Russian', 'ux-flag-ru']
,['sk_SK', 'Slovenský', 'ux-flag-sk']
,['es_ES', 'Spanish', 'ux-flag-es']
,['tr_TR', 'Turkish', 'ux-flag-tr']
]
})
,valueField:'langCode'
,displayField:'langName'
,iconClsField:'langCls'
,triggerAction:'all'
,mode:'local'
,forceSelection:true
,value:langCode

}) // eo apply

// call parent
Ext.ux.LangSelectCombo.superclass.initComponent.apply(this, arguments);

} // eo function initComponent

,onSelect:function(record) {
// call parent
Ext.ux.LangSelectCombo.superclass.onSelect.apply(this, arguments);

var langCode = record.get('langCode');
// save state to state manager
if(Ext.state.Manager.getProvider()) {
Ext.state.Manager.set(this.langVariable, langCode);
}

// reload page
window.location.search = this.langVariable + '=' + langCode;

} // eo function onSelect

}) // eo extend

// eof

// vim: ts=4:sw=4:nu:fdc=4:nospell
/**
* Ext.ux.ThemeCombo - Combo pre-configured for themes selection
*
* @author Ing. Jozef Sakáloš <jsakalos@aariadne.com>
* @copyright (c) 2008, by Ing. Jozef Sakáloš
* @date 30. January 2008
* @version $Id: Ext.ux.ThemeCombo.js 116 2008-03-30 01:09:45Z jozo $
*
* @license Ext.ux.ThemeCombo is licensed under the terms of
* the Open Source LGPL 3.0 license. Commercial use is permitted to the extent
* that the code/component(s) do NOT become part of another Open Source or Commercially
* licensed development library or toolkit without explicit permission.
*
* License details: http://www.gnu.org/licenses/lgpl.html
*/

/*global Ext */

Ext.ux.ThemeCombo = Ext.extend(Ext.form.ComboBox, {
// configurables
themeBlueText: 'Ext Blue Theme'
,themeGrayText: 'Gray Theme'
,themeBlackText: 'Black Theme'
,themeOliveText: 'Olive Theme'
,themePurpleText: 'Purple Theme'
,themeDarkGrayText: 'Dark Gray Theme'
,themeSlateText: 'Slate Theme'
,themeVistaText: 'Vista Theme'
,themePeppermintText: 'Peppermint Theme'
,themeChocolateText: 'Chocolate Theme'
,themeGreenText: 'Green Theme'
,themeIndigoText: 'Indigo Theme'
,themeMidnightText: 'Midnight Theme'
,themeSilverCherryText: 'Silver Cherry Theme'
,themeSlicknessText: 'Slickness Theme'
,themeVar:'theme'
,selectThemeText: 'Select Theme'
,themeGrayExtndText:'Gray-Extended Theme'
,lazyRender:true
,lazyInit:true
,cssPath:'../ext/resources/css/' // mind the trailing slash

// {{{
,initComponent:function() {

Ext.apply(this, {
store: new Ext.data.SimpleStore({
fields: ['themeFile', {name:'themeName', type:'string'}]
,data: [
['xtheme-default.css', this.themeBlueText]
,['xtheme-gray.css', this.themeGrayText]
,['xtheme-darkgray.css', this.themeDarkGrayText]
,['xtheme-black.css', this.themeBlackText]
,['xtheme-olive.css', this.themeOliveText]
,['xtheme-purple.css', this.themePurpleText]
,['xtheme-slate.css', this.themeSlateText]
,['xtheme-peppermint.css', this.themePeppermintText]
,['xtheme-chocolate.css', this.themeChocolateText]
,['xtheme-green.css', this.themeGreenText]
,['xtheme-indigo.css', this.themeIndigoText]
,['xtheme-midnight.css', this.themeMidnightText]
,['xtheme-silverCherry.css', this.themeSilverCherryText]
,['xtheme-slickness.css', this.themeSlicknessText]
,['xtheme-gray-extend.css', this.themeGrayExtndText]
]
})
,valueField: 'themeFile'
,displayField: 'themeName'
,triggerAction:'all'
,mode: 'local'
,forceSelection:true
,editable:false
,fieldLabel: this.selectThemeText
}); // end of apply

this.store.sort('themeName');

// call parent
Ext.ux.ThemeCombo.superclass.initComponent.apply(this, arguments);

this.setValue(Ext.state.Manager.get(this.themeVar) || 'xtheme-default.css');

} // end of function initComponent
// }}}
// {{{
,setValue:function(val) {
Ext.ux.ThemeCombo.superclass.setValue.apply(this, arguments);

// set theme
Ext.util.CSS.swapStyleSheet(this.themeVar, this.cssPath + val);

if(Ext.state.Manager.getProvider()) {
Ext.state.Manager.set(this.themeVar, val);
}
} // eo function setValue
// }}}

}); // end of extend

// register xtype
Ext.reg('themecombo', Ext.ux.ThemeCombo);

// end of file

// vim: ts=4:sw=4:nu:fdc=4:nospell
/**
* Ext.ux.form.UploadPanel
*
* @author Ing. Jozef Sakáloš
* @version $Id: Ext.ux.UploadPanel.js 94 2008-03-24 01:04:27Z jozo $
* @date 13. March 2008
*
* @license Ext.ux.form.UploadPanel is licensed under the terms of
* the Open Source LGPL 3.0 license. Commercial use is permitted to the extent
* that the code/component(s) do NOT become part of another Open Source or Commercially
* licensed development library or toolkit without explicit permission.
*
* License details: http://www.gnu.org/licenses/lgpl.html
*/


/*global Ext */

/**
* @class Ext.ux.UploadPanel
* @extends Ext.Panel
*/
Ext.ux.UploadPanel = Ext.extend(Ext.Panel, {

// configuration options overridable from outside
// {{{
/**
* @cfg {String} addIconCls icon class for add (file browse) button
*/
addIconCls:'icon-plus'

/**
* @cfg {String} addText Text on Add button
*/
,addText:'Add'

/**
* @cfg {Object} baseParams This object is not used directly by FileTreePanel but it is
* propagated to lower level objects instead. Included here for convenience.
*/

/**
* @cfg {String} bodyStyle style to use for panel body
*/
,bodyStyle:'padding:2px'

/**
* @cfg {String} buttonsAt Where buttons are placed. Valid values are tbar, bbar, body (defaults to 'tbar')
*/
,buttonsAt:'tbar'

/**
* @cfg {String} clickRemoveText
*/
,clickRemoveText:'Click to remove'

/**
* @cfg {String} clickStopText
*/
,clickStopText:'Click to stop'

/**
* @cfg {String} emptyText empty text for dataview
*/
,emptyText:'No files'

/**
* @cfg {Boolean} enableProgress true to enable querying server for progress information
* Passed to underlying uploader. Included here for convenience.
*/
,enableProgress:true

/**
* @cfg {String} errorText
*/
,errorText:'Error'

/**
* @cfg {String} fileCls class prefix to use for file type classes
*/
,fileCls:'file'

/**
* @cfg {String} fileQueuedText File upload status text
*/
,fileQueuedText:'File <b>{0}</b> is queued for upload'

/**
* @cfg {String} fileDoneText File upload status text
*/
,fileDoneText:'File <b>{0}</b> has been successfully uploaded'

/**
* @cfg {String} fileFailedText File upload status text
*/
,fileFailedText:'File <b>{0}</b> failed to upload'

/**
* @cfg {String} fileStoppedText File upload status text
*/
,fileStoppedText:'File <b>{0}</b> stopped by user'

/**
* @cfg {String} fileUploadingText File upload status text
*/
,fileUploadingText:'Uploading file <b>{0}</b>'

/**
* @cfg {Number} maxFileSize Maximum upload file size in bytes
* This config property is propagated down to uploader for convenience
*/
,maxFileSize:524288

/**
* @cfg {Number} Maximum file name length for short file names
*/
,maxLength:18

/**
* @cfg {String} removeAllIconCls iconClass to use for Remove All button (defaults to 'icon-cross'
*/
,removeAllIconCls:'icon-cross'

/**
* @cfg {String} removeAllText text to use for Remove All button tooltip
*/
,removeAllText:'Remove All'

/**
* @cfg {String} removeIconCls icon class to use for remove file icon
*/
,removeIconCls:'icon-minus'

/**
* @cfg {String} removeText Remove text
*/
,removeText:'Remove'

/**
* @cfg {String} selectedClass class for selected item of DataView
*/
,selectedClass:'ux-up-item-selected'

/**
* @cfg {Boolean} singleUpload true to upload files in one form, false to upload one by one
* This config property is propagated down to uploader for convenience
*/
,singleUpload:false

/**
* @cfg {String} stopAllText
*/
,stopAllText:'Stop All'

/**
* @cfg {String} stopIconCls icon class to use for stop
*/
,stopIconCls:'icon-stop'

/**
* @cfg {String/Ext.XTemplate} tpl Template for DataView.
*/

/**
* @cfg {String} uploadText Upload text
*/
,uploadText:'Upload'

/**
* @cfg {String} uploadIconCls icon class to use for upload button
*/
,uploadIconCls:'icon-upload'

/**
* @cfg {String} workingIconCls iconClass to use for busy indicator
*/
,workingIconCls:'icon-working'

// }}}

// overrides
// {{{
,initComponent:function() {

// {{{
// create buttons
// add (file browse button) configuration
var addCfg = {
xtype:'browsebutton'
,text:this.addText + '...'
,iconCls:this.addIconCls
,scope:this
,handler:this.onAddFile
};

// upload button configuration
var upCfg = {
xtype:'button'
,iconCls:this.uploadIconCls
,text:this.uploadText
,scope:this
,handler:this.onUpload
,disabled:true
};

// remove all button configuration
var removeAllCfg = {
xtype:'button'
,iconCls:this.removeAllIconCls
,tooltip:this.removeAllText
,scope:this
,handler:this.onRemoveAllClick
,disabled:true
};

// todo: either to cancel buttons in body or implement it
if('body' !== this.buttonsAt) {
this[this.buttonsAt] = [addCfg, upCfg, '->', removeAllCfg];
}
// }}}
// {{{
// create store
// fields for record
var fields = [
{name:'id', type:'text', system:true}
,{name:'shortName', type:'text', system:true}
,{name:'fileName', type:'text', system:true}
,{name:'filePath', type:'text', system:true}
,{name:'fileCls', type:'text', system:true}
,{name:'input', system:true}
,{name:'form', system:true}
,{name:'state', type:'text', system:true}
,{name:'error', type:'text', system:true}
,{name:'progressId', type:'int', system:true}
,{name:'bytesTotal', type:'int', system:true}
,{name:'bytesUploaded', type:'int', system:true}
,{name:'estSec', type:'int', system:true}
,{name:'filesUploaded', type:'int', system:true}
,{name:'speedAverage', type:'int', system:true}
,{name:'speedLast', type:'int', system:true}
,{name:'timeLast', type:'int', system:true}
,{name:'timeStart', type:'int', system:true}
,{name:'pctComplete', type:'int', system:true}
];


// add custom fields if passed
if(Ext.isArray(this.customFields)) {
fields.push(this.customFields);
}

// create store
this.store = new Ext.data.SimpleStore({
id:0
,fields:fields
,data:[]
});
// }}}
// {{{
// create view
Ext.apply(this, {
items:[{
xtype:'dataview'
,itemSelector:'div.ux-up-item'
,store:this.store
,selectedClass:this.selectedClass
,singleSelect:true
,emptyText:this.emptyText
,tpl: this.tpl || new Ext.XTemplate(
'<tpl for=".">'
+ '<div class="ux-up-item">'
// + '<div class="ux-up-indicator">&#160;</div>'
+ '<div class="ux-up-icon-file {fileCls}">&#160;</div>'
+ '<div class="ux-up-text x-unselectable" qtip="{fileName}">{shortName}</div>'
+ '<div id="remove-{[values.input.id]}" class="ux-up-icon-state ux-up-icon-{state}"'
+ 'qtip="{[this.scope.getQtip(values)]}">&#160;</div>'
+ '</div>'
+ '</tpl>'
, {scope:this}
)
,listeners:{click:{scope:this, fn:this.onViewClick}}

}]
});
// }}}

// call parent
Ext.ux.UploadPanel.superclass.initComponent.apply(this, arguments);

// save useful references
this.view = this.items.itemAt(0);

// {{{
// add events
this.addEvents(
/**
* Fires before the file is added to store. Return false to cancel the add
* @event beforefileadd
* @param {Ext.ux.UploadPanel} this
* @param {Ext.Element} input (type=file) being added
*/
'beforefileadd'
/**
* Fires after the file is added to the store
* @event fileadd
* @param {Ext.ux.UploadPanel} this
* @param {Ext.data.Store} store
* @param {Ext.data.Record} Record (containing the input) that has been added to the store
*/
,'fileadd'
/**
* Fires before the file is removed from the store. Return false to cancel the remove
* @event beforefileremove
* @param {Ext.ux.UploadPanel} this
* @param {Ext.data.Store} store
* @param {Ext.data.Record} Record (containing the input) that is being removed from the store
*/
,'beforefileremove'
/**
* Fires after the record (file) has been removed from the store
* @event fileremove
* @param {Ext.ux.UploadPanel} this
* @param {Ext.data.Store} store
*/
,'fileremove'
/**
* Fires before all files are removed from the store (queue). Return false to cancel the clear.
* Events for individual files being removed are suspended while clearing the queue.
* @event beforequeueclear
* @param {Ext.ux.UploadPanel} this
* @param {Ext.data.Store} store
*/
,'beforequeueclear'
/**
* Fires after the store (queue) has been cleared
* Events for individual files being removed are suspended while clearing the queue.
* @event queueclear
* @param {Ext.ux.UploadPanel} this
* @param {Ext.data.Store} store
*/
,'queueclear'
/**
* Fires after the upload button is clicked but before any upload is started
* Return false to cancel the event
* @param {Ext.ux.UploadPanel} this
*/
,'beforeupload'
);
// }}}
// {{{
// relay view events
this.relayEvents(this.view, [
'beforeclick'
,'beforeselect'
,'click'
,'containerclick'
,'contextmenu'
,'dblclick'
,'selectionchange'
]);
// }}}

// create uploader
var config = {
store:this.store
,singleUpload:this.singleUpload
,maxFileSize:this.maxFileSize
,enableProgress:this.enableProgress
,url:this.url
,path:this.path
};
if(this.baseParams) {
config.baseParams = this.baseParams;
}
this.uploader = new Ext.ux.FileUploader(config);

// relay uploader events
this.relayEvents(this.uploader, [
'beforeallstart'
,'allfinished'
,'progress'
]);

// install event handlers
this.on({
beforeallstart:{scope:this, fn:function() {
this.uploading = true;
this.updateButtons();
}}
,allfinished:{scope:this, fn:function() {
this.uploading = false;
this.updateButtons();
}}
,progress:{fn:this.onProgress.createDelegate(this)}
});
} // eo function initComponent
// }}}
// {{{
/**
* onRender override, saves references to buttons
* @private
*/
,onRender:function() {
// call parent
Ext.ux.UploadPanel.superclass.onRender.apply(this, arguments);

// save useful references
var tb = 'tbar' === this.buttonsAt ? this.getTopToolbar() : this.getBottomToolbar();
this.addBtn = Ext.getCmp(tb.items.first().id);
this.uploadBtn = Ext.getCmp(tb.items.itemAt(1).id);
this.removeAllBtn = Ext.getCmp(tb.items.last().id);
} // eo function onRender
// }}}

// added methods
// {{{
/**
* called by XTemplate to get qtip depending on state
* @private
* @param {Object} values XTemplate values
*/
,getQtip:function(values) {
var qtip = '';
switch(values.state) {
case 'queued':
qtip = String.format(this.fileQueuedText, values.fileName);
qtip += '<br>' + this.clickRemoveText;
break;

case 'uploading':
qtip = String.format(this.fileUploadingText, values.fileName);
qtip += '<br>' + values.pctComplete + '% done';
qtip += '<br>' + this.clickStopText;
break;

case 'done':
qtip = String.format(this.fileDoneText, values.fileName);
qtip += '<br>' + this.clickRemoveText;
break;

case 'failed':
qtip = String.format(this.fileFailedText, values.fileName);
qtip += '<br>' + this.errorText + ':' + values.error;
qtip += '<br>' + this.clickRemoveText;
break;


case 'stopped':
qtip = String.format(this.fileStoppedText, values.fileName);
qtip += '<br>' + this.clickRemoveText;
break;
}
return qtip;
} // eo function getQtip
// }}}
// {{{
/**
* get file name
* @private
* @param {Ext.Element} inp Input element containing the full file path
* @return {String}
*/
,getFileName:function(inp) {
return inp.getValue().split(/[\/\\]/).pop();
} // eo function getFileName
// }}}
// {{{
/**
* get file path (excluding the file name)
* @private
* @param {Ext.Element} inp Input element containing the full file path
* @return {String}
*/
,getFilePath:function(inp) {
return inp.getValue().replace(/[^\/\\]+$/,'');
} // eo function getFilePath
// }}}
// {{{
/**
* returns file class based on name extension
* @private
* @param {String} name File name to get class of
* @return {String} class to use for file type icon
*/
,getFileCls: function(name) {
var atmp = name.split('.');
if(1 === atmp.length) {
return this.fileCls;
}
else {
return this.fileCls + '-' + atmp.pop().toLowerCase();
}
}
// }}}
// {{{
/**
* called when file is added - adds file to store
* @private
* @param {Ext.ux.BrowseButton}
*/
,onAddFile:function(bb) {
if(true !== this.eventsSuspended && false === this.fireEvent('beforefileadd', this, bb.getInputFile())) {
return;
}
var inp = bb.detachInputFile();
inp.addClass('x-hidden');
var fileName = this.getFileName(inp);

// create new record and add it to store
var rec = new this.store.recordType({
input:inp
,fileName:fileName
,filePath:this.getFilePath(inp)
,shortName: Ext.util.Format.ellipsis(fileName, this.maxLength)
,fileCls:this.getFileCls(fileName)
,state:'queued'
}, inp.id);
rec.commit();
this.store.add(rec);

this.syncShadow();

this.uploadBtn.enable();
this.removeAllBtn.enable();

if(true !== this.eventsSuspended) {
this.fireEvent('fileadd', this, this.store, rec);
}

} // eo onAddFile
// }}}
// {{{
/**
* destroys child components
* @private
*/
,onDestroy:function() {

// destroy uploader
if(this.uploader) {
this.uploader.stopAll();
this.uploader.purgeListeners();
this.uploader = null;
}

// destroy view
if(this.view) {
this.view.purgeListeners();
this.view.destroy();
this.view = null;
}

// destroy store
if(this.store) {
this.store.purgeListeners();
this.store.destroy();
this.store = null;
}

} // eo function onDestroy
// }}}
// {{{
/**
* progress event handler
* @private
* @param {Ext.ux.FileUploader} uploader
* @param {Object} data progress data
* @param {Ext.data.Record} record
*/
,onProgress:function(uploader, data, record) {
var bytesTotal, bytesUploaded, pctComplete, state, idx, item, width, pgWidth;
if(record) {
state = record.get('state');
bytesTotal = record.get('bytesTotal') || 1;
bytesUploaded = record.get('bytesUploaded') || 0;
if('uploading' === state) {
pctComplete = Math.round(1000 * bytesUploaded/bytesTotal) / 10;
}
else if('done' === 'state') {
pctComplete = 100;
}
else {
pctComplete = 0;
}
record.set('pctComplete', pctComplete);

idx = this.store.indexOf(record);
item = Ext.get(this.view.getNode(idx));
if(item) {
width = item.getWidth();
item.applyStyles({'background-position':width * pctComplete / 100 + 'px'});
}
}
} // eo function onProgress
// }}}
// {{{
/**
* called when file remove icon is clicked - performs the remove
* @private
* @param {Ext.data.Record}
*/
,onRemoveFile:function(record) {
if(true !== this.eventsSuspended && false === this.fireEvent('beforefileremove', this, this.store, record)) {
return;
}

// remove DOM elements
var inp = record.get('input');
var wrap = inp.up('em');
inp.remove();
if(wrap) {
wrap.remove();
}

// remove record from store
this.store.remove(record);

var count = this.store.getCount();
this.uploadBtn.setDisabled(!count);
this.removeAllBtn.setDisabled(!count);

if(true !== this.eventsSuspended) {
this.fireEvent('fileremove', this, this.store);
this.syncShadow();
}
} // eo function onRemoveFile
// }}}
// {{{
/**
* Remove All/Stop All button click handler
* @private
*/
,onRemoveAllClick:function(btn) {
if(true === this.uploading) {
this.stopAll();
}
else {
this.removeAll();
}
} // eo function onRemoveAllClick

,stopAll:function() {
this.uploader.stopAll();
} // eo function stopAll
// }}}
// {{{
/**
* DataView click handler
* @private
*/
,onViewClick:function(view, index, node, e) {
var t = e.getTarget('div:any(.ux-up-icon-queued|.ux-up-icon-failed|.ux-up-icon-done|.ux-up-icon-stopped)');
if(t) {
this.onRemoveFile(this.store.getAt(index));
}
t = e.getTarget('div.ux-up-icon-uploading');
if(t) {
this.uploader.stopUpload(this.store.getAt(index));
}
} // eo function onViewClick
// }}}
// {{{
/**
* tells uploader to upload
* @private
*/
,onUpload:function() {
if(true !== this.eventsSuspended && false === this.fireEvent('beforeupload', this)) {
return false;
}
this.uploader.upload();
} // eo function onUpload
// }}}
// {{{
/**
* url setter
*/
,setUrl:function(url) {
this.url = url;
this.uploader.setUrl(url);
} // eo function setUrl
// }}}
// {{{
/**
* path setter
*/
,setPath:function(path) {
this.uploader.setPath(path);
} // eo function setPath
// }}}
// {{{
/**
* Updates buttons states depending on uploading state
* @private
*/
,updateButtons:function() {
if(true === this.uploading) {
this.addBtn.disable();
this.uploadBtn.disable();
this.removeAllBtn.setIconClass(this.stopIconCls);
this.removeAllBtn.getEl().child(this.removeAllBtn.buttonSelector).dom[this.removeAllBtn.tooltipType] = this.stopAllText;
}
else {
this.addBtn.enable();
this.uploadBtn.enable();
this.removeAllBtn.setIconClass(this.removeAllIconCls);
this.removeAllBtn.getEl().child(this.removeAllBtn.buttonSelector).dom[this.removeAllBtn.tooltipType] = this.removeAllText;
}
} // eo function updateButtons
// }}}
// {{{
/**
* Removes all files from store and destroys file inputs
*/
,removeAll:function() {
var suspendState = this.eventsSuspended;
if(false !== this.eventsSuspended && false === this.fireEvent('beforequeueclear', this, this.store)) {
return false;
}
this.suspendEvents();


this.store.each(this.onRemoveFile, this);

this.eventsSuspended = suspendState;
if(true !== this.eventsSuspended) {
this.fireEvent('queueclear', this, this.store);
}
this.syncShadow();
} // eo function removeAll
// }}}
// {{{
/**
* synchronize context menu shadow if we're in contextmenu
* @private
*/
,syncShadow:function() {
if(this.contextmenu && this.contextmenu.shadow) {
this.contextmenu.getEl().shadow.show(this.contextmenu.getEl());
}
} // eo function syncShadow
// }}}

}); // eo extend

// register xtype
Ext.reg('uploadpanel', Ext.ux.UploadPanel);

// eof

// vim: ts=4:sw=4:nu:fdc=4:nospell
/**
* CellActions plugin for Ext grid
*
* Contains renderer for an icon and fires events when icon is clicked
*
* @author Ing. Jozef Sakáloš
* @date 22. March 2008
* @version $Id: Ext.ux.grid.CellActions.js 253 2008-05-11 09:31:48Z jozo $
*
* @license Ext.ux.grid.CellActions is licensed under the terms of
* the Open Source LGPL 3.0 license. Commercial use is permitted to the extent
* that the code/component(s) do NOT become part of another Open Source or Commercially
* licensed development library or toolkit without explicit permission.
*
* License details: http://www.gnu.org/licenses/lgpl.html
*/

/**
* The following css is required:
*
* .ux-cell-value {
* position:relative;
* zoom:1;
* }
* .ux-cell-actions {
* position:absolute;
* right:0;
* top:-2px;
* }
* .ux-cell-actions-left {
* left:0;
* top:-2px;
* }
* .ux-cell-action {
* width:16px;
* height:16px;
* float:left;
* cursor:pointer;
* margin: 0 0 0 4px;
* }
* .ux-cell-actions-left .ux-cell-action {
* margin: 0 4px 0 0;
* }
*/

/*global Ext */

Ext.ns('Ext.ux.grid');

// constructor and cellActions documentation
// {{{
/**
* @class Ext.ux.grid.CellActions
* @extends Ext.util.Observable
* @constructor
*
* CellActions plugin causes that column model recognizes the config property cellAcions
* that is the array of configuration objects for that column. The documentationi follows.
*
* THE FOLLOWING CONFIG OPTIONS ARE FOR COLUMN MODEL COLUMN, NOT FOR CellActions ITSELF.
*
* @cfg {Array} cellActions Mandatory. Array of action configuration objects. The following
* configuration options of action are recognized:
*
* - @cfg {Function} callback Optional. Function to call if the action icon is clicked.
* This function is called with same signature as action event and in its original scope.
* If you need to call it in different scope or with another signature use
* createCallback or createDelegate functions. Works for statically defined actions. Use
* callbacks configuration options for store bound actions.
*
* - @cfg {Function} cb Shortcut for callback.
*
* - @cfg {String} iconIndex Optional, however either iconIndex or iconCls must be
* configured. Field name of the field of the grid store record that contains
* css class of the icon to show. If configured, shown icons can vary depending
* of the value of this field.
*
* - @cfg {String} iconCls. css class of the icon to show. It is ignored if iconIndex is
* configured. Use this if you want static icons that are not base on the values in the record.
*
* - @cfg {String} qtipIndex Optional. Field name of the field of the grid store record that
* contains tooltip text. If configured, the tooltip texts are taken from the store.
*
* - @cfg {String} tooltip Optional. Tooltip text to use as icon tooltip. It is ignored if
* qtipIndex is configured. Use this if you want static tooltips that are not taken from the store.
*
* - @cfg {String} qtip Synonym for tooltip
*
* - @cfg {String} style Optional. Style to apply to action icon container.
*/
Ext.ux.grid.CellActions = function(config) {
Ext.apply(this, config);

this.addEvents(
/**
* @event action
* Fires when user clicks a cell action
* @param {Ext.grid.GridPanel} grid
* @param {Ext.data.Record} record Record containing data of clicked cell
* @param {String} action Action clicked (equals iconCls);
* @param {Mixed} value Value of the clicke cell
* @param {String} dataIndex as specified in column model
* @param {Number} rowIndex Index of row clicked
* @param {Number} colIndex Incex of col clicked
*/
'action'
/**
* @event beforeaction
* Fires when user clicks a cell action but before action event is fired. Return false to cancel the action;
* @param {Ext.grid.GridPanel} grid
* @param {Ext.data.Record} record Record containing data of clicked cell
* @param {String} action Action clicked (equals iconCls);
* @param {Mixed} value Value of the clicke cell
* @param {String} dataIndex as specified in column model
* @param {Number} rowIndex Index of row clicked
* @param {Number} colIndex Incex of col clicked
*/
,'beforeaction'
);
// call parent
Ext.ux.grid.CellActions.superclass.constructor.call(this);

}; // eo constructor
// }}}

Ext.extend(Ext.ux.grid.CellActions, Ext.util.Observable, {

/**
* @cfg {String} actionEvnet Event to trigger actions, e.g. click, dblclick, mouseover (defaults to 'click')
*/
actionEvent:'click'

/**
* @cfg {Number} actionWidth Width of action icon in pixels. Has effect only if align:'left'
*/
,actionWidth:20

/**
* @cfg {String} align Set to 'left' to put action icons before the cell text. (defaults to undefined, meaning right)
*/

/**
* @private
* @cfg {String} tpl Template for cell with actions
*/
,tpl:'<div class="ux-cell-value" style="padding-left:{padding}px">'
+'<tpl if="\'left\'!==align">{value}</tpl>'
+'<div class="ux-cell-actions<tpl if="\'left\'===align"> ux-cell-actions-left</tpl>" style="width:{width}px">'
+'<tpl for="actions"><div class="ux-cell-action {cls}" qtip="{qtip}" style="{style}">&#160;</div></tpl>'
+'</div>'
+'<tpl if="\'left\'===align">{value}</tpl>'
+'<div>'

/**
* Called at the end of processActions. Override this if you need it.
* @param {Object} c Column model configuration object
* @param {Object} data See this.processActions method for details
*/
,userProcessing:Ext.emptyFn

// {{{
/**
* Init function
* @param {Ext.grid.GridPanel} grid Grid this plugin is in
*/
,init:function(grid) {
this.grid = grid;
// grid.on({scope:this, render:this.onRenderGrid});
grid.afterRender = grid.afterRender.createSequence(this.onRenderGrid, this);

var cm = this.grid.getColumnModel();
Ext.each(cm.config, function(c, idx) {
if('object' === typeof c.cellActions) {
c.origRenderer = cm.getRenderer(idx);
c.renderer = this.renderActions.createDelegate(this);
}
}, this);



} // eo function init
// }}}
// {{{
/**
* grid render event handler, install actionEvent handler on view.mainBody
* @private
*/
,onRenderGrid:function() {

// install click event handler on view mainBody
this.view = this.grid.getView();
var cfg = {scope:this};
cfg[this.actionEvent] = this.onClick;
this.view.mainBody.on(cfg);

} // eo function onRender
// }}}
// {{{
/**
* Returns data to apply to template. Override this if needed
* @param {Mixed} value
* @param {Object} cell object to set some attributes of the grid cell
* @param {Ext.data.Record} record from which the data is extracted
* @param {Number} row row index
* @param {Number} col col index
* @param {Ext.data.Store} store object from which the record is extracted
* @returns {Object} data to apply to template
*/
,getData:function(value, cell, record, row, col, store) {
return record.data || {};
}
// }}}
// {{{
/**
* replaces (but calls) the original renderer from column model
* @private
* @param {Mixed} value
* @param {Object} cell object to set some attributes of the grid cell
* @param {Ext.data.Record} record from which the data is extracted
* @param {Number} row row index
* @param {Number} col col index
* @param {Ext.data.Store} store object from which the record is extracted
* @returns {String} markup of cell content
*/
,renderActions:function(value, cell, record, row, col, store) {

// get column config from column model
var c = this.grid.getColumnModel().config[col];

// get output of the original renderer
var val = c.origRenderer(value, cell, record, row, col, store);

// get actions template if we need but don't have one
if(c.cellActions && !c.actionsTpl) {
c.actionsTpl = this.processActions(c);
c.actionsTpl.compile();
}
// return original renderer output if we don't have actions
else if(!c.cellActions) {
return val;
}

// get and return final markup
var data = this.getData.apply(this, arguments);
data.value = val;
return c.actionsTpl.apply(data);

} // eo function renderActions
// }}}
// {{{
/**
* processes the actions configs from column model column, saves callbacks and creates template
* @param {Object} c column model config of one column
* @private
*/
,processActions:function(c) {

// callbacks holder
this.callbacks = this.callbacks || {};

// data for intermediate template
var data = {
align:this.align || 'right'
,width:this.actionWidth * c.cellActions.length
,padding:'left' === this.align ? this.actionWidth * c.cellActions.length : 0
,value:'{value}'
,actions:[]
};

// cellActions loop
Ext.each(c.cellActions, function(a, i) {

// save callback
if(a.iconCls && 'function' === typeof (a.callback || a.cb)) {
this.callbacks[a.iconCls] = a.callback || a.cb;
}

// data for intermediate xtemplate action
var o = {
cls:a.iconIndex ? '{' + a.iconIndex + '}' : (a.iconCls ? a.iconCls : '')
,qtip:a.qtipIndex ? '{' + a.qtipIndex + '}' : (a.tooltip || a.qtip ? a.tooltip || a.qtip : '')
,style:a.style ? a.style : ''
};
data.actions.push(o);

}, this); // eo cellActions loop

this.userProcessing(c, data);

// get and return final template
var xt = new Ext.XTemplate(this.tpl);
return new Ext.Template(xt.apply(data));

} // eo function processActions
// }}}
// {{{
/**
* Grid body actionEvent event handler
* @private
*/
,onClick:function(e, target) {

// collect all variables for callback and/or events
var t = e.getTarget('div.ux-cell-action');
var row = e.getTarget('.x-grid3-row');
var col = this.view.findCellIndex(target.parentNode.parentNode);
var c = this.grid.getColumnModel().config[col];
var record, dataIndex, value, action;
if(t) {
record = this.grid.store.getAt(row.rowIndex);
dataIndex = c.dataIndex;
value = record.get(dataIndex);
action = t.className.replace(/ux-cell-action /, '');
}

// check if we've collected all necessary variables
if(false !== row && false !== col && record && dataIndex && action) {

// call callback if any
if(this.callbacks && 'function' === typeof this.callbacks[action]) {
this.callbacks[action](this.grid, record, action, value, dataIndex, row.rowIndex, col);
}

// fire events
if(true !== this.eventsSuspended && false === this.fireEvent('beforeaction', this.grid, record, action, value, dataIndex, row.rowIndex, col)) {
return;
}
else if(true !== this.eventsSuspended) {
this.fireEvent('action', this.grid, record, action, value, dataIndex, row.rowIndex, col);
}

}
} // eo function onClick
// }}}

});

// register xtype
Ext.reg('cellactions', Ext.ux.grid.CellActions);

// eof

// vim: ts=4:sw=4:nu:fdc=4:nospell
/**
* RowActions plugin for Ext grid
*
* Contains renderer for icons and fires events when an icon is clicked
*
* @author Ing. Jozef Sakáloš
* @date 22. March 2008
* @version $Id: Ext.ux.grid.RowActions.js 150 2008-04-08 21:50:58Z jozo $
*
* @license Ext.ux.grid.RowActions is licensed under the terms of
* the Open Source LGPL 3.0 license. Commercial use is permitted to the extent
* that the code/component(s) do NOT become part of another Open Source or Commercially
* licensed development library or toolkit without explicit permission.
*
* License details: http://www.gnu.org/licenses/lgpl.html
*/

/*global Ext */

Ext.ns('Ext.ux.grid');

/**
* @class Ext.ux.grid.RowActions
* @extends Ext.util.Observable
*
* CSS rules from Ext.ux.RowActions.css are mandatory
*
* Important general information: Actions are identified by iconCls. Wherever an <i>action</i>
* is referenced (event argument, callback argument), the iconCls of clicked icon is used.
* In another words, action identifier === iconCls.
*
* Creates new RowActions plugin
* @constructor
* @param {Object} config The config object
*/
Ext.ux.grid.RowActions = function(config) {
Ext.apply(this, config);


// {{{
this.addEvents(
/**
* @event beforeaction
* Fires before action event. Return false to cancel the subsequent action event.
* @param {Ext.grid.GridPanel} grid
* @param {Ext.data.Record} record Record corresponding to row clicked
* @param {String} action Identifies the action icon clicked. Equals to icon css class name.
* @param {Integer} rowIndex Index of clicked grid row
* @param {Integer} colIndex Index of clicked grid column that contains all action icons
*/
'beforeaction'
/**
* @event action
* Fires when icon is clicked
* @param {Ext.grid.GridPanel} grid
* @param {Ext.data.Record} record Record corresponding to row clicked
* @param {String} action Identifies the action icon clicked. Equals to icon css class name.
* @param {Integer} rowIndex Index of clicked grid row
* @param {Integer} colIndex Index of clicked grid column that contains all action icons
*/
,'action'
/**
* @event beforegroupaction
* Fires before group action event. Return false to cancel the subsequent groupaction event.
* @param {Ext.grid.GridPanel} grid
* @param {Array} records Array of records in this group
* @param {String} action Identifies the action icon clicked. Equals to icon css class name.
* @param {String} groupId Identifies the group clicked
*/
,'beforegroupaction'
/**
* @event groupaction
* Fires when icon in a group header is clicked
* @param {Ext.grid.GridPanel} grid
* @param {Array} records Array of records in this group
* @param {String} action Identifies the action icon clicked. Equals to icon css class name.
* @param {String} groupId Identifies the group clicked
*/
,'groupaction'
);
// }}}

// call parent
Ext.ux.grid.RowActions.superclass.constructor.call(this);
};

Ext.extend(Ext.ux.grid.RowActions, Ext.util.Observable, {

// configuration options
// {{{
/**
* @cfg {Array} actions Mandatory. Array of action configuration objects. The following
* configuration options of action are recognized:
*
* - @cfg {Function} callback Optional. Function to call if the action icon is clicked.
* This function is called with same signature as action event and in its original scope.
* If you need to call it in different scope or with another signature use
* createCallback or createDelegate functions. Works for statically defined actions. Use
* callbacks configuration options for store bound actions.
*
* - @cfg {Function} cb Shortcut for callback.
*
* - @cfg {String} iconIndex Optional, however either iconIndex or iconCls must be
* configured. Field name of the field of the grid store record that contains
* css class of the icon to show. If configured, shown icons can vary depending
* of the value of this field.
*
* - @cfg {String} iconCls. css class of the icon to show. It is ignored if iconIndex is
* configured. Use this if you want static icons that are not base on the values in the record.
*
* - @cfg {Boolean} hide Optional. True to hide this action while still have a space in
* the grid column allocated to it. IMO, it doesn't make too much sense, use hideIndex instead.
*
* - @cfg (string} hideIndex Optional. Field name of the field of the grid store record that
* contains hide flag (falsie [null, '', 0, false, undefined] to show, anything else to hide).
*
* - @cfg {String} qtipIndex Optional. Field name of the field of the grid store record that
* contains tooltip text. If configured, the tooltip texts are taken from the store.
*
* - @cfg {String} tooltip Optional. Tooltip text to use as icon tooltip. It is ignored if
* qtipIndex is configured. Use this if you want static tooltips that are not taken from the store.
*
* - @cfg {String} qtip Synonym for tooltip
*
* - @cfg {String} textIndex Optional. Field name of the field of the grids store record
* that contains text to display on the right side of the icon. If configured, the text
* shown is taken from record.
*
* - @cfg {String} text Optional. Text to display on the right side of the icon. Use this
* if you want static text that are not taken from record. Ignored if textIndex is set.
*
* - @cfg {String} style Optional. Style to apply to action icon container.
*/

/**
* @cfg {String} actionEvnet Event to trigger actions, e.g. click, dblclick, mouseover (defaults to 'click')
*/
actionEvent:'click'

/**
* @cfg {Boolean} autoWidth true to calculate field width for iconic actions only.
*/
,autoWidth:true

/**
* @cfg {Array} groupActions Array of action to use for group headers of grouping grids.
* These actions support static icons, texts and tooltips same way as actions. There is one
* more action config recognized:
* - @cfg {String} align Set it to 'left' to place action icon next to the group header text.
* (defaults to undefined = icons are placed at the right side of the group header.
*/

/**
* @cfg {Object} callbacks iconCls keyed object that contains callback functions. For example:
* callbacks:{
* 'icon-open':function(...) {...}
* ,'icon-save':function(...) {...}
* }
*/

/**
* @cfg {String} header Actions column header
*/
,header:''

/**
* @cfg {Boolean} menuDisabled No sense to display header menu for this column
*/
,menuDisabled:true

/**
* @cfg {Boolean} sortable Usually it has no sense to sort by this column
*/
,sortable:false

/**
* @cfg {String} tplGroup Template for group actions
* @private
*/
,tplGroup:
'<tpl for="actions">'
+'<div class="ux-grow-action-item<tpl if="\'right\'===align"> ux-action-right</tpl> '
+'{cls}" style="{style}" qtip="{qtip}">{text}</div>'
+'</tpl>'

/**
* @cfg {String} tplRow Template for row actions
* @private
*/
,tplRow:
'<div class="ux-row-action">'
+'<tpl for="actions">'
+'<div class="ux-row-action-item {cls} <tpl if="text">'
+'ux-row-action-text</tpl>" style="{hide}{style}" qtip="{qtip}">'
+'<tpl if="text"><span qtip="{qtip}">{text}</span></tpl></div>'
+'</tpl>'
+'</div>'

/**
* @private {Number} widthIntercept constant used for auto-width calculation
*/
,widthIntercept:4

/**
* @private {Number} widthSlope constant used for auto-width calculation
*/
,widthSlope:21
// }}}

// methods
// {{{
/**
* Init function
* @param {Ext.grid.GridPanel} grid Grid this plugin is in
*/
,init:function(grid) {
this.grid = grid;

// {{{
// setup template
if(!this.tpl) {
this.tpl = this.processActions(this.actions);

} // eo template setup
// }}}

// calculate width
if(this.autoWidth) {
this.width = this.widthSlope * this.actions.length + this.widthIntercept;
this.fixed = true;
}

// body click handler
var view = grid.getView();
var cfg = {scope:this};
cfg[this.actionEvent] = this.onClick;
grid.on({
render:{scope:this, fn:function() {
view.mainBody.on(cfg);
}}
});


// setup renderer
if(!this.renderer) {
this.renderer = function(value, cell, record, row, col, store) {
cell.css += (cell.css ? ' ' : '') + 'ux-row-action-cell';
return this.tpl.apply(this.getData(value, cell, record, row, col, store));
}.createDelegate(this);
}

// actions in grouping grids support
if(view.groupTextTpl && this.groupActions) {
view.interceptMouse = view.interceptMouse.createInterceptor(function(e) {
if(e.getTarget('.ux-grow-action-item')) {
return false;
}
});
view.groupTextTpl =
'<div class="ux-grow-action-text">' + view.groupTextTpl +'</div>'
+this.processActions(this.groupActions, this.tplGroup).apply()
;
}

} // eo function init
// }}}
// {{{
/**
* Returns data to apply to template. Override this if needed.
* @param {Mixed} value
* @param {Object} cell object to set some attributes of the grid cell
* @param {Ext.data.Record} record from which the data is extracted
* @param {Number} row row index
* @param {Number} col col index
* @param {Ext.data.Store} store object from which the record is extracted
* @returns {Object} data to apply to template
*/
,getData:function(value, cell, record, row, col, store) {
return record.data || {};
} // eo function getData
// }}}
// {{{
/**
* Processes actions configs and returns template.
* @param {Array} actions
* @param {String} template Optional. Template to use for one action item.
* @return {String}
* @private
*/
,processActions:function(actions, template) {
var acts = [];

// actions loop
Ext.each(actions, function(a, i) {
// save callback
if(a.iconCls && 'function' === typeof (a.callback || a.cb)) {
this.callbacks = this.callbacks || {};
this.callbacks[a.iconCls] = a.callback || a.cb;
}

// data for intermediate template
var o = {
cls:a.iconIndex ? '{' + a.iconIndex + '}' : (a.iconCls ? a.iconCls : '')
,qtip:a.qtipIndex ? '{' + a.qtipIndex + '}' : (a.tooltip || a.qtip ? a.tooltip || a.qtip : '')
,text:a.textIndex ? '{' + a.textIndex + '}' : (a.text ? a.text : '')
,hide:a.hideIndex ? '<tpl if="' + a.hideIndex + '">visibility:hidden;</tpl>' : (a.hide ? 'visibility:hidden;' : '')
,align:a.align || 'right'
,style:a.style ? a.style : ''
};
acts.push(o);

}, this); // eo actions loop

var xt = new Ext.XTemplate(template || this.tplRow);
return new Ext.XTemplate(xt.apply({actions:acts}));

} // eo function processActions
// }}}
// {{{
/**
* Grid body actionEvent event handler
* @private
*/
,onClick:function(e, target) {

var view = this.grid.getView();
var action = false;

// handle row action click
var row = e.getTarget('.x-grid3-row');
var col = view.findCellIndex(target.parentNode.parentNode);

var t = e.getTarget('.ux-row-action-item');
if(t) {
action = t.className.replace(/ux-row-action-item /, '');
if(action) {
action = action.replace(/ ux-row-action-text/, '');
action = action.trim();
}
}
if(false !== row && false !== col && false !== action) {
var record = this.grid.store.getAt(row.rowIndex);

// call callback if any
if(this.callbacks && 'function' === typeof this.callbacks[action]) {
this.callbacks[action](this.grid, record, action, row.rowIndex, col);
}

// fire events
if(true !== this.eventsSuspended && false === this.fireEvent('beforeaction', this.grid, record, action, row.rowIndex, col)) {
return;
}
else if(true !== this.eventsSuspended) {
this.fireEvent('action', this.grid, record, action, row.rowIndex, col);
}

}

// handle group action click
t = e.getTarget('.ux-grow-action-item');
if(t) {
// get groupId
var group = view.findGroup(target);
var groupId = group ? group.id.replace(/ext-gen[0-9]+-gp-/, '') : null;

// get matching records
var records;
if(groupId) {
var re = new RegExp(groupId);
records = this.grid.store.queryBy(function(r) {
return r._groupId.match(re);
});
records = records ? records.items : [];
}
action = t.className.replace(/ux-grow-action-item (ux-action-right )*/, '');

// call callback if any
if('function' === typeof this.callbacks[action]) {
this.callbacks[action](this.grid, records, action, groupId);
}

// fire events
if(true !== this.eventsSuspended && false === this.fireEvent('beforegroupaction', this.grid, records, action, groupId)) {
return false;
}
this.fireEvent('groupaction', this.grid, records, action, groupId);
}
} // eo function onClick
// }}}

});

// registre xtype
Ext.reg('rowactions', Ext.ux.grid.RowActions);

// eof

/**
* RowExpander changed from RowExpander.js in the Ext examples and some ideas taken
* from the forum (http://extjs.com/forum/showthread.php?t=21017&page=3).
*
* Override the createExpandingRowPanelItems function to make Ext expanded row content
* (as opposed to using Ext.Template to make the expanded row content).
*
* If config.store is passed in, pass a record for the row from that store instead
* of the grid store into the createExpandingRowPanelItems function.
*/

/*global Ext */

Ext.ns('Ext.ux.grid');
Ext.ux.grid.RowExpander = function(config){
Ext.apply(this, config);

this.state = {};

this.addEvents({
beforeexpand : true,
expand: true,
beforecollapse: true,
collapse: true
});

Ext.ux.grid.RowExpander.superclass.constructor.call(this);
};

Ext.extend(Ext.ux.grid.RowExpander, Ext.util.Observable, {
header: "",
width: 20,
sortable: false,
fixed:true,
dataIndex: '',
id: 'expander',
lazyRender : true,
enableCaching: true,
menuDisabled: true,

getRowClass : function(record, rowIndex, rowParams, ds){
// cols: The column count to apply to the body row's TD colspan attribute (defaults to the current column count of the grid).
rowParams.cols = rowParams.cols-1; // make it the width of the whole row
return this.state[record.id] ? 'x-grid3-row-expanded' : 'x-grid3-row-collapsed';
},


init : function(grid){
this.grid = grid;

var view = grid.getView();
view.getRowClass = this.getRowClass.createDelegate(this);

view.enableRowBody = true;

//recreate component when it was expanded and row was changed, the component must then change as well
view.on("rowupdated", function(gridView, rowIndex, record){
var row = gridView.getRow(rowIndex);
if(Ext.fly(row).hasClass('x-grid3-row-expanded')){
this.expandRow(rowIndex);
}
},this);

//recreate component when it was expanded and grid was refreshed
view.on("refresh", function(grid){
var aRows = this.grid.getView().getRows();
for(var i = 0; i < aRows.length; i++) {
if(Ext.fly(aRows[i]).hasClass('x-grid3-row-expanded')){
this.expandRow(aRows[i].rowIndex);
}
}
/*var panel = this.expandingRowPanel[record.id];
var row = this.grid.view.getRow(panel.rowIndex);

if(panel){
// if using additional store passed in config, pass record from it instead of from the grid store
var rowBody = panel.renderTo;
var rowIndex = panel.rowIndex;
this.state[record.id] = false;
Ext.fly(row).replaceClass('x-grid3-row-expanded', 'x-grid3-row-collapsed');
this.fireEvent('collapse', this, record, rowBody, row.rowIndex);
//this.expandRow(rowIndex);
//this.createExpandingRowPanel(record, rowBody, rowIndex);
}*/
},this);

grid.on('render', function(){
view.mainBody.on( 'mousedown', this.onMouseDown, this );
}, this);

// store
/* this.grid.getStore().on("load", function(store, records, options){
Ext.select('div.x-grid3-row-expanded').replaceClass('x-grid3-row-expanded', 'x-grid3-row-collapsed');
this.state = {};
}, this);
*/
this.store = this.store ? this.store : this.grid.store;
this.store.load(); // load here instead of in beforeExpand cuz that would wipe out additions to store
},

onMouseDown : function( e, t ) {
if(t.className == 'x-grid3-row-expander'){
e.stopEvent();
var row = e.getTarget('.x-grid3-row');
this.toggleRow(row);
}
},

renderer : function(v, p, record){
p.cellAttr = 'rowspan="2"';
return '<div class="x-grid3-row-expander">&#160;</div>';
},

beforeExpand : function(record, rowBody, rowIndex){
var isContinue = true;
if(this.fireEvent('beforeexpand', this.grid, record, rowIndex) !== false){
if(rowBody.innerHTML == '' || !this.enableCaching) {
this.createExpandingRowPanel( record, rowBody, rowIndex );
}
} else {
isContinue = false;
}

return isContinue;
},

toggleRow : function(row){
if(typeof row == 'number'){
row = this.grid.view.getRow(row);
}
this[Ext.fly(row).hasClass('x-grid3-row-collapsed') ? 'expandRow' : 'collapseRow'](row);
},

expandRow : function(row){
if(typeof row == 'number'){
row = this.grid.view.getRow(row);
}
var record = this.store.getAt(row.rowIndex);
// if using additional store passed in config, pass record from it instead of from the grid store
var rowBody = Ext.DomQuery.selectNode('tr:nth(2) div.x-grid3-row-body', row);
if(this.beforeExpand(record, rowBody, row.rowIndex)){

this.state[record.id] = true;
Ext.fly(row).replaceClass('x-grid3-row-collapsed', 'x-grid3-row-expanded');
this.fireEvent('expand', this.grid, record, row.rowIndex);
}
},

collapseRow : function(row){
if(typeof row == 'number'){
row = this.grid.view.getRow(row);
}
var record = this.store.getAt(row.rowIndex);
// if using additional store passed in config, pass record from it instead of from the grid store
var body = Ext.fly(row).child('tr:nth(1) div.x-grid3-row-body', true);
if(this.fireEvent('beforecollapse', this, record, body, row.rowIndex) !== false){
this.state[record.id] = false;
Ext.fly(row).replaceClass('x-grid3-row-expanded', 'x-grid3-row-collapsed');
this.fireEvent('collapse', this, record, body, row.rowIndex);
}
},

// Expand all rows
expandAll : function() {
var aRows = this.grid.getView().getRows();
for(var i = 0; i < aRows.length; i++) {
this.expandRow(aRows[i]);
}
},

// Collapse all rows
collapseAll : function() {
var aRows = this.grid.getView().getRows();
for(var i = 0; i < aRows.length; i++) {
this.collapseRow(aRows[i]);
}
},


createExpandingRowPanel: function( record, rowBody, rowIndex ) {

// record.id is more stable than rowIndex for panel item's key; rows can be deleted.
var panelItemIndex = record.id;
// var panelItemIndex = rowIndex;

// init array of expanding row panels if not already inited
if ( !this.expandingRowPanel ) {
this.expandingRowPanel = [];
}

// Add a new panel to the row body if not already there
if ( this.expandingRowPanel[panelItemIndex] ) {
Ext.destroy(this.expandingRowPanel[panelItemIndex]);
}
this.expandingRowPanel[panelItemIndex] = new Ext.Panel(
{
// title: 'Custom Fields',
layout:'fit', // this doesn't put the labels there
border: false,
bodyBorder: false,
//layout:'form',
renderTo: rowBody,
rowIndex: rowIndex,
items: this.createExpandingRowPanelItems( this.grid, this.store, record, rowIndex )
}
);

//prevents from propagating these events up to parent grid
this.expandingRowPanel[panelItemIndex].getEl().swallowEvent([
'click', 'mousedown','keydown','mouseover',
'contextmenu',
'dblclick'
],true);


}/*,

/**
* Override this method to put Ext form items into the expanding row panel.
* @return Array of panel items.
*
createExpandingRowPanelItems: function( record, rowIndex ) {
var panelItems = new Ext.Panel({
// renderTo: rowBody,
width: '200px',
title: 'My Title',
html: 'My HTML content'
});

return panelItems;
}
*/
});
// registre xtype
Ext.reg('rowexpander', Ext.ux.grid.RowExpander);




// vim: ts=4:sw=4:nu:fdc=4:nospell
/**
* Ext.ux.grid.Search
*
* Search plugin
*
* @author Ing. Jozef Sakalos
* @copyright (c) 2008, by Ing. Jozef Sakalos
* @date 17. January 2008
* @version $Id: ext-ux-grid-search.js,v 1.1 2008/03/06 18:56:02 u0083428 Exp $
*/


Ext.namespace('Ext.ux', 'Ext.ux.grid');

/**
* @constructor
*/
Ext.ux.grid.Search = function(config) {
Ext.apply(this, config);

Ext.ux.grid.Search.superclass.constructor.call(this);
}; // end of constructor

Ext.extend(Ext.ux.grid.Search, Ext.util.Observable, {
// defaults
searchText:'Search'
,searchTipText:'Type a text to search and press Enter'
,position:'bottom'
,iconCls:false//'icon-magnifier'
,checkIndexes:'all'
,disableIndexes:[]
,dateFormat:undefined
,mode:'remote'
,xtype:'gridsearch'
,paramNames: {
fields:'fields'
,query:'query'
}

// {{{
,init:function(grid) {
this.grid = grid;

grid.onRender = grid.onRender.createSequence(this.onRender, this);
grid.reconfigure = grid.reconfigure.createSequence(this.reconfigure, this);
} // end of function init
// }}}
// {{{
,onRender:function() {
var grid = this.grid;
var tb = 'bottom' == this.position ? grid.bottomToolbar : grid.topToolbar;

// add menu
this.menu = new Ext.menu.Menu();
//tb.addSeparator();
tb.add({
text:this.searchText
,menu:this.menu
,iconCls:this.iconCls
});

// add filter field
this.field = new Ext.form.TwinTriggerField({
width:this.width
,selectOnFocus:undefined === this.selectOnFocus ? true : this.selectOnFocus
,trigger1Class:'x-form-clear-trigger'
,trigger2Class:'x-form-search-trigger'
,onTrigger1Click:this.onTriggerClear.createDelegate(this)
,onTrigger2Click:this.onTriggerSearch.createDelegate(this)
});
this.field.on('render', function() {
this.field.el.dom.qtip = this.searchTipText;
var map = new Ext.KeyMap(this.field.el, [{
key:Ext.EventObject.ENTER
,scope:this
,fn:this.onTriggerSearch
},{
key:Ext.EventObject.ESC
,scope:this
,fn:this.onTriggerClear
}]);
map.stopEvent = true;
}, this, {single:true});

tb.add(this.field);

// reconfigure
this.reconfigure();
} // end of function onRender
// }}}
// {{{
,onTriggerClear:function() {
this.field.setValue('');
this.field.focus();
this.onTriggerSearch();
} // end of function onTriggerClear
// }}}
// {{{
,onTriggerSearch:function() {
var val = this.field.getValue();
var store = this.grid.store;

if('local' === this.mode) {
store.clearFilter();
if(val) {
store.filterBy(function(r) {
var retval = false;
this.menu.items.each(function(item) {
if(!item.checked || retval) {
return;
}
var rv = r.get(item.dataIndex);
if(!(rv instanceof Date) || (this.dateFormat || r.fields.get(item.dataIndex).dateFormat)) {
rv = rv instanceof Date ? rv.format(this.dateFormat || r.fields.get(item.dataIndex).dateFormat) : rv;
var re = new RegExp(val, 'gi');
retval = re.test(rv);
}
}, this);
if(retval) {
return true;
}
return retval;
}, this);
}
else {
}
}
else {
// clear start (necessary if we have paging)
if(store.lastOptions && store.lastOptions.params) {
store.lastOptions.params[store.paramNames.start] = 0;
}

// get fields to search array
var fields = [];
this.menu.items.each(function(item) {
if(item.checked) {
fields.push(item.dataIndex);
}
});

// add fields and query to baseParams of store
delete(store.baseParams[this.paramNames.fields]);
delete(store.baseParams[this.paramNames.query]);
if (store.lastOptions && store.lastOptions.params) {
delete(store.lastOptions.params[this.paramNames.fields]);
delete(store.lastOptions.params[this.paramNames.query]);
}
if(fields.length) {
store.baseParams[this.paramNames.fields] = Ext.encode(fields);
store.baseParams[this.paramNames.query] = val;
}

// reload store
store.reload();
}

} // end of function onTriggerSearch
// }}}
// {{{
,setDisabled:function() {
this.field.setDisabled.apply(this.field, arguments);
} // end of function setDisabled
// }}}
// {{{
,enable:function() {
this.setDisabled(false);
} // end of function enable
// }}}
// {{{
,disable:function() {
this.setDisabled(true);
} // end of function disable
// }}}
// {{{
,reconfigure:function() {

// {{{
// remove old items
var menu = this.menu;
menu.removeAll();

// }}}
// {{{
// add new items
var cm = this.grid.colModel;
Ext.each(cm.config, function(config) {
var disable = false;
if(config.header && config.dataIndex) {
Ext.each(this.disableIndexes, function(item) {
disable = disable ? disable : item === config.dataIndex;
});
if(!disable) {
menu.add(new Ext.menu.CheckItem({
text:config.header
,hideOnClick:false
,checked:'all' === this.checkIndexes
,dataIndex:config.dataIndex
}));
}
}
}, this);
// }}}
// {{{
// check items
if(this.checkIndexes instanceof Array) {
Ext.each(this.checkIndexes, function(di) {
var item = menu.items.find(function(itm) {
return itm.dataIndex === di;
});
if(item) {
item.setChecked(true, true);
}
}, this);
}
// }}}


} // end of function reconfigure
// }}}

}); // end of extend
Ext.ns('Ext.ux.grid');

Ext.ux.grid.GridSummary = function(config) {
Ext.apply(this, config);
};

Ext.extend(Ext.ux.grid.GridSummary, Ext.util.Observable, {
init : function(grid) {
this.grid = grid;
this.cm = grid.getColumnModel();
this.view = grid.getView();

var v = this.view;

// override GridView's onLayout() method
v.onLayout = this.onLayout;

v.afterMethod('render', this.refreshSummary, this);
v.afterMethod('refresh', this.refreshSummary, this);
v.afterMethod('syncScroll', this.syncSummaryScroll, this);
v.afterMethod('onColumnWidthUpdated', this.doWidth, this);
v.afterMethod('onAllColumnWidthsUpdated', this.doAllWidths, this);
v.afterMethod('onColumnHiddenUpdated', this.doHidden, this);

// update summary row on store's add/remove/clear/update events
grid.store.on({
add: this.refreshSummary,
remove: this.refreshSummary,
clear: this.refreshSummary,
update: this.refreshSummary,
scope: this
});

if (!this.rowTpl) {
this.rowTpl = new Ext.Template(
'<div class="x-grid3-summary-row x-grid3-gridsummary-row-offset">',
'<table class="x-grid3-summary-table" border="0" cellspacing="0" cellpadding="0" style="{tstyle}">',
'<tbody><tr>{cells}</tr></tbody>',
'</table>',
'</div>'
);
this.rowTpl.disableFormats = true;
}
this.rowTpl.compile();

if (!this.cellTpl) {
this.cellTpl = new Ext.Template(
'<td class="x-grid3-col x-grid3-cell x-grid3-td-{id} {css}" style="{style}">',
'<div class="x-grid3-cell-inner x-grid3-col-{id}" unselectable="on" {attr}>{value}</div>',
"</td>"
);
this.cellTpl.disableFormats = true;
}
this.cellTpl.compile();
},

calculate : function(rs, cm) {
var data = {}, cfg = cm.config;
for (var i = 0, len = cfg.length; i < len; i++) { // loop through all columns in ColumnModel
var cf = cfg[i], // get column's configuration
cname = cf.dataIndex; // get column dataIndex

// initialise grid summary row data for
// the current column being worked on
data[cname] = 0;

if (cf.summaryType) {
for (var j = 0, jlen = rs.length; j < jlen; j++) {
var r = rs[j]; // get a single Record
data[cname] = Ext.ux.grid.GridSummary.Calculations[cf.summaryType](r.get(cname), r, cname, data, j);
}
}
}

return data;
},

onLayout : function(vw, vh) {
if (Ext.type(vh) != 'number') { // handles grid's height:'auto' config
return;
}
// note: this method is scoped to the GridView
if (!this.grid.getGridEl().hasClass('x-grid-hide-gridsummary')) {
// readjust gridview's height only if grid summary row is visible
this.scroller.setHeight(vh - this.summary.getHeight());
}
},

syncSummaryScroll : function() {
var mb = this.view.scroller.dom;

this.view.summaryWrap.dom.scrollLeft = mb.scrollLeft;
this.view.summaryWrap.dom.scrollLeft = mb.scrollLeft; // second time for IE (1/2 time first fails, other browsers ignore)
},

doWidth : function(col, w, tw) {
var s = this.view.summary.dom;

s.firstChild.style.width = tw;
s.firstChild.rows[0].childNodes[col].style.width = w;
},

doAllWidths : function(ws, tw) {
var s = this.view.summary.dom, wlen = ws.length;

s.firstChild.style.width = tw;

var cells = s.firstChild.rows[0].childNodes;

for (var j = 0; j < wlen; j++) {
cells[j].style.width = ws[j];
}
},

doHidden : function(col, hidden, tw) {
var s = this.view.summary.dom,
display = hidden ? 'none' : '';

s.firstChild.style.width = tw;
s.firstChild.rows[0].childNodes[col].style.display = display;
},

renderSummary : function(o, cs, cm) {
cs = cs || this.view.getColumnData();
var cfg = cm.config,
buf = [],
last = cs.length - 1;

for (var i = 0, len = cs.length; i < len; i++) {
var c = cs[i], cf = cfg[i], p = {};

p.id = c.id;
p.style = c.style;
p.css = i == 0 ? 'x-grid3-cell-first ' : (i == last ? 'x-grid3-cell-last ' : '');

if (cf.summaryType || cf.summaryRenderer) {
//p.value = (cf.summaryRenderer || c.renderer)(o.data[c.name], p, o);
p.value = (cf.summaryRenderer || c.renderer)(o.data[c.name], p, new Ext.data.Record(o.data), -1, i, this.grid.store);
} else {
p.value = '';
}
if (p.value == undefined || p.value === "") p.value = "&#160;";
buf[buf.length] = this.cellTpl.apply(p);
}

return this.rowTpl.apply({
tstyle: 'width:' + this.view.getTotalWidth() + ';',
cells: buf.join('')
});
},

refreshSummary : function() {
var g = this.grid, ds = g.store,
cs = this.view.getColumnData(),
cm = this.cm,
rs = ds.getRange(),
data = this.calculate(rs, cm),
buf = this.renderSummary({data: data}, cs, cm);

if (!this.view.summaryWrap) {
this.view.summaryWrap = Ext.DomHelper.insertAfter(this.view.scroller, {
tag: 'div',
cls: 'x-grid3-gridsummary-row-inner'
}, true);
}
this.view.summary = this.view.summaryWrap.update(buf).first();
},

toggleSummary : function(visible) { // true to display summary row
var el = this.grid.getGridEl();

if (el) {
if (visible === undefined) {
visible = el.hasClass('x-grid-hide-gridsummary');
}
el[visible ? 'removeClass' : 'addClass']('x-grid-hide-gridsummary');

this.view.layout(); // readjust gridview height
}
},

getSummaryNode : function() {
return this.view.summary
}
});
Ext.reg('gridsummary', Ext.ux.grid.GridSummary);

/*
* all Calculation methods are called on each Record in the Store
* with the following 5 parameters:
*
* v - cell value
* record - reference to the current Record
* colName - column name (i.e. the ColumnModel's dataIndex)
* data - the cumulative data for the current column + summaryType up to the current Record
* rowIdx - current row index
*/
Ext.ux.grid.GridSummary.Calculations = {
sum : function(v, record, colName, data, rowIdx) {
return data[colName] + Ext.num(v, 0);
},


count : function(v, record, colName, data, rowIdx) {
return rowIdx + 1;
},

max : function(v, record, colName, data, rowIdx) {
return Math.max(Ext.num(v, 0), data[colName]);
},

min : function(v, record, colName, data, rowIdx) {
return Math.min(Ext.num(v, 0), data[colName]);
},

average : function(v, record, colName, data, rowIdx) {
var t = data[colName] + Ext.num(v, 0), count = record.store.getCount();
return rowIdx == count - 1 ? (t / count) : t;
}
}
/*
* Ext.ux.grid.BufferedGridDragZone V0.1
* Copyright(c) 2007, http://www.siteartwork.de
*
* Licensed under the terms of the Open Source LGPL 3.0
* http://www.gnu.org/licenses/lgpl.html
*
* @author Thorsten Suckow-Homberg <ts@siteartwork.de>
*/

// private
// This is a support class used internally by the Grid components

Ext.namespace('Ext.ux.grid');

Ext.ux.grid.BufferedGridDragZone = function(grid, config){



this.view = grid.getView();
Ext.ux.grid.BufferedGridDragZone.superclass.constructor.call(this, this.view.mainBody.dom, config);

// this.addEvents({
// 'startdrag' : true
//});

if(this.view.lockedBody){
this.setHandleElId(Ext.id(this.view.mainBody.dom));
this.setOuterHandleElId(Ext.id(this.view.lockedBody.dom));
}
this.scroll = false;
this.grid = grid;
this.ddel = document.createElement('div');
this.ddel.className = 'x-grid-dd-wrap';

this.view.ds.on('beforeselectionsload', this.onBeforeSelectionsLoad, this);
this.view.ds.on('selectionsload', this.onSelectionsLoad, this);
};

Ext.extend(Ext.ux.grid.BufferedGridDragZone, Ext.dd.DragZone, {
ddGroup : "GridDD",

isDropValid : true,

getDragData : function(e)
{
var t = Ext.lib.Event.getTarget(e);
var rowIndex = this.view.findRowIndex(t);
if(rowIndex !== false){
var sm = this.grid.selModel;
if(!sm.isSelected(rowIndex) || e.hasModifier()){
sm.handleMouseDown(this.grid, rowIndex, e);
}
return {grid: this.grid, ddel: this.ddel, rowIndex: rowIndex, selections:sm.getSelections()};
}
return false;
},

onInitDrag : function(e)
{
this.view.ds.loadSelections(this.grid.selModel.getPendingSelections(true));

var data = this.dragData;
this.ddel.innerHTML = this.grid.getDragDropText();
this.proxy.update(this.ddel);
// fire start drag?
},

onBeforeSelectionsLoad : function()
{
this.isDropValid = false;
Ext.fly(this.proxy.el.dom.firstChild).addClass('x-dd-drop-waiting');
},

onSelectionsLoad : function()
{
this.isDropValid = true;
this.ddel.innerHTML = this.grid.getDragDropText();
Ext.fly(this.proxy.el.dom.firstChild).removeClass('x-dd-drop-waiting');
},

afterRepair : function()
{
this.dragging = false;
},

getRepairXY : function(e, data)
{
return false;
},

onStartDrag : function()
{

},

onEndDrag : function(data, e)
{
// fire end drag?
},

onValidDrop : function(dd, e, id)
{
// fire drag drop?
this.hideProxy();
},

beforeInvalidDrop : function(e, id)
{

}
});
/*
* Ext.ux.BufferedGridToolbar V1.0
* Copyright(c) 2007, http://www.siteartwork.de
*
* Licensed under the terms of the Open Source LGPL 3.0
* http://www.gnu.org/licenses/lgpl.html
*
*
* @author Thorsten Suckow-Homberg <ts@siteartwork.de>
*/

/**
* @class Ext.ux.BufferedGridToolbar
* @extends Ext.Toolbar
* A specialized toolbar that is bound to a {@link Ext.ux.grid.BufferedGridView}
* and provides information about the indexes of the requested data and the buffer
* state.
* @constructor
* @param {Object} config
*/
Ext.namespace('Ext.ux');

Ext.ux.BufferedGridToolbar = Ext.extend(Ext.Toolbar, {
/**
* @cfg {Boolean} displayInfo
* True to display the displayMsg (defaults to false)
*/

/**
* @cfg {String} displayMsg
* The paging status message to display (defaults to "Displaying {start} - {end} of {total}")
*/
displayMsg : 'Displaying {0} - {1} of {2}',

/**
* @cfg {String} emptyMsg
* The message to display when no records are found (defaults to "No data to display")
*/
emptyMsg : 'No data to display',

/**
* Value to display as the tooltip text for the refresh button. Defaults to
* "Refresh"
* @param {String}
*/
refreshText : "Refresh",

initComponent : function()
{
Ext.ux.BufferedGridToolbar.superclass.initComponent.call(this);
this.bind(this.view);
},

// private
updateInfo : function(rowIndex, visibleRows, totalCount)
{
if(this.displayEl){
var msg = totalCount == 0 ?
this.emptyMsg :
String.format(this.displayMsg, rowIndex+1,
rowIndex+visibleRows, totalCount);
this.displayEl.update(msg);
}
},

/**
* Unbinds the toolbar from the specified {@link Ext.ux.grid.BufferedGridView}
* @param {@link Ext.ux.grid.BufferedGridView} view The view to unbind
*/
unbind : function(view)
{
view.un('rowremoved', this.onRowRemoved, this);
view.un('rowsinserted', this.onRowsInserted, this);
view.un('beforebuffer', this.beforeBuffer, this);
view.un('cursormove', this.onCursorMove, this);
view.un('buffer', this.onBuffer, this);
this.view = undefined;
},


/**
* Binds the toolbar to the specified {@link Ext.ux.grid.BufferedGridView}
* @param {Ext.grid.GridPanel} view The view to bind
*/
bind : function(view)
{
view.on('rowremoved', this.onRowRemoved, this);
view.on('rowsinserted', this.onRowsInserted, this);
view.on('beforebuffer', this.beforeBuffer, this);
view.on('cursormove', this.onCursorMove, this);
view.on('buffer', this.onBuffer, this);
this.view = view;
},

// ----------------------------------- Listeners -------------------------------
onCursorMove : function(view, rowIndex, visibleRows, totalCount)
{
this.updateInfo(rowIndex, visibleRows, totalCount);
},

// private
onRowsInserted : function(view, start, end)
{
this.updateInfo(view.rowIndex, Math.min(view.ds.totalLength, view.visibleRows),
view.ds.totalLength);
},

// private
onRowRemoved : function(view, index, record)
{
this.updateInfo(view.rowIndex, Math.min(view.ds.totalLength, view.visibleRows),
view.ds.totalLength);
},

// private
beforeBuffer : function(view, store, rowIndex, visibleRows, totalCount)
{
this.loading.disable();
this.updateInfo(rowIndex, visibleRows, totalCount);
},

// private
onBuffer : function(view, store, rowIndex, visibleRows, totalCount)
{
this.loading.enable();
this.updateInfo(rowIndex, visibleRows, totalCount);
},

// private
onClick : function(type)
{
switch (type) {
case 'refresh':
this.view.reset(true);
break;

}
},

// private
onRender : function(ct, position)
{
Ext.PagingToolbar.superclass.onRender.call(this, ct, position);

this.loading = this.addButton({
tooltip : this.refreshText,
iconCls : "x-tbar-loading",
handler : this.onClick.createDelegate(this, ["refresh"])
});

this.addSeparator();

if(this.displayInfo){
this.displayEl = Ext.fly(this.el.dom).createChild({cls:'x-paging-info'});
}
}
});
/*
* Ext.ux.grid.BufferedGridView V0.1
* Copyright(c) 2007, http://www.siteartwork.de
*
* Licensed under the terms of the Open Source LGPL 3.0
* http://www.gnu.org/licenses/lgpl.html
*
* @author Thorsten Suckow-Homberg <ts@siteartwork.de>
*/

/**
* @class Ext.ux.grid.BufferedGridView
* @extends Ext.grid.GridView
*
*
* @constructor
* @param {Object} config
*/
Ext.namespace('Ext.ux.grid');
Ext.ux.grid.BufferedGridView = function(config) {

this.addEvents({
/**
* @event beforebuffer
* Fires when the store is about to buffer new data.
* @param {Ext.ux.BufferedGridView} this
* @param {Ext.data.Store} store The store
* @param {Number} rowIndex
* @param {Number} visibleRows
* @param {Number} totalCount
*/
'beforebuffer' : true,
/**
* @event buffer
* Fires when the store is finsihed buffering new data.
* @param {Ext.ux.BufferedGridView} this
* @param {Ext.data.Store} store The store
* @param {Number} rowIndex
* @param {Number} visibleRows
* @param {Number} totalCount
*/
'buffer' : true,
/**
* @event cursormove
* Fires when the the user scrolls through the data.
* @param {Ext.ux.BufferedGridView} this
* @param {Number} rowIndex The index of the first visible row in the
* grid absolute to it's position in the model.
* @param {Number} visibleRows The number of rows visible in the grid.
* @param {Number} totalCount
*/
'cursormove' : true

});

/**
* @cfg {Number} bufferSize The number of records that will at least always
* be available in the store for rendering. This value will be send to the
* server as the <tt>limit</tt> parameter and should not change during the
* lifetime of a grid component. Note: In a paging grid, this number would
* indicate the page size.
* The value should be set high enough to make a userfirendly scrolling
* possible and should be greater than the sum of {nearLimit} and
* {visibleRows}. Usually, a value in between 150 and 200 is good enough.
* A lesser value will more often make the store re-request new data, while
* a larger number will make loading times higher.
*/

/**
* @cfg {Number} nearLimit This value represents a near value that is responsible
* for deciding if a request for new data is needed. The lesser the number, the
* more often new data will be requested. The number should be set to a value
* that lies in between 1/4 to 1/2 of the {bufferSize}.
*/

/**
* @cfg {Number} horizontalScrollOffset The height of a horizontal aligned
* scrollbar. The scrollbar is shown if the total width of all visible
* columns exceeds the width of the grid component.
* On Windows XP (IE7, FF2), this value defaults to 16.
*/
this.horizontalScrollOffset = 16;

/**
* @cfg {Object} loadMaskConfig The config of the load mask that will be shown
* by the view if a request for new data is underway.
*/
this.loadMask = false;

Ext.apply(this, config);

/**
* The array represents the range of rows available in the buffer absolute to
* the indexes of the data model.
* @param {Array}
*/
this.bufferRange = [0, -1];

this.templates = {};
/**
* The master template adds an addiiotnal scrollbar to make cursoring in the
* data possible.
*/
this.templates.master = new Ext.Template(
'<div class="x-grid3" hidefocus="true"><div style="z-index:2000;background:none;position:relative;height:321px; float:right; width: 18px;overflow: scroll;"><div style="background:none;width:1px;overflow:hidden;font-size:1px;height:0px;"></div></div>',
'<div class="x-grid3-viewport" style="float:left">',
'<div class="x-grid3-header"><div class="x-grid3-header-inner"><div class="x-grid3-header-offset">{header}</div></div><div class="x-clear"></div></div>',
'<div class="x-grid3-scroller" style="overflow-y:hidden !important;"><div class="x-grid3-body" style="position:relative;">{body}</div><a href="#" class="x-grid3-focus" tabIndex="-1"></a></div>',
"</div>",
'<div class="x-grid3-resize-marker">&#160;</div>',
'<div class="x-grid3-resize-proxy">&#160;</div>',
"</div>"
);

Ext.ux.grid.BufferedGridView.superclass.constructor.call(this);
};


Ext.extend(Ext.ux.grid.BufferedGridView, Ext.grid.GridView, {

// {{{ --------------------------properties-------------------------------------
/**
* This is the actual y-scroller that does control sending request to the server
* based upon the position of the scrolling cursor.
* @param {Ext.Element}
*/
liveScroller : null,

/**
* This is the panel that represents the amount of data in a given repository.
* The height gets computed via the total amount of records multiplied with
* the fixed(!) row height
* @param {native HTMLObject}
*/
liveScrollerInset : null,

/**
* The <b>fixed</b> row height for <b>every</b> row in the grid. The value is
* computed once the store has been loaded for the first time and used for
* various calculations during the lifetime of the grid component, such as
* the height of the scroller and the number of visible rows.
* @param {Number}
*/
rowHeight : -1,

/**
* Stores the number of visible rows that have to be rendered.
* @param {Number}
*/
visibleRows : 1,

/**
* Stores the last offset relative to a previously scroll action. This is
* needed for deciding wether the user scrolls up or down.
* @param {Number}
*/
lastIndex : -1,

/**
* Stores the last visible row at position "0" in the table view before
* a new scroll event was created and fired.
* @param {Number}
*/
lastRowIndex : 0,

/**
* Stores the value of the <tt>liveScroller</tt>'s <tt>scrollTop</tt> DOM
* property.
* @param {Number}
*/
lastScrollPos : 0,

/**
* The current index of the row in the model that is displayed as the first
* visible row in the view.
* @param {Number}
*/
rowIndex : 0,

/**
* Set to <tt>true</tt> if the store is busy with loading new data.
* @param {Boolean}
*/
isBuffering : false,

/**
* If a request for new data was made and the user scrolls to a new position
* that lays not within the requested range of the new data, the queue will
* hold the latest requested position. If the buffering succeeds and the value
* of requestQueue is not within the range of the current buffer, data may be
* re-requested.
*
* @param {Number}
*/
requestQueue : -1,

/**
* The view's own load mask that will be shown when a request to data was made
* and there are no rows in the buffer left to render.
* @see {loadMaskConfig}
* @param {Ext.LoadMask}
*/
loadMask : null,

/**
* Set to <tt>true</tt> if a request for new data has been made while there
* are still rows in the buffer that can be rendered before the request
* finishes.
* @param {Boolean}
*/
isPrebuffering : false,
// }}}


// {{{ --------------------------public API methods-----------------------------

/**
* Resets the view to display the first row in the data model. This will
* change the scrollTop property of the scroller and may trigger a request
* to buffer new data, if the row index "0" is not within the buffer range and
* forceReload is set to true.
*
* @param {Boolean} forceReload <tt>true</tt> to reload the buffers contents,
* othwerwise <tt>false</tt>
*/
reset : function(forceReload)
{
if (forceReload === false) {
this.ds.modified = [];
this.grid.selModel.clearSelections(true);
this.rowIndex = 0;
this.lastScrollPos = 0;
this.lastRowIndex = 0;
this.lastIndex = 0;
this.bufferRange = [0, this.ds.bufferSize];
this.adjustScrollerPos(-this.liveScroller.dom.scrollTop, true);
this.showLoadMask(false);
this.refresh(true);
//this.replaceLiveRows(0, true);
this.fireEvent('cursormove', this, 0,
Math.min(this.ds.totalLength, this.visibleRows),
this.ds.totalLength);
} else {

var params = {
start : 0,
limit : this.ds.bufferSize
};

var sInfo = this.ds.sortInfo;

if (sInfo) {
params.dir = sInfo.direction;
params.sort = sInfo.field;
}

this.ds.load({
callback : function(){this.reset(false);},
scope : this,
params : params
});
}

},

// {{{ ------------adjusted methods for applying custom behavior----------------
/**
* Overwritten so the {@link Ext.ux.grid.BufferedGridDragZone} can be used
* with this view implementation.
*
* Since detaching a previously created DragZone from a grid panel seems to
* be impossible, a little workaround will tell the parent implementation
* that drad/drop is not enabled for this view's grid, and right after that
* the custom DragZone will be created, if neccessary.
*/
renderUI : function()
{
var g = this.grid;
var dEnabled = g.enableDragDrop || g.enableDrag;

g.enableDragDrop = false;
g.enableDrag = false;

Ext.ux.grid.BufferedGridView.superclass.renderUI.call(this);

var g = this.grid;

g.enableDragDrop = dEnabled;
g.enableDrag = dEnabled;

if(dEnabled){
var dd = new Ext.ux.grid.BufferedGridDragZone(g, {
ddGroup : g.ddGroup || 'GridDD'
});
}


if (this.loadMask) {
this.loadMask = new Ext.LoadMask(
this.mainBody.dom.parentNode.parentNode,
this.loadMask
);
}
},

/**
* The extended implementation attaches an listener to the beforeload
* event of the store of the grid. It is guaranteed that the listener will
* only be executed upon reloading of the store, sorting and initial loading
* of data. When the store does "buffer", all events are suspended and the
* beforeload event will not be triggered.
*
* @param {Ext.grid.GridPanel} grid The grid panel this view is attached to
*/
init: function(grid)
{
Ext.ux.grid.BufferedGridView.superclass.init.call(this, grid);

this.bufferRange = [0, this.ds.bufferSize];
this.ds.on('beforeload', this.onBeforeLoad, this);
},


/**
* Only render the viewable rect of the table. The number of rows visible to
* the user is defined in <tt>visibleRows</tt>.
* This implementation does completely overwrite the parent's implementation.
*/
// private
renderBody : function()
{
var markup = this.renderRows(0, this.visibleRows-1);
return this.templates.body.apply({rows: markup});
},

/**
* Inits the DOM native elements for this component.
* The properties <tt>liveScroller</tt> and <tt>liveScrollerInset</tt> will
* be respected as provided by the master template.
* The <tt>scroll</tt> listener for the <tt>liverScroller</tt> will also be
* added here as the <tt>mousewheel</tt> listener.
* This method overwrites the parents implementation.
*/
// private
initElements : function()
{
var E = Ext.Element;

var el = this.grid.getGridEl().dom.firstChild;
var cs = el.childNodes;

this.el = new E(el);

this.mainWrap = new E(cs[1]);

// liveScroller and liveScrollerInset
this.liveScroller = new E(cs[0]);
this.liveScrollerInset = this.liveScroller.dom.firstChild;
this.liveScroller.on('scroll', this.onLiveScroll, this);

this.mainHd = new E(this.mainWrap.dom.firstChild);
this.innerHd = this.mainHd.dom.firstChild;
this.scroller = new E(this.mainWrap.dom.childNodes[1]);
if(this.forceFit){
this.scroller.setStyle('overflow-x', 'hidden');
}
this.mainBody = new E(this.scroller.dom.firstChild);

// addd the mousewheel event to the table's body
this.mainBody.on('mousewheel', this.handleWheel, this);

this.focusEl = new E(this.scroller.dom.childNodes[1]);
this.focusEl.swallowEvent("click", true);

this.resizeMarker = new E(cs[2]);
this.resizeProxy = new E(cs[3]);

},

/**
* Layouts the grid's view taking the scroller into account. The height
* of the scroller gets adjusted depending on the total width of the columns.
* The width of the grid view will be adjusted so the header and the rows do
* not overlap the scroller.
* This method will also compute the row-height based on the first row this
* grid displays and will adjust the number of visible rows if a resize
* of the grid component happened.
* This method overwrites the parents implementation.
*/
//private
layout : function()
{
if(!this.mainBody){
return; // not rendered
}
var g = this.grid;
var c = g.getGridEl(), cm = this.cm,
expandCol = g.autoExpandColumn,
gv = this;

var csize = c.getSize(true);

// set vw to 19 to take scrollbar width into account!
var vw = csize.width-this.scrollOffset;

if(vw < 20 || csize.height < 20){ // display: none?
return;
}

if(g.autoHeight){
this.scroller.dom.style.overflow = 'visible';
}else{
this.el.setSize(csize.width, csize.height);


var hdHeight = this.mainHd.getHeight();
var vh = csize.height - (hdHeight);

this.scroller.setSize(vw, vh);
if(this.innerHd){
this.innerHd.style.width = (vw)+'px';
}
}

if(this.forceFit){
if(this.lastViewWidth != vw){
this.fitColumns(false, false);
this.lastViewWidth = vw;
}
}else {
this.autoExpand();
}


// adjust the number of visible rows and the height of the scroller.
this.adjustVisibleRows();
this.adjustBufferInset();


this.onLayout(vw, vh);
},

// {{{ ----------------------dom/mouse listeners--------------------------------
/**
* Called when a column width has been updated. Adjusts the scroller height
* and the number of visible rows wether the horizontal scrollbar is shown
* or not.
*/
onColumnWidthUpdated : function(col, w, tw)
{
this.adjustVisibleRows();
this.adjustBufferInset();
},

/**
* Called when the width of all columns has been updated. Adjusts the scroller
* height and the number of visible rows wether the horizontal scrollbar is shown
* or not.
*/
onAllColumnWidthsUpdated : function(ws, tw)
{
this.adjustVisibleRows();
this.adjustBufferInset();
},

/**
* Callback for selecting a row. The index of the row is the absolute index
* in the datamodel and gets translated to the index in the view.
* Overwrites the parent's implementation.
*/
// private
onRowSelect : function(row)
{
if (row < this.rowIndex || row > this.rowIndex+this.visibleRows) {
return;
}

var viewIndex = row-this.rowIndex;

this.addRowClass(viewIndex, "x-grid3-row-selected");
},

/**
* Callback for deselecting a row. The index of the row is the absolute index
* in the datamodel and gets translated to the index in the view.
* Overwrites the parent's implementation.
*/
// private
onRowDeselect : function(row)
{
if (row < this.rowIndex || row > this.rowIndex+this.visibleRows) {
return;
}

var viewIndex = row-this.rowIndex;

this.removeRowClass(viewIndex, "x-grid3-row-selected");
},

/**
* Callback for selecting a cell. The index of the row is the absolute index
* in the datamodel and gets translated to the index in the view.
* Overwrites the parent's implementation.
*/
// private
onCellSelect : function(row, col)
{
if (row < this.rowIndex || row > this.rowIndex+this.visibleRows) {
return;
}

var viewIndex = row-this.rowIndex;

var cell = this.getCell(viewIndex, col);
if(cell){
this.fly(cell).addClass("x-grid3-cell-selected");
}
},

/**
* Callback for deselecting a cell. The index of the row is the absolute index
* in the datamodel and gets translated to the index in the view.
* Overwrites the parent's implementation.
*/
// private
onCellDeselect : function(row, col)
{
if (row < this.rowIndex || row > this.rowIndex+this.visibleRows) {
return;
}

var viewIndex = row-this.rowIndex;

var cell = this.getCell(viewIndex, col);
if(cell){
this.fly(cell).removeClass("x-grid3-cell-selected");
}
},

/**
* Callback for onmouseover event of the grid's rows. The index of the row is
* the absolute index in the datamodel and gets translated to the index in the
* view.
* Overwrites the parent's implementation.
*/
// private
onRowOver : function(e, t)
{
var row;
if((row = this.findRowIndex(t)) !== false){
var viewIndex = row-this.rowIndex;
this.addRowClass(viewIndex, "x-grid3-row-over");
}
},

/**
* Callback for onmouseout event of the grid's rows. The index of the row is
* the absolute index in the datamodel and gets translated to the index in the
* view.
* Overwrites the parent's implementation.
*/
// private
onRowOut : function(e, t)
{
var row;
if((row = this.findRowIndex(t)) !== false && row !== this.findRowIndex(e.getRelatedTarget())){
var viewIndex = row-this.rowIndex;
this.removeRowClass(viewIndex, "x-grid3-row-over");
}
},


// {{{ ----------------------data listeners-------------------------------------
/**
* Called when the buffer gets cleared. Simply calls the updateLiveRows method
* with the adjusted index and should force the store to reload
*/
// private
onClear : function()
{
this.reset(false);//var newIndex = Math.max(this.bufferRange[0] - this.visibleRows, 0);
//this.updateLiveRows(newIndex, true, true);
},


/**
* Callback for the underlying store's remove method. The current
* implementation does only remove the selected row which record is in the
* current store.
*
*/
// private
onRemove : function(ds, record, index, isUpdate)
{
if (index == Number.MIN_VALUE || index == Number.MAX_VALUE) {
this.fireEvent("beforerowremoved", this, index, record);
this.fireEvent("rowremoved", this, index, record);
this.adjustBufferInset();
return;
}
var viewIndex = index + this.bufferRange[0];
if(isUpdate !== true){
this.fireEvent("beforerowremoved", this, viewIndex, record);
}

var domLength = this.getRows().length;

if (viewIndex < this.rowIndex) {
// if the according row is not displayed within the visible rect of
// the grid, just adjust the row index and the liveScroller
this.rowIndex--;
this.lastRowIndex = this.rowIndex;
this.adjustScrollerPos(-this.rowHeight, true);
this.fireEvent('cursormove', this, this.rowIndex,
Math.min(this.ds.totalLength, this.visibleRows),
this.ds.totalLength);

} else if (viewIndex >= this.rowIndex && viewIndex < this.rowIndex+domLength) {

var lastPossibleRIndex = this.rowIndex-this.bufferRange[0]+this.visibleRows;

var cInd = viewIndex-this.rowIndex;
var rec = this.ds.getAt(lastPossibleRIndex);

// did we reach the end of the buffer range?
if (rec == null) {
// are there more records we could use? send a buffer request
if (this.ds.totalLength > this.rowIndex+this.visibleRows) {
if(isUpdate !== true){
this.fireEvent("rowremoved", this, viewIndex, record);
}
this.updateLiveRows(this.rowIndex, true, true);
return;
} else {
// no more records, neither in the underlying data model
// nor in the data store
if (this.rowIndex == 0) {
// simply remove the row from the dom
this.removeRows(cInd, cInd);
} else {
// scroll a new row in the rect so the whole rect is filled
// with rows
this.rowIndex--;
if (this.rowIndex < this.bufferRange[0]) {
// buffer range is invalid! request new data
if(isUpdate !== true){
this.fireEvent("rowremoved", this, viewIndex, record);
}
this.updateLiveRows(this.rowIndex);
return;
} else {
// still records in the store, simply update the dom
this.replaceLiveRows(this.rowIndex);
}
}
}
} else {

// the record is right within the visible rect of the grid.
// remove the row that represents the record and append another
// record from the store
this.removeRows(cInd, cInd);
var html = this.renderRows(lastPossibleRIndex, lastPossibleRIndex);
Ext.DomHelper.insertHtml('beforeEnd', this.mainBody.dom, html);
}
}

// a record within the bufferrange was removed, so adjust the buffer
// range
this.bufferRange[1]--;
this.adjustBufferInset();

if(isUpdate !== true){
this.fireEvent("rowremoved", this, viewIndex, record);
}

this.processRows(0, undefined, true);
},


/**
* The callback for the underlying data store when new data was added.
* If <tt>index</tt> equals to <tt>Number.MIN_VALUE</tt> or <tt>Number.MAX_VALUE</tt>, the
* method can't tell at which position in the underlying data model the
* records where added. However, if <tt>index</tt> equals to <tt>Number.MIN_VALUE</tt>,
* the <tt>rowIndex</tt> property will be adjusted to <tt>rowIndex+records.length</tt>,
* and the <tt>liveScroller</tt>'s properties get adjusted so it matches the
* new total number of records of the underlying data model.
* The same will happen to any records that get added at the store index which
* is currently represented by the first visible row in the view.
* Any other value will cause the method to compute the number of rows that
* have to be (re-)painted and calling the <tt>insertRows</tt> method, if
* neccessary.
*
* This method triggers the <tt>beforerowsinserted</tt> and <tt>rowsinserted</tt>
* event, passing the indexes of the records as they may default to the
* positions in the underlying data model. However, due to the fact that
* any sort algorithm may have computed the indexes of the records, it is
* not guaranteed that the computed indexes equal to the indexes of the
* underlying data model.
*
* @param {Ext.ux.grid.BufferedStore} ds The datastore that buffers records
* from the underlying data model
* @param {Array} records An array containing the newly added
* {@link Ext.data.Record}s
* @param {Number} index The index of the position in the underlying
* {@link Ext.ux.grid.BufferedStore} where the rows
* were added.
*/
// private
onAdd : function(ds, records, index)
{
var recordLen = records.length;

// values of index which equal to Number.MIN_VALUE or Number.MAX_VALUE
// indicate that the records were not added to the store. The component
// does not know which index those records do have in the underlying
// data model
if (index == Number.MAX_VALUE || index == Number.MIN_VALUE) {
this.fireEvent("beforerowsinserted", this, index, index);

// if index equals to Number.MIN_VALUE, shift rows!
if (index == Number.MIN_VALUE) {

this.rowIndex = this.rowIndex + recordLen;
this.lastRowIndex = this.rowIndex;
this.bufferRange[0] += recordLen;
this.bufferRange[1] += recordLen;

this.adjustBufferInset();
this.adjustScrollerPos(this.rowHeight*recordLen, true);

this.fireEvent("rowsinserted", this, index, index);
this.processRows();
// the cursor did virtually move
this.fireEvent('cursormove', this, this.rowIndex,
Math.min(this.ds.totalLength, this.visibleRows),
this.ds.totalLength);

return;
}
this.adjustBufferInset();
this.fireEvent("rowsinserted", this, index, index);
return;
}

// only insert the rows which affect the current view.
var start = index+this.bufferRange[0];
var end = start + (recordLen-1);
var len = this.getRows().length;

var firstRow = 0;
var lastRow = 0;

// rows would be added at the end of the rows which are currently
// displayed, so fire the evnt and return
if (index >= (this.rowIndex-this.bufferRange[0])+len && len == this.visibleRows) {
this.fireEvent("beforerowsinserted", this, start, end);
this.fireEvent("rowsinserted", this, start, end);

this.adjustVisibleRows();
this.adjustBufferInset();

}

// rows get added before the first row in the view
else if (len == this.visibleRows && index <= this.rowIndex-this.bufferRange[0]) {
this.fireEvent("beforerowsinserted", this, start, end);
this.liveScroller.un('scroll', this.onLiveScroll, this);
this.rowIndex += recordLen;
this.lastRowIndex = this.rowIndex;

this.adjustVisibleRows();
this.adjustBufferInset();

this.adjustScrollerPos(this.rowHeight*recordLen, true);

this.fireEvent("rowsinserted", this, start, end);
this.processRows();
this.fireEvent('cursormove', this, this.rowIndex,
Math.min(this.ds.totalLength, this.visibleRows),
this.ds.totalLength);
}

// rows get added somewhere IN the current view
else if ((len < this.visibleRows ) || index > this.rowIndex-this.bufferRange[0]) {
firstRow = index;
lastRow = Math.min(end, this.rowIndex+this.visibleRows-1) - this.bufferRange[0];
this.insertRows(ds, firstRow, lastRow);

this.adjustVisibleRows();
this.adjustBufferInset();

}




},

// {{{ ----------------------store listeners------------------------------------
/**
* This callback for the store's "beforeload" event will adjust the start
* position and the limit of the data in the model to fetch. It is guaranteed
* that this method will only be called when the store initially loads,
* remeote-sorts or reloads.
* All other load events will be suspended when the view requests buffer data.
* See {updateLiveRows}.
*
* @param {Ext.data.Store} store The store the Grid Panel uses
* @param {Object} options The configuration object for the proxy that loads
* data from the server
*/
onBeforeLoad : function(store, options)
{
if (!options.params) {
options.params = {start : 0, limit : this.ds.bufferSize};
} else {
options.params.start = 0;
options.params.limit = this.ds.bufferSize;
}

options.scope = this;
options.callback = function(){this.reset(false);};

return true;
},

/**
* Method is used as a callback for the load-event of the attached data store.
* Adjusts the buffer inset based upon the <tt>totalCount</tt> property
* returned by the response.
* Overwrites the parent's implementation.
*/
onLoad : function(o1, o2, options)
{
this.adjustBufferInset();
},

/**
* This will be called when the data in the store has changed, i.e. a
* re-buffer has occured. If the table was not rendered yet, a call to
* <tt>refresh</tt> will initially render the table, which DOM elements will
* then be used to re-render the table upon scrolling.
*
*/
// private
onDataChange : function(store)
{
this.updateHeaderSortState();
},

/**
* A callback for the store when new data has been buffered successfully.
* If the current row index is not within the range of the newly created
* data buffer or another request to new data has been made while the store
* was loading, new data will be re-requested.
*
* Additionally, if there are any rows that have been selected which were not
* in the data store, the method will request the pending selections from
* the grid's selection model and add them to the selections if available.
* This is because the component assumes that a user who scrolls through the
* rows and updates the view's buffer during scrolling, can check the selected
* rows which come into the view for integrity. It is up to the user to
* deselect those rows not matchuing the selection.
* Additionally, if the version of the store changes during various requests
* and selections are still pending, the versionchange event of the store
* can delete the pending selections after a re-bufer happened and before this
* method was called.
*
*/
// private
liveBufferUpdate : function(o1, options, o2)
{
this.fireEvent('buffer', this, this.ds, this.rowIndex,
Math.max(this.visibleRows, this.getRows().length),
this.ds.getTotalCount());
this.isBuffering = false;
this.isPrebuffering = false;
this.showLoadMask(false);
// we have to stay in sync with rows that may have been skipped while
// the request was loading.
this.bufferRange = [
options.params.start,
options.params.start+options.params.limit
];

var pendingSelections = this.grid.selModel.getPendingSelections(false);

for (var i = 0, max_i = pendingSelections.length; i < max_i; i++) {
this.grid.selModel.clearPendingSelection(pendingSelections[i]);
}

if (this.isInRange(this.rowIndex)) {
this.replaceLiveRows(this.rowIndex);
} else {
this.updateLiveRows(this.rowIndex);
}

if (this.requestQueue >= 0) {
var offset = this.requestQueue;
this.requestQueue = -1;
this.updateLiveRows(offset);
}
},


// {{{ ----------------------scroll listeners------------------------------------
/**
* Handles mousewheel event on the table's body. This is neccessary since the
* <tt>liveScroller</tt> element is completely detached from the table's body.
*
* @param {Ext.EventObject} e The event object
*/
handleWheel : function(e)
{
if (this.rowHeight == -1) {
e.stopEvent();
return;
}
var d = e.getWheelDelta();

this.adjustScrollerPos(-(d*this.rowHeight));

e.stopEvent();
},

/**
* Handles scrolling through the grid. Since the grid is fixed and rows get
* removed/ added subsequently, the only way to determine the actual row in
* view is to measure the <tt>scrollTop</tt> property of the <tt>liveScroller</tt>'s
* DOM element.
*
*/
onLiveScroll : function()
{
var scrollTop = this.liveScroller.dom.scrollTop;
var pixelsSkipped = scrollTop-this.lastScrollPos;

var rowsSkipped = Math.floor(pixelsSkipped/this.rowHeight);

// happens when no scrolling actually happened
if (pixelsSkipped == 0) {
return;
}

var cursor = Math.floor((scrollTop)/this.rowHeight);
this.rowIndex = cursor;
// the lastRowIndex will be set when refreshing the view finished
if (cursor == this.lastRowIndex) {
return;
}

this.updateLiveRows(cursor, rowsSkipped);

this.lastScrollPos = this.liveScroller.dom.scrollTop;
},



// {{{ --------------------------helpers----------------------------------------


// private
refreshRow : function(record)
{
var ds = this.ds, index;
if(typeof record == 'number'){
index = record;
record = ds.getAt(index);
}else{
index = ds.indexOf(record);
}

var viewIndex = index + this.bufferRange[0];

if (viewIndex < this.rowIndex || viewIndex >= this.rowIndex + this.visibleRows) {
this.fireEvent("rowupdated", this, viewIndex, record);
return;
}

this.insertRows(ds, index, index, true);
//this.getRow(index).rowIndex = index;
//this.onRemove(ds, record, index+1, true);
this.fireEvent("rowupdated", this, viewIndex, record);
},

/**
* Overwritten so the rowIndex can be changed to the absolute index.
*
* If the third parameter equals to <tt>true</tt>, the method will also
* repaint the selections.
*/
// private
processRows : function(startRow, skipStripe, paintSelections)
{
skipStripe = skipStripe || !this.grid.stripeRows;
// we will always process all rows in the view
startRow = 0;
var rows = this.getRows();
var cls = ' x-grid3-row-alt ';
var cursor = this.rowIndex;

var index = 0;
var selections = this.grid.selModel.selections;
var ds = this.ds;
for(var i = startRow, len = rows.length; i < len; i++){
index = i+cursor;
var row = rows[i];
// changed!
row.rowIndex = index;

if (paintSelections == true) {
if (this.grid.selModel.bufferedSelections[index] === true) {
this.addRowClass(i, "x-grid3-row-selected");
selections.add(ds.getAt(index-this.bufferRange[0]));
}
this.fly(row).removeClass("x-grid3-row-over");
}

if(!skipStripe){
var isAlt = ((i+1) % 2 == 0);
var hasAlt = (' '+row.className + ' ').indexOf(cls) != -1;
if(isAlt == hasAlt){
continue;
}
if(isAlt){
row.className += " x-grid3-row-alt";
}else{
row.className = row.className.replace("x-grid3-row-alt", "");
}
}
}
},

/**
* API only, since the passed arguments are the indexes in the buffer store.
* However, the method will try to compute the indexes so they might match
* the indexes of the records in the underlying data model.
*
*/
// private
insertRows : function(dm, firstRow, lastRow, isUpdate)
{
var viewIndexFirst = firstRow + this.bufferRange[0];
var viewIndexLast = lastRow + this.bufferRange[0];

if (!isUpdate) {
this.fireEvent("beforerowsinserted", this, viewIndexFirst, viewIndexLast);
}

// first off, remove the rows at the bottom of the view to match the
// visibleRows value and to not cause any spill in the DOM
if (isUpdate !== true && this.getRows().length == this.visibleRows) {
this.removeRows((this.visibleRows-1)-(lastRow-firstRow), this.visibleRows-1);
}

if (isUpdate) {
this.removeRows(viewIndexFirst-this.rowIndex, viewIndexLast-this.rowIndex);
}

var html = this.renderRows(firstRow, lastRow);

var before = this.getRow(firstRow-(this.rowIndex-this.bufferRange[0]));

if (before) {
Ext.DomHelper.insertHtml('beforeBegin', before, html);
} else {
Ext.DomHelper.insertHtml('beforeEnd', this.mainBody.dom, html);
}



if (isUpdate === true) {
var rows = this.getRows();
var cursor = this.rowIndex;
for (var i = 0, max_i = rows.length; i < max_i; i++) {
rows[i].rowIndex = cursor+i;
}
}

if (!isUpdate) {
this.fireEvent("rowsinserted", this, viewIndexFirst, viewIndexLast);
this.processRows(firstRow);
}
},

/**
* Focuses the specified cell.
* @param {Number} row The row index
* @param {Number} col The column index
*/
focusCell : function(row, col, hscroll)
{
var xy = this.ensureVisible(row, col, hscroll);
if (!xy) {
return;
}
this.focusEl.setXY(xy);
if(Ext.isGecko){
this.focusEl.focus();
}else{
this.focusEl.focus.defer(1, this.focusEl);
}

},

/**
* Makes sure that the requested /row/col is visible in the viewport.
* The method may invoke a request for new buffer data and triggers the
* scroll-event of the <tt>liveScroller</tt> element.
*
*/
// private
ensureVisible : function(row, col, hscroll)
{
if(typeof row != "number"){
row = row.rowIndex;
}
if(row < 0 || row >= this.ds.totalLength){
return;
}

col = (col !== undefined ? col : 0);

var rowInd = row-this.rowIndex;
if (row >= this.rowIndex+this.visibleRows) {
this.adjustScrollerPos(((row-(this.rowIndex+this.visibleRows))+1)*this.rowHeight);
} else if (row < this.rowIndex) {
this.adjustScrollerPos((rowInd)*this.rowHeight);
}
var rowInd = rowInd < 0 ? row : rowInd;
var rowEl = this.getRow(rowInd), cellEl;
if(!(hscroll === false && col === 0)){
while(this.cm.isHidden(col)){
col++;
}
cellEl = this.getCell(row-this.rowIndex, col);
}
if(!rowEl){
return;
}

var c = this.scroller.dom;

return cellEl ? Ext.fly(cellEl).getXY() : [c.scrollLeft, Ext.fly(rowEl).getY()];
},


/**
* Checks if the passed argument <tt>cursor</tt> lays within a renderable
* area. The area is renderable, if the sum of cursor and the visibleRows
* property does not exceed the current upper buffer limit.
*
* If this method returns <tt>true</tt>, it's basically save to re-render
* the view with <tt>cursor</tt> as the absolute position in the model
* as the first visible row.
*
* @param {Number} cursor The absolute position of the row in the data model.
*
* @return {Boolean} <tt>true</tt>, if the row can be rendered, otherwise
* <tt>false</tt>
*
*/
isInRange : function(rowIndex)
{
var lastRowIndex = Math.min(this.ds.totalLength-1,
rowIndex + this.visibleRows);
return (rowIndex >= this.bufferRange[0]) &&
(lastRowIndex <= this.bufferRange[1]);
},


/**
* Calculates the bufferRange start index for a buffer request
*
* @param {Boolean} inRange If the index is within the current buffer range
* @param {Number} index The index to use as a reference for the calculations
* @param {Boolean} down Wether the calculation was requested when the user scrolls down
*/
getPredictedBufferIndex : function(index, inRange, down)
{
if (!inRange) {
return Math.max(0, index-(2*this.nearLimit));
}
if (!down) {
return Math.max(0, (index-this.ds.bufferSize)+this.visibleRows);
}

if (down) {
return Math.max(0, Math.min(index, this.ds.totalLength-this.ds.bufferSize));
}
},


/**
* Updates the table view. Removes/appends rows as needed and fetches the
* cells content out of the available store. If the needed rows are not within
* the buffer, the method will advise the store to update it's contents.
*
* The method puts the requested cursor into the queue if a previously called
* buffering is in process.
*
* @param {Number} cursor The row's position, absolute to it's position in the
* data model
*
*/
updateLiveRows: function(index, forceRepaint, forceReload)
{
this.fireEvent('cursormove', this, index,
Math.min(this.ds.totalLength, this.visibleRows),
this.ds.totalLength);

var inRange = this.isInRange(index);

if (this.isBuffering && this.isPrebuffering) {
if (inRange) {
this.replaceLiveRows(index);
} else {
this.showLoadMask(true);
}
}
if (this.isBuffering) {
this.requestQueue = index;
return;
}

var lastIndex = this.lastIndex;
this.lastIndex = index;
var inRange = this.isInRange(index);

var down = false;

if (inRange && forceReload !== true) {

// repaint the table's view
this.replaceLiveRows(index, forceRepaint);

// lets decide if we can void this method or stay in here for
// requesting a buffer update
if (index > lastIndex) { // scrolling down

down = true;
var totalCount = this.ds.totalLength;

// while scrolling, we have not yet reached the row index
// that would trigger a re-buffer
if (index+this.visibleRows+this.nearLimit < this.bufferRange[1]) {
return;
}

// If we have already buffered the last range we can ever get
// by the queried data repository, we don't need to buffer again.
// This basically means that a re-buffer would only occur again
// if we are scrolling up.
if (this.bufferRange[1] >= totalCount) {
return;
}
} else if (index < lastIndex) { // scrolling up

down = false;
// We are scrolling up in the first buffer range we can ever get
// Re-buffering would only occur upon scrolling down.
if (this.bufferRange[0] <= 0) {
return;
}

// if we are scrolling up and we are moving in an acceptable
// buffer range, lets return.
if (index - this.nearLimit > this.bufferRange[0]) {
return;
}
} else {
return;
}

this.isPrebuffering = true;
}

// prepare for rebuffering
this.isBuffering = true

var bufferOffset = this.getPredictedBufferIndex(index, inRange, down);
var fetchSize = this.ds.bufferSize;

if (!inRange) {
this.showLoadMask(true);
}

this.fireEvent('beforebuffer', this, this.ds, index,
this.visibleRows, this.ds.totalLength);

this.ds.suspendEvents();
var sInfo = this.ds.sortInfo;

var params = {};
Ext.apply(params, this.ds.lastOptions);
params.start = bufferOffset;
params.limit = this.ds.bufferSize;

if (sInfo) {
params.dir = sInfo.direction;
params.sort = sInfo.field;
}
this.ds.load({
callback : this.liveBufferUpdate,
scope : this,
params : params
});
this.ds.resumeEvents();
},

/**
* Shows this' view own load mask to indicate that a large amount of buffer
* data was requested by the store.
* @param {Boolean} show <tt>true</tt> to show the load mask, otherwise
* <tt>false</tt>
*/
showLoadMask : function(show)
{
if (this.loadMask == null) {
if (show) {
this.loadMask = new Ext.LoadMask(this.mainBody.dom.parentNode.parentNode,
this.loadMaskConfig);
} else {
return;
}
}

if (show) {
this.loadMask.show();
} else {
this.loadMask.hide();
}
},

/**
* Renders the table body with the contents of the model. The method will
* prepend/ append rows after removing from either the end or the beginning
* of the table DOM to reduce expensive DOM calls.
* It will also take care of rendering the rows selected, taking the property
* <tt>bufferedSelections</tt> of the {@link BufferedRowSelectionModel} into
* account.
* Instead of calling this method directly, the <tt>updateLiveRows</tt> method
* should be called which takes care of rebuffering if needed, since this method
* will behave erroneous if data of the buffer is requested which may not be
* available.
*
* @param {Number} cursor The position of the data in the model to start
* rendering.
*
* @param {Boolean} forceReplace <tt>true</tt> for recomputing the DOM in the
* view, otherwise <tt>false</tt>.
*/
// private
replaceLiveRows : function(cursor, forceReplace)
{
var spill = cursor-this.lastRowIndex;

if (spill == 0 && forceReplace !== true) {
return;
}

// decide wether to prepend or append rows
// if spill is negative, we are scrolling up. Thus we have to prepend
// rows. If spill is positive, we have to append the buffers data.
var append = spill > 0;

// abs spill for simplyfiying append/prepend calculations
spill = Math.abs(spill);

// adjust cursor to the buffered model index
var cursorBuffer = cursor-this.bufferRange[0];

// we can skip checking for append or prepend if the spill is larger than
// visibleRows. We can paint the whole rows new then-
if (spill >= this.visibleRows || spill == 0) {
this.mainBody.update(this.renderRows(
cursorBuffer,
cursorBuffer+this.visibleRows-1
));
} else {
if (append) {
this.removeRows(0, spill-1);
var html = this.renderRows(cursorBuffer+this.visibleRows-spill,
cursorBuffer+this.visibleRows-1);
Ext.DomHelper.insertHtml('beforeEnd', this.mainBody.dom, html);
} else {
this.removeRows(this.visibleRows-spill, this.visibleRows-1);
var html = this.renderRows(cursorBuffer, cursorBuffer+spill-1);
Ext.DomHelper.insertHtml('beforeBegin', this.mainBody.dom.firstChild, html);
}
}

this.processRows(0, undefined, true);
this.lastRowIndex = cursor;
},



/**
* Adjusts the scroller height to make sure each row in the dataset will be
* can be displayed, no matter which value the current height of the grid
* component equals to.
*/
// protected
adjustBufferInset : function()
{
var g = this.grid, ds = g.store;

var c = g.getGridEl();

var scrollbar = this.cm.getTotalWidth()+this.scrollOffset > c.getSize().width;

// adjust the height of the scrollbar
this.liveScroller.dom.style.height = this.liveScroller.dom.parentNode.offsetHeight +
(Ext.isGecko
? ((ds.totalLength > 0 && scrollbar)
? - this.horizontalScrollOffset
: 0)
: (((ds.totalLength > 0 && scrollbar)
? 0 : this.horizontalScrollOffset)))+"px";
if (this.rowHeight == -1) {
return;
}

if (ds.totalLength <= this.visibleRows) {
this.liveScrollerInset.style.height = "0px";
return;
}

var height = this.rowHeight*ds.totalLength;

height += (c.getSize().height-(this.visibleRows*this.rowHeight));

if (scrollbar) {
height -= this.horizontalScrollOffset;
}

this.liveScrollerInset.style.height = (height)+"px";
},

/**
* Recomputes the number of visible rows in the table based upon the height
* of the component. The method adjusts the <tt>rowIndex</tt> property as
* needed, if the sum of visible rows and the current row index exceeds the
* number of total data available.
*/
// protected
adjustVisibleRows : function()
{
if (this.rowHeight == -1) {
if (this.getRows()[0]) {
this.rowHeight = this.getRows()[0].offsetHeight;
} else {
return;
}
}


var g = this.grid, ds = g.store;

var c = g.getGridEl();
var cm = this.cm;
var size = c.getSize(true);
var vh = size.height;

var vw = size.width-this.scrollOffset;
// horizontal scrollbar shown?
if (cm.getTotalWidth() > vw) {
// yes!
vh -= this.horizontalScrollOffset;
}

vh -= this.mainHd.getHeight();

var visibleRows = Math.max(1, Math.floor(vh/this.rowHeight));

if (this.visibleRows == visibleRows) {
return;
}

this.visibleRows = visibleRows;

var totalLength = ds.totalLength;

if (this.rowIndex + visibleRows > totalLength) {
this.rowIndex = Math.max(0, ds.totalLength-this.visibleRows);
this.lastRowIndex = this.rowIndex;
this.updateLiveRows(this.rowIndex, true);
} else {
this.updateLiveRows(this.rowIndex, true);
}
},


adjustScrollerPos : function(pixels, suspendEvent)
{
var liveScroller = this.liveScroller;

if (suspendEvent === true) {
liveScroller.un('scroll', this.onLiveScroll, this);
}

liveScroller.dom.scrollTop += pixels;

if (suspendEvent === true) {
liveScroller.dom.scrollTop = liveScroller.dom.scrollTop;
liveScroller.on('scroll', this.onLiveScroll, this);
}

}



});
/*
* Ext.ux.data.BufferedJsonReader V0.1
* Copyright(c) 2007, http://www.siteartwork.de
*
* Licensed under the terms of the Open Source LGPL 3.0
* http://www.gnu.org/licenses/lgpl.html
*
* @author Thorsten Suckow-Homberg <ts@siteartwork.de>
*/

Ext.namespace('Ext.ux.data');


Ext.ux.data.BufferedJsonReader = function(meta, recordType){

Ext.ux.data.BufferedJsonReader.superclass.constructor.call(this, meta, recordType);
};


Ext.extend(Ext.ux.data.BufferedJsonReader, Ext.data.JsonReader, {

/**
* @cfg {String} versionProperty Name of the property from which to retrieve the
* version of the data repository this reader parses
* the reponse from
*/



/**
* Create a data block containing Ext.data.Records from a JSON object.
* @param {Object} o An object which contains an Array of row objects in the property specified
* in the config as 'root, and optionally a property, specified in the config as 'totalProperty'
* which contains the total size of the dataset.
* @return {Object} data A data block which is used by an Ext.data.Store object as
* a cache of Ext.data.Records.
*/
readRecords : function(o)
{
var s = this.meta;

if(!this.ef && s.versionProperty) {
this.getVersion = this.getJsonAccessor(s.versionProperty);
}

// shorten for future calls
if (!this.__readRecords) {
this.__readRecords = Ext.ux.data.BufferedJsonReader.superclass.readRecords;
}

var intercept = this.__readRecords.call(this, o);


if (s.versionProperty) {
var v = this.getVersion(o);
intercept.version = (v === undefined || v === "") ? null : v;
}


return intercept;
}

});
/*
* Ext.ux.grid.BufferedRowSelectionModel V0.1
* Copyright(c) 2007, http://www.siteartwork.de
*
* Licensed under the terms of the Open Source LGPL 3.0
* http://www.gnu.org/licenses/lgpl.html
*
* @author Thorsten Suckow-Homberg <ts@siteartwork.de>
*/

Ext.namespace('Ext.ux.grid');

Ext.ux.grid.BufferedRowSelectionModel = function(config) {


this.addEvents({
/**
* The selection dirty event will be triggered in case records were
* inserted/ removed at view indexes that may affect the current
* selection ranges which are only represented by view indexes, but not
* current record-ids
*/
'selectiondirty' : true
});





Ext.apply(this, config);

this.bufferedSelections = {};

this.pendingSelections = {};

Ext.ux.grid.BufferedRowSelectionModel.superclass.constructor.call(this);

};

Ext.extend(Ext.ux.grid.BufferedRowSelectionModel, Ext.grid.RowSelectionModel, {



// private
initEvents : function()
{
Ext.ux.grid.BufferedRowSelectionModel.superclass.initEvents.call(this);

this.grid.store.on('add', this.onAdd, this);
this.grid.store.on('selectionsload', this.onSelectionsLoad, this);
},



// private
onRefresh : function()
{
//var ds = this.grid.store, index;
//var s = this.getSelections();
this.clearSelections(true);
/*for(var i = 0, len = s.length; i < len; i++){
var r = s[i];
if((index = ds.indexOfId(r.id)) != -1){
this.selectRow(index, true);
}
}
if(s.length != this.selections.getCount()){
this.fireEvent("selectionchange", this);
}*/
},



/**
* Callback is called when a row gets removed in the view. The process to
* invoke this method is as follows:
*
* <ul>
* <li>1. store.remove(record);</li>
* <li>2. view.onRemove(store, record, indexInStore, isUpdate)<br />
* [view triggers rowremoved event]</li>
* <li>3. this.onRemove(view, indexInStore, record)</li>
* </ul>
*
* If r defaults to <tt>null</tt> and index is within the pending selections
* range, the selectionchange event will be called, too.
* Additionally, the method will shift all selections and trigger the
* selectiondirty event if any selections are pending.
*
*/
onRemove : function(v, index, r)
{
// if index equals to Number.MIN_VALUE or Number.MAX_VALUE, mark current
// pending selections as dirty
if ((index == Number.MIN_VALUE || index == Number.MAX_VALUE)) {
this.selections.remove(r);
this.fireEvent('selectiondirty', this, index, r);
return;
}

var viewIndex = index;
var fire = this.bufferedSelections[viewIndex] === true;
var ranges = this.getPendingSelections();
var rangesLength = ranges.length;

delete this.bufferedSelections[viewIndex];
delete this.pendingSelections[viewIndex];
if (r) {
this.selections.remove(r);
}

if (rangesLength == 0) {
this.shiftSelections(viewIndex, -1);
return;
}

var s = ranges[0];
var e = ranges[rangesLength-1];
if (viewIndex <= e || viewIndex <= s) {
if (this.fireEvent('selectiondirty', this, viewIndex, -1) !== false) {
this.shiftSelections(viewIndex, -1);
}
}

if (fire) {
this.fireEvent('selectionchange', this);
}
},


/**
* If records where added to the store, this method will work as a callback.
* The method gets usually called <b>after</b> the onAdd method of the according
* view was called.
* Selections will be shifted down if, and only if, the listeners for the
* selectiondirty event will return <tt>true</tt>.
*
*/
onAdd : function(store, records, index)
{
var ranges = this.getPendingSelections();
var rangesLength = ranges.length;

// if index equals to Number.MIN_VALUE or Number.MAX_VALUE, mark current
// pending selections as dirty
if ((index == Number.MIN_VALUE || index == Number.MAX_VALUE)) {

// we may shift selections if there are no pendning selections. Everything
// in the current buffer range will be shifted up!
if (rangesLength == 0 && index == Number.MIN_VALUE) {
this.shiftSelections(this.grid.view.bufferRange[0], records.length);
}

this.fireEvent('selectiondirty', this, index, records.length);
return;
}

if (rangesLength == 0) {
this.shiftSelections(this.grid.view.bufferRange[0]+index, records.length);
return;
}

// it is safe to say that the selection is dirty when the inserted index
// is less or equal to the first selection range index or less or equal
// to the last selection range index
var s = ranges[0];
var e = ranges[rangesLength-1];
var viewIndex = this.grid.view.bufferRange[0]+index;
if (viewIndex <= e || viewIndex <= s) {
if (this.fireEvent('selectiondirty', this, viewIndex, records.length) !== false) {
this.shiftSelections(viewIndex, records.length);
}
}
},



/**
* Shifts current/pending selections. This method can be used when rows where
* inserted/removed and the selection model has to synchronize itself.
*/
shiftSelections : function(startRow, length)
{
var index = 0;
var newSelections = {};
var newIndex = 0;
var newRequests = {};
var totalLength = this.grid.store.totalLength;

for (var i in this.bufferedSelections) {
index = parseInt(i);
newIndex = index+length;
if (newIndex >= totalLength) {
break;
}

if (index >= startRow) {
newSelections[newIndex] = true;

if (this.pendingSelections[i]) {
newRequests[newIndex] = true;
}

} else {
newSelections[i] = true;

if (this.pendingSelections[i]) {
newRequests[i] = true;
}
}
}

this.bufferedSelections = newSelections;
this.pendingSelections = newRequests;
},

/**
*
* @param {Array} records The records that have been loaded
* @param {Array} ranges An array representing the model index ranges the
* reords have been loaded for.
*/
onSelectionsLoad : function(store, records, ranges)
{
this.pendingSelections = {};

this.selections.addAll(records);

},

/**
* Returns true if there is a next record to select
* @return {Boolean}
*/
hasNext : function()
{
return this.last !== false && (this.last+1) < this.grid.store.getTotalCount();
},

/**
* Gets the number of selected rows.
* @return {Number}
*/
getCount : function()
{
var sels = this.bufferedSelections;

var c = 0;
for (var i in sels) {
c++;
}

return c;
},

/**
* Returns True if the specified row is selected.
* @param {Number/Record} record The record or index of the record to check
* @return {Boolean}
*/
isSelected : function(index)
{
if (typeof index == "number") {
return this.bufferedSelections[index] === true;
}

var r = index;
return (r && this.selections.key(r.id) ? true : false);
},



/**
* Deselects a row.
* @param {Number} row The index of the row to deselect
*/
deselectRow : function(index, preventViewNotify)
{
if(this.locked) return;
if(this.last == index){
this.last = false;
}

if(this.lastActive == index){
this.lastActive = false;
}
var r = this.grid.store.getAt(index-this.grid.getView().bufferRange[0]);

delete this.pendingSelections[index];
delete this.bufferedSelections[index];
if (r) {
this.selections.remove(r);
}
if(!preventViewNotify){
this.grid.getView().onRowDeselect(index);
}
this.fireEvent("rowdeselect", this, index, r);
this.fireEvent("selectionchange", this);
},


/**
* Selects a row.
* @param {Number} row The index of the row to select
* @param {Boolean} keepExisting (optional) True to keep existing selections
*/
selectRow : function(index, keepExisting, preventViewNotify){
// ignore store count, since this is our buffer and the rowIndex may indeed
// be greater than the count of records in the store
if(this.locked || index < 0) return;// || index >= this.grid.store.getCount())) return;

//var r = this.grid.store.getAt(index);
var r = this.grid.store.getAt(index-this.grid.getView().bufferRange[0]);

if(this.fireEvent("beforerowselect", this, index, keepExisting, r) !== false){
if(!keepExisting || this.singleSelect){
this.clearSelections();
}

if (r) {
this.selections.add(r);
delete this.pendingSelections[index];
} else {
this.pendingSelections[index] = true;
}

this.bufferedSelections[index] = true;

this.last = this.lastActive = index;

if(!preventViewNotify){
this.grid.getView().onRowSelect(index);
}
this.fireEvent("rowselect", this, index, r);
this.fireEvent("selectionchange", this);
}
},

clearPendingSelection : function(index)
{
var r = this.grid.store.getAt(index-this.grid.getView().bufferRange[0]);
if (!r) {
return;
}
this.selections.add(r);
delete this.pendingSelections[index];
},

getPendingSelections : function(asRange)
{
var index = 1;
var ranges = [];
var currentRange = 0;
var tmpArray = [];

for (var i in this.pendingSelections) {
tmpArray.push(parseInt(i));
}

tmpArray.sort(function(o1,o2){
if (o1 > o2) {
return 1;
} else if (o1 < o2) {
return -1;
} else {
return 0;
}
});

if (asRange === false) {
return tmpArray;
}

var max_i = tmpArray.length;

if (max_i == 0) {
return [];
}

ranges[currentRange] = [tmpArray[0], tmpArray[0]];
for (var i = 0, max_i = max_i-1; i < max_i; i++) {
if (tmpArray[i+1] - tmpArray[i] == 1) {
ranges[currentRange][1] = tmpArray[i+1];
} else {
currentRange++;
ranges[currentRange] = [tmpArray[i+1], tmpArray[i+1]];
}
}

return ranges;
},

/**
* Clears all selections.
*/
clearSelections : function(fast)
{
if(this.locked) return;
if(fast !== true){
//var ds = this.grid.store;
var s = this.selections;
/*s.each(function(r){
this.deselectRow(ds.indexOfId(r.id)+this.grid.getView().bufferRange[0]);
}, this);*/
s.clear();

for (var i in this.bufferedSelections) {
this.deselectRow(i);
}

}else{
this.selections.clear();
this.bufferedSelections = {};
this.pendingSelections = {};
}
this.last = false;
},


/**
* Selects a range of rows. All rows in between startRow and endRow are also
* selected.
*
* @param {Number} startRow The index of the first row in the range
* @param {Number} endRow The index of the last row in the range
* @param {Boolean} keepExisting (optional) True to retain existing selections
*/
selectRange : function(startRow, endRow, keepExisting)
{
if(this.locked) {
return;
}

if(!keepExisting) {
this.clearSelections();
}

if (startRow <= endRow) {
for(var i = startRow; i <= endRow; i++) {
this.selectRow(i, true);
}
} else {
for(var i = startRow; i >= endRow; i--) {
this.selectRow(i, true);
}
}
}

});



/*
* Ext.ux.grid.BufferedStore V0.9
* Copyright(c) 2007, http://www.siteartwork.de
*
* Licensed under the terms of the Open Source LGPL 3.0
* http://www.gnu.org/licenses/lgpl.html
*
* @author Thorsten Suckow-Homberg <ts@siteartwork.de>
*/

Ext.namespace('Ext.ux.grid');


/**
* @class Ext.ux.grid.BufferedStore
* @extends Ext.data.Store
*
* The BufferedGridSore is a special implementation of a Ext.data.Store. It is used
* for loading chunks of data from the underlying data repository as requested
* by the Ext.ux.BufferedGridView. It's size is limited to the config parameter
* bufferSize and is thereby guaranteed to never hold more than this amount
* of records in the store.
*
* Requesting selection ranges:
* ----------------------------
* This store implementation has 2 Http-proxies: A data proxy for requesting data
* from the server for displaying and another proxy to request pending selections:
* Pending selections are represented by row indexes which have been selected but
* which records have not yet been available in the store. The loadSelections method
* will initiate a request to the data repository (same url as specified in the
* url config parameter for the store) to fetch the pending selections. The additional
* parameter send to the server is the "ranges" parameter, which will hold a json
* encoded string representing ranges of row indexes to load from the data repository.
* As an example, pending selections with the indexes 1,2,3,4,5,9,10,11,16 would
* have to be translated to [1,5],[9,11],[16].
* Please note, that by indexes we do not understand (primary) keys of the data,
* but indexes as represented by the view. To get the ranges of pending selections,
* you can use the getPendingSelections method of the BufferedRowSelectionModel, which
* should be used as the default selection model of the grid.
*
* Version-property:
* -----------------
* This implementation does also introduce a new member called "version". The version
* property will help you in determining if any pending selections indexes are still
* valid or may have changed. This is needed to reduce the danger of data inconsitence
* when you are requesting data from the server: As an example, a range of indexes must
* be read from the server but may have been become invalid when the row represented
* by the index is no longer available in teh underlying data store, caused by a
* delete or insert operation. Thus, you have to take care of the version property
* by yourself (server side) and change this value whenever a row was deleted or
* inserted. You can specify the path to the version property in the BufferedJsonReader,
* which should be used as the default reader for this store. If the store recognizes
* a version change, it will fire the versionchange event. It is up to the user
* to remove all selections which are pending, or use them anyway.
*
* Inserting data:
* ---------------
* Another thing to notice is the way a user inserts records into the data store.
* A user should always provide a sortInfo for the grid, so the findInsertIndex
* method can return a value that comes close to the value as it would have been
* computed by the underlying store's sort algorithm. Whenever a record should be
* added to the store, the insert index should be calculated and the used as the
* parameter for the insert method. The findInsertIndex method will return a value
* that equals to Number.MIN_VALUE or Number.MAX_VALUE if the added record would not
* change the current state of the store. If that happens, this data is not available
* in the store, and may be requested later on when a new request for new data is made.
*
* Sorting:
* --------
* remoteSort will always be set to true, no matter what value the user provides
* using the config object.
*
* @constructor
* Creates a new Store.
* @param {Object} config A config object containing the objects needed for the Store to access data,
* and read the data into Records.
*/
Ext.ux.grid.BufferedStore = function(config) {

config = config || {};

// remoteSort will always be set to true.
config.remoteSort = true;

Ext.apply(this, config);

this.addEvents({
/**
* @event versionchange
* Fires when the version property has changed.
* @param {Ext.ux.BufferedGridStore} this
* @param {String} oldValue
* @param {String} newValue
*/
'versionchange' : true,
/**
* @event beforeselectionsload
* Fires before the store sends a request for ranges of records to
* the server.
* @param {Ext.ux.BufferedGridStore} this
* @param {Array} ranges
*/
'beforeselectionsload' : true,
/**
* @event selectionsload
* Fires when selections have been loaded.
* @param {Ext.ux.BufferedGridStore} this
* @param {Array} records An array containing the loaded records from
* the server.
* @param {Array} ranges An array containing the ranges of indexes this
* records may represent.
*/
'selectionsload' : true

});

Ext.ux.grid.BufferedStore.superclass.constructor.call(this, config);


if(this.url && !this.selectionsProxy){
this.selectionsProxy = new Ext.data.HttpProxy({url: this.url});
}

};


Ext.extend(Ext.ux.grid.BufferedStore, Ext.data.Store, {

/**
* The version of the data in the store. This value is represented by the
* versionProperty-property of the BufferedJsonReader.
* @property
*/
version : null,

/**
* Inserts a record at the position as specified in index.
* If the index equals to Number.MIN_VALUE or Number.MAX_VALUE, the record will
* not be added to the store, but still fire the add-event to indicate that
* the set of data in the underlying store has been changed.
* If the index equals to 0 and the length of data in the store equals to
* bufferSize, the add-event will be triggered with Number.MIN_VALUE to
* indicate that a record has been prepended. If the index equals to
* bufferSize, the method will assume that the record has been appended and
* trigger the add event with index set to Number.MAX_VALUE.
*
* Note:
* -----
* The index parameter is not a view index, but a value in the range of
* [0, this.bufferSize].
*
*/
insert : function(index, records)
{
records = [].concat(records);
var cRecords = [];
var cCount = this.bufferSize-1;
var pIndex = (index == Number.MIN_VALUE || index == Number.MAX_VALUE) ? 0 : index;

this.totalLength += records.length;

if (index == Number.MIN_VALUE || index == Number.MAX_VALUE || index == this.bufferSize
|| (index == 0 && this.getCount() == this.bufferSize)) {
var tIndex = (index == this.bufferSize)
? Number.MAX_VALUE
: ((index == Number.MIN_VALUE || index == 0) ? Number.MIN_VALUE : Number.MAX_VALUE);

this.fireEvent("add", this, records, tIndex);
return;
}

for (var i = 0, max_i = records.length; i < max_i; i++) {
if (i + pIndex > cCount) {
break;
}
cRecords.push(records[i]);
}

for(var i = 0, len = cRecords.length; i < len; i++){
// different from original impl, shift one up
this.data.insert(index+i, cRecords[i]);
cRecords[i].join(this);
}

while (this.getCount() > this.bufferSize) {
this.data.remove(this.data.last());
}


this.fireEvent("add", this, cRecords, index);
},

/**
* Remove a Record from the Store and fires the remove event.
*
* If the record is not within the store, the method will try to guess it's
* index by calling findInsertIndex.
*/
remove : function(record)
{
var index = this.data.indexOf(record);

if (index < 0) {
var ind = this.findInsertIndex(record);
this.totalLength -= 1;
if(this.pruneModifiedRecords){
this.modified.remove(record);
}
this.fireEvent("remove", this, record, ind);
return false;
}
this.data.removeAt(index);
if(this.pruneModifiedRecords){
this.modified.remove(record);
}

this.totalLength -= 1;
this.fireEvent("remove", this, record, index);
return true;
},

/**
* Remove all Records from the Store and fires the clear event.
* The method assumes that there will be no data available anymore in the
* underlying data store.
*/
removeAll : function()
{
this.totalLength = 0;
this.data.clear();

if(this.pruneModifiedRecords){
this.modified = [];
}
this.fireEvent("clear", this);
},

/**
* Requests a range of data from the underlying data store. Similiar to the
* start and limit parameter usually send to the server, the method needs
* an array of ranges of indexes.
* Example: To load all records at the positions 1,2,3,4,9,12,13,14, the supplied
* parameter should equal to [[1,4],[9],[12,14]].
* The request will only be done if the beforeselectionsloaded events return
* value does not equal to false.
*/
loadRanges : function(ranges)
{
var max_i = ranges.length;

if(max_i > 0 && !this.selectionsProxy.activeRequest
&& this.fireEvent("beforeselectionsload", this, ranges) !== false){

var lParams = this.lastOptions.params;

var params = {};
params.ranges = Ext.encode(ranges);

if (lParams) {
if (lParams.sort) {
params.sort = lParams.sort;
}
if (lParams.dir) {
params.dir = lParams.dir;
}
}

var options = {};
for (var i in this.lastOptions) {
options.i = this.lastOptions.i;
}

options.ranges = params.ranges;

this.selectionsProxy.load(params, this.reader,
this.selectionsLoaded, this,
options);
}
},

/**
* Alias for loadRanges.
*/
loadSelections : function(ranges)
{
this.loadRanges(ranges);
},

/**
* Called as a callback by the proxy which loads pending selections.
* Will fire the selectionsload event with the loaded records if, and only
* if the return value of the checkVersionChange event does not equal to
* false.
*/
selectionsLoaded : function(o, options, success)
{
if (this.checkVersionChange(o, options, success) !== false) {
this.fireEvent("selectionsload", this, o.records, Ext.decode(options.ranges));
} else {
this.fireEvent("selectionsload", this, [], Ext.decode(options.ranges));
}
},

/**
* Checks if the version supplied in <tt>o</tt> differs from the version
* property of the current instance of this object and fires the versionchange
* event if it does.
*/
// private
checkVersionChange : function(o, options, success)
{
if(o && success !== false){
if (o.version !== undefined) {
var old = this.version;
this.version = o.version;
if (this.version !== old) {
return this.fireEvent('versionchange', this, old, this.version);
}
}
}
},

/**
* Overwrites the parent implementation to pass the boolean bufferedSort
* parameter to {applySort}.
* The sort procedure tries to respect the current data in the buffer. If the
* found index would not be within the bufferRange, Number.MIN_VALUE is returned to
* indicate that the record would be sorted below the first record in the buffer
* range, while Number.MAX_VALUE would indicate that the record would be added after
* the last record in the buffer range.
*
* The method is not guaranteed to return the relative index of the record
* in the data model as returned by the underlying domain model.
*/
findInsertIndex : function(record)
{
this.suspendEvents();
// store original data
var data = this.data.clone();

// check if the record would be added to/inserted before the current
// buffer range
if (data.getCount() >= this.bufferSize) {
var first = this.data.first();
var last = this.data.last();

this.data.clear();
this.data.add(record);
this.data.add(first);
this.data.add(last);
this.applySort(true);
var index = this.data.indexOf(record);

if (index == 0 || index == 2) {
this.data = data;
this.resumeEvents();
return index == 0 ? Number.MIN_VALUE : Number.MAX_VALUE;
}
}

this.data = data.clone();
this.data.add(record);
this.applySort(true);
var index = this.data.indexOf(record);
this.data = data;
this.resumeEvents();


return index;
},

/**
* Removed snapshot check
*/
// private
sortData : function(f, direction)
{
direction = direction || 'ASC';
var st = this.fields.get(f).sortType;
var fn = function(r1, r2){
var v1 = st(r1.data[f]), v2 = st(r2.data[f]);
return v1 > v2 ? 1 : (v1 < v2 ? -1 : 0);
};
this.data.sort(direction, fn);
},

/**
* Overwritten so the method can be used by findInsertIndex. Any call from
* loadRecords will do nothing, since bufferedSort argument won't equal to
* <tt>true</tt>.
*/
applySort : function(bufferedSort)
{
if(bufferedSort === true && this.sortInfo){
var s = this.sortInfo, f = s.field;
this.sortData(f, s.direction);
}
},

/**
* @cfg {Number} bufferSize The number of records that will at least always
* be available in the store for rendering. This value will be send to the
* server as the <tt>limit</tt> parameter and should not change during the
* lifetime of a grid component. Note: In a paging grid, this number would
* indicate the page size.
* The value should be set high enough to make a userfirendly scrolling
* possible and should be greater than the sum of {nearLimit} and
* {visibleRows}. Usually, a value in between 150 and 200 is good enough.
* A lesser value will more often make the store re-request new data, while
* a larger number will make loading times higher.
*/


// private
onMetaChange : function(meta, rtype, o)
{
this.version = null;
Ext.ux.grid.BufferedStore.superclass.onMetaChange.call(this, meta, rtype, o);
},


/**
* Will fire the versionchange event if the version of incoming data has changed.
*/
// private
loadRecords : function(o, options, success)
{
this.checkVersionChange(o, options, success);

Ext.ux.grid.BufferedStore.superclass.loadRecords.call(this, o, options, success);
},
//--------------------------------------EMPTY-----------------------------------
// no interface concept, so simply overwrite and leave them empty as for now
clearFilter : function(){},
isFiltered : function(){},
collect : function(){},
createFilterFn : function(){},
sum : function(){},
filter : function(){},
filterBy : function(){},
query : function(){},
queryBy : function(){},
find : function(){},
findBy : function(){}

});
// vim: ts=4:sw=4:nu:fdc=4:nospell
/**
* Ext.ux.form.LovCombo, List of Values Combo
*
* @author Ing. Jozef Sakáloš
* @copyright (c) 2008, by Ing. Jozef Sakáloš
* @date 16. April 2008
* @version $Id: Ext.ux.form.LovCombo.js 291 2008-07-03 07:46:49Z jozo $
*
* @license Ext.ux.form.LovCombo.js is licensed under the terms of the Open Source
* LGPL 3.0 license. Commercial use is permitted to the extent that the
* code/component(s) do NOT become part of another Open Source or Commercially
* licensed development library or toolkit without explicit permission.
*
* License details: http://www.gnu.org/licenses/lgpl.html
*/

/*global Ext */

// add RegExp.escape if it has not been already added
if('function' !== typeof RegExp.escape) {
RegExp.escape = function(s) {
if('string' !== typeof s) {
return s;
}
// Note: if pasting from forum, precede ]/\ with backslash manually
return s.replace(/([.*+?^=!:${}()|[\]\/\\])/g, '\\$1');
}; // eo function escape
}

// create namespace
Ext.ns('Ext.ux.form');

/**
*
* @class Ext.ux.form.LovCombo
* @extends Ext.form.ComboBox
*/
Ext.ux.form.LovCombo = Ext.extend(Ext.form.ComboBox, {

// {{{
// configuration options
/**
* @cfg {String} checkField name of field used to store checked state.
* It is automatically added to existing fields.
* Change it only if it collides with your normal field.
*/
checkField:'checked'

/**
* @cfg {String} separator separator to use between values and texts
*/
,separator:','

/**
* @cfg {String/Array} tpl Template for items.
* Change it only if you know what you are doing.
*/
// }}}
// {{{
,initComponent:function() {

// template with checkbox
if(!this.tpl) {
this.tpl =
'<tpl for=".">'
+'<div class="x-combo-list-item">'
+'<img src="' + Ext.BLANK_IMAGE_URL + '" '
+'class="ux-lovcombo-icon ux-lovcombo-icon-'
+'{[values.' + this.checkField + '?"checked":"unchecked"' + ']}">'
+'<div class="ux-lovcombo-item-text">{' + (this.displayField || 'text' )+ '}</div>'
+'</div>'
+'</tpl>'
;
}

// call parent
Ext.ux.form.LovCombo.superclass.initComponent.apply(this, arguments);

// install internal event handlers
this.on({
scope:this
,beforequery:this.onBeforeQuery
,blur:this.onRealBlur
});

// remove selection from input field
this.onLoad = this.onLoad.createSequence(function() {
if(this.el) {
var v = this.el.dom.value;
this.el.dom.value = '';
this.el.dom.value = v;
}
});

} // e/o function initComponent
// }}}
// {{{
/**
* Disables default tab key bahavior
* @private
*/
,initEvents:function() {
Ext.ux.form.LovCombo.superclass.initEvents.apply(this, arguments);

// disable default tab handling - does no good
this.keyNav.tab = false;

} // eo function initEvents
// }}}
// {{{
/**
* clears value
*/
,clearValue:function() {
this.value = '';
this.setRawValue(this.value);
this.store.clearFilter();
this.store.each(function(r) {
r.set(this.checkField, false);
}, this);
if(this.hiddenField) {
this.hiddenField.value = '';
}
this.applyEmptyText();
} // eo function clearValue
// }}}
// {{{
/**
* @return {String} separator (plus space) separated list of selected displayFields
* @private
*/
,getCheckedDisplay:function() {
var re = new RegExp(this.separator, "g");
return this.getCheckedValue(this.displayField).replace(re, this.separator + ' ');
} // eo function getCheckedDisplay
// }}}
// {{{
/**
* @return {String} separator separated list of selected valueFields
* @private
*/
,getCheckedValue:function(field) {
field = field || this.valueField;
var c = [];


// store may be filtered so get all records
var snapshot = this.store.snapshot || this.store.data;

snapshot.each(function(r) {
if(r.get(this.checkField)) {
c.push(r.get(field));
}
}, this);

return c.join(this.separator);
} // eo function getCheckedValue
// }}}
// {{{
/**
* beforequery event handler - handles multiple selections
* @param {Object} qe query event
* @private
*/
,onBeforeQuery:function(qe) {
qe.query = qe.query.replace(new RegExp(RegExp.escape(this.getCheckedDisplay()) + '[ ' + this.separator + ']*'), '');
} // eo function onBeforeQuery
// }}}
// {{{
/**
* blur event handler - runs only when real blur event is fired
*/
,onRealBlur:function() {
this.list.hide();
var rv = this.getRawValue();
var rva = rv.split(new RegExp(RegExp.escape(this.separator) + ' *'));
var va = [];
var snapshot = this.store.snapshot || this.store.data;

// iterate through raw values and records and check/uncheck items
Ext.each(rva, function(v) {
snapshot.each(function(r) {
if(v === r.get(this.displayField)) {
va.push(r.get(this.valueField));
}
}, this);
}, this);
this.setValue(va.join(this.separator));
this.store.clearFilter();
} // eo function onRealBlur
// }}}
// {{{
/**
* Combo's onSelect override
* @private
* @param {Ext.data.Record} record record that has been selected in the list
* @param {Number} index index of selected (clicked) record
*/
,onSelect:function(record, index) {
if(this.fireEvent('beforeselect', this, record, index) !== false){

// toggle checked field
record.set(this.checkField, !record.get(this.checkField));

// display full list
if(this.store.isFiltered()) {
this.doQuery(this.allQuery);
}

// set (update) value and fire event
this.setValue(this.getCheckedValue());
this.fireEvent('select', this, record, index);
}
} // eo function onSelect
// }}}
// {{{
/**
* Sets the value of the LovCombo
* @param {Mixed} v value
*/
,setValue:function(v) {
if(v) {
v = '' + v;
if(this.valueField) {
this.store.clearFilter();
this.store.each(function(r) {
var checked = !(!v.match(
'(^|' + this.separator + ')' + RegExp.escape(r.get(this.valueField))
+'(' + this.separator + '|$)'))
;

r.set(this.checkField, checked);
}, this);
this.value = this.getCheckedValue();
this.setRawValue(this.getCheckedDisplay());
if(this.hiddenField) {
this.hiddenField.value = this.value;
}
}
else {
this.value = v;
this.setRawValue(v);
if(this.hiddenField) {
this.hiddenField.value = v;
}
}
if(this.el) {
this.el.removeClass(this.emptyClass);
}
}
else {
this.clearValue();
}
} // eo function setValue
// }}}
// {{{
/**
* Selects all items
*/
,selectAll:function() {
this.store.each(function(record){
// toggle checked field
record.set(this.checkField, true);
}, this);

//display full list
this.doQuery(this.allQuery);
this.setValue(this.getCheckedValue());
} // eo full selectAll
// }}}
// {{{
/**
* Deselects all items. Synonym for clearValue
*/
,deselectAll:function() {
this.clearValue();
} // eo full deselectAll
// }}}

}); // eo extend

// register xtype
Ext.reg('lovcombo', Ext.ux.form.LovCombo);

// eof

/*
* @class Ext.ux.ManagedIFrame
* Version: RC1 (Release Candidate 1)
* Author: Doug Hendricks. doug[always-At]theactivegroup.com
* Copyright 2007-2008, Active Group, Inc. All rights reserved.
*
************************************************************************************
* This file is distributed on an AS IS BASIS WITHOUT ANY WARRANTY;
* without even the implied warranty of MERCHANTABILITY or
* FITNESS FOR A PARTICULAR PURPOSE.
************************************************************************************

License: ux.ManagedIFrame and ux.ManagedIFramePanel are licensed under the terms of
the Open Source LGPL 3.0 license. Commercial use is permitted to the extent
that the code/component(s) do NOT become part of another Open Source or Commercially
licensed development library or toolkit without explicit permission.

Donations are welcomed: http://donate.theactivegroup.com

License details: http://www.gnu.org/licenses/lgpl.html

* <p> An Ext harness for iframe elements.

Adds Ext.UpdateManager(Updater) support and a compatible 'update' method for
writing content directly into an iFrames' document structure.

* Usage:<br>
* <pre><code>
* // Harness it from an existing Iframe from markup
* var i = new Ext.ux.ManagedIFrame("myIframe");
* // Replace the iFrames document structure with the response from the requested URL.
* i.load("http://myserver.com/index.php", "param1=1&amp;param2=2");
* //Notes: this is not the same as setting the Iframes src property !
* // Content loaded in this fashion does not share the same document namespaces as it's parent --
* // meaning, there (by default) will be no Ext namespace defined in it since the document is
* // overwritten after each call to the update method, and no styleSheets.
* </code></pre>
* <br>
* @cfg {Boolean/Object} autoCreate True to auto generate the IFRAME element, or a {@link Ext.DomHelper} config of the IFRAME to create
* @cfg {String} html Any markup to be applied to the IFRAME's document content when rendered.
* @cfg {Object} loadMask An {@link Ext.LoadMask} config or true to mask the iframe while using the update or setSrc methods (defaults to false).
* @cfg {Object} src The src attribute to be assigned to the Iframe after initialization (overrides the autoCreate config src attribute)
* @constructor

* @param {Mixed} el, Config object The iframe element or it's id to harness or a valid config object.

*/

Ext.ux.ManagedIFrame = function(){
var args=Array.prototype.slice.call(arguments, 0)
,el = Ext.get(args[0])
,config = args[0];

if(el && el.dom && el.dom.tagName == 'IFRAME'){
config = args[1] || {};
}else{
config = args[0] || args[1] || {};
el = config.autoCreate?
Ext.get(Ext.DomHelper.append(document.body, Ext.apply({tag:'iframe', src:(Ext.isIE&&Ext.isSecure)?Ext.SSL_SECURE_URL:''},config.autoCreate))):null;
}


if(!el || el.dom.tagName != 'IFRAME') return el;

!!el.dom.name.length || (el.dom.name = el.dom.id); //make sure there is a valid frame name

this.addEvents({
/**
* @event domready
* Fires when the iFrame's Document(DOM) has reach a state where the DOM may be manipulated
* @param {Ext.ux.ManagedIFrame} this
* Note: This event is only available when overwriting the iframe document using the update method.
*/
"domready" : true,

/**
* @event documentloaded
* Fires when the iFrame has reached a loaded/complete state.
* @param {Ext.ux.ManagedIFrame} this
*/
"documentloaded" : true
});

if(config.listeners){
this.listeners=config.listeners;
Ext.ux.ManagedIFrame.superclass.constructor.call(this);
}

Ext.apply(el,this); // apply this class interface ( pseudo Decorator )

el.addClass('x-managed-iframe');

el.loadMask = Ext.apply({msg:'Loading..',msgCls:'x-mask-loading',maskEl:null, enabled:!!config.loadMask},config.loadMask);

//Hook the Iframes loaded state handler
var h= Ext.isIE?'onreadystatechange':'onload';
el.dom[h] = el.dom[h]?
el.dom[h].createSequence(el.loadHandler,el):
el.loadHandler.createDelegate(el);

if(config.src){
el.setSrc(config.src);
}else{
el.src = el.dom.src||null;
var content = config.html || config.content || false;

if(content){
el.update(content);
}
}

return el;
};

Ext.extend(Ext.ux.ManagedIFrame , Ext.util.Observable,
{
/**
* Sets the embedded Iframe src property.

* @param {String/Function} url (Optional) A string or reference to a Function that returns a URI string when called
* @param {Boolean} discardUrl (Optional) If not passed as <tt>false</tt> the URL of this action becomes the default SRC attribute for
* this iframe, and will be subsequently used in future setSrc calls (emulates autoRefresh by calling setSrc without params).
* Note: invoke the function with no arguments to refresh the iframe based on the current defaultSrc value.
*/
setSrc : function(url, discardUrl){
var src = url || this.src || (Ext.isIE&&Ext.isSecure?Ext.SSL_SECURE_URL:'');
this.showMask();
this._windowContext = null;
(function(){
var s = typeof src == 'function'?src()||'':src;
if(Ext.isOpera)this.dom.src="";
this.dom.src = s;
}).defer(100,this);
if(discardUrl !== true){ this.src = src; }
},
//Private: script removal RegeXp
scriptRE : /(?:<script.*?>)((\n|\r|.)*?)(?:<\/script>)/gi
,
/*
Write(replacing) string content into the IFrames document structure
* @param {String} content The new content
* @param {Boolean} loadScripts (optional) true to also render and process embedded scripts
* @param {Function} callback (optional) Callback when update is complete.
*/
update : function(content,loadScripts,callback){

loadScripts = loadScripts || this.getUpdateManager().loadScripts || false;

this._windowContext = false;
content = Ext.DomHelper.markup(content||'');

var doc = this.getDocument();
if(doc){
this._inUpdate = true;
this.showMask();

doc.open();
doc.write(loadScripts===true ? content:content.replace(this.scriptRE , ""));

//create an 'eval'able context for the iframe and this.execScript
doc.write ('<script type="text/javascript">(function(){'+
"var MSIE/*@cc_on =1@*/;"+ // IE sniff
"parent.Ext.get('"+this.dom.id +"')._windowContext=MSIE?this:{eval:function(s){return eval(s);}}"+
"})();<\/script>" );
doc.close();

if(!!content.length){
this.checkDOM(false,callback);
} else if(callback){
callback();
}
}
return this;
},
_windowContext : null,
/*
Return the Iframes document object
*/
getDocument:function(){
return this.getWindow().document;
},

/*
Return the Iframes window object
*/
getWindow:function(){
var dom= this.dom;
return dom?dom.contentWindow||window.frames[dom.name]:window;
},

/*
Print the contents of the Iframes (if we own the document)
*/
print:function(){
try{
var win = this.getWindow();
if(Ext.isIE){win.focus();}
win.print();
} catch(ex){
throw 'print exception: ' + (ex.description || ex);
}
},
//private
destroy:function(){
this.removeAllListeners();
if(this.dom){
//unHook the Iframes loaded state handlers
this.dom.onreadystatechange=null;
this.dom.onload =null;

//IE Iframe cleanup
if(this.dom.src){
this.dom.src = 'javascript:false';
}
Ext.removeNode(this.dom);
}
this._windowContext = null;
Ext.apply(this.loadMask,{masker :null ,maskEl : null});
}
/*
Execute a javascript code block(string) within the context of the Iframes window object.
* @param {String} block A valid ('eval'able) script source block.
* <p> Note: execScript will only work after a successful update (document.write);
*/
,execScript: function(block){
if(this._windowContext){
return this._windowContext.eval( block );
} else {
throw 'execScript:no script context';
}
}

,loadMask: {msg:'Loading..',msgCls:'x-mask-loading',maskEl:null, enabled:false}

//Private
,showMask: function(msg,msgCls,forced){
if(this.loadMask && (this.loadMask.enabled || forced)){
var lmask = this.loadMask; //Wrap the Iframe if no masking ELement is available.
lmask.masker || (lmask.masker = Ext.get(lmask.maskEl||this.dom.parentNode||this.wrap({tag:'div',style:{position:'relative'}})));
//lmask.masker.mask(msg||lmask.msg , msgCls||lmask.msgCls );
lmask.masker.mask.defer(lmask.defer||150,lmask.masker,[msg||lmask.msg , msgCls||lmask.msgCls] );
}
}
//Private
,hideMask: function(forced){
var tlm = this.loadMask;
if(tlm && tlm.masker && (tlm.enabled || forced) &&
(forced || !!this.dom.src.length || this._inUpdate)){
tlm.masker.unmask.defer(tlm.defer||150,tlm.masker);
}
}

/* Private
Evaluate the Iframes readyState/load event to determine its 'load' state,
and raise the 'documentloaded' event when applicable.
*/
,loadHandler : function(e){
var rstatus = this.dom.readyState || e.type;
switch(rstatus){
case 'loading':
this.showMask();
break;
case 'load':
case 'complete':
this.fireEvent("documentloaded",this);
this.hideMask();
this._inUpdate = false;
break;
default:
}
}
/* Private
Poll the Iframes document structure to determine DOM ready state,
and raise the 'domready' event when applicable.
*/
,checkDOM : function(win,callback){
//initialise the counter
var n = 0
,win = win||this.getWindow()
,manager = this;
var t =function(){ //DOM polling


var domReady=false;
//if DOM methods are supported, and the body element exists
//(using a double-check including document.body, for the benefit of older moz builds [eg ns7.1]
//in which getElementsByTagName('body')[0] is undefined, unless this script is in the body section)

domReady = (win.document && typeof win.document.getElementsByTagName != 'undefined'
&& ( win.document.getElementsByTagName('body')[0] != null || win.document.body != null ));
//if the timer has reached 70 (timeout after ~10.5 seconds)
//in practice, shouldn't take longer than 7 iterations [in kde 3
//in second place was IE6, which takes 2 or 3 iterations roughly 5% of the time]
if(n++ < 70 && !domReady)
{
//try again
t.defer(50);
return;
}
if(callback)callback();
manager.fireEvent("domready",manager); //fallback
};
t();
}
});

/*
* @class Ext.ux.ManagedIFramePanel
* Version: RC1
* Adds unsupportedText property to render an element/text indicating lack of Iframe support
* Improves el visibility/display support when hiding panels (FF does not reload iframe if using visibility mode)
* Adds custom renderer definition to autoLoad config.
* Version: 0.16
* fixed (inherited)panel destroy bugs and iframe cleanup. (now, no orphans/leaks for IE).
* added loadMask.enabled = (true/false) toggle
* Requesting the Panel.getUpdater now returns the Updater for the Iframe.
* MIP.load modified to load content into panel.iframe (rather than panel.body)
* Version: 0.15
* enhanced loadMask.maskEl support to support panel element names ie: 'body, bwrap' etc
* Version: 0.13
* Added loadMask support and refactored domready/documentloaded events
* Version: 0.11
* Made Panel state-aware.
* Version: 0.1
* Author: Doug Hendricks 12/2007 doug[always-At]theactivegroup.com
*
*
*/
Ext.ux.ManagedIframePanel = Ext.extend(Ext.Panel, {

/**
* Cached Iframe.src url to use for refreshes. Overwritten every time setSrc() is called unless "discardUrl" param is set to true.
* @type String/Function (which will return a string URL when invoked)
*/
defaultSrc :null,
bodyStyle:{width:'100%',height:'100%'},
/**
* @cfg {String/Object} iframeStyle
* Custom CSS styles to be applied to the ux.ManagedIframe element in the format expected by {@link Ext.Element#applyStyles}
* (defaults to {overflow:'auto'}).
*/
iframeStyle : {overflow:'auto'},
loadMask : false,
animCollapse: false,
autoScroll : false,
closable : true, /* set True by default in the event a site times-out while loadMasked */
ctype : "Ext.ux.ManagedIframePanel",

/**
*@cfg {String/Object} unsupportedText Text (or Ext.DOMHelper config) to display within the rendered iframe tag to indicate the frame is not supported
*/
unsupportedText : {tag:'span'
,cls:'x-error-noframes'
,html:'Inline frames are NOT enabled\/supported by your browser.'
},

initComponent : function(){

var unsup = false;
if(this.unsupportedText){
unsup =typeof this.unsupportedText == 'object'? {children:[this.unsupportedText]}:{html:this.unsupportedText};
}

this.bodyCfg ||
(this.bodyCfg =
{tag:'div'
,cls:'x-panel-body'
,children:[Ext.apply({tag:'iframe',
frameBorder :0,
cls :'x-managed-iframe',
style :{width:'100%',height:'100%'}
},unsup)
]
});

Ext.ux.ManagedIframePanel.superclass.initComponent.call(this);
this.addEvents({documentloaded:true, domready:true});

if(this.defaultSrc){
this.on('render', this.setSrc.createDelegate(this,[this.defaultSrc],0), this, {single:true});
}
},

// private
beforeDestroy : function(){

if(this.rendered){

if(this.tools){
for(var k in this.tools){
Ext.destroy(this.tools[k]);
}
}

if(this.header && this.headerAsText){
var s;
if( s=this.header.child('span')) s.remove();
this.header.update('');
}


Ext.each(['iframe','header','topToolbar','bottomToolbar','footer','loadMask','body','bwrap'],
function(elName){
if(this[elName]){

if(typeof this[elName].destroy == 'function'){
this[elName].destroy();
} else { Ext.destroy(this[elName]); }

this[elName] = null;
delete this[elName];
}
},this);
}

Ext.ux.ManagedIframePanel.superclass.beforeDestroy.call(this);
},
onDestroy : function(){
//Yes, Panel.super (Component), since we're doing Panel cleanup beforeDestroy instead.
Ext.Panel.superclass.onDestroy.call(this);
},
// private
onRender : function(ct, position){
Ext.ux.ManagedIframePanel.superclass.onRender.call(this, ct, position);

if(this.iframe = this.body.child('iframe.x-managed-iframe')){

// Set the Visibility Mode for el, bwrap for collapse/expands/hide/show
Ext.each(
[this[this.collapseEl],this.el]
,function(el){
el.setVisibilityMode(Ext.Element[this.hideMode.toUpperCase()] || 1).originalDisplay = (this.hideMode != 'display'?'visible':'block');
},this);

if(this.loadMask){
this.loadMask = Ext.apply({enabled:true,maskEl:this.body},this.loadMask);

}
this.iframe = new Ext.ux.ManagedIFrame(this.iframe, {loadMask:this.loadMask});
this.loadMask = this.iframe.loadMask;
this.iframe.ownerCt = this;

this.relayEvents(this.iframe, ["documentloaded","domready"]);
if(this.iframeStyle){
this.iframe.applyStyles(this.iframeStyle);
}

this.getUpdater().showLoadIndicator = !this.loadMask.enabled;


}
},
// private
afterRender : function(container){
var html = this.html;
delete this.html;
Ext.ux.ManagedIframePanel.superclass.afterRender.call(this);
if(html && this.iframe){
this.iframe.update(typeof html == 'object' ? Ext.DomHelper.markup(html) : html);
}

},
/**
* Sets the embedded Iframe src property.
* @param {String/Function} url (Optional) A string or reference to a Function that returns a URI string when called
* @param {Boolean} discardUrl (Optional) If not passed as <tt>false</tt> the URL of this action becomes the default URL for
* this panel, and will be subsequently used in future setSrc calls.
* Note: invoke the function with no arguments to refresh the iframe based on the current defaultSrc value.
*/
setSrc : function(url, discardUrl){
var src = url || this.defaultSrc || (Ext.isIE&&Ext.isSecure?Ext.SSL_SECURE_URL:'');
if(this.rendered && this.iframe){
this.iframe.setSrc(src,discardUrl);
}
if(discardUrl !== true){ this.defaultSrc = src; }
this.saveState();
return this;
},

//Make it state-aware
getState: function(){
return Ext.apply(Ext.ux.ManagedIframePanel.superclass.getState.call(this) || {},
{defaultSrc :(typeof this.defaultSrc == 'function')?this.defaultSrc():this.defaultSrc});
},
/**
* Get the {@link Ext.Updater} for this panel's iframe/or body. Enables you to perform Ajax-based document replacement of this panel's iframe document.
* @return {Ext.Updater} The Updater
*/
getUpdater : function(){
return this.rendered?(this.iframe||this.body).getUpdater():false;
},
/**
* Loads this panel's iframe immediately with content returned from an XHR call.
* @param {Object/String/Function} config A config object containing any of the following options:
<pre><code>
panel.load({
url: "your-url.php",
params: {param1: "foo", param2: "bar"}, // or a URL encoded string
callback: yourFunction,
scope: yourObject, // optional scope for the callback
discardUrl: false,
nocache: false,
text: "Loading...",
timeout: 30,
scripts: false,
renderer:{render:function(el, response, updater, callback){....}} //optional custom renderer
});
</code></pre>
* The only required property is url. The optional properties nocache, text and scripts
* are shorthand for disableCaching, indicatorText and loadScripts and are used to set their
* associated property on this panel Updater instance.
* @return {Ext.Panel} this
*/
load : function(loadCfg){
var um;
if(um = this.getUpdater()){
if (loadCfg && loadCfg.renderer) {
um.setRenderer(loadCfg.renderer);
delete loadCfg.renderer;
}
um.update.apply(um, arguments);
}
return this;
}
// private
,doAutoLoad : function(){
this.load(
typeof this.autoLoad == 'object' ?
this.autoLoad : {url: this.autoLoad});
}
// private
,onShow : function(){
this.body.setVisible(true);
Ext.ux.ManagedIframePanel.superclass.onShow.call(this);
}


// private
,onHide : function(){
this.body.setVisible(false);
Ext.ux.ManagedIframePanel.superclass.onHide.call(this);
}
});

Ext.reg('iframepanel', Ext.ux.ManagedIframePanel);

Ext.ux.ManagedIframePortlet = Ext.extend(Ext.ux.ManagedIframePanel, {
anchor: '100%',
frame:true,
collapseEl:'bwrap',
collapsible:true,
draggable:true,
cls:'x-portlet'
});
Ext.reg('iframeportlet', Ext.ux.ManagedIframePortlet);
//version 3.0

Ext.ux.Multiselect = Ext.extend(Ext.form.Field, {
store:null,
dataFields:[],
data:[],
width:100,
height:100,
displayField:0,
valueField:1,
allowBlank:true,
minLength:0,
maxLength:Number.MAX_VALUE,
blankText:Ext.form.TextField.prototype.blankText,
minLengthText:'Minimum {0} item(s) required',
maxLengthText:'Maximum {0} item(s) allowed',
copy:false,
allowDup:false,
allowTrash:false,
legend:null,
focusClass:undefined,
delimiter:',',
view:null,
dragGroup:null,
dropGroup:null,
tbar:null,
appendOnly:false,
sortField:null,
sortDir:'ASC',
textDelimiter:'|',
defaultAutoCreate : {tag: "div"},

initComponent: function(){
Ext.ux.Multiselect.superclass.initComponent.call(this);
this.addEvents({
'dblclick' : true,
'click' : true,
'change' : true,
'drop' : true
});
},
onRender: function(ct, position){
var fs, cls, tpl;
Ext.ux.Multiselect.superclass.onRender.call(this, ct, position);

cls = 'ux-mselect';

fs = new Ext.form.FieldSet({
renderTo:this.el,
title:this.legend,
height:this.height,
width:this.width,
style:"padding:1px;",
tbar:this.tbar
});
if(!this.legend) { var e = fs.el.down('.'+fs.headerCls); if(e) {e.remove();}}
fs.body.addClass(cls);

tpl = '<tpl for="."><div class="' + cls + '-item';
if(Ext.isIE || Ext.isIE7)tpl+='" unselectable=on';
else tpl+=' x-unselectable"';
tpl+='>{' + this.displayField + '}</div></tpl>';

if(!this.store){
this.store = new Ext.data.SimpleStore({
fields: this.dataFields,
data : this.data
});
}

this.view = new Ext.ux.DDView({
multiSelect: true, store: this.store, selectedClass: cls+"-selected", tpl:tpl,
allowDup:this.allowDup, copy: this.copy, allowTrash: this.allowTrash,
dragGroup: this.dragGroup, dropGroup: this.dropGroup, itemSelector:"."+cls+"-item",
isFormField:false, applyTo:fs.body, appendOnly:this.appendOnly,
sortField:this.sortField, sortDir:this.sortDir
});

fs.add(this.view);

this.view.on('click', this.onViewClick, this);
this.view.on('beforeClick', this.onViewBeforeClick, this);
this.view.on('dblclick', this.onViewDblClick, this);
this.view.on('drop', function(ddView, n, dd, e, data){
return this.fireEvent("drop", ddView, n, dd, e, data);
}, this);

this.hiddenName = this.name;
var hiddenTag={tag: "input", type: "hidden", value: "", name:this.name};
if (this.isFormField) {
this.hiddenField = this.el.createChild(hiddenTag);
} else {
this.hiddenField = Ext.get(document.body).createChild(hiddenTag);
}
fs.doLayout();
},

initValue:Ext.emptyFn,

onViewClick: function(vw, index, node, e) {
var arrayIndex = this.preClickSelections.indexOf(index);
if (arrayIndex != -1)
{
this.preClickSelections.splice(arrayIndex, 1);
this.view.clearSelections(true);
this.view.select(this.preClickSelections);
}
this.fireEvent('change', this, this.getValue(), this.hiddenField.dom.value);
this.hiddenField.dom.value = this.getValue();
this.fireEvent('click', this, e);
this.validate();
},

onViewBeforeClick: function(vw, index, node, e) {
this.preClickSelections = this.view.getSelectedIndexes();
if (this.disabled) {return false;}
},

onViewDblClick : function(vw, index, node, e) {
return this.fireEvent('dblclick', vw, index, node, e);
},

getValue: function(valueField){
if(this.view)
{
var returnArray = [];
var selectionsArray = this.view.getSelectedIndexes();
if (selectionsArray.length == 0) {return '';}
for (var i=0; i<selectionsArray.length; i++) {
returnArray.push(this.store.getAt(selectionsArray[i]).get(((valueField != null)? valueField : this.valueField)));
}
return returnArray.join(this.delimiter);
}
else
{
return "";
}
},

setValue: function(values) {
var index;
var selections = [];
this.view.clearSelections();
this.hiddenField.dom.value = '';

if (!values || (values == '')) { return; }

if (!(values instanceof Array)) { values = values.split(this.delimiter); }
for (var i=0; i<values.length; i++) {
index = this.view.store.indexOf(this.view.store.query(this.valueField,
new RegExp('^' + values[i] + '$', "i")).itemAt(0));
selections.push(index);
}
this.view.select(selections);
this.hiddenField.dom.value = this.getValue();
this.validate();
},

reset : function() {
this.setValue('');
},

getRawValue: function(valueField) {
var tmp = this.getValue(valueField);
if (tmp.length) {
tmp = tmp.split(this.delimiter);
}
else{
tmp = [];
}
return tmp;
},

setRawValue: function(values){
setValue(values);
},

selectAll: function() {
var selections = [];
for (var i = 0; i < this.view.store.getTotalCount(); i++) {
selections.push(i);
}
this.view.select(selections);
this.hiddenField.dom.value = this.getValue();
this.validate();
},


getSelectedTexts: function() {
var selectedItems = this.view.getSelectedNodes();
var selectedTexts = [];
for (var i = 0; i < selectedItems.length; i++) {
selectedTexts.push(selectedItems[i].innerHTML);
}
return selectedTexts.join(this.textDelimiter);
},

validateValue : function(value){
if (value.length < 1) { // if it has no value
if (this.allowBlank) {
this.clearInvalid();
return true;
} else {
this.markInvalid(this.blankText);
return false;
}
}
if (value.length < this.minLength) {
this.markInvalid(String.format(this.minLengthText, this.minLength));
return false;
}
if (value.length > this.maxLength) {
this.markInvalid(String.format(this.maxLengthText, this.maxLength));
return false;
}
return true;
},

onDestroy: function() {
if( this.view ) {
this.view.destroy();
}
Ext.ux.Multiselect.superclass.onDestroy.call(this);
}
});

Ext.reg("multiselect", Ext.ux.Multiselect);

Ext.ux.ItemSelector = Ext.extend(Ext.form.Field, {
msWidth:200,
msHeight:300,
hideNavIcons:false,
imagePath:"",
iconUp:"up2.gif",
iconDown:"down2.gif",
iconLeft:"left2.gif",
iconRight:"right2.gif",
iconTop:"top2.gif",
iconBottom:"bottom2.gif",
drawUpIcon:true,
drawDownIcon:true,
drawLeftIcon:true,
drawRightIcon:true,
drawTopIcon:true,
drawBotIcon:true,
fromStore:null,
toStore:null,
fromData:null,
toData:null,
displayField:0,
valueField:1,
switchToFrom:false,
allowDup:false,
focusClass:undefined,
delimiter:',',
readOnly:false,
toLegend:null,
fromLegend:null,
toSortField:null,
fromSortField:null,
toSortDir:'ASC',
fromSortDir:'ASC',
toTBar:null,
fromTBar:null,
bodyStyle:null,
border:false,
defaultAutoCreate:{tag: "div"},

initComponent: function(){
Ext.ux.ItemSelector.superclass.initComponent.call(this);
this.addEvents({
'rowdblclick' : true,
'change' : true
});
},

onRender: function(ct, position){
Ext.ux.ItemSelector.superclass.onRender.call(this, ct, position);

this.fromMultiselect = new Ext.ux.Multiselect({
legend: this.fromLegend,
delimiter: this.delimiter,
allowDup: this.allowDup,
copy: this.allowDup,
allowTrash: this.allowDup,
dragGroup: this.readOnly ? null : "drop2-"+this.el.dom.id,
dropGroup: this.readOnly ? null : "drop2-"+this.el.dom.id+",drop1-"+this.el.dom.id,
width: this.msWidth,
height: this.msHeight,
dataFields: this.dataFields,
data: this.fromData,
displayField: this.displayField,
valueField: this.valueField,
store: this.fromStore,
isFormField: false,
tbar: this.fromTBar,
appendOnly: true,
sortField: this.fromSortField,
sortDir: this.fromSortDir
});
this.fromMultiselect.on('dblclick', this.onRowDblClick, this);

if (!this.toStore) {
this.toStore = new Ext.data.SimpleStore({
fields: this.dataFields,
data : this.toData
});
}
this.toStore.on('add', this.valueChanged, this);
this.toStore.on('remove', this.valueChanged, this);
this.toStore.on('load', this.valueChanged, this);

this.toMultiselect = new Ext.ux.Multiselect({
legend: this.toLegend,
delimiter: this.delimiter,
allowDup: this.allowDup,
dragGroup: this.readOnly ? null : "drop1-"+this.el.dom.id,
dropGroup: this.readOnly ? null : "drop2-"+this.el.dom.id+",drop1-"+this.el.dom.id,
width: this.msWidth,
height: this.msHeight,
displayField: this.displayField,
valueField: this.valueField,
store: this.toStore,
isFormField: false,
tbar: this.toTBar,
sortField: this.toSortField,
sortDir: this.toSortDir
});
this.toMultiselect.on('dblclick', this.onRowDblClick, this);

var p = new Ext.Panel({
bodyStyle:this.bodyStyle,
border:this.border,
layout:"table",
layoutConfig:{columns:3}
});
p.add(this.switchToFrom ? this.toMultiselect : this.fromMultiselect);
var icons = new Ext.Panel({header:false});
p.add(icons);
p.add(this.switchToFrom ? this.fromMultiselect : this.toMultiselect);
p.render(this.el);
icons.el.down('.'+icons.bwrapCls).remove();

if (this.imagePath!="" && this.imagePath.charAt(this.imagePath.length-1)!="/")
this.imagePath+="/";
this.iconUp = this.imagePath + (this.iconUp || 'up2.gif');
this.iconDown = this.imagePath + (this.iconDown || 'down2.gif');
this.iconLeft = this.imagePath + (this.iconLeft || 'left2.gif');
this.iconRight = this.imagePath + (this.iconRight || 'right2.gif');
this.iconTop = this.imagePath + (this.iconTop || 'top2.gif');
this.iconBottom = this.imagePath + (this.iconBottom || 'bottom2.gif');
var el=icons.getEl();
if (!this.toSortField) {
this.toTopIcon = el.createChild({tag:'img', src:this.iconTop, style:{cursor:'pointer', margin:'2px'}});
el.createChild({tag: 'br'});
this.upIcon = el.createChild({tag:'img', src:this.iconUp, style:{cursor:'pointer', margin:'2px'}});
el.createChild({tag: 'br'});
}
this.addIcon = el.createChild({tag:'img', src:this.switchToFrom?this.iconLeft:this.iconRight, style:{cursor:'pointer', margin:'2px'}});
el.createChild({tag: 'br'});
this.removeIcon = el.createChild({tag:'img', src:this.switchToFrom?this.iconRight:this.iconLeft, style:{cursor:'pointer', margin:'2px'}});
el.createChild({tag: 'br'});
if (!this.toSortField) {
this.downIcon = el.createChild({tag:'img', src:this.iconDown, style:{cursor:'pointer', margin:'2px'}});
el.createChild({tag: 'br'});
this.toBottomIcon = el.createChild({tag:'img', src:this.iconBottom, style:{cursor:'pointer', margin:'2px'}});
}
if (!this.readOnly) {
if (!this.toSortField) {
this.toTopIcon.on('click', this.toTop, this);
this.upIcon.on('click', this.up, this);
this.downIcon.on('click', this.down, this);
this.toBottomIcon.on('click', this.toBottom, this);
}
this.addIcon.on('click', this.fromTo, this);
this.removeIcon.on('click', this.toFrom, this);
}
if (!this.drawUpIcon || this.hideNavIcons) { this.upIcon.dom.style.display='none'; }
if (!this.drawDownIcon || this.hideNavIcons) { this.downIcon.dom.style.display='none'; }
if (!this.drawLeftIcon || this.hideNavIcons) { this.addIcon.dom.style.display='none'; }
if (!this.drawRightIcon || this.hideNavIcons) { this.removeIcon.dom.style.display='none'; }
if (!this.drawTopIcon || this.hideNavIcons) { this.toTopIcon.dom.style.display='none'; }
if (!this.drawBotIcon || this.hideNavIcons) { this.toBottomIcon.dom.style.display='none'; }

var tb = p.body.first();
this.el.setWidth(p.body.first().getWidth());
p.body.removeClass();

this.hiddenName = this.name;
var hiddenTag={tag: "input", type: "hidden", value: "", name:this.name};
this.hiddenField = this.el.createChild(hiddenTag);
this.valueChanged(this.toStore);
},

initValue:Ext.emptyFn,

toTop : function() {
var selectionsArray = this.toMultiselect.view.getSelectedIndexes();
var records = [];
if (selectionsArray.length > 0) {
selectionsArray.sort();
for (var i=0; i<selectionsArray.length; i++) {
record = this.toMultiselect.view.store.getAt(selectionsArray[i]);
records.push(record);
}
selectionsArray = [];
for (var i=records.length-1; i>-1; i--) {
record = records[i];
this.toMultiselect.view.store.remove(record);
this.toMultiselect.view.store.insert(0, record);
selectionsArray.push(((records.length - 1) - i));
}
}
this.toMultiselect.view.refresh();
this.toMultiselect.view.select(selectionsArray);
},


toBottom : function() {
var selectionsArray = this.toMultiselect.view.getSelectedIndexes();
var records = [];
if (selectionsArray.length > 0) {
selectionsArray.sort();
for (var i=0; i<selectionsArray.length; i++) {
record = this.toMultiselect.view.store.getAt(selectionsArray[i]);
records.push(record);
}
selectionsArray = [];
for (var i=0; i<records.length; i++) {
record = records[i];
this.toMultiselect.view.store.remove(record);
this.toMultiselect.view.store.add(record);
selectionsArray.push((this.toMultiselect.view.store.getCount()) - (records.length - i));
}
}
this.toMultiselect.view.refresh();
this.toMultiselect.view.select(selectionsArray);
},

up : function() {
var record = null;
var selectionsArray = this.toMultiselect.view.getSelectedIndexes();
selectionsArray.sort();
var newSelectionsArray = [];
if (selectionsArray.length > 0) {
for (var i=0; i<selectionsArray.length; i++) {
record = this.toMultiselect.view.store.getAt(selectionsArray[i]);
if ((selectionsArray[i] - 1) >= 0) {
this.toMultiselect.view.store.remove(record);
this.toMultiselect.view.store.insert(selectionsArray[i] - 1, record);
newSelectionsArray.push(selectionsArray[i] - 1);
}
}
this.toMultiselect.view.refresh();
this.toMultiselect.view.select(newSelectionsArray);
}
},

down : function() {
var record = null;
var selectionsArray = this.toMultiselect.view.getSelectedIndexes();
selectionsArray.sort();
selectionsArray.reverse();
var newSelectionsArray = [];
if (selectionsArray.length > 0) {
for (var i=0; i<selectionsArray.length; i++) {
record = this.toMultiselect.view.store.getAt(selectionsArray[i]);
if ((selectionsArray[i] + 1) < this.toMultiselect.view.store.getCount()) {
this.toMultiselect.view.store.remove(record);
this.toMultiselect.view.store.insert(selectionsArray[i] + 1, record);
newSelectionsArray.push(selectionsArray[i] + 1);
}
}
this.toMultiselect.view.refresh();
this.toMultiselect.view.select(newSelectionsArray);
}
},

fromTo : function() {
var selectionsArray = this.fromMultiselect.view.getSelectedIndexes();
var records = [];
if (selectionsArray.length > 0) {
for (var i=0; i<selectionsArray.length; i++) {
record = this.fromMultiselect.view.store.getAt(selectionsArray[i]);
records.push(record);
}
if(!this.allowDup)selectionsArray = [];
for (var i=0; i<records.length; i++) {
record = records[i];
if(this.allowDup){
var x=new Ext.data.Record();
record.id=x.id;
delete x;
this.toMultiselect.view.store.add(record);
}else{
this.fromMultiselect.view.store.remove(record);
this.toMultiselect.view.store.add(record);
selectionsArray.push((this.toMultiselect.view.store.getCount() - 1));
}
}
}
this.toMultiselect.view.refresh();
this.fromMultiselect.view.refresh();
if(this.toSortField)this.toMultiselect.store.sort(this.toSortField, this.toSortDir);
if(this.allowDup)this.fromMultiselect.view.select(selectionsArray);
else this.toMultiselect.view.select(selectionsArray);
},

toFrom : function() {
var selectionsArray = this.toMultiselect.view.getSelectedIndexes();
var records = [];
if (selectionsArray.length > 0) {
for (var i=0; i<selectionsArray.length; i++) {
record = this.toMultiselect.view.store.getAt(selectionsArray[i]);
records.push(record);
}
selectionsArray = [];
for (var i=0; i<records.length; i++) {
record = records[i];
this.toMultiselect.view.store.remove(record);
if(!this.allowDup){
this.fromMultiselect.view.store.add(record);
selectionsArray.push((this.fromMultiselect.view.store.getCount() - 1));
}
}
}
this.fromMultiselect.view.refresh();
this.toMultiselect.view.refresh();
if(this.fromSortField)this.fromMultiselect.store.sort(this.fromSortField, this.fromSortDir);
this.fromMultiselect.view.select(selectionsArray);
},

valueChanged: function(store) {
var record = null;
var values = [];
for (var i=0; i<store.getCount(); i++) {
record = store.getAt(i);
values.push(record.get(this.valueField));
if(!this.allowDup) {
var index = this.fromMultiselect.view.store.find(this.valueField, record.get(this.valueField));
var rdup = this.fromMultiselect.view.store.getAt(index);

if(rdup) {
this.fromMultiselect.view.store.remove(rdup);
}
}
}
this.fromMultiselect.view.refresh();
this.fromMultiselect.store.sort(this.displayField,'ASC');

this.hiddenField.dom.value = values.join(this.delimiter);
this.fireEvent('change', this, this.getValue(), this.hiddenField.dom.value);
},

getValue : function() {
return this.hiddenField.dom.value;
},

onRowDblClick : function(vw, index, node, e) {
return this.fireEvent('rowdblclick', vw, index, node, e);
},

reset: function(){
range = this.toMultiselect.store.getRange();
this.toMultiselect.store.removeAll();
if (!this.allowDup) {
this.fromMultiselect.store.add(range);
this.fromMultiselect.store.sort(this.displayField,'ASC');
}
this.valueChanged(this.toMultiselect.store);
},

selectAll: function() {
var selections = [];
for (var i = 0; i < this.fromMultiselect.store.getCount(); i++) {
selections.push(i);
}
this.fromMultiselect.view.select(selections);
this.fromTo();
this.toMultiselect.view.clearSelections(true);
},

setValue: function(val)
{
if(!val) {
return;
}
val = val instanceof Array ? val : val.split(',');
var rec, i, id;
for(i = 0; i < val.length; i++) {
id = val[i];
if(this.toStore.find(valueField,id)) {
continue;
}
index = this.fromStore.find(valueField,id);
if(index) {
rec = this.fromStore.getAt(index)
this.toStore.add(rec);
this.fromStore.remove(rec);
}
}
},

onDestroy: function() {
if( this.fromMultiselect ) {
this.fromMultiselect.destroy();
}
if( this.toMultiselect ) {
this.toMultiselect.destroy();
}
Ext.ux.ItemSelector.superclass.onDestroy.call(this);
}
});

Ext.reg("itemselector", Ext.ux.ItemSelector);
Array.prototype.contains = function(element) {
return this.indexOf(element) !== -1;
};


Ext.namespace("Ext.ux");

/**
* @class Ext.ux.DDView
* A DnD enabled version of Ext.View.
* @param {Element/String} container The Element in which to create the View.
* @param {String} tpl The template string used to create the markup for each element of the View
* @param {Object} config The configuration properties. These include all the config options of
* {@link Ext.View} plus some specific to this class.<br>
* <p>
* Drag/drop is implemented by adding {@link Ext.data.Record}s to the target DDView. If copying is
* not being performed, the original {@link Ext.data.Record} is removed from the source DDView.<br>
* <p>
* The following extra CSS rules are needed to provide insertion point highlighting:<pre><code>
.x-view-drag-insert-above {
border-top:1px dotted #3366cc;
}
.x-view-drag-insert-below {
border-bottom:1px dotted #3366cc;
}
</code></pre>
*
*/
Ext.ux.DDView = function(config) {
if (!config.itemSelector) {
var tpl = config.tpl;
if (this.classRe.test(tpl)) {
config.tpl = tpl.replace(this.classRe, 'class=$1x-combo-list-item $2$1');
}
else {
config.tpl = tpl.replace(this.tagRe, '$1 class="x-combo-list-item" $2');
}
config.itemSelector = ".x-combo-list-item";
}
Ext.ux.DDView.superclass.constructor.call(this, Ext.apply(config, {
border: false
}));
};

Ext.extend(Ext.ux.DDView, Ext.DataView, {
/** @cfg {String/Array} dragGroup The ddgroup name(s) for the View's DragZone. */
/** @cfg {String/Array} dropGroup The ddgroup name(s) for the View's DropZone. */
/** @cfg {Boolean} copy Causes drag operations to copy nodes rather than move. */
/** @cfg {Boolean} allowCopy Causes ctrl/drag operations to copy nodes rather than move. */

sortDir: 'ASC',

isFormField: true,

classRe: /class=(['"])(.*)\1/,

tagRe: /(<\w*)(.*?>)/,

reset: Ext.emptyFn,

clearInvalid: Ext.form.Field.prototype.clearInvalid,

msgTarget: 'qtip',

afterRender: function() {
Ext.ux.DDView.superclass.afterRender.call(this);
if (this.dragGroup) {
this.setDraggable(this.dragGroup.split(","));
}
if (this.dropGroup) {
this.setDroppable(this.dropGroup.split(","));
}
if (this.deletable) {
this.setDeletable();
}
this.isDirtyFlag = false;
this.addEvents(
"drop"
);
},

validate: function() {
return true;
},

destroy: function() {
this.purgeListeners();
this.getEl().removeAllListeners();
this.getEl().remove();
if (this.dragZone) {
Ext.dd.ScrollManager.unregister(this.dragZone.el);
if (this.dragZone.destroy) {
this.dragZone.destroy();
}
}
if (this.dropZone) {
Ext.dd.ScrollManager.unregister(this.dropZone.el);
if (this.dropZone.destroy) {
this.dropZone.destroy();
}
}
},

/** Allows this class to be an Ext.form.Field so it can be found using {@link Ext.form.BasicForm#findField}. */
getName: function() {
return this.name;
},

/** Loads the View from a JSON string representing the Records to put into the Store. */
setValue: function(v) {
if (!this.store) {
throw "DDView.setValue(). DDView must be constructed with a valid Store";
}
var data = {};
data[this.store.reader.meta.root] = v ? [].concat(v) : [];
this.store.proxy = new Ext.data.MemoryProxy(data);
this.store.load();
},

/** @return {String} a parenthesised list of the ids of the Records in the View. */
getValue: function() {
var result = '(';
this.store.each(function(rec) {
result += rec.id + ',';
});
return result.substr(0, result.length - 1) + ')';
},

getIds: function() {
var i = 0, result = new Array(this.store.getCount());
this.store.each(function(rec) {
result[i++] = rec.id;
});
return result;
},

isDirty: function() {
return this.isDirtyFlag;
},

/**
* Part of the Ext.dd.DropZone interface. If no target node is found, the
* whole Element becomes the target, and this causes the drop gesture to append.
*/
getTargetFromEvent : function(e) {
var target = e.getTarget();
while ((target !== null) && (target.parentNode != this.el.dom)) {
target = target.parentNode;
}
if (!target) {
target = this.el.dom.lastChild || this.el.dom;
}
return target;
},

/**
* Create the drag data which consists of an object which has the property "ddel" as
* the drag proxy element.
*/
getDragData : function(e) {
var target = this.findItemFromChild(e.getTarget());
if(target) {
if (!this.isSelected(target)) {
delete this.ignoreNextClick;
this.onItemClick(target, this.indexOf(target), e);
this.ignoreNextClick = true;
}
var dragData = {
sourceView: this,
viewNodes: [],
records: [],
copy: this.copy || (this.allowCopy && e.ctrlKey)
};
if (this.getSelectionCount() == 1) {
var i = this.getSelectedIndexes()[0];
var n = this.getNode(i);
dragData.viewNodes.push(dragData.ddel = n);
dragData.records.push(this.store.getAt(i));
dragData.repairXY = Ext.fly(n).getXY();
} else {
dragData.ddel = document.createElement('div');
dragData.ddel.className = 'multi-proxy';
this.collectSelection(dragData);
}
return dragData;
}
return false;
},

// override the default repairXY.
getRepairXY : function(e){
return this.dragData.repairXY;
},

/** Put the selections into the records and viewNodes Arrays. */
collectSelection: function(data) {
data.repairXY = Ext.fly(this.getSelectedNodes()[0]).getXY();
if (this.preserveSelectionOrder === true) {
Ext.each(this.getSelectedIndexes(), function(i) {
var n = this.getNode(i);
var dragNode = n.cloneNode(true);
dragNode.id = Ext.id();
data.ddel.appendChild(dragNode);
data.records.push(this.store.getAt(i));
data.viewNodes.push(n);
}, this);
} else {
var i = 0;
this.store.each(function(rec){
if (this.isSelected(i)) {
var n = this.getNode(i);
var dragNode = n.cloneNode(true);
dragNode.id = Ext.id();
data.ddel.appendChild(dragNode);
data.records.push(this.store.getAt(i));
data.viewNodes.push(n);
}
i++;
}, this);
}
},

/** Specify to which ddGroup items in this DDView may be dragged. */
setDraggable: function(ddGroup) {
if (ddGroup instanceof Array) {
Ext.each(ddGroup, this.setDraggable, this);
return;
}
if (this.dragZone) {
this.dragZone.addToGroup(ddGroup);
} else {
this.dragZone = new Ext.dd.DragZone(this.getEl(), {
containerScroll: true,
ddGroup: ddGroup
});
// Draggability implies selection. DragZone's mousedown selects the element.
if (!this.multiSelect) { this.singleSelect = true; }


// Wire the DragZone's handlers up to methods in *this*
this.dragZone.getDragData = this.getDragData.createDelegate(this);
this.dragZone.getRepairXY = this.getRepairXY;
this.dragZone.onEndDrag = this.onEndDrag;
}
},

/** Specify from which ddGroup this DDView accepts drops. */
setDroppable: function(ddGroup) {
if (ddGroup instanceof Array) {
Ext.each(ddGroup, this.setDroppable, this);
return;
}
if (this.dropZone) {
this.dropZone.addToGroup(ddGroup);
} else {
this.dropZone = new Ext.dd.DropZone(this.getEl(), {
owningView: this,
containerScroll: true,
ddGroup: ddGroup
});

// Wire the DropZone's handlers up to methods in *this*
this.dropZone.getTargetFromEvent = this.getTargetFromEvent.createDelegate(this);
this.dropZone.onNodeEnter = this.onNodeEnter.createDelegate(this);
this.dropZone.onNodeOver = this.onNodeOver.createDelegate(this);
this.dropZone.onNodeOut = this.onNodeOut.createDelegate(this);
this.dropZone.onNodeDrop = this.onNodeDrop.createDelegate(this);
}
},

/** Decide whether to drop above or below a View node. */
getDropPoint : function(e, n, dd){
if (n == this.el.dom) { return "above"; }
var t = Ext.lib.Dom.getY(n), b = t + n.offsetHeight;
var c = t + (b - t) / 2;
var y = Ext.lib.Event.getPageY(e);
if(y <= c) {
return "above";
}else{
return "below";
}
},

isValidDropPoint: function(pt, n, data) {
if (!data.viewNodes || (data.viewNodes.length != 1)) {
return true;
}
var d = data.viewNodes[0];
if (d == n) {
return false;
}
if ((pt == "below") && (n.nextSibling == d)) {
return false;
}
if ((pt == "above") && (n.previousSibling == d)) {
return false;
}
return true;
},

onNodeEnter : function(n, dd, e, data){
if (this.highlightColor && (data.sourceView != this)) {
this.el.highlight(this.highlightColor);
}
return false;
},

onNodeOver : function(n, dd, e, data){
var dragElClass = this.dropNotAllowed;
var pt = this.getDropPoint(e, n, dd);
if (this.isValidDropPoint(pt, n, data)) {
if (this.appendOnly || this.sortField) {
return "x-tree-drop-ok-below";
}

// set the insert point style on the target node
if (pt) {
var targetElClass;
if (pt == "above"){
dragElClass = n.previousSibling ? "x-tree-drop-ok-between" : "x-tree-drop-ok-above";
targetElClass = "x-view-drag-insert-above";
} else {
dragElClass = n.nextSibling ? "x-tree-drop-ok-between" : "x-tree-drop-ok-below";
targetElClass = "x-view-drag-insert-below";
}
if (this.lastInsertClass != targetElClass){
Ext.fly(n).replaceClass(this.lastInsertClass, targetElClass);
this.lastInsertClass = targetElClass;
}
}
}
return dragElClass;
},

onNodeOut : function(n, dd, e, data){
this.removeDropIndicators(n);
},

onNodeDrop : function(n, dd, e, data){
if (this.fireEvent("drop", this, n, dd, e, data) === false) {
return false;
}
var pt = this.getDropPoint(e, n, dd);
var insertAt = (this.appendOnly || (n == this.el.dom)) ? this.store.getCount() : n.viewIndex;
if (pt == "below") {
insertAt++;
}

// Validate if dragging within a DDView
if (data.sourceView == this) {
// If the first element to be inserted below is the target node, remove it
if (pt == "below") {
if (data.viewNodes[0] == n) {
data.viewNodes.shift();
}
} else { // If the last element to be inserted above is the target node, remove it
if (data.viewNodes[data.viewNodes.length - 1] == n) {
data.viewNodes.pop();
}
}

// Nothing to drop...
if (!data.viewNodes.length) {
return false;
}

// If we are moving DOWN, then because a store.remove() takes place first,
// the insertAt must be decremented.
if (insertAt > this.store.indexOf(data.records[0])) {
insertAt--;
}
}

// Dragging from a Tree. Use the Tree's recordFromNode function.
if (data.node instanceof Ext.tree.TreeNode) {
var r = data.node.getOwnerTree().recordFromNode(data.node);
if (r) {
data.records = [ r ];
}
}

if (!data.records) {
alert("Programming problem. Drag data contained no Records");
return false;
}

for (var i = 0; i < data.records.length; i++) {
var r = data.records[i];
var dup = this.store.getById(r.id);
if (dup && (dd != this.dragZone)) {
if(!this.allowDup && !this.allowTrash){
Ext.fly(this.getNode(this.store.indexOf(dup))).frame("red", 1);
return true
}
var x=new Ext.data.Record();
r.id=x.id;
delete x;
}
if (data.copy) {
this.store.insert(insertAt++, r.copy());
} else {
if (data.sourceView) {
data.sourceView.isDirtyFlag = true;
data.sourceView.store.remove(r);
}
if(!this.allowTrash)this.store.insert(insertAt++, r);
}
if(this.sortField){
this.store.sort(this.sortField, this.sortDir);
}
this.isDirtyFlag = true;
}
this.dragZone.cachedTarget = null;
return true;
},

// Ensure the multi proxy is removed
onEndDrag: function(data, e) {
var d = Ext.get(this.dragData.ddel);
if (d && d.hasClass("multi-proxy")) {
d.remove();
//delete this.dragData.ddel;
}
},

removeDropIndicators : function(n){
if(n){
Ext.fly(n).removeClass([
"x-view-drag-insert-above",
"x-view-drag-insert-left",
"x-view-drag-insert-right",
"x-view-drag-insert-below"]);
this.lastInsertClass = "_noclass";
}
},

/**
* Utility method. Add a delete option to the DDView's context menu.
* @param {String} imageUrl The URL of the "delete" icon image.
*/
setDeletable: function(imageUrl) {
if (!this.singleSelect && !this.multiSelect) {
this.singleSelect = true;
}
var c = this.getContextMenu();
this.contextMenu.on("itemclick", function(item) {
switch (item.id) {
case "delete":
this.remove(this.getSelectedIndexes());
break;
}
}, this);
this.contextMenu.add({
icon: imageUrl || AU.resolveUrl("/images/delete.gif"),
id: "delete",
text: AU.getMessage("deleteItem")
});
},

/** Return the context menu for this DDView. */
getContextMenu: function() {
if (!this.contextMenu) {
// Create the View's context menu
this.contextMenu = new Ext.menu.Menu({
id: this.id + "-contextmenu"
});
this.el.on("contextmenu", this.showContextMenu, this);
}
return this.contextMenu;
},

disableContextMenu: function() {
if (this.contextMenu) {
this.el.un("contextmenu", this.showContextMenu, this);
}
},


showContextMenu: function(e, item) {
item = this.findItemFromChild(e.getTarget());
if (item) {
e.stopEvent();
this.select(this.getNode(item), this.multiSelect && e.ctrlKey, true);
this.contextMenu.showAt(e.getXY());
}
},

/**
* Remove {@link Ext.data.Record}s at the specified indices.
* @param {Array/Number} selectedIndices The index (or Array of indices) of Records to remove.
*/
remove: function(selectedIndices) {
selectedIndices = [].concat(selectedIndices);
for (var i = 0; i < selectedIndices.length; i++) {
var rec = this.store.getAt(selectedIndices[i]);
this.store.remove(rec);
}
},

/**
* Double click fires the event, but also, if this is draggable, and there is only one other
* related DropZone that is in another DDView, it drops the selected node on that DDView.
*/
onDblClick : function(e){
var item = this.findItemFromChild(e.getTarget());
if(item){
if (this.fireEvent("dblclick", this, this.indexOf(item), item, e) === false) {
return false;
}
if (this.dragGroup) {
var targets = Ext.dd.DragDropMgr.getRelated(this.dragZone, true);

// Remove instances of this View's DropZone
while (targets.contains(this.dropZone)) {
targets.remove(this.dropZone);
}

// If there's only one other DropZone, and it is owned by a DDView, then drop it in
if ((targets.length == 1) && (targets[0].owningView)) {
this.dragZone.cachedTarget = null;
var el = Ext.get(targets[0].getEl());
var box = el.getBox(true);
targets[0].onNodeDrop(el.dom, {
target: el.dom,
xy: [box.x, box.y + box.height - 1]
}, null, this.getDragData(e));
}
}
}
},

onItemClick : function(item, index, e){
// The DragZone's mousedown->getDragData already handled selection
if (this.ignoreNextClick) {
delete this.ignoreNextClick;
return;
}

if(this.fireEvent("beforeclick", this, index, item, e) === false){
return false;
}
if(this.multiSelect || this.singleSelect){
if(this.multiSelect && e.shiftKey && this.lastSelection){
this.select(this.getNodes(this.indexOf(this.lastSelection), index), false);
} else if (this.isSelected(item) && e.ctrlKey) {
this.deselect(item);
}else{
this.deselect(item);
this.select(item, this.multiSelect && e.ctrlKey);
this.lastSelection = item;
}
e.preventDefault();
}
return true;
}
});

/*
* Ext JS Library 2.0 RC 1
* Copyright(c) 2006-2007, Ext JS, LLC.
* licensing@extjs.com
*
* http://extjs.com/license
*/

/**
* @class Ext.MultiMonthCalendar
* @extends Ext.Component
* Multi-month Calendar
* @constructor
* @param {Object} config The config object
*/
Ext.ux.MultiMonthCalendar = Ext.extend(Ext.Component, {
/**
* @cfg {Date} minDate
* Minimum allowable date (JavaScript date object, defaults to null)
*/
minDate : null,
/**
* @cfg {Date} maxDate
* Maximum allowable date (JavaScript date object, defaults to null)
*/
maxDate : null,
/**
* @cfg {String} minText
* The error text to display if the minDate validation fails (defaults to "This date is before the minimum date")
*/
minText : "This date is before the minimum date",
/**
* @cfg {String} maxText
* The error text to display if the maxDate validation fails (defaults to "This date is after the maximum date")
*/
maxText : "This date is after the maximum date",
/**
* @cfg {String} format
* The default date format string which can be overriden for localization support. The format must be
* valid according to {@link Date#parseDate} (defaults to 'm/d/y').
*/
format : "m/d/y",
/**
* @cfg {Array} disabledDays
* An array of days to disable, 0-based. For example, [0, 6] disables Sunday and Saturday (defaults to null).
*/
disabledDays : null,
/**
* @cfg {String} disabledDaysText
* The tooltip to display when the date falls on a disabled day (defaults to "")
*/
disabledDaysText : "",
/**
* @cfg {RegExp} disabledDatesRE
* JavaScript regular expression used to disable a pattern of dates (defaults to null)
*/
disabledDatesRE : null,
/**
* @cfg {String} disabledDatesText
* The tooltip text to display when the date falls on a disabled date (defaults to "")
*/
disabledDatesText : "",
/**
* @cfg {Boolean} constrainToViewport
* True to constrain the date picker to the viewport (defaults to true)
*/
constrainToViewport : true,
/**
* @cfg {Array} monthNames
* An array of textual month names which can be overriden for localization support (defaults to Date.monthNames)
*/
monthNames : Date.monthNames,
/**
* @cfg {Array} dayNames
* An array of textual day names which can be overriden for localization support (defaults to Date.dayNames)
*/
dayNames : Date.dayNames,
/**
* @cfg {String} nextText
* The next month navigation button tooltip (defaults to 'Next Month (Control+Right)')
*/
nextText: 'Next Month (Control+Right)',
/**
* @cfg {String} prevText
* The previous month navigation button tooltip (defaults to 'Previous Month (Control+Left)')
*/
prevText: 'Previous Month (Control+Left)',
/**
* @cfg {Number} startDay
* Day index at which the week should begin, 0-based (defaults to 0, which is Sunday)
*/
startDay : 0,
/**
* @cfg {Number} noOfMonth
* No of Month to be displayed
*/
noOfMonth : 2,
/**
* @cfg {Array} eventDates
* List of Dates which have an event (show as selected in UI)
*/
eventDates : null,

/**
* @cfg {Array} noOfMonthPerRow
* No. Of Month to be displayed in a row
*/
noOfMonthPerRow : 3,

initComponent : function(){
Ext.ux.MultiMonthCalendar.superclass.initComponent.call(this);
this.value = this.value ?
this.value.clearTime() : new Date().clearTime();
this.initDisabledDays();
this.noOfMonthPerRow = this.noOfMonthPerRow > this.noOfMonth ?this.noOfMonth : this.noOfMonthPerRow
},

// private
initDisabledDays : function(){
if(!this.disabledDatesRE && this.disabledDates){
var dd = this.disabledDates;
var re = "(?:";
for(var i = 0; i < dd.length; i++){
re += dd[i];
if(i != dd.length-1) re += "|";
}
this.disabledDatesRE = new RegExp(re + ")");
}
},

/**
* Sets the value of the date field
* @param {Date} value The date to set
*/


setValue : function(value){
var old = this.value;
this.value = value.clearTime(true);
if(this.el){
this.update(this.value);
}
},

/**
* Gets the current selected value of the date field
* @return {Date} The selected date
*/
getValue : function(){
return this.value;
},


// private
focus : function(){
if(this.el){
this.update(this.activeDate);
}
},

// private
onRender : function(container, position){
var m = ["<table cellspacing='0'>"];
if(this.noOfMonthPerRow > 1) {
m.push("<tr><td class='x-date-left'><a href='#' title='", this.prevText ,"'> </a></td>");
m.push("<td class='x-date-left' colspan='"+ eval(this.noOfMonthPerRow *2 -3) +"'></td>");
m.push("<td class='x-date-right'><a href='#' title='", this.nextText ,"'> </a></td></tr><tr>");
} else {
//Special case of only one month
m.push("<tr><td><table cellspacing='0' width='100%'><tr><td class='x-date-left'><a href='#' title='", this.prevText ,"'> </a></td>");
m.push("<td class='x-date-right'><a href='#' title='", this.nextText ,"'> </a></td></tr></table></td></tr><tr>");
}
for(var x=0; x<this.noOfMonth; x++) {
m.push("<td><table border='1' cellspacing='0'><tr>");
m.push("<td class='x-date-middle' align='center'><span id='monthLabel" + x + "'></span></td>");
m.push("</tr><tr><td><table class='x-date-inner' id='inner-date"+x+"' cellspacing='0'><thead><tr>");
var dn = this.dayNames;
for(var i = 0; i < 7; i++){
var d = this.startDay+i;
if(d > 6){
d = d-7;
}
m.push("<th><span>", dn[d].substr(0,1), "</span></th>");
}
m[m.length] = "</tr></thead><tbody><tr>";
for(var i = 0; i < 42; i++) {
if(i % 7 == 0 && i != 0){
m[m.length] = "</tr><tr>";
}
m[m.length] = '<td><a href="#" hidefocus="on" class="x-date-date" tabIndex="1"><em><span></span></em></a></td>';
}
m[m.length] = '</tr></tbody></table></td></tr></table></td>';
if(x != this.noOfMonth-1) {
m[m.length] = "<td width='3'></td>";
}
if( (x+1) % this.noOfMonthPerRow == 0 && x!= 0) {
m[m.length] = "</tr><tr>";
}
}
m[m.length] = "</tr></table>";
var el = document.createElement("div");
el.className = "x-date-picker";
el.innerHTML = m.join("");

container.dom.insertBefore(el, position);

this.el = Ext.get(el);
this.eventEl = Ext.get(el.firstChild);

new Ext.util.ClickRepeater(this.el.child("td.x-date-left a"), {
handler: this.showPrevMonth,
scope: this,
preventDefault:true,
stopDefault:true
});

new Ext.util.ClickRepeater(this.el.child("td.x-date-right a"), {
handler: this.showNextMonth,
scope: this,
preventDefault:true,
stopDefault:true
});

var kn = new Ext.KeyNav(this.eventEl, {
"left" : function(e){
e.ctrlKey ?
this.showPrevMonth() :
this.update(this.activeDate.add("d", -1));
},

"right" : function(e){
e.ctrlKey ?
this.showNextMonth() :
this.update(this.activeDate.add("d", 1));
},

"pageUp" : function(e){
this.showNextMonth();
},

"pageDown" : function(e){
this.showPrevMonth();
},

"enter" : function(e){
e.stopPropagation();
return true;
},
scope : this
});

this.cellsArray = new Array();
this.textNodesArray = new Array();
for(var x=0; x< this.noOfMonth; x++) {
var cells = Ext.get('inner-date'+x).select("tbody td");
var textNodes = Ext.get('inner-date'+x).query("tbody span");
this.cellsArray[x] = cells;
this.textNodesArray[x] = textNodes;
}

if(Ext.isIE){
this.el.repaint();
}
this.update(this.value);
},

// private
showPrevMonth : function(e){
this.update(this.activeDate.add("mo", -1));
},

// private
showNextMonth : function(e){
this.update(this.activeDate.add("mo", 1));
},

// private
update : function(date){
this.activeDate = date;
for(var x=0;x<this.noOfMonth;x++) {
var days = date.getDaysInMonth();
var firstOfMonth = date.getFirstDateOfMonth();
var startingPos = firstOfMonth.getDay()-this.startDay;

if(startingPos <= this.startDay){
startingPos += 7;
}

var pm = date.add("mo", -1);
var prevStart = pm.getDaysInMonth()-startingPos;

var cells = this.cellsArray[x].elements;
var textEls = this.textNodesArray[x];
days += startingPos;

// convert everything to numbers so it's fast
var day = 86400000;
var d = (new Date(pm.getFullYear(), pm.getMonth(), prevStart)).clearTime();
var min = this.minDate ? this.minDate.clearTime() : Number.NEGATIVE_INFINITY;
var max = this.maxDate ? this.maxDate.clearTime() : Number.POSITIVE_INFINITY;
var ddMatch = this.disabledDatesRE;
var ddText = this.disabledDatesText;
var ddays = this.disabledDays ? this.disabledDays.join("") : false;
var ddaysText = this.disabledDaysText;
var format = this.format;

var setCellClass = function(cal, cell){
cell.title = "";
var t = d.getTime();
cell.firstChild.dateValue = t;

// disabling
if(t < min) {
cell.className = " x-date-disabled";
cell.title = cal.minText;
return;
}
if(t > max) {
cell.className = " x-date-disabled";
cell.title = cal.maxText;
return;
}
if(ddays){
if(ddays.indexOf(d.getDay()) != -1){
cell.title = ddaysText;
cell.className = " x-date-disabled";
}
}
if(ddMatch && format){
var fvalue = d.dateFormat(format);
if(ddMatch.test(fvalue)){
cell.title = ddText.replace("%0", fvalue);
cell.className = " x-date-disabled";
}
}
//Only active days need to be selected
if(cal.eventDates && (cell.className.indexOf('x-date-active') != -1)) {
for(var y=0; y < cal.eventDates.length; y++) {
var evtDate = cal.eventDates[y].clearTime().getTime();
if(t == evtDate) {
cell.className += " x-date-selected";
break;
}
}
}
};

var i = 0;
for(; i < startingPos; i++) {
textEls[i].innerHTML = (++prevStart);
d.setDate(d.getDate()+1);
cells[i].className = "x-date-prevday";
setCellClass(this, cells[i]);
}
for(; i < days; i++){
intDay = i - startingPos + 1;
textEls[i].innerHTML = (intDay);
d.setDate(d.getDate()+1);
cells[i].className = "x-date-active";
setCellClass(this, cells[i]);
}
var extraDays = 0;
for(; i < 42; i++) {
textEls[i].innerHTML = (++extraDays);
d.setDate(d.getDate()+1);
cells[i].className = "x-date-nextday";
setCellClass(this, cells[i]);
}
var monthLabel = Ext.get('monthLabel' + x);
monthLabel.update(this.monthNames[date.getMonth()] + " " + date.getFullYear());


if(!this.internalRender){
var main = this.el.dom.firstChild;
var w = main.offsetWidth;
this.el.setWidth(w + this.el.getBorderWidth("lr"));
Ext.fly(main).setWidth(w);
this.internalRender = true;
// opera does not respect the auto grow header center column
// then, after it gets a width opera refuses to recalculate
// without a second pass
if(Ext.isOpera && !this.secondPass){
main.rows[0].cells[1].style.width = (w - (main.rows[0].cells[0].offsetWidth+main.rows[0].cells[2].offsetWidth)) + "px";
this.secondPass = true;
this.update.defer(10, this, [date]);
}
}
date = date.add('mo',1);
}
}
});
Ext.reg('mmcalendar', Ext.ux.MultiMonthCalendar);


/**
* SWFUpload v2.0 by Jacob Roberts, Nov 2007, http://www.swfupload.org, http://linebyline.blogspot.com
* -------- -------- -------- -------- -------- -------- -------- --------
* SWFUpload is (c) 2006 Lars Huring and Mammon Media and is released under the MIT License:
* http://www.opensource.org/licenses/mit-license.php
*
* See Changelog.txt for version history
*
* Development Notes:
* * This version of SWFUpload requires Flash Player 9.0.28 and should autodetect the correct flash version.
* * In Linux Flash Player 9 setting the post file variable name does not work. It is always set to "Filedata".
* * There is a lot of repeated code that could be refactored to single functions. Feel free.
* * It's dangerous to do "circular calls" between Flash and JavaScript. I've taken steps to try to work around issues
* by having the event calls pipe through setTimeout. However you should still avoid calling in to Flash from
* within the event handler methods. Especially the "startUpload" event since it cannot use the setTimeout hack.
*/


/* *********** */
/* Constructor */
/* *********** */

var SWFUpload = function (init_settings) {
this.initSWFUpload(init_settings);
};

SWFUpload.prototype.initSWFUpload = function (init_settings) {
// Remove background flicker in IE (read this: http://misterpixel.blogspot.com/2006/09/forensic-analysis-of-ie6.html)
// This doesn't have anything to do with SWFUpload but can help your UI behave better in IE.
try {
document.execCommand('BackgroundImageCache', false, true);
} catch (ex1) {
}


try {
this.customSettings = {}; // A container where developers can place their own settings associated with this instance.
this.settings = {};
this.eventQueue = [];
this.movieName = "SWFUpload_" + SWFUpload.movieCount++;
this.movieElement = null;

// Setup global control tracking
SWFUpload.instances[this.movieName] = this;

// Load the settings. Load the Flash movie.
this.initSettings(init_settings);
this.loadFlash();

this.displayDebugInfo();

} catch (ex2) {
this.debug(ex2);
}
}

/* *************** */
/* Static thingies */
/* *************** */
SWFUpload.instances = {};
SWFUpload.movieCount = 0;
SWFUpload.QUEUE_ERROR = {
QUEUE_LIMIT_EXCEEDED : -100,
FILE_EXCEEDS_SIZE_LIMIT : -110,
ZERO_BYTE_FILE : -120,
INVALID_FILETYPE : -130
};
SWFUpload.UPLOAD_ERROR = {
HTTP_ERROR : -200,
MISSING_UPLOAD_URL : -210,
IO_ERROR : -220,
SECURITY_ERROR : -230,
UPLOAD_LIMIT_EXCEEDED : -240,
UPLOAD_FAILED : -250,
SPECIFIED_FILE_ID_NOT_FOUND : -260,
FILE_VALIDATION_FAILED : -270,
FILE_CANCELLED : -280,
UPLOAD_STOPPED : -290
};
SWFUpload.FILE_STATUS = {
QUEUED : -1,
IN_PROGRESS : -2,
ERROR : -3,
COMPLETE : -4,
CANCELLED : -5
};


/* ***************** */
/* Instance Thingies */
/* ***************** */
// init is a private method that ensures that all the object settings are set, getting a default value if one was not assigned.

SWFUpload.prototype.initSettings = function (init_settings) {
// Upload backend settings
this.addSetting("upload_url", init_settings.upload_url, "");
this.addSetting("file_post_name", init_settings.file_post_name, "Filedata");
this.addSetting("post_params", init_settings.post_params, {});

// File Settings
this.addSetting("file_types", init_settings.file_types, "*.*");
this.addSetting("file_types_description", init_settings.file_types_description, "All Files");
this.addSetting("file_size_limit", init_settings.file_size_limit, "1024");
this.addSetting("file_upload_limit", init_settings.file_upload_limit, "0");
this.addSetting("file_queue_limit", init_settings.file_queue_limit, "0");

// Flash Settings
this.addSetting("flash_url", init_settings.flash_url, "swfupload.swf");
this.addSetting("flash_width", init_settings.flash_width, "1px");
this.addSetting("flash_height", init_settings.flash_height, "1px");
this.addSetting("flash_color", init_settings.flash_color, "#FFFFFF");

// Debug Settings
this.addSetting("debug_enabled", init_settings.debug, false);

// Event Handlers
this.flashReady_handler = SWFUpload.flashReady; // This is a non-overrideable event handler
this.swfUploadLoaded_handler = this.retrieveSetting(init_settings.swfupload_loaded_handler, SWFUpload.swfUploadLoaded);

this.fileDialogStart_handler = this.retrieveSetting(init_settings.file_dialog_start_handler, SWFUpload.fileDialogStart);
this.fileQueued_handler = this.retrieveSetting(init_settings.file_queued_handler, SWFUpload.fileQueued);
this.fileQueueError_handler = this.retrieveSetting(init_settings.file_queue_error_handler, SWFUpload.fileQueueError);
this.fileDialogComplete_handler = this.retrieveSetting(init_settings.file_dialog_complete_handler, SWFUpload.fileDialogComplete);

this.uploadStart_handler = this.retrieveSetting(init_settings.upload_start_handler, SWFUpload.uploadStart);
this.uploadProgress_handler = this.retrieveSetting(init_settings.upload_progress_handler, SWFUpload.uploadProgress);
this.uploadError_handler = this.retrieveSetting(init_settings.upload_error_handler, SWFUpload.uploadError);
this.uploadSuccess_handler = this.retrieveSetting(init_settings.upload_success_handler, SWFUpload.uploadSuccess);
this.uploadComplete_handler = this.retrieveSetting(init_settings.upload_complete_handler, SWFUpload.uploadComplete);

this.debug_handler = this.retrieveSetting(init_settings.debug_handler, SWFUpload.debug);

// Other settings
this.customSettings = this.retrieveSetting(init_settings.custom_settings, {});
};

// loadFlash is a private method that generates the HTML tag for the Flash
// It then adds the flash to the "target" or to the body and stores a
// reference to the flash element in "movieElement".
SWFUpload.prototype.loadFlash = function () {
var html, target_element, container;

// Make sure an element with the ID we are going to use doesn't already exist
if (document.getElementById(this.movieName) !== null) {
return false;
}

// Get the body tag where we will be adding the flash movie
try {
target_element = document.getElementsByTagName("body")[0];
if (typeof(target_element) === "undefined" || target_element === null) {
this.debug('Could not find the BODY element. SWFUpload failed to load.');
return false;
}
} catch (ex) {
return false;
}

// Append the container and load the flash
container = document.createElement("div");
container.style.width = this.getSetting("flash_width");
container.style.height = this.getSetting("flash_height");

target_element.appendChild(container);
container.innerHTML = this.getFlashHTML(); // Using innerHTML is non-standard but the only sensible way to dynamically add Flash in IE (and maybe other browsers)
};

// Generates the embed/object tags needed to embed the flash in to the document
SWFUpload.prototype.getFlashHTML = function () {
var html = "";

// Create Mozilla Embed HTML
if (navigator.plugins && navigator.mimeTypes && navigator.mimeTypes.length) {
// Build the basic embed html
html = '<embed type="application/x-shockwave-flash" src="' + this.getSetting("flash_url") + '" width="' + this.getSetting("flash_width") + '" height="' + this.getSetting("flash_height") + '"';
html += ' id="' + this.movieName + '" name="' + this.movieName + '" ';
html += 'bgcolor="' + this.getSetting("flash_color") + '" quality="high" menu="false" flashvars="';


html += this.getFlashVars();

html += '" />';

// Create IE Object HTML
} else {

// Build the basic Object tag
html = '<object id="' + this.movieName + '" classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" width="' + this.getSetting("flash_width") + '" height="' + this.getSetting("flash_height") + '">';
html += '<param name="movie" value="' + this.getSetting("flash_url") + '">';

html += '<param name="bgcolor" value="' + this.getSetting("flash_color") + '" />';
html += '<param name="quality" value="high" />';
html += '<param name="menu" value="false" />';

html += '<param name="flashvars" value="' + this.getFlashVars() + '" />';
html += '</object>';
}

return html;
};

// This private method builds the parameter string that will be passed
// to flash.
SWFUpload.prototype.getFlashVars = function () {
// Build a string from the post param object
var param_string = this.buildParamString();

// Build the parameter string
var html = "";
html += "movieName=" + encodeURIComponent(this.movieName);
html += "&uploadURL=" + encodeURIComponent(this.getSetting("upload_url"));
html += "&params=" + encodeURIComponent(param_string);
html += "&filePostName=" + encodeURIComponent(this.getSetting("file_post_name"));
html += "&fileTypes=" + encodeURIComponent(this.getSetting("file_types"));
html += "&fileTypesDescription=" + encodeURIComponent(this.getSetting("file_types_description"));
html += "&fileSizeLimit=" + encodeURIComponent(this.getSetting("file_size_limit"));
html += "&fileUploadLimit=" + encodeURIComponent(this.getSetting("file_upload_limit"));
html += "&fileQueueLimit=" + encodeURIComponent(this.getSetting("file_queue_limit"));
html += "&debugEnabled=" + encodeURIComponent(this.getSetting("debug_enabled"));

return html;
};

SWFUpload.prototype.getMovieElement = function () {
if (typeof(this.movieElement) === "undefined" || this.movieElement === null) {
this.movieElement = document.getElementById(this.movieName);

// Fix IEs "Flash can't callback when in a form" issue (http://www.extremefx.com.ar/blog/fixing-flash-external-interface-inside-form-on-internet-explorer)
// Removed because Revision 6 always adds the flash to the body (inside a containing div)
// If you insist on adding the Flash file inside a Form then in IE you have to make you wait until the DOM is ready
// and run this code to make the form's ID available from the window object so Flash and JavaScript can communicate.
//if (typeof(window[this.movieName]) === "undefined" || window[this.moveName] !== this.movieElement) {
// window[this.movieName] = this.movieElement;
//}
}

return this.movieElement;
};

SWFUpload.prototype.buildParamString = function () {
var post_params = this.getSetting("post_params");
var param_string_pairs = [];
var i, value, name;

// Retrieve the user defined parameters
if (typeof(post_params) === "object") {
for (name in post_params) {
if (post_params.hasOwnProperty(name)) {
if (typeof(post_params[name]) === "string") {
param_string_pairs.push(encodeURIComponent(name) + "=" + encodeURIComponent(post_params[name]));
}
}
}
}

return param_string_pairs.join("&");
};

// Saves a setting. If the value given is undefined or null then the default_value is used.
SWFUpload.prototype.addSetting = function (name, value, default_value) {
if (typeof(value) === "undefined" || value === null) {
this.settings[name] = default_value;
} else {
this.settings[name] = value;
}

return this.settings[name];
};

// Gets a setting. Returns empty string if not found.
SWFUpload.prototype.getSetting = function (name) {
if (typeof(this.settings[name]) === "undefined") {
return "";
} else {
return this.settings[name];
}
};

// Gets a setting, if the setting is undefined then return the default value
// This does not affect or use the interal setting object.
SWFUpload.prototype.retrieveSetting = function (value, default_value) {
if (typeof(value) === "undefined" || value === null) {
return default_value;
} else {
return value;
}
};


// It loops through all the settings and displays
// them in the debug Console.
SWFUpload.prototype.displayDebugInfo = function () {
var key, debug_message = "";

debug_message += "----- SWFUPLOAD SETTINGS ----\nID: " + this.moveName + "\n";

debug_message += this.outputObject(this.settings);

debug_message += "----- SWFUPLOAD SETTINGS END ----\n";
debug_message += "\n";

this.debug(debug_message);
};
SWFUpload.prototype.outputObject = function (object, prefix) {
var output = "", key;

if (typeof(prefix) !== "string") {
prefix = "";
}
if (typeof(object) !== "object") {
return "";
}

for (key in object) {
if (object.hasOwnProperty(key)) {
if (typeof(object[key]) === "object") {
output += (prefix + key + ": { \n" + this.outputObject(object[key], "\t" + prefix) + prefix + "}" + "\n");
} else {
output += (prefix + key + ": " + object[key] + "\n");
}
}
}

return output;
};

/* *****************************
-- Flash control methods --
Your UI should use these
to operate SWFUpload
***************************** */

SWFUpload.prototype.selectFile = function () {
var movie_element = this.getMovieElement();
if (movie_element !== null && typeof(movie_element.SelectFile) === "function") {
try {
movie_element.SelectFile();
}
catch (ex) {
this.debug("Could not call SelectFile: " + ex);
}
} else {
this.debug("Could not find Flash element");
}

};

SWFUpload.prototype.selectFiles = function () {
var movie_element = this.getMovieElement();
if (movie_element !== null && typeof(movie_element.SelectFiles) === "function") {
try {
movie_element.SelectFiles();
}
catch (ex) {
this.debug("Could not call SelectFiles: " + ex);
}
} else {
this.debug("Could not find Flash element");
}

};


/* Start the upload. If a file_id is specified that file is uploaded. Otherwise the first
* file in the queue is uploaded. If no files are in the queue then nothing happens.
* This call uses setTimeout since Flash will be calling back in to JavaScript
*/
SWFUpload.prototype.startUpload = function (file_id) {
var self = this;
var movie_element = this.getMovieElement();
if (movie_element !== null && typeof(movie_element.StartUpload) === "function") {
setTimeout(
function () {
try {
movie_element.StartUpload(file_id);
}
catch (ex) {
self.debug("Could not call StartUpload: " + ex);
}
}, 0
);
} else {
this.debug("Could not find Flash element");
}


};

/* Cancels a the file upload. You must specify a file_id */
SWFUpload.prototype.cancelUpload = function (file_id) {
var movie_element = this.getMovieElement();
if (movie_element !== null && typeof(movie_element.CancelUpload) === "function") {
try {
movie_element.CancelUpload(file_id);
}
catch (ex) {
this.debug("Could not call CancelUpload: " + ex);
}
} else {
this.debug("Could not find Flash element");
}

};

// Stops the current upload. The file is re-queued. If nothing is currently uploading then nothing happens.
SWFUpload.prototype.stopUpload = function () {
var movie_element = this.getMovieElement();
if (movie_element !== null && typeof(movie_element.StopUpload) === "function") {
try {
movie_element.StopUpload();
}
catch (ex) {
this.debug("Could not call StopUpload: " + ex);
}
} else {
this.debug("Could not find Flash element");
}

};

/* ************************
* Settings methods
* These methods change the settings inside SWFUpload
* They shouldn't need to be called in a setTimeout since they
* should not call back from Flash to JavaScript (except perhaps in a Debug call)
* and some need to return data so setTimeout won't work.
*/

/* Gets the file statistics object. It looks like this (where n = number):
{
files_queued: n,
complete_uploads: n,
upload_errors: n,
uploads_cancelled: n,
queue_errors: n
}
*/
SWFUpload.prototype.getStats = function () {
var movie_element = this.getMovieElement();
if (movie_element !== null && typeof(movie_element.GetStats) === "function") {
try {
return movie_element.GetStats();
}
catch (ex) {
this.debug("Could not call GetStats");
}
} else {
this.debug("Could not find Flash element");
}
};
SWFUpload.prototype.setStats = function (stats_object) {
var movie_element = this.getMovieElement();
if (movie_element !== null && typeof(movie_element.SetStats) === "function") {
try {
movie_element.SetStats(stats_object);
}
catch (ex) {
this.debug("Could not call SetStats");
}
} else {
this.debug("Could not find Flash element");
}
};

SWFUpload.prototype.setCredentials = function(name, password) {
var movie_element = this.getMovieElement();
if (movie_element !== null && typeof(movie_element.SetCredentials) === "function") {
try {
return movie_element.SetCredentials(name, password);
}
catch (ex) {
this.debug("Could not call SetCredentials");
}
} else {
this.debug("Could not find Flash element");
}
};

SWFUpload.prototype.getFile = function (file_id) {
var movie_element = this.getMovieElement();
if (typeof(file_id) === "number") {
if (movie_element !== null && typeof(movie_element.GetFileByIndex) === "function") {
try {
return movie_element.GetFileByIndex(file_id);
}
catch (ex) {
this.debug("Could not call GetFileByIndex");
}
} else {
this.debug("Could not find Flash element");
}
} else {
if (movie_element !== null && typeof(movie_element.GetFile) === "function") {
try {
return movie_element.GetFile(file_id);
}
catch (ex) {
this.debug("Could not call GetFile");
}
} else {
this.debug("Could not find Flash element");
}
}
};

SWFUpload.prototype.addFileParam = function (file_id, name, value) {
var movie_element = this.getMovieElement();
if (movie_element !== null && typeof(movie_element.AddFileParam) === "function") {
try {
return movie_element.AddFileParam(file_id, name, value);
}
catch (ex) {
this.debug("Could not call AddFileParam");
}
} else {
this.debug("Could not find Flash element");
}
};

SWFUpload.prototype.removeFileParam = function (file_id, name) {
var movie_element = this.getMovieElement();
if (movie_element !== null && typeof(movie_element.RemoveFileParam) === "function") {
try {
return movie_element.RemoveFileParam(file_id, name);
}
catch (ex) {
this.debug("Could not call AddFileParam");
}
} else {
this.debug("Could not find Flash element");
}

};

SWFUpload.prototype.setUploadURL = function (url) {
var movie_element = this.getMovieElement();
if (movie_element !== null && typeof(movie_element.SetUploadURL) === "function") {
try {
this.addSetting("upload_url", url);
movie_element.SetUploadURL(this.getSetting("upload_url"));
}
catch (ex) {
this.debug("Could not call SetUploadURL");
}
} else {
this.debug("Could not find Flash element in setUploadURL");
}
};

SWFUpload.prototype.setPostParams = function (param_object) {
var movie_element = this.getMovieElement();
if (movie_element !== null && typeof(movie_element.SetPostParams) === "function") {
try {
this.addSetting("post_params", param_object);
movie_element.SetPostParams(this.getSetting("post_params"));
}
catch (ex) {
this.debug("Could not call SetPostParams");
}
} else {
this.debug("Could not find Flash element in SetPostParams");
}
};

SWFUpload.prototype.setFileTypes = function (types, description) {
var movie_element = this.getMovieElement();
if (movie_element !== null && typeof(movie_element.SetFileTypes) === "function") {
try {
this.addSetting("file_types", types);
this.addSetting("file_types_description", description);
movie_element.SetFileTypes(this.getSetting("file_types"), this.getSetting("file_types_description"));
}
catch (ex) {
this.debug("Could not call SetFileTypes");
}
} else {
this.debug("Could not find Flash element in SetFileTypes");
}
};

SWFUpload.prototype.setFileSizeLimit = function (file_size_limit) {
var movie_element = this.getMovieElement();
if (movie_element !== null && typeof(movie_element.SetFileSizeLimit) === "function") {
try {
this.addSetting("file_size_limit", file_size_limit);
movie_element.SetFileSizeLimit(this.getSetting("file_size_limit"));
}
catch (ex) {
this.debug("Could not call SetFileSizeLimit");
}
} else {
this.debug("Could not find Flash element in SetFileSizeLimit");
}
};


SWFUpload.prototype.setFileUploadLimit = function (file_upload_limit) {
var movie_element = this.getMovieElement();
if (movie_element !== null && typeof(movie_element.SetFileUploadLimit) === "function") {
try {
this.addSetting("file_upload_limit", file_upload_limit);
movie_element.SetFileUploadLimit(this.getSetting("file_upload_limit"));
}
catch (ex) {
this.debug("Could not call SetFileUploadLimit");
}
} else {
this.debug("Could not find Flash element in SetFileUploadLimit");
}
};

SWFUpload.prototype.setFileQueueLimit = function (file_queue_limit) {
var movie_element = this.getMovieElement();
if (movie_element !== null && typeof(movie_element.SetFileQueueLimit) === "function") {
try {
this.addSetting("file_queue_limit", file_queue_limit);
movie_element.SetFileQueueLimit(this.getSetting("file_queue_limit"));
}
catch (ex) {
this.debug("Could not call SetFileQueueLimit");
}
} else {
this.debug("Could not find Flash element in SetFileQueueLimit");
}
};

SWFUpload.prototype.setFilePostName = function (file_post_name) {
var movie_element = this.getMovieElement();
if (movie_element !== null && typeof(movie_element.SetFilePostName) === "function") {
try {
this.addSetting("file_post_name", file_post_name);
movie_element.SetFilePostName(this.getSetting("file_post_name"));
}
catch (ex) {
this.debug("Could not call SetFilePostName");
}
} else {
this.debug("Could not find Flash element in SetFilePostName");
}
};

SWFUpload.prototype.setDebugEnabled = function (debug_enabled) {
var movie_element = this.getMovieElement();
if (movie_element !== null && typeof(movie_element.SetDebugEnabled) === "function") {
try {
this.addSetting("debug_enabled", debug_enabled);
movie_element.SetDebugEnabled(this.getSetting("debug_enabled"));
}
catch (ex) {
this.debug("Could not call SetDebugEnabled");
}
} else {
this.debug("Could not find Flash element in SetDebugEnabled");
}
};

/* *******************************
Internal Event Callers
Don't override these! These event callers ensure that your custom event handlers
are called safely and in order.
******************************* */

/* This is the callback method that the Flash movie will call when it has been loaded and is ready to go.
Calling this or showUI() "manually" will bypass the Flash Detection built in to SWFUpload.
Use a ui_function setting if you want to control the UI loading after the flash has loaded.
*/
SWFUpload.prototype.flashReady = function () {
// Check that the movie element is loaded correctly with its ExternalInterface methods defined
var movie_element = this.getMovieElement();
if (movie_element === null || typeof(movie_element.StartUpload) !== "function") {
this.debug("ExternalInterface methods failed to initialize.");
return;
}

var self = this;
if (typeof(self.flashReady_handler) === "function") {
this.eventQueue[this.eventQueue.length] = function() { self.flashReady_handler(); };
setTimeout(function () { self.executeNextEvent();}, 0);
} else {
this.debug("flashReady_handler event not defined");
}
};

/*
Event Queue. Rather can call events directly from Flash they events are
are placed in a queue and then executed. This ensures that each event is
executed in the order it was called which is not guarenteed when calling
setTimeout. Out of order events was especially problematic in Safari.
*/
SWFUpload.prototype.executeNextEvent = function () {
var f = this.eventQueue.shift();
if (typeof(f) === "function") {
f();
}
}

/* This is a chance to do something before the browse window opens */
SWFUpload.prototype.fileDialogStart = function () {
var self = this;
if (typeof(self.fileDialogStart_handler) === "function") {
this.eventQueue[this.eventQueue.length] = function() { self.fileDialogStart_handler(); };
setTimeout(function () { self.executeNextEvent();}, 0);
} else {
this.debug("fileDialogStart event not defined");
}
};


/* Called when a file is successfully added to the queue. */
SWFUpload.prototype.fileQueued = function (file) {
var self = this;
if (typeof(self.fileQueued_handler) === "function") {
this.eventQueue[this.eventQueue.length] = function() { self.fileQueued_handler(file); };
setTimeout(function () { self.executeNextEvent();}, 0);
} else {
this.debug("fileQueued event not defined");
}
};


/* Handle errors that occur when an attempt to queue a file fails. */
SWFUpload.prototype.fileQueueError = function (file, error_code, message) {
var self = this;
if (typeof(self.fileQueueError_handler) === "function") {
this.eventQueue[this.eventQueue.length] = function() { self.fileQueueError_handler(file, error_code, message); };
setTimeout(function () { self.executeNextEvent();}, 0);
} else {
this.debug("fileQueueError event not defined");
}
};

/* Called after the file dialog has closed and the selected files have been queued.
You could call startUpload here if you want the queued files to begin uploading immediately. */
SWFUpload.prototype.fileDialogComplete = function (num_files_selected) {
var self = this;
if (typeof(self.fileDialogComplete_handler) === "function") {
this.eventQueue[this.eventQueue.length] = function() { self.fileDialogComplete_handler(num_files_selected); };
setTimeout(function () { self.executeNextEvent();}, 0);
} else {
this.debug("fileDialogComplete event not defined");
}
};

/* Gets called when a file upload is about to be started. Return true to continue the upload. Return false to stop the upload.
If you return false then uploadError and uploadComplete are called (like normal).

This is a good place to do any file validation you need.
*/
SWFUpload.prototype.uploadStart = function (file) {
var self = this;
if (typeof(self.fileDialogComplete_handler) === "function") {
this.eventQueue[this.eventQueue.length] = function() { self.returnUploadStart(self.uploadStart_handler(file)); };
setTimeout(function () { self.executeNextEvent();}, 0);
} else {
this.debug("uploadStart event not defined");
}
};

/* Note: Internal use only. This function returns the result of uploadStart to
flash. Since returning values in the normal way can result in Flash/JS circular
call issues we split up the call in a Timeout. This is transparent from the API
point of view.
*/
SWFUpload.prototype.returnUploadStart = function (return_value) {
var movie_element = this.getMovieElement();
if (movie_element !== null && typeof(movie_element.ReturnUploadStart) === "function") {
try {
movie_element.ReturnUploadStart(return_value);
}
catch (ex) {
this.debug("Could not call ReturnUploadStart");
}
} else {
this.debug("Could not find Flash element in returnUploadStart");
}
};



/* Called during upload as the file progresses. Use this event to update your UI. */
SWFUpload.prototype.uploadProgress = function (file, bytes_complete, bytes_total) {
var self = this;
if (typeof(self.uploadProgress_handler) === "function") {
this.eventQueue[this.eventQueue.length] = function() { self.uploadProgress_handler(file, bytes_complete, bytes_total); };
setTimeout(function () { self.executeNextEvent();}, 0);
} else {
this.debug("uploadProgress event not defined");
}
};

/* Called when an error occurs during an upload. Use error_code and the SWFUpload.UPLOAD_ERROR constants to determine
which error occurred. The uploadComplete event is called after an error code indicating that the next file is
ready for upload. For files cancelled out of order the uploadComplete event will not be called. */
SWFUpload.prototype.uploadError = function (file, error_code, message) {
var self = this;
if (typeof(this.uploadError_handler) === "function") {
this.eventQueue[this.eventQueue.length] = function() { self.uploadError_handler(file, error_code, message); };
setTimeout(function () { self.executeNextEvent();}, 0);
} else {
this.debug("uploadError event not defined");
}
};


/* This gets called when a file finishes uploading and the server-side upload script has completed and returned a 200
status code. Any text returned by the server is available in server_data.
**NOTE: The upload script MUST return some text or the uploadSuccess and uploadComplete events will not fire and the
upload will become 'stuck'. */
SWFUpload.prototype.uploadSuccess = function (file, server_data) {
var self = this;
if (typeof(self.uploadSuccess_handler) === "function") {
this.eventQueue[this.eventQueue.length] = function() { self.uploadSuccess_handler(file, server_data); };
setTimeout(function () { self.executeNextEvent();}, 0);
} else {
this.debug("uploadSuccess event not defined");
}
};

/* uploadComplete is called when the file is uploaded or an error occurred and SWFUpload is ready to make the next upload.
If you want the next upload to start to automatically you can call startUpload() from this event. */
SWFUpload.prototype.uploadComplete = function (file) {
var self = this;
if (typeof(self.uploadComplete_handler) === "function") {
this.eventQueue[this.eventQueue.length] = function() { self.uploadComplete_handler(file); };
setTimeout(function () { self.executeNextEvent();}, 0);
} else {
this.debug("uploadComplete event not defined");
}
};

/* Called by SWFUpload JavaScript and Flash functions when debug is enabled. By default it writes messages to the
internal debug console. You can override this event and have messages written where you want. */
SWFUpload.prototype.debug = function (message) {
var self = this;
if (typeof(self.debug_handler) === "function") {
this.eventQueue[this.eventQueue.length] = function() { self.debug_handler(message); };
setTimeout(function () { self.executeNextEvent();}, 0);
} else {
this.eventQueue[this.eventQueue.length] = function() { self.debugMessage(message); };
setTimeout(function () { self.executeNextEvent();}, 0);
}
};


/* **********************************
Default Event Handlers.
These event handlers are used by default if an overriding handler is
not defined in the SWFUpload settings object.

JS Note: even though these are defined on the SWFUpload object (rather than the prototype) they
are attached (read: copied) to a SWFUpload instance and 'this' is given the proper context.
********************************** */

/* This is a special event handler that has no override in the settings. Flash calls this when it has
been loaded by the browser and is ready for interaction. You should not override it. If you need
to do something with SWFUpload has loaded then use the swfupload_loaded_handler setting.
*/
SWFUpload.flashReady = function () {
try {
this.debug("Flash called back and is ready.");

if (typeof(this.swfUploadLoaded_handler) === "function") {
this.swfUploadLoaded_handler();
}
} catch (ex) {
this.debug(ex);
}
};

/* This is a chance to something immediately after SWFUpload has loaded.
Like, hide the default/degraded upload form and display the SWFUpload form. */
SWFUpload.swfUploadLoaded = function () {
};

/* This is a chance to do something before the browse window opens */
SWFUpload.fileDialogStart = function () {
};


/* Called when a file is successfully added to the queue. */
SWFUpload.fileQueued = function (file) {
};


/* Handle errors that occur when an attempt to queue a file fails. */
SWFUpload.fileQueueError = function (file, error_code, message) {
try {
switch (error_code) {
case SWFUpload.QUEUE_ERROR.FILE_EXCEEDS_SIZE_LIMIT:
this.debug("Error Code: File too big, File name: " + file.name + ", File size: " + file.size + ", Message: " + message);
break;
case SWFUpload.QUEUE_ERROR.ZERO_BYTE_FILE:
this.debug("Error Code: Zero Byte File, File name: " + file.name + ", File size: " + file.size + ", Message: " + message);
break;
case SWFUpload.QUEUE_ERROR.QUEUE_LIMIT_EXCEEDED:
this.debug("Error Code: Upload limit reached, File name: " + file.name + ", File size: " + file.size + ", Message: " + message);
break;
case SWFUpload.QUEUE_ERROR.INVALID_FILETYPE:
this.debug("Error Code: File extension is not allowed, Message: " + message);
break;
default:
this.debug("Error Code: Unhandled error occured. Errorcode: " + error_code);
}
} catch (ex) {
this.debug(ex);
}
};

/* Called after the file dialog has closed and the selected files have been queued.
You could call startUpload here if you want the queued files to begin uploading immediately. */
SWFUpload.fileDialogComplete = function (num_files_selected) {
};

/* Gets called when a file upload is about to be started. Return true to continue the upload. Return false to stop the upload.
If you return false then the uploadError callback is called and then uploadComplete (like normal).

This is a good place to do any file validation you need.

This is the only function that cannot be called on a setTimeout because it must return a value to Flash.
You SHOULD NOT make any calls in to Flash (e.i, changing settings, getting stats, etc). Flash Player bugs prevent
calls in to Flash from working reliably.
*/
SWFUpload.uploadStart = function (file) {
return true;
};

// Called during upload as the file progresses
SWFUpload.uploadProgress = function (file, bytes_complete, bytes_total) {
this.debug("File Progress: " + file.id + ", Bytes: " + bytes_complete + ". Total: " + bytes_total);
};

/* This gets called when a file finishes uploading and the upload script has completed and returned a 200 status code. Any text returned by the
server is available in server_data. The upload script must return some text or uploadSuccess will not fire (neither will uploadComplete). */
SWFUpload.uploadSuccess = function (file, server_data) {
};

/* This is called last. The file is uploaded or an error occurred and SWFUpload is ready to make the next upload.
If you want to automatically start the next file just call startUpload from here.
*/
SWFUpload.uploadComplete = function (file) {
};

// Called by SWFUpload JavaScript and Flash functions when debug is enabled.
// Override this method in your settings to call your own debug message handler
SWFUpload.debug = function (message) {
if (this.getSetting("debug_enabled")) {
this.debugMessage(message);
}
};

/* Called when an upload occurs during upload. For HTTP errors 'message' will contain the HTTP STATUS CODE */
SWFUpload.uploadError = function (file, error_code, message) {
try {
switch (errcode) {
case SWFUpload.UPLOAD_ERROR.SPECIFIED_FILE_ID_NOT_FOUND:
this.debug("Error Code: File ID specified for upload was not found, Message: " + msg);
break;
case SWFUpload.UPLOAD_ERROR.HTTP_ERROR:
this.debug("Error Code: HTTP Error, File name: " + file.name + ", Message: " + msg);
break;
case SWFUpload.UPLOAD_ERROR.MISSING_UPLOAD_URL:
this.debug("Error Code: No backend file, File name: " + file.name + ", Message: " + msg);
break;
case SWFUpload.UPLOAD_ERROR.IO_ERROR:
this.debug("Error Code: IO Error, File name: " + file.name + ", Message: " + msg);
break;
case SWFUpload.UPLOAD_ERROR.SECURITY_ERROR:
this.debug("Error Code: Security Error, File name: " + file.name + ", Message: " + msg);
break;
case SWFUpload.UPLOAD_ERROR.UPLOAD_LIMIT_EXCEEDED:
this.debug("Error Code: Upload limit reached, File name: " + file.name + ", File size: " + file.size + ", Message: " + msg);
break;
case SWFUpload.UPLOAD_ERROR.UPLOAD_FAILED:
this.debug("Error Code: Upload Initialization exception, File name: " + file.name + ", File size: " + file.size + ", Message: " + msg);
break;
case SWFUpload.UPLOAD_ERROR.FILE_VALIDATION_FAILED:
this.debug("Error Code: uploadStart callback returned false, File name: " + file.name + ", File size: " + file.size + ", Message: " + msg);
break;
case SWFUpload.UPLOAD_ERROR.FILE_CANCELLED:
this.debug("Error Code: The file upload was cancelled, File name: " + file.name + ", File size: " + file.size + ", Message: " + msg);
break;
case SWFUpload.UPLOAD_ERROR.UPLOAD_STOPPED:
this.debug("Error Code: The file upload was stopped, File name: " + file.name + ", File size: " + file.size + ", Message: " + msg);
break;
default:
this.debug("Error Code: Unhandled error occured. Errorcode: " + errcode);
}
} catch (ex) {
this.debug(ex);
}
};



/* **********************************
Debug Console
The debug console is a self contained, in page location
for debug message to be sent. The Debug Console adds
itself to the body if necessary.

The console is automatically scrolled as messages appear.

You can override this console (to use FireBug's console for instance) by setting the debug event method to your own function
that handles the debug message
********************************** */
SWFUpload.prototype.debugMessage = function (message) {
var exception_message, exception_values;


if (typeof(message) === "object" && typeof(message.name) === "string" && typeof(message.message) === "string") {
exception_message = "";
exception_values = [];
for (var key in message) {
exception_values.push(key + ": " + message[key]);
}
exception_message = exception_values.join("\n");
exception_values = exception_message.split("\n");
exception_message = "EXCEPTION: " + exception_values.join("\nEXCEPTION: ");
SWFUpload.Console.writeLine(exception_message);
} else {
SWFUpload.Console.writeLine(message);
}
};

SWFUpload.Console = {};
SWFUpload.Console.writeLine = function (message) {
/*
var console, documentForm;

try {
console = document.getElementById("SWFUpload_Console");

if (!console) {
documentForm = document.createElement("form");
document.getElementsByTagName("body")[0].appendChild(documentForm);

console = document.createElement("textarea");
console.id = "SWFUpload_Console";
console.style.fontFamily = "monospace";
console.setAttribute("wrap", "off");
console.wrap = "off";
console.style.overflow = "auto";
console.style.width = "700px";
console.style.height = "350px";
console.style.margin = "5px";
documentForm.appendChild(console);
}

console.value += message + "\n";

console.scrollTop = console.scrollHeight - console.clientHeight;
} catch (ex) {
alert("Exception: " + ex.name + " Message: " + ex.message);
}
*/
console.log(message);

};

/**
* Makes a Panel to provide the ability to upload multiple files using the SwfUpload flash script.
*
* @author Michael Giddens
* @website http://www.silverbiology.com
* @created 2008-03-06
* @version 0.4
*
* @known_issues
* - Progress bar used hardcoded width. Not sure how to make 100% in bbar
* - Panel requires width / height to be set. Not sure why it will not fit
* - when panel is nested sometimes the column model is not always shown to fit until a file is added. Render order issue.
*/

// Create user extension namespace
Ext.namespace('Ext.ux');

Ext.ux.SwfUploadPanel = function(config){

// Default Params for SwfUploader
// if (!config.title) config.title = 'File Upload';
if (!config.single_select) config.single_select = false;
if (!config.file_types) config.file_types = "*.*";
if (!config.file_types_description) config.file_types_description = "All Files";
if (!config.file_size_limit) config.file_size_limit = "102400"; //100MB
if (!config.file_upload_limit) config.file_upload_limit = "0";
if (!config.file_post_name) config.file_post_name = "Filedata"; // This is the "name" of the file item that the server-side script will receive. Setting this doesn't work in the Linux Flash Player
if (!config.flash_url) config.flash_url = "swfupload_f9.swf"; // Relative to this file
if (!config.debug) config.debug = false;

this.rec = Ext.data.Record.create([
{name: 'name'},
{name: 'size'},
{name: 'id'},
{name: 'type'},
{name: 'creationdate', type: 'date', dateFormat: 'm/d/Y'},
{name: 'status'},
{name: 'note'}
]);

var store = new Ext.data.Store({
reader: new Ext.data.JsonReader({
id: 'id'
}, this.rec)
});

this.suo = new SWFUpload({
upload_url: config.upload_url,
post_params: config.post_params,
file_post_name: config.file_post_name,
file_size_limit : config.file_size_limit,
file_types : config.file_types,
file_types_description : config.file_types_description,
file_upload_limit : config.file_upload_limit,
flash_url : config.flash_url,

// Event Handler Settings
file_queued_handler : this.fileQueue.createDelegate(this),
file_queue_error_handler : this.fileQueueError.createDelegate(this),
file_dialog_complete_handler : this.fileDialogComplete.createDelegate(this),

upload_error_handler : this.uploadError.createDelegate(this),
upload_progress_handler : this.uploadProgress.createDelegate(this),
upload_complete_handler : this.uploadComplete.createDelegate(this),

upload_success_handler : this.fileComplete.createDelegate(this),
upload_error_handler : this.fileCancelled.createDelegate(this),

swfupload_loaded_handler : this.swfUploadLoaded.createDelegate(this),

debug: config.debug

});

this.progress_bar = new Ext.ProgressBar({
text:'Progress Bar'
// , width: config.width - 7
});

var cm = new Ext.grid.ColumnModel([{id:'name', header: "Filename", width: 150, dataIndex: 'name'}
, {id:'size', header: "Size", width: 80, dataIndex: 'size', renderer: this.formatBytes }
, {id:'status', header: "Status", width: 80, dataIndex: 'status', renderer: this.formatStatus }
, {id:'note', header: "Note", dataIndex: 'note'}
]);

config = config || {};
config = Ext.apply(config || {}, {

store: store
, cm: cm
, autoExpandColumn: 'note'
, enableColumnResize: true
, enableColumnMove: false

, sm: new Ext.grid.RowSelectionModel({singleSelect: config.single_select})
, tbar: [{ text:'Add File(s)'
, iconCls: 'SwfUploadPanel_iconAdd'
, scope: this
, handler: function() {
if (config.single_file_select)
this.suo.selectFile();
else
this.suo.selectFiles();
}
}, '->', {
text: 'Cancel Upload'
, id: 'cancel'
, iconCls: 'SwfUploadPanel_iconCancel'
, handler: this.stopUpload.createDelegate(this)
, hidden: true
}, {
text: 'Upload File(s)'
, iconCls: 'SwfUploadPanel_iconUpload'
, handler: this.uploadFiles.createDelegate(this)
, hidden: true
}
]

, bbar: [ this.progress_bar ]

, listeners: {

'keydown': function(e) {
// Delete Row
function removeRows(grid) {
var selRecords = grid.getSelections();
for (var i=0; i < selRecords.length; i++) {
if (selRecords[i].data.status != 1) {
grid.suo.cancelUpload(selRecords[i].id);
grid.store.remove(selRecords[i]);
}
}

if (grid.suo.getStats().files_queued == 0) {
grid.getTopToolbar().items.get(3).setVisible(false);
}

}

if (config.confirm_delete) {
if(e.getKey() == e.DELETE) {
Ext.MessageBox.confirm('Remove File','Are you sure you wish to remove this file from queue?', function(e) {
if (e == 'yes') {
removeRows(this);
}
});
}
} else {
if(e.getKey() == e.DELETE) {
removeRows(this);
}
}
}

// Prevent the default right click to show up in the grid.
, 'contextmenu': function(e) {
e.stopEvent();
}
}


});

cm.defaultSortable = false;

Ext.ux.SwfUploadPanel.superclass.constructor.apply(this, arguments);

try {
this.progress_bar.setWidth(this.bbar.getWidth() - 5);
Ext.fly(this.progress_bar.el.dom.firstChild.firstChild).applyStyles("height: 16px");
} catch (ex1) {
}

this.on('resize', function() {
this.progress_bar.setWidth(this.bbar.getWidth() - 5);
Ext.fly(this.progress_bar.el.dom.firstChild.firstChild).applyStyles("height: 16px");
});

};

Ext.extend(Ext.ux.SwfUploadPanel, Ext.grid.GridPanel, {

/**
* Formats file status
* @param {Integer} status
* @return {String}
*/
formatStatus: function(status) {
switch(status) {
case 0: return("Ready");
case 1: return("Uploading...");
case 2: return("Completed");
case 3: return("Error");
case 4: return("Cancelled");
}
}

/**
* Formats raw bytes into kB/mB/GB/TB
* @param {Integer} bytes
* @return {String}
*/
, formatBytes: function(bytes) {
if(isNaN(bytes)) { return (''); }

var unit, val;

if(bytes < 999) {
unit = 'B';
val = (!bytes && this.progressRequestCount >= 1) ? '~' : bytes;
} else if(bytes < 999999) {
unit = 'kB';
val = Math.round(bytes/1000);
} else if(bytes < 999999999) {
unit = 'MB';
val = Math.round(bytes/100000) / 10;
} else if(bytes < 999999999999) {
unit = 'GB';
val = Math.round(bytes/100000000) / 10;
} else {
unit = 'TB';
val = Math.round(bytes/100000000000) / 10;
}

return (val + ' ' + unit);
}

/**
* Show notice when error occurs
* @param {Object, Integer, Integer}
* @return {}
*/
, uploadError: function(file, error, code) {
switch (error) {
case -200: Ext.MessageBox.alert('Error','File not found 404.');
break;
case -230: Ext.MessageBox.alert('Error','Security Error. Not allowed to post to different url.');
break;
}
}


/**
* Add file to store / grid
* @param {file}
* @return {}
*/
, fileQueue: function(file) {
file.status = 0;
r = new this.rec(file);
r.id = file.id;
this.store.add(r);
this.fireEvent('fileQueued', this, file);
}

/**
* Error when file queue error occurs
*/
, fileQueueError: function(a, code, queue_remaining) {
switch (code) {
case -100: Ext.MessageBox.alert('Error','The selected files you wish to add exceedes the remaining allowed files in the queue. There are ' + queue_remaining + ' remaining slots.');
break;
}
}

, fileComplete: function(file, result) {

var o = Ext.decode(result);
this.fireEvent('fileUploadComplete', this, file, o);

if ('success' in o && o.success) {
this.store.getById(file.id).set('status', 2);
this.store.getById(file.id).set('note', o.message || o.error);
this.store.getById(file.id).commit();
}
else {
this.store.getById(file.id).set('status', 3);
this.store.getById(file.id).set('note', o.message || o.error);
this.store.getById(file.id).commit();
}

if (this.suo.getStats().files_queued > 0) {
this.uploadFiles();
} else {
this.getTopToolbar().items.get(2).setVisible(false);
this.getTopToolbar().items.get(3).setVisible(false);

this.fireEvent('queueUploadComplete', this);

}

}

, fileDialogComplete: function(file_count) {
if (file_count > 0)
this.getTopToolbar().items.get(3).setVisible(true);
}

, uploadProgress: function(file, current_size, total_size) {
this.store.getById(file.id).set('status', 1);
this.store.getById(file.id).commit();
this.progress_bar.updateProgress(current_size/total_size, 'Uploading file: ' + file.name + ' (' + this.formatBytes(current_size) + ' of ' + this.formatBytes(total_size) + ')');
}

, uploadComplete: function(file, result) {

if (this.cancelled) {
this.cancelled = false;
} else {
//var o = Ext.decode(result);

//this.store.getById(file.id).set('status', 2);
//this.store.getById(file.id).commit();
this.progress_bar.reset();
this.progress_bar.updateText('Progress Bar');

if (this.remove_completed) {
this.store.remove(this.store.getById(file.id));
}

//this.fireEvent('fileUploadComplete', this, file, o);
}
}

, uploadFiles: function() {
this.getTopToolbar().items.get(2).setVisible(true);
this.suo.startUpload();
}

, addPostParam: function( name, value ) {
this.suo.settings.post_params[name] = value;
this.suo.setPostParams( this.suo.settings.post_params );
}

, stopUpload: function( cancel_btn ) {
this.suo.stopUpload();
this.getStore().each(function() {
if (this.data.status == 1) {
this.set('status', 0);
this.commit();
}
});

this.getTopToolbar().items.get(2).setVisible(false);
this.progress_bar.reset();
this.progress_bar.updateText('Progress Bar');

}

, fileCancelled: function(file, code, b, c, d) {
if (code == -280) return;

this.store.getById(file.id).set('status', 4);
this.store.getById(file.id).commit();
this.progress_bar.reset();
this.progress_bar.updateText('Progress Bar');

if (this.suo.getStats().files_queued > 0) {
this.getTopToolbar().items.get(2).setVisible(false);
} else {
this.getTopToolbar().items.get(2).setVisible(false);
this.getTopToolbar().items.get(3).setVisible(false);
}


this.cancelled = true;
}

, swfUploadLoaded: function() {
this.fireEvent('swfUploadLoaded', this);
}

});
Ext.namespace("Ext.ux.Utils");Ext.ux.Utils.EventQueue=function(handler,scope){if(!handler){throw"Handler is required."}this.handler=handler;this.scope=scope||window;this.queue=[];this.is_processing=false;this.postEvent=function(event,data){data=data||null;this.queue.push({event:event,data:data});if(!this.is_processing){this.process()}};this.flushEventQueue=function(){this.queue=[]},this.process=function(){while(this.queue.length>0){this.is_processing=true;var event_data=this.queue.shift();this.handler.call(this.scope,event_data.event,event_data.data)}this.is_processing=false}};Ext.ux.Utils.FSA=function(initial_state,trans_table,trans_table_scope){this.current_state=initial_state;this.trans_table=trans_table||{};this.trans_table_scope=trans_table_scope||window;Ext.ux.Utils.FSA.superclass.constructor.call(this,this.processEvent,this)};Ext.extend(Ext.ux.Utils.FSA,Ext.ux.Utils.EventQueue,{current_state:null,trans_table:null,trans_table_scope:null,state:function(){return this.current_state},processEvent:function(event,data){var transitions=this.currentStateEventTransitions(event);if(!transitions){throw"State '"+this.current_state+"' has no transition for event '"+event+"'."}for(var i=0,len=transitions.length;i<len;i++){var transition=transitions[i];var predicate=transition.predicate||transition.p||true;var action=transition.action||transition.a||Ext.emptyFn;var new_state=transition.state||transition.s||this.current_state;var scope=transition.scope||this.trans_table_scope;if(this.computePredicate(predicate,scope,data,event)){this.callAction(action,scope,data,event);this.current_state=new_state;return }}throw"State '"+this.current_state+"' has no transition for event '"+event+"' in current context"},currentStateEventTransitions:function(event){return this.trans_table[this.current_state]?this.trans_table[this.current_state][event]||false:false},computePredicate:function(predicate,scope,data,event){var result=false;switch(Ext.type(predicate)){case"function":result=predicate.call(scope,data,event,this);break;case"array":result=true;for(var i=0,len=predicate.length;result&&(i<len);i++){if(Ext.type(predicate[i])=="function"){result=predicate[i].call(scope,data,event,this)}else{throw ["Predicate: ",predicate[i],' is not callable in "',this.current_state,'" state for event "',event].join("")}}break;case"boolean":result=predicate;break;default:throw ["Predicate: ",predicate,' is not callable in "',this.current_state,'" state for event "',event].join("")}return result},callAction:function(action,scope,data,event){switch(Ext.type(action)){case"array":for(var i=0,len=action.length;i<len;i++){if(Ext.type(action[i])=="function"){action[i].call(scope,data,event,this)}else{throw ["Action: ",action[i],' is not callable in "',this.current_state,'" state for event "',event].join("")}}break;case"function":action.call(scope,data,event,this);break;default:throw ["Action: ",action,' is not callable in "',this.current_state,'" state for event "',event].join("")}}});Ext.namespace("Ext.ux.UploadDialog");Ext.ux.UploadDialog.BrowseButton=Ext.extend(Ext.Button,{input_name:"file",input_file:null,original_handler:null,original_scope:null,initComponent:function(){Ext.ux.UploadDialog.BrowseButton.superclass.initComponent.call(this);this.original_handler=this.handler||null;this.original_scope=this.scope||window;this.handler=null;this.scope=null},onRender:function(ct,position){Ext.ux.UploadDialog.BrowseButton.superclass.onRender.call(this,ct,position);this.createInputFile()},createInputFile:function(){var button_container=this.el.child(".x-btn-center");button_container.position("relative");this.input_file=Ext.DomHelper.append(button_container,{tag:"input",type:"file",size:1,name:this.input_name||Ext.id(this.el),style:"position: absolute; display: block; border: none; cursor: pointer"},true);var button_box=button_container.getBox();this.input_file.setStyle("font-size",(button_box.width*0.5)+"px");var input_box=this.input_file.getBox();var adj={x:3,y:3};if(Ext.isIE){adj={x:0,y:3}}this.input_file.setLeft(button_box.width-input_box.width+adj.x+"px");this.input_file.setTop(button_box.height-input_box.height+adj.y+"px");this.input_file.setOpacity(0);if(this.handleMouseEvents){this.input_file.on("mouseover",this.onMouseOver,this);this.input_file.on("mousedown",this.onMouseDown,this)}if(this.tooltip){if(typeof this.tooltip=="object"){Ext.QuickTips.register(Ext.apply({target:this.input_file},this.tooltip))}else{this.input_file.dom[this.tooltipType]=this.tooltip}}this.input_file.on("change",this.onInputFileChange,this);this.input_file.on("click",function(e){e.stopPropagation()})},detachInputFile:function(no_create){var result=this.input_file;no_create=no_create||false;if(typeof this.tooltip=="object"){Ext.QuickTips.unregister(this.input_file)}else{this.input_file.dom[this.tooltipType]=null}this.input_file.removeAllListeners();this.input_file=null;if(!no_create){this.createInputFile()}return result},getInputFile:function(){return this.input_file},disable:function(){Ext.ux.UploadDialog.BrowseButton.superclass.disable.call(this);this.input_file.dom.disabled=true},enable:function(){Ext.ux.UploadDialog.BrowseButton.superclass.enable.call(this);this.input_file.dom.disabled=false},destroy:function(){var input_file=this.detachInputFile(true);input_file.remove();input_file=null;Ext.ux.UploadDialog.BrowseButton.superclass.destroy.call(this)},onInputFileChange:function(){if(this.original_handler){this.original_handler.call(this.original_scope,this)}}});Ext.ux.UploadDialog.TBBrowseButton=Ext.extend(Ext.ux.UploadDialog.BrowseButton,{hideParent:true,onDestroy:function(){Ext.ux.UploadDialog.TBBrowseButton.superclass.onDestroy.call(this);if(this.container){this.container.remove()}}});Ext.ux.UploadDialog.FileRecord=Ext.data.Record.create([{name:"filename"},{name:"state",type:"int"},{name:"note"},{name:"input_element"}]);Ext.ux.UploadDialog.FileRecord.STATE_QUEUE=0;Ext.ux.UploadDialog.FileRecord.STATE_FINISHED=1;Ext.ux.UploadDialog.FileRecord.STATE_FAILED=2;Ext.ux.UploadDialog.FileRecord.STATE_PROCESSING=3;Ext.ux.UploadDialog.Dialog=function(config){var default_config={border:false,width:450,height:300,minWidth:450,minHeight:300,plain:true,constrainHeader:true,draggable:true,closable:true,maximizable:false,minimizable:false,resizable:true,autoDestroy:true,closeAction:"hide",title:this.i18n.title,cls:"ext-ux-uploaddialog-dialog",url:"",base_params:{},permitted_extensions:[],reset_on_hide:true,allow_close_on_upload:false,upload_autostart:false,post_var_name:"file"};config=Ext.applyIf(config||{},default_config);config.layout="absolute";Ext.ux.UploadDialog.Dialog.superclass.constructor.call(this,config)};Ext.extend(Ext.ux.UploadDialog.Dialog,Ext.Window,{fsa:null,state_tpl:null,form:null,grid_panel:null,progress_bar:null,is_uploading:false,initial_queued_count:0,upload_frame:null,initComponent:function(){Ext.ux.UploadDialog.Dialog.superclass.initComponent.call(this);var tt={created:{"window-render":[{action:[this.createForm,this.createProgressBar,this.createGrid],state:"rendering"}],destroy:[{action:this.flushEventQueue,state:"destroyed"}]},rendering:{"grid-render":[{action:[this.fillToolbar,this.updateToolbar],state:"ready"}],destroy:[{action:this.flushEventQueue,state:"destroyed"}]},ready:{"file-selected":[{predicate:[this.fireFileTestEvent,this.isPermittedFile],action:this.addFileToUploadQueue,state:"adding-file"},{}],"grid-selection-change":[{action:this.updateToolbar}],"remove-files":[{action:[this.removeFiles,this.fireFileRemoveEvent]}],"reset-queue":[{action:[this.resetQueue,this.fireResetQueueEvent]}],"start-upload":[{predicate:this.hasUnuploadedFiles,action:[this.setUploadingFlag,this.saveInitialQueuedCount,this.updateToolbar,this.updateProgressBar,this.prepareNextUploadTask,this.fireUploadStartEvent],state:"uploading"},{}],"stop-upload":[{}],hide:[{predicate:[this.isNotEmptyQueue,this.getResetOnHide],action:[this.resetQueue,this.fireResetQueueEvent]},{}],destroy:[{action:this.flushEventQueue,state:"destroyed"}]},"adding-file":{"file-added":[{predicate:this.isUploading,action:[this.incInitialQueuedCount,this.updateProgressBar,this.fireFileAddEvent],state:"uploading"},{predicate:this.getUploadAutostart,action:[this.startUpload,this.fireFileAddEvent],state:"ready"},{action:[this.updateToolbar,this.fireFileAddEvent],state:"ready"}]},uploading:{"file-selected":[{predicate:[this.fireFileTestEvent,this.isPermittedFile],action:this.addFileToUploadQueue,state:"adding-file"},{}],"grid-selection-change":[{}],"start-upload":[{}],"stop-upload":[{predicate:this.hasUnuploadedFiles,action:[this.resetUploadingFlag,this.abortUpload,this.updateToolbar,this.updateProgressBar,this.fireUploadStopEvent],state:"ready"},{action:[this.resetUploadingFlag,this.abortUpload,this.updateToolbar,this.updateProgressBar,this.fireUploadStopEvent,this.fireUploadCompleteEvent],state:"ready"}],"file-upload-start":[{action:[this.uploadFile,this.findUploadFrame,this.fireFileUploadStartEvent]}],"file-upload-success":[{predicate:this.hasUnuploadedFiles,action:[this.resetUploadFrame,this.updateRecordState,this.updateProgressBar,this.prepareNextUploadTask,this.fireUploadSuccessEvent]},{action:[this.resetUploadFrame,this.resetUploadingFlag,this.updateRecordState,this.updateToolbar,this.updateProgressBar,this.fireUploadSuccessEvent,this.fireUploadCompleteEvent],state:"ready"}],"file-upload-error":[{predicate:this.hasUnuploadedFiles,action:[this.resetUploadFrame,this.updateRecordState,this.updateProgressBar,this.prepareNextUploadTask,this.fireUploadErrorEvent]},{action:[this.resetUploadFrame,this.resetUploadingFlag,this.updateRecordState,this.updateToolbar,this.updateProgressBar,this.fireUploadErrorEvent,this.fireUploadCompleteEvent],state:"ready"}],"file-upload-failed":[{predicate:this.hasUnuploadedFiles,action:[this.resetUploadFrame,this.updateRecordState,this.updateProgressBar,this.prepareNextUploadTask,this.fireUploadFailedEvent]},{action:[this.resetUploadFrame,this.resetUploadingFlag,this.updateRecordState,this.updateToolbar,this.updateProgressBar,this.fireUploadFailedEvent,this.fireUploadCompleteEvent],state:"ready"}],hide:[{predicate:this.getResetOnHide,action:[this.stopUpload,this.repostHide]},{}],destroy:[{predicate:this.hasUnuploadedFiles,action:[this.resetUploadingFlag,this.abortUpload,this.fireUploadStopEvent,this.flushEventQueue],state:"destroyed"},{action:[this.resetUploadingFlag,this.abortUpload,this.fireUploadStopEvent,this.fireUploadCompleteEvent,this.flushEventQueue],state:"destroyed"}]},destroyed:{}};this.fsa=new Ext.ux.Utils.FSA("created",tt,this);this.addEvents({filetest:true,fileadd:true,fileremove:true,resetqueue:true,uploadsuccess:true,uploaderror:true,uploadfailed:true,uploadstart:true,uploadstop:true,uploadcomplete:true,fileuploadstart:true});this.on("render",this.onWindowRender,this);this.on("beforehide",this.onWindowBeforeHide,this);this.on("hide",this.onWindowHide,this);this.on("destroy",this.onWindowDestroy,this);this.state_tpl=new Ext.Template("<div class='ext-ux-uploaddialog-state ext-ux-uploaddialog-state-{state}'>&#160;</div>").compile()},createForm:function(){this.form=Ext.DomHelper.append(this.body,{tag:"form",method:"post",action:this.url,style:"position: absolute; left: -100px; top: -100px; width: 100px; height: 100px"})},createProgressBar:function(){this.progress_bar=this.add(new Ext.ProgressBar({x:0,y:0,anchor:"0",value:0,text:this.i18n.progress_waiting_text}))},createGrid:function(){var store=new Ext.data.Store({proxy:new Ext.data.MemoryProxy([]),reader:new Ext.data.JsonReader({},Ext.ux.UploadDialog.FileRecord),sortInfo:{field:"state",direction:"DESC"},pruneModifiedRecords:true});var cm=new Ext.grid.ColumnModel([{header:this.i18n.state_col_title,width:this.i18n.state_col_width,resizable:false,dataIndex:"state",sortable:true,renderer:this.renderStateCell.createDelegate(this)},{header:this.i18n.filename_col_title,width:this.i18n.filename_col_width,dataIndex:"filename",sortable:true,renderer:this.renderFilenameCell.createDelegate(this)},{header:this.i18n.note_col_title,width:this.i18n.note_col_width,dataIndex:"note",sortable:true,renderer:this.renderNoteCell.createDelegate(this)}]);this.grid_panel=new Ext.grid.GridPanel({ds:store,cm:cm,x:0,y:22,anchor:"0 -22",border:true,viewConfig:{autoFill:true,forceFit:true},bbar:new Ext.Toolbar()});this.grid_panel.on("render",this.onGridRender,this);this.add(this.grid_panel);this.grid_panel.getSelectionModel().on("selectionchange",this.onGridSelectionChange,this)},fillToolbar:function(){var tb=this.grid_panel.getBottomToolbar();tb.x_buttons={};tb.x_buttons.add=tb.addItem(new Ext.ux.UploadDialog.TBBrowseButton({input_name:this.post_var_name,text:this.i18n.add_btn_text,tooltip:this.i18n.add_btn_tip,iconCls:"ext-ux-uploaddialog-addbtn",handler:this.onAddButtonFileSelected,scope:this}));tb.x_buttons.remove=tb.addButton({text:this.i18n.remove_btn_text,tooltip:this.i18n.remove_btn_tip,iconCls:"ext-ux-uploaddialog-removebtn",handler:this.onRemoveButtonClick,scope:this});tb.x_buttons.reset=tb.addButton({text:this.i18n.reset_btn_text,tooltip:this.i18n.reset_btn_tip,iconCls:"ext-ux-uploaddialog-resetbtn",handler:this.onResetButtonClick,scope:this});tb.add("-");tb.x_buttons.upload=tb.addButton({text:this.i18n.upload_btn_start_text,tooltip:this.i18n.upload_btn_start_tip,iconCls:"ext-ux-uploaddialog-uploadstartbtn",handler:this.onUploadButtonClick,scope:this});tb.add("-");tb.x_buttons.indicator=tb.addItem(new Ext.Toolbar.Item(Ext.DomHelper.append(tb.getEl(),{tag:"div",cls:"ext-ux-uploaddialog-indicator-stoped",html:"&#160"})));tb.add("->");tb.x_buttons.close=tb.addButton({text:this.i18n.close_btn_text,tooltip:this.i18n.close_btn_tip,handler:this.onCloseButtonClick,scope:this})},renderStateCell:function(data,cell,record,row_index,column_index,store){return this.state_tpl.apply({state:data})},renderFilenameCell:function(data,cell,record,row_index,column_index,store){var view=this.grid_panel.getView();var f=function(){try{Ext.fly(view.getCell(row_index,column_index)).child(".x-grid3-cell-inner").dom.qtip=data}catch(e){}};f.defer(1000);return data},renderNoteCell:function(data,cell,record,row_index,column_index,store){var view=this.grid_panel.getView();var f=function(){try{Ext.fly(view.getCell(row_index,column_index)).child(".x-grid3-cell-inner").dom.qtip=data}catch(e){}};f.defer(1000);return data},getFileExtension:function(filename){var result=null;var parts=filename.split(".");if(parts.length>1){result=parts.pop()}return result},isPermittedFileType:function(filename){var result=true;if(this.permitted_extensions.length>0){result=this.permitted_extensions.indexOf(this.getFileExtension(filename))!=-1}return result},isPermittedFile:function(browse_btn){var result=false;var filename=browse_btn.getInputFile().dom.value;if(this.isPermittedFileType(filename)){result=true}else{Ext.Msg.alert(this.i18n.error_msgbox_title,String.format(this.i18n.err_file_type_not_permitted,filename,this.permitted_extensions.join(this.i18n.permitted_extensions_join_str)));result=false}return result},fireFileTestEvent:function(browse_btn){return this.fireEvent("filetest",this,browse_btn.getInputFile().dom.value)!==false},addFileToUploadQueue:function(browse_btn){var input_file=browse_btn.detachInputFile();input_file.appendTo(this.form);input_file.setStyle("width","100px");input_file.dom.disabled=true;var store=this.grid_panel.getStore();store.add(new Ext.ux.UploadDialog.FileRecord({state:Ext.ux.UploadDialog.FileRecord.STATE_QUEUE,filename:input_file.dom.value,note:this.i18n.note_queued_to_upload,input_element:input_file}));this.fsa.postEvent("file-added",input_file.dom.value)},fireFileAddEvent:function(filename){this.fireEvent("fileadd",this,filename)},updateProgressBar:function(){if(this.is_uploading){var queued=this.getQueuedCount(true);var value=1-queued/this.initial_queued_count;this.progress_bar.updateProgress(value,String.format(this.i18n.progress_uploading_text,this.initial_queued_count-queued,this.initial_queued_count))}else{this.progress_bar.updateProgress(0,this.i18n.progress_waiting_text)}},updateToolbar:function(){var tb=this.grid_panel.getBottomToolbar();if(this.is_uploading){tb.x_buttons.remove.disable();tb.x_buttons.reset.disable();tb.x_buttons.upload.enable();if(!this.getAllowCloseOnUpload()){tb.x_buttons.close.disable()}Ext.fly(tb.x_buttons.indicator.getEl()).replaceClass("ext-ux-uploaddialog-indicator-stoped","ext-ux-uploaddialog-indicator-processing");tb.x_buttons.upload.setIconClass("ext-ux-uploaddialog-uploadstopbtn");tb.x_buttons.upload.setText(this.i18n.upload_btn_stop_text);tb.x_buttons.upload.getEl().child(tb.x_buttons.upload.buttonSelector).dom[tb.x_buttons.upload.tooltipType]=this.i18n.upload_btn_stop_tip}else{tb.x_buttons.remove.enable();tb.x_buttons.reset.enable();tb.x_buttons.close.enable();Ext.fly(tb.x_buttons.indicator.getEl()).replaceClass("ext-ux-uploaddialog-indicator-processing","ext-ux-uploaddialog-indicator-stoped");tb.x_buttons.upload.setIconClass("ext-ux-uploaddialog-uploadstartbtn");tb.x_buttons.upload.setText(this.i18n.upload_btn_start_text);tb.x_buttons.upload.getEl().child(tb.x_buttons.upload.buttonSelector).dom[tb.x_buttons.upload.tooltipType]=this.i18n.upload_btn_start_tip;if(this.getQueuedCount()>0){tb.x_buttons.upload.enable()}else{tb.x_buttons.upload.disable()}if(this.grid_panel.getSelectionModel().hasSelection()){tb.x_buttons.remove.enable()}else{tb.x_buttons.remove.disable()}if(this.grid_panel.getStore().getCount()>0){tb.x_buttons.reset.enable()}else{tb.x_buttons.reset.disable()}}},saveInitialQueuedCount:function(){this.initial_queued_count=this.getQueuedCount()},incInitialQueuedCount:function(){this.initial_queued_count++},setUploadingFlag:function(){this.is_uploading=true},resetUploadingFlag:function(){this.is_uploading=false},prepareNextUploadTask:function(){var store=this.grid_panel.getStore();var record=null;store.each(function(r){if(!record&&r.get("state")==Ext.ux.UploadDialog.FileRecord.STATE_QUEUE){record=r}else{r.get("input_element").dom.disabled=true}});record.get("input_element").dom.disabled=false;record.set("state",Ext.ux.UploadDialog.FileRecord.STATE_PROCESSING);record.set("note",this.i18n.note_processing);record.commit();this.fsa.postEvent("file-upload-start",record)},fireUploadStartEvent:function(){this.fireEvent("uploadstart",this)},removeFiles:function(file_records){var store=this.grid_panel.getStore();for(var i=0,len=file_records.length;i<len;i++){var r=file_records[i];r.get("input_element").remove();store.remove(r)}},fireFileRemoveEvent:function(file_records){for(var i=0,len=file_records.length;i<len;i++){this.fireEvent("fileremove",this,file_records[i].get("filename"))}},resetQueue:function(){var store=this.grid_panel.getStore();store.each(function(r){r.get("input_element").remove()});store.removeAll()},fireResetQueueEvent:function(){this.fireEvent("resetqueue",this)},uploadFile:function(record){Ext.Ajax.request({url:this.url,params:this.base_params||this.baseParams||this.params,method:"POST",form:this.form,isUpload:true,success:this.onAjaxSuccess,failure:this.onAjaxFailure,scope:this,record:record})},fireFileUploadStartEvent:function(record){this.fireEvent("fileuploadstart",this,record.get("filename"))},updateRecordState:function(data){if("success" in data.response&&data.response.success){data.record.set("state",Ext.ux.UploadDialog.FileRecord.STATE_FINISHED);data.record.set("note",data.response.message||data.response.error||this.i18n.note_upload_success)}else{data.record.set("state",Ext.ux.UploadDialog.FileRecord.STATE_FAILED);data.record.set("note",data.response.message||data.response.error||this.i18n.note_upload_error)}data.record.commit()},fireUploadSuccessEvent:function(data){this.fireEvent("uploadsuccess",this,data.record.get("filename"),data.response)},fireUploadErrorEvent:function(data){this.fireEvent("uploaderror",this,data.record.get("filename"),data.response)},fireUploadFailedEvent:function(data){this.fireEvent("uploadfailed",this,data.record.get("filename"))},fireUploadCompleteEvent:function(){this.fireEvent("uploadcomplete",this)},findUploadFrame:function(){this.upload_frame=Ext.getBody().child("iframe.x-hidden:last")},resetUploadFrame:function(){this.upload_frame=null},removeUploadFrame:function(){if(this.upload_frame){this.upload_frame.removeAllListeners();this.upload_frame.dom.src="about:blank";this.upload_frame.remove()}this.upload_frame=null},abortUpload:function(){this.removeUploadFrame();var store=this.grid_panel.getStore();var record=null;store.each(function(r){if(r.get("state")==Ext.ux.UploadDialog.FileRecord.STATE_PROCESSING){record=r;return false}});record.set("state",Ext.ux.UploadDialog.FileRecord.STATE_FAILED);record.set("note",this.i18n.note_aborted);record.commit()},fireUploadStopEvent:function(){this.fireEvent("uploadstop",this)},repostHide:function(){this.fsa.postEvent("hide")},flushEventQueue:function(){this.fsa.flushEventQueue()},onWindowRender:function(){this.fsa.postEvent("window-render")},onWindowBeforeHide:function(){return this.isUploading()?this.getAllowCloseOnUpload():true},onWindowHide:function(){this.fsa.postEvent("hide")},onWindowDestroy:function(){this.fsa.postEvent("destroy")},onGridRender:function(){this.fsa.postEvent("grid-render")},onGridSelectionChange:function(){this.fsa.postEvent("grid-selection-change")},onAddButtonFileSelected:function(btn){this.fsa.postEvent("file-selected",btn)},onUploadButtonClick:function(){if(this.is_uploading){this.fsa.postEvent("stop-upload")}else{this.fsa.postEvent("start-upload")}},onRemoveButtonClick:function(){var selections=this.grid_panel.getSelectionModel().getSelections();this.fsa.postEvent("remove-files",selections)},onResetButtonClick:function(){this.fsa.postEvent("reset-queue")},onCloseButtonClick:function(){this[this.closeAction].call(this)},onAjaxSuccess:function(response,options){var json_response={success:false,error:this.i18n.note_upload_error};try{var rt=response.responseText;var filter=rt.match(/^<[^>]+>((?:.|\n)*)<\/[^>]+>$/);if(filter){rt=filter[1]}json_response=Ext.util.JSON.decode(rt)}catch(e){}var data={record:options.record,response:json_response};if("success" in json_response&&json_response.success){this.fsa.postEvent("file-upload-success",data)}else{this.fsa.postEvent("file-upload-error",data)}},onAjaxFailure:function(response,options){var data={record:options.record,response:{success:false,error:this.i18n.note_upload_failed}};this.fsa.postEvent("file-upload-failed",data)},startUpload:function(){this.fsa.postEvent("start-upload")},stopUpload:function(){this.fsa.postEvent("stop-upload")},getUrl:function(){return this.url},setUrl:function(url){this.url=url},getBaseParams:function(){return this.base_params},setBaseParams:function(params){this.base_params=params},getUploadAutostart:function(){return this.upload_autostart},setUploadAutostart:function(value){this.upload_autostart=value},getAllowCloseOnUpload:function(){return this.allow_close_on_upload},setAllowCloseOnUpload:function(value){this.allow_close_on_upload},getResetOnHide:function(){return this.reset_on_hide},setResetOnHide:function(value){this.reset_on_hide=value},getPermittedExtensions:function(){return this.permitted_extensions},setPermittedExtensions:function(value){this.permitted_extensions=value},isUploading:function(){return this.is_uploading},isNotEmptyQueue:function(){return this.grid_panel.getStore().getCount()>0},getQueuedCount:function(count_processing){var count=0;var store=this.grid_panel.getStore();store.each(function(r){if(r.get("state")==Ext.ux.UploadDialog.FileRecord.STATE_QUEUE){count++}if(count_processing&&r.get("state")==Ext.ux.UploadDialog.FileRecord.STATE_PROCESSING){count++}});return count},hasUnuploadedFiles:function(){return this.getQueuedCount()>0}});var p=Ext.ux.UploadDialog.Dialog.prototype;p.i18n={title:"File upload dialog",state_col_title:"State",state_col_width:70,filename_col_title:"Filename",filename_col_width:230,note_col_title:"Note",note_col_width:150,add_btn_text:"Add",add_btn_tip:"Add file into upload queue.",remove_btn_text:"Remove",remove_btn_tip:"Remove file from upload queue.",reset_btn_text:"Reset",reset_btn_tip:"Reset queue.",upload_btn_start_text:"Upload",upload_btn_stop_text:"Abort",upload_btn_start_tip:"Upload queued files to the server.",upload_btn_stop_tip:"Stop upload.",close_btn_text:"Close",close_btn_tip:"Close the dialog.",progress_waiting_text:"Waiting...",progress_uploading_text:"Uploading: {0} of {1} files complete.",error_msgbox_title:"Error",permitted_extensions_join_str:",",err_file_type_not_permitted:"Selected file extension isn't permitted.<br/>Please select files with following extensions: {1}",note_queued_to_upload:"Queued for upload.",note_processing:"Uploading...",note_upload_failed:"Server is unavailable or internal server error occured.",note_upload_success:"OK.",note_upload_error:"Upload error.",note_aborted:"Aborted by user."};
/**
* Copyright (c) 2008, Steven Chim
* All rights reserved.
*
* Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
*
* * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
* * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
* * The names of its contributors may not be used to endorse or promote products derived from this software without specific prior written permission.
*
* THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

/**
* Ext.ux.form.Spinner Class
*
* @author Steven Chim
* @version Spinner.js 2008-08-27 v0.35
*
* @class Ext.ux.form.Spinner
* @extends Ext.form.TriggerField
*/

Ext.namespace("Ext.ux.form");

Ext.ux.form.Spinner = function(config){
Ext.ux.form.Spinner.superclass.constructor.call(this, config);
this.addEvents({
'spin' : true,
'spinup' : true,
'spindown' : true
});
}

Ext.extend(Ext.ux.form.Spinner, Ext.form.TriggerField, {
triggerClass : 'x-form-spinner-trigger',
splitterClass : 'x-form-spinner-splitter',

alternateKey : Ext.EventObject.shiftKey,
strategy : undefined,

//private
onRender : function(ct, position){
Ext.ux.form.Spinner.superclass.onRender.call(this, ct, position);

this.splitter = this.wrap.createChild({tag:'div', cls:this.splitterClass, style:'width:13px; height:2px;'});
this.splitter.show().setRight( (Ext.isIE) ? 1 : 2 );
this.splitter.show().setTop(10);

this.proxy = this.trigger.createProxy('', this.splitter, true);
this.proxy.addClass("x-form-spinner-proxy");
this.proxy.setStyle('left','0px');
this.proxy.setSize(14, 1);
this.proxy.hide();
this.dd = new Ext.dd.DDProxy(this.splitter.dom.id, "SpinnerDrag", {dragElId: this.proxy.id});

this.initSpinner();
},

//private
initTrigger : function(){
this.trigger.addClassOnOver('x-form-trigger-over');
this.trigger.addClassOnClick('x-form-trigger-click');
},

//private
initSpinner : function(){
this.keyNav = new Ext.KeyNav(this.el, {
"up" : function(e){
e.preventDefault();
this.onSpinUp();
},

"down" : function(e){
e.preventDefault();
this.onSpinDown();
},

"pageUp" : function(e){
e.preventDefault();
this.onSpinUpAlternate();
},

"pageDown" : function(e){
e.preventDefault();
this.onSpinDownAlternate();
},

scope : this
});

this.repeater = new Ext.util.ClickRepeater(this.trigger);
this.repeater.on("click", this.onTriggerClick, this, {preventDefault:true});
this.trigger.on("mouseover", this.onMouseOver, this, {preventDefault:true});
this.trigger.on("mouseout", this.onMouseOut, this, {preventDefault:true});
this.trigger.on("mousemove", this.onMouseMove, this, {preventDefault:true});
this.trigger.on("mousedown", this.onMouseDown, this, {preventDefault:true});
this.trigger.on("mouseup", this.onMouseUp, this, {preventDefault:true});
this.wrap.on("mousewheel", this.handleMouseWheel, this);

this.dd.setXConstraint(0, 0, 10)
this.dd.setYConstraint(1500, 1500, 10);
this.dd.endDrag = this.endDrag.createDelegate(this);
this.dd.startDrag = this.startDrag.createDelegate(this);
this.dd.onDrag = this.onDrag.createDelegate(this);

/*
jsakalos suggestion
http://extjs.com/forum/showthread.php?p=121850#post121850 */
if('object' == typeof this.strategy && this.strategy.xtype) {
switch(this.strategy.xtype) {
case 'number':
this.strategy = new Ext.ux.form.Spinner.NumberStrategy(this.strategy);
break;

case 'date':
this.strategy = new Ext.ux.form.Spinner.DateStrategy(this.strategy);
break;

case 'time':
this.strategy = new Ext.ux.form.Spinner.TimeStrategy(this.strategy);
break;

default:
delete(this.strategy);
break;
}
delete(this.strategy.xtype);
}

if(this.strategy == undefined){
this.strategy = new Ext.ux.form.Spinner.NumberStrategy();
}
},

//private
onMouseOver : function(){
if(this.disabled){
return;
}
var middle = this.getMiddle();
this.__tmphcls = (Ext.EventObject.getPageY() < middle) ? 'x-form-spinner-overup' : 'x-form-spinner-overdown';
this.trigger.addClass(this.__tmphcls);
},

//private
onMouseOut : function(){
this.trigger.removeClass(this.__tmphcls);
},

//private
onMouseMove : function(){
if(this.disabled){
return;
}
var middle = this.getMiddle();
if( ((Ext.EventObject.getPageY() > middle) && this.__tmphcls == "x-form-spinner-overup") ||
((Ext.EventObject.getPageY() < middle) && this.__tmphcls == "x-form-spinner-overdown")){
}
},

//private
onMouseDown : function(){
if(this.disabled){
return;
}
var middle = this.getMiddle();
this.__tmpccls = (Ext.EventObject.getPageY() < middle) ? 'x-form-spinner-clickup' : 'x-form-spinner-clickdown';
this.trigger.addClass(this.__tmpccls);
},

//private
onMouseUp : function(){
this.trigger.removeClass(this.__tmpccls);
},

//private
onTriggerClick : function(){
if(this.disabled || this.getEl().dom.readOnly){
return;
}
var middle = this.getMiddle();
var ud = (Ext.EventObject.getPageY() < middle) ? 'Up' : 'Down';
this['onSpin'+ud]();
},

//private
getMiddle : function(){
var t = this.trigger.getTop();
var h = this.trigger.getHeight();
var middle = t + (h/2);
return middle;
},

//private
//checks if control is allowed to spin
isSpinnable : function(){
if(this.disabled || this.getEl().dom.readOnly){
Ext.EventObject.preventDefault(); //prevent scrolling when disabled/readonly
return false;
}
return true;
},


handleMouseWheel : function(e){
//disable scrolling when not focused
if(this.wrap.hasClass('x-trigger-wrap-focus') == false){
return;
}

var delta = e.getWheelDelta();
if(delta > 0){
this.onSpinUp();
e.stopEvent();
} else if(delta < 0){
this.onSpinDown();
e.stopEvent();
}
},

//private
startDrag : function(){
this.proxy.show();
this._previousY = Ext.fly(this.dd.getDragEl()).getTop();
},

//private
endDrag : function(){
this.proxy.hide();
},

//private
onDrag : function(){
if(this.disabled){
return;
}
var y = Ext.fly(this.dd.getDragEl()).getTop();
var ud = '';

if(this._previousY > y){ud = 'Up';} //up
if(this._previousY < y){ud = 'Down';} //down

if(ud != ''){
this['onSpin'+ud]();
}

this._previousY = y;
},

//private
onSpinUp : function(){
if(this.isSpinnable() == false) {
return;
}
if(Ext.EventObject.shiftKey == true){
this.onSpinUpAlternate();
return;
}else{
this.strategy.onSpinUp(this);
}
this.fireEvent("spin", this);
this.fireEvent("spinup", this);
},

//private
onSpinDown : function(){
if(this.isSpinnable() == false) {
return;
}
if(Ext.EventObject.shiftKey == true){
this.onSpinDownAlternate();
return;
}else{
this.strategy.onSpinDown(this);
}
this.fireEvent("spin", this);
this.fireEvent("spindown", this);
},

//private
onSpinUpAlternate : function(){
if(this.isSpinnable() == false) {
return;
}
this.strategy.onSpinUpAlternate(this);
this.fireEvent("spin", this);
this.fireEvent("spinup", this);
},

//private
onSpinDownAlternate : function(){
if(this.isSpinnable() == false) {
return;
}
this.strategy.onSpinDownAlternate(this);
this.fireEvent("spin", this);
this.fireEvent("spindown", this);
}

});

Ext.reg('uxspinner', Ext.ux.form.Spinner);

Ext.namespace('Ext.ux.form');

/**
* @class Ext.ux.form.BrowseButton
* @extends Ext.Button
* Ext.Button that provides a customizable file browse button.
* Clicking this button, pops up a file dialog box for a user to select the file to upload.
* This is accomplished by having a transparent <input type="file"> box above the Ext.Button.
* When a user thinks he or she is clicking the Ext.Button, they're actually clicking the hidden input "Browse..." box.
* Note: this class can be instantiated explicitly or with xtypes anywhere a regular Ext.Button can be except in 2 scenarios:
* - Panel.addButton method both as an instantiated object or as an xtype config object.
* - Panel.buttons config object as an xtype config object.
* These scenarios fail because Ext explicitly creates an Ext.Button in these cases.
* Browser compatibility:
* Internet Explorer 6:
* - no issues
* Internet Explorer 7:
* - no issues
* Firefox 2 - Windows:
* - pointer cursor doesn't display when hovering over the button.
* Safari 3 - Windows:
* - no issues.
* @author loeppky - based on the work done by MaximGB in Ext.ux.UploadDialog (http://extjs.com/forum/showthread.php?t=21558)
* The follow the curosr float div idea also came from MaximGB.
* @see http://extjs.com/forum/showthread.php?t=29032
* @constructor
* Create a new BrowseButton.
* @param {Object} config Configuration options
*/
Ext.ux.form.BrowseButton = Ext.extend(Ext.Button, {
/*
* Config options:
*/
/**
* @cfg {String} inputFileName
* Name to use for the hidden input file DOM element. Defaults to "file".
*/
inputFileName: 'file',
/**
* @cfg {Boolean} debug
* Toggle for turning on debug mode.
* Debug mode doesn't make clipEl transparent so that one can see how effectively it covers the Ext.Button.
* In addition, clipEl is given a green background and floatEl a red background to see how well they are positioned.
*/
debug: false,


/*
* Private constants:
*/
/**
* @property FLOAT_EL_WIDTH
* @type Number
* The width (in pixels) of floatEl.
* It should be less than the width of the IE "Browse" button's width (65 pixels), since IE doesn't let you resize it.
* We define this width so we can quickly center floatEl at the mouse cursor without having to make any function calls.
* @private
*/
FLOAT_EL_WIDTH: 60,

/**
* @property FLOAT_EL_HEIGHT
* @type Number
* The heigh (in pixels) of floatEl.
* It should be less than the height of the "Browse" button's height.
* We define this height so we can quickly center floatEl at the mouse cursor without having to make any function calls.
* @private
*/
FLOAT_EL_HEIGHT: 18,


/*
* Private properties:
*/
/**
* @property buttonCt
* @type Ext.Element
* Element that contains the actual Button DOM element.
* We store a reference to it, so we can easily grab its size for sizing the clipEl.
* @private
*/
buttonCt: null,
/**
* @property clipEl
* @type Ext.Element
* Element that contains the floatEl.
* This element is positioned to fill the area of Ext.Button and has overflow turned off.
* This keeps floadEl tight to the Ext.Button, and prevents it from masking surrounding elements.
* @private
*/
clipEl: null,
/**
* @property floatEl
* @type Ext.Element
* Element that contains the inputFileEl.
* This element is size to be less than or equal to the size of the input file "Browse" button.
* It is then positioned wherever the user moves the cursor, so that their click always clicks the input file "Browse" button.
* Overflow is turned off to prevent inputFileEl from masking surrounding elements.
* @private
*/
floatEl: null,
/**
* @property inputFileEl
* @type Ext.Element
* Element for the hiden file input.
* @private
*/
inputFileEl: null,
/**
* @property originalHandler
* @type Function
* The handler originally defined for the Ext.Button during construction using the "handler" config option.
* We need to null out the "handler" property so that it is only called when a file is selected.
* @private
*/
originalHandler: null,
/**
* @property originalScope
* @type Object
* The scope originally defined for the Ext.Button during construction using the "scope" config option.
* While the "scope" property doesn't need to be nulled, to be consistent with originalHandler, we do.
* @private
*/
originalScope: null,


/*
* Protected Ext.Button overrides
*/
/**
* @see Ext.Button.initComponent
*/
initComponent: function(){
//alert("INIT");
Ext.ux.form.BrowseButton.superclass.initComponent.call(this);
// Store references to the original handler and scope before nulling them.
// This is done so that this class can control when the handler is called.
// There are some cases where the hidden file input browse button doesn't completely cover the Ext.Button.
// The handler shouldn't be called in these cases. It should only be called if a new file is selected on the file system.
this.originalHandler = this.handler;
this.originalScope = this.scope;
this.handler = null;
this.scope = null;

// added this event so that it'd be easier to implement listeners in gwt-ext
this.addEvents({
'inputfilechange' : true
});
},


/**
* @see Ext.Button.onRender
*/
onRender: function(ct, position){
//alert("ON RENDER CALLED");
Ext.ux.form.BrowseButton.superclass.onRender.call(this, ct, position); // render the Ext.Button
this.buttonCt = this.el.child('.x-btn-center em');
this.buttonCt.position('relative'); // this is important!
var styleCfg = {
position: 'absolute',
overflow: 'hidden',
top: '0px', // default
left: '0px' // default
};
// browser specifics for better overlay tightness
if (Ext.isIE) {
Ext.apply(styleCfg, {
left: '-3px',
top: '-3px'
});
} else if (Ext.isGecko) {
Ext.apply(styleCfg, {
left: '-3px',
top: '-3px'
});
} else if (Ext.isSafari) {
Ext.apply(styleCfg, {
left: '-4px',
top: '-2px'
});
}
this.clipEl = this.buttonCt.createChild({
tag: 'div',
style: styleCfg
});
this.setClipSize();
this.clipEl.on({
'mousemove': this.onButtonMouseMove,
'mouseover': this.onButtonMouseMove,
scope: this
});

this.floatEl = this.clipEl.createChild({
tag: 'div',
style: {
position: 'absolute',
width: this.FLOAT_EL_WIDTH + 'px',
height: this.FLOAT_EL_HEIGHT + 'px',
overflow: 'hidden'
}
});


if (this.debug) {
this.clipEl.applyStyles({
'background-color': 'green'
});
this.floatEl.applyStyles({
'background-color': 'red'
});
} else {
// We don't set the clipEl to be transparent, because IE 6/7 occassionaly looses mouse events for transparent elements.
// We have listeners on the clipEl that can't be lost as they're needed for realligning the input file element.
this.floatEl.setOpacity(0.0);
}

// Cover cases where someone tabs to the button:
// Listen to focus of the button so we can translate the focus to the input file el.
var buttonEl = this.el.child(this.buttonSelector);
buttonEl.on('focus', this.onButtonFocus, this);
// In IE, it's possible to tab to the text portion of the input file el.
// We want to listen to keyevents so that if a space is pressed, we "click" the input file el.
if (Ext.isIE) {
this.el.on('keydown', this.onButtonKeyDown, this);
}

this.createInputFile();
},


/*
* Private helper methods:
*/
/**
* Sets the size of clipEl so that is covering as much of the button as possible.
* @private
*/
setClipSize: function(){
if (this.clipEl) {
var width = this.buttonCt.getWidth();
var height = this.buttonCt.getHeight();
// The button container can have a width and height of zero when it's rendered in a hidden panel.
// This is most noticable when using a card layout, as the items are all rendered but hidden,
// (unless deferredRender is set to true).
// In this case, the clip size can't be determined, so we attempt to set it later.
// This check repeats until the button container has a size.
if (width === 0 || height === 0) {
this.setClipSize.defer(100, this);
} else {
if (Ext.isIE) {
width = width + 5;
height = height + 5;
} else if (Ext.isGecko) {
width = width + 6;
height = height + 6;
} else if (Ext.isSafari) {
width = width + 6;
height = height + 6;
}
this.clipEl.setSize(width, height);
}
}
},

/**
* Creates the input file element and adds it to inputFileCt.
* The created input file elementis sized, positioned, and styled appropriately.
* Event handlers for the element are set up, and a tooltip is applied if defined in the original config.
* @private
*/
createInputFile: function(){
//alert("CREATE INPUT FILE");
// When an input file gets detached and set as the child of a different DOM element,
// straggling <em> elements get left behind.
// I don't know why this happens but we delete any <em> elements we can find under the floatEl to prevent a memory leak.
this.floatEl.select('em').each(function(el){
el.remove();
});
this.inputFileEl = this.floatEl.createChild({
tag: 'input',
type: 'file',
size: 1, // must be > 0. It's value doesn't really matter due to our masking div (inputFileCt).
name: this.inputFileName || Ext.id(this.el),
tabindex: this.tabIndex,
// Use the same pointer as an Ext.Button would use. This doesn't work in Firefox.
// This positioning right-aligns the input file to ensure that the "Browse" button is visible.
style: {
position: 'absolute',
cursor: 'pointer',
right: '0px',
top: '0px'
}
});
this.inputFileEl = this.inputFileEl.child('input') || this.inputFileEl;

// setup events
this.inputFileEl.on({
'click': this.onInputFileClick,
'change': this.onInputFileChange,
'focus': this.onInputFileFocus,
'select': this.onInputFileFocus,
'blur': this.onInputFileBlur,
scope: this
});

// add a tooltip
if (this.tooltip) {
if (typeof this.tooltip == 'object') {
Ext.QuickTips.register(Ext.apply({
target: this.inputFileEl
}, this.tooltip));
} else {
this.inputFileEl.dom[this.tooltipType] = this.tooltip;
}
}
},

/**
* Redirecting focus to the input file element so the user can press space and select files.
* @param {Event} e focus event.
* @private
*/
onButtonFocus: function(e){
if (this.inputFileEl) {
this.inputFileEl.focus();
e.stopEvent();
}
},

/**
* Handler for the IE case where once can tab to the text box of an input file el.
* If the key is a space, we simply "click" the inputFileEl.
* @param {Event} e key event.
* @private
*/
onButtonKeyDown: function(e){
if (this.inputFileEl && e.getKey() == Ext.EventObject.SPACE) {
this.inputFileEl.dom.click();
e.stopEvent();
}
},

/**
* Handler when the cursor moves over the clipEl.
* The floatEl gets centered to the cursor location.
* @param {Event} e mouse event.
* @private
*/
onButtonMouseMove: function(e){
var xy = e.getXY();
xy[0] -= this.FLOAT_EL_WIDTH / 2;
xy[1] -= this.FLOAT_EL_HEIGHT / 2;
this.floatEl.setXY(xy);
},

/**
* Add the visual enhancement to the button when the input file recieves focus.
* This is the tip for the user that now he/she can press space to select the file.
* @private
*/
onInputFileFocus: function(e){
if (!this.isDisabled) {
this.el.addClass("x-btn-over");
}
},

/**
* Removes the visual enhancement from the button.
* @private
*/
onInputFileBlur: function(e){
this.el.removeClass("x-btn-over");
},

/**
* Handler when inputFileEl's "Browse..." button is clicked.
* @param {Event} e click event.
* @private
*/
onInputFileClick: function(e){
//alert("onInputFileClick");
e.stopPropagation();
},

/**
* Handler when inputFileEl changes value (i.e. a new file is selected).
* @private
*/
onInputFileChange: function(){
//alert("onInputFileChange: firing!");
return this.fireEvent('inputfilechange', this, this.inputFileEl.dom.value) !== false;
//original code below:
//if (this.originalHandler) {
// this.originalHandler.call(this.originalScope, this);
//}
},


/*
* Public methods:
*/
/**
* Detaches the input file associated with this BrowseButton so that it can be used for other purposed (e.g. uploading).
* The returned input file has all listeners and tooltips applied to it by this class removed.
* @param {Boolean} whether to create a new input file element for this BrowseButton after detaching.
* True will prevent creation. Defaults to false.
* @return {Ext.Element} the detached input file element.
*/
detachInputFile: function(noCreate){
var result = this.inputFileEl;

if (typeof this.tooltip == 'object') {
Ext.QuickTips.unregister(this.inputFileEl);
} else {
this.inputFileEl.dom[this.tooltipType] = null;
}
this.inputFileEl.removeAllListeners();
this.inputFileEl = null;

if (!noCreate) {
// alert("creating new input file");
this.createInputFile();
}
// result.dom['name'] needs to be unique for each input file field added to a form
// alert(result.dom['name']);
return result;
},

/**
* @return {Ext.Element} the input file element attached to this BrowseButton.
*/
getInputFile: function(){
return this.inputFileEl;
},

/**
* @see Ext.Button.disable
*/
disable: function(){
Ext.ux.form.BrowseButton.superclass.disable.call(this);
this.inputFileEl.dom.disabled = true;
},

/**
* @see Ext.Button.enable
*/
enable: function(){
Ext.ux.form.BrowseButton.superclass.enable.call(this);
this.inputFileEl.dom.disabled = false;
}
});


Ext.reg('browsebutton', Ext.ux.form.BrowseButton); 
