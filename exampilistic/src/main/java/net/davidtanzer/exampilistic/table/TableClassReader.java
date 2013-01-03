package net.davidtanzer.exampilistic.table;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import net.davidtanzer.exampilistic.ReadableJavaNames;
import net.davidtanzer.exampilistic.TableInitialization;
import net.davidtanzer.exampilistic.TabularExample;
import net.davidtanzer.exampilistic.element.TabularElement.InitParameter;
import net.davidtanzer.exampilistic.table.TableContent.ColumnName;
import net.davidtanzer.exampilistic.table.TableContent.TableCellValue;
import net.davidtanzer.exampilistic.table.TableContent.TableRow;
import net.sf.cglib.asm.ClassReader;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class TableClassReader {
	private static final TableClassReader instance = new TableClassReader();
	
	private TableClassReader() {
	}
	
	public static TableClassReader getInstance() {
		return instance;
	}

	public TableContent readTableContent(final TabularExample tabularExample, final Object tableDataProvider) {
		TableContent result = new TableContent();
		result.setHeadline(ReadableJavaNames.asReadableName(tabularExample.getClass().getSimpleName(), false));
		
		TabularExampleClassData classData = readExampleClassFile(tabularExample);
		TableData tableData = loadTableData(tableDataProvider, tabularExample.getClass());

		readInitParameters(tabularExample, classData, tableData, result);
		readColumnNames(tabularExample, classData, result);
		readTableData(tabularExample, tableData, result);
		
		return result;
	}

	private void readTableData(final TabularExample tabularExample, final TableData tableData, final TableContent result) {
		List<TableRowData> rows = tableData.getTableRows();
		Collections.sort(rows);
		
		for(TableRowData rowData : rows) {
			TableRow row = new TableRow();
			
			for(Object value : rowData.getRowValues()) {
				TableCellValue cellValue = new TableCellValue();
				cellValue.setValue(String.valueOf(value));
				row.getCellValues().add(cellValue);
			}
			row.setDescription(rowData.getRowDescription());
			result.getRows().add(row);
		}
	}

	private void readColumnNames(final TabularExample tabularExample, final TabularExampleClassData classData, final TableContent result) {
		for(String name : classData.getColumnNames()) {
			ColumnName columnName = new ColumnName();
			columnName.setName(ReadableJavaNames.asReadableName(name, true));
			result.getColumnNames().add(columnName);
		}
	}

	private void readInitParameters(final TabularExample tabularExample, final TabularExampleClassData classData, final TableData tableData, final TableContent result) {
		for(String initParameterName : classData.getInitParameterNames()) {
			InitParameter param = new InitParameter();
			param.setParameterName(ReadableJavaNames.asReadableName(initParameterName, true));
			result.getInitParameters().add(param);
		}
		
		int parameterIndex = 0;
		for(Object parameterValue : tableData.getInitParameters()) {
			if(parameterIndex < result.getInitParameters().size()) {
				result.getInitParameters().get(parameterIndex).setParameterValue(String.valueOf(parameterValue));
			}
			parameterIndex++;
		}
	}

	private TableData loadTableData(final Object tableDataProvider, final Class<? extends TabularExample> tableClass) {
		final TableData result = new TableData();
		
		Enhancer providerEnhancer = new Enhancer();
		providerEnhancer.setSuperclass(tableDataProvider.getClass());
		providerEnhancer.setCallback(new MethodInterceptor() {
			private TableRowData tableRowData;

			@Override
			public Object intercept(final Object obj, final Method method, final Object[] args, final MethodProxy proxy) throws Throwable {
				if("getTabularExample".equals(method.getName())) {
					if(args[0].equals(tableClass)) {
						Enhancer tableEnhancer = new Enhancer();
						tableEnhancer.setSuperclass(tableClass);
						
						//Ensure new table row data for every invocation
						tableRowData = new TableRowData();
						tableEnhancer.setCallback(new TableMethodInterceptor(result, tableRowData));
						
						return tableEnhancer.create();
					}
					return proxy.invoke(tableDataProvider, args);
				} else if("runTableRow".equals(method.getName())) {
					StackTraceElement[] stackTrace = new Exception().getStackTrace();
					for(StackTraceElement element : stackTrace) {
						if(element.getClassName().equals(tableDataProvider.getClass().getName())) {
							String methodName = element.getMethodName();
							if(methodName.contains("_")) {
								String descriptionPart = methodName.substring(methodName.indexOf("_")+1);
								if(!descriptionPart.matches("row_\\d+")) {
									tableRowData.setRowDescription(ReadableJavaNames.asReadableName(descriptionPart, true));
								}
							}
							break;
						}
					}
					tableRowData.setRowOrderNumber(((Integer) args[0]).intValue());
					result.addTableRow(tableRowData);
				}
				return proxy.invokeSuper(obj, args);
			}
		});
		
		Object provider = providerEnhancer.create();
		Method[] methods = tableDataProvider.getClass().getMethods();
		for(Method m : methods) {
			try {
				if(m.isAnnotationPresent(TableInitialization.class) || m.isAnnotationPresent(net.davidtanzer.exampilistic.TableRow.class)) {
					m.invoke(provider);
				}
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				throw new IllegalStateException("Could not invoke table method "+m.getName()+" on "+tableDataProvider.getClass(), e);
			}
		}
		
		return result;
	}

	private TabularExampleClassData readExampleClassFile(final TabularExample tabularExample) {
		Class<? extends TabularExample> exampleClass = tabularExample.getClass();
		try {
			InputStream classFileStream = exampleClass.getClassLoader().getResourceAsStream(exampleClass.getName().replaceAll("\\.", "/")+".class");
			ClassReader classReader = new ClassReader(classFileStream);
			TabularExampleClassVisitor tabularExampleClassVisitor = new TabularExampleClassVisitor();
			classReader.accept(tabularExampleClassVisitor, 0);
			
			return tabularExampleClassVisitor.getExampleClassData();
		} catch (IOException e) {
			throw new IllegalStateException("Could not load class file: "+exampleClass, e);
		}
	}

	private final class TableMethodInterceptor implements MethodInterceptor {
		private final TableData result;
		private final TableRowData tableRowData;

		private TableMethodInterceptor(final TableData result, final TableRowData tableRowData) {
			this.result = result;
			this.tableRowData = tableRowData;
		}

		@Override
		public Object intercept(final Object obj, final Method method, final Object[] args, final MethodProxy proxy) throws Throwable {
			if("initialize".equals(method.getName())) {
				result.setInitParameters(args);
			} else if("tableRow".equals(method.getName())) {
				tableRowData.setRowValues(args);
			}
			return null;
		}
	}

}
