package net.davidtanzer.exampilistic.server.generator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import net.davidtanzer.exampilistic.ReadableJavaNames;
import net.davidtanzer.exampilistic.WikiPage;
import net.davidtanzer.exampilistic.server.pages.wiki.WikiContentPart;

public class CodeGenerator {
	private final List<PartGenerator> partGenerators = new ArrayList<>();
	
	public void generate(final String packageName, final String title, final List<WikiContentPart> contentParts) {
		File baseDirectory = new File("src/spec/java");
		if(!baseDirectory.exists()) {
			baseDirectory.mkdirs();
		}
		
		String packageDirectoryName = packageName.replaceAll("\\.", "/");
		File packageDirectory = new File(baseDirectory, packageDirectoryName);
		if(!packageDirectory.exists()) {
			packageDirectory.mkdirs();
		}
		
		for(WikiContentPart part : contentParts) {
			partGenerators.add(PartGenerator.forPart(part, packageDirectory, packageName));
		}
		
		String className = ReadableJavaNames.toJavaName(title);
		String javaFileName = className+".java";
		try {
			PrintWriter resultWriter = new PrintWriter(new File(packageDirectory, javaFileName));
			
			resultWriter.append("package ").append(packageName).println(";");
			resultWriter.println();
			
			resultWriter.println("import "+WikiPage.class.getName()+";");
			resultWriter.println();
			
			HashSet<String> importDirectives = new HashSet<>();
			for(PartGenerator partGenerator : partGenerators) {
				importDirectives.addAll(partGenerator.getImportDirectives());
			}
			for(String importDirective : importDirectives) {
				resultWriter.println(importDirective);
			}
			resultWriter.println();
			
			resultWriter.println("public class "+className+" extends WikiPage {");
			
			for(PartGenerator partGenerator : partGenerators) {
				partGenerator.generateMemberVariables(resultWriter);
			}
			
			resultWriter.println();
			resultWriter.println("\t@Override public void initializeContent() {");
			for(PartGenerator partGenerator : partGenerators) {
				partGenerator.generateConstructor(resultWriter);
			}
			resultWriter.println("\t}");

			for(PartGenerator partGenerator : partGenerators) {
				partGenerator.generateMethods(resultWriter);
			}
			
			resultWriter.println("}");
			
			resultWriter.close();
			
			for(PartGenerator partGenerator : partGenerators) {
				partGenerator.generateOtherFiles();
			}
		} catch (FileNotFoundException e) {
			throw new IllegalStateException("Could not write result", e);
		}
	}

}
