package generator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Predicate;

public class FileStudio {
	public static void copyAndModify(Path sourceBasePath, Path targetBasePath, Predicate<Path> fileFilter, Map<String, String> codeConvertionMap) throws IOException {
		copy(sourceBasePath, targetBasePath);
		modify(targetBasePath, fileFilter, codeConvertionMap);
	}
	
	public static void modify(Path basePath, Predicate<Path> fileFilter, Map<String, String> codeConvertionMap) throws IOException {
		Files.walk(basePath).filter(path -> path.toFile().isFile() && fileFilter.test(path)).forEach(path -> { 
			try {
				List<String> updatedLines = new LinkedList<>();
				Files.lines(path).forEach(line -> {
					String updatedLine = line;
					for (Entry<String, String> entry : codeConvertionMap.entrySet()) {
						if (line.equals(entry.getKey())) {
							updatedLine = entry.getValue();
							break;
						}
					}
					updatedLines.add(updatedLine);
				});
				Files.write(path, updatedLines);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}
	
	public static void copy(Path sourceBasePath, Path targetBasePath) throws IOException {
		Files.walk(sourceBasePath).forEach(sourcePath -> {
			try { Files.copy(sourcePath, Paths.get(sourcePath.toString().replace(sourceBasePath.toString(), targetBasePath.toString())), StandardCopyOption.REPLACE_EXISTING); } catch (IOException e) {}
		});
	}
	
	public static Path fileDirectoryPath(Path path) {
		String pathInString = path.toString();
		return Paths.get(pathInString.substring(0, pathInString.lastIndexOf(path.toFile().getName()) - 1));
	}
}
