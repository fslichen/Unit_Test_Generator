package generator;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Properties;

import org.apache.commons.io.FilenameUtils;

public class Lang {
	public static boolean isUpperCase(char character) {
		return 'A' <= character && character <= 'Z';
	}
	
	public static boolean isUpperCase(String string, int index) {
		return isUpperCase(string.charAt(index));
	}
	
	public static String lowerFirstCharacter(String string) {
		return string.substring(0, 1).toLowerCase() + string.substring(1);
	}
	
	public static String pathInString(Path path) {
		return pathInString(path.toString());
	}
	
	public static String pathInString(String path) {
		return path.replace("\\", "/");// The issue occurs in Windows.
	}
	
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

	public static String trimEndingComma(String string) {
		string = string.trim();
		if (string.endsWith(",")) {
			return string.substring(0, string.length() - 1);
		}
		return string;
	}
	
	public static String trimEndingComma(StringBuilder stringBuilder) {
		return trimEndingComma(stringBuilder.toString());
	}
	
	public static String upperFirstCharacter(String string) {
		return string.substring(0, 1).toUpperCase() + string.substring(1);
	}

	public static boolean withExtension(Path path, String extension) {
		return withExtension(path.toString(), extension);
	}
	
	public static boolean withExtension(String path, String extension) {
		return extension.equals(FilenameUtils.getExtension(Lang.pathInString(path)));
	}
}
