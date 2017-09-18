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
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
	
	public void invokeMethodsUnderSrcMainJava() throws Exception {
		Map<Path, Class<?>> classes = classesUnderSrcMainJava(null);
		ObjectMocker mocker = new ObjectMocker();
		for (Entry<Path, Class<?>> entry : classes.entrySet()) {
			Class<?> clazz = entry.getValue();
			for (Method method : clazz.getDeclaredMethods()) {
				try {
					int i = 0;
					Object[] methodParameterValues = new Object[method.getParameterCount()];
					for (Class<?> methodParameterType : method.getParameterTypes()) {
						methodParameterValues[i++] = mocker.mockObject(methodParameterType);
					}
					Object returnValue = method.invoke(clazz.newInstance(), methodParameterValues);
					// Print out the result
					for (Object methodParameterValue : methodParameterValues) {
						System.out.println("method " + method.getName() + "parameter value = " + methodParameterValue);
					}
					System.out.println("method " + method.getName() + " return value = " + returnValue);
				} catch (Exception e) {
					System.out.println("Unable to invoke " + method.getName() + ".");
				}
			}
		}
	}
	
	public Map<Path, Class<?>> classesUnderSrcMainJava(final Predicate<Class<?>> predicate) throws IOException {
		final Map<Path, Class<?>> classes = new LinkedHashMap<>();
		try (Stream<Path> paths = Files.walk(Paths.get(System.getProperty("user.dir")))) {
			paths.forEach(new Consumer<Path>() {
				@Override
				public void accept(Path path) {
					if (!path.toString().replace("\\", "/").contains(SRC_MAIN_JAVA) || !withExtension(path, "java")) {
						return;
					}
					try {
						String className = path.toString().replace("\\", "/");
						className = className.substring(className.lastIndexOf(SRC_MAIN_JAVA) + SRC_MAIN_JAVA.length() + 1).replace("/", ".").replace(".java", "");
						Class<?> clazz = Class.forName(className);
						if (predicate == null || predicate.test(clazz)) {
							classes.put(path, clazz);
						}
					} catch (ClassNotFoundException e) {
						System.out.println("Unable to determine the class.");
					}
				}
			});
		}
		return classes;
	}
	
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
	
	public int keywordCount(List<String> codes, String... keywords) {
		int keywordCount = 0;
		for (String code : codes) {
			for (String keyword : keywords) {
				if (code.contains(keyword)) {
					keywordCount++;
				}
			}
		}
		return keywordCount;
	}
	
	public void scanClassesUnderSrcMainJavaAndGenerateUnitTestClassesUnderSrcTestJava(final Map<Class<?>, UnitTestClassWriter> unitTestClassWriters, final Map<Class<?>, UnitTestMethodWriter> unitTestMethodWriters, final boolean overwrite) throws IOException {
		for (Entry<Path, Class<?>> entry : classesUnderSrcMainJava(null).entrySet()) {
			// Generate unit test class related codes.
			Class<?> clazz = entry.getValue();
			String className = clazz.getName();
			List<String> classCodes = new LinkedList<>();
			classCodes.add("package " + className.substring(0, className.lastIndexOf(".")) + ";");
			classCodes.add("import org.junit.Test;");
			Class<?> classAnnotation = classAnnnotation(clazz);
			UnitTestClassWriter unitTestClassWriter = unitTestClassWriters.get(classAnnotation);
			unitTestClassWriter = unitTestClassWriter == null ? unitTestClassWriters.get(null) : unitTestClassWriter;// If the specific unit test class writer is not found, use the default unit test class writer.
			classCodes.addAll(unitTestClassWriter.write());
			classCodes.add(keywordCount(classCodes, "package", "import"), "public class " + clazz.getSimpleName() + "Test {");// Put the class signature in the right place.
			CodeWriter codeWriter = new CodeWriter();
			StringBuilder completeCodes = new StringBuilder();
			codeWriter.writeCodes(classCodes, completeCodes);
			// Generate unit test method codes.
			UnitTestMethodWriter unitTestMethodWriter = unitTestMethodWriters.get(classAnnotation);
			unitTestMethodWriter = unitTestMethodWriter == null ? unitTestMethodWriters.get(null) : unitTestMethodWriter;// If the specific unit test method writer is not found, use the default unit test method writer.
			for (Method method : clazz.getDeclaredMethods()) {
				List<String> methodCodes = new LinkedList<>();
				methodCodes.add("@Test");
				methodCodes.addAll(unitTestMethodWriter.write(method));
				methodCodes.add(keywordCount(methodCodes, "@"), "public void test" + capitalizedFirstCharacter(method.getName()) + "() {");
				methodCodes.add("}");
				codeWriter.writeCodes(methodCodes, completeCodes);
				completeCodes.append("\n");
			}
			completeCodes.append("}");
			// Write unit test file.
			String unitTestFilePath = entry.getKey().toString().replace("\\", "/").replace("src/main/java", "src/test/java").replace(".java", "Test.java");
			File unitTestFileDirectory = new File(unitTestFilePath.substring(0, unitTestFilePath.lastIndexOf("/")));
			if (!unitTestFileDirectory.exists()) {
				unitTestFileDirectory.mkdirs();
			}
			File unitTestFile = new File(unitTestFilePath);
			if (unitTestFile.exists() && !overwrite) {
				System.out.println("The file " + unitTestFilePath + " already exists.");
				return;
			}
			try (PrintWriter printWriter = new PrintWriter(unitTestFile)) {
				printWriter.write(completeCodes.toString());
				System.out.println("The unit test file " + unitTestFilePath + " is successfully generated.");
			} catch (FileNotFoundException e) {
				System.out.println("Failed to write the file " + unitTestFilePath + ".");
			}
			System.out.println(completeCodes + "\n");					
		}
	}
	
	public String capitalizedFirstCharacter(String string) {
		return string.substring(0, 1).toUpperCase() + string.substring(1);
	}
}
