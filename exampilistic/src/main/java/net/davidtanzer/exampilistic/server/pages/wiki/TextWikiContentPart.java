package net.davidtanzer.exampilistic.server.pages.wiki;

import net.davidtanzer.exampilistic.element.TextElement;

public class TextWikiContentPart extends WikiContentPart {
	private String text;

	public TextWikiContentPart(final TextElement element) {
		this(element.getText());
	}
	
	public TextWikiContentPart(final String text) {
		this.text = text;
	}

	public String getText() {
		if(text == null) {
			text = "";
		}
		return text;
	}
	
	public void setText(final String text) {
		this.text = text;
	}

	public void merge(final TextWikiContentPart mergeIn) {
		text = text+"\n"+mergeIn.getText();
	}
}
