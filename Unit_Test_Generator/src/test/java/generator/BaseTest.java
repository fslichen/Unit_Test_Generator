package generator;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;

import evolution.Application;
import generator.template.TestCaseClient;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Application.class) 
@WebAppConfiguration
@TestPropertySource("classpath:application.properties")
public class BaseTest {
	protected TestCaseClient testCaseClient;
	
	@Autowired
	protected WebApplicationContext webApplicationContext;

	public BaseTest() {
		testCaseClient = null;// TODO Add an implementation.
	}
}
