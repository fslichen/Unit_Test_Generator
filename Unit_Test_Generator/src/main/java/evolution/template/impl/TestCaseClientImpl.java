package evolution.template.impl;

import evolution.template.TestCase;
import evolution.template.TestCaseClient;

public class TestCaseClientImpl implements TestCaseClient {
	@Override
	public TestCase getTestCase() {
		return new TestCaseImpl();
	}
}
