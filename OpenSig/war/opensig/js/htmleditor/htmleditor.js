Ext.namespace('Ext.ux');

// Ext.ux.HTMLEditorToolbar
// extension of Ext.Toolbar to cater for extensibility
Ext.ux.HTMLEditorToolbar = Ext.extend(Ext.Toolbar, {

  // overrides Ext.Toolbar.initComponent
  // first function to be called upon creation of toolbar
  initComponent: function() {

    // call Ext.Toolbar.initComponent
    Ext.ux.HTMLEditorToolbar.superclass.initComponent.call(this);

    // unable to use existing items collection for pre-render
    // configuration as it's updated by Ext.Toolbar during render
    this.tools = new Ext.util.MixedCollection(false, function(tool) {
      return tool.itemId || tool.id || Ext.id();
    });

  },

  // add tools (pre-render)
  addTools: function(tools) {
    tools = (tools instanceof Array) ? tools : [tools];
    for (var i = 0, len = tools.length; i < len; i++) {
      this.tools.add(tools[i]);
    }
  },

  // insert tools (pre-render)
  insertTools: function(index, tools) {
    tools = (tools instanceof Array) ? tools : [tools];
    for (var i = 0, len = tools.length; i < len; i++) {
      this.tools.insert(index + i, tools[i]);
    }
  },
  
  // insert tools before another tool (pre-render)
  insertToolsBefore: function(itemId, tools) {
    var index = this.tools.indexOfKey(itemId);
    this.insertTools(index, tools);
  },
  
  // insert tools after another tool (pre-render)
  insertToolsAfter: function(itemId, tools) {
    var index = this.tools.indexOfKey(itemId) + 1;
    this.insertTools(index, tools);
  },

  // render tools (performed after tools/plugins have been configured/reordered)
  renderTool: function(tool) {

    // cater for new tbcombo component
    // created to split configuration from render
    if (typeof tool == "object" && tool.xtype && tool.xtype == "tbcombo") {

      // not catered for in Ext.Toolbar.add function
      // as it defaults to addField instead of addItem
      this.addItem(Ext.ComponentMgr.create(tool));

    }
    else {
      
      // else use existing Ext.Toolbar.add function
      // to render tools
      this.add(tool);

    }

  },

  // overrides Ext.Toolbar.onRender
  onRender: function(ct, position) {
    
    // call Ext.Toolbar.onRender
    Ext.ux.HTMLEditorToolbar.superclass.onRender.call(this, ct, position);

    // loop through pre-configured/reordered tools and render each accordingly
    this.tools.each(this.renderTool, this);

  }
  
});

// Ext.ux.HTMLEditorToolbar.ComboBox
// created to handle the pre-configuration of a combobox (pre-render)
Ext.ux.HTMLEditorToolbar.ComboBox = function(config) {
  
  Ext.apply(this, config);

  // create combobox in memory before render
  var selEl = document.createElement("select");
  selEl.className = this.cls;
  for (var i = 0, len = this.opts.length; i < len; i++) {
    var opt = this.opts[i];
    var optEl = document.createElement('option');
    optEl.text = opt.text;
    optEl.value = opt.value;
    if (opt.selected) {
      optEl.selected = true;
      this.defaultValue = opt.value;
    }
    selEl.options.add(optEl);
  }
  if (! this.defaultValue) {
    this.defaultValue = this.opts[0].value;
  }
  
  // call Ext.Toolbar.Item constructor passing combobox
  Ext.ux.HTMLEditorToolbar.ComboBox.superclass.constructor.call(this, selEl);
  
}

// Ext.ux.HTMLEditorToolbar.ComboBox
// extension of Ext.Toolbar.Item
Ext.extend(Ext.ux.HTMLEditorToolbar.ComboBox, Ext.Toolbar.Item, {

  // overrides Ext.Toolbar.Item.render
  render: function(td) {
    
    // call Ext.Toolbar.Item.render
    Ext.ux.HTMLEditorToolbar.ComboBox.superclass.render.call(this, td);

    // add handler for combobox change event
    Ext.EventManager.on(this.el, 'change', this.handler, this.scope);
    
  }
  
});

// register Ext.ux.HTMLEditorToolbar.ComboBox as a new component
Ext.ComponentMgr.registerType('tbcombo', Ext.ux.HTMLEditorToolbar.ComboBox);

// Ext.ux.HTMLEditor
// extends Ext.form.HtmlEditor to provide extensibility
Ext.ux.HTMLEditor = Ext.extend(Ext.form.HtmlEditor, {

  // using the enable... flags to define content meant that items
  // were always added in the same order.
  // using the toolbarItems list instead allows the user to override
  // the order of items, and even exclude items not wanted.
  // the enable... flags are now no longer used
  toolbarItems: [
    'fonts',
  	'allformats',
  	'allfontsizes',
  	'allcolors',
  	'allalignments',
  	'alllinks',
  	'alllists',
  	'sourceedit'
  ],

  // as an alternative, the toolbarItemExcludes list can be used to
  // exclude items from the toolbarItem list
  toolbarItemExcludes: [],

  // overrides Ext.form.HtmlEditor.initComponent
  // first function to be called upon creation of the editor
  initComponent: function() {

    // call Ext.form.HtmlEditor.initComponent
    Ext.ux.HTMLEditor.superclass.initComponent.call(this);

    // add important event missing from Ext.form.HtmlEditor
    this.addEvents({
      editorevent: true
    });

    // remove any toolbarItemExcludes from the toolbarItems array
    for (var i = 0, iMax = this.toolbarItemExcludes.length; i < iMax; i++) {
      var item = this.toolbarItemExcludes[i].toLowerCase();
      for (var j = 0, jMax = this.toolbarItems.length; j < jMax; j++) {
        if (this.toolbarItems[j] == item) {
          this.toolbarItems.splice(j, 1);
          break;
        }
      }
    }

    // create the editor toolbar
    this.tb = new Ext.ux.HTMLEditorToolbar();
    
    // create the toolbar items
    this.createTools(this.toolbarItems);
        
  },

  // overrides Ext.form.HtmlEditor.createFontOptions
  createFontOptions: function() {
    var opts = [], ffs = this.fontFamilies, ff;
    for (var i = 0, len = ffs.length; i < len; i++) {
      ff = ffs[i];
      fflc = ff.toLowerCase();
      var opt = {text: ff, value: fflc};
      if (fflc == this.defaultFont) opt.selected = true;
      opts.push(opt);
    }
    return opts;
  },

  // create default button config
  btn: function(id, toggle, queryState, handler) {
    return {
      itemId: id,
      cls: 'x-btn-icon x-edit-' + id,
      enableToggle: toggle !== false,
      queryState: queryState !== false,
      handler: handler || this.relayBtnCmd,
      scope: this,
      clickEvent: 'mousedown',
      tooltip: this.buttonTips[id] || undefined,
      tabIndex: -1
    };
  },

  // create known tools based on the passed item list (initially
  // from the toolbarItems list) and add it to the tools collection.
  // this function allows random tool allocation as opposed
  // to the old version that added tools sequentially
  createTools: function(toolbarItems) {

    // convert single items to a list
    toolbarItems = (toolbarItems instanceof Array) ? toolbarItems : [toolbarItems];

    // loop through the item list
    for (var i = 0, len = toolbarItems.length; i < len; i++) {

      //add the item to the toolbar
      var item = toolbarItems[i];
      switch (item) {
        
        // add the fonts combobox
        case 'fonts':
          if (! Ext.isSafari) {
            this.tb.addTools({
              itemId: 'fontname',
              xtype: 'tbcombo',
              cls: 'x-font-select',
              opts: this.createFontOptions(),
              queryValue: true,
              handler: function(event, el) {
                this.relayCmd('fontname', el.value);
                this.deferFocus();
              },
              scope: this
            });
  	      }
          break;
  
        // add the bold button
        case 'bold':
          this.tb.addTools(this.btn('bold'));
          break;
  
        // add the italic button
        case 'italic':
          this.tb.addTools(this.btn('italic'));
          break;
  
        // add the underline button
        case 'underline':
          this.tb.addTools(this.btn('underline'));
          break;
  
        // add all format buttons (with a leading separator)
        case 'allformats':
          this.createTools(['-', 'bold', 'italic', 'underline']);
          break;
  
        // add the increasefontsize button
        case 'increasefontsize':
          this.tb.addTools(this.btn('increasefontsize', false, false, this.adjustFont));
          break;
  
        // add the decreasefontsize button
        case 'decreasefontsize':
          this.tb.addTools(this.btn('decreasefontsize', false, false, this.adjustFont));
          break;
  
        // add both fontsize buttons (with a leading separator)
        case 'allfontsizes':
          this.createTools(['-', 'increasefontsize', 'decreasefontsize']);
          break;
  
        // add the forecolor button and associated menu
        case 'forecolor':
          this.tb.addTools({
            itemId: 'forecolor',
            cls: 'x-btn-icon x-edit-forecolor',
            clickEvent: 'mousedown',
            tooltip: this.buttonTips['forecolor'],
            tabIndex: -1,
            menu: new Ext.menu.ColorMenu({
              allowReselect: true,
              focus: Ext.emptyFn,
              value: '000000',
              plain: true,
              selectHandler: function(cp, color) {
                this.execCmd('forecolor', Ext.isSafari || Ext.isIE ? '#' + color : color);
                this.deferFocus();
              },
              scope: this,
              clickEvent:'mousedown'
            })
          });
          break;
  
        // add the backcolor button and associated menu
        case 'backcolor':
          this.tb.addTools({
            itemId: 'backcolor',
            cls: 'x-btn-icon x-edit-backcolor',
            clickEvent: 'mousedown',
            tooltip: this.buttonTips['backcolor'],
            tabIndex: -1,
            menu: new Ext.menu.ColorMenu({
              focus: Ext.emptyFn,
              value: 'FFFFFF',
              plain: true,
              allowReselect: true,
              selectHandler: function(cp, color) {
                if (Ext.isGecko) {
                  this.execCmd('useCSS', false);
                  this.execCmd('hilitecolor', color);
                  this.execCmd('useCSS', true);
                  this.deferFocus();
                }
                else {
                  this.execCmd(Ext.isOpera ? 'hilitecolor' : 'backcolor',
                    Ext.isSafari || Ext.isIE ? '#' + color : color);
                  this.deferFocus();
                }
              },
              scope: this,
              clickEvent: 'mousedown'
            })
          });
          break;
  
        // add both color buttons (with a leading separator)
        case 'allcolors':
          this.createTools(['-', 'forecolor', 'backcolor']);
          break;
  
        // add the justifyleft button
        case 'justifyleft':
          this.tb.addTools(this.btn('justifyleft'));
          break;
  
        // add the justifycenter button
        case 'justifycenter':
          this.tb.addTools(this.btn('justifycenter'));
          break;
  
        // add the justifyright button
        case 'justifyright':
          this.tb.addTools(this.btn('justifyright'));
          break;
  
        // add all alignment buttons (with a leading separator)
        case 'allalignments':
          this.createTools(['-', 'justifyleft', 'justifycenter', 'justifyright']);
          break;
  
        // add the link button
        case 'link':
          if (! Ext.isSafari) {
            this.tb.addTools(this.btn('createlink', false, false, this.createLink));
          }
          break;
  
        // add the link button (with a leading separator)
        case 'alllinks':
          if (! Ext.isSafari) {
            this.createTools(['-', 'link']);
          }
          break;
  
        // add the orderedlist button
        case 'orderedlist':
          if (! Ext.isSafari) {
            this.tb.addTools(this.btn('insertorderedlist'));
          }
          break;
  
        // add the unorderedlist button
        case 'unorderedlist':
          if (! Ext.isSafari) {
            this.tb.addTools(this.btn('insertunorderedlist'));
          }
          break;
  
        // add both list buttons (with a leading separator)
        case 'alllists':
          if (! Ext.isSafari) {
            this.createTools(['-', 'orderedlist', 'unorderedlist']);
          }
          break;
  
        // add the sourceedit button
        case 'sourceedit':
          if (! Ext.isSafari) {
            this.tb.addTools(this.btn('sourceedit', true, false, function(btn) {
              this.toggleSourceEdit(btn.pressed);
            }));
          }
          break;
  
        // allows for '-', 'separator', ' ', '->', labels, or other item types
        default:
          this.tb.addTools(item);
  
      }
    }

  },

  // overrides Ext.form.HtmlEditor.createToolbar
  // most functionality has been removed as this is called
  // upon render 
  createToolbar: function() {

    // render toolbar
    this.tb.render(this.wrap.dom.firstChild);

    // inherited
    this.tb.el.on('click', function(e) {
      e.preventDefault();
    });

  },

  // overrides Ext.form.HtmlEditor.getDocMarkup
  // provides ability to include stylesheets in the editor document
  // created by bpjohnson (see http://extjs.com/forum/showthread.php?t=9588)
  getDocMarkup: function() {
    var markup = '<html><head><style type="text/css">body{border:0;margin:0;padding:3px;height:98%;cursor:text;}</style>';
    if (this.styles) {
      for (var i = 0; i < this.styles.length; i++) {
        markup = markup + '<link rel="stylesheet" type="text/css" href="' + this.styles[i] + '" />';
      }
    }
    markup = markup + '</head><body></body></html>';
    return markup;
  },

  // overrides Ext.form.HtmlEditor.onEditorEvent
  onEditorEvent: function(e) {
    
    // call Ext.form.HtmlEditor.onEditorEvent
    Ext.ux.HTMLEditor.superclass.onEditorEvent.call(this, e);

    // fire new editorevent to tell plugins that an event occurred
    // in the editor.
    // this saves plugins from having to monitor multiple events
    // i.e. 'click', 'keyup', etc.
    this.fireEvent('editorevent', this, e);
    
  },

  // overrides Ext.form.HtmlEditor.updateToolbar
  // does not call superclass function as much of it was no
  // longer needed, but duplicates some code
  updateToolbar: function() {

    // inherited
    if (! this.activated) {
      this.onFirstFocus();
      return;
    }
    
    // loop through toolbar items and update status based on
    // query values return from the browser (if configured)
    this.tb.items.each(function(item) {
      if (item.queryState) {
        item.toggle(this.doc.queryCommandState(item.itemId));
      }
      else if (item.queryEnabled) {
        item.setDisabled(! this.doc.queryCommandEnabled(item.itemId));
      }
      else if (item.xtype = "tbcombo" && item.queryValue) {
        var value = (this.doc.queryCommandValue(item.itemId) || item.defaultValue).toLowerCase();
        if (value != item.el.value) {
          item.el.value = value;
        }
      }
    }, this);
    
    // inherited
    Ext.menu.MenuMgr.hideAll();

    // inherited
    this.syncValue();

  }

});
