package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import java.time.LocalDate;

import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class Film {
    private Integer id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
}