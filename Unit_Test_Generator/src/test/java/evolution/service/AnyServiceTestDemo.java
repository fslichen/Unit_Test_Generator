package evolution.service;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import evolution.annotation.Database4UcaseSetup;
import evolution.annotation.ExpectedDatabase4Ucase;
import evolution.pojo.AnyPojo;
import generator.BaseTestCase;
import generator.Json;
import generator.template.ReflectionAssert;

public class AnyServiceTestDemo extends BaseTestCase {
	@Autowired
	private AnyService anyService;
	
    @Test
    @Database4UcaseSetup
    @ExpectedDatabase4Ucase
    public void testAnotherMethodWithTypesAnyPojoList0() throws Exception {
        String requestData = "{\"data\":[{\"name\":\"Abraham Lincoln\",\"age\":1295568196}],\"status\":\"Success\"}";
        String responseData = "{\"data\":[{\"name\":\"Bill Clinton\",\"age\":-202610213}],\"status\":\"Mocked\"}";
        List<String> parameterValues = Json.splitSubJsons(requestData, "data");
        List<AnyPojo> actualResult = anyService.anotherMethod(Json.fromJson(parameterValues.get(0), AnyPojo.class), 0);
        List<AnyPojo> expectedResult = Json.fromSubJson(responseData, "data", List.class);
        ReflectionAssert.assertReflectionEquals(actualResult, expectedResult);
    }
}
