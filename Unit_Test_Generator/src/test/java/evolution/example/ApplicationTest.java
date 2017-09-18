package evolution.example;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import evolution.example.Application;
import evolution.annotation.Database4UcaseSetup;
import evolution.annotation.ExpectedDatabase4Ucase;
public class ApplicationTest {
    private String name;
    
    @Autowired
    private Application application;
    
    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testMain() {
        System.out.println("Hello World");
    }

}