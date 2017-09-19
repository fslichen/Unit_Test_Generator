package evolution;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
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
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import evolution.template.UnitTestClassWriter;
import evolution.template.UnitTestMethodWriter;

public class UnitTestGenerator {
	public static final String SRC_MAIN_JAVA = "src/main/java";
	public static final String SRC_TEST_JAVA = "src/test/java"; 
	
	public String upperFirstCharacter(String string) {
		return string.substring(0, 1).toUpperCase() + string.substring(1);
	}
	
	public String lowerFirstCharacter(String string) {
		return string.substring(0, 1).toLowerCase() + string.substring(1);
	}
	
	public String pathInString(Path path) {
		return pathInString(path.toString());
	}
	
	public String pathInString(String path) {
		return path.replace("\\", "/");
	}
	
	public Map<Path, Class<?>> classesUnderBasePackageOfSrcMainJava(final String basePackage, final Predicate<Class<?>> predicate) throws Exception {
		final Map<Path, Class<?>> classes = new LinkedHashMap<>();
		try (Stream<Path> paths = Files.walk(Paths.get(System.getProperty("user.dir")))) {
			paths.filter(new Predicate<Path>() {
				@Override
				public boolean test(Path path) {
					String pathInString = pathInString(path);
					if (!pathInString.contains(SRC_MAIN_JAVA) || (basePackage != null && !pathInString.contains(basePackage.replace(".", "/"))) || !withExtension(pathInString, "java")) {
						return false;
					}
					return true;
				}}).forEach(new Consumer<Path>() {
				@Override
				public void accept(Path path) {
					String pathInString = pathInString(path);
					String className = pathInString.substring(pathInString.lastIndexOf(SRC_MAIN_JAVA) + SRC_MAIN_JAVA.length() + 1).replace("/", ".").replace(".java", "");
					try {
						Class<?> clazz = Class.forName(className);
						if (predicate == null || predicate.test(clazz)) {
							classes.put(path, clazz);
						}
					} catch (ClassNotFoundException e) {
						System.out.println("Unable to determine the class " + className + ".");
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
	
	@SuppressWarnings("unchecked")
	public <T> T newInstance(Class<T> clazz, WebApplicationContext webApplicationContext) throws InstantiationException, IllegalAccessException {
		T currentInstance = null;
		if (webApplicationContext == null) {
			return clazz.newInstance();
		} else {
			currentInstance = (T) webApplicationContext.getBean(lowerFirstCharacter(clazz.getSimpleName()));
			return currentInstance == null ? currentInstance = clazz.newInstance() : currentInstance; 
		}
	}
	
	public void invokeMethodsUnderBasePackageUnderSrcMainJavaAndGetMockedParameterValuesAndReturnValues(String basePackage, Predicate<Class<?>> predicate, WebApplicationContext webApplicationContext) throws Exception {
		for (Entry<Path, Class<?>> entry : classesUnderBasePackageOfSrcMainJava(basePackage, predicate).entrySet()) {
			Class<?> clazz = entry.getValue();
			String jsonDirectoryPath = pathInString(entry.getKey()).replace("src/main/java", "src/test/java").replace(".java", "");
			int index = jsonDirectoryPath.lastIndexOf("/");
			jsonDirectoryPath = jsonDirectoryPath.substring(0, index + 1) + lowerFirstCharacter(jsonDirectoryPath.substring(index + 1));
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
				ObjectMapper objectMapper = new ObjectMapper();
				String jsonFileBasePath = jsonDirectoryPath + "/" + method.getName();
				objectMapper.writeValue(createDirectoriesAndFile(jsonFileBasePath + "Request.json"), parameterValues);
				objectMapper.writeValue(createDirectoriesAndFile(jsonFileBasePath + "Response.json"), method.invoke(newInstance(clazz, webApplicationContext), parameterValues4InvokingMethod));
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
	
	public void scanClassesUnderBasePackageOfSrcMainJavaAndGenerateUnitTestClassesUnderSrcTestJava(String basePackage, Predicate<Class<?>> predicate, final boolean overwrite, final UnitTestClassWriter unitTestClassWriter, final UnitTestMethodWriter unitTestMethodWriter) throws Exception {
		for (Entry<Path, Class<?>> entry : classesUnderBasePackageOfSrcMainJava(basePackage, predicate).entrySet()) {
			// Generate unit test class related codes.
			Class<?> clazz = entry.getValue();
			CodeWriter codeWriter = new CodeWriter();
			List<String> classCodes = new LinkedList<>();
			codeWriter.writePackage(clazz, classCodes);
			codeWriter.writeImport(Test.class, classCodes);
			codeWriter.writeImport(clazz, classCodes);
			classCodes.add(codeWriter.writeClass(clazz, "Test"));// Put the class signature in the right place.
			codeWriter.writeAnnotation(Autowired.class, classCodes);
			String instanceName = codeWriter.writeField(clazz, classCodes);
			classCodes.add(null);
			classCodes.addAll(unitTestClassWriter.write());
			// Generate unit test method codes.
			for (Method method : clazz.getDeclaredMethods()) {
				codeWriter.writeAnnotation(Test.class, classCodes);
				classCodes.addAll(unitTestMethodWriter.write(method));
				codeWriter.writeMethod(method, "test", classCodes);
				classCodes.add("ObjectMapperPlus objectMapperPlus = new ObjectMapperPlus();");
				classCodes.add("List<String> jsons = objectMapperPlus.splitJson(requestData);");
				StringBuilder parameters = new StringBuilder();
				int i = 0;
				for (Class<?> parameterType : method.getParameterTypes()) {
					parameters.append(String.format("objectMapperPlus.fromJson(jsons.get(%s), %s.class), ", i, parameterType.getSimpleName()));
					i++;
				}
				String s = parameters.toString();
				if (s.length() > 2) {
					s = parameters.substring(0, parameters.length() - 2);
				}
				classCodes.add(instanceName + "." + method.getName() + "(" + s +  ");");
				classCodes.add("}");
				codeWriter.writeBlankLine(classCodes);
			}
			classCodes.add("}");
			// Write unit test file.
			String unitTestFilePath = pathInString(entry.getKey()).replace("src/main/java", "src/test/java").replace(".java", "Test.java");
			File unitTestFile = createDirectoriesAndFile(unitTestFilePath);
			if (unitTestFile.exists() && !overwrite) {
				System.out.println("The file " + unitTestFilePath + " already exists.");
				return;
			}
			try (PrintWriter printWriter = new PrintWriter(unitTestFile)) {
				printWriter.write(codeWriter.writeCodes(classCodes).toString());
				System.out.println("The unit test file " + unitTestFilePath + " is successfully generated.");
			} catch (FileNotFoundException e) {
				System.out.println("Failed to write the file " + unitTestFilePath + ".");
			}
		}
	}
	
	public File createDirectoriesAndFile(String filePath) {
		File fileDirectory = new File(filePath.substring(0, filePath.lastIndexOf("/")));
		if (!fileDirectory.exists()) {
			fileDirectory.mkdirs();
		}
		return new File(filePath);
	}
	
	public boolean withExtension(String path, String extension) {
		return extension.equals(FilenameUtils.getExtension(pathInString(path)));
	}
	
	public boolean withExtension(Path path, String extension) {
		return withExtension(path.toString(), extension);
	}
}
