package evolution.pojo;

import java.util.Date;

public class AnyPojo {
	private String name;
	private Integer age;
	private Date date;
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	@Override
	public String toString() {
		return "AnyPojo [name=" + name + ", age=" + age + "]";
	}
}
