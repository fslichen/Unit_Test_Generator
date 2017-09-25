package evolution.controller;
import evolution.controller.dto.AnyAbstractDto;
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
import generator.BaseTestCase;
import org.junit.Test;
public class AnyControllerTest extends BaseTestCase {
    @Autowired
    private AnyController anyController;
    
    private String name;
    
    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testGetWithParameterTypesIntAndReturnTypePrimitiveInt0() throws Exception {
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
    public void testListWithParameterTypesAnyDtoListAndReturnTypeList0() throws Exception {
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
    public void testTreeWithParameterTypesTreeListAndReturnTypeList0() throws Exception {
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
    public void testPostWithParameterTypesAnyDtoAnyDtoAndReturnTypeAnyDto0() throws Exception {
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
    public void testPostWithParameterTypesAnyDtoAnyDtoAndReturnTypeAnyDto1() throws Exception {
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
    public void testPostWithParameterTypesAnyDtoAnyDtoAndReturnTypeAnyDto2() throws Exception {
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
    public void testPostWithParameterTypesAnyDtoAnyDtoAndReturnTypeAnyDto3() throws Exception {
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
    public void testHideWithParameterTypesStringStringAndReturnTypeString0() throws Exception {
        TestCase testCase = testCaseClient.getTestCase();
        String requestData = testCase.getRequestData();
        String responseData = testCase.getResponseData();
        List<String> parameterValues = Json.splitSubJsons(requestData, "data");
        try {
            Method method = AnyController.class.getDeclaredMethod("hide", String.class);
            method.setAccessible(true);
            String actualResult = (String) method.invoke(anyController, Json.fromJson(parameterValues.get(0), String.class));
            String expectedResult = Json.fromSubJson(responseData, "data", String.class);
            ReflectionAssert.assertReflectionEquals(actualResult, expectedResult);
        } catch (Exception e){}
    }
    
    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testServletGetWithParameterTypesHttpServletRequestVoidAndReturnTypePrimitiveVoid0() throws Exception {
        TestCase testCase = testCaseClient.getTestCase();
        String requestData = testCase.getRequestData();
        String responseData = testCase.getResponseData();
        List<String> parameterValues = Json.splitSubJsons(requestData, "data");
        anyController.servletGet(Json.fromJson(parameterValues.get(0), HttpServletRequest.class));
    }
    
    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testAbstractPojoWithParameterTypesAnyAbstractDtoVoidAndReturnTypePrimitiveVoid0() throws Exception {
        TestCase testCase = testCaseClient.getTestCase();
        String requestData = testCase.getRequestData();
        String responseData = testCase.getResponseData();
        List<String> parameterValues = Json.splitSubJsons(requestData, "data");
        anyController.abstractPojo(Json.fromJson(parameterValues.get(0), AnyAbstractDto.class));
    }
    
    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testHttpWithParameterTypesVoidAndReturnTypePrimitiveVoid0() throws Exception {
        TestCase testCase = testCaseClient.getTestCase();
        String requestData = testCase.getRequestData();
        String responseData = testCase.getResponseData();
        List<String> parameterValues = Json.splitSubJsons(requestData, "data");
        anyController.http();
    }
    
    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testExceptionWithParameterTypesAnyDtoAnyDtoAndReturnTypeAnyDto0() throws Exception {
        TestCase testCase = testCaseClient.getTestCase();
        String requestData = testCase.getRequestData();
        String responseData = testCase.getResponseData();
        List<String> parameterValues = Json.splitSubJsons(requestData, "data");
        AnyDto actualResult = anyController.exception(Json.fromJson(parameterValues.get(0), AnyDto.class));
        AnyDto expectedResult = Json.fromSubJson(responseData, "data", AnyDto.class);
        ReflectionAssert.assertReflectionEquals(actualResult, expectedResult);
    }
    
}
