// vim: ts=4:sw=4:nu:fdc=4:nospell
/**
 * Ext.ux.grid.Search
 *
 * Search plugin
 *
 * @author    Ing. Jozef Sakalos
 * @copyright (c) 2008, by Ing. Jozef Sakalos
 * @date      17. January 2008
 * @version   $Id: ext-ux-grid-search.js,v 1.1 2008/03/06 18:56:02 u0083428 Exp $
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
    ,
    searchTipText:'Type a text to search and press Enter'
    ,
    position:'top'
    ,
    iconCls:false//'icon-magnifier'
    ,
    checkIndexes:'all'
    ,
    disableIndexes:[]
    ,
    dateFormat:undefined
    ,
    mode:'remote'
    ,
    xtype:'gridsearch'
    ,
    paramNames: {
        fields:'fields'
        ,
        query:'query'
    }
    
    // {{{
    ,
    init:function(grid) {
        this.grid = grid;

        grid.onRender = grid.onRender.createSequence(this.onRender, this);
        grid.reconfigure = grid.reconfigure.createSequence(this.reconfigure, this);
    } // end of function init
    // }}}
    // {{{
    ,
    onRender:function() {
        var grid = this.grid;
        var tb = 'bottom' == this.position ? grid.bottomToolbar : grid.topToolbar;

        // add menu
        this.menu = new Ext.menu.Menu();
        //tb.addSeparator();
        tb.add({
            text:this.searchText
            ,
            menu:this.menu
            ,
            iconCls:this.iconCls
        });

        // add filter field
        this.field = new Ext.form.TwinTriggerField({
            width:this.width
            ,
            selectOnFocus:undefined === this.selectOnFocus ? true : this.selectOnFocus
            ,
            trigger1Class:'x-form-clear-trigger'
            ,
            trigger2Class:'x-form-search-trigger'
            ,
            onTrigger1Click:this.onTriggerClear.createDelegate(this)
            ,
            onTrigger2Click:this.onTriggerSearch.createDelegate(this)
        });
        this.field.on('render', function() {
            this.field.el.dom.qtip = this.searchTipText;
            var map = new Ext.KeyMap(this.field.el, [{
                key:Ext.EventObject.ENTER
                ,
                scope:this
                ,
                fn:this.onTriggerSearch
            },{
                key:Ext.EventObject.ESC
                ,
                scope:this
                ,
                fn:this.onTriggerClear
            }]);
            map.stopEvent = true;
        }, this, {
            single:true
        });

        tb.add(this.field);

        // reconfigure
        this.reconfigure();
    } // end of function onRender
    // }}}
    // {{{
    ,
    onTriggerClear:function() {
        this.field.setValue('');
        this.field.focus();
        this.onTriggerSearch();
    } // end of function onTriggerClear
    // }}}
    // {{{
    ,
    onTriggerSearch:function() {
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
        }
        else {
            // get fields to search array
            var params = {};
            this.menu.items.each(function(item) {
                if(item.checked) {
                    params['[field]'] = item.dataIndex;
                    params['[query]'] = val;
                }
            });

            // reload store
            store.baseParams = params;
            store.reload();

        /*
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
            */
        }

    } // end of function onTriggerSearch
    // }}}
    // {{{
    ,
    setDisabled:function() {
        this.field.setDisabled.apply(this.field, arguments);
    } // end of function setDisabled
    // }}}
    // {{{
    ,
    enable:function() {
        this.setDisabled(false);
    } // end of function enable
    // }}}
    // {{{
    ,
    disable:function() {
        this.setDisabled(true);
    } // end of function disable
    // }}}
    // {{{
    ,
    reconfigure:function() {

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
                        ,
                        hideOnClick:false
                        ,
                        checked:'all' === this.checkIndexes
                        ,
                        dataIndex:config.dataIndex
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