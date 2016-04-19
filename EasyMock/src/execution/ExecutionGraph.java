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
		for(int id: graph.adj(curPoint))
			if(graph.get(id).matches(method, args)){
				curPoint = id;
				return graph.get(id);
			}
		return null;
	}
	
	public static class Builder{
		
		private final Graph<InvocationDefinition> graph;
		private final Stack<Integer> switchPoints;
		private final Stack<LinkedList<Integer>> branches;
		private final int root;
		
		public Builder(){
			graph = new Graph<>();
			switchPoints = new Stack<>();
			branches = new Stack<>();
			root = graph.add(null);
			LinkedList<Integer> mainBranch = new LinkedList<Integer>();
			mainBranch.add(0);
			branches.add(mainBranch);
		}
		
		private int currentNode(){
			return branches.peek().peekFirst();
		}
		
		private int setCurrentNode(int val){
			branches.peek().pollFirst();
			branches.peek().addFirst(val);
			return val;
		}
		
		/**
		 * Add an invocation to the graph
		 * @param i the invocation
		 * @return if the operation succeeds or not
		 */
		public boolean addInvocation(InvocationDefinition i){
			if(i == null)
				throw new NullPointerException();
			
			int cur = currentNode();
			
			//If the invocation is already defined in some nodes pointed from current node, return false
			for(int id: graph.adj(cur))
				if(graph.get(id).matches(i.method, i.args))
					return false;
			
			//Otherwise, we add this invocation
			graph.addEdge(cur, setCurrentNode(graph.add(i)));
			return true;
		}
		
		public void startBranch(){
			
		}
		
		/**
		 * Switch the branch to the brother branch (Branch which has the same parent with current branch).
		 * @return if the operation is successful or not
		 */
		public boolean switchBranch(){
			LinkedList<Integer> branch = branches.peek();
			branch.add(branch.pollFirst());
			return true;
		}
		
		/**
		 * End the current branch, merge current branch and its brother branch into a single branch.
		 * @return if the operation is successful or not
		 */
		public boolean endBranch(){
			return true;
		}
		
		public ExecutionGraph build(){
			return null;
		}
		
	}

}
