package net.davidtanzer.exampilistic.server.generator;

import net.davidtanzer.util.textile.SimpleText;
import net.davidtanzer.util.textile.TextilePart;
import net.davidtanzer.util.textile.renderer.Renderer;

public class JavaCodeResultRenderer implements Renderer {
	private final StringBuilder result = new StringBuilder();
	int currentLineLength = 0;
	private boolean isCurrentListOrdered;
	
	public JavaCodeResultRenderer() {
		result.append("\t\taddText(\n\t\t\t\"");
	}
	
	@Override
	public void addLineBreak() {
		result.append("\\n\" +\n\t\t\t\"");
		currentLineLength = 0;
	}

	@Override
	public void append(final String text) {
		String toAppend = escape(text);
		while(currentLineLength + toAppend.length() > 80) {
			int nextSplit = toAppend.indexOf(' ', 80-currentLineLength) + 1;
			result.append(toAppend.substring(0, nextSplit)).append("\" +\n\t\t\t\"");
			toAppend = toAppend.substring(nextSplit);
			currentLineLength = 0;
		}
		result.append(toAppend);
		currentLineLength += toAppend.length();
	}

	private String escape(final String text) {
		return text.replaceAll("\\\\", "\\\\\\\\").replaceAll("\\\"", "\\\\\"").replaceAll("\\\'", "\\\\\'");
	}

	@Override
	public void endBold() {
		result.append("*");
		currentLineLength++;
	}

	@Override
	public void endHeadline() {
		result.append("=");
		currentLineLength++;
	}

	@Override
	public void endItalic() {
		result.append("/");
		currentLineLength++;
	}

	@Override
	public void endItemList(final boolean isOrdered) {
		result.append("\\n\\n\" +\n\t\t\t\"");
		currentLineLength=0;
	}

	@Override
	public void endLink() {
		result.append("]");
		currentLineLength++;
	}

	@Override
	public void endListItem() {
	}

	@Override
	public void endParagraph() {
		result.append("\\n\\n\" +\n\t\t\t\"");
		currentLineLength=0;
	}

	@Override
	public void endStrikeThrough() {
		result.append("-");
		currentLineLength++;
	}

	@Override
	public String getRenderedResult() {
		result.append("\");");
		return result.toString();
	}

	@Override
	public void startBold() {
		result.append("*");
		currentLineLength++;
	}

	@Override
	public void startHeadline() {
		result.append("=");
		currentLineLength++;
	}

	@Override
	public void startItalic() {
		result.append("/");
		currentLineLength++;
	}

	@Override
	public void startItemList(final boolean isOrdered) {
		isCurrentListOrdered = isOrdered;
	}

	@Override
	public void startLink(final TextilePart linkUrl) {
		result.append("[").append(((SimpleText) linkUrl).getText()).append("|");
	}

	@Override
	public void startListItem() {
		result.append("\\n\" + \n\t\t\t\"");
		if(isCurrentListOrdered) {
			result.append("#");
		} else {
			result.append("*");
		}
		currentLineLength++;
	}

	@Override
	public void startParagraph() {

	}

	@Override
	public void startStrikeThrough() {
		result.append("-");
		currentLineLength++;
	}

}
