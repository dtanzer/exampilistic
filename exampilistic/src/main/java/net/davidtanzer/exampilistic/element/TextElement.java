package net.davidtanzer.exampilistic.element;


public class TextElement extends PageElement {

	private final String text;

	public TextElement(final String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}
}
