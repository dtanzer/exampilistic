package net.davidtanzer.exampilistic.spec.test;

import net.davidtanzer.exampilistic.InconclusiveExampleException;
import net.davidtanzer.exampilistic.TabularExample;

public class TheFirstExample extends TabularExample {
	protected String someParameter;
	protected String otherParameter;
	protected String third;

	public void initialize(final String someParameter, final String otherParameter, final String third) {
		super.initialize(new TabularExample.ExampleInitializer() {
			@Override
			public void initialize() {
				TheFirstExample.this.someParameter = someParameter;
				TheFirstExample.this.otherParameter = otherParameter;
				TheFirstExample.this.third = third;
			}
		});
	}

	public TabularExample.ExampleRunner tableRow(final String test, final String expected) {
		return new TabularExample.ExampleRunner() {
			@Override
			public void run() {
				throw new InconclusiveExampleException("Inconclusive: This example is not yet implemented.");
			}
		};
	}
}
