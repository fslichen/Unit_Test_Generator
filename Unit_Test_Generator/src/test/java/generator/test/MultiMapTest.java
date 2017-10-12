package generator.test;

import java.util.Date;

import org.junit.Test;

import generator.util.map.MultiHashMap;
import generator.util.map.MultiMap;

public class MultiMapTest {
	@Test
	public void test() {
		MultiMap<String, Date> map = new MultiHashMap<>();
		map.push("Chen", new Date(), new Date());
		System.out.println(map);
	}
}
