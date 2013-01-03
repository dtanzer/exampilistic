package net.davidtanzer.exampilistic.runner;

import net.davidtanzer.exampilistic.TabularExample;
import net.davidtanzer.exampilistic.WikiPage;
import net.davidtanzer.exampilistic.element.PageElement;

public class TestRunner {
	private Class<WikiPage> pageClass;
	private ResultCollector resultCollector;

	public TestRunner(Class<WikiPage> pageClass, ResultCollector resultCollector) {
		this.pageClass = pageClass;
		this.resultCollector = resultCollector;
	}

	public void run() {
		resultCollector.startTestPage(pageClass);
		
		try {
			WikiPage page = pageClass.newInstance();
			for(PageElement element : page.elements()) {
				if(element instanceof TabularExample) {
					TabularExampleRunner exampleRunner = new TabularExampleRunner((TabularExample) element);
					exampleRunner.run(resultCollector);
				} else {
					resultCollector.collectElement(element);
				}
			}
		} catch (InstantiationException | IllegalAccessException e) {
			resultCollector.error("Could not instantiate page class "+pageClass+" with default constructor.", e);
		}
		
		resultCollector.endTestPage();
	}

}
