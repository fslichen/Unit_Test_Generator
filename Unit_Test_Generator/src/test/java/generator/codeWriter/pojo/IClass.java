package generator.codeWriter.pojo;

import java.util.LinkedList;
import java.util.List;

public class IClass {
	private Class<?> clazz;
	private String suffix;
	private Class<?> extendedClass;
	private List<Class<?>> implementedInterfaces;
	private List<Class<?>> annotationTypes;
	public IClass() {
		implementedInterfaces = new LinkedList<>();
		annotationTypes = new LinkedList<>(); 
	}
	public Class<?> getClazz() {
		return clazz;
	}
	public void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}
	public String getSuffix() {
		return suffix;
	}
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
	public Class<?> getExtendedClass() {
		return extendedClass;
	}
	public void setExtendedClass(Class<?> extendedClass) {
		this.extendedClass = extendedClass;
	}
	public List<Class<?>> getImplementedInterfaces() {
		return implementedInterfaces;
	}
	public void setImplementedInterfaces(List<Class<?>> implementedInterfaces) {
		this.implementedInterfaces = implementedInterfaces;
	}
	public List<Class<?>> getAnnotationTypes() {
		return annotationTypes;
	}
	public void setAnnotationTypes(List<Class<?>> annotationTypes) {
		this.annotationTypes = annotationTypes;
	}
}
