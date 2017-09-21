package evolution.controller;
import evolution.controller.dto.AnyDto;
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
    public void testGet0() {
        TestCase testCase = testCaseClient.getTestCase();
        String requestData = testCase.getRequestData();
        String responseData = testCase.getResponseData();
        List<String> parameterValues = Json.splitJsonList(requestData, "data");
        int actualResult = anyController.get();
        int expectedResult = Json.fromJson(responseData, int.class);
    }
    
    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testHide0() {
        TestCase testCase = testCaseClient.getTestCase();
        String requestData = testCase.getRequestData();
        String responseData = testCase.getResponseData();
        List<String> parameterValues = Json.splitJsonList(requestData, "data");
        String actualResult = anyController.hide(Json.fromJson(parameterValues.get(0), String.class));
        String expectedResult = Json.fromJson(responseData, String.class);
    }
    
    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testPost0() {
        TestCase testCase = testCaseClient.getTestCase();
        String requestData = testCase.getRequestData();
        String responseData = testCase.getResponseData();
        List<String> parameterValues = Json.splitJsonList(requestData, "data");
        AnyDto actualResult = anyController.post(Json.fromJson(parameterValues.get(0), AnyDto.class));
        AnyDto expectedResult = Json.fromJson(responseData, AnyDto.class);
    }
    
    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testPost1() {
        TestCase testCase = testCaseClient.getTestCase();
        String requestData = testCase.getRequestData();
        String responseData = testCase.getResponseData();
        List<String> parameterValues = Json.splitJsonList(requestData, "data");
        AnyDto actualResult = anyController.post(Json.fromJson(parameterValues.get(0), AnyDto.class));
        AnyDto expectedResult = Json.fromJson(responseData, AnyDto.class);
    }
    
    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testPost2() {
        TestCase testCase = testCaseClient.getTestCase();
        String requestData = testCase.getRequestData();
        String responseData = testCase.getResponseData();
        List<String> parameterValues = Json.splitJsonList(requestData, "data");
        AnyDto actualResult = anyController.post(Json.fromJson(parameterValues.get(0), AnyDto.class));
        AnyDto expectedResult = Json.fromJson(responseData, AnyDto.class);
    }
    
    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testPost3() {
        TestCase testCase = testCaseClient.getTestCase();
        String requestData = testCase.getRequestData();
        String responseData = testCase.getResponseData();
        List<String> parameterValues = Json.splitJsonList(requestData, "data");
        AnyDto actualResult = anyController.post(Json.fromJson(parameterValues.get(0), AnyDto.class));
        AnyDto expectedResult = Json.fromJson(responseData, AnyDto.class);
    }
    
    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testException0() {
        TestCase testCase = testCaseClient.getTestCase();
        String requestData = testCase.getRequestData();
        String responseData = testCase.getResponseData();
        List<String> parameterValues = Json.splitJsonList(requestData, "data");
        AnyDto actualResult = anyController.exception(Json.fromJson(parameterValues.get(0), AnyDto.class));
        AnyDto expectedResult = Json.fromJson(responseData, AnyDto.class);
    }
    
    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testHttp0() {
        TestCase testCase = testCaseClient.getTestCase();
        String requestData = testCase.getRequestData();
        String responseData = testCase.getResponseData();
        List<String> parameterValues = Json.splitJsonList(requestData, "data");
        anyController.http();
    }
    
}
