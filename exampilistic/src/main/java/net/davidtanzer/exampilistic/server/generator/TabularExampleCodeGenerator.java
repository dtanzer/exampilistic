package net.davidtanzer.exampilistic.server.generator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

import net.davidtanzer.exampilistic.ReadableJavaNames;
import net.davidtanzer.exampilistic.TableInitialization;
import net.davidtanzer.exampilistic.element.TabularElement.InitParameter;
import net.davidtanzer.exampilistic.table.TableContent;
import net.davidtanzer.exampilistic.table.TableContent.ColumnName;
import net.davidtanzer.exampilistic.table.TableContent.TableCellValue;
import net.davidtanzer.exampilistic.table.TableContent.TableRow;

public class TabularExampleCodeGenerator extends PartGenerator {
	private final TableContent content;
	private final File baseDirectory;
	private final String tableClassName;
	private final String tableInstanceName;
	private final String packageName;

	public TabularExampleCodeGenerator(final TableContent content, final File baseDirectory, final String packageName) {
		this.content = content;
		this.baseDirectory = baseDirectory;
		this.packageName = packageName;

		tableClassName = ReadableJavaNames.toJavaName(content.getHeadline());
		tableInstanceName = Character.toLowerCase(tableClassName.charAt(0))+tableClassName.substring(1);
	}

	@Override
	public Set<String> getImportDirectives() {
		return new HashSet<String>() {{
			add("import org.junit.Test;");
			add("import "+TableInitialization.class.getName()+";");
			add("import "+net.davidtanzer.exampilistic.TableRow.class.getName()+";");
		}};
	}
	
	@Override
	public void generateConstructor(final PrintWriter resultWriter) {
		if(content.getHeadline() == null || content.getHeadline().length()==0) {
			return;
		}
		
		resultWriter.append("\t\taddTabularExample(").append(tableClassName).println(".class);");
		resultWriter.append("\t\tinitializeTable_").append(tableClassName).println("();");
	}

	@Override
	public void generateMethods(final PrintWriter resultWriter) {
		resultWriter.append("\t@TableInitialization public void initializeTable_").append(tableClassName).println("() {");
		resultWriter.append("\t\tgetTabularExample(").append(tableClassName).append(".class).initialize(");
		boolean firstParameter = true;
		for(InitParameter parameter : content.getInitParameters()) {
			if(parameter.getParameterName() != null && parameter.getParameterName().length()>0) {
				if(!firstParameter) {
					resultWriter.append(", ");
				}
				//TODO the parameter value has to be "formatted" for the correct data type!
				resultWriter.append("\"").append(parameter.getParameterValue()).append("\"");
				firstParameter = false;
			}
		}
		resultWriter.println(");");
		resultWriter.println("\t}");
		
		int rowNumber = 0;
		for(TableRow row : content.getRows()) {
			if(isEmpty(row)) {
				continue;
			}
			String rowName = ReadableJavaNames.toJavaName(row.getDescription(), false);
			if(rowName == null || rowName.length() == 0) {
				rowName = "row_"+rowNumber;
			}
			resultWriter.append("\t@Test @TableRow public void ").append(tableInstanceName)
				.append("_").append(rowName).println("() {");
			resultWriter.append("\t\trunTableRow(").append(String.valueOf(rowNumber*10))
				.append(", getTabularExample(").append(tableClassName).append(".class).tableRow(");
			int columnNumber = 0;
			for(TableCellValue cellValue : row.getCellValues()) {
				String columnName = content.getColumnNames().get(columnNumber).getName();
				if(columnName != null && columnName.length() > 0) {
					if(columnNumber > 0) {
						resultWriter.append(", ");
					}
					//TODO the value has to be "formatted" for the correct data type!
					resultWriter.append("\"").append(cellValue.getValue()).append("\"");
				}
				columnNumber++;
			}
			resultWriter.println("));");
			resultWriter.println("\t}");
			rowNumber++;
		}
	}
	
	private boolean isEmpty(final TableRow row) {
		for(TableCellValue value : row.getCellValues()) {
			if(value.getValue() != null && value.getValue().length() > 0) {
				return false;
			}
		}
		return true;
	}

	@Override
	public void generateOtherFiles() {
		File tableFile = new File(baseDirectory, tableClassName+".java");
		if(tableFile.exists()) {
			//FIXME we should refactor this file if it already exists!
			return;
		}
		try {
			PrintWriter resultWriter = new PrintWriter(tableFile);
			
			resultWriter.append("package ").append(packageName).println(";");
			resultWriter.println();
			resultWriter.println("import net.davidtanzer.exampilistic.TabularExample;");
			resultWriter.println("import net.davidtanzer.exampilistic.InconclusiveExampleException;");
			resultWriter.println();
			
			resultWriter.append("public class ").append(tableClassName).println(" extends TabularExample {");
			resultWriter.append("\tpublic void initialize(");
			boolean first = true;
			for(InitParameter parameter : content.getInitParameters()) {
				if(parameter.getParameterName() != null && parameter.getParameterName().length()>0) {
					if(!first) {
						resultWriter.append(", ");
					}
					String parameterName = ReadableJavaNames.toJavaName(parameter.getParameterName(), false);
					//TODO determine the correct parameter type
					resultWriter.append("final String ").append(parameterName);
				}
				first = false;
			}
			resultWriter.println(") {");
			resultWriter.println("\t\tsuper.initialize(new TabularExample.ExampleInitializer() {");
			resultWriter.println("\t\t\tpublic void initialize() {");
			for(InitParameter parameter : content.getInitParameters()) {
				String parameterName = ReadableJavaNames.toJavaName(parameter.getParameterName(), false);
				if(parameterName != null && parameterName.length()>0) {
					resultWriter.append("\t\t\t\t").append(tableClassName).append(".this.")
						.append(parameterName).append(" = ")
						.append(parameterName).println(";");
				}
			}
			resultWriter.println("\t\t\t}");
			resultWriter.println("\t\t});");
			resultWriter.println("\t}");
			
			resultWriter.println();
			resultWriter.append("\tpublic TabularExample.ExampleRunner tableRow(");
			first = true;
			for(ColumnName colName : content.getColumnNames()) {
				String columnName = ReadableJavaNames.toJavaName(colName.getName(), false);
				if(columnName != null && columnName.length() > 0) {
					if(!first) {
						resultWriter.append(", ");
					}
					//TODO determine the correct column type
					resultWriter.append("final String ").append(columnName);
				}
				first = false;
			}
			resultWriter.println(") {");
			resultWriter.println("\t\treturn new TabularExample.ExampleRunner() {");
			resultWriter.println("\t\t\tpublic void run() {");
			resultWriter.println("\t\t\t\tthrow new InconclusiveExampleException(\"Inconclusive: This example is not yet implemented.\");");
			resultWriter.println("\t\t\t}");
			resultWriter.println("\t\t};");
			resultWriter.println("\t}");
			resultWriter.println("}");
			
			resultWriter.close();
		} catch (FileNotFoundException e) {
			throw new IllegalStateException("Could not write table class", e);
		}
	}
}
