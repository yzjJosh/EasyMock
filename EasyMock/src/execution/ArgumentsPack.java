package execution;

/**
 * Arguments pack is a wrapper which stores the arguments for a method invocation.
 *
 */
public class ArgumentsPack {
	
	private final Object[] arguments;
	private final int hashCode;
	
	/**
	 * Pack arguments
	 * @param arguments the arguments
	 */
	public ArgumentsPack(Object[] arguments){
		if(arguments == null)
			throw new NullPointerException();
		this.arguments = arguments;
		int tempHashCode = 0;
		for(Object arg: arguments)
			tempHashCode += hash(arg);
		hashCode = tempHashCode;
	}
	
	/**
	 * Get the arguments packed in this pack;
	 * @return the packed arguments
	 */
	public Object[] getArguments(){
		return arguments;
	}
	
	private int hash(Object obj){
		return obj == null? 0: obj.hashCode();
	}
	
	private boolean equals(Object a, Object b){
		if(a == null) return b == null;
		return a.equals(b);
	}
	
	@Override
	public int hashCode(){
		return hashCode;
	}
	
	@Override
	public boolean equals(Object that){
		if(that == null) return false;
		if(that == this) return true;
		if(that.getClass() != ArgumentsPack.class) return false;
		ArgumentsPack args = (ArgumentsPack) that;
		if(args.getArguments().length != getArguments().length) return false;
		for(int i=0; i<arguments.length; i++)
			if(!equals(arguments[i], args.arguments[i]))
				return false;
		return true;
	}
	

}
