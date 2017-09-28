package generator.codeWriter.pojo;

import java.util.LinkedList;
import java.util.List;

public class IHeader {
	private String packageName;
	private List<String> classNames;
	public IHeader() {
		classNames = new LinkedList<>();
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public List<String> getClassNames() {
		return classNames;
	}
	public void setClassNames(List<String> classNames) {
		this.classNames = classNames;
	}
}
