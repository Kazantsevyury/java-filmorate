package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Component;

@Component
public class GeneratorId {
    private int nextFreeId = 1;

    public int getNextFreeId() {
        return nextFreeId++;
    }
}
