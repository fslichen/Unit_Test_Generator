package evolution.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import evolution.pojo.AnotherPojo;
import evolution.pojo.AnyPojo;

@Service
public class AnyService {
	public AnyPojo anyMethod(AnyPojo anyPojo, AnotherPojo anotherPojo) {
		anyPojo.setName(anyPojo.getName() + anotherPojo.getAddress());
		return anyPojo;
	}
	
	public List<AnyPojo> anotherMethod(AnyPojo anyPojo) {
		return Arrays.asList(anyPojo);
	}
	
	private AnyPojo anyMethod() {
		return null;
	}
}
