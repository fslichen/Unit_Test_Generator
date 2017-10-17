package evolution.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import evolution.pojo.AbstractAlphaPojo;
import evolution.pojo.AlphaPojo;
import evolution.service.AnyService;

@RestController
@RequestMapping("/another")
public class AnotherController {
	private AnyService myService;
	
	private RestTemplate restTemplate;
	
	@PostMapping("/post/abstract")
	public AlphaPojo postAbstract(AbstractAlphaPojo anyPojo) {
		return myService.anyAbstract(null);
	}
	
	@GetMapping("/rest/template")
	public void restTemplate() {
		myService.anyAbstract(null);
		restTemplate.postForObject(null, null, null, new Object());
	}
	
	@PostMapping("/post/objects") 
	public Object[] postObjects(int[] ints) {
		return null;
	}
}
