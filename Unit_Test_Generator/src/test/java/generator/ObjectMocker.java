//package generator;
//
//import java.lang.reflect.Method;
//import java.lang.reflect.Type;
//import java.math.BigDecimal;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.LinkedHashMap;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Map;
//import java.util.Random;
//import java.util.Set;
//import java.util.UUID;
//
//public class ObjectMocker {
//	private static List<String> stringVocabulary;
//	public static String capitalizeFirstCharacter(String string) {
//		return string.substring(0, 1).toUpperCase() + string.substring(1);
//	}
//	
//	public static BigDecimal mockBigDecimal() {
//		return new BigDecimal(mockDouble());
//	}
//	
//	public static Boolean mockBoolean() {
//		return new Random().nextBoolean();
//	}
//	
//	public static Byte mockByte() {
//		byte[] b = new byte[0];
//		new Random().nextBytes(b);
//		return b[0];
//	}
//	
//	public static Character mockCharacter() {
//		return Math.random() < .5 ? 'a' : 'z';
//	}
//	
//	public static Double mockDouble() {
//		return new Random().nextDouble();
//	}
//	
//	public static Float mockFloat() {
//		return new Random().nextFloat();
//	}
//	
//	public static Integer mockInt() {
//		return new Random().nextInt();
//	}
//	
//	@SuppressWarnings("unchecked")
//	public static <T> T mockInvokingMethod(Method method, Object currentInstance) throws Exception {
//		return (T) method.invoke(currentInstance, mockParameterValues(method));
//	}
//	
//	public static Long mockLong() {
//		return new Random().nextLong();
//	}
//	
//	public static Object[] mockParameterValues(Method method) throws Exception {
//		Object[] arguments = new Object[method.getParameterCount()];
//		int i = 0;
//		for (Class<?> parameterType : method.getParameterTypes()) {
//			ObjectMocker objectMocker = new ObjectMocker();
//			if (parameterType == List.class) {
//				arguments[i] = objectMocker.mockList(method, i);
//			} else if (parameterType == Map.class) {
//				arguments[i] = objectMocker.mockMap(method, i);
//			} else {
//				try {
//					arguments[i] = objectMocker.mockObject(parameterType);
//				} catch (Exception e) {
//					arguments[i] = null;
//				}
//			}
//			i++;
//		}
//		return arguments;
//	}
//	
//	@SuppressWarnings("unchecked")
//	public static <T> T mockReturnValue(Method method) throws Exception {
//		Class<?> returnType = method.getReturnType();
//		ObjectMocker objectMocker = new ObjectMocker();
//		if (returnType == List.class) {
//			return (T) Arrays.asList(objectMocker.mockObject(Pointer.returnTypeArguments(method).get(0)));
//		} else if (returnType == Map.class) {
//			List<Class<?>> typeArguments = Pointer.returnTypeArguments(method);
//			Map<Object, Object> map = new LinkedHashMap<>();
//			map.put(objectMocker.mockObject(typeArguments.get(0)), typeArguments.get(1));
//			return (T) map;
//		} else if (returnType == void.class || returnType == Void.class) {
//			return null;
//		} else {
//			return (T) objectMocker.mockObject(returnType);
//		}
//	}
//	
//	public static Short mockShort() {
//		return new Short(new Random().nextInt(32767) + "");
//	}
//	
//	public static String mockString() {// Make it smart.
//		return stringVocabulary.get(((Double) Math.floor(Math.random() * stringVocabulary.size())).intValue());
//	}
//	
//	public static List<Class<?>> parameterTypeArguments(Method method, int parameterIndex) throws NoSuchMethodException, SecurityException, ClassNotFoundException {
//		Type[] types = method.getGenericParameterTypes();
//		return Pointer.typeArguments(types[parameterIndex].getTypeName());
//	}
//	
//	private Set<Class<?>> mockedClasses;
//	
//	public ObjectMocker() {
//		mockedClasses = new HashSet<>();
//		stringVocabulary = Arrays.asList("Donald Trump", "Cat", "Dog", "Abraham Lincoln", UUID.randomUUID().toString());
//	}
//	
//	@SuppressWarnings("unchecked")
//	public <T> List<T> mockList(Method method, int parameterIndex) throws Exception {
//		Class<?> clazz = parameterTypeArguments(method, parameterIndex).get(0);
//		List<T> list = new LinkedList<>();
//		for (int i = 0; i < 2; i++) {
//			list.add((T) mockObject(clazz));
//		}
//		return list;
//	}
//	
//	@SuppressWarnings("unchecked")
//	public <T, V> Map<T, V> mockMap(Method method, int parameterIndex) throws Exception {
//		List<Class<?>> typeArguments = parameterTypeArguments(method, parameterIndex);
//		Class<?> keyClass = typeArguments.get(0);
//		Class<?> valueClass = typeArguments.get(1);
//		Map<T, V> map = new HashMap<>();
//		for (int i = 0; i < 2; i++) {
//			map.put((T) mockObject(keyClass), (V) mockObject(valueClass));
//		}
//		return map;
//	}
//		
//	@SuppressWarnings("unchecked")
//	public <T> T mockObject(Class<T> clazz) throws Exception {
//		if (clazz == boolean.class || clazz == Boolean.class) {
//			return (T) mockBoolean();
//		} else if (clazz == byte.class || clazz == Byte.class) {
//			return (T) mockByte();
//		} else if (clazz == short.class || clazz == Short.class) {
//			return (T) mockShort();
//		} else if (clazz == int.class || clazz == Integer.class) {
//			return (T) mockInt();
//		} else if (clazz == long.class || clazz == Long.class) {
//			return (T) mockLong();
//		} else if (clazz == float.class || clazz == Float.class) {
//			return (T) mockFloat();
//		} else if (clazz == double.class || clazz == Double.class) {
//			return (T) mockDouble();
//		} else if (clazz == char.class || clazz == Character.class) {
//			return (T) mockCharacter();
//		} else if (clazz == BigDecimal.class) {
//			return (T) mockBigDecimal();
//		} else if (clazz == String.class) {
//			return (T) mockString();
//		} else if (clazz == List.class) {
//			System.out.println("Unable to mock list due to type erasure in JVM.");
//			return null;
//		} else if (clazz == Map.class) {
//			System.out.println("Unable to mock map due to type erasure in JVM.");
//			return null;
//		} else {
//			return mockPojo(clazz);
//		}
//	}
//	
//	public <T> T mockPojo(Class<T> clazz) throws Exception {
//		if (mockedClasses.contains(clazz)) {
//			return null;
//		}
//		mockedClasses.add(clazz);
//		T t = clazz.newInstance();
//		for (Method setter : Pointer.setters(clazz)) {
//			Class<?> parameterType = setter.getParameterTypes()[0];
//			if (parameterType == boolean.class || parameterType == Boolean.class) {
//				setter.invoke(t, mockBoolean());
//			} else if (parameterType == byte.class || parameterType == Byte.class) {
//				setter.invoke(t, mockByte());
//			} else if (parameterType == short.class || parameterType == Short.class) {
//				setter.invoke(t, mockShort());
//			} else if (parameterType == int.class || parameterType == Integer.class) {
//				setter.invoke(t, mockInt());
//			} else if (parameterType == long.class || parameterType == Long.class) {
//				setter.invoke(t, mockLong());
//			} else if (parameterType == float.class || parameterType == Float.class) {
//				setter.invoke(t, mockFloat());
//			} else if (parameterType == double.class || parameterType == Double.class) {
//				setter.invoke(t, mockDouble());
//			} else if (parameterType == char.class || parameterType == Character.class) {
//				setter.invoke(t, mockCharacter());
//			} else if (parameterType == BigDecimal.class) {
//				setter.invoke(t, mockBigDecimal());
//			} else if (parameterType == String.class) {
//				setter.invoke(t, mockString());
//			} else if (parameterType == List.class) {
//				setter.invoke(t, mockList(setter, 0));
//			} else if (parameterType == Map.class) {
//				setter.invoke(t, mockMap(setter, 0));
//			} else {// Invoke recursion if the field is also a POJO.
//				setter.invoke(t, mockPojo(parameterType));
//			}
//		}
//		return t;
//	}
//}