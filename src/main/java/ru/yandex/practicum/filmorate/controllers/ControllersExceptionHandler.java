package ru.yandex.practicum.filmorate.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exceptions.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;

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
}
