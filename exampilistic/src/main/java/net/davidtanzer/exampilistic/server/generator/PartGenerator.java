package net.davidtanzer.exampilistic.server.generator;

import java.io.File;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Set;

import net.davidtanzer.exampilistic.server.pages.wiki.TableWikiContentPart;
import net.davidtanzer.exampilistic.server.pages.wiki.TextWikiContentPart;
import net.davidtanzer.exampilistic.server.pages.wiki.WikiContentPart;

public abstract class PartGenerator {

	public static PartGenerator forPart(final WikiContentPart part, final File baseDirectory, final String packageName) {
		if(part instanceof TextWikiContentPart) {
			return new TextContentGenerator((TextWikiContentPart) part, baseDirectory, packageName);
		} else if(part instanceof TableWikiContentPart) {
			return new TabularExampleCodeGenerator(((TableWikiContentPart) part).getContent(), baseDirectory, packageName);
		}
		throw new IllegalArgumentException("Unsupported part type: "+part.getClass());
	}
	
	public Set<String> getImportDirectives() {
		return Collections.emptySet();
	}
	public void generateMemberVariables(final PrintWriter resultWriter) {
	}
	public void generateConstructor(final PrintWriter resultWriter) {
	}
	public void generateMethods(final PrintWriter resultWriter) {
	}
	public void generateOtherFiles() {
	}
}
