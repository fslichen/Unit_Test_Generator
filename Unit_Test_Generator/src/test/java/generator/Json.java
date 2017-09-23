package generator;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Json {
	private static ObjectMapper objectMapper;
	
	static {
		objectMapper = new ObjectMapper();
	}
	
	public static <T> T fromJson(String json, Class<T> clazz) {
		try {
			return objectMapper.readValue(json, clazz);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static <T> List<T> fromJsons(String json, Class<T> clazz) {
		List<T> ts = new LinkedList<>();
		for (String subJson : splitJsons(json)) {
			ts.add(fromJson(subJson, clazz));
		}
		return ts;
	}
	
	public static <T> T fromSubJson(String json, String fieldName, Class<T> clazz) {
		try {
			return objectMapper.readValue(subJson(json, fieldName), clazz);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static <T> List<T> fromSubJsons(String json, String fieldName, Class<T> clazz) {
		List<T> ts = new LinkedList<>();
		for (String subJson : splitSubJsons(json, fieldName)) {
			ts.add(fromJson(subJson, clazz));
		}
		return ts;
	}
	
	public static List<String> splitJsons(String json) {
		try {
			List<String> result = new LinkedList<>();
			for (Object subJson : objectMapper.readValue(json, List.class)) {
				result.add(objectMapper.writeValueAsString(subJson));
			}
			return result;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static List<String> splitSubJsons(String json, String fieldName) {
		return splitJsons(subJson(json, fieldName));
	}
	
	public static String subJson(String json, String fieldName) {
		try {
			return objectMapper.writeValueAsString(objectMapper.readValue(json, Map.class).get(fieldName));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T copyObject(T t) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			return (T) objectMapper.readValue(objectMapper.writeValueAsString(t), t.getClass());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
