package easymock;

import java.lang.reflect.Proxy;

import exceptions.CustomedException;
import exceptions.IllegalTypeException;

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
	 * @throws IllegalStateException if the mock object of last called method is not in record state
	 */
	private static MockControl controlLastCalledMethod(){
		LastInvocation lastInvocation = LastInvocation.getLastInvocation();
		if(lastInvocation == null) return null;
		return new MockControl(lastInvocation.getMockObject(), lastInvocation.getMethod(), lastInvocation.getArgument());
	}
	
	/**
	 * Specify the expected behavior for the last called method. This method works only for method which has a return value.
	 *	@return the controller
	 * @throws IllegalStateException if no method can be controlled or the mock object of last called method is not in record state
	 * @throws IllegalTypeException if the last called method has void return type
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
	 * @throws IllegalStateException if no method can be controlled  or the mock object of last called method is not in record state
	 * @throws IllegalTypeException if the last called method has a non-void return type
	 */
	public static MockControl.ControlVoid expectLastCall(){
		MockControl control = controlLastCalledMethod();
		if(control == null)
			throw new IllegalStateException();
		return control.controlVoid();
	}
	
	/**
	 * Change the state of the mocking object to REPLAY.
	 * @param o the mock object.
	 * @throws IllegalTypeException if the object is not a mock object
	 */
	public static void replay(Object o) {
		if(!(o instanceof HandlerHelper))
			throw new IllegalTypeException("Not a mock object!");
		MockObjectInvocationHandler handler = ((HandlerHelper)o).getHandler();
		handler.replay();
	}
	
	/**
	 * Change the state of the mocking object to RECORD.
	 * @param o the mock object.
	 * @throws IllegalTypeException if the object is not a mock object
	 */
	public static void record(Object o){
		if(!(o instanceof HandlerHelper))
			throw new IllegalTypeException("Not a mock object!");
		MockObjectInvocationHandler handler = ((HandlerHelper)o).getHandler();
		handler.record();
	}
	
	/**
	 * Create the branch
	 * @param o the mock object.
	 * @throws IllegalTypeException if the object is not a mock object
	 */
	public static void startBranch(Object o) {
		if(!(o instanceof HandlerHelper))
			throw new IllegalTypeException("Not a mock object!");
		MockObjectInvocationHandler handler = ((HandlerHelper)o).getHandler();
		handler.startBranch();
	}
	
	/**
	 * Switch the branch
	 * @param o the mock object.
	 * @throws IllegalTypeException if the object is not a mock object
	 */
	public static void switchBranch(Object o) {
		if(!(o instanceof HandlerHelper))
			throw new IllegalTypeException("Not a mock object!");
		MockObjectInvocationHandler handler = ((HandlerHelper)o).getHandler();
		handler.switchBranch();
	}
	
	/**
	 * End the branch
	 * @param o the mock object.
	 * @throws IllegalTypeException if the object is not a mock object
	 */
	public static void endBranch(Object o) {
		if(!(o instanceof HandlerHelper))
			throw new IllegalTypeException("Not a mock object!");
		MockObjectInvocationHandler handler = ((HandlerHelper)o).getHandler();
		handler.endBranch();
	}
	
	
	
	
	
	//Following is an example
	interface Foo{
		int doit(String s, Integer i);
		void foo(String s);
		int doit2(String s) throws CustomedException;
	}
	
	public static void main(String[] args){
		Foo f = (Foo) createMock(Foo.class);
		
		record(f);
		
		f.foo(null);
		expectLastCall().setPrint("node 1");
		startBranch(f);
			expect(f.doit("  ", 0)).setPrint("node 2").setReturn(0);
		switchBranch(f);
			expect(f.doit("  ", 0)).setPrint("node 3").setReturn(0);
		endBranch(f);
		f.foo(null);
		expectLastCall().setPrint("node 4");
		
		replay(f);
		f.foo(null);
		f.doit("", 0);
		f.foo(null);
		
		replay(f);
		f.foo(null);
		f.doit("  ", 0);
		f.foo(null);
		
	}
	
}
