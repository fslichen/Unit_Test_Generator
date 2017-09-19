package evolution.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import evolution.controller.dto.AnyDto;
import evolution.pojo.AnotherPojo;
import evolution.pojo.AnyPojo;
import evolution.service.AnyService;

@RestController
public class AnyController {
	@Autowired
	private AnyService anyService;
	
	@GetMapping("/get")
	public void get() {
		
	}
	
	@PostMapping("/post")
	public AnyDto post(@RequestBody AnyDto anyDto) {
		anyDto.setName("Chen");
		anyDto.setAge(27);
		AnyPojo anyPojo = new AnyPojo();
		AnotherPojo anotherPojo = new AnotherPojo();
		anyService.anyMethod(anyPojo, anotherPojo);
		return anyDto;
	}
}