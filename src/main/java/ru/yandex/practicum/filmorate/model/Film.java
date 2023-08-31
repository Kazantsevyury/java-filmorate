package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
public class Film {
    private static Long idCounter = 0L;

    private final Long id;
    @NotBlank
    private final String name;
    @Size(max = 200)
    private final String description;
    @NotEmpty
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private final LocalDate releaseDate;
    @Positive
    private final long duration;

    public Film(String name, String description, LocalDate releaseDate, long duration) {
        this.id = ++idCounter;
        this.name = name;
        this.description = description;

        LocalDate minDate = LocalDate.of(1895, 12, 28);
        if (releaseDate.isBefore(minDate)) {
            throw new IllegalArgumentException("Release date cannot be earlier than 28-12-1895");
        }

        this.releaseDate = releaseDate;
        this.duration = duration;
    }
}
