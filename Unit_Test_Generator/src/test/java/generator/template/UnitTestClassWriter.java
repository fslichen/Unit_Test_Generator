package generator.template;

import generator.codeWriter.CodeWriter;

@FunctionalInterface
public interface UnitTestClassWriter {
	public CodeWriter write();
}
