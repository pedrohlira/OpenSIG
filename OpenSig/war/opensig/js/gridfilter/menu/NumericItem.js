Ext.namespace("Ext.ux.menu");
Ext.ux.menu.NumericItem = Ext.extend(Ext.menu.BaseItem, {
    itemCls : "x-menu-item",
    hideOnClick: false,
    
    initComponent: function(){
    	this.addEvents({keyup: true});
    	
		this.editor = this.editor || new Ext.form.NumberField();
		this.editor.allowNegative = true;
		if(this.text)
			this.editor.setValue(this.text);
    },
    
    onRender: function(container){
        var s = container.createChild({
        	cls: this.itemCls,
        	html: '<img src="' + (this.icon||Ext.BLANK_IMAGE_URL)+ '" class="x-menu-item-icon'+(this.iconCls?' '+this.iconCls:'')+'" style="margin: 3px 7px 2px 2px;" />'});;
        
        Ext.apply(this.config, {width: 125});
        this.editor.render(s);
        
        this.el = s;
        this.relayEvents(this.editor.el, ["keyup"]);
        
        if(Ext.isGecko)
			s.setStyle('overflow', 'auto');
			
        Ext.ux.menu.NumericItem.superclass.onRender.apply(this, arguments);
    },
    
    getValue: function(){
    	return this.editor.getValue();
    },
    
    setValue: function(value){
    	this.editor.setValue(value);
    },
    
    isValid: function(preventMark){
    	return this.editor.isValid(preventMark);
    },
    
    setAllowDecimals: function(allowDecimals){
    	this.editor.allowDecimals = allowDecimals;
    }
});