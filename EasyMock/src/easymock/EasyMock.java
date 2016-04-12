package easymock;

import java.lang.reflect.Proxy;

public class EasyMock {
	
	/**
	 * Create a mock object of a interface.
	 * @param clazz the interface which needs mocking
	 * @return the mock object itself
	 */
	public static Object createMock(Class<?> clazz){
		return Proxy.newProxyInstance(clazz.getClassLoader(),
                new Class[] { clazz, HandlerHelper.class},
                new MockObjectInvocationHandler());
	}
	
	
	
	//Following is an example
	interface Foo{
		
	}
	
	public static void main(String[] args){
		Foo f = (Foo) createMock(Foo.class);
		MockObjectInvocationHandler handler = ((HandlerHelper)f).getHandler();
		System.out.println(handler);
	}
	
}
