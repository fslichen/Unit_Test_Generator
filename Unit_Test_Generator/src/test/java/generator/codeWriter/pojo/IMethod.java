package generator.codeWriter.pojo;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import generator.util.map.MultiHashMap;
import generator.util.map.MultiMap;

public class IMethod {
	private String typeParameterName;
	private String prefix;
	private Method method;
	private String suffix;
	private Class<?> exceptionType;
	private MultiMap<Class<?>, AnnotationArgument> annotationTypeAndArgumentsMap;
	private List<String> codes;
	public IMethod() {
		annotationTypeAndArgumentsMap = new MultiHashMap<>();
		codes = new LinkedList<>();
	}
	public String getTypeParameterName() {
		return typeParameterName;
	}
	public void setTypeParameterName(String typeParameterName) {
		this.typeParameterName = typeParameterName;
	}
	public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	public Method getMethod() {
		return method;
	}
	public void setMethod(Method method) {
		this.method = method;
	}
	public String getSuffix() {
		return suffix;
	}
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
	public Class<?> getExceptionType() {
		return exceptionType;
	}
	public void setExceptionType(Class<?> exceptionType) {
		this.exceptionType = exceptionType;
	}
	public MultiMap<Class<?>, AnnotationArgument> getAnnotationTypeAndArgumentsMap() {
		return annotationTypeAndArgumentsMap;
	}
	public void setAnnotationTypeAndArgumentsMap(MultiMap<Class<?>, AnnotationArgument> annotationTypeAndArgumentsMap) {
		this.annotationTypeAndArgumentsMap = annotationTypeAndArgumentsMap;
	}
	public List<String> getCodes() {
		return codes;
	}
	public void setCodes(List<String> codes) {
		this.codes = codes;
	}
}
