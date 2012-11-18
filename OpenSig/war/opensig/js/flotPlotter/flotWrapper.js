/*
 Copyright 2008 - Pantaleo de Pinto (depinto.pantaleo@gmail.com)

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions
are met:

1. Redistributions of source code must retain the above copyright
   notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright
   notice, this list of conditions and the following disclaimer in the
   documentation and/or other materials provided with the distribution.

3. The name of the author may not be used to endorse or promote products
   derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

function drawOverviewWithNoOptions(datasets, contOverview){
     var overview = $.plot($(contOverview), datasets, {
        //lines: { show: false },
        //points: { show: true },
        //shadowSize: 0,
        xaxis: { ticks: 4 },
        yaxis: { ticks: 4},
        grid: { color: "#999" },
        selection: { mode: "xy" }
    });
    
    return overview;
};

function drawOverviewWithOptions(datasets, overOptions, contOverview){
     var overview = $.plot($(contOverview), datasets, overOptions);
    
     return overview;
};

function bindPlotWithOverview(options, datasets, plot, overview, container, contOverview){
         var internalSelection = false;
    
    $(container).bind("selected", function (event, area) {
        // clamp the zooming to prevent eternal zoom
        if (area.x2 - area.x1 < 0.00001)
            area.x2 = area.x1 + 0.00001;
        if (area.y2 - area.y1 < 0.00001)
            area.y2 = area.y1 + 0.00001;
        
        // do the zooming
        plot = $.plot($(container), datasets,
                      $.extend(true, {}, options, {
                          xaxis: { min: area.x1, max: area.x2 },
                          yaxis: { min: area.y1, max: area.y2 }
                      }));
        
        if (internalSelection)
            return; // prevent eternal loop
        internalSelection = true;
        overview.setSelection(area);
        internalSelection = false;
    });
    
     $(contOverview).bind("selected", function (event, area) {
        if (internalSelection)
            return;
        internalSelection = true;
        plot.setSelection(area);
        internalSelection = false;
    });
}

function bindPlotWithOverviewNoOptions(datasets, plot, overview, container, contOverview){
         var internalSelection = false;
    
    $(container).bind("selected", function (event, area) {
        // clamp the zooming to prevent eternal zoom
        if (area.x2 - area.x1 < 0.00001)
            area.x2 = area.x1 + 0.00001;
        if (area.y2 - area.y1 < 0.00001)
            area.y2 = area.y1 + 0.00001;
        
        // do the zooming
        plot = $.plot($(container), datasets,
                      $.extend(true, {}, {
                          xaxis: { min: area.x1, max: area.x2 },
                          yaxis: { min: area.y1, max: area.y2 }
                      }));
        
        if (internalSelection)
            return; // prevent eternal loop
        internalSelection = true;
        overview.setSelection(area);
        internalSelection = false;
    });
    
     $(contOverview).bind("selected", function (event, area) {
        if (internalSelection)
            return;
        internalSelection = true;
        plot.setSelection(area);
        internalSelection = false;
    });
}

function drawPlotWithNoOptions(datasets, container){
     var plot = $.plot($(container), datasets);
     return plot;
};

function drawPlotWithOptions(datasets, options, container){
     var plot = $.plot($(container), datasets, options);
     return plot;
};

function setAttributeAsJQueryObject(obj, attr, container){
     obj[attr] = $(container);
};

function reset(plot, datasets, options, overview, container){
    plot = $.plot($(container), datasets,
                      $.extend(true, {}, options));
    if (overview != null){
      overview.clearSelection();
    }
                      
    return plot;
};