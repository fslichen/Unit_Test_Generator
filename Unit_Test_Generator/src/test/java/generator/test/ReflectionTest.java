package generator.test;

import org.junit.Test;

import evolution.controller.MockController;
import evolution.service.AnyService;
import generator.Reflection;

public class ReflectionTest {
	@Test
	public void test() {
		MockController mockController = new MockController();
		AnyService anyService = new AnyService();
		Reflection.set(mockController, "myService", anyService);
		System.out.println(mockController);
	}
}
