package execution;
import java.util.*;


/**
 * Data structure which represents a graph
 */
public class Graph <T> {
	
	private Map<Integer, LinkedList<Integer>> edges = new HashMap<>();
	private Map<Integer, T> values = new HashMap<>();
	
	/**
	 * add a new node to the DAG
	 * @param val the new value to be added  
	 * @return the id of the new added node;
	 */
	public int add(T val) {
		int id = values.size();
		values.put(id, val);
		edges.put(id, new LinkedList<Integer>());
		return id;
	}
	
	/**
	 * Get the node represented by an id
	 * @param id the id of a node
	 * @return the value associated with that node, null if id does not exist
	 */
	public T get(int id){
		return values.get(id);
	}
	
	/**
	 * Get the nodes which are pointed from this node
	 * @param id the id of a node
	 * @return id of nodes pointed from this node
	 */
	public Iterable<Integer> adj(int id){
		return edges.get(id);
	}
	
	/**
	 * Add an edge to the graph
	 * @param from the id of from node
	 * @param to the id of the to node
	 */
	public void addEdge(int from, int to){
		if(from >= size() || to >= size())
			throw new IndexOutOfBoundsException();
		edges.get(from).add(to);
	}
	
	/**
	 * Return the number of nodes in this graph
	 * @return the size of this graph
	 */
	public int size(){
		return values.size();
	}
		
}
