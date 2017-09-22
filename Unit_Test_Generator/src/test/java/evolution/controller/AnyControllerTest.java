package evolution.controller;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.String;
import evolution.pojo.Tree;
import evolution.controller.dto.AnyDto;
import generator.template.ReflectionAssert;
import java.util.List;
import generator.Json;
import evolution.annotation.ExpectedDatabase4Ucase;
import generator.template.TestCase;
import evolution.annotation.Database4UcaseSetup;
import evolution.controller.AnyController;
import org.springframework.beans.factory.annotation.Autowired;
import generator.BaseTest;
import org.junit.Test;
public class AnyControllerTest extends BaseTest {
    @Autowired
    private AnyController anyController;
    
    private String name;
    
    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testGet0() throws Exception {
        TestCase testCase = testCaseClient.getTestCase();
        String requestData = testCase.getRequestData();
        String responseData = testCase.getResponseData();
        List<String> parameterValues = Json.splitSubJsons(requestData, "data");
        int actualResult = anyController.get();
        int expectedResult = Json.fromSubJson(responseData, "data", int.class);
        ReflectionAssert.assertReflectionEquals(actualResult, expectedResult);
    }
    
    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testList0() throws Exception {
        TestCase testCase = testCaseClient.getTestCase();
        String requestData = testCase.getRequestData();
        String responseData = testCase.getResponseData();
        List<String> parameterValues = Json.splitSubJsons(requestData, "data");
        List<AnyDto> actualResult = anyController.list(Json.fromJson(parameterValues.get(0), AnyDto.class));
        List<AnyDto> expectedResult = Json.fromSubJson(responseData, "data", List.class);
        ReflectionAssert.assertReflectionEquals(actualResult, expectedResult);
    }
    
    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testTree0() throws Exception {
        TestCase testCase = testCaseClient.getTestCase();
        String requestData = testCase.getRequestData();
        String responseData = testCase.getResponseData();
        List<String> parameterValues = Json.splitSubJsons(requestData, "data");
        List<Tree> actualResult = anyController.tree(Json.fromJson(parameterValues.get(0), Tree.class));
        List<Tree> expectedResult = Json.fromSubJson(responseData, "data", List.class);
        ReflectionAssert.assertReflectionEquals(actualResult, expectedResult);
    }
    
    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testPost0() throws Exception {
        TestCase testCase = testCaseClient.getTestCase();
        String requestData = testCase.getRequestData();
        String responseData = testCase.getResponseData();
        List<String> parameterValues = Json.splitSubJsons(requestData, "data");
        AnyDto actualResult = anyController.post(Json.fromJson(parameterValues.get(0), AnyDto.class));
        AnyDto expectedResult = Json.fromSubJson(responseData, "data", AnyDto.class);
        ReflectionAssert.assertReflectionEquals(actualResult, expectedResult);
    }
    
    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testPost1() throws Exception {
        TestCase testCase = testCaseClient.getTestCase();
        String requestData = testCase.getRequestData();
        String responseData = testCase.getResponseData();
        List<String> parameterValues = Json.splitSubJsons(requestData, "data");
        AnyDto actualResult = anyController.post(Json.fromJson(parameterValues.get(0), AnyDto.class));
        AnyDto expectedResult = Json.fromSubJson(responseData, "data", AnyDto.class);
        ReflectionAssert.assertReflectionEquals(actualResult, expectedResult);
    }
    
    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testPost2() throws Exception {
        TestCase testCase = testCaseClient.getTestCase();
        String requestData = testCase.getRequestData();
        String responseData = testCase.getResponseData();
        List<String> parameterValues = Json.splitSubJsons(requestData, "data");
        AnyDto actualResult = anyController.post(Json.fromJson(parameterValues.get(0), AnyDto.class));
        AnyDto expectedResult = Json.fromSubJson(responseData, "data", AnyDto.class);
        ReflectionAssert.assertReflectionEquals(actualResult, expectedResult);
    }
    
    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testPost3() throws Exception {
        TestCase testCase = testCaseClient.getTestCase();
        String requestData = testCase.getRequestData();
        String responseData = testCase.getResponseData();
        List<String> parameterValues = Json.splitSubJsons(requestData, "data");
        AnyDto actualResult = anyController.post(Json.fromJson(parameterValues.get(0), AnyDto.class));
        AnyDto expectedResult = Json.fromSubJson(responseData, "data", AnyDto.class);
        ReflectionAssert.assertReflectionEquals(actualResult, expectedResult);
    }
    
    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testHide0() throws Exception {
        TestCase testCase = testCaseClient.getTestCase();
        String requestData = testCase.getRequestData();
        String responseData = testCase.getResponseData();
        List<String> parameterValues = Json.splitSubJsons(requestData, "data");
        try {
            Method method = AnyController.class.getDeclaredMethod("hide", String.class);
            method.setAccessible(true);
            String actualResult = (String) method.invoke(anyController, Json.fromJson(parameterValues.get(0), String.class));
        } catch (Exception e){}
    }
    
    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testServletGet0() throws Exception {
        TestCase testCase = testCaseClient.getTestCase();
        String requestData = testCase.getRequestData();
        String responseData = testCase.getResponseData();
        List<String> parameterValues = Json.splitSubJsons(requestData, "data");
        anyController.servletGet(Json.fromJson(parameterValues.get(0), HttpServletRequest.class));
    }
    
    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testException0() throws Exception {
        TestCase testCase = testCaseClient.getTestCase();
        String requestData = testCase.getRequestData();
        String responseData = testCase.getResponseData();
        List<String> parameterValues = Json.splitSubJsons(requestData, "data");
        AnyDto actualResult = anyController.exception(Json.fromJson(parameterValues.get(0), AnyDto.class));
        AnyDto expectedResult = Json.fromSubJson(responseData, "data", AnyDto.class);
        ReflectionAssert.assertReflectionEquals(actualResult, expectedResult);
    }
    
    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testHttp0() throws Exception {
        TestCase testCase = testCaseClient.getTestCase();
        String requestData = testCase.getRequestData();
        String responseData = testCase.getResponseData();
        List<String> parameterValues = Json.splitSubJsons(requestData, "data");
        anyController.http();
    }
    
}
