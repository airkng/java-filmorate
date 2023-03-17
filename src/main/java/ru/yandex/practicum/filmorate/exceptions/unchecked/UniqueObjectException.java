package ru.yandex.practicum.filmorate.exceptions.unchecked;

public class UniqueObjectException extends RuntimeException {
    public UniqueObjectException() {
        super();
    }

    public UniqueObjectException(String message) {
        super(message);
    }
}
