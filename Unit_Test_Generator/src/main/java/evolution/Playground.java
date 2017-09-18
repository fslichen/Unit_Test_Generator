package evolution;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import evolution.example.controller.dto.AnyDto;

public class Playground {
	@Test
	@SuppressWarnings("unchecked")
	public void test() {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			List<AnyDto> list = objectMapper.readValue("[{\"name\":\"Chen\",\"age\":27}]", List.class);
			System.out.println(list);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
