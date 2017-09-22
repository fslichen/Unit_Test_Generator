package evolution.pojo;

import java.util.List;

public class Tree {
	private String id;
	private Tree tree;
	private List<Tree> trees;
	public Tree getTree() {
		return tree;
	}
	public void setTree(Tree tree) {
		this.tree = tree;
	}
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
