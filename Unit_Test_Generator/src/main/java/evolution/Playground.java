package evolution;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import evolution.example.controller.dto.AnyDto;

public class Playground {
	@Test
	@SuppressWarnings("unchecked")
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
	
	@Test
	public void test() {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			List<Object> objects = objectMapper.readValue("[{\"name\":\"Chen\",\"age\":27},{\"address\":\"SZ\"}]", List.class);
			System.out.println(objects);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
