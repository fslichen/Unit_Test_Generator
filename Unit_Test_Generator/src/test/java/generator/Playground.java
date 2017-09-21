package generator;

import org.junit.Test;

import evolution.controller.dto.AnyDto;

public class Playground {
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
}
