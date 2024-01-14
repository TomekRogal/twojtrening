package pl.coderslab.twojtrening.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.METHOD_NOT_ALLOWED)
public class AccessUserException extends RuntimeException{
    public AccessUserException() {
    }

    public AccessUserException(String message) {
        super(message);
    }
}
