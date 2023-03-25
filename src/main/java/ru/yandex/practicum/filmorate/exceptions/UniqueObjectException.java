package ru.yandex.practicum.filmorate.exceptions;

public class UniqueObjectException extends RuntimeException {
    public UniqueObjectException() {
        super();
    }

    public UniqueObjectException(String message) {
        super(message);
    }
}
