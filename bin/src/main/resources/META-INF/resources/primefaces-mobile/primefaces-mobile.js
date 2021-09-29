PrimeFaces.ajax.AjaxUtils.updateElement = function(id, content) {        
    if(id == PrimeFaces.VIEW_STATE) {
        PrimeFaces.ajax.AjaxUtils.updateState.call(this, content);
    }
    else if(id == PrimeFaces.VIEW_ROOT) {
        document.open();
        document.write(content);
        document.close();
    }
    else {
        if ($.mobile) {
            var context = $(PrimeFaces.escapeClientId(id)).parent(),
                controls = context.find(":input, button, a[data-role='button'], ul");
                                            
            //selects
            controls.filter("select:not([data-role='slider'])").selectmenu().selectmenu("destroy");             
        }
        
        $(PrimeFaces.escapeClientId(id)).replaceWith(content);

        //PrimeFaces Mobile
        if($.mobile) {
            context = $(PrimeFaces.escapeClientId(id)).parent(),
            controls = context.find(":input, button, a[data-role='button'], ul, table");

            //input text and textarea
            var inputs = controls.filter("[type='text'],[type='tel'],[type='range'],[type='number'],[type='email'],[type='password'],[type='date'],[type='datetime'],[type='time'],[type='url'],[type='password'],[type='file'],textarea").textinput();            
            if (inputs.parent().parent().hasClass("ui-input-text")){
                inputs.unwrap(); //prevent duplicate input
            }
            
            //radio-checkbox
            controls.filter("[type='radio'], [type='checkbox']").checkboxradio();
            
            //selects
            controls.filter("select:not([data-role='slider'])" ).selectmenu();
            
            //slider
            controls.filter(":jqmData(type='range')").slider();
            
            //switch
            controls.filter("select[data-role='slider']" ).slider();
            
            //lists                        
            var lists = controls.filter("ul[data-role='listview']").listview();     
            lists.prev("form.ui-listview-filter").prev("form.ui-listview-filter").remove();  //prevent duplicate filter                      
                                    
            //buttons
            controls.filter("button, [type='button'], [type='submit'], [type='reset'], [type='image']").button();
            controls.filter("a").buttonMarkup();
            
            //table                   
            var tables = controls.filter("table[data-role='table']");
            tables.table().table("refresh");
            tables.prev().prev(".ui-table-columntoggle-btn").prev().prev(".ui-table-columntoggle-btn").remove(); //prevent duplicate button
                        
            //field container
            context.find(":jqmData(role='fieldcontain')").fieldcontain();
            
            //control groups
            context.find(":jqmData(role='controlgroup')").controlgroup();
            
            //panel
            context.find("div[data-role='collapsible']").collapsible();
            
            //accordion
            context.find("div[data-role='collapsibleset']").collapsibleset();
            
            //navbar
            context.find("div[data-role='navbar']").navbar();     
            
            //popup
            context.find("div[data-role='popup']").popup();            
        }
    }
}

PrimeFaces.navigate = function(to, cfg) {        
    //cast
    cfg.reverse = (cfg.reverse == 'true' || cfg.reverse == true) ? true : false;

    $.mobile.changePage(to, cfg);
}

PrimeFaces.back = function() {        
    $.mobile.back();
    return false;
}

/**
 * PrimeFaces InputText Widget
 */
PrimeFaces.widget.InputText = PrimeFaces.widget.BaseWidget.extend({
    
    init: function(cfg) {
        this._super(cfg);
        this.input = this.jq.is(':input') ? this.jq : this.jq.children(':input');
        
        //Client behaviors
        if(this.cfg.behaviors) {
            PrimeFaces.attachBehaviors(this.input, this.cfg.behaviors);
        }
    }
});

/**
 * PrimeFaces InputText Widget
 */
PrimeFaces.widget.InputTextarea = PrimeFaces.widget.BaseWidget.extend({
    
    init: function(cfg) {
        this._super(cfg);
        this.input = this.jq.is(':input') ? this.jq : this.jq.children(':input');
        
        this.cfg.rowsDefault = this.input.attr('rows');
        this.cfg.colsDefault = this.input.attr('cols');

        //max length
        if(this.cfg.maxlength){
            this.applyMaxlength();
        }

        //Client behaviors
        if(this.cfg.behaviors) {
            PrimeFaces.attachBehaviors(this.input, this.cfg.behaviors);
        }
    },    
    
    applyMaxlength: function() {
        var _self = this;

        this.input.keyup(function(e) {
            var value = _self.input.val(),
            length = value.length;

            if(length > _self.cfg.maxlength) {
                _self.input.val(value.substr(0, _self.cfg.maxlength));
            }
        });
    }
});

/**
 * PrimeFaces SelectBooleanCheckbox Widget
 */
PrimeFaces.widget.SelectBooleanCheckbox = PrimeFaces.widget.BaseWidget.extend({
    
    init: function(cfg) {
        this._super(cfg);
        
        this.input = $(this.jqId + '_input');

        if(this.cfg.behaviors) {
            PrimeFaces.attachBehaviors(this.input, this.cfg.behaviors);
        }
    }
});

/**
 * PrimeFaces SelectManyCheckbox Widget
 */
PrimeFaces.widget.SelectManyCheckbox = PrimeFaces.widget.BaseWidget.extend({
    
    init: function(cfg) {
        this._super(cfg);
        
        this.inputs = this.jq.find(':checkbox:not(:disabled)');
                        
        //Client Behaviors
        if(this.cfg.behaviors) {
            PrimeFaces.attachBehaviors(this.inputs, this.cfg.behaviors);
        }
    }
});

/**
 * PrimeFaces SelectOneRadio Widget
 */
PrimeFaces.widget.SelectOneRadio = PrimeFaces.widget.BaseWidget.extend({
    
    init: function(cfg) {
        this._super(cfg);

        this.inputs = this.jq.find(':radio:not(:disabled)');
                
        //Client Behaviors
        if(this.cfg.behaviors) {
            PrimeFaces.attachBehaviors(this.inputs, this.cfg.behaviors);
        }
    }
});

/**
 * PrimeFaces Dialog Widget
 */
PrimeFaces.widget.Dialog = PrimeFaces.widget.BaseWidget.extend({
    
    show: function() {
        var _self = this;
        //Wait other popup close
        var delay = 300;
        setTimeout(function() {_self.jq.popup('open')}, delay);
    },
    
    hide: function() {
        this.jq.popup('close');
    }
});

/**
 * PrimeFaces Growl Widget
 */
PrimeFaces.widget.Growl = PrimeFaces.widget.BaseWidget.extend({
    init: function(cfg) {
        this._super(cfg);
        cfg.y = $(document).height();
        cfg.delay = 300;
        
        this.show(this.cfg.msgs);
    },                     
            
    show: function(msgs) {   
        var _self = this;
        if (msgs.length !== 0){                
            
            //clear previous messages
            this.removeAll();
            
            $.each(msgs, function(index, msg) {
                _self.renderMessage(msg);
            });                       
        }
    },            
            
    renderMessage: function(msg) {        

        switch (msg.severity){
            case 'info' : msg.severity = 'info'; break;
            case 'warn' : msg.severity = 'alert'; break;
            case 'error' : msg.severity = 'delete'; break;
            case 'fatal' : msg.severity = 'minus'; break;
            default : msg.severity = 'delete';
        }

        var markup = '<p>';
        markup += '<span class="ui-icon ui-icon-' + msg.severity + '" style="float: left;margin-right: 5px;" />';
        markup += '<span>';        
        if (msg.summary) markup += '<b>'+ msg.summary +'</b>'; 
        if (msg.summary && msg.detail) markup += ' ';
        if (msg.summary) markup += msg.detail;         
        markup += '</span>';
        markup += '</p>';  
        
        var message = $(markup);
        
        this.bindEvents(message);        
        
        message.appendTo(this.jq);     
        
        this.openPopup();
        
    }, 
    
    openPopup: function() {
        var _self = this;
     
        setTimeout(function() {_self.jq.popup().popup('open', _self.cfg)},_self.cfg.delay);    
    },
                            
            
    removeAll: function() {
        this.jq.contents().remove();
    },
            
    bindEvents: function(message) {
        var sticky = this.cfg.sticky;

        //remove message on click of close icon
        this.jq.bind('popupafterclose', function(event, ui) {
            //clear timeout if removed manually            
            if(!sticky) {
                clearTimeout(message.data('timeout'));
            }
        });        
        
        //hide the message after given time if not sticky
        if(!sticky) {
            this.setRemovalTimeout(message);
        }
    },            
            
    setRemovalTimeout: function(message) {
        var _self = this;
        
        var timeout = setTimeout(function() {
            _self.jq.popup('close');
        }, this.cfg.life+this.cfg.delay);

        message.data('timeout', timeout);
    }            
});

/**
 * PrimeFaces Calendar Widget
 */
PrimeFaces.widget.Calendar = PrimeFaces.widget.BaseWidget.extend({
    init: function(cfg) {
        this._super(cfg);
        this.input = this.jq.is(':input') ? this.jq : this.jq.children(':input');

        this.cfg.theme = 'jqm';

        if (this.cfg.hasTime) {
            if (this.cfg.timeOnly) {
                this.jq.mobiscroll().time(this.cfg);
            } else {
                this.jq.mobiscroll().datetime(this.cfg);
            }
        } else {
            this.jq.mobiscroll().date(this.cfg);
        }

        if (this.cfg.defaultDate !== 'null') {
            this.jq.mobiscroll('setDate', $.scroller.parseDate(this.cfg.pattern, this.cfg.defaultDate), true);
        }
        
        //Client behaviors
        if(this.cfg.behaviors) {
            PrimeFaces.attachBehaviors(this.input, this.cfg.behaviors);
        }        
        
        //Select listener
        this.bindDateSelectListener();
    },
    show: function() {
        this.jq.mobiscroll('show');
    },
    
    bindDateSelectListener: function() {
        if (this.cfg.behaviors) {
            var dateSelectBehavior = this.cfg.behaviors['dateSelect'];

            if (dateSelectBehavior) {
                this.jq.bind('change', function(e) {
                    dateSelectBehavior.call(this);
                });
            }
        }
    }
});

/**
 * PrimeFaces AutoComplete Widget
 */
PrimeFaces.widget.AutoComplete = PrimeFaces.widget.BaseWidget.extend({
    init: function(cfg) {
        this._super(cfg);
        this.cfg.minLength = this.cfg.minLength != undefined ? this.cfg.minLength : 1;
        this.cfg.delay = this.cfg.delay != undefined ? this.cfg.delay : 1000;

        this.bindEvents();
    },
    bindEvents: function() {
        var $this = this;

        this.jq.bind('listviewbeforefilter', function(event, ui) {
            $this.beforefilter(event, ui);
        });

        this.jq.find('a').bind('click', function(event) {
            $this.invokeItemSelectBehavior(event, $(event.target).attr('item-value'));
        });

    },
    beforefilter: function(event, ui) {
        var _self = this;
        $input = $(ui.input);
        query = $input.val();

        if (query.length === 0 || query.length >= _self.cfg.minLength) {

            //Cancel the search request if user types within the timeout
            if (_self.timeout) {
                clearTimeout(_self.timeout);
            }

            _self.timeout = setTimeout(function() {
                _self.search(query);
            },
            _self.cfg.delay);
        }

    },
    search: function(query) {
        var options = {
            source: this.id,
            update: this.id,
            formId: this.cfg.formId,
            onsuccess: function(responseXML) {
                var xmlDoc = $(responseXML.documentElement),
                        updates = xmlDoc.find("update");
                for (var i = 0; i < updates.length; i++) {
                    var update = updates.eq(i),
                            id = update.attr('id'),
                            data = update.get(0).childNodes[0].nodeValue;

                    PrimeFaces.ajax.AjaxUtils.updateElement.call(this, id, data);
                    var context = $(PrimeFaces.escapeClientId(id));
                    context.find(":input").focus().val(query);

                }

                PrimeFaces.ajax.AjaxUtils.handleResponse.call(this, xmlDoc);

                return true;
            }
        };

        options.params = [
            {name: this.id + '_query', value: query}
        ];

        PrimeFaces.ajax.AjaxRequest(options);
    },
    invokeItemSelectBehavior: function(event, itemValue) {
        if (this.cfg.behaviors) {
            var itemSelectBehavior = this.cfg.behaviors['itemSelect'];

            if (itemSelectBehavior) {
                var ext = {
                    params: [
                        {name: this.id + '_itemSelect', value: itemValue}
                    ]
                };

                itemSelectBehavior.call(this, event, ext);
            }
        }
    }
});

/**
 * PrimeFaces OverlayPanel Widget
 */
PrimeFaces.widget.OverlayPanel = PrimeFaces.widget.BaseWidget.extend({
    init: function(cfg) {
        this._super(cfg);        

        this.targetId = PrimeFaces.escapeClientId(this.cfg.target);
        this.target = $(this.targetId);        
        //configuration      
        this.cfg.showEvent = this.cfg.showEvent||'click.ui-overlaypanel';        
        
        this.bindEvents();
    },
            
    bindEvents: function() {
        var _self = this;
        //show and hide events for target        
        var event = this.cfg.showEvent;

        $(document).off(event, this.targetId).on(event, this.targetId, this, function(e) {
            e.data.show();
        });

        this.jq.bind('panelopen', function(event, ui) {
            if (_self.cfg.onShow) {
                _self.cfg.onShow.call(_self);
            }
        });

        this.jq.bind('panelclose', function(event, ui) {
            if (_self.cfg.onHide) {
                _self.cfg.onHide.call(_self);
            }
        });

    },            
            
    show: function() {
        this.jq.panel('open');
    },
    
    hide: function() {
        this.jq.panel('close');
    },
            
    toggle: function() {
        this.jq.panel('toggle');
    }                      
});


/**
 * PrimeFaces DataList Widget
 */
PrimeFaces.widget.DataList = PrimeFaces.widget.BaseWidget.extend({
    init: function(cfg) {
        var _self = this;
        this._super(cfg);
        this.scrollOffset = this.cfg.scrollStep;

        if (_self.cfg.isPaginator) {
            var btn = $(PrimeFaces.escapeClientId(_self.id + '_btn'));

            btn.click(function() {
                _self.loadRows();                
            });
        }
    },
            
    loadRows: function() {
        var options = {
            source: this.id,
            process: this.id,
            update: this.id,
            formId: this.cfg.formId
        },
        _self = this;

        options.onsuccess = function(responseXML) {
            var xmlDoc = $(responseXML.documentElement),
                    updates = xmlDoc.find("update");

            for (var i = 0; i < updates.length; i++) {
                var update = updates.eq(i),
                        id = update.attr('id'),
                        content = update.get(0).childNodes[0].nodeValue;

                if (id == _self.id) {
                    var lastRow = $(_self.jqId + ' li:last');

                    //insert new rows
                    lastRow.after(content);
                    
                    _self.scrollOffset += _self.cfg.scrollStep;

                    //Disable scroll if there is no more data left
                    if(_self.scrollOffset >= _self.cfg.scrollLimit) {
                        var btn = $(PrimeFaces.escapeClientId(id + '_btn'));
                        btn.remove();
                    }                    

                    var context = $(PrimeFaces.escapeClientId(id)).parent();         
                    context.find("ul[data-role='listview']").listview("refresh");
                }
                else {
                    PrimeFaces.ajax.AjaxUtils.updateElement.call(this, id, content);
                }
            }

            PrimeFaces.ajax.AjaxUtils.handleResponse.call(this, xmlDoc);

            return true;
        };

        options.params = [
            {name: this.id + '_pagination', value: true},
            {name: this.id + '_first', value: this.scrollOffset},
            {name: this.id + '_rows', value: _self.cfg.scrollStep}
        ];

        PrimeFaces.ajax.AjaxRequest(options);

    }
});   


/**
 * PrimeFaces ContextMenu Widget
 */
PrimeFaces.widget.ContextMenu = PrimeFaces.widget.BaseWidget.extend({
    
    init: function(cfg) {
        this._super(cfg);
        
        var _self = this,
        documentTarget = (this.cfg.target === undefined); 

        //event
        this.cfg.event = this.cfg.event||'taphold';
        
        var viewId = this.jq.closest("div[data-role='page']").attr('id');

        //target
        this.jqTargetId = documentTarget ? PrimeFaces.escapeClientId(viewId) : PrimeFaces.escapeClientId(this.cfg.target);
        this.jqTarget = $(this.jqTargetId);

        //attach contextmenu        
        if (documentTarget) {
            $(document).off(_self.cfg.event, this.jqTargetId).on(_self.cfg.event, this.jqTargetId, null, function() {
                _self.show();
            });
        } else {
            if (this.cfg.type === 'DataList') {
                this.bindDataList();
            }
        }
                
        //close menu when link is clicked
        this.jq.find('li a').bind('click', function(event) {            
            _self.hide();
        });        
        
    },      
            
    bindDataList: function() {
        var _self = this;

        //target
        var selector = PrimeFaces.escapeClientId(this.cfg.target) + ' li.ui-li:not(.ui-li-divider)';

        $(document).off(_self.cfg.event, selector).on(_self.cfg.event, selector, null, function() {
            var linkSelection = $(this).find('a.selection');

            var options = {
                source: linkSelection.attr('id'),
                process: linkSelection.attr('id'),                               
                oncomplete: function(xhr, status, args) {
                    _self.show();
                }
            };

            PrimeFaces.ajax.AjaxRequest(options);
        });
            
    },            
            
    show: function() {  

        if(this.cfg.beforeShow) {
            this.cfg.beforeShow.call(this);
        }                
        
        if (this.cfg.hasContent) {
            this.jq.popup('open');
        }
    },
            
    hide: function() {
        this.jq.popup('close');
    }              

}); 

/**
 * PrimeFaces Accordion Panel Widget
 */
PrimeFaces.widget.AccordionPanel = PrimeFaces.widget.BaseWidget.extend({
    init: function(cfg) {
        this._super(cfg);        
        
        this.bindEvents();
    },
            
    bindEvents: function() {
        var $this = this;

        var tabs = $this.jq.find("div[data-role='collapsible'] > h3");
        tabs.bind('click', function() {
            var selectedTab = $(PrimeFaces.escapeClientId(this.parentElement.id));
            if (selectedTab.hasClass('ui-collapsible-collapsed')) {
                
                if ($this.cfg.onTabChange) {
                    var result = $this.cfg.onTabChange.call($this, panel);
                    if (result === false)
                        return false;
                }                
                
                if ($this.hasBehavior('tabChange')) {
                    $this.fireTabChangeEvent(selectedTab);
                }
            } else {
                if ($this.hasBehavior('tabClose')) {
                    $this.fireTabCloseEvent(selectedTab);
                }                
            }
        });

    },
            
    fireTabChangeEvent : function(panel) {
        var tabChangeBehavior = this.cfg.behaviors['tabChange'],
        ext = {
            params: [
                {name: this.id + '_newTab', value: panel.attr('id')},
                {name: this.id + '_tabindex', value: parseInt(panel.index())}
            ]
        };
        
        tabChangeBehavior.call(this, null, ext);
    },

    fireTabCloseEvent : function(panel) {
        var tabCloseBehavior = this.cfg.behaviors['tabClose'],
        ext = {
            params: [
                {name: this.id + '_tabId', value: panel.attr('id')},
                {name: this.id + '_tabindex', value: parseInt(panel.index())}
            ]
        };
        
        tabCloseBehavior.call(this, null, ext);
    },
            
    hasBehavior: function(event) {
        if(this.cfg.behaviors) {
            return this.cfg.behaviors[event] != undefined;
        }

        return false;
    }            
                        
});

/**
 * PrimeFaces ConfirmDialog Widget
 */
PrimeFaces.widget.ConfirmDialog = PrimeFaces.widget.Dialog.extend({

    init: function(cfg) {
        
        this._super(cfg);
        
        this.content = this.jq.children('.ui-dialog-content');
        this.titlebar = this.jq.children('.ui-dialog-titlebar');    
        
        this.title = this.titlebar.children('.ui-dialog-title');
        this.message = this.content.children('.ui-confirm-dialog-message');
        this.icon = this.content.children('.ui-confirm-dialog-severity');        

        if(this.cfg.global) {
            PrimeFaces.confirmDialog = this;

            this.jq.find('.ui-confirmdialog-yes').on('click.ui-confirmdialog', function(e) {                
                if(PrimeFaces.confirmSource) {
                    var fn = eval('(function(){' + PrimeFaces.confirmSource.data('pfconfirmcommand') + '})');
                    
                    fn.call(PrimeFaces.confirmSource);
                    PrimeFaces.confirmDialog.hide();
                    PrimeFaces.confirmSource = null;
                }
                
                e.preventDefault();
            });

            this.jq.find('.ui-confirmdialog-no').on('click.ui-confirmdialog', function(e) {
                PrimeFaces.confirmDialog.hide();
                PrimeFaces.confirmSource = null;
                
                e.preventDefault();
            });
        }
    },
            
    showMessage: function(msg) {
        if(msg.header)
            this.title.text(msg.header);
        
        if(msg.message)
            this.message.text(msg.message);
        
        if(msg.icon)
            this.icon.removeClass().addClass('ui-icon ' + msg.icon);
        
        this.show();
    }

});
