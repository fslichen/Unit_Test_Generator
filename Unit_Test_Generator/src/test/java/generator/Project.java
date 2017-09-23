package generator;

import java.io.IOException;
import java.util.Properties;

public class Project {
	@SuppressWarnings("unchecked")
	public static <T> T property(String key, Class<T> clazz) {
		Properties properties = new Properties();
		try {
			properties.load(UnitTestGenerator.class.getResourceAsStream("/unit-test.properties"));
			String value = properties.get(key).toString();
			if (clazz == int.class || clazz == Integer.class) {
				return (T) new Integer(value);
			} else if (clazz == double.class || clazz == Double.class) {
				return (T) new Double(value);
			} else if (clazz == boolean.class || clazz == Boolean.class) {
				return (T) new Boolean(value);
			}
			return (T) value;
		} catch (IOException e) {
			System.out.println("Please create unit-test.properties under src/test/resources");
			return null;
		}
	}
}
