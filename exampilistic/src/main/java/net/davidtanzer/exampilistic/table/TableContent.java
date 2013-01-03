package net.davidtanzer.exampilistic.table;

import java.util.ArrayList;
import java.util.List;

import net.davidtanzer.exampilistic.element.TabularElement.InitParameter;

public class TableContent {
	public static class ColumnName {
		private String name;
		
		public String getName() {
			return name;
		}
		public void setName(final String name) {
			this.name = name;
		}
	}
	
	public static class TableCellValue {
		private String value;
		
		public String getValue() {
			return value;
		}
		public void setValue(final String value) {
			this.value = value;
		}
	}
	
	public static class TableRow {
		private List<TableCellValue> cellValues = new ArrayList<>();
		private String description;
		
		public List<TableCellValue> getCellValues() {
			return cellValues;
		}
		public void setCellValues(final List<TableCellValue> cellValues) {
			this.cellValues = cellValues;
		}
		
		public String getDescription() {
			return description;
		}
		public void setDescription(final String description) {
			this.description = description;
		}
	}

	private String headline;
	private List<InitParameter> initParameters = new ArrayList<>();
	private List<ColumnName> columnNames = new ArrayList<>();
	private List<TableRow> rows = new ArrayList<>();
	
	public TableContent() {
	}

	public String getHeadline() {
		return headline;
	}
	public void setHeadline(final String headline) {
		this.headline = headline;
	}
	
	public List<InitParameter> getInitParameters() {
		return initParameters;
	}
	public void setInitParameters(final List<InitParameter> initParameters) {
		this.initParameters = initParameters;
	}

	public List<ColumnName> getColumnNames() {
		return columnNames;
	}
	public void setColumnNames(final List<ColumnName> columnNames) {
		this.columnNames = columnNames;
	}
	
	public List<TableRow> getRows() {
		return rows;
	}
	public void setRows(final List<TableRow> rows) {
		this.rows = rows;
	}
}
