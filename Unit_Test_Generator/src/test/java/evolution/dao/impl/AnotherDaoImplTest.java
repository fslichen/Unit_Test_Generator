package evolution.dao.impl;
import org.junit.Test;
import generator.BaseTestCase;
import evolution.dao.impl.AnotherDaoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import evolution.annotation.Database4UcaseSetup;
import evolution.annotation.ExpectedDatabase4Ucase;
import generator.Json;
import generator.template.TestCase;
import evolution.pojo.AnyPojoImpl;
import generator.template.ReflectionAssert;
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
