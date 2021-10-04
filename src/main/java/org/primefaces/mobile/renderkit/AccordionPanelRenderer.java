/*
 * Copyright 2009-2015 PrimeTek.
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
import org.primefaces.component.accordionpanel.AccordionPanel;
import org.primefaces.component.tabview.Tab;
import org.primefaces.mobile.util.MobileRenderUtils;
import org.primefaces.mobile.util.MobileUtils;
import org.primefaces.util.HTML;
import org.primefaces.util.WidgetBuilder;

public class AccordionPanelRenderer extends org.primefaces.component.accordionpanel.AccordionPanelRenderer {
    
    public final static String MOBILE_CONTAINER_CLASS = "ui-accordion ui-collapsible-set ui-corner-all ui-hidden-container";
    public final static String MOBILE_INACTIVE_TAB_CONTAINER_CLASS = "ui-collapsible ui-collapsible-inset ui-corner-all ui-collapsible-themed-content ui-collapsible-collapsed";
    public final static String MOBILE_ACTIVE_TAB_CONTAINER_CLASS = "ui-collapsible ui-collapsible-inset ui-corner-all ui-collapsible-themed-content";
    public final static String MOBILE_ACTIVE_TAB_HEADER_CLASS = "ui-collapsible-heading";
    public final static String MOBILE_INACTIVE_TAB_HEADER_CLASS = "ui-collapsible-heading ui-collapsible-heading-collapsed";
    public final static String MOBILE_ACTIVE_TAB_CONTENT_CLASS = "ui-collapsible-content ui-body-inherit";
    public final static String MOBILE_INACTIVE_TAB_CONTENT_CLASS = "ui-collapsible-content ui-body-inherit ui-collapsible-content-collapsed";
    
    public final static String MOBILE_TOGGLE_CLASS = "ui-collapsible-heading-toggle ui-button";
	
    @Override
    protected void encodeMarkup(FacesContext context, AccordionPanel acco) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		String clientId = acco.getClientId(context);
        String widgetVar = acco.resolveWidgetVar();
        String style = acco.getStyle();
        String styleClass = acco.getStyleClass();
        styleClass = styleClass == null ? MOBILE_CONTAINER_CLASS : MOBILE_CONTAINER_CLASS + " " + styleClass;
        
		writer.startElement("div", acco);
		writer.writeAttribute("id", clientId, null);
        writer.writeAttribute("class", styleClass, null);
		if(acco.getStyle() != null) {
            writer.writeAttribute("style", style, null);
        }
        
        writer.writeAttribute("role", "tablist", null);
        
        writer.writeAttribute(HTML.WIDGET_VAR, widgetVar, null);

		encodeTabs(context, acco);
        encodeStateHolder(context, acco);
        
        renderDynamicPassThruAttributes(context, acco);

		writer.endElement("div");
	}

    @Override
	protected void encodeScript(FacesContext context, AccordionPanel acco) throws IOException {
		String clientId = acco.getClientId(context);
        boolean multiple = acco.isMultiple();
        
        WidgetBuilder wb = getWidgetBuilder(context);
        wb.init("AccordionPanel", acco.resolveWidgetVar(), clientId);
         		
        if(acco.isDynamic()) {
            wb.attr("dynamic", true).attr("cache", acco.isCache());
        }
        
        wb.attr("multiple", multiple, false)
        .callback("onTabChange", "function(panel)", acco.getOnTabChange())
        .callback("onTabShow", "function(panel)", acco.getOnTabShow());
        
        encodeClientBehaviors(context, acco);
        
        wb.finish();
	}
    
    @Override
    protected void encodeTab(FacesContext context, AccordionPanel accordionPanel, Tab tab, boolean active, boolean dynamic, boolean rtl) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        
        String containerClass = active ? MOBILE_ACTIVE_TAB_CONTAINER_CLASS : MOBILE_INACTIVE_TAB_CONTAINER_CLASS;
        String headerClass = active ? MOBILE_ACTIVE_TAB_HEADER_CLASS : MOBILE_INACTIVE_TAB_HEADER_CLASS;
        headerClass = tab.isDisabled() ? headerClass + " ui-state-disabled" : headerClass;
        headerClass = tab.getTitleStyleClass() == null ? headerClass : headerClass + " " + tab.getTitleStyleClass();

        String contentClass = active ? MOBILE_ACTIVE_TAB_CONTENT_CLASS : MOBILE_INACTIVE_TAB_CONTENT_CLASS;
        UIComponent titleFacet = tab.getFacet("title");

        //tab
        writer.startElement("div", null);
        writer.writeAttribute("id", tab.getClientId(context), null);
        writer.writeAttribute("class", containerClass, null);
        writer.writeAttribute("role", "tabpanel", null);
        
        //header container
        writer.startElement("div", null);
        writer.writeAttribute("class", headerClass, null);
        writer.writeAttribute("role", "tab", null);
        writer.writeAttribute("aria-expanded", String.valueOf(active), null);
        if(tab.getTitleStyle() != null) writer.writeAttribute("style", tab.getTitleStyle(), null);
        if(tab.getTitletip() != null) writer.writeAttribute("title", tab.getTitletip(), null);

        writer.startElement("a", null);
        writer.writeAttribute("href", "#", null);
        writer.writeAttribute("class", MOBILE_TOGGLE_CLASS, null);
        
        // icon
        MobileRenderUtils.renderIconSpan(writer, PanelRenderer.getIconClass(!active), null);
        
        // text
        MobileRenderUtils.renderTextSpan(writer, tab.getTitle(), false, context, titleFacet);

        writer.endElement("a");
        
        writer.endElement("div");

        //content
        writer.startElement("div", null);
        writer.writeAttribute("class", contentClass, null);
        writer.writeAttribute("aria-hidden", String.valueOf(!active), null);

        writer.startElement("p", null);
        if(dynamic) {
            if(active) {
                tab.encodeAll(context);
                tab.setLoaded(true);
            }
        }
        else {
            tab.encodeAll(context);
        }
        writer.endElement("p");

        writer.endElement("div");
        
        writer.endElement("div");
    }
    
}
