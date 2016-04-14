package easymock;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class MockObjectInvocationHandler implements InvocationHandler{

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
	
	private Map <Method, Map<ArgumentsPack, Object[]>> map = new HashMap<>();
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		if(method.equals(HandlerHelper.class.getMethod("getHandler", new Class<?>[0])))
			return this;
		
		ArgumentsPack argsPack = new ArgumentsPack(args);
		Class<?> retType = method.getReturnType();
		
		//Store this invocation information
		LastInvocation.addInvocation(proxy, method, argsPack);
		
		//If we can find information for this method invocation, we return the predefined value
		if(map.containsKey(method) && map.get(method).containsKey(argsPack)){
			Object[] res = map.get(method).get(argsPack);
			Object ret = res[0];
			Exception exception = (Exception) res[1];
			//Firstly, handle the exception
			if (exception != null) {
				System.out.println(exception.getMessage());
				throw exception;
			}
			if(retType == void.class || retType == Void.class){
				System.out.println(ret);
				return null;
			}else
				return ret; 
		}
			
		//Otherwise, return a default value
		if(retType.isPrimitive())
			return PRIMITIVES_DEFAULT_VALUES.get(retType);
		else
			return null;
	}
	
	/**
	 * Add the new correlation between method and return val to the map.
	 * @param m target method; 
	 * @param args parameters of the method; 
	 * @param ret return value.
	 * @return if the operation is successful.
	 */
	public boolean add(Method m, ArgumentsPack args, Object ret) {
		Map<ArgumentsPack, Object[]> dict = map.get(m);
		if(dict == null){
			dict = new HashMap<>();
			map.put(m, dict);
		}
		Object[] bundles = dict.get(args);
		if (bundles == null) 
			bundles = new Object[3];
		bundles[0] = ret;
		dict.put(args, bundles);
		return true;
	}
	
	/**
	 * Add the new correlation between method and exception to the map.
	 * @param m target method; 
	 * @param args parameters of the method; 
	 * @param ret return value.
	 * @return if the operation is successful.
	 */
	public boolean addException(Method m, ArgumentsPack args, Exception e) {
		if (m.getExceptionTypes() == null) {
			System.out.println("The method doesn't throw an exception!");
			return false;
		}
		Map<ArgumentsPack, Object[]> dict = map.get(m);
		if(dict == null){
			dict = new HashMap<>();
			map.put(m, dict);
		}
		Object[] bundles = dict.get(args);
		if (bundles == null) 
			bundles = new Object[3];
		bundles[1] = e;
		dict.put(args, bundles);
		return true;
	}

}
