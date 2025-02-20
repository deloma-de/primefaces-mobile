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
import java.util.List;
import java.util.Map;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.primefaces.component.api.DynamicColumn;
import org.primefaces.component.api.UIColumn;
import org.primefaces.component.column.Column;
import org.primefaces.component.columngroup.ColumnGroup;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.row.Row;
import org.primefaces.mobile.renderkit.paginator.PaginatorRenderer;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;
import org.primefaces.util.ComponentUtils;
import org.primefaces.util.Constants;
import org.primefaces.util.LangUtils;
import org.primefaces.util.MessageFactory;
import org.primefaces.util.WidgetBuilder;

public class DataTableRenderer extends org.primefaces.component.datatable.DataTableRenderer {
 
    public static final String MOBILE_CONTAINER_CLASS = "ui-datatable ui-shadow";
    public static final String MOBILE_TABLE_CLASS = "ui-responsive ui-table table-stripe";
    public static final String MOBILE_COLUMN_HEADER_CLASS = "ui-column-header";
    public static final String MOBILE_ROW_CLASS = "ui-table-row";
    public static final String MOBILE_SORT_ICON_CLASS = "ui-sortable-column-icon ui-icon ui-icon-bars ui-button-icon-only ui-widget-icon-floatend";
    public static final String MOBILE_SORT_ICON_ASC_CLASS = "ui-sortable-column-icon ui-icon-arrow-u ui-button-icon-only ui-widget-icon-floatend";
    public static final String MOBILE_SORT_ICON_DESC_CLASS = "ui-sortable-column-icon ui-icon-arrow-d ui-button-icon-only ui-widget-icon-floatend";
    public static final String MOBILE_SORTED_COLUMN_CLASS = "ui-column-sorted";
    public static final String MOBILE_CELL_LABEL = "ui-table-cell-label";
	
    @Override
    protected void encodeScript(FacesContext context, DataTable table) throws IOException
    {
        WidgetBuilder wb = getWidgetBuilder(context);
        wb.init("DataTable", table);
                
        wb.attr("selectionMode", table.getSelectionMode(), null)
            .attr("reflow", table.isReflow(), false);
        
        if(table.isPaginator()) {
            PaginatorRenderer paginatorRenderer = getPaginatorRenderer(context);
            paginatorRenderer.encodeScript(context, table, wb);
        }
        
        encodeClientBehaviors(context, table);

        wb.finish();
	}
    
    @Override
    protected void encodeMarkup(FacesContext context, DataTable table) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
		String clientId = table.getClientId(context);
        String style = table.getStyle();
        String defaultStyleClass = MOBILE_CONTAINER_CLASS;
        String styleClass = table.getStyleClass();
        styleClass = (styleClass == null) ? defaultStyleClass: defaultStyleClass + " " + styleClass;
        boolean hasPaginator = table.isPaginator();
        String paginatorPosition = table.getPaginatorPosition();
        PaginatorRenderer paginatorRenderer = getPaginatorRenderer(context);
        
        writer.startElement("div", table);
        writer.writeAttribute("id", clientId, "id");
        writer.writeAttribute("class", styleClass, "styleClass");
        if(style != null) {
            writer.writeAttribute("style", style, "style");
        }
        
        if(table.isReflow()) {
            encodeSortableHeaderOnReflow(context, table);
        }
        
        if(hasPaginator && !paginatorPosition.equalsIgnoreCase("bottom")) {
            paginatorRenderer.encodeMarkup(context, table, "top");
        }
        
        encodeRegularTable(context, table);
        
        renderDynamicPassThruAttributes(context, table);
        
        if(hasPaginator && !paginatorPosition.equalsIgnoreCase("top")) {
            paginatorRenderer.encodeMarkup(context, table, "bottom");
        }
        
        if(table.isSelectionEnabled()) {
            encodeStateHolder(context, table, table.getClientId(context) + "_selection", table.getSelectedRowKeysAsString());
        }
        
        writer.endElement("div");
    }
    
    @Override
    protected void encodeRegularTable(FacesContext context, DataTable table) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String styleClass = table.getTableStyleClass();
        styleClass = (styleClass == null) ? MOBILE_TABLE_CLASS : MOBILE_TABLE_CLASS + " " + styleClass;
        if(table.isReflow()) {
            styleClass = styleClass + " ui-table-reflow";
        }           
        
        writer.startElement("div", null);
        writer.writeAttribute("class", DataTable.TABLE_WRAPPER_CLASS, null);
        
        writer.startElement("table", null);
        writer.writeAttribute("role", "grid", null);
        writer.writeAttribute("class", styleClass, null);
        if(table.getTableStyle() != null) writer.writeAttribute("style", table.getTableStyle(), null);
        if(table.getSummary() != null) writer.writeAttribute("summary", table.getSummary(), null);
        
        encodeThead(context, table);
        encodeTFoot(context, table);
        encodeTbody(context, table, false);
        
        writer.endElement("table");
        writer.endElement("div");
    }
    
    @Override
    protected void encodeThead(FacesContext context, DataTable table, int columnStart, int columnEnd, String theadId, String columnGroupName) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        ColumnGroup group = table.getColumnGroup("header");
        List<UIColumn> columns = table.getColumns();
        String theadClientId = (theadId == null) ? table.getClientId(context) + "_head" : theadId;
        
        writer.startElement("thead", null);
        writer.writeAttribute("id", theadClientId, null);
        
        if(group != null && group.isRendered()) {
            context.getAttributes().put(Constants.HELPER_RENDERER, "columnGroup");

            for(UIComponent child : group.getChildren()) {
                if(child.isRendered()) {
                    if(child instanceof Row) {
                        Row headerRow = (Row) child;

                        writer.startElement("tr", null);
                        writer.writeAttribute("class", "ui-bar-a", null);
                        
                        for(UIComponent headerRowChild: headerRow.getChildren()) {
                            if(headerRowChild.isRendered()) {
                                if(headerRowChild instanceof Column)
                                    encodeColumnHeader(context, table, (Column) headerRowChild);
                                else
                                    headerRowChild.encodeAll(context);
                            }
                        }

                        writer.endElement("tr");
                    }
                    else {
                        child.encodeAll(context);
                    }
                }
            }
            
            context.getAttributes().remove(Constants.HELPER_RENDERER);
        } 
        else {
            writer.startElement("tr", null);
            writer.writeAttribute("class", "ui-bar-a", null);
            writer.writeAttribute("role", "row", null);
            
            for(int i = columnStart; i < columnEnd; i++) {
                UIColumn column = columns.get(i);

                if(column instanceof Column) {
                    encodeColumnHeader(context, table, column);
                }
                else if(column instanceof DynamicColumn) {
                    DynamicColumn dynamicColumn = (DynamicColumn) column;
                    dynamicColumn.applyModel();

                    encodeColumnHeader(context, table, dynamicColumn);
                }
            }
            
            writer.endElement("tr");
        }
        
        writer.endElement("thead");
    }
    
    @Override
    public void encodeColumnHeader(FacesContext context, DataTable table, UIColumn column) throws IOException {
        if(!column.isRendered()) {
            return;
        }
        
        ResponseWriter writer = context.getResponseWriter();
        String clientId = column.getContainerClientId(context);
        int priority = column.getResponsivePriority();
        boolean sortable = table.isColumnSortable(context, column);
        
        SortMeta sortMeta = null;
        String defaultStyleClass = sortable ? MOBILE_COLUMN_HEADER_CLASS + " " + DataTable.SORTABLE_COLUMN_CLASS : MOBILE_COLUMN_HEADER_CLASS; 
        String style = column.getStyle();
        String styleClass = column.getStyleClass();
        styleClass = (styleClass == null) ? defaultStyleClass: defaultStyleClass + " " + styleClass;
              
        if(priority > 0)
            styleClass = styleClass + " ui-table-priority-" + priority;
        
        if (sortable) 
        {
            sortMeta = table.getSortByAsMap().get(column.getColumnKey());
            if (sortMeta.isActive())
            	styleClass += MOBILE_SORTED_COLUMN_CLASS;
        }
        
        String width = column.getWidth();
        if(width != null) {
            String unit = width.endsWith("%") ? "" : "px";
            if(style != null)
                style = style + ";width:" + width + unit;
            else
                style = "width:" + width + unit;
        }
        
        writer.startElement("th", null);
        writer.writeAttribute("id", clientId, null);
        writer.writeAttribute("role", "columnheader", null);
        writer.writeAttribute("class", styleClass, null);
        
        if(style != null) writer.writeAttribute("style", style, null);
        if(column.getRowspan() != 1) writer.writeAttribute("rowspan", column.getRowspan(), null);
        if(column.getColspan() != 1) writer.writeAttribute("colspan", column.getColspan(), null);
                
        encodeColumnHeaderContent(context, table, column, sortMeta);
        
        writer.endElement("th");
    }
    
    @Override
    public void encodeColumnFooter(FacesContext context, DataTable table, UIColumn column) throws IOException {
        if(!column.isRendered()) {
            return;
        }
        
        ResponseWriter writer = context.getResponseWriter();
        String style = column.getStyle();
        String styleClass = column.getStyleClass();

        writer.startElement("td", null);
        
        if(style != null) writer.writeAttribute("style", style, null);
        if(styleClass != null) writer.writeAttribute("class", styleClass, null);
        if(column.getRowspan() != 1) writer.writeAttribute("rowspan", column.getRowspan(), null);
        if(column.getColspan() != 1) writer.writeAttribute("colspan", column.getColspan(), null);
        
        //Footer content
        UIComponent facet = column.getFacet("footer");
        String text = column.getFooterText();
        if(facet != null) {
            facet.encodeAll(context);
        } else if(text != null) {
            writer.write(text);
        }

        writer.endElement("td");
    }
    
    @Override
    public void encodeTbody(FacesContext context, DataTable table, boolean dataOnly, int columnStart, int columnEnd, String tbodyId) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String rowIndexVar = table.getRowIndexVar();
        String clientId = table.getClientId(context);
        String emptyMessage = table.getEmptyMessage();
        UIComponent emptyFacet = table.getFacet("emptyMessage");
        String tbodyClientId = (tbodyId == null) ? clientId + "_data" : tbodyId;
                       
        if(table.isSelectionEnabled()) 
            table.getSelectedRowKeys();
        
        int rows = table.getRows();
		int first = table.getFirst();
        int rowCount = table.getRowCount();
        int rowCountToRender = rows == 0 ? rowCount : rows;
        boolean hasData = rowCount > 0;
                      
        if(!dataOnly) {
            writer.startElement("tbody", null);
            writer.writeAttribute("id", tbodyClientId, null);
        }

        if(hasData) {
            encodeRows(context, table, first, (first + rowCountToRender), columnStart, columnEnd);
        }
        else {
            //Empty message
            writer.startElement("tr", null);
            writer.writeAttribute("class", DataTable.EMPTY_MESSAGE_ROW_CLASS, null);

            writer.startElement("td", null);
            writer.writeAttribute("colspan", table.getColumnsCount(), null);
            
            if(emptyFacet != null)
                emptyFacet.encodeAll(context);
            else
                writer.write(emptyMessage);

            writer.endElement("td");
            
            writer.endElement("tr");
        }
		
        if(!dataOnly) {
            writer.endElement("tbody");
        }

		//Cleanup
		table.setRowIndex(-1);
		if(rowIndexVar != null) {
			context.getExternalContext().getRequestMap().remove(rowIndexVar);
		}
    }
    
    @Override
    protected void encodeRows(FacesContext context, DataTable table, int first, int last, int columnStart, int columnEnd) throws IOException {
        String clientId = table.getClientId(context);
        
        for(int i = first; i < last; i++) {
            table.setRowIndex(i);
            if(!table.isRowAvailable()) {
                break;
            }

            encodeRow(context, table, clientId, i, columnStart, columnEnd);
        }
    }
    
    @Override
    public boolean encodeRow(FacesContext context, DataTable table, String clientId, 
    	int rowIndex, int columnStart, int columnEnd) throws IOException
    {
    	ResponseWriter writer = context.getResponseWriter();
        boolean selectionEnabled = table.isSelectionEnabled();
        Object rowKey = null;
        List<UIColumn> columns = table.getColumns();
        
        if(selectionEnabled) {
            //try rowKey attribute
            rowKey = table.getRowKey();
            
            //ask selectable datamodel
            if(rowKey == null)
                rowKey = table.getRowKey(table.getRowData());
        }
        
        //Preselection
        
        boolean selected = table.getSelectedRowKeys().contains(rowKey);
        boolean disabled = table.isDisabledSelection();
        
        String userRowStyleClass = table.getRowStyleClass();
        String rowStyleClass = MOBILE_ROW_CLASS;
        if(selectionEnabled && !table.isDisabledSelection()) {
            rowStyleClass = rowStyleClass + " " + DataTable.SELECTABLE_ROW_CLASS;
        }
            
        if(selected) {
            rowStyleClass = rowStyleClass + " ui-bar-b";
        }
            
        if(userRowStyleClass != null) {
            rowStyleClass = rowStyleClass + " " + userRowStyleClass;
        }
        
        writer.startElement("tr", null);
        writer.writeAttribute("data-ri", rowIndex, null);
        if(rowKey != null) {
            writer.writeAttribute("data-rk", rowKey, null);
        }
        writer.writeAttribute("class", rowStyleClass, null);
        
        for(int i = columnStart; i < columnEnd; i++) {
            UIColumn column = columns.get(i);
            
            if(column instanceof Column) {
                encodeCell(context, table, column, false, disabled, rowIndex);
            }
            else if(column instanceof DynamicColumn) {
                DynamicColumn dynamicColumn = (DynamicColumn) column;
                dynamicColumn.applyModel();

                encodeCell(context, table, dynamicColumn, false, disabled, rowIndex);
            }
        }

        writer.endElement("tr");

        return true;
    }
    
    @Override
    protected void encodeCell(FacesContext context, DataTable table, UIColumn column, 
    	boolean selected, boolean disabled, int rowIndex) throws IOException
    {
    	if(!column.isRendered())
            return;
        
        ResponseWriter writer = context.getResponseWriter();
        String style = column.getStyle();
        String styleClass = column.getStyleClass();
        int colspan = column.getColspan();
        int rowspan = column.getRowspan();
        int priority = column.getResponsivePriority();
        
        if(priority > 0) {
            styleClass = (styleClass == null) ? "ui-table-priority-" + priority : styleClass + " ui-table-priority-" + priority;
        }
        
        writer.startElement("td", null);
        writer.writeAttribute("role", "gridcell", null);
        if(colspan != 1) writer.writeAttribute("colspan", colspan, null);
        if(rowspan != 1) writer.writeAttribute("rowspan", rowspan, null);
        if(style != null) writer.writeAttribute("style", style, null);
        if(styleClass != null) writer.writeAttribute("class", styleClass, null);

        if(table.isReflow()) {
            writer.startElement("b", table);
            writer.writeAttribute("class", MOBILE_CELL_LABEL, null);
            String headerText = column.getHeaderText();
            if (!LangUtils.isValueBlank(headerText)) {
                writer.writeText(headerText, null);
            }
            writer.endElement("b");
        }
        
        column.renderChildren(context);       

        writer.endElement("td");
    }
    
    @Override
    protected String resolveDefaultSortIcon(SortMeta sortMeta)
    {
    	SortOrder sortOrder = sortMeta.getOrder();
        
        switch(sortOrder)
        {
        	case ASCENDING:
        		return MOBILE_SORT_ICON_ASC_CLASS;
        	case DESCENDING:
        		return MOBILE_SORT_ICON_DESC_CLASS;
        	case UNSORTED:
        	default:
        		return MOBILE_SORT_ICON_CLASS;
        }
    }
    
    private PaginatorRenderer getPaginatorRenderer(FacesContext context) {
        PaginatorRenderer renderer = ComponentUtils.getUnwrappedRenderer(
                context,
                "org.primefaces.component",
                "org.primefaces.component.PaginatorRenderer");
        return renderer;
    }
    
    @Override
    protected void encodeSortableHeaderOnReflow(FacesContext context, DataTable table) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        
        Map<SortMeta, String> headers = getSortableColumnHeaders(context, table);
        
        if(!headers.isEmpty()) {
            String reflowId = table.getContainerClientId(context) + "_reflowDD";
            
            writer.startElement("div", null);
            writer.writeAttribute("class", "ui-reflow-dropdown", null);
            
            writer.startElement("select", null);
            writer.writeAttribute("id", reflowId, null);
            writer.writeAttribute("name", reflowId, null);
            writer.writeAttribute("data-role", "none", null);
            
            encodeOptionOnReflow(context, "", MessageFactory.getMessage(DataTable.SORT_LABEL), null, null);
            
            for (Map.Entry<SortMeta, String> header : headers.entrySet()) {
                for(int sortOrder = 0; sortOrder < 2; sortOrder++) 
                {
                	String sortOrderLabel = (sortOrder == 0)
                        ? MessageFactory.getMessage(DataTable.SORT_ASC)
                        : MessageFactory.getMessage(DataTable.SORT_DESC);

                	String value = header.getKey().getColumnKey() + "_" + sortOrder;
            		String label = header.getValue() + " " + sortOrderLabel;

            		encodeOptionOnReflow(context, value,label, header.getKey().getColumnKey(), sortOrder);
                }
            }
            
            writer.endElement("select");
            
            writer.endElement("div");
        }
    }
    
    protected void encodeOptionOnReflow(FacesContext context, String value, String label,
    	String columnKey, Integer sortOrder) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        
        writer.startElement("option", null);
        writer.writeAttribute("value", value, null);
        
        if (columnKey != null)
        	writer.writeAttribute("data-columnkey", columnKey, null);
	    if (sortOrder != null)  
	    	 writer.writeAttribute("data-sortorder", sortOrder, null);
	    
        writer.writeText(label, null);
        
        writer.endElement("option");
    }
}
