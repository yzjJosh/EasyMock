package easymock;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.*;

import tree.TreeNode;

import tree.Tree;


public class MockObjectInvocationHandler implements InvocationHandler{
	public static enum State {
		RECORD, REPLAY
	};
	
	
	private static final Map<Class<?>, Object> DEFAULT_RETURN_VALUES = new HashMap<>();
	
	static{
		DEFAULT_RETURN_VALUES.put(boolean.class, false);
		DEFAULT_RETURN_VALUES.put(byte.class, 0);
		DEFAULT_RETURN_VALUES.put(char.class, '\0');
		DEFAULT_RETURN_VALUES.put(double.class, 0.0);
		DEFAULT_RETURN_VALUES.put(float.class, 0.0f);
		DEFAULT_RETURN_VALUES.put(int.class, 0);
		DEFAULT_RETURN_VALUES.put(long.class, 0L);
		DEFAULT_RETURN_VALUES.put(short.class, 0);
	}
	
	private Tree<InvocationDefinition> tree = new Tree<>();
	private int curIndex = 0;
	private State state = State.RECORD; 
	private boolean inBranch = false;
	private TreeNode<InvocationDefinition> curParent;
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		if(method.equals(HandlerHelper.class.getMethod("getHandler", new Class<?>[0])))
			return this;
		
		ArgumentsPack argsPack = new ArgumentsPack(args);
		Class<?> retType = method.getReturnType();
		
		//Store this invocation information
		LastInvocation.addInvocation(proxy, method, argsPack);
		
		//If we are replaying this mock object
		if(state == State.REPLAY){
			//If the behavior is defined for this invocation, we behave as defined.
			if (curIndex < tree.size()) {
				while (tree.getNode(curIndex).isDummy()) {
					curIndex++;
					inBranch = false;
					if (curIndex >= tree.size())
						throw new IllegalStateException(" Missing behavior definition for the method \"" + method
								+ "\" with arguments \"" + Arrays.toString(args) + "\"");
				}
				//Currently the execution is not in a branch
				if (!inBranch) {
					TreeNode<InvocationDefinition> node = tree.getNode(curIndex);
					InvocationDefinition invocation = node.getVal();
					if (invocation.matches(method, argsPack)) {
						if(invocation.isBehaviorLegal()){
							curIndex = node.getSeq() + 1;
							if (node.getNumOfChildren() > 1) {
								curParent = node;
								inBranch = true;
							}
							return invocation.behavior.behave();
						}
						else
							throw new IllegalStateException(" Missing behavior definition for the method \"" + method
									+ "\" with arguments \"" + Arrays.toString(args) + "\"");
					}
				} else {    //In a branch
					for (TreeNode<InvocationDefinition> node : curParent.getChildren()) {
						InvocationDefinition invocation = node.getVal();
						if (invocation.matches(method, argsPack)){
							if(invocation.isBehaviorLegal()){
								curIndex = node.getSeq() + 1;
								if (node.getNumOfChildren() > 1) {
									curParent = node;
									inBranch = true;
								}
								return invocation.behavior.behave();
							}else
								throw new IllegalStateException(" Missing behavior definition for the method \"" + method
										+ "\" with arguments \"" + Arrays.toString(args) + "\"");
						}
					}
				}
			}
			
			//Otherwise, we throw an exception
			throw new IllegalStateException(" Missing behavior definition for the method \""+method+
					"\" with arguments \""+Arrays.toString(args)+"\"");		
		}
			
		//If we are not replaying, return a default value
		return DEFAULT_RETURN_VALUES.get(retType);
	}
	
	/**
	 * Add an invocation of a method, define its behavior
	 * @param method the method to be added
	 * @param args the arguments of the method
	 * @param behavior the behavior of this invocation
	 * @throws IllegalStateException if is not in record state
	 */
	void addInvocation(Method method, ArgumentsPack args, Behavior behavior){
		if(state != State.RECORD)
			throw new IllegalStateException("Not in record state!");
		tree.addNode(new InvocationDefinition(method, args, behavior));
	}
	
	
	/**
	 * Start recording. This method will clear previously defined behaviors.
	 */
	void record(){
		tree = new Tree<>();
		state = State.RECORD;
	}
	
	/**
	 * Start playing. This method will make this object start playing from the beginning.
	 */
	void replay(){
		curIndex = 0;
		state = State.REPLAY;
	}
	
	/**
	 * Get the current state of this mock object
	 * @return the current state
	 */
	State getState(){
		return state;
	}

}
