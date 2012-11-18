/**
 * Makes a Panel to provide the ability to upload multiple files using the SwfUpload flash script.
 *
 * @author Michael Giddens
 * @website http://www.silverbiology.com
 * @created 2008-03-06
 * @version 0.4
 *
 * @known_issues 
 *		- Progress bar used hardcoded width. Not sure how to make 100% in bbar
 *		- Panel requires width / height to be set.  Not sure why it will not fit
 *		- when panel is nested sometimes the column model is not always shown to fit until a file is added. Render order issue.
*/

// Create user extension namespace
Ext.namespace('Ext.ux');

Ext.ux.SwfUploadPanel = function(config){

	// Default Params for SwfUploader
//	if (!config.title) config.title = 'File Upload';
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
				 {name: 'status'}
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
//			, width: config.width - 7
  }); 

	var cm = new Ext.grid.ColumnModel([{id:'name', header: "Filename", dataIndex: 'name'}
				,	{id:'size', header: "Size", width: 80, dataIndex: 'size', renderer: this.formatBytes }
				,	{id:'status', header: "Status", width: 80, dataIndex: 'status', renderer: this.formatStatus }
			]);
																		 
	config = config || {};
	config = Ext.apply(config || {}, {

			store: store
		,	cm: cm
		, autoExpandColumn: 'name'
		, enableColumnResize: false
		, enableColumnMove: false
					
		,	sm: new Ext.grid.RowSelectionModel({singleSelect: config.single_select})
		,	tbar: [{	text:'Add File(s)'
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
			
							'keypress': function(e) {
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
									removeRows(this);
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
	}	catch (ex1) {
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
	,	formatBytes: function(bytes) {
			if(isNaN(bytes)) {	return (''); }
	
			var unit, val;
	
			if(bytes < 999)	{
				unit = 'B';
				val = (!bytes && this.progressRequestCount >= 1) ? '~' : bytes;
			}	else if(bytes < 999999)	{
				unit = 'kB';
				val = Math.round(bytes/1000);
			}	else if(bytes < 999999999)	{
				unit = 'MB';
				val = Math.round(bytes/100000) / 10;
			}	else if(bytes < 999999999999)	{
				unit = 'GB';
				val = Math.round(bytes/100000000) / 10;
			}	else	{
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
				case -200: 	Ext.MessageBox.alert('Error','File not found 404.');
										break;
				case -230: 	Ext.MessageBox.alert('Error','Security Error. Not allowed to post to different url.');
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
				case -100: 	Ext.MessageBox.alert('Error','The selected files you wish to add exceedes the remaining allowed files in the queue. There are ' + queue_remaining + ' remaining slots.');
										break;
			}
		}

	, fileComplete: function(file) {

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
				var o = Ext.decode(result);	
	
				this.store.getById(file.id).set('status', 2);
				this.store.getById(file.id).commit();
				this.progress_bar.reset();
				this.progress_bar.updateText('Progress Bar');
				
				if (this.remove_completed) {
					this.store.remove(this.store.getById(file.id));
				}
				
				this.fireEvent('fileUploadComplete', this, file, o);
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