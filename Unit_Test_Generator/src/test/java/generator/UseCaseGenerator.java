package generator;

import java.util.function.Predicate;

import org.junit.Test;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

public class UseCaseGenerator {
	@Test
	public void testInvokeMethodsUnderBasePackageOfSrcMainJavaAndGetMockedParameterValuesAndReturnValues() throws Exception {
		Predicate<Class<?>> classFileter = new Predicate<Class<?>>() {
			@Override
			public boolean test(Class<?> clazz) {
				return clazz.getAnnotation(RestController.class) != null || clazz.getAnnotation(Service.class) != null;
			}
		};
		new UnitTestGenerator().invokeMethodsUnderBasePackageOfSrcMainJavaAndGetMockedParameterValuesAndReturnValues("evolution", classFileter, null);
	}
}
