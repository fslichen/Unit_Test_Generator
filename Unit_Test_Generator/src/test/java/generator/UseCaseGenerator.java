package generator;

import org.junit.Test;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

public class UseCaseGenerator extends BaseTestCase {
	@Test
	public void run() throws Exception {
		UnitTestGenerator unitTestGenerator = new UnitTestGenerator();
		unitTestGenerator.invokeMethodsUnderBasePackageOfSrcMainJavaAndGenerateUseCasesUnderSrcTestJava(
				Lang.property("use-case-base-package", String.class), 
				x -> x.isAnnotationPresent(Controller.class)
				|| x.isAnnotationPresent(RestController.class)
				|| x.isAnnotationPresent(Service.class)
				|| x.isAnnotationPresent(Repository.class)
				|| x.getSimpleName().endsWith("Mapper"), 
				webApplicationContext);
	}
}
