package net.davidtanzer.exampilistic.table;

public class TableRowData implements Comparable<TableRowData> {
	private int rowOrderNumber;
	private Object[] rowValues;
	private String rowDescription;
	
	public int getRowOrderNumber() {
		return rowOrderNumber;
	}
	public void setRowOrderNumber(final int rowOrderNumber) {
		this.rowOrderNumber = rowOrderNumber;
	}
	
	public Object[] getRowValues() {
		return rowValues;
	}
	public void setRowValues(final Object[] rowValues) {
		this.rowValues = rowValues;
	}
	
	@Override public int compareTo(final TableRowData o) {
		return rowOrderNumber-o.rowOrderNumber;
	}
	
	public void setRowDescription(final String rowDescription) {
		this.rowDescription = rowDescription;
	}
	public String getRowDescription() {
		return rowDescription;
	}
}