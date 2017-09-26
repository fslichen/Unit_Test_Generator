package generator;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import org.junit.Test;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import evolution.Application;
import evolution.annotation.Database4UcaseSetup;
import evolution.annotation.ExpectedDatabase4Ucase;
import generator.template.TestCase;
import generator.template.UnitTestClassWriter;
import generator.template.UnitTestMethodWriter;

public class TestCaseGenerator {
	@Test
	public void run() throws Exception {
		// Class Filter
		Predicate<Class<?>> classFilter = new Predicate<Class<?>>() {
			@Override
			public boolean test(Class<?> clazz) {
				return clazz != Application.class && (clazz.getAnnotation(Controller.class) != null || clazz.getAnnotation(RestController.class) != null || clazz.getAnnotation(Service.class) != null || clazz.getAnnotation(Repository.class) != null || clazz.getSimpleName().endsWith("Mapper"));
			}
		};
		// Unit Test Class Writer
		Map<Class<?>, UnitTestClassWriter> unitTestClassWriters = new LinkedHashMap<>();
		UnitTestClassWriter defaultClassWriter = new UnitTestClassWriter() {
			@Override
			public List<String> write() {
				CodeWriter codeWriter = new CodeWriter();
				codeWriter.writeCode("private String name;");
				codeWriter.writeBlankLine();
				return codeWriter.getCodes();
			}
		};
		unitTestClassWriters.put(null, defaultClassWriter);
		// Unit Test Method Writer
		Map<Class<?>, UnitTestMethodWriter> unitTestMethodWriters = new LinkedHashMap<>();
		UnitTestMethodWriter defaultMethodWriter = new UnitTestMethodWriter() {
			@Override
			public List<String> write(Method method) {
				CodeWriter codeWriter = new CodeWriter();
				codeWriter.writeAnnotation(Database4UcaseSetup.class);
				codeWriter.writeAnnotation(ExpectedDatabase4Ucase.class);
				codeWriter.writeImport(TestCase.class);
				codeWriter.writeCode("TestCase testCase = testCaseClient.getTestCase();");
				codeWriter.writeCode("String requestData = testCase.getRequestData();");
				codeWriter.writeCode("String responseData = testCase.getResponseData();");
				return codeWriter.getCodes();
			}
		};
		unitTestMethodWriters.put(null, defaultMethodWriter);
		UnitTestGenerator unitTestGenerator = new UnitTestGenerator();
		unitTestGenerator.scanClassesUnderBasePackageOfSrcMainJavaAndGenerateTestCasesUnderSrcTestJava(Lang.property("test-case-base-package", String.class), classFilter, unitTestClassWriters, unitTestMethodWriters);
	}
}
