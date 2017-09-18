package evolution.example.service;

import org.springframework.stereotype.Service;

import evolution.example.pojo.AnotherPojo;
import evolution.example.pojo.AnyPojo;

@Service
public class AnyService {
	public AnyPojo anyMethod(AnyPojo anyPojo, AnotherPojo anotherPojo) {
		anyPojo.setName(anyPojo.getName() + anotherPojo.getAddress());
		return anyPojo;
	}
}
