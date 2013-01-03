package net.davidtanzer.exampilistic.spec.textile;

import net.davidtanzer.exampilistic.WikiPage;

public class PageWithLotsOfTextileElements extends WikiPage {
	@Override
	public void initializeContent() {
		addText(
			"Normal text flow: As long as you do not add new lines to your text, it flows as a " +
			"paragraph. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Quisque velit " +
			"turpis, ultricies nec placerat et, accumsan faucibus massa. Nullam pretium, lacus " +
			"sit amet vestibulum pretium, ligula tortor dictum urna, ut rhoncus tortor leo et " +
			"mauris. Integer porttitor lorem sit amet.\n\n" +
			"An empty line forces a new paragraph.\n" +
			"A new line forces a new line.\n" +
			"Another line\n" +
			"The paragraph continues: Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
			"Quisque velit turpis, ultricies nec placerat et, accumsan faucibus massa. Nullam " +
			"pretium, lacus sit amet vestibulum pretium, ligula tortor dictum urna, ut rhoncus " +
			"tortor leo et mauris. Integer porttitor lorem sit amet.\n\n" +
			"=Headline=\n" +
			"This paragraph contains text in *bold*, /italic/ and -strikethrough-. Attributes " +
			"can be combined: */bold,italic/*, */-bold,italic,strikethrough-/*.\n\n" +
			"=Multiple words=\n" +
			"All basic formatting options *can contain multiple words*. /Yes all of them/. -Really " +
			"all of them-. /*And also in combination with others*/.\n\n" +
			"[http://davidtanzer.net|http://davidtanzer.net]\n" +
			"[http://gclimbing.com|http://gclimbing.com]\n" +
			"[http://gclimbing.com|A really nice mountain sports site]\n\n" +
			"* Bullet point\n" +
			"* Another *bullet* point\n" +
			"* Go here: [http://gclimbing.com|http://gclimbing.com]\n\n" +
			"# Numbered point\n" +
			"# Numbered point\n" +
			"# /Numbered *point*/\n\n" +
			"And some more text\n\n" +
			"");
	}

}
