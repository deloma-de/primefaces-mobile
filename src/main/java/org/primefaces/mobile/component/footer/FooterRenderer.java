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
package org.primefaces.mobile.component.footer;

import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.primefaces.mobile.util.MobileRenderUtils;
import org.primefaces.renderkit.CoreRenderer;

public class FooterRenderer extends CoreRenderer 
{
	public static final String MOBILE_FOOTER_CLASS = "ui-toolbar-footer ui-bar-inherit";
	public static final String MOBILE_TITLE_CLASS = "ui-toolbar-title";
	
    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        Footer footer = (Footer) component;
        String swatch = footer.getSwatch();
        String title = footer.getTitle();

        writer.startElement("div", null);
        writer.writeAttribute("data-role", "footer", null);
        
        String footerStyleClass = MobileRenderUtils.concatSpace(MOBILE_FOOTER_CLASS, footer.getStyleClass());
        writer.writeAttribute("class", footerStyleClass, null);
        
        if(footer.getStyle() != null) writer.writeAttribute("style", footer.getStyle(), null);
        if(swatch != null) writer.writeAttribute("data-theme", swatch, null);        
        if(footer.isFixed()) {
            writer.writeAttribute("data-position", "fixed", null);
            
            if(!footer.isTapToggle())
                writer.writeAttribute("data-tap-toggle", "false", null);
        }
        
        renderDynamicPassThruAttributes(context, component);
        
        if(title != null) {
             writer.startElement("h4", null);
             writer.writeAttribute("class", MOBILE_TITLE_CLASS, null);
             writer.writeText(title, null);
             writer.endElement("h4");
        }
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        context.getResponseWriter().endElement("div");
    }
}