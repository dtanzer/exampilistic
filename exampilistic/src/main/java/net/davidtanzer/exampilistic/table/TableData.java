package net.davidtanzer.exampilistic.table;

import java.util.ArrayList;
import java.util.List;

public class TableData {
	private Object[] initParameters;
	private final List<TableRowData> tableRows = new ArrayList<>();
	
	public void setInitParameters(final Object[] initParameters) {
		this.initParameters = initParameters;
	}
	public Object[] getInitParameters() {
		return initParameters;
	}
	public void addTableRow(final TableRowData tableRowData) {
		this.tableRows.add(tableRowData);
	}
	
	public List<TableRowData> getTableRows() {
		return tableRows;
	}
}
