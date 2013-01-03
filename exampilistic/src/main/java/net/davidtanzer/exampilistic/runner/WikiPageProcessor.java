package net.davidtanzer.exampilistic.runner;

import java.io.File;

import net.davidtanzer.exampilistic.WikiPage;

public abstract class WikiPageProcessor {
	public boolean shouldProcess(final File file, final String pageClassName) {
		return true;
	}
	
	public abstract void doPage(File file, Class<WikiPage> pageClass);
}
