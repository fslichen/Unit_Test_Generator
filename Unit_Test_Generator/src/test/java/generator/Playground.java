package generator;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import evolution.controller.dto.AnyDto;

public class Playground {
//	@Test
//	@SuppressWarnings("unchecked")
	public void testToList() {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			List<AnyDto> list = objectMapper.readValue("[{\"name\":\"Chen\",\"age\":27}]", List.class);
			for (AnyDto anyDto : list) {
				System.out.println(anyDto);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
//	@Test
	public void test() {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			List<Object> objects = objectMapper.readValue("[{\"name\":\"Chen\",\"age\":27},{\"address\":\"SZ\"}]", List.class);
			System.out.println(objects);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
//	@Test
	public void testAddNull2List() {
		List<String> strs = new LinkedList<>();
		strs.add("Hello");
		strs.add(null);
		strs.add("World");
		System.out.println(strs);
	}
	
//	@Test
	public void testSplitJson() {
		String str = "[{\"name\":\"Chen\"},{\"address\":\"FS\"}]";
		Json json = new Json();
		System.out.println(json.splitJsonList(str));
	}
	
//	@Test
	public void testJavaAssist() {
		for (StackTraceElement e : Thread.currentThread().getStackTrace()) {
			System.out.println(e.getLineNumber());
			System.out.println(e);
		}
	}
	
	@Test
	public void testAnyhow() throws JsonParseException, JsonMappingException, IOException {
		List<AnyDto> i = Json.fromJsonList("[ {    \"name\"   :       \"Chen\"   }   ,   {   \"age\"   : 27   } ]", AnyDto.class);
		System.out.println(i);	
		List<String> j = Json.splitJsonList("{    \"data\":[ {    \"name\"   :       \"Chen\"   }   ,   {   \"age\"   : 27   } ] }", "data");
		System.out.println(j);
	}
}
