package evolution;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
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
	public static final String SRC_TEST_JAVA = "src/test/java"; 
	
	public boolean endsWithExtension(Path path, String extension) {
		File file = path.toFile();
		if (file.isFile()) {
			String fileName = file.getName().replace("\\", "/");
			if (extension.equals(fileName.substring(fileName.lastIndexOf(".") + 1))) {
				return true;
			}
		}
		return false;
	}
	
	@FunctionalInterface
	public interface UnitTestMethodWriter {
		public List<String> write(Method method);
	}
	
	@FunctionalInterface
	public interface UnitTestImportsWriter {
		public List<String> write();
	}
	
	public void scanClassesUnderSrcMainJavaAndGenerateUnitTestClassesUnderSrcTestJava(final UnitTestImportsWriter unitTestImportsWriter, final UnitTestMethodWriter unitTestMethodWriter) throws IOException {
		try (Stream<Path> paths = Files.walk(Paths.get(System.getProperty("user.dir")))) {
			paths.filter(new Predicate<Path>() {
				@Override
				public boolean test(Path path) {
					if (path.toString().replace("\\", "/").contains(SRC_MAIN_JAVA) && endsWithExtension(path, "java")) {
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
					String packageName = null;
					String className = null;
					try {
						classPackageName = path.toString().replace("\\", "/");
						classPackageName = classPackageName.substring(classPackageName.lastIndexOf(SRC_MAIN_JAVA) + SRC_MAIN_JAVA.length() + 1).replace("/", ".").replaceAll(".java", "");
						clazz = Class.forName(classPackageName);
						packageName = classPackageName.substring(0, classPackageName.lastIndexOf("."));
						className = classPackageName.substring(classPackageName.lastIndexOf(".") + 1);
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
					StringBuilder classCodes = new StringBuilder();
					List<String> metaCodes = new LinkedList<>();
					metaCodes.add("package " + packageName + ";");
					metaCodes.add("import org.junit.Test;");
					metaCodes.addAll(unitTestImportsWriter.write());
					metaCodes.add("public class " + className + "Test {");
					CodeWriter codeWriter = new CodeWriter();
					codeWriter.writeCodes(metaCodes, classCodes);
					for (Method method : clazz.getDeclaredMethods()) {
						codeWriter.writeCodes(unitTestMethodWriter.write(method), classCodes);
						classCodes.append("\n");
					}
					classCodes.append("}");
					// Write unit test file.
					String unitTestFilePath = path.toString().replace("\\", "/").replace("src/main/java", "src/test/java").replace(".java", "Test.java");
					File unitTestFileDirectory = new File(unitTestFilePath.substring(0, unitTestFilePath.lastIndexOf("/")));
					if (!unitTestFileDirectory.exists()) {
						unitTestFileDirectory.mkdirs();
					}
					try (PrintWriter printWriter = new PrintWriter(new File(unitTestFilePath))) {
						printWriter.write(classCodes.toString());
						System.out.println("The unit test file is successfully generated.");
					} catch (FileNotFoundException e) {
						System.out.println("The file path is unavailable.");
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
		new Generator().scanClassesUnderSrcMainJavaAndGenerateUnitTestClassesUnderSrcTestJava(new UnitTestImportsWriter() {
			@Override
			public List<String> write() {
				return Arrays.asList("import evolution.annotation.DatabaseSetup4Ucase;",
                "import evolution.annotation.ExpectedDatabase4Ucase;");
			}}, new UnitTestMethodWriter() {
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
