package exceptions;

/**
 * An exception thrown in Person flows
 * */
public class InvalidPersonException extends Exception{
    public InvalidPersonException(String message) {
        super(message);
    }
}
