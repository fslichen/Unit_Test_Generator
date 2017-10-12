package generator.util.map;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MultiHashMap<T, V> implements MultiMap<T, V> {
	private Map<T, List<V>> map;
	
	public MultiHashMap() {
		map = new LinkedHashMap<>();
	}
	
	@Override
	public int size() {
		return map.size();
	}

	@Override
	public boolean isEmpty() {
		return map.isEmpty();
	}

	@Override
	public boolean containsKey(Object key) {
		return map.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return map.containsValue(value);
	}

	@Override
	public List<V> get(Object key) {
		return map.get(key);
	}

	@Override
	public List<V> put(T key, List<V> value) {
		return map.put(key, value);
	}
	
	@SuppressWarnings("unchecked")
	public void push(T key, V... values) {
		map.put(key, Arrays.asList(values));
	}

	@Override
	public List<V> remove(Object key) {
		return map.remove(key);
	}

	@Override
	public void putAll(Map<? extends T, ? extends List<V>> m) {
		map.putAll(m);
	}

	@Override
	public void clear() {
		map.clear();
	}

	@Override
	public Set<T> keySet() {
		return map.keySet();
	}

	@Override
	public Collection<List<V>> values() {
		return map.values();
	}

	@Override
	public Set<Entry<T, List<V>>> entrySet() {
		return map.entrySet();
	}

	@Override
	public String toString() {
		return "MultiHashMap [map=" + map + "]";
	}
}
