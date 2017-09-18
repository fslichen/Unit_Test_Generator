package evolution.example.service;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import evolution.ObjectMapperPlus;
import evolution.annotation.Database4UcaseSetup;
import evolution.annotation.ExpectedDatabase4Ucase;
import evolution.example.pojo.AnotherPojo;
import evolution.example.pojo.AnyPojo;
public class AnyServiceTest {
    private String name;
    
    @Autowired
    private AnyService anyService;
    
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