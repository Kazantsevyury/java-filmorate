package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class  Film {
    private final Long id;
    private final String name;
    private final String releaseDate;
    private final long duration;
}

