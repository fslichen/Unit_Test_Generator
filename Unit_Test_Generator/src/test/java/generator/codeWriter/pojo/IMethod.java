package generator.codeWriter.pojo;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

public class IMethod {
	private String typeParameterName;
	private String prefix;
	private Method method;
	private String suffix;
	private Class<?> exceptionType;
	private List<Class<?>> annotationTypes;
	private List<String> codes;
	public IMethod() {
		annotationTypes = new LinkedList<>();
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
	public List<Class<?>> getAnnotationTypes() {
		return annotationTypes;
	}
	public void setAnnotationTypes(List<Class<?>> annotationTypes) {
		this.annotationTypes = annotationTypes;
	}
	public List<String> getCodes() {
		return codes;
	}
	public void setCodes(List<String> codes) {
		this.codes = codes;
	}
}
