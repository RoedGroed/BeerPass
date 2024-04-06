package Util;

public class ApplicationException extends Exception{
    // Blank Exception handling
    public ApplicationException() {}

    // Exception with a custom message
    public ApplicationException(String msg) { super(msg); }

    // Exception with a custom message and cause(from the superclass Exception)
    public ApplicationException(String msg, Exception cause) { super(msg, cause); }

    // Exception that is just a throwable object.
    public ApplicationException(Throwable cause) { super(cause); }
}
