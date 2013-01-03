package net.davidtanzer.exampilistic.server.pages.wiki;

import java.util.ArrayList;
import java.util.List;

import net.davidtanzer.exampilistic.server.components.wiki.TabularPartEditor;
import net.davidtanzer.exampilistic.server.components.wiki.TextPartEditor;
import net.davidtanzer.exampilistic.server.components.wiki.WikiPageMenu;
import net.davidtanzer.exampilistic.server.pages.wiki.InputUpdateBehavior.InputUpdateListener;
import net.davidtanzer.exampilistic.server.util.ListEditor;
import net.davidtanzer.exampilistic.server.util.ListItem;
import net.davidtanzer.exampilistic.server.util.UpdateFormComponentValueBehavior;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class EditWikiPage extends WebPage implements InputUpdateListener {
	private static final long serialVersionUID = 1L;
	final EditWikiPagePM presentationModel = new EditWikiPagePM();
	private Form<Void> editForm;
	private ListEditor<WikiContentPart> editorParts;

	public EditWikiPage(final PageParameters parameters) {
		String pageName = parameters.get("page").toString("");
		presentationModel.startPage(pageName);
		add(new WikiPageMenu("pageMenu", pageName));

		editForm = new Form<Void>("editForm") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit() {
				presentationModel.save();
			}
		};
		editForm.setOutputMarkupId(true);
		add(editForm);
		
		Label basePackageName = new Label("basePackageName", presentationModel.getBasePackageName());
		editForm.add(basePackageName);
		
		TextField<String> packageName = new TextField<String>("packageName", new PropertyModel<String>(presentationModel, "packageName")) {
			private static final long serialVersionUID = 1L;
			
			@Override
			public boolean isEnabled() {
				return presentationModel.isPackageNameEditable();
			}
		};
		packageName.add(new UpdateFormComponentValueBehavior());
		editForm.add(packageName);
		
		TextField<String> title = new TextField<String>("title", new PropertyModel<String>(presentationModel, "title")) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isEnabled() {
				return presentationModel.isTitleEditable();
			}
		};
		title.add(new UpdateFormComponentValueBehavior());
		editForm.add(title);
		
		editorParts = new ListEditor<WikiContentPart>("editorParts", new PropertyModel<List<WikiContentPart>>(presentationModel, "contentParts")) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(final ListItem<WikiContentPart> item) {
				WikiContentPart model = item.getModelObject();
				if(model instanceof TextWikiContentPart) {
					item.add(new TextPartEditor("editor", model, new InputUpdateBehavior("onkeypress", EditWikiPage.this, 500)));
				} else if(model instanceof TableWikiContentPart) {
					item.add(new TabularPartEditor("editor", ((TableWikiContentPart) model).getContent()));
				}
			}
		};
		editForm.add(editorParts);
	}

	@Override
	public void inputUpdated(final AjaxRequestTarget target) {
		List<WikiContentPart> items = new ArrayList<>(editorParts.getItems());
		for(WikiContentPart part : items) {
			if(part instanceof TextWikiContentPart) {
				textChanged((TextWikiContentPart) part, target);
			}
		}
	}

	private void textChanged(final TextWikiContentPart changedContentPart, final AjaxRequestTarget target) {
		ArrayList<WikiContentPart> partsToInsert = new ArrayList<WikiContentPart>();
		String changedText = changedContentPart.getText();
		while(changedText.contains("{table}")) {
			int tableStart = changedText.indexOf("{table}");
			String contentBeforeTable = changedText.substring(0, tableStart);
			changedText = changedText.substring(tableStart+"{table}".length());
			
			partsToInsert.add(new TextWikiContentPart(contentBeforeTable));
			partsToInsert.add(new TableWikiContentPart());
		}
		
		if(!partsToInsert.isEmpty()) {
			partsToInsert.add(new TextWikiContentPart(changedText));
			editorParts.replace(changedContentPart, partsToInsert);
			target.add(editForm);
		}
	}
}
