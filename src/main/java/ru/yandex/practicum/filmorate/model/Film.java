package ru.yandex.practicum.filmorate.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NotNull
public class Film {
    private int id;
    @NotBlank
    private final String name;
    @NotBlank
    private final String description;
    private final LocalDate releaseDate;
    private final int duration;
    private final int rate;
    @JsonIgnore
    private final Set<Integer> likes = new HashSet<>();
    private final Mpa mpa;
    private Set<Genre> genres;

    @Override
    public String toString() {
        return "Film{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", releaseDate=" + releaseDate +
                ", duration=" + duration +
                ", rate=" + rate +
                ", likes=" + likes +
                ", mpa=" + mpa +
                ", genres=" + genres +
                '}';
    }

}
