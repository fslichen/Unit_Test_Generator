package generator.template;

import java.lang.reflect.Method;

import generator.codeWriter.CodeWriter;

@FunctionalInterface
public interface UnitTestMethodWriter {
	public CodeWriter write(Method method);
}
