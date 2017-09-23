package generator.test.pojo;

public class AnyPojo4Test {
	private String name;
	private Object data;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	@Override
	public String toString() {
		return "AnyPojo4Test [name=" + name + ", data=" + data + "]";
	}
}
