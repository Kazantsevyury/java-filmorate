package ru.yandex.practicum.filmorate.exception;

import lombok.Getter;

@Getter
public class IncorrectValueException extends RuntimeException {

    private final int value;

    public IncorrectValueException(int value) {
        this.value = value;
    }
}