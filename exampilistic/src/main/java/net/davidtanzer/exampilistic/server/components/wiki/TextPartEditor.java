package net.davidtanzer.exampilistic.server.components.wiki;

import net.davidtanzer.exampilistic.server.pages.wiki.WikiContentPart;

import org.apache.wicket.behavior.AbstractAjaxBehavior;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;

public class TextPartEditor extends Panel {
	private static final long serialVersionUID = 1L;

	public TextPartEditor(final String id, final WikiContentPart model, final AbstractAjaxBehavior ajaxBehavior) {
		super(id);
		
		TextArea<String> content = new TextArea<String>("content", new PropertyModel<String>(model, "text"));
		content.add(ajaxBehavior);
		add(content);
	}
	
}
