package evolution.service;

import org.springframework.stereotype.Service;

import evolution.pojo.AnotherPojo;
import evolution.pojo.AnyPojo;

@Service
public class RealService {
	public AnotherPojo anyMethod(AnyPojo anyPojo) {
		return new AnotherPojo();
	}
}
