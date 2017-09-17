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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import evolution.template.UnitTestClassWriter;
import evolution.template.UnitTestMethodWriter;

public class Generator {
	public static final String SRC_MAIN_JAVA = "src/main/java";
	public static final String SRC_TEST_JAVA = "src/test/java"; 
	
	public boolean withExtension(Path path, String extension) {
		return extension.equals(FilenameUtils.getExtension(path.toString().replace("\\", "/")));
	}
	
	public Class<?> classAnnnotation(Class<?> clazz) {
		Class<?> classAnnotation = null;
		for (Annotation annotation : clazz.getAnnotations()) {
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
		return classAnnotation;
	}
	
	public void scanClassesUnderSrcMainJavaAndGenerateUnitTestClassesUnderSrcTestJava(final Map<Class<?>, UnitTestClassWriter> unitTestClassWriters, final Map<Class<?>, UnitTestMethodWriter> unitTestMethodWriters, final boolean overwrite) throws IOException {
		try (Stream<Path> paths = Files.walk(Paths.get(System.getProperty("user.dir")))) {
			paths.filter(new Predicate<Path>() {
				@Override
				public boolean test(Path path) {
					if (path.toString().replace("\\", "/").contains(SRC_MAIN_JAVA) && withExtension(path, "java")) {
						return true;
					}
					return false;
				}
			}).forEach(new Consumer<Path>() {
				@Override
				public void accept(Path path) {
					// Get the class.
					Class<?> clazz = null;
					String className = null;
					try {
						className = path.toString().replace("\\", "/");
						className = className.substring(className.lastIndexOf(SRC_MAIN_JAVA) + SRC_MAIN_JAVA.length() + 1).replace("/", ".").replace(".java", "");
						clazz = Class.forName(className);
					} catch (ClassNotFoundException e) {
						System.out.println("Unable to determine the class.");
						return;
					}
					String simpleClassName = clazz.getSimpleName();
					// Generate unit test class codes.
					StringBuilder codes = new StringBuilder();
					List<String> classCodes = new LinkedList<>();
					classCodes.add("package " + className.substring(0, className.lastIndexOf(".")) + ";");
					classCodes.add("import org.junit.Test;");
					Class<?> classAnnotation = classAnnnotation(clazz);
					UnitTestClassWriter unitTestClassWriter = unitTestClassWriters.get(classAnnotation);
					List<String> dynamicClassCodes = unitTestClassWriter.write();
					int importCount = 0;
					for (String dynamicClassCode : dynamicClassCodes) {
						if (dynamicClassCode.contains("import")) {
							importCount++;
						}
					}
					classCodes.addAll(dynamicClassCodes);
					classCodes.add(importCount + 2, "public class " + simpleClassName + "Test {");
					CodeWriter codeWriter = new CodeWriter();
					codeWriter.writeCodes(classCodes, codes);
					// Generate unit test method codes.
					UnitTestMethodWriter unitTestMethodWriter = unitTestMethodWriters.get(classAnnotation);
					for (Method method : clazz.getDeclaredMethods()) {
						codeWriter.writeCodes(unitTestMethodWriter.write(method), codes);
						codes.append("\n");
					}
					codes.append("}");
					// Write unit test file.
					String unitTestFilePath = path.toString().replace("\\", "/").replace("src/main/java", "src/test/java").replace(".java", "Test.java");
					File unitTestFileDirectory = new File(unitTestFilePath.substring(0, unitTestFilePath.lastIndexOf("/")));
					if (!unitTestFileDirectory.exists()) {
						unitTestFileDirectory.mkdirs();
					}
					File unitTestFile = new File(unitTestFilePath);
					if (unitTestFile.exists() && !overwrite) {
						System.out.println("The file already exists.");
						return;
					}
					try (PrintWriter printWriter = new PrintWriter(unitTestFile)) {
						printWriter.write(codes.toString());
						System.out.println("The unit test file is successfully generated.");
					} catch (FileNotFoundException e) {
						System.out.println("The file path is unavailable.");
					}
					System.out.println(codes + "\n");					
				}
			});
		}
	}
	
	public String capitalizedFirstCharacter(String string) {
		return string.substring(0, 1).toUpperCase() + string.substring(1);
	}
}
