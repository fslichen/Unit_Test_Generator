package evolution.pojo;

import java.util.List;

public class ParameterValuesAndReturnValue {
	private List<Object> parameterValues;
	private Object returnValue;
	public ParameterValuesAndReturnValue() {

	}
	public ParameterValuesAndReturnValue(List<Object> parameterValues, Object returnValue) {
		this.parameterValues = parameterValues;
		this.returnValue = returnValue;
	}
	public List<Object> getParameterValues() {
		return parameterValues;
	}
	public void setParameterValues(List<Object> parameterValues) {
		this.parameterValues = parameterValues;
	}
	public Object getReturnValue() {
		return returnValue;
	}
	public void setReturnValue(Object returnValue) {
		this.returnValue = returnValue;
	}
	@Override
	public String toString() {
		return "ParameterValuesAndReturnValue [parameterValues=" + parameterValues + ", returnValue=" + returnValue
				+ "]";
	}
}
