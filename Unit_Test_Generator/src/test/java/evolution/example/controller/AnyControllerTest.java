package evolution.example.controller;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import evolution.example.controller.AnyController;
import evolution.annotation.Database4UcaseSetup;
import evolution.annotation.ExpectedDatabase4Ucase;
public class AnyControllerTest {
    private String name;
    
    @Autowired
    private AnyController anyController;
    
    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testGet() {
        System.out.println("Hello World");
    }

    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testPost() {
        System.out.println("Hello World");
    }

}