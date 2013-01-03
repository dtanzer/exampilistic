package net.davidtanzer.exampilistic.table;

import org.objectweb.asm.Type;

public class TabularExampleClassData {
	private String[] initParameterNames;
	private Type[] initParameterTypes;
	private String[] columnNames;
	private Type[] columnTypes;

	public void setInitParameterNames(final String[] initParameterNames) {
		if(this.initParameterNames != null) {
			throw new IllegalStateException("Only one initialize method is allowed!");
		}
		this.initParameterNames = initParameterNames;
	}
	public String[] getInitParameterNames() {
		return initParameterNames;
	}

	public void setInitParameterTypes(final Type[] initParameterTypes) {
		this.initParameterTypes = initParameterTypes;
	}
	public Type[] getInitParameterTypes() {
		return initParameterTypes;
	}
	
	public void setColumnNames(final String[] columnNames) {
		if(this.columnNames != null) {
			throw new IllegalStateException("Only one tableRow method is allowed!");
		}
		this.columnNames = columnNames;
	}
	public String[] getColumnNames() {
		return columnNames;
	}
	
	public void setColumnTypes(final Type[] columnTypes) {
		this.columnTypes = columnTypes;
	}
	public Type[] getColumnTypes() {
		return columnTypes;
	}
}
