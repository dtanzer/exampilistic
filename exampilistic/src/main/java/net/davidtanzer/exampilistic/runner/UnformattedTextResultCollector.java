package net.davidtanzer.exampilistic.runner;

import java.io.PrintStream;
import java.util.List;

import net.davidtanzer.exampilistic.ReadableJavaNames;
import net.davidtanzer.exampilistic.TabularExample;
import net.davidtanzer.exampilistic.WikiPage;
import net.davidtanzer.exampilistic.element.PageElement;

public class UnformattedTextResultCollector implements ResultCollector {
	private PrintStream out;

	public UnformattedTextResultCollector() {
		out = System.out;
	}
	
	@Override
	public void runningAllTests(String classpath) {
		out.println("Running all tests in: \""+classpath+"\".");
	}

	@Override
	public void fatal(Exception e) {
		out.println("FATAL: "+e.getMessage());
	}

	@Override
	public void startTestPage(Class<WikiPage> pageClass) {
		out.println();
		out.println(ReadableJavaNames.asReadableName(pageClass.getSimpleName(), false));
	}

	@Override
	public void endTestPage() {
	}

	@Override
	public void error(String message, Exception exception) {
		out.println("ERROR: "+message);
		out.println("       "+exception.getMessage());
	}

	@Override
	public void collectElement(PageElement element) {
		out.println("Collecting: "+element);
	}

	@Override
	public void collectTableResult(Class<? extends TabularExample> exampleClass, String[] columnNames, List<TableRow> rows) {
		int[] columnWidths = new int[columnNames.length];
		
		for(int i=0; i<columnNames.length; i++) {
			if(columnNames[i] != null) columnWidths[i] = columnNames[i].length();
		}
		
		for(TableRow row : rows) {
			int pos = 0;
			for(String parameter : row.getParameters()) {
				columnWidths[pos] = Math.max(columnWidths[pos], parameter.length());
				pos++;
			}
		}
		
		out.println("\t"+ReadableJavaNames.asReadableName(exampleClass.getSimpleName(), false));
		printTableSeparator(columnWidths);
		
		out.print("\t");
		for(int i=0; i<columnNames.length; i++) {
			out.print("| "+String.format("%-"+columnWidths[i]+"."+columnWidths[i]+"s ", columnNames[i]));
		}
		out.println("|");
		printTableSeparator(columnWidths);
		
		for(TableRow row : rows) {
			out.print("\t");
			int pos=0;
			for(String parameter : row.getParameters()) {
				int width = columnWidths[pos];
				out.print("| "+String.format("%-"+width+"."+width+"s ", parameter));
				pos++;
			}
			out.print("| ");
			out.print(row.getResult());
			if(row.getMessage() != null) {
				out.print(" "+row.getMessage());
			}
			out.println();
		}
		printTableSeparator(columnWidths);
	}

	private void printTableSeparator(int[] columnWidths) {
		out.print("\t");
		for(int i=0; i<columnWidths.length; i++) {
			out.print("+"+String.format(" %-"+columnWidths[i]+"."+columnWidths[i]+"s ", "").replace(' ', '-'));
		}
		out.println("+");
	}
}
