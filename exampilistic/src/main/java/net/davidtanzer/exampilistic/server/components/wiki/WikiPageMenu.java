package net.davidtanzer.exampilistic.server.components.wiki;

import net.davidtanzer.exampilistic.server.pages.wiki.EditWikiPage;
import net.davidtanzer.exampilistic.server.pages.wiki.ViewWikiPage;

import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class WikiPageMenu extends Panel {
	private static final long serialVersionUID = 1L;

	public WikiPageMenu(final String id, final String pageName) {
		super(id);
		
		PageParameters parameters = new PageParameters();
		parameters.add("page", pageName);
		
		add(new BookmarkablePageLink<>("view", ViewWikiPage.class, parameters));
		add(new BookmarkablePageLink<>("edit", EditWikiPage.class, parameters));
	}
	
}
