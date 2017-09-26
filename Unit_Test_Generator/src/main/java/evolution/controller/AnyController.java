package evolution.controller;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import evolution.controller.dto.AnyAbstractDto;
import evolution.controller.dto.AnyDto;
import evolution.pojo.AnotherPojo;
import evolution.pojo.AnyPojo;
import evolution.pojo.Tree;
import evolution.service.AnyService;

@RestController
public class AnyController {
	@Autowired
	private AnyService anyService;
	
	@RequestMapping("/test/post")
	public void post(AnyPojo anyPojo) {
		
	}
	
	@RequestMapping("/another/test/post")
	public void postAnother() {
		
	}
	
	@GetMapping("/get")
	public int get() {
		return 0;
	}
	
	@RequestMapping(method = RequestMethod.PATCH, value = "/http")
	public void http() {
		
	}
	
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
		anyService.anyMethod(anyPojo, anotherPojo);
		return anyDto;
	}
	
	@GetMapping("/exception")
	public AnyDto exception(AnyDto anyDto) {
		int i = 1 / 0;
		anyDto.setName("Chen");
		anyDto.setAge(27);
		return anyDto;
	}
	
	private String hide(String info) {
		return null;
	}
	
	@GetMapping("/servlet/get")
	public void servletGet(HttpServletRequest request) {
		
	}
	
	@GetMapping("/list")
	public List<AnyDto> list(AnyDto anyDto) {
		int i = 1 / 0;
		return Arrays.asList(anyDto);
	}
	
	@GetMapping("/tree")
	public List<Tree> tree(Tree tree) {
		return null;
	}
	
	@GetMapping("/abstract")
	public void abstractPojo(AnyAbstractDto dto) {
		
	}
}
