package evolution.pojo;

import java.util.List;

public class Tree {
	private String id;
	private List<Tree> trees;
	public List<Tree> getTrees() {
		return trees;
	}
	public void setTrees(List<Tree> trees) {
		this.trees = trees;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@Override
	public String toString() {
		return "Tree [id=" + id + ", trees=" + trees + "]";
	}
}
