package evolution.dao.impl;
import org.junit.Test;
import generator.BaseTestCase;
import evolution.dao.impl.AnyDaoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import generator.Json;
import generator.template.TestCase;
import evolution.annotation.Database4UcaseSetup;
import evolution.annotation.ExpectedDatabase4Ucase;
import generator.template.ReflectionAssert;
import java.util.LinkedList;
import java.util.List;
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
        String mockedData = testCase.getMockData();
        String mockedDataToBeUploaded = "{'requestData':{},'responseData':{}}";
        LinkedList<T> actualResult = anyDaoImpl.anyMethod();
        System.out.println("{\"data\":" + Json.toJson(actualResult) + ",\"status\":\"Success\"}");
        LinkedList<T> expectedResult = Json.fromJson(responseData, LinkedList.class, "data");
        ReflectionAssert.assertReflectionEquals(actualResult, expectedResult);
    }
    
    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testAnyMethodWithTypesList0() throws Exception {
        TestCase testCase = testCaseClient.getTestCase();
        String requestData = testCase.getRequestData();
        String responseData = testCase.getResponseData();
        String mockedData = testCase.getMockData();
        String mockedDataToBeUploaded = "{'requestData':{},'responseData':{}}";
        List actualResult = anyDaoImpl.anyMethod();
        System.out.println("{\"data\":" + Json.toJson(actualResult) + ",\"status\":\"Success\"}");
        List expectedResult = Json.fromJson(responseData, List.class, "data");
        ReflectionAssert.assertReflectionEquals(actualResult, expectedResult);
    }
    
}
