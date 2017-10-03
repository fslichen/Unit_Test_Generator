package evolution.controller;
import org.junit.Test;
import generator.BaseTestCase;
import evolution.controller.AnyController;
import org.springframework.beans.factory.annotation.Autowired;
import generator.Json;
import generator.template.TestCase;
import evolution.annotation.Database4UcaseSetup;
import evolution.annotation.ExpectedDatabase4Ucase;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.mockito.Mockito.*;
import evolution.service.AnyService;
import org.springframework.boot.test.mock.mockito.MockBean;
import evolution.pojo.AnyPojo;
import evolution.pojo.AnotherPojo;
import java.lang.String;
import java.lang.reflect.Method;
import generator.template.ReflectionAssert;
public class AnyControllerTest extends BaseTestCase {
    @Autowired
    private AnyController anyController;
    
    @MockBean
    private AnyService anyService;
    
    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testHttpWithTypesPrimitiveVoid0() throws Exception {
        TestCase testCase = testCaseClient.getTestCase();
        String requestData = testCase.getRequestData();
        String responseData = testCase.getResponseData();
        String mockedData = testCase.getMockData();
        String mockedDataToBeUploaded = "{'requestData':{},'responseData':{}}";
        anyController.http();
    }
    
    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testExceptionWithTypesAnyDtoAnyDto0() throws Exception {
        TestCase testCase = testCaseClient.getTestCase();
        String requestData = testCase.getRequestData();
        String responseData = testCase.getResponseData();
        String mockedData = testCase.getMockData();
        String mockedDataToBeUploaded = "{'requestData':{},'responseData':{}}";
        mockMvc.perform(get("/project/exception")).andExpect(status().isOk());
    }
    
    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testPostWithTypesAnyPojoPrimitiveVoid3() throws Exception {
        TestCase testCase = testCaseClient.getTestCase();
        String requestData = testCase.getRequestData();
        String responseData = testCase.getResponseData();
        String mockedData = testCase.getMockData();
        String mockedDataToBeUploaded = "{'requestData':{'anyService.anotherMethod':[{'name':'George Washington','age':1446562753},-2136636828]},'responseData':{}}";
        System.out.println(mockMvc.perform(post("/project/test/post").content("The request body is missing.").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn().getResponse().getContentAsString());
        mockMvc.perform(post("/project/test/post").content("The request body is missing.").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(content().json(Json.toJson(responseData, "data"), false));
    }
    
    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testPostWithTypesAnyDtoAnyDto3() throws Exception {
        TestCase testCase = testCaseClient.getTestCase();
        String requestData = testCase.getRequestData();
        String responseData = testCase.getResponseData();
        String mockedData = testCase.getMockData();
        when(anyService.anyMethod(Json.fromJson(mockedData, AnyPojo.class, "requestData", "anyService.anyMethod", 0), Json.fromJson(mockedData, AnotherPojo.class, "requestData", "anyService.anyMethod", 1))).thenReturn(Json.fromJson(mockedData, AnyPojo.class, "responseData", "anyService.anyMethod"));
        when(anyService.anyMethod(Json.fromJson(mockedData, AnyPojo.class, "requestData", "anyService.anyMethod", 0))).thenReturn(Json.fromJson(mockedData, AnyPojo.class, "responseData", "anyService.anyMethod"));
        String mockedDataToBeUploaded = "{'requestData':{'anyService.anyMethod':[{'name':'Abraham Lincoln','age':-595792003}]},'responseData':{'anyService.anyMethod':{'name':'Abraham Lincoln','age':-1839218907}}}";
        System.out.println(mockMvc.perform(post("/project/post").content(Json.toJson(requestData, "data", 0)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn().getResponse().getContentAsString());
        mockMvc.perform(post("/project/post").content(Json.toJson(requestData, "data", 0)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(content().json(Json.toJson(responseData, "data"), false));
    }
    
    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testPostAnotherWithTypesPrimitiveVoid0() throws Exception {
        TestCase testCase = testCaseClient.getTestCase();
        String requestData = testCase.getRequestData();
        String responseData = testCase.getResponseData();
        String mockedData = testCase.getMockData();
        String mockedDataToBeUploaded = "{'requestData':{},'responseData':{}}";
        System.out.println(mockMvc.perform(post("/project/another/test/post").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn().getResponse().getContentAsString());
        mockMvc.perform(post("/project/another/test/post").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(content().json(Json.toJson(responseData, "data"), false));
    }
    
    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testHideWithTypesStringString0() throws Exception {
        TestCase testCase = testCaseClient.getTestCase();
        String requestData = testCase.getRequestData();
        String responseData = testCase.getResponseData();
        String mockedData = testCase.getMockData();
        when(anyService.anyMethod(Json.fromJson(mockedData, AnyPojo.class, "requestData", "anyService.anyMethod", 0), Json.fromJson(mockedData, AnotherPojo.class, "requestData", "anyService.anyMethod", 1))).thenReturn(Json.fromJson(mockedData, AnyPojo.class, "responseData", "anyService.anyMethod"));
        when(anyService.anyMethod(Json.fromJson(mockedData, AnyPojo.class, "requestData", "anyService.anyMethod", 0))).thenReturn(Json.fromJson(mockedData, AnyPojo.class, "responseData", "anyService.anyMethod"));
        String mockedDataToBeUploaded = "{'requestData':{'anyService.anyMethod':[{'name':'Abraham Lincoln','age':-841797149}]},'responseData':{'anyService.anyMethod':{'name':'Richard Nixon','age':-387184805}}}";
        try {
            Method method = AnyController.class.getDeclaredMethod("hide", String.class);
            method.setAccessible(true);
            String actualResult = (String) method.invoke(anyController, Json.fromJson(requestData, String.class, "data", 0));
            System.out.println(Json.toJson(actualResult));
            String expectedResult = Json.fromJson(responseData, String.class, "data");
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
        String mockedData = testCase.getMockData();
        String mockedDataToBeUploaded = "{'requestData':{},'responseData':{}}";
        mockMvc.perform(get("/project/servlet/get")).andExpect(status().isOk());
    }
    
    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testAbstractPojoWithTypesAnyAbstractDtoPrimitiveVoid0() throws Exception {
        TestCase testCase = testCaseClient.getTestCase();
        String requestData = testCase.getRequestData();
        String responseData = testCase.getResponseData();
        String mockedData = testCase.getMockData();
        String mockedDataToBeUploaded = "{'requestData':{},'responseData':{}}";
        mockMvc.perform(get("/project/abstract")).andExpect(status().isOk());
    }
    
    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testGetWithTypesPrimitiveInt0() throws Exception {
        TestCase testCase = testCaseClient.getTestCase();
        String requestData = testCase.getRequestData();
        String responseData = testCase.getResponseData();
        String mockedData = testCase.getMockData();
        String mockedDataToBeUploaded = "{'requestData':{},'responseData':{}}";
        mockMvc.perform(get("/project/get")).andExpect(status().isOk());
    }
    
    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testListWithTypesAnyDtoList0() throws Exception {
        TestCase testCase = testCaseClient.getTestCase();
        String requestData = testCase.getRequestData();
        String responseData = testCase.getResponseData();
        String mockedData = testCase.getMockData();
        String mockedDataToBeUploaded = "{'requestData':{},'responseData':{}}";
        mockMvc.perform(get("/project/list")).andExpect(status().isOk());
    }
    
    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testTreeWithTypesTreeList0() throws Exception {
        TestCase testCase = testCaseClient.getTestCase();
        String requestData = testCase.getRequestData();
        String responseData = testCase.getResponseData();
        String mockedData = testCase.getMockData();
        String mockedDataToBeUploaded = "{'requestData':{},'responseData':{}}";
        mockMvc.perform(get("/project/tree")).andExpect(status().isOk());
    }
    
}
