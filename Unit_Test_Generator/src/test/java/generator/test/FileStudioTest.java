package generator.test;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;

import generator.FileStudio;

public class FileStudioTest {
	@Test
	public void test() throws IOException {
//		FileStudio.copy(Paths.get("/Users/chenli/Desktop/Buffer"), Paths.get("/Users/chenli/Desktop/New_Buffer"));
		Map<String, String> map = new LinkedHashMap<>();
		map.put("import java.util.Map", "import java.util.HashMap");
		FileStudio.modify(Paths.get("/Users/chenli/Desktop/Program_Buffer"), x -> x.toFile().toString().endsWith(".java"), map);
	}
}
