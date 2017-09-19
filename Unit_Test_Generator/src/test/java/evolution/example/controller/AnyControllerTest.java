package evolution.example.controller;
import org.springframework.beans.factory.annotation.Autowired;
import evolution.example.controller.AnyController;
import org.junit.Test;
import evolution.annotation.Database4UcaseSetup;
import evolution.annotation.ExpectedDatabase4Ucase;
public class AnyControllerTest {
    @Autowired
    private AnyController anyController;
    
    private String name;
    
    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testGet() {
        String requestData = null;
        ObjectMapperPlus objectMapperPlus = new ObjectMapperPlus();
        List<String> jsons = objectMapperPlus.splitJson(requestData);
        anyController.get();
    }
    
    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testPost() {
        String requestData = null;
        ObjectMapperPlus objectMapperPlus = new ObjectMapperPlus();
        List<String> jsons = objectMapperPlus.splitJson(requestData);
        anyController.post(objectMapperPlus.fromJson(jsons.get(0), AnyDto.class));
    }
    
}
