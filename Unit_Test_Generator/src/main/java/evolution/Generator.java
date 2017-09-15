package evolution;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.junit.Test;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

public class Generator {
	public static final String SRC_MAIN_JAVA = "src/main/java";
	
	public boolean endsWithExtension(Path path, String extension) {
		File file = path.toFile();
		if (file.isFile()) {
			String fileName = file.getName();
			if (extension.equals(fileName.substring(fileName.lastIndexOf(".") + 1))) {
				return true;
			}
		}
		return false;
	}
	
	@FunctionalInterface
	public interface UnitTestClassWriter {
		public List<String> write(Method method);
	}
	
	public void addIndent(int indentCount, StringBuilder code) {
		for (int i = 0; i < indentCount; i++) {
			for (int j = 0; j < 4; j++) {
				code.append(" ");
			}
		}
	}
	
	public void scanClassesUnderSrcMainJavaAndGenerateUnitTestClassesUnderSrcTestJava(final UnitTestClassWriter writeUnitTest) throws IOException {
		try (Stream<Path> paths = Files.walk(Paths.get(System.getProperty("user.dir")))) {
			paths.filter(new Predicate<Path>() {
				@Override
				public boolean test(Path path) {
					if (path.toString().contains(SRC_MAIN_JAVA) && endsWithExtension(path, "java")) {
						return true;
					}
					return false;
				}
			}).forEach(new Consumer<Path>() {
				@Override
				public void accept(Path path) {
					// Get the class.
					Class<?> clazz = null;
					String classPackageName = null;
					try {
						classPackageName = path.toString();
						classPackageName = classPackageName.substring(classPackageName.lastIndexOf(SRC_MAIN_JAVA) + SRC_MAIN_JAVA.length() + 1).replace("/", ".").replaceAll(".java", "");
						clazz = Class.forName(classPackageName);
					} catch (ClassNotFoundException e) {
						System.out.println("Unable to determine the class.");
						return;
					}
					// Determine the class annotation.
					Annotation[] annotations = clazz.getAnnotations();
					Class<?> classAnnotation = null;
					for (Annotation annotation : annotations) {
						if (annotation.annotationType() == Controller.class || annotation.annotationType() == RestController.class) {
							classAnnotation = Controller.class;
							break;
						} else if (annotation.annotationType() == Service.class) {
							classAnnotation = Service.class;
							break;
						} else if (annotation.annotationType() == Repository.class) {
							classAnnotation = Repository.class;
							break;
						} else if (annotation.annotationType() == Component.class) {
							classAnnotation = Component.class;
							break;
						}
					}
					// Generate Unit Test Classes.
					int indentCount = 0;
					StringBuilder classCodes = new StringBuilder();
					Method[] methods = clazz.getDeclaredMethods();
					for (Method method : methods) {
						List<String> methodCodes = writeUnitTest.write(method);
						for (String methodCode : methodCodes) {
							String trimedMethodCode = methodCode.trim();
							if (trimedMethodCode.endsWith("{")) {
								addIndent(indentCount, classCodes);
								indentCount++;
							} else if (trimedMethodCode.endsWith("}")) {
								indentCount--;
								addIndent(indentCount, classCodes);
							} else {
								addIndent(indentCount, classCodes);
							}
							classCodes.append(trimedMethodCode).append("\n");
						}
						classCodes.append("\n");
					}
					System.out.println(classCodes);
				}
			});
		}
	}
	
	public String capitalizedFirstCharacter(String string) {
		return string.substring(0, 1).toUpperCase() + string.substring(1);
	}
	
	@Test
	public void test() throws IOException {
		new Generator().scanClassesUnderSrcMainJavaAndGenerateUnitTestClassesUnderSrcTestJava(new UnitTestClassWriter() {
			@Override
			public List<String> write(Method method) {
				List<String> codes = new LinkedList<>();
				codes.add("@Test");
				codes.add("@DatabaseSetup4Ucase");
				codes.add("@ExpectedDatabase4Ucase");
				codes.add("public void test" + capitalizedFirstCharacter(method.getName()) + "() {");
				codes.add("System.out.println(\"Hello World\");");
				codes.add("}");
				return codes;
			}
		});
	}
}
