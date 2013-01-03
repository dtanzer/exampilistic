package net.davidtanzer.exampilistic.runner;

import java.io.File;

import net.davidtanzer.exampilistic.WikiPage;

public class Runner {
	private final ResultCollector resultCollector;
	private final String classpath;
	private final WikiPage startPage;

	public Runner(final ResultCollector resultCollector, final String classpath, final WikiPage startPage) {
		this.resultCollector = resultCollector;
		this.classpath = classpath;
		this.startPage = startPage;
	}
	
	private void run() {
		if(startPage != null) {
			throw new UnsupportedOperationException("Running tests from a start page is not yet supported.");
		}
		
		resultCollector.runningAllTests(classpath);
		
		try {
			new WikiPageFinder(classpath).forEach(new WikiPageProcessor() {
				@Override
				public void doPage(final File file, final Class<WikiPage> pageClass) {
					new TestRunner(pageClass, resultCollector).run();
				}
			});
		} catch (IllegalClassPathException e) {
			resultCollector.fatal(e);
		}
	}

	public static void main(final String[] args) {
		String classpath = args[0];
		new Runner(new UnformattedTextResultCollector(), classpath, null).run();
	}
}
