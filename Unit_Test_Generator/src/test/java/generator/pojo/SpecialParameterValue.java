package generator.pojo;

public class SpecialParameterValue {
	private String parameterTypeName;
	public SpecialParameterValue() {
		
	}
	public SpecialParameterValue(Class<?> parameterType) {
		parameterTypeName = parameterType.getName();
	}
	public String getParameterTypeName() {
		return parameterTypeName;
	}
	public void setParameterTypeName(String parameterTypeName) {
		this.parameterTypeName = parameterTypeName;
	}
}
