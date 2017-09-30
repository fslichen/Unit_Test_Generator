package evolution.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import evolution.pojo.AlphaPojo;
import evolution.pojo.AnotherPojo;
import evolution.pojo.AnyPojo;

@Service
public class AnyService {
	public AnyPojo anyMethod(AnyPojo anyPojo, AnotherPojo anotherPojo) {
		anyPojo.setName(anyPojo.getName() + anotherPojo.getAddress());
		return anyPojo;
	}
	
	public AnyPojo anyMethod(AnyPojo anyPojo) {
		return anyPojo;
	}
	
	public List<AnyPojo> anotherMethod(AnyPojo anyPojo, int code) {
		return Arrays.asList(anyPojo);
	}
	
	private AnyPojo anyMethod() {
		return null;
	}
	
	public AlphaPojo anyAbstract(AlphaPojo pojo) {
		return null;
	}
}
