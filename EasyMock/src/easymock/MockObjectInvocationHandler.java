package easymock;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MockObjectInvocationHandler implements InvocationHandler{

	Map <String, Map<Object[], Object>> map = new HashMap<>();
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		if(method.equals(HandlerHelper.class.getMethod("getHandler", new Class<?>[0])))
			return this;
		String mName = method.getName();
		
		if (map.containsKey(mName)) {
			//A dictionary connects the args and the return val;
			Map dict = map.get(mName);
			//A tricky way to check if the arguments are a match.
			Set <Object[]> keys = dict.keySet();
			for (Object[] key : keys) {
				if (key.length != args.length)
					continue;
				boolean isMatch = true;
				for (int i = 0; i < args.length; i++)
					if (!key[i].equals(args[i])) {
						isMatch = false;
						break;
					}
				if (isMatch) return dict.get(key);
			}
				
		}
		return null;
	}
	
	/**
	 * Add the new correlation between method and return val to the map.
	 * @param m: target method; args: parameters of the method; ret: return value.
	 * @return if the operation is successful.
	 */
	public boolean add(String m, Object[] args, Object ret) {
		if (map.containsKey(m)) {
			Map dict = map.get(m);
			if (dict.containsKey(args)) 
				return false;
			else
				dict.put(args, ret);
		} else {
			Map<Object[], Object> dict = new HashMap<>();
			dict.put(args, ret);
			map.put(m, dict);
		}
		return true;
	}

}
