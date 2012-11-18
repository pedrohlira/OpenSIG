// Ext.ux.HTMLEditorStyles
// a plugin that add the styles list to the Ext.ux.HtmlEditor toolbar
Ext.ux.HTMLEditorStyles = function(s) {

  // PRIVATE

  // pointer to Ext.ux.HTMLEditor
  var editor;
  
  // styles list
  var styles = s;

  // ensure styles is an array
  if (! styles instanceof Array) {
    styles = [styles];
  }

  // include the No Style option
  styles = [{text: "No Style", value: "none"}].concat(styles);

  // update the styles combobox (runs when editorevent occurs in the editor)
  var updateCombo = function() {
    var element = Ext.isIE ? editor.doc.selection.createRange().parentElement() :
      editor.win.getSelection().anchorNode;
    var parent = getParentStyleElement(element);
    var style = parent ? parent.className : "none";
    if (editor.tb.items.map.style.el.value != style) {
      editor.tb.items.map.style.el.value = style;
    }
  }
  
  // this function gets the parent style property of an element.
  // it searches the parent hierarchy until it finds one
  var getParentStyleElement = function(element) {
    if (element) {
      if (element.nodeType == 1 && element.tagName.toLowerCase() == "span" &&
          element.className != "") {
        return element;
      }
      else {
        return getParentStyleElement(element.parentNode);
      }
    }
  }
  
  // perform the style request on the selected text.
  // this function is for IE only
  var doStyleIE = function(event, el) {

    // remove a style (performed if No Style is selected).
    // if the className is the only attribute of the element
    // then the entire element is removed
    var removeStyle = function(element) {
      element.removeAttribute('className');
      var clone = element.cloneNode(false);
      if (clone.outerHTML.toLowerCase() == "<span></span>") {
        element.insertAdjacentHTML('beforeBegin', element.innerHTML);
        element.parentNode.removeChild(element);
      }
    }

    // remove the styles of any children (within selected text).
    // searches entire child hierarchy so that selected text
    // now has only one style element
    var removeChildStyle = function(element) {
      for (var i = 0; i < element.children.length; i++) {
        var child = element.children[i];
        if (child.nodeType == 1) {
          removeChildStyle(child);
          if (child.tagName.toLowerCase() == "span") {
            removeStyle(child);
          }
        }
      }
    }

    // get selected text/range
    var selection = editor.doc.selection;
    var range = selection.createRange();

    // get the chosen style from the style combobox
    var style = el.value;

    // if No Style is chosen then remove the style from the parent
    if (style == "none") {
      var element = range.parentElement();
      var parent = getParentStyleElement(element);
      removeStyle(parent);
    }

    // else apply the style to the selected text
    else {
      if (selection.type == "Text") {
        var element = document.createElement("span");
        element.innerHTML = range.htmlText;
        element.className = style;
        removeChildStyle(element);
        range.pasteHTML(element.outerHTML);
      }
    }
    
    // perform required toolbar operations
    editor.updateToolbar();
    editor.deferFocus();
  }
  
  // perform the style request on the selected text.
  // this function is for Gecko-compliant browsers (not IE or Safari)
  var doStyleGecko = function(event, el) {

    // remove a style (performed if No Style is selected).
    // if the className is the only attribute of the element
    // then the entire element is removed
    var removeStyle = function(element) {
      element.removeAttribute('class');
      var wrapper = document.createElement("span");
      wrapper.appendChild(element.cloneNode(false));
      if (wrapper.innerHTML.toLowerCase() == "<span></span>") {
        var fragment = document.createDocumentFragment() ;
        for (var i = 0; i < element.childNodes.length; i++) {
           fragment.appendChild(element.childNodes[i].cloneNode(true));
         }
        element.parentNode.replaceChild(fragment, element);
      }
    }

    // remove the styles of any children (within selected text).
    // searches entire child hierarchy so that selected text
    // now has only one style element
    var removeChildStyle = function(element) {
      for (var i = 0; i < element.childNodes.length; i++) {
        var child = element.childNodes[i];
        if (child.nodeType == 1) {
          removeChildStyle(child);
          if (child.tagName.toLowerCase() == "span") {
            removeStyle(child);
          }
        }
      }
    }

    // get selected text
    var selection = editor.win.getSelection();

    // get the chosen style from the style combobox
    var style = el.value;

    // if No Style is chosen then remove the style from the parent
    if (style == "none") {
      var element = selection.anchorNode;
      var parent = getParentStyleElement(element);
      removeStyle(parent);
    }

    // else apply the style to the selected text
    else {
      if (! selection.isCollapsed) {
        var element = document.createElement("span");
        for (var i = 0; i < selection.rangeCount; i++) {
          element.appendChild(selection.getRangeAt(i).extractContents());
        }
        element.className = style;
        removeChildStyle(element);
        selection.getRangeAt(0).insertNode(element);
      }
    }

    // perform required toolbar operations
    editor.updateToolbar();
    editor.deferFocus();
  }
  
  // PUBLIC

  return {

    // Ext.ux.HTMLEditorStyles.init
    // called upon instantiation
    init: function(htmlEditor) {
      editor = htmlEditor;

      // doesn't work for safari so exclude
      if (! Ext.isSafari) {
  
        // add the styles combo and spacer to the toolbar.
        // insert before the fontname combo
        editor.tb.insertToolsBefore('fontname', [{
          itemId: 'style',
          xtype: 'tbcombo',
          cls: 'x-font-select',
          opts: styles,
          handler: Ext.isIE ? doStyleIE : doStyleGecko,
          scope: this
        }, ' ']);
  
        // add listener to editorevent when editor is rendered
        editor.on('render', function() {

          // if editorevent occurs then update the styles combobox
          editor.on("editorevent", updateCombo, this);
        }, this);
      }
    }
  }
}
