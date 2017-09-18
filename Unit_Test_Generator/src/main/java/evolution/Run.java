package evolution;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import org.junit.Test;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import evolution.example.controller.dto.AnyDto;
import evolution.template.UnitTestClassWriter;
import evolution.template.UnitTestMethodWriter;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = Application.class)
//@WebAppConfiguration
public class Run extends UnitTestGenerator {
//	@Test
	public void testCopy() {
		AnyDto anyDto = new AnyDto();
		anyDto.setName("Chen");
		anyDto.setAge(27);
		AnyDto anyDto0 = new UnitTestGenerator().copyObject(anyDto);
		System.out.println(anyDto0);
	}
	
	@Test
	public void testInvokeMethodsUnderSrcMainJava() throws Exception {
		new UnitTestGenerator().invokeMethodsUnderBasePackageUnderSrcMainJavaAndGetMockedParameterValuesAndReturnValues("evolution.example.controller", new Predicate<Class<?>>() {
			@Override
			public boolean test(Class<?> clazz) {
				return clazz.getAnnotation(RestController.class) != null;
			}
		});
	}
	
//	@Test
	public void test() throws IOException {
		UnitTestClassWriter generalClassWriter = new UnitTestClassWriter() {
			@Override
			public List<String> write() {
				return Arrays.asList("import evolution.annotation.Database4UcaseSetup;",
		                "import evolution.annotation.ExpectedDatabase4Ucase;", 
		                "private String name;", "");
			}
		};
		Map<Class<?>, UnitTestClassWriter> unitTestClassWriters = new LinkedHashMap<>();
		unitTestClassWriters.put(null, generalClassWriter);
		UnitTestMethodWriter generalMethodWriter = new UnitTestMethodWriter() {
			@Override
			public List<String> write(Method method) {
				return Arrays.asList("@Database4UcaseSetup", "@ExpectedDatabase4Ucase",
						"System.out.println(\"Hello World\");");
			}
		};
		Map<Class<?>, UnitTestMethodWriter> unitTestMethodWriters = new LinkedHashMap<>();
		unitTestMethodWriters.put(null, generalMethodWriter);
		new UnitTestGenerator().scanClassesUnderBasePackageOfSrcMainJavaAndGenerateUnitTestClassesUnderSrcTestJava("evolution.example", true, unitTestClassWriters, unitTestMethodWriters);
	}
}
