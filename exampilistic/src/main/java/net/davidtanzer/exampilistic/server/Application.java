package net.davidtanzer.exampilistic.server;

import net.davidtanzer.exampilistic.server.pages.wiki.EditWikiPage;
import net.davidtanzer.exampilistic.server.pages.wiki.ViewWikiPage;

import org.apache.wicket.Page;
import org.apache.wicket.protocol.http.WebApplication;

public class Application extends WebApplication {

	@Override
	protected void init() {
		mountPage("edit", EditWikiPage.class);
		mountPage("view", ViewWikiPage.class);
	}
	
	@Override
	public Class<? extends Page> getHomePage() {
		return HomePage.class;
	}
}
