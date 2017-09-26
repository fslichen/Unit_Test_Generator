package evolution.dao.impl;
import java.util.List;
import generator.template.ReflectionAssert;
import java.util.LinkedList;
import generator.Json;
import evolution.annotation.ExpectedDatabase4Ucase;
import generator.template.TestCase;
import evolution.annotation.Database4UcaseSetup;
import evolution.dao.impl.AnyDaoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import generator.BaseTestCase;
import org.junit.Test;
public class AnyDaoImplTest extends BaseTestCase {
    @Autowired
    private AnyDaoImpl anyDaoImpl;
    
    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public <T> void testAnyMethodWithTypesLinkedList0() throws Exception {
        TestCase testCase = testCaseClient.getTestCase();
        String requestData = testCase.getRequestData();
        String responseData = testCase.getResponseData();
        LinkedList<T> actualResult = anyDaoImpl.anyMethod();
        LinkedList<T> expectedResult = Json.fromSubJson(responseData, "data", LinkedList.class);
        ReflectionAssert.assertReflectionEquals(actualResult, expectedResult);
    }
    
    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testAnyMethodWithTypesList0() throws Exception {
        TestCase testCase = testCaseClient.getTestCase();
        String requestData = testCase.getRequestData();
        String responseData = testCase.getResponseData();
        List actualResult = anyDaoImpl.anyMethod();
        List expectedResult = Json.fromSubJson(responseData, "data", List.class);
        ReflectionAssert.assertReflectionEquals(actualResult, expectedResult);
    }
    
}
