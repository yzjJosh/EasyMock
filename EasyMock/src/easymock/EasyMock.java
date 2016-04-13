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
	 * Get MockControl for last invoked method
	 * @return the MockControl object
	 */
	private static MockControl controlLastCalledMethod(){
		LastInvocation lastInvocation = LastInvocation.getLastInvocation();
		if(lastInvocation == null) return null;
		return new MockControl(lastInvocation.getMockObject(), lastInvocation.getMethod(), lastInvocation.getArgument());
	}
	
	/**
	 * Specify the expected behavior for the last called method. This method works only for method which has a return value.
	 *	@return the controller
	 * @throws IllegalStateException if no method can be controlled
	 */
	public static<T> MockControl.ControlReturn expect(T value) {
		MockControl control = controlLastCalledMethod();
		if(control == null)
			throw new IllegalStateException();
		return control.controlReturn();
	}
	
	/**
	 * Specify the expected behavior for the last called method. This method works only for method which has no return value.
	 * @return the controller
	 * @throws IllegalStateException if no method can be controlled
	 */
	public static MockControl.ControlVoid expectLastCall(){
		MockControl control = controlLastCalledMethod();
		if(control == null)
			throw new IllegalStateException();
		return control.controlVoid();
	}
	
	
	
	//Following is an example
	interface Foo{
		int doit(String s, Integer i);
		void foo(String s);
	}
	
	public static void main(String[] args){
		Foo f = (Foo) createMock(Foo.class);
		
		expect(f.doit("sss", 4)).addReturn(7);
		expect(f.doit(null, 0)).addReturn(0);
		System.out.println(f.doit("sss", 4));
		System.out.println(f.doit(null, 0));
		
		f.foo("fooooo");
		expectLastCall().addPrint("i am foooo!");
		f.foo("fooooo");
	}
	
}