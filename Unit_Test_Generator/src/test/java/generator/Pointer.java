package generator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import generator.pojo.ControllerMethodPojo;

@Controller
public class Pointer {
	public static Class<?> classAnnotationType(Class<?> clazz) {
		for (Annotation annotation : clazz.getAnnotations()) {
			if (annotation.annotationType() == Controller.class || annotation.annotationType() == RestController.class) {
				return Controller.class;
			} else if (annotation.annotationType() == Service.class) {
				return Service.class;
			} else if (annotation.annotationType() == Repository.class || clazz.getSimpleName().endsWith("Mapper")) {
				return Repository.class;
			} else if (annotation.annotationType() == Component.class) {
				return Component.class;
			}
		}
		return null;
	}
	
	public static String concatenatedTypeArgumentNames(String genericTypeName) {
		return genericTypeName.substring(genericTypeName.indexOf("<") + 1, genericTypeName.lastIndexOf(">"));
	}
	
	public static String concatenateParameterTypeSimpleNames(Method method) {
		StringBuilder result = new StringBuilder();
		for (Class<?> parameterType : method.getParameterTypes()) {
			result.append(parameterType.getSimpleName());
		}
		return result.toString();
	}
	
	public static ControllerMethodPojo controllerMethodPojo(Method method) {
		ControllerMethodPojo pojo = new ControllerMethodPojo();
		RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
		if (requestMapping != null) {
			pojo.setRequestPath(requestMapping.value()[0]);
			pojo.setRequestMethod(requestMapping.method()[0]);
			return pojo;
		}
		GetMapping getMapping = method.getAnnotation(GetMapping.class);
		if (getMapping != null) {
			pojo.setRequestPath(getMapping.value()[0]);
			pojo.setRequestMethod(RequestMethod.GET);
		}
		PostMapping postMapping = method.getAnnotation(PostMapping.class);
		if (postMapping != null) {
			pojo.setRequestPath(postMapping.value()[0]);
			pojo.setRequestMethod(RequestMethod.POST);
		}// TODO Add supports for PATCH, PUT
		pojo.setReturnType(method.getReturnType());
		return pojo;
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
	
	public static String genericParameterTypeName(Method method, int parameterIndex) {
		return method.getGenericParameterTypes()[parameterIndex].getTypeName();
	}
	
	public static String instanceName(Class<?> clazz) {
		return Lang.lowerFirstCharacter(clazz.getSimpleName());
	}
	
	public static boolean isClass(String code) {
		return code.contains(" class ");
	}
	
	public static boolean isMethod(String code) {
		code = code.trim();
		if (!isClass(code)) {
			return code.endsWith("{") && (code.startsWith("public") || code.startsWith("private") || code.startsWith("protected"));
		}
		return false;
	}
	
	public static int level(char character, char openCharacter, char endCharacter, int level) {
		if (character == openCharacter) {
			level++;
		} else if (character == endCharacter) {
			level--;
		}
		return level;
	}
	
	public static String returnTypeSimpleName(Method method) {
		Class<?> returnType = method.getReturnType();
		if (returnType.isPrimitive()) {
			return "Primitive" + Lang.upperFirstCharacter(returnType.getSimpleName());
		}
		return returnType.getSimpleName();
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
	
	public static String simpleParameterTypeName(Method method, int parameterIndex, CodeWriter codeWriter) {
		return simpleTypeName(method.getGenericParameterTypes()[parameterIndex].getTypeName(), codeWriter);
	}
	
	public static String simpleReturnTypeName(Method method, CodeWriter codeWriter) {
		return simpleTypeName(method.getGenericReturnType().getTypeName(), codeWriter);
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
}
