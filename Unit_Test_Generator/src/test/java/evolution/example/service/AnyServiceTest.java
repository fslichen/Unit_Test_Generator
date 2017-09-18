package evolution.example.service;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import evolution.example.service.AnyService;
import evolution.annotation.Database4UcaseSetup;
import evolution.annotation.ExpectedDatabase4Ucase;
public class AnyServiceTest {
    private String name;
    
    @Autowired
    private AnyService anyService;
    
    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testAnyMethod() {
        System.out.println("Hello World");
    }

}