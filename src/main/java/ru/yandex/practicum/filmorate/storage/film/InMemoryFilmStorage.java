package ru.yandex.practicum.filmorate.storage.film;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.IncorrectValueException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.enums.Genres;
import ru.yandex.practicum.filmorate.model.enums.Rating;
import ru.yandex.practicum.filmorate.service.FilmValidator;
import ru.yandex.practicum.filmorate.service.GeneratorId;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
@Slf4j
@Builder
public class InMemoryFilmStorage implements FilmStorage {

    private final GeneratorId generatorId = new GeneratorId();
    private final FilmValidator filmValidator;
    private final Map<Integer, Film> films = new HashMap<>();

    @Override
    public Film create(Film film) {
        if (filmValidator.validateFilm(film)) {
            int id = generatorId.getNextFreeId();
            log.info("Creating a new film: {}", film.toString());
            film.setId(id);
            films.put(id, film);
        } else {
            throw new ValidationException("Film did not pass validation");
        }
        return film;
    }

    @Override
    public Film update(Film film) {
        if (filmValidator.validateFilm(film)) {
            if (films.containsKey(film.getId())) {
                log.info("Updating film: {}", film.toString());
                films.put(film.getId(), film);
            } else {
                throw new ValidationException("Film with id: " + film.getId() + " does not exist.");
            }
        }
        return film;
    }

    @Override
    public List<Film> getFilms() {
        List<Film> filmsList = new ArrayList<>(films.values());
        log.info("Current number of films: {}", films.size());
        return filmsList;
    }

    @Override
    public Film getFilm(int id) {
        if (films.containsKey(id)) {
            log.info("Fetching film with id {}: {}", id, films.get(id).toString());
            return films.get(id);
        } else {
            throw new IncorrectValueException(id);
        }
    }

    @Override
    public List<Mpa> getAllMpa() {
        List<Mpa> mpaList = new ArrayList<>();
        List<Rating> ratings = Arrays.asList(Rating.values());
        for (int i = 0; i < ratings.size(); i++) {
            mpaList.add(new Mpa(i + 1, ratings.get(i).toString()));
        }
        return mpaList;
    }

    @Override
    public Mpa getMpa(int id) {
        List<Rating> ratings = Arrays.asList(Rating.values());
        return new Mpa(id, ratings.get(id - 1).toString());
    }

    @Override
    public List<Genre> getAllGenres() {
        List<Genre> genresList = new ArrayList<>();
        List<Genres> genresEnumList = Arrays.asList(Genres.values());
        for (int i = 0; i < genresEnumList.size(); i++) {
            genresList.add(new Genre(i + 1, genresEnumList.get(i).toString()));
        }
        return genresList;
    }

    @Override
    public Genre getGenre(int id) {
        List<Genres> genresEnumList = Arrays.asList(Genres.values());
        return new Genre(id, genresEnumList.get(id).toString());
    }

    @Override
    public void checkFilmExistence(int id) {
        if (!films.containsKey(id)) {
            throw new FilmNotFoundException(String.format("Film with id %s does not exist.", id));
        }
    }
}
