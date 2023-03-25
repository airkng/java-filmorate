package ru.yandex.practicum.filmorate.exceptions;

public class ObjectAlreadyExistException extends RuntimeException{
    public ObjectAlreadyExistException() {
        super();
    }

    public ObjectAlreadyExistException(String message) {
        super(message);
    }
}
