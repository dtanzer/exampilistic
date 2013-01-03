package net.davidtanzer.exampilistic.server.pages.wiki;

import net.davidtanzer.exampilistic.table.TableContent;

public class TableWikiContentPart extends WikiContentPart {
	private final TableContent content;
	
	public TableWikiContentPart(final TableContent content) {
		this.content = content;
	}
	
	public TableWikiContentPart() {
		this(new TableContent());
	}

	public TableContent getContent() {
		return content;
	}
}
