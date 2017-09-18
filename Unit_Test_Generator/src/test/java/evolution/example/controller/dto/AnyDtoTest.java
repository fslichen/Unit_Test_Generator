package evolution.example.controller.dto;
import org.junit.Test;
import evolution.annotation.Database4UcaseSetup;
import evolution.annotation.ExpectedDatabase4Ucase;
public class AnyDtoTest {
    private String name;
    
    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testToString() {
        System.out.println("Hello World");
    }

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