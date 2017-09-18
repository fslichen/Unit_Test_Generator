package evolution;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import evolution.example.controller.dto.AnyDto;

public class ObjectMapperPlus {
	protected ObjectMapper objectMapper;
	
	public ObjectMapperPlus() {
		objectMapper = new ObjectMapper();
	}
	
	public List<String> splitJson(String json) {
		int level = 0;
		int startIndex = 0;
		int endIndex = 0;
		List<String> subJsons = new LinkedList<>();
		for (int i = 0; i < json.length(); i++) {
			char character = json.charAt(i);
			if (character == '{') {
				if (level == 0) {
					startIndex = i;
				}
				level++;
			} else if (character == '}') {
				level--;
				if (level == 0) {
					endIndex = i;
					subJsons.add(json.substring(startIndex, endIndex + 1));
				}
			}
		}
		return subJsons;
	}
	
	public <T> List<T> fromJsonList(String json, Class<T> clazz) {
		List<T> ts = new LinkedList<>();
		for (String subJson : splitJson(json)) {
			ts.add(fromJson(subJson, clazz));
		}
		return ts;
	}
	
	public <T> T fromJson(String json, Class<T> clazz) {
		try {
			return objectMapper.readValue(json, clazz);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Test
	public void test() {
		List<AnyDto> anyDtos = fromJsonList("[{\"name\":\"Chen\"}]", AnyDto.class);
		System.out.println(anyDtos);
	}
}
