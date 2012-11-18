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
    
  //  this.addEvents({
    //    'startdrag' : true
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
    this.view.ds.on('selectionsload',       this.onSelectionsLoad,       this);
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