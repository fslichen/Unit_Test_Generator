package generator;

import java.lang.reflect.Method;
import java.util.List;
import java.util.function.Predicate;

import org.junit.Test;
import org.springframework.web.bind.annotation.RestController;

import evolution.Application;
import evolution.annotation.Database4UcaseSetup;
import evolution.annotation.ExpectedDatabase4Ucase;
import generator.template.UnitTestClassWriter;
import generator.template.UnitTestMethodWriter;

public class Run extends BaseTest {
	@Test
	public void testInvokeMethodsUnderBasePackageUnderSrcMainJavaAndGetMockedParameterValuesAndReturnValues() throws Exception {
		new UnitTestGenerator().invokeMethodsUnderBasePackageUnderSrcMainJavaAndGetMockedParameterValuesAndReturnValues("evolution.example.controller", new Predicate<Class<?>>() {
			@Override
			public boolean test(Class<?> clazz) {
				return clazz.getAnnotation(RestController.class) != null;
			}
		}, webApplicationContext);
	}
	
	@Test
	public void testScanClassesUnderBasePackageOfSrcMainJavaAndGenerateUnitTestClassesUnderSrcTestJava() throws Exception {
		UnitTestClassWriter generalClassWriter = new UnitTestClassWriter() {
			@Override
			public List<String> write() {
				CodeWriter codeWriter = new CodeWriter();
				codeWriter.writeCode("private String name;");
				codeWriter.writeBlankLine();
				return codeWriter.getCodes();
			}
		};
		UnitTestMethodWriter generalMethodWriter = new UnitTestMethodWriter() {
			@Override
			public List<String> write(Method method) {
				CodeWriter codeWriter = new CodeWriter();
				codeWriter.writeAnnotation(Database4UcaseSetup.class);
				codeWriter.writeAnnotation(ExpectedDatabase4Ucase.class);
				codeWriter.writeCode("String requestData = null;");
				codeWriter.writeCode("String responseData = null;");
				return codeWriter.getCodes();
			}
		};
		new UnitTestGenerator().scanClassesUnderBasePackageOfSrcMainJavaAndGenerateUnitTestClassesUnderSrcTestJava("evolution", new Predicate<Class<?>>() {
			@Override
			public boolean test(Class<?> clazz) {
				return clazz.getAnnotations().length > 0 && clazz != Application.class;
			}}, true, generalClassWriter, generalMethodWriter);
	}
}
