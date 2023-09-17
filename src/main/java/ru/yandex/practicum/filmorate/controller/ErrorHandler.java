package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.InvalidInputException;
import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.ErrorResponse;


@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<ErrorResponse> handleInvalidInputException(InvalidInputException e) {
        log.error("Invalid Input Exception: " + e.getMessage());
        ErrorResponse response = new ErrorResponse()
                .setCode(400)
                .setMessage("Invalid input: " + e.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(ObjectAlreadyExistException.class)
    public ResponseEntity<ErrorResponse> handleObjectAlreadyExistException(ObjectAlreadyExistException e) {
        log.error("Object Already Exists Exception: " + e.getMessage());
        ErrorResponse response = new ErrorResponse()
                .setCode(409)
                .setMessage("Object already exists: " + e.getMessage());
        return ResponseEntity.status(409).body(response);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException e) {
        log.error("User Not Found Exception: " + e.getMessage());
        ErrorResponse response = new ErrorResponse()
                .setCode(404)
                .setMessage("User not found: " + e.getMessage());
        return ResponseEntity.status(404).body(response);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(ValidationException e) {
        log.error("Validation Exception: " + e.getMessage());
        ErrorResponse response = new ErrorResponse()
                .setCode(400)
                .setMessage("Validation error: " + e.getMessage());
        return ResponseEntity.badRequest().body(response);
    }
}