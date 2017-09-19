package evolution.service;
import evolution.pojo.AnotherPojo;
import evolution.pojo.AnyPojo;
import java.util.List;
import generator.Json;
import evolution.annotation.ExpectedDatabase4Ucase;
import evolution.annotation.Database4UcaseSetup;
import evolution.service.AnyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.Test;
public class AnyServiceTest {
    @Autowired
    private AnyService anyService;
    
    private String name;
    
    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testAnyMethod0() {
        String requestData = null;
        String responseData = null;
        Json json = new Json();
        List<String> parameterValues = json.splitJsonList(requestData);
        AnyPojo actualResult = anyService.anyMethod(json.fromJson(parameterValues.get(0), AnyPojo.class), json.fromJson(parameterValues.get(1), AnotherPojo.class));
        AnyPojo expectedResult = json.fromJson(responseData, AnyPojo.class);
    }
    
    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testAnyMethod1() {
        String requestData = null;
        String responseData = null;
        Json json = new Json();
        List<String> parameterValues = json.splitJsonList(requestData);
        AnyPojo actualResult = anyService.anyMethod(json.fromJson(parameterValues.get(0), AnyPojo.class), json.fromJson(parameterValues.get(1), AnotherPojo.class));
        AnyPojo expectedResult = json.fromJson(responseData, AnyPojo.class);
    }
    
}
