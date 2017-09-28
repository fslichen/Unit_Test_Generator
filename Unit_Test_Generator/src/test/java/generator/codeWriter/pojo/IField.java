package generator.codeWriter.pojo;

import java.util.LinkedList;
import java.util.List;

public class IField {
	private Class<?> fieldType;
	private List<Class<?>> annotationTypes;
	public IField() {
		annotationTypes = new LinkedList<>();
	}
	public Class<?> getFieldType() {
		return fieldType;
	}
	public void setFieldType(Class<?> fieldType) {
		this.fieldType = fieldType;
	}
	public List<Class<?>> getAnnotationTypes() {
		return annotationTypes;
	}
	public void setAnnotationTypes(List<Class<?>> annotationTypes) {
		this.annotationTypes = annotationTypes;
	}
}
