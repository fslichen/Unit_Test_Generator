package evolution;
import org.junit.Test;
import evolution.annotation.DatabaseSetup4Ucase;
import evolution.annotation.ExpectedDatabase4Ucase;
public class GeneratorTest {
    @Test
    @DatabaseSetup4Ucase
    @ExpectedDatabase4Ucase
    public void testTest() {
        System.out.println("Hello World");
    }

    @Test
    @DatabaseSetup4Ucase
    @ExpectedDatabase4Ucase
    public void testScanClassesUnderSrcMainJavaAndGenerateUnitTestClassesUnderSrcTestJava() {
        System.out.println("Hello World");
    }

    @Test
    @DatabaseSetup4Ucase
    @ExpectedDatabase4Ucase
    public void testCapitalizedFirstCharacter() {
        System.out.println("Hello World");
    }

    @Test
    @DatabaseSetup4Ucase
    @ExpectedDatabase4Ucase
    public void testEndsWithExtension() {
        System.out.println("Hello World");
    }

}