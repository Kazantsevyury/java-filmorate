package model;

import lombok.Data;

import java.time.LocalDate;
@Data
public class Film {
    private final long id;
    private final String name;
    private final LocalDate releaseDate;
    private final long duration;
}
