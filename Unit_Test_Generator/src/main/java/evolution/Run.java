package evolution;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import org.junit.Test;
import org.springframework.web.bind.annotation.RestController;

import evolution.template.UnitTestClassWriter;
import evolution.template.UnitTestMethodWriter;

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
				return Arrays.asList("import evolution.annotation.Database4UcaseSetup;",
		                "import evolution.annotation.ExpectedDatabase4Ucase;", 
		                "private String name;", "");
			}
		};
		UnitTestMethodWriter generalMethodWriter = new UnitTestMethodWriter() {
			@Override
			public List<String> write(Method method) {
				return Arrays.asList("@Database4UcaseSetup", "@ExpectedDatabase4Ucase",
						"String requestData = null;");
			}
		};
		new UnitTestGenerator().scanClassesUnderBasePackageOfSrcMainJavaAndGenerateUnitTestClassesUnderSrcTestJava("evolution.example", new Predicate<Class<?>>() {
			@Override
			public boolean test(Class<?> clazz) {
				return clazz.getAnnotations().length > 0;
			}}, true, generalClassWriter, generalMethodWriter);
	}
}
