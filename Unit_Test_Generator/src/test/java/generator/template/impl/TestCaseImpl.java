package generator.template.impl;

import generator.template.TestCase;

public class TestCaseImpl implements TestCase {
	@Override
	public String getRequestData() {
		return "{'session':'ebe8d24f-bf5d-4f9e-ab54-26040f7c2b9e','requestPath':'/real/post','data':[{'name':'George Washington','age':1598897994,'date':1507632626273}],'requestMethod':'POST'}".replace("'", "\"");
	}

	@Override
	public String getResponseData() {
		return "{'data':{'address':null},'status':'Success'}".replace("'", "\"");
	}

	@Override
	public String getMockData() {
		return "{'requestData':{'realService.anyMethod':[{'name':'George Washington','age':1598897994,'date':1507632626273}]},'responseData':{'realService.anyMethod':{'address':null}}}".replace("'", "\"");
	}
}
