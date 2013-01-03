package net.davidtanzer.exampilistic.server.pages.wiki;

import net.davidtanzer.exampilistic.TabularExample;
import net.davidtanzer.exampilistic.WikiPage;
import net.davidtanzer.exampilistic.element.PageElement;
import net.davidtanzer.exampilistic.element.TextElement;
import net.davidtanzer.exampilistic.table.TableClassReader;

public abstract class WikiContentPart {
	public static WikiContentPart forPageElement(final PageElement element, final WikiPage parentWikiPage) {
		if(element instanceof TextElement) {
			return new TextWikiContentPart((TextElement) element);
		} else if(element instanceof TabularExample) {
			return new TableWikiContentPart(TableClassReader.getInstance().readTableContent((TabularExample) element, parentWikiPage));
		}
		throw new IllegalArgumentException("Unknown type of page element: "+element.getClass().getName());
	}
}
