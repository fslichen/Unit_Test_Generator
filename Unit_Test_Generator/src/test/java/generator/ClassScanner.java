package generator;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.junit.Test;

public class ClassScanner {
	private static int previousIndex(String string, int index, Predicate<Character> predicate) {
		for (int i = index - 1; i >= 0; i--) {
			if (predicate.test(string.charAt(i))) {
				return i;
			}
		}
		return -1;
	}
	
	public static void scanProject(Path sourcePath) throws IOException {
		Map<Class<?>, Map<Method, List<String>>> result = new LinkedHashMap<>();
		Files.walk(sourcePath).forEach(path -> {
			File file = path.toFile();
			if (file.isFile() && file.toString().endsWith(".java")) {
				try {
					List<String> codes = Files.lines(path).collect(Collectors.toList());
					Map<Method, List<String>> codesByMethodMap = new LinkedHashMap<>();
					Method overloadingProveMethod = null;
					String packageName = codes.get(0);
					Class<?> clazz = Class.forName(packageName.substring(packageName.indexOf(" ") + 1).replace(";", ".") + file.getName().replace(".java", ""));
					for (String code : codes) {
						// Determine whether it is a method head.
						if (code.contains("(") && code.contains(")") && !code.contains("class") && (code.contains("public") || code.contains("protected") || code.contains("private"))) {
							int leftBracketIndex = code.indexOf("(");
							int rightBracketIndex = code.indexOf(")");
							int methodNameStartIndex = previousIndex(code, leftBracketIndex, x -> x == ' ') + 1;
							String methodName = code.substring(methodNameStartIndex, leftBracketIndex);
							List<String> parameterTypesInString = new LinkedList<>();
							for (String parameterInString : code.substring(leftBracketIndex + 1, rightBracketIndex).split(",")) {
								parameterTypesInString.add(parameterInString.split(" ")[0]);
							}
							for (Method method : clazz.getDeclaredMethods()) {
								if (method.getName().equals(methodName)) {
									int i = 0;
									boolean match = true;
									for (Class<?> parameterType : method.getParameterTypes()) {
										if (!parameterType.getSimpleName().equals(parameterTypesInString.get(i))) {
											match = false;
											break;
										}
										i++;
									}
									if (match) {
										List<String> codesByMethod = new LinkedList<>();
										codesByMethod.add(code);
										codesByMethodMap.put(method, codesByMethod);
										overloadingProveMethod = method;
										break;
									}
								}
							}
						} else if (overloadingProveMethod != null) {
							codesByMethodMap.get(overloadingProveMethod).add(code);
						}
					}
					result.put(clazz, codesByMethodMap);
				} catch (Exception e) {}
			}
		});
		System.out.println(result);
	}
	
	@Test
	public void test() throws IOException {
		scanProject(Paths.get("/home/chen/Desktop/Playground/Git/Unit_Test_Generator"));
	}
}
