package generator.codeWriter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;

import generator.Lang;
import generator.codeWriter.pojo.IClass;
import generator.codeWriter.pojo.IField;
import generator.codeWriter.pojo.IHeader;
import generator.codeWriter.pojo.IMethod;

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

	public String combineCodes() {
		StringBuilder completeCodes = new StringBuilder();
		List<String> sortedCodes = new LinkedList<>();
		// Package
		sortedCodes.add(iHeader.getPackageName());
		// Import
		for (String className : iHeader.getClassNames()) {
			sortedCodes.add(className);
		}
		// Class
		String classCode = String.format("public class %s ", simpleClassNameWithSurfix(iClass.getClazz(), iClass.getSurfix()));
		Class<?> extendedClass = iClass.getExtendedClass();
		if (extendedClass != null) {
			classCode += String.format("extends %s", extendedClass.getSimpleName());
		}
		sortedCodes.add(classCode + " {");
		// Field
		for (IField iField : iFields.values()) {
			String fieldName = iField.getFieldType().getSimpleName();
			for (Class<?> annotationType : iField.getAnnotationTypes()) {
				sortedCodes.add("@" + annotationType.getSimpleName());
			}
			sortedCodes.add(String.format("private %s %s;", fieldName, Lang.lowerFirstCharacter(fieldName)));
			sortedCodes.add("");// Field End
		}
		// Method
		for (IMethod iMethod : iMethods.values()) {
			for (Class<?> annotationType : iMethod.getAnnotationTypes()) {
				sortedCodes.add("@" + annotationType.getSimpleName());
			}
			String exceptionString = "";
			Class<?> exceptionType = iMethod.getExceptionType();
			if (exceptionType != null) {
				exceptionString = String.format("throws %s", exceptionType.getSimpleName()); 
			}
			String typeParameterName = iMethod.getTypeParameterName() != null ? "<" + iMethod.getTypeParameterName() + ">" : "";
			String methodSignature = String.format("public %s void %s%s%s() %s {", typeParameterName, iMethod.getPrefix(), upperFirstCharacter(iMethod.getMethod().getName()), iMethod.getSuffix(), exceptionString);
			sortedCodes.add(methodSignature);
			for (String code : iMethod.getCodes()) {
				sortedCodes.add(code);
			}
			sortedCodes.addAll(Arrays.asList("}", ""));// Method End
		}
		sortedCodes.add("}");// Class End
		for (String sortedCode : sortedCodes) {
			String trimedCode = sortedCode.trim();
			if (trimedCode.endsWith("{")) {
				updateIndent(completeCodes);
				indentCount++;
			} else if (trimedCode.endsWith("}")) {
				indentCount--;
				updateIndent(completeCodes);
			} else {
				updateIndent(completeCodes);
			}
			completeCodes.append(trimedCode).append("\n");
		}
		return completeCodes.toString();
	}

	public void updateIndent(StringBuilder completeCodes) {
		for (int i = 0; i < indentCount; i++) {
			for (int j = 0; j < 4; j++) {
				completeCodes.append(" ");
			}
		}
	}

	public IClass getiClass() {
		return iClass;
	}

	public Map<Class<?>, IField> getiFields() {
		return iFields;
	}

	public IHeader getiHeader() {
		return iHeader;
	}

	public Map<Method, IMethod> getiMethods() {
		return iMethods;
	}

	public Integer getIndentCount() {
		return indentCount;
	}

	public void importAnnotations(Class<?>... annotationTypes) {
		if (annotationTypes != null) {
			for (Class<?> annotationType : annotationTypes) {
				writeImport(annotationType);
			}
		}
	}

	public void merge(CodeWriter codeWriter) {
		this.iHeader.getClassNames().addAll(codeWriter.getiHeader().getClassNames());
		this.iMethods.putAll(codeWriter.getiMethods());
		this.iFields.putAll(codeWriter.getiFields());
	}


	
	public void patchTypeParameterToMethod(Method method, String typeParameterName) {
		IMethod iMethod = iMethods.get(method);
		iMethod.setTypeParameterName(typeParameterName);
	}
	
	public void setiClass(IClass iClass) {
		this.iClass = iClass;
	}
	
	public void setiFields(Map<Class<?>, IField> iFields) {
		this.iFields = iFields;
	}
	
	public void setiHeader(IHeader iHeader) {
		this.iHeader = iHeader;
	}
	
	public void setiMethods(Map<Method, IMethod> iMethods) {
		this.iMethods = iMethods;
	}
	
	public void setIndentCount(Integer indentCount) {
		this.indentCount = indentCount;
	}
	
	public String simpleClassNameWithSurfix(Class<?> clazz, String suffix) {
		return clazz.getSimpleName() + Lang.upperFirstCharacter(suffix);
	}
	
	public String upperFirstCharacter(String string) {
		return string.substring(0, 1).toUpperCase() + string.substring(1);
	}
	
	public void writeAnnotation(Method method, Class<?>... annotationTypes) {
		IMethod iMethod = iMethods.get(method);
		if (iMethod == null) {
			System.out.println(String.format("%s should not be null", method));
			iMethod = new IMethod();
		}
		iMethod.getAnnotationTypes().addAll(Arrays.asList(annotationTypes));
	}
	
	public void writeClass(Class<?> clazz, String surfix, Class<?> extendedClass, List<Class<?>> implementedInterfaces, Class<?>... annotationTypes) {
		iClass = new IClass();
		iClass.setClazz(clazz);
		iClass.setSurfix(surfix);
		iClass.setExtendedClass(extendedClass);
		writeImport(iClass.getExtendedClass());
		iClass.setImplementedInterfaces(implementedInterfaces);
		if (implementedInterfaces != null) {
			for (Class<?> implementedInterface : implementedInterfaces) {
				writeImport(implementedInterface);
			}
		}
		if (annotationTypes != null) {
			iClass.setAnnotationTypes(Arrays.asList(annotationTypes));
			importAnnotations(annotationTypes);
		}
	}
	
	public void writeCode(Method method, String code) {
		IMethod iMethod = iMethods.get(method);
		if (iMethod == null) {
			System.out.println(String.format("%s does not exist.", method.getName()));
			iMethod = new IMethod();
		}
		iMethod.getCodes().add(code);
	}
	
	public void writeField(Class<?> fieldType, Class<?>... annotationTypes) {
		if (iFields.containsKey(fieldType)) {
			return;
		}
		writeImport(fieldType);
		IField iField = new IField();
		iField.setFieldType(fieldType);
		iField.setAnnotationTypes(Arrays.asList(annotationTypes));
		importAnnotations(annotationTypes);
		iFields.put(fieldType, iField);
	}
	
	public void writeImport(Class<?> clazz) {
		if (clazz != null && !classesIgnoringImport().contains(clazz)) {
			String code = String.format("import %s;", clazz.getName());
			List<String> classNames = iHeader.getClassNames();
			if (!classNames.contains(code)) {
				classNames.add(code);
			}
		}
	}
	
	public void writeMethod(Method method, String prefix, String suffix, Class<?> exceptionType, Class<?>... annotationTypes) {
		IMethod iMethod = new IMethod();
		iMethod.setMethod(method);
		iMethod.setAnnotationTypes(Arrays.asList(annotationTypes));
		iMethod.setExceptionType(exceptionType);
		iMethod.setPrefix(prefix);
		iMethod.setSuffix(suffix);
		importAnnotations(annotationTypes);
		iMethods.put(method, iMethod);
	}
	
	public void writeMockito4InvokingComponentMethod(Method currentMethod, Field autowiredComponent) {
		writeStaticImport(Mockito.class);
		StringBuilder parameterValuesInString = new StringBuilder();
		writeStaticImport(Mockito.class);
		writeField(autowiredComponent.getType(), MockBean.class);
		for (Method method : autowiredComponent.getType().getDeclaredMethods()) {
			for (int i = 0; i < method.getParameterCount(); i++) {
				parameterValuesInString.append("null, ");
			}
			writeCode(currentMethod, String.format("when(%s.%s(%s)).thenReturn(%s)", Lang.lowerFirstCharacter(autowiredComponent.getType().getSimpleName()), method.getName(), parameterValuesInString.length() > 2 ? parameterValuesInString.substring(0, parameterValuesInString.length() - 2) : "", "null"));
		}
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
