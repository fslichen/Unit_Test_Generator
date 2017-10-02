package generator.template.impl;

import generator.template.TestCase;
import generator.template.TestCaseClient;

public class TestCaseClientImpl implements TestCaseClient {
	@Override
	public TestCase getTestCase() {
		return new TestCaseImpl();
	}
}
