package EasyNety.authservice.exception;

import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

public class AppException extends Exception{
    private final HttpStatus httpStatus;

    public AppException(String message, HttpStatus status) {
        super(message);
        this.httpStatus = status;
    }

    public AppException(String message) {
        super(message);
        this.httpStatus = INTERNAL_SERVER_ERROR;
    }

    public AppException(Throwable cause) {
        super(cause);
        this.httpStatus = INTERNAL_SERVER_ERROR;
    }

    public AppException(Throwable cause, HttpStatus status) {
        super(cause);
        this.httpStatus = status;
    }

    public AppException(String message, Throwable cause) {
        super(message, cause);
        this.httpStatus = INTERNAL_SERVER_ERROR;
    }

    public AppException(String message, Throwable cause, HttpStatus status) {
        super(message, cause);
        this.httpStatus = status;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
