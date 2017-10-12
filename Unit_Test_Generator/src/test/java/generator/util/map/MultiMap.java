package generator.util.map;

import java.util.List;
import java.util.Map;

public interface MultiMap<T, V> extends Map<T, List<V>> {
	@SuppressWarnings("unchecked")
	public void push(T t, V... vs);
}
