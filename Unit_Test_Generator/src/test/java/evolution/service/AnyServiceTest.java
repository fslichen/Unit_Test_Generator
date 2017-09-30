package evolution.service;
import org.junit.Test;
import generator.BaseTestCase;
import evolution.service.AnyService;
import org.springframework.beans.factory.annotation.Autowired;
import generator.Json;
import generator.template.TestCase;
import evolution.annotation.Database4UcaseSetup;
import evolution.annotation.ExpectedDatabase4Ucase;
import evolution.pojo.AnyPojo;
import generator.template.ReflectionAssert;
import java.util.List;
import java.lang.reflect.Method;
import evolution.pojo.AnotherPojo;
public class AnyServiceTest extends BaseTestCase {
    @Autowired
    private AnyService anyService;
    
    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testAnotherMethodWithTypesAnyPojointList0() throws Exception {
        TestCase testCase = testCaseClient.getTestCase();
        String requestData = testCase.getRequestData();
        String responseData = testCase.getResponseData();
        String mockedData = testCase.getMockData();
        String mockedDataToBeUploaded = "{'requestData':{},'responseData':{}}";
        List<AnyPojo> actualResult = anyService.anotherMethod(Json.fromJson(requestData, AnyPojo.class, "data", 0), Json.fromJson(requestData, int.class, "data", 1));
        List<AnyPojo> expectedResult = Json.fromSubJson(responseData, "data", List.class);
        ReflectionAssert.assertReflectionEquals(actualResult, expectedResult);
    }
    
    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testAnyMethodWithTypesAnyPojo0() throws Exception {
        TestCase testCase = testCaseClient.getTestCase();
        String requestData = testCase.getRequestData();
        String responseData = testCase.getResponseData();
        String mockedData = testCase.getMockData();
        String mockedDataToBeUploaded = "{'requestData':{},'responseData':{}}";
        try {
            Method method = AnyService.class.getDeclaredMethod("anyMethod");
            method.setAccessible(true);
            AnyPojo actualResult = (AnyPojo) method.invoke(anyService);
            AnyPojo expectedResult = Json.fromSubJson(responseData, "data", AnyPojo.class);
            ReflectionAssert.assertReflectionEquals(actualResult, expectedResult);
        } catch (Exception e){}
    }
    
    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testAnyMethodWithTypesAnyPojoAnyPojo0() throws Exception {
        TestCase testCase = testCaseClient.getTestCase();
        String requestData = testCase.getRequestData();
        String responseData = testCase.getResponseData();
        String mockedData = testCase.getMockData();
        String mockedDataToBeUploaded = "{'requestData':{},'responseData':{}}";
        AnyPojo actualResult = anyService.anyMethod(Json.fromJson(requestData, AnyPojo.class, "data", 0));
        AnyPojo expectedResult = Json.fromSubJson(responseData, "data", AnyPojo.class);
        ReflectionAssert.assertReflectionEquals(actualResult, expectedResult);
    }
    
    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testAnyMethodWithTypesAnyPojoAnotherPojoAnyPojo0() throws Exception {
        TestCase testCase = testCaseClient.getTestCase();
        String requestData = testCase.getRequestData();
        String responseData = testCase.getResponseData();
        String mockedData = testCase.getMockData();
        String mockedDataToBeUploaded = "{'requestData':{},'responseData':{}}";
        AnyPojo actualResult = anyService.anyMethod(Json.fromJson(requestData, AnyPojo.class, "data", 0), Json.fromJson(requestData, AnotherPojo.class, "data", 1));
        AnyPojo expectedResult = Json.fromSubJson(responseData, "data", AnyPojo.class);
        ReflectionAssert.assertReflectionEquals(actualResult, expectedResult);
    }
    
}
