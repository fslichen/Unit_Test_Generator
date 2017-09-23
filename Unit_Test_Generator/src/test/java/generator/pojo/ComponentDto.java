package generator.pojo;

public class ComponentDto {
	private Object data;
	private String status;
	public ComponentDto() {

	}
	public ComponentDto(Object data, String status) {
		this.data = data;
		this.status = status;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
}
