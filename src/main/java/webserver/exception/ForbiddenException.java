package webserver.exception;

public final class ForbiddenException extends RuntimeException {
    public ForbiddenException() {
    }

    public ForbiddenException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
