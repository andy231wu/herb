package au.com.new1step.herbalist.exception;

public final class EntityHasIdException extends Exception {

    public EntityHasIdException() {
        super();
    }

    public EntityHasIdException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public EntityHasIdException(final String message) {
        super(message);
    }

    public EntityHasIdException(final Throwable cause) {
        super(cause);
    }

}
