package exceptions;

/**
 * An exception thrown in Task flows
 * */
public class InvalidTaskException extends Exception{

    public InvalidTaskException(String message) {
        super(message);
    }
}
