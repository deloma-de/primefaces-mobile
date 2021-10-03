package org.primefaces.mobile.util;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

/**
 * generic render utilitites
 *
 * @author Marco Janc 2021
 */
public class MobileRenderUtils 
{
    public static void renderButtonIconSpan(ResponseWriter writer, String icon, String position) throws IOException
    {
        String iconCssClass = "ui-button-icon ui-icon " + icon;
		if (position != null)
			if (position.equals("left"))
				iconCssClass += " ui-widget-icon-floatbeginning";
			else if (position.equals("right"))
				iconCssClass += " ui-widget-icon-floatend";

		writer.startElement("span", null);
		writer.writeAttribute("class", iconCssClass, null);
		writer.endElement("span");
    }
    
    /**
     * Renders span spacer between button icon and text
     * 
     * @param writer
     * 
     * @throws IOException
     */
    public static void renderButtonIconSpace(ResponseWriter writer) throws IOException
    {
    	writer.startElement("span", null);
		writer.writeAttribute("class", "ui-button-icon-space", null);
		writer.endElement("span");
    }
    
	public static void renderButtonIconValue(ResponseWriter writer, Object value, boolean escape, String icon, String iconPos) throws IOException
	{
		// icon
		if (icon != null)
			MobileRenderUtils.renderButtonIconSpan(writer, icon, iconPos);

		// text
		if (value != null)
			MobileRenderUtils.renderTextSpan(writer, value, escape);
	}
    
    public static void renderTextSpan(ResponseWriter writer, Object value, boolean escape) throws IOException
    {
    	renderTextSpan(writer, value, escape, null, null);
    }
    
    public static void renderTextSpan(ResponseWriter writer, Object value, boolean escape, FacesContext context, UIComponent facet) throws IOException
    {
		writer.startElement("span", null);
		
        if(facet == null)
        {
            if (escape)
				writer.writeText(value, "value");
			else
				writer.write(value.toString());
        }    
        else
        	facet.encodeAll(context);
		
		writer.endElement("span");
    }
}
