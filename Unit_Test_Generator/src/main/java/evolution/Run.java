package evolution;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import evolution.template.UnitTestClassWriter;
import evolution.template.UnitTestMethodWriter;

public class Run extends Generator {
	@Test
	public void test() throws IOException {
		Map<Class<?>, UnitTestMethodWriter> unitTestMethodWriters = new LinkedHashMap<>();
		UnitTestMethodWriter generalMethodWriter = new UnitTestMethodWriter() {
			@Override
			public List<String> write(Method method) {
				List<String> codes = new LinkedList<>();
				codes.add("@Test");
				codes.add("@Database4UcaseSetup");
				codes.add("@ExpectedDatabase4Ucase");
				codes.add("public void test" + capitalizedFirstCharacter(method.getName()) + "() {");
				codes.add("System.out.println(\"Hello World\");");
				codes.add("}");
				return codes;
			}
		};
		Map<Class<?>, UnitTestClassWriter> unitTestClassWriters = new LinkedHashMap<>();
		UnitTestClassWriter generalClassWriter = new UnitTestClassWriter() {
			@Override
			public List<String> write() {
				return Arrays.asList("import evolution.annotation.Database4UcaseSetup;",
		                "import evolution.annotation.ExpectedDatabase4Ucase;",
		                "private String name;", "");
			}
		};
		unitTestClassWriters.put(null, generalClassWriter);
		unitTestMethodWriters.put(null, generalMethodWriter);
		new Generator().scanClassesUnderSrcMainJavaAndGenerateUnitTestClassesUnderSrcTestJava(unitTestClassWriters, unitTestMethodWriters, true);
	}
}
