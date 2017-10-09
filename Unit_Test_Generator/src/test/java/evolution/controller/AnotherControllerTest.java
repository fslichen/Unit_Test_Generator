package evolution.controller;
import org.junit.Test;
import generator.BaseTestCase;
import evolution.controller.AnotherController;
import org.springframework.beans.factory.annotation.Autowired;
import generator.Json;
import generator.template.TestCase;
import evolution.annotation.Database4UcaseSetup;
import evolution.annotation.ExpectedDatabase4Ucase;
import static org.mockito.Mockito.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.boot.test.mock.mockito.MockBean;
import java.lang.String;
import java.lang.Class;
import java.util.Map;
import java.net.URI;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import evolution.service.AnyService;
import evolution.pojo.AlphaPojo;
public class AnotherControllerTest extends BaseTestCase {
    @Autowired
    private AnotherController anotherController;
    
    @MockBean
    private RestTemplate restTemplate;
    
    @MockBean
    private AnyService anyService;
    
    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testRestTemplateWithTypesPrimitiveVoid0() throws Exception {
        TestCase testCase = testCaseClient.getTestCase();
        String requestData = testCase.getRequestData();
        String responseData = testCase.getResponseData();
        String mockedData = testCase.getMockData();
        when(restTemplate.postForObject(Json.fromJson(mockedData, String.class, "requestData", "restTemplate.postForObject", 0), Json.fromJson(mockedData, Object.class, "requestData", "restTemplate.postForObject", 1), Json.fromJson(mockedData, Class.class, "requestData", "restTemplate.postForObject", 2), Json.fromJson(mockedData, Map.class, "requestData", "restTemplate.postForObject", 3))).thenReturn(Json.fromJson(mockedData, Object.class, "responseData", "restTemplate.postForObject"));
        when(restTemplate.postForObject(Json.fromJson(mockedData, URI.class, "requestData", "restTemplate.postForObject", 0), Json.fromJson(mockedData, Object.class, "requestData", "restTemplate.postForObject", 1), Json.fromJson(mockedData, Class.class, "requestData", "restTemplate.postForObject", 2))).thenReturn(Json.fromJson(mockedData, Object.class, "responseData", "restTemplate.postForObject"));
        when(restTemplate.postForObject(Json.fromJson(mockedData, String.class, "requestData", "restTemplate.postForObject", 0), Json.fromJson(mockedData, Object.class, "requestData", "restTemplate.postForObject", 1), Json.fromJson(mockedData, Class.class, "requestData", "restTemplate.postForObject", 2), Json.fromJson(mockedData, Object[].class, "requestData", "restTemplate.postForObject", 3))).thenReturn(Json.fromJson(mockedData, Object.class, "responseData", "restTemplate.postForObject"));
        String mockedDataToBeUploaded = "{'requestData':{'restTemplate.postForObject':['Donald Trump','Donald Trump',null,null]},'responseData':{'restTemplate.postForObject':null}}";
        mockMvc.perform(get("/another/rest/template")).andExpect(status().isOk());
    }
    
    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testPostAbstractWithTypesAbstractAlphaPojoAlphaPojo0() throws Exception {
        TestCase testCase = testCaseClient.getTestCase();
        String requestData = testCase.getRequestData();
        String responseData = testCase.getResponseData();
        String mockedData = testCase.getMockData();
        when(anyService.anyAbstract(Json.fromJson(mockedData, AlphaPojo.class, "requestData", "anyService.anyAbstract", 0))).thenReturn(Json.fromJson(mockedData, AlphaPojo.class, "responseData", "anyService.anyAbstract"));
        String mockedDataToBeUploaded = "{'requestData':{'anyService.anyAbstract':[{'name':'Bill Clinton'}]},'responseData':{'anyService.anyAbstract':{'name':'Barack Obama'}}}";
        System.out.println("{\"data\":" + mockMvc.perform(post("/another/post/abstract").content("The request body is missing.").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn().getResponse().getContentAsString() + ",\"status\":\"Success\"}");
        mockMvc.perform(post("/another/post/abstract").content("The request body is missing.").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(content().json(Json.toJson(responseData, "data"), false));
    }
    
    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testPostObjectsWithTypesintArrayObjectArray0() throws Exception {
        TestCase testCase = testCaseClient.getTestCase();
        String requestData = testCase.getRequestData();
        String responseData = testCase.getResponseData();
        String mockedData = testCase.getMockData();
        String mockedDataToBeUploaded = "{'requestData':{},'responseData':{}}";
        System.out.println("{\"data\":" + mockMvc.perform(post("/another/post/objects").content("The request body is missing.").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn().getResponse().getContentAsString() + ",\"status\":\"Success\"}");
        mockMvc.perform(post("/another/post/objects").content("The request body is missing.").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(content().json(Json.toJson(responseData, "data"), false));
    }
    
}
