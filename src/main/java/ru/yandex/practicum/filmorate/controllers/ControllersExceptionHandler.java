package ru.yandex.practicum.filmorate.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exceptions.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UniqueObjectException;
import ru.yandex.practicum.filmorate.exceptions.ValidateException;

import java.util.Map;

@RestControllerAdvice
public class ControllersExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handleNotFound(final ObjectNotFoundException e) {
        return new ResponseEntity<>(Map.of("Exception: ", e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handleBadRequest(final ObjectAlreadyExistException e) {
        return new ResponseEntity<>(Map.of("Exception: ", e.getMessage()), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handleUniqueObjectException (final UniqueObjectException e) {
        return new ResponseEntity<>(Map.of("Exception: ", e.getMessage()), HttpStatus.ALREADY_REPORTED);
    }
    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handleValidationException (final ValidateException e) {
        return new ResponseEntity<>(Map.of("Exception: ", e.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
