/*
 * Copyright 2009-2014 PrimeTek.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.primefaces.mobile.renderkit;

import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.primefaces.component.tabview.Tab;
import org.primefaces.component.tabview.TabView;
import org.primefaces.util.HTML;
import org.primefaces.util.WidgetBuilder;

public class TabViewRenderer extends org.primefaces.component.tabview.TabViewRenderer 
{
    public static final String MOBILE_CONTAINER_CLASS = "ui-tabs ui-widget ui-widget-content ui-corner-all ui-hidden-container";
    public static final String MOBILE_NAVBAR_CLASS = "ui-navbar";
    public static final String MOBILE_NAVIGATOR_CLASS = "ui-tabs-nav ui-corner-all ui-helper-reset ui-helper-clearfix ui-widget-header";
    
    public static final String MOBILE_TAB_HEADER_CLASS = "ui-tabs-header";
    public static final String MOBILE_TAB_HEADER_ACTIVE_CLASS = "ui-tabs-active ui-state-active";
    
    public static final String MOBILE_TAB_HEADER_TITLE_CLASS = "ui-tabs-anchor ui-button ui-shadow ui-corner-all ui-widget ui-button-inherit";
    public static final String MOBILE_TAB_HEADER_TITLE_ACTIVE_CLASS = "ui-button-active";
   
    public static final String MOBILE_TAB_CONTENT_CLASS = "ui-tabs-panel ui-corner-bottom ui-widget-content";
    
    @Override
    protected void encodeScript(FacesContext context, TabView tabView) throws IOException {
        String clientId = tabView.getClientId(context);
        boolean dynamic = tabView.isDynamic();
        
        WidgetBuilder wb = getWidgetBuilder(context);
        wb.init("TabView", tabView.resolveWidgetVar(), clientId);
        
        if(dynamic) {
            wb.attr("dynamic", true).attr("cache", tabView.isCache());
        }
        
        wb.callback("onTabChange", "function(index)", tabView.getOnTabChange())
            .callback("onTabShow", "function(index)", tabView.getOnTabShow())
            .callback("onTabClose", "function(index)", tabView.getOnTabClose());
        
        encodeClientBehaviors(context, tabView);

        wb.finish();
    }
    
    @Override
    protected void encodeMarkup(FacesContext context, TabView tabView) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String clientId = tabView.getClientId(context);
        String widgetVar = tabView.resolveWidgetVar();
        String style = tabView.getStyle();
        String styleClass = tabView.getStyleClass();
        styleClass = (styleClass == null) ? MOBILE_CONTAINER_CLASS : MOBILE_CONTAINER_CLASS + " " + styleClass;
    
        writer.startElement("div", tabView);
        writer.writeAttribute("id", clientId, null);
        writer.writeAttribute("class", styleClass, "styleClass");
        writer.writeAttribute(HTML.WIDGET_VAR, widgetVar, null);
        if(style != null) { 
            writer.writeAttribute("style", tabView.getStyle(), "style");
        }
        
        encodeHeaders(context, tabView);
        encodeContents(context, tabView);
        
        encodeStateHolder(context, tabView, clientId + "_activeIndex", String.valueOf(tabView.getActiveIndex()));
    
        renderDynamicPassThruAttributes(context, tabView);
        
        writer.endElement("div");
    }
    
    @Override
    protected void encodeHeaders(FacesContext context, TabView tabView) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        int activeIndex = tabView.getActiveIndex();
        
        writer.startElement("div", null);
        writer.writeAttribute("class", MOBILE_NAVBAR_CLASS, null);
        
        writer.startElement("ul", null);
        writer.writeAttribute("class", MOBILE_NAVIGATOR_CLASS, null);

        if(!tabView.isRepeating()) {
            int i = 0;
            for(UIComponent kid : tabView.getChildren()) {
                if(kid.isRendered() && kid instanceof Tab) {
                    encodeTabHeader(context, tabView, (Tab) kid, i, (i == activeIndex));
                    i++;
                }
            }
        } 
        else {
            int dataCount = tabView.getRowCount();
            activeIndex = activeIndex >= dataCount ? 0 : activeIndex;
            Tab tab = (Tab) tabView.getChildren().get(0);
            
            for(int i = 0; i < dataCount; i++) {
                tabView.setIndex(i);
                encodeTabHeader(context, tabView, tab, i, (i == activeIndex));
            }
            
            tabView.setIndex(-1);
        }

        writer.endElement("ul");
        writer.endElement("div");
    }
    
    @Override
    protected void encodeTabHeader(FacesContext context, TabView tabView, Tab tab, int index, boolean active) throws IOException 
    {
        ResponseWriter writer = context.getResponseWriter();
        
        String headerClass = MOBILE_TAB_HEADER_CLASS;
        String titleClass = MOBILE_TAB_HEADER_TITLE_CLASS;
        if (active)
        {
        	headerClass += " " + MOBILE_TAB_HEADER_ACTIVE_CLASS;
        	titleClass+= " " + MOBILE_TAB_HEADER_TITLE_ACTIVE_CLASS;
        }
        
        String styleClass = tab.getTitleStyleClass();
        String style = tab.getTitleStyle();
        styleClass = (styleClass == null) ? headerClass : headerClass + " " + styleClass;
        UIComponent titleFacet = tab.getFacet("title");
        
        //header container
        writer.startElement("li", null);
        writer.writeAttribute("class", styleClass, null);
        writer.writeAttribute("role", "tab", null);
        writer.writeAttribute("aria-expanded", String.valueOf(active), null);
        if(style != null) {
            writer.writeAttribute("style", style, null);
        }
        
        if(tab.isDisabled()) {
            titleClass = titleClass + " ui-state-disabled"; 
        }

        writer.startElement("a", null);
        writer.writeAttribute("class", titleClass, null);
        writer.writeAttribute("href", "#" + tab.getClientId(context), null);
        if(titleFacet == null) {
            writer.write(tab.getTitle());
        } else {
            titleFacet.encodeAll(context);
        }        
        writer.endElement("a");

        writer.endElement("li");
    }
    
    @Override
    protected void encodeContents(FacesContext context, TabView tabView) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        int activeIndex = tabView.getActiveIndex();
        boolean dynamic = tabView.isDynamic();
        
        writer.startElement("div", null);
        writer.writeAttribute("class", TabView.PANELS_CLASS, null);
        
        if(!tabView.isRepeating()) {
            int i = 0;
            for(UIComponent kid : tabView.getChildren()) {
                if(kid.isRendered() && kid instanceof Tab) {
                    encodeTabContent(context, (Tab) kid, i, (i == activeIndex), dynamic);
                    i++;
                }
            }
        }
        else {
            int dataCount = tabView.getRowCount();
            activeIndex = activeIndex >= dataCount ? 0 : activeIndex;
            Tab tab = (Tab) tabView.getChildren().get(0);
            
            for(int i = 0; i < dataCount; i++) {
                tabView.setIndex(i);
                encodeTabContent(context, tab, i, (i == activeIndex), dynamic);
            }
            
            tabView.setIndex(-1);
        }
        
        writer.endElement("div");
    }
        
    @Override
    protected void encodeTabContent(FacesContext context, Tab tab, int index, boolean active, boolean dynamic) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String visibility = active ? "display:block" : "display:none";
        
        writer.startElement("div", null);
        writer.writeAttribute("id", tab.getClientId(context), null);
        writer.writeAttribute("style", visibility, null);
        writer.writeAttribute("role", "tabpanel", null);
        writer.writeAttribute("aria-hidden", String.valueOf(!active), null);

        if(dynamic) {
            if(active) {
                tab.encodeAll(context);
                tab.setLoaded(true);
            }
        } 
        else {
            tab.encodeAll(context);
        }

        writer.endElement("div");
    }
}
