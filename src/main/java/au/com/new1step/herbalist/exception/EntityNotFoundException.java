package au.com.new1step.herbalist.exception;

public final class EntityNotFoundException extends Exception {

   // private static final long serialVersionUID = 5861310537366287163L;

    public EntityNotFoundException() {
        super();
    }

    public EntityNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public EntityNotFoundException(final String message) {
        super(message);
    }

    public EntityNotFoundException(final Throwable cause) {
        super(cause);
    }

}
