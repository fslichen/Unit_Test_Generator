package generator.test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.junit.Test;

import evolution.controller.AnyController;
import evolution.pojo.AnyPojo;
import generator.CodeWriter;

public class CodeWriterTest {
	@Test
	public void test() throws Exception {
		CodeWriter codeWriter = new CodeWriter();
		Field serviceField = AnyController.class.getDeclaredField("anyService");
		Method method = serviceField.getType().getMethod("anotherMethod", AnyPojo.class);
		codeWriter.writeMockito4InvokingComponentMethod(serviceField, method);
		System.out.println(codeWriter.generateCodes());
	}
}
