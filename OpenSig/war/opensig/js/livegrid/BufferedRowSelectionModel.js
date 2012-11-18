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
        'selectiondirty'       : true
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
        
        this.grid.store.on('add',            this.onAdd,            this);
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
     *  <li>1. store.remove(record);</li>
     *  <li>2. view.onRemove(store, record, indexInStore, isUpdate)<br />
     *   [view triggers rowremoved event]</li>
     *  <li>3. this.onRemove(view, indexInStore, record)</li>
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
        
        var viewIndex    = index;
        var fire         = this.bufferedSelections[viewIndex] === true;
        var ranges       = this.getPendingSelections();
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
        var ranges       = this.getPendingSelections();
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
        var s         = ranges[0];
        var e         = ranges[rangesLength-1];
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
        var index         = 0;
        var newSelections = {};
        var newIndex      = 0;
        var newRequests   = {};
        var totalLength = this.grid.store.totalLength;
        
        for (var i in this.bufferedSelections) {
            index    = parseInt(i);
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
        
        this.bufferedSelections  = newSelections;
        this.pendingSelections   = newRequests;
    },
    
    /**
     *
     * @param {Array} records The records that have been loaded 
     * @param {Array} ranges  An array representing the model index ranges the
     *                        reords have been loaded for.
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
        var index         = 1;
        var ranges        = [];
        var currentRange  = 0;
        var tmpArray      = [];
        
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
            this.bufferedSelections  = {};
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


