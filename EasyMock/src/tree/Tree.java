package tree;
import java.util.*;


/**
 * The flow control tree
 * The tree is used to record the program execution structure.
 */
public class Tree <T> {
	//The definition of tree node.

	
	private TreeNode<T> root;
	private TreeNode<T> curParent;
	private boolean isBranching = false;
	private int size = 0;
	
	/**
	 * add a new node to the tree
	 * @param val the new value to be added
	 */
	public void addNode(T val) {
		if (root == null) {
			root = new TreeNode<T>(val, size);
			curParent = root;
		} else {
			TreeNode<T> cur = new TreeNode<T>(val, size);
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
		TreeNode<T> endNode = new TreeNode<>(size);
		//Auto connect all the children of the parent node to this end node.
		for (TreeNode<T> node : curParent.children) {
			node.children.add(endNode);
		}
		curParent = endNode;
		size++;
	}

	//Get the current size of the tree.
	public int size() {
		return size;
	}
	
	public TreeNode<T> getNode(int seq) {
		if (seq < 0 || seq >= size || root == null) return null;
		//dfs
		Stack<TreeNode<T>> stack = new Stack<>();
		stack.push(root);
		while (!stack.isEmpty()) {
			TreeNode<T> cur = stack.pop();
			if (cur.seq == seq)
				return cur;
			if (cur.children == null || cur.children.isEmpty()) continue;
			for (TreeNode<T> child : cur.children) {
				if (!stack.contains(child))
					stack.push(child);
			}
		}
		return null;
	}
		
}
