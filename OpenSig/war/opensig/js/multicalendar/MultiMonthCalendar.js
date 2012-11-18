/*
 * Ext JS Library 2.0 RC 1
 * Copyright(c) 2006-2007, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */

/**
 * @class Ext.MultiMonthCalendar
 * @extends Ext.Component
 * Multi-month Calendar
 * @constructor
  * @param {Object} config The config object
 */
Ext.ux.MultiMonthCalendar = Ext.extend(Ext.Component, {
    /**
     * @cfg {Date} minDate
     * Minimum allowable date (JavaScript date object, defaults to null)
     */
    minDate : null,
    /**
     * @cfg {Date} maxDate
     * Maximum allowable date (JavaScript date object, defaults to null)
     */
    maxDate : null,
    /**
     * @cfg {String} minText
     * The error text to display if the minDate validation fails (defaults to "This date is before the minimum date")
     */
    minText : "This date is before the minimum date",
    /**
     * @cfg {String} maxText
     * The error text to display if the maxDate validation fails (defaults to "This date is after the maximum date")
     */
    maxText : "This date is after the maximum date",
    /**
     * @cfg {String} format
     * The default date format string which can be overriden for localization support.  The format must be
     * valid according to {@link Date#parseDate} (defaults to 'm/d/y').
     */
    format : "m/d/y",
    /**
     * @cfg {Array} disabledDays
     * An array of days to disable, 0-based. For example, [0, 6] disables Sunday and Saturday (defaults to null).
     */
    disabledDays : null,
    /**
     * @cfg {String} disabledDaysText
     * The tooltip to display when the date falls on a disabled day (defaults to "")
     */
    disabledDaysText : "",
    /**
     * @cfg {RegExp} disabledDatesRE
     * JavaScript regular expression used to disable a pattern of dates (defaults to null)
     */
    disabledDatesRE : null,
    /**
     * @cfg {String} disabledDatesText
     * The tooltip text to display when the date falls on a disabled date (defaults to "")
     */
    disabledDatesText : "",
    /**
     * @cfg {Boolean} constrainToViewport
     * True to constrain the date picker to the viewport (defaults to true)
     */
    constrainToViewport : true,
    /**
     * @cfg {Array} monthNames
     * An array of textual month names which can be overriden for localization support (defaults to Date.monthNames)
     */
    monthNames : Date.monthNames,
    /**
     * @cfg {Array} dayNames
     * An array of textual day names which can be overriden for localization support (defaults to Date.dayNames)
     */
    dayNames : Date.dayNames,
    /**
     * @cfg {String} nextText
     * The next month navigation button tooltip (defaults to 'Next Month (Control+Right)')
     */
    nextText: 'Next Month (Control+Right)',
    /**
     * @cfg {String} prevText
     * The previous month navigation button tooltip (defaults to 'Previous Month (Control+Left)')
     */
    prevText: 'Previous Month (Control+Left)',    
    /**
     * @cfg {Number} startDay
     * Day index at which the week should begin, 0-based (defaults to 0, which is Sunday)
     */
    startDay : 0,
    /**
    * @cfg {Number} noOfMonth
    * No of Month to be displayed
    */
    noOfMonth : 2,
/**
    * @cfg {Array} eventDates
    * List of Dates which have an event (show as selected in UI)
    */    
    eventDates : null,
    
    /**
    * @cfg {Array} noOfMonthPerRow
    * No. Of Month to be displayed in a row
    */    
    noOfMonthPerRow : 3,
    
    initComponent : function(){
        Ext.ux.MultiMonthCalendar.superclass.initComponent.call(this);
        this.value = this.value ?
                 this.value.clearTime() : new Date().clearTime();
        this.initDisabledDays();   
        this.noOfMonthPerRow = this.noOfMonthPerRow > this.noOfMonth ?this.noOfMonth : this.noOfMonthPerRow    
    }, 
    
    // private
    initDisabledDays : function(){
        if(!this.disabledDatesRE && this.disabledDates){
            var dd = this.disabledDates;
            var re = "(?:";
            for(var i = 0; i < dd.length; i++){
                re += dd[i];
                if(i != dd.length-1) re += "|";
            }
            this.disabledDatesRE = new RegExp(re + ")");
        }
    },
        
    /**
     * Sets the value of the date field
     * @param {Date} value The date to set
     */

    setValue : function(value){
        var old = this.value;
        this.value = value.clearTime(true);
        if(this.el){
            this.update(this.value);
        }
    },

    /**
     * Gets the current selected value of the date field
     * @return {Date} The selected date
     */
    getValue : function(){
        return this.value;
    },


    // private
    focus : function(){
        if(this.el){
            this.update(this.activeDate);
        }
    },

    // private
	onRender : function(container, position){
        var m = ["<table cellspacing='0'>"];
        if(this.noOfMonthPerRow > 1) {
            m.push("<tr><td class='x-date-left'><a href='#' title='", this.prevText ,"'> </a></td>");
               m.push("<td class='x-date-left' colspan='"+ eval(this.noOfMonthPerRow *2 -3) +"'></td>");
               m.push("<td class='x-date-right'><a href='#' title='", this.nextText ,"'> </a></td></tr><tr>");
       } else {
               //Special case of only one month
               m.push("<tr><td><table cellspacing='0' width='100%'><tr><td class='x-date-left'><a href='#' title='", this.prevText ,"'> </a></td>");
               m.push("<td class='x-date-right'><a href='#' title='", this.nextText ,"'> </a></td></tr></table></td></tr><tr>");
       }       
        for(var x=0; x<this.noOfMonth; x++) {            
            m.push("<td><table border='1' cellspacing='0'><tr>");
            m.push("<td class='x-date-middle' align='center'><span id='monthLabel" + x + "'></span></td>");
            m.push("</tr><tr><td><table class='x-date-inner' id='inner-date"+x+"' cellspacing='0'><thead><tr>");        
            var dn = this.dayNames;
            for(var i = 0; i < 7; i++){
               var d = this.startDay+i;
               if(d > 6){
                   d = d-7;
               }
                m.push("<th><span>", dn[d].substr(0,1), "</span></th>");
            }
            m[m.length] = "</tr></thead><tbody><tr>";
            for(var i = 0; i < 42; i++) {
                if(i % 7 == 0 && i != 0){
                    m[m.length] = "</tr><tr>";
                }
                m[m.length] = '<td><a href="#" hidefocus="on" class="x-date-date" tabIndex="1"><em><span></span></em></a></td>';
            }
            m[m.length] = '</tr></tbody></table></td></tr></table></td>';
            if(x != this.noOfMonth-1) {
                m[m.length] = "<td width='3'></td>";
            }
            if( (x+1) % this.noOfMonthPerRow == 0 && x!= 0) {
                m[m.length] = "</tr><tr>"; 
            }            
        }
        m[m.length] = "</tr></table>";            
        var el = document.createElement("div");
        el.className = "x-date-picker";
        el.innerHTML = m.join("");  

        container.dom.insertBefore(el, position);

        this.el = Ext.get(el);        
        this.eventEl = Ext.get(el.firstChild);

        new Ext.util.ClickRepeater(this.el.child("td.x-date-left a"), {
            handler: this.showPrevMonth,
            scope: this,
            preventDefault:true,
            stopDefault:true
        });

        new Ext.util.ClickRepeater(this.el.child("td.x-date-right a"), {
            handler: this.showNextMonth,
            scope: this,
            preventDefault:true,
            stopDefault:true
        });

        var kn = new Ext.KeyNav(this.eventEl, {
            "left" : function(e){
                e.ctrlKey ?
                    this.showPrevMonth() :
                    this.update(this.activeDate.add("d", -1));
            },

            "right" : function(e){
                e.ctrlKey ?
                    this.showNextMonth() :
                    this.update(this.activeDate.add("d", 1));
            },

            "pageUp" : function(e){
                this.showNextMonth();
            },

            "pageDown" : function(e){
                this.showPrevMonth();
            },

            "enter" : function(e){
                e.stopPropagation();
                return true;
            }, 
            scope : this 
        });
        
        this.cellsArray = new Array();
        this.textNodesArray = new Array(); 
        for(var x=0; x< this.noOfMonth; x++) {
            var cells = Ext.get('inner-date'+x).select("tbody td");
            var textNodes = Ext.get('inner-date'+x).query("tbody span");
            this.cellsArray[x] = cells;
            this.textNodesArray[x] = textNodes;
        }

        if(Ext.isIE){
            this.el.repaint();
        }
        this.update(this.value);
    },

    // private
    showPrevMonth : function(e){
        this.update(this.activeDate.add("mo", -1));
    },

    // private
    showNextMonth : function(e){
        this.update(this.activeDate.add("mo", 1));
    },

    // private
    update : function(date){
        this.activeDate = date;
        for(var x=0;x<this.noOfMonth;x++) {
            var days = date.getDaysInMonth();
            var firstOfMonth = date.getFirstDateOfMonth();
            var startingPos = firstOfMonth.getDay()-this.startDay;
    
            if(startingPos <= this.startDay){
                startingPos += 7;
            }
    
            var pm = date.add("mo", -1);
            var prevStart = pm.getDaysInMonth()-startingPos;
    
            var cells = this.cellsArray[x].elements;
            var textEls = this.textNodesArray[x];
            days += startingPos;
    
            // convert everything to numbers so it's fast
            var day = 86400000;
            var d = (new Date(pm.getFullYear(), pm.getMonth(), prevStart)).clearTime();
            var min = this.minDate ? this.minDate.clearTime() : Number.NEGATIVE_INFINITY;
            var max = this.maxDate ? this.maxDate.clearTime() : Number.POSITIVE_INFINITY;
            var ddMatch = this.disabledDatesRE;
            var ddText = this.disabledDatesText;
            var ddays = this.disabledDays ? this.disabledDays.join("") : false;
            var ddaysText = this.disabledDaysText;
            var format = this.format;
    
            var setCellClass = function(cal, cell){
                cell.title = "";
                var t = d.getTime();
                cell.firstChild.dateValue = t;

                // disabling
                if(t < min) {
                    cell.className = " x-date-disabled";
                    cell.title = cal.minText;
                    return;
                }
                if(t > max) {
                    cell.className = " x-date-disabled";
                    cell.title = cal.maxText;
                    return;
                }
                if(ddays){
                    if(ddays.indexOf(d.getDay()) != -1){
                        cell.title = ddaysText;
                        cell.className = " x-date-disabled";
                    }
                }
                if(ddMatch && format){
                    var fvalue = d.dateFormat(format);
                    if(ddMatch.test(fvalue)){
                        cell.title = ddText.replace("%0", fvalue);
                        cell.className = " x-date-disabled";
                    }
                }
                //Only active days need to be selected
                if(cal.eventDates && (cell.className.indexOf('x-date-active') != -1)) {
                    for(var y=0; y < cal.eventDates.length; y++) {
                        var evtDate = cal.eventDates[y].clearTime().getTime();
                        if(t == evtDate) {
                            cell.className += " x-date-selected";
                            break;
                        }
                    }
                }
            };
    
            var i = 0;
            for(; i < startingPos; i++) {
                textEls[i].innerHTML = (++prevStart);
                d.setDate(d.getDate()+1);
                cells[i].className = "x-date-prevday";
                setCellClass(this, cells[i]);
            }
            for(; i < days; i++){
                intDay = i - startingPos + 1;
                textEls[i].innerHTML = (intDay);
                d.setDate(d.getDate()+1);
                cells[i].className = "x-date-active";
                setCellClass(this, cells[i]);
            }
            var extraDays = 0;
            for(; i < 42; i++) {
                 textEls[i].innerHTML = (++extraDays);
                 d.setDate(d.getDate()+1);
                 cells[i].className = "x-date-nextday";
                 setCellClass(this, cells[i]);
            }
            var monthLabel = Ext.get('monthLabel' + x);                    
            monthLabel.update(this.monthNames[date.getMonth()] + " " + date.getFullYear());

            if(!this.internalRender){
                var main = this.el.dom.firstChild;
                var w = main.offsetWidth;
                this.el.setWidth(w + this.el.getBorderWidth("lr"));
                Ext.fly(main).setWidth(w);
                this.internalRender = true;
                // opera does not respect the auto grow header center column
                // then, after it gets a width opera refuses to recalculate
                // without a second pass
                if(Ext.isOpera && !this.secondPass){
                    main.rows[0].cells[1].style.width = (w - (main.rows[0].cells[0].offsetWidth+main.rows[0].cells[2].offsetWidth)) + "px";
                    this.secondPass = true;
                    this.update.defer(10, this, [date]);
                }
            }
            date = date.add('mo',1);
        }    
    }
});
Ext.reg('mmcalendar', Ext.ux.MultiMonthCalendar);  

