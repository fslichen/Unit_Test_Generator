package generator.test;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.junit.Test;

import evolution.controller.AnyController;
import evolution.pojo.AnyPojo;
import generator.codeWriter.CodeWriter;
import generator.pointer.Pointer;

public class PointerTest {
	@Test
	public void testFindMethod() throws Exception {
		Method method = Pointer.class.getDeclaredMethod("scanMethod", File.class, Method.class, Function.class);
		List<Boolean> booleans = Pointer.scanMethod(new File("/Users/chenli/Desktop/Pointer.java"), method, new Function<String, Boolean>() {
			@Override
			public Boolean apply(String code) {
				return code.contains("if") || code.contains("for");
			}
		});
		System.out.println(booleans);
	}
	
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
	
	@Test
	public void testAutowiredFields() {
		System.out.println(Pointer.autowiredFields(AnyController.class));
	}
}
