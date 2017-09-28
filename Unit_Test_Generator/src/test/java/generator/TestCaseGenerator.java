package generator;

import java.util.function.Predicate;

import org.junit.Test;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import evolution.Application;

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
		new UnitTestGenerator().scanClassesUnderBasePackageOfSrcMainJavaAndGenerateTestCasesUnderSrcTestJava(Lang.property("test-case-base-package", String.class), classFilter);
	}
}
