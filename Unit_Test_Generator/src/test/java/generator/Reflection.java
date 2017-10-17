package generator;

import java.lang.reflect.Field;

public class Reflection {
	public static void set(Object classInstance, String fieldName, Object fieldInstance) {
		try {
			Field field = classInstance.getClass().getDeclaredField(fieldName);
			field.setAccessible(true);
			field.set(classInstance, fieldInstance);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
