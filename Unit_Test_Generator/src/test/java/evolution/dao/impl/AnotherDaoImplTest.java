package evolution.dao.impl;
import evolution.pojo.AnyBasePojo;
import generator.template.ReflectionAssert;
import evolution.pojo.AnyPojoImpl;
import generator.Json;
import evolution.annotation.ExpectedDatabase4Ucase;
import generator.template.TestCase;
import evolution.annotation.Database4UcaseSetup;
import evolution.dao.impl.AnotherDaoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import generator.BaseTestCase;
import org.junit.Test;
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
        AnyPojoImpl actualResult = anotherDaoImpl.anyMethod();
        AnyPojoImpl expectedResult = Json.fromSubJson(responseData, "data", AnyPojoImpl.class);
        ReflectionAssert.assertReflectionEquals(actualResult, expectedResult);
    }
    
    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testAnyMethodWithTypesAnyBasePojo0() throws Exception {
        TestCase testCase = testCaseClient.getTestCase();
        String requestData = testCase.getRequestData();
        String responseData = testCase.getResponseData();
        AnyBasePojo actualResult = anotherDaoImpl.anyMethod();
        AnyBasePojo expectedResult = Json.fromSubJson(responseData, "data", AnyBasePojo.class);
        ReflectionAssert.assertReflectionEquals(actualResult, expectedResult);
    }
    
}
