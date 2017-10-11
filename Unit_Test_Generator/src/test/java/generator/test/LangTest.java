package generator.test;

import org.junit.Test;

import generator.Lang;

public class LangTest {
	@Test
	public void testRandomProperty() {
		String car = Lang.randomProperty("mock.cars", String.class);
		System.out.println(car);
	}
}
