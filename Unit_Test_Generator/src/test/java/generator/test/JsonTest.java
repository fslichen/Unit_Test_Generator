package generator.test;

import org.junit.Test;

import evolution.controller.dto.AnyDto;
import generator.Json;

public class JsonTest {
	@Test
	public void test() {
		System.out.println(Json.subJson("{\"data\":{\"name\":\"Chen\",\"age\":27}}", "data"));
		System.out.println(Json.splitJsons("[{\"name\":\"Chen\"},{\"address\":\"FS\"}]"));
		System.out.println(Json.splitSubJsons("{    \"data\":[ {    \"name\"   :       \"Chen\"   }   ,   {   \"age\"   : 27   } ] }", "data"));
		System.out.println(Json.fromJson("{    \"name\" :    \"Chen\"   ,     \"age\" :  27}", AnyDto.class));
		System.out.println(Json.fromJsons("[ {    \"name\"   :       \"Chen\"   }   ,   {   \"age\"   : 27   } ]", AnyDto.class));	
		System.out.println(Json.fromSubJson("{\"data\":{\"name\":\"Chen\",\"age\":27}}", "data", AnyDto.class));
		System.out.println(Json.fromSubJsons("{\"data\":[{\"name\":\"Chen\",\"age\":27}]}", "data", AnyDto.class));
	}
	
	@Test
	public void testAdvancedFromJson() throws Exception {
		String name = Json.fromJson("{'data':{'name':'Chen','age':27,'list':['Elsa','Anna']}}".replace("'", "\""), String.class, "data", "list", 1);
		System.out.println(name);
		int age = Json.fromJson("{'data':{'name':'Chen','age':27,'list':['Elsa','Anna']}}".replace("'", "\""), Integer.class, "data", "age");
		System.out.println(age);
		String str = Json.fromJson("{'requestData':{'anyService.anotherMethod':[{'name':'Abraham Lincoln','age':1669620757},984334412]},'responseData':{'anyService.anotherMethod':[{'name':'Richard Nixon','age':755938847},null]}}".replace("'", "\""), String.class, "requestData", "anyService.anotherMethod", 0, "name");
		System.out.println(str);
	}
	
	@Test
	public void testA() throws Exception {
		Integer integer = Json.fromJson("{'name':'Chen','data':[{'age':27},{'age':28}]}".replace("'", "\""), Integer.class, "data", 0, "age");
		System.out.println(integer);
	}
}
