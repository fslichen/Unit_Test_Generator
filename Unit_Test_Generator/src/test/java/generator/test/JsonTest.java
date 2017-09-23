package generator.test;

import org.junit.Test;

import generator.Json;
import generator.test.pojo.PojoWithObject;

public class JsonTest {
	@Test
	public void testCopyObject() {
		PojoWithObject p = new PojoWithObject();
		PojoWithObject q = Json.copyObject(p);
		System.out.println(q);
	}
}
