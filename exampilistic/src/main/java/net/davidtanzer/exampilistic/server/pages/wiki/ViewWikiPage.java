package net.davidtanzer.exampilistic.server.pages.wiki;

import java.util.List;

import net.davidtanzer.exampilistic.TabularExample;
import net.davidtanzer.exampilistic.element.PageElement;
import net.davidtanzer.exampilistic.element.TextElement;
import net.davidtanzer.exampilistic.server.components.wiki.TabularExamplePanel;
import net.davidtanzer.exampilistic.server.components.wiki.WikiPageMenu;
import net.davidtanzer.util.textile.TextileUserInput;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class ViewWikiPage extends WebPage {
	private static final long serialVersionUID = 1L;
	
	private final ViewWikiPagePM presentationModel = new ViewWikiPagePM();
	
	public ViewWikiPage(final PageParameters parameters) {
		String pageName = parameters.get("page").toString("");
		presentationModel.startPage(pageName);
		
		add(new WikiPageMenu("pageMenu", pageName));
		add(new Label("headline", new PropertyModel<String>(presentationModel, "headline")));
		
		ListView<PageElement> pageParts = new ListView<PageElement>("pageParts", new PropertyModel<List<PageElement>>(presentationModel, "pageParts")) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(final ListItem<PageElement> item) {
				PageElement model = item.getModelObject();
				if(model instanceof TextElement) {
					String formattedText = new TextileUserInput(((TextElement) model).getText()).convertToHtml();
					
					Label formattedTextLabel = new Label("partContent", formattedText);
					formattedTextLabel.setEscapeModelStrings(false);
					
					item.add(formattedTextLabel);
				} else if(model instanceof TabularExample) {
					item.add(new TabularExamplePanel("partContent", presentationModel.compileTableContent((TabularExample) model)));
				} else {
					throw new UnsupportedOperationException("Model type "+model.getClass().getName()+" is not yet supported");
				}
			}
		};
		add(pageParts);
	}
}
