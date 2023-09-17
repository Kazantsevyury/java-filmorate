package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@Accessors(chain = true)
public class Film {
    private Long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    private int likes;

}