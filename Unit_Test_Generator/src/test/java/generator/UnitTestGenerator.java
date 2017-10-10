package generator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import evolution.annotation.Database4UcaseSetup;
import evolution.annotation.ExpectedDatabase4Ucase;
import generator.codeWriter.CodeWriter;
import generator.pointer.Pointer;
import generator.pointer.pojo.Dependency;
import generator.pojo.ComponentDto;
import generator.pojo.ControllerDto;
import generator.pojo.ControllerMethodPojo;
import generator.pojo.SpecialParameterValue;
import generator.pojo.VoidReturnValue;
import generator.template.ReflectionAssert;
import generator.template.TestCase;

public class UnitTestGenerator {
	public static final String SRC_MAIN_JAVA = "src/main/java";
	public static final String SRC_TEST_JAVA = "src/test/java"; 
	
	public Map<String, Integer> caseCountsByMethod(File file) throws IOException {
		int caseCount = 0;
		String methodName = null;
		Map<String, Integer> caseCountsByMethod = new LinkedHashMap<>();
		Set<String> conditionStatements = new HashSet<>(Arrays.asList("if (", "if(", "else if (", "else if(", "else {", "else{"));
		for (String code : FileUtils.readLines(file, "UTF-8")) {
			code = code.trim();
			if (Pointer.isMethod(code)) {
				caseCount = 1;
				methodName = Pointer.methodName(code);
			} else if (code.endsWith("{")) {
				for (String conditionStatement : conditionStatements) {
					if (code.contains(conditionStatement)) {
						caseCount++;
						break;
					}
				}
			} else if (code.endsWith("}")) {
				caseCountsByMethod.put(methodName, caseCount);
			}
		}
		return caseCountsByMethod;
	}
	
	public Map<Path, Class<?>> classesUnderBasePackageOfSrcMainJava(final String basePackage, final Predicate<Class<?>> classFilter) throws Exception {
		final Map<Path, Class<?>> classes = new LinkedHashMap<>();
		try (Stream<Path> paths = Files.walk(Paths.get(System.getProperty("user.dir")))) {
			paths.filter(new Predicate<Path>() {
				@Override
				public boolean test(Path path) {
					String pathInString = Lang.pathInString(path);
					return pathInString.contains(SRC_MAIN_JAVA) && (basePackage == null || pathInString.contains(basePackage.replace(".", "/"))) && Lang.withExtension(pathInString, "java");
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
						System.out.println(String.format("Unable to determine the class %s.", className));
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
			if (requestMapping.method().length > 0) {
				controllerDto.setRequestMethod(requestMapping.method()[0].toString());
			} else {
				controllerDto.setRequestMethod("ANY");
			}
			controllerDto.setRequestPath(requestMapping.value()[0]);
		}
		return controllerDto;
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
					File requestJsonFile = Lang.createDirectoriesAndFile(jsonFileBasePath + "Request" + useCaseIndex + ".json");
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
							method.setAccessible(true);
							returnValue = method.invoke(newInstance(clazz, webApplicationContext), parameterValues4InvokingMethod);// The method invocation may fail due to failing to start WebApplicationContext, coding errors within method, exceptions caused by boundary conditions, or calling remote services.
						} catch (Exception e) {
							responseStatus = "Mocked";
							returnValue = Mocker.mockReturnValue(method);
						}
					}
					File responseJsonFile = Lang.createDirectoriesAndFile(jsonFileBasePath + "Response" + useCaseIndex + ".json");
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
		if (webApplicationContext == null || !Lang.property("use-web-application-context", Boolean.class)) {
			return clazz.newInstance();
		} else {
			currentInstance = (T) webApplicationContext.getBean(Lang.lowerFirstCharacter(clazz.getSimpleName()));
			return currentInstance == null ? clazz.newInstance() : currentInstance; 
		}
	}
		
	public int safeCaseCount(Method method, Map<String, Integer> caseCountsByMethod, int maxCaseCount) {
		return Math.min(Optional.ofNullable(caseCountsByMethod).map(x -> x.get(method.getName())).orElse(1), maxCaseCount);
	}
	
	public void scanClassesUnderBasePackageOfSrcMainJavaAndGenerateTestCasesUnderSrcTestJava(String basePackage, Predicate<Class<?>> classFilter) throws Exception {
		for (Entry<Path, Class<?>> entry : classesUnderBasePackageOfSrcMainJava(basePackage, classFilter).entrySet()) {
			// Generate unit test class related codes.
			Path path = entry.getKey();
			Class<?> clazz = entry.getValue();
			CodeWriter codeWriter = new CodeWriter();
			codeWriter.writePackage(clazz);
			codeWriter.writeImport(Test.class);
			codeWriter.writeClass(clazz, "Test", BaseTestCase.class, null);
			codeWriter.writeField(clazz, Autowired.class);
			// Generate unit test method related codes.
			Class<?> classAnnotationType = Pointer.classAnnotationType(clazz);
			int maxTestCaseCount = Lang.property("max-test-case-count", Integer.class);
			Map<String, Integer> testCaseCountsByMethod = caseCountsByMethod(entry.getKey().toFile());
			for (Method method : clazz.getDeclaredMethods()) {
				String concatenatedTypeSimpleNames = Pointer.concatenateParameterTypeSimpleNames(method) + Pointer.returnTypeSimpleName(method);
				for (int methodIndex = 0; methodIndex < safeCaseCount(method, testCaseCountsByMethod, maxTestCaseCount); methodIndex++) {
					// Method Header
					codeWriter.writeImport(Json.class);
					codeWriter.writeImport(TestCase.class);
					codeWriter.writeMethod(method, "test", "WithTypes" + concatenatedTypeSimpleNames + methodIndex, Exception.class, Test.class, Database4UcaseSetup.class, ExpectedDatabase4Ucase.class);
					codeWriter.writeCode(method, "TestCase testCase = testCaseClient.getTestCase();");
					codeWriter.writeCode(method, "String requestData = testCase.getRequestData();");
					codeWriter.writeCode(method, "String responseData = testCase.getResponseData();");
					codeWriter.writeCode(method, "String mockedData = testCase.getMockData();");
					// Dependencies
					writeMockito4InvokingDependencyMethod(path, clazz, method, codeWriter);
					// Method Body
					if (classAnnotationType == Controller.class) {
						writeCodes4InvokingControllerMethod(clazz, method, codeWriter);
					} else if (method.getModifiers() == Modifier.PRIVATE) {
						writeCodes4InvokingPrivateMethod(clazz, method, codeWriter);
					} else {
						writeCodes4InvokingOrdinaryMethod(clazz, method, codeWriter);
					}
				}
			}
			// Write unit test file.
			File unitTestFile = Lang.createDirectoriesAndFile(Lang.pathInString(entry.getKey()).replace("src/main/java", "src/test/java").replace(".java", "Test.java"));
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
		codeWriter.writeImport(MediaType.class);
		codeWriter.writeStaticImport(MockMvcResultMatchers.class);
		codeWriter.writeStaticImport(MockMvcRequestBuilders.class);
		if (requestMethod == RequestMethod.POST) {
			String code = null;
			if (method.getParameterCount() > 0) {
				StringBuilder parametersBuilder = new StringBuilder();
				int i = 0;
				for (Parameter parameter : method.getParameters()) {
					if (parameter.getAnnotation(RequestBody.class) != null) {
						parametersBuilder.append(String.format("Json.toJson(requestData, \"data\", %s)", i++));
						break;
					}
				}
				if (parametersBuilder.length() == 0) {
					System.out.println(String.format("%s is missing request body.", method));
					parametersBuilder.append("\"The request body is missing.\"");
				}
				code = String.format("mockMvc.perform(post(%s).content(%s).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())", "\"" + pojo.getRequestPath() + "\"", parametersBuilder.toString());
			} else {
				code = String.format("mockMvc.perform(post(%s).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())", "\"" + pojo.getRequestPath() + "\"");
			}
			if (pojo.getReturnType() != void.class && pojo.getReturnType() != Void.class) {
				codeWriter.writeOutput(method, "{'data':", code + ".andReturn().getResponse().getContentAsString()", ",'status':'Success'}");
				code += String.format(".andExpect(content().json(Json.toJson(responseData, \"data\"), false))");
			}
			codeWriter.writeCode(method, code + ";");
		} else if (requestMethod == RequestMethod.GET) {
			codeWriter.writeCode(method, String.format("mockMvc.perform(get(%s)).andExpect(status().isOk());", "\"" + pojo.getRequestPath() + "\""));
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
		String parametersInString = method.getParameterCount() > 0 ? writeCodes4PreparingParameterValues(method, codeWriter) : "";
		if (returnType == void.class || returnType == Void.class) {
			codeWriter.writeCode(method, String.format("%s.%s(%s);", Pointer.instanceName(clazz), method.getName(), parametersInString));
		} else {
			codeWriter.writeImport(ReflectionAssert.class);
			codeWriter.writeCode(method, String.format("%s actualResult = %s.%s(%s);", Pointer.simpleReturnTypeName(method, codeWriter), Pointer.instanceName(clazz), method.getName(), parametersInString));
			codeWriter.writeOutput(method, "{'data':", "Json.toJson(actualResult)", ",'status':'Success'}");
			codeWriter.writeCode(method, String.format("%s expectedResult = Json.fromJson(responseData, %s.class, \"data\");", Pointer.simpleReturnTypeName(method, codeWriter), returnType.getSimpleName()));
			codeWriter.writeCode(method, "ReflectionAssert.assertReflectionEquals(actualResult, expectedResult);");
		}
	}
	
	public void writeCodes4InvokingPrivateMethod(Class<?> clazz, Method method, CodeWriter codeWriter) {
		StringBuilder parameterTypesBuilder = new StringBuilder();
		for (Class<?> parameterType : method.getParameterTypes()) {
			codeWriter.writeImport(parameterType);
			parameterTypesBuilder.append(parameterType.getSimpleName() + ".class" + ", ");
		}
		codeWriter.writeImport(Method.class);
		codeWriter.writeCode(method, "try {");
		int parameterCount = method.getParameterCount();
		if (parameterCount > 0) {
			codeWriter.writeCode(method, String.format("Method method = %s.class.getDeclaredMethod(\"%s\", %s);", clazz.getSimpleName(), method.getName(), Lang.trimEndingComma(parameterTypesBuilder)));
		} else {
			codeWriter.writeCode(method, String.format("Method method = %s.class.getDeclaredMethod(\"%s\");", clazz.getSimpleName(), method.getName()));
		}
		codeWriter.writeCode(method, "method.setAccessible(true);");
		Class<?> returnType = method.getReturnType();
		if (returnType == void.class || returnType == Void.class) {
			if (parameterCount > 0) {
				codeWriter.writeCode(method, String.format("method.invoke(%s, %s);", Pointer.instanceName(clazz), writeCodes4PreparingParameterValues(method, codeWriter)));
			} else {
				codeWriter.writeCode(method, String.format("method.invoke(%s);", Pointer.instanceName(clazz)));
			}
		} else {
			codeWriter.writeImport(ReflectionAssert.class);
			String returnTypeSimpleName = returnType.getSimpleName();
			if (parameterCount > 0) {
				codeWriter.writeCode(method, String.format("%s actualResult = (%s) method.invoke(%s, %s);", Pointer.simpleReturnTypeName(method, codeWriter), returnTypeSimpleName, Pointer.instanceName(clazz), writeCodes4PreparingParameterValues(method, codeWriter)));
			} else {
				codeWriter.writeCode(method, String.format("%s actualResult = (%s) method.invoke(%s);", Pointer.simpleReturnTypeName(method, codeWriter), returnTypeSimpleName, Pointer.instanceName(clazz)));
			}
			codeWriter.writeOutput(method, "{'data':", "Json.toJson(actualResult)", ",'status':'Success'}");
			codeWriter.writeCode(method, String.format("%s expectedResult = Json.fromJson(responseData, %s.class, \"data\");", Pointer.simpleReturnTypeName(method, codeWriter), returnType.getSimpleName()));
			codeWriter.writeCode(method, "ReflectionAssert.assertReflectionEquals(actualResult, expectedResult);");
		}
		codeWriter.writeCode(method, "} catch (Exception e){}");
	}
	
	public String writeCodes4PreparingParameterValues(Method method, CodeWriter codeWriter) {
		StringBuilder parametersBuilder = new StringBuilder();
		int i = 0;
		for (Class<?> parameterType : method.getParameterTypes()) {
			codeWriter.writeImport(parameterType);
			parametersBuilder.append(String.format("Json.fromJson(requestData, %s.class, \"data\", %s), ", parameterType.getSimpleName(), i++));
		}
		return Lang.trimEndingComma(parametersBuilder);
	}
	
	public void writeMockito4InvokingDependencyMethod(Path path, Class<?> clazz, final Method method, final CodeWriter codeWriter) throws IOException {
		final List<Dependency> dependencies = Pointer.dependencies(clazz);
		final Map<String, Object> requestData = new LinkedHashMap<>();
		final Map<String, Object> responseData = new LinkedHashMap<>();
		Pointer.scanMethod(path.toFile(), method, new Function<String, Boolean>() {
			@Override
			public Boolean apply(String code) {
				for (Dependency dependency : dependencies) {
					Field dependencyField = dependency.getField();
					Method dependencyMethod = dependency.getMethod();
					String instanceAndMethod = Pointer.instanceName(dependencyField) + "." + dependencyMethod.getName();
					if (code.contains(dependencyField.getName()) && code.contains(dependencyMethod.getName())) {// TODO Also consider method overloading.
						codeWriter.writeStaticImport(Mockito.class);
						codeWriter.writeField(dependencyField.getType(), MockBean.class);
						StringBuilder parameterValuesInString = new StringBuilder();
						if (dependencyMethod.getParameterCount() > 0) {
							Object[] parameterValues = new Object[dependencyMethod.getParameterCount()];
							Class<?>[] parameterTypes = dependencyMethod.getParameterTypes();
							for (int i = 0; i < dependencyMethod.getParameterCount(); i++) {
								codeWriter.writeImport(parameterTypes[i]);
								parameterValuesInString.append(String.format("Json.fromJson(mockedData, %s.class, \"requestData\", \"%s\", %s), ", parameterTypes[i].getSimpleName(), instanceAndMethod, i));
								Mocker mocker = new Mocker();
								try {
									parameterValues[i] = mocker.mockObject(dependencyMethod.getGenericParameterTypes()[i].getTypeName());
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
							requestData.put(instanceAndMethod, parameterValues);
						}
						Class<?> returnType = dependencyMethod.getReturnType();
						if (!Modifier.isAbstract(returnType.getModifiers()) && !Modifier.isPrivate(dependencyMethod.getModifiers()) && returnType != void.class && returnType != Void.class) {
							Mocker mocker = new Mocker();
							responseData.put(instanceAndMethod, mocker.mockObject(dependencyMethod.getGenericReturnType().getTypeName()));
							codeWriter.writeImport(returnType);
							codeWriter.writeCode(method, String.format("when(%s.%s(%s)).thenReturn(%s);", Lang.lowerFirstCharacter(dependencyField.getType().getSimpleName()), dependencyMethod.getName(), Lang.trimEndingComma(parameterValuesInString), String.format("Json.fromJson(mockedData, %s.class, \"responseData\", \"%s\")", returnType.getSimpleName(), instanceAndMethod)));
						}
					}
				}
				return true;
			}
		});
		Map<String, Map<String, Object>> result = new LinkedHashMap<>();
		result.put("requestData", requestData);
		result.put("responseData", responseData);
		codeWriter.writeCode(method, String.format("String mockedDataToBeUploaded = \"%s\";", Json.toJson(result).replace("\"", "'").replace("\\", "")));
	}
}
