package evolution.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import evolution.pojo.AnotherPojo;
import evolution.pojo.AnyPojo;

@RestController
public class RealController {
	@PostMapping("/real/post")
	public AnotherPojo anyMethod(@RequestBody AnyPojo anyPojo) {
		return new AnotherPojo();
	}
}
