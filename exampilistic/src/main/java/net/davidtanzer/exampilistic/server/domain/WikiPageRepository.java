package net.davidtanzer.exampilistic.server.domain;

import java.io.File;
import java.util.HashMap;

import net.davidtanzer.exampilistic.WikiPage;
import net.davidtanzer.exampilistic.runner.IllegalClassPathException;
import net.davidtanzer.exampilistic.runner.WikiPageFinder;
import net.davidtanzer.exampilistic.runner.WikiPageProcessor;

public class WikiPageRepository {
	private static WikiPageRepository instance = new WikiPageRepository();

	private final HashMap<String, WikiPageInfo> wikiPageCache = new HashMap<>();
	
	private WikiPageRepository() {
	}
	
	public static WikiPageRepository getInstance() {
		return instance;
	}

	public WikiPage findPage(final String pageName) {
		updateCaches();
		WikiPageInfo wikiPageInfo = wikiPageCache.get(pageName);
		if(wikiPageInfo == null) {
			return null;
		}
		return wikiPageInfo.pageInstance;
	}

	private void updateCaches() {
		try {
			WikiPageFinder finder = new WikiPageFinder(new File("./target").getAbsolutePath());
			finder.forEach(new WikiPageProcessor() {
				@Override
				public boolean shouldProcess(final File file, final String pageClassName) {
					return pageIsNotYetCached(pageClassName) || pageCacheEntryIsStale(file, pageClassName);
				}

				private boolean pageCacheEntryIsStale(final File file,
						final String pageClassName) {
					return file.lastModified() > wikiPageCache.get(pageClassName).lastModified;
				}

				private boolean pageIsNotYetCached(final String pageClassName) {
					return wikiPageCache.get(pageClassName)==null;
				}
				
				@Override
				public void doPage(final File file, final Class<WikiPage> pageClass) {
					WikiPageInfo info = new WikiPageInfo();
					info.lastModified = file.lastModified();
					info.pageClass = pageClass;
					try {
						info.pageInstance = pageClass.newInstance();
						info.pageInstance.initializeContent();
					} catch (InstantiationException | IllegalAccessException e) {
						throw new PageRepositoryRefreshException("Could not instantiate page class \""+pageClass.getName()+"\"", e);
					}
					wikiPageCache.put(pageClass.getName(), info);
				}
			});
		} catch (IllegalClassPathException e) {
			throw new PageRepositoryRefreshException("Could not refresh wiki page cache", e);
		}
	}

	private class WikiPageInfo {
		public long lastModified;
		public Class<? extends WikiPage> pageClass;
		public WikiPage pageInstance;
	}

}
