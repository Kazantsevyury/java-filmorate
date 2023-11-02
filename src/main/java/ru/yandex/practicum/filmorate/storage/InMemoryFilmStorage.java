package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.IncorrectValueException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmValidator;

import java.util.*;

@Component
@Slf4j
@RequiredArgsConstructor
public class InMemoryFilmStorage implements FilmStorage {

    private final GeneratorId generatorId = new GeneratorId();
    private final FilmValidator filmValidator;
    private final Map<Integer, Film> films = new HashMap<>();

    @Override
    public Film createFilm(Film film) {
        if (filmValidator.validatorFilm(film)) {
            int id = generatorId.getNextFreeId();
            log.info("Creating a film: " + film.toString());
            film.setId(id);
            films.put(id, film);
        } else {
            throw new ValidationException("Film did not pass validation");
        }
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (filmValidator.validatorFilm(film)) {
            if (films.containsKey(film.getId())) {
                log.info("Updating a film: " + film.toString());
                films.put(film.getId(), film);
            } else {
                throw new ValidationException("ID: " + film.getId() + " does not exist.");
            }
        }
        return film;
    }

    @Override
    public List<Film> retrieveAllFilms() {
        List<Film> filmsList = new ArrayList<>(films.values());
        log.info("Current number of films: {}", films.size());
        return filmsList;
    }

    @Override
    public Film retrieveFilmById(int id) {
        if (films.containsKey(id)) {
            log.info("Retrieving a film with ID: " + id + " - " + films.get(id).toString());
            return films.get(id);
        } else {
            throw new IncorrectValueException(id);
        }
    }

    @Override
    public Map<Integer, Film> retrieveFilmMap() {
        return films;
    }

    @Override
    public void deleteFilmById(int id) {
        if (films.containsKey(id)) {
             films.remove(id);
        } else {
            throw new IncorrectValueException(id);
        }
    }

    @Override
    public void deleteAllFilms() {
        films.clear();
    }

    @Override
    public void checkFilmExistence(int id) {
        if (!films.containsKey(id)) {
            throw new FilmNotFoundException(String.format("Film with id %s does not exist.", id));
        }
    }
}