package generator.test;

import java.lang.reflect.Field;

import org.junit.Test;

import evolution.controller.AnyController;
import generator.CodeWriter;

public class CodeWriterTest {
	@Test
	public void test() throws Exception {
		CodeWriter codeWriter = new CodeWriter();
		Field serviceField = AnyController.class.getDeclaredField("anyService");
		codeWriter.writeMockito4InvokingComponentMethod(serviceField);
		System.out.println(codeWriter.generateCodes());
	}
}
