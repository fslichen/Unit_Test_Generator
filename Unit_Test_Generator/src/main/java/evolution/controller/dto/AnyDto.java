package evolution.controller.dto;

import java.math.BigDecimal;

public class AnyDto {
	private String name;
	private Integer age;
	private BigDecimal bigDecimal;
	public BigDecimal getBigDecimal() {
		return bigDecimal;
	}
	public void setBigDecimal(BigDecimal bigDecimal) {
		this.bigDecimal = bigDecimal;
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
		return "AnyDto [name=" + name + ", age=" + age + "]";
	}
}
