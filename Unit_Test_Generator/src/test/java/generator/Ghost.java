package generator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.TreeMap;

import org.junit.Test;

public class Ghost {
	public Byte createByte(int x) {
		return new Byte(x + "");
	}
	
	public void writeFile(Path path, Map<Byte, Byte> map) throws IOException {
		byte[] oldBytes = Files.readAllBytes(path);
		byte[] newBytes = new byte[oldBytes.length];
		for (int i = 0; i < oldBytes.length; i++) {
			newBytes[i] = map.get(oldBytes[i]);
		}
		Files.write(path, newBytes);
	}
	
	public void encode(Path path) throws IOException {
		Map<Byte, Byte> map = new TreeMap<>();
		for (int i = -128; i < 127; i++) {
			map.put(createByte(i), createByte(i + 1));
		}
		map.put(createByte(127), createByte(-128));
		writeFile(path, map);
	}
	
	public void decode(Path path) throws IOException {
		Map<Byte, Byte> map = new TreeMap<>();
		map.put(createByte(-128), createByte(127));
		for (int i = -127; i < 128; i++) {
			map.put(createByte(i), createByte(i - 1));
		}
		writeFile(path, map);
	}
	
	@Test
	public void test() throws IOException {
		encode(Paths.get("/Users/chenli/Desktop/Unit_Test_Generator.zip"));
//		decode(Paths.get("/Users/chenli/Desktop/application.properties"));
	}
}
