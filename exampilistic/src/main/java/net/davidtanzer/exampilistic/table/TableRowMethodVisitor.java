package net.davidtanzer.exampilistic.table;

import net.davidtanzer.exampilistic.table.cglibadapter.MethodVisitorAdapter;

public class TableRowMethodVisitor extends MethodVisitorAdapter {
	private final TabularExampleClassData classData;

	public TableRowMethodVisitor(final String name, final String desc, final boolean isStatic, final TabularExampleClassData classData) {
		super(name, desc, isStatic);
		this.classData = classData;
	}

	@Override
	public void visitEnd() {
		classData.setColumnNames(getParameterNames());
		classData.setColumnTypes(getParameterTypes());
	}
}
