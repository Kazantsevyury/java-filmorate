package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.InvalidInputException;
import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.IdGenerator;

import java.time.LocalDate;
import java.time.Month;
import java.util.Collection;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;
    private final LocalDate birthOfCinema = LocalDate.of(1895, Month.DECEMBER, 28);

    public Film saveFilm(Film film) {
        Long id = film.getId();
        if (conditionsCheck(film)) {
            if (id == null) {
                id = IdGenerator.generateSimpleFilmId();
                film.setId(id);
            }
            if (existenceOfTheFilmIdInStorage(id)) {
                throw new ObjectAlreadyExistException("Failed ID, film is already exist");
            } else {
                return filmStorage.save(film);
            }

        } else {
            throw new ValidationException("Name, description, releaseDate, or duration is failed");
        }
    }

    public Film getFilmById(long id) {
        return filmStorage.getById(id);
    }

    public void deleteFilmById(long id) {
        filmStorage.deleteById(id);
    }

    public boolean existenceOfTheFilmIdInStorage(Long id) {
        return filmStorage.existenceOfTheFilmIdInStorage(id);
    }

    public Collection<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public void idCheck(Film film) {
        if (film.getId() == null) {
            film.setId(IdGenerator.generateSimpleFilmId());
        }
    }

    public boolean conditionsCheck(Film film) {
        boolean isNameNotBlank = !film.getName().isBlank();
        boolean isDescriptionValid = film.getDescription().length() <= 200;
        boolean isReleaseDateValid = film.getReleaseDate().isAfter(birthOfCinema);
        boolean isDurationValid = film.getDuration() > 0;

        if (!isNameNotBlank) {
            log.error("Film name is blank or null");
        }

        if (!isDescriptionValid) {
            log.error("Film description is too long (more than 200 characters)");
        }

        if (!isReleaseDateValid) {
            log.error("Film release date is not after the birth of cinema");
        }

        if (!isDurationValid) {
            log.error("Film duration is not greater than 0");
        }

        return isNameNotBlank && isDescriptionValid && isReleaseDateValid && isDurationValid;
    }

    public void addLikeToFilm(long filmId) {
        Film film = filmStorage.getById(filmId);

        if (film != null) {
            film.setLikes(film.getLikes() + 1);
            filmStorage.updateById(filmId, film);
        } else {
            throw new InvalidInputException("Film not found");
        }
    }

    public void removeLikeFromFilm(Long filmId) {
        if (!(existenceOfTheFilmIdInStorage(filmId))) {
            throw new InvalidInputException("Film not found");
        } else {
            Film film = filmStorage.getById(filmId);
            int likes = film.getLikes();
            if (likes <= 0) {
                throw new InvalidInputException("The film has not been given a single like yet.");
            } else {
                film.setLikes(likes - 1);
                filmStorage.deleteById(filmId);
                filmStorage.save(film);
            }
        }
    }

    public Collection<Film> getPopularFilms(int count) {
        return filmStorage.getPopularFilms(count);
    }
}