package net.davidtanzer.exampilistic.server.generator;

import java.io.File;
import java.io.PrintWriter;

import net.davidtanzer.exampilistic.server.pages.wiki.TextWikiContentPart;
import net.davidtanzer.util.textile.TextileUserInput;

public class TextContentGenerator extends PartGenerator {
	private final TextWikiContentPart part;

	public TextContentGenerator(final TextWikiContentPart part, final File baseDirectory, final String packageName) {
		this.part = part;
	}

	@Override
	public void generateConstructor(final PrintWriter resultWriter) {
		JavaCodeResultRenderer renderer = new JavaCodeResultRenderer();
		new TextileUserInput(part.getText()).parse().renderResult(renderer);
		resultWriter.println(renderer.getRenderedResult());
	}
}
