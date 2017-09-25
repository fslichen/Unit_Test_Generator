package evolution.dao;
import java.util.List;
import generator.Json;
import evolution.annotation.ExpectedDatabase4Ucase;
import generator.template.TestCase;
import evolution.annotation.Database4UcaseSetup;
import evolution.dao.AnyDao;
import org.springframework.beans.factory.annotation.Autowired;
import generator.BaseTestCase;
import org.junit.Test;
public class AnyDaoTest extends BaseTestCase {
    @Autowired
    private AnyDao anyDao;
    
    private String name;
    
    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testAnyMethod0() throws Exception {
        TestCase testCase = testCaseClient.getTestCase();
        String requestData = testCase.getRequestData();
        String responseData = testCase.getResponseData();
        List<String> parameterValues = Json.splitSubJsons(requestData, "data");
        anyDao.anyMethod();
    }
    
}
