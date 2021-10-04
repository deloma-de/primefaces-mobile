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
	public static enum IconPosition
	{
		LEFT,
		RIGHT;
		
		public static IconPosition convert(String value)
		{
			if (value == null)
				return null;
			else if("left".equals(value))
				return LEFT;
			else if ("right".equals(value))
				return RIGHT;
			else
				return null;
		}
	}
    public static void renderIconSpan(ResponseWriter writer, String icon, IconPosition position) throws IOException
    {
        String iconCssClass = "ui-button-icon ui-icon " + icon;
        
		if (position == IconPosition.LEFT)
			iconCssClass += " ui-widget-icon-floatbeginning";
		else if (position == IconPosition.RIGHT)
			iconCssClass += " ui-widget-icon-floatend";
		
		// spacer
		if (position == IconPosition.RIGHT)
			renderSpan(writer, "ui-button-icon-space", " ", false);

		writer.startElement("span", null);
		writer.writeAttribute("class", iconCssClass, null);
		writer.endElement("span");
		
		// spacer
		if (position == IconPosition.LEFT)
			renderSpan(writer, "ui-button-icon-space", " ", false);
    }
    

    
    public static void renderSpan(ResponseWriter writer, String styleClass) throws IOException
    {
    	renderSpan(writer, styleClass, null, false);
    }
    
    /**
     * Renders span spacer between button icon and text
     * 
     * @param writer
     * 
     * @throws IOException
     */
    public static void renderSpan(ResponseWriter writer, String styleClass, String text, boolean escape) throws IOException
    {
    	writer.startElement("span", null);
		writer.writeAttribute("class", styleClass, null);
		if (text != null)
			if (escape)
				writer.writeText(text, null);
			else
				writer.write(text);
		writer.endElement("span");
    }
    
	public static void renderIconValueSpans(ResponseWriter writer, Object value, boolean escape, String icon, IconPosition position) throws IOException
	{
		// icon
		if (icon != null)
			MobileRenderUtils.renderIconSpan(writer, icon, position);

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
