package generator;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class Mocker {
	private Set<Class<?>> mockedClasses;
	
	public Mocker() {
		mockedClasses = new HashSet<>();
	}
	
	public static BigDecimal mockBigDecimal() {
		return new BigDecimal(mockDouble());
	}
	
	public static Boolean mockBoolean() {
		return new Random().nextBoolean();
	}

	public static Byte mockByte() {
		byte[] b = new byte[0];
		new Random().nextBytes(b);
		return b[0];
	}
	
	public static Character mockCharacter() {
		return Math.random() < .5 ? 'a' : 'z';
	}
	
	public static Double mockDouble() {
		return new Random().nextDouble();
	}
	
	public static Float mockFloat() {
		return new Random().nextFloat();
	}
	
	public static Integer mockInteger() {
		return new Random().nextInt();
	}
	
	public static Integer mockInteger(int bound) {
		return new Random().nextInt(bound);
	}
	
	public static Object mockInvokingMethod(Method method, Object currentInstance) throws Exception {
		return method.invoke(currentInstance, mockParameterValues(method));
	}
	

	
	public static Long mockLong() {
		return new Random().nextLong();
	}
	
	public static Object[] mockParameterValues(Method method) throws Exception {
		Mocker mocker = new Mocker();
		int parameterCount = method.getParameterCount();
		Object[] parameterValues = new Object[parameterCount];
		for (int i = 0; i < parameterCount; i++) {
			parameterValues[i] = mocker.mockObject(method.getGenericParameterTypes()[i].getTypeName());
		}
		return parameterValues;
	}
	
	public static Object mockReturnValue(Method method) throws Exception {
		return new Mocker().mockObject(method.getGenericReturnType().getTypeName());
	}
	
	public static Short mockShort() {
		return new Short(new Random().nextInt(32767) + "");
	}
	
	public static String mockString() {
		List<String> strings = Arrays.asList("George Washington", "Abraham Lincoln", "Donald Trump", "Richard Nixon", "Bill Clinton", "Barack Obama");
		return strings.get(mockInteger(strings.size()));
	}
	
	public Collection<Object> mockCollection(String genericTypeName, Collection<Object> collection) {
		for (int i = 0; i < mockInteger(Lang.property("max-collection-size", Integer.class)) + 1; i++) {
			collection.add(mockObject(Pointer.typeArgumentNames(genericTypeName).get(0)));
		}
		return collection;
	}
	
	public Object mockObject(String genericTypeName) {
		if (genericTypeName.equals(boolean.class.getName()) || genericTypeName.equals(Boolean.class.getName())) {
			return mockBoolean();
		} else if (genericTypeName.equals(byte.class.getName()) || genericTypeName.equals(Byte.class.getName())) {
			return mockByte();
		} else if (genericTypeName.equals(short.class.getName()) || genericTypeName.equals(Short.class.getName())) {
			return mockShort();
		} else if (genericTypeName.equals(int.class.getName()) || genericTypeName.equals(Integer.class.getName())) {
			return mockInteger();
		} else if (genericTypeName.equals(long.class.getName()) || genericTypeName.equals(Long.class.getName())) {
			return mockLong();
		} else if (genericTypeName.equals(float.class.getName()) || genericTypeName.equals(Float.class.getName())) {
			return mockFloat();
		} else if (genericTypeName.equals(double.class.getName()) || genericTypeName.equals(Double.class.getName())) {
			return mockDouble();
		} else if (genericTypeName.equals(char.class.getName()) || genericTypeName.equals(Character.class.getName())) {
			return mockCharacter();
		} else if (genericTypeName.equals(BigDecimal.class.getName())) {
			return mockBigDecimal();
		} else if (genericTypeName.equals(String.class.getName())) {// TODO Add more primitive types.
			return mockString();
		} else if (genericTypeName.startsWith(List.class.getName())) {
			return mockCollection(genericTypeName, new LinkedList<>());
		} else if (genericTypeName.startsWith(Set.class.getName())) {
			return mockCollection(genericTypeName, new LinkedHashSet<>());
		} else if (genericTypeName.startsWith(Map.class.getName())) {
			Map<Object, Object> map = new LinkedHashMap<>();
			for (int i = 0; i < mockInteger(Lang.property("max-collection-size", Integer.class)) + 1; i++) {
				List<String> typeArgumentNames = Pointer.typeArgumentNames(genericTypeName);
				map.put(mockObject(typeArgumentNames.get(0)), mockObject(typeArgumentNames.get(1)));
			}
			return map;
		} else if (genericTypeName.equals(Object.class.getName())) {
			return mockString();
		} else {// Miscellaneous
			try {
				Class<?> clazz = Class.forName(genericTypeName);
				if (!mockedClasses.contains(clazz)) {
					mockedClasses.add(clazz);
					Object instance = clazz.newInstance();
					for (Method setter : Pointer.setters(clazz)) {
						setter.invoke(instance, mockObject(setter.getGenericParameterTypes()[0].getTypeName()));
					}
					return instance;
				}
			} catch (Exception e) {
				System.out.println(String.format("Unable to create %s.", genericTypeName));
			}
		}
		return null;
	}
}