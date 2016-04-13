package easymock;

import java.lang.reflect.Method;
import java.util.Arrays;

public class MockControl<T> {
	Object mockedObject;
	MockObjectInvocationHandler handler;
	Method method;
	Object[] arguments;
	public MockControl(Object proxy, Method m, Object[] args) {
		mockedObject = proxy;
		handler = ((HandlerHelper)proxy).getHandler();
		method = m;
		arguments = args;
	}
	
	public MockControl addReturn(Object val) {
//		System.out.println(Arrays.asList(arguments));
		handler.add(method.getName(), arguments, val);
		return this;
	}
	
	//currently used for void func
	public MockControl addPrint(String msg) {
		handler.add(method.getName(), arguments, msg);
		return this;
	}

}
