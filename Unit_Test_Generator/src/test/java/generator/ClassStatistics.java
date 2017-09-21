package generator;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;

public class ClassStatistics {
	public static boolean isMethodStartLine(String code) {
		code = code.trim();
		return !code.startsWith("public class") && (code.startsWith("public") || code.startsWith("protected") || code.startsWith("private"));// TODO Also consider the protect case without access modifier.
	}
	
	public static Map<String, Integer> useCaseCountByMethod(File file) throws IOException {
		int useCaseCount = 1;
		String methodName = null;
		Map<String, Integer> useCaseCountByMethod = new LinkedHashMap<>();
		Set<String> conditionStatements = new HashSet<>(Arrays.asList("if (", "if(", "else if (", "else if(", "else {", "else{"));
		for (String code : FileUtils.readLines(file, "UTF-8")) {
			code = code.trim();
			if (isMethodStartLine(code)) {// Method Begin
				useCaseCount = 1;
				int leftBracketIndex = code.indexOf("(");
				for (int i = leftBracketIndex - 1; i >= 0; i--) {
					if (code.charAt(i) == ' ') {
						methodName = code.substring(i + 1, leftBracketIndex);
						break;
					}
				}
			} else if (code.endsWith("{")) {
				for (String conditionStatement : conditionStatements) {
					if (code.contains(conditionStatement)) {// Conditional Statement
						useCaseCount++;
						break;
					}
				}
			} else if (code.endsWith("}")) {
				useCaseCountByMethod.put(methodName, useCaseCount);
			}
		}
		return useCaseCountByMethod;
	}
}
