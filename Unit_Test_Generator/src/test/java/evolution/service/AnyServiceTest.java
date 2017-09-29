package evolution.service;
import org.junit.Test;
import generator.BaseTestCase;
import evolution.service.AnyService;
import org.springframework.beans.factory.annotation.Autowired;
import generator.Json;
import generator.template.TestCase;
import evolution.annotation.Database4UcaseSetup;
import evolution.annotation.ExpectedDatabase4Ucase;
import java.lang.reflect.Method;
import java.util.List;
import evolution.pojo.AnyPojo;
import evolution.pojo.AnotherPojo;
import generator.template.ReflectionAssert;
public class AnyServiceTest extends BaseTestCase {
    @Autowired
    private AnyService anyService;
    
    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testAnyMethodWithTypesPrimitiveVoid0() throws Exception {
        TestCase testCase = testCaseClient.getTestCase();
        String requestData = testCase.getRequestData();
        String responseData = testCase.getResponseData();
        try {
            Method method = AnyService.class.getDeclaredMethod("anyMethod");
            method.setAccessible(true);
            method.invoke(anyService);
        } catch (Exception e){}
    }
    
    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testAnyMethodWithTypesAnyPojoAnotherPojoAnyPojo0() throws Exception {
        TestCase testCase = testCaseClient.getTestCase();
        String requestData = testCase.getRequestData();
        String responseData = testCase.getResponseData();
        List<String> parameterValues = Json.splitSubJsons(requestData, "data");
        AnyPojo actualResult = anyService.anyMethod(Json.fromJson(parameterValues.get(0), AnyPojo.class), Json.fromJson(parameterValues.get(1), AnotherPojo.class));
        AnyPojo expectedResult = Json.fromSubJson(responseData, "data", AnyPojo.class);
        ReflectionAssert.assertReflectionEquals(actualResult, expectedResult);
    }
    
    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testAnotherMethodWithTypesAnyPojoList0() throws Exception {
        TestCase testCase = testCaseClient.getTestCase();
        String requestData = testCase.getRequestData();
        String responseData = testCase.getResponseData();
        List<String> parameterValues = Json.splitSubJsons(requestData, "data");
        List<AnyPojo> actualResult = anyService.anotherMethod(Json.fromJson(parameterValues.get(0), AnyPojo.class));
        List<AnyPojo> expectedResult = Json.fromSubJson(responseData, "data", List.class);
        ReflectionAssert.assertReflectionEquals(actualResult, expectedResult);
    }
    
}
