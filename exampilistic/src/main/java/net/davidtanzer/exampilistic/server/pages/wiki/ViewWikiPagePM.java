package net.davidtanzer.exampilistic.server.pages.wiki;

import java.io.Serializable;
import java.util.List;

import net.davidtanzer.exampilistic.ReadableJavaNames;
import net.davidtanzer.exampilistic.TabularExample;
import net.davidtanzer.exampilistic.WikiPage;
import net.davidtanzer.exampilistic.element.PageElement;
import net.davidtanzer.exampilistic.server.domain.WikiPageRepositoryLocator;
import net.davidtanzer.exampilistic.table.TableClassReaderLocator;
import net.davidtanzer.exampilistic.table.TableContent;

import org.apache.wicket.request.http.flow.AbortWithHttpErrorCodeException;

public class ViewWikiPagePM implements Serializable {
	private static final long serialVersionUID = 1L;

	private final WikiPageRepositoryLocator pageRepositoryLocator;
	private WikiPage wikiPage;
	private final TableClassReaderLocator tableClassReaderLocator = new TableClassReaderLocator();
	
	public ViewWikiPagePM() {
		pageRepositoryLocator = new WikiPageRepositoryLocator();
	}
	
	public void startPage(final String pageName) {
		wikiPage = pageRepositoryLocator.get().findPage(pageName);
		if(wikiPage == null) {
			throw new AbortWithHttpErrorCodeException(404, "Page not found: "+pageName);
		}
	}

	public String getHeadline() {
		return ReadableJavaNames.asReadableName(wikiPage.getClass().getSimpleName(), false);
	}
	
	public List<PageElement> getPageParts() {
		return wikiPage.elements();
	}

	public TableContent compileTableContent(final TabularExample tabularExample) {
		return tableClassReaderLocator.get().readTableContent(tabularExample, wikiPage);
	}
}
