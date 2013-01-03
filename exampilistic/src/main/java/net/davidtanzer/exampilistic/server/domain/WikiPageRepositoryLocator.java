package net.davidtanzer.exampilistic.server.domain;

import java.io.Serializable;

public class WikiPageRepositoryLocator implements Serializable {
	private static final long serialVersionUID = 1L;

	public WikiPageRepository get() {
		return WikiPageRepository.getInstance();
	}

}
