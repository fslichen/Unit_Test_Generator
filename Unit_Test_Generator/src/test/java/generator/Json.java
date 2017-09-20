package generator;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Json {
	private static ObjectMapper objectMapper;
	
	static {
		objectMapper = new ObjectMapper();
	}
	
	public static List<String> splitJsonList(String jsonArray) {
		int level = 0;
		int leftBracketIndex = 0;
		List<String> jsons = new LinkedList<>();
		for (int i = 0; i < jsonArray.length(); i++) {
			char character = jsonArray.charAt(i);
			if (character == '{') {
				if (level == 0) {
					leftBracketIndex = i;
				}
				level++;
			} else if (character == '}') {
				level--;
				if (level == 0) {
					jsons.add(jsonArray.substring(leftBracketIndex, i + 1));
				}
			}
		}
		return jsons;
	}
	
	public static <T> List<T> fromJsonList(String json, Class<T> clazz) {
		List<T> ts = new LinkedList<>();
		for (String subJson : splitJsonList(json)) {
			ts.add(fromJson(subJson, clazz));
		}
		return ts;
	}
	
	public static List<String> splitJsonList(String json, String fieldName) {
		int level = 0;
		int leftBracketIndex = 0;
		for (int i = json.indexOf(fieldName) + fieldName.length(); i < json.length(); i++) {
			if (json.charAt(i) == '[') {
				if (level == 0) {
					leftBracketIndex = i;
				}
				level++;
			} else if (json.charAt(i) == ']') {
				level--;
				if (level == 0) {
					return splitJsonList(json.substring(leftBracketIndex, i + 1));
				}
			}
		}
		return null;
	}
	
	public static <T> T fromJson(String json, Class<T> clazz) {
		try {
			return objectMapper.readValue(json, clazz);
		} catch (IOException e) {
			System.out.println("Unable to convert JSON into " + clazz.getSimpleName() + ".");
			return null;
		}
	}
}
