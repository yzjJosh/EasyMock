package easymock;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.*;
import tree.Tree;


public class MockObjectInvocationHandler implements InvocationHandler{
	public static enum State {
		RECORD, REPLAY
	};
	
	
	private static final Map<Class<?>, Object> PRIMITIVES_DEFAULT_VALUES = new HashMap<>();
	
	static{
		PRIMITIVES_DEFAULT_VALUES.put(boolean.class, false);
		PRIMITIVES_DEFAULT_VALUES.put(byte.class, 0);
		PRIMITIVES_DEFAULT_VALUES.put(char.class, '\0');
		PRIMITIVES_DEFAULT_VALUES.put(double.class, 0.0);
		PRIMITIVES_DEFAULT_VALUES.put(float.class, 0.0f);
		PRIMITIVES_DEFAULT_VALUES.put(int.class, 0);
		PRIMITIVES_DEFAULT_VALUES.put(long.class, 0L);
		PRIMITIVES_DEFAULT_VALUES.put(short.class, 0);
		PRIMITIVES_DEFAULT_VALUES.put(void.class, null);
	}
	
	private ArrayList<InvocationDefinition> queue = new ArrayList<>();
	private Tree<InvocationDefinition> tree = new Tree<>();
	private int curIndex = 0;
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
		if(state == State.REPLAY){
			//If the behavior is defined for this invocation, we behave as defined.
			if(curIndex < queue.size()){
				InvocationDefinition invocation = queue.get(curIndex);
				if(invocation.method.equals(method) && invocation.args.equals(argsPack)&&
					(retType == void.class || retType == Void.class || 
					invocation.behavior.hasThrowable() || invocation.behavior.hasReturnValue())){
					curIndex ++;
					return invocation.behavior.behave();
				}
			}
			
			//Otherwise, we throw an exception
			throw new IllegalStateException(" Missing behavior definition for the method \""+method+
					"\" with arguments \""+Arrays.toString(args)+"\"");		
		}
			
		//If we are not replaying, return a default value
		if(retType.isPrimitive())
			return PRIMITIVES_DEFAULT_VALUES.get(retType);
		else
			return null;
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
		queue.add(new InvocationDefinition(method, args, behavior));
	}
	
	
	/**
	 * Start recording. This method will clear previously defined behaviors.
	 */
	void record(){
		queue = new ArrayList<InvocationDefinition>();
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
