package evolution.example;
import java.util.List;
import evolution.Json;
import evolution.annotation.ExpectedDatabase4Ucase;
import evolution.annotation.Database4UcaseSetup;
import evolution.example.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.Test;
public class ApplicationTest {
    @Autowired
    private Application application;
    
    private String name;
    
    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testMain() {
        String requestData = null;
        Json json = new Json();
        List<String> parameterValues = json.splitJsonList(requestData);
        application.main(json.fromJson(parameterValues.get(0), String[].class));
    }
    
}
