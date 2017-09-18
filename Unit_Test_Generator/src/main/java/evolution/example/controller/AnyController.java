package evolution.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import evolution.example.controller.dto.AnyDto;
import evolution.example.pojo.AnotherPojo;
import evolution.example.pojo.AnyPojo;
import evolution.example.service.AnyService;

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
