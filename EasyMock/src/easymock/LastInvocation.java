package easymock;

import java.lang.reflect.Method;

/**
 * Last Invocation stores information of the last invoked method
 *
 */
public class LastInvocation {
	
	private static LastInvocation lastInvocation;
	
	private final Object mock;
	private final Method method;
	private final ArgumentsPack args;
	
	/**
	 * Get information of the last invocation.
	 * @return the LastInvocation object
	 */
	static LastInvocation getLastInvocation(){
		return lastInvocation;
	}
	
	static void addInvocation(Object mock, Method method, ArgumentsPack args){
		lastInvocation = new LastInvocation(mock, method, args);
	}
	
	private LastInvocation(Object mock, Method method, ArgumentsPack args){
		if(method == null || args == null)
			throw new NullPointerException();
		this.mock = mock;
		this.method = method;
		this.args = args;
	}
	
	public Method getMethod(){
		return method;
	}
	
	public ArgumentsPack getArgument(){
		return args;
	}
	
	public Object getMockObject(){
		return mock;
	}
	
	
	
}
