package ru.yandex.practicum.filmorate.exceptions;

public class ValidateException extends RuntimeException {
    public ValidateException(String msg) {
        super(msg);
    }

    public ValidateException() {
        super();
    }
}
