package evolution.service;
import org.junit.Test;
import generator.BaseTestCase;
import evolution.service.RealService;
import org.springframework.beans.factory.annotation.Autowired;
import generator.Json;
import generator.template.TestCase;
import evolution.annotation.Database4UcaseSetup;
import evolution.annotation.ExpectedDatabase4Ucase;
import evolution.pojo.AnyPojo;
import generator.template.ReflectionAssert;
import evolution.pojo.AnotherPojo;
public class RealServiceTest extends BaseTestCase {
    @Autowired
    private RealService realService;
    
    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testAnyMethodWithTypesAnyPojoAnotherPojo0() throws Exception {
        TestCase testCase = testCaseClient.getTestCase();
        String requestData = testCase.getRequestData();
        String responseData = testCase.getResponseData();
        String mockedData = testCase.getMockData();
        String mockedDataToBeUploaded = "{'requestData':{},'responseData':{}}";
        AnotherPojo actualResult = realService.anyMethod(Json.fromJson(requestData, AnyPojo.class, "data", 0));
        AnotherPojo expectedResult = Json.fromJson(responseData, AnotherPojo.class, "data");
        ReflectionAssert.assertReflectionEquals(actualResult, expectedResult);
    }
    
}
