package evolution.controller;
import generator.template.ReflectionAssert;
import java.lang.reflect.Method;
import java.lang.String;
import java.util.List;
import org.springframework.http.MediaType;
import generator.Json;
import evolution.annotation.ExpectedDatabase4Ucase;
import generator.template.TestCase;
import evolution.annotation.Database4UcaseSetup;
import evolution.controller.AnyController;
import org.springframework.beans.factory.annotation.Autowired;
import generator.BaseTestCase;
import org.junit.Test;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
public class AnyControllerTest extends BaseTestCase {
    @Autowired
    private AnyController anyController;
    
    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testGetWithTypesPrimitiveInt0() throws Exception {
        TestCase testCase = testCaseClient.getTestCase();
        String requestData = testCase.getRequestData();
        String responseData = testCase.getResponseData();
        mockMvc.perform(get("/project/get")).andExpect(status().isOk());
    }
    
    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testListWithTypesAnyDtoList0() throws Exception {
        TestCase testCase = testCaseClient.getTestCase();
        String requestData = testCase.getRequestData();
        String responseData = testCase.getResponseData();
        mockMvc.perform(get("/project/list")).andExpect(status().isOk());
    }
    
    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testTreeWithTypesTreeList0() throws Exception {
        TestCase testCase = testCaseClient.getTestCase();
        String requestData = testCase.getRequestData();
        String responseData = testCase.getResponseData();
        mockMvc.perform(get("/project/tree")).andExpect(status().isOk());
    }
    
    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testPostWithTypesAnyPojoPrimitiveVoid0() throws Exception {
        TestCase testCase = testCaseClient.getTestCase();
        String requestData = testCase.getRequestData();
        String responseData = testCase.getResponseData();
        List<String> parameterValues = Json.splitSubJsons(requestData, "data");
        mockMvc.perform(post("/project/test/post").content(parameterValues.get(0)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(content().json(Json.subJson(responseData, "data"), false));
    }
    
    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testPostWithTypesAnyPojoPrimitiveVoid1() throws Exception {
        TestCase testCase = testCaseClient.getTestCase();
        String requestData = testCase.getRequestData();
        String responseData = testCase.getResponseData();
        List<String> parameterValues = Json.splitSubJsons(requestData, "data");
        mockMvc.perform(post("/project/test/post").content(parameterValues.get(0)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(content().json(Json.subJson(responseData, "data"), false));
    }
    
    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testPostWithTypesAnyPojoPrimitiveVoid2() throws Exception {
        TestCase testCase = testCaseClient.getTestCase();
        String requestData = testCase.getRequestData();
        String responseData = testCase.getResponseData();
        List<String> parameterValues = Json.splitSubJsons(requestData, "data");
        mockMvc.perform(post("/project/test/post").content(parameterValues.get(0)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(content().json(Json.subJson(responseData, "data"), false));
    }
    
    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testPostWithTypesAnyPojoPrimitiveVoid3() throws Exception {
        TestCase testCase = testCaseClient.getTestCase();
        String requestData = testCase.getRequestData();
        String responseData = testCase.getResponseData();
        List<String> parameterValues = Json.splitSubJsons(requestData, "data");
        mockMvc.perform(post("/project/test/post").content(parameterValues.get(0)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(content().json(Json.subJson(responseData, "data"), false));
    }
    
    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testPostWithTypesAnyDtoAnyDto0() throws Exception {
        TestCase testCase = testCaseClient.getTestCase();
        String requestData = testCase.getRequestData();
        String responseData = testCase.getResponseData();
        List<String> parameterValues = Json.splitSubJsons(requestData, "data");
        mockMvc.perform(post("/project/post").content(parameterValues.get(0)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(content().json(Json.subJson(responseData, "data"), false));
    }
    
    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testPostWithTypesAnyDtoAnyDto1() throws Exception {
        TestCase testCase = testCaseClient.getTestCase();
        String requestData = testCase.getRequestData();
        String responseData = testCase.getResponseData();
        List<String> parameterValues = Json.splitSubJsons(requestData, "data");
        mockMvc.perform(post("/project/post").content(parameterValues.get(0)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(content().json(Json.subJson(responseData, "data"), false));
    }
    
    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testPostWithTypesAnyDtoAnyDto2() throws Exception {
        TestCase testCase = testCaseClient.getTestCase();
        String requestData = testCase.getRequestData();
        String responseData = testCase.getResponseData();
        List<String> parameterValues = Json.splitSubJsons(requestData, "data");
        mockMvc.perform(post("/project/post").content(parameterValues.get(0)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(content().json(Json.subJson(responseData, "data"), false));
    }
    
    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testPostWithTypesAnyDtoAnyDto3() throws Exception {
        TestCase testCase = testCaseClient.getTestCase();
        String requestData = testCase.getRequestData();
        String responseData = testCase.getResponseData();
        List<String> parameterValues = Json.splitSubJsons(requestData, "data");
        mockMvc.perform(post("/project/post").content(parameterValues.get(0)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(content().json(Json.subJson(responseData, "data"), false));
    }
    
    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testPostAnotherWithTypesPrimitiveVoid0() throws Exception {
        TestCase testCase = testCaseClient.getTestCase();
        String requestData = testCase.getRequestData();
        String responseData = testCase.getResponseData();
        mockMvc.perform(post("/project/another/test/post").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(content().json(Json.subJson(responseData, "data"), false));
    }
    
    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testHideWithTypesStringString0() throws Exception {
        TestCase testCase = testCaseClient.getTestCase();
        String requestData = testCase.getRequestData();
        String responseData = testCase.getResponseData();
        try {
            Method method = AnyController.class.getDeclaredMethod("hide", String.class);
            method.setAccessible(true);
            List<String> parameterValues = Json.splitSubJsons(requestData, "data");
            String actualResult = (String) method.invoke(anyController, Json.fromJson(parameterValues.get(0), String.class));
            String expectedResult = Json.fromSubJson(responseData, "data", String.class);
            ReflectionAssert.assertReflectionEquals(actualResult, expectedResult);
        } catch (Exception e){}
    }
    
    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testServletGetWithTypesHttpServletRequestPrimitiveVoid0() throws Exception {
        TestCase testCase = testCaseClient.getTestCase();
        String requestData = testCase.getRequestData();
        String responseData = testCase.getResponseData();
        mockMvc.perform(get("/project/servlet/get")).andExpect(status().isOk());
    }
    
    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testAbstractPojoWithTypesAnyAbstractDtoPrimitiveVoid0() throws Exception {
        TestCase testCase = testCaseClient.getTestCase();
        String requestData = testCase.getRequestData();
        String responseData = testCase.getResponseData();
        mockMvc.perform(get("/project/abstract")).andExpect(status().isOk());
    }
    
    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testHttpWithTypesPrimitiveVoid0() throws Exception {
        TestCase testCase = testCaseClient.getTestCase();
        String requestData = testCase.getRequestData();
        String responseData = testCase.getResponseData();
        anyController.http();
    }
    
    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testExceptionWithTypesAnyDtoAnyDto0() throws Exception {
        TestCase testCase = testCaseClient.getTestCase();
        String requestData = testCase.getRequestData();
        String responseData = testCase.getResponseData();
        mockMvc.perform(get("/project/exception")).andExpect(status().isOk());
    }
    
}
