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
import org.primefaces.util.ComponentUtils;
import org.primefaces.util.HTML;
import org.primefaces.component.inputtext.InputText;
import org.primefaces.mobile.util.MobileRenderUtils;

public class InputTextRenderer extends org.primefaces.component.inputtext.InputTextRenderer {

    public final static String MOBILE_STYLE_CLASS = "ui-textinput ui-corner-all ui-shadow-inset ui-textinput-text ui-body-inherit ui-textinput-has-clear-button";
    public final static String MOBILE_SEARCH_STYLE_CLASS = "ui-textinput ui-corner-all ui-shadow-inset ui-textinput-search ui-body-inherit ui-textinput-has-clear-button";
    public final static String MOBILE_CLEAR_BUTTON_CLASS = "ui-textinput-clear-button ui-corner-all ui-button ui-button-icon-only ui-button-right";
    public final static String MOBILE_CLEAR_BUTTON_HIDDEN_CLASS = "ui-textinput-clear-button-hidden";
	public final static String MOBILE_CLEAR_ICON_CLASS = "ui-textinput-clear-button-icon ui-icon-delete ui-icon";

    @Override
	public void decode(FacesContext context, UIComponent component) {
		InputText inputText = (InputText) component;
        if(inputText.isDisabled() || inputText.isReadonly()) {
            return;
        }
        
        decodeBehaviors(context, inputText);
        
        String inputId = inputText.getClientId(context) + "_input";
		String submittedValue = (String) context.getExternalContext().getRequestParameterMap().get(inputId);
        if(submittedValue != null) {
            inputText.setSubmittedValue(submittedValue);
        }
	}

	@Override
	public void encodeMarkup(FacesContext context, InputText inputText) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String clientId = inputText.getClientId(context);
        String inputId = clientId + "_input";
        String type = inputText.getType();
        boolean search = type.equals("search");
        String style = inputText.getStyle();
        String defaultStyleClass = search ? MOBILE_SEARCH_STYLE_CLASS : MOBILE_STYLE_CLASS;
        String styleClass = inputText.getStyleClass();
        styleClass = (styleClass == null) ? defaultStyleClass : defaultStyleClass + " " + styleClass;
        if(inputText.isDisabled()) {
            styleClass = styleClass + " ui-state-disabled";
        }
        
        writer.startElement("div", null);
        writer.writeAttribute("id", clientId, null);
        writer.writeAttribute("class", styleClass, null);
        if(style != null) { 
            writer.writeAttribute("style", style, null);
        }
        
        // search
        if (search)
        	MobileRenderUtils.renderIconSpan(writer, "ui-textinput-search-icon ui-alt-icon ui-icon-search", null);

        encodeInput(context, inputText, inputId);
        
        writer.endElement("div");
	}
    
    protected void encodeInput(FacesContext context, InputText inputText, String inputId) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String valueToRender = ComponentUtils.getValueToRender(context, inputText);
        
        writer.startElement("input", inputText);
        writer.writeAttribute("data-role", "none", null);
        writer.writeAttribute("id", inputId, null);
		writer.writeAttribute("name", inputId, null);
		writer.writeAttribute("type", inputText.getType(), null);           
		writer.writeAttribute("data-clear-btn", "true", null);
		
        if(inputText.isDisabled()) writer.writeAttribute("disabled", "disabled", null);
        if(inputText.isReadonly()) writer.writeAttribute("readonly", "readonly", null);
        if(valueToRender != null) writer.writeAttribute("value", valueToRender , null);
        
        renderPassThruAttributes(context, inputText, HTML.INPUT_TEXT_ATTRS_WITHOUT_EVENTS);
        renderDomEvents(context, inputText, HTML.INPUT_TEXT_EVENTS);
        
        writer.endElement("input");
       
        // clear icon
        encodeClearIcon(writer, valueToRender);
    }
    
    public static void encodeClearIcon(ResponseWriter writer, String value) throws IOException 
    {
    	String buttonCssClass = MOBILE_CLEAR_BUTTON_CLASS;
    	
    	if (value == null)
    		buttonCssClass += " " + MOBILE_CLEAR_BUTTON_HIDDEN_CLASS;
    		
        // button
        writer.startElement("a", null);
        writer.writeAttribute("href", "#", null);
        writer.writeAttribute("class", buttonCssClass, null);
        
        // icon
        MobileRenderUtils.renderIconSpan(writer, MOBILE_CLEAR_ICON_CLASS, null);
        
        writer.endElement("a");
    }
}
