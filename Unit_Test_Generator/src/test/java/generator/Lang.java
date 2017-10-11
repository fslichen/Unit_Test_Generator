package generator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.Random;

import org.apache.commons.io.FilenameUtils;

public class Lang {
	private static final String RESOURCE_PATH = "/unit-test.properties";
	
	public static File createDirectoriesAndFile(String filePath) {
		File fileDirectory = fileDirectory(filePath);
		if (!fileDirectory.exists()) {
			fileDirectory.mkdirs();
		}
		return new File(filePath);
	}
	
	public static File fileDirectory(String filePath) {
		return new File(filePath.substring(0, filePath.lastIndexOf("/")));
	}
	
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
	
	public static <T> T randomProperty(String key, Class<T> clazz) {
		List<T> properties = properties(key, clazz);
		int propertyIndex = Optional.ofNullable(properties).map(x -> x.size()).filter(x -> x > 0).map(x -> new Random().nextInt(x)).orElse(-1);
		return propertyIndex >= 0 ? properties.get(propertyIndex) : null;
	}
	
	public static Properties loadProperties() {
		try {
			Properties properties = new Properties();
			properties.load(Lang.class.getResourceAsStream(RESOURCE_PATH));
			return properties;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} 
	}
	
	public static <T> List<T> properties(String key, Class<T> clazz) {
		List<T> result = new LinkedList<>();
		for (String value : Optional.ofNullable(loadProperties()).map(x -> x.getProperty(key)).map(x -> x.split(",")).orElse(new String[0])) {
			result.add(cast(value, clazz));
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T cast(String value, Class<?> clazz) {
		if (value == null) {
			return null;
		}
		if (clazz == int.class || clazz == Integer.class) {
			return (T) new Integer(value);
		} else if (clazz == double.class || clazz == Double.class) {
			return (T) new Double(value);
		} else if (clazz == boolean.class || clazz == Boolean.class) {
			return (T) new Boolean(value);
		}
		return (T) value;
	}
	
	public static <T> T property(String key, Class<T> clazz) {
		return cast(loadProperties().getProperty(key), clazz);
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
