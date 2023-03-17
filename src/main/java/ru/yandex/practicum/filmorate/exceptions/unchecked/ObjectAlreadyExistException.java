package ru.yandex.practicum.filmorate.exceptions.unchecked;

public class ObjectAlreadyExistException extends RuntimeException{
    public ObjectAlreadyExistException() {
        super();
    }

    public ObjectAlreadyExistException(String message) {
        super(message);
    }
}
