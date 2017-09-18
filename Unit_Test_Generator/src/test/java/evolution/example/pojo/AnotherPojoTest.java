package evolution.example.pojo;
import org.junit.Test;
import evolution.annotation.Database4UcaseSetup;
import evolution.annotation.ExpectedDatabase4Ucase;
public class AnotherPojoTest {
    private String name;
    
    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testGetAddress() {
        System.out.println("Hello World");
    }

    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testSetAddress() {
        System.out.println("Hello World");
    }

}