package generator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.apache.commons.io.FilenameUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import generator.pojo.CommonDto;
import generator.pojo.ControllerDto;
import generator.pojo.MethodReturnValue;
import generator.template.UnitTestClassWriter;
import generator.template.UnitTestMethodWriter;

public class UnitTestGenerator {
	public static final String SRC_MAIN_JAVA = "src/main/java";
	public static final String SRC_TEST_JAVA = "src/test/java"; 
	
	@SuppressWarnings("unchecked")
	public <T> T property(String key, Class<T> clazz) {
		Properties properties = new Properties();
		try {
			properties.load(UnitTestGenerator.class.getResourceAsStream("/unit-test.properties"));
			String value = properties.get(key).toString();
			if (clazz == int.class || clazz == Integer.class) {
				return (T) new Integer(value);
			} if (clazz == double.class || clazz == Double.class) {
				return (T) new Double(value);
			} if (clazz == boolean.class || clazz == Boolean.class) {
				return (T) new Boolean(value);
			}
			return (T) value;
		} catch (IOException e) {
			System.out.println("Please create unit-test.properties under src/test/resources");
			return null;
		}
	}
	
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
	
	public int caseCount(Method method, Map<String, Integer> caseCountByMethod, int maxCaseCount) {
		Integer methodCaseCount = caseCountByMethod.get(method.getName());
		if (methodCaseCount == null) {
			return 1;
		} 
		return Math.min(methodCaseCount, maxCaseCount);
	}
	
	public void invokeMethodsUnderBasePackageOfSrcMainJavaAndGetMockedParameterValuesAndReturnValues(String basePackage, Predicate<Class<?>> predicate, WebApplicationContext webApplicationContext) throws Exception {
		for (Entry<Path, Class<?>> entry : classesUnderBasePackageOfSrcMainJava(basePackage, predicate).entrySet()) {
			Class<?> clazz = entry.getValue();
			String jsonDirectoryPath = pathInString(entry.getKey()).replace("src/main/java", "src/test/java").replace(".java", "");
			int index = jsonDirectoryPath.lastIndexOf("/");
			jsonDirectoryPath = jsonDirectoryPath.substring(0, index + 1) + lowerFirstCharacter(jsonDirectoryPath.substring(index + 1));
			int maxUseCaseCount = property("max-use-case-count", Integer.class);
			Map<String, Integer> useCaseCountByMethod = ClassStatistics.useCaseCountByMethod(entry.getKey().toFile());
			boolean isController = clazz.getAnnotation(Controller.class) != null || clazz.getAnnotation(RestController.class) != null;
			for (Method method : clazz.getDeclaredMethods()) {
				for (int methodIndex = 0; methodIndex < caseCount(method, useCaseCountByMethod, maxUseCaseCount); methodIndex++) {
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
					File requestJsonFile = createDirectoriesAndFile(jsonFileBasePath + "Request" + methodIndex + ".json");
					if (!requestJsonFile.exists() || property("overwrite-use-case", Boolean.class)) {
						if (isController) {
							objectMapper.writeValue(requestJsonFile, controllerDto(method, parameterValues));
						} else {
							objectMapper.writeValue(requestJsonFile, new CommonDto(parameterValues));
						}
					} else {
						System.out.println("The file " + requestJsonFile.getAbsolutePath() + " already exists.");
					}
					Object returnValue = null;
					Class<?> returnType = method.getReturnType();
					if (returnType == void.class || returnType == Void.class) {
						returnValue = new MethodReturnValue("OK", "void");
					} else {
						try {
							returnValue = method.invoke(newInstance(clazz, webApplicationContext), parameterValues4InvokingMethod);
						} catch (Exception e) {
							returnValue = method.getReturnType().newInstance();
						}
					}
					File responseJsonFile = createDirectoriesAndFile(jsonFileBasePath + "Response" + methodIndex + ".json");
					if (!responseJsonFile.exists() || property("overwrite-use-case", Boolean.class)) {
						objectMapper.writeValue(responseJsonFile, returnValue);
					} else {
						System.out.println("The file " + responseJsonFile.getAbsolutePath() + " already exists.");
					}
				}
			}
		}
	}
	
	public ControllerDto controllerDto(Method method, Object data) {
		ControllerDto dto = new ControllerDto();
		dto.setSession(UUID.randomUUID().toString());
		dto.setData(data);
		GetMapping getMapping = method.getAnnotation(GetMapping.class);
		PostMapping postMapping = method.getAnnotation(PostMapping.class);
		RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
		if (getMapping != null) {
			dto.setRequestMethod("GET");
			dto.setRequestPath(getMapping.value()[0]);
		} else if (postMapping != null) {
			dto.setRequestMethod("POST");
			dto.setRequestPath(postMapping.value()[0]);
		} else if (requestMapping != null) {
			RequestMethod[] requestMethods = requestMapping.method();
			if (requestMethods.length > 0) {
				dto.setRequestMethod(requestMapping.method()[0].toString());
			} else {
				dto.setRequestMethod("ANY");
			}
			dto.setRequestPath(requestMapping.value()[0]);
		}
		return dto;
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
	
	public String instanceName(Class<?> clazz) {
		return lowerFirstCharacter(clazz.getSimpleName());
	}
	
	public String trimEndingComma(String string) {
		if (string.endsWith(", ")) {
			return string.substring(0, string.length() - 2);
		} else if (string.endsWith(",")) {
			return string.substring(0, string.length() - 1);
		}
		return string;
	}
	
	public void scanClassesUnderBasePackageOfSrcMainJavaAndGenerateUnitTestClassesUnderSrcTestJava(String basePackage, Predicate<Class<?>> predicate, final UnitTestClassWriter unitTestClassWriter, final UnitTestMethodWriter unitTestMethodWriter) throws Exception {
		for (Entry<Path, Class<?>> entry : classesUnderBasePackageOfSrcMainJava(basePackage, predicate).entrySet()) {
			// Generate unit test class related codes.
			Class<?> clazz = entry.getValue();
			CodeWriter codeWriter = new CodeWriter();
			codeWriter.writePackage(clazz);
			codeWriter.writeImport(Test.class);
			codeWriter.writeClass(clazz, "Test", BaseTest.class);
			codeWriter.writeAnnotation(Autowired.class);
			codeWriter.writeField(clazz);
			codeWriter.writeBlankLine();
			codeWriter.writeCodes(unitTestClassWriter.write());
			// Generate unit test method related codes.
			Map<String, Integer> caseCountByMethod = ClassStatistics.useCaseCountByMethod(entry.getKey().toFile());
			int maxTestCaseCount = property("max-test-case-count", Integer.class);
			for (Method method : clazz.getDeclaredMethods()) {
				for (int methodIndex = 0; methodIndex < caseCount(method, caseCountByMethod, maxTestCaseCount); methodIndex++) {
					codeWriter.writeAnnotation(Test.class);
					codeWriter.writeCodes(unitTestMethodWriter.write(method));
					codeWriter.writeMethod(method, "test", methodIndex);
					codeWriter.writeImport(Json.class);
					codeWriter.writeImport(List.class);
					codeWriter.writeCode("List<String> parameterValues = Json.splitJsonList(requestData, \"data\");");
					StringBuilder parametersBuilder = new StringBuilder();
					int i = 0;
					for (Class<?> parameterType : method.getParameterTypes()) {
						codeWriter.writeImport(parameterType);
						parametersBuilder.append(String.format("Json.fromJson(parameterValues.get(%s), %s.class), ", i++, parameterType.getSimpleName()));
					}
					String parameters = trimEndingComma(parametersBuilder.toString());
					Class<?> returnType = method.getReturnType();
					if (returnType != void.class && returnType != Void.class) {
						codeWriter.writeImport(returnType);
						String returnTypeSimpleName = returnType.getSimpleName();
						String instanceName = instanceName(clazz);
						if (method.getModifiers() == Modifier.PRIVATE) {
							// TODO Encapsulate the logic within a block.
							StringBuilder s = new StringBuilder();
							for (Class<?> parameterType : method.getParameterTypes()) {
								codeWriter.writeImport(parameterType);
								s.append(parameterType.getSimpleName() + ".class" + ", ");
							}
							codeWriter.writeImport(Method.class);
							codeWriter.writeCode("try {");
							codeWriter.writeCode(String.format("Method method = %s.class.getDeclaredMethod(\"%s\", %s);", clazz.getSimpleName(), method.getName(), trimEndingComma(s.toString())));
							codeWriter.writeCode("method.setAccessible(true);");
							codeWriter.writeCode(String.format("%s actualResult = (%s) method.invoke(%s, %s);", returnTypeSimpleName, returnTypeSimpleName, instanceName, parameters));
							codeWriter.writeCode("} catch (Exception e){}");
						} else {
							codeWriter.writeCode(String.format("%s actualResult = %s.%s(%s);", returnTypeSimpleName, instanceName, method.getName(), parameters));
						}
						codeWriter.writeCode(String.format("%s expectedResult = Json.fromJson(responseData, %s.class);", returnTypeSimpleName, returnTypeSimpleName));
					} else {
						if (method.getModifiers() == Modifier.PRIVATE) {
							// TODO Encapsulate the logic within a block.
							codeWriter.writeCode("method.setAccessible(true)");
							codeWriter.writeCode(String.format("method.invoke(%s, %s)", instanceName(clazz), parameters));
						} else {
							codeWriter.writeCode(String.format("%s.%s(%s);", instanceName(clazz), method.getName(), trimEndingComma(parameters.toString())));
						}
					}
					codeWriter.writeRightCurlyBrace();
					codeWriter.writeBlankLine();
				}
			}
			codeWriter.writeRightCurlyBrace();
			// Write unit test file.
			File unitTestFile = createDirectoriesAndFile(pathInString(entry.getKey()).replace("src/main/java", "src/test/java").replace(".java", "Test.java"));
			if (!unitTestFile.exists() || property("overwrite-test-case", Boolean.class)) {
				try (PrintWriter printWriter = new PrintWriter(unitTestFile)) {
					printWriter.write(codeWriter.generateCodes().toString());
					System.out.println("The unit test file " + unitTestFile.getAbsolutePath() + " is successfully generated.");
				} catch (FileNotFoundException e) {
					System.out.println("Failed to write the file " + unitTestFile.getAbsolutePath() + ".");
				}
			} else {
				System.out.println("The file " + unitTestFile.getAbsolutePath() + " already exists.");
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
