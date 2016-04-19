package execution;

/**
 * Behavior defines the behavior of a method
 *
 */
public class Behavior {
	
	private static class RetValue{
		public final Object value;
		
		public RetValue(Object value){
			this.value = value;
		}
	}
	
	private static class PrintString{
		public final String str;
		
		public PrintString(String str){
			this.str = str;
		}
	}
	
	private PrintString printString;
	private Throwable t;
	private RetValue ret;
	
	/**
	 * Run this behavior
	 * @return the return value, null if no return value is defined
	 * @throws Throwable throw the throwable object if it is defined
	 */
	public Object behave() throws Throwable{
		if(hasPrintString())
			System.out.println(printString.str);
		if(hasThrowable())
			throw t;
		return hasReturnValue()? ret.value: null;
	}
	
	/**
	 * Set the print behavior
	 * @param printString the string to be printed
	 */
	public void setPrint(String printString){
		this.printString = new PrintString(printString);
	}
	
	/**
	 * Set the throw behavior
	 * @param t the throwable object to be thrown
	 */
	public void setThrowable(Throwable t){
		this.t = t;
	}
	
	/**
	 * Set the return behavior
	 * @param ret the value to return
	 */
	public void setReturn(Object ret){
		this.ret = new RetValue(ret);
	}
	
	/**
	 * Check if this behavior has a return value
	 * @return if this behavior will return a value
	 */
	public boolean hasReturnValue(){
		return ret != null;
	}
	
	/**
	 * Check if this behavior has a throwable object
	 * @return if this behavior will throw an object
	 */
	public boolean hasThrowable(){
		return t != null;
	}
	
	/**
	 * Check if this behavior has a string to print
	 * @return if it has a string to print or not
	 */
	public boolean hasPrintString(){
		return printString != null;
	}

}
