package evolution.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import evolution.pojo.AnotherPojo;
import evolution.pojo.AnyPojo;
import evolution.service.RealService;

@RestController
public class RealController {
	@Autowired
	private RealService realService;
	
	@PostMapping("/real/post")
	public AnotherPojo anyMethod(@RequestBody AnyPojo anyPojo) {
		return realService.anyMethod(anyPojo);
	}
}
