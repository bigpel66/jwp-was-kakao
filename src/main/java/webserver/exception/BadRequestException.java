package webserver.exception;

public final class BadRequestException extends RuntimeException {
    public BadRequestException() {
    }

    public BadRequestException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
