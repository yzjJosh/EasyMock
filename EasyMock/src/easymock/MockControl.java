package easymock;

import java.lang.reflect.Method;
import java.util.*;

import exceptions.IlegalTypeException;

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
	
	private final ControlReturn returnController;
	private final ControlVoid voidController;
	
	MockControl(Object proxy, Method m, ArgumentsPack args) {
		if(proxy == null || m == null || args == null)
			throw new NullPointerException();
		this.handler = ((HandlerHelper)proxy).getHandler();
		this.method = m;
		this.arguments = args;
		this.returnController = new ControlReturn();
		this.voidController = new ControlVoid();
	}
	
	public ControlReturn controlReturn(){
		return returnController;
	}
	
	public ControlVoid controlVoid(){
		return voidController;
	}
	
	class ControlReturn{
		
		Class<?> returnType(){
			return method.getReturnType();
		}
		
		/**
		 * Specify the return value for controlled method
		 * @param val the return value
		 * @return this controller
		 * @throws IllegalTypeException if the specified value has a wrong type
		 */
		public ControlReturn addReturn(Object val) {
			Class<?> retType = returnType();
			if(retType.isPrimitive()){
				if(val == null)
					throw new IlegalTypeException("Return type is wrong!");
				if(!PRIMITIVES_TO_WRAPPERS.get(retType).isInstance(val))
					throw new IlegalTypeException("Return type is wrong!");
			} else if(val != null && !retType.isInstance(val))
				throw new IlegalTypeException("Return type is wrong!");
			handler.add(method, arguments, val);
			return this;
		}
	}
	
	class ControlVoid{
		
		/**
		 * Specify the print string for controlled method
		 * @param msg the string to be printed
		 * @return this controller
		 */
		public ControlVoid addPrint(String msg) {
			handler.add(method, arguments, msg);
			return this;
		}
	}

}
