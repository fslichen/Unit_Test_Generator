package generator.test;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import evolution.pojo.AnyPojo;
import evolution.pojo.Tree;
import generator.Mocker;
import generator.test.pojo.AnyPojo4Test;

public class MockerTest {
	public void anyMethod(List<Map<String, String>> string) {
		
	}
	
	public void anotherMethod(Map<Map<String, String>, Map<String, String>> map) {
		
	}
	
	public void alphaMethod(Map<AnyPojo, List<Map<Map<List<AnyPojo>, Map<String, List<AnyPojo>>>, List<AnyPojo>>>> map) {
		
	}
	
	public void betaMethod(Tree tree) {
		
	}
	
	public void gammaMethod(AnyPojo4Test pojo) {
		
	}
	
	public void anyMethod(AnyPojo anyPojo) {
		
	}
	
	@Test
	public void test() throws NoSuchMethodException, SecurityException {
		Mocker mocker = new Mocker();
		Method method = MockerTest.class.getDeclaredMethod("anyMethod", List.class);
		System.out.println(mocker.mockObject(method.getGenericParameterTypes()[0].getTypeName()));
		Method method0 = MockerTest.class.getDeclaredMethod("anotherMethod", Map.class);
		System.out.println(mocker.mockObject(method0.getGenericParameterTypes()[0].getTypeName()));
		Method method1 = MockerTest.class.getDeclaredMethod("alphaMethod", Map.class);
		System.out.println(mocker.mockObject(method1.getGenericParameterTypes()[0].getTypeName()));
		Method method2 = MockerTest.class.getDeclaredMethod("betaMethod", Tree.class);
		System.out.println(mocker.mockObject(method2.getGenericParameterTypes()[0].getTypeName()));
		Method method3 = MockerTest.class.getDeclaredMethod("gammaMethod", AnyPojo4Test.class);
		System.out.println(mocker.mockObject(method3.getGenericParameterTypes()[0].getTypeName()));
		Method method4 = MockerTest.class.getDeclaredMethod("anyMethod", AnyPojo.class);
		Object obj = mocker.mockObject(method4.getGenericParameterTypes()[0].getTypeName());
		System.out.println(obj);
	}
}
