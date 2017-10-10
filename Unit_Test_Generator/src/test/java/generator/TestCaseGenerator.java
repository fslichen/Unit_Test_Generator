package generator;

import org.junit.Test;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import evolution.Application;

public class TestCaseGenerator {
	@Test
	public void run() throws Exception {
		new UnitTestGenerator().scanClassesUnderBasePackageOfSrcMainJavaAndGenerateTestCasesUnderSrcTestJava(
				Lang.property("test-case-base-package", String.class), 
				x -> x != Application.class 
				&& (x.isAnnotationPresent(Controller.class)
				|| x.isAnnotationPresent(RestController.class)
				|| x.isAnnotationPresent(Service.class)
				|| x.isAnnotationPresent(Repository.class)
				|| x.getSimpleName().endsWith("Mapper")));
	}
}
