package evolution.controller;
import evolution.controller.dto.AnyDto;
import java.util.List;
import generator.Json;
import evolution.annotation.ExpectedDatabase4Ucase;
import evolution.annotation.Database4UcaseSetup;
import evolution.controller.AnyController;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.Test;
public class AnyControllerTest {
    @Autowired
    private AnyController anyController;
    
    private String name;
    
    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testGet0() {
        String requestData = null;
        String responseData = null;
        Json json = new Json();
        List<String> parameterValues = json.splitJsonList(requestData);
        anyController.get();
    }
    
    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testPost0() {
        String requestData = null;
        String responseData = null;
        Json json = new Json();
        List<String> parameterValues = json.splitJsonList(requestData);
        AnyDto actualResult = anyController.post(json.fromJson(parameterValues.get(0), AnyDto.class));
        AnyDto expectedResult = json.fromJson(responseData, AnyDto.class);
    }
    
    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testPost1() {
        String requestData = null;
        String responseData = null;
        Json json = new Json();
        List<String> parameterValues = json.splitJsonList(requestData);
        AnyDto actualResult = anyController.post(json.fromJson(parameterValues.get(0), AnyDto.class));
        AnyDto expectedResult = json.fromJson(responseData, AnyDto.class);
    }
    
    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testPost2() {
        String requestData = null;
        String responseData = null;
        Json json = new Json();
        List<String> parameterValues = json.splitJsonList(requestData);
        AnyDto actualResult = anyController.post(json.fromJson(parameterValues.get(0), AnyDto.class));
        AnyDto expectedResult = json.fromJson(responseData, AnyDto.class);
    }
    
    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testPost3() {
        String requestData = null;
        String responseData = null;
        Json json = new Json();
        List<String> parameterValues = json.splitJsonList(requestData);
        AnyDto actualResult = anyController.post(json.fromJson(parameterValues.get(0), AnyDto.class));
        AnyDto expectedResult = json.fromJson(responseData, AnyDto.class);
    }
    
    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testHttp0() {
        String requestData = null;
        String responseData = null;
        Json json = new Json();
        List<String> parameterValues = json.splitJsonList(requestData);
        anyController.http();
    }
    
}
