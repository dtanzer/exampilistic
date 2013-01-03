package net.davidtanzer.exampilistic.table;

import java.io.Serializable;

public class TableClassReaderLocator implements Serializable {
	private static final long serialVersionUID = 1L;

	public TableClassReader get() {
		return TableClassReader.getInstance();
	}
}
