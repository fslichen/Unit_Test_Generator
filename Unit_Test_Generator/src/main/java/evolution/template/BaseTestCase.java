package evolution.template;

import javax.annotation.PostConstruct;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import evolution.Application;
import evolution.template.impl.TestCaseClientImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Application.class) 
@WebAppConfiguration
@TestPropertySource("classpath:application.properties")
// Add necessary listeners.
public class BaseTestCase {
	protected MockMvc mockMvc;

	protected TestCaseClient testCaseClient;
	
	@Autowired
	protected WebApplicationContext webApplicationContext;
	
	@PostConstruct
	public void postConstruct() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		testCaseClient = new TestCaseClientImpl();
	}
}
