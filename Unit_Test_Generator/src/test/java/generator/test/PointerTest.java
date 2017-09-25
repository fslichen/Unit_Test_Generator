package generator.test;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import evolution.pojo.AnyPojo;
import generator.CodeWriter;
import generator.Pointer;

public class PointerTest {
	public Map<Date, List<Map<String, AnyPojo>>> testMethod() {
		return null;
	}
	
	public AnyPojo testMethod0() {
		return null;
	}
	
	@Test
	public void test0() throws NoSuchMethodException, SecurityException {
		Method method = PointerTest.class.getDeclaredMethod("testMethod0");
		CodeWriter codeWriter = new CodeWriter();
		String simple = Pointer.simpleReturnTypeName(method, codeWriter);
		System.out.println(simple);
	}
	
	@Test
	public void test() throws NoSuchMethodException, SecurityException, ClassNotFoundException {
		Method method = PointerTest.class.getDeclaredMethod("testMethod");
		CodeWriter codeWriter = new CodeWriter();
		String simple = Pointer.simpleReturnTypeName(method, codeWriter);
		System.out.println(simple);
	}
}
