package generator;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class CodeWriter {
	private Integer indentCount;
	private List<String> codes;
	
	public CodeWriter() {
		indentCount = 0;
		codes = new LinkedList<>();
	}

	public List<String> getCodes() {
		return codes;
	}
	
	public void writeRightCurlyBrace() {
		codes.add("}");
	}
	
	public void writePackage(Class<?> clazz) {
		String className = clazz.getName();
		codes.add(0, String.format("package %s;", className.substring(0, clazz.getName().lastIndexOf("."))));
	}
	
	public void writeImport(String code) {
		if (!codes.contains(code)) {
			if (codes.size() == 0) {
				codes.add(0, code);
			} else {
				codes.add(1, code);
			}
		}
	}
	
	public List<Class<?>> classesIgnoringImport() {
		List<Class<?>> classes = new LinkedList<>();
		classes.addAll(Arrays.asList(byte.class, Byte.class, 
				short.class, Short.class,
				int.class, Integer.class, 
				long.class, Long.class, 
				float.class, Float.class, 
				double.class, Double.class, 
				char.class, Character.class, 
				boolean.class, Boolean.class));
		return classes;
	}
	
	public void writeImport(Class<?> clazz) {
		if (!classesIgnoringImport().contains(clazz)) {
			writeImport(String.format("import %s;", clazz.getName()));
		}
	}
	
	public void writeClass(Class<?> clazz, String suffix) {
		codes.add(String.format("public class %s%s {", clazz.getSimpleName(), suffix));
	}
	
	public void writeClass(Class<?> clazz, String suffix, Class<?> extendedClass) {
		writeImport(extendedClass);
		codes.add(String.format("public class %s%s extends %s {", clazz.getSimpleName(), suffix, extendedClass.getSimpleName()));
	}
	
	public void writeAnnotation(Class<?> clazz) {
		writeImport(clazz);
		codes.add("@" + clazz.getSimpleName());
	}
	
	public void writeField(Class<?> clazz) {
		writeImport(clazz);
		String simpleClassName = clazz.getSimpleName();
		String fieldName = lowerFirstCharacter(simpleClassName);
		codes.add(String.format("private %s %s;", simpleClassName, fieldName));
	}
	
	public void writeBlankLine() {
		codes.add("\n");
	}
	
	public String upperFirstCharacter(String string) {
		return string.substring(0, 1).toUpperCase() + string.substring(1);
	}
	
	public String lowerFirstCharacter(String string) {
		return string.substring(0, 1).toLowerCase() + string.substring(1);
	}
	
	public void writeMethod(Method method, String prefix, Object suffix) {
		String methodSignature = String.format("public void %s%s%s() {", prefix, upperFirstCharacter(method.getName()), suffix);
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
	
	public void writeCode(String code) {
		if (code.startsWith("package")) {
			codes.add(0, code);
		} else if (code.startsWith("import")) {
			writeImport(code);
		} else {
			codes.add(code);
		}
	}
	
	public void writeCodes(List<String> codesToBeWritten) {
		for (String codeToBeWritten : codesToBeWritten) {
			writeCode(codeToBeWritten);
		}
	}
	
	public void generateIndent(StringBuilder completeCodes) {
		for (int i = 0; i < indentCount; i++) {
			for (int j = 0; j < 4; j++) {
				completeCodes.append(" ");
			}
		}
	}
	
	public void generateCode(String code, StringBuilder completeCodes) {
		String trimedCode = code.trim();
		if (trimedCode.endsWith("{")) {
			generateIndent(completeCodes);
			indentCount++;
		} else if (trimedCode.endsWith("}")) {
			indentCount--;
			generateIndent(completeCodes);
		} else {
			generateIndent(completeCodes);
		}
		completeCodes.append(trimedCode).append("\n");
	}
	
	public String generateCodes() {
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
			generateCode(sortedCode, completeCodes);
		}
		return completeCodes.toString();
	}
}
