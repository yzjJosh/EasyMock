package easymock;

/**
 * HandlerHelper is an interface which can be used to retrieve MockObjectInvocationHandler. Every mock object
 * has implemented this interface.
 * @author josh
 *
 */
public interface HandlerHelper {
	
	/**
	 * Get the MockObjectInvocationHandler of this mock object
	 * @return the MockObjectInvocationHandler
	 */
	public MockObjectInvocationHandler getHandler();
	
}
