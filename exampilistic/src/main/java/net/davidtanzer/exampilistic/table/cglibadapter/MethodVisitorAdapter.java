package net.davidtanzer.exampilistic.table.cglibadapter;

import java.util.HashMap;

import net.sf.cglib.asm.AnnotationVisitor;
import net.sf.cglib.asm.Attribute;
import net.sf.cglib.asm.Label;
import net.sf.cglib.asm.MethodVisitor;

import org.objectweb.asm.Type;

public class MethodVisitorAdapter implements MethodVisitor {
	private String name;
	private HashMap<Integer, Integer> variableTableSlotIndices;
	private String[] parameterNames;
	private Type[] argumentTypes;

	public MethodVisitorAdapter() {
	}
	
	public MethodVisitorAdapter(final String name, final String desc, final boolean isStatic) {
		this.name = name;
		argumentTypes = Type.getArgumentTypes(desc);
		parameterNames = new String[argumentTypes.length];
		variableTableSlotIndices = computeSlotIndices(isStatic, argumentTypes);
	}
	
	protected String[] getParameterNames() {
		return parameterNames;
	}

	protected Type[] getParameterTypes() {
		return argumentTypes;
	}
	
	private HashMap<Integer, Integer> computeSlotIndices(final boolean isStatic, final Type[] argumentTypes) {
		HashMap<Integer, Integer> slotIndices = new HashMap<>();
		
		int nextIndex = 1;
		if(isStatic) {
			nextIndex = 0;
		}
		
		for(int i=0; i<argumentTypes.length; i++) {
			slotIndices.put(nextIndex, i);
			if(argumentTypes[i] == Type.LONG_TYPE || argumentTypes[i]==Type.DOUBLE_TYPE) {
				nextIndex += 2;
			} else {
				nextIndex++;
			}
		}
		return slotIndices;
	}
	
	@Override
	public AnnotationVisitor visitAnnotation(final String arg0, final boolean arg1) {
		return null;
	}

	@Override
	public AnnotationVisitor visitAnnotationDefault() {
		return null;
	}

	@Override
	public void visitAttribute(final Attribute arg0) {
	}

	@Override
	public void visitCode() {
	}

	@Override
	public void visitEnd() {
	}

	@Override
	public void visitFieldInsn(final int arg0, final String arg1, final String arg2, final String arg3) {
	}

	@Override
	public void visitFrame(final int arg0, final int arg1, final Object[] arg2, final int arg3, final Object[] arg4) {
	}

	@Override
	public void visitIincInsn(final int arg0, final int arg1) {
	}

	@Override
	public void visitInsn(final int arg0) {
	}

	@Override
	public void visitIntInsn(final int arg0, final int arg1) {
	}

	@Override
	public void visitJumpInsn(final int arg0, final Label arg1) {
	}

	@Override
	public void visitLabel(final Label arg0) {
	}

	@Override
	public void visitLdcInsn(final Object arg0) {
	}

	@Override
	public void visitLineNumber(final int arg0, final Label arg1) {
	}

	@Override
	public void visitLocalVariable(final String name, final String description, final String signature, final Label start, final Label end, final int index) {
		if(variableTableSlotIndices != null && variableTableSlotIndices.containsKey(index)) {
			parameterNames[variableTableSlotIndices.get(index)] = name;
		}
	}

	@Override
	public void visitLookupSwitchInsn(final Label arg0, final int[] arg1, final Label[] arg2) {
	}

	@Override
	public void visitMaxs(final int arg0, final int arg1) {
	}

	@Override
	public void visitMethodInsn(final int arg0, final String arg1, final String arg2, final String arg3) {
	}

	@Override
	public void visitMultiANewArrayInsn(final String arg0, final int arg1) {
	}

	@Override
	public AnnotationVisitor visitParameterAnnotation(final int arg0, final String arg1, final boolean arg2) {
		return null;
	}

	@Override
	public void visitTableSwitchInsn(final int arg0, final int arg1, final Label arg2, final Label[] arg3) {
	}

	@Override
	public void visitTryCatchBlock(final Label arg0, final Label arg1, final Label arg2, final String arg3) {
	}

	@Override
	public void visitTypeInsn(final int arg0, final String arg1) {
	}

	@Override
	public void visitVarInsn(final int arg0, final int arg1) {
	}

}
