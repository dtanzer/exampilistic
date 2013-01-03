package net.davidtanzer.exampilistic.runner;

import java.util.List;

import net.davidtanzer.exampilistic.TabularExample;
import net.davidtanzer.exampilistic.WikiPage;
import net.davidtanzer.exampilistic.element.PageElement;

public interface ResultCollector {

	void runningAllTests(String classpath);
	void fatal(Exception e);
	void startTestPage(Class<WikiPage> pageClass);
	void endTestPage();
	void error(String string, Exception e);
	void collectElement(PageElement element);
	void collectTableResult(Class<? extends TabularExample> exampleClass, String[] columnNames, List<TableRow> rows);

}
