package exceptions;

public class IlegalTypeException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public IlegalTypeException(String message) {
		super(message);
	}
	public IlegalTypeException(String message, Throwable throwable) {
        super(message, throwable);
    }
	public IlegalTypeException() {}
}


