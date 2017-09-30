package evolution.controller;

import org.springframework.beans.factory.annotation.Autowired;
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
	@Autowired
	private AnyService myService;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@PostMapping("/post/abstract")
	public AlphaPojo postAbstract(AbstractAlphaPojo anyPojo) {
		return myService.anyAbstract(null);
	}
	
	@GetMapping("/rest/template")
	public void restTemplate() {
		restTemplate.postForObject(null, null, null, new Object());
	}
	
	// TODO Remove the [] symbols from the method name.
//	@PostMapping("/post/objects") 
//	public Object[] postObjects() {
//		return null;
//	}
}
