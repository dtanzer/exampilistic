package net.davidtanzer.exampilistic.spec.test;

import net.davidtanzer.exampilistic.WikiPage;

import org.junit.Test;
import net.davidtanzer.exampilistic.TableRow;
import net.davidtanzer.exampilistic.TableInitialization;

public class SomeTestSpecification extends WikiPage {

	@Override public void initializeContent() {
		addText(
			"Some *text*\n\n" +
			"");
		addTabularExample(TheFirstExample.class);
		initializeTable_TheFirstExample();
		addText(
			"");
	}
	@TableInitialization public void initializeTable_TheFirstExample() {
		getTabularExample(TheFirstExample.class).initialize("some value", "5", "other value");
	}
	@Test @TableRow public void theFirstExample_named() {
		runTableRow(0, getTabularExample(TheFirstExample.class).tableRow("foo", "bar"));
	}
	@Test @TableRow public void theFirstExample_row_1() {
		runTableRow(10, getTabularExample(TheFirstExample.class).tableRow("snafu", "3"));
	}
	@Test @TableRow public void theFirstExample_aThirdRow() {
		runTableRow(20, getTabularExample(TheFirstExample.class).tableRow("yet another row", "expected value"));
	}
	@Test @TableRow public void theFirstExample_row_3() {
		runTableRow(30, getTabularExample(TheFirstExample.class).tableRow("4", "4"));
	}
}
