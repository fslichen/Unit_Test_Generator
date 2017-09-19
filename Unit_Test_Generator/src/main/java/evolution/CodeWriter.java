package evolution;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

public class CodeWriter {
	private int indentCount;
	
	public void writePackage(Class<?> clazz, List<String> codes) {
		String className = clazz.getName();
		codes.add(0, String.format("package %s;", className.substring(0, clazz.getName().lastIndexOf("."))));
	}
	
	public void writeImport(Class<?> clazz, List<String> codes) {
		String code = String.format("import %s;", clazz.getName());
		if (!codes.contains(code)) {
			if (codes.size() == 0) {
				codes.add(0, code);
			} else {
				codes.add(1, code);
			}
		}
	}
	
	public String writeClass(Class<?> clazz, String suffix) {
		return String.format("public class %s%s {", clazz.getSimpleName(), suffix);
	}
	
	public void writeAnnotation(Class<?> clazz, List<String> codes) {
		writeImport(clazz, codes);
		codes.add("@" + clazz.getSimpleName());
	}
	
	public String writeField(Class<?> clazz, List<String> codes) {
		String simpleClassName = clazz.getSimpleName();
		String fieldName = simpleClassName.substring(0, 1).toLowerCase() + simpleClassName.substring(1);
		codes.add(String.format("private %s %s;", simpleClassName, fieldName));
		return fieldName;
	}
	
	public void writeBlankLine(List<String> codes) {
		codes.add("\n");
	}
	
	public String upperFirstCharacter(String string) {
		return string.substring(0, 1).toUpperCase() + string.substring(1);
	}
	
	public void writeMethod(Method method, String prefix, List<String> codes) {
		String methodSignature = String.format("public void test%s() {", upperFirstCharacter(method.getName()));
		for (int i = codes.size() - 1; i >= 0; i--) {
			String code = codes.get(i);
			if (code.equals("\n")) {
				break;
			} else if (code.startsWith("@")) {
				codes.add(i + 1, methodSignature);
				return;
			}
		}
		codes.add(methodSignature);
	}
	
	public void writeIndent(StringBuilder code) {
		for (int i = 0; i < indentCount; i++) {
			for (int j = 0; j < 4; j++) {
				code.append(" ");
			}
		}
	}
	
	public void writeCode(String code, StringBuilder codes) {
		String trimedCode = code.trim();
		if (trimedCode.endsWith("{")) {
			writeIndent(codes);
			indentCount++;
		} else if (trimedCode.endsWith("}")) {
			indentCount--;
			writeIndent(codes);
		} else {
			writeIndent(codes);
		}
		codes.append(trimedCode).append("\n");
	}
	
	public String writeCodes(List<String> codes) {
		StringBuilder completeCodes = new StringBuilder();
		List<String> sortedCodes = new LinkedList<>();
		List<String> remainingCodes = new LinkedList<>();
		for (String code : codes) {
			if (code == null) {
				remainingCodes.add("\n");
			} else if (code.startsWith("import")) {
				sortedCodes.add(code);
			} else if (code.startsWith("package")) {
				sortedCodes.add(0, code);
			} else {
				remainingCodes.add(code);
			}
		}
		sortedCodes.addAll(remainingCodes);
		for (String sortedCode : sortedCodes) {
			writeCode(sortedCode, completeCodes);
		}
		return completeCodes.toString();
	}
}
