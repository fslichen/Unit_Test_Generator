package evolution;
import org.junit.Test;
import evolution.annotation.Database4UcaseSetup;
import evolution.annotation.ExpectedDatabase4Ucase;
public class GeneratorTest {
    private String name;
    
    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testScanClassesUnderSrcMainJavaAndGenerateUnitTestClassesUnderSrcTestJava() {
        System.out.println("Hello World");
    }

    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testA() {
        System.out.println("Hello World");
    }

    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testClassesUnderSrcMainJava() {
        System.out.println("Hello World");
    }

    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testWithExtension() {
        System.out.println("Hello World");
    }

    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testClassAnnnotation() {
        System.out.println("Hello World");
    }

    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testKeywordCount() {
        System.out.println("Hello World");
    }

    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testCapitalizedFirstCharacter() {
        System.out.println("Hello World");
    }

}