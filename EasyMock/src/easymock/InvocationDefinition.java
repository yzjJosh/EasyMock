package easymock;

import java.lang.reflect.Method;

/**
 * Bundle which binds a method, its arguments, and its behavior together
 *
 */
class InvocationDefinition {
	public final Method method;
	public final ArgumentsPack args;
	public final Behavior behavior;
	
	public InvocationDefinition(Method method, ArgumentsPack args, Behavior behavior){
		if(method == null || args == null || behavior == null)
			throw new NullPointerException();
		this.method = method;
		this.args = args;
		this.behavior = behavior;
	}
}
