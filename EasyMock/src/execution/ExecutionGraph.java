package execution;

import java.lang.reflect.Method;
import java.util.*;

/**
 * Execution graph is a graph which represents all legal paths for method invocation.
 * This should be a DAG with a start point, which is called root.
 */
public class ExecutionGraph {
	
	private final Graph<InvocationDefinition> graph;
	private final int root;
	private int curPoint;
	
	private ExecutionGraph(Graph<InvocationDefinition> graph, int root){
		if(graph == null)
			throw new NullPointerException();
		if(root >= graph.size())
			throw new IndexOutOfBoundsException();
		this.graph = graph;
		this.root = root;
		this.curPoint = root;
	}
	
	/**
	 * Reset the execution, put current execution point back to the root.
	 */
	public void reset(){
		curPoint = root;
	}
	
	/**
	 * Find the next invocation which matches given method and arguments, move cursor to that node.
	 * @param method the method to be invoked
	 * @param args the arguments passed to the method
	 * @return the invocation which matches the input. Null if cannot find a match.
	 */
	public InvocationDefinition nextIvocation(Method method, ArgumentsPack args){
		if(curPoint >= graph.size()) return null;
		for(int id: graph.adj(curPoint)){
			InvocationDefinition invocation = graph.get(id);
			if(invocation == null){
				//If it is a dummy node
				int cur = curPoint;
				curPoint = id;
				InvocationDefinition temp = nextIvocation(method, args);
				if(temp != null) return temp;
				curPoint = cur;
			}else{
				if(invocation.matches(method, args)){
					curPoint = id;
					return graph.get(id);
				}
			}
		}
		return null;
	}
	
	public static class Builder{
		
		private static class Branch{
			public final Branch parent;
			public int node; //The node id in graph
			public Branch left;
			public Branch right;
			public Set<Integer> representatives; //The id of representatives of this node
			
			public Branch(int node, Branch parent){
				this.node = node;
				this.parent = parent;
			}
			
			public void split(){
				if(left != null || right != null) return;
				left = new Branch(node, this);
				right = new Branch(node, this);
				left.representatives = representatives;
				right.representatives = representatives;
			}
			
			public Branch brother(){
				if(parent == null) return null;
				return parent.left == this? parent.right: parent.left;
			}
		}
		
		private final Graph<InvocationDefinition> graph;
		private final int root;
		private Branch current;
		
		public Builder(){
			graph = new Graph<>();
			root = graph.add(null);
			current = new Branch(root, null);
			current.representatives = new HashSet<Integer>();
			current.representatives.add(root);
		}
		
		
		private boolean hasConfusion(int cur, Method method, ArgumentsPack args, boolean[] visited){
			if(visited[cur]) return false;
			visited[cur] = true;
			for(int next: graph.adj(cur)){
				InvocationDefinition invocation = graph.get(next);
				if(invocation == null){
					//If it is a dummy node
					if(hasConfusion(next, method, args, visited))
						return true;
				} else if(invocation.matches(method, args))
					return true;
			}
			return false;
		}
		
		private boolean hasConfusion(Set<Integer> begin, Method method, ArgumentsPack args){
			boolean[] visited = new boolean[graph.size()];
			for(int node: begin)
				if(hasConfusion(node, method, args, visited))
					return true;
			return false;
		}
		
		
		/**
		 * Add an invocation to the graph
		 * @param i the invocation
		 * @return if the operation succeeds or not
		 */
		public boolean addInvocation(InvocationDefinition i){
			if(i == null)
				throw new NullPointerException();
			
			//If the invocation is redefined, return false
			if(hasConfusion(current.representatives, i.method, i.args))
				return false;
			
			//Otherwise, we add this invocation
			int id = graph.add(i);
			graph.addEdge(current.node, id);
			current.node = id;
			current.representatives = new HashSet<Integer>();
			current.representatives.add(id);
			return true;
		}
		
		/**
		 * Start a new branch, this method will split current branch into two sub-branches, and set current branch
		 * into one of the two sub-branches
		 */
		public void startBranch(){
			 current.split();
			 current = current.left;
		}
		
		/**
		 * Switch the branch to the brother branch (Branch which has the same parent with current branch).
		 * @return if the operation is successful or not
		 */
		public boolean switchBranch(){
			if(current.parent == null) return false;
			current = current.brother();
			return true;
		}
		
		/**
		 * End the current branch, merge current branch and its brother branch into a single branch.
		 * @return if the operation is successful or not
		 */
		public boolean endBranch(){
			if(current.parent == null) return false;
			Branch brother = current.brother();
			int dummy_node = graph.add(null);
			graph.addEdge(current.node, dummy_node);
			graph.addEdge(brother.node, dummy_node);
			Set<Integer> representatives = current.representatives;
			representatives.addAll(brother.representatives);
			current = current.parent;
			current.left = null;
			current.right = null;
			current.node = dummy_node;
			current.representatives = representatives;
			return true;
		}
		
		public ExecutionGraph build(){
			return new ExecutionGraph(graph, root);
		}
		
	}

}
