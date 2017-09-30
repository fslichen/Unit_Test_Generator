package evolution.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import evolution.pojo.AbstractAlphaPojo;
import evolution.pojo.AlphaPojo;
import evolution.service.AnyService;

@RestController
@RequestMapping("/another")
public class AnotherController {
	@Autowired
	private AnyService myService;
	
	@PostMapping("/post/abstract")
	public AlphaPojo postAbstract(AbstractAlphaPojo anyPojo) {
		return myService.anyAbstract(null);
	}
}
