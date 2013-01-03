package net.davidtanzer.exampilistic.runner;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.davidtanzer.exampilistic.ReadableJavaNames;
import net.davidtanzer.exampilistic.TabularExample;
import net.sf.cglib.asm.AnnotationVisitor;
import net.sf.cglib.asm.Attribute;
import net.sf.cglib.asm.ClassReader;
import net.sf.cglib.asm.ClassVisitor;
import net.sf.cglib.asm.FieldVisitor;
import net.sf.cglib.asm.Label;
import net.sf.cglib.asm.MethodVisitor;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public class TabularExampleRunner {
	private final TabularExample example;
	private final List<TableRow> rows = new ArrayList<>();
	private String[] columnNames;

	public TabularExampleRunner(final TabularExample example) {
		this.example = example;
		readMethods();
	}

	private void readMethods() {
		Class<? extends TabularExample> cl = example.getClass();
		try {
			InputStream classFileStream = cl.getClassLoader().getResourceAsStream(cl.getName().replaceAll("\\.", "/")+".class");
			ClassReader classReader = new ClassReader(classFileStream);
			classReader.accept(new MethodParameterVisitor(), 0);
		} catch (IOException e) {
			throw new IllegalStateException("Could not load class file: "+example.getClass(), e);
		}
	}

	public void run(final ResultCollector resultCollector) {
		Class<? extends TabularExample> exampleClass = example.getClass();
		Enhancer exampleEnhancer = new Enhancer();
		exampleEnhancer.setSuperclass(exampleClass);
		exampleEnhancer.setCallback(new MethodInterceptor() {
			@Override
			public Object intercept(final Object obj, final Method method, final Object[] args, final MethodProxy proxy) throws Throwable {
				if("table".equals(method.getName())) {
					return proxy.invokeSuper(obj, args);
				} else if("tableRow".equals(method.getName())){
					collectTableColumnNamesAndValues(method, args);
					try {
						proxy.invoke(example, args);
						tableRowSucceeded();
					} catch(Throwable e) {
						tableRowHadError(e);
					}
				} else {
					throw new IllegalStateException("Only \"tableRow\" calls are allowed within \"table\".");
				}
				return null;
			}
		});
		TabularExample enhanced = (TabularExample) exampleEnhancer.create();
		//FIXME interface of tabular example changed! enhanced.table();
		
		resultCollector.collectTableResult(exampleClass, columnNames, rows);
	}

	protected void tableRowHadError(final Throwable e) {
		TableRow currentRow = rows.get(rows.size()-1);
		currentRow.setResult("ERROR");
		currentRow.setMessage(e.getMessage());
	}

	protected void tableRowSucceeded() {
		TableRow currentRow = rows.get(rows.size()-1);
		currentRow.setResult("OK");
	}

	protected void collectTableColumnNamesAndValues(final Method method, final Object[] args) {
		TableRow currentRow = new TableRow();
		ArrayList<String> parameters = new ArrayList<>();
		for(int i=0; i<args.length; i++) {
			parameters.add(String.valueOf(args[i]));
		}
		currentRow.setParameters(parameters);
		rows.add(currentRow);
	}
	
	private void addMethod(final String name, final Type[] argumentTypes, final String[] parameterNames) {
		if("tableRow".equals(name)) {
			if(columnNames != null) {
				throw new IllegalStateException("Only one \"tableRow\" method is allowed per table class!");
			}
			columnNames = new String[parameterNames.length];
			for(int i=0; i<parameterNames.length; i++) {
				columnNames[i] = ReadableJavaNames.asReadableName(parameterNames[i], true);
			}
		}
	}

	private final class MethodParameterVisitor implements ClassVisitor {
		@Override
		public void visitSource(final String arg0, final String arg1) {
		}

		@Override
		public void visitOuterClass(final String arg0, final String arg1, final String arg2) {
		}

		@Override
		public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature, final String[] exceptions) {
			return new ParameterNameVisitor(name, desc, isStatic(access));
		}

		private boolean isStatic(final int access) {
			return (access & Opcodes.ACC_STATIC) > 0;
		}

		@Override
		public void visitInnerClass(final String arg0, final String arg1, final String arg2, final int arg3) {
		}

		@Override
		public FieldVisitor visitField(final int arg0, final String arg1, final String arg2, final String arg3, final Object arg4) {
			return null;
		}

		@Override
		public void visitEnd() {
		}

		@Override
		public void visitAttribute(final Attribute arg0) {
		}

		@Override
		public AnnotationVisitor visitAnnotation(final String arg0, final boolean arg1) {
			return null;
		}

		@Override
		public void visit(final int arg0, final int arg1, final String arg2, final String arg3, final String arg4, final String[] arg5) {
		}
	}
	private final class ParameterNameVisitor implements MethodVisitor {
		private final String name;
		private final HashMap<Integer, Integer> variableTableSlotIndices;
		private final String[] parameterNames;
		private final Type[] argumentTypes;
		
		public ParameterNameVisitor(final String name, final String desc, final boolean isStatic) {
			this.name = name;
			argumentTypes = Type.getArgumentTypes(desc);
			parameterNames = new String[argumentTypes.length];
			variableTableSlotIndices = computeSlotIndices(isStatic, argumentTypes);
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
		public void visitVarInsn(final int arg0, final int arg1) {
		}

		@Override
		public void visitTypeInsn(final int arg0, final String arg1) {
		}

		@Override
		public void visitTryCatchBlock(final Label arg0, final Label arg1, final Label arg2, final String arg3) {
		}

		@Override
		public void visitTableSwitchInsn(final int arg0, final int arg1, final Label arg2, final Label[] arg3) {
		}

		@Override
		public AnnotationVisitor visitParameterAnnotation(final int arg0, final String arg1, final boolean arg2) {
			return null;
		}

		@Override
		public void visitMultiANewArrayInsn(final String arg0, final int arg1) {
		}

		@Override
		public void visitMethodInsn(final int arg0, final String arg1, final String arg2, final String arg3) {
		}

		@Override
		public void visitMaxs(final int arg0, final int arg1) {
		}

		@Override
		public void visitLookupSwitchInsn(final Label arg0, final int[] arg1, final Label[] arg2) {
		}

		@Override
		public void visitLocalVariable(final String name, final String description, final String signature, final Label start, final Label end, final int index) {
			if(variableTableSlotIndices.containsKey(index)) {
				parameterNames[variableTableSlotIndices.get(index)] = name;
			}
		}

		@Override
		public void visitLineNumber(final int arg0, final Label arg1) {
			
		}

		@Override
		public void visitLdcInsn(final Object arg0) {
		}

		@Override
		public void visitLabel(final Label arg0) {
		}

		@Override
		public void visitJumpInsn(final int arg0, final Label arg1) {
		}

		@Override
		public void visitIntInsn(final int arg0, final int arg1) {
		}

		@Override
		public void visitInsn(final int arg0) {
		}

		@Override
		public void visitIincInsn(final int arg0, final int arg1) {
		}

		@Override
		public void visitFrame(final int arg0, final int arg1, final Object[] arg2, final int arg3,
				final Object[] arg4) {
		}

		@Override
		public void visitFieldInsn(final int arg0, final String arg1, final String arg2, final String arg3) {
		}

		@Override
		public void visitEnd() {
			addMethod(name, argumentTypes, parameterNames);
		}

		@Override
		public void visitCode() {
		}

		@Override
		public void visitAttribute(final Attribute arg0) {
		}

		@Override
		public AnnotationVisitor visitAnnotationDefault() {
			return null;
		}

		@Override
		public AnnotationVisitor visitAnnotation(final String arg0, final boolean arg1) {
			return null;
		}
	}

}
