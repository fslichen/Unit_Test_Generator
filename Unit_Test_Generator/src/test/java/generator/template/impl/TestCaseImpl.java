package generator.template.impl;

import generator.template.TestCase;

public class TestCaseImpl implements TestCase {
	@Override
	public String getRequestData() {
		return "{'session':'ebe8d24f-bf5d-4f9e-ab54-26040f7c2b9e','requestPath':'/real/post','data':[{'name':'George Washington','age':532646848}],'requestMethod':'POST'}".replace("'", "\"");
	}

	@Override
	public String getResponseData() {
		return "{'data':{'address':null},'status':'Success'}".replace("'", "\"");
	}

	@Override
	public String getMockData() {
		// TODO Auto-generated method stub
		return null;
	}
}
