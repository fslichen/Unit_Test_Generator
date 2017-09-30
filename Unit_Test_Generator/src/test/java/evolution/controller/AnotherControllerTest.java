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
import evolution.service.AnyService;
import org.springframework.boot.test.mock.mockito.MockBean;
import evolution.pojo.AlphaPojo;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
public class AnotherControllerTest extends BaseTestCase {
    @Autowired
    private AnotherController anotherController;
    
    @MockBean
    private AnyService anyService;
    
    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testPostAbstractWithTypesAbstractAlphaPojoAlphaPojo0() throws Exception {
        TestCase testCase = testCaseClient.getTestCase();
        String requestData = testCase.getRequestData();
        String responseData = testCase.getResponseData();
        String mockedData = testCase.getMockData();
        when(anyService.anyAbstract(Json.fromJson(mockedData, AlphaPojo.class, "requestData", "anyService.anyAbstract", 0))).thenReturn(Json.fromJson(mockedData, AlphaPojo.class, "responseData", "anyService.anyAbstract"));
        String mockedDataToBeUploaded = "{'requestData':{'anyService.anyAbstract':[{'name':'Richard Nixon'}]},'responseData':{'anyService.anyAbstract':{'name':'Donald Trump'}}}";
        mockMvc.perform(post("/another/post/abstract").content("The request body is missing.").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(content().json(Json.toJson(responseData, "data"), false));
    }
    
}
