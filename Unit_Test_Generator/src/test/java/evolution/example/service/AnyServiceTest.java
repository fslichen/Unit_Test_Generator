package evolution.example.service;
import org.springframework.beans.factory.annotation.Autowired;
import evolution.example.service.AnyService;
import org.junit.Test;
import evolution.annotation.Database4UcaseSetup;
import evolution.annotation.ExpectedDatabase4Ucase;
public class AnyServiceTest {
    @Autowired
    private AnyService anyService;
    
    private String name;
    
    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testAnyMethod() {
        String requestData = null;
        ObjectMapperPlus objectMapperPlus = new ObjectMapperPlus();
        List<String> jsons = objectMapperPlus.splitJson(requestData);
        anyService.anyMethod(objectMapperPlus.fromJson(jsons.get(0), AnyPojo.class), objectMapperPlus.fromJson(jsons.get(1), AnotherPojo.class));
    }
    
}
