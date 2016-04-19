package tree;

import java.util.ArrayList;
import java.util.List;

public class TreeNode <T>{
	T val;
	int seq;
	List<TreeNode<T>> children;
	boolean isDummyNode;  //This is the program or branching end node.
	
	public TreeNode(T val, int seq) { 
		isDummyNode = false;
		this.val = val; 
		this.seq = seq;
		children = new ArrayList<>();
	}
	public TreeNode(int seq) {
		isDummyNode = true;
		this.seq = seq;
		children = new ArrayList<>();
	}
	
	public T getVal() {
		return val;
	}
	
	public int getNumOfChildren() {
		return children.size();
	}
	
	public boolean isDummy() {
		return isDummyNode;
	}
	
	public int getSeq() {
		return seq;
	}
	
	public List<TreeNode<T>> getChildren() {
		return children;
	}
}
