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
	public static final String BUTTON_ICON_SPACER_CLASS = "ui-button-icon-space";
		
	public static enum IconPos
	{
		LEFT,
		LEFT_FLOAT,
		RIGHT,
		RIGHT_FLOAT,
		TOP,
		BOTTOM;
		
		public static boolean renderBeforeText(IconPos iconPosition)
		{
			return iconPosition == null || iconPosition.renderBeforeText();
		}
		
		public boolean renderBeforeText()
		{
			return this == LEFT || this == LEFT_FLOAT || this == TOP;
		}
		
		public static IconPos convert(String value)
		{
			if (value == null)
				return null;
			else if("left".equals(value))
				return LEFT;
			else if("left-float".equals(value))
				return LEFT_FLOAT;
			else if ("right".equals(value))
				return RIGHT;
			else if ("right-float".equals(value))
				return RIGHT_FLOAT;
			else if ("top".equals(value))
				return TOP;
			else if ("bottom".equals(value))
				return BOTTOM;
			else
				return null;
		}
	}
    public static void renderIconSpan(ResponseWriter writer, String icon, IconPos position) throws IOException
    {
        String iconCssClass = "ui-button-icon ui-icon " + icon;
        
        if (position != null)
	        switch (position)
	        {
	        	case LEFT:
	        	case RIGHT:
	        		break;
	        	case BOTTOM:
	        	case TOP:
	        		iconCssClass += " ui-widget-icon-block";
	        		break;
	        	case LEFT_FLOAT:
	        		iconCssClass += " ui-widget-icon-floatbeginning";
	        		break;
	        	case RIGHT_FLOAT:
	        		iconCssClass += " ui-widget-icon-floatend";
	        		break;
	        }
		
		// spacer
		if (position == IconPos.RIGHT)
			renderSpan(writer, BUTTON_ICON_SPACER_CLASS, " ", false);

		writer.startElement("span", null);
		writer.writeAttribute("class", iconCssClass, null);
		writer.endElement("span");
		
		// spacer
		if (position == IconPos.LEFT)
			renderSpan(writer, BUTTON_ICON_SPACER_CLASS, " ", false);
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
    
    /**
     * Renders icon and text value spans
     * 
     * @param writer
     * @param value
     * @param escape
     * @param icon
     * @param iconPos
     * 
     * @throws IOException
     */
	public static void renderIconValueSpans(ResponseWriter writer, Object value, boolean escape, String icon, IconPos iconPos) throws IOException
	{
		// icon
		if (icon != null && IconPos.renderBeforeText(iconPos))
			MobileRenderUtils.renderIconSpan(writer, icon, iconPos);

		// text
		if (value != null)
			MobileRenderUtils.renderTextSpan(writer, value, escape);
		
		// icon - bottom
		if (icon != null && !IconPos.renderBeforeText(iconPos))
			MobileRenderUtils.renderIconSpan(writer, icon, iconPos);
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
    
    /**
     * Concatenates the given values with space as separator
     * 
     * @param values
     * 
     * @return empty string if no values present
     */
    public static String concatSpace(String ... values)
    {
    	String result = "";
    	for(String value : values)
    		if (value != null)
    			result += " " + value;
    	return result.trim();
    }
}
