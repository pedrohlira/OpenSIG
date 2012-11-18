<?
	session_start();
	$_SESSION['test'] = 'cool. same session.';
?>
<!--DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/loose.dtd"-->
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<title>Ext.ux.SwfUploadPanel Example</title>
<link rel="stylesheet" type="text/css" href="../../ext/resources/css/ext-all.css" />
<script type="text/javascript" src="../../ext/ext-base.js"></script>
<script type="text/javascript" src="../../ext/ext-all.js"></script>
<!-- Files needed for SwfUploaderPanel -->
<link rel="stylesheet" type="text/css" href="../../ext/plugins/SwfUploadPanel/SwfUploadPanel.css" />
<script type="text/javascript" src="../../ext/plugins/SwfUploadPanel/SwfUpload.js"></script>
<script type="text/javascript" src="../../ext/plugins/SwfUploadPanel/SwfUploadPanel.js"></script>
<script>	
		Ext.onReady(function() {		

			String.prototype.trim = function() {
				return this.replace(/^\s+|\s+$/g,"");
			}
		
			// Get SessionId from cookie
			var PHPSESSIDX = null;
			var cookies = document.cookie.split(";");
			Ext.each(cookies, function(cookie) {
				var nvp = cookie.split("=");
				if (nvp[0].trim() == 'PHPSESSID')
					PHPSESSIDX = nvp[1];
			});
			
			var uploader = new Ext.ux.SwfUploadPanel({
					title: 'Upload Test'
				, renderTo: 'grid'
				, width: 500
				, height: 300

				// Uploader Params				
				, upload_url: 'http://www.silverbiology.com/ext/plugins/SwfUploadPanel/upload_example.php'
//				, upload_url: 'http://localhost/www.silverbiology.com/ext/plugins/SwfUploadPanel/upload_example.php'
				, post_params: { PHPSESSIDX: PHPSESSIDX }
<?
		if (isset($_REQUEST[debug])) print ", debug: true";
?>				
				, flash_url: "../../ext/plugins/SwfUploadPanel/swfupload_f9.swf"
//				, single_select: true // Select only one file from the FileDialog

				// Custom Params
				, single_file_select: false // Set to true if you only want to select one file from the FileDialog.
				, confirm_delete: false // This will prompt for removing files from queue.
				, remove_completed: false // Remove file from grid after uploaded.
			});

			uploader.on('swfUploadLoaded', function() { 
				this.addPostParam( 'Post1', 'example1' );
			});
			
			uploader.on('fileUploadComplete', function(panel, file, response) {
			});
			
			uploader.on('queueUploadComplete', function() {
				if ( Ext.isGecko ) {
					console.log("Files Finished");
				} else {
					alert("Files Finished");
				}
			});
						
			
			var btn = new Ext.Button({
					text: 'Launch Sample Uploader'
				, renderTo: 'btn'
				, handler: function() {

						var dlg = new Ext.ux.SwfUploadPanel({
								title: 'Dialog Sample'
							, width: 500
							, height: 300
							, border: false
			
							// Uploader Params				
							, upload_url: 'http://www.silverbiology.com/ext/plugins/SwfUploadPanel/upload_example.php'
//							, upload_url: 'http://localhost/www.silverbiology.com/ext/plugins/SwfUploadPanel/upload_example.php'
							, post_params: { id: 123}
							, file_types: '*.jpg'
							, file_types_description: 'Image Files'
			<?
					if (isset($_REQUEST[debug])) print ", debug: true";
			?>				
							, flash_url: "../../ext/plugins/SwfUploadPanel/swfupload_f9.swf"
			//				, single_select: true // Select only one file from the FileDialog
			
							// Custom Params
							, single_file_select: false // Set to true if you only want to select one file from the FileDialog.
							, confirm_delete: false // This will prompt for removing files from queue.
							, remove_completed: true // Remove file from grid after uploaded.
						}); // End Dialog

						var win = new Ext.Window({
								title: 'Window'
							, width: 514
							, height: 330
							, resizable: false
							, items: [ dlg ]
						}); // End Window
								
						win.show();
					
					} // End Btn Handler
								
				}); // end Btn
		});
	</script>
<style type="text/css">
<!--
.style1 {
	font-weight: bold
}
body, td, th {
	font-family: Arial, Helvetica, sans-serif;
}
body {
	margin-left: 5px;
	margin-top: 5px;
	margin-right: 5px;
	margin-bottom: 5px;
}
.style3 {font-size: 10px; font-style: italic;}
-->
</style>
</head>
<body>
<pre><span class="style1"><strong></strong>ExtJS Extension</span>
LastModified: March 6th 2008
<br>The SwfUploadPanel widget is a control, where the user can pick mulitple files and upload them in a queue style fasion.

<br>Current Version: 0.5<br>Uses <a href="http://swfupload.org/" target="_blank">SwfUpload v2.0.2</a><br><br>
Released for ExtJS 2.x.

Demo: See Below
<br>Usage
    * Download ExtJS 2.x library
    * Download <a href="../../ext/plugins/SwfUploadPanel/Ext.ux.SwfUploadPanel.js.zip">Ext.ux.SwfUploadPanel</a> library
    * Unpack ExtJS library to a folder
    * Unpack Ext.ux.SwfUploadPanel.js.zip to s plugin folder for example and follow example!
    * I will try and document more when I have time.
     
<br>View Forum Posts

    * <a href="http://extjs.com/forum/showthread.php?t=19082" target="_blank">http://extjs.com/forum/showthread.php?t=19082</a> 

<br>Changelog
  * Ver.: 0.4 (beta) Migrated to SwfUpload 2.0.2
  * Ver.: 0.3 (beta) Fixed the progress bar to be dynamic 100
        - Fixed the postParam to work correctly now just past the (name, value)
        - added a few events so you can bind some listeners ie.  swfUploadLoaded, fileUploadComplete, queueUploadComplete
  * Ver.: 0.2 (beta) Added Stop Upload, Remove Files from Queue
  * Ver.: 0.1 (beta) Basic MultiFile Upload

FAQ:
	Q: I set everything up and the file select dialog does not open when I click Add Files?
	A: Double check to make sure the Flash swf path is correct. (In FireBug you will see: &quot;Could not find Flash element&quot; when the path is not right.)

	Q: The Dialog opens and the files show up but when I click upload it hangs up and nothing happens.
	A: The upload_url needs to be correct.  From my testing it only works with absolute paths. So make sure you have the complete url.

	Q: SWFUpload is a Flash object and uses a different session from by browser window. How do I force the same.
	A: Look at the code for the first example and see how the session is sent from the cookie session. Thanks MD!
		(Note: This may not be the safest approach from session hijacking but the only solution I have at the moment.)

PHP code to swap out with the browser session:
</pre>
<p class="style3"> if ( (!empty($_REQUEST[&quot;PHPSESSID&quot;]) &amp;&amp; !empty($_REQUEST[&quot;PHPSESSIDX&quot;])) <br>
  &amp;&amp; $_REQUEST[&quot;PHPSESSID&quot;] != $_REQUEST[&quot;PHPSESSIDX&quot;] ) { <br>
  <br>
  $_REQUEST[&quot;PHPSESSID&quot;] = $_REQUEST[&quot;PHPSESSIDX&quot;]; <br>
  unset($_REQUEST[&quot;PHPSESSIDX&quot;]); <br>
  $_COOKIE[&quot;PHPSESSID&quot;] = $_REQUEST[&quot;PHPSESSID&quot;]; <br>
  } </p>
<p class="style3"> session_start();</p>
<pre>
<br><br>
</pre>
<div id="grid"></div>
<br>
<div id="btn"></div>
<script src="http://www.google-analytics.com/urchin.js" type="text/javascript"></script>
<script type="text/javascript">_uacct = "UA-2844304-1"; urchinTracker();</script>
</body>
</html>
