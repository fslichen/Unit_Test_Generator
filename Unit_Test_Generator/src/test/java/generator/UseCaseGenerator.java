package generator;

import java.util.function.Predicate;

import org.junit.Test;
import org.springframework.web.bind.annotation.RestController;

public class UseCaseGenerator extends BaseTest {
	@Test
	public void testInvokeMethodsUnderBasePackageOfSrcMainJavaAndGetMockedParameterValuesAndReturnValues() throws Exception {
		Predicate<Class<?>> classFileter = new Predicate<Class<?>>() {
			@Override
			public boolean test(Class<?> clazz) {
				return clazz.getAnnotation(RestController.class) != null;
			}
		};
		new UnitTestGenerator().invokeMethodsUnderBasePackageOfSrcMainJavaAndGetMockedParameterValuesAndReturnValues("evolution.controller", classFileter, webApplicationContext);
	}
}
