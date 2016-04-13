package easymock;

import java.lang.reflect.Proxy;
import java.util.Arrays;

import exceptions.IlegalTypeException;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

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
	
	/**
	 * Specify the expected behavior from a method.
	 * @param proxy: the mocked object, mName: the method name, args: method parameters
	 * @return the MockControl object.
	 */
	public static<T> MockControl expect(Object proxy, String mName, Object[] args) {
		try {
			if (!(proxy instanceof HandlerHelper)) {
				throw new IlegalTypeException("Cannot get the handler!");
			}
			Class[] classes = new Class[args.length];
			for (int i = 0; i < args.length; i++)
				classes[i] = args[i].getClass();
			System.out.println(Arrays.asList(classes));
			Method m = proxy.getClass().getMethod(mName, classes);

			return new MockControl(proxy, m, args);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	
	//Following is an example
	interface Foo{
		int doit(String s, Integer i);
		void foo(String s);
	}
	
	public static void main(String[] args){
		Foo f = (Foo) createMock(Foo.class);
		MockObjectInvocationHandler handler = ((HandlerHelper)f).getHandler();
		System.out.println(handler);
		expect(f, "doit", new Object[]{"sss", 4}).addReturn(7);
		System.out.println(f.doit("sss", 4));
		expect(f, "foo", new Object[]{"fooooo"}).addPrint("i am foooo!");
		f.foo("fooooo");
	}
	
}
