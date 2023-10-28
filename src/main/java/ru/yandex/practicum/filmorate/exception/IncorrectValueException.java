package ru.yandex.practicum.filmorate.exception;

import lombok.Getter;

@Getter
public class IncorrectValueException extends RuntimeException {

    private final Long value;

    public IncorrectValueException(Long value) {
        this.value = value;
    }
}