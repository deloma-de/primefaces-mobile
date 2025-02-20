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
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.primefaces.component.inputtextarea.InputTextarea;
import org.primefaces.util.ComponentUtils;
import org.primefaces.util.HTML;
import org.primefaces.util.WidgetBuilder;

public class InputTextareaRenderer extends org.primefaces.component.inputtextarea.InputTextareaRenderer {

    public final static String MOBILE_STYLE_CLASS = "ui-input-text ui-shadow-inset ui-body-inherit ui-corner-all";
	
	@Override
	public void encodeMarkup(FacesContext context, InputTextarea inputTextarea) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
        String clientId = inputTextarea.getClientId(context);
        String valueToRender = ComponentUtils.getValueToRender(context, inputTextarea);
        String style = inputTextarea.getStyle();
        String styleClass = inputTextarea.getStyleClass();
        styleClass = (styleClass == null) ? MOBILE_STYLE_CLASS: MOBILE_STYLE_CLASS + " " + styleClass;
        styleClass = !inputTextarea.isDisabled() ? styleClass : styleClass + " ui-state-disabled";
        
		writer.startElement("textarea", inputTextarea);
        writer.writeAttribute("data-role", "none", null);
		writer.writeAttribute("id", clientId, null);
		writer.writeAttribute("name", clientId, null);
        writer.writeAttribute("class", styleClass, null); 

		renderPassThruAttributes(context, inputTextarea, HTML.INPUT_TEXTAREA_ATTRS);
        renderDomEvents(context, inputTextarea, HTML.INPUT_TEXT_EVENTS);

        if(inputTextarea.isDisabled()) writer.writeAttribute("disabled", "disabled", "disabled");
        if(inputTextarea.isReadonly()) writer.writeAttribute("readonly", "readonly", "readonly");
        if(style != null) writer.writeAttribute("style", style, null);  
		if(valueToRender != null) writer.writeText(valueToRender, "value");        

        writer.endElement("textarea");
	}
    
    @Override
	public void encodeScript(FacesContext context, InputTextarea inputTextarea) throws IOException 
    {
        WidgetBuilder wb = getWidgetBuilder(context);
        wb.init("InputTextarea", inputTextarea).finish();
	}
}
