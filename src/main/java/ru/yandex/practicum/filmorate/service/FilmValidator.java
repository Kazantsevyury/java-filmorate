package ru.yandex.practicum.filmorate.service;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

@Data
@Component
public class FilmValidator {
    private static final Logger log = LoggerFactory.getLogger(FilmValidator.class);

    private int maxDescriptionLength = 200;
    private LocalDate releaseDate = LocalDate.parse("1895-12-28");

    public boolean validatorFilm(Film film) {
        if (film.getDescription().length() > maxDescriptionLength) {
            log.info("Film description length exceeds the maximum allowed length.");
            return false;
        }
        if (film.getReleaseDate().isBefore(releaseDate)) {
            log.info("Film release date is before the earliest recorded film release date.");
            return false;
        }
        if (film.getDuration() < 0) {
            log.info("Film duration is negative.");
            return false;
        }
        if (film.getName() == null || film.getName().isEmpty()) {
            log.info("Film name is empty or null.");
            return false;
        }
        return true;
    }

    public void validatorParameter(Integer id, Integer filmId) {
        if (id < 0) {
            log.info("Invalid userId parameter: " + id);
            throw new IncorrectParameterException("userId");
        }
        if (filmId < 0) {
            log.info("Invalid friendId parameter: " + filmId);
            throw new IncorrectParameterException("friendId");
        }
    }
}