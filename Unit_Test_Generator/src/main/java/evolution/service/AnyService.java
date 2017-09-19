package evolution.service;

import org.springframework.stereotype.Service;

import evolution.pojo.AnotherPojo;
import evolution.pojo.AnyPojo;

@Service
public class AnyService {
	public AnyPojo anyMethod(AnyPojo anyPojo, AnotherPojo anotherPojo) {
		anyPojo.setName(anyPojo.getName() + anotherPojo.getAddress());
		return anyPojo;
	}
}