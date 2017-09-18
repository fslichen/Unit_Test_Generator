package evolution;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import evolution.example.Application;
import evolution.example.controller.AnyController;
import evolution.example.controller.dto.AnyDto;
import evolution.pojo.ParameterValuesAndReturnValue;
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
	
//	@Test
	public void testInvokeMethodsUnderSrcMainJava() throws Exception {
		AnyController anyController = new AnyController();
		ParameterValuesAndReturnValue result = new UnitTestGenerator().invokeMethodAndGetMockedParameterValuesAndReturnValue(AnyController.class.getMethod("post", AnyDto.class), anyController);
		System.out.println(result);
	}
	
	@Test
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
