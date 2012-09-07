package monitoring;

public class CheckFailedException extends Exception {

    private static final long serialVersionUID = 7564113775829788135L;

    public CheckFailedException() {
        super();
    }

    public CheckFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public CheckFailedException(String message) {
        super(message);
    }

    public CheckFailedException(Throwable cause) {
        super(cause);
    }

}
