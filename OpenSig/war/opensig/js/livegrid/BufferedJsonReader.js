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
     *                               version of the data repository this reader parses 
     *                               the reponse from
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