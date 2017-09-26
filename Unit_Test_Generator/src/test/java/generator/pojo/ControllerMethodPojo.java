package generator.pojo;

import org.springframework.web.bind.annotation.RequestMethod;

public class ControllerMethodPojo {
	private String requestPath;
	private RequestMethod requestMethod;
	private Class<?> returnType;
	public String getRequestPath() {
		return requestPath;
	}
	public void setRequestPath(String requestPath) {
		this.requestPath = requestPath;
	}
	public RequestMethod getRequestMethod() {
		return requestMethod;
	}
	public void setRequestMethod(RequestMethod requestMethod) {
		this.requestMethod = requestMethod;
	}
	public Class<?> getReturnType() {
		return returnType;
	}
	public void setReturnType(Class<?> returnType) {
		this.returnType = returnType;
	}
}
