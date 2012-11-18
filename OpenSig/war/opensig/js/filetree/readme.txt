Extension Page : http://filetree.extjs.eu/
Author : Jozef Sakalos, aka Saki
Version: Beta 1
License : ?

There were some changes to the original code.  
The original files are renamed .org so that a comparison can be made.
Here is the list of changes:
1.  Explicitly send 'cmd:get' whenever a directory is loaded.  For some reason 
    it was not sending. The author cannot replicate and I only can do it with 
    gwt-ext.  So, for now, I've just updated the .js code to include the 
    'cmd:get' again.
2.  Call a callback method to calculate the path when download is needed or 
    open is perform.  Usually, the file is not at the same root level as
    displayed in the tree; so, there is a way to overwrite this path as 
    a callback.
3.  ext image directory in .css are one level above the one from the css files 
    for the Showcase. So, I've added another "../" to go one more level up to 
    find the ext/resource/images.
    