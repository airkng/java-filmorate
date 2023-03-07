package ru.yandex.practicum.filmorate.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR, reason = "There film variables incorrect")
public class ValidateException extends RuntimeException {
    public ValidateException(String msg) {
        super(msg);
    }

    public ValidateException() {
        super();
    }
}
