package evolution.controller;
import evolution.controller.dto.AnyAbstractDto;
import javax.servlet.http.HttpServletRequest;
import generator.template.ReflectionAssert;
import java.lang.reflect.Method;
import java.lang.String;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import evolution.pojo.Tree;
import evolution.controller.dto.AnyDto;
import java.util.List;
import generator.Json;
import evolution.annotation.ExpectedDatabase4Ucase;
import generator.template.TestCase;
import evolution.annotation.Database4UcaseSetup;
import org.springframework.test.web.servlet.MockMvc;
import evolution.controller.AnyController;
import org.springframework.beans.factory.annotation.Autowired;
import generator.BaseTestCase;
import org.junit.Test;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
public class AnyControllerTest extends BaseTestCase {
    @Autowired
    private AnyController anyController;
    
    private MockMvc mockMvc;
    
    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testGetWithParameterTypesAndReturnTypePrimitiveInt0() throws Exception {
        TestCase testCase = testCaseClient.getTestCase();
        String requestData = testCase.getRequestData();
        String responseData = testCase.getResponseData();
        List<String> parameterValues = Json.splitSubJsons(requestData, "data");
    }
    
    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testListWithParameterTypesAnyDtoAndReturnTypeList0() throws Exception {
        TestCase testCase = testCaseClient.getTestCase();
        String requestData = testCase.getRequestData();
        String responseData = testCase.getResponseData();
        List<String> parameterValues = Json.splitSubJsons(requestData, "data");
    }
    
    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testTreeWithParameterTypesTreeAndReturnTypeList0() throws Exception {
        TestCase testCase = testCaseClient.getTestCase();
        String requestData = testCase.getRequestData();
        String responseData = testCase.getResponseData();
        List<String> parameterValues = Json.splitSubJsons(requestData, "data");
    }
    
    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testPostWithParameterTypesAnyDtoAndReturnTypeAnyDto0() throws Exception {
        TestCase testCase = testCaseClient.getTestCase();
        String requestData = testCase.getRequestData();
        String responseData = testCase.getResponseData();
        List<String> parameterValues = Json.splitSubJsons(requestData, "data");
        mockMvc.perform(MockMvcRequestBuilders.post("/post").content(parameterValues.get(0)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(content().json(Json.subJson(responseData, "data"), false));
    }
    
    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testPostWithParameterTypesAnyDtoAndReturnTypeAnyDto1() throws Exception {
        TestCase testCase = testCaseClient.getTestCase();
        String requestData = testCase.getRequestData();
        String responseData = testCase.getResponseData();
        List<String> parameterValues = Json.splitSubJsons(requestData, "data");
        mockMvc.perform(MockMvcRequestBuilders.post("/post").content(parameterValues.get(0)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(content().json(Json.subJson(responseData, "data"), false));
    }
    
    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testPostWithParameterTypesAnyDtoAndReturnTypeAnyDto2() throws Exception {
        TestCase testCase = testCaseClient.getTestCase();
        String requestData = testCase.getRequestData();
        String responseData = testCase.getResponseData();
        List<String> parameterValues = Json.splitSubJsons(requestData, "data");
        mockMvc.perform(MockMvcRequestBuilders.post("/post").content(parameterValues.get(0)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(content().json(Json.subJson(responseData, "data"), false));
    }
    
    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testPostWithParameterTypesAnyDtoAndReturnTypeAnyDto3() throws Exception {
        TestCase testCase = testCaseClient.getTestCase();
        String requestData = testCase.getRequestData();
        String responseData = testCase.getResponseData();
        List<String> parameterValues = Json.splitSubJsons(requestData, "data");
        mockMvc.perform(MockMvcRequestBuilders.post("/post").content(parameterValues.get(0)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(content().json(Json.subJson(responseData, "data"), false));
    }
    
    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testHideWithParameterTypesStringAndReturnTypeString0() throws Exception {
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
    public void testServletGetWithParameterTypesHttpServletRequestAndReturnTypePrimitiveVoid0() throws Exception {
        TestCase testCase = testCaseClient.getTestCase();
        String requestData = testCase.getRequestData();
        String responseData = testCase.getResponseData();
        List<String> parameterValues = Json.splitSubJsons(requestData, "data");
    }
    
    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testAbstractPojoWithParameterTypesAnyAbstractDtoAndReturnTypePrimitiveVoid0() throws Exception {
        TestCase testCase = testCaseClient.getTestCase();
        String requestData = testCase.getRequestData();
        String responseData = testCase.getResponseData();
        List<String> parameterValues = Json.splitSubJsons(requestData, "data");
    }
    
    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testHttpWithParameterTypesAndReturnTypePrimitiveVoid0() throws Exception {
        TestCase testCase = testCaseClient.getTestCase();
        String requestData = testCase.getRequestData();
        String responseData = testCase.getResponseData();
        List<String> parameterValues = Json.splitSubJsons(requestData, "data");
    }
    
    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testExceptionWithParameterTypesAnyDtoAndReturnTypeAnyDto0() throws Exception {
        TestCase testCase = testCaseClient.getTestCase();
        String requestData = testCase.getRequestData();
        String responseData = testCase.getResponseData();
        List<String> parameterValues = Json.splitSubJsons(requestData, "data");
    }
    
}
