package net.davidtanzer.exampilistic.table;

import net.davidtanzer.exampilistic.table.cglibadapter.ClassVisitorAdapter;
import net.davidtanzer.exampilistic.table.cglibadapter.MethodVisitorAdapter;
import net.sf.cglib.asm.MethodVisitor;

import org.objectweb.asm.Opcodes;

public class TabularExampleClassVisitor extends ClassVisitorAdapter {
	private final TabularExampleClassData classData = new TabularExampleClassData();

	public TabularExampleClassData getExampleClassData() {
		return classData;
	}

	@Override
	public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature, final String[] exceptions) {
		switch(name) {
		case "initialize": return new InitializeMethodVisitor(name, desc, isStatic(access), classData);
		case "tableRow": return new TableRowMethodVisitor(name, desc, isStatic(access), classData);
		default: return new MethodVisitorAdapter();
		}
	}

	private boolean isStatic(final int access) {
		return (access & Opcodes.ACC_STATIC) > 0;
	}

}
