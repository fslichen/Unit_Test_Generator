package evolution.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import evolution.example.controller.dto.AnyDto;
import evolution.example.service.AnyService;

@RestController
public class AnyController {
	@Autowired
	private AnyService anyService;
	
	@PostMapping("/post")
	public AnyDto post(@RequestBody AnyDto anyDto) {
		anyDto.setName("Chen");
		anyDto.setAge(27);
		return anyDto;
	}
}
