package easymock;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.*;

import execution.*;


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
	
	private ExecutionGraph.Builder builder = new ExecutionGraph.Builder();
	private ExecutionGraph graph;
	private State state = State.RECORD; 


	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		if(method.equals(HandlerHelper.class.getMethod("getHandler", new Class<?>[0])))
			return this;
		
		ArgumentsPack argsPack = new ArgumentsPack(args);
		Class<?> retType = method.getReturnType();
		
		//Store this invocation information
		LastInvocation.addInvocation(proxy, method, argsPack);
		
		//If we are replaying this mock object
		if(state == State.REPLAY) {
			//If the behavior is defined for this invocation, we behave as defined.
			InvocationDefinition invocation = graph.nextIvocation(method, argsPack);
			if (invocation != null && invocation.isBehaviorLegal()) {
				return invocation.behavior.behave();
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
		builder.addInvocation(new InvocationDefinition(method, args, behavior));
	}
	
	/**
	 * Start branching.
	 */
	void startBranch() {
		builder.startBranch();
	}
	
	/**
	 * Switch branching.
	 */
	void switchBranch() {
		builder.switchBranch();
	}
	
	/**
	 * End branching.
	 */
	void endBranch() {
		builder.endBranch();
	}
	
	
	/**
	 * Start recording. This method will clear previously defined behaviors.
	 */
	void record(){
		builder = new ExecutionGraph.Builder();
		state = State.RECORD;
	}
	
	/**
	 * Start playing. This method will make this object start playing from the beginning.
	 */
	void replay(){
		graph = builder.build();
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
