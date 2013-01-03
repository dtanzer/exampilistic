package net.davidtanzer.exampilistic;

import net.davidtanzer.exampilistic.element.PageElement;

public abstract class TabularExample extends PageElement {
	protected void initialize(final ExampleInitializer exampleInitializer) {
		exampleInitializer.initialize();
	}
	
	public static interface ExampleInitializer {
		public void initialize();
	}

	public interface ExampleRunner {
		void run();
	}
	
}
