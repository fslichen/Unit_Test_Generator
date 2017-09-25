package generator;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

public class Pointer {
	public static String genericParameterTypeName(Method method, int parameterIndex) {
		return method.getGenericParameterTypes()[parameterIndex].getTypeName();
	}
	
	public static String concatenatedTypeArgumentNames(String genericTypeName) {
		return genericTypeName.substring(genericTypeName.indexOf("<") + 1, genericTypeName.lastIndexOf(">"));
	}
	
	public static String fieldName(Method method) {// Getter or Setter
		String methodName = method.getName();
		if ((methodName.startsWith("set") || methodName.startsWith("get"))) {
			return methodName.substring(3, 4).toLowerCase() + methodName.substring(4);
		} else {
			System.out.println("The method is neither a getter or a setter.");
			return null;
		}
	}
	
	public static int level(char character, char openCharacter, char endCharacter, int level) {
		if (character == openCharacter) {
			level++;
		} else if (character == endCharacter) {
			level--;
		}
		return level;
	}
	
	public static <T> List<Method> setters(Class<T> clazz) throws Exception {
		Method[] methods = clazz.getMethods();
		List<Method> setters = new LinkedList<>();
		for (Method method : methods) {
			if (method.getName().startsWith("set") && method.getParameterCount() == 1) {
				setters.add(method);
			}
		}
		return setters;
	}
	
	public static List<String> typeArgumentNames(String genericTypeName) {
		int level = 0;
		String concatenatedTypeArgumentNames = concatenatedTypeArgumentNames(genericTypeName).replace(", ", ",");
		List<Integer> separatorIndexes = new LinkedList<>();
		separatorIndexes.add(-1);
		for (int i = 0; i < concatenatedTypeArgumentNames.length(); i++) {
			char character = concatenatedTypeArgumentNames.charAt(i);
			level = level(character, '<', '>', level);
			if (level == 0 && character == ',') {
				separatorIndexes.add(i);
			}
		}
		separatorIndexes.add(concatenatedTypeArgumentNames.length());
		List<String> typeArgumentNames = new LinkedList<>();
		for (int i = 0; i < separatorIndexes.size() - 1; i++) {
			typeArgumentNames.add(concatenatedTypeArgumentNames.substring(separatorIndexes.get(i) + 1, separatorIndexes.get(i + 1)).replace(",", ", "));
		}
		return typeArgumentNames;
	}
	
	public static String instanceName(Class<?> clazz) {
		return Lang.lowerFirstCharacter(clazz.getSimpleName());
	}
	
	public static String simpleReturnTypeName(Method method, CodeWriter codeWriter) {
		return simpleTypeName(method.getGenericReturnType().getTypeName(), codeWriter);
	}
	
	public static String simpleParameterTypeName(Method method, int parameterIndex, CodeWriter codeWriter) {
		return simpleTypeName(method.getGenericParameterTypes()[parameterIndex].getTypeName(), codeWriter);
	}
	
	public static String simpleTypeName(String genericTypeName, CodeWriter codeWriter) {
		int classNameStartIndex = 0;
		int length = genericTypeName.length();
		String simpleGenericTypeName = genericTypeName;
		for (int i = 0; i < length; i++) {
			char iCharacter = genericTypeName.charAt(i);
			if (Lang.isUpperCase(iCharacter)) {
				for (int j = i + 1; j < length; j++) {
					char jCharacter = genericTypeName.charAt(j);
					if (jCharacter == ',' || jCharacter == '<' || jCharacter == '>' || j == length - 1) {
						Class<?> clazz = null;
						String subGenericTypeName = genericTypeName.substring(classNameStartIndex, (j == length - 1 && jCharacter != ',' && jCharacter != '<' && jCharacter != '>') ? j + 1 : j);
						try {
							clazz = Class.forName(subGenericTypeName);
							codeWriter.writeImport(clazz);
							simpleGenericTypeName = simpleGenericTypeName.replace(clazz.getName(), clazz.getSimpleName());
						} catch (ClassNotFoundException e) {
							codeWriter.patchTypeParameterToMethod(subGenericTypeName);
						}
						break;
					}
				}
			} else if (iCharacter == ' ' || iCharacter == '<') {
				classNameStartIndex = i + 1;
			}
		}
		return simpleGenericTypeName;
	}
	
	public static String concatenateParameterTypeSimpleNames(Method method) {
		StringBuilder result = new StringBuilder();
		for (Class<?> parameterType : method.getParameterTypes()) {
			result.append(parameterType.getSimpleName());
		}
		return result.toString();
	}
	
	public static String returnTypeSimpleName(Method method) {
		Class<?> returnType = method.getReturnType();
		if (returnType.isPrimitive()) {
			return "Primitive" + Lang.upperFirstCharacter(returnType.getSimpleName());
		}
		return returnType.getSimpleName();
	}
}
