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
import org.primefaces.component.dialog.Dialog;
import org.primefaces.expression.SearchExpressionFacade;
import org.primefaces.mobile.util.MobileRenderUtils;
import org.primefaces.util.WidgetBuilder;

public class DialogRenderer extends org.primefaces.component.dialog.DialogRenderer 
{
    
    public static final String MOBILE_CONTAINER_CLASS = "ui-popup-container ui-popup-hidden ui-popup-truncate";
    public static final String MOBILE_POPUP_CLASS = "ui-popup ui-body-inherit ui-overlay-shadow ui-corner-all";
    public static final String MOBILE_MASK_CLASS = "ui-popup-screen ui-overlay-b ui-screen-hidden";
    public static final String MOBILE_TITLE_BAR_CLASS = "ui-toolbar-header ui-bar-inherit";
    public static final String MOBILE_TITLE_CLASS = "ui-toolbar-title";
    public static final String MOBILE_CONTENT_CLASS = "ui-content";
    public static final String MOBILE_CLOSE_ICON_CLASS = "ui-button ui-corner-all ui-button-icon-only ui-toolbar-header-button-left";
	
    @Override
    protected void encodeScript(FacesContext context, Dialog dialog) throws IOException {
        String clientId = dialog.getClientId(context);
        WidgetBuilder wb = getWidgetBuilder(context);
        wb.initWithDomReady("Dialog", dialog.resolveWidgetVar(), clientId);

        wb.attr("visible", dialog.isVisible(), false)
            .attr("modal", dialog.isModal(), false)
            .attr("dynamic", dialog.isDynamic(), false)
            .attr("showEffect", dialog.getShowEffect(), null)
            .attr("closeOnEscape", dialog.isCloseOnEscape(), false)  
            .callback("onHide", "function()", dialog.getOnHide())
            .callback("onShow", "function()", dialog.getOnShow());
        
        String focusExpressions = SearchExpressionFacade.resolveClientIds(
        		context, dialog, dialog.getFocus());
        if (focusExpressions != null) {
        	wb.attr("focus", focusExpressions);
        }

        encodeClientBehaviors(context, dialog);
         
        wb.finish();
    }

    @Override
    protected void encodeMarkup(FacesContext context, Dialog dialog) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String clientId = dialog.getClientId(context);
        String style = dialog.getStyle();
        String styleClass = dialog.getStyleClass();
        styleClass = (styleClass == null) ? MOBILE_CONTAINER_CLASS : MOBILE_CONTAINER_CLASS + " " + styleClass;

        writer.startElement("div", null);
        writer.writeAttribute("id", clientId + "_mask", null);
        writer.writeAttribute("class", MOBILE_MASK_CLASS, null);
        writer.endElement("div");
        
        writer.startElement("div", dialog);
        writer.writeAttribute("id", clientId, null);
        writer.writeAttribute("class", styleClass, null);
        if(style != null) {
            writer.writeAttribute("style", style, null);
        }
        
        writer.startElement("div", null);
        writer.writeAttribute("class", MOBILE_POPUP_CLASS, null);
        
        if(dialog.isShowHeader()) {
            encodeHeader(context, dialog);
        }
        
        renderDynamicPassThruAttributes(context, dialog);
        
        encodeContent(context, dialog);
        
        writer.endElement("div");

        writer.endElement("div");
    }
    
    @Override
    protected void encodeHeader(FacesContext context, Dialog dialog) throws IOException 
    {
       DialogRenderer.encodeHeader(context, dialog, dialog.getHeader(), dialog.isClosable());
    }
    
    @Override
    protected void encodeContent(FacesContext context, Dialog dialog) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        
        writer.startElement("div", null);
        writer.writeAttribute("class", "ui-content", null);
        writer.writeAttribute("role", "main", null);
        
        if(!dialog.isDynamic()) {
            renderChildren(context, dialog);
        }
        
        writer.endElement("div");
    }
    
    public static void encodeHeader(FacesContext context, UIComponent dialog,
    	String header, boolean closable) throws IOException
    {
    	ResponseWriter writer = context.getResponseWriter();
    	
        UIComponent headerFacet = dialog.getFacet("header");
        
        writer.startElement("div", null);
        writer.writeAttribute("class", DialogRenderer.MOBILE_TITLE_BAR_CLASS, null);
        
        //close
        if(closable)
        {
    	    writer.startElement("a", null);
    	    writer.writeAttribute("href", "#", null);
    	    writer.writeAttribute("class", MOBILE_CLOSE_ICON_CLASS, null);
    	    
    	    // icon
    	    MobileRenderUtils.renderIconSpan(writer, "ui-icon-delete", null);
    	    
    	    writer.endElement("a");
        }
        
        //title
        writer.startElement("h1", null);
        writer.writeAttribute("class", DialogRenderer.MOBILE_TITLE_CLASS, null);
        writer.writeAttribute("role", "heading", null);
        
        if(headerFacet != null)
            headerFacet.encodeAll(context);
        else if(header != null)
            writer.write(header);
        
        writer.endElement("h1");
        
        writer.endElement("div");
    
    }
}