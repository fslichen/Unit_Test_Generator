package generator;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ObjectMocker {
private List<String> stringVocabulary;
	public ObjectMocker() {
		stringVocabulary = Arrays.asList("Donald Trump", "cat", "dog", "Abraham Lincoln");
	}
	
	public String capitalizeFirstCharacter(String string) {
		return string.substring(0, 1).toUpperCase() + string.substring(1);
	}
	
	public String fieldName(Method method) {// Getter or Setter
		String methodName = method.getName();
		if ((methodName.startsWith("set") || methodName.startsWith("get"))) {
			return methodName.substring(3, 4).toLowerCase() + methodName.substring(4);
		} else {
			System.out.println("The method is neither a getter or a setter.");
			return null;
		}
	}
	
	public <T> List<Method> getters(Class<T> clazz) throws Exception {
		Method[] methods = clazz.getMethods();
		List<Method> getters = new LinkedList<>();
		for (Method method : methods) {
			String methodName = method.getName();
			if (methodName.startsWith("get") && !"getClass".equals(methodName)) {
				if (method.getParameterCount() == 0) {
					getters.add(method);
				}
			}
		}
		return getters;
	}
	
	public Double mockDouble() {
		return (Math.random() < .5 ? 1 : -1) * Math.random() * Double.MAX_VALUE;
	}
	
	public Integer mockInt() {
		Double result = (Math.random() < .5 ? 1 : -1) * Math.random() * Integer.MAX_VALUE;
		return result.intValue();
	}
	
	@SuppressWarnings("unchecked")
	public <T> List<T> mockList(Method method, int parameterIndex) throws Exception {
		Class<?> clazz = typeArguments(method, parameterIndex).get(0);
		List<T> list = new LinkedList<>();
		for (int i = 0; i < 2; i++) {
			list.add((T) mockObject(clazz));
		}
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public <T, V> Map<T, V> mockMap(Method method, int parameterIndex) throws Exception {
		List<Class<?>> typeArguments = typeArguments(method, parameterIndex);
		Class<?> keyClass = typeArguments.get(0);
		Class<?> valueClass = typeArguments.get(1);
		Map<T, V> map = new HashMap<>();
		for (int i = 0; i < 2; i++) {
			map.put((T) mockObject(keyClass), (V) mockObject(valueClass));
		}
		return map;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T mockObject(Class<T> clazz) throws Exception {
		if (clazz == int.class || clazz == Integer.class) {
			return (T) mockInt();
		} else if (clazz == String.class) {
			return (T) mockString();
		} else if (clazz == double.class || clazz == Double.class) {
			return (T) mockDouble();
		} else if (clazz == List.class) {
			System.out.println("Unable to mock list due to type erasure in JVM.");
			return null;
		} else if (clazz == Map.class) {
			System.out.println("Unable to mock map due to type erasure in JVM.");
			return null;
		} else {
			return mockPojo(clazz);
		}
	}
	
	public <T> T mockPojo(Class<T> clazz) throws Exception {
		T t = clazz.newInstance();
		for (Method setter : setters(clazz)) {
			Class<?> parameterType = setter.getParameterTypes()[0];
			if (parameterType == String.class) {
				setter.invoke(t, mockString());
			} else if (parameterType == int.class || parameterType == Integer.class) {
				setter.invoke(t, mockInt());
			} else if (parameterType == List.class) {
				setter.invoke(t, mockList(setter, 0));
			} else if (parameterType == Map.class) {
				setter.invoke(t, mockMap(setter, 0));
			} else {// Invoke recursion if the field is also a POJO.
				setter.invoke(t, mockPojo(parameterType));
			}
		}
		return t;
	}
	
	public String mockString() {// Make it smart.
		return stringVocabulary.get(((Double) Math.floor(Math.random() * stringVocabulary.size())).intValue());
	}
	
	public <T> List<Method> setters(Class<T> clazz) throws Exception {
		Method[] methods = clazz.getMethods();
		List<Method> setters = new LinkedList<>();
		for (Method method : methods) {
			if (method.getName().startsWith("set") && method.getParameterCount() == 1) {
				setters.add(method);
			}
		}
		return setters;
	}
		
	public List<Class<?>> typeArguments(Method method, int parameterIndex) throws NoSuchMethodException, SecurityException, ClassNotFoundException {
		Type[] types = method.getGenericParameterTypes();
		String typeName = types[parameterIndex].getTypeName();
		List<Class<?>> typeArguments = new LinkedList<>();
		for (String typeArgumentName : typeName.substring(typeName.indexOf("<") + 1, typeName.indexOf(">")).split(",")) {
			Class<?> clazz = Class.forName(typeArgumentName.replace(" ", ""));
			typeArguments.add(clazz);
		}
		return typeArguments;
	}
}
