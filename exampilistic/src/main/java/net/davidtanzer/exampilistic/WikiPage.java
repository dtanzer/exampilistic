package net.davidtanzer.exampilistic;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.davidtanzer.exampilistic.TabularExample.ExampleRunner;
import net.davidtanzer.exampilistic.element.PageElement;
import net.davidtanzer.exampilistic.element.TextElement;

import org.junit.Before;

public abstract class WikiPage {
	private final List<PageElement> elements = new ArrayList<>();
	private final Map<Class<? extends TabularExample>, TabularExample> allTables = new HashMap<>();

	public WikiPage() {
	}
	
	public abstract void initializeContent();
	
	@Before
	public void setup() {
		initializeContent();
	}
	
	public void addText(final String text) {
		elements.add(new TextElement(text));
	}
	
	public void addTabularExample(final Class<? extends TabularExample> tableClass) {
		if(allTables.containsKey(tableClass)) {
			throw new IllegalArgumentException("The tabular example \""+tableClass.getName()+"\" has already been added.");
		}
		
		try {
			Constructor<? extends TabularExample> constructor = tableClass.getConstructor();
			TabularExample table = constructor.newInstance();
			elements.add(table);
			allTables.put(tableClass, table);
		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new IllegalStateException("Could not add tabular example \""+tableClass.getName()+"\"", e);
		}
	}

	protected <T extends TabularExample> T getTabularExample(final Class<T> tableClass) {
		return (T) allTables.get(tableClass);
	}

	public List<PageElement> elements() {
		return Collections.unmodifiableList(elements);
	}
	
	protected void runTableRow(final int displayOrderNumber, final ExampleRunner tableRowRunner) {
		tableRowRunner.run();
	}

}
