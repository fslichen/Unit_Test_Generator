package generator.pojo;

public class MethodReturnValue {
	private String status;
	private String returnType;
	public MethodReturnValue() {

	}
	public MethodReturnValue(String status, String returnType) {
		this.status = status;
		this.returnType = returnType;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getReturnType() {
		return returnType;
	}
	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}
}
