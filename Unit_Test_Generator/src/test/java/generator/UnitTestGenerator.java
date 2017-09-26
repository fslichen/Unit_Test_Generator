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
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import generator.pojo.ComponentDto;
import generator.pojo.ControllerDto;
import generator.pojo.ControllerMethodPojo;
import generator.pojo.SpecialParameterValue;
import generator.pojo.VoidReturnValue;
import generator.template.ReflectionAssert;
import generator.template.UnitTestClassWriter;
import generator.template.UnitTestMethodWriter;

public class UnitTestGenerator {
	public static final String SRC_MAIN_JAVA = "src/main/java";
	public static final String SRC_TEST_JAVA = "src/test/java"; 
	
	public Map<String, Integer> caseCountsByMethod(File file) throws IOException {
		int caseCount = 0;
		String methodName = null;
		Map<String, Integer> caseCountByMethod = new LinkedHashMap<>();
		Set<String> conditionStatements = new HashSet<>(Arrays.asList("if (", "if(", "else if (", "else if(", "else {", "else{"));
		for (String code : FileUtils.readLines(file, "UTF-8")) {
			code = code.trim();
			if (Pointer.isMethod(code)) {
				caseCount = 1;
				int leftBracketIndex = code.indexOf("(");
				for (int i = leftBracketIndex - 1; i >= 0; i--) {
					if (code.charAt(i) == ' ') {
						methodName = code.substring(i + 1, leftBracketIndex);
						break;
					}
				}
			} else if (code.endsWith("{")) {
				for (String conditionStatement : conditionStatements) {
					if (code.contains(conditionStatement)) {
						caseCount++;
						break;
					}
				}
			} else if (code.endsWith("}")) {
				caseCountByMethod.put(methodName, caseCount);
			}
		}
		return caseCountByMethod;
	}
	
	public Map<Path, Class<?>> classesUnderBasePackageOfSrcMainJava(final String basePackage, final Predicate<Class<?>> classFilter) throws Exception {
		final Map<Path, Class<?>> classes = new LinkedHashMap<>();
		try (Stream<Path> paths = Files.walk(Paths.get(System.getProperty("user.dir")))) {
			paths.filter(new Predicate<Path>() {
				@Override
				public boolean test(Path path) {
					String pathInString = Lang.pathInString(path);
					if (!pathInString.contains(SRC_MAIN_JAVA) || (basePackage != null && !pathInString.contains(basePackage.replace(".", "/"))) || !Lang.withExtension(pathInString, "java")) {
						return false;
					}
					return true;
				}}).forEach(new Consumer<Path>() {
				@Override
				public void accept(Path path) {
					String pathInString = Lang.pathInString(path);
					String className = pathInString.substring(pathInString.lastIndexOf(SRC_MAIN_JAVA) + SRC_MAIN_JAVA.length() + 1).replace("/", ".").replace(".java", "");
					try {
						Class<?> clazz = Class.forName(className);
						if (classFilter == null || classFilter.test(clazz)) {
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
	
	public ControllerDto controllerDto(Method method, Object data) {
		ControllerDto controllerDto = new ControllerDto();
		controllerDto.setSession(UUID.randomUUID().toString());
		controllerDto.setData(data);
		GetMapping getMapping = method.getAnnotation(GetMapping.class);
		PostMapping postMapping = method.getAnnotation(PostMapping.class);
		RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
		if (getMapping != null) {
			controllerDto.setRequestMethod("GET");
			controllerDto.setRequestPath(getMapping.value()[0]);
		} else if (postMapping != null) {
			controllerDto.setRequestMethod("POST");
			controllerDto.setRequestPath(postMapping.value()[0]);
		} else if (requestMapping != null) {
			RequestMethod[] requestMethods = requestMapping.method();
			if (requestMethods.length > 0) {
				controllerDto.setRequestMethod(requestMapping.method()[0].toString());
			} else {
				controllerDto.setRequestMethod("ANY");
			}
			controllerDto.setRequestPath(requestMapping.value()[0]);
		}
		return controllerDto;
	}
	
	public File createDirectoriesAndFile(String filePath) {
		File fileDirectory = new File(filePath.substring(0, filePath.lastIndexOf("/")));
		if (!fileDirectory.exists()) {
			fileDirectory.mkdirs();
		}
		return new File(filePath);
	}
	
	public void invokeMethodsUnderBasePackageOfSrcMainJavaAndGenerateUseCasesUnderSrcTestJava(String basePackage, Predicate<Class<?>> classFilter, WebApplicationContext webApplicationContext) throws Exception {
		for (Entry<Path, Class<?>> entry : classesUnderBasePackageOfSrcMainJava(basePackage, classFilter).entrySet()) {
			Class<?> clazz = entry.getValue();
			String jsonDirectoryPath = Lang.pathInString(entry.getKey()).replace("src/main/java", "src/test/java").replace(".java", "");
			int index = jsonDirectoryPath.lastIndexOf("/");
			jsonDirectoryPath = jsonDirectoryPath.substring(0, index + 1) + Lang.lowerFirstCharacter(jsonDirectoryPath.substring(index + 1));
			int maxUseCaseCount = Lang.property("max-use-case-count", Integer.class);
			Map<String, Integer> useCaseCountsByMethod = caseCountsByMethod(entry.getKey().toFile());
			boolean isController = clazz.getAnnotation(Controller.class) != null || clazz.getAnnotation(RestController.class) != null;
			for (Method method : clazz.getDeclaredMethods()) {
				for (int useCaseIndex = 0; useCaseIndex < safeCaseCount(method, useCaseCountsByMethod, maxUseCaseCount); useCaseIndex++) {
					// Request Data
					int i = 0;
					Object[] parameterValues = Mocker.mockParameterValues(method);
					Object[] parameterValues4InvokingMethod = new Object[parameterValues.length];
					for (Object parameterValue : parameterValues) {
						if (parameterValue == null) {
							parameterValue = new SpecialParameterValue(method.getParameterTypes()[i]);
						}
						parameterValues4InvokingMethod[i++] = Json.copyObject(parameterValue);
					}
					ObjectMapper objectMapper = new ObjectMapper();
					String jsonFileBasePath = jsonDirectoryPath + "/" + method.getName();
					File requestJsonFile = createDirectoriesAndFile(jsonFileBasePath + "Request" + useCaseIndex + ".json");
					if (!requestJsonFile.exists() || Lang.property("overwrite-use-case", Boolean.class)) {
						if (isController) {
							objectMapper.writeValue(requestJsonFile, controllerDto(method, parameterValues));
						} else {
							objectMapper.writeValue(requestJsonFile, new ComponentDto(parameterValues, "Success"));
						}
					} else {
						System.out.println("The file " + requestJsonFile.getAbsolutePath() + " already exists.");
					}
					// Response Data
					Object returnValue = null;
					Class<?> returnType = method.getReturnType();
					String responseStatus = "Success";
					if (returnType == void.class || returnType == Void.class) {
						returnValue = new VoidReturnValue("OK", "void");
					} else {
						try {
							returnValue = method.invoke(newInstance(clazz, webApplicationContext), parameterValues4InvokingMethod);// The method invocation may fail due to failing to start WebApplicationContext, coding errors within method, exceptions caused by boundary conditions, or calling remote services.
						} catch (Exception e) {
							responseStatus = "Mocked";
							returnValue = Mocker.mockReturnValue(method);
						}
					}
					File responseJsonFile = createDirectoriesAndFile(jsonFileBasePath + "Response" + useCaseIndex + ".json");
					if (!responseJsonFile.exists() || Lang.property("overwrite-use-case", Boolean.class)) {
						objectMapper.writeValue(responseJsonFile, new ComponentDto(returnValue, responseStatus));
					} else {
						System.out.println("The file " + responseJsonFile.getAbsolutePath() + " already exists.");
					}
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T> T newInstance(Class<T> clazz, WebApplicationContext webApplicationContext) throws InstantiationException, IllegalAccessException {
		T currentInstance = null;
		if (webApplicationContext == null) {
			return clazz.newInstance();
		} else {
			currentInstance = (T) webApplicationContext.getBean(Lang.lowerFirstCharacter(clazz.getSimpleName()));
			return currentInstance == null ? clazz.newInstance() : currentInstance; 
		}
	}
		
	public int safeCaseCount(Method method, Map<String, Integer> caseCountsByMethod, int maxCaseCount) {
		Integer methodCaseCount = caseCountsByMethod.get(method.getName());
		if (methodCaseCount == null) {
			return 1;
		} 
		return Math.min(methodCaseCount, maxCaseCount);
	}
	
	public void scanClassesUnderBasePackageOfSrcMainJavaAndGenerateTestCasesUnderSrcTestJava(String basePackage, Predicate<Class<?>> classFilter, final Map<Class<?>, UnitTestClassWriter> unitTestClassWriters, final Map<Class<?>, UnitTestMethodWriter> unitTestMethodWriters) throws Exception {
		for (Entry<Path, Class<?>> entry : classesUnderBasePackageOfSrcMainJava(basePackage, classFilter).entrySet()) {
			// Generate unit test class related codes.
			Class<?> clazz = entry.getValue();
			CodeWriter codeWriter = new CodeWriter();
			codeWriter.writePackage(clazz);
			codeWriter.writeImport(Test.class);
			codeWriter.writeClass(clazz, "Test", BaseTestCase.class);
			codeWriter.writeAnnotation(Autowired.class);
			codeWriter.writeField(clazz);
			codeWriter.writeBlankLine();
			Class<?> classAnnotationType = Pointer.classAnnotationType(clazz);
			UnitTestClassWriter unitTestClassWriter = unitTestClassWriters.get(classAnnotationType);
			if (unitTestClassWriter == null) {
				unitTestClassWriter = unitTestClassWriters.get(null);
			}
			codeWriter.writeCodes(unitTestClassWriter.write());
			// Generate unit test method related codes.
			int maxTestCaseCount = Lang.property("max-test-case-count", Integer.class);
			Map<String, Integer> testCaseCountsByMethod = caseCountsByMethod(entry.getKey().toFile());
			UnitTestMethodWriter unitTestMethodWriter = unitTestMethodWriters.get(classAnnotationType);
			if (unitTestMethodWriter == null) {
				unitTestMethodWriter = unitTestMethodWriters.get(null);
			}
			for (Method method : clazz.getDeclaredMethods()) {
				for (int methodIndex = 0; methodIndex < safeCaseCount(method, testCaseCountsByMethod, maxTestCaseCount); methodIndex++) {
					codeWriter.writeAnnotation(Test.class);
					codeWriter.writeCodes(unitTestMethodWriter.write(method));
					codeWriter.writeMethod(method, "test", "WithTypes" + Pointer.concatenateParameterTypeSimpleNames(method) + Pointer.returnTypeSimpleName(method) + methodIndex, Exception.class);
					codeWriter.writeImport(Json.class);
					if (classAnnotationType == Controller.class) {
						writeCodes4InvokingControllerMethod(clazz, method, codeWriter);
					} else if (method.getModifiers() == Modifier.PRIVATE) {
						writeCodes4InvokingPrivateMethod(clazz, method, codeWriter);
					} else {
						writeCodes4InvokingOrdinaryMethod(clazz, method, codeWriter);
					}
					codeWriter.writeRightCurlyBrace();
					codeWriter.writeBlankLine();
				}
			}
			codeWriter.writeRightCurlyBrace();
			// Write unit test file.
			File unitTestFile = createDirectoriesAndFile(Lang.pathInString(entry.getKey()).replace("src/main/java", "src/test/java").replace(".java", "Test.java"));
			if (!unitTestFile.exists() || Lang.property("overwrite-test-case", Boolean.class)) {
				try (PrintWriter printWriter = new PrintWriter(unitTestFile)) {
					printWriter.write(codeWriter.generateCodes().toString());
					System.out.println("The unit test file " + unitTestFile.getAbsolutePath() + " is successfully generated.");
				} catch (FileNotFoundException e) {
					System.out.println("The file " + unitTestFile.getAbsolutePath() + " is not found.");
				}
			} else {
				System.out.println("The file " + unitTestFile.getAbsolutePath() + " already exists.");
			}
		}
	}
	
	public void writeCodes4InvokingControllerMethod(Class<?> clazz, Method method, CodeWriter codeWriter) {
		ControllerMethodPojo pojo = Pointer.controllerMethodPojo(clazz, method);
		RequestMethod requestMethod = pojo.getRequestMethod();
		codeWriter.writeImport(MockMvcRequestBuilders.class);
		codeWriter.writeImport(MediaType.class);
		codeWriter.writeStaticImport(MockMvcResultMatchers.class);
		if (requestMethod == RequestMethod.POST) {
			String code = null;
			if (method.getParameterCount() > 0) {
				writeCodes4PreparingParameterValues(method, codeWriter, false);
				code = String.format("mockMvc.perform(MockMvcRequestBuilders.post(%s).content(parameterValues.get(0)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())", "\"" + pojo.getRequestPath() + "\"");
			} else {
				code = String.format("mockMvc.perform(MockMvcRequestBuilders.post(%s).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())", "\"" + pojo.getRequestPath() + "\"");
			}
			if (pojo.getReturnType() != void.class && pojo.getReturnType() != Void.class) {
				code += ".andExpect(content().json(Json.subJson(responseData, \"data\"), false))";
			}
			codeWriter.writeCode(code + ";");
		} else if (requestMethod == RequestMethod.GET) {
			codeWriter.writeCode(String.format("mockMvc.perform(MockMvcRequestBuilders.get(%s)).andExpect(status().isOk());", "\"" + pojo.getRequestPath() + "\""));
		} else {
			if (method.getModifiers() == Modifier.PRIVATE) {
				writeCodes4InvokingPrivateMethod(clazz, method, codeWriter);
			} else {
				writeCodes4InvokingOrdinaryMethod(clazz, method, codeWriter);
			}
		}
	}
	
	public void writeCodes4InvokingOrdinaryMethod(Class<?> clazz, Method method, CodeWriter codeWriter) {
		Class<?> returnType = method.getReturnType();
		String parametersInString = method.getParameterCount() > 0 ? writeCodes4PreparingParameterValues(method, codeWriter, true) : "";
		if (returnType == void.class || returnType == Void.class) {
			codeWriter.writeCode(String.format("%s.%s(%s);", Pointer.instanceName(clazz), method.getName(), parametersInString));
		} else {
			codeWriter.writeCode(String.format("%s actualResult = %s.%s(%s);", Pointer.simpleReturnTypeName(method, codeWriter), Pointer.instanceName(clazz), method.getName(), parametersInString));
			codeWriter.writeCode(String.format("%s expectedResult = Json.fromSubJson(responseData, \"data\", %s.class);", Pointer.simpleReturnTypeName(method, codeWriter), returnType.getSimpleName()));
			codeWriter.writeImport(ReflectionAssert.class);
			codeWriter.writeCode("ReflectionAssert.assertReflectionEquals(actualResult, expectedResult);");
		}
	}
	
	public void writeCodes4InvokingPrivateMethod(Class<?> clazz, Method method, CodeWriter codeWriter) {
		StringBuilder parameterTypesBuilder = new StringBuilder();
		for (Class<?> parameterType : method.getParameterTypes()) {
			codeWriter.writeImport(parameterType);
			parameterTypesBuilder.append(parameterType.getSimpleName() + ".class" + ", ");
		}
		codeWriter.writeImport(Method.class);
		codeWriter.writeCode("try {");
		int parameterCount = method.getParameterCount();
		if (parameterCount > 0) {
			codeWriter.writeCode(String.format("Method method = %s.class.getDeclaredMethod(\"%s\", %s);", clazz.getSimpleName(), method.getName(), Lang.trimEndingComma(parameterTypesBuilder)));
		} else {
			codeWriter.writeCode(String.format("Method method = %s.class.getDeclaredMethod(\"%s\");", clazz.getSimpleName(), method.getName()));
		}
		codeWriter.writeCode("method.setAccessible(true);");
		Class<?> returnType = method.getReturnType();
		if (returnType == void.class || returnType == Void.class) {
			if (parameterCount > 0) {
				codeWriter.writeCode(String.format("method.invoke(%s, %s);", Pointer.instanceName(clazz), writeCodes4PreparingParameterValues(method, codeWriter, true)));
			} else {
				codeWriter.writeCode(String.format("method.invoke(%s);", Pointer.instanceName(clazz)));
			}
		} else {
			String returnTypeSimpleName = returnType.getSimpleName();
			if (parameterCount > 0) {
				codeWriter.writeCode(String.format("%s actualResult = (%s) method.invoke(%s, %s);", Pointer.simpleReturnTypeName(method, codeWriter), returnTypeSimpleName, Pointer.instanceName(clazz), writeCodes4PreparingParameterValues(method, codeWriter, true)));
			} else {
				codeWriter.writeCode(String.format("%s actualResult = (%s) method.invoke(%s);", Pointer.simpleReturnTypeName(method, codeWriter), returnTypeSimpleName, Pointer.instanceName(clazz)));
			}
			codeWriter.writeCode(String.format("%s expectedResult = Json.fromSubJson(responseData, \"data\", %s.class);", Pointer.simpleReturnTypeName(method, codeWriter), returnType.getSimpleName()));
			codeWriter.writeImport(ReflectionAssert.class);
			codeWriter.writeCode("ReflectionAssert.assertReflectionEquals(actualResult, expectedResult);");
		}
		codeWriter.writeCode("} catch (Exception e){}");
	}
	
	public String writeCodes4PreparingParameterValues(Method method, CodeWriter codeWriter, boolean writeImports4Parameters) {
		StringBuilder parametersBuilder = new StringBuilder();
		codeWriter.writeImport(List.class);
		codeWriter.writeCode("List<String> parameterValues = Json.splitSubJsons(requestData, \"data\");");
		int i = 0;
		for (Class<?> parameterType : method.getParameterTypes()) {
			if (writeImports4Parameters) {
				codeWriter.writeImport(parameterType);
			}
			parametersBuilder.append(String.format("Json.fromJson(parameterValues.get(%s), %s.class), ", i++, parameterType.getSimpleName()));
		}
		return Lang.trimEndingComma(parametersBuilder);
	}
}
