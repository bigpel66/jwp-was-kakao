package webserver.exception;

public final class ConflictException extends RuntimeException {
    public ConflictException() {
    }

    public ConflictException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
