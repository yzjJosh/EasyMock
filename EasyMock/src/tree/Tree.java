package tree;
import java.util.*;

/* 
 * The flow control tree
 * The tree is used to record the program execution structure.
 */
public class Tree <T> {
	//The definition of tree node.
	class TreeNode {
		T val;
		List<TreeNode> children;
		boolean isDummyNode;  //This is the program or branching end node.
		
		public TreeNode(T val) { 
			isDummyNode = false;
			this.val = val; 
			children = new ArrayList<>();
		}
		public TreeNode() {
			isDummyNode = true;
			children = new ArrayList<>();
		}
	}
	
	private TreeNode root;
	private TreeNode curParent;
	private boolean isBranching = false;
	
	/*
	 * add a new node to the tree
	 * @param T the new value to be added
	 */
	public void addNode(T val) {
		if (root == null) {
			root = new TreeNode(val);
			curParent = root;
		} else {
			TreeNode cur = new TreeNode(val);
			curParent.children.add(cur);
			if (!isBranching) curParent = cur;
		}
	}
	
	//the branch starts.
	public void startBranching() {
		isBranching = true;
	}
	
	//the branch ends.
	public void endBranching() {
		TreeNode endNode = new TreeNode();
		//Auto connect all the children of the parent node to this end node.
		for (TreeNode node : curParent.children) {
			node.children.add(endNode);
		}
		curParent = endNode;
	}

	
	
}
