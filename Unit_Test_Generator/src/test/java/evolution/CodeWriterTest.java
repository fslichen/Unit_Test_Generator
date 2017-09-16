package evolution;
import org.junit.Test;
import evolution.annotation.DatabaseSetup4Ucase;
import evolution.annotation.ExpectedDatabase4Ucase;
public class CodeWriterTest {
    @Test
    @DatabaseSetup4Ucase
    @ExpectedDatabase4Ucase
    public void testWriteIndent() {
        System.out.println("Hello World");
    }

    @Test
    @DatabaseSetup4Ucase
    @ExpectedDatabase4Ucase
    public void testWriteCode() {
        System.out.println("Hello World");
    }

    @Test
    @DatabaseSetup4Ucase
    @ExpectedDatabase4Ucase
    public void testWriteCodes() {
        System.out.println("Hello World");
    }

}