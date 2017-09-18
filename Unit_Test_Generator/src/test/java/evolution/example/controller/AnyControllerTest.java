package evolution.example.controller;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import evolution.example.controller.AnyController;
import evolution.annotation.Database4UcaseSetup;
import evolution.annotation.ExpectedDatabase4Ucase;
public class AnyControllerTest {
    private String name;
    
    @Autowired
    private AnyController anyController;
    
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