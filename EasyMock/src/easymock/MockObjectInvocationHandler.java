package easymock;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class MockObjectInvocationHandler implements InvocationHandler{

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		if(method.equals(HandlerHelper.class.getMethod("getHandler", new Class<?>[0])))
			return this;
		return null;
	}
}
