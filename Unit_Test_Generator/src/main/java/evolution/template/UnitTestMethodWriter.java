package evolution.template;

import java.lang.reflect.Method;
import java.util.List;

@FunctionalInterface
public interface UnitTestMethodWriter {
	public List<String> write(Method method);
}
