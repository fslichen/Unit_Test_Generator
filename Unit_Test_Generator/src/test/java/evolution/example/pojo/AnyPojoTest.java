package evolution.example.pojo;
import org.junit.Test;
import evolution.annotation.Database4UcaseSetup;
import evolution.annotation.ExpectedDatabase4Ucase;
public class AnyPojoTest {
    private String name;
    
    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testGetName() {
        System.out.println("Hello World");
    }

    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testSetName() {
        System.out.println("Hello World");
    }

    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testSetAge() {
        System.out.println("Hello World");
    }

    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testGetAge() {
        System.out.println("Hello World");
    }

}