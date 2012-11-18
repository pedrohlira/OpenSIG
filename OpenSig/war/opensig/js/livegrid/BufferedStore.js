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
        'versionchange'        : true,
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
        'selectionsload'       : true
        
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
        records      = [].concat(records);
        var cRecords = [];
        var cCount   = this.bufferSize-1;
        var pIndex   = (index == Number.MIN_VALUE || index == Number.MAX_VALUE) ? 0 : index;
     
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
                var old      = this.version;
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
            var last  = this.data.last();
            
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