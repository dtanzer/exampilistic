package net.davidtanzer.exampilistic.server.pages.wiki;

import java.util.ArrayList;
import java.util.List;

import net.davidtanzer.exampilistic.ReadableJavaNames;
import net.davidtanzer.exampilistic.WikiPage;
import net.davidtanzer.exampilistic.element.PageElement;
import net.davidtanzer.exampilistic.server.domain.WikiPageRepositoryLocator;
import net.davidtanzer.exampilistic.server.generator.CodeGenerator;

public class EditWikiPagePM {
	private static final String BASE_PACKAGE_NAME = "net.davidtanzer.exampilistic.spec.";
	
	private String title;
	private final List<WikiContentPart> contentParts = new ArrayList<>();
	private boolean titleEditable = true;
	private String packageName;
	
	private final CodeGenerator generator = new CodeGenerator();
	private final WikiPageRepositoryLocator pageRepositoryLocator;

	public EditWikiPagePM() {
		pageRepositoryLocator = new WikiPageRepositoryLocator();
	}
	
	public void save() {
		generator.generate(BASE_PACKAGE_NAME+packageName, title, contentParts);
		//TODO compile and wait for compiled class...
	}

	public void startPage(final String pageName) {
		WikiPage wikiPage = pageRepositoryLocator.get().findPage(pageName);
		if(wikiPage == null) {
			contentParts.add(new TextWikiContentPart(""));
		} else {
			title = ReadableJavaNames.asReadableName(wikiPage.getClass().getSimpleName(), false);
			titleEditable = false;
			for(PageElement element : wikiPage.elements()) {
				contentParts.add(WikiContentPart.forPageElement(element, wikiPage));
			}
			
			String fullPackageName = wikiPage.getClass().getPackage().getName();
			if(!fullPackageName.startsWith(BASE_PACKAGE_NAME)) {
				throw new IllegalStateException("Package name \""+fullPackageName+"\" does not start with \""+BASE_PACKAGE_NAME+"\".");
			}
			packageName = fullPackageName.substring(BASE_PACKAGE_NAME.length());
		}
	}
	
	public boolean isTitleEditable() {
		return titleEditable;
	}

	public String getBasePackageName() {
		return BASE_PACKAGE_NAME;
	}

	public boolean isPackageNameEditable() {
		return isTitleEditable();
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(final String title) {
		this.title = title;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(final String packageName) {
		this.packageName = packageName;
	}
}
