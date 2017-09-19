package generator;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Json {
	protected ObjectMapper objectMapper;
	
	public Json() {
		objectMapper = new ObjectMapper();
	}
	
	public List<String> splitJsonList(String jsonArray) {
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
	
	public <T> List<T> fromJsonList(String json, Class<T> clazz) {
		List<T> ts = new LinkedList<>();
		for (String subJson : splitJsonList(json)) {
			ts.add(fromJson(subJson, clazz));
		}
		return ts;
	}
	
	public <T> T fromJson(String json, Class<T> clazz) {
		try {
			return objectMapper.readValue(json, clazz);
		} catch (IOException e) {
			System.out.println("Unable to convert JSON into " + clazz.getSimpleName() + ".");
			return null;
		}
	}
}
