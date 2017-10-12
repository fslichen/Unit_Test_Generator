package generator.codeWriter;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import generator.Lang;
import generator.codeWriter.pojo.AnnotationArgument;
import generator.codeWriter.pojo.IClass;
import generator.codeWriter.pojo.IField;
import generator.codeWriter.pojo.IHeader;
import generator.codeWriter.pojo.IMethod;
import generator.util.map.MultiMap;

public class CodeWriter {
	private Integer indentCount;
	private IHeader iHeader;
	private IClass iClass;
	private Map<Class<?>, IField> iFields;
	private Map<Method, IMethod> iMethods;
	
	public CodeWriter() {
		indentCount = 0;
		iFields = new LinkedHashMap<>();
		iMethods = new LinkedHashMap<>();
		iHeader = new IHeader();
	}
	
	public List<Class<?>> classesDontNeedToBeImported() {
		List<Class<?>> classes = new LinkedList<>();
		classes.addAll(Arrays.asList(byte.class, Byte.class, 
				short.class, Short.class,
				int.class, Integer.class, 
				long.class, Long.class, 
				float.class, Float.class, 
				double.class, Double.class, 
				char.class, Character.class, 
				boolean.class, Boolean.class, Object.class));
		return classes;
	}

	public String generateCodes() {
		List<String> codes = new LinkedList<>();
		// Package
		codes.add(iHeader.getPackageName());
		// Import
		for (String className : iHeader.getClassNames()) {
			codes.add(className);
		}
		// Class
		String classCode = String.format("public class %s ", simpleClassNameWithSuffix(iClass.getClazz(), iClass.getSuffix()));
		Class<?> extendedClass = iClass.getExtendedClass();// Extended Class
		if (extendedClass != null) {
			classCode += String.format("extends %s ", extendedClass.getSimpleName());
		}
		List<Class<?>> implementedInterfaces = iClass.getImplementedInterfaces();// Implemented Interfaces
		if (implementedInterfaces != null && !implementedInterfaces.isEmpty()) {
			classCode += "implements ";
			for (Class<?> implementedInterface : iClass.getImplementedInterfaces()) {
				classCode += implementedInterface.getSimpleName() + ", ";
			}
			classCode = classCode.substring(0, classCode.length() - 2) + " ";
		}
		codes.add(classCode + "{");// Class Begin
		// Field
		for (IField iField : iFields.values()) {
			for (Class<?> annotationType : iField.getAnnotationTypes()) {// Field Annotations
				codes.add("@" + annotationType.getSimpleName());
			}
			String fieldName = iField.getFieldType().getSimpleName();// Field Definition
			codes.add(String.format("private %s %s;", fieldName, Lang.lowerFirstCharacter(fieldName)));
			codes.add("");// Field End
		}
		// Method
		for (IMethod iMethod : iMethods.values()) {
			iMethod.getAnnotationTypeAndArgumentsMap().forEach((annotationType, annotationArguments) -> {
				StringBuilder annotationValuesBuilder = new StringBuilder();
				Optional.ofNullable(annotationArguments).ifPresent(x -> x.forEach(annotationArgument -> {
					annotationValuesBuilder.append(annotationArgument.getCode() + ", ");
				}));
				codes.add("@" + annotationType.getSimpleName() + Optional.of(annotationValuesBuilder).map(x -> Lang.trimEndingComma(x)).filter(x -> !x.isEmpty()).map(x -> "(" + x + ")").orElse(""));
			});
			String methodCode = "public ";
			String typeParameterName = iMethod.getTypeParameterName();// Type Parameter
			if (typeParameterName != null) {
				methodCode += "<" + iMethod.getTypeParameterName() + "> ";
			}
			methodCode += String.format("void %s%s%s() ", iMethod.getPrefix(), Lang.upperFirstCharacter(iMethod.getMethod().getName()), iMethod.getSuffix());// Method Begin
			Class<?> exceptionType = iMethod.getExceptionType();
			if (exceptionType != null) {
				methodCode += "throws " + exceptionType.getSimpleName() + " ";
			}
			codes.add(methodCode + "{");
			for (String code : iMethod.getCodes()) {
				codes.add(code);
			}
			codes.addAll(Arrays.asList("}", ""));// Method End
		}
		codes.add("}");// Class End
		// Combine Codes
		StringBuilder result = new StringBuilder();
		for (String code : codes) {
			String trimedCode = code.trim();
			if (trimedCode.endsWith("{")) {
				writeIndent(result);
				indentCount++;
			} else if (trimedCode.endsWith("}")) {
				indentCount--;
				writeIndent(result);
			} else {
				writeIndent(result);
			}
			result.append(trimedCode).append("\n");
		}
		return result.toString();
	}

	public IClass getIClass() {
		return iClass;
	}

	public Map<Class<?>, IField> getIFields() {
		return iFields;
	}

	public IHeader getIHeader() {
		return iHeader;
	}

	public Map<Method, IMethod> getIMethods() {
		return iMethods;
	}

	public Integer getIndentCount() {
		return indentCount;
	}

	public void importAnnotations(Class<?>... annotationTypes) {// TODO Change the method name
		if (annotationTypes != null) {
			for (Class<?> annotationType : annotationTypes) {
				writeImport(annotationType);
			}
		}
	}
	
	public void importClasses(Collection<Class<?>> classes) {// TODO Determine whether ifPresent is needed.
		Optional.ofNullable(classes).ifPresent(x -> x.forEach(y -> writeImport(y)));
	}

	public void patchTypeParameter(Method method, String typeParameterName) {
		IMethod iMethod = iMethods.get(method);
		if (iMethod == null) {
			System.out.println(String.format("%s is not registered.", method));
			return;
		}
		iMethod.setTypeParameterName(typeParameterName);
	}
	
	public void setIClass(IClass iClass) {
		this.iClass = iClass;
	}
	
	public void setIFields(Map<Class<?>, IField> iFields) {
		this.iFields = iFields;
	}
	
	public void setIHeader(IHeader iHeader) {
		this.iHeader = iHeader;
	}
	
	public void setIMethods(Map<Method, IMethod> iMethods) {
		this.iMethods = iMethods;
	}
	
	public void setIndentCount(Integer indentCount) {
		this.indentCount = indentCount;
	}
	
	public String simpleClassNameWithSuffix(Class<?> clazz, String suffix) {
		return clazz.getSimpleName() + Lang.upperFirstCharacter(suffix);
	}
	
	public void writeClass(Class<?> clazz, String suffix, Class<?> extendedClass, List<Class<?>> implementedInterfaces, Class<?>... annotationTypes) {
		if (iClass != null) {
			System.out.println(String.format("%s is already defined.", clazz.getName()));
			return;
		}
		iClass = new IClass();
		iClass.setClazz(clazz);
		iClass.setSuffix(suffix);
		// Extended Class
		if (extendedClass != null) {
			iClass.setExtendedClass(extendedClass);
			writeImport(extendedClass);
		}
		// Implemented Interfaces
		if (implementedInterfaces != null) {
			iClass.setImplementedInterfaces(implementedInterfaces);
			for (Class<?> implementedInterface : implementedInterfaces) {
				writeImport(implementedInterface);
			}
		}
		// Class Annotations
		if (annotationTypes != null) {
			iClass.setAnnotationTypes(Arrays.asList(annotationTypes));
			importAnnotations(annotationTypes);
		}
	}
	
	public void writeOutput(Method method, String code) {
		writeCode(method, String.format("System.out.println(%s);", code));
	}
	
	public void writeOutput(Method method, String prefix, String code, String suffix) {
		writeCode(method, String.format("System.out.println(%s + %s + %s);", "\"" + prefix.replace("'", "\\\"") + "\"", code, "\"" + suffix.replace("'", "\\\"") + "\""));
	}
	
	public void writeCode(Method method, String code) {
		IMethod iMethod = iMethods.get(method);
		if (iMethod == null) {
			System.out.println(String.format("%s is not registered.", method));
			return;
		}
		iMethod.getCodes().add(code);
	}
	
	public void writeField(Class<?> fieldType, Class<?>... annotationTypes) {
		if (iFields.containsKey(fieldType)) {
			return;
		}
		IField iField = new IField();
		iField.setFieldType(fieldType);
		writeImport(fieldType);
		if (annotationTypes != null) {
			iField.setAnnotationTypes(Arrays.asList(annotationTypes));
			importAnnotations(annotationTypes);
		}
		iFields.put(fieldType, iField);
	}
	
	public void writeImport(Class<?> clazz) {
		if (!clazz.isArray() && !classesDontNeedToBeImported().contains(clazz)) {
			String code = String.format("import %s;", clazz.getName());
			List<String> classNames = iHeader.getClassNames();
			if (!classNames.contains(code)) {
				classNames.add(code);
			}
		}
	}
	
	public void writeIndent(StringBuilder completeCodes) {
		for (int i = 0; i < indentCount; i++) {
			for (int j = 0; j < 4; j++) {
				completeCodes.append(" ");
			}
		}
	}
	
	public void writeMethod(Method method, String prefix, String suffix, Class<?> exceptionType, MultiMap<Class<?>, AnnotationArgument> annotationTypeAndArgumentsMap) {
		IMethod iMethod = new IMethod();
		iMethod.setMethod(method);
		iMethod.setPrefix(prefix);
		iMethod.setSuffix(suffix);
		iMethod.setExceptionType(exceptionType);
		if (annotationTypeAndArgumentsMap != null) {
			importClasses(annotationTypeAndArgumentsMap.keySet());			
			annotationTypeAndArgumentsMap.values().stream().filter(x -> x != null).flatMap(x -> x.stream()).filter(x -> x != null).map(x -> x.getClazz()).filter(x -> x != null).forEach(x -> writeImport(x));
			iMethod.setAnnotationTypeAndArgumentsMap(annotationTypeAndArgumentsMap);
		}
		iMethods.put(method, iMethod);
	}
	
	public void writePackage(Class<?> clazz) {
		String className = clazz.getName();
		iHeader.setPackageName(String.format("package %s;", className.substring(0, clazz.getName().lastIndexOf("."))));
	}
	
	public void writeStaticImport(Class<?> clazz) {
		String code = String.format("import static %s.*;", clazz.getName());
		List<String> classNames = iHeader.getClassNames();
		if (!classNames.contains(code)) {
			classNames.add(code);
		}
	}
}
