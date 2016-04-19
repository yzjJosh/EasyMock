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
		int seq;
		List<TreeNode> children;
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
	}
	
	private TreeNode root;
	private TreeNode curParent;
	private boolean isBranching = false;
	private int size = 0;
	
	/*
	 * add a new node to the tree
	 * @param T the new value to be added
	 */
	public void addNode(T val) {
		if (root == null) {
			root = new TreeNode(val, size);
			curParent = root;
		} else {
			TreeNode cur = new TreeNode(val, size);
			curParent.children.add(cur);
			if (!isBranching) curParent = cur;
		}
		size++;
	}
	
	//the branch starts.
	public void startBranching() {
		isBranching = true;
	}
	
	//the branch ends.
	public void endBranching() {
		TreeNode endNode = new TreeNode(size);
		//Auto connect all the children of the parent node to this end node.
		for (TreeNode node : curParent.children) {
			node.children.add(endNode);
		}
		curParent = endNode;
		size++;
	}

	//Get the current size of the tree.
	public int size() {
		return size;
	}
	
	public TreeNode getNode(int seq) {
		if (seq < 0 || root == null) return null;
		TreeNode temp = root;
		//dfs
		Stack<TreeNode> stack = new Stack<>();
		stack.push(root);
		while (!stack.isEmpty()) {
			TreeNode cur = stack.pop();
			if (cur.seq == seq)
				return cur;
			if (cur.children == null || cur.children.isEmpty()) continue;
			for (TreeNode child : cur.children) {
				if (!stack.contains(child))
					stack.push(child);
			}
		}
		return null;
	}
	
	//Get the node content.
	public T getVal(int seq) {
		TreeNode cur = getNode(seq);
		if (cur != null) return cur.val;
		else return null;
	}
	
	
	
	//Get the node children number.
	public int childrenNumOfNode(int seq) {
		TreeNode cur = getNode(seq);
		if (cur == null) return 0;
		else return cur.children.size();
	}
	
}
