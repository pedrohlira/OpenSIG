Extension Page : http://www.silverbiology.com/ext_samples/SwfUploadPanel/uploaddemo.php
Author : Michael Giddens
v 0.4 (March 6th 2008)
License : MIT

There were some changes to the original code.  
The original files are renamed .org so that a comparison can be made.
Here is the list of changes:
1.  Event handlers were mixed up.  I've replied to the forum on this but don't
    know when the author will fix.  I've added the fixes in the code.
2.  Added a "Note" column so to display errors when upload fails.  This follows 
    the UploadDialog so that the same server code can be used.
3.  Return value "success" was not analyzed by the code from the return data 
    from the server.  Success=false will set the status to Error.
4.  Fixed delete key to delete entries.  Also, restricting it to just the
    Delete key and not all the other keys.