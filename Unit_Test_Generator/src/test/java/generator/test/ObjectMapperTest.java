package generator.test;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectMapperTest {
	@Test
	public void test() throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper o = new ObjectMapper();
		double age = o.readValue("28", double.class);
		System.out.println(age);
	}
}
