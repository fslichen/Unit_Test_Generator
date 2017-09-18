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

import com.fasterxml.jackson.databind.ObjectMapper;

import evolution.pojo.ParameterValuesAndReturnValue;
import evolution.template.UnitTestClassWriter;
import evolution.template.UnitTestMethodWriter;

public class UnitTestGenerator {
	public static final String SRC_MAIN_JAVA = "src/main/java";
	public static final String SRC_TEST_JAVA = "src/test/java"; 
	
	public String capitalizedFirstCharacter(String string) {
		return string.substring(0, 1).toUpperCase() + string.substring(1);
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
	
	public Map<Path, Class<?>> classesUnderBasePackageOfSrcMainJava(final String basePackage, final Predicate<Class<?>> predicate) throws IOException {
		final Map<Path, Class<?>> classes = new LinkedHashMap<>();
		try (Stream<Path> paths = Files.walk(Paths.get(System.getProperty("user.dir")))) {
			paths.filter(new Predicate<Path>() {
				@Override
				public boolean test(Path path) {
					String pathInString = path.toString().replace("\\", "/");
					if (!pathInString.contains(SRC_MAIN_JAVA) || (basePackage != null && !pathInString.contains(basePackage.replace(".", "/"))) || !withExtension(pathInString, "java")) {
						return false;
					}
					return true;
				}}).forEach(new Consumer<Path>() {
				@Override
				public void accept(Path path) {
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
	
	@SuppressWarnings("unchecked")
	public <T> T copyObject(T t) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			return (T) objectMapper.readValue(objectMapper.writeValueAsString(t), t.getClass());
		} catch (Exception e) {
			System.out.println("Unable to copy the object.");
			return null;
		}
	}
	
	public void invokeMethodsUnderBasePackageUnderSrcMainJavaAndGetMockedParameterValuesAndReturnValues(String basePackage, Predicate<Class<?>> predicate) throws Exception {
		Map<Path, Class<?>> map = classesUnderBasePackageOfSrcMainJava(basePackage, predicate);
		for (Entry<Path, Class<?>> entry : map.entrySet()) {
			Class<?> clazz = entry.getValue();
			for (Method method : clazz.getDeclaredMethods()) {
				int i = 0;
				ObjectMocker mocker = new ObjectMocker();
				List<Object> parameterValues = new LinkedList<>();
				Object[] parameterValues4InvokingMethod = new Object[method.getParameterCount()];
				for (Class<?> parameterType : method.getParameterTypes()) {
					Object parameterValue = mocker.mockObject(parameterType);
					parameterValues.add(copyObject(parameterValue));
					parameterValues4InvokingMethod[i++] = parameterValue;
				}
				ParameterValuesAndReturnValue result = new ParameterValuesAndReturnValue();
				result.setParameterValues(parameterValues);
				result.setReturnValue(method.invoke(clazz.newInstance(), parameterValues4InvokingMethod));
				System.out.println(result);
			}
		}
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
	
	public void scanClassesUnderBasePackageOfSrcMainJavaAndGenerateUnitTestClassesUnderSrcTestJava(String basePackage, final boolean overwrite, final UnitTestClassWriter unitTestClassWriter, final UnitTestMethodWriter unitTestMethodWriter) throws IOException {
		for (Entry<Path, Class<?>> entry : classesUnderBasePackageOfSrcMainJava(basePackage, null).entrySet()) {
			// Generate unit test class related codes.
			Class<?> clazz = entry.getValue();
			String className = clazz.getName();
			List<String> classCodes = new LinkedList<>();
			classCodes.add("package " + className.substring(0, className.lastIndexOf(".")) + ";");
			classCodes.add("import org.junit.Test;");
			classCodes.addAll(unitTestClassWriter.write());
			classCodes.add(keywordCount(classCodes, "package", "import"), "public class " + clazz.getSimpleName() + "Test {");// Put the class signature in the right place.
			CodeWriter codeWriter = new CodeWriter();
			StringBuilder completeCodes = new StringBuilder();
			codeWriter.writeCodes(classCodes, completeCodes);
			// Generate unit test method codes.
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
	
	public boolean withExtension(String path, String extension) {
		return extension.equals(FilenameUtils.getExtension(path.toString().replace("\\", "/")));
	}
}
