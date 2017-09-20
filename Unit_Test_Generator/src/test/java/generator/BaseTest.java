package generator;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;

import evolution.Application;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Application.class) 
@WebAppConfiguration
@TestPropertySource("classpath:application.properties")
public abstract class BaseTest {
	@Value("${max-use-case-count}")
	protected Integer maxUseCaseCount;
	
	@Autowired
	protected WebApplicationContext webApplicationContext;
}
