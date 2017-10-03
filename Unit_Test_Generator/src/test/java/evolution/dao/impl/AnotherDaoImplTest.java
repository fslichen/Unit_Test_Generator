package evolution.dao.impl;
import org.junit.Test;
import generator.BaseTestCase;
import evolution.dao.impl.AnotherDaoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import generator.Json;
import generator.template.TestCase;
import evolution.annotation.Database4UcaseSetup;
import evolution.annotation.ExpectedDatabase4Ucase;
import generator.template.ReflectionAssert;
import evolution.pojo.AnyPojoImpl;
import evolution.pojo.AnyBasePojo;
public class AnotherDaoImplTest extends BaseTestCase {
    @Autowired
    private AnotherDaoImpl anotherDaoImpl;
    
    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testAnyMethodWithTypesAnyPojoImpl0() throws Exception {
        TestCase testCase = testCaseClient.getTestCase();
        String requestData = testCase.getRequestData();
        String responseData = testCase.getResponseData();
        String mockedData = testCase.getMockData();
        String mockedDataToBeUploaded = "{'requestData':{},'responseData':{}}";
        AnyPojoImpl actualResult = anotherDaoImpl.anyMethod();
        System.out.println(Json.toJson(actualResult));
        AnyPojoImpl expectedResult = Json.fromJson(responseData, AnyPojoImpl.class, "data");
        ReflectionAssert.assertReflectionEquals(actualResult, expectedResult);
    }
    
    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testAnyMethodWithTypesAnyBasePojo0() throws Exception {
        TestCase testCase = testCaseClient.getTestCase();
        String requestData = testCase.getRequestData();
        String responseData = testCase.getResponseData();
        String mockedData = testCase.getMockData();
        String mockedDataToBeUploaded = "{'requestData':{},'responseData':{}}";
        AnyBasePojo actualResult = anotherDaoImpl.anyMethod();
        System.out.println(Json.toJson(actualResult));
        AnyBasePojo expectedResult = Json.fromJson(responseData, AnyBasePojo.class, "data");
        ReflectionAssert.assertReflectionEquals(actualResult, expectedResult);
    }
    
}
