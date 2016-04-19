package easymock;

import java.lang.reflect.Method;

/**
 * Bundle which binds a method, its arguments, and its behavior together
 *
 */
public class InvocationDefinition {
	public final Method method;
	public final ArgumentsPack args;
	public final Behavior behavior;
	
	/**
	 * Associate a method invocation with its behavior
	 * @param method the method to be invoked
	 * @param args the arguments for this invocation
	 * @param behavior the behavior to be behaved for this invocation
	 * @throws NullPointerException if any of the arguments is null pointer
	 */
	public InvocationDefinition(Method method, ArgumentsPack args, Behavior behavior){
		if(method == null || args == null || behavior == null)
			throw new NullPointerException();
		this.method = method;
		this.args = args;
		this.behavior = behavior;
	}
	
	/**
	 * Check if the defined behavior is legal for this method. E.g., if the method is supposed to return a value,
	 * but the behavior neither has a return value or a throwable object to throw, then the behavior is illegal.
	 * Similarly, if the method is supposed not to return a value, but the behavior will return a value, the behavior
	 * is illegal too.
	 * @return if the behavior is legal for this method
	 */
	public boolean isBehaviorLegal(){
		Class<?> retType = method.getReturnType();
		if((retType == void.class || retType == Void.class) && behavior.hasReturnValue() && !behavior.hasThrowable())
			return false;
		if(retType != void.class && retType != Void.class && !behavior.hasThrowable() && !behavior.hasReturnValue())
			return false;
		return true;
	}
}
