package net.davidtanzer.exampilistic.runner;

import java.io.File;
import java.io.FileFilter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import net.davidtanzer.exampilistic.WikiPage;

public class WikiPageFinder {
	private String classPath;
	private URL[] urls;

	public WikiPageFinder(final String classPath) throws IllegalClassPathException {
		try {
			this.urls = new URL[] { new URL("file:///"+classPath+"/") } ;
			this.classPath = classPath;
		} catch(MalformedURLException e) {
			throw new IllegalClassPathException("Could not open class path \""+classPath+"\"", e);
		}
	}
	
	public void forEach(final WikiPageProcessor processor) {
		File base = new File(classPath);
		
		if(base.isDirectory()) {
			forEachInFolder("", base, processor);
		} else {
			throw new UnsupportedOperationException("Jar files are not yet supported as class path ("+classPath+")");
		}
	}

	private void forEachInFolder(final String basePackage, final File base, final WikiPageProcessor processor) {
		File[] classFiles = base.listFiles(new FileFilter() {
			@Override
			public boolean accept(final File file) {
				return file.isFile() && file.getName().endsWith(".class");
			}
		});
		
		for(File classFile : classFiles) {
			String fileName = classFile.getName();
			String fullClassName = getQualifiedName(basePackage, fileName.substring(0, fileName.length()-".class".length()));
			if(processor.shouldProcess(classFile, fullClassName)) {
				try {
					URLClassLoader classLoader = new URLClassLoader(urls);
					Class<?> cl = classLoader.loadClass(fullClassName);
					if(WikiPage.class.isAssignableFrom(cl)) {
						processor.doPage(classFile, (Class<WikiPage>) cl);
					}
				} catch (ClassNotFoundException e) {
					throw new IllegalStateException("The class \""+fullClassName+"\" can not be found - Something seems to be wrong with the classpath!", e);
				}
			}
		}
		
		File[] subdirs = base.listFiles(new FileFilter() {
			@Override
			public boolean accept(final File file) {
				return file.isDirectory();
			}
		});
		for(File subdirectory : subdirs) {
			forEachInFolder(getQualifiedName(basePackage, subdirectory.getName()), subdirectory, processor);
		}
	}

	private String getQualifiedName(final String basePackage, final String name) {
		String fullName = basePackage;
		if(fullName.length() > 0) {
			fullName += ".";
		}
		fullName += name;
		return fullName;
	}
}
