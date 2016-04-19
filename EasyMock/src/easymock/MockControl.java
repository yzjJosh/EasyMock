package easymock;

import java.lang.reflect.Method;
import java.util.*;

import easymock.MockObjectInvocationHandler.State;
import exceptions.IllegalTypeException;
import execution.ArgumentsPack;
import execution.Behavior;

public class MockControl {
	
	private static final Map<Class<?>, Class<?>> PRIMITIVES_TO_WRAPPERS = new HashMap<>();
	
	static{
		PRIMITIVES_TO_WRAPPERS.put(boolean.class, Boolean.class);
		PRIMITIVES_TO_WRAPPERS.put(byte.class, Byte.class);
		PRIMITIVES_TO_WRAPPERS.put(char.class, Character.class);
		PRIMITIVES_TO_WRAPPERS.put(double.class, Double.class);
		PRIMITIVES_TO_WRAPPERS.put(float.class, Float.class);
		PRIMITIVES_TO_WRAPPERS.put(int.class, Integer.class);
		PRIMITIVES_TO_WRAPPERS.put(long.class, Long.class);
		PRIMITIVES_TO_WRAPPERS.put(short.class, Short.class);
		PRIMITIVES_TO_WRAPPERS.put(void.class, Void.class);
	}
	
	private final MockObjectInvocationHandler handler;
	private final Method method;
	private final ArgumentsPack arguments;
	private final Behavior behavior;
	
	
	MockControl(Object proxy, Method m, ArgumentsPack args) {
		if(proxy == null || m == null || args == null)
			throw new NullPointerException();
		this.handler = ((HandlerHelper)proxy).getHandler();
		this.method = m;
		this.arguments = args;
		this.behavior = new Behavior();
		handler.addInvocation(method, arguments, behavior);
	}
	
	/**
	 * Get the return controller for this controlled method
	 * @return the return controller
	 * @throws IllegalTypeException if the return type of the controlled method is void
	 * @throws IllegalStateException if the mock object is not in record state
	 */
	public ControlReturn controlReturn(){
		if(handler.getState() != State.RECORD)
			throw new IllegalStateException("Not in record state!");
		return new ControlReturn();
	}
	
	/**
	 * Get the void controller for this controlled method
	 * @return the return controller
	 * @throws IllegalTypeException if the return type of the controlled method is not void
	 * @throws IllegalStateException if the mock object is not in record state
	 */
	public ControlVoid controlVoid(){
		if(handler.getState() != State.RECORD)
			throw new IllegalStateException("Not in record state!");
		return new ControlVoid();
	}
	
	/**
	 * Base class of all controllers
	 * @param <T> the actual subclass type
	 */
	@SuppressWarnings("unchecked")
	private abstract class Control <T> {	
		
		/**
		 * Specify the throwable to be thrown for controlled method
		 * @param t the throwable object to be thrown
		 * @return this controller
		 * @throws IllegalTypeException If the throwable added is a exception that is not declared by the method
		 * @throws NullPointerException If the exception added is a null pointer
		 * @throws IllegalStateException if the mock object is not in record state
		 */
		public T setThrowable(Throwable t) {
			if(handler.getState() != State.RECORD)
				throw new IllegalStateException("Not in record state!");
			if(t == null)
				throw new NullPointerException("Cannot throw a null pointer!");
			if(!(t instanceof RuntimeException) && !(t instanceof Error)){
				Class<?>[] exceptions = method.getExceptionTypes();
				boolean match = false;
				for(Class<?> eType: exceptions)
					if(eType.isInstance(t)){
						match = true;
						break;
					}
				if(!match)
					throw new IllegalTypeException("Cannot add an undecleared exception to the method!");
			}
			behavior.setThrowable(t);
			return (T) this;
		}
		
		/**
		 * Specify the print string for controlled method
		 * @param msg the string to be printed
		 * @return this controller
		 * @throws IllegalStateException if the mock object is not in record state
		 */
		public T setPrint(String msg) {
			if(handler.getState() != State.RECORD)
				throw new IllegalStateException("Not in record state!");
			behavior.setPrint(msg);
			return (T)this;
		}
	}
	
	
	/**
	 * Controller which controls the behavior of a method with a return value
	 *
	 */
	 class ControlReturn extends Control <ControlReturn> {
		 
		 public ControlReturn(){
				if(method.getReturnType() == void.class || method.getReturnType() == Void.class)
					throw new IllegalTypeException("Cannot get return controller for a void returned method");
		 }
		
		Class<?> returnType(){
			return method.getReturnType();
		}
		
		/**
		 * Specify the return value for controlled method
		 * @param val the return value
		 * @return this controller
		 * @throws IllegalTypeException if the specified value has a wrong type
		 * @throws IllegalStateException if the mock object is not in record state
		 */
		public ControlReturn setReturn(Object val) {
			if(handler.getState() != State.RECORD)
				throw new IllegalStateException("Not in record state!");
			Class<?> retType = returnType();
			if(retType.isPrimitive()){
				if(val == null)
					throw new IllegalTypeException("Return type is wrong!");
				if(!PRIMITIVES_TO_WRAPPERS.get(retType).isInstance(val))
					throw new IllegalTypeException("Return type is wrong!");
			} else if(val != null && !retType.isInstance(val))
				throw new IllegalTypeException("Return type is wrong!");
			behavior.setReturn(val);
			return this;
		}
	}
	
	
	/**
	 * Controller which controls the behavior of a method without a return value
	 *
	 */
	class ControlVoid extends Control <ControlVoid> {
		public ControlVoid(){
			if(method.getReturnType() != void.class && method.getReturnType() != Void.class)
				throw new IllegalTypeException("Cannot get void controller for a non-void returned method");
		}
	}
	

}
