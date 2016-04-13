package exceptions;

public class IllegalTypeException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public IllegalTypeException(String message) {
		super(message);
	}
	public IllegalTypeException(String message, Throwable throwable) {
        super(message, throwable);
    }
	public IllegalTypeException() {}
}


