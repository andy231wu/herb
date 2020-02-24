package au.com.new1step.herbalist.exception;

public final class EntityNotHaveIdException extends Exception {

  //  private static final long serialVersionUID = 5861310537366287163L;

    public EntityNotHaveIdException() {
        super();
    }

    public EntityNotHaveIdException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public EntityNotHaveIdException(final String message) {
        super(message);
    }

    public EntityNotHaveIdException(final Throwable cause) {
        super(cause);
    }

}
