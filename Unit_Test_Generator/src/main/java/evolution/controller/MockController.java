package evolution.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import evolution.controller.dto.AnyDto;
import evolution.pojo.AnotherPojo;
import evolution.pojo.AnyPojo;
import evolution.service.AnyService;

@RestController
public class MockController {
	@Autowired
	private AnyService myService;
	
	@PostMapping("/post")
	public AnyDto post(@RequestBody AnyDto anyDto) {
		if (anyDto == null) {
			anyDto = new AnyDto();
		} else if (anyDto.getName() == null) {
			anyDto.setName("Ling");
		} else if (anyDto.getAge() == null) {
			anyDto.setAge(26);
		}
		anyDto.setName("Chen");
		anyDto.setAge(27);
		AnyPojo anyPojo = new AnyPojo();
		AnotherPojo anotherPojo = new AnotherPojo();
		myService.anyMethod(anyPojo, anotherPojo);
		return anyDto;
	}
}
