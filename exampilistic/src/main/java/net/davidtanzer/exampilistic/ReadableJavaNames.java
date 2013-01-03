package net.davidtanzer.exampilistic;

public class ReadableJavaNames {

	public static String asReadableName(final String name, final boolean toLowerCase) {
		if(name == null) {
			return null;
		}
		
		StringBuilder resultBuilder = new StringBuilder();
		for(int i=0; i<name.length(); i++) {
			char currentChar = name.charAt(i);
			if(currentChar >= 'A' && currentChar <= 'Z') {
				if(resultBuilder.length() > 0) {
					resultBuilder.append(" ");
				}
			}
			if(toLowerCase) {
				currentChar = Character.toLowerCase(currentChar);
			}
			resultBuilder.append(currentChar);
		}
		return resultBuilder.toString();
	}

	public static String toJavaName(final String readableName) {
		return toJavaName(readableName, true);
	}

	public static String toJavaName(final String readableName, final boolean firstUpper) {
		if(readableName == null) {
			return null;
		}
		if(readableName.length() == 0) {
			return "";
		}
		
		StringBuilder resultBuilder = new StringBuilder();
		
		boolean first = true;
		for(String part : readableName.split("[ \t\\-_\\.]")) {
			char firstCharacter = part.charAt(0);
			firstCharacter = Character.toUpperCase(firstCharacter);
			if(first) {
				if(!firstUpper) {
					firstCharacter = Character.toLowerCase(firstCharacter);
				}
			}
			resultBuilder.append(firstCharacter);
			if(part.length() > 1) {
				resultBuilder.append(part.substring(1));
			}
			first = false;
		}
		return resultBuilder.toString();
	}

}
