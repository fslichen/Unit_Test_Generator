package generator.template;

import java.util.List;

@FunctionalInterface
public interface UnitTestClassWriter {
	public List<String> write();
}
