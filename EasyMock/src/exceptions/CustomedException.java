package exceptions;

public class CustomedException extends Exception {
	public CustomedException(String message) {
		super(message);
	}
	public CustomedException(String message, Throwable throwable) {
        super(message, throwable);
    }
	public CustomedException(){}
}
