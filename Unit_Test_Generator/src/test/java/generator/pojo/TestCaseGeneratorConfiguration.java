package generator.pojo;

public class TestCaseGeneratorConfiguration {
	private Boolean overwrite;
	private Integer maxTestCaseCount;
	public Boolean getOverwrite() {
		return overwrite;
	}
	public void setOverwrite(Boolean overwrite) {
		this.overwrite = overwrite;
	}
	public Integer getMaxTestCaseCount() {
		return maxTestCaseCount;
	}
	public void setMaxTestCaseCount(Integer maxTestCaseCount) {
		this.maxTestCaseCount = maxTestCaseCount;
	}
	@Override
	public String toString() {
		return "TestCaseGeneratorConfiguration [overwrite=" + overwrite + ", maxTestCaseCount=" + maxTestCaseCount
				+ "]";
	}
}
