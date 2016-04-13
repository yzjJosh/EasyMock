package exceptions;

public class IlegalTypeException extends Exception {
	public IlegalTypeException(String message) {
		super(message);
	}
	public IlegalTypeException(String message, Throwable throwable) {
        super(message, throwable);
    }
	public IlegalTypeException() {}
}


