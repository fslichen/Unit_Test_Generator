package evolution;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import evolution.template.UnitTestClassWriter;
import evolution.template.UnitTestMethodWriter;

public class Run extends Generator {
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
		new Generator().scanClassesUnderSrcMainJavaAndGenerateUnitTestClassesUnderSrcTestJava(unitTestClassWriters, unitTestMethodWriters, true);
	}
}
