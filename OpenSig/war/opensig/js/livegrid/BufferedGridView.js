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
             *                          grid absolute to it's position in the model.
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
     * scrollbar.  The scrollbar is shown if the total width of all visible 
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
     *                              othwerwise <tt>false</tt>
     */
    reset : function(forceReload)
    {
        if (forceReload === false) {
            this.ds.modified = [];
            this.grid.selModel.clearSelections(true);
            this.rowIndex      = 0;
            this.lastScrollPos = 0;
            this.lastRowIndex = 0;
            this.lastIndex    = 0;
            this.bufferRange   = [0, this.ds.bufferSize];
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
                params.dir  = sInfo.direction;
                params.sort = sInfo.field;
            }
            
            this.ds.load({
                callback : function(){this.reset(false);},
                scope    : this, 
                params   : params
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
        g.enableDrag     = false;
        
        Ext.ux.grid.BufferedGridView.superclass.renderUI.call(this);
        
        var g = this.grid;
        
        g.enableDragDrop = dEnabled;
        g.enableDrag     = dEnabled;
        
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
        this.liveScroller       = new E(cs[0]);
        this.liveScrollerInset  = this.liveScroller.dom.firstChild;
        this.liveScroller.on('scroll', this.onLiveScroll,  this);
        
	    this.mainHd = new E(this.mainWrap.dom.firstChild);
	    this.innerHd = this.mainHd.dom.firstChild;
        this.scroller = new E(this.mainWrap.dom.childNodes[1]);
        if(this.forceFit){
            this.scroller.setStyle('overflow-x', 'hidden');
        }
        this.mainBody = new E(this.scroller.dom.firstChild);

        // addd the mousewheel event to the table's body
        this.mainBody.on('mousewheel', this.handleWheel,  this);
        
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
            this.fireEvent("rowremoved",       this, index, record);
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
            var rec  = this.ds.getAt(lastPossibleRIndex);
            
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
     *                                       from the underlying data model
     * @param {Array} records An array containing the newly added 
     *                        {@link Ext.data.Record}s 
     * @param {Number} index The index of the position in the underlying
     *                       {@link Ext.ux.grid.BufferedStore} where the rows
     *                       were added.
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
                
                this.rowIndex     = this.rowIndex + recordLen;
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
        var end   = start + (recordLen-1);
        var len   = this.getRows().length;
        
        var firstRow = 0;
        var lastRow  = 0;
        
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
            this.rowIndex      += recordLen;
            this.lastRowIndex  = this.rowIndex;
            
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
            lastRow  = Math.min(end, this.rowIndex+this.visibleRows-1) - this.bufferRange[0];
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
     *                         data from the server
     */
    onBeforeLoad : function(store, options)
    {
        if (!options.params) {
            options.params = {start : 0, limit : this.ds.bufferSize};    
        } else {
            options.params.start = 0;
            options.params.limit = this.ds.bufferSize;
        }
        
        options.scope    = this;
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
        this.isBuffering    = false;
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
        var scrollTop     = this.liveScroller.dom.scrollTop; 
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
        
        var index      = 0;
        var selections = this.grid.selModel.selections;
        var ds         = this.ds;
        for(var i = startRow, len = rows.length; i < len; i++){
            index   = i+cursor;
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
        var viewIndexLast  = lastRow  + this.bufferRange[0];
        
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
        
        var html   = this.renderRows(firstRow, lastRow);
    
        var before = this.getRow(firstRow-(this.rowIndex-this.bufferRange[0]));
        
        if (before) {
            Ext.DomHelper.insertHtml('beforeBegin', before, html);
        } else {
            Ext.DomHelper.insertHtml('beforeEnd', this.mainBody.dom, html);
        }
        
        
        
        if (isUpdate === true) {
            var rows   = this.getRows();
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
     *                   <tt>false</tt>
     *
     */
    isInRange : function(rowIndex)
    {
        var lastRowIndex = Math.min(this.ds.totalLength-1, 
                                    rowIndex + this.visibleRows);
        return (rowIndex     >= this.bufferRange[0]) && 
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
     *                        data model 
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
        
        var lastIndex  = this.lastIndex;
        this.lastIndex = index;
        var inRange    = this.isInRange(index);
        
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
        var fetchSize    = this.ds.bufferSize;
        
        if (!inRange) {
            this.showLoadMask(true);
        }
        
        this.fireEvent('beforebuffer', this, this.ds, index, 
                       this.visibleRows, this.ds.totalLength);
        
        this.ds.suspendEvents();
        var sInfo  = this.ds.sortInfo;
        
        var params = {};
        Ext.apply(params, this.ds.lastOptions);
        params.start = bufferOffset;
        params.limit = this.ds.bufferSize;
        
        if (sInfo) {
            params.dir  = sInfo.direction;
            params.sort = sInfo.field;
        }
        this.ds.load({
            callback : this.liveBufferUpdate,
            scope    : this,
            params   : params
        });
        this.ds.resumeEvents();
    },    
    
    /**
     * Shows this' view own load mask to indicate that a large amount of buffer
     * data was requested by the store.
     * @param {Boolean} show <tt>true</tt> to show the load mask, otherwise
     *                       <tt>false</tt>
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
     *                        rendering.
     *
     * @param {Boolean} forceReplace <tt>true</tt> for recomputing the DOM in the
     *                               view, otherwise <tt>false</tt>.
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
        
        var c  = g.getGridEl();

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
        
        var c    = g.getGridEl();
        var cm   = this.cm;
        var size = c.getSize(true);
        var vh   = size.height;    
        
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
            this.rowIndex     = Math.max(0, ds.totalLength-this.visibleRows);
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