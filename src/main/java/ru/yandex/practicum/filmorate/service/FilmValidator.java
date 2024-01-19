package ru.yandex.practicum.filmorate.service;

import lombok.Data;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

@Data
@Component
public class FilmValidator {
    private static final int MAX_DESCRIPTION_LENGTH = 200;
    private static final LocalDate RELEASE_DATE = LocalDate.parse("1895-12-28");

    public boolean validateFilm(Film film) {
        if (film.getDescription().length() > MAX_DESCRIPTION_LENGTH) {
            return false;
        }
        if (film.getReleaseDate().isBefore(RELEASE_DATE)) {
            return false;
        }
        return film.getDuration() >= 0;
    }

    public void validateParameter(Integer filmId) {
        if (filmId < 0) {
            throw new IncorrectParameterException("friendId");
        }
    }

    public void validateMpaId(Integer id) {
        if (id < 0 || id > 5) {
            throw new IncorrectParameterException("mpaId");
        }
    }

    public void validateGenreId(Integer id) {
        if (id < 0 || id > 6) {
            throw new IncorrectParameterException("genreId");
        }
    }
}